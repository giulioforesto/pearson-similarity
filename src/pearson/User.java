package pearson;

import java.util.HashMap;

import pearson.Film;
import pearson.Functions;

public class User {
	
	public User(int value) {
		this.id = value;
	}
	
	private int id;
	
	public HashMap<Film,Float> ratedFilms = new HashMap<Film,Float>();

	public float meanRating = 0;
	private int numberOfRatedFilms = 0;
	
	public void increaseMeanRating (float rating) {
		meanRating *= numberOfRatedFilms;
		meanRating += rating;
		numberOfRatedFilms++;
		meanRating /= (float)numberOfRatedFilms; 
	}
	
	public float getMeanRating() {
		float sum = 0;
		for (Film f : this.ratedFilms.keySet()) {
			sum += this.ratedFilms.get(f);
		}
		return sum/(float)this.ratedFilms.size();
	}
	
	public float getPredictedRating(Film f) {
		float result = this.meanRating;
		for (User u : f.ratings.keySet()) {
			Pearson.tuple XY = new Pearson.tuple(this.id, u.id);
			Float calcSim = Pearson.calculatedSim.get(XY);
			
			if (calcSim == null) {
				calcSim = Functions.pearsonSimilarity(this, u);
				Pearson.calculatedSim.put(XY,calcSim);
			}
			result += calcSim*(u.ratedFilms.get(f) - u.meanRating);
		}
		return result;
	}
}