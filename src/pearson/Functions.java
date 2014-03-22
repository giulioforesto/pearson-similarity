package pearson;

import java.util.Enumeration;
import java.util.HashSet;

import weka.core.DistanceFunction;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.neighboursearch.PerformanceStats;
import pearson.Film;

public class Functions {
	
	public static class fct implements DistanceFunction {

		@Override
		public String[] getOptions() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Enumeration listOptions() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setOptions(String[] arg0) throws Exception {
			// TODO Auto-generated method stub
			
		}

		@Override
		public double distance(Instance arg0, Instance arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double distance(Instance arg0, Instance arg1,
				PerformanceStats arg2) throws Exception {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double distance(Instance arg0, Instance arg1, double arg2) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double distance(Instance arg0, Instance arg1, double arg2,
				PerformanceStats arg3) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String getAttributeIndices() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Instances getInstances() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean getInvertSelection() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void postProcessDistances(double[] arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setAttributeIndices(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setInstances(Instances arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setInvertSelection(boolean arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void update(Instance arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
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