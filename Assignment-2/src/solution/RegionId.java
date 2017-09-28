package solution;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import problem.Obstacle;

public class RegionId {
	
	private double narrownessThreshold;
	private double lowerThreshold;
	private List <Rectangle2D> narrowRegions;
	
	public RegionId(int n,double threshold,List<Obstacle> obsts) {
		narrownessThreshold = threshold;
		setLowerThreshold(n);
		narrowRegions = overallNarrowRegions(obsts);
	}
	
	/**
	 * Returns list narrow regions scanning in the X direction
	 * @param obstacles
	 * @return List<Rectangle2D>
	 */
	public List<Rectangle2D> narrowRegionsX(List<Obstacle> obstacles){
		List<Rectangle2D> regions = new ArrayList<Rectangle2D>();
		List<Obstacle> obstWithBounds = new ArrayList<Obstacle>();
		obstWithBounds.addAll(obstacles);
		obstWithBounds.add(new Obstacle("0.0 -0.01 1.0 -0.01 1.0 0.0 0.0 0.0"));
		obstWithBounds.add(new Obstacle("1.0 0.0 1.01 0.0 1.01 1.0 1.0 1.0"));
		obstWithBounds.add(new Obstacle("0.0 1.0 1.0 1.0 1.0 1.01 0.0 1.01"));
		obstWithBounds.add(new Obstacle("-0.01 0.0 0.0 0.0 0.0 1.0 -0.01 1.0"));
		
		for (int i=0;i<obstacles.size();i++) {
			Rectangle2D rectUp = null;
			Rectangle2D rectDown = null;
			double minDistUp = 1;
			double minDistDown = 1;
			for (int j=i+1;j<obstWithBounds.size();j++) {
				if (intersectX(obstacles.get(i),obstWithBounds.get(j)) && 
						!intersectY(obstacles.get(i),obstWithBounds.get(j))) {
					//Obstacles on top of current obstacle
					if (obstWithBounds.get(j).getRect().getMinY()>obstacles.get(i).getRect().getMaxY()) {
						double x = Math.max(obstWithBounds.get(j).getRect().getMinX(), obstacles.get(i).getRect().getMinX());
						double y = obstacles.get(i).getRect().getMaxY();
						double h = obstWithBounds.get(j).getRect().getMinY()-obstacles.get(i).getRect().getMaxY();
						double w = Math.min(obstWithBounds.get(j).getRect().getMaxX(),
								obstacles.get(i).getRect().getMaxX())-x;
						Rectangle2D rect = new Rectangle2D.Double(x,y,w,h);
						if (rect.getHeight()<minDistUp) {
							rectUp = rect;
							minDistUp = rect.getHeight();
						}
					
					//Obstacles on bottom of current obstacle
					} else if(obstWithBounds.get(j).getRect().getMaxY()<obstacles.get(i).getRect().getMinY()) {
						double x = Math.max(obstWithBounds.get(j).getRect().getMinX(), obstacles.get(i).getRect().getMinX());
						double y = obstWithBounds.get(j).getRect().getMaxY();
						double h = obstacles.get(i).getRect().getMinY()-obstWithBounds.get(j).getRect().getMaxY();
						double w = Math.min(obstWithBounds.get(j).getRect().getMaxX(),
								obstacles.get(i).getRect().getMaxX())-x;
						Rectangle2D rect = new Rectangle2D.Double(x,y,w,h);
						if (rect.getHeight()<minDistDown) {
							rectDown = rect;
							minDistDown = rect.getHeight();
						}
							
					}
				}
			}	
			if (rectUp!=null && 
					rectUp.getHeight()<narrownessThreshold
					&& rectUp.getHeight()>lowerThreshold)
				regions.add(rectUp);
			if (rectDown!=null && 
					rectDown.getHeight()<narrownessThreshold &&
					rectDown.getHeight()>lowerThreshold)
				regions.add(rectDown);
		}
		
		return regions;
	}
	
	/**
	 * Returns list narrow regions scanning in the Y direction
	 * @param obstacles
	 * @return List<Rectangle2D>
	 */
	public List<Rectangle2D> narrowRegionsY(List<Obstacle> obstacles){
		List<Rectangle2D> regions = new ArrayList<Rectangle2D>();
		List<Obstacle> obstWithBounds = new ArrayList<Obstacle>();
		obstWithBounds.addAll(obstacles);
		obstWithBounds.add(new Obstacle("0.0 -0.01 1.0 -0.01 1.0 0.0 0.0 0.0"));
		obstWithBounds.add(new Obstacle("1.0 0.0 1.01 0.0 1.01 1.0 1.0 1.0"));
		obstWithBounds.add(new Obstacle("0.0 1.0 1.0 1.0 1.0 1.01 0.0 1.01"));
		obstWithBounds.add(new Obstacle("-0.01 0.0 0.0 0.0 0.0 1.0 -0.01 1.0"));
		
		for (int i=0;i<obstacles.size();i++) {
			Rectangle2D rectRight = null;
			Rectangle2D rectLeft = null;
			double minDistRight = 1;
			double minDistLeft = 1;
			for (int j=i+1;j<obstWithBounds.size();j++) {
				if (intersectY(obstacles.get(i),obstWithBounds.get(j)) && 
						!intersectX(obstacles.get(i),obstWithBounds.get(j))) {
					//Obstacles on the right of current obstacle
					if (obstWithBounds.get(j).getRect().getMinX()>obstacles.get(i).getRect().getMaxX()) {
						double y = Math.max(obstWithBounds.get(j).getRect().getMinY(), obstacles.get(i).getRect().getMinY());
						double x = obstacles.get(i).getRect().getMaxX();
						double h = Math.min(obstWithBounds.get(j).getRect().getMaxY(),
								obstacles.get(i).getRect().getMaxY())-y;
						double w = obstWithBounds.get(j).getRect().getMinX()-obstacles.get(i).getRect().getMaxX();
						Rectangle2D rect = new Rectangle2D.Double(x,y,w,h);
						if (rect.getWidth()<minDistRight) {
							rectRight = rect;
							minDistRight = rect.getWidth();
						}
					
					//Obstacles on the Left of current obstacle
					} else if(obstWithBounds.get(j).getRect().getMaxX()<obstacles.get(i).getRect().getMinX()) {
						double y = Math.max(obstWithBounds.get(j).getRect().getMinY(), obstacles.get(i).getRect().getMinY());
						double x = obstWithBounds.get(j).getRect().getMaxX();
						double h = Math.min(obstWithBounds.get(j).getRect().getMaxY(),
								obstacles.get(i).getRect().getMaxY())-y;
						double w = obstacles.get(i).getRect().getMinX()-obstWithBounds.get(j).getRect().getMaxX();
						Rectangle2D rect = new Rectangle2D.Double(x,y,w,h);
						if (rect.getWidth()<minDistLeft) {
							rectLeft = rect;
							minDistLeft = rect.getWidth();
						}
							
					}
				}
			}
			if (rectRight!=null && 
					rectRight.getWidth()<narrownessThreshold && 
					rectRight.getWidth()>lowerThreshold)
				regions.add(rectRight);
			if (rectLeft!=null && 
					rectLeft.getWidth()<narrownessThreshold && 
					rectLeft.getWidth()>lowerThreshold)
				regions.add(rectLeft);
		}
		
		return regions;
	}
	
	/**
	 * Returns list of narrow regions obtained scanning in the X and Y directions
	 * @return List<Rectangle2D)
	 */
	public List<Rectangle2D> overallNarrowRegions(List<Obstacle> obstacles){
		List<Rectangle2D> mergedList = new ArrayList<Rectangle2D>();
		mergedList.addAll(narrowRegionsX(obstacles));
		mergedList.addAll(narrowRegionsY(obstacles));
		return mergedList;
	}
	
	/**
	 * Tests whether two objects intersect in the X direction
	 * @param obsA
	 * @param obsB
	 * @return boolean
	 */
	private boolean intersectX(Obstacle obsA,Obstacle obsB) {
		if (obsA.getRect().getMinX()<=obsB.getRect().getMaxX() && 
				obsA.getRect().getMinX()>=obsB.getRect().getMinX())
			return true;
		if (obsA.getRect().getMaxX()<=obsB.getRect().getMaxX() && 
				obsA.getRect().getMaxX()>=obsB.getRect().getMinX())
			return true;
		
		if (obsB.getRect().getMinX()<=obsA.getRect().getMaxX() && 
				obsB.getRect().getMinX()>=obsA.getRect().getMinX())
			return true;
		if (obsB.getRect().getMaxX()<=obsA.getRect().getMaxX() && 
				obsB.getRect().getMaxX()>=obsA.getRect().getMinX())
			return true;
		return false;
	}
	
	/**
	 * Tests whether two obstacles intersect in the Y direction
	 * @param obsA
	 * @param obsB
	 * @return boolean
	 */
	private boolean intersectY(Obstacle obsA,Obstacle obsB) {
		if (obsA.getRect().getMinY()<=obsB.getRect().getMaxY() && 
				obsA.getRect().getMinY()>=obsB.getRect().getMinY())
			return true;
		if (obsA.getRect().getMaxY()<=obsB.getRect().getMaxY() && 
				obsA.getRect().getMaxY()>=obsB.getRect().getMinY())
			return true;
		
		if (obsB.getRect().getMinY()<=obsA.getRect().getMaxY() && 
				obsB.getRect().getMinY()>=obsA.getRect().getMinY())
			return true;
		if (obsB.getRect().getMaxY()<=obsA.getRect().getMaxY() && 
				obsB.getRect().getMaxY()>=obsA.getRect().getMinY())
			return true;
		return false;
	}
	
	//Queries
	/**
	 * getter of narrowRegions
	 * @return List<Rectangle2D>
	 */
	public List<Rectangle2D> getNarrowRegions() {
		return this.narrowRegions;
	}
	
	/**
	 * Set the height threshold by minimising the Area of a trapezoid formed by
	 * the ASV chain using the Newton-Raphson method.
	 * @require that the number of ASVs is larger than or equal to 3
	 * @ensure that the height will be an approximation of the local minimum
	 * 		   and a positive number
	 * @param n: the number of  ASVs in the chain
	 */
	private void setLowerThreshold(int n){
		if(n >= 3) {
			double L = 0.005; //Standard broom length
			double theta = 15.0*(Math.PI/360.0); // Initial guess for solution
			double Ai = (n - 3) * Math.sin(theta) + 0.5 * Math.sin(2 * theta);
			double Ai_dash = (n - 3) * Math.cos(theta) + Math.cos(2 * theta);
			// Iterate with Newton-Raphson until desire tolerance is achieved
			while(Ai_dash < 0.0001) {
				theta -= Ai/Ai_dash;
				Ai = (n - 3) * Math.sin(theta) + 0.5 * Math.sin(2 * theta);
				Ai_dash = (n - 3) * Math.cos(theta) + Math.cos(2 * theta);
			}
			// Set the height threshold
			lowerThreshold = Math.sin(theta) * L;
		}
		else
			lowerThreshold = 0;
	}
	
}
