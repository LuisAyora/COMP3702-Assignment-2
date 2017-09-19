package problem;

import java.awt.geom.Point2D;
import java.util.*;

/**
 * 
 * @author The A-Team
 *
 * A mutable class that contains the angles of the brooms with respect with the
 * ASVs. Initially, the broom will be considered to have t = n - 1 brooms, 
 * where n is the number of ASVs
 */
public class ASVAngle {
	private ASVConfig config;
	private ArrayList<Double> alphaAngles;
	private ArrayList<Double> thetaAngles;
	
	/**
	 * Constructor:
	 * 
	 * Create a new ASVAngle object calculating the angles of the initial 
	 * state.
	 * @param ProblemSpec: the problem specification that the class will
	 * 		  be working with
	 * @return an ASVAngle object
	 */
	public ASVAngle(ASVConfig config) {
		this.config = config;
		alphaAngles = calculateAlphaAngles(this.config.getASVPositions());
		thetaAngles = calculateThetaAngles(alphaAngles);
	}
	
	// Queries
	
	/**
	 * Return the configuration of the ASV
	 * @return ASVConfig 
	 */
	public ASVConfig getConfig() {
		// Something something, I got 99 problems something...
		return config;
	}
	
	/**
	 * 
	 * Return the alpha angles of the ASVs
	 * @return ArrayList<Double>
	 */
	public ArrayList<Double> getAlphaAngles() {
		return alphaAngles;
	}
	
	/**
	 * Return the theta angles of the ASVs
	 * @return ArrayList<Double>
	 */
	public ArrayList<Double> getThetaAngles() {
		return thetaAngles;
	}
	
	// Commands
	
	/**
	 * Reset the values of the angles given a new configuration of the ASVs
	 * @param config
	 */
	void updateStatus(ASVConfig newConfig) {
		config = newConfig;
		alphaAngles = this.calculateAlphaAngles(config.getASVPositions());
		thetaAngles = this.calculateThetaAngles(alphaAngles);
	}
	
	// Helper methods
	
	/**
	 * Calculate the values of the alpha angles of the ASVs
	 * @param config 
	 * @return ArrayList<Double>
	 */
	private ArrayList<Double> calculateAlphaAngles(List<Point2D> config) {
		ArrayList<Double> angles = new ArrayList<Double>();
		for(int i = 0; i < config.size() - 1; i++)
			angles.add(Math.toDegrees(Math.atan2(config.get(i+1).getY() - 
					config.get(i).getY(), config.get(i+1).getX() - 
					config.get(i).getX())));
		return angles;
	}
	
	/**
	 * Calculate the values of the theta angles of the ASVs
	 * @param alpha
	 * @return ArrayList<Double>
	 */
	private ArrayList<Double> calculateThetaAngles(ArrayList<Double> alpha) {
		ArrayList<Double> angles = new ArrayList<Double>();
		for(int i = 0; i < alpha.size(); i++) {
			if(i == 0)
				angles.add(alpha.get(i)/180);
			else
				angles.add(((alpha.get(i) - alpha.get(i - 1)) % 360)/180);
		}
		return angles;
	}
	
	// Dummy test method - Delete this
	public static void main(String[] args) {
		// Generate a simple ASVConfig object
		ASVConfig config = new ASVConfig(7, 
				"0.193301 0.238602 0.15 0.213602 0.125 0.170301 0.150 0.127 "
				+ "0.200 0.127 0.225 0.170301 0.225 0.220301");
		System.out.println(config.toString());
		
		// Generate a simple ASVAngles object
		ASVAngle angles = new ASVAngle(config);
		
		// Display the configuration, alpha and theta angles
		System.out.println(angles.getConfig().toString());
		System.out.println(angles.getAlphaAngles());
		System.out.println(angles.getThetaAngles());
		
		// Test the commands - Should I merge both into the one command?
		angles.updateStatus(new ASVConfig(7, 
				"0.893301 0.238602 0.85 0.213602 0.825 0.170301 0.850 "
				+ "0.127 0.900 0.127 0.925 0.170301 0.925 0.220301"));
		
		// Display the results of using the commands
		System.out.println(angles.getConfig().toString());
		System.out.println(angles.getAlphaAngles());
		System.out.println(angles.getThetaAngles());
	}
}
