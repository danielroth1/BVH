package logic;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

public class Square {
	
	private List<WorldObject> objects;
	
	private Point arrayPosition;
	
	public Square(Point arrayPosition) {
		this.arrayPosition = arrayPosition;
		objects = new LinkedList<WorldObject>();
	}

	public List<WorldObject> getObjects() {
		return objects;
	}

	public Point getArrayPosition() {
		return arrayPosition;
	}

	public void setObjects(List<WorldObject> objects) {
		this.objects = objects;
	}

	public void setArrayPosition(Point arrayPosition) {
		this.arrayPosition = arrayPosition;
	}
	
	

}
