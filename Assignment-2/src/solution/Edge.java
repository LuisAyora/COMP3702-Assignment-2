package solution;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import problem.ProblemSpec;

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
	final private double distance;
	
	public Edge(Node init, Node end) {
		this.init = init;
		this.end = end;
		distance = calcCostDistance(init,end);
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
	
	/**
	 * Return the sum of manhattan distances
	 * between the two linked nodes
	 * @return
	 */
	public double getDistance() {
		return distance;
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
		double valA=a;
		double valB=b;
		double dif1=a-b;
		if (b<a) 
			valB+=360;
		else if(b>a)
			valA+=360;
		
		double dif2=valA-valB;
		if (dif1<=dif2) 
			return dif1;
		else 
			return dif2;
	}
	
	/**
	 * Obtains the node in the middle of the node
	 * @return Node
	 */
	public Node middleNode() {
		Point2D middlePoint=
				new Point2D.Double((this.getInit().getLocation().getX()+this.getEnd().getLocation().getX())/2,
						(this.getInit().getLocation().getY()+this.getEnd().getLocation().getY())/2);
		ArrayList<Double> angles=new ArrayList<Double>();
		for (int i=0;i<this.getInit().getTheta().size();i++) {
			//Accounts for circularity of angles when they have different sign
			double valA=this.getEnd().getTheta().get(i);
			double valB=this.getInit().getTheta().get(i);
			double dif1=valA-valB;
			
			double valA2=valA;
			double valB2=valB;
			
			if(valB<valA)
				valB2+=360;
			else if(valA<valB)
				valA2+=360;
			
			double dif2=valA2-valB2;
			
			if (Math.abs(dif1)<=Math.abs(dif2)) {
				angles.add(((valA+valB)/2)%360);
			}else {
				angles.add(((valA2+valB2)/2)%360);
			}
			
		}
		return new Node(middlePoint.getX(),middlePoint.getY(),angles);
		
	}
	/**
	 * Returns the sum of mahattan distances between each ASV node
	 * @param a
	 * @param b
	 * @return double distance
	 */
	private double calcCostDistance(Node a,Node b) {
		double distance=0;
		for (int i=0;i<a.getConfigCoords().getASVPositions().size();i++) {
			distance+=Math.abs(a.getConfigCoords().getASVPositions().get(i).getX()-
					b.getConfigCoords().getASVPositions().get(i).getX())+
					Math.abs(a.getConfigCoords().getASVPositions().get(i).getY()-
							b.getConfigCoords().getASVPositions().get(i).getY());
		}
		return distance;
	}
	
	@Override
	/**
	 * String representation of the Edge object
	 */
	public String toString() {
		return "Initial: " + init.toString() + "\n Final: " + end.toString()
		+ "\n Weight:" + Double.toString(weight);
	}

}
