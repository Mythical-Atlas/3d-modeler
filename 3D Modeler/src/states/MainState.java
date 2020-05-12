package states;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

import datatypes.Mesh;
import datatypes.State;
import datatypes.Vector;
import jdk.internal.org.jline.utils.Colors;

import static java.awt.event.KeyEvent.*;

import main.Camera;
import main.Loader;
import shapes.Arrow;
import shapes.Cuboid;
import shapes.Donut;
import shapes.RectangularPyramid;

import static main.MathFunctions.*;
import static main.RenderFunctions.*;

public class MainState extends State {
	private final int VIEWPORT_X = 240;
	private final int VIEWPORT_Y = 0;
	private final int VIEWPORT_W = 1280 - 240;
	private final int VIEWPORT_H = 720;
	
	// default state
	private final int DEFAULT = 0;
	
	// mouse states
	private final int CAMERA  = 1;
	private final int GIZMO   = 2;
	
	// gizmo states
	private final int TRANSLATING = 1;
	private final int ROTATING    = 2;
	private final int SCALING     = 3;
	
	// display modes
	private final int WIREFRAME = 2;
	private final int FLAT      = 0;
	private final int FLAT_WIRE = 1;
	
	// constant values
	private final double PAN_SPEED = 20;
	private final double ZOOM_SPEED = 50;
	private final double MOUSE_SPEED = 0.25;
	private final double SCROLL_SPEED = 300;
	
	private final double TRANSLATE_SPEED = 5;
	private final double ROTATE_SPEED = 1;
	
	private boolean leftButton = false;
	private boolean middleButton = false;
	//private boolean rightButton = false;
	
	private boolean controlKey = false;
	private boolean shiftKey = false;
	private boolean altKey = false;
	
	private Mesh[] meshes;
	private Color[] colors;
	
	private Camera cam;
	
	private int displayMode = 0;
	
	private boolean mouseInWindow;
	private int mouseState;
	private int gizmoState;
	private Vector oldMousePos;
	
	private int selection = -1;
	private int gizmoSelection = -1;
	
	public MainState() {
		resetView();
		
		double cubeSize = 1000;
		double cubeHalf = 500;
		
		Mesh cube = new Cuboid(-cubeHalf, -cubeHalf, -cubeHalf, cubeSize, cubeSize, cubeSize, new Vector(0, 0, 0));
		Mesh pyramid = new RectangularPyramid(-cubeSize * 2, -cubeHalf, -cubeHalf, cubeSize, cubeSize, cubeSize, new Vector(0, 0, 0));
		Mesh arrow = new Arrow(2000, 0, 0, cubeSize / 2, cubeSize / 2, cubeSize * 2, new Color(128, 0, 0), new Vector(0, 0, 0));
		Mesh donut = new Donut(-cubeHalf, -cubeHalf, -cubeHalf - 1000, cubeSize, cubeSize / 8, cubeSize, new Color(0, 128, 0), new Vector(0, 0, 0));
		
		meshes = new Mesh[]{cube, pyramid, arrow, donut};
		
		colors = new Color[meshes.length];
		for(int i = 0; i < meshes.length; i++) {colors[i] = Color.WHITE;}
	}
	
	public void update() {
		if(mouseState == CAMERA) {
			Vector mouseDelta = getMouseDelta();
				
			if(!controlKey && !shiftKey) {cam.rotateAroundAnchor(new Vector(-mouseDelta.y * MOUSE_SPEED, mouseDelta.x * MOUSE_SPEED, 0));}
			if( controlKey && !shiftKey) {cam.zoom(-mouseDelta.y * ZOOM_SPEED);}
			if(!controlKey &&  shiftKey) {cam.translateRelative(new Vector(mouseDelta.x * PAN_SPEED, mouseDelta.y * PAN_SPEED, 0), new Vector(1, 1, 0));}
			//if(!controlKey &&  shiftKey &&  altKey) {cam.translateRelative(new Vector(mouseDelta.x * PAN_SPEED, mouseDelta.y * PAN_SPEED, 0), new Vector(0, 1, 0));}
		}
		
		if(leftButton && gizmoSelection != -1) {
			Vector mouseDelta = getMouseDelta();
			Vector gizmoAxis = rotatePointAroundPoint(new Vector(mouseDelta.x, 0, mouseDelta.y), new Vector(0, 0, 0), new Vector(0, cam.angle.y, 0));
			
			if(gizmoState == TRANSLATING) {
				if(gizmoSelection == 0) {meshes[selection].translate(new Vector(gizmoAxis.x * TRANSLATE_SPEED, 0, 0));}
				if(gizmoSelection == 1) {meshes[selection].translate(new Vector(0, mouseDelta.y * TRANSLATE_SPEED, 0));}
				if(gizmoSelection == 2) {meshes[selection].translate(new Vector(0, 0, gizmoAxis.z * TRANSLATE_SPEED));}
			}
			if(gizmoState == ROTATING) {
				if(gizmoSelection == 0) {meshes[selection].rotate(new Vector(gizmoAxis.z * ROTATE_SPEED, 0, 0));}
				if(gizmoSelection == 1) {meshes[selection].rotate(new Vector(0, mouseDelta.x * ROTATE_SPEED, 0));}
				if(gizmoSelection == 2) {meshes[selection].rotate(new Vector(0, 0, -gizmoAxis.x * ROTATE_SPEED));}
			}
			
			mouseState = GIZMO;
			
			centerMouse();
		}
		else {
			if(checkMouseInWindow()) {
				double mx = getMousePos().x;
				double my = getMousePos().y;
				
				if(mx >= VIEWPORT_X && mx < VIEWPORT_X + VIEWPORT_W && my >= VIEWPORT_Y && my < VIEWPORT_Y + VIEWPORT_H) {
					if(middleButton && !leftButton || middleButton && mouseState == CAMERA) {
						if(getMouseVisible()) {
							setMouseVisible(false);
							oldMousePos = getMousePos();
							mouseState = CAMERA;
						}
						centerMouse();
					}
					else if(leftButton && !middleButton && mouseState != CAMERA) {
						if(selection != -1) {
							if(gizmoState == TRANSLATING) {
								Mesh[] gizmoMeshes = cam.getTranslationGizmoMeshes(meshes[selection].getPosition());
								gizmoSelection = cam.selectMesh(getMouseDelta(), gizmoMeshes);
							}
							if(gizmoState == ROTATING) {
								Mesh[] gizmoMeshes = cam.getRotationGizmoMeshes(meshes[selection].getPosition());
								gizmoSelection = cam.selectMesh(getMouseDelta(), gizmoMeshes);
							}
							
							if(gizmoSelection != -1) {
								if(getMouseVisible()) {
									setMouseVisible(false);
									oldMousePos = getMousePos();
									mouseState = GIZMO;
								}
								centerMouse();
							}
						}
						else {gizmoSelection = -1;}
						
						if(gizmoSelection == -1) {
							leftButton = false;
							
							selection = cam.selectMesh(getMouseDelta(), meshes);
							colors = new Color[meshes.length];
							
							for(int i = 0; i < meshes.length; i++) {colors[i] = Color.WHITE;}
							if(selection != -1) {colors[selection] = Color.YELLOW;}
						}
					}
					else {
						if(!getMouseVisible()) {
							gizmoSelection = -1;
							setMousePos(oldMousePos);
							mouseState = DEFAULT;
							setMouseVisible(true);
						}
					}
				}
			}
			else {
				if(!getMouseVisible()) {
					gizmoSelection = -1;
					setMousePos(oldMousePos);
					mouseState = DEFAULT;
					setMouseVisible(true);
				}
			}
		}
	}
	
	public void draw(Graphics2D graphics) {
		graphics.setColor(Color.DARK_GRAY);
		graphics.fillRect(0, 0, Loader.WIDTH, Loader.HEIGHT);
		
		if(displayMode == WIREFRAME) {cam.drawWireframe(meshes, colors, graphics);}
		if(displayMode == FLAT) {cam.drawFlat(meshes, colors, graphics);}
		if(displayMode == FLAT_WIRE) {cam.drawFlatWire(meshes, colors, graphics);}
		
		if(mouseState == CAMERA) {cam.drawAnchor(graphics);}
		
		if(selection != -1) {
			if(gizmoState == TRANSLATING) {cam.drawTranslationGizmo(meshes[selection].getPosition(), graphics);}
			if(gizmoState == ROTATING) {cam.drawRotationGizmo(meshes[selection].getPosition(), graphics);}
		}
		
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fillRect(0, 0, VIEWPORT_X, Loader.HEIGHT);
		
		drawDebugString("Camera Position = " + getVectorString(cam.position), 1, graphics);
		drawDebugString("Anchor Position = " + getVectorString(cam.anchor), 2, graphics);
		drawDebugString("Camera Angle = " + getVectorString(cam.angle), 3, graphics);
		
		String gizmoStateString = " ";
		
		if(gizmoState == DEFAULT) {gizmoStateString = "Selection";}
		if(gizmoState == TRANSLATING) {gizmoStateString = "Translation";}
		if(gizmoState == ROTATING) {gizmoStateString = "Rotation";}
		if(gizmoState == SCALING) {gizmoStateString = "Scale";}
		
		drawDebugString("Gizmo = " + gizmoStateString, 5, graphics);
		
		drawDebugString("Mouse Controls:",                           26, graphics);
		drawDebugString("Left Button = Select Object/Operate Gizmo", 27, graphics);
		drawDebugString("Middle Button = Rotate View",               28, graphics);
		drawDebugString("Middle Button + Shift = Pan View",          29, graphics);
		drawDebugString("Middle Button + Control = Zoom View",       30, graphics);
		drawDebugString("Scroll Wheel = Zoom View",                  31, graphics);
		
		drawDebugString("Keyboard Shortcuts:",                       33, graphics);
		drawDebugString("A = Select",                                34, graphics);
		drawDebugString("G = Translate",                             35, graphics);
		drawDebugString("R = Rotate",                                36, graphics);
		drawDebugString("S = Scale",                                 37, graphics);
		
		drawDebugString("1 = Flat View",                             39, graphics);
		drawDebugString("2 = Flat + Wireframe View",                 40, graphics);
		drawDebugString("3 = Wireframe View",                        41, graphics);
		
		drawDebugString("Home = Reset View",                         43, graphics);
		drawDebugString("End = Center View on Selected Object",      44, graphics);
	}
	
	public void keyPressed(int key) {
		switch(key) {
			case(VK_CONTROL): {controlKey = true; break;}
			case(VK_SHIFT): {shiftKey = true; break;}
			case(VK_ALT): {altKey = true; break;}
			
			case(VK_1): {displayMode = FLAT; break;}
			case(VK_2): {displayMode = FLAT_WIRE; break;}
			case(VK_3): {displayMode = WIREFRAME; break;}
			//case(VK_4): {displayMode = SHADED; break;}
			
			case(VK_ESCAPE): {System.exit(0); break;}
		}
		
		if(mouseState == DEFAULT) {
			switch(key) {
				case(VK_A): {gizmoState = DEFAULT; break;}
				case(VK_R): {gizmoState = ROTATING; break;}
				case(VK_G): {gizmoState = TRANSLATING; break;}
				//case(VK_S): {displayMode = WIREFRAME; break;}
				
				case(VK_HOME): {resetView(); break;}
				case(VK_END): {
					if(selection != -1) {
						cam.anchor = meshes[selection].getPosition();
						cam.rotateAroundAnchor(new Vector(0, 0, 0));
					}
					break;
				}
			}
		}
	}
	public void keyReleased(int key) {
		switch(key) {
			case(VK_CONTROL): {controlKey = false; break;}
			case(VK_SHIFT): {shiftKey = false; break;}
			case(VK_ALT): {altKey = false; break;}
		}
	}

	public void mouseClicked(MouseEvent mouse) {}
	public void mouseEntered(MouseEvent mouse) {mouseInWindow = true;}
	public void mouseExited(MouseEvent mouse) {mouseInWindow = false;}
	public void mousePressed(MouseEvent mouse) {
		switch(mouse.getButton()) {
			case(MouseEvent.BUTTON1): {leftButton = true; break;}
			case(MouseEvent.BUTTON2): {middleButton = true; break;}
			//case(MouseEvent.BUTTON3): {rightButton = true; break;}
		}
	}
	public void mouseReleased(MouseEvent mouse) {
		switch(mouse.getButton()) {
			case(MouseEvent.BUTTON1): {leftButton = false; break;}
			case(MouseEvent.BUTTON2): {middleButton = false; break;}
			//case(MouseEvent.BUTTON3): {rightButton = false; break;}
		}
	}
	
	public void mouseWheelMoved(MouseWheelEvent wheel) {if(mouseState == DEFAULT) {cam.zoom(wheel.getWheelRotation() * SCROLL_SPEED);}}
	
	private void setMousePos(Vector pos) {
		Point windowPos = Loader.frame.getLocationOnScreen();
		
		Point p = new Point((int)pos.x + windowPos.x + 8, (int)pos.y + windowPos.y + 32);
		
		moveMouse(p);
	}
	private Vector getMousePos() {
		Point windowPos = Loader.frame.getLocationOnScreen();
		
		double mouseDeltaX = MouseInfo.getPointerInfo().getLocation().x - windowPos.x;
		double mouseDeltaY = MouseInfo.getPointerInfo().getLocation().y - windowPos.y;
		
		return(new Vector(mouseDeltaX - 8, mouseDeltaY - 32, 0));
	}
	private Vector getMouseDelta() {
		Point windowPos = Loader.frame.getLocationOnScreen();
		Point centerPos = new Point(windowPos.x + Loader.WIDTH / 2, windowPos.y + Loader.HEIGHT / 2);
		
		double mouseDeltaX = MouseInfo.getPointerInfo().getLocation().x - centerPos.x;
		double mouseDeltaY = MouseInfo.getPointerInfo().getLocation().y - centerPos.y;
		
		return(new Vector(mouseDeltaX - 8 - VIEWPORT_X / 2, -mouseDeltaY + 32 - VIEWPORT_Y / 2, 0));
	}
	private void setMouseVisible(boolean visible) {
		if(visible) {Loader.frame.getContentPane().setCursor(Cursor.getDefaultCursor());}
		else {
			BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
			Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
			Loader.frame.getContentPane().setCursor(blankCursor);
		}
	}
	private boolean getMouseVisible() {return(Loader.frame.getContentPane().getCursor() == Cursor.getDefaultCursor());}
	
	private void moveMouse(Point p) {
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice[] gs = ge.getScreenDevices();

	    for(GraphicsDevice device: gs) { 
	        GraphicsConfiguration[] configurations = device.getConfigurations();
	        
	        for(GraphicsConfiguration config: configurations) {
	            Rectangle bounds = config.getBounds();
	           
	            if(bounds.contains(p)) {
	                Point b = bounds.getLocation(); 
	                Point s = new Point(p.x - b.x, p.y - b.y);

	                try {
	                    Robot r = new Robot(device);
	                    r.mouseMove(s.x, s.y);
	                }
	                catch (AWTException e) {e.printStackTrace();}

	                return;
	            }
	        }
	    }
	    
	    return;
	}
	
	private boolean checkMouseInWindow() {return(mouseInWindow && Loader.frame.isActive() && Loader.frame.isFocused());}
	
	private void centerMouse() {setMousePos(new Vector(VIEWPORT_X + VIEWPORT_W / 2, VIEWPORT_Y + VIEWPORT_H / 2, 0));}
	
	private String getVectorString(Vector in) {return("(" + (int)in.x + ", " + (int)in.y + ", " + (int)in.z + ")");}
	
	private void drawDebugString(String string, int row, Graphics2D graphics) {
		int fontHeight = graphics.getFontMetrics(graphics.getFont()).getHeight();
		
		graphics.setColor(Color.DARK_GRAY);
		graphics.drawString(string, fontHeight / 2, fontHeight * row);
	}
	
	private void resetView() {cam = new Camera(new Vector(0, 0, 0), 5000, new Vector(22.5, -45, 0), 90, VIEWPORT_X, VIEWPORT_Y, VIEWPORT_W, VIEWPORT_H);}
}