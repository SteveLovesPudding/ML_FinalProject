package robotArm;

public class Position {

	/**
	 * {x,y,z}
	 */
	double[] coordinates = new double[3];

	public Position(double[] coordinates) {
		this.coordinates = coordinates;
	}

	public Position(double x, double y, double z) {
		this.coordinates = new double[] { x, y, z };
	}

	public Position() {

	}

	public double[] getCoordinates() {
		return this.coordinates;
	}

	public void setCoordinates(double[] coordinates) {
		this.coordinates = coordinates;
	}

	public String toString() {
		return "" + coordinates[0] + "  " + coordinates[1] + "  "
				+ coordinates[2] + "";
	}
}
