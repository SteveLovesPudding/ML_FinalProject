package driver;

import kdTree.KdTree;
import nearestNeighbor.NearestNeighborAlgorithm;
import nearestNeighbor.Node;
import robotArm.Position;


public class KdTest {

	public static void main(String[] args) {
		
//		List<Position> test = new ArrayList<>();
//		double[] pos0 = {0,0,0};
//		double[] pos1 = {Math.random(),Math.random(),Math.random()};
//		double[] pos2 = {Math.random(),Math.random(),Math.random()};
//		double[] pos3 = {Math.random(),Math.random(),Math.random()};
//		double[] pos4 = {Math.random(),Math.random(),Math.random()};
//		double[] pos5 = {Math.random(),Math.random(),Math.random()};
//		double[] pos6 = {Math.random(),Math.random(),Math.random()};
//		double[] pos7 = {Math.random(),Math.random(),Math.random()};
//		test.add(new Position(pos0));
//		test.add(new Position(pos1));
//		test.add(new Position(pos2));
//		test.add(new Position(pos3));
//		test.add(new Position(pos4));
//		test.add(new Position(pos5));
//		test.add(new Position(pos6));
//		test.add(new Position(pos7));
//		
//		
		/*KdTree tree = new KdTree(test);
		System.out.println(KdTree.TreePrinter.getString(tree));*/
		//NearestNeighborAlgorithm nn = new NearestNeighborAlgorithm("data_oneArm_100.txt", 1);
		
		int numNeighbors = 5;
		int dimensions = 2;
		NearestNeighborAlgorithm nn = new NearestNeighborAlgorithm("data_twoArm_1500.txt", dimensions);
		System.out.println(KdTree.TreePrinter.getString(nn.getTree()));
		
		double[] query = {6.277, 4.3};
		Node[] neighbors = nn.getNearestNeighbors(new Node(query, new Position(0.5,0,0.5)), 5);
		for (Node n : neighbors) {
			System.out.println(n);
		}
	}
}
