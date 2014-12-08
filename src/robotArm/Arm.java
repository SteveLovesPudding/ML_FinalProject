package robotArm;

import java.util.HashMap;
import java.util.Map;

/**
 * Model of robotic arm.
 * 
 * @author Steven Lin slin45 and Tae Soo Kim tkim60
 *
 */
public class Arm {

	/**
	 * Structures for basic information of each arm
	 */
	private double[] armLengths;
	private JointDirection[] jointDirections;

	/**
	 * Constructor
	 * 
	 * @param armLengths
	 *            Length of each arm, arranged from closest to base to farthest
	 *            away from base.
	 * @param jointDirections
	 *            Rotational direction of each arm, arranged from closest to
	 *            base to farthest away from base.
	 */
	public Arm(double[] armLengths, JointDirection[] jointDirections) {
		this.armLengths = armLengths;
		this.jointDirections = jointDirections;
	}

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
	public boolean move(double[] angle, Position position) {

		double xend = 0, yend = 0, zend = 0;
		Map<Integer, double[][]> armEnds = new HashMap<Integer, double[][]>();
		// check that enough dimensions are sent
		// default orientation
		double x = 1, y = 0, z = 0;
		for (int i = 0; i < armLengths.length; i++) {

			double[] begin = new double[3];
			begin[0] = xend;
			begin[1] = yend;
			begin[2] = zend;

			double[] calcAngles = new double[i + 1];
			JointDirection[] calcDirections = new JointDirection[i + 1];
			for (int j = 0; j <= i; j++) {
				calcAngles[j] = angle[j];
				calcDirections[j] = jointDirections[j];
			}

			double[] vector = findOrientation(calcAngles, calcDirections);
			x = vector[0];
			y = vector[1];
			z = vector[2];

			double armLength = armLengths[i];
			double[] end = new double[3];
			end[0] = (xend += x * armLength);
			end[1] = (yend += y * armLength);
			end[2] = (zend += z * armLength);
			xend = end[0];
			yend = end[1];
			zend = end[2];

			armEnds.put(i, new double[][] { begin, end });

			// check each arm to make sure none intersect
			for (int j = 1; j < i; j++) {
				double[][] arm = armEnds.get(j);
				// if intersect return false, no reason for storing position if
				// invalid
				if (intersects3D(arm[0], arm[1], begin, end))
					return false;
			}
		}

		// return value of end effector
		position.setCoordinates(new double[] { xend, yend, zend });
		return true;
	}

	/**
	 * Calculate orientation based on angle of rotation
	 * 
	 * @param angles
	 *            Angles of rotation arranged from base to end of arm.
	 * @param directions
	 *            Direction of rotation, arranged from base to end of arm.
	 * @return Final orientation of join/end effector.
	 */
	private double[] findOrientation(double[] angles,
			JointDirection[] directions) {

		double[] vector = { 1, 0, 0 };
		// chronological order

		for (int i = angles.length - 1; i >= 0; i--) {
			double x = vector[0], y = vector[1], z = vector[2];

			// Apply rotational matrix based on direction of rotation
			if (directions[i] == JointDirection.Y) {

				vector[0] = x * Math.cos(angles[i]) + z * Math.sin(angles[i]);
				vector[2] = x * -1.0 * Math.sin(angles[i]) + z
						* Math.cos(angles[i]);

			} else if (directions[i] == JointDirection.Z) {

				vector[0] = x * Math.cos(angles[i]) + y * -1.0
						* Math.sin(angles[i]);
				vector[1] = x * Math.sin(angles[i]) + y * Math.cos(angles[i]);
			}

		}

		return vector;
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

		// checks that does not intersect in any plane, there exists a more
		// efficient method
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
