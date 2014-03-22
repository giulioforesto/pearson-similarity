package test;

import weka.core.DistanceFunction;
import weka.core.Instances;

import java.io.BufferedReader;
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
		
		HashMap<Integer,Double> a = new HashMap<Integer,Double>();
		Double b = a.get(2);
		if (b == null) {
			System.out.println(b);
		}
	}
}
