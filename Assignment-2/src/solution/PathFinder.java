package solution;
import java.util.Queue;
import java.util.Random;

import problem.Obstacle;

import java.util.ArrayList;
import java.util.List;

public class PathFinder {
	private Node initNode;
	private Node goalNode;
	private ArrayList<Obstacle> obstacles;
	private Queue<Node> theQueue;
	private List<Node> inTree;
	private int maxRandWalk = 100;
	private int maxIter = 5000;
	private int mode = 1;
	private int branchingFact=4;
	
	private Random numGenerator=new Random();
	
	public PathFinder(Node init,Node end,ArrayList<Obstacle> obstacles) {
		initNode=init;
		goalNode=end;
		this.obstacles=obstacles;
		//theQueue=new PriorityQueue();
	}
	
	/**
	 * Obtains a random Node taking in count the convexity direction
	 * @param node
	 * @return Node
	 */
	private Node randomNode(Node node) {
		ArrayList<Double> angles=new ArrayList<Double>();
		boolean convexity=nodeConvexity(node);
		double x=numGenerator.nextDouble();
		double y=numGenerator.nextDouble();
		angles.add(numGenerator.nextDouble()*360);
		for (int i=1;i<node.getTheta().size();i++) {
			if (convexity)
				angles.add(numGenerator.nextDouble()*180);
			else
				angles.add(numGenerator.nextDouble()*180-180);
		}
		return (new Node(x,y,angles));
	}
	
	/**
	 * Obtains random Node from a Gaussian Distribution
	 * of mean the given points and variance given
	 * @param node
	 * @param variance
	 * @return
	 */
	private Node randomGaussianNode(Node node,double variance) {
		ArrayList<Double> angles=new ArrayList<Double>();
		boolean convexity = nodeConvexity(node);
		
		double x = numGenerator.nextGaussian()*variance+node.getLocation().getX();
		if (x>1)
			x=1-1e-5;
		if (x<0)
			x=1e-5;
		double y = numGenerator.nextGaussian()*variance+node.getLocation().getY();
		if (y>1)
			y=1-1e-5;
		if (y<0)
			y=1e-5;
		double angleToAdd=((numGenerator.nextGaussian()*variance+node.getTheta().get(0)/360)*360)%360;
		if (angleToAdd<0)
			angleToAdd+=360;
		angles.add(angleToAdd);
		for (int i=1;i<node.getTheta().size();i++) {
			if (convexity) {
				angleToAdd=(numGenerator.nextGaussian()*variance+node.getTheta().get(i)/180)*180;
				if (angleToAdd>180)
					angleToAdd=180;
				if (angleToAdd<0)
					angleToAdd=0;
			}else {
				angleToAdd=(numGenerator.nextGaussian()*variance+(node.getTheta().get(i)-180)/180)*180+180;
				if (angleToAdd>360)
					angleToAdd=359;
				if (angleToAdd<180)
					angleToAdd=181;
			}
			angles.add(angleToAdd);
		}
		return (new Node(x,y,angles));
	}
	
	/**
	 * Gives true if clockwise convexity and 
	 * false if counter-clockwise convexity
	 * @param node
	 * @return boolean
	 */
	private boolean nodeConvexity(Node node) {
		double accumulator=0;
		for (int i=1;i<node.getTheta().size();i++)
			accumulator+=node.getTheta().get(i);
		accumulator=accumulator/(node.getTheta().size()-1);
		if (accumulator>180)
			return false;
		return true;
	}
}
