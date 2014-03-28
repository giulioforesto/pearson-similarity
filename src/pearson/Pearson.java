package pearson;

import weka.core.Attribute;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;

public class Pearson {	
	
	// Set data volume version: "100k", "1M" or "10M"
	public static String VOLUME = "1M";
	// Set mode: "su" (single user) or "all"
	// If mode == "su", set user
	public static String MODE = "all";
	public static int USER = 15;
	
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
	
	public static long startTime;
	
	public static void main(String[] args) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(FILE));
			data = new Instances(reader);
			reader.close();
			UserID = data.attribute("UserID");
			ItemID = data.attribute("ItemID");
			Rating = data.attribute("Rating");
			size = data.size();
			
			startTime = System.currentTimeMillis();
			
			Functions.calculateMeanRatings();
			System.out.println("Calculated mean ratings after "
					+ (System.currentTimeMillis()-startTime)/1000
					+ " s");
			
			if (MODE == "all") {
				Functions.all all = new Functions.all();
				all.execute();
			}
			else if (MODE == "su") {
				Functions.su su = new Functions.su(USER);
				su.execute();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}