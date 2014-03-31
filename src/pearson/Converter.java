package pearson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

public class Converter {
	
	public static String FILE_NAME = "DataALL10M";
	
	public static void main(String[] args) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME + ".dat"));
			PrintWriter output = new PrintWriter(FILE_NAME + ".arff", "UTF-8");
			
			output.println("@RELATION " + FILE_NAME + "\n");
			output.println("@ATTRIBUTE UserID");
			output.println("@ATTRIBUTE ItemID");
			output.println("@ATTRIBUTE Rating");
			output.println("\n@DATA");
			
			String line;
			while ((line = reader.readLine()) != null) {
				String newLine = line.replaceAll("::", ",").replaceAll(",[0-9]+$", "");
				output.println(newLine);
			}
			output.close();
			reader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}