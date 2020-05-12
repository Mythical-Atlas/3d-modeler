package shapes;

import static main.MathFunctions.rotatePointAroundPoint;

import java.awt.Color;

import datatypes.Mesh;
import datatypes.Polygon;
import datatypes.Vector;

public class Donut extends Mesh {
	public Donut(Vector position, Vector size, Color color, Vector rotation) {this(position.x, position.y, position.z, size.x, size.y, size.z, color, rotation);}
	public Donut(Vector position, double w, double h, double l, Color color, Vector rotation) {this(position.x, position.y, position.z, w, h, l, color, rotation);}
	public Donut(double x, double y, double z, double w, double h, double l, Color color, Vector rotation) {
		double radius = 7 / 8;
		
		double w1 = w;
		double h1 = h / 2;
		double l1 = l;
		
		double w2 = w / 8 * 7;
		double h2 = h / 2;
		double l2 = l / 8 * 7;
		
		double corner = Math.sqrt(2) / 2;
		
		double w3 = w * corner;
		double h3 = h / 2;
		double l3 = l * corner;
		
		double w4 = w3 / 8 * 7;
		double h4 = h / 2;
		double l4 = l3 / 8 * 7;
		
		Vector center = new Vector(x, y, z);
		
		Vector northFarTop    = new Vector(x     , y + h1, z + l1);
		Vector northFarBottom = new Vector(x     , y - h1, z + l1);
		Vector southFarTop    = new Vector(x     , y + h1, z - l1);
		Vector southFarBottom = new Vector(x     , y - h1, z - l1);
		Vector eastFarTop     = new Vector(x + w1, y + h1, z     );
		Vector eastFarBottom  = new Vector(x + w1, y - h1, z     );
		Vector westFarTop     = new Vector(x - w1, y + h1, z     );
		Vector westFarBottom  = new Vector(x - w1, y - h1, z     );
		
		Vector northEastFarTop    = new Vector(x + w3, y + h3, z + l3);
		Vector northEastFarBottom = new Vector(x + w3, y - h3, z + l3);
		Vector southEastFarTop    = new Vector(x + w3, y + h3, z - l3);
		Vector southEastFarBottom = new Vector(x + w3, y - h3, z - l3);
		Vector northWestFarTop    = new Vector(x - w3, y + h3, z + l3);
		Vector northWestFarBottom = new Vector(x - w3, y - h3, z + l3);
		Vector southWestFarTop    = new Vector(x - w3, y + h3, z - l3);
		Vector southWestFarBottom = new Vector(x - w3, y - h3, z - l3);
		
		Vector northMidTop    = new Vector(x     , y + h2, z + l2);
		Vector northMidBottom = new Vector(x     , y - h2, z + l2);
		Vector southMidTop    = new Vector(x     , y + h2, z - l2);
		Vector southMidBottom = new Vector(x     , y - h2, z - l2);
		Vector eastMidTop     = new Vector(x + w2, y + h2, z     );
		Vector eastMidBottom  = new Vector(x + w2, y - h2, z     );
		Vector westMidTop     = new Vector(x - w2, y + h2, z     );
		Vector westMidBottom  = new Vector(x - w2, y - h2, z     );
		
		Vector northEastMidTop    = new Vector(x + w4, y + h4, z + l4);
		Vector northEastMidBottom = new Vector(x + w4, y - h4, z + l4);
		Vector southEastMidTop    = new Vector(x + w4, y + h4, z - l4);
		Vector southEastMidBottom = new Vector(x + w4, y - h4, z - l4);
		Vector northWestMidTop    = new Vector(x - w4, y + h4, z + l4);
		Vector northWestMidBottom = new Vector(x - w4, y - h4, z + l4);
		Vector southWestMidTop    = new Vector(x - w4, y + h4, z - l4);
		Vector southWestMidBottom = new Vector(x - w4, y - h4, z - l4);
		
		Vector nft = rotatePointAroundPoint(northFarTop,    center, rotation);
		Vector nfb = rotatePointAroundPoint(northFarBottom, center, rotation);
		Vector sft = rotatePointAroundPoint(southFarTop,    center, rotation);
		Vector sfb = rotatePointAroundPoint(southFarBottom, center, rotation);
		Vector eft = rotatePointAroundPoint(eastFarTop,     center, rotation);
		Vector efb = rotatePointAroundPoint(eastFarBottom,  center, rotation);
		Vector wft = rotatePointAroundPoint(westFarTop,     center, rotation);
		Vector wfb = rotatePointAroundPoint(westFarBottom,  center, rotation);
		
		Vector neft = rotatePointAroundPoint(northEastFarTop,    center, rotation);
		Vector nefb = rotatePointAroundPoint(northEastFarBottom, center, rotation);
		Vector seft = rotatePointAroundPoint(southEastFarTop,    center, rotation);
		Vector sefb = rotatePointAroundPoint(southEastFarBottom, center, rotation);
		Vector nwft = rotatePointAroundPoint(northWestFarTop,    center, rotation);
		Vector nwfb = rotatePointAroundPoint(northWestFarBottom, center, rotation);
		Vector swft = rotatePointAroundPoint(southWestFarTop,    center, rotation);
		Vector swfb = rotatePointAroundPoint(southWestFarBottom, center, rotation);
		
		Vector nmt = rotatePointAroundPoint(northMidTop,    center, rotation);
		Vector nmb = rotatePointAroundPoint(northMidBottom, center, rotation);
		Vector smt = rotatePointAroundPoint(southMidTop,    center, rotation);
		Vector smb = rotatePointAroundPoint(southMidBottom, center, rotation);
		Vector emt = rotatePointAroundPoint(eastMidTop,     center, rotation);
		Vector emb = rotatePointAroundPoint(eastMidBottom,  center, rotation);
		Vector wmt = rotatePointAroundPoint(westMidTop,     center, rotation);
		Vector wmb = rotatePointAroundPoint(westMidBottom,  center, rotation);
		
		Vector nemt = rotatePointAroundPoint(northEastMidTop,    center, rotation);
		Vector nemb = rotatePointAroundPoint(northEastMidBottom, center, rotation);
		Vector semt = rotatePointAroundPoint(southEastMidTop,    center, rotation);
		Vector semb = rotatePointAroundPoint(southEastMidBottom, center, rotation);
		Vector nwmt = rotatePointAroundPoint(northWestMidTop,    center, rotation);
		Vector nwmb = rotatePointAroundPoint(northWestMidBottom, center, rotation);
		Vector swmt = rotatePointAroundPoint(southWestMidTop,    center, rotation);
		Vector swmb = rotatePointAroundPoint(southWestMidBottom, center, rotation);
		
		Polygon rect0  = new Quadrilateral(neft,  nft, nefb,  nfb, -1, color);
		Polygon rect1  = new Quadrilateral( eft, neft,  efb, nefb, -1, color);
		Polygon rect2  = new Quadrilateral(seft,  eft, sefb,  efb, -1, color);
		Polygon rect3  = new Quadrilateral( sft, seft,  sfb, sefb, -1, color);
		Polygon rect4  = new Quadrilateral(swft,  sft, swfb,  sfb, -1, color);
		Polygon rect5  = new Quadrilateral( wft, swft,  wfb, swfb, -1, color);
		Polygon rect6  = new Quadrilateral(nwft,  wft, nwfb,  wfb, -1, color);
		Polygon rect7  = new Quadrilateral( nft, nwft,  nfb, nwfb, -1, color);
		
		Polygon rect8  = new Quadrilateral(nemt,  nmt, nemb,  nmb,  1, color);
		Polygon rect9  = new Quadrilateral( emt, nemt,  emb, nemb,  1, color);
		Polygon rect10 = new Quadrilateral(semt,  emt, semb,  emb,  1, color);
		Polygon rect11 = new Quadrilateral( smt, semt,  smb, semb,  1, color);
		Polygon rect12 = new Quadrilateral(swmt,  smt, swmb,  smb,  1, color);
		Polygon rect13 = new Quadrilateral( wmt, swmt,  wmb, swmb,  1, color);
		Polygon rect14 = new Quadrilateral(nwmt,  wmt, nwmb,  wmb,  1, color);
		Polygon rect15 = new Quadrilateral( nmt, nwmt,  nmb, nwmb,  1, color);
		
		Polygon rect16 = new Quadrilateral(neft,  nft, nemt,  nmt,  1, color);
		Polygon rect17 = new Quadrilateral( eft, neft,  emt, nemt,  1, color);
		Polygon rect18 = new Quadrilateral(seft,  eft, semt,  emt,  1, color);
		Polygon rect19 = new Quadrilateral( sft, seft,  smt, semt,  1, color);
		Polygon rect20 = new Quadrilateral(swft,  sft, swmt,  smt,  1, color);
		Polygon rect21 = new Quadrilateral( wft, swft,  wmt, swmt,  1, color);
		Polygon rect22 = new Quadrilateral(nwft,  wft, nwmt,  wmt,  1, color);
		Polygon rect23 = new Quadrilateral( nft, nwft,  nmt, nwmt,  1, color);
		
		Polygon rect24 = new Quadrilateral(nefb,  nfb, nemb,  nmb, -1, color);
		Polygon rect25 = new Quadrilateral( efb, nefb,  emb, nemb, -1, color);
		Polygon rect26 = new Quadrilateral(sefb,  efb, semb,  emb, -1, color);
		Polygon rect27 = new Quadrilateral( sfb, sefb,  smb, semb, -1, color);
		Polygon rect28 = new Quadrilateral(swfb,  sfb, swmb,  smb, -1, color);
		Polygon rect29 = new Quadrilateral( wfb, swfb,  wmb, swmb, -1, color);
		Polygon rect30 = new Quadrilateral(nwfb,  wfb, nwmb,  wmb, -1, color);
		Polygon rect31 = new Quadrilateral( nfb, nwfb,  nmb, nwmb, -1, color);
		
		polys = new Polygon[]{rect0, rect1, rect2, rect3, rect4, rect5, rect6, rect7, rect8, rect9, rect10, rect11, rect12, rect13, rect14, rect15, rect16, rect17, rect18, rect19, rect20, rect21, rect22, rect23, rect24, rect25, rect26, rect27, rect28, rect29, rect30, rect31};
	}
}
