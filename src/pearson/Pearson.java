package pearson;

import weka.core.Attribute;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;

public class Pearson {
	
	public static int NUMBER_OF_USERS = 943;
	public static int NUMBER_OF_FILMS = 1682;
	
	public static User[] users = new User[NUMBER_OF_USERS+1];
	public static Film[] films = new Film[NUMBER_OF_FILMS+1];
	
	public static void main(String[] args) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("DataALL.arff"));
			Instances data = new Instances(reader);
			reader.close();
			Attribute UserID = data.attribute("UserID");
			Attribute ItemID = data.attribute("ItemID");
			Attribute Rating = data.attribute("Rating");
			for (int i = 0; i < data.size(); i++) {
				System.out.println(i);
				int userID = (int)data.get(i).value(UserID);
				int filmID = (int)data.get(i).value(ItemID);
				Double rating = data.get(i).value(Rating);
				
				User userInstance = users[userID];
				if (userInstance == null) {
					userInstance = new User(userID);
				}
				
			Film filmInstance = films[filmID];
				if (filmInstance == null) {
					filmInstance = new Film(filmID);
				}
				
				userInstance.ratedFilms.put(filmInstance, rating);
				filmInstance.ratings.put(userInstance, rating);
				users[userID] = userInstance;
				films[filmID] = filmInstance;
			}
			
			
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}