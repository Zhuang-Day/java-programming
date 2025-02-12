/*
 * 撰寫日期 2021/03
主要功能
1.讀取 Excel: 讀取學生學號、姓名和加權數 (抽中機率加權)。
2.隨機抽籤:根據學生的加權數，使用加權隨機演算法決定抽中的學生。
3.確保每個學生 只會被抽到一次。
4.顯示資訊:列出學生加權列表及顯示目前的學生名單。
5.修改學生加權: 允許使用者輸入學生 學號、姓名或序號 來修改權重。
6.寫入 Excel 檔案: 將抽籤結果存入 Excel 檔案。
 * */

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import jxl.*;
import jxl.format.CellFormat;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

class Student {
	String id;// 學號
	String name;// 名字
	int weighted;// 加權數

	Student() {

	}

	Student(String id, String name, int weighted) {
		this.id = id;
		this.name = name;
		this.weighted = weighted;
	}

	public void setweighted(int weighted)// 修改學生資訊
	{
		this.weighted = weighted;
	}

	public String showInfo() {
		return ("學號" + id + " 姓名:" + name + " 權重:" + weighted);
	}

}

public class Drawlots {
	static int sum = 0;// 總加權

	public static void main(String[] args) throws BiffException, IOException, WriteException {
		// 宣告變數

		Student stu[];// 宣告物件名stu
		Scanner scn = new Scanner(System.in);
		System.out.println("進入隨機抽籤系統");

		int choose;
		stu = setStu();// 建立學生物件資本資訊
		System.out.println(sum);
		do {
			System.out.println("-------");
			System.out.println("1.開始抽籤並顯示抽到的名單排序");
			System.out.println("2.顯示學生各自加權列表");
			System.out.println("3.顯示目前抽獎池參與學生列表");
			System.out.println("4.修改學生加權");
			System.out.println("5.離開系統");
			System.out.print("=>");
			choose = scn.nextInt();// 使用者選擇

			switch (choose) {
			case 1:
				Lotto(stu);
				break;
			case 2:
				info_weighted(stu);// 顯示學生各自加權列表
				break;
			case 3:
				info_name(stu);// 顯示目前抽獎池參與學生列表
				break;
			case 4:
				setWeighted(stu);// 修改學生加權
				break;
			case 5:
				System.out.println("結束");
				break;
			default:
				System.out.println("請輸入選單上的選項");
			}
		} while (choose != 5);

	}

	public static void Lotto(Student[] stu) throws BiffException, IOException, WriteException {
		// 宣告
		Random ran = new Random();

		ArrayList list = new ArrayList();// 使用 ArrayList儲存抽到的先後順序
		int i = 0;

		while (i < stu.length) // 抽76次
		{
			int count = 0;// 加權總值
			int n = ran.nextInt(sum) + 1;// 隨機變數
			for (int j = 0; j < stu.length; j++) {
				count += stu[j].weighted;
				if (j != 0)
					System.out.println("目前累積加權(變數名count):" + count + " 目前抽到的值(變數名n):" + n + "  [" + (j) + "]的加權數:" + stu[j].weighted
							+ "  [" + (j - 1) + "]的加權數:" + stu[j - 1].weighted+" 名單中是否有["+j+ "]:"+list.contains(stu[j].name));
				if ((n - count) <= 0 & !list.contains(stu[j].name)) //如果 目前抽到的值(變數名n)-目前累積加權(變數名count)小於0 則代表目前加權者為抽到的人
				{
					System.out.println("加入:["+j+"]:"+stu[j].name);
					list.add(stu[j].name);// 加入
					break;
				}
			}
			i++;
		}
		System.out.print(list);

		input(list);

	}

	public static void input(ArrayList list) throws IOException, BiffException, WriteException {
		Workbook wb = Workbook.getWorkbook(new File("抽籤系統資訊.xls"));
		// 開啟一個檔案的副本,並且指定資料寫回到原檔案
		WritableWorkbook book2 = Workbook.createWorkbook(new File("抽籤系統資訊.xls"), wb);
		Sheet[] sheets = book2.getSheets();
		WritableSheet sheet = book2.createSheet("result"+(sheets.length-1), (sheets.length-1));
//		System.out.println(sheets[2].getName());
		sheet.addCell(new Label(0, 0, "結果 #result"));
		for(int i=0;i<list.size();i++) {
			sheet.addCell(new Label(0, (i+1), (String) list.get(i)));
		}
		book2.write();
		book2.close();
	}

	public static Student[] setStu() // 建立學生物件資本資訊
	{
		// 宣告變數
		Workbook workbook = null;// 建立一個Workbook
		Student stu[] = null;// 宣告物件名stu
		try {
			workbook = Workbook.getWorkbook(new File("抽籤系統資訊.xls"));
			Sheet[] sheets = workbook.getSheets();// 抓取excel有的工作頁籤
			stu = new Student[sheets[0].getRows() - 1];// 76位學生

//			System.out.println(sheets[0].getRows() + "排"+sheets[0].getColumns()+"行");//顯示幾排幾行

			for (int i = 0; i < sheets[0].getRows() - 1; i++) {

				stu[i] = new Student();// 宣告物件
//				System.out.print(i + " ");
				for (int j = 1; j < sheets[0].getColumns(); j++) {
					// 讀取儲存格(Cell)的方法，getCell(Columns, Rows)，也是一樣從0開始(直,橫)
					Cell c = sheets[0].getCell(j, i + 1);
					String s = c.getContents();
//					System.out.print(j + " ");

					switch (j) {
					case 1:
//						System.out.print(s + " ");//學號
						stu[i].id = s;// 轉成int型態
						break;
					case 2:
//						System.out.print(s + " ");// 名字
						stu[i].name = s;
						break;
					case 3:
//						System.out.print(s + " ");//加權
						stu[i].weighted = Integer.valueOf(s);// 轉成int型態
						sum += Integer.valueOf(s);// 加總
						break;
					}

				}
//				System.out.println("學生目前資訊:"+stu[i].showInfo());

			}
		} catch (BiffException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stu;
	}

	public static void info_weighted(Student[] stu) // 顯示學生各自加權列表
	{
		for (Student s : stu) {
			System.out.println("姓名:" + s.name + " 權重:" + s.weighted);
		}
	}

	public static void info_name(Student[] stu) // 顯示目前抽獎池參與學生詳細資訊
	{
		for (int i = 0; i < stu.length; i++) {
			System.out.print("序號" + (i + 1) + " ");
			System.out.println(stu[i].showInfo());
		}
	}

	public static void setWeighted(Student[] stu) // 修改學生加權
	{
		// 宣告
		Scanner scn = new Scanner(System.in);
		int choose = 0;

		do {
			try {
				System.out.println("-------");
				System.out.println("1.選擇輸入學生序號");
				System.out.println("2.選擇輸入學生姓名");
				System.out.println("3.選擇輸入學生學號");
				System.out.println("4.顯示學生列表");
				System.out.println("5.返回主選單");
				System.out.print("=>");
				choose = scn.nextInt();// 使用者選擇
				switch (choose) {
				case 1:

					System.out.print("輸入學生序號=>");
					int n = scn.nextInt();// 序號
					set(stu, (n - 1));
					break;
				case 2:
					System.out.print("輸入學生姓名=>");
					String name = scn.next();// 姓名
					for (int i = 0; i < stu.length; i++) {
						if (stu[i].name.equals(name)) {
							set(stu, i);
						}
					}

					break;
				case 3:
					System.out.print("輸入學生學號=>");
					String id = scn.next();// 姓名
					for (int i = 0; i < stu.length; i++) {
						if (stu[i].id.equals(id)) {
							set(stu, i);
						}
					}
					break;
				case 4:
					System.out.println("顯示目前抽獎池參與學生列表");
					info_name(stu); // 顯示目前抽獎池參與學生列表
					break;
				case 5:
					System.out.println("已返回主選單");
					break;
				default:
					System.out.println("請輸入選單上的選項");
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("查無此序號");
			}
		} while (choose != 5);

	}

	public static void set(Student[] stu, int n) {
		Scanner scn = new Scanner(System.in);
		System.out.println("此學生目前資訊:" + stu[n].showInfo());
		System.out.print("更改此學生權重=>");
		int w = scn.nextInt();
		stu[n].setweighted(w);
		System.out.println("更改後，此學生目前資訊:" + stu[n].showInfo());
	}
}
