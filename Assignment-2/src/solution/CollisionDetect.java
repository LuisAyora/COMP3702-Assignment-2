package solution;

import java.util.List;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import problem.Obstacle;
import tester.*;

public class CollisionDetect {
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
		//if (!isConvex(node))
		if(!test.isConvex(node.getConfigCoords()))
			return false;
		if (!test.fitsBounds(node.getConfigCoords()))
			return false;
		if (!test.hasEnoughArea(node.getConfigCoords()))
			return false;
		//if (broomsCross(node))
		//	return false;
		if (test.hasCollision(node.getConfigCoords(), obstacles))
			return false;
		return true;
	}
	/**
	 * Checks if the configuration of the Node is valid
	 * @param node
	 * @param obstacles
	 * @return boolean
	 */
	public static boolean isNodeConfigValid(Node node) {	
		//if (!isConvex(node))
		if(!test.isConvex(node.getConfigCoords()))
			return false;
		if (!test.fitsBounds(node.getConfigCoords()))
			return false;
		if (!test.hasEnoughArea(node.getConfigCoords()))
			return false;
		
		//if (broomsCross(node))
		//	return false;
		return true;
	}
	
	
	
	/**
	 * Checks if Node is convex
	 * @param node
	 * @return boolean
	 */
	public static boolean isConvex(Node node) {
		double dummyAngle=0;
		if (node.getTheta().size()<2)
			return true;
		for (int i=1;i<node.getTheta().size();i++) {
			double readAngle=node.getTheta().get(i);
			if (readAngle>180+angularMaxError)
				readAngle=readAngle-360;
			if (dummyAngle*readAngle<-angularMaxError)
				return false;
			if (dummyAngle*readAngle>angularMaxError || dummyAngle*readAngle<-angularMaxError)
				dummyAngle=readAngle;
		}
		for (int i=1;i<node.getConfigCoords().getASVPositions().size()-1;i++) {
			if (!sameSide(node.getConfigCoords().getASVPositions().get(0),
					node.getConfigCoords().getASVPositions().get(node.getConfigCoords().getASVPositions().size()-1),
					node.getConfigCoords().getASVPositions().get(i),
					node.getConfigCoords().getASVPositions().get(i+1))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks if brooms of a configuration collide with each other
	 * @param node
	 * @return
	 */
	public static boolean broomsCross(Node node) {
		ArrayList<Line2D> links = new ArrayList<Line2D>();
		for (int i=0;i<node.getConfigCoords().getASVPositions().size()-1;i++) {
			links.add(new Line2D.Double(node.getConfigCoords().getPosition(i),
					node.getConfigCoords().getPosition(i+1)));
		}
		for (int i=0;i<links.size()-1;i++) {
			for (int j=i+1;j<links.size();j++) {
				if (links.get(i).intersectsLine(links.get(j)))
					return false;
			}
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
		if (!isNodeValid(edge.getEnd(), obstacles)  ||
				!isNodeValid(middle, obstacles) ||
				!isNodeValid(edge.getInit(), obstacles)) {
			return false;
		}
		if (edge.getInit().getLocation().distance(middle.getLocation())<resolution) {
			return true;
		}
		return (isEdgeValid (new Edge(middle,edge.getEnd()),obstacles) 
				&& isEdgeValid (new Edge(edge.getInit(),middle),obstacles));
	}
	/**
	 * Generates longest valid edge in the given edge direction
	 * @param edge
	 * @param obstacles
	 * @return Edge
	 */
	/*
	public static Edge furthestValidEdge(Edge edge,List<Obstacle> obstacles) {
		if (isEdgeValid(edge, obstacles))
			return edge;
		return furthestValidEdge(new Edge(edge.getInit(),edge.middleNode()),obstacles);
	}*/
	
	
	public static Edge furthestValidEdge(Edge edge,List<Obstacle> obstacles) {
		Edge edgeA= new Edge(edge.getInit(),edge.getEnd());
		int count=0;
		while(!isEdgeValid(edgeA,obstacles) && !edgeA.getInit().equals(edgeA.getEnd())) {
			edgeA= new Edge(edgeA.getInit(),edgeA.middleNode());
			/*
			System.out.println("the Edge: "+edgeA.toString());
			System.out.println("First node valid? : "+Boolean.toString(isNodeValid(edgeA.getInit(),obstacles)));
			System.out.println(edgeA.getInit().getConfigCoords());
			System.out.println("Middle node valid? : "+Boolean.toString(isNodeValid(edgeA.getEnd(),obstacles)));
			System.out.println(edgeA.getEnd().getConfigCoords());
			System.out.println("Counter: "+Integer.toString(count));
			System.out.println("Edge: "+edgeA.toString());
			*/
			count++;
		}
		return edgeA;
	}
	
	/**
	 * Checks if two points are on the same side of a line
	 * @param initial
	 * @param fina
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static boolean sameSide (Point2D initial, Point2D fina, Point2D p1, Point2D p2) {
		
		double a = initial.getY()-fina.getY() ;
		double b = fina.getX() - initial.getX();
		double c = (initial.getX()-fina.getX()) * initial.getY() + (fina.getY()-initial.getY())*initial.getX();
		
		if (a*p1.getX() + b*p1.getY() + c < 0 && a*p2.getX() + b*p2.getY()  < 0 ) 
			return true;
		if (a*p1.getX() + b*p1.getY() + c > 0 && a*p2.getX() + b*p2.getY()  > 0 )
			return true;
		else 
			return false;

	}
	
}
