package solution;


import java.awt.geom.Point2D;
import java.util.ArrayList;

import problem.ASVConfig;

/**
 * An immutable class representing the Nodes for the Dijkstra algorithm
 * implementation. Each Node is represented as a 2D point in space
 * @author The A-Team
 *
 */

public class Node {
	//final private String name;
	final private Point2D location;
	final private ArrayList<Double> theta;
	final private ASVConfig configCoords;
	protected Node parent;
	public static final double EQ_THRESHOLD = 0.0001;
	
	public Node(double x, double y,ArrayList<Double> angles) {
		location = new Point2D.Double(x, y);
		theta=angles;
		configCoords=stateToASVConfig(x,y,angles);
	}
	
	//Generic node definition, DELETE THIS AFTER INCLUDING PROPER METHODS IN COOLLISION DETECT
	public Node() {
		location=new Point2D.Double(0,0);
		theta=new ArrayList<Double>();
		configCoords=new ASVConfig(new ArrayList<Point2D>());
		
	}

	//Queries:

	
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
	/**
	 * Return the ASVs coords
	 * @return ASVConfig getConfigCoords
	 */
	public ASVConfig getConfigCoords() {
		return configCoords;
	}
	
	/**
	 *  Gives the parent
	 * @return Node parent
	 */
	public Node getParent() {
		return parent;
	}
	
	/*
	 * Gives puts a parent node
	 */
	public void setParent(Node parent){
		this.parent=parent;
	}
	
	/**
	 * Returns the Node in the middle of current
	 * and another node
	 * @param end second node
	 * @return Node
	 */

	//Help functions
	/**
	 * Obtains ASVs coords given state
	 * @param x x position
	 * @param y y position
	 * @param angles  list of angles theta
	 * @return ASVConfig
	 */
	private ASVConfig stateToASVConfig(double x,double y,ArrayList<Double> angles) {
		double broomLength=0.05;
		ArrayList<Point2D> asvs=new ArrayList<Point2D>();
		asvs.add(new Point2D.Double(x,y));
		double psi=0;
		
		for (int i=0;i<angles.size();i++) {
			psi+=angles.get(i);
			double deltaX=broomLength*Math.cos(Math.PI*psi/180);
			double deltaY=broomLength*Math.sin(Math.PI*psi/180);
			Point2D nextPoint=new Point2D.Double(asvs.get(i).getX()+deltaX,asvs.get(i).getY()+deltaY);
			asvs.add(nextPoint);
		}
		
		return new ASVConfig(asvs);		
		
	}
	
	/**
	 * Returns a cloned version of itself
	 */
	public Node clone() {
		Node a =new Node(this.getLocation().getX(),this.getLocation().getY(),this.getTheta());
		if (! (this.getParent() == null))
			a.setParent(this.getParent());
		return (a);
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
		Edge edge = new Edge(this,nd);
		if(edge.getDistance()>EQ_THRESHOLD)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return ("Position: "+getLocation().toString()+"  Angles: "+getTheta().toString());
	}
}
