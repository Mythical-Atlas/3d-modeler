package datatypes;

public class Mesh {
	public Polygon[] polys;
	
	public Mesh() {}
	public Mesh(Polygon poly) {polys = new Polygon[]{poly};}
	public Mesh(Polygon[] polys) {this.polys = polys;}
	
	public void translate(Vector positionDelta) {for(int p = 0; p < polys.length; p++) {polys[p].translate(positionDelta);}}
	public void rotate(Vector angles) {
		Vector center = getPosition();
		
		for(int p = 0; p < polys.length; p++) {polys[p].rotateAround(center, angles);}
	}
	
	public Vector getPosition() {
		double x = 0;
		double y = 0;
		double z = 0;
		
		for(int p = 0; p < polys.length; p++) {
			Vector temp = polys[p].getPosition();
			x += temp.x;
			y += temp.y;
			z += temp.z;
		}
		
		return(new Vector(x / polys.length, y / polys.length, z / polys.length));
	}
}
