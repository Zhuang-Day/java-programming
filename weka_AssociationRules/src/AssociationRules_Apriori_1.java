/**
 * @description Steps to mine association rules using the Apriori Algorithm
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
import weka.core.converters.ArffLoader;

public class AssociationRules_Apriori_1 {

	public static void main(String[] args) {
		AssociationRules_Apriori_1 ara = new AssociationRules_Apriori_1();
		ara.runAssociator();
	}
	
	public void runAssociator() {
		try {
			// Step 1: load the data set (need the categorical data)
			
			// ARFF format
			// available data set: "data/vote.arff", "data/weather.nominal.arff"
			ArffLoader loader = new ArffLoader();
			loader.setSource(new File("data/vote.arff"));
			Instances instances = loader.getDataSet();		
			
         	// Step 2: get the associator 
			Apriori associator = new Apriori();
			
			// Step 3: show the options
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

}