package solution;

import problem.*;

import java.io.IOException;
import java.util.*;

public class SolutionTest {
	// Dummy test method - Delete this
	public static void main(String[] args) throws IOException{
		ProblemSpec problemData = new ProblemSpec();
		String directory="testcases//";
		String fileName="3ASV.txt";
		
		
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
		
		problemData.setPath(pathFinder.finalSolution(solPath));
		problemData.saveSolution("testcases//"+"Solution-"+fileName);			

	}
}

	
