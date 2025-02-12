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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class AssociationRules_Apriori_Car_4 {

	public static void main(String[] args) {
		AssociationRules_Apriori_Car_4 miner = new AssociationRules_Apriori_Car_4();
		ArrayList<Car> cars_from_file = miner.readCARs("cars_from_titanic.cars");
		miner.showCARs(cars_from_file);
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