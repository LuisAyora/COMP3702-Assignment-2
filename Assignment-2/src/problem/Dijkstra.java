package problem;

import java.util.*;

/**
 * An implementation of the Dijkstra's Search Algorithm using Nodes that are
 * 2-Dimensional
 * PriorityQueue Comparator has not yet been implemented
 * @author The A-Team
 *
 */

public class Dijkstra {
	final private Graph graph;
	private HashSet<Node> settledNodes;
	private PriorityQueue<Node> unsettledNodes;
	private HashMap<Node, Node> predecessors;
	private HashMap<Node, Double> distance;
	
	/**
	 * Constructor: Generate a new instance of the class by feeding in a 
	 * Graph to conduct the search on.
	 * @param graph
	 */
	public Dijkstra(Graph graph) {
		this.graph = graph;
	}
	
	/**
	 * Conduct a search given a root node using Dijkstra's Algorithm
	 * @param root
	 */
	public void search(Node root) {
		settledNodes = new HashSet<Node>();
		unsettledNodes = new PriorityQueue<Node>();
		predecessors = new HashMap<Node, Node>();
		distance = new HashMap<Node, Double>();
		distance.put(root, (double) 0);
		unsettledNodes.add(root);
		while(unsettledNodes.size() > 0) {
			Node node = getMinimum(unsettledNodes);
			settledNodes.add(node);
			unsettledNodes.remove(node);
			getMinDist(node);
		}
	}
	
	/**
	 * Return the shortest distance between a Node and its adjacent Nodes
	 * @param node
	 */
	private void getMinDist(Node node) {
		List<Node> adjNodes = getNeighbours(node);
		for(Node target : adjNodes) {
			if(getShortestDist(target) > getShortestDist(node)
				+ getDist(node, target)) {
				distance.put(target, getShortestDist(node) 
						+ getDist(node, target));
				predecessors.put(target, node);
				unsettledNodes.add(target);
			}
		}
	}
	
	/**
	 * Return the distance between two Nodes that form an Edge
	 * @param node
	 * @param target
	 * @return
	 * @throws RuntimeException
	 */
	private double getDist(Node node, Node target)
	throws RuntimeException{
		for(Edge edge : graph.getEdges()) {
			if(edge.getInit().equals(node) &&
					edge.getEnd().equals(target)) {
				return edge.getWeight();
			}
		}
		throw new RuntimeException("Should not happen");
	}
	
	/**
	 * Return the List of Nodes that are connected to the input Node via 
	 * an Edge
	 * @param node
	 * @return
	 */
	private List<Node> getNeighbours(Node node) {
		List<Node> neighbours = new ArrayList<Node>();
		for(Edge edge : graph.getEdges()) {
			if(edge.getInit().equals(node) && !isSettled(edge.getEnd())) 
				neighbours.add(edge.getEnd());
		}
		return neighbours;
	}
	
	/**
	 * Return the Node that has the shortest distance to the input Node
	 * @param nodes
	 * @return
	 */
	private Node getMinimum(PriorityQueue<Node> nodes) {
		Node minimum = null;
		for(Node node : nodes) {
			if(minimum == null) {
				minimum = node;
			}
			else {
				if(getShortestDist(node) < 
					getShortestDist(minimum)) {
					minimum = node;
				}
			}
		}
		return minimum;
	}
	
	/**
	 * Determine if the input Node is settled or not.
	 * @param node
	 * @return
	 */
	private boolean isSettled(Node node) {
		return settledNodes.contains(node);
	}
	
	
	private double getShortestDist(Node destination) {
		Double d = distance.get(destination);
		if(d == null)
			return Integer.MAX_VALUE;
		else
			return d;
	}
	
	/**
	 * Return the graph of the current search.
	 * @return
	 */
	public Graph getGraph() {
		return graph;
	}
	
	/**
	 * Generate the LinkedList of the instructions to reach the target Node in
	 * the shortest path
	 * @param target
	 * @return
	 */
	public LinkedList<Node> getPath(Node target) {
		LinkedList<Node> path = new LinkedList<Node>();
		Node step = target;
		// Check to see if a path exists
		if(predecessors.get(step) == null)
			return null;
		path.add(step);
		while(predecessors.get(step) != null) {
			step = predecessors.get(step);
			path.add(step);
		}
		// Reverse the path order to show it in the correct orientation
		Collections.reverse(path);
		return path;
	}
	
}
