package wekaapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Car implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7009407528529162482L;
	// metric
	String metric_str;
	double metric;
	// premise
	ArrayList<String> premise_attrs;
	ArrayList<String> premise_values;
	int premise_support;
	// conclusion
	ArrayList<String> conclusion_attrs;
	ArrayList<String> conclusion_values;
	int conclusion_support;

	public Car() {
		this.metric_str = new String("");
		this.metric = 0.0d;
		this.premise_attrs = new ArrayList<String>();
		this.premise_values = new ArrayList<String>();
		this.premise_support = 0;
		this.conclusion_attrs = new ArrayList<String>();
		this.conclusion_values = new ArrayList<String>();
		this.conclusion_support = 0;
	}
	
	public String getKey() {	
		int n = premise_attrs.size();
		String[] ps = new String[n];
		for(int i = 0; i < n; i++) {
			StringBuffer sb = new StringBuffer();
			sb.append(premise_attrs.get(i));
			sb.append("=");
			sb.append(premise_values.get(i));
			ps[i] = sb.toString().trim();
		}
		Arrays.sort(ps);
		return Arrays.toString(ps).trim();
	}

	public void setMetricStr(String metric_str) {
		this.metric_str = metric_str;
	}

	public void setMetric(double metric) {
		this.metric = metric;
	}

	public void setPremiseAttrs(ArrayList<String> attrs) {
		for (String attr : attrs)
			this.premise_attrs.add(attr);
	}

	public void setPremiseValues(ArrayList<String> values) {
		for (String value : values)
			this.premise_values.add(value);
	}

	public void setPremiseSupport(int support) {
		this.premise_support = support;
	}

	public void setConclusionAttrs(ArrayList<String> attrs) {
		for (String attr : attrs)
			this.conclusion_attrs.add(attr);
	}

	public void setConclusionValues(ArrayList<String> values) {
		for (String value : values)
			this.conclusion_values.add(value);
	}

	public void setConclusionSupport(int support) {
		this.conclusion_support = support;
	}

	public String getMetricStr() {
		return this.metric_str;
	}

	public double getMetric() {
		return this.metric;
	}

	public ArrayList<String> getPremiseAttrs() {
		return this.premise_attrs;
	}

	public ArrayList<String> getPremiseValues() {
		return this.premise_values;
	}

	public int getPremiseSupport() {
		return this.premise_support;
	}

	public ArrayList<String> getConclusionAttrs() {
		return this.conclusion_attrs;
	}

	public ArrayList<String> getConclusionValues() {
		return this.conclusion_values;
	}

	public int getConclusionSupport() {
		return this.conclusion_support;
	}

	public String toString() {
		StringBuffer text = new StringBuffer();

		// premise
		for (int i = 0; i < premise_attrs.size(); i++) {
			text.append(this.premise_attrs.get(i));
			text.append("=");
			text.append(this.premise_values.get(i));
			text.append(" ");
		}
		text.append(this.premise_support);
		text.append(" ==> ");
		// conclusion
		for (int i = 0; i < conclusion_attrs.size(); i++) {
			text.append(this.conclusion_attrs.get(i));
			text.append("=");
			text.append(this.conclusion_values.get(i));
			text.append(" ");
		}
		text.append(this.conclusion_support);
		text.append("  <" + this.metric_str + "=" + String.format("%.4f", this.metric) + ">");

		return text.toString();
	}
	
	public String toPremiseString() {
		StringBuffer text = new StringBuffer();
		// premise
		for (int i = 0; i < premise_attrs.size(); i++) {
			text.append(this.premise_attrs.get(i));
			text.append("=");
			text.append(this.premise_values.get(i));
			text.append(" ");
		}
		return text.toString();
	}
	
	public String toConclusionString() {
		StringBuffer text = new StringBuffer();
		// conclusion
		for (int i = 0; i < conclusion_attrs.size(); i++) {
			text.append(this.conclusion_attrs.get(i));
			text.append("=");
			text.append(this.conclusion_values.get(i));
			text.append(" ");
		}
		return text.toString();
	}
}