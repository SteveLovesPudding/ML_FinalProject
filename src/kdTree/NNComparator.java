package kdTree;

import java.util.Comparator;

/**
 * Comparator used to be compatible with kd tree.
 * 
 * @author Steven Lin slin45 and Tae Soo Kim tkim60
 *
 * @param <T>
 *            Class to be compared
 */
public class NNComparator<T extends Point> implements Comparator<Point> {

	/**
	 * Axis for comparison.
	 */
	int axis;

	/**
	 * Axis of comparison.
	 * 
	 * @param axis
	 *            Specifies which configuration to be used for comparison
	 */
	public NNComparator(int axis) {
		this.axis = axis;
	}

	@Override
	public int compare(Point o1, Point o2) {

		double[] first = o1.getConfigs();
		double[] second = o2.getConfigs();
		if (first[axis] < second[axis])
			return -1;
		if (first[axis] > second[axis])
			return 1;
		return 0;
	}
}
