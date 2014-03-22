package pearson;

import java.util.HashMap;

public class Film {
	public Film(int filmID) {
		this.id = filmID;
	}

	private int id;
	
	public HashMap<User,Double> ratings = new HashMap<User,Double>();
}