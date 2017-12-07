package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import logic.GameControl;
import logic.WorldObject;
import treeNodeGui.TreeNodePanel;
import engine.GameEngine;
import engine.World;

public class GameGui {

	private GameControl gc;
	
	private JFrame mainFrame;
	
	private JDialog treeFrame;
	
	private TreeNodePanel<WorldObject> treePanel;
	
	private GamePanel gamePanel;
	
	private KeyControl keyControl;
	
	public GameGui(GameControl gc) {
		this.gc = gc;
		mainFrame = new JFrame("Blocks");
		gamePanel = new GamePanel(this, gc);
		gamePanel.setBackground(Color.white);
		keyControl = new KeyControl(gc, this, mainFrame, gamePanel);
		JScrollPane scroll = new JScrollPane(gamePanel);
		mainFrame.setContentPane(scroll);
		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(1024, 768);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
		
		//initiateTreePanel
//		treeFrame = new JDialog();
//		treeFrame.setSize(new Dimension(300, 768));
//		treeFrame.setLocationRelativeTo(mainFrame);
//		treeFrame.setLocation(new Point(treeFrame.getLocation().x+mainFrame.getSize().width/2+treeFrame.getSize().width/2, treeFrame.getLocation().y));
//		
//		treePanel = new TreeNodePanel<WorldObject>(gc.getWorld().getRoot());
//		JScrollPane scroll = new JScrollPane(treePanel);
//		treeFrame.add(scroll);
//		treeFrame.setVisible(true);
		
		initiateTreeFrameFirst(gc.getWorld(), 0);
		mainFrame.setVisible(false);
		mainFrame.setVisible(true);
		initiateThread();
		
		
		
	}
	
	public Point getTransformedCoordinated(Point p){
		return new Point(p.x, gamePanel.getHeight()-p.y);
		
	}
	
	public void switchDebug(){
		if (GameEngine.debug){
			hideDebug();
		}
		else{
			showDebug();
		}
	}
	
	public void showDebug(){
		GameEngine.debug = true;
		treeFrame.setVisible(true);
		mainFrame.setVisible(true);
		updateTree();
	}
	
	public void hideDebug(){
		GameEngine.debug = false;
		treeFrame.setVisible(false);
	}
	
	private void initiateTreeFrameFirst(World w, int number){
		treeFrame = new JDialog();
		treeFrame.setTitle("no. " + number);
		treeFrame.setSize(new Dimension(300, 768));
		if (number <= 0){
			treeFrame.setLocationRelativeTo(mainFrame);
			treeFrame.setLocation(new Point(treeFrame.getLocation().x+mainFrame.getSize().width/2+treeFrame.getSize().width/2, treeFrame.getLocation().y));
		}
		else{
			treeFrame.setLocation(new Point((int)((number-1)*200), 0));
		}
		
		treePanel = new TreeNodePanel<WorldObject>(w.getRoot());
		JScrollPane scroll = new JScrollPane(treePanel);
		treeFrame.add(scroll);
	}
	
	public void initiateTreeFrame(World w, int number){
		treeFrame = new JDialog();
		treeFrame.setTitle("no. " + number);
		treeFrame.setSize(new Dimension(300, 768));
		if (number <= 0){
			treeFrame.setLocationRelativeTo(mainFrame);
			treeFrame.setLocation(new Point(treeFrame.getLocation().x+mainFrame.getSize().width/2+treeFrame.getSize().width/2, treeFrame.getLocation().y));
		}
		else{
			treeFrame.setLocation(new Point((int)((number-1)*200), 0));
		}
		
		treePanel = new TreeNodePanel<WorldObject>(w.getRoot());
		JScrollPane scroll = new JScrollPane(treePanel);
		treeFrame.add(scroll);
		treeFrame.setVisible(true);
	}
	
	/**
	 * renderThread
	 */
	private void initiateThread(){
		RenderThread thread = new RenderThread(this, 60);
		thread.setShowFps(false);
		thread.start();
	}
	
	public synchronized void sendKeyCommands(){
		keyControl.sendKeyCommands();
	}
	
	public WorldObject getTetsObject(Point p){
		double[] xn = {0, 100, 100, 0};
		double[] yn = {0, 0, 100, 100};
		Random r = new Random();
		//new Color((float)(r.nextFloat()/2+0.5), (float)(r.nextFloat()/2+0.5), (float)(r.nextFloat()/2+0.5))
		return new WorldObject(xn, yn, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat()) , p.x, p.y);
	}
	
	public WorldObject getStarObject(Point p){
		double[] xn = {0, 40, 50, 60, 100, 70, 80, 50, 20, 30};
		double[] yn = {60, 60, 100, 60, 60, 40, 0, 30, 0, 40};
		Random r = new Random();
		//new Color((float)(r.nextFloat()/2+0.5), (float)(r.nextFloat()/2+0.5), (float)(r.nextFloat()/2+0.5))
		return new WorldObject(xn, yn, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat()) , p.x, p.y);
	}
	
	public void render(){
		//System.out.println("performe render");
		gamePanel.repaint();
		if (GameEngine.debug)
			treePanel.repaint();
	}
	
	public void updateTree(){
		if (GameEngine.debug){
			treePanel.updateTreeNode(gc.getWorld().getRoot());
			treePanel.repaint();
		}
	}

}
