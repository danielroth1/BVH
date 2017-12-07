package gui;

import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import logic.GameControl;
import logic.WorldObject;

public class KeyControl {

	private GameControl gc;
	
	private GameGui gg;
	
	private JPanel mousePanel;
	
	private JFrame keyPanel;
	
	private WorldObject movingObject;
	
	private Set<Integer> keysPressed = new HashSet<Integer>();
	private Set<Character> keysTyped = new HashSet<Character>();
	
	private MouseEvent mousePressed = null;
	
	public KeyControl(GameControl gc, GameGui gg, JFrame keyPanel, JPanel mousePanel) {
		this.gc = gc;
		this.gg = gg;
		this.mousePanel = mousePanel;
		this.keyPanel = keyPanel;
		mousePanel.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				//KeyControl.this.gg.mouseButtonClicked(e);
				mousePressed = e;
			}
			
			
		});
		mousePanel.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				super.mouseDragged(e);
//				KeyControl.this.gg.mouseDraged(e);
				
			}
			
			
		});
		keyPanel.addKeyListener(new KeyAdapter() {

			
			
			@Override
			public void keyTyped(KeyEvent e) {
				super.keyTyped(e);
				keysTyped.add(e.getKeyChar());
			}

			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				keysPressed.add(e.getKeyCode());
			}

			@Override
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
				keysPressed.remove(e.getKeyCode());
			}
			
		});
		
	}
	
	public void sendKeyCommands(){
		double vX = 0.0;
		double vY = 0.0;
		double speed = 20.0;
		if (keysPressed.contains(KeyEvent.VK_LEFT)){
			vX -= speed;
			//System.out.println("move left");
		}
		if (keysPressed.contains(KeyEvent.VK_RIGHT)){
			//System.out.println("move right");
			vX += speed;
		}
		if (keysPressed.contains(KeyEvent.VK_UP)){
			//System.out.println("move up");
			vY += speed;
		}
		if (keysPressed.contains(KeyEvent.VK_DOWN)){
			//System.out.println("move down");
			vY -= speed;
		}
		if (keysTyped.contains('r')){
			gg.updateTree();
		}
		if (keysTyped.contains('d')){
			gg.switchDebug();
		}
		if (movingObject != null && (vX != 0.0 || vY != 0.0))
			gc.move(movingObject, vX, vY);
		if (keysTyped.contains('z')){
			gc.getWorld().recreateTree();
			gg.updateTree();
		}
		keysTyped.clear();
		//keysPressed.clear();
		
		if (mousePressed != null){
			if (SwingUtilities.isLeftMouseButton(mousePressed)){
				WorldObject testObject = gg.getStarObject(gg.getTransformedCoordinated(mousePressed.getPoint()));
				setMovingObject(testObject);
				gc.addObject(testObject);
			}
			if (SwingUtilities.isRightMouseButton(mousePressed)){
				if (getMovingObject() != null)
					gc.removeObject(getMovingObject());
				setMovingObject(null);
			}
			mousePressed = null;	
		}
	}
	
	
	
	public void setMovingObject(WorldObject movingObject){
		this.movingObject = movingObject;
	}

	public WorldObject getMovingObject() {
		return movingObject;
	}

	
}
