package driver;

import robotArm.Arm;
import robotArm.JointDirection;
import robotArm.Position;

public class Runner {

	public static void main(String[] args){
		double[] armLengths = {1.0};
		JointDirection[] jointDirections = {JointDirection.Z}; 
		System.out.println(Math.cos(Math.PI/2));
		double[] angles = {Math.PI/2};
		
		Arm arm = new Arm(armLengths, jointDirections);
		Position p = new Position(); 
		System.out.println(p);
		arm.move(angles,p);
		System.out.println(p);
	}
}
