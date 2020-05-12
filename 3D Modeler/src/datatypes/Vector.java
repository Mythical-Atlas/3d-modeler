package datatypes;

import static main.MathFunctions.*;

public class Vector {
	public double x;
	public double y;
	public double z;
	
	public Vector() {}
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Vector(double[] values) {
		x = values[0];
		y = values[1];
		z = values[2];
	}
	
	public double[] getDoubles() {return(new double[]{x, y, z});}
	
	public boolean equals(Vector b) {return(x == b.x && y == b.y && z == b.z);}
	
	// vector math functions
	
	public double length() {return(Math.sqrt(x * x + y * y + z * z));}
	
	public Vector normalize() {
		double length = Math.sqrt(x * x + y * y + z * z);
		
		return(new Vector(x / length, y / length, z / length));
	}
	
	public double dot(Vector b) {return(x * b.x + y * b.y + z * b.z);}
	public double distanceFrom(Vector b) {return(subtract(b).length());}
	
	public Vector square() {return(new Vector(x * x, y * y, z * z));}
	public Vector scale(double b) {return(new Vector(x * b, y * b, z * b));}
	public Vector plus(Vector b) {return(new Vector(x + b.x, y + b.y, z + b.z));}
	public Vector multiply(Vector b) {return(new Vector(x * b.x, y * b.y, z * b.z));}
	public Vector divide(Vector b) {return(new Vector(x / b.x, y / b.y, z / b.z));}
	public Vector subtract(Vector b) {return(new Vector(x - b.x, y - b.y, z - b.z));}
	public Vector cross(Vector b) {return(new Vector(y*b.z - z * b.y, z * b.x - x * b.z, x * b.y - y * b.x));}
}
