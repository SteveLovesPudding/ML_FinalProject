package nearestNeighbor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import kdTree.KdTree;
import kdTree.Point;
import robotArm.Position;
import Jama.Matrix;

/**
 * 
 * @author Steven Lin slin45 and Tae Koo Kim tkim60 with code from Comparator
 *         class, courtesy Justin Wetherell <phishman3579@gmail.com>
 *         http://en.wikipedia.org/wiki/K-d_tree
 */
public class NearestNeighborAlgorithm {

	/**
	 * Kd tree that provides efficient nearest neighbor search.
	 */
	private KdTree<Point> tree;

	/**
	 * List of points that hold a node that belongs to the tree.
	 */
	private List<Point> nodesForTree;

	/**
	 * Number of joints which corresponds to the kd tree.
	 */
	private int dimension;

	/**
	 * Maps a point to a node
	 */
	private Map<Point, Node> mapping;

	private int numNearestNeighbors;

	/**
	 * Constructor.
	 * 
	 * @param dataFile
	 *            Datafile that contains the generated examples.
	 * @param dimension
	 *            Number of joints which corresponds k-d tree.
	 */
	public NearestNeighborAlgorithm(String dataFile, int dimension,
			int numNearestNeighbors) {

		nodesForTree = new ArrayList<Point>();
		mapping = new HashMap<Point, Node>();
		this.dimension = dimension;
		this.numNearestNeighbors = numNearestNeighbors;
		try {
			buildTree(dataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Implementation based on the literature from :
	 * http://link.springer.com/chapter/10.1007%2F978-3-642-01213-6_2#page-1
	 * 
	 * @param desiredPosition
	 *            Position for which the inverse kinematics is found.
	 * @return Configuration set to achieved desired position.
	 */
	public double[] getConfiguration(Position desiredPosition) {

		// Creates vector of desired position for use of JAMA matrix package
		double[] dpArray = desiredPosition.getCoordinates();
		Matrix desiredPositionVector = new Matrix(new double[][] { {
				dpArray[0], dpArray[1], dpArray[2], 1 } });

		Node closestPoint;
		Node[] nearestNeighbors;
		List<Double> calculatedConfigurations = new ArrayList<Double>();

		// Models Physical space x * linear weights w = configuration space q
		double[][] xArray;
		double[][] qArray;
		// Greedy calculation for each joint
		for (int i = 0; i < dimension; i++) {

			closestPoint = this.getClosestPoint(desiredPosition,
					convertToArray(calculatedConfigurations));
			nearestNeighbors = getNearestNeighbors(closestPoint,
					numNearestNeighbors);

			// Solve for Xw = Q
			xArray = new double[numNearestNeighbors][4];
			qArray = new double[numNearestNeighbors][1];
			for (int j = 0; j < numNearestNeighbors; j++) {
				qArray[j][0] = nearestNeighbors[j].getConfigurations()[i];
				for (int k = 0; k < 3; k++) {
					xArray[j][k] = nearestNeighbors[j].getEndEffectorArray()[k];
				}
				xArray[j][3] = 1;
			}

			Matrix x = new Matrix(xArray), q = new Matrix(qArray);
			Matrix w = x.solve(q);
			Matrix derivedConfiguration = desiredPositionVector.times(w);

			// Add configuration for one joint, need to re-evaluate for nearest
			// neighbor
			calculatedConfigurations.add(derivedConfiguration.get(0, 0));

		}

		return convertToArray(calculatedConfigurations);
	}

	/**
	 * 
	 * @return Tree used by nearest neighbor algorithm.
	 */
	public KdTree<Point> getTree() {
		return this.tree;
	}

	/**
	 * Return the point closest to desiredPosition in physical space
	 * 
	 * @param desiredPosition
	 *            Physical location
	 * @param configurations
	 *            Provided configuration
	 * @return
	 */
	private Node getClosestPoint(Position desiredPosition,
			double[] configurations) {

		double minValue = Double.MAX_VALUE;
		double[] target = desiredPosition.getCoordinates();
		Node closestNeighbor = null;

		for (Node node : mapping.values()) {

			double[] configs = node.getConfigurations(), endEffector = node
					.getEndEffectorArray();

			double sum = 0;
			// euclidean distance
			for (int i = 0; i < 3; i++) {
				sum += Math.pow(target[i] - endEffector[i], 2);
			}
			// factors in known configuration
			for (int i = 0; i < configurations.length; i++) {
				sum += Math.pow(configurations[i] - configs[i], 2);
			}

			if (sum < minValue) {
				minValue = sum;
				closestNeighbor = node;
			}
		}
		return closestNeighbor;
	}

	/**
	 * Return the nearest neighbors in configuration space
	 * 
	 * @param node
	 *            node for comparison
	 * @param numNeighbors
	 *            number of nearest neighbors
	 * @return an array of nearest neighbors
	 */
	public Node[] getNearestNeighbors(Node node, int numNeighbors) {
		Collection<Point> positions = tree.nearestNeighbourSearch(numNeighbors,
				new Point(node.getConfigurations()));
		Node[] neighbors = new Node[positions.size()];
		int index = 0;
		for (Point p : positions) {
			neighbors[index] = mapping.get(p);
			index++;
		}

		return neighbors;
	}

	/**
	 * 
	 * @param list
	 *            Converts list to array.
	 * @return List in double array format.
	 */
	private double[] convertToArray(List<Double> list) {

		double[] array = new double[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}

		return array;
	}

	/**
	 * Creates tree based on generated datafile.
	 * 
	 * courtesy Justin Wetherell <phishman3579@gmail.com>
	 * http://en.wikipedia.org/wiki/K-d_tree utilized in Mr. Wetherell's
	 * implementation of Kd tree to construct a kd tree.
	 * 
	 * @param dataFile
	 *            Generated data file.
	 * 
	 * @throws IOException
	 *             When data file can't be found
	 */
	private void buildTree(String dataFile) throws IOException {

		BufferedReader data = null;
		try {
			data = new BufferedReader(new FileReader(dataFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line = data.readLine(); // first line is the configs, the
										// following line is the position
		while (line != null) {
			StringTokenizer st = new StringTokenizer(line);
			double[] configs = new double[st.countTokens()];
			int index = 0;
			while (st.hasMoreTokens()) {
				configs[index] = Double.parseDouble(st.nextToken());
				index++;
			}
			line = data.readLine();

			st = new StringTokenizer(line);
			Point p = new Point(configs);
			Node n = new Node(configs, new Position(Double.parseDouble(st
					.nextToken()), Double.parseDouble(st.nextToken()),
					Double.parseDouble(st.nextToken())));
			mapping.put(p, n);
			nodesForTree.add(p);
			line = data.readLine();
		}
		tree = new KdTree<Point>(nodesForTree, dimension);
	}
}
