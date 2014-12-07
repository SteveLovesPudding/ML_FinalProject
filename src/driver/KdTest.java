package driver;

import java.util.ArrayList;
import java.util.List;

import kdTree.KdTree;
import robotArm.Position;


public class KdTest {

	public static void main(String[] args) {
		
		List<Position> test = new ArrayList<>();
		double[] pos0 = {0,0,0};
		double[] pos1 = {Math.random(),Math.random(),Math.random()};
		double[] pos2 = {Math.random(),Math.random(),Math.random()};
		double[] pos3 = {Math.random(),Math.random(),Math.random()};
		double[] pos4 = {Math.random(),Math.random(),Math.random()};
		double[] pos5 = {Math.random(),Math.random(),Math.random()};
		double[] pos6 = {Math.random(),Math.random(),Math.random()};
		double[] pos7 = {Math.random(),Math.random(),Math.random()};
		test.add(new Position(pos0));
		test.add(new Position(pos1));
		test.add(new Position(pos2));
		test.add(new Position(pos3));
		test.add(new Position(pos4));
		test.add(new Position(pos5));
		test.add(new Position(pos6));
		test.add(new Position(pos7));
		
		
		KdTree tree = new KdTree(test);
		System.out.println(KdTree.TreePrinter.getString(tree));
	}
}
