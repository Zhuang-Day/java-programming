
/**
 * @description Steps to mine association rules using the Apriori Algorithm
 * @description Sample code for Association Rule I/O
 * @see Weka Java API documentation at https://weka.sourceforge.io/doc.stable-3-8/
 * @author Ching-Sheng Hsu
 * @since 2023-03-01
 * @version 1.0.0
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import weka.associations.Apriori;
import weka.associations.AssociationRule;
import weka.associations.AssociationRules;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.CSVLoader;


public class AssociationRules_Apriori_4 {

	public static void main(String[] args) {
		AssociationRules_Apriori_4 miner = new AssociationRules_Apriori_4();
		List<AssociationRule> rules = miner.runAssociator();
		miner.writeRules(rules, "rules_from_titanic.rules");
		List<AssociationRule> rules_from_file = miner.readRules("rules_from_titanic.rules");
		miner.showRules(rules_from_file);
	}
	
	public List<AssociationRule> runAssociator() {
		List<AssociationRule> rules = null;
		try {
			// Step 1: load the data set (need the categorical data)
			
            // CSV format
         	// available data set: "data/titanic.csv"
       		CSVLoader loader = new CSVLoader();
            loader.setSource(new File("data/titanic.csv"));
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
			
			// Step 5: output the mined association rules to a file
			AssociationRules ars = associator.getAssociationRules();
			rules = ars.getRules();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rules;
	}
	
	public void writeRules(List<AssociationRule> rules, String path) {
		try {
			FileOutputStream fos;
			fos = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(rules);
			oos.close();
			fos.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<AssociationRule> readRules(String path) {
		List<AssociationRule> rules = null;
		try {
			FileInputStream fis;
			fis = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fis);
			@SuppressWarnings("unchecked")
			List<AssociationRule> list = (List<AssociationRule>) ois.readObject();
			rules = list;
			ois.close();
			fis.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return rules;
	}
	
	public void showRules(List<AssociationRule> rules) {
		// list the mined association rules
		System.out.println("\n--- Mined Association Rules ---\n");
		for(AssociationRule rule : rules) {
			System.out.println(rule.toString());
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