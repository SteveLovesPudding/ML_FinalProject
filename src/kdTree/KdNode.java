package kdTree;

import robotArm.Position;

/**
 * Node for KdTree. 
 * 
 * @author Steven Lin slin45 and Tae Soo Kim tkim60 provided by Justin Wetherell
 *         <phishman3579@gmail.com> http://en.wikipedia.org/wiki/K-d_tree
 *
 */
public class KdNode implements Comparable<KdNode> {

	protected static final int X_AXIS = 0;
	protected static final int Y_AXIS = 1;
	protected static final int Z_AXIS = 2;
	public int k = 3;
	public int depth = 0;
	public Position id = null;
	public KdNode parent = null;
	public KdNode lesser = null;
	public KdNode greater = null;

	public KdNode(Position id) {
		this.id = id;
	}

	public KdNode(int k, int depth, Position id) {
		this(id);
		this.k = k;
		this.depth = depth;
	}

	public static int compareTo(int depth, int k, Position o1, Position o2) {
		int axis = depth % k;
		if (axis == X_AXIS)
			return Position.X_COMPARATOR.compare(o1, o2);
		if (axis == Y_AXIS)
			return Position.Y_COMPARATOR.compare(o1, o2);
		return Position.Z_COMPARATOR.compare(o1, o2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof KdNode))
			return false;

		KdNode kdNode = (KdNode) obj;
		if (this.compareTo(kdNode) == 0)
			return true;
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(KdNode o) {
		return compareTo(depth, k, this.id, o.id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("k=").append(k);
		builder.append(" depth=").append(depth);
		builder.append(" id=").append(id.toString());
		return builder.toString();
	}
}