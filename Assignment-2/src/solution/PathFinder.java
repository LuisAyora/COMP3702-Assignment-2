package solution;
import java.util.Random;

import problem.Obstacle;
import solution.CollisionDetect;
import problem.ASVConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.awt.geom.Rectangle2D;

public class PathFinder {
	private Node initNode;
	private Node goalNode;
	private List<Obstacle> obstacles;
	private ArrayList<Node> randomWalked;
	private Random numGenerator = new Random();
	private RegionId narrowAreas;
	private int maxRandWalk = 4;
	private int maxNodesInRandWalk = 200;
	private int branchingFact = 6;
	private double gausVariance = 0.01;
	private int MAX_ITERATIONS = 2000;
	private int RANDOM_GAUSS_LIMIT = 5;
	private double narrowThreshold;
	private double PROB_IN_NARROW = 0.7;
	private double BROOM_LENGTH = 0.05;
	
	public PathFinder(Node init,Node end,List<Obstacle> obstacles) {
		initNode=init;
		init.setParent(null);
		goalNode=end;
		end.setParent(null);
		narrowThreshold = obtainNarrowThreshold();
		narrowAreas = new RegionId(narrowThreshold,obstacles);
		this.obstacles=obstacles;
		//theQueue=new PriorityQueue();
	}
	
	public void navigate() {
		int mode = 1;
		randomWalked=new ArrayList<Node>();		
		double currentMin=2*initNode.getConfigCoords().getASVPositions().size();
		randomWalked.add(this.initNode);
		int randWalks=0;
		int iterCount=0;
		while ((this.goalNode.getParent()==null)&&(iterCount<this.MAX_ITERATIONS)) {
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
				Node nextNode = bestFirstStep(prevNode,this.goalNode);
				double dist = currentMin;
				Edge edge;
				if (nextNode!=null) {
					edge = new Edge(prevNode,nextNode);
					if (CollisionDetect.isEdgeValid(edge, obstacles)) {
						nextNode.setParent(prevNode);
						dist=this.disToGoal(nextNode);
						prevNode=nextNode.clone();
					}
				}
				while (dist<=currentMin) {
					if (nextNode!=null) {
						nextNode.setParent(prevNode);
						dist=this.disToGoal(nextNode);
						edge = new Edge(prevNode,nextNode);
	
						if (dist<currentMin && CollisionDetect.isEdgeValid(edge, obstacles))
							currentMin=dist;
							prevNode=nextNode.clone();
							if (CollisionDetect.isEdgeValid(new Edge(prevNode,this.goalNode), obstacles))
								this.goalNode.setParent(nextNode);
					}
					
					//double prevDist=this.disToGoal(prevNode);
					nextNode = bestFirstStep(prevNode,this.goalNode);
					/*nextNode.setParent(prevNode);
					dist = this.disToGoal(nextNode);
					*/
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
				Node root;
				if (randomWalked.size()<=1)
					root = randomWalked.get(randomWalked.size()-1);
				else
					root = popList(randomWalked);
				List<Node> randomWalking = new ArrayList<Node>();
				prevNode = root.clone();
				randomWalking.add(prevNode);
				double localMin=this.disToGoal(prevNode);
				dist = localMin;
				
				Node dummyNode = this.randomNode(prevNode);
				Edge connection = new Edge(prevNode,dummyNode);

				
				int counter = 0;
				while (counter<this.maxNodesInRandWalk && dist>=localMin) {
					dummyNode = this.randomNode(prevNode);
					Node closeNode = this.closestNode(randomWalking, dummyNode);
					connection = new Edge(closeNode,dummyNode);
					//Checks if can connect randomNode the closest node in the RRT
					if (CollisionDetect.isEdgeValid(connection, obstacles)) {
						nextNode = connection.getEnd();
						nextNode.setParent(closeNode);
						this.randomWalked.add(nextNode);
						randomWalking.add(nextNode);
						dist = connection.getDistance();
						prevNode = nextNode.clone();
						//Checks if can reach goalNode
						if (CollisionDetect.isEdgeValid(new Edge(prevNode,this.goalNode), obstacles))
							this.goalNode.setParent(prevNode);
						counter++;
					}
				}
				/*
				Edge furthestEdge=CollisionDetect.furthestValidEdge(new Edge(prevNode,this.randomNode(prevNode)), obstacles);
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
				*/
				
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
			iterCount++;
			System.out.println("Iterations: "+Integer.toString(iterCount));
		}
		System.out.println("OUT OF BIG WHILE");
		if (this.goalNode.getParent()==null)
			this.goalNode.setParent(popList(randomWalked));
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
		double dist = 2*(head.getTheta().size()+1);
		if (output!=null)
			dist=this.disToGoal(output);
		for (int i=1;i<this.branchingFact;i++) {
			Node rand=this.randomGaussianNode(head, this.gausVariance);
			if (rand!=null) {
				double ndist=this.disToGoal(rand);
				if (ndist<dist)
					output=rand;
			}			
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
		ArrayList<Double> angles;
		Node result;
		double decision = this.numGenerator.nextDouble();
		Rectangle2D nearestRect = this.nearestNarrowRegion(node);
		if (decision<this.PROB_IN_NARROW && nearestRect!=null) {
			do{
				angles =new ArrayList<Double>();
				boolean convexity=nodeConvexity(node);
				double x=numGenerator.nextDouble()*nearestRect.getWidth()+nearestRect.getMinX();
				double y=numGenerator.nextDouble()*nearestRect.getHeight()+nearestRect.getMinY();
				angles.add(numGenerator.nextDouble()*360);
				for (int i=1;i<node.getTheta().size();i++) {
					if (convexity)
						angles.add(numGenerator.nextDouble()*180);
					else
						angles.add(numGenerator.nextDouble()*180+180);
				}
				result=new Node(x,y,angles);
				System.out.println("Inside Nearest Rect: ");
				System.out.println(nearestRect);
			}while(!CollisionDetect.isNodeConfigValid(result));
			return (result);
		}else {
			do{
				angles =new ArrayList<Double>();
				boolean convexity=nodeConvexity(node);
				double x=numGenerator.nextDouble();
				double y=numGenerator.nextDouble();
				angles.add(numGenerator.nextDouble()*360);
				for (int i=1;i<node.getTheta().size();i++) {
					if (convexity)
						angles.add(numGenerator.nextDouble()*180);
					else
						angles.add(numGenerator.nextDouble()*180+180);
				}
				result=new Node(x,y,angles);
				//System.out.println("Inside "+Integer.toString(count));
			}while(!CollisionDetect.isNodeConfigValid(result));
			return (result);
		}
	}
	
	private Rectangle2D nearestNarrowRegion(Node prevNode) {
		Rectangle2D rect = null;
		double minDist = 2;
		for (int i=0;i<this.narrowAreas.getNarrowRegions().size();i++) {
			double xNarrow = this.narrowAreas.getNarrowRegions().get(i).getCenterX();
			double yNarrow = this.narrowAreas.getNarrowRegions().get(i).getCenterY();
			double dist = prevNode.getLocation().distanceSq(xNarrow, yNarrow);
			if (dist<minDist) {
				minDist = dist;
				rect = this.narrowAreas.getNarrowRegions().get(i);
			}
		}
		return rect;
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
		int counter=0;
		do {
			angles.clear();
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
			counter++;
			//System.out.println("Random Gen: "+Integer.toString(counter));
		}while(!CollisionDetect.isNodeValid(result,obstacles) && counter<this.RANDOM_GAUSS_LIMIT);
		if (CollisionDetect.isNodeValid(result,obstacles))
			return result;
		return null;
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
	
	//Queries
	/**
	 * 
	 * @return Goal Node
	 */
	public Node getGoalNode() {
		return this.goalNode;
	}
	
	public ArrayList<Node> getRandomWalked(){
		return randomWalked;
	}
	
	/*
	public ArrayList<Node> getNodeList(Node last,ArrayList<Node> theList){
		if (last.parent==null) {
			theList.add(last);
			return theList;
		}
		theList.add(last);
		return getNodeList(last.getParent(),theList);
	}*/
	
	public ArrayList<Node> getNodeList(Node last){
		ArrayList<Node> nodeList = new ArrayList<Node>();
		Node end = last.clone();
		nodeList.add(end);
		while (end.getParent()!=null) {
			nodeList.add(end.getParent());
			end = end.getParent().clone();
		}
		return nodeList;
	}
	
	public ArrayList<ASVConfig> finalSolution(ArrayList<Node> nodePath) {
		ArrayList<ASVConfig> finalPath = new ArrayList<ASVConfig>();
		Collections.reverse(nodePath);
		for (Node nd : nodePath) {
			finalPath.add(nd.getConfigCoords());
		}
		return finalPath;
	}
	
	/**
	 * Return a list of the transition Nodes between two arbitrary Nodes.
	 * @param nodeA
	 * @param nodeB
	 * @return
	 */
	private ArrayList<Node> smoothPath(Node nodeA, Node nodeB) { 
		Edge edge = new Edge(nodeA, nodeB);
		Node inter = edge.middleNode();
		ArrayList<Node> smooth = new ArrayList<Node>();
		if(edge.getDistance() <= 0.001) {
			ArrayList<Node> fin = new ArrayList<Node>();
			fin.add(nodeA);
			fin.add(nodeB);
			smooth.addAll(fin);
			return smooth;
		}
		else
			return nodeListUnion(smoothPath(nodeA, inter), 
					smoothPath(inter, nodeB));
	}
	
	private ArrayList<Node> nodeListUnion(ArrayList<Node> a, 
			ArrayList<Node> b) {
		ArrayList<Node> union = new ArrayList<Node>();
		union.addAll(a);
		union.addAll(b);
		return union;
		
	}
	
	public ArrayList<Node> completePath(ArrayList<Node> nodeList) {
		ArrayList<Node> theList = new ArrayList<Node>();
		for (int i=0;i<nodeList.size()-2;i++) {
			theList.addAll(smoothPath(nodeList.get(i),nodeList.get(i+1)));
		}
		return theList;
	}
	
	/**
	 * Obtains Narrow passage threshold based on regular polygon diameter
	 * @return double
	 */
	private double obtainNarrowThreshold() {
		int sides = this.initNode.getConfigCoords().getASVCount()-1;
		return (BROOM_LENGTH/Math.sin(Math.PI/sides));
	}
	
}
