package main;

import static main.MathFunctions.*;
import static main.CollisionFunctions.*;

import datatypes.Face;
import datatypes.Mesh;
import datatypes.Polygon;
import datatypes.Vector;

public class RenderFunctions {
	// projects 3d point to 2d point with regard to camera variables
	public static Vector project(Vector point, Camera cam) {
		// gets focal distance info from field of view and "sensor size" (window size)
		double halfSize = Math.sqrt(cam.viewportW * cam.viewportW + cam.viewportH * cam.viewportH) / 2;
		double focalDistance = halfSize / Math.tan(Math.toRadians(cam.fieldOfView / 2));
		
		// rename variables
		double[] a = point.getDoubles();
		double[] c = cam.position.getDoubles();
		double[] t = cam.angle.getDoubles();
		double[] e = new double[]{0, 0, focalDistance};
		
		// more renaming (saves space)
		double ax = a[0]; double ay = a[1]; double az = a[2];
		double cx = c[0]; double cy = c[1]; double cz = c[2];
		double tx = t[0]; double ty = t[1]; double tz = t[2];
		double ex = e[0]; double ey = e[1]; double ez = e[2];
		
		// more renaming again
		double x = ax - cx; double y = ay - cy; double z = az - cz;
		
		// even more renaming (I promise it improves readablity)
		double cosx = cos(tx); double cosy = cos(ty); double cosz = cos(tz);
		double sinx = sin(tx); double siny = sin(ty); double sinz = sin(tz);
		
		// rotation matrix
		double dx = cosy * (sinz * y + cosz * x) - siny * z;
		double dy = sinx * (cosy * z + siny * (sinz * y + cosz * x)) + cosx * (cosz * y - sinz * x);
		double dz = cosx * (cosy * z + siny * (sinz * y + cosz * x)) - sinx * (cosz * y - sinz * x);
		
		// perspective projection
		double bx = ez / dz * dx + ex;
		double by = ez / dz * dy + ey;
		
		return(new Vector(bx, by, 0));
	}
	
	public static boolean checkFaceVisible(Face face, Camera cam) {
		Vector[] points = face.getPoints();
		
		Vector faceNormal = face.getNormal().scale(-1);
		Vector positionOffset = cam.position.subtract(points[0]);
		
		return(positionOffset.dot(faceNormal) <= 0);
	}
	
	public static Vector[] clipEdgeToView(Vector[] points, Camera cam) {
		// variable renaming
		double ax = points[0].x;
		double ay = points[0].y;
		double az = points[0].z;
		double bx = points[1].x;
		double by = points[1].y;
		double bz = points[1].z;
		
		// more variable renaming
		Vector a = new Vector(ax, ay, az);
		Vector b = new Vector(bx, by, bz);
		
		// if a point is at the same position at the camera, move it towards the other point so it's not in the camera anymore
		if(a.equals(cam.position)) {a = a.plus(new Vector(bx - ax, by - ay, bz - az).normalize());}
		if(b.equals(cam.position)) {b = b.plus(new Vector(ax - bx, ay - by, az - bz).normalize());}
		
		// reset variables
		ax = a.x;
		ay = a.y;
		az = a.z;
		bx = b.x;
		by = b.y;
		bz = b.z;
		
		// get normal vector of image plane
		Vector planeNormal = rotateVector(new Vector(0, 0, 1), cam.angle).normalize();
		
		// get directions of rays pointing from a vertex to the camera
		Vector lineDirectionA = new Vector(cam.position.x - ax, cam.position.y - ay, cam.position.z - az);
		Vector lineDirectionB = new Vector(cam.position.x - bx, cam.position.y - by, cam.position.z - bz);
		
		// gets point of intersections between above rays and image plane
		Vector newA = rayPlaneIntersection(cam.position, planeNormal, a, lineDirectionA);
		Vector newB = rayPlaneIntersection(cam.position, planeNormal, b, lineDirectionB);
		
		// check if edge collides with image plane at all
		// if not, skip projection entirely
		if(newA != null || newB != null) {
			// if point a is behind the camera, clip
			if(newA == null) {
				Vector lineDirection = new Vector(ax - bx, ay - by, az - bz);
				newA = rayPlaneIntersection(cam.position.plus(planeNormal), planeNormal, b, lineDirection);
			}
			else {newA = a;}
			
			// if point b is behind the camera, clip
			if(newB == null) {
				Vector lineDirection = new Vector(bx - ax, by - ay, bz - az);
				newB = rayPlaneIntersection(cam.position.plus(planeNormal), planeNormal, a, lineDirection);
			}
			else {newB = b;}
			
			// project the points
			Vector temp0 = project(newA, cam);
			Vector temp1 = project(newB, cam);
			
			return(new Vector[]{temp0, temp1});
		}
		
		return(null);
	}
	
	public static Vector[] clipFaceToView(Face face, Camera cam) {
		Vector[][] edges = new Vector[][]{{face.a, face.b}, {face.b, face.c}, {face.c, face.a}};
		
		Vector[] pointsToDraw = null;
		
		for(int i = 0; i < 3; i++) {
			Vector[] tempEdge = clipEdgeToView(edges[i], cam);
			
			if(tempEdge != null) {
				pointsToDraw = appendVectorToListIfNotAlreadyThere(pointsToDraw, new Vector(tempEdge[0].x, tempEdge[0].y, 0));
				pointsToDraw = appendVectorToListIfNotAlreadyThere(pointsToDraw, new Vector(tempEdge[1].x, tempEdge[1].y, 0));
			}
		}
		
		return(pointsToDraw);
	}
	
	public static Face[] reorderFaces(Face[] faces, Camera cam) {
		Face[] oldFaceList = faces;
		Face[] newFaceList = null;
		
		int faceIndex = faces.length;
		while(faceIndex > 0) {
			double[] distances = new double[faceIndex];
			
			// TODO: fix order of drawing faces, should be more complicated, raycasting or whatever - currently just simple average distance check
			
			for(int i = 0; i < faceIndex; i++) {distances[i] = oldFaceList[i].getAverageDistanceFromPoint(cam.position);}
			
			int tempIndex = findLargestDoubleInList(distances);
			
			Face[][] tempList = transferFaceBetweenLists(oldFaceList, newFaceList, tempIndex);
			
			oldFaceList = tempList[0];
			newFaceList = tempList[1];
			
			faceIndex--;
		}
		
		/*for(int f = 0; f < newFaceList.length; f++) {
			for(int v = 0; v < 3; v++) {
				Vector origin = newFaceList[f].getPoints()[v];
				Vector direction = cam.position.subtract(origin);
				
				/*int intersectIndex = rayCast(origin, direction, removeFaceFromList(newFaceList, f));
				if(intersectIndex >= f) {intersectIndex++;}
				
				if(intersectIndex != -1 && f > intersectIndex) {
					if(!newFaceList[intersectIndex].a.equals(origin) && !newFaceList[intersectIndex].b.equals(origin) && !newFaceList[intersectIndex].c.equals(origin)) {
						System.out.println(f + " is farther from the camera than " + intersectIndex);
						
						for(int i = 0; i < f - intersectIndex; i++) {newFaceList = swapFacesInList(newFaceList, f - i, f - i - 1);}
					}
				}*/
				
				/*int[] indexes = rayCast2(origin, direction, newFaceList);
				
				if(indexes != null) {
					int[] toRemove = null;
					for(int i = 0; i < indexes.length; i++) {
						if(indexes[i] == f) {toRemove = appendIntToList(toRemove, i);}
						else if(f < indexes[i]) {toRemove = appendIntToList(toRemove, i);}
						else if(newFaceList[indexes[i]].a.equals(origin)) {toRemove = appendIntToList(toRemove, i);}
						else if(newFaceList[indexes[i]].b.equals(origin)) {toRemove = appendIntToList(toRemove, i);}
						else if(newFaceList[indexes[i]].a.equals(origin)) {toRemove = appendIntToList(toRemove, i);}
					}
					
					if(toRemove != null) {for(int i = 0; i < toRemove.length; i++) {indexes = removeIntFromList(indexes, toRemove[i]);}}
					
					if(indexes != null) {
						int smallestNum;
						int smallestIndex = -1;
						
						for(int i = 0; i < indexes.length; i++) {
							if(smallestIndex == -1 || indexes)
						}
					}
					
					for(int i = 0; i < indexes.length; i++) {
						if(f > indexes[i]) {
							if(!newFaceList[indexes[i]].a.equals(origin) && !newFaceList[indexes[i]].b.equals(origin) && !newFaceList[indexes[i]].c.equals(origin)) {
								System.out.println(f + " is farther from the camera than " + indexes[i]);
								
								for(int i2 = 0; i2 < f - indexes[i]; i2++) {newFaceList = swapFacesInList(newFaceList, f - i2, f - i2 - 1);}
								
								
							}
						}
					}
				}
			}
		}*/
		
		return(newFaceList);
	}
}
