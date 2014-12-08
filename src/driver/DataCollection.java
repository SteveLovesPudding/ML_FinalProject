package driver;

import nearestNeighbor.NearestNeighborAlgorithm;
import nearestNeighbor.Node;
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
	public static void main(String[] args) {
		NearestNeighborAlgorithm nn = new NearestNeighborAlgorithm("test_twoArm_5000_YZ");
		double[] joints = {1,1};
		JointDirection[] dir = {JointDirection.Y, JointDirection.Z};
		Arm groundTruthArm = new Arm(joints, dir);
		Arm computingArm = new Arm(joints, dir);
		
		//ground truth computations
		Position groundTruthEndEffectorPosition = new Position();
		double[] groundTruthConfigs = createRandomConfiguration(2);
		//double[] groundTruthConfigs = new double[]{4.3517,5.2204};
		groundTruthArm.move(groundTruthConfigs, groundTruthEndEffectorPosition);
		//computed inverse kinematics
		double[] computedConfigs = nn.getConfiguration(groundTruthEndEffectorPosition, numNeighbors);
		Position computedEndEffectorPosition = new Position();
		computingArm.move(computedConfigs, computedEndEffectorPosition);
		
		System.out.println("GROUND TRUTH DATA");
		for (Double d: groundTruthConfigs) {
			System.out.println(d);
		}
		System.out.println(groundTruthEndEffectorPosition);
		
		System.out.println("\nCOMPUTED TRUTH DATA");
		for (Double d: computedConfigs) {
			System.out.println(d);
		}
		System.out.println(computedEndEffectorPosition);
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
