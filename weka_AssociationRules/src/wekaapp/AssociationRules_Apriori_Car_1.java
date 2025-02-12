/**
 * @description Steps to mine class association rules (CARs) using the Apriori Algorithm
 * @description With user defined options
 * @see Weka Java API documentation at https://weka.sourceforge.io/doc.stable-3-8/
 * @author Ching-Sheng Hsu
 * @since 2023-03-15
 * @version 1.0.0
 */

package wekaapp;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import weka.associations.Apriori;
import weka.associations.ItemSet;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.CSVLoader;

public class AssociationRules_Apriori_Car_1 {

	public static void main(String[] args) {
		AssociationRules_Apriori_Car_1 miner = new AssociationRules_Apriori_Car_1();
		miner.runAssociator();
	}
	
	public void runAssociator() {
		try {
			// Step 1: load the data set (need the categorical data)
			CSVLoader loader = new CSVLoader();
			loader.setSource(new File("data/titanic.csv"));
			Instances instances = loader.getDataSet();
            
         	// Step 2: get the associator 
			Apriori associator = new Apriori();
			
			// Step 3: set the options
			setAprioriOptions(associator);
			
			// show the options
			System.out.println("Options: " + Arrays.toString(associator.getOptions()));
			
			// Step 4: run the Apriori algorithm to mine CARs
			ArrayList<Object>[] cars = associator.mineCARs(instances);
			
			// Step 5: list the mined association rules
			System.out.println("\n--- Mined Association Rules ---\n");
			if(cars.length != 0) {
				ArrayList<Object> list_premise = (ArrayList<Object>)cars[0];
				ArrayList<Object> list_conclusion = (ArrayList<Object>)cars[1];
				ArrayList<Object> list_metric = (ArrayList<Object>)cars[2];
				
				String metricStr = associator.metricString();
				int n = list_premise.size();  // number of the mined rules
				
				// list the mined CARs
				for(int i = 0; i < n; i++) {
					ItemSet premise = (ItemSet)list_premise.get(i);
					ItemSet conclusion = (ItemSet)list_conclusion.get(i);
					double metric = (double)list_metric.get(i);
					
					System.out.print("[" + (i+1) + "] ");
					System.out.print(premise.toString(associator.getInstancesNoClass()));
					System.out.print(" ==> ");
					System.out.print(conclusion.toString(associator.getInstancesOnlyClass()));
					System.out.println("  <" + metricStr + "=" + String.format("%.4f", metric) + ">");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
