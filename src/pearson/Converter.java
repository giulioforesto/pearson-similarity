package pearson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

public class Converter {
	
	public static String FILE_NAME = "DataALL10M";
	public static int NUMBER_OF_FILM_INDEXES = 65133;
	public static int NUMBER_OF_USER_INDEXES = 71567;
	
	public static void main(String[] args) {
		try {
			int films[] = new int[NUMBER_OF_FILM_INDEXES+1];
			int users[] = new int[NUMBER_OF_USER_INDEXES+1];
			int nextFilm = 1;
			int nextUser = 1;
			
			BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME + ".dat"));
			PrintWriter output = new PrintWriter(FILE_NAME + ".arff", "UTF-8");
			
			output.println("@RELATION " + FILE_NAME + "\n");
			output.println("@ATTRIBUTE UserID"+"\t"+"numeric");
			output.println("@ATTRIBUTE ItemID"+"\t"+"numeric");
			output.println("@ATTRIBUTE Rating"+"\t"+"numeric\n");
			output.println("@DATA");
			
			/*
			 * For 10M file, Films and Users numbers have holes.
			 * The highest film number is 65133.
			 * The highest user number is 71567.
			 * The actual cardinals are:
			 * * films: 10677
			 * * users: 69878
			 */
			
			String line;
			while ((line = reader.readLine()) != null) {
				String newLine = line.replaceAll("::", ",").replaceAll(",[0-9]+$", "");
				String[] split = newLine.split(",");
				int filmID = Integer.parseInt(split[1]);
				int userID = Integer.parseInt(split[0]);
				
				if (films[filmID] != 0) {
					split[1] = String.valueOf(films[filmID]);
				}
				else {
					films[filmID] = nextFilm;
					split[1] = String.valueOf(nextFilm);
					nextFilm++;
				}
				
				if (users[userID] != 0) {
					split[0] = String.valueOf(users[userID]);
				}
				else {
					users[userID] = nextUser;
					split[0] = String.valueOf(nextUser);
					nextUser++;
				}
				
				newLine = split[0] + "," + split[1] + "," + split[2];
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