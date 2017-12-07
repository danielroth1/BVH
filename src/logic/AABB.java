package logic;

import java.awt.Graphics;

public class AABB {

	private double x1;
	
	private double y1;
	
	private double x2;
	
	private double y2;
	
	
	
	public AABB(double x1, double y1, double x2, double y2) {
		super();
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public void updateAABB(AABB aabb){
		x1 = Math.min(aabb.getX1(), x1);
		y1 = Math.min(aabb.getY1(), y1);
		x2 = Math.max(aabb.getX2(), x2);
		y2 = Math.max(aabb.getY2(), y2);
	}
	
	public void clear(){
		x1 = 0;
		x2 = 0;
		y1 = 0;
		y2 = 0;
	}
	
	
	public boolean isInside(double x, double y){
		return x >= x1
				&& y >= y1
				&& x <= x2
				&& y <= y2;
	}
	
	public boolean isInside(int x, int y){
		return x >= x1
				&& y >= y1
				&& x <= x2
				&& y <= y2;
	}
	
	public AABB copy(){
		return new AABB(x1, y1, x2, y2);
	}
	
	public void copyValues(double x1, double y1, double x2, double y2){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public void copyValues(AABB aabb){
		copyValues(aabb.getX1(), aabb.getY1(), aabb.getX2(), aabb.getY2());
	}

	public double getLengthX(){
		return x2-x1;
	}
	
	public double getLengthY(){
		return y2-y1;
	}
	
	public double getCenterX(){
		return (x1 + ((double)Math.abs(x2-x1))/2);
	}
	
	public double getCenterY(){
		return (y1 + ((double)Math.abs(y2-y1))/2);
	}
	
	public void draw(Graphics g){
		g.drawRect((int)x1, (int)y1, (int)(x2-x1), (int)(y2-y1));
	}

	@Override
	public String toString(){
		return "x1 = " + x1 + ", y1 = " + y1 + ", x2 = " + x2 + ", y2 = " + y2;
	}


	public double getX1() {
		return x1;
	}



	public double getY1() {
		return y1;
	}



	public double getX2() {
		return x2;
	}



	public double getY2() {
		return y2;
	}



	public void setX1(double x1) {
		this.x1 = x1;
	}



	public void setY1(double y1) {
		this.y1 = y1;
	}



	public void setX2(double x2) {
		this.x2 = x2;
	}



	public void setY2(double y2) {
		this.y2 = y2;
	}

	

}
