package datatypes;

import java.awt.Color;

import static main.MathFunctions.*;

public class Face {
	public Vector a;
	public Vector b;
	public Vector c;
	public Color color;
	public Color wireColor;
	
	private int normalDirection;
	
	public Face(double ax, double ay, double az, double bx, double by, double bz, double cx, double cy, double cz, int normalDirection, Color color) {
		a = new Vector(ax, ay, az);
		b = new Vector(bx, by, bz);
		c = new Vector(cx, cy, cz);
		
		this.color = color;
		this.normalDirection = normalDirection;
	}
	public Face(Vector a, Vector b, Vector c, int normalDirection, Color color) {this(a.x, a.y, a.z, b.x, b.y, b.z, c.x, c.y, c.z, normalDirection, color);}
	
	public void translate(Vector positionDelta) {
		a = a.plus(positionDelta);
		b = b.plus(positionDelta);
		c = c.plus(positionDelta);
	}
	
	public void rotateAround(Vector position, Vector angles) {
		a = rotatePointAroundPoint(a, position, angles);
		b = rotatePointAroundPoint(b, position, angles);
		c = rotatePointAroundPoint(c, position, angles);
	}
	
	public Vector[] getPoints() {return(new Vector[]{a, b, c});}
	
	public Vector getPosition() {
		Vector[] points = getPoints();
		Vector pos = new Vector((points[0].x + points[1].x + points[2].x) / 3, (points[0].y + points[1].y + points[2].y) / 3, (points[0].z + points[1].z + points[2].z) / 3);
		
		return(pos);
	}
	
	public Vector getNormal() {
		Vector[] points = getPoints();
		Vector u = points[1].subtract(points[0]);
		Vector v = points[2].subtract(points[0]);
		
		return(new Vector(u.y * v.z - u.z * v.y, u.z * v.x - u.x * v.z, u.x * v.y - u.y * v.x).scale(normalDirection));
	}
	
	public double getAverageDistanceFromPoint(Vector point) {
		double aa = a.distanceFrom(point);
		double ba = b.distanceFrom(point);
		double ca = c.distanceFrom(point);
		
		double ab = Math.min(aa, ba);
		double bb = Math.min(ab, ca);
		
		return(bb);
	}
}
