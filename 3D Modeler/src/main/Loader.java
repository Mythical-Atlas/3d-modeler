package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import datatypes.State;
import states.MainState;

@SuppressWarnings("serial")
public class Loader extends JPanel implements MouseListener, KeyListener, Runnable, MouseWheelListener {
	private Thread thread;
	public static JFrame frame;
	private static State currentState;
	
	public static boolean fullscreen = false;
	
	public static final String TITLE = "3D Modeler";
	public static final int TARGET_FPS = 60;
	public static final int SCALE = 1;
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	
	public static void main(String[] args) {
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		
		frame = new JFrame(TITLE);
		
		frame.setBounds(center.x - WIDTH * SCALE / 2, center.y - HEIGHT * SCALE / 2, WIDTH * SCALE, HEIGHT * SCALE);
		frame.setContentPane(new Loader());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}

	public Loader() {
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();
	}

	public void addNotify() {
		super.addNotify();
		
		addKeyListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
		
		thread = new Thread(this);
		thread.start();
	}

	@SuppressWarnings("static-access")
	public void run() {
		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = (Graphics2D)image.getGraphics();
		
		currentState = new MainState();
		
		while(true) {
			long start = System.nanoTime();
			
			currentState.update();	
			currentState.draw(graphics);
			
			Graphics graphicsTemp = getGraphics();
			
			graphicsTemp.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
			graphicsTemp.dispose();
			
			long wait = 1000 / TARGET_FPS - (System.nanoTime() - start) / 1000000;
			if(wait <= 0) {wait = 0;}
			
			try {thread.sleep(wait);}
			catch(Exception e) {e.printStackTrace();}
		}
	}

	public void keyTyped(KeyEvent key) {}
	public void keyPressed(KeyEvent key) {currentState.keyPressed(key.getKeyCode());}
	public void keyReleased(KeyEvent key) {currentState.keyReleased(key.getKeyCode());}
	
	public void mouseClicked(MouseEvent mouse) {currentState.mouseClicked(mouse);}
	public void mouseEntered(MouseEvent mouse) {currentState.mouseEntered(mouse);}
	public void mouseExited(MouseEvent mouse) {currentState.mouseExited(mouse);}
	public void mousePressed(MouseEvent mouse) {currentState.mousePressed(mouse);}
	public void mouseReleased(MouseEvent mouse) {currentState.mouseReleased(mouse);}
	
	public void mouseWheelMoved(MouseWheelEvent wheel) {currentState.mouseWheelMoved(wheel);}
}