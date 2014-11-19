package driver;

import robotArm.Arm;
import robotArm.Position;

public class Runner {

	public static void main(String[] args){
		double[] armLengths = {1.0, 1.0, 2.0};
		double[][] angles = {new double[]{0,Math.PI/6,0},new double[]{0,0,0},new double[]{0,0,0}};
		
		Arm arm = new Arm(armLengths);
		Position p = new Position(); 
		System.out.println(p);
		arm.move(angles,p);
		System.out.println(p);
	}
}
