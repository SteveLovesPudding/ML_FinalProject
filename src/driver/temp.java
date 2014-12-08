package driver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class temp {
	private static String filename = "data_twoArm_1000_positions.txt";
	public static void main(String[] args) throws IOException {
		// helper driver to just pick out position values from the data file.
		BufferedReader data = new BufferedReader(new FileReader("data_twoArm_1000.txt"));
		String out = "";
		String line = data.readLine();
		while (line != null) {
	        StringTokenizer st = new StringTokenizer(line);
	        if (st.countTokens() == 3) {
	        	out += line+"\n";
	        }
	        line = data.readLine();
		}
		BufferedWriter writer = null;
        try {
            File logFile = new File(filename);

            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
		System.out.println("Position data extracted and written to '"+filename+"'");
	}
}
