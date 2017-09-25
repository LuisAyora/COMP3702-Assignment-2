package solution;

import problem.*;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.*;

public class SolutionTest {
	// Dummy test method - Delete this
	public static void main(String[] args) throws IOException{
		// Generate two ASVConfig object
		/*
		ASVConfig config = new ASVConfig(7, 
						"0.193301 0.238602 0.15 0.213602 0.125 0.170301 0.150 0.127 "
						+ "0.200 0.127 0.225 0.170301 0.225 0.220301");	
		ASVConfig config2 = new ASVConfig(7, 
					"0.893301 0.238602 0.85 0.213602 0.825 0.170301 0.850 "
					+ "0.127 0.900 0.127 0.925 0.170301 0.925 0.220301");
		
		ASVConfig config3 = new ASVConfig(7, 
				"0.193301 0.538602 0.15 0.513602 0.125 0.470301 0.150 0.427 "
				+ "0.200 0.427 0.225 0.470301 0.225 0.520301");	
			
		//Load problem to have the obstacles
		ProblemSpec tet1 = new ProblemSpec();
		String address="testcases//7ASV.txt";
		tet1.loadProblem(address);
			
		//Printing configuration
		System.out.println("First configuration: "+config.toString());
		System.out.println("Second configuration: "+config2.toString());
		System.out.println("Third configuration: "+config3.toString());	
		//Generate angles
		ASVAngle angles = new ASVAngle(config);
		System.out.println("First angles: "+angles.getThetaAngles());
			
		ASVAngle angles2 = new ASVAngle(config2);
		System.out.println("Second angles: "+angles2.getThetaAngles());
		
		ASVAngle angles3 = new ASVAngle(config3);
		System.out.println("Third angles: "+angles3.getThetaAngles());
		
		//creation of NODES
		Node n1=new Node(config.getASVPositions().get(0).getX(),config.getASVPositions().get(0).getY(),
						angles.getThetaAngles());
		Node n2 = new Node(config2.getASVPositions().get(0).getX(),config2.getASVPositions().get(0).getY(),
						angles2.getThetaAngles());
		Node n3 = new Node(config3.getASVPositions().get(0).getX(),config3.getASVPositions().get(0).getY(),
				angles3.getThetaAngles());
		
		//creation of edges
		Edge e1 = new Edge(n1, n2);
		Node middle=e1.middleNode();
		Edge e3 = new Edge(n1, middle);
		Edge e4 = new Edge (n1,n3);
		Node middle2 =e4.middleNode();
		Edge e5 = new Edge (n1,middle2);
				
		//check collision
		System.out.println("Obstacles list: \n"+tet1.getObstacles().toString());
		//System.out.println("Is convex?: \n"+Boolean.toString(CollisionDetect.isConvex(n1)));
		
		System.out.println("Collision free node?: \n"+Boolean.toString(CollisionDetect.isNodeValid(n2,tet1.getObstacles())));
		//System.out.println("Edge free?: \n"+ );
		System.out.println("Middle node: "+ middle2.toString());
		System.out.println("Edge free?: \n"+Boolean.toString(CollisionDetect.isEdgeValid(e1,tet1.getObstacles())));
		System.out.println("Edge valid?: \n"+(CollisionDetect.furthestValidEdge(e1,tet1.getObstacles())));
			
		Edge e6 = CollisionDetect.furthestValidEdge(e1,tet1.getObstacles());
		System.out.println("Edge free?: \n"+Boolean.toString(CollisionDetect.isEdgeValid(e6,tet1.getObstacles())));
					
		//pathfinder checking
		PathFinder path1 = new PathFinder(n1, n2, tet1.getObstacles());
		Node n4 = path1.randomGaussianNode(n1, 0.01);
		System.out.println(n4.toString());
		//System.out.println(n3.toString());
		/*
		int count = 0;
		
		while(CollisionDetect.isConvex(n1) && count < 1000000) {
			count += 1;
			System.out.println("count is: " +
			    Integer.toString(count));
			n1 = path1.randomGaussianNode(n1, 1.0/360.0);
		}
		
		System.out.println(n1.toString());
		*/
		
		//path1.navigate();
		
		/*
		
		/*
		 * Test for 3ASV-easy 
		 * */
		
		// Initial conf
		ASVConfig c31 = new ASVConfig(3, "0.150 0.225 0.150 0.275 0.200 0.275 ");	
		//final conf
		ASVConfig c32 = new ASVConfig(3, "0.850 0.225 0.850 0.275 0.900 0.275");
		//initial with more 'y' 
		ASVConfig c33 = new ASVConfig(3, "0.150 0.775 0.150 0.775 0.200 0.575");
		//initial config to be in front of the middle space between obstacles			
		ASVConfig c34 = new ASVConfig(3, "0.150 0.46 0.150 0.510 0.200 0.510");
		//initial config to be at the end of the middle space between obstacles			
		ASVConfig c35 = new ASVConfig(3, "0.850 0.46 0.850 0.510 0.900 0.510");
		
		//Load problem to have the obstacles
		ProblemSpec test = new ProblemSpec();
		String address3="testcases//3ASV-easy.txt";
		test.loadProblem(address3);
					
		//Printing configuration
		System.out.println("1 configuration 3ASV: "+c31.toString());
		System.out.println("2 configuration 3ASV: "+c32.toString());
		System.out.println("3 configuration 3ASV: "+c33.toString());
		System.out.println("4 configuration 3ASV: "+c34.toString());
		System.out.println("5 configuration 3ASV: "+c35.toString());
		//Generate angles
		ASVAngle a31 = new ASVAngle(c31);
		System.out.println("First angles 3ASV: "+a31.getThetaAngles());
					
		ASVAngle a32 = new ASVAngle(c32);
		System.out.println("Second angles: "+a32.getThetaAngles());
				
		//Same x position for ASV 31 but changed in y direction
		ASVAngle a33 = new ASVAngle(c33);
		System.out.println("Third angles: "+a33.getThetaAngles());
		
		//In front of the space between obstacles
		ASVAngle a34 = new ASVAngle(c34);
		System.out.println("4 angles: "+a34.getThetaAngles());
		
		//At the end of the space
		ASVAngle a35 = new ASVAngle(c35);
		System.out.println("5 angles: "+a35.getThetaAngles());
		
		//creation of NODES
		Node n31=new Node(c31.getASVPositions().get(0).getX(),c32.getASVPositions().get(0).getY(),
						  a31.getThetaAngles());
		Node n32 = new Node(c32.getASVPositions().get(0).getX(),c32.getASVPositions().get(0).getY(),
							a32.getThetaAngles());
		Node n33 = new Node(c33.getASVPositions().get(0).getX(),c33.getASVPositions().get(0).getY(),
							a33.getThetaAngles());
		Node n34 = new Node(c34.getASVPositions().get(0).getX(),c34.getASVPositions().get(0).getY(),
				a34.getThetaAngles());
		Node n35 = new Node(c35.getASVPositions().get(0).getX(),c35.getASVPositions().get(0).getY(),
				a35.getThetaAngles());
		//creation of edges
		//test between initial and final node
		Edge e31 = new Edge(n31, n32);
		//middle node from edge from initial to end
		Node middle31 = e31.middleNode();
		
		//edge from initial to middle
		Edge e32 = new Edge(n31, middle31);
		
		//edge from initial to edge in the same direction up
		Edge e33 = new Edge(n31, n33);
		
		//edge from node located in the space between obstacles and final node
		Edge e34 =  new Edge(n34,n32);
		Node middle32 = e34.middleNode();
		
		//Test horizontal
		//edge from node located in the space between obstacles and final node
		Edge e35 =  new Edge(n34,n35);
		Node middle33 = e35.middleNode();
		
		//check collision
		System.out.println("Obstacles 3ASV list: \n"+test.getObstacles().toString());
				
		System.out.println("Collision free node?: \n"+Boolean.toString(CollisionDetect.isNodeValid(n34,test.getObstacles())));
		
		System.out.println("Middle node: "+ middle32.toString());
		System.out.println("Edge free?: \n"+Boolean.toString(CollisionDetect.isEdgeValid(e35,test.getObstacles())));
					
		Edge e36 = CollisionDetect.furthestValidEdge(e35,test.getObstacles());
		System.out.println("Edge valid location with furthestEdge?: \n"+(e36.toString()));
		System.out.println("Edge free with furthest edge?: \n"+Boolean.toString(CollisionDetect.isEdgeValid(e36,test.getObstacles())));
							
		//pathfinder checking
		PathFinder path31 = new PathFinder(n34, n32, test.getObstacles());
		//Node n34 = path31.randomGaussianNode(n31, 0.01);
		//System.out.println(n34.toString());
				
				
		path31.navigate();
		Node fina = path31.getGoalNode();
		System.out.println("SecondLast: \n");
		System.out.println(fina.getParent());
		System.out.println(fina.getParent().getParent());
		List<Node> nodeList=getNodeList(path31.getGoalNode(),new ArrayList<Node>());
		for (int i=0;i<nodeList.size();i++) {
			System.out.println(nodeList.get(i));
		}
	}
	
public static ArrayList<Node> getNodeList(Node last,ArrayList<Node> theList){
	if (last.parent==null)
		return theList;
	theList.add(last);
	return getNodeList(last.getParent(),theList);
}
	
}