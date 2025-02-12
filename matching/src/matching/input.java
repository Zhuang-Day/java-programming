package matching;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class input // 取得陣列內容
{

	static student stu[];// 學生物件陣列
	static school sch[];// 學校物件陣列
	static Workbook workbook = null;
	static String Path = "TestData.xls";

	public static void reading(String path) throws BiffException, IOException {
		Path=path;
		try{
			workbook = Workbook.getWorkbook(new File(Path));
		}catch(FileNotFoundException e) {
			System.out.println("系統找不到此檔案，請確定是否輸入錯檔名或是未放在正確位置");
			System.exit(0);
		}
		

		// 處理學生
		Sheet stu_sheet = workbook.getSheet("學生志願");// 讀取工作表的名稱
//		System.out.println(stu_sheet.getRows() + "排");
//		System.out.println(stu_sheet.getColumns() + "行");
		stu = new student[104];// 取得學生數;宣告物件陣列
		for (int i = 1; i <= stu.length; i++) // i數量為學生數=排的數量
		{
			stu[i - 1] = new student();// 宣告物件
			// 讀取儲存格(Cell)的方法，getCell(Columns, Rows)，也是一樣從0開始(直,橫)
			Cell c = stu_sheet.getCell(0, i);
			if(c.getContents()=="")
				break;
			stu[i - 1].name = c.getContents();
//			System.out.println(" 索引值"+(i-1)+" "+stu[i-1].name);//確定人名
			// 存入i學生的志願
			for (int j = 1; j <= 4; j++) {
				c = stu_sheet.getCell(j, i);// 第j志願
				stu[i - 1].OPT[j - 1] = c.getContents();
//				System.out.print("第"+j+"志願"+stu[i-1].OPT[j-1]+" ");//確定志願
//				if(stu[i-1].OPT[j-1]=="")
//					System.out.print("空");
			}
//			System.out.println();

		}

		// 處理學校名
		Sheet school_sheet = workbook.getSheet("名額");// 讀取工作表的名稱
		sch = new school[school_sheet.getRows() - 1];// 取得學校數;宣告物件陣列
		int count=1;//計算排
//		System.out.println(school_sheet.getRows());
		for (int i = 1; i < school_sheet.getRows(); i++) // i數量為學校數=排的數量
		{
			sch[i - 1] = new school();// 宣告物件
			Cell c = school_sheet.getCell(0, i);// 取得學校名
			sch[i - 1].name = c.getContents();// 存入學校名
//			System.out.println(sch[i-1].name);//確定學校名
			c = school_sheet.getCell(1, i);// 取得學校報名人數
			sch[i - 1].OPTnum = Integer.parseInt(c.getContents());// 存入學校報名人數
//			System.out.println(sch[i - 1].name + "確定學校報名人數統計" + sch[i - 1].OPTnum);// 確定學校報名人數統計

			// 存入學校對學生的排名
			Sheet OPT_sheet = workbook.getSheet("成績");// 讀取工作表的名稱("學校/學系")
			int num=0;//OPT陣列長度
			c = OPT_sheet.getCell(0, count);
			while(c.getContents().equals(sch[i - 1].name)) {
//				System.out.println("排數"+count+" 內容:"+c.getContents());//確認位置及科系代碼
				c = OPT_sheet.getCell(2, count);
				num=Integer.parseInt(c.getContents());
				count++;
				try{
					c = OPT_sheet.getCell(0, count);
				}catch(ArrayIndexOutOfBoundsException e) {
					break;
				}
			}
//			System.out.println(sch[i - 1].name+"名次總共"+num+" 排數"+count+"學系開始排數"+(count-num));//確認名次
			sch[i - 1].OPT = new String[num];// 決定OPT數量;裡面的志願者排名
			int j = 0;
			for(int Rows=count-num;Rows<count;Rows++) {
				c = OPT_sheet.getCell(1, Rows);
				sch[i - 1].OPT[j] = c.getContents();
//				System.out.println(sch[i - 1].name+" "+sch[i - 1].OPT[j]);//確認學校排名
				j++;
			}
		}
	}

}

//學生類別
class student {
	String name;// 名字
	String OPT[] = new String[4];// 學生志願
}

//學校類別
class school {
	int OPTnum;// 學校的報名人數統計
	String name;// 學校名字
	String OPT[];// 學校志願

}
