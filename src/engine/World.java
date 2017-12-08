package engine;

import java.util.LinkedList;
import java.util.List;

import logic.WorldNode;
import logic.WorldObject;

public class World {
	
	private WorldNode root;
	
	//public static long currentTime = System.currentTimeMillis();
	
	public World() {
		root = new WorldNode();
	}
	
	public synchronized boolean addObject(WorldObject wo){
		WorldNode node = root;
		double centerX = wo.getAabb().getCenterX();
		double centerY = wo.getAabb().getCenterY();
		boolean isRight = false;
		WorldNode tempNode;
		
		if (root.getWorldObject() == null && root.getLeft() == null && root.getRight() == null){
			//there are no WorldObjects in the structure
			root.setWorldNode(wo);
			return true;
		}
		
		if (root.getWorldObject() != null){
			//System.out.println("root ungleich 0 at " + (System.currentTimeMillis()-currentTime));
			//root
			//root.setLeft(new WorldNode(root.getLeft().getAabb()));
			tempNode = root;
			root = new WorldNode(root.getAabb().copy());
			root.getAabb().updateAABB(wo.getAabb());
			root.updateCenter();
			if (root.getCenterOrientation() == WorldNode.CENTER_X){
				// x-axis
				if (root.getCenter()<wo.getAabb().getCenterX()){
//					System.out.println("center = " + tempNode.getCenter() + ", " + wo.getAabb().getX1() + ", " + wo.getAabb().getX2() + " = " + wo.getAabb().getCenterX() + " to the right");
					
					root.setLeft(tempNode);
					root.setRight(new WorldNode(wo));
					
				}
				else{
//					System.out.println("center = " + tempNode.getCenter() + ", " + wo.getAabb().getX1() + ", " + wo.getAabb().getX2() + " = " + wo.getAabb().getCenterX() + " to the left");
					
					root.setRight(tempNode);
					root.setLeft(new WorldNode(wo));
					
				}
				root.getAabb().updateAABB(wo.getAabb());
				root.updateCenter();
			}
			else{
				// y-axis
				if (root.getCenter()<wo.getAabb().getCenterY()){
					
					root.setLeft(tempNode);
					root.setRight(new WorldNode(wo));
				}
				else{
					
					root.setRight(tempNode);
					root.setLeft(new WorldNode(wo));
				}
				root.getAabb().updateAABB(wo.getAabb());
				root.updateCenter();
				return true;
			}
			
			
			
			return true;
		}
		
		while(true){
			//AABB tempAabb = node.getAabb().copy();
			node.getAabb().updateAABB(wo.getAabb());
			
			
			if (node.getCenterOrientation() == WorldNode.CENTER_X){
				//sort by x-axis
				if (centerX>node.getCenter())//go right
					isRight = true;
				else if (centerX<node.getCenter())//go left
					isRight = false;
				else{ //even
					if (node.getLeft().isInTree(wo)) //ineffective but really unlikely
						isRight = false;
					else
						isRight = true;
				}
					
			}
			else{
				//sort by y-axis
				if (centerY>node.getCenter()) //go right
					isRight = true;
				else if (centerY<node.getCenter())//got left
					isRight = false;
				else{ //even
					if (node.getLeft().isInTree(wo)) //ineffective but really unlikely
						isRight = false;
					else
						isRight = true;
				}
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
//		node.setCenter(tempNode.getCenter());
//		node.setCenterOrientation(tempNode.getCenterOrientation());
		node.updateCenter();
		
		if (node.getCenterOrientation() == WorldNode.CENTER_X){
			// x-axis
//			if (node.getCenter()<=wo.getAabb().getCenterX()){
			if (tempNode.getAabb().getCenterX()<wo.getAabb().getCenterX()){
				if (GameEngine.debug)
					if (node.getCenter()< tempNode.getAabb().getCenterX())
						System.out.println("ERROR1, node.Center() = " + node.getCenter() 
								+ ", tempNode.getAabb().getCenterX() = " + tempNode.getAabb().getCenterX() 
								+ ", wo.getAabb.getCenterX() = " +  wo.getAabb().getCenterX());
				node.setLeft(tempNode);
				node.setRight(new WorldNode(wo));
			}
			else{
				if (GameEngine.debug)
					if (node.getCenter()> tempNode.getAabb().getCenterX()){
						System.out.println("ERROR2, node.Center() = " + node.getCenter() 
								+ ", tempNode.getAabb().getCenterX() = " + tempNode.getAabb().getCenterX() 
								+ ", wo.getAabb.getCenterX() = " +  wo.getAabb().getCenterX());
				}
				node.setRight(tempNode);
				node.setLeft(new WorldNode(wo));
			}
//			node.updateCenter();
			return true;
			
		}
		else{
			// y-axis
//			if (node.getCenter()<=wo.getAabb().getCenterY()){
			if (tempNode.getAabb().getCenterY()<wo.getAabb().getCenterY()){
				if (GameEngine.debug)
					if (node.getCenter()< tempNode.getAabb().getCenterY())
						System.out.println("ERROR3, node.Center() = " + node.getCenter() 
							+ ", tempNode.getAabb().getCenterY() = " + tempNode.getAabb().getCenterY() 
							+ ", wo.getAabb.getCenterY() = " +  wo.getAabb().getCenterY());
				node.setLeft(tempNode);
				node.setRight(new WorldNode(wo));
			}
			else{
				if (GameEngine.debug)
					if (node.getCenter()> tempNode.getAabb().getCenterY()){
						System.out.println("ERROR4, node.Center() = " + node.getCenter() 
							+ ", tempNode.getAabb().getCenterY() = " + tempNode.getAabb().getCenterY() 
							+ ", wo.getAabb.getCenterY() = " +  wo.getAabb().getCenterY());
				}
				node.setRight(tempNode);
				node.setLeft(new WorldNode(wo));
			}
//			node.updateCenter();
			return true;
		}
		
		//return true;
	}
	
	public synchronized boolean removeObject(WorldObject wo){
		WorldNode node = root;
		double centerX = wo.getAabb().getCenterX();
		double centerY = wo.getAabb().getCenterY();
		boolean isRight = false;
		WorldNode tempNode;
		if (root.getWorldObject() != null){
			if (root.getWorldObject() == wo){
				root.setWorldObject(null);
				return true;
			}
		}
		
		LinkedList<WorldNode> way = new LinkedList<WorldNode>();
		while(true){
			//node.getAabb().updateAABB(wo.getAabb());
			way.add(node);
			
			if (node.getCenterOrientation() == WorldNode.CENTER_X){
				//sort by x-axis
				if (centerX>node.getCenter())//go right
					isRight = true;
				else if (centerX<node.getCenter())//go left
					isRight = false;
				else{ //even
					if (node.getLeft().isInTree(wo)) //ineffective but really unlikely
						isRight = false;
					else
						isRight = true;
//					if (GameEngine.debug)
//						System.out.println("even3");
				}
			}
			else{
				//sort by y-axis
				if (centerY>node.getCenter()) //go right
					isRight = true;
				else if (centerY<node.getCenter())//got left
					isRight = false;
				else{ //even
					if (node.getLeft().isInTree(wo)) //ineffective but really unlikely
						isRight = false;
					else
						isRight = true;
//					if (GameEngine.debug)
//						System.out.println("even4");
				}
			}
			if (isRight){
				if (node.getRight().getWorldObject() != null){
						//&& node.getRight().getWorldObject() == wo){
					//is leaf
					if (node.getRight().getWorldObject() != wo){
						if (GameEngine.debug){
							System.out.println("ERROR OCCURED -> no object found to remove");
							System.out.println("with no. " + wo.number + " found instead no. " + node.getRight().getWorldObject().number);
						}
						
						throw new NullPointerException();
//						return false;
					}
					if (node.getLeft().getWorldObject() != null){
						node.setWorldObject(node.getLeft().getWorldObject());
//						AABB aabb2 = node.getAabb();
//						node.getWorldObject().setAabb(aabb2);
					}
					node.setAabb(node.getLeft().getAabb());
					node.setCenter(node.getLeft().getCenter());
					node.setCenterOrientation(node.getLeft().getCenterOrientation());
					node.setRight(node.getLeft().getRight());
					node.setLeft(node.getLeft().getLeft());
					node.updateAabb();
//					node.updateCenter();
					break;
				}
				
				node = node.getRight();
			}
			else{
				if (node.getLeft().getWorldObject() != null){
					//	&& node.getLeft().getWorldObject() == wo){
					//is leaf
					if (node.getLeft().getWorldObject() != wo){
						if (GameEngine.debug){
							System.out.println("ERROR OCCURED -> no object found to remove");
							System.out.println("with no. " + wo.number + " found instead no. " + node.getLeft().getWorldObject().number);
						}
						throw new NullPointerException();
//						return false;
					}
					if (node.getRight().getWorldObject() != null){
						node.setWorldObject(node.getRight().getWorldObject());
//						AABB aabb2 = node.getAabb();
//						node.getWorldObject().setAabb(aabb2);
					}
					node.setAabb(node.getRight().getAabb());
					node.setCenter(node.getRight().getCenter());
					node.setCenterOrientation(node.getRight().getCenterOrientation());
					node.setLeft(node.getRight().getLeft());
					node.setRight(node.getRight().getRight());
					node.updateAabb();
//					node.updateCenter();
					break;
				}
				node = node.getLeft();
			}
		}
		
		/*
		Object[] wayArray = way.toArray();
		for(int i = wayArray.length-1; i > -1; i--){
			WorldNode wn = (WorldNode) wayArray[i];
			wn.updateAabb();
//			wn.updateCenter();
		}
		*/
		
		while(!way.isEmpty()){
			WorldNode wn = way.removeLast();
			wn.updateAabb();
		}
		return true;
	}
	
	/**
	 * this is an inefficient but consistent method to ask if a worldObject is inside the structure
	 * it is not recommended to use this method, because it simply searches the whole tree and isn't
	 * exploiting the properties of the structure.
	 * @param o
	 * @return
	 */
	public boolean isInTree(WorldObject o){
		return root.isInTree(o);
	}
	
	public void recreateTree(){
		List<WorldObject> objects = root.getAll();
		root = new WorldNode();
		for (WorldObject wo : objects){
			addObject(wo);
		}
	}
	
	@Override
	public String toString(){
		return root.toString();
	}

	public WorldNode getRoot() {
		return root;
	}
	
	
	
	public void setRoot(WorldNode root) {
		this.root = root;
	}

	public World getCopy(){
		World w = new World();
		w.setRoot(root.getCopy());
		return w;
	}

}
