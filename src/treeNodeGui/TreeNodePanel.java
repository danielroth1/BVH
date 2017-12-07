package treeNodeGui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import logic.WorldNode;
import logic.WorldObject;
import treeNodeLogic.NodeInterface;

public class TreeNodePanel<T> extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5514497159499728068L;

	private NodeInterface<T> root;
	
	private Map<NodeInterface<T>, Point> positions = new HashMap<NodeInterface<T>, Point>();
	
	private int distanceY;
	
	private int minDistanceX;
	
	private int prefferredDistanceX;
	
	private int maxDistanceX;
	
	private int nodeWidth;
	
	private int panelWidthMin = 0;
	
	private int panelWidthMax = 0;
	
	private int maxDepth = 0;
	
	private Color fillColor = Color.DARK_GRAY;
	
	private int offsetX;
	
	private int offsetY;
	
	public TreeNodePanel(NodeInterface<T> root){
		this.root = root;
		minDistanceX = 45;
		prefferredDistanceX = 25;
		maxDistanceX = 50;
		distanceY = 50;
		nodeWidth = 20;
		offsetX = 60;
		offsetY = 60;
		
		updateTreeNode(root);
	}
	
	public void updateTreeNode(NodeInterface<T> root){
		this.root = root;
		panelWidthMin = 0;
		panelWidthMax = 0;
		maxDepth = root.getMaxDepth();
		positions = new HashMap<NodeInterface<T>, Point>();
		positions.put(root, new Point(0, 0));
		updateTreeNode(root, 1, 0);
		pack();
	}
	
	
	
	
	private void updateTreeNode(NodeInterface<T> node, int depth, int mid){
		int distance = (int)Math.pow(node.getChildren().size(), maxDepth-depth)*Math.max(minDistanceX, Math.min(prefferredDistanceX, maxDistanceX));
		double unit = distance/(double)node.getChildren().size();
		int positionCounter = 0;
		for(NodeInterface<T> c : node.getChildren()){
			int newX = (int)(mid-distance/2+positionCounter*unit + unit/2);
			int newY = depth*distanceY;
			Point p = new Point(newX, newY);
			positions.put(c, p);
			updateTreeNode(c, depth+1, newX);
			panelWidthMin = Math.min(panelWidthMin, newX);
			panelWidthMax = Math.max(panelWidthMax, newX);
			positionCounter++;
		}
		//maxDepth = Math.max(maxDepth, depth+1);
		revalidate();
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		try {
			for(Map.Entry<NodeInterface<T>, Point> entry : positions.entrySet()){
				NodeInterface<T> n = entry.getKey();
				Point p = entry.getValue();
				
				int xReal = p.x + Math.abs(panelWidthMin) + offsetX;
				int yReal = p.y + offsetY;
				
				//draw lines to children
				for (NodeInterface<T> childNode : n.getChildren()){
					Point childPoint = positions.get(childNode);
					int childPointXReal = childPoint.x + Math.abs(panelWidthMin) + offsetX;
					int childPointYReal = childPoint.y + offsetY;
					g.drawLine(xReal, yReal, childPointXReal, childPointYReal);
				}
				
			}
			
			for(Map.Entry<NodeInterface<T>, Point> entry : positions.entrySet()){
				NodeInterface<T> n = entry.getKey();
				Point p = entry.getValue();
				
				int xReal = p.x + Math.abs(panelWidthMin) + offsetX;
				int yReal = p.y + offsetY;
				
				int offsetOval = nodeWidth/2;
				//draw node
				g.setColor(n.getColor());
//			g.setColor(fillColor);
				g.fillOval(xReal-offsetOval, yReal-offsetOval, nodeWidth, nodeWidth);
				g.setColor(Color.BLACK);
				g.drawOval(xReal-offsetOval, yReal-offsetOval, nodeWidth, nodeWidth);
				WorldNode wn = (WorldNode)n;
				String s2;
				g.setColor(Color.RED);
				if (wn.getCenterOrientation() == WorldNode.CENTER_X)
					s2 = "X";
				else
					s2 = "Y";
				g.drawBytes(s2.getBytes(), 0, s2.getBytes().length, 
						xReal, yReal+20);
			if (n.getContent() != null){
				g.setColor(Color.CYAN);
				g.drawOval(xReal-offsetOval, yReal-offsetOval, nodeWidth, nodeWidth);
				if (n.getContent() instanceof WorldObject){
					WorldObject wo = (WorldObject) n.getContent();
					String s = String.valueOf(wo.number);
					g.setColor(Color.white);
					g.drawBytes(s.getBytes(), 0, s.getBytes().length, 
							xReal, yReal);
					
				}
			}
//				g.drawBytes(n.getContent().toString().getBytes(), 0, n.getContent().toString().getBytes().length, 
//						xReal, yReal);
			}
		} catch (NullPointerException | ConcurrentModificationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
	}
	
	public void pack(){
		int x = Math.abs(panelWidthMin) + panelWidthMax + offsetX*2;
		int y = maxDepth*distanceY + offsetY*2;
		setMinimumSize(new Dimension(x, y));
		setPreferredSize(new Dimension(x, y));
		setMaximumSize(new Dimension(x, y));
	}
	
	
}
