package pearson;

import weka.core.Instances;
import weka.clusterers.*;
import weka.clusterers.ClusterEvaluation;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class Pearson {
	
	public class film {
		private int id;
		
		private HashMap<user,Double> ratings;
	}
	
	public class user {
		
		private int id;
		
		private HashMap<film,Double> ratedFilms;
		
		private double getMeanRating() {
			double sum = 0;
			for (film f : this.ratedFilms.keySet()) {
				sum += this.ratedFilms.get(f);
			}
			return sum/(double)this.ratedFilms.size();
		}
		
		private double getPredictedRating(film f) {
			double result = this.getMeanRating();
			for (user u : f.ratings.keySet()) {
				result += pearsonSimilarity(this, u)
						*(u.ratedFilms.get(f) - u.getMeanRating());
			}
			return result;
		}
	}
	
	public static Set<film> commonFilmSet(user x, user y) {
		Set<film> result = new TreeSet<film>(x.ratedFilms.keySet());
		result.retainAll(y.ratedFilms.keySet());
		return result;
	}
	
	public static double pearsonSimilarity (user x, user y) {
		double numerator = 0;
		double xSum = 0;
		double ySum = 0;
		
		for (film s : commonFilmSet(x,y)) {
			numerator += (x.ratedFilms.get(s)-x.getMeanRating())
					*(y.ratedFilms.get(s)-y.getMeanRating());
			
			xSum += Math.pow(x.ratedFilms.get(s)-x.getMeanRating(),2);
			ySum += Math.pow(y.ratedFilms.get(s)-y.getMeanRating(),2);
		}
		
		double denominator = Math.sqrt(xSum)*Math.sqrt(ySum);
		if (denominator == 0) {
			numerator = 0; // result is NaN
		}
		return numerator/denominator;
	}
}