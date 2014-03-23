package pearson;

import java.util.HashSet;

import pearson.Film;

public class Functions {
	
	public static HashSet<Film> commonFilmSet(User x, User y) {
		HashSet<Film> result = new HashSet<Film>(x.ratedFilms.keySet());
		result.retainAll(y.ratedFilms.keySet());
		return result;
	}
	
	public static float pearsonSimilarity (User x, User y) {
		float numerator = 0;
		float xSum = 0;
		float ySum = 0;
		
		HashSet<Film> commonFilmSet = commonFilmSet(x,y); 
		for (Film s : commonFilmSet) {
			float sx = x.ratedFilms.get(s);
			float sy = y.ratedFilms.get(s);
			numerator += (sx - x.meanRating)
					*(sy - y.meanRating);
			
			xSum += (float)Math.pow(sx-x.meanRating,2);
			ySum += (float)Math.pow(sy-y.meanRating,2);
		}
		
		float denominator = (float)(Math.sqrt(xSum)*Math.sqrt(ySum));
		if (denominator*numerator == 0) {
			numerator = 0;
			denominator = 0; // result is NaN
		}
		return numerator/denominator;
	}
}