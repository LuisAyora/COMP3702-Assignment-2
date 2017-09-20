package solution;

import java.util.List;
import java.util.HashMap;

/**
 * An immutable graph representing the Graph that contains all the Nodes and
 * Vertices in the Workspace
 * @author The A-Team
 *
 */

public class Graph {
	final private List<Node> nodes;
	final private List<Edge> edges;
	//HashMap<Node, List<Edge>> connect;
	
	/**
	 * Constructor: Create a Graph object by feeding it a List of Nodes and
	 * Edges in the Graph
	 * @param nodes
	 * @param edges
	 */
	public Graph(List<Node> nodes, List<Edge> edges) {
		this.nodes = nodes;
		this.edges = edges;
	}
	
	//Queries
	/**
	 * Return the Nodes of the Graph
	 * @return List<Node> nodes
	 */
	public List<Node> getNodes() {
		return nodes;
	}
	
	/**
	 * Return the Edges of the Graph
	 * @return List<Edge> edges
	 */
	public List<Edge> getEdges() {
		return edges;
	}
	
}
