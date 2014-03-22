package pearson;

import java.util.Set;
import java.util.TreeSet;

import pearson.Film;

public class Functions {
	
	public static Set<Film> commonFilmSet(User x, User y) {
		Set<Film> result = new TreeSet<Film>(x.ratedFilms.keySet());
		result.retainAll(y.ratedFilms.keySet());
		return result;
	}
	
	public static double pearsonSimilarity (User x, User y) {
		double numerator = 0;
		double xSum = 0;
		double ySum = 0;
		
		for (Film s : commonFilmSet(x,y)) {
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