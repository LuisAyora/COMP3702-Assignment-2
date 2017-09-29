package solution;

import problem.*;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		// Initialise ProblemSpec instance and load the given environment
		System.out.println(System.getProperty("user.dir"));
		ProblemSpec problem = new ProblemSpec();
		try {
			problem.loadProblem(args[0]);
		} catch(Exception e) {
			System.out.println("Could not find file. Please reenter filename");
		}
		
		/*Me love you*/ long time = System.currentTimeMillis();
		
		// Define initial required objects to run the navigation application
		ASVAngle initAngles = new ASVAngle(problem.getInitialState());
		ASVAngle goalAngles = new ASVAngle(problem.getGoalState());
		
		Node initNode = new Node(problem.getInitialState().getASVPositions().get(0).getX(),
				problem.getInitialState().getASVPositions().get(0).getY(), 
				initAngles.getThetaAngles());
		Node goalNode = new Node(problem.getGoalState().getASVPositions().get(0).getX(),
				problem.getGoalState().getASVPositions().get(0).getY(),
				goalAngles.getThetaAngles());
		
		// Initialise a PathFinder object to navigate the environment
		PathFinder pf = new PathFinder(initNode, goalNode, problem.getObstacles());
		System.out.println("Navigating the environment");
		pf.navigate();
		
		// Generate a solution file for the user
		ArrayList<Node> solPath = pf.getNodeList(pf.getGoalNode());
		problem.setPath(pf.finalSolution(pf.completePath(solPath)));
		System.out.println("Query solved in " + 
		    Double.toString((System.currentTimeMillis() - time)/1000) + " s");
		System.out.println("Saving solution file, please wait =)");
		try {
			problem.saveSolution(System.getProperty("user.dir")
					+ "//query-solution.txt");
		} catch(Exception e) {
			System.out.println("Something has gone horribly wrong! Please"
					+ "run the application again");
		}
		System.out.println("File saved :)");
		System.out.println("Total runtime: " +
		    Double.toString((System.currentTimeMillis() - time)/1000) + " s");
	}
}
