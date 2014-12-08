package nearestNeighbor;

import robotArm.Position;

/**
 * Represents the set of configuration and position of a robot arm.
 * 
 * @author Steven Lin slin45 and Tae Soo Kim tkim60
 *
 */
public class Node {

	/**
	 * Configuration space and physical space.
	 */
	private double[] configurations;
	private Position endEffector;

	/**
	 * Constructor
	 * 
	 * @param configurations
	 *            Configuration space coordinates.
	 * @param endEffector
	 *            Physical space coordinates.
	 */
	public Node(double[] configurations, Position endEffector) {
		this.configurations = configurations;
		this.endEffector = endEffector;
	}

	/**
	 * 
	 * @return configurations
	 */
	public double[] getConfigurations() {
		return this.configurations;
	}

	/**
	 * 
	 * @return end effector positions.
	 */
	public Position getEndEffectorPosition() {
		return this.endEffector;
	}

	/**
	 * 
	 * @return end effecto positions in doubles array.
	 */
	public double[] getEndEffectorArray() {
		return this.endEffector.getCoordinates();
	}

	/**
	 * 
	 * @return number of joints in arm that this node belongs to.
	 */
	public int getNumJoints() {
		return configurations.length;
	}

	/**
	 * 
	 * @param index
	 *            Joint identifier.
	 * @return configuration at a specific joint.
	 */
	public double getConfig(int index) {
		return this.configurations[index];
	}

	@Override
	public String toString() {
		String val = "config=[";
		for (Double d : this.configurations) {
			val += d + ",";
		}
		val += "] ";
		val += "endPos=[";
		val += this.endEffector.getCoordinates()[0] + ","
				+ this.endEffector.getCoordinates()[1] + ","
				+ this.endEffector.getCoordinates()[2];
		val += "]";
		return val;
	}
}
