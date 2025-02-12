package wekaapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.CSVLoader;

public class Classify_J48_1 {
	public static void main(String[] args) {
		String trainingDataPath = "data/pima_train.csv";
		String modelPath = "model/pima_j48.model";
		buildModel(new J48(), trainingDataPath, modelPath);
	}

	// build a J48 classification model
	public static void buildModel(Classifier algorithm, String trainingDataPath, String modelPath) {
		try {
			// training data set
			CSVLoader loader = new CSVLoader();
			loader.setSource(new File(trainingDataPath));
			Instances train = loader.getDataSet();

			train.setClassIndex(train.numAttributes() - 1); // target attribute: the last one

			// build model
			algorithm.buildClassifier(train);

			// evaluation: cross-validation
			Evaluation eval = new Evaluation(train);
			eval.crossValidateModel(algorithm, train, 10, new Random(1));

			System.out.println(eval.toSummaryString("Cross-Validation Results\n----------------------\n", true));
			System.out.println("F-Measure = " + eval.fMeasure(1) + "\nPrecision = " + eval.precision(1) + "\nRecall = "
					+ eval.recall(1));

			// save model
			SerializationHelper.write(modelPath, algorithm);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
