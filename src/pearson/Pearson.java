package pearson;

import weka.core.Attribute;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

/*
 * Please use the Converter class to create an arff file from the 10M file.
 * The other arff files (100k and 1M) can be converted manually by adding the usual arff labels.
 */

public class Pearson {	
	
	public static Set<Float> f = new HashSet<Float>();
	
	// Set data volume version: "100k", "1M" or "10M"
	public static String VOLUME = "10M";
	// Set mode: "su" (single user) or "all"
	// If mode == "su", set user
	public static String MODE = "su";
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
	
	
	public static long startTime;
	
	public static void main(String[] args) {
		try {
			startTime = System.currentTimeMillis();
			
			BufferedReader reader = new BufferedReader(new FileReader(FILE));
			data = new Instances(reader);
			reader.close();
			UserID = data.attribute("UserID");
			ItemID = data.attribute("ItemID");
			Rating = data.attribute("Rating");
			size = data.size();
			
			System.out.println("Initialized data after "
					+ (System.currentTimeMillis()-Pearson.startTime)/1000
					+ " s");
			
			if (MODE == "all") {
				Functions.all all = new Functions.all();
				all.execute();
			}
			else if (MODE == "su") {
				Functions.su su = new Functions.su(USER);
				su.execute();
			}
			
			System.out.println("Executed in: "
					+ (System.currentTimeMillis()-Pearson.startTime)/1000
					+ " s");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}