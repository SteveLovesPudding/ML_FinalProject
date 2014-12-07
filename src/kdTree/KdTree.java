package kdTree;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import robotArm.Position;

/**
 * A k-d tree (short for k-dimensional tree) is a space-partitioning data
 * structure for organizing points in a k-dimensional space. k-d trees are a
 * useful data structure for several applications, such as searches involving a
 * multidimensional search key (e.g. range searches and nearest neighbor
 * searches). k-d trees are a special case of binary space partitioning trees.
 * 
 * http://en.wikipedia.org/wiki/K-d_tree
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class KdTree {

    private int k = 3;
    private KdNode root = null;

    protected static final int X_AXIS = 0;
    protected static final int Y_AXIS = 1;
    protected static final int Z_AXIS = 2;

    /**
     * Default constructor.
     */
    public KdTree() {
    }

    /**
     * Constructor for creating a more balanced tree. It uses the
     * "median of points" algorithm.
     * 
     * @param list
     *            of Positions.
     */
    public KdTree(List<Position> list) {
        root = createNode(list, k, 0);
    }

    /**
     * Create node from list of Positions.
     * 
     * @param list
     *            of Positions.
     * @param k
     *            of the tree.
     * @param depth
     *            depth of the node.
     * @return node created.
     */
    private static KdNode createNode(List<Position> list, int k, int depth) {
        if (list == null || list.size() == 0)
            return null;

        int axis = depth % k;
        if (axis == X_AXIS)
            Collections.sort(list, Position.X_COMPARATOR);
        else if (axis == Y_AXIS)
            Collections.sort(list, Position.Y_COMPARATOR);
        else
            Collections.sort(list, Position.Z_COMPARATOR);

        KdNode node = null;
        if (list.size() > 0) {
            int medianIndex = list.size() / 2;
            node = new KdNode(k, depth, list.get(medianIndex));
            List<Position> less = new ArrayList<Position>(list.size() - 1);
            List<Position> more = new ArrayList<Position>(list.size() - 1);
            // Process list to see where each non-median point lies
            for (int i = 0; i < list.size(); i++) {
                if (i == medianIndex)
                    continue;
                Position p = list.get(i);
                if (KdNode.compareTo(depth, k, p, node.id) <= 0) {
                    less.add(p);
                } else {
                    more.add(p);
                }
            }
            if ((medianIndex - 1) >= 0) {
                // Cannot assume points before the median are less since they
                // could be equal
                // List<Position> less = list.subList(0, mediaIndex);
                if (less.size() > 0) {
                    node.lesser = createNode(less, k, depth + 1);
                    node.lesser.parent = node;
                }
            }
            if ((medianIndex + 1) <= (list.size() - 1)) {
                // Cannot assume points after the median are less since they
                // could be equal
                // List<Position> more = list.subList(mediaIndex + 1,
                // list.size());
                if (more.size() > 0) {
                    node.greater = createNode(more, k, depth + 1);
                    node.greater.parent = node;
                }
            }
        }

        return node;
    }

    /**
     * Add value to the tree. Tree can contain multiple equal values.
     * 
     * @param value
     *            T to add to the tree.
     * @return True if successfully added to tree.
     */
    public boolean add(Position value) {
        if (value == null)
            return false;

        if (root == null) {
            root = new KdNode(value);
            return true;
        }

        KdNode node = root;
        while (true) {
            if (KdNode.compareTo(node.depth, node.k, value, node.id) <= 0) {
                // Lesser
                if (node.lesser == null) {
                    KdNode newNode = new KdNode(k, node.depth + 1, value);
                    newNode.parent = node;
                    node.lesser = newNode;
                    break;
                }
                node = node.lesser;
            } else {
                // Greater
                if (node.greater == null) {
                    KdNode newNode = new KdNode(k, node.depth + 1, value);
                    newNode.parent = node;
                    node.greater = newNode;
                    break;
                }
                node = node.greater;
            }
        }

        return true;
    }

    /**
     * Does the tree contain the value.
     * 
     * @param value
     *            T to locate in the tree.
     * @return True if tree contains value.
     */
    public boolean contains(Position value) {
        if (value == null)
            return false;

        KdNode node = getNode(this, value);
        return (node != null);
    }

    /**
     * Locate T in the tree.
     * 
     * @param tree
     *            to search.
     * @param value
     *            to search for.
     * @return KdNode or NULL if not found
     */
    private static final KdNode getNode(KdTree tree, Position value) {
        if (tree == null || tree.root == null || value == null)
            return null;

        KdNode node = tree.root;
        while (true) {
            if (node.id.equals(value)) {
                return node;
            } else if (KdNode.compareTo(node.depth, node.k, value, node.id) <= 0) {
                // Lesser
                if (node.lesser == null) {
                    return null;
                }
                node = node.lesser;
            } else {
                // Greater
                if (node.greater == null) {
                    return null;
                }
                node = node.greater;
            }
        }
    }

    /**
     * Remove first occurrence of value in the tree.
     * 
     * @param value
     *            T to remove from the tree.
     * @return True if value was removed from the tree.
     */
    public boolean remove(Position value) {
        if (value == null)
            return false;

        KdNode node = getNode(this, value);
        if (node == null)
            return false;

        KdNode parent = node.parent;
        if (parent != null) {
            if (parent.lesser != null && node.equals(parent.lesser)) {
                List<Position> nodes = getTree(node);
                if (nodes.size() > 0) {
                    parent.lesser = createNode(nodes, node.k, node.depth);
                    if (parent.lesser != null) {
                        parent.lesser.parent = parent;
                    }
                } else {
                    parent.lesser = null;
                }
            } else {
                List<Position> nodes = getTree(node);
                if (nodes.size() > 0) {
                    parent.greater = createNode(nodes, node.k, node.depth);
                    if (parent.greater != null) {
                        parent.greater.parent = parent;
                    }
                } else {
                    parent.greater = null;
                }
            }
        } else {
            // root
            List<Position> nodes = getTree(node);
            if (nodes.size() > 0)
                root = createNode(nodes, node.k, node.depth);
            else
                root = null;
        }

        return true;
    }

    /**
     * Get the (sub) tree rooted at root.
     * 
     * @param root
     *            of tree to get nodes for.
     * @return points in (sub) tree, not including root.
     */
    private static final List<Position> getTree(KdNode root) {
        List<Position> list = new ArrayList<Position>();
        if (root == null)
            return list;

        if (root.lesser != null) {
            list.add(root.lesser.id);
            list.addAll(getTree(root.lesser));
        }
        if (root.greater != null) {
            list.add(root.greater.id);
            list.addAll(getTree(root.greater));
        }

        return list;
    }

    /**
     * K Nearest Neighbor search
     * 
     * @param K
     *            Number of neighbors to retrieve. Can return more than K, if
     *            last nodes are equal distances.
     * @param value
     *            to find neighbors of.
     * @return collection of T neighbors.
     */
    @SuppressWarnings("unchecked")
    public Collection<Position> nearestNeighbourSearch(int K, Position value) {
        if (value == null)
            return null;

        // Map used for results
        TreeSet<KdNode> results = new TreeSet<KdNode>(new EuclideanComparator(value));

        // Find the closest leaf node
        KdNode prev = null;
        KdNode node = root;
        while (node != null) {
            if (KdNode.compareTo(node.depth, node.k, value, node.id) <= 0) {
                // Lesser
                prev = node;
                node = node.lesser;
            } else {
                // Greater
                prev = node;
                node = node.greater;
            }
        }
        KdNode leaf = prev;

        if (leaf != null) {
            // Used to not re-examine nodes
            Set<KdNode> examined = new HashSet<KdNode>();

            // Go up the tree, looking for better solutions
            node = leaf;
            while (node != null) {
                // Search node
                searchNode(value, node, K, results, examined);
                node = node.parent;
            }
        }

        // Load up the collection of the results
        Collection<Position> collection = new ArrayList<Position>(K);
        for (KdNode kdNode : results) {
            collection.add((Position) kdNode.id);
        }
        return collection;
    }

    private static final <Position> void searchNode(robotArm.Position value, KdNode node, int K,
            TreeSet<KdNode> results, Set<KdNode> examined) {
        examined.add(node);

        // Search node
        KdNode lastNode = null;
        Double lastDistance = Double.MAX_VALUE;
        if (results.size() > 0) {
            lastNode = results.last();
            lastDistance = lastNode.id.euclideanDistance(value);
        }
        Double nodeDistance = node.id.euclideanDistance(value);
        if (nodeDistance.compareTo(lastDistance) < 0) {
            if (results.size() == K && lastNode != null)
                results.remove(lastNode);
            results.add(node);
        } else if (nodeDistance.equals(lastDistance)) {
            results.add(node);
        } else if (results.size() < K) {
            results.add(node);
        }
        lastNode = results.last();
        lastDistance = lastNode.id.euclideanDistance(value);

        int axis = node.depth % node.k;
        KdNode lesser = node.lesser;
        KdNode greater = node.greater;

        // Search children branches, if axis aligned distance is less than
        // current distance
        if (lesser != null && !examined.contains(lesser)) {
            examined.add(lesser);

            double nodePoint = Double.MIN_VALUE;
            double valuePlusDistance = Double.MIN_VALUE;
            if (axis == X_AXIS) {
                nodePoint = node.id.coordinates[0];
                valuePlusDistance = value.coordinates[0] - lastDistance;
            } else if (axis == Y_AXIS) {
                nodePoint = node.id.coordinates[1];
                valuePlusDistance = value.coordinates[1] - lastDistance;
            } else {
                nodePoint = node.id.coordinates[2];
                valuePlusDistance = value.coordinates[2] - lastDistance;
            }
            boolean lineIntersectsCube = ((valuePlusDistance <= nodePoint) ? true : false);

            // Continue down lesser branch
            if (lineIntersectsCube)
                searchNode(value, lesser, K, results, examined);
        }
        if (greater != null && !examined.contains(greater)) {
            examined.add(greater);

            double nodePoint = Double.MIN_VALUE;
            double valuePlusDistance = Double.MIN_VALUE;
            if (axis == X_AXIS) {
                nodePoint = node.id.coordinates[0];
                valuePlusDistance = value.coordinates[0] + lastDistance;
            } else if (axis == Y_AXIS) {
                nodePoint = node.id.coordinates[1];
                valuePlusDistance = value.coordinates[1] + lastDistance;
            } else {
                nodePoint = node.id.coordinates[2];
                valuePlusDistance = value.coordinates[2] + lastDistance;
            }
            boolean lineIntersectsCube = ((valuePlusDistance >= nodePoint) ? true : false);

            // Continue down greater branch
            if (lineIntersectsCube)
                searchNode(value, greater, K, results, examined);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return TreePrinter.getString(this);
    }

    protected static class EuclideanComparator implements Comparator<KdNode> {

        private Position point = null;

        public EuclideanComparator(Position point) {
            this.point = point;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int compare(KdNode o1, KdNode o2) {
            Double d1 = point.euclideanDistance(o1.id);
            Double d2 = point.euclideanDistance(o2.id);
            if (d1.compareTo(d2) < 0)
                return -1;
            else if (d2.compareTo(d1) < 0)
                return 1;
            return o1.id.compareTo(o2.id);
        }
    };

    

//    public static class Position implements Comparable<Position> {
//
//        public static final Comparator<? super Position> Z_COMPARATOR = null;
//		public static final Comparator<? super Position> Y_COMPARATOR = null;
//		public static final Comparator<? super Position> X_COMPARATOR = null;
//		private double x = Double.NEGATIVE_INFINITY;
//        private double y = Double.NEGATIVE_INFINITY;
//        private double z = Double.NEGATIVE_INFINITY;
//
//        public Position(double x, double y) {
//            this.x = x;
//            this.y = y;
//            this.z = 0;
//        }
//
//        public Position(double x, double y, double z) {
//            this.x = x;
//            this.y = y;
//            this.z = z;
//        }
//
//        /**
//         * Computes the Euclidean distance from this point to the other.
//         * 
//         * @param o1
//         *            other point.
//         * @return euclidean distance.
//         */
//        public double euclideanDistance(Position o1) {
//            return euclideanDistance(o1, this);
//        }
//
//        /**
//         * Computes the Euclidean distance from one point to the other.
//         * 
//         * @param o1
//         *            first point.
//         * @param o2
//         *            second point.
//         * @return euclidean distance.
//         */
//        private static final double euclideanDistance(Position o1, Position o2) {
//            return Math.sqrt(Math.pow((o1.x - o2.x), 2) + Math.pow((o1.y - o2.y), 2) + Math.pow((o1.z - o2.z), 2));
//        };
//
//        /**
//         * {@inheritDoc}
//         */
//        @Override
//        public boolean equals(Object obj) {
//            if (obj == null)
//                return false;
//            if (!(obj instanceof Position))
//                return false;
//
//            Position xyzPoint = (Position) obj;
//            return compareTo(xyzPoint) == 0;
//        }
//
//        /**
//         * {@inheritDoc}
//         */
//        @Override
//        public int compareTo(Position o) {
//            int xComp = X_COMPARATOR.compare(this, o);
//            if (xComp != 0)
//                return xComp;
//            int yComp = Y_COMPARATOR.compare(this, o);
//            if (yComp != 0)
//                return yComp;
//            int zComp = Z_COMPARATOR.compare(this, o);
//            return zComp;
//        }
//
//        /**
//         * {@inheritDoc}
//         */
//        @Override
//        public String toString() {
//            StringBuilder builder = new StringBuilder();
//            builder.append("(");
//            builder.append(x).append(", ");
//            builder.append(y).append(", ");
//            builder.append(z);
//            builder.append(")");
//            return builder.toString();
//        }
//    }
    public static class TreePrinter {

        public static String getString(KdTree tree) {
            if (tree.root == null)
                return "Tree has no nodes.";
            return getString(tree.root, "", true);
        }

        private static String getString(KdNode node, String prefix, boolean isTail) {
            StringBuilder builder = new StringBuilder();

            if (node.parent != null) {
                String side = "left";
                if (node.parent.greater != null && node.id.equals(node.parent.greater.id))
                    side = "right";
                builder.append(prefix + (isTail ? "戌式式 " : "戍式式 ") + "[" + side + "] " + "depth=" + node.depth + " id="
                        + node.id + "\n");
            } else {
                builder.append(prefix + (isTail ? "戌式式 " : "戍式式 ") + "depth=" + node.depth + " id=" + node.id + "\n");
            }
            List<KdNode> children = null;
            if (node.lesser != null || node.greater != null) {
                children = new ArrayList<KdNode>(2);
                if (node.lesser != null)
                    children.add(node.lesser);
                if (node.greater != null)
                    children.add(node.greater);
            }
            if (children != null) {
                for (int i = 0; i < children.size() - 1; i++) {
                    builder.append(getString(children.get(i), prefix + (isTail ? "    " : "弛   "), false));
                }
                if (children.size() >= 1) {
                    builder.append(getString(children.get(children.size() - 1), prefix + (isTail ? "    " : "弛   "),
                            true));
                }
            }

            return builder.toString();
        }
    }
}