package solution;
import java.util.Queue;
import java.util.Random;
import java.util.ArrayList;
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
	
	private Random numGenerator=new Random();
	public PathFinder(Node init,Node end) {
		initNode=init;
		goalNode=end;
		//theQueue=new PriorityQueue();
	}
	
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
	
	private Node randomGaussianNode(Node node,double variance) {
		ArrayList<Double> angles=new ArrayList<Double>();
		boolean convexity = nodeConvexity(node);
		//double x = numGenerator.nextGaussian()+;
	}
	
	/**
	 * Gives true if clockwise convexity and 
	 * false if conter-clockwise convexity
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
