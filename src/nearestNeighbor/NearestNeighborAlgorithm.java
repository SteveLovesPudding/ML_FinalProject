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

public class NearestNeighborAlgorithm {

	private KdTree tree;
	private List<Point> nodesForTree;
	private int dimension;
	private Map<Point, Node> mapping;
	
	public NearestNeighborAlgorithm(String dataFile, int dimension) {
		nodesForTree = new ArrayList<>();
		mapping = new HashMap<>();
		this.dimension = dimension;
		try {
			buildTree(dataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public double[] getConfiguration(Position desiredPosition) {
		double[] dpArray = desiredPosition.getCoordinates();
		Matrix desiredPositionVector = new Matrix(new double[][] { {
				dpArray[0], dpArray[1], dpArray[2], 1 } });
		int numNearestNeighbors = 5;
		desiredPositionVector.print(1, 1);
		Node closestPoint;
		Node[] nearestNeighbors;
		int numJoints = getNearestNeighbors(
				this.getClosestPoint(desiredPosition, new double[] {}),
				numNearestNeighbors)[0].getNumJoints();
		List<Double> calculatedConfigurations = new ArrayList<Double>();

		double[][] xArray;
		double[][] qArray;
		// Greedy calculation for each joint
		for (int i = 1; i < numJoints; i++) {
			closestPoint = this.getClosestPoint(desiredPosition,
					convertToArray(calculatedConfigurations));
			nearestNeighbors = getNearestNeighbors(closestPoint,
					numNearestNeighbors);

			// Solve for Xw = Q
			xArray = new double[numNearestNeighbors][4];
			qArray = new double[numNearestNeighbors][1];

			for (int j = 0; j < numNearestNeighbors; j++) {
				qArray[0][j] = nearestNeighbors[j].getConfigurations()[j];
				for (int k = 0; k < 4; k++) {
					xArray[j][k] = nearestNeighbors[j].getEndEffectorArray()[k];
				}
			}

			Matrix x = new Matrix(xArray), q = new Matrix(qArray);
			Matrix w = x.solve(q);
			Matrix derivedConfiguration = desiredPositionVector.times(w);

			calculatedConfigurations.add(derivedConfiguration.get(0, 0));

		}
		
		return convertToArray(calculatedConfigurations);
	}
	
	public KdTree getTree() {
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

		// TODO add equation to take into account configurations
		return null;
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
		Collection<Point> positions = tree.nearestNeighbourSearch(numNeighbors, new Point(node.getConfigurations()) );
		Node[] neighbors = new Node[positions.size()];
		int index = 0;
		for (Point p: positions) {
			//System.out.println(p);
			neighbors[index] = mapping.get(p);
			index++;
		}
		
		return neighbors;
	}

	private double[] convertToArray(List<Double> list) {

		double[] array = new double[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}

		return array;
	}
	
	private void buildTree(String dataFile) throws IOException {
		BufferedReader data = null;
		try {
			data = new BufferedReader(new FileReader(dataFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line = data.readLine(); // first line is the configs, the following line is the position 
		while (line != null) {
	        StringTokenizer st = new StringTokenizer(line);
	        double[] configs = new double[st.countTokens()];
	        int index = 0;
	        while (st.hasMoreTokens()) {
	        	configs[index] = Double.parseDouble(st.nextToken());
	        	index++;
	        }
	        line = data.readLine();
	        //System.out.println(line);
	        st = new StringTokenizer(line);
	        Point p = new Point(configs);
	        Node n = new Node(configs, new Position(Double.parseDouble(st.nextToken()),
	        										Double.parseDouble(st.nextToken()),
	        										Double.parseDouble(st.nextToken()) ) );   
	        mapping.put(p, n);
	        nodesForTree.add(p);
	        line = data.readLine();
		}
		tree = new KdTree(nodesForTree, dimension);
	}
}
