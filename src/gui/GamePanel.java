package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import javax.swing.JPanel;

import logic.GameControl;
import logic.WorldNode;
import logic.WorldObject;
import engine.GameEngine;
import engine.World;

public class GamePanel extends JPanel{

	private GameGui gg;
	
	private GameControl gc;
	
	
	public GamePanel(GameGui gg, GameControl gc) {
		this.gg = gg;
		this.gc = gc;
		
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		World w = gc.getWorld();
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(1, -1);
//		g2.rotate(Math.PI);
		g2.translate(0, -getHeight());
		renderWorldNode(g2, w.getRoot());
		
		
//		g.drawRect(500, 500, 100, 100);
	}
	
//	public Polygon transformPolygon(WorldObject p){
//		int[] xn = new int[p.npoints];
//		int[] yn = new int[p.npoints];
//		for (int i = 0; i < p.npoints; i++){
//			xn[i] = p.xpoints[i] + (int)p.getX();
//			yn[i] = p.ypoints[i] + (int)p.getY();
//		}
//		return new Polygon(xn, yn, p.npoints);
//	}
	
	private void renderWorldNode(Graphics2D g, WorldNode wn){
		//Graphics2D g = (Graphics2D) g2;
		
//		g.translate(0,100);
		//render aabbs
		if (GameEngine.debug && wn.getAabb() != null){
			g.setColor(Color.GREEN);
			g.drawRect((int)wn.getAabb().getX1(), (int)wn.getAabb().getY1(), (int)wn.getAabb().getLengthX(), (int)wn.getAabb().getLengthY());
			//render centerlines
			/*
			g.setColor(Color.RED);
			int x1, y1, x2, y2;
			if (wn.getAabb().getLengthX()>wn.getAabb().getLengthY()){
				//x-axis
				x1 = wn.getAabb().getCenterX();
				x2 = x1;
				y1 = wn.getAabb().getY1();
				y2 = wn.getAabb().getY2();
			}
			else{
				//y-axis
				y1 = wn.getAabb().getCenterY();
				y2 = y1;
				x1 = wn.getAabb().getX1();
				x2 = wn.getAabb().getX2();
			}
			g.drawLine(x1, y1, x2, y2);
			*/
			g.setColor(Color.RED);
			double x1, y1, x2, y2;
			if (wn.getCenterOrientation() == WorldNode.CENTER_X){
				x1 = (int)wn.getCenter();
				x2 = x1;
				y1 = wn.getAabb().getY1();
				y2 = wn.getAabb().getY2();
			}
			else{
				y1 = (int)wn.getCenter();
				y2 = y1;
				x1 = wn.getAabb().getX1();
				x2 = wn.getAabb().getX2();
			}
			g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
			
		}
		
		
		//render world
		if (wn.getLeft() != null){
			renderWorldNode(g, wn.getLeft());
		}
		if (wn.getRight() != null){
			renderWorldNode(g, wn.getRight());
		}
		if (wn.getWorldObject() != null){
			//Polygon p = transformPolygon(wn.getWorldObject());
			g.setColor(wn.getWorldObject().getColor());
			g.draw(wn.getWorldObject());
			if (GameEngine.debug){
				g.setColor(Color.RED);
				g.drawString(String.valueOf(wn.getWorldObject().number), (int)wn.getAabb().getCenterX(), (int)wn.getAabb().getCenterY());
			}
		}
		
		
	}
	
	

}
