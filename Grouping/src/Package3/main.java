/*
 * 撰寫日期: 2020/08
 * 讀取 Excel 檔案 (112公告分組.xls)，將其中的學生資料（學號、姓名、組別）讀入後，
 * 依組別進行排序，然後將整理後的分組名單輸出到另一個 Excel 檔案 (112分組小考紀錄.xls)
 * 視窗呈現
 主要功能: 
 1.取 Excel 分組資料、排序
 2.輸出分組名單的 Java 程式，使用 JXL 函式庫 (jxl.Workbook、jxl.write.*) 來處理 Excel 讀寫
 3.氣泡排序法來整理學生分組順序
 
 * 
 * */

package Package3;

import java.util.InputMismatchException;
import java.io.IOException;
import java.io.File;
import jxl.Sheet;
import jxl.Workbook;
import jxl.Cell;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.biff.*;

public class main {
	// 路徑
	protected static String Path = "112公告分組.xls";
	protected static String PathExcel = "112分組小考紀錄.xls";

	private static String[] student_ID;// 學號
	public static String[] name;// 姓名
	private static int[] group;// 組別
	public static int groupsnum;// 組別數

	public static void main(String[] args) throws BiffException, IOException, RowsExceededException, WriteException {
		Input();
		sort();
		Excel();
	}

	public static void Input() throws BiffException, IOException, RowsExceededException, WriteException {
		int i, j;
		String s;
		Workbook WB = Workbook.getWorkbook(new File(Path));
		Sheet sheet = WB.getSheet(0);// 提取第一頁工作表

		student_ID = new String[sheet.getRows() - 1];// 學號
		name = new String[sheet.getRows() - 1];// 姓名
		group = new int[sheet.getRows() - 1];// 組別

		for (i = 0; i < sheet.getRows() - 1; i++) {
			for (j = 0; j < sheet.getColumns() - 2; j++) {
				Cell c = sheet.getCell(j, i + 1);
				s = c.getContents();// 將資料指定給字串s
				if (j == 0) {
					student_ID[i] = s;
				}
				if (j == 1) {
					name[i] = s;
				}
				if (j == 2) {
					int group_number = Integer.valueOf(s);// 將小組的組別轉為整數
					group[i] = group_number;// 再將組別的數字放到陣列裡，之後運用在氣泡排序法
				}
			}
		}

	}

	public static void sort() throws BiffException, IOException, RowsExceededException, WriteException {
		int i, j;
		String temp_student_ID;// 氣泡排序法中拿來暫存學號的;
		String temp_name;// 氣泡排序法中拿來暫存姓名的;
		int temp_group;
		for (i = 0; i < name.length; i++)// 氣泡排序法
		{
			for (j = 0; j < name.length - 1 - i; j++) {
				if (group[j] > group[j + 1]) {
					temp_group = group[j];
					group[j] = group[j + 1];// 組別排序
					group[j + 1] = temp_group;

					temp_student_ID = student_ID[j];
					student_ID[j] = student_ID[j + 1];// 學號排序
					student_ID[j + 1] = temp_student_ID;

					temp_name = name[j];
					name[j] = name[j + 1];// 姓名排序
					name[j + 1] = temp_name;
				}
			}
		}
	}

	public static void Excel() throws BiffException, IOException, RowsExceededException, WriteException {
		int i, j;
		groupsnum = group[group.length - 1];// 將最後一組的組數字指定給組別數量
		WritableWorkbook newWB = Workbook.createWorkbook(new File(PathExcel));
		WritableSheet ws0 = newWB.createSheet("分組名單(直向)", 0);
		WritableSheet ws1 = newWB.createSheet("分組名單(橫向)", 1);
		for (i = 0; i < name.length; i++) {
			for (j = 0; j < 3; j++) {
				if (j == 0) {
					Label label = new Label(j, i, student_ID[i]);
					ws0.addCell(label);
				}
				if (j == 1) {
					Label label = new Label(j, i, name[i]);
					ws0.addCell(label);
				}
				if (j == 2) {
					String string_group = Integer.toString(group[i]);
					Label label = new Label(j, i, string_group);
					ws0.addCell(label);
				}
			}
		}
		int count = 0;
		for (i = 0; i < groupsnum; i++) {
			Label label = new Label(0, i, "第" + (i + 1) + "組");
			ws1.addCell(label);
			for (j = 0;; j++) {
				Label label2 = new Label(j + 1, i, name[count]);
				ws1.addCell(label2);
				if (count >= name.length - 1) {
					break;
				}
				if (group[count] != group[count + 1]) {
					count++;
					break;
				}
				count++;
			}
		}
		/*
		 * int groupnum=0; for(int k=0;k<name.length;k++) {
		 * if(Integer.parseInt(name[k][2])>groupnum)
		 * groupnum=Integer.parseInt(name[k][2]); } for (i = 0; i < groupnum; i++) { int
		 * sum = 0; Label label = new Label(0, i, "第" + (i + 1) + "組");
		 * ws1.addCell(label); for (j = 1; j < sheet.getRows(); j++) { if
		 * (Integer.parseInt(sheet.getCell(2, j).getContents())== (i + 1)) { label = new
		 * Label(sum+1, i, sheet.getCell(1, j).getContents()); // 名字 ws1.addCell(label);
		 * sum++; } } }
		 */
		// 第二種寫法

		newWB.write();
		newWB.close();
		new button_outputstudent();// 呼叫按鈕包裹
	}
}
