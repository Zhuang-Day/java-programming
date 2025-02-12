/**
 * @description Steps to mine class association rules (CARs) using the Apriori Algorithm
 * @description With user defined options
 * @description Sample code for CARs I/O
 * @description Sample code for the application of CARs
 * @see Weka Java API documentation at https://weka.sourceforge.io/doc.stable-3-8/
 * @see https://github.com/bnjmn/weka/blob/master/weka/src/main/java/weka/associations/ItemSet.java
 * @author Ching-Sheng Hsu
 * @since 2023-03-15
 * @version 1.0.0
 */

package wekaapp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class AssociationRules_Apriori_Car_5 {

	public static void main(String[] args) {
		AssociationRules_Apriori_Car_5 miner = new AssociationRules_Apriori_Car_5();
		ArrayList<Car> cars_from_file = miner.readCARs("cars_from_titanic.cars");
		miner.showCARs(cars_from_file);
		
		// application of the mined CARs to recommender systems
		CarMap car_map = miner.toCarMap(cars_from_file);
		
		// attributes of a premise for searching
		ArrayList<String> p_attrs = new ArrayList<String>();
		p_attrs.add("Class");
		p_attrs.add("Sex");
		
		// values of a premise for searching
		ArrayList<String> p_values = new ArrayList<String>();
		p_values.add("Crew");
		p_values.add("Male");
		
		// search by the premise
		String key = car_map.toKey(p_attrs, p_values);
		System.out.println("\nKey: " + key);
		Car car = car_map.get(key);
		
		// searching result (conclusion)
		if(car != null) {
			System.out.println("Conclusion: " + car.toConclusionString());
			// do something for attr and value
			ArrayList<String> c_attrs = car.getConclusionAttrs();
			ArrayList<String> c_values = car.getConclusionValues();
			int n = c_attrs.size();
			for(int i = 0; i < n; i++) {
				String k = c_attrs.get(i);
				String v = c_values.get(i);
				// do something for attr and value
				//System.out.println(k + "=" + v);
			}
		}
		
	}
	
	public CarMap toCarMap(ArrayList<Car> cars){
		CarMap car_map = new CarMap();
		for(Car car : cars) {
			car_map.put(car.getKey(), car);
		}
		return car_map;
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
}