package solution;

import java.util.List;

import problem.ASVConfig;
import problem.Obstacle;
import tester.Tester;

public class collisionDetect {
	static double resolution=0.001;
	static Tester test=new Tester();
	
	//PLEASE IMPLEMENT THIS METHOD CORRECTLY
	public static boolean isNodeValid(Node node,List<Obstacle> obstacles) {	
		return true;
	}
	
	//PLEASE IMPLEMENT THIS METHOD PROPERLY
	public static Edge nearestEdge(Edge edge,List<Obstacle> obstacles) {
		return (new Edge(new Node(),new Node()));
	}
	
	/**
	 * Checks whether a path is collision free depending on resolution
	 * @param edge edge to be checked
	 * @param obstacles workspace obstacles
	 * @return boolean
	 */
	
	
	public static boolean isEdgeValid(Edge edge,List<Obstacle> obstacles) {
		Node middle=edge.middleNode();
		if (isNodeValid(middle, obstacles)) {
			return true;
		}
		if (edge.getInit().getLocation().distance(middle.getLocation())<resolution) {
			return true;
		}
		return (isEdgeValid (new Edge(edge.getInit(),middle),obstacles)
				&&isEdgeValid (new Edge(middle,edge.getInit()),obstacles));
	}
	
}
