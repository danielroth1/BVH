package engine;

import gui.GameGui;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import logic.GameControl;
import logic.WorldObject;

public class GameEngine extends Thread{
	
	private GameControl gc;
	
	private GameGui gg;
	
	private World world;
	
	private boolean running = true;
	
	private int updatesPerSecond;
	
	private boolean showUpdatesPerSecond;
	
	private int currentUps;
	
	private Set<WorldObject> movingObjects = new HashSet<WorldObject>();
	
	private List<Move> moveQuerry = new LinkedList<Move>();
	
	private List<WorldObject> objectQuerry = new LinkedList<WorldObject>();
	
	private List<WorldObject> removeObjectQuerry = new LinkedList<WorldObject>();
	
	private List<WorldObject> removedObjects = new LinkedList<WorldObject>();
	
	private LinkedList<WorldObject> collisionObjects = new LinkedList<WorldObject>();
	
	private double inertia = 20;//10;
	
	public GameEngine(World world, int updatesPerSecond, GameControl gc, GameGui gg) {
		this.world = world;
		this.gc = gc;
		this.gg = gg;
		this.updatesPerSecond = updatesPerSecond;
		
	}
	
	private int collisionSize = 0;
	
	private synchronized void update(long timeStep){
		double h = timeStep/(double)100;
		for(Move m : moveQuerry){
			if (removedObjects.contains(m.getWorldObject()))
				continue;
			WorldObject o = m.getWorldObject();
			if (m.getVelocityX()>=0)
				o.setCurrentVelocityX(Math.min(o.getMaxSpeed(), o.getCurrentVelocityX()+m.getVelocityX()));
			else
				o.setCurrentVelocityX(Math.max(-o.getMaxSpeed(), o.getCurrentVelocityX()+m.getVelocityX()));
			if (m.getVelocityY()>=0)
				o.setCurrentVelocityY(Math.min(o.getMaxSpeed(), o.getCurrentVelocityY()+m.getVelocityY()));
			else
				o.setCurrentVelocityY(Math.max(-o.getMaxSpeed(), o.getCurrentVelocityY()+m.getVelocityY()));

//			o.setX(o.getX()+h*m.getVelocityX());
//			o.setY(o.getY()+h*m.getVelocityY());
			movingObjects.add(o);
		}
		moveQuerry.clear();
		
//		List<WorldObject> sortList = new LinkedList<WorldObject>(movingObjects);
//		Collections.sort(sortList);
		Object[] movingObjectsArray = movingObjects.toArray();
//		Object[] movingObjectsArray = sortList.toArray();
		for (int i = 0; i < movingObjectsArray.length; i++){
//		for (WorldObject o : sortList){
			WorldObject o = (WorldObject)movingObjectsArray[i];
			
			
			
			
			double vX = o.getCurrentVelocityX();
			double vY = o.getCurrentVelocityY();
			if (vX<0){
				vX = o.getCurrentVelocityX()+h*inertia;//*Math.abs(o.getVelocityX());
				if (vX>-0.5)
					vX = 0;
			}
			else{
				vX = o.getCurrentVelocityX()-h*inertia;//*o.getVelocityX();
				if (vX<0.5)
					vX = 0;
			}
			if (vY<0){
				vY = o.getCurrentVelocityY()+h*inertia;//*Math.abs(o.getVelocityY());
				if (vY>-0.5)
					vY = 0;
			}
			else{
				vY = o.getCurrentVelocityY()-h*inertia;//*o.getVelocityY();
				if (vY<0.5)
					vY = 0;
			}
			o.setCurrentVelocityX(vX);
			o.setCurrentVelocityY(vY);
			
			o.setX(o.getX()+o.getCurrentVelocityX()*h);
			o.setY(o.getY()+o.getCurrentVelocityY()*h);
			//o.resetAabb();
			o.updatePosition();
			//System.out.println("new position " + o.getX() + ", " + o.getY());
			
			if (o.getCurrentVelocityX() == 0.0 && o.getCurrentVelocityY() == 0.0)
				movingObjects.remove(o);
			
			
			//check if data structure is still consistent
			if (!o.isWithinAabb()){
				//it's not consistent
//				collisionObjects.add(o);
				try {
					removeDebug(o);
//					world.removeObject(o);
				} catch (NullPointerException e) {
					if (GameEngine.debug)
						debugShowAllTreeFrames();
					e.printStackTrace();
					throw new NullPointerException();
				}
				o.resetAabb();
				addDebug(o);
//				world.addObject(o);
				gg.updateTree();
			}
		}
//		if (collisionSize != collisionObjects.size()){
//			collisionSize = collisionObjects.size();
//			System.out.println(collisionObjects.size());
//		}
		/*
		Collections.sort(collisionObjects);
		
		for(WorldObject o : collisionObjects){
			world.removeObject(o);
			
		}
		for(WorldObject o : collisionObjects){
			o.resetAabb();
			world.addObject(o);
		}
		collisionObjects.clear();
		*/
		gg.updateTree();
		
//		if (!collisionObjects.isEmpty()){
//			world.removeObject(collisionObjects.getFirst());
//			collisionObjects.getFirst().resetAabb();
//			world.addObject(collisionObjects.getFirst());
//			collisionObjects.removeFirst();
//		}
//		else{
		for(WorldObject o : objectQuerry){
//			world.addObject(o);
			addDebug(o);
		}
		objectQuerry.clear();
//		}
		removedObjects.clear();
		for(WorldObject o : removeObjectQuerry){
			try {
				removeDebug(o);
//				world.removeObject(o);
			} catch (NullPointerException e) {
				if (GameEngine.debug)
					debugShowAllTreeFrames();
				e.printStackTrace();
				throw new NullPointerException();
			}
			movingObjects.remove(o);
			removedObjects.add(o);
//			Move move = null;
//			for (Move m : moveQuerry){
//				if (m.getWorldObject()== o)
//					move = m;
//			}
//			if (move != null)
//				moveQuerry.remove(move);
		}
		removeObjectQuerry.clear();
	}
	
	public void move(WorldObject o, double velocityX, double velocityY){
		if (velocityX>=0)
			velocityX = Math.min(velocityX, o.getMaxVelocityX());
		else
			velocityX = Math.max(velocityX, -o.getMaxVelocityX());
		if (velocityY>=0)
			velocityY = Math.min(velocityY, o.getMaxVelocityY());
		else
			velocityY = Math.max(velocityY, -o.getMaxVelocityY());
			
		moveQuerry.add(new Move(o, velocityX, velocityY));
		
	}
	
	public static boolean debug = false;
	
	private int debugNumberOfSaves = 10;
	
	private World[] debugWorld = new World[debugNumberOfSaves];
	
	private int debugCounter = 0;
	
	
	private void removeDebug(WorldObject wo){
		if (debug){
			debugWorld[debugCounter] = world.getCopy();
			debugCounter = (debugCounter+1)%debugNumberOfSaves;
		}
		world.removeObject(wo);
	}
	
	private void addDebug(WorldObject wo){
		if (debug){
			debugWorld[debugCounter] = world.getCopy();
			debugCounter = (debugCounter+1)%debugNumberOfSaves;
		}
		world.addObject(wo);
	}
	
	public void debugShowAllTreeFrames(){
		int counter = 1;
		for (int i = debugCounter; i < debugNumberOfSaves; i++){
			if (debugWorld[i] != null){
				gg.initiateTreeFrame(debugWorld[i], counter);
				System.out.println("debugWorld[" + i + "] is shown");
			}
			counter++;
		}
		for (int i = 1; i < debugCounter; i++){
			if (debugWorld[i] != null){
				gg.initiateTreeFrame(debugWorld[i], counter);
				System.out.println("debugWorld[" + i + "] is shown");
			}
			counter++;
		}
	}
	
	private int number = 0;
	
	public boolean addObject(WorldObject wo){
		number++;
		wo.number = number;
		return objectQuerry.add(wo);
	}
	
	public boolean removeObject(WorldObject wo){
		return removeObjectQuerry.add(wo);
	}
	
	@Override
	public void run(){
		while(true){
			if (running){
				long t0 = System.currentTimeMillis();
				long t1 = System.currentTimeMillis();
				double intervallTime = 1000/(double)updatesPerSecond;
				long upsCounterTimer = System.currentTimeMillis();
				int upsCounter = 0;
				while(true){
					t0 = System.currentTimeMillis();
					
					update(System.currentTimeMillis()-t1);
					gg.sendKeyCommands();
					t1 = System.currentTimeMillis();
					upsCounter++;
					if (System.currentTimeMillis()-upsCounterTimer >= 1000){
						if (showUpdatesPerSecond)
							System.out.println("ups: " + upsCounter);
						currentUps = upsCounter;
						upsCounter = 0;
						upsCounterTimer = System.currentTimeMillis();
					}
					
					long sleepingTime = (long)(intervallTime - (System.currentTimeMillis() - t0));
					if (sleepingTime < 0){
						//rendering took longer than upsIntervall -> game is slowing down
						
					}
					else{
						try {
							Thread.sleep(sleepingTime);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	
	

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public int getUpdatesPerSecond() {
		return updatesPerSecond;
	}

	public boolean isShowUpdatesPerSecond() {
		return showUpdatesPerSecond;
	}

	public int getCurrentUps() {
		return currentUps;
	}

	public void setUpdatesPerSecond(int updatesPerSecond) {
		this.updatesPerSecond = updatesPerSecond;
	}

	public void setShowUpdatesPerSecond(boolean showUpdatesPerSecond) {
		this.showUpdatesPerSecond = showUpdatesPerSecond;
	}

	public void setCurrentUps(int currentUps) {
		this.currentUps = currentUps;
	}
	
	

}
