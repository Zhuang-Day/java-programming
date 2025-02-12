package Package3;

/* 
 * 輸出出缺席名單
 * 全班名單GUI(開始考試BUTTON)
 * 08130521莊佳穎
 * 2020/08/06 完成
 * */
import java.awt.*;
import java.awt.Font;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.*;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;

public class button_outputstudent implements ActionListener {
	// 小組
	static String ButtonName[][];// 按鈕人員名字
	static JButton Button[];// 人員按鈕物件

	// 選擇模式
	static JButton selButton[];// 選擇模式按鈕物件
	static String selNeme[] = { "考試開始" };

	// 缺席
	static int absencenum = 0;// 缺席人數
	static String absence[] = null;// 紀錄缺席名單
	static ArrayList List = new ArrayList();
	// 視窗
	JFrame mainfrm;

	public button_outputstudent() {
		// 視窗設定
		mainfrm = new JFrame("程式設計考試分組");
		mainfrm.setSize(700, 700);
		mainfrm.setResizable(false);
		mainfrm.getContentPane().setLayout(null);
		mainfrm.setLocation(200, 120);

		// 讀取文檔，得到全班分組狀況
		readbook();

		// 設定按鈕容器
		Container ButtonPan = mainfrm.getContentPane();
		ButtonPan.setLayout(null); // 取消預設之 BorderLayout

		// 設定模式按鈕物件
		int x = 250, y = 20;
		selButton = new JButton[selNeme.length];
		for (int i = 0; i < selNeme.length; i++) {
			selButton[i] = new JButton(selNeme[i]);// 將模式名字放在按鈕上
			selButton[i].setBounds(x, y, 200, 30); // 自行決定元件位置與大小
			x += 200;
			selButton[i].setBackground(Color.white);
			selButton[i].setFont(new Font("標楷體", Font.PLAIN, 20));
			ButtonPan.add(selButton[i]);
			selButton[i].addActionListener(this);
		}

		x = 120;
		y = 70;// 位置
		// (Columns, Rows)，(直,橫)
		int Columns = 0, Rows = 0;

		for (int i = 0; i < Button.length; i++) {

//			System.out.println((Columns)+" "+(Rows));
			Button[i] = new JButton(ButtonName[Columns][Rows]);// 將學生名字放在按鈕上
			Rows++;

			Button[i].setBounds(x, y, 100, 30); // 自行決定元件位置與大小
			// 如果Rows=一組學生數，則換下一行
			// Columns=小組數
			// 否則按鈕往右擺放
			if (Rows == ButtonName[Columns].length) {
//				System.out.println("第"+(Columns+1)+"組有"+Rows+"人");
				Columns++;
				Rows = 0;
				x = 120;
				y += 30;
			} else {
				x += 100;
			}

			Button[i].setBackground(Color.white);
			ButtonPan.add(Button[i]);
			Button[i].addActionListener(this);

		}
		mainfrm.setVisible(true);
		mainfrm.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	public void actionPerformed(ActionEvent e) {
		// 選項按鈕

		if (e.getSource() == selButton[0]) {
			try {
				output();
				mainfrm.setVisible(false);
				new examination();// 呼叫examination
			} catch (WriteException | BiffException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		// 學生按鈕

		for (int i = 0; i < Button.length; i++) {
			if (e.getSource() == Button[i]) {
				if (Button[i].getBackground() == Color.white) {
					absencenum++;
//					System.out.println("被點擊的按鈕名字" + Button[i].getText());
					List.add(Button[i].getText());// 加入資料
					Button[i].setBackground(Color.lightGray);
				} else {
					absencenum--;
					List.remove(Button[i].getText());// 刪除資料
					Button[i].setBackground(Color.white);
				}

			}
		}

		absence = new String[absencenum];
		// 動態陣列轉換成一般陣列
		Object list[] = List.toArray();
		absence = Arrays.copyOf(list, list.length, String[].class);

	}

	public static void readbook() {
		String bookName = main.PathExcel;
		Workbook workbook = null;
		Workbook workbook1 = null;
		/*
		 * Cell c取得(直,橫)的內容 workbook為抓到的資料表
		 */
		try {
			workbook = Workbook.getWorkbook(new File(bookName));// 讀取excel的位置
			workbook1 = Workbook.getWorkbook(new File("112公告分組.xls"));// 讀取excel的位置
		} catch (BiffException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Sheet[] sheets = workbook.getSheets();// 得到每一工作表名稱
		Sheet sheet = workbook1.getSheet(0);// 得到工作表
		Button = new JButton[sheets[0].getRows()];// 設定按鈕數量=全班人數=第一張工作表的列總數

		// 計算小組人數
		StuGroup[] people = new StuGroup[main.groupsnum];// 學生物件陣列
		for (int i = 0; i < main.groupsnum; i++)
			people[i] = new StuGroup();
//				System.out.println(sheets[0].getRows());
		for (int i = 1; i < sheet.getRows(); i++)// 得到有幾列
		{
			try {
				Cell c = sheet.getCell(2, i);// 第三行是小組編碼
				int num = Integer.parseInt(c.getContents());// 現在遇到的小組編碼為num
//						System.out.println(num);
				people[num - 1].sum++;
			} catch (NumberFormatException e) {
				continue;
			}
		}
		// 抓學生名稱
		ButtonName = new String[main.groupsnum][];// 得到按鈕的名字
		for (int i = 0; i < ButtonName.length; i++) {
			ButtonName[i] = new String[people[i].sum];// 統計出來的總數等於此小組陣列長度
		}
		int num = 0;
		for (int i = 0; i < ButtonName.length; i++)// 得到有幾列
		{

			for (int j = 0; j < people[i].sum; j++) {
				ButtonName[i][j] = main.name[num];
				num++;
			}

		}

	}

	public static void output() throws IOException, RowsExceededException, WriteException, BiffException {
		String bookName = "112分組小考紀錄.xls";
		Workbook wb = Workbook.getWorkbook(new File(bookName));
		// 開啟一個檔案的副本,並且指定資料寫回到原檔案
		WritableWorkbook book = Workbook.createWorkbook(new File(bookName), wb);
		WritableSheet sheetAttend = book.createSheet("出席名單", 2);
		WritableSheet sheetAbsence = book.createSheet("缺席名單", 3);
		jxl.write.Label label = new jxl.write.Label(0, 0, "缺席名單");// 會和java.awt.Label同名，所以這邊直接呼叫
		// 將定義好的單元格新增到工作表中
		sheetAbsence.addCell(label);
		if (absence != null)
			for (int j = 0; j < absence.length; j++) {
				label = new jxl.write.Label(0, (j + 1), absence[j]);
				sheetAbsence.addCell(label);
			}
		else {
			label = new jxl.write.Label(0, 1, "無");
			sheetAbsence.addCell(label);
		}

		String[][] attendStudent = attend.attend();// 呼叫整理出席包裹
		label = new jxl.write.Label(0, 0, "出席名單");
		// 將定義好的單元格新增到工作表中
		sheetAttend.addCell(label);
		for (int i = 0; i < attendStudent.length; i++) {
			label = new jxl.write.Label(0, (i + 1), "第" + String.valueOf(i + 1) + "組");
			sheetAttend.addCell(label);
			for (int j = 0; j < attendStudent[i].length; j++) {
				label = new jxl.write.Label((j + 1), (i + 1), attendStudent[i][j]);
				sheetAttend.addCell(label);
			}
		}
		// 以下轉移到考試包裹，因為考試那邊也要輸入內容，否則檔案會重新建立
		// 寫入資料並關閉檔案
		book.write();
		book.close();

	}

}

class StuGroup {
	int num;// 現在輸入進去的編號
	int sum;// 總數

	StuGroup() {
		num = 0;
		sum = 0;
	}
}
