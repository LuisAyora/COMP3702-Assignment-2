package solution;
import java.util.Queue;
import java.util.List;

public class PathFinder {
	private Node initNode;
	private Node goalNode;
	private Queue<Node> theQueue;
	private List<Node> inTree;
	private int maxRandWalk = 100;
	private int maxIter = 5000;
	private int mode = 1;
	private int branchingFact=4;
	
	public PathFinder(Node init,Node end) {
		initNode=init;
		goalNode=end;
		//theQueue=new PriorityQueue();
	}
}
