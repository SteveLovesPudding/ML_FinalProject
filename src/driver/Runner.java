package driver;

import robotArm.Arm;
import robotArm.Position;

public class Runner {

	public static void main(String[] args){
		double[] armLengths = {1.0};
		System.out.println(Math.cos(Math.PI/2));
		double[][] angles = {new double[]{Math.PI/6,0,0}};
		
		Arm arm = new Arm(armLengths);
		Position p = new Position(); 
		System.out.println(p);
		arm.move(angles,p);
		System.out.println(p);
	}
}
