package test;

import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;
import weka.clusterers.*;

public class test {	
	public static void main(String[] args) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("./ml-100k/u.data"));
			Instances data = new Instances(reader);
			reader.close();
			
			String[] options = new String[2];
			options[0] = "-I"; // maxIterations
			options[1] = "100";
			EM clusterer = new EM();
			clusterer.setOptions(options);
			clusterer.buildClusterer(data);
			
			ClusterEvaluation eval = new ClusterEvaluation();
			eval.setClusterer(clusterer);
			eval.evaluateClusterer(data);
			System.out.println("# of clusters: " + eval.getNumClusters());
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		}
	}
}