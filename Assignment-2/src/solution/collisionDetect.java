package solution;

import java.util.List;

import problem.ASVConfig;
import problem.Obstacle;
import tester.Tester;

public class collisionDetect {
	static double resolution=0.001;
	static Tester test=new Tester();
	public static boolean isValidPath(ASVConfig getConfigCoords) {	
		return true;
	}
	
	/**
	 * Checks whether a path is collision free depending on resolution
	 * @param edge edge to be checked
	 * @param obstacles workspace obstacles
	 * @return boolean
	 */
	public static boolean isPathColliding(Edge edge,List<Obstacle> obstacles) {
		Node middle=edge.getInit().middleNode(edge.getEnd());
		if (test.hasCollision(middle.getConfigCoords(), obstacles)) {
			return true;
		}
		if (edge.getInit().getLocation().distance(middle.getLocation())<resolution) {
			return true;
		}
		return (isPathColliding (new Edge(edge.getInit(),middle),obstacles)
				&&isPathColliding (new Edge(middle,edge.getInit()),obstacles));
	}
	
}
