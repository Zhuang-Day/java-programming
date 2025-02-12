import java.util.HashMap;

public class AprioriOptions {	
	/**
	 * -N <required number of rules output>
	 *   The required number of rules. (default = 10)
	 *  
	 * -T <0=confidence | 1=lift | 2=leverage | 3=Conviction>
	 *   The metric type by which to rank rules. (default = confidence)
	 *  
	 * -C <minimum metric score of a rule>
	 *   The minimum confidence of a rule. (default = 0.9)
	 *  
	 * -D <delta for minimum support>
	 *   The delta by which the minimum support is decreased in
	 *   each iteration. (default = 0.05)
	 *  
	 * -U <upper bound for minimum support>
	 *   Upper bound for minimum support. (default = 1.0)
	 *  
	 * -M <lower bound for minimum support>
	 *   The lower bound for the minimum support. (default = 0.1)
	 *  
	 * -S <significance level>
	 *   If used, rules are tested for significance at
	 *   the given level. Slower. (default = no significance testing)
	 *  
	 * -I
	 *   If set the itemsets found are also output. (default = no)
	 *  
	 * -R
	 *   Remove columns that contain all missing values (default = no)
 	 * 
	 * -V
	 *   Report progress iteratively. (default = no)
	 *  
	 * -A
	 *   If set class association rules are mined. (default = no)
	 *  
	 * -Z
	 *   Treat zero (i.e. first value of nominal attributes) as missing
	 *  
	 * -B <toString delimiters>
	 *   If used, two characters to use as rule delimiters
	 *   in the result of toString: the first to delimit fields,
	 *   the second to delimit items within fields.
	 *   (default = traditional toString result)
	 *  
	 * -c <the class index>
	 *   The class index. (default = last)
	 */
	HashMap<String, String> optionMap = new HashMap<String, String>();
	
	public AprioriOptions() {
		this.optionMap.put("-N", "10");   // required number of rules output
		this.optionMap.put("-T", "0");    // the metric type by which to rank rules: 0=confidence | 1=lift | 2=leverage | 3=Conviction
		this.optionMap.put("-C", "0.9");  // minimum metric score of a rule
		this.optionMap.put("-D", "0.05"); // delta for minimum support
		this.optionMap.put("-U", "1.0");  // upper bound for minimum support
		this.optionMap.put("-M", "0.1");  // lower bound for minimum support
		this.optionMap.put("-S", "-1.0"); // significance level
		this.optionMap.put("-c", "-1");   // the class index
	}
	
	public String setOption(String key, String value) {
		return this.optionMap.replace(key, value);
	}
	
	public String getOption(String key) {
		return this.optionMap.get(key);
	};
	
	
	public String[] getOptions() {
		String [] options = {
				"-N", this.optionMap.get("-N"),  // required number of rules output
				"-T", this.optionMap.get("-T"),  // the metric type by which to rank rules: 0=confidence | 1=lift | 2=leverage | 3=Conviction
				"-C", this.optionMap.get("-C"),  // minimum metric score of a rule
				"-D", this.optionMap.get("-D"),  // delta for minimum support
				"-U", this.optionMap.get("-U"),  // upper bound for minimum support
				"-M", this.optionMap.get("-M"),  // lower bound for minimum support
				"-S", this.optionMap.get("-S"),  // significance level
				"-c", this.optionMap.get("-c")   // the class index
		};
		return options;
	}
}
