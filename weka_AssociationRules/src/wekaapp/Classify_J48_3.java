package wekaapp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.HashMap;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.CSVLoader;

public class Classify_J48_3 {
	public static void main(String[] args) {
		String unlabeledPath = "data/pima_unlabeled.csv"; // to be predicted
		String labeledPath = "data/pima_labeled.csv"; // prediction result
		String modelPath = "model/pima_j48.model";
		predict(modelPath, unlabeledPath, labeledPath);
	}

	public static void predict(String modelPath, String unlabeledPath, String labeledPath) {
		try {
			// load model
			Classifier model = (Classifier) SerializationHelper.read(new FileInputStream(modelPath));

			// load unlabeled data
			CSVLoader loader = new CSVLoader();
			loader.setSource(new File(unlabeledPath));
			Instances unlabeled = loader.getDataSet();

			// set class attribute
			unlabeled.setClassIndex(unlabeled.numAttributes() - 1);

			// create copy
			Instances labeled = new Instances(unlabeled);
			labeled.setClassIndex(labeled.numAttributes() - 1);

			HashMap<Integer, String> classMap = new HashMap<Integer, String>();
			classMap.put(0, "Yes");
			classMap.put(1, "No");

			Evaluation eval = new Evaluation(unlabeled);

			// label instances
			for (int i = 0; i < unlabeled.numInstances(); i++) {
				double cls = eval.evaluateModelOnce(model, unlabeled.instance(i));
				// double cls = model.classifyInstance(unlabeled.instance(i));
				String clsLabel = classMap.get((int) cls);
				labeled.instance(i).setClassValue(clsLabel);
				System.out.printf("%3d. %s%s", i + 1, clsLabel, "\n");
			}

			// save labeled data
			BufferedWriter writer = new BufferedWriter(new FileWriter(labeledPath));
			writer.write(labeled.toString());
			writer.newLine();
			writer.flush();
			writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
