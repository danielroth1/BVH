package treeNodeLogic;

import java.awt.Color;
import java.util.List;

public abstract class NodeInterface<T> {

	public int getMaxDepth(){
		return getMaxDepth(this);
	}
	
	protected int getMaxDepth(NodeInterface<T> n){
		int maxDepth = 0;
		for(NodeInterface<T> n2 : n.getChildren()){
			maxDepth = Math.max(maxDepth, getMaxDepth(n2)+1);
		}
		return maxDepth;
	}

	public abstract List<NodeInterface<T>> getChildren();

	public abstract T getContent();
	
	public abstract Color getColor();
	

}