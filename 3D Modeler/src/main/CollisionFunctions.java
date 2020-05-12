package main;

import datatypes.Face;
import datatypes.Vector;

import static main.RenderFunctions.*;
import static main.MathFunctions.*;

public class CollisionFunctions {
	public static boolean checkPointOnFace(Vector point, Face face) {
		if(point != null) {
			Vector[] points = face.getPoints();
			
			double bigArea = getTriangleArea(points[0], points[1], points[2]);
			double small0  = getTriangleArea(point    , points[1], points[2]);
			double small1  = getTriangleArea(points[0], point    , points[2]);
			double small2  = getTriangleArea(points[0], points[1], point    );
			
			return(bigArea == Math.round(small0 + small1 + small2));
		}
		else {return(false);}
	}
	
	/*public static boolean checkPointOnFace(Vector point, Face face) {
		if(point != null) {
			Vector v0 = face.a;
			Vector v1 = face.b;
			Vector v2 = face.c;
			Vector q = point;
			
			Vector e1 = v1.subtract(v0);
			Vector e2 = v2.subtract(v0);
			
			Vector a = e1.square();
			Vector b = e1.multiply(e2);
			Vector c = e2.square();
			
			Vector D = a.multiply(c).subtract(b.square());
			Vector A = a.divide(D);
			Vector B = b.divide(D);
			Vector C = c.divide(D);
			
			Vector uBeta = C.multiply(e1).subtract(B.multiply(e2));
			Vector uGamma = A.multiply(e2).subtract(B.multiply(e1));
			
			double beta = uBeta.dot(q.subtract(v0));
			double gamma = uGamma.dot(q.subtract(v0));
			double alpha = 1 - beta - gamma;
			
			return(beta >= 0 && gamma >= 0 && alpha >= 0);
		}
		else {return(false);}
	}*/
	
	// get point of intersection between ray and plane
	public static Vector rayPlaneIntersection(Vector planePoint, Vector planeNormal, Vector rayPoint, Vector rayDirection) {
		// normalize direction vectors
		Vector planeDir = planeNormal.normalize();
		Vector rayDir = rayDirection.normalize();
		
		// if ray and plane don't intersect, return null
	    if(planeDir.dot(rayDir) >= 0) {return(null);}

	    // otherwise, return the point of intersection
	    return(rayPoint.plus(rayDir.scale((planeDir.dot(planePoint) - planeDir.dot(rayPoint)) / planeDir.dot(rayDir))));
	}
	
	public static Vector rayFaceIntersection(Vector rayPoint, Vector rayDirection, Face face) {
		Vector planePoint = face.getPoints()[0];
		Vector planeNormal = face.getNormal().scale(-1);
		
		Vector intersect = rayPlaneIntersection(planePoint, planeNormal, rayPoint, rayDirection);
		
		if(checkPointOnFace(intersect, face)) {return(intersect);}
		else {return(null);}
	}
	
	public static int rayCast(Vector origin, Vector direction, Face[] faces) {
		int smallestIndex = -1;
		double smallestDistance = 0;
		
		for(int f = 0; f < faces.length; f++) {
			Vector intersect = rayFaceIntersection(origin, direction, faces[f]);
			
			if(intersect != null) {
				double distance = intersect.distanceFrom(origin);
				
				if(distance < smallestDistance || smallestIndex == -1) {
					smallestDistance = distance;
					smallestIndex = f;
				}
			}
		}
		
		return(smallestIndex);
	}
	
	public static int[] rayCast2(Vector origin, Vector direction, Face[] faces) {
		int[] indexes = null;
		
		for(int f = 0; f < faces.length; f++) {if(rayFaceIntersection(origin, direction, faces[f]) != null) {indexes = appendIntToList(indexes, f);}}
		
		return(indexes);
	}
}
