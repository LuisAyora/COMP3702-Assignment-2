package solution;

import java.util.List;

import problem.ASVConfig;
import problem.Obstacle;
import tester.Tester;

public class collisionDetect {
	static double resolution=0.001;
	static Tester test=new Tester();
	static double angularMaxError=1e5;
	
	/**
	 * Returns whether a node is Valid
	 * @param node
	 * @param obstacles
	 * @return boolean
	 */
	public static boolean isNodeValid(Node node,List<Obstacle> obstacles) {	
		if (!isConvex(node))
			return false;
		if (!test.fitsBounds(node.getConfigCoords()))
			return false;
		if (!test.hasEnoughArea(node.getConfigCoords()))
			return false;
		if (test.hasCollision(node.getConfigCoords(), obstacles))
			return false;
		return true;
	}
	/**
	 * Checks if Node is convex
	 * @param node
	 * @return boolean
	 */
	public static boolean isConvex(Node node) {
		double dummyAngle=0;
		for (int i=1;i<node.getTheta().size();i++) {
			double readAngle=node.getTheta().get(i);
			if (readAngle<180+angularMaxError)
				readAngle=readAngle-360;
			if (dummyAngle*readAngle<-angularMaxError)
				return false;
			if (dummyAngle*readAngle>angularMaxError || dummyAngle*readAngle<-angularMaxError)
				dummyAngle=readAngle;
		}
		return true;
	}
	
	/**
	 * Checks whether a path is collision free and valid depending on resolution
	 * @param edge edge to be checked
	 * @param obstacles workspace obstacles
	 * @return boolean
	 */
	public static boolean isEdgeValid(Edge edge,List<Obstacle> obstacles) {
		Node middle=edge.middleNode();
		if (!isNodeValid(middle, obstacles)) {
			return false;
		}
		if (edge.getInit().getLocation().distance(middle.getLocation())<resolution) {
			return true;
		}
		return (isEdgeValid (new Edge(edge.getInit(),middle),obstacles)
				&&isEdgeValid (new Edge(middle,edge.getInit()),obstacles));
	}
	
	//PLEASE IMPLEMENT THIS METHOD PROPERLY
	public static Edge nearestEdge(Edge edge,List<Obstacle> obstacles) {
		
		return (new Edge(new Node(),new Node()));
	}
}
