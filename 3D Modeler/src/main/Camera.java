package main;

import java.awt.Color;
import java.awt.Graphics2D;

import datatypes.Face;
import datatypes.Mesh;
import datatypes.Polygon;
import datatypes.Vector;
import shapes.Arrow;
import shapes.Donut;

import static main.MathFunctions.*;
import static main.RenderFunctions.*;
import static main.CollisionFunctions.*;

public class Camera {
	public double fieldOfView;
	
	public Vector position;
	public Vector angle;
	
	public Vector anchor;
	
	public double distance;
	
	public int viewportX;
	public int viewportY;
	public int viewportW;
	public int viewportH;
	
	public Camera(Vector position, double distance, Vector angle, double fieldOfView, int viewportX, int viewportY, int viewportW, int viewportH) {
		this.angle = angle;
		this.fieldOfView = fieldOfView;
		anchor = position;
		this.viewportX = viewportX;
		this.viewportY = viewportY;
		this.viewportW = viewportW;
		this.viewportH = viewportH;
		this.distance = distance;
		
		rotateAroundAnchor(new Vector(0, 0, 0));
	}
	
	public void translate(Vector deltaPos) {
		anchor.x += deltaPos.x;
		anchor.y += deltaPos.y;
		anchor.z += deltaPos.z;
		
		rotateAroundAnchor(new Vector(0, 0, 0));
	}
	// translates relative to camera POV
	public void translateRelative(Vector deltaPos, Vector deltaAngle) {
		// deltaAngle just means which axis to translate relative to
		// ex: (0, 1, 0) means translate with relative y axis rotation but regular x and z axis rotation
		Vector deltaPos2 = rotateVector(deltaPos, new Vector(angle.x * deltaAngle.x, angle.y * deltaAngle.y, angle.z * deltaAngle.z));
		
		anchor.x += deltaPos2.x;
		anchor.y += deltaPos2.y;
		anchor.z += deltaPos2.z;
		
		rotateAroundAnchor(new Vector(0, 0, 0));
	}

	public void rotate(Vector deltaAngle) {
		angle.x += deltaAngle.x;
		angle.y += deltaAngle.y;
		angle.z += deltaAngle.z;
	}
	
	public void rotateAroundAnchor(Vector angles) {
		rotate(angles);
		position = rotatePointAroundPoint(new Vector(0, 0, -distance).subtract(anchor.scale(-1)), anchor, angle);
	}
	
	public void zoom(double zoomAmount) {
		distance += zoomAmount;
		if(distance < 0) {distance = 0;}
		rotateAroundAnchor(new Vector(0, 0, 0));
	}
	
	public int selectMesh(Vector mousePos, Mesh[] meshes) {
		int smallestIndex = -1;
		double smallestDistance = 0;
		
		for(int m = 0; m < meshes.length; m++) {
			for(int p = 0; p < meshes[m].polys.length; p++) {
				for(int f = 0; f < meshes[m].polys[p].faces.length; f++) {
					// gets focal distance info from field of view and "sensor size" (window size)
					double halfSize = Math.sqrt(viewportW * viewportW + viewportH * viewportH) / 2;
					double focalDistance = halfSize / Math.tan(Math.toRadians(fieldOfView / 2));
					
					Vector rayOrigin = position;
					Vector rayDirection = new Vector(mousePos.x, mousePos.y, focalDistance);
					
					rayDirection = rotatePointAroundPoint(rayDirection, new Vector(0, 0, 0), angle);
					
					if(rayFaceIntersection(rayOrigin, rayDirection, meshes[m].polys[p].faces[f]) != null) {
						double distance = rayFaceIntersection(rayOrigin, rayDirection, meshes[m].polys[p].faces[f]).distanceFrom(position);
						
						if(distance < smallestDistance || smallestIndex == -1) {
							smallestDistance = distance;
							smallestIndex = m;
						}
					}
				}
			}
		}
		
		return(smallestIndex);
	}

	public void drawAnchor(Graphics2D graphics) {
		double anchorSize = 100;
		
		Vector[] xArrow = new Vector[]{anchor, anchor.plus(new Vector(anchorSize, 0, 0))};
		Vector[] yArrow = new Vector[]{anchor, anchor.plus(new Vector(0, anchorSize, 0))};
		Vector[] zArrow = new Vector[]{anchor, anchor.plus(new Vector(0, 0, anchorSize))};
		
		drawEdgeWireframe(xArrow, Color.RED, graphics);
		drawEdgeWireframe(yArrow, Color.GREEN, graphics);
		drawEdgeWireframe(zArrow, Color.BLUE, graphics);
	}
	
	public Mesh[] getTranslationGizmoMeshes(Vector position) {
		double arrowSize = 400;
		double arrowGirth = arrowSize / 4;
		
		Mesh xArrow = new Arrow(position, arrowGirth, arrowGirth, arrowSize, Color.RED,   new Vector(0, 90, 0));
		Mesh yArrow = new Arrow(position, arrowGirth, arrowGirth, arrowSize, Color.GREEN, new Vector(-90, 0, 0));
		Mesh zArrow = new Arrow(position, arrowGirth, arrowGirth, arrowSize, Color.BLUE,  new Vector(0, 0, 0));
		
		Mesh[] meshes = new Mesh[]{xArrow, yArrow, zArrow};
		
		return(meshes);
	}
	public Mesh[] getRotationGizmoMeshes(Vector position) {
		double donutSize = 300;
		double donutHeight = donutSize / 8;
		
		Mesh xArrow = new Donut(position, donutSize, donutHeight, donutSize, Color.RED,   new Vector(0, 0, -90));
		Mesh yArrow = new Donut(position, donutSize, donutHeight, donutSize, Color.GREEN, new Vector(0, 0, 0));
		Mesh zArrow = new Donut(position, donutSize, donutHeight, donutSize, Color.BLUE,  new Vector(90, 0, 0));
		
		Mesh[] meshes = new Mesh[]{xArrow, yArrow, zArrow};
		
		return(meshes);
	}
	
	public void drawTranslationGizmo(Vector position, Graphics2D graphics) {
		Mesh[] meshes = getTranslationGizmoMeshes(position);
		Color[] colors = new Color[]{Color.RED, Color.GREEN, Color.BLUE};
		
		drawFlat(meshes, colors, graphics);
	}
	public void drawRotationGizmo(Vector position, Graphics2D graphics) {
		Mesh[] meshes = getRotationGizmoMeshes(position);
		Color[] colors = new Color[]{Color.RED, Color.GREEN, Color.BLUE};
		
		drawFlat(meshes, colors, graphics);
	}

	public void drawWireframe(Mesh[] meshes, Color[] colors, Graphics2D graphics) {
		Face[] faces = null;
		
		for(int m = 0; m < meshes.length; m++) {
			for(int p = 0; p < meshes[m].polys.length; p++) {
				for(int f = 0; f < meshes[m].polys[p].faces.length; f++) {
					faces = appendFaceToList(faces, meshes[m].polys[p].faces[f]);
					faces[faces.length - 1].wireColor = colors[m];
				}
			}
		}
		
		faces = reorderFaces(faces, this);
		
		drawFacesWireframe(faces, graphics);
	}
	
	public void drawFlat(Mesh[] meshes, Color[] colors, Graphics2D graphics) {
		Face[] faces = null;
		
		for(int m = 0; m < meshes.length; m++) {
			for(int p = 0; p < meshes[m].polys.length; p++) {
				for(int f = 0; f < meshes[m].polys[p].faces.length; f++) {
					faces = appendFaceToList(faces, meshes[m].polys[p].faces[f]);
					faces[faces.length - 1].wireColor = colors[m];
				}
			}
		}
		
		faces = reorderFaces(faces, this);
		
		drawFacesFlat(faces, graphics);
	}
	
	public void drawFlatWire(Mesh[] meshes, Color[] colors, Graphics2D graphics) {
		Face[] faces = null;
		
		for(int m = 0; m < meshes.length; m++) {
			for(int p = 0; p < meshes[m].polys.length; p++) {
				for(int f = 0; f < meshes[m].polys[p].faces.length; f++) {
					faces = appendFaceToList(faces, meshes[m].polys[p].faces[f]);
					faces[faces.length - 1].wireColor = colors[m];
				}
			}
		}
		
		faces = reorderFaces(faces, this);
		
		drawFacesFlatWire(faces, graphics);
	}

	public void drawEdgeWireframe(Vector[] edge, Color color, Graphics2D graphics) {
		graphics.setColor(color);
		
		Vector[] drawnEdge = clipEdgeToView(edge, this);
			
		// draw as 2d image
		if(drawnEdge != null) {graphics.drawLine((int)drawnEdge[0].x + Loader.WIDTH / 2 + viewportX / 2, (int)-drawnEdge[0].y + Loader.HEIGHT / 2 + viewportY / 2, (int)drawnEdge[1].x + Loader.WIDTH / 2 + viewportX / 2, (int)-drawnEdge[1].y + Loader.HEIGHT / 2 + viewportY / 2);}
	}
	
	public void drawFacesWireframe(Face[] faces, Graphics2D graphics) {
		// loop through edges of mesh
		for(int f = 0; f < faces.length; f++) {
			Vector[] pointsToDraw = clipFaceToView(faces[f], this);
			
			if(pointsToDraw != null) {
				int[] xPoints = new int[pointsToDraw.length];
				int[] yPoints = new int[pointsToDraw.length];
				
				for(int i = 0; i < pointsToDraw.length; i++) {
					xPoints[i] = (int)pointsToDraw[i].x + Loader.WIDTH / 2 + viewportX / 2;
					yPoints[i] = (int)-pointsToDraw[i].y + Loader.HEIGHT / 2 + viewportY / 2;
				}
				
				graphics.setColor(faces[f].wireColor);
				graphics.drawPolygon(xPoints, yPoints, pointsToDraw.length);
			}
		}
	}
	public void drawFacesFlat(Face[] faces, Graphics2D graphics) {
		// loop through edges of mesh
		for(int f = 0; f < faces.length; f++) {
			if(checkFaceVisible(faces[f], this)) {
				Vector[] pointsToDraw = clipFaceToView(faces[f], this);
				
				if(pointsToDraw != null) {
					int[] xPoints = new int[pointsToDraw.length];
					int[] yPoints = new int[pointsToDraw.length];
					
					for(int i = 0; i < pointsToDraw.length; i++) {
						xPoints[i] = (int)pointsToDraw[i].x + Loader.WIDTH / 2 + viewportX / 2;
						yPoints[i] = (int)-pointsToDraw[i].y + Loader.HEIGHT / 2 + viewportY / 2;
					}
					
					graphics.setColor(faces[f].color);
					graphics.fillPolygon(xPoints, yPoints, pointsToDraw.length);
					
					if(faces[f].wireColor == Color.YELLOW) {
						graphics.setColor(faces[f].wireColor);
						graphics.drawPolygon(xPoints, yPoints, pointsToDraw.length);
					}
				}
			}
		}
	}
	public void drawFacesFlatWire(Face[] faces, Graphics2D graphics) {
		// loop through edges of mesh
		for(int f = 0; f < faces.length; f++) {
			if(checkFaceVisible(faces[f], this)) {
				Vector[] pointsToDraw = clipFaceToView(faces[f], this);
				
				if(pointsToDraw != null) {
					int[] xPoints = new int[pointsToDraw.length];
					int[] yPoints = new int[pointsToDraw.length];
					
					for(int i = 0; i < pointsToDraw.length; i++) {
						xPoints[i] = (int)pointsToDraw[i].x + Loader.WIDTH / 2 + viewportX / 2;
						yPoints[i] = (int)-pointsToDraw[i].y + Loader.HEIGHT / 2 + viewportY / 2;
					}
					
					graphics.setColor(faces[f].color);
					graphics.fillPolygon(xPoints, yPoints, pointsToDraw.length);
					
					graphics.setColor(faces[f].wireColor);
					graphics.drawPolygon(xPoints, yPoints, pointsToDraw.length);
				}
			}
		}
	}
}
