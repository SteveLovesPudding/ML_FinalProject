package driver;

import robotArm.Arm;

public class Runner {

	public static void main(String[] args){
		double[] armLengths = {2.0, 1.3, 2.1};
		double[] angles = {Math.PI/4, Math.PI/2, Math.PI/2, Math.PI/2, Math.PI/4, Math.PI/2, 1.0, 3.0, 1.0};
		
		Arm arm = new Arm(armLengths);
		arm.move(angles);
	}
}
