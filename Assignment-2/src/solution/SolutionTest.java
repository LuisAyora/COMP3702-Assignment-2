package solution;

import problem.*;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.*;

public class SolutionTest {
	// Dummy test method - Delete this
	public static void main(String[] args) throws IOException{
		// Generate two ASVConfig object
		ASVConfig config = new ASVConfig(7, 
						"0.193301 0.238602 0.15 0.213602 0.125 0.170301 0.150 0.127 "
						+ "0.200 0.127 0.225 0.170301 0.225 0.220301");	
		ASVConfig config2 = new ASVConfig(7, 
					"0.893301 0.238602 0.85 0.213602 0.825 0.170301 0.850 "
					+ "0.127 0.900 0.127 0.925 0.170301 0.925 0.220301");
			
		//Load problem to have the obstacles
		ProblemSpec tet1 = new ProblemSpec();
		String address="testcases//7ASV.txt";
		tet1.loadProblem(address);
			
		//Printing configuration
		System.out.println("First configuration: "+config.toString());
		System.out.println("Second configuration: "+config2.toString());
			
		//Generate angles
		ASVAngle angles = new ASVAngle(config);
		System.out.println("First angles: "+angles.getThetaAngles());
			
		ASVAngle angles2 = new ASVAngle(config2);
		System.out.println("Second angles: "+angles2.getThetaAngles());
			
		//creation of NODES
		Node n1=new Node(config.getASVPositions().get(0).getX(),config.getASVPositions().get(0).getY(),
						angles.getThetaAngles());
		Node n2 = new Node(config2.getASVPositions().get(0).getX(),config2.getASVPositions().get(0).getY(),
						angles2.getThetaAngles());
			
		//creation of edges
		Edge e1 = new Edge(n1, n2);
		Node middle=e1.middleNode();
				
		//check collision
		System.out.println("Collision free?: \n"+Boolean.toString(CollisionDetect.isNodeValid(middle,tet1.getObstacles())));
		System.out.println("Edge free?: \n"+Boolean.toString(CollisionDetect.isEdgeValid(e1,tet1.getObstacles())));
		//System.out.println("Edge valid?: \n"+(CollisionDetect.furthestValidEdge(e1,tet1.getObstacles())));
			
		Edge e2 = CollisionDetect.furthestValidEdge(e1,tet1.getObstacles());
		System.out.println("Edge free?: \n"+Boolean.toString(CollisionDetect.isEdgeValid(e2,tet1.getObstacles())));
					
		//pathfinder checking
		//PathFinder path1 = new PathFinder(n1, n2, tet1.getObstacles());
		//Node n3 = path1.randomGaussianNode(n1, 1.0/360.0);
		//System.out.println(n1.toString());
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
		
	
		}
	
}

