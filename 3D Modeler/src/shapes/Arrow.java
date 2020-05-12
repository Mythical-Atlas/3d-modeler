package shapes;

import static main.MathFunctions.rotatePointAroundPoint;

import java.awt.Color;

import datatypes.Face;
import datatypes.Mesh;
import datatypes.Polygon;
import datatypes.Vector;

public class Arrow extends Mesh {
	public Arrow(Vector position, double w, double h, double l, Color color, Vector rotation) {this(position.x, position.y, position.z, w, h, l, color, rotation);}
	public Arrow(Vector position, Vector size, Color color, Vector rotation) {this(position.x, position.y, position.z, size.x, size.y, size.z, color, rotation);}
	public Arrow(double x, double y, double z, double w, double h, double l, Color color, Vector rotation) {
		// base
		double x1 = x;
		double y1 = y;
		double z1 = z;
		
		// between head and shaft
		double x2 = x1;
		double y2 = y1;
		double z2 = z + l * 0.67;
		
		// point
		double x3 = x1;
		double y3 = y1;
		double z3 = z + l;
		
		double w1 = w;
		double h1 = h;
		
		double w2 = w / 2;
		double h2 = h / 2;
		
		double w3 = w / 2;
		double h3 = h / 2;
		
		Vector p0 = new Vector(x1, y1, z1);
		
		Vector p11 = new Vector(x1 - w2 / 2, y1 + h2 / 2, z1);
		Vector p12 = new Vector(x1 + w2 / 2, y1 + h2 / 2, z1);
		Vector p13 = new Vector(x1 - w2 / 2, y1 - h2 / 2, z1);
		Vector p14 = new Vector(x1 + w2 / 2, y1 - h2 / 2, z1);
		
		Vector p21 = new Vector(x1 - w2 / 2, y1 + h2 / 2, z2);
		Vector p22 = new Vector(x1 + w2 / 2, y1 + h2 / 2, z2);
		Vector p23 = new Vector(x1 - w2 / 2, y1 + h2 / 2, z1);
		Vector p24 = new Vector(x1 + w2 / 2, y1 + h2 / 2, z1);
		
		Vector p31 = new Vector(x1 + w2 / 2, y1 + h2 / 2, z2);
		Vector p32 = new Vector(x1 + w2 / 2, y1 - h2 / 2, z2);
		Vector p33 = new Vector(x1 + w2 / 2, y1 + h2 / 2, z1);
		Vector p34 = new Vector(x1 + w2 / 2, y1 - h2 / 2, z1);
		
		Vector p41 = new Vector(x1 + w2 / 2, y1 - h2 / 2, z2);
		Vector p42 = new Vector(x1 - w2 / 2, y1 - h2 / 2, z2);
		Vector p43 = new Vector(x1 + w2 / 2, y1 - h2 / 2, z1);
		Vector p44 = new Vector(x1 - w2 / 2, y1 - h2 / 2, z1);
		
		Vector p51 = new Vector(x1 - w2 / 2, y1 - h2 / 2, z2);
		Vector p52 = new Vector(x1 - w2 / 2, y1 + h2 / 2, z2);
		Vector p53 = new Vector(x1 - w2 / 2, y1 - h2 / 2, z1);
		Vector p54 = new Vector(x1 - w2 / 2, y1 + h2 / 2, z1);
		
		Vector p61 = new Vector(x2 - w1 / 2, y2 + h1 / 2, z2);
		Vector p62 = new Vector(x2 + w3 / 2, y2 + h1 / 2, z2);
		Vector p63 = new Vector(x2 - w1 / 2, y2 + h3 / 2, z2);
		Vector p64 = new Vector(x2 + w3 / 2, y2 + h3 / 2, z2);
		
		Vector p71 = new Vector(x2 + w1 / 2, y2 + h1 / 2, z2);
		Vector p72 = new Vector(x2 + w1 / 2, y2 - h3 / 2, z2);
		Vector p73 = new Vector(x2 + w3 / 2, y2 + h1 / 2, z2);
		Vector p74 = new Vector(x2 + w3 / 2, y2 - h3 / 2, z2);
		
		Vector p81 = new Vector(x2 + w1 / 2, y2 - h1 / 2, z2);
		Vector p82 = new Vector(x2 - w3 / 2, y2 - h1 / 2, z2);
		Vector p83 = new Vector(x2 + w1 / 2, y2 - h3 / 2, z2);
		Vector p84 = new Vector(x2 - w3 / 2, y2 - h3 / 2, z2);
		
		Vector p91 = new Vector(x2 - w1 / 2, y2 - h1 / 2, z2);
		Vector p92 = new Vector(x2 - w1 / 2, y2 + h3 / 2, z2);
		Vector p93 = new Vector(x2 - w3 / 2, y2 - h1 / 2, z2);
		Vector p94 = new Vector(x2 - w3 / 2, y2 + h3 / 2, z2);
		
		Vector p7 = new Vector(x3, y3, z3);
		
		p11 = rotatePointAroundPoint(p11, p0, rotation);
		p12 = rotatePointAroundPoint(p12, p0, rotation);
		p13 = rotatePointAroundPoint(p13, p0, rotation);
		p14 = rotatePointAroundPoint(p14, p0, rotation);
		p21 = rotatePointAroundPoint(p21, p0, rotation);
		p22 = rotatePointAroundPoint(p22, p0, rotation);
		p23 = rotatePointAroundPoint(p23, p0, rotation);
		p24 = rotatePointAroundPoint(p24, p0, rotation);
		p31 = rotatePointAroundPoint(p31, p0, rotation);
		p32 = rotatePointAroundPoint(p32, p0, rotation);
		p33 = rotatePointAroundPoint(p33, p0, rotation);
		p34 = rotatePointAroundPoint(p34, p0, rotation);
		p41 = rotatePointAroundPoint(p41, p0, rotation);
		p42 = rotatePointAroundPoint(p42, p0, rotation);
		p43 = rotatePointAroundPoint(p43, p0, rotation);
		p44 = rotatePointAroundPoint(p44, p0, rotation);
		p51 = rotatePointAroundPoint(p51, p0, rotation);
		p52 = rotatePointAroundPoint(p52, p0, rotation);
		p53 = rotatePointAroundPoint(p53, p0, rotation);
		p54 = rotatePointAroundPoint(p54, p0, rotation);
		p61 = rotatePointAroundPoint(p61, p0, rotation);
		p62 = rotatePointAroundPoint(p62, p0, rotation);
		p63 = rotatePointAroundPoint(p63, p0, rotation);
		p64 = rotatePointAroundPoint(p64, p0, rotation);
		p71 = rotatePointAroundPoint(p71, p0, rotation);
		p72 = rotatePointAroundPoint(p72, p0, rotation);
		p73 = rotatePointAroundPoint(p73, p0, rotation);
		p74 = rotatePointAroundPoint(p74, p0, rotation);
		p81 = rotatePointAroundPoint(p81, p0, rotation);
		p82 = rotatePointAroundPoint(p82, p0, rotation);
		p83 = rotatePointAroundPoint(p83, p0, rotation);
		p84 = rotatePointAroundPoint(p84, p0, rotation);
		p91 = rotatePointAroundPoint(p91, p0, rotation);
		p92 = rotatePointAroundPoint(p92, p0, rotation);
		p93 = rotatePointAroundPoint(p93, p0, rotation);
		p94 = rotatePointAroundPoint(p94, p0, rotation);
		p7  = rotatePointAroundPoint(p7 , p0, rotation);
		
		Polygon rect0 = new Quadrilateral(p11, p12, p13, p14, -1, color);
		Polygon rect1 = new Quadrilateral(p21, p22, p23, p24, -1, color);
		Polygon rect2 = new Quadrilateral(p31, p32, p33, p34, -1, color);
		Polygon rect3 = new Quadrilateral(p41, p42, p43, p44, -1, color);
		Polygon rect4 = new Quadrilateral(p51, p52, p53, p54, -1, color);
		Polygon rect5 = new Quadrilateral(p61, p62, p63, p64, -1, color);
		Polygon rect6 = new Quadrilateral(p71, p72, p73, p74, -1, color);
		Polygon rect7 = new Quadrilateral(p81, p82, p83, p84, -1, color);
		Polygon rect8 = new Quadrilateral(p91, p92, p93, p94, -1, color);
		
		Polygon tri0 = new Polygon(new Face(p61, p71, p7, -1, color));
		Polygon tri1 = new Polygon(new Face(p71, p81, p7, -1, color));
		Polygon tri2 = new Polygon(new Face(p81, p91, p7, -1, color));
		Polygon tri3 = new Polygon(new Face(p91, p61, p7, -1, color));
		
		polys = new Polygon[]{rect0, rect1, rect2, rect3, rect4, rect5, rect6, rect7, rect8, tri0, tri1, tri2, tri3};
	}
}
