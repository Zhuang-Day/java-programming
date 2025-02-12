package matching;
//檔案是新的

//A作法
//選用"名額"及"成績", "成績"表中欄位"名次"是該生在某系的成績排名(無空行, 一系接在另一系之後)
//input記得檔案名稱更改以及學生數(陣列長度是手動
//完成

import java.io.*;
import java.util.*;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;

public class main {
	public static void main(String[] args) throws BiffException, IOException, WriteException {
		long time1, time2, time3, time4;

		Scanner scn = new Scanner(System.in);
		System.out.println("請確定你的資料放在程式所屬的資料夾內");
		System.out.println("請輸入檔案名稱(此程式只支援.xls檔)");
		String path = "";
		path = scn.next();
		String replace = path.replace(".xls", "");

		time1 = System.currentTimeMillis();
		input.reading(replace + ".xls");// 呼叫讀取excel的檔案
		time2 = System.currentTimeMillis();

//		System.out.println("讀完excel花了：" + (time2 - time1) / 1000 + "秒" + (time2 - time1) % 1000);

		new matching();
		time3 = System.currentTimeMillis();
//		System.out.println("分配完成" + (time3 - time2) / 1000 + "秒" + (time3 - time2) % 1000);

		new output();
		time4 = System.currentTimeMillis();
//		System.out.println("輸出完成" + (time4 - time3) / 1000 + "秒" + (time4 - time3) % 1000);

		int choose;
		do {
			System.out.println("1.各系分發結果");
			System.out.println("2.無上榜的學生名單");
			System.out.println("3.各系空缺人數");
			System.out.println("4.呈現各學生上榜名單");
			System.out.println("5.結束");
			choose = scn.nextInt();
			switch (choose) {
			case 1:
				System.out.println("放榜結果");
				for (int k = 0; k < input.sch.length; k++) {
					System.out.print(input.sch[k].name + "錄取者有 ");
					for (int i = 0; i < matching.Stu.length; i++) {
						if (matching.Stu[i].school.equals(input.sch[k].name)) {
							System.out.print(input.stu[i].name + " ");
						}
					}
					System.out.println();
				}
				break;
			case 2:
				System.out.println("無錄取者有以下幾位");
				for (int i = 0; i < matching.Stu.length; i++) {
					if (matching.Stu[i].school.equals("-1")) {
						System.out.println(input.stu[i].name + " ");
					}

				}
				System.out.println();
				break;
			case 3:
				System.out.println("各系空缺人數");
				for (int k = 0; k < input.sch.length; k++) {
					System.out.println(input.sch[k].name + " " + (input.sch[k].OPTnum - matching.Sch[k].count) + "人");
				}
				break;
			case 4:
				System.out.println("放榜結果");
				for (int k = 0; k < matching.Stu.length; k++) {
					if (matching.Stu[k].school.equals("-1"))
						System.out.println(input.stu[k].name + "並沒有上榜 ");
					else
						System.out.println(input.stu[k].name + "上榜於 " + matching.Stu[k].school);
				}
				break;
			}
		} while (choose != 5);
		System.out.println("感謝使用");

	}
}
