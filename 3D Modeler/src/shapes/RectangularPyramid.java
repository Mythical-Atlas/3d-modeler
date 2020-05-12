package shapes;

import static main.MathFunctions.rotatePointAroundPoint;

import java.awt.Color;

import datatypes.Face;
import datatypes.Mesh;
import datatypes.Polygon;
import datatypes.Vector;

public class RectangularPyramid extends Mesh {
	public RectangularPyramid(double x, double y, double z, double w, double h, double l, Vector rotation) {
		Vector b0 = new Vector(x    , y    , z    );
		Vector b1 = new Vector(x + w, y    , z    );
		Vector b2 = new Vector(x    , y    , z + l);
		Vector b3 = new Vector(x + w, y    , z + l);
		Vector p = new Vector(x + w / 2, y + h, z + l / 2);
		
		b0 = rotatePointAroundPoint(b0, b0, rotation);
		b1 = rotatePointAroundPoint(b1, b0, rotation);
		b2 = rotatePointAroundPoint(b2, b0, rotation);
		b3 = rotatePointAroundPoint(b3, b0, rotation);
		p = rotatePointAroundPoint(p, b0, rotation);
		
		Polygon tri0 = new Polygon(new Face(b2, b3, p,  1, new Color(  0,   0, 128)));
		Polygon tri1 = new Polygon(new Face(b1, b3, p, -1, new Color(128,   0,   0)));
		Polygon tri2 = new Polygon(new Face(b0, b1, p, -1, new Color(128, 128,   0)));
		Polygon tri3 = new Polygon(new Face(b0, b2, p,  1, new Color(  0, 128, 128)));
		
		Polygon rect0 = new Quadrilateral(b0, b1, b2, b3, -1, new Color(128,   0, 128));
		
		polys = new Polygon[]{rect0, tri0, tri1, tri2, tri3};
	}
}
