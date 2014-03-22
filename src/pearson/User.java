package pearson;

import java.util.HashMap;

import pearson.Film;
import pearson.Functions;

public class User {
	
	private int id;
	
	public HashMap<Film,Double> ratedFilms = new HashMap<Film,Double>();
	
	public User(int value) {
		this.id = value;
	}

	public double getMeanRating() {
		double sum = 0;
		for (Film f : this.ratedFilms.keySet()) {
			sum += this.ratedFilms.get(f);
		}
		return sum/(double)this.ratedFilms.size();
	}
	
	private double getPredictedRating(Film f) {
		double result = this.getMeanRating();
		for (User u : f.ratings.keySet()) {
			result += Functions.pearsonSimilarity(this, u)
					*(u.ratedFilms.get(f) - u.getMeanRating());
		}
		return result;
	}
}