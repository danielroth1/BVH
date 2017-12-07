package logic;

import gui.GameGui;
import engine.*;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;

public class GameControl {

	private GameGui gg;
	
	private World world;
	
	private GameEngine ge;
	
	public GameControl() {
		this.world = new World();
		gg = new GameGui(this);
		ge = new GameEngine(world, 25, this, gg);
		ge.setShowUpdatesPerSecond(false);
		ge.start();
	}
	
	public void startNewWorld(){
		this.world = new World();
		ge.setWorld(world);
	}
	
	
	
	
	public boolean addObject(WorldObject wo) {
		
		boolean b = ge.addObject(wo);
		gg.updateTree();
		return b;
	}
	
	public boolean removeObject(WorldObject wo){
		boolean b = ge.removeObject(wo);
		gg.updateTree();
		return b;
	}
	
	

	public static void main(String[] args){
		GameControl gc = new GameControl();
//		int[] xn = {100, 200, 200, 100};
//		int[] yn = {100, 100, 200, 200};
//		
//		gc.addObject(new WorldObject(xn, yn, 4, Color.BLUE));
	}

	public World getWorld() {
		return world;
	}

	public void move(WorldObject o, double velocityX, double velocityY) {
		ge.move(o, velocityX, velocityY);
	}
	
	
	
}
