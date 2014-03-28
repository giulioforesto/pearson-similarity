package pearson;

import weka.core.Attribute;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

public class Pearson {	
	
	// Set data volume version: "100k", "1M" or "10M"
	public static String VOLUME = "1M";
	
	public static String FILE = "DataALL" + VOLUME + ".arff";
	public static int NUMBER_OF_USERS = Functions.getData("users", VOLUME);
	public static int NUMBER_OF_FILMS = Functions.getData("films", VOLUME);;
	
	public static Instances data;
	
	public static int size;
	public static Attribute UserID;
	public static Attribute ItemID;
	public static Attribute Rating;
	
	public static float[] meanRatings = new float[NUMBER_OF_USERS+1];
	public static int[] cardinals = new int[NUMBER_OF_USERS+1];
	public static float[][] filmSets = new float[NUMBER_OF_FILMS+1][NUMBER_OF_USERS+1];
	public static float[][] similarities = new float[NUMBER_OF_USERS+1][NUMBER_OF_USERS+1];
	public static float[][] simDenominator = new float[NUMBER_OF_USERS+1][NUMBER_OF_USERS+1];
	public static float[][] predictions = new float[NUMBER_OF_USERS+1][NUMBER_OF_FILMS+1];
	
	public static void main(String[] args) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(FILE));
			data = new Instances(reader);
			reader.close();
			UserID = data.attribute("UserID");
			ItemID = data.attribute("ItemID");
			Rating = data.attribute("Rating");
			size = data.size();
			
			long startTime = System.currentTimeMillis();
			
			Functions.calculateMeanRatings();
			System.out.println("Calculated mean ratings after "
					+ (System.currentTimeMillis()-startTime)/1000
					+ " s");
			Functions.calculateSimilarities();
			System.out.println("Calculated similarities after "
					+ (System.currentTimeMillis()-startTime)/1000
					+ " s");
			Functions.calculatePredictions();
			System.out.println("Calculated predictions after "
					+ (System.currentTimeMillis()-startTime)/1000
					+ " s");
			
			PrintWriter output = new PrintWriter("ratingPredictions.txt", "UTF-8");
			for (int i = 1; i <= NUMBER_OF_USERS; i++) {
				for (int j = 1; j <= NUMBER_OF_FILMS; j++) {
					if (predictions[i][j] != 0) {
						output.println(i + "," + j + "," + predictions[i][j]);
					}
				}
			}
			output.println(
					"Executed in: "
					+ (System.currentTimeMillis()-startTime)/1000
					+ " s");
			output.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}