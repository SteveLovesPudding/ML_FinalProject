package robotArm;

import java.io.IOException;

import nearestNeighbor.NearestNeighborAlgorithm;

public class Tester {

	public static void main(String[] args) throws IOException {
		NearestNeighborAlgorithm alg = new NearestNeighborAlgorithm("test.txt");

		double[] config = alg.getConfiguration(new Position(3.463654325519007,29.755851066636115,3.6168436054842092), 4);
		for (double con : config)
			System.out.println(con);
	}
}
