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
	public double getConfig(int index) {
		return this.configurations[index];
	}
	public String toString() {
		String val = "config=[";
		for (Double d: this.configurations) {
			val += d + ",";
		}
		val += "] ";
		val += "endPos=[";
		val += this.endEffector.getCoordinates()[0] +","+ this.endEffector.getCoordinates()[1]+","+this.endEffector.getCoordinates()[2] ;
		val += "]";
		return val;
	}
}
