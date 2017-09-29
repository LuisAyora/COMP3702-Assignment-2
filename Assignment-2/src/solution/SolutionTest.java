package solution;

import problem.*;

import java.io.IOException;
import java.util.*;

public class SolutionTest {
	// Dummy test method - Delete this
	public static void main(String[] args) throws IOException{
		ProblemSpec problemData = new ProblemSpec();
		String directory="testcases//";
		String fileName="7-ASV-x4.txt";
		
		long time = System.currentTimeMillis();
		
		String address=directory+fileName;
		problemData.loadProblem(address);

		ASVAngle initAngles = new ASVAngle(problemData.getInitialState());
		ASVAngle goalAngles = new ASVAngle(problemData.getGoalState());

		Node initNode = new Node(problemData.getInitialState().getASVPositions().get(0).getX(),
				problemData.getInitialState().getASVPositions().get(0).getY(),initAngles.getThetaAngles());
		Node goalNode = new Node(problemData.getGoalState().getASVPositions().get(0).getX(),
				problemData.getGoalState().getASVPositions().get(0).getY(),goalAngles.getThetaAngles());
		
		PathFinder pathFinder = new PathFinder(initNode, goalNode, problemData.getObstacles());
		
		pathFinder.navigate();
		ArrayList<Node> solPath=pathFinder.getNodeList(pathFinder.getGoalNode());
		
		problemData.setPath(pathFinder.finalSolution(pathFinder.completePath(solPath)));
		System.out.println("Time runing Algorithm: " +Double.toString((System.currentTimeMillis()-time)/1000)+" s");
		System.out.println("Saving File, Please wait");
		problemData.saveSolution(directory+"Solution-"+fileName);
		System.out.println("File Saved :)");
		System.out.println("Time Including saving: " +Double.toString((System.currentTimeMillis()-time)/1000)+" s");
	} 
}

	
