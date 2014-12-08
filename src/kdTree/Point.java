package kdTree;

/**
 * Representation of a configuration, made compatible with Kd tree by
 * subclassing XYZPoint.
 * 
 * Super class and super class methods of XYZPoint provided by Justin Wetherell
 * <phishman3579@gmail.com> http://en.wikipedia.org/wiki/K-d_tree
 * 
 * @author Steven Lin slin45 and Tae Soo Kim tkim60
 *
 */
public class Point extends KdTree.XYZPoint {

	double[] configs;

	/**
	 * Configuration.
	 * 
	 * @param configs
	 *            Configuration in format of double array arranged from base to
	 *            tip.
	 */
	public Point(double[] configs) {
		this.configs = configs;
	}

	/**
	 * 
	 * @return Configuration of the point.
	 */
	public double[] getConfigs() {
		return this.configs;
	}

	@Override
	public double euclideanDistance(KdTree.XYZPoint point) {
		if (point instanceof Point) {
			double[] ptConfigs = ((Point) point).getConfigs();
			double sum = 0;
			for (int i = 0; i < configs.length; i++) {
				sum += Math.pow((this.configs[i] - ptConfigs[i]), 2);
			}
			return Math.sqrt(sum);
		} else
			return -1;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Point))
			return false;

		Point xyzPoint = (Point) obj;
		return compareTo(xyzPoint) == 0;
	}

	@Override
	public int compareTo(KdTree.XYZPoint o) {
		if (o instanceof Point) {
			double[] ptConfig = ((Point) o).getConfigs();
			for (int i = 0; i < ptConfig.length; i++) {
				if (this.configs[i] > ptConfig[i])
					return 1;
				if (this.configs[i] < ptConfig[i])
					return -1;
			}

			return 0;
		}
		return -1;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		for (int i = 0; i < configs.length; i++)
			builder.append(configs[i]).append(", ");
		builder.append(")");
		return builder.toString();
	}
}