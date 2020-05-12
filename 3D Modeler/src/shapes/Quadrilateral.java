package shapes;

import java.awt.Color;

import datatypes.Face;
import datatypes.Polygon;
import datatypes.Vector;

public class Quadrilateral extends Polygon {
	public Quadrilateral(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, int normalDirection, Color color) {
		Face f1 = new Face(x1, y1, z1, x3, y3, z3, x4, y4, z4, normalDirection, color);
		Face f2 = new Face(x1, y1, z1, x2, y2, z2, x4, y4, z4, -normalDirection, color);
		
		faces = new Face[]{f1, f2};
	}
	public Quadrilateral(Vector vert0, Vector vert1, Vector vert2, Vector vert3, int normalDirection, Color color) {
		double x1 = vert0.x;
		double y1 = vert0.y;
		double z1 = vert0.z;
		double x2 = vert1.x;
		double y2 = vert1.y;
		double z2 = vert1.z;
		double x3 = vert2.x;
		double y3 = vert2.y;
		double z3 = vert2.z;
		double x4 = vert3.x;
		double y4 = vert3.y;
		double z4 = vert3.z;
		
		Face f1 = new Face(x1, y1, z1, x3, y3, z3, x4, y4, z4, normalDirection, color);
		Face f2 = new Face(x1, y1, z1, x2, y2, z2, x4, y4, z4, -normalDirection, color);
		
		faces = new Face[]{f1, f2};
	}
}
