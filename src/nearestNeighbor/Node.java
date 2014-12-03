package nearestNeighbor;

import robotArm.Position;

public class Node {

	private double[] configurations;
	private Position endEffector;

	public Node(double[] configurations, Position endEffector) {
		this.configurations = configurations;
		this.endEffector = endEffector;
	}

	public double[] getConfigurations() {
		return this.configurations;
	}

	public Position getEndEffectorPosition() {
		return this.endEffector;
	}
	
	public double[] getEndEffectorArray(){
		return this.endEffector.getCoordinates();
	}
	public int getNumJoints(){
		return configurations.length;
	}
}
