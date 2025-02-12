/*
 * 撰寫日期: 2020/06/01
 * 踩地雷小遊戲
 * */

package game_landmine;

import java.util.Random;
import java.util.Scanner;
import java.io.*;
import java.util.InputMismatchException;

class OperandException extends ArithmeticException {
	OperandException(String s) {
		super(s);
	}
}

public class Landmine {
	static int board;// 棋盤大小
	static Board_seat Boa_seat[][];// 針對棋盤每個位置的類別
	static int gameMode;
	static player P;// 玩家的物件
	static int mines_count;// 炸彈的總數
	static mines M[];// 炸彈的物件陣列
	static boolean confirm_err = true;// 確定錯誤
	static boolean GameContinue = true;

	public static void main(String[] args) throws IOException {
		Scanner scn = new Scanner(System.in);
		System.out.println("開始踩地雷，依據給予的數字題示避免選到地雷");

		// 除錯機制
		while (confirm_err) {
			try {
				System.out.println("請選擇模式，(1簡單 (2普通 (3困難(4自選設定");// 簡單3*3;普通5*5;困難10*10
				System.out.println("   簡單3*3，炸彈有4個");
				System.out.println("   普通4*4，炸彈有5個");
				System.out.println("   困難5*5，炸彈有10個");
				gameMode = scn.nextInt();// 儲存他選擇的遊戲模式
				if (gameMode > 4 | gameMode == 0)
					throw new OperandException("\r\n你輸入的值超過選項，請重新輸入\r\n");
				confirm_err = false;
			} catch (OperandException e) {
				System.out.println("發生意外:" + e);
				confirm_err = true;
			} catch (InputMismatchException e) {
				System.out.println("使用者輸入非整數時的例外");
				scn.next();
				confirm_err = true;
			}
		}

		switch (gameMode) {
		case 1:
			board = 3;
			mines_count = 4;
			break;
		case 2:
			board = 4;
			mines_count = 5;
			break;
		case 3:
			board = 5;
			mines_count = 10;
			break;
		case 4:
			confirm_err = true;
			while (confirm_err) {
				try {
					System.out.println("請輸入地圖大小(規模都是正方形,請輸入5~9");
					board = scn.nextInt();
					if (board > 9 | board < 5 | board == 0)
						throw new OperandException("\r\n你輸入的值超過數值，請重新輸入\r\n");
					System.out.println("請輸入地雷數量(請不要超過地圖大小，或輸入0");
					mines_count = scn.nextInt();
					if (mines_count > board * board | mines_count == 0)
						throw new OperandException("\r\n你輸入的值超過數值，請重新輸入\r\n");
					confirm_err = false;
				} catch (OperandException e) {
					System.out.println("發生意外:" + e);
					confirm_err = true;
				} catch (InputMismatchException e) {
					System.out.println("使用者輸入非整數時的例外");
					scn.next();
					confirm_err = true;
				}
			}

			break;
		}

		M = new mines[mines_count];// 炸彈的一維陣列，陣列大小為地雷數量
		for (int i = 0; i < mines_count; i++)
			M[i] = new mines();// 建立每一個炸彈的物件
		Boa_seat = new Board_seat[board][board];// 棋盤大小[y軸][x軸]
		for (int i = 0; i < board; i++)
			for (int j = 0; j < board; j++)
				Boa_seat[i][j] = new Board_seat();// 建立每一個棋盤位置的物件

		Gameing();// 進入遊戲
		System.out.println("遊戲結束  Ending");
		System.out.println("玩家踩了"+P.count+"次");
		
		file();//讀取紀錄檔案
		
	}
	public static void file() throws IOException {
		Scanner scn=new Scanner(System.in);
		System.out.println();
		System.out.println("請問是否要記錄此次的紀錄(y/n");
		String Word_ans=scn.next();//儲存是否要記錄
		BufferedReader BR = new BufferedReader(new InputStreamReader(System.in));
		FileWriter fw = new FileWriter("score.txt",true);// 建立FileWriter物件
		
		if(Word_ans.equalsIgnoreCase("Y")) {
			System.out.println();
			System.out.println("請輸入你的名字");
			String str = BR.readLine();
			fw.write(str, 0, str.length());// 寫入字串
			if(GameContinue==false)
			{
				str = ":遊戲失敗";
				fw.write(str, 0, str.length());// 寫入字串
				str = " 總共踩";
				fw.write(str, 0, str.length());// 寫入字串
				str=Integer.toString(P.count);
				fw.write(str, 0, str.length());// 寫入踩的次數
				str = "次";
				fw.write(str, 0, str.length());// 寫入字串
				fw.write('\n');// 寫入換行
			}
			else
			{
				str = ":遊戲破關";
				fw.write(str, 0, str.length());// 寫入字串
			}
			/*
			 * 結束檔案輸入
			 */
			fw.flush();
			fw.close();// 關閉FileWriter串流物件
		}
		/*
		 * 讀取檔案內容
		 */
		System.out.println();
		System.out.print("目前記錄為");
		FileReader fr = new FileReader("score.txt");// 建立FileReader物件
		int ch;
		if((ch = fr.read()) == -1) {
			System.out.println("空");
		}
		else {
			System.out.println();
			while ((ch = fr.read()) != -1)
				System.out.print((char) ch);
		}
		fr.close();// 關閉FileReader串流物件
	}
	public static void Mine() // 處理地雷的方法
	{
		Random ran = new Random();// 隨機出地雷出現的位置
		for (int i = 0; i < mines_count; i++) {
			M[i].X = ran.nextInt(board);// x軸123
			M[i].Y = ran.nextInt(board);// y軸ABC
			for (int j = 0; j < i;) {
				if (M[i].X == M[j].X && M[i].Y == M[j].Y) // 座標一樣
				{
					M[i].X = ran.nextInt(board);// x軸123
					M[i].Y = ran.nextInt(board);// y軸ABC
					j = 0;
				} else
					j++;
			}
//			System.out.println("地雷位置為" + ((char) (M[i].Y + 65)) + "," + (M[i].X + 1));
//			System.out.println("地雷位置為" + (M[i].X) + "," + (M[i].Y));
		}
	}

	public static void game_Board() // 顯示目前遊戲盤上的狀況
	{
		System.out.print("   ");
		int straight_coding = 0;// 豎邊的編碼
		Horizontal_coding();// 呼叫橫邊編碼
		for (int i = 0; i < Boa_seat.length; i++) // y軸123
		{
			straight_coding++;
			if (straight_coding < 10) {
				System.out.print(straight_coding + "  ");
			} else
				System.out.print(straight_coding + " ");
			for (int j = 0; j < Boa_seat[i].length; j++) // x軸ABC
			{
				if (Boa_seat[i][j].now == 1)
					System.out.print("☠     ");
				else if (Boa_seat[i][j].now == 2)
					Warn(i, j);
				else
					System.out.print("☐       ");
			}
			System.out.println();

		}
	}

	public static void Warn(int X, int Y) // 提示周圍有幾個炸彈
	{
		Boa_seat[X][Y].warn = 0;
		for (int i = 0; i < mines_count; i++) {
			if ((X + 1) == M[i].X && (Y + 1) == M[i].Y)
				Boa_seat[X][Y].warn++;
			else if ((X) == M[i].X && (Y + 1) == M[i].Y)
				Boa_seat[X][Y].warn++;
			else if ((X + 1) == M[i].X && (Y) == M[i].Y)
				Boa_seat[X][Y].warn++;
			else if ((X - 1) == M[i].X && (Y - 1) == M[i].Y)
				Boa_seat[X][Y].warn++;
			else if ((X - 1) == M[i].X && (Y) == M[i].Y)
				Boa_seat[X][Y].warn++;
			else if ((X) == M[i].X && (Y - 1) == M[i].Y)
				Boa_seat[X][Y].warn++;
			else if ((X - 1) == M[i].X && (Y + 1) == M[i].Y)
				Boa_seat[X][Y].warn++;
			else if ((X + 1) == M[i].X && (Y - 1) == M[i].Y)
				Boa_seat[X][Y].warn++;
		}
		System.out.print(Boa_seat[X][Y].warn + "   ");
	}

	public static void Horizontal_coding() // 顯示橫的編碼
	{
		for (int i = 0; i < board; i++) // 顯示英文A B C
		{
			System.out.print((char) (65 + i) + "   ");
		}
		System.out.println();
	}

	public static void Gameing() // 開始遊戲
	{
		Mine();// 決定地雷的位置
		Scanner scn = new Scanner(System.in);
		int err = 0;// 紀錄回答錯誤的次數
		P = new player();
		String text = "";//使用者輸入的字串
		char Char_ans;//英文
		int Int_ans;//數字
		// 如果玩家猜到地雷位置就結束遊戲
		GameContinue = true;
		run: while (GameContinue)// 判斷是否繼續遊戲
		{
			System.out.println("-------------------------------------");
			System.out.println("目前遊戲棋盤如下，請輸入你要猜的編號(如:A1/a1");
			game_Board();

			// 防止錯誤機制
			confirm_err = true;
			do {
				try {
					text = scn.next();//a2
					text = text.toUpperCase();// 轉換成大寫;A2
					Char_ans = text.charAt(0);//A
					Int_ans = Character.getNumericValue(text.charAt(1));// 將char強制轉成數字;2
					P.Xans = (Int_ans - 1);//[1]
					P.Yans = ((int) Char_ans - 65);//[0]=[A]
//					System.out.println("["+P.Xans+"]["+P.Yans+"]");//確認座標是否正確
					if(Boa_seat[P.Xans][P.Yans].now != 0) {
						err++;
						throw new OperandException("\r\n您可能輸入重複輸入了，請重新輸入\r\n");
					}
					confirm_err = false;
				}
				catch (OperandException e) {
					System.out.println("發生意外:" + e);
					confirm_err = true;
				}catch(ArrayIndexOutOfBoundsException e) {
					err++;
					System.out.println("您可能輸入數值超過棋盤大小了，請重新輸入");
				}catch(StringIndexOutOfBoundsException e) {
					err++;
					System.out.println("您只輸入一個字，請重新輸入");
				}
				if (err >= 3 && confirm_err == true)
					System.out.println("你還想要玩嗎?想要就認真輸入");
			} while (confirm_err);

			for (int i = 0; i < mines_count; i++) {
				if (M[i].X == P.Xans && M[i].Y == P.Yans) {
					Boa_seat[P.Xans][P.Yans].now = 1;
					game_Board();//呼叫棋盤
					System.out.println("踩到地雷");
					System.out.println();
					
					GameContinue = false;//踩到地雷
				} else
					Boa_seat[P.Xans][P.Yans].now = 2;
			}
			P.count++;
			if(P.count==(board*board-mines_count)) {
				break run;
			}
		}
		
	}

}

class Board_seat// 棋盤位置
{
	int warn;
	int now;// 現在位置所處的狀態，1是打中炸彈；2是為踩空

	Board_seat() {
		this.now = 0;// 原本都處於0
		this.warn = 0;
	}
}

class mines// 地雷
{
	int X;// 地雷所在的x點
	int Y;// 地雷所在的y點

}

class player// 玩家
{
	int Xans;// 玩家的x答案
	int Yans;// 玩家的y答案
	int count;
	player(){
		count=0;
	}
}
