package problem;
import java.io.IOException;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.ArrayList;
public class cellDecomp {
	
	public static List<Point2D> getPoints(List<Obstacle> obstacles){
		List<Obstacle> obst=new ArrayList<Obstacle>();
		obst.addAll(obstacles);
		obst.add(new Obstacle("0.0 0.0 1.0 0.0 0.0 -0.01 1.0 -0.01"));
		obst.add(new Obstacle("1.0 0.0 1.01 0.0 1.01 1.0 1.0 1.0"));
		obst.add(new Obstacle("0.0 1.0 1.0 1.0 1.0 1.01 0.0 1.01"));
		obst.add(new Obstacle("-0.01 0.0 0.0 0.0 0.0 1.0 -0.01 1.0"));
		
		
		/**
		 * Vertical lines sweep
		 */
		List<Point2D> pointsVertSweep=new ArrayList<Point2D>();
		for (Obstacle obstacle:obst) {
			int counter=0;
			for (Point2D vertix:obstacle.getPoints()) {
				if (!((vertix.getX()==0)||(vertix.getX()==1)||(vertix.getY()==0)||(vertix.getY()==1))) {
					Point2D transcient=new Point2D.Double(2,2);
					for (Obstacle iterobstacle:obst) {
						if (!iterobstacle.equals(obstacle)) {
							if (iterobstacle.getRect().intersectsLine(vertix.getX(), 0, vertix.getX(), 1) 
									&& !iterobstacle.getRect().intersectsLine(0, vertix.getY(), 1, vertix.getY())) {
								if ((counter==0 || counter==1)&&(vertix.getY()>iterobstacle.getRect().getMaxY())) {
									Point2D intersection=new Point2D.Double(vertix.getX(),iterobstacle.getRect().getMaxY());
									if (vertix.distance(intersection)<vertix.distance(transcient)) {
										transcient=new Point2D.Double((vertix.getX()+intersection.getX())/2,(vertix.getY()+intersection.getY())/2);
									}
									
								}else if((counter==2 || counter==3)&&(vertix.getY()<iterobstacle.getRect().getMinY())) {
									Point2D intersection=new Point2D.Double(vertix.getX(),iterobstacle.getRect().getMinY());
									if (vertix.distance(intersection)<vertix.distance(transcient)) {
										transcient=new Point2D.Double((vertix.getX()+intersection.getX())/2,(vertix.getY()+intersection.getY())/2);
									}
								}
							}
						}
					}
					if (transcient.getX()<=1 && !pointsVertSweep.contains(transcient) && !isPointColliding(obstacles,transcient)) {
						pointsVertSweep.add(transcient);
					}
				}
				counter++;
			}
		}
		
		/**
		 * Horizontal lines sweep
		 */
		
		
		List<Point2D> pointsHoriSweep=new ArrayList<Point2D>();
		for (Obstacle obstacle:obst) {
			int counter=0;
			for (Point2D vertix:obstacle.getPoints()) {
				if (!((vertix.getX()==0)||(vertix.getX()==1)||(vertix.getY()==0)||(vertix.getY()==1))) {
					Point2D transcient=new Point2D.Double(2,2);
					for (Obstacle iterobstacle:obst) {
						if (!iterobstacle.equals(obstacle)) {
							if (iterobstacle.getRect().intersectsLine(0,vertix.getY(), 1 , vertix.getY()) 
									&& !iterobstacle.getRect().intersectsLine(vertix.getX(),0, vertix.getX(),1)) {
								if ((counter==0 || counter==3)&&(vertix.getX()>iterobstacle.getRect().getMaxX())) {
									Point2D intersection=new Point2D.Double(iterobstacle.getRect().getMaxX(),vertix.getY());
									if (vertix.distance(intersection)<vertix.distance(transcient)) {
										transcient=new Point2D.Double((vertix.getX()+intersection.getX())/2,(vertix.getY()+intersection.getY())/2);
									}
									
								}else if((counter==1 || counter==2)&&(vertix.getX()<iterobstacle.getRect().getMinX())) {
									Point2D intersection=new Point2D.Double(iterobstacle.getRect().getMinX(),vertix.getY());
									if (vertix.distance(intersection)<vertix.distance(transcient)) {
										transcient=new Point2D.Double((vertix.getX()+intersection.getX())/2,(vertix.getY()+intersection.getY())/2);
									}
								}
							}
						}
					}
					if (transcient.getX()<=1 && !pointsHoriSweep.contains(transcient)&& !isPointColliding(obstacles,transcient)) {
						pointsHoriSweep.add(transcient);
					}
				}
				counter++;
			}
		}
		
		
		pointsVertSweep.addAll(pointsHoriSweep);
		return pointsVertSweep;
	}
	
	private static Boolean isPointColliding(List<Obstacle> obstacles,Point2D point) {
		for (Obstacle obs:obstacles) {
			//if (obs.getRect().contains(point.getX(), point.getY())) {
			if (obs.getRect().intersectsLine(point.getX(), point.getY(),point.getX(), point.getY())) {
				return true;
			}
		}
		return false;
	}
	
	
	public static void main(String[] args) throws IOException {
		ProblemSpec problem=new ProblemSpec();
		String address="testcases\\3ASV.txt";
		problem.loadProblem(address);
		
		List<Point2D> thePoints=getPoints(problem.getObstacles());
		
		System.out.println("Points Length: "+Integer.toString(thePoints.size()));
		
		for (Point2D point:thePoints) {
			System.out.println("Point "+" ["+Double.toString(point.getX())+","+Double.toString(point.getY())+"]");
		}
		
	}	
	
}
