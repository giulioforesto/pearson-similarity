package test;

import weka.core.DistanceFunction;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

import weka.clusterers.*;

public class test {
	
	public static void main(String[] args) {
//		try {
//			BufferedReader reader = new BufferedReader(new FileReader("DataALL.arff"));
//			Instances data = new Instances(reader);
//			reader.close();
//			
//			String[] options = new String[2];
//			options[0] = "-I"; // maxIterations
//			options[1] = "100";
//			EM clusterer = new EM();
//			clusterer.setOptions(options);
//			clusterer.buildClusterer(data);
//			
////			DistanceFunction df = new DistanceFunction();
//			
//			SimpleKMeans SKMClusterer = new SimpleKMeans();
////			SKMClusterer.setDistanceFunction(df);
//			
//			ClusterEvaluation eval = new ClusterEvaluation();
//			eval.setClusterer(clusterer);
//			eval.evaluateClusterer(data);
//			System.out.println("# of clusters: " + eval.getNumClusters());
//		} catch (Exception e1) {
//			System.out.println(e1.getMessage());
//		}
		
		String FILE = "DataALL10M.arff";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(FILE));
			String line;
			int NUMBER_OF_FILMS = 65133;
			int NUMBER_OF_USERS = 71567;
			int users[] = new int[NUMBER_OF_USERS+1];
			
			
			while ((line = reader.readLine()) != null) {
				if (line.matches("[0-9]+.*")) {
					String[] split = line.split(",");		
					users[Integer.parseInt(split[0])] = 1;
				}
			}
			reader.close();
			int numOfHoles = 0;
			int numOfUsers = 0;
			for (int i = 1 ; i <= NUMBER_OF_USERS; i++) {
				if (users[i] == 0) {
					numOfHoles++;
					System.out.println("trou à " + i);
				}
				else if (users[i] == 1) {
					numOfUsers++;
				}
			}
			System.out.println(numOfHoles);
			System.out.println(numOfUsers);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
