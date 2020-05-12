package main;

import java.awt.Color;

import datatypes.Face;
import datatypes.Mesh;
import datatypes.Polygon;
import datatypes.Vector;

import static main.CollisionFunctions.*;
import static main.MathFunctions.appendFaceToList;

public class MathFunctions {
	// rotate vector in 3d space
	public static Vector rotateVector(Vector inputVector, Vector rotationAngles) {
		// renaming variables
		double ix = inputVector.x; double iy = inputVector.y; double iz = inputVector.z;
		double rx = rotationAngles.x; double ry = rotationAngles.y; double rz = rotationAngles.z;
		
		// x rotation matrix
		double ax = ix;
		double ay = iy * cos(rx) + iz * -sin(rx);
		double az = iy * sin(rx) + iz * cos(rx);
		
		// y rotation matrix
		double bx = ax * cos(ry) + az * sin(ry);
		double by = ay;
		double bz = ax * -sin(ry) + az * cos(ry);
		
		// z rotation matrix
		double cx = bx * cos(rz) + by * -sin(rz);
		double cy = bx * sin(rz) + by * cos(rz);
		double cz = bz;
		
		return(new Vector(cx, cy, cz));
	}
	public static Vector rotatePointAroundPoint(Vector point, Vector origin, Vector rotation) {
		if(rotation.x != 0 || rotation.y != 0 || rotation.z != 0) {
			double[] inputVector = point.getDoubles();
			double[] rotationAngles = rotation.getDoubles();
			
			inputVector[0] -= origin.x;
			inputVector[1] -= origin.y;
			inputVector[2] -= origin.z;
			
			// renaming variables
			double ix = inputVector[0]; double iy = inputVector[1]; double iz = inputVector[2];
			double rx = rotationAngles[0]; double ry = rotationAngles[1]; double rz = rotationAngles[2];
			
			// x rotation matrix
			double ax = ix;
			double ay = iy * cos(rx) + iz * -sin(rx);
			double az = iy * sin(rx) + iz * cos(rx);
			
			// y rotation matrix
			double bx = ax * cos(ry) + az * sin(ry);
			double by = ay;
			double bz = ax * -sin(ry) + az * cos(ry);
			
			// z rotation matrix
			double cx = bx * cos(rz) + by * -sin(rz);
			double cy = bx * sin(rz) + by * cos(rz);
			double cz = bz;
			
			return(new Vector(cx + origin.x, cy + origin.y, cz + origin.z));
		}
		else {return(point);}
	}
	public static Vector rotatePointAroundPointOnAxis(Vector point, Vector origin, Vector axis, double angle) {
		if(axis.x != 0 || axis.y != 0 || axis.z != 0) {
			Vector inPoint = new Vector(point.x - origin.x, point.y - origin.y, point.z - origin.z);
			Vector outPoint = new Vector();
			
			Vector u = axis;
			
			outPoint.x = (
				inPoint.x * (cos(angle) + u.x * u.x * (1 - cos(angle))) +
				inPoint.y * (u.x * u.y * (1 - cos(angle)) - u.z * sin(angle)) + 
				inPoint.z * (u.x * u.z * (1 - cos(angle)) + u.y * sin(angle))
			);
			outPoint.y = (
				inPoint.x * (u.y * u.x * (1 - cos(angle)) + u.z * sin(angle)) + 
				inPoint.y * (cos(angle) + u.y * u.y * (1 - cos(angle))) +
				inPoint.z * (u.y * u.z * (1 - cos(angle)) - u.x * sin(angle))
			);
			outPoint.z = (
				inPoint.x * (u.z * u.x * (1 - cos(angle)) - u.y * sin(angle)) + 
				inPoint.y * (u.z * u.y * (1 - cos(angle)) + u.x * sin(angle)) + 
				inPoint.z * (cos(angle) + u.z * u.z * (1 - cos(angle)))
			);
			
			return(new Vector(outPoint.x + origin.x, outPoint.y + origin.y, outPoint.z + origin.z));
		}
		else {return(point);}
	}
	
	// trig functions for degrees
	public static double sin(double theta) {return(Math.sin(Math.toRadians(theta)));}
	public static double cos(double theta) {return(Math.cos(Math.toRadians(theta)));}
	public static double tan(double theta) {return(Math.tan(Math.toRadians(theta)));}
	
	public static Face[] getFacesFromMeshes(Mesh[] meshes) {
		Face[] faces = null;
		
		for(int m = 0; m < meshes.length; m++) {for(int p = 0; p < meshes[m].polys.length; p++) {for(int f = 0; f < meshes[m].polys[p].faces.length; f++) {faces = appendFaceToList(faces, meshes[m].polys[p].faces[f]);}}}
		
		return(faces);
	}
	
	public static double getTriangleArea(Vector p0, Vector p1, Vector p2) {
		Vector ba = p1.subtract(p0);
		Vector ca = p2.subtract(p0);
		
		Vector para = ba.cross(ca);
		
		return(para.length() / 2);
	}
	
	public static double getFocalDistance(Camera cam) {
		// gets focal distance info from field of view and "sensor size" (window size)
		double halfSize = Math.sqrt(cam.viewportW * cam.viewportW + cam.viewportH * cam.viewportH) / 2;
		double focalDistance = halfSize / Math.tan(Math.toRadians(cam.fieldOfView / 2));
		
		return(focalDistance);
	}
	
	public static int findLargestDoubleInList(double[] list) {
		double largest = 0;
		int index = -1;
		
		for(int i = 0; i < list.length; i++) {
			if(index == -1 || list[i] > largest) {
				largest = list[i];
				index = i;
			}
		}
		
		return(index);
	}
	public static int findSmallestDoubleInList(double[] list) {
		double smallest = 0;
		int index = -1;
		
		for(int i = 0; i < list.length; i++) {
			if(index == -1 || list[i] < smallest) {
				smallest = list[i];
				index = i;
			}
		}
		
		return(index);
	}
	
	public static double[][] appendDoubleToListIfNotAlreadyThere(double[][] existingPoints, double[] pointToCheck) {
		if(existingPoints == null) {
			if(pointToCheck != null) {return(new double[][]{pointToCheck});}
			else {return(null);}
		}
		
		if(pointToCheck != null) {
			boolean alreadyThere = false;
			for(int i = 0; i < existingPoints.length; i++) {if(pointToCheck == existingPoints[i]) {alreadyThere = true;}}
			
			if(!alreadyThere) {
				double[][] newList = new double[existingPoints.length + 1][2];
				
				for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
				newList[existingPoints.length] = pointToCheck;
				
				return(newList);
			}
			else {return(existingPoints);}
		}
		else {return(existingPoints);}
	}
	public static Vector[] appendVectorToListIfNotAlreadyThere(Vector[] existingPoints, Vector pointToCheck) {
		if(existingPoints == null) {
			if(pointToCheck != null) {return(new Vector[]{pointToCheck});}
			else {return(null);}
		}
		
		if(pointToCheck != null) {
			boolean alreadyThere = false;
			for(int i = 0; i < existingPoints.length; i++) {if(pointToCheck == existingPoints[i]) {alreadyThere = true;}}
			
			if(!alreadyThere) {
				Vector[] newList = new Vector[existingPoints.length + 1];
				
				for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
				newList[existingPoints.length] = pointToCheck;
				
				return(newList);
			}
			else {return(existingPoints);}
		}
		else {return(existingPoints);}
	}
	
	public static Color[] appendColorToList(Color[] existingPoints, Color pointToCheck) {
		if(existingPoints == null) {return(new Color[]{pointToCheck});}
		
		Color[] newList = new Color[existingPoints.length + 1];
		
		for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
		newList[existingPoints.length] = pointToCheck;
		
		return(newList);
	}
	public static int[] appendIntToList(int[] existingPoints, int pointToCheck) {
		if(existingPoints == null) {return(new int[]{pointToCheck});}
		
		int[] newList = new int[existingPoints.length + 1];
		
		for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
		newList[existingPoints.length] = pointToCheck;
		
		return(newList);
	}
	public static double[] appendDoubleToList(double[] existingPoints, double pointToCheck) {
		if(existingPoints == null) {return(new double[]{pointToCheck});}
		
		double[] newList = new double[existingPoints.length + 1];
		
		for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
		newList[existingPoints.length] = pointToCheck;
		
		return(newList);
	}
	public static Face[] appendFaceToList(Face[] existingPoints, Face pointToCheck) {
		if(existingPoints == null) {
			if(pointToCheck != null) {return(new Face[]{pointToCheck});}
			else {return(null);}
		}
		
		if(pointToCheck != null) {
			Face[] newList = new Face[existingPoints.length + 1];
			
			for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
			newList[existingPoints.length] = pointToCheck;
			
			return(newList);
		}
		else {return(existingPoints);}
	}
	public static int[] removeIntFromList(int[] existingPoints, int index) {
		if(existingPoints == null) {return(null);}
		if(index < 0) {return(existingPoints);}
		
		int[] newList = new int[existingPoints.length - 1];
		
		int offset = 0;
		for(int i = 0; i < existingPoints.length; i++) {
			if(i != index) {newList[i + offset] = existingPoints[i];}
			else {offset = -1;}
		}
		
		return(newList);
	}
	public static Face[] removeFaceFromList(Face[] existingPoints, int index) {
		if(existingPoints == null) {return(null);}
		if(index < 0) {return(existingPoints);}
		
		Face[] newList = new Face[existingPoints.length - 1];
		
		int offset = 0;
		for(int i = 0; i < existingPoints.length; i++) {
			if(i != index) {newList[i + offset] = existingPoints[i];}
			else {offset = -1;}
		}
		
		return(newList);
	}
	public static Polygon[] appendPolyToList(Polygon[] existingPoints, Polygon pointToCheck) {
		if(existingPoints == null) {
			if(pointToCheck != null) {return(new Polygon[]{pointToCheck});}
			else {return(null);}
		}
		
		if(pointToCheck != null) {
			Polygon[] newList = new Polygon[existingPoints.length + 1];
			
			for(int i = 0; i < existingPoints.length; i++) {newList[i] = existingPoints[i];}
			newList[existingPoints.length] = pointToCheck;
			
			return(newList);
		}
		else {return(existingPoints);}
	}
	public static Polygon[] removePolyFromList(Polygon[] existingPoints, int index) {
		if(existingPoints == null) {return(null);}
		if(index < 0) {return(existingPoints);}
		
		Polygon[] newList = new Polygon[existingPoints.length - 1];
		
		int offset = 0;
		for(int i = 0; i < existingPoints.length; i++) {
			if(i != index) {newList[i + offset] = existingPoints[i];}
			else {offset = -1;}
		}
		
		return(newList);
	}
	
	public static Face[][] transferFaceBetweenLists(Face[] origin, Face[] destination, int indexInOrigin) {
		Face[] newDestination = appendFaceToList(destination, origin[indexInOrigin]);
		Face[] newOrigin = removeFaceFromList(origin, indexInOrigin);
		
		return(new Face[][]{newOrigin, newDestination});
	}
	public static Polygon[][] transferPolyBetweenLists(Polygon[] origin, Polygon[] destination, int indexInOrigin) {
		Polygon[] newDestination = appendPolyToList(destination, origin[indexInOrigin]);
		Polygon[] newOrigin = removePolyFromList(origin, indexInOrigin);
		
		return(new Polygon[][]{newOrigin, newDestination});
	}
	
	public static Face[] swapFacesInList(Face[] origin, int index1, int index2) {
		Face[] destination = null;
		
		for(int f = 0; f < origin.length; f++) {
			if(f == index1) {destination = appendFaceToList(destination, origin[index2]);}
			else if(f == index2) {destination = appendFaceToList(destination, origin[index1]);}
			else {destination = appendFaceToList(destination, origin[f]);}
		}
		
		return(destination);
	}
}
