package Package3;

import java.io.*;
import jxl.*;
import jxl.write.*;

public class writableworkbook {

	public writableworkbook(int round,int ran,String[][] student) throws Exception, IOException{
		Workbook wb = Workbook.getWorkbook(new File(main.PathExcel));
		Sheet sheet = wb.getSheet(2);
		WritableWorkbook book = Workbook.createWorkbook(new File(main.PathExcel),wb); 
		WritableSheet sheetname = book.createSheet("考試紀錄", 4);
		//在已有檔案中新增頁籤
		Write(sheetname,sheet,round,ran,student); //呼叫方法
		book.write();
		book.close();
	}

	private void Write(WritableSheet sheetname, Sheet sheet,int round,int ran,String[][] student) throws Exception, IOException{
		Label label;
		int count=1; //第幾輪
		int[] num=new int[sheet.getRows()-1]; //各組輪流順序
		
		for(int i=1;i<sheet.getRows();i++) {
			label=new Label(i, 0, sheet.getCell(0,i).getContents());
			sheetname.addCell(label); //表頭
		}
		for(int i=1;i<=round;i++) { 
			label=new Label(0, i, "第"+count+"輪");
			sheetname.addCell(label);//表頭
			
			for(int j=1;j<sheet.getRows();j++) {
				if(count==1)num[j-1]=ran%student[j-1].length; //各組第一輪人員
				
				if(num[j-1]>student[j-1].length||num[j-1]==0) 
					num[j-1]=1;
				//數字超過出席組員數則歸零
				label=new Label(j, count, student[j-1][num[j-1]-1]);
				sheetname.addCell(label); //放入工作表
				num[j-1]++; //各組人員輪流
			}
			count++;
		}
		
	}

}
