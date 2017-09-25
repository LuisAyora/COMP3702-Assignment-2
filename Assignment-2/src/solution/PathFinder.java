package solution;
import java.util.Random;

import problem.Obstacle;
import solution.CollisionDetect;
import problem.ASVConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class PathFinder {
	private Node initNode;
	private Node goalNode;
	private List<Obstacle> obstacles;
	private int maxRandWalk = 10;
	private int maxNodesInRandWalk=20;
	private int branchingFact=4;
	private double gausVariance=0.001;
	private List<ASVConfig> finalPath;
	
	private Random numGenerator=new Random();
	
	public PathFinder(Node init,Node end,List<Obstacle> obstacles) {
		initNode=init;
		init.setParent(null);
		goalNode=end;
		end.setParent(null);
		this.obstacles=obstacles;
		//theQueue=new PriorityQueue();
	}
	
	public void navigate() {
		int mode =1;
		List<Node> randomWalked=new ArrayList<Node>();		
		double currentMin=2*initNode.getConfigCoords().getASVPositions().size();
		randomWalked.add(this.initNode);
		int randWalks=0;
		while (this.goalNode.getParent()==null) {
			switch (mode) {
			case 1:
				//Minimise wrt overall minimum
				Node head;
				/*if (gotBetterRandom2) 	
					head=lastRandWalk.get(lastRandWalk.size()-1);
				else
					head=lastRandWalk.get(this.numGenerator.nextInt(lastRandWalk.size()));
				*/
				head = popList(randomWalked);
				Node prevNode = head.clone();
				Edge furthestEdge=CollisionDetect.furthestValidEdge(
						new Edge(prevNode,this.bestFirstStep(prevNode, this.goalNode))
						,obstacles);
				//double prevDist=this.disToGoal(prevNode);
				Node nextNode = furthestEdge.getEnd();
				nextNode.setParent(prevNode);
				double dist=this.disToGoal(nextNode);
				while (dist<currentMin) {
					nextNode.setParent(prevNode);
					if (CollisionDetect.isEdgeValid(new Edge(nextNode,this.goalNode), obstacles))
						this.goalNode.setParent(nextNode);
					currentMin=dist;
					prevNode=nextNode.clone();
					furthestEdge=CollisionDetect.furthestValidEdge(new Edge(prevNode,this.bestFirstStep(prevNode, this.goalNode)),obstacles);
					//double prevDist=this.disToGoal(prevNode);
					nextNode = furthestEdge.getEnd();
					nextNode.setParent(prevNode);
					dist = this.disToGoal(nextNode);
				}
				if (CollisionDetect.isEdgeValid(new Edge(prevNode,this.goalNode), obstacles))
					this.goalNode.setParent(prevNode);
				
				if (!prevNode.equals(head)) {
					randomWalked.add(prevNode);
				}
				mode=2;
				break;
				
			case 2:
				//Minimise wrt this root minimum
				
				//Initialising Mini RRT
				Node root = popList(randomWalked);
				List<Node> randomWalking = new ArrayList<Node>();
				prevNode = root.clone();
				randomWalking.add(prevNode);
				double localMin=this.disToGoal(prevNode);
				
				furthestEdge=CollisionDetect.furthestValidEdge(new Edge(prevNode,this.randomNode(prevNode)), obstacles);
				nextNode = furthestEdge.getEnd();
				nextNode.setParent(prevNode);
				
				if (CollisionDetect.isEdgeValid(new Edge(nextNode,this.goalNode), obstacles))
					this.goalNode.setParent(nextNode);
				
				//prevDist = this.disToGoal(prevNode);
				dist = this.disToGoal(nextNode);
				randomWalking.add(nextNode);
				randomWalked.add(nextNode);
				
				int counter=1;
				while(counter<this.maxNodesInRandWalk && dist>=localMin) {					
					prevNode = nextNode.clone();
					Node randomN = this.randomNode(prevNode);
					Node closestNode = this.closestNode(randomWalking, randomN);
					furthestEdge=CollisionDetect.furthestValidEdge(new Edge(closestNode,randomN), obstacles);
					nextNode = furthestEdge.getEnd();
					nextNode.setParent(closestNode);
					if (CollisionDetect.isEdgeValid(new Edge(nextNode,this.goalNode), obstacles))
						this.goalNode.setParent(nextNode);
					//prevDist = this.disToGoal(prevNode);
					dist = this.disToGoal(nextNode);
					randomWalking.add(nextNode);
					randomWalked.add(nextNode);
					counter++;
				}
				randWalks++;
				if (randWalks<this.maxRandWalk-1) {
					mode=1;
				}else {
					mode=3;
				}
				break;
			case 3:
				int index=numGenerator.nextInt(randomWalked.size());
				Node toSearch=randomWalked.remove(index);
				randomWalked.add(toSearch);
				randWalks=0;
				mode = 1;
				break;
			}
		}
	}
	
	/**
	 * Pops last element of a list
	 * @param list
	 * @return Node
	 */
	private Node popList(List<Node> list) {
		if (list.isEmpty())
			return null;
		return list.remove(list.size()-1);
	}
	
	/**
	 * Returns the best First Step sample
	 * @param head
	 * @param destination
	 * @return Node
	 */
	private Node bestFirstStep(Node head,Node destination) {
		Node output=this.randomGaussianNode(head, this.gausVariance);
		double dist=this.disToGoal(output);
		for (int i=1;i<this.branchingFact;i++) {
			Node rand=this.randomGaussianNode(head, this.gausVariance);
			double ndist=this.disToGoal(rand);
			if (ndist<dist)
				output=rand;
		}
		return output;
	}
	
	/**
	 * Gets the closest node from a list to a given node
	 * @param nodes
	 * @param node
	 * @return Node
	 */
	private Node closestNode(List<Node> nodes,Node node) {
		Edge edge=new Edge(nodes.get(0),node);
		double minDist=edge.getDistance();
		int index=0;
		for (int i=1;i<nodes.size();i++) {
			edge=new Edge(nodes.get(i),node);
			if (minDist>edge.getDistance()) {
				minDist=edge.getDistance();
				index=i;
			}
		}
		return nodes.get(index);
	}
	
	/**
	 * Get the distance to the goal
	 * @param node
	 * @return
	 */
	private double disToGoal(Node node) {
		Edge edge=new Edge(node,this.goalNode);
		return edge.getDistance();
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
		Node result=new Node(x,y,angles);
		if (CollisionDetect.isNodeConfigValid(result))
			return (new Node(x,y,angles));
		else
			return randomNode(node);
	}
	
	/**
	 * Obtains random Node from a Gaussian Distribution
	 * of mean the given points and variance given
	 * @param node
	 * @param variance
	 * @return
	 */
	public Node randomGaussianNode(Node node,double variance) {
		ArrayList<Double> angles=new ArrayList<Double>();
		boolean convexity = nodeConvexity(node);
		Node result;
		do {
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
			result=new Node(x,y,angles);
		}while(!CollisionDetect.isNodeConfigValid(result));
		return result;
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
	
	//Querys
	/**
	 * 
	 * @return Goal Node
	 */
	public Node getGoalNode() {
		return this.goalNode;
	}
	
	public ArrayList<Node> getNodeList(Node last,ArrayList<Node> theList){
		if (last.parent==null)
			return theList;
		theList.add(last);
		return getNodeList(last.getParent(),theList);
	}
	
	public ArrayList<ASVConfig> finalSolution(ArrayList<Node> nodePath) {
		ArrayList<ASVConfig> finalPath = new ArrayList<ASVConfig>();
		Collections.reverse(nodePath);
		for (Node nd : nodePath) {
			finalPath.add(nd.getConfigCoords());
		}
		return finalPath;
	}
}
