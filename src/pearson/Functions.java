package pearson;

public class Functions {
	
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
	
	public static void calculateSimilarities() {
		for (int i = 0; i < Pearson.size; i++) {
			int userID = (int)Pearson.data.get(i).value(Pearson.UserID);
			int filmID = (int)Pearson.data.get(i).value(Pearson.ItemID);
			float rating = (float)Pearson.data.get(i).value(Pearson.Rating);
			
			for (int j = 1; j <= Pearson.NUMBER_OF_USERS; j++) {
				if (Pearson.filmSets[filmID][j] != 0) {
					if (userID >= j) {
						Pearson.similarities[userID][j] +=
								(rating - Pearson.meanRatings[userID])
								*(Pearson.filmSets[filmID][j] - Pearson.meanRatings[j]);
					}
					else {
						Pearson.similarities[j][userID] +=
								(rating - Pearson.meanRatings[userID])
								*(Pearson.filmSets[filmID][j] - Pearson.meanRatings[j]);
					}
					Pearson.simDenominator[userID][j] +=
							Math.pow(rating - Pearson.meanRatings[userID], 2);
					Pearson.simDenominator[j][userID] +=
							Math.pow(rating - Pearson.meanRatings[j], 2);
				}
			}
		}
		
		for (int i = 0; i < Pearson.NUMBER_OF_USERS; i++) {
			for (int j = 0; j <= i; j++) {
				Pearson.similarities[i][j] /= (Math.sqrt(Pearson.simDenominator[i][j])
						*Math.sqrt(Pearson.simDenominator[j][i]));
			}
		}
	}
	
	public static void calculatePredictions() {
		for (int i = 0; i < Pearson.size; i++) {
			int userID = (int)Pearson.data.get(i).value(Pearson.UserID);
			int filmID = (int)Pearson.data.get(i).value(Pearson.ItemID);
			float rating = (float)Pearson.data.get(i).value(Pearson.Rating);
			
			for (int j = userID; j <= Pearson.NUMBER_OF_USERS; j++) {
				if (Pearson.predictions[j][filmID] == 0) {
					Pearson.predictions[j][filmID] = Pearson.meanRatings[j];
				}
				if (Pearson.filmSets[filmID][j] == 0) {
					Pearson.predictions[j][filmID] += Pearson.similarities[j][userID]
							*(rating - Pearson.meanRatings[userID]);
				}
			}
		}
	}
}