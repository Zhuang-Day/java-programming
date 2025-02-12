/**
 * @description Steps to mine association rules using the Apriori Algorithm
 * @description With user defined options
 * @see Weka Java API documentation at https://weka.sourceforge.io/doc.stable-3-8/
 * @author Ching-Sheng Hsu
 * @since 2023-02-22
 * @version 1.0.0
 */

import java.io.File;
import java.util.List;

import weka.associations.Apriori;
import weka.associations.AssociationRule;
import weka.associations.AssociationRules;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.Tag;
import weka.core.converters.ArffLoader;

public class AssociationRules_Apriori_2 {

	public static void main(String[] args) {
		AssociationRules_Apriori_2 ara = new AssociationRules_Apriori_2();
		ara.runAssociator();
	}
	
	public void runAssociator() {
		try {
			// Step 1: load the data set (need the categorical data)
			// ARFF format
			// available data set: "data/vote.arff", "data/weather.nominal.arff"
			ArffLoader loader = new ArffLoader();
			loader.setSource(new File("data/weather.nominal.arff"));
			Instances instances = loader.getDataSet();
            
         	// Step 2: get the associator 
			Apriori associator = new Apriori();
			
			// Step 3: set the options
			associator.setCar(false);
			associator.setClassIndex(-1);
			associator.setDelta(0.05);
			associator.setDoNotCheckCapabilities(false);
			associator.setLowerBoundMinSupport(0.1);
			
			// the metric type by which to rank rules: 
			// 0=Confidence | 1=Lift | 2=Leverage | 3=Conviction
			for(Tag t: Apriori.TAGS_SELECTION) {
				System.out.println(t.getIDStr()+", "+t.getReadable());
			}
			//associator.setMetricType(new SelectedTag(0, Apriori.TAGS_SELECTION));
			associator.setMetricType(new SelectedTag("Confidence", Apriori.TAGS_SELECTION));
			
			associator.setMinMetric(0.9);
			associator.setNumRules(10);
			associator.setSignificanceLevel(-1.0);
			associator.setTreatZeroAsMissing(false);
			associator.setUpperBoundMinSupport(1.0);
			associator.setVerbose(false);
			
			// show the options
			System.out.println("Options: ");
			String[] options = associator.getOptions();
			for(int i = 0; i < options.length; i += 2) {
				System.out.println(options[i] + '=' + options[i+1]);
			}
			
			// Step 4: run the Apriori algorithm
			associator.buildAssociations(instances);
			
			// Step 5: list the mined association rules
			System.out.println("\n--- Mined Association Rules ---\n");
			AssociationRules ars = associator.getAssociationRules();
			List<AssociationRule> rules = ars.getRules();
			for(AssociationRule rule : rules) {
				System.out.println(rule.toString());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
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
