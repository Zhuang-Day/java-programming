package wekaapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.CSVLoader;

public class Classify_J48_2 {
	public static void main(String[] args) {
		String testingDataPath = "data/pima_test.csv";
		String modelPath = "model/pima_j48.model";
		testModel(modelPath, testingDataPath);
	}

	// test the mined J48 model
	public static void testModel(String modelPath, String testingDataPath) {
		try {
			// load model
			Classifier model = (Classifier) SerializationHelper.read(new FileInputStream(modelPath));

			// load the testing data set
			CSVLoader loader = new CSVLoader();
			loader.setSource(new File(testingDataPath));
			Instances test = loader.getDataSet();
			test.setClassIndex(test.numAttributes() - 1); // target attribute: the last one
			
			Evaluation eval = new Evaluation(test);
			eval.evaluateModel(model, test);

			System.out.println("-----Evaluation with Testing Data-----\n" + eval.toSummaryString());
			System.out.println("\n<instance#, actual, prediction>\n");

			int k = 0;
			for (Instance instance : test) {
				double actual = instance.classValue();
				String actual_class = instance.classAttribute().value((int)actual);
				double prediction = eval.evaluateModelOnce(model, instance);
				String prediction_class = instance.classAttribute().value((int)prediction);
				
				System.out.printf("%3d. %s %s", ++k, actual_class, prediction_class);
				System.out.println(prediction != actual ? " *" : "");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
