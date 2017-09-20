package solution;

import java.util.ArrayList;

/**
 * An immutable class that represents the weighted edge between two nodes for
 * the Dijkstra Algorithm implementation.
 * The weight of the Node is given by the Manhattan metric (L1 norm) between
 * the initial and end goals.
 * @author The A-Team
 *
 */

public class Edge {
	final private Node init;
	final private Node end;
	final private double weight;
	
	public Edge(Node init, Node end) {
		this.init = init;
		this.end = end;
		weight = manhattanMetric(init, end);
	}
	
	// Queries
	
	/**
	 * Return the initial state Node
	 * @return Node init
	 */
	public Node getInit() {
		return init;
	}
	
	/**
	 * Return the end state Node
	 * @return Node end
	 */
	public Node getEnd() {
		return end;
	}
	
	/**
	 * Return the weight of the Edge
	 * @return double weight
	 */
	public double getWeight() {
		return weight;
	}
	
	

	
	//Helping functions
	
	private double manhattanMetric(Node start, Node end) {
		double distance = Math.abs(((start.getLocation().getX() - end.getLocation().getX() + 
				Math.abs(start.getLocation().getY() - end.getLocation().getY()))));
		for (int i=0;i<start.getTheta().size();i++) {
			double thetaStart=start.getTheta().get(i);
			double thetaEnd=end.getTheta().get(i);
			distance+=Math.abs(angularDif(thetaStart,thetaEnd));
		}
		return distance;
	}
	
	/**
	 * Takes the angular difference of normalised angles
	 * @param a first angle
	 * @param b second angle
	 * @return returns a-b noting circularity of angles
	 */
	private double angularDif(double a,double b) {
		double a1=(a+2)%2;
		double b1=(b+2)%2;
		return a1-b1;
	}
	
	
	@Override
	/**
	 * String representation of the Edge object
	 */
	public String toString() {
		return "Initial: " + init.toString() + "\n Final: " + end.toString()
		+ "\n Weight:" + Double.toString(weight);
	}
	
	// Dummy test method - Delete this!!!
	public static void main(String[] args) {
		//Create test objects
		Node n1 = new Node( 0, 0,new ArrayList<Double>());
		Node n2 = new Node( 3, 4,new ArrayList<Double>());
		Edge e1 = new Edge(n1, n2);
		
		System.out.println(Double.toString(e1.getWeight()));
	}
}
