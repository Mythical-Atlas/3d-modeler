package shapes;

import datatypes.Mesh;
import datatypes.Polygon;
import datatypes.Vector;

import static main.MathFunctions.*;

import java.awt.Color;

public class Cuboid extends Mesh {
	public Cuboid(double x, double y, double z, double w, double h, double l, Vector rotation) {
		Vector ftl = new Vector(x    , y    , z    );
		Vector ftr = new Vector(x + w, y    , z    );
		Vector fbl = new Vector(x    , y + h, z    );
		Vector fbr = new Vector(x + w, y + h, z    );
		Vector btl = new Vector(x    , y    , z + l);
		Vector btr = new Vector(x + w, y    , z + l);
		Vector bbl = new Vector(x    , y + h, z + l);
		Vector bbr = new Vector(x + w, y + h, z + l);
		
		ftl = rotatePointAroundPoint(ftl, ftl, rotation);
		ftr = rotatePointAroundPoint(ftr, ftl, rotation);
		fbl = rotatePointAroundPoint(fbl, ftl, rotation);
		fbr = rotatePointAroundPoint(fbr, ftl, rotation);
		btl = rotatePointAroundPoint(btl, ftl, rotation);
		btr = rotatePointAroundPoint(btr, ftl, rotation);
		bbl = rotatePointAroundPoint(bbl, ftl, rotation);
		bbr = rotatePointAroundPoint(bbr, ftl, rotation);
		
		Polygon rect0 = new Quadrilateral(ftl, ftr, fbl, fbr,  1, new Color(128, 128,   0));
		Polygon rect1 = new Quadrilateral(ftr, btr, fbr, bbr,  1, new Color(128,   0,   0));
		Polygon rect2 = new Quadrilateral(btr, btl, bbr, bbl,  1, new Color(  0,   0, 128));
		Polygon rect3 = new Quadrilateral(btl, ftl, bbl, fbl,  1, new Color(  0, 128, 128));
		Polygon rect4 = new Quadrilateral(btl, ftl, btr, ftr, -1, new Color(128,   0, 128));
		Polygon rect5 = new Quadrilateral(fbl, fbr, bbl, bbr,  1, new Color(  0, 128,   0));
		
		polys = new Polygon[]{rect0, rect1, rect2, rect3, rect4, rect5};
	}
}
