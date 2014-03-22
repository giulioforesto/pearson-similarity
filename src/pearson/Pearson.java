package pearson;

import weka.core.Attribute;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;

public class Pearson {
	
	public static class tuple {
		private int x;
		private int y;
		
		public tuple(int X, int Y) {
			if (X <= Y) {
				this.x = X;
				this.y = Y;
			} else {
				this.x = Y;
				this.y = X;
			}
		}
	}
	
	public static int NUMBER_OF_USERS = 943;
	public static int NUMBER_OF_FILMS = 1682;
	
	public static HashMap<Integer,User> users = new HashMap<Integer,User>();
	public static HashMap<Integer,Film> films = new HashMap<Integer,Film>();
	public static HashMap<tuple,Float> calculatedSim = new HashMap<tuple,Float>();
	
	public static void main(String[] args) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("DataALL.arff"));
			Instances data = new Instances(reader);
			reader.close();
			Attribute UserID = data.attribute("UserID");
			Attribute ItemID = data.attribute("ItemID");
			Attribute Rating = data.attribute("Rating");
			for (int i = 0; i < data.size(); i++) {
				int userID = (int)data.get(i).value(UserID);
				int filmID = (int)data.get(i).value(ItemID);
				float rating = (float)data.get(i).value(Rating);
				
				User userInstance = users.get(userID);
				if (userInstance == null) {
					userInstance = new User(userID);
				}
				
			Film filmInstance = films.get(filmID);
				if (filmInstance == null) {
					filmInstance = new Film(filmID);
				}
				
				userInstance.ratedFilms.put(filmInstance, rating);
				userInstance.increaseMeanRating(rating);
				
				filmInstance.ratings.put(userInstance, rating);
				users.put(userID,userInstance);
				films.put(filmID,filmInstance);
			}
			
			PrintWriter output = new PrintWriter("ratingPredictions.txt", "UTF-8");
			for (int i : users.keySet()) {
				for (int j : films.keySet()) {
					if (!users.get(i).ratedFilms.containsKey(films.get(j))) {
						output.println(
								i + ","
								+ j + ","
								+ users.get(i).getPredictedRating(films.get(j))
						);
					}
				}
			}
			output.close();
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}