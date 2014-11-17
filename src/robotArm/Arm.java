package robotArm;

public class Arm {

	private double[] armLengths;

	public Arm(double[] armLengths) {
		this.armLengths = armLengths;
	}

	/**
	 * Moves the robot arm according to the angle.
	 * 
	 * @param angles
	 *            assumes that the angles are along x, y, z for one joint, then
	 *            repeat for next joint {{x1,y1,z1},{x2,y2,z2}...{xn,yn,zn}}
	 * @return position of the end effector
	 */
	public double[] move(double[] angles) {

		double xend = 0, yend = 0, zend = 0;

		if (angles.length == armLengths.length * 3) {
			// 3 dimensions
			double x = 1, y = 1, z = 1;
			for (int i = 0; i < armLengths.length; i++) {

				double xangle = angles[0 + 3 * i], yangle = angles[1 + 3 * i], zangle = angles[2 + 3 * i];
				double armLength = armLengths[i];

				// rotation along x
				y *= Math.cos(xangle);
				z *= Math.sin(xangle);
				// rotation along y
				x *= Math.cos(yangle);
				z *= Math.sin(yangle);
				// rotation along z
				y *= Math.sin(zangle);
				x *= Math.cos(zangle);

				// adjust to unit vector
				double vectorLength = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)
						+ Math.pow(z, 2));
				x /= vectorLength;
				y /= vectorLength;
				z /= vectorLength;

				xend += x * armLength;
				yend += y * armLength;
				zend += z * armLength;

				//TODO Check for cross overs
			}
		} else {
			System.err.println("Wrong number of angular inputs");
			System.exit(-1);
		}
		System.out.println(xend + " " + yend + " " + zend);

		return new double[] { xend, yend, zend };
	}
}
