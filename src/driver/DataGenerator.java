package driver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import robotArm.Arm;
import robotArm.JointDirection;
import robotArm.Position;

public class DataGenerator {
	private static int numDataPoints;
	private static int numJoints;
	private static String fileName;

	public static void main(String[] args) throws IOException {

		Scanner kb = new Scanner(System.in);

		System.out.println("How many joints?");
		boolean pass = false;
		numJoints = 0;
		while (!pass) {

			try {
				numJoints = kb.nextInt();
			} catch (InputMismatchException e) {
				kb.nextLine();
				System.out.println("Thats invalid");
				System.out.println("How many joints?");
				continue;
			}

			kb.nextLine();
			pass = true;

		}

		double[] armLengths = new double[numJoints];
		JointDirection[] joints = new JointDirection[numJoints];
		for (int i = 1; i <= numJoints; i++) {

			System.out.println("What is the length of arm " + i + "?");
			pass = false;
			while (!pass) {

				try {
					armLengths[i - 1] = kb.nextDouble();
				} catch (InputMismatchException e) {
					kb.nextLine();
					System.out.println("Thats invalid");
					System.out.println("What is the length of arm " + i + "?");
					continue;
				}

				kb.nextLine();
				pass = true;

			}
			pass = false;

			while (!pass) {
				System.out
						.println("What is the direction of rotation for the arm? [Z,Y]");

				try {
					String direction = kb.next();
					if (direction.equalsIgnoreCase("Y")) {
						joints[i - 1] = JointDirection.Y;
					} else if (direction.equalsIgnoreCase("Z")) {
						joints[i - 1] = JointDirection.Z;
					} else {
						System.out.println("Thats invalid");
						continue;
					}
				} catch (InputMismatchException e) {
					kb.nextLine();
					System.out.println("Thats invalid");
					continue;
				}

				kb.nextLine();
				pass = true;

			}
		}

		System.out.println("How many data points?");
		pass = false;
		numDataPoints = 0;
		while (!pass) {

			try {
				numDataPoints = kb.nextInt();
			} catch (InputMismatchException e) {
				kb.nextLine();
				System.out.println("Thats invalid");
				System.out.println("How many data points?");
				continue;
			}

			kb.nextLine();
			pass = true;

		}

		System.out
				.println("What file would you like the data to be generated in?");
		fileName = kb.nextLine();
		kb.close();

		double[] randomConfigurations;

		int numDataCreated = 0;
		BufferedWriter writer = null; // for data writing purposes
		File dataFile = new File(fileName);
		BufferedWriter secretWriter = null;
		File secretDataFile = new File(fileName + ".position_only");
		String out = "" + numJoints + "\n";
		String secretOut = "";

		Arm arm = new Arm(armLengths, joints);
		while (numDataCreated < numDataPoints) {

			Position p = new Position();
			// System.out.println(""+jointConfigurations[0][0] +" "+
			// jointConfigurations[0][1] +" "+ jointConfigurations[0][2]);
			randomConfigurations = createRandomConfiguration();
			if (arm.move(createRandomConfiguration(), p)) {
				// if the arm moved successfully, # separates each joint (ex.
				// q1x,q1y,q1z # q2x,q2y,q2z #...)
				for (int joint = 0; joint < numJoints; joint++) {
					// write out for each joint
					out += randomConfigurations[joint] + " ";
				}
				out += "\n";
				out += p.toString() + "\n"; // end-effector x,y,z
				secretOut += p.toString() + "\n";
				numDataCreated++;
			}
		}
		try {
			writer = new BufferedWriter(new FileWriter(dataFile));
			writer.write(out);
			secretWriter = new BufferedWriter(new FileWriter(secretDataFile));
			secretWriter.write(secretOut);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			writer.close();
			secretWriter.close();
		}
		System.out.println("DATA WRITTEN TO " + fileName + "  :)");
	}

	/**
	 * 
	 * @return double array of random configurations.
	 */
	private static double[] createRandomConfiguration() {
		double[] jointConfigurations = new double[numJoints];
		for (int i = 0; i < numJoints; i++) { // populate random joint
												// Configurations
			jointConfigurations[i] = Math.random() * 2 * Math.PI;
		}
		return jointConfigurations;
	}
}
