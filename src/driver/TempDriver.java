package driver;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TempDriver {

	private static int joints;
	private static double[] armLengths; 
	
	public static void main(String[] args) {
		Scanner kb = new Scanner(System.in);

		System.out.println("How many joints?");
		boolean pass = false;
		while (!pass) {

			try {
				joints = kb.nextInt();
			} catch (InputMismatchException e) {
				kb.nextLine();
				System.out.println("Thats invalid");
				System.out.println("How many joints?");
				continue;
			}

			kb.nextLine();
			pass = true;

		}
		
		armLengths = new double[joints]; 
		
		for(int i=1; i<=joints; i++){
			
			System.out.println("What is the length of arm " + i + "?");
			pass = false;
			while (!pass) {

				try {
					armLengths[i-1] = kb.nextDouble();
				} catch (InputMismatchException e) {
					kb.nextLine();
					System.out.println("Thats invalid");
					System.out.println("What is the length of arm " + i + "?");
					continue;
				}

				kb.nextLine();
				pass = true;

			}
		}
		
	}
}
