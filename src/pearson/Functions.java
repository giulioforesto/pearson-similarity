package pearson;

import java.io.PrintWriter;

public class Functions {
	
	/*
	 * DATA		NUMBER_OF_USERS	NUMBER_OF_FILMS
	 * 100k		943				1682
	 * 1M		6040			3952
	 * 10M		71567			10681
	 */
	
	public static int getData(String type, String volume) {
		switch (volume) {
		case "100k":
			if (type == "users") {
				return 943;
			}
			else if (type == "films") {
				return 1682;
			}
		case "1M":
			if (type == "users") {
				return 6040;
			}
			else if (type == "films") {
				return 3952;
			}
		case "10M":
			if (type == "users") {
				return 71567;
			}
			else if (type == "films") {
				return 10681;
			}
		}
		return 0;
	}
	
	public static void calculateMeanRatings() {
		for (int i = 0; i < Pearson.size; i++) {
			int userID = (int)Pearson.data.get(i).value(Pearson.UserID);
			int filmID = (int)Pearson.data.get(i).value(Pearson.ItemID);
			float rating = (float)Pearson.data.get(i).value(Pearson.Rating);
			
			Pearson.meanRatings[userID] += rating;
			Pearson.cardinals[userID]++; 
			
			Pearson.filmSets[filmID][userID] = rating;
		}
		
		for (int i = 1; i <= Pearson.NUMBER_OF_USERS; i++) {
			Pearson.meanRatings[i] /= Pearson.cardinals[i];
		}
	}
	
	public static class all {
		private float[][] similarities = new float[Pearson.NUMBER_OF_USERS+1][Pearson.NUMBER_OF_USERS+1];
		private float[][] simDenominator = new float[Pearson.NUMBER_OF_USERS+1][Pearson.NUMBER_OF_USERS+1];
		private float[][] predictions = new float[Pearson.NUMBER_OF_USERS+1][Pearson.NUMBER_OF_FILMS+1];
		
		void calculateSimilarities() {
			for (int i = 0; i < Pearson.size; i++) {
				int userID = (int)Pearson.data.get(i).value(Pearson.UserID);
				int filmID = (int)Pearson.data.get(i).value(Pearson.ItemID);
				float rating = (float)Pearson.data.get(i).value(Pearson.Rating);
				
				for (int j = 1; j < userID; j++) {
					if (Pearson.filmSets[filmID][j] != 0) {
						similarities[userID][j] +=
								(rating - Pearson.meanRatings[userID])
								*(Pearson.filmSets[filmID][j] - Pearson.meanRatings[j]);
						simDenominator[userID][j] +=
								Math.pow(rating - Pearson.meanRatings[userID], 2);
						simDenominator[j][userID] +=
								Math.pow(Pearson.filmSets[filmID][j]
										- Pearson.meanRatings[j], 2);
					}
				}
				for (int j = userID; j <= Pearson.NUMBER_OF_USERS; j++) {
					if (Pearson.filmSets[filmID][j] != 0) {
						similarities[j][userID] +=
								(rating - Pearson.meanRatings[userID])
								*(Pearson.filmSets[filmID][j] - Pearson.meanRatings[j]);
						simDenominator[userID][j] +=
								Math.pow(rating - Pearson.meanRatings[userID], 2);
						simDenominator[j][userID] +=
								Math.pow(Pearson.filmSets[filmID][j]
										- Pearson.meanRatings[j], 2);
					}
				}
			}
			
			for (int i = 1; i < Pearson.NUMBER_OF_USERS; i++) {
				for (int j = 1; j <= i; j++) {
					similarities[i][j] /= (Math.sqrt(simDenominator[i][j])
							*Math.sqrt(simDenominator[j][i]));
				}
			}
		}
		
		void calculatePredictions() {
			for (int i = 0; i <= Pearson.NUMBER_OF_USERS; i++) {
				for (int j = 0; j <= Pearson.NUMBER_OF_FILMS; j++) {
					predictions[i][j] = Pearson.meanRatings[i];
				}
			}
			for (int i = 0; i < Pearson.size; i++) {
				int userID = (int)Pearson.data.get(i).value(Pearson.UserID);
				int filmID = (int)Pearson.data.get(i).value(Pearson.ItemID);
				float rating = (float)Pearson.data.get(i).value(Pearson.Rating);
				
				for (int j = 1; j < userID; j++) {
					if (Pearson.filmSets[filmID][j] == 0) {
						predictions[j][filmID] += similarities[userID][j]
								*(rating - Pearson.meanRatings[userID]);
					}
				}
				for (int j = userID; j <= Pearson.NUMBER_OF_USERS; j++) {
					if (Pearson.filmSets[filmID][j] == 0) {
						predictions[j][filmID] += similarities[j][userID]
								*(rating - Pearson.meanRatings[userID]);
					}
				}
			}
		}
		
		public void execute() {
			calculateSimilarities();
			System.out.println("Calculated similarities after "
					+ (System.currentTimeMillis()-Pearson.startTime)/1000
					+ " s");
			calculatePredictions();
			System.out.println("Calculated predictions after "
					+ (System.currentTimeMillis()-Pearson.startTime)/1000
					+ " s");
			
			try {
				PrintWriter output = new PrintWriter("ratingPredictions2.txt", "UTF-8");
				for (int i = 1; i <= Pearson.NUMBER_OF_USERS; i++) {
					for (int j = 1; j <= Pearson.NUMBER_OF_FILMS; j++) {
						if (predictions[i][j] != 0) {
							output.println(i + "," + j + "," + predictions[i][j]);
						}
					}
				}
				output.println(
						"Executed in: "
						+ (System.currentTimeMillis()-Pearson.startTime)/1000
						+ " s");
				output.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static class su {
		
		public su(int inputUser) {
			this.user = inputUser;
		}
		
		private int user;
		
		private float[] similarities = new float[Pearson.NUMBER_OF_USERS+1];

		public float similarity(int y) {
			float[] simDenominator = new float[2];
			for (int i = 1; i <= Pearson.NUMBER_OF_FILMS; i++) {
				if (Pearson.filmSets[i][y] != 0 && Pearson.filmSets[i][user] != 0) {
					float xx =  Pearson.filmSets[i][user] - Pearson.meanRatings[user];
					float yy =  Pearson.filmSets[i][y] - Pearson.meanRatings[y];
					similarities[y] +=  xx*yy;
					simDenominator[0] += Math.pow(xx,2);
					simDenominator[1] += Math.pow(yy,2);
				}
			}
			similarities[y] /= Math.sqrt(simDenominator[0])*Math.sqrt(simDenominator[1]);
			return similarities[y]; 
		}
		
		public void execute() {
			float prediction = Pearson.meanRatings[user];
			
			try {
				PrintWriter output = new PrintWriter("ratingPredictionsForUser" + user + ".txt", "UTF-8");
				for (int i = 1; i <= Pearson.NUMBER_OF_FILMS; i++) {
					if (Pearson.filmSets[i][user] == 0) {
						for (int j = 1; j <= Pearson.NUMBER_OF_USERS; j++) {
							if (Pearson.filmSets[i][j] != 0) {
								if (similarities[j] != 0) {
									prediction += similarities[j]
											*(Pearson.filmSets[i][j] - Pearson.meanRatings[j]);
								}
								else {
									prediction += similarity(j)
											*(Pearson.filmSets[i][j] - Pearson.meanRatings[j]);
								}
							}
						}
						output.println(i + "," + prediction);
						prediction = Pearson.meanRatings[user];
					}
				}
				output.println(
						"Executed in: "
						+ (System.currentTimeMillis()-Pearson.startTime)/1000
						+ " s");
				output.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
}