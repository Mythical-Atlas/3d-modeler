package datatypes;

import static main.MathFunctions.appendDoubleToList;

public class Polygon {
	public Face[] faces;
	
	public Polygon() {}
	public Polygon(Face face) {faces = new Face[]{face};}
	public Polygon(Face[] faces) {this.faces = faces;}
	
	public void translate(Vector positionDelta) {for(int i = 0; i < faces.length; i++) {faces[i].translate(positionDelta);}}
	public void rotateAround(Vector center, Vector angles) {for(int p = 0; p < faces.length; p++) {faces[p].rotateAround(center, angles);}}
	
	public Vector getPosition() {
		double x = 0;
		double y = 0;
		double z = 0;
		
		for(int f = 0; f < faces.length; f++) {
			Vector temp = faces[f].getPosition();
			
			x += temp.x;
			y += temp.y;
			z += temp.z;
		}
		
		x /= faces.length;
		y /= faces.length;
		z /= faces.length;
		
		return(new Vector(x, y, z));
	}
	
	public Vector getNormal() {return(faces[0].getNormal());}
	
	public double getAverageDistanceFromPoint(Vector point) {return(getPosition().distanceFrom(point));}
}
