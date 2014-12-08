package driver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import robotArm.Arm;
import robotArm.JointDirection;
import robotArm.Position;

public class DataGenerator {
	private static int numDataPoints = 1000;
	private static int numJoints = 2;
	private static String fileName = "data_twoArm_1000.txt";
	public static void main(String[] args) throws IOException {
		double[] armLengths = {3.0,1.0};
		double[] randomConfigurations;
		JointDirection[] joints = new JointDirection[numJoints];
		/*for (int i=0; i < numJoints; i++) {
			joints[i] = JointDirection.Y;
		}*/
		joints[0] = JointDirection.Y; 
		joints[1] = JointDirection.Z;
		
		int numDataCreated = 0;
	    BufferedWriter writer = null; // for data writing purposes
	    File dataFile = new File(fileName);
	    String out = "";
	    
	    Arm arm = new Arm(armLengths, joints);
		while (numDataCreated < numDataPoints) {
			
			Position p = new Position();
			//System.out.println(""+jointConfigurations[0][0] +" "+ jointConfigurations[0][1] +" "+ jointConfigurations[0][2]);
			randomConfigurations = createRandomConfiguration();
			if (arm.move(createRandomConfiguration(),p)) {
				// if the arm moved successfully, # separates each joint (ex. q1x,q1y,q1z # q2x,q2y,q2z #...)
				for (int joint = 0; joint < numJoints; joint++) {
					// write out for each joint
					out += randomConfigurations[joint] + " ";  
				}  
				out += "\n";
				out += p.toString() +"\n"; // end-effector x,y,z
				numDataCreated++;
			}
		}
		try {
            writer = new BufferedWriter(new FileWriter(dataFile));
            writer.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	writer.close();
        }
		System.out.println("DATA WRITTEN TO " +fileName+"  :)");
	}
	private static double[] createRandomConfiguration() {
		double[] jointConfigurations = new double[numJoints];
		for(int i=0; i<numJoints; i++) {  // populate random joint Configurations
			jointConfigurations[i] =  Math.random()*2*Math.PI;
		}
		return jointConfigurations;
	}
}
