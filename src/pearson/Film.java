package pearson;

import java.util.HashMap;

public class Film {
	public Film(int filmID) {
		this.id = filmID;
	}

	public int id;
	
	public HashMap<User,Float> ratings = new HashMap<User,Float>();
}