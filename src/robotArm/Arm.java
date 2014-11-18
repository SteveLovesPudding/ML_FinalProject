package robotArm;

import java.util.HashMap;
import java.util.Map;

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

		// check that enough dimensions are sent
		if (angles.length == armLengths.length * 3) {

			Map<Integer, double[]> armEnds = new HashMap<Integer, double[]>();
			double x = 1, y = 1, z = 1;
			for (int i = 0; i < armLengths.length; i++) {

				// {start x, start y, start z, end x, end y, end z}
				double[] ends = new double[6];
				ends[0] = xend;
				ends[1] = yend;
				ends[2] = zend;

				int adjust = 3 * i;
				double[] vector = findOrientation(new double[] { x, y, z },
						new double[] { angles[0 + adjust], angles[1 + adjust],
								angles[2 + adjust] });
				vector = convertToUnitVector(new double[] { x, y, z });
				x = vector[0];
				y = vector[1];
				z = vector[2];

				double armLength = armLengths[i];

				ends[3] = (xend += x * armLength);
				ends[4] = (yend += y * armLength);
				ends[5] = (zend += z * armLength);

				armEnds.put(i, ends);

				System.out.println(Math.sqrt(Math.pow(x * armLength, 2)
						+ Math.pow(y * armLength, 2)
						+ Math.pow(z * armLength, 2)));
				// TODO Check for cross overs
			}
		} else {
			System.err.println("Wrong number of angular inputs");
			System.exit(-1);
		}
		System.out.println(xend + " " + yend + " " + zend);

		return new double[] { xend, yend, zend };
	}

	/**
	 * 
	 * @param vector
	 *            {x,y,z}
	 * @return
	 */
	private double[] convertToUnitVector(double[] vector) {

		double vectorLength = Math.sqrt(Math.pow(vector[0], 2)
				+ Math.pow(vector[1], 2) + Math.pow(vector[2], 2));

		return new double[] { vector[0] / vectorLength,
				vector[1] / vectorLength, vector[2] / vectorLength };
	}

	/**
	 * 
	 * @param original
	 *            Original orientation {x,y,z}
	 * @param angles
	 *            New orientation angle {x_angle, y_angle, z_angle}
	 * @return
	 */
	private double[] findOrientation(double[] original, double[] angles) {
		double x = original[0], y = original[1], z = original[2];
		double xangle = angles[0], yangle = angles[1], zangle = angles[2];

		y *= Math.cos(xangle);
		z *= Math.sin(xangle);
		// rotation along y
		x *= Math.cos(yangle);
		z *= Math.sin(yangle);
		// rotation along z
		y *= Math.sin(zangle);
		x *= Math.cos(zangle);

		return new double[] { x, y, z };

	}

	/**
	 * 
	 * @param i
	 *            Arm to check
	 * @return
	 */
	private boolean intersects(int i) {
		
		return false;
	}
}
