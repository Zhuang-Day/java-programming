package game;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class GuessNumber {

	public static void main(String[] args) throws IOException, InterruptedException {
		Scanner scn = new Scanner(System.in);
		/*
		 * 宣告 檔案路徑、猜測範圍
		 */
		String path = "C:\\程式\\java\\game\\ans\\";// 檔案路徑
		char ch[] = new char[10];// 81~90 Q到Z
		for (int i = 0; i < ch.length; i++) {
			ch[i] = (char) (i + 81);
			System.out.print(ch[i]);
		}
		/*
		 * 初始動作 選擇答案、存入720個組合、建立每一次的紀錄資料夾
		 */
		String ans = ans(ch);// 電腦隨機選擇答案
//		String ans = "XYR";// 電腦隨機選擇答案
		ArrayList<String> nowList = all(ch);// 第一次先在陣列存入總共720個組合；型態為字串
		int count = 0;// 第幾次的猜測紀錄
		path += ans;

		/*
		 * 建立每一次紀錄的資料夾
		 */
		prepareFolder(path);// 建立每一次紀錄的資料夾
		String respond = "";// 使用者回復的提示字
		String jud = "";// 電腦猜測的結果
//		String j[]= {"XZS","RZU","XUY","YUS","XYR"};
//		int i=0;
		do {

			jud = jud(nowList);// 電腦猜測的結果
//			jud=jud(j[i]);
//			i++;
			System.out.println("輸入提示字(幾A幾B)=>");
//			respond = scn.next();//使用者幫忙判斷
			
			respond=computerRes(ans,jud);//電腦幫忙判斷 傳入答案以及電腦猜測的結果
			System.out.println(respond);//電腦幫忙判斷
			
			nowList = judgment(nowList, respond, jud);// 判斷得到的回應
			input(path + "\\" + count + "_" + jud + "_" + respond + ".txt", nowList);// 將每一次的紀錄儲存進檔案裡
			count++;
		} while (!respond.equals("3A"));
		System.out.println("恭喜答對，結束遊戲");

	}
	public static String computerRes(String ans, String jud) //電腦幫忙判斷
	{
		/*
		 * 傳入答案以及猜測組合
		 * 傳回判斷幾A幾B
		 * */
		String respond="";
		
		char[] chAns = ans.toCharArray();// 將現在要比對的組合拆成三個單字
		char[] chRe = jud.toCharArray();// 將猜測的組合拆成三個單字
		int A = 0, B = 0;// 幾A幾B
		
		for (int i = 0; i < chRe.length; i++)// chRe猜測的組合的組合
		{
			for (int j = 0; j < chAns.length; j++) // chAns現在要比對的組合
			{
				if (chRe[i] == chAns[j]) {
					if (i == j)
						A++;
					else
						B++;
				}
			}
		}
		
		if (A == 0) {
			if (B == 0) {
				respond = A + "A" + B + "B";
			} else {
				respond = B + "B";
			}
		} else if (B == 0) {
			respond = A + "A";
		} else {
			respond = A + "A" + B + "B";
		}

		
		return respond;
	}

	public static ArrayList<String> judgment(ArrayList<String> nowList, String respond, String jud) {

		/*
		 * 將電腦猜的組合視為答案和擁有的組合們再次比對，如果結果不等於提示字，則刪除此組合
		 */
		
//		因為如果ArrayList刪除後他的陣列長度會改，導致要找下一個物件時發生錯誤
//		你可以使用 Iterator 本身的方法 remove() 來刪除物件 
//		Iterator.remove()方法會在刪除當前迭代物件的同時維護索引的一致性
		Iterator<String> iterator = nowList.iterator();
		
		while (iterator.hasNext()) {
			String Words = iterator.next();// 現在擁有的組合們一個個比對
			//computerJud 組合們一個個比對結果 幾A幾B
			String computerJud = computerRes(jud,Words);//jud將電腦猜的組合視為答案;Words傳入現在要比對的組合
			if (!computerJud.equals(respond)) // 如果結果不等於提示字，則刪除此組合
			{
				iterator.remove();
			}
//			System.out.println(Words + " 電腦判斷=>" + computerJud);
//			System.out.println("結果不等於提示字=>"+!computerJud.equals(respond));
		}
		return nowList;
	}
//ArrayList<String> nowList String jud
	public static String jud(ArrayList<String> nowList) {
		/*
		 * 電腦選擇此次的猜測
		 */
		String jud = "";
		/*
		 * 隨機從範圍中抽取一個組合;以index為隨機值
		 */
		Random ran = new Random(); // 宣告一個Random，變數名稱為ran
		int index = ran.nextInt(nowList.size());
		jud = nowList.get(index);// 取出這次隨機出的組合

		System.out.println("電腦猜=>" + jud);
		return jud;
	}

	public static ArrayList<String> all(char ch[]) // 第一次先在陣列存入總共720個組合
	{
		// 宣告
		ArrayList<String> all = new ArrayList(720);// 不重複，總共720個組合；用指定的大小來初始化內部的陣列
		int num = 0;

		System.out.println();
		for (int i = 0; i < ch.length; i++)
			for (int j = 0; j < ch.length; j++)
				for (int k = 0; k < ch.length; k++)
					if (i != j && k != i && k != j) {
						all.add(String.valueOf(ch[i]) + String.valueOf(ch[j]) + String.valueOf(ch[k]));
						num++;
					}
//		System.out.println(all);
		return all;
	}

	public static String ans(char ch[]) // 電腦隨機選擇答案
	{
		/*
		 * 電腦隨機選擇答案 傳入可選的範圍 傳回答案
		 */
		String ans = "";
		Random ran = new Random(); // 宣告一個Random，變數名稱為ran
		int n = ran.nextInt(ch.length);
		int n2 = ran.nextInt(ch.length);
		int n3 = ran.nextInt(ch.length);
		/*
		 * 但是 && 、|| 這一組運算子會在左 邊的運算元就可以決定運算結果的情況下忽略右 邊運算元
		 */
		/*
		 * false true false=>
		 */
		while (n == n2 || n2 == n3 || n == n3) {
			n = ran.nextInt(ch.length);
			n2 = ran.nextInt(ch.length);
			n3 = ran.nextInt(ch.length);
		}
		// 這邊要+1是因為原本範圍為0~9
		ans = String.valueOf(ch[n]) + String.valueOf(ch[n2]) + String.valueOf(ch[n3]);

		System.out.println("\n選擇為答案" + ans);// 輸出答案

		/*
		 * 確認判斷的狀況
		 */
//		System.out.print(n == n2);
//		System.out.print( n2 == n3 );
//		System.out.print(n == n3);
//		System.out.print(n == n2 || n2 == n3 || n == n3);

		return ans;
	}

	public static void input(String filename, ArrayList<String> nowList) throws IOException, InterruptedException // 儲存進檔案裡
	{

		FileWriter fw = new FileWriter(filename, true);// 建立FileWriter物件

		/*
		 * 當動態陣列元素確定不在新增的時候,可以呼叫這個方法來釋放空餘的記憶體。
		 */
		nowList.trimToSize();// 將ArrayList固定到實際元素的大小

		for (String str : nowList) {
			fw.write(str, 0, str.length());// 寫入字串
			fw.write('\n');// 寫入換行
		}

		/*
		 * 結束檔案輸入
		 */
		fw.flush();
		fw.close();// 關閉FileWriter串流物件

	}

	private static void prepareFolder(String dir) throws IOException, InterruptedException // 建立每一次紀錄的資料夾
	{
		Path p = Paths.get(dir); // 路徑設定
		/* 確認資料夾是否存在 */
		if (Files.exists(p)) {
			System.out.println("資料夾已存在");
		}
		if (!Files.exists(p)) {
			/* 不存在的話,直接建立資料夾 */
			Files.createDirectory(p);
			System.out.println("已成功建立資料夾");
		}
	}

}
