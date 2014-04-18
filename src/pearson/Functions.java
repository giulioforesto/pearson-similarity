package pearson;

import java.io.PrintWriter;

public class Functions {
	
	/*
	 * DATA		NUMBER_OF_USERS	NUMBER_OF_FILMS
	 * 100k		943				1682
	 * 1M		6040			3952
	 * 10M		69878			10677
	 * 
	 * NUMBER_OF_FILMS for volume = 10M in README.html is correct but film numbers range from 1 to
	 * 65133 with holes. Converter rename them from 1 to 10677.
	 * NUMBER_OF_USERS for volume = 10M in README.html is incorrect: user number range from 1 to
	 * 71567 with holes. Converter rename them from 1 to 69878.
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
				return 69878;
			}
			else if (type == "films") {
				return 10677;
			}
		}
		return 0;
	}
	
	public static class all {
		private float[][] filmSets = new float[Pearson.NUMBER_OF_FILMS+1][Pearson.NUMBER_OF_USERS+1];
		private float[][] similarities = new float[Pearson.NUMBER_OF_USERS+1][Pearson.NUMBER_OF_USERS+1];
		private float[][] simDenominator = new float[Pearson.NUMBER_OF_USERS+1][Pearson.NUMBER_OF_USERS+1];
		private float[][] predictions = new float[Pearson.NUMBER_OF_USERS+1][Pearson.NUMBER_OF_FILMS+1];
		
		void calculateMeanRatings() {
			for (int i = 0; i < Pearson.size; i++) {
				int userID = (int)Pearson.data.get(i).value(Pearson.UserID);
				int filmID = (int)Pearson.data.get(i).value(Pearson.ItemID);
				float rating = (float)Pearson.data.get(i).value(Pearson.Rating);
				
				Pearson.meanRatings[userID] += rating;
				Pearson.cardinals[userID]++; 
				
				filmSets[filmID][userID] = rating;
			}
			
			for (int i = 1; i <= Pearson.NUMBER_OF_USERS; i++) {
				Pearson.meanRatings[i] /= Pearson.cardinals[i];
			}
		}
		
		void calculateSimilarities() {
			for (int i = 0; i < Pearson.size; i++) {
				int userID = (int)Pearson.data.get(i).value(Pearson.UserID);
				int filmID = (int)Pearson.data.get(i).value(Pearson.ItemID);
				float rating = (float)Pearson.data.get(i).value(Pearson.Rating);
				
				for (int j = 1; j < userID; j++) {
					if (filmSets[filmID][j] != 0) {
						similarities[userID][j] +=
								(rating - Pearson.meanRatings[userID])
								*(filmSets[filmID][j] - Pearson.meanRatings[j]);
						simDenominator[userID][j] +=
								Math.pow(rating - Pearson.meanRatings[userID], 2);
						simDenominator[j][userID] +=
								Math.pow(filmSets[filmID][j]
										- Pearson.meanRatings[j], 2);
					}
				}
				for (int j = userID; j <= Pearson.NUMBER_OF_USERS; j++) {
					if (filmSets[filmID][j] != 0) {
						similarities[j][userID] +=
								(rating - Pearson.meanRatings[userID])
								*(filmSets[filmID][j] - Pearson.meanRatings[j]);
						simDenominator[userID][j] +=
								Math.pow(rating - Pearson.meanRatings[userID], 2);
						simDenominator[j][userID] +=
								Math.pow(filmSets[filmID][j]
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
					if (filmSets[filmID][j] == 0) {
						predictions[j][filmID] += similarities[userID][j]
								*(rating - Pearson.meanRatings[userID]);
					}
				}
				for (int j = userID; j <= Pearson.NUMBER_OF_USERS; j++) {
					if (filmSets[filmID][j] == 0) {
						predictions[j][filmID] += similarities[j][userID]
								*(rating - Pearson.meanRatings[userID]);
					}
				}
			}
		}
		
		public void execute() {
			calculateMeanRatings();
			System.out.println("Calculated mean ratings after "
					+ (System.currentTimeMillis()-Pearson.startTime)/1000
					+ " s");
			
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
		
		private float[] userRatings = new float[Pearson.NUMBER_OF_FILMS+1];
		private float[] similarities = new float[Pearson.NUMBER_OF_USERS+1];
		private float[] simDenominatorX = new float[Pearson.NUMBER_OF_USERS+1];
		private float[] simDenominatorY = new float[Pearson.NUMBER_OF_USERS+1];

		void calculateMeanRatings() {
			for (int i = 0; i < Pearson.size; i++) {
				int userID = (int)Pearson.data.get(i).value(Pearson.UserID);
				int filmID = (int)Pearson.data.get(i).value(Pearson.ItemID);
				float rating = (float)Pearson.data.get(i).value(Pearson.Rating);
				
				Pearson.meanRatings[userID] += rating;
				Pearson.cardinals[userID]++; 
				
				if (userID == Pearson.USER) {
					userRatings[filmID] = rating;
				}
			}
			
			for (int i = 1; i <= Pearson.NUMBER_OF_USERS; i++) {
				Pearson.meanRatings[i] /= Pearson.cardinals[i];
			}
		}
		
		void calculateSimilarities() {
			for (int i = 0; i < Pearson.size; i++) {
				int userID = (int)Pearson.data.get(i).value(Pearson.UserID);
				int filmID = (int)Pearson.data.get(i).value(Pearson.ItemID);
				float rating = (float)Pearson.data.get(i).value(Pearson.Rating);
				
				if (userRatings[filmID] != 0) {
					float xx =  userRatings[filmID] - Pearson.meanRatings[user];
					float yy =  rating - Pearson.meanRatings[userID];
					similarities[userID] +=  xx*yy;
					simDenominatorX[userID] += Math.pow(xx,2);
					simDenominatorY[userID] += Math.pow(yy,2);
				}
			}
			for (int i = 1; i <= Pearson.NUMBER_OF_USERS; i++) {
				similarities[i] /= Math.sqrt(simDenominatorX[i])*Math.sqrt(simDenominatorY[i]);
			}
		}
		
		public void execute() {
			
			calculateMeanRatings();
			System.out.println("Calculated mean ratings after "
					+ (System.currentTimeMillis()-Pearson.startTime)/1000
					+ " s");
			
			calculateSimilarities();
			System.out.println("Calculated similarities after "
					+ (System.currentTimeMillis()-Pearson.startTime)/1000
					+ " s");
			
			float prediction = Pearson.meanRatings[user];
			
			try {
				PrintWriter output = new PrintWriter("ratingPredictionsForUser"
						+ user + "at" + Pearson.VOLUME + ".txt", "UTF-8");
				
				System.out.println("Calculating and printing predictions...");
				int progress = 0;
				
				for (int i = 1; i <= Pearson.NUMBER_OF_FILMS; i++) {
					if (userRatings[i] == 0) {
						int realProgress = i*100/Pearson.NUMBER_OF_FILMS;
						if (realProgress > progress) {
							progress = realProgress;
							System.out.println("Calculating and printing predictions... "
									+ progress + "%");
						}
						for (int j = 0; j < Pearson.size; j++) {
							int filmID = (int)Pearson.data.get(j).value(Pearson.ItemID);
							if (filmID == i) {
								int userID = (int)Pearson.data.get(j).value(Pearson.UserID);
								float rating = (float)Pearson.data.get(j).value(Pearson.Rating);
								
								prediction += similarities[userID]
											*(rating - Pearson.meanRatings[userID]);
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