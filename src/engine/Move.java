package engine;

import logic.WorldObject;

public class Move {
	
	
	private WorldObject worldObject;
	
	private double velocityX;
	
	private double velocityY;

	public Move(WorldObject worldObject, double velocityX, double velocityY) {
		super();
		this.worldObject = worldObject;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
	}

	public WorldObject getWorldObject() {
		return worldObject;
	}

	public double getVelocityX() {
		return velocityX;
	}

	public double getVelocityY() {
		return velocityY;
	}

	public void setWorldObject(WorldObject worldObject) {
		this.worldObject = worldObject;
	}

	public void setVelocityX(double velocityX) {
		this.velocityX = velocityX;
	}

	public void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
	}
	
	
	
}
