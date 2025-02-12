package Package3;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class attend {
	static String[][] attendStudent;

	public static String[][] attend() {
		// 移除缺席者，出席名單重整
		// 迴圈外層為全班學生名(已經照小組排好了)
		// i=第幾組
		// j=他在組中的第幾人
		if (attendStudent == null) {
//			System.out.println("attendStudent == null");//確認當有跑過一次後，是否還會重複跑進迴圈
			attendStudent = new String[main.groupsnum][];
			for (int i = 0; i < attendStudent.length; i++) {
				ArrayList<String> List = new ArrayList();// 動態陣列
				int num=button_outputstudent.ButtonName[i].length;//計算每一組現在的人數
				for (int j = 0; j < button_outputstudent.ButtonName[i].length; j++) {
					// 將學生名單一個個輸進去目前出席的學生
					// attendStudent為之後考試時呼叫的出席陣列
					List.add(button_outputstudent.ButtonName[i][j]);
					if (button_outputstudent.absence != null) {
						for (int k = 0; k < button_outputstudent.absence.length; k++) {
							if(button_outputstudent.ButtonName[i][j].equals(button_outputstudent.absence[k])) {
								List.remove(button_outputstudent.absence[k]);
								num--;//計算小組目前人數
							}
						}
					}
				}
				attendStudent[i] = new String[num];
				// 動態陣列轉換成一般陣列
				Object list[] = List.toArray();
				attendStudent[i] = Arrays.copyOf(list, list.length, String[].class);
			}
		}
		return attendStudent;
	}


}
