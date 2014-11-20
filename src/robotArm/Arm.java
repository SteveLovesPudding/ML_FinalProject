package robotArm;

import java.util.HashMap;
import java.util.Map;

public class Arm {
	private double[] armLengths;
	private JointDirection[] jointDirections;

	public Arm(double[] armLengths, JointDirection[] jointDirections) {
		this.armLengths = armLengths;
		this.jointDirections = jointDirections;
	}

	// /**
	// * Moves the robot arm according to the angle.
	// *
	// * @param angles
	// * assumes that the angles are along x, y, z for one joint, then
	// * repeat for next joint {{x1,y1,z1},{x2,y2,z2}...{xn,yn,zn}}
	// * @param position
	// * wrapper for returning the end position
	// * @return true if movement is valid, false if invalid
	// */
	// public boolean move(double[][] angles, Position position) {
	//
	// double xend = 0, yend = 0, zend = 0;
	// Map<Integer, double[][]> armEnds = new HashMap<Integer, double[][]>();
	// // check that enough dimensions are sent
	// //default orientation
	// double x = 1, y = 0, z = 0;
	// for (int i = 0; i < armLengths.length; i++) {
	//
	// // {start x, start y, start z, end x, end y, end z}
	// double[] begin = new double[3];
	// begin[0] = xend;
	// begin[1] = yend;
	// begin[2] = zend;
	//
	// double[] vector = findOrientation(new double[] { x, y, z },
	// angles[i]);
	// vector = convertToUnitVector(vector);
	// x = vector[0];
	// y = vector[1];
	// z = vector[2];
	//
	// double armLength = armLengths[i];
	//
	// double[] end = new double[3];
	// end[0] = (xend += x * armLength);
	// end[1] = (yend += y * armLength);
	// end[2] = (zend += z * armLength);
	//
	// armEnds.put(i, new double[][] { begin, end });
	//
	// // System.out.println(Math.sqrt(Math.pow(x * armLength, 2)
	// // + Math.pow(y * armLength, 2) + Math.pow(z * armLength, 2)));
	// System.out.println("ARM END " + end[0] + " " + end[1] + " "
	// + end[2]);
	//
	// // check each arm to make sure none intersect
	// for (int j = 1; j < i; j++) {
	// double[][] arm = armEnds.get(j);
	// // if intersect return false, no point storing position its
	// // invalid
	// if (intersects3D(arm[0], arm[1], begin, end))
	// return false;
	// }
	// }
	//
	// position.setCoordinates(new double[] { xend, yend, zend });
	// return true;
	// }

	/**
	 * Moves the robot arm according to the angle.
	 * 
	 * @param angles
	 *            assumes that the angles are along x, y, z for one joint, then
	 *            repeat for next joint {{x1,y1,z1},{x2,y2,z2}...{xn,yn,zn}}
	 * @param position
	 *            wrapper for returning the end position
	 * @return true if movement is valid, false if invalid
	 */
	public boolean move(double angle, Position position) {

		double xend = 0, yend = 0, zend = 0;
		Map<Integer, double[][]> armEnds = new HashMap<Integer, double[][]>();
		// check that enough dimensions are sent
		// default orientation
		double x = 1, y = 0, z = 0;
		for (int i = 0; i < armLengths.length; i++) {

			// {start x, start y, start z, end x, end y, end z}
			double[] begin = new double[3];
			begin[0] = xend;
			begin[1] = yend;
			begin[2] = zend;

			double[] vector = findOrientation(new double[] { x, y, z }, angle,
					jointDirections[i]);
			vector = convertToUnitVector(vector);
			x = vector[0];
			y = vector[1];
			z = vector[2];

			double armLength = armLengths[i];

			double[] end = new double[3];
			end[0] = (xend += x * armLength);
			end[1] = (yend += y * armLength);
			end[2] = (zend += z * armLength);

			armEnds.put(i, new double[][] { begin, end });

			// System.out.println(Math.sqrt(Math.pow(x * armLength, 2)
			// + Math.pow(y * armLength, 2) + Math.pow(z * armLength, 2)));
			System.out.println("ARM END " + end[0] + " " + end[1] + " "
					+ end[2]);

			// check each arm to make sure none intersect
			for (int j = 1; j < i; j++) {
				double[][] arm = armEnds.get(j);
				// if intersect return false, no point storing position its
				// invalid
				if (intersects3D(arm[0], arm[1], begin, end))
					return false;
			}
		}

		position.setCoordinates(new double[] { xend, yend, zend });
		return true;
	}

	/**
	 * 
	 * @param vector
	 *            {x,y,z}
	 * @return conver to unit vector
	 */
	private double[] convertToUnitVector(double[] vector) {

		double vectorLength = Math.sqrt(Math.pow(vector[0], 2)
				+ Math.pow(vector[1], 2) + Math.pow(vector[2], 2));

		if (vectorLength == 0) {
			return new double[] { 0, 0, 0 };
		} else {
			return new double[] { vector[0] / vectorLength,
					vector[1] / vectorLength, vector[2] / vectorLength };

		}
	}

	// /**
	// *
	// * @param original
	// * Original orientation {x,y,z}
	// * @param angles
	// * New orientation angle {x_angle, y_angle, z_angle}
	// * @return return orientation of a joint
	// */
	// private double[] findOrientation(double[] original, double[] angles) {
	// double x = original[0], y = original[1], z = original[2];
	// double xangle = angles[0], yangle = angles[1], zangle = angles[2];
	//
	// double newX, newY, newZ;
	// System.out.println(x + " " + y + " " + z);
	// // rotation along x
	// newY = y*Math.cos(xangle) - z*Math.sin(xangle);
	// newZ = y*Math.sin(xangle) + z*Math.cos(xangle);
	// y = newY;
	// z = newZ;
	// System.out.println(x + " " + y + " " + z);
	// // rotation along y
	// newZ = z*Math.cos(yangle) - x*Math.sin(yangle);
	// newX = z*Math.sin(yangle) + x*Math.cos(yangle);
	// z = newZ;
	// x = newX;
	// System.out.println(x + " " + y + " " + z);
	// // rotation along z
	// newX = x*Math.cos(zangle) - y*Math.sin(zangle);
	// newY = x*Math.sin(zangle) + y*Math.cos(zangle);
	// x = newX;
	// y = newY;
	// System.out.println(x + " " + y + " " + z);
	//
	// return new double[] { x, y, z };
	//
	// }

	/**
	 * 
	 * @param original
	 *            {x,y,z}
	 * @param angle
	 *            [0, 2*pi]
	 * @param direction
	 *            rotation direction
	 * @return
	 */
	private double[] findOrientation(double[] original, double angle,
			JointDirection direction) {

		double x = original[0], y = original[1], z = original[2];

		double newX, newY, newZ;
		// Rotates in vertical
		if (direction == JointDirection.Y) {
			newZ = z * Math.cos(angle) - x * Math.sin(angle);
			newX = z * Math.sin(angle) + x * Math.cos(angle);
			z = newZ;
			x = newX;
			// Rotates in horizontal
		} else if (direction == JointDirection.Z) {
			newX = x * Math.cos(angle) - y * Math.sin(angle);
			newY = x * Math.sin(angle) + y * Math.cos(angle);
			x = newX;
			y = newY;
		}
		return new double[] { x, y, z };
	}

	/**
	 * 
	 * @param a
	 *            Arm 1 end 1
	 * @param b
	 *            Arm 1 end 2
	 * @param c
	 *            Arm 2 end 1
	 * @param d
	 *            Arm 2 end 2
	 * @return true if intersect, false if doesn't intersect
	 */
	public static boolean intersects3D(double[] a, double[] b, double[] c,
			double[] d) {

		return intersects2D(new double[] { a[0], a[1] }, new double[] { b[0],
				b[1] }, new double[] { c[0], c[1] },
				new double[] { d[0], d[1] })
				&& intersects2D(new double[] { a[1], a[2] }, new double[] {
						b[1], b[2] }, new double[] { c[1], c[2] },
						new double[] { d[1], d[2] })
				&& intersects2D(new double[] { a[0], a[2] }, new double[] {
						b[0], b[2] }, new double[] { c[0], c[2] },
						new double[] { d[0], d[2] });
	}

	/**
	 * 
	 * @param a
	 *            line 1 end 1
	 * @param b
	 *            line 1 end 2
	 * @param c
	 *            line 2 end 1
	 * @param d
	 *            line 2 end 2
	 * @return true if intersect false if doesn't
	 */
	public static boolean intersects2D(double[] a, double[] b, double[] c,
			double[] d) {

		double[] start1, start2, end1, end2;

		// Determine orientation
		if (a[0] <= b[0]) {
			start1 = a;
			end1 = b;
		} else {
			start1 = b;
			end1 = a;
		}

		if (c[0] <= d[0]) {
			start2 = c;
			end2 = d;
		} else {
			start2 = d;
			end2 = c;
		}

		double slope1 = (end1[1] - start1[1]) / (end1[0] - start1[0]);
		double slope2 = (end2[1] - start2[1]) / (end2[0] - start2[0]);
		double b1 = start1[1] - slope1 * start1[0];
		double b2 = start2[1] - slope2 * start2[0];

		// case of "vertical lines"
		if (a[0] == b[0]) {

			double intersect = b2 + slope2 * a[0];
			// If the intersect is not between the two points
			if (!(intersect < a[1] && intersect > b[1])
					&& !(intersect < b[1] && intersect > a[1])) {
				return false;
			} else {
				return true;
			}
		}

		if (c[0] == d[0]) {

			double intersect = b1 + slope1 * c[0];

			// If the intersect is not between the two points
			if (!(intersect < c[1] && intersect > d[1])
					&& !(intersect < d[1] && intersect > c[1])) {
				return false;
			} else {
				return true;
			}
		}

		// if parallel slopes
		if (slope1 == slope2) {
			if (b1 == b2) {
				// case of same line
				if (start1[0] < start2[0] ? start2[0] < end1[0]
						: start1[0] < end2[0])// case of overlap
					return true;
				else
					// case of not overlapping
					return false;
			} else {
				// case of parallel lines
				return false;
			}
		} else {
			// case of non parallel lines

			// find intersect, corresponds to first dimension
			double intersect = (b2 - b1) / (slope1 - slope2);
			// check that the intersect does not fall outside the bound of the
			// two segments
			if (intersect <= start1[0] || intersect >= end1[0]
					|| intersect <= start2[0] || intersect >= end2[0]) {
				// case where intersect falls outside of bound
				return false;
			} else {
				// case where intersect is inside bound of both segments
				return true;
			}
		}

	}
}
