package logic;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;

public class WorldObject extends Path2D.Double implements Comparable<WorldObject>{
	
	private AABB aabb;
	
	private Color color = Color.black;
	
	private double x;
	
	private double y;
	
	private double oldX;
	
	private double oldY;
	
	private double currentVelocityX = 0;
	
	private double currentVelocityY = 0;
	
	//Object specific constant that describes the force when accelerated
	private double maxVelocityX = 20;
	private double maxVelocityY = 20;
	
	private double maxSpeed = 60;
	
	private int aabbOffset = 20;
	
	public int number = 0;
	
	
	public WorldObject(Shape s, Color color, double x, double y) {
		super(s);
		
		transform(new AffineTransform(1, 0, 0, 1, x, y));
		oldX = x;
		oldY = y;
		
		this.color = color;
		resetAabb();
		
		this.x = x;
		this.y = y;
	}
	
	public WorldObject(double[] xn, double[] yn, Color color) {
		this(xn, yn, color, 0, 0);
		x = 0;
		y = 0;
	}
	
	public WorldObject(double[] xn, double[] yn, Color color, double x, double y) {
		moveTo(xn[0],yn[0]);
		for (int i = 1; i < xn.length; i++){
			lineTo(xn[i], yn[i]);
			
		}
		closePath();
		transform(new AffineTransform(1, 0, 0, 1, x, y));
		oldX = x;
		oldY = y;

		this.color = color;
		resetAabb();
		
		this.x = x;
		this.y = y;
	}
	
	
	public void updatePosition(){
		transform(new AffineTransform(1, 0, 0, 1, -oldX, -oldY));
		transform(new AffineTransform(1, 0, 0, 1, x, y));
		oldX = x;
		oldY = y;
	}
	
	public void resetAabb(){
		aabb = getNewAabb(aabbOffset);
		//System.out.println("reset aabb to " + "x1 = " + x1 + ", y1 = " + y1 + ", x2 = " + x2 + ", y2 = " + y2);
	}
	
	public AABB getNewAabb(double offset){
		Rectangle r = getBounds();
		return new AABB(r.getX()-offset, r.getY()-offset, r.getX()+r.getWidth()+offset, r.getY()+r.getHeight()+offset);
	}
	
	public boolean isWithinAabb(){
		PathIterator pi = getPathIterator(null);
		double[] value = new double[6];
		int type;
		while (!pi.isDone()){
			type = pi.currentSegment(value);
			if (type == PathIterator.SEG_LINETO){
				if (!aabb.isInside(value[0], value[1]))
					return false;
			}
			pi.next();
		}
		return true;
	}
	
	
	@Override
	public String toString(){
		return "WorldObject: " + aabb.toString();
	}
	

	public AABB getAabb() {
		return aabb;
	}

	public void setAabb(AABB aabb) {
		this.aabb = aabb;
	}



	public Color getColor() {
		return color;
	}



	public double getX() {
		return x;
	}



	public double getY() {
		return y;
	}



	public void setColor(Color color) {
		this.color = color;
	}



	public void setX(double x) {
		this.x = x;
	}



	public void setY(double y) {
		this.y = y;
	}

	public double getCurrentVelocityX() {
		return currentVelocityX;
	}

	public double getCurrentVelocityY() {
		return currentVelocityY;
	}

	public void setCurrentVelocityX(double currentVelocityX) {
		this.currentVelocityX = currentVelocityX;
	}

	public void setCurrentVelocityY(double currentVelocityY) {
		this.currentVelocityY = currentVelocityY;
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	@Override
	public int compareTo(WorldObject wo) {
		return number-wo.number;
	}
	
	
	public WorldObject cloneWorldObject(){
		WorldObject wo = new WorldObject(this, color, x, y);
		wo.number = number;
		return wo;
	}

	public double getMaxVelocityX() {
		return maxVelocityX;
	}

	public double getMaxVelocityY() {
		return maxVelocityY;
	}

	public void setMaxVelocityX(double maxVelocityX) {
		this.maxVelocityX = maxVelocityX;
	}

	public void setMaxVelocityY(double maxVelocityY) {
		this.maxVelocityY = maxVelocityY;
	}
	
	
	


}
