/**
 * @description Steps to mine class association rules (CARs) using the Apriori Algorithm
 * @description With user defined options
 * @description Sample code for CARs I/O
 * @see Weka Java API documentation at https://weka.sourceforge.io/doc.stable-3-8/
 * @see https://github.com/bnjmn/weka/blob/master/weka/src/main/java/weka/associations/ItemSet.java
 * @author Ching-Sheng Hsu
 * @since 2023-03-15
 * @version 1.0.0
 */

package wekaapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import weka.associations.Apriori;
import weka.associations.ItemSet;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.CSVLoader;

public class AssociationRules_Apriori_Car_3 {

	public static void main(String[] args) {
		AssociationRules_Apriori_Car_3 miner = new AssociationRules_Apriori_Car_3();
		
		Apriori associator = miner.createAssociator();
		System.out.println("Options: " + Arrays.toString(associator.getOptions()));
		ArrayList<Object>[] cars = miner.runAssociator(associator);
		miner.writeCARs(associator, cars, "cars_from_titanic.cars");
		
		//ArrayList<CAR> cars_from_file = miner.readCARs("cars_from_titanic.cars");
		//miner.showCARs(cars_from_file);
	}
	
	public Apriori createAssociator() {
		Apriori associator = new Apriori();
		setAprioriOptions(associator);
		return associator;
	}
	
	public ArrayList<Object>[] runAssociator(Apriori associator){
		ArrayList<Object>[] cars = null;
		try {
			// Step 1: load the data set (need the categorical data)
			CSVLoader loader = new CSVLoader();
			loader.setSource(new File("data/titanic.csv"));
			Instances instances = loader.getDataSet();
			// Step 2: run the Apriori algorithm to mine CARs
			cars = associator.mineCARs(instances);
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cars;
	}
	
	private void setAprioriOptions(Apriori associator) {
		associator.setCar(true);
		associator.setClassIndex(4);  // from 1 to n, and -1 represents the last attribute
		associator.setDelta(0.05);
		associator.setDoNotCheckCapabilities(false);
		associator.setLowerBoundMinSupport(0.1);
		// the metric type by which to rank rules: 
		// 0=Confidence | 1=Lift | 2=Leverage | 3=Conviction
		//associator.setMetricType(new SelectedTag(0, Apriori.TAGS_SELECTION));
		associator.setMetricType(new SelectedTag("Confidence", Apriori.TAGS_SELECTION));
		associator.setMinMetric(0.7);
		associator.setNumRules(20);
		associator.setSignificanceLevel(-1.0);
		associator.setTreatZeroAsMissing(false);
		associator.setUpperBoundMinSupport(1.0);
		associator.setVerbose(false);		
	}
	
	public void writeCARs(Apriori associator, ArrayList<Object>[] cars, String path) {
		ArrayList<Car> mycars = new ArrayList<Car>();
		if(cars.length != 0) {			
			ArrayList<Object> list_premise = (ArrayList<Object>)cars[0];
			ArrayList<Object> list_conclusion = (ArrayList<Object>)cars[1];
			ArrayList<Object> list_metric = (ArrayList<Object>)cars[2];
			
			String metricStr = associator.metricString();
			int n = list_premise.size();  // number of the mined rules
			
			for(int i = 0; i < n; i++) {
				// get the premise, conclusion, and metric of a CAR
				ItemSet premise = (ItemSet)list_premise.get(i);
				ItemSet conclusion = (ItemSet)list_conclusion.get(i);
				double metric = (double)list_metric.get(i);
				
				Car mycar = new Car();
				
				// metric
				mycar.setMetricStr(metricStr);
				mycar.setMetric(metric);
				
				// premise
				int[] items_p = premise.getItems();
				ArrayList<String> premise_keys = new ArrayList<String>();
				ArrayList<String> premise_values = new ArrayList<String>();
				for (int j = 0; j < associator.getInstancesNoClass().numAttributes(); j++) {
					if (items_p[j] != -1) {
						premise_keys.add(associator.getInstancesNoClass().attribute(j).name());
						premise_values.add(associator.getInstancesNoClass().attribute(j).value(items_p[j]));
					}
				}
				mycar.setPremiseAttrs(premise_keys);
				mycar.setPremiseValues(premise_values);
				mycar.setPremiseSupport(premise.counter());
				
				// conclusion
				int[] items_c = conclusion.getItems();
				ArrayList<String> conclusion_keys = new ArrayList<String>();
				ArrayList<String> conclusion_values = new ArrayList<String>();
				for (int j = 0; j < associator.getInstancesOnlyClass().numAttributes(); j++) {
					if (items_c[j] != -1) {
						conclusion_keys.add(associator.getInstancesOnlyClass().attribute(j).name());
						conclusion_values.add(associator.getInstancesOnlyClass().attribute(j).value(items_c[j]));
					}
				}
				mycar.setConclusionAttrs(conclusion_keys);
				mycar.setConclusionValues(conclusion_values);
				mycar.setConclusionSupport(conclusion.counter());
				
				mycars.add(mycar);
			}
		}
		
		writeCARs(mycars, path);
	}
	
	public void writeCARs(ArrayList<Car> cars, String path) {
		try {
			FileOutputStream fos;
			fos = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(cars);
			oos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Car> readCARs(String path) {
		ArrayList<Car> cars = null;
		try {
			FileInputStream fis;
			fis = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fis);
			@SuppressWarnings("unchecked")
			ArrayList<Car> lists = (ArrayList<Car>) ois.readObject();
			cars = lists;
			ois.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return cars;
	}
	
	public void showCARs(ArrayList<Car> cars) {
		System.out.println("\n--- Mined Class Association Rules ---\n");
		int counter = 0;
		for(Car car : cars) {
			System.out.println("[" + (++counter) + "] " + car.toString());
		}
	}
	
	
	/*
	 * OPTIONS
	 * 
	 * minMetric -- Minimum metric score. Consider only rules with scores
	 * higher than this value.
	 * 
	 * verbose -- If enabled the algorithm will be run in verbose mode.
	 * 
	 * numRules -- Number of rules to find.
	 * 
	 * lowerBoundMinSupport -- Lower bound for minimum support.
	 * 
	 * classIndex -- Index of the class attribute. If set to -1, the last attribute
	 * is taken as class attribute.
	 * 
	 * outputItemSets -- If enabled the itemsets are output as well.
	 * 
	 * car -- If enabled class association rules are mined instead of (general)
	 * association rules.
	 * 
	 * doNotCheckCapabilities -- If set, associator capabilities are not checked
	 * before associator is built (Use with caution to reduce runtime).
	 * 
	 * removeAllMissingCols -- Remove columns with all missing values.
	 * 
	 * significanceLevel -- Significance level. Significance test (confidence metric
	 * only).
	 * 
	 * treatZeroAsMissing -- If enabled, zero (that is, the first value of a
	 * nominal) is treated in the same way as a missing value.
	 * 
	 * delta -- Iteratively decrease support by this factor. Reduces support until
	 * min support is reached or required number of rules has been generated.
	 * 
	 * metricType -- Set the type of metric by which to rank rules. Confidence is
	 * the proportion of the examples covered by the premise that are also covered
	 * by the consequence (Class association rules can only be mined using
	 * confidence). Lift is confidence divided by the proportion of all examples
	 * that are covered by the consequence. This is a measure of the importance of
	 * the association that is independent of support. Leverage is the proportion of
	 * additional examples covered by both the premise and consequence above those
	 * expected if the premise and consequence were independent of each other. The
	 * total number of examples that this represents is presented in brackets
	 * following the leverage. Conviction is another measure of departure from
	 * independence. Conviction is given by P(premise)P(!consequence) / P(premise,
	 * !consequence).
	 * 
	 * upperBoundMinSupport -- Upper bound for minimum support. Start iteratively
	 * decreasing minimum support from this value.
	 */

}