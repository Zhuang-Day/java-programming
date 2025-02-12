package matching;

import java.io.*;

import jxl.Workbook;
import jxl.write.*;

public class output {

	output() throws IOException, WriteException {
		WritableWorkbook book = Workbook.createWorkbook(new File(input.Path.replace(".xls", "")+"放榜結果.xls"));
		WritableSheet sheet = book.createSheet("各系分發結果", 0);
		WritableSheet sheet1 = book.createSheet("落榜結果", 1);
		WritableSheet sheet3 = book.createSheet("各學生上榜名單", 2);

		// 各系分發結果
		Label label;
		label = new Label(0, 0, "放榜結果");
		sheet.addCell(label);
		label = new Label(0, 1, "科系代碼");
		sheet.addCell(label);
		label = new Label(1, 1, "缺額數");
		sheet.addCell(label);
		for (int k = 0; k < input.sch.length; k++) {
			jxl.write.Number number = new jxl.write.Number(0, k + 2, Integer.valueOf(input.sch[k].name));
			sheet.addCell(number);
			number = new jxl.write.Number(1, k + 2, (input.sch[k].OPTnum - matching.Sch[k].count));
			sheet.addCell(number);
		}
		int Row = 2;//橫排
		for (int k = 0; k < input.sch.length; k++) {
			int col = 2;//直行
			
			for (int i = 0; i < matching.Stu.length; i++) {
				if (matching.Stu[i].school.equals(input.sch[k].name)) {
					label = new Label(col, Row, input.stu[i].name);
					col++;
					sheet.addCell(label);
				}
				
			}
			Row++;
		}

		// 落榜結果
		label = new Label(0, 0, "無錄取者有以下幾位");
		sheet1.addCell(label);
		Row = 1;
		for (int i = 0; i < matching.Stu.length; i++) {
			if (matching.Stu[i].school.equals("-1")) {
				label = new Label(0, Row, input.stu[i].name);
				Row++;
				sheet1.addCell(label);
			}

		}

		// 各學生上榜名單
		label = new Label(0, 0, "學生名");
		sheet3.addCell(label);
		label = new Label(1, 0, "放榜結果");
		sheet3.addCell(label);
		for (int k = 0; k < matching.Stu.length; k++) {
			label = new Label(0, k + 1, input.stu[k].name);
			sheet3.addCell(label);
			jxl.write.Number number = new jxl.write.Number(1, k + 1, Integer.valueOf(matching.Stu[k].school));
			sheet3.addCell(number);
		}

		// 寫入資料並關閉檔案
		book.write();
		book.close();
	}

}
