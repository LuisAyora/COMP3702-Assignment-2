package problem;


import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * An immutable class representing the Nodes for the Dijkstra algorithm
 * implementation. Each Node is represented as a 2D point in space
 * @author The A-Team
 *
 */
public class Node {
	final private String name;
	final private Point2D location;
	private ArrayList<Double> theta;
	
	public Node(String name, double x, double y,ArrayList<Double> angles) {
		this.name = name;
		location = new Point2D.Double(x, y);
		theta=angles;
	}

	//Queries:
	
	/**
	 * Return the Node's name
	 * @return String name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Return the 2D location of the Node
	 * @return Point2D location
	 */
	public Point2D getLocation() {
		return location;
	}
	
	/**
	 * Return the theta angles
	 * @return ArrayList<Double> theta
	 */
	public ArrayList<Double> getTheta(){
		return theta;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(this == obj)
			return true;
		if(getClass() != obj.getClass())
			return false;
		Node nd = (Node) obj;
		if((nd.location != location) || nd.getName() != name)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
