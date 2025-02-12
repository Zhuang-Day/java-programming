/*
 * 打地鼠遊戲
 * */

package game;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.Random;

public class knock_Gophers {

	static int board;// 棋盤大小n*n
	static int[] Boa_seat;// 棋盤位置編碼;一開始是0;猜中是1;猜錯是2
	static int Gophers = 0;// 地鼠數量
	static int[] Gophers_seat;// 儲存地鼠出現的位置編碼
	static player P[];// 玩家物件陣列
	static int players;// 記錄幾名玩家
	static boolean confirm_err;// 確認除錯機制
	static int gameMode;
	static int ranking[];// 排行榜
	static int ranking_player[];// 玩家分數排行榜

	public static void main(String[] args) throws IOException {
		/*
		 * 20200523 莊佳穎08130521 基本選單顯示 選擇遊戲模式、棋盤大小、地鼠數量
		 */
		Scanner scn = new Scanner(System.in);
		System.out.println("在顯示棋盤時有設定延遲，請放心不是程式卡了");
		System.out.println("本程式沒有設置輸入格式錯誤的對應模式");

		// 除錯機制
		confirm_err = true;
		while (confirm_err) {
			try {
				System.out.println("打地鼠遊戲，請選擇遊戲模式(1單人模式(2多人模式");
				gameMode = scn.nextInt();// 儲存他選擇的遊戲模式
				confirm_err = false;
				while (gameMode > 2 | gameMode <= 0) {
					System.out.println("輸入選項以外的數字");
					confirm_err = true;
					break;
				}
			} catch (InputMismatchException e) {
				System.out.println("規格錯誤");
				scn.next();
				confirm_err = true;
			}
		}

		confirm_err = true;
		while (confirm_err) {
			try {
				System.out.println("請自訂棋盤大小n值(棋盤大小為n*n)，輸入1~10之間的數");
				board = scn.nextInt();// 棋盤大小
				confirm_err = false;
				if (board <= 0 || board > 10) {
					confirm_err = true;
					System.out.println("請勿輸入範圍以外的");
				}
			} catch (InputMismatchException e) {
				System.out.println("規格錯誤");
				scn.next();
				confirm_err = true;
			}
		}

		Boa_seat = new int[board * board];// 棋盤位置
		while (Gophers >= Boa_seat.length || Gophers == 0)// 地鼠數量大於棋盤數直接進入迴圈，或輸入0
		{
			confirm_err = true;
			while (confirm_err)// 未檢查到則是false，因此進入迴圈的重點是要為true
			{
				try {
					System.out.println("請自訂地鼠數量(勿大於等於棋盤大小或輸入0");
					Gophers = scn.nextInt();// 地鼠數量
					confirm_err = false;// 設置false
					if (Gophers <= 0) {
						confirm_err = true;
						System.out.println("請勿輸入0或負數");
					}
				} catch (InputMismatchException e) {
					System.out.println("規格錯誤");
					scn.next();
					confirm_err = true;
				}
			}
		}

		Gophers_seat = new int[Gophers];// 儲存地鼠出現的位置編碼
		Gophers();// 有關於地鼠的方法
		switch (gameMode) {
		case 1:
			Gamemode();// 進入單人遊戲環節
			System.out.println();
			System.out.println("遊戲結束");
			break;
		case 2:
			Gamemode();// 進入多人遊戲環節
			System.out.println();
			System.out.println("遊戲結束");
			break;
		}
		file();// 讀取紀錄檔案
	}

	public static void file() throws IOException {
		Scanner scn = new Scanner(System.in);
		System.out.println();
		System.out.println("請問是否要記錄此次的紀錄(y/n");
		String Word_ans = scn.next();// 儲存是否要記錄
		BufferedReader BR = new BufferedReader(new InputStreamReader(System.in));
		FileWriter fw = new FileWriter("score.txt", true);// 建立FileWriter物件

		if (Word_ans.equalsIgnoreCase("Y")) {
			String str;
			if (gameMode == 1) {
				confirm_err = true;
				while (confirm_err)// 未檢查到則是false，因此進入迴圈的重點是要為true
				{
					try {
						System.out.println();
						System.out.println("請輸入你的名字");
						str = BR.readLine();
						fw.write(str, 0, str.length());// 寫入字串
						confirm_err = false;
					} catch (InputMismatchException e) {
						System.out.println("規格錯誤");
						scn.next();
						confirm_err = true;
					}
					str = "玩家 " + 1 + " 得分" + P[0].right;
					fw.write(str, 0, str.length());// 寫入字串
					fw.write('\n');// 寫入換行
				}

			} else {
				confirm_err = true;
				while (confirm_err)// 未檢查到則是false，因此進入迴圈的重點是要為true
				{
					try {
						boolean conform = true;
						while (conform) {
							System.out.println();
							System.out.println("請問是玩家幾(輸入系統給的編號");
							int n = scn.nextInt();// 輸入編號
							System.out.println("請輸入你的名字");
							str = BR.readLine();
							fw.write(str, 0, str.length());// 寫入字串
							str = "玩家 " + n + " 得分" + P[n-1].right;
							fw.write(str, 0, str.length());// 寫入字串
							fw.write('\n');// 寫入換行
							System.out.println();
							System.out.println("是否還要繼續輸入紀錄(y/n");
							Word_ans = scn.next();// 儲存是否要記錄
							if (Word_ans.equalsIgnoreCase("N"))
								conform = false;
							confirm_err = false;
						}
					} catch (InputMismatchException e) {
						System.out.println("規格錯誤");
						scn.next();
						confirm_err = true;
					}

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
			int ch = 0;
			if ((ch = fr.read()) == -1) {
				System.out.println("空");
			} else {
				System.out.println();
				while ((ch = fr.read()) != -1)
					System.out.print((char) ch);
			}
			fr.close();// 關閉FileReader串流物件
		}
	}

	public static void Gophers() // 處理地鼠的方法
	{
		/*
		 * 劉伊庭08130076
		 */
		Random ran = new Random();// 隨機出地鼠出現的位置
		// 以下是以防不同地鼠出現相同位置;兩重迴圈去重
		for (int i = 0; i < Gophers; i++) {
			Gophers_seat[i] = ran.nextInt(Boa_seat.length) + 1;
			for (int j = 0; j < i;) {
				if (Gophers_seat[i] == Gophers_seat[j]) {
					Gophers_seat[i] = ran.nextInt(Boa_seat.length) + 1;// r.nextInt (201) ;這個是0 - 200
					j = 0;
				} else
					j++;
			}
		}
	}

	public static void game_Board() // 顯示目前遊戲盤上的狀況
	{
		/*
		 * 張筑媗08131471 20200602✔為打中;✘為沒打中 因為個位數少一個空格，因此將個位數和十位數規格分開 規定換行:
		 * 例如n=5，則到五的倍數時需換一行
		 */
		for (int i = 0; i < Boa_seat.length; i++) {
			if (i < 10) {
				if (Boa_seat[i] == 0)
					System.out.print((i + 1) + "  ");
				else if (Boa_seat[i] == 1)
					System.out.print("✔   ");
				else
					System.out.print("✘     ");

			} else {
				if (Boa_seat[i] == 0)
					System.out.print((i + 1) + " ");
				else if (Boa_seat[i] == 1)
					System.out.print("✔   ");
				else
					System.out.print("✘    ");

			}
			// 使用&&，必須要是倍數才要檢查他是否為零
			if ((i + 1) % board == 0 && i != 0)// 因為是倍數，因此要小心i=0的時候。增加規則除了要倍數外還不能是0
				System.out.println();
		}
	}

	public static void Gamemode() {
		/*
		 * 20200523 莊佳穎08130521 先判對gameMode 再建立玩家陣列，建立每一位玩家物件 顯示遊戲棋盤，呼叫game_Board() 進入遊戲
		 * 確認是否打到地鼠 給予回饋
		 */
		Scanner scn = new Scanner(System.in);

		int err = 0;// 紀錄回答錯誤的次數
		int Total_Right = 0;// 紀錄玩家們的總答對數

		if (gameMode == 1) {
			System.out.println("正在進入單人遊戲模式中");
			players = 1;// 記錄1名玩家
		} else {
			System.out.println("正在進入多人遊戲模式中");
			confirm_err = true;
			while (confirm_err)// 未檢查到則是false，因此進入迴圈的重點是要為true
			{
				try {
					System.out.println("請輸入有幾名玩家");
					players = scn.nextInt();// 記錄多名玩家
					confirm_err = false;// 設置false
					if (players <= 0) {
						confirm_err = true;
						System.out.println("請勿輸入0或負數");
					}
				} catch (InputMismatchException e) {
					System.out.println("規格錯誤");
					scn.next();
					confirm_err = true;
				}
			}
		}
		player P[] = new player[players];// 建立玩家陣列
		for (int i = 0; i < players; i++)
			P[i] = new player();// 建立每一位玩家物件

		// 開始遊戲
		run: while (Gophers > Total_Right) {
			for (int i = 0; i < players; i++) {
				// 延遲棋盤
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				// 以下是顯示目前棋盤
				System.out.println();// 換行
				System.out.println("-------------------------------------");
				System.out.println("以下是現在棋盤目前狀況");
				game_Board();// 顯示目前遊戲盤上的狀況

				// 防止錯誤機制
				confirm_err = true;
				while (confirm_err)// 未檢查到則是false，因此進入迴圈的重點是要為true
				{
					try {
						System.out.println("請玩家" + (i + 1) + "輸入你想猜的位置");
						P[i].ans = scn.nextInt();// 儲存此玩家的答案
						confirm_err = false;// 設置false
						while (P[i].ans <= 0 || P[i].ans >= Boa_seat.length + 1 || Boa_seat[P[i].ans - 1] == 1
								|| Boa_seat[P[i].ans - 1] == 2) {
							P[i].err++;
							if (P[i].err >= 3)
								System.out.println("你還想要玩嗎?想要就認真輸入");
							System.out.println("輸入錯誤或重複輸入，請重新輸入");
							P[i].ans = scn.nextInt();// 儲存此玩家的答案
							confirm_err = false;// 設置false
						}
					} catch (InputMismatchException e) {
						System.out.println("規格錯誤");
						scn.next();
						confirm_err = true;
						i -= 1;
					}
				}

				P[i].count++;// 玩家猜的次數加一
				/*
				 * 20200602 呂郁萱08130475 以下迴圈為確認是否打到地鼠
				 */
				for (int j = 0; j < Gophers_seat.length; j++) {
					if (Gophers_seat[j] == P[i].ans) {
						P[i].right++;
						Total_Right++;// 累加全玩家總共猜對幾隻地鼠
						Boa_seat[P[i].ans - 1] = 1;
						System.out.println();
						System.out.println("▶▶▶打中了，恭喜可以再猜一次");
						System.out.println();
						break;
					} else
						Boa_seat[P[i].ans - 1] = 2;
				}
				if (Boa_seat[P[i].ans - 1] == 2) {
					System.out.println("☐☐☐☐☐☐☐");
					System.out.println("☐  沒打到  ☐");
					System.out.println("☐☐☐☐☐☐☐");
				}

				P[i].show(i, Gophers, Total_Right);// 為玩家猜完後得到的反饋

				if (players != 1 && Boa_seat[P[i].ans - 1] == 1)// 判斷猜對了，給予玩家第二次猜的機會
					i--;
				if (Gophers == Total_Right)
					break run;// 結束遊戲

			} // 跑玩家迴圈結束點
		}

		// 莊佳穎08130521
		// 挑出最後勝者
		ranking = new int[players];// 排行榜
		ranking_player = new int[players];// 玩家分數排行榜
		for (int i = 0; i < players; i++) {
			ranking[i] = i;// 先把每個玩家編號放進去
			ranking_player[i] = P[i].right;
		}
		int stop = 0;// 暫存玩家分數
		int Stop = 0;// 暫存玩家編號
		for (int i = 0; i < players; i++) {
			for (int j = 0; j < players - 1; j++) {
				if (ranking_player[j + 1] > ranking_player[j]) {
					Stop = ranking[j];
					ranking[j] = ranking[j + 1];
					ranking[j + 1] = Stop;
					stop = ranking_player[j];
					ranking_player[j] = ranking_player[j + 1];
					ranking_player[j + 1] = stop;
				}
			}

		}
		// 顯示最後排行
		System.out.println("\r\n");
		if (players >= 3) {
			System.out.println("第一名為: 玩家 " + (ranking[0] + 1) + " 得分" + ranking_player[0]);
			System.out.println("第二名為: 玩家 " + (ranking[1] + 1) + " 得分" + ranking_player[1]);
			System.out.println("第三名為: 玩家 " + (ranking[2] + 1) + " 得分" + ranking_player[2]);
		} else {
			for (int i = 0; i < players; i++) {
				System.out.println("第" + (i + 1) + "名為: 玩家 " + (ranking[i] + 1) + " 得分" + ranking_player[i]);
			}
		}
	}

}

/*
 * 玩家類別 莊佳穎08130521 屬於玩家的類別 答案、答對次數、猜了幾次、開始時間
 */
class player// 玩家
{
	int ans;// 玩家的答案
	int right;// 答對的次數
	int count;// 計算猜了幾次
	long start;// 此玩家開始的時間
	int err;// 計算輸入錯誤幾次

	player() {
		this.start = System.currentTimeMillis();// 此玩家開始的時間
	}

	// 以下為玩家猜完後得到的反饋
	void show(int i, int Gophers, int Right) {
		long end = System.currentTimeMillis();

		System.out.println("玩家" + (i + 1) + "已猜了" + count + "次，打到" + right + "隻地鼠，還有" + (Gophers - Right) + "隻地鼠");
		System.out.println("目前使用時間:" + (end - start) / 1000 / 60 + "分" + (end - start) / 1000 % 60 + "秒"
				+ (end - start) % 1000 / 60 + " 猜對比例為" + right + "/" + count);
	}

}
