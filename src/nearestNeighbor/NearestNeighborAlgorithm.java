package nearestNeighbor;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;
import robotArm.Position;

public class NearestNeighborAlgorithm {

	public double[] getConfiguration(Position desiredPosition) {

		double[] dpArray = desiredPosition.getCoordinates();
		Matrix desiredPositionVector = new Matrix(new double[][] { {
				dpArray[0], dpArray[1], dpArray[2], 1 } });
		int numNearestNeighbors = 5;
		desiredPositionVector.print(1, 1);
		Node closestPoint;
		Node[] nearestNeighbors;
		int numJoints = getNearestNeighbors(
				this.getClosestPoint(desiredPosition, new double[] {}),
				numNearestNeighbors)[0].getNumJoints();
		List<Double> calculatedConfigurations = new ArrayList<Double>();

		double[][] xArray;
		double[][] qArray;
		// Greedy calculation for each joint
		for (int i = 1; i < numJoints; i++) {
			closestPoint = this.getClosestPoint(desiredPosition,
					convertToArray(calculatedConfigurations));
			nearestNeighbors = getNearestNeighbors(closestPoint,
					numNearestNeighbors);

			// Solve for Xw = Q
			xArray = new double[numNearestNeighbors][4];
			qArray = new double[numNearestNeighbors][1];

			for (int j = 0; j < numNearestNeighbors; j++) {
				qArray[0][j] = nearestNeighbors[j].getConfigurations()[j];
				for (int k = 0; k < 4; k++) {
					xArray[j][k] = nearestNeighbors[j].getEndEffectorArray()[k];
				}
			}

			Matrix x = new Matrix(xArray), q = new Matrix(qArray);
			Matrix w = x.solve(q);
			Matrix derivedConfiguration = desiredPositionVector.times(w);

			calculatedConfigurations.add(derivedConfiguration.get(0, 0));

		}
		
		return convertToArray(calculatedConfigurations);
	}

	/**
	 * Return the point closest to desiredPosition in physical space
	 * 
	 * @param desiredPosition
	 *            Physical location
	 * @param configurations
	 *            Provided configuration
	 * @return
	 */
	private Node getClosestPoint(Position desiredPosition,
			double[] configurations) {

		// TODO add equation to take into account configurations
		return null;
	}

	/**
	 * Return the nearest neighbors in configuration space
	 * 
	 * @param node
	 *            node for comparison
	 * @param numNeighbors
	 *            number of nearest neighbors
	 * @return an array of nearest neighbors
	 */
	private Node[] getNearestNeighbors(Node node, int numNeighbors) {
		// TODO
		return null;
	}

	private double[] convertToArray(List<Double> list) {

		double[] array = new double[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}

		return array;
	}
}
