package logic;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import treeNodeLogic.NodeInterface;

public class WorldNode extends NodeInterface<WorldObject>{
	
	public final static int CENTER_X = 0;
	public final static int CENTER_Y = 1;
	
	private WorldNode left;
	
	private WorldNode right;
	
	private AABB aabb;
	
	private WorldObject worldObject;
	
	private double center;
	
	private int centerOrientation;

	
	
	
	public WorldNode() {
		super();
	}


	public WorldNode(AABB aabb) {
		super();
		this.aabb = aabb;
		updateCenter();
	}
	
	

	
	public WorldNode(WorldObject worldObject) {
		super();
		this.worldObject = worldObject;
		aabb = worldObject.getAabb();
		updateCenter();
	}


	public WorldNode(WorldNode left, WorldNode right, AABB aabb) {
		super();
		this.left = left;
		this.right = right;
		this.aabb = aabb;
		updateCenter();
	}

	
	public WorldNode(WorldNode left, WorldNode right, AABB aabb,
			WorldObject worldObject) {
		super();
		this.left = left;
		this.right = right;
		this.aabb = aabb;
		this.worldObject = worldObject;
		updateCenter();
	}
	
	public void updateCenter(){
		if (aabb != null){
			if (aabb.getLengthX()>aabb.getLengthY()){
				//x-axis
				centerOrientation = CENTER_X;
				center = aabb.getCenterX();
			}
			else{
				//y-axis
				centerOrientation = CENTER_Y;
				center = aabb.getCenterY();
			}
		}
		
	}
	
	public boolean isInTree(WorldObject o){
		if (worldObject != null){
			if (worldObject == o)
				return true;
			return false;
		}
		return left.isInTree(o) || right.isInTree(o);
	}
	
	public List<WorldObject> getAll(){
		List<WorldObject> list = new LinkedList<WorldObject>();
		if (worldObject != null){
			list.add(worldObject);
			return list;
		}
		else{
			list.addAll(left.getAll());
			list.addAll(right.getAll());
		}
		return list;
	}
	
	
	/**
	 * this method does not include the center and centerOrientation variable!
	 */
	public void updateAabb(){
		
		if (left == null || right == null)
			aabb = worldObject.getAabb();
		else{
			aabb.copyValues(right.getAabb());
			//aabb.updateAABB(left.getAabb());
			aabb.updateAABB(left.getAabb());
		}
	}
	
	public WorldNode clone(){
		WorldNode clone = new WorldNode();
		clone.setLeft(left);
		clone.setRight(right);
		if (aabb != null)
			clone.setAabb(aabb.copy());
		clone.setCenter(center);
		clone.setCenterOrientation(centerOrientation);
		//clone.setWorldObject(worldObject);
		return clone;
	}
	
	public List<WorldObject> getAllObjects(){
		List<WorldObject> list = new LinkedList<WorldObject>();
		//recursive implementation
		//iterative would be better!
		if (left == null || right == null){
			//leaf -> recursive anchor
			list.add(left.getWorldObject());
			list.add(right.getWorldObject());
			return list;
		}
		list.addAll(left.getAllObjects());
		list.addAll(right.getAllObjects());
		return list;
	}
	/*
	public boolean addObject(WorldObject wo){
		WorldNode node = this;
		int centerX = wo.getAabb().getCenterX();
		int centerY = wo.getAabb().getCenterY();
		boolean isRight = false;
		WorldNode tempNode;
		
		if (this.getWorldObject() == null && this.getLeft() == null && this.getRight() == null){
			//there are no WorldObjects in the structure
			this.setWorldNode(wo);
			return true;
		}
		
		if (this.getWorldObject() != null){
			//root
			tempNode = new WorldNode(this.getAabb().copy());
			this.getAabb().updateAABB(wo.getAabb());
			if (this.getAabb().getLengthX()>this.getAabb().getLengthY()){
				// x-axis
				if (tempNode.getAabb().getCenterX()<wo.getAabb().getCenterX()){
					this.setLeft(tempNode);
					this.setRight(new WorldNode(wo));
				}
				else{
					this.setRight(tempNode);
					this.setLeft(new WorldNode(wo));
				}
				this.getAabb().updateAABB(wo.getAabb());
			}
			else{
				// y-axis
				if (tempNode.getAabb().getCenterY()<wo.getAabb().getCenterY()){
					this.setLeft(tempNode);
					this.setRight(new WorldNode(wo));
				}
				else{
					this.setRight(tempNode);
					this.setLeft(new WorldNode(wo));
				}
				this.getAabb().updateAABB(wo.getAabb());
				return true;
			}
			return true;
		}
		
		while(true){
			AABB tempAabb = node.getAabb().copy();
			node.getAabb().updateAABB(wo.getAabb());
			
			
			if (node.getAabb().getLengthX()>node.getAabb().getLengthY()){
				//sort by x-axis
				if (centerX>node.getAabb().getCenterX())//go right
					isRight = true;
				else //go left
					isRight = false;
			}
			else{
				//sort by y-axis
				if (centerY>node.getAabb().getCenterY()) //go right
					isRight = true;
				else //got left
					isRight = false;
			}
			if (isRight){
				if (node.getRight().getWorldObject() != null){
					//System.out.println("right");
					//is leaf
					tempNode = node.getRight(); 
					node.setRight(new WorldNode(node.getRight().getAabb().copy()));
					node = node.getRight();
					break;
				}
				
				node = node.getRight();
			}
			else{
				if (node.getLeft().getWorldObject() != null){
					//System.out.println("left");
					//is leaf
					tempNode = node.getLeft();
					node.setLeft(new WorldNode(node.getLeft().getAabb().copy()));
					node = node.getLeft();
					break;
				}
				node = node.getLeft();
			}
		}
		node.getAabb().updateAABB(wo.getAabb());
		
		if (node.getAabb().getLengthX()>node.getAabb().getLengthY()){
			// x-axis
			if (tempNode.getAabb().getCenterX()<wo.getAabb().getCenterX()){
				node.setLeft(tempNode);
				node.setRight(new WorldNode(wo));
			}
			else{
				node.setRight(tempNode);
				node.setLeft(new WorldNode(wo));
			}
			//tempNode.getWorldObject().resetAabb();
			//tempNode.setAabb(tempNode.getWorldObject().getAabb());
			
			return true;
			
		}
		else{
			// y-axis
			if (tempNode.getAabb().getCenterY()<wo.getAabb().getCenterY()){
				node.setLeft(tempNode);
				node.setRight(new WorldNode(wo));
			}
			else{
				node.setRight(tempNode);
				node.setLeft(new WorldNode(wo));
			}
			//tempNode.getWorldObject().resetAabb();
			//tempNode.setAabb(tempNode.getWorldObject().getAabb());
			return true;
		}
		
		//return true;
	}
*/

	public WorldNode getLeft() {
		return left;
	}


	public WorldNode getRight() {
		return right;
	}


	public AABB getAabb() {
		return aabb;
	}


	public WorldObject getWorldObject() {
		return worldObject;
	}
	
	


	public double getCenter() {
		return center;
	}


	public int getCenterOrientation() {
		return centerOrientation;
	}


	public void setCenter(double center) {
		this.center = center;
	}


	public void setCenterOrientation(int centerOrientation) {
		this.centerOrientation = centerOrientation;
	}


	public void setWorldObject(WorldObject worldObject) {
		this.worldObject = worldObject;
		if (worldObject == null){
			this.aabb = null;
			return;
		}
		this.aabb = worldObject.getAabb();
		updateCenter();
	}
	
	public void setWorldObjectDebug(WorldObject worldObject) {
		this.worldObject = worldObject;
	}
	
	public WorldNode getCopy(){
		WorldNode wn = clone();
		if (getWorldObject() != null)
			wn.setWorldObjectDebug(worldObject.cloneWorldObject());
		if (wn.getLeft() != null || wn.getRight() != null){
			wn.setLeft(left.getCopy());
			wn.setRight(right.getCopy());
		}
		return wn;
	}


	public void setLeft(WorldNode left) {
		this.left = left;
	}


	public void setRight(WorldNode right) {
		this.right = right;
	}


	public void setAabb(AABB aabb) {
		this.aabb = aabb;
	}


	public void setWorldNode(WorldObject worldObject) {
		this.worldObject = worldObject;
		this.aabb = worldObject.getAabb();
		updateCenter();
	}

	@Override
	public String toString(){
		if (worldObject != null)
			return "[" + worldObject.toString() + "]";
		
		String s = "[";
		if (left != null)
			s = s + left.toString();
		else
			s = s + "left:null";
		s = s + ", ";
		if (right != null)
			s = s + right.toString();
		else
			s = s + "right:null";
		s = s + "]";
		return s;	
	}


	@Override
	public List<NodeInterface<WorldObject>> getChildren() {
		List<NodeInterface<WorldObject>> list = new LinkedList<NodeInterface<WorldObject>>();
		if (left != null)
			list.add(left);
		if (right != null)
			list.add(right);
		return list;
	}



	@Override
	public WorldObject getContent() {
		return worldObject;
	}


	@Override
	public Color getColor() {
		if (worldObject != null)
			return worldObject.getColor();
		return Color.darkGray;
	}
	

}
