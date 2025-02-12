package wekaapp;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CarMap extends HashMap<String, Car> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CarMap() {
		super();
	}
	
	public String toKey(List<String> attrs, List<String> values) {
		int n = attrs.size();
		String[] ps = new String[n];
		for(int i = 0; i < n; i++) {
			StringBuffer sb = new StringBuffer();
			sb.append(attrs.get(i));
			sb.append("=");
			sb.append(values.get(i));
			ps[i] = sb.toString().trim();
		}
		Arrays.sort(ps);
		return Arrays.toString(ps).trim();
	}

}
