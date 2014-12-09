package driver;

import nearestNeighbor.NearestNeighborAlgorithm;
import robotArm.Arm;
import robotArm.JointDirection;
import robotArm.Position;

/**
 * Class used to validate the accuracy of the algorithm
 * @author Tae Soo Kim, Steven Lin
 *
 */
public class DataCollection {
	private static int numNeighbors = 5;
	private static int numJoints = 2;
	public static void main(String[] args) {
		NearestNeighborAlgorithm nn = new NearestNeighborAlgorithm("test_twoArm_5000_YZ");
		double[] joints = {1,1};
		JointDirection[] dir = {JointDirection.Y, JointDirection.Z};
		Arm groundTruthArm = new Arm(joints, dir);
		Arm computingArm = new Arm(joints, dir);
		
		//ground truth computations
		Position groundTruthEndEffectorPosition = new Position();
		double[] groundTruthConfigs = createRandomConfiguration(numJoints);
		groundTruthArm.move(groundTruthConfigs, groundTruthEndEffectorPosition);
		
		//computed inverse kinematics
		double[] computedConfigs = nn.getConfiguration(groundTruthEndEffectorPosition, numNeighbors);
		Position computedEndEffectorPosition = new Position();
		computingArm.move(computedConfigs, computedEndEffectorPosition);
		
		double[] gtCoords = groundTruthEndEffectorPosition.getCoordinates();
		double[] cpCoords = computedEndEffectorPosition.getCoordinates();
		double xDiff = Math.abs(gtCoords[0] - cpCoords[0]);
		double yDiff = Math.abs(gtCoords[1] - cpCoords[1]);
		double zDiff = Math.abs(gtCoords[2] - cpCoords[2]);
		System.out.print("Error (x,y,z) : ");
		System.out.print(xDiff + "  ");
		System.out.print(yDiff + "  ");
		System.out.println(zDiff);
		double percentError = (Math.abs((xDiff/gtCoords[0])*100) + Math.abs((yDiff/gtCoords[1])*100) + Math.abs((zDiff/gtCoords[2])*100))/3; 
		System.out.println("% err: "+percentError+" %");
//		for (int i=0; i < groundTruthConfigs.length; i++) {
//			System.out.print(groundTruthConfigs[i]+ " ");
//			System.out.print(computedConfigs[i]+ "\n");
//			System.out.println(((groundTruthConfigs[i] -  computedConfigs[i])/groundTruthConfigs[i])*100);
//		}
	}
	
	/**
	 * 
	 * @return double array of random configurations.
	 */
	private static double[] createRandomConfiguration(int numJoints) {
		double[] jointConfigurations = new double[numJoints];
		for (int i = 0; i < numJoints; i++) { // populate random joint
												// Configurations
			jointConfigurations[i] = Math.random() * 2 * Math.PI;
		}
		return jointConfigurations;
	}
}
