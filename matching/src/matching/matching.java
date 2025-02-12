package matching;

//學生類別
class stuOPT {
	String school = "-1";// 最後上榜的學校
	int order = 0;// 學校順位
}

class schOPT {
	int count = 0;// 計算現在學校有哪些學生進
}

public class matching {
	static stuOPT[] Stu = new stuOPT[input.stu.length];// 學生物件陣列
	static schOPT[] Sch = new schOPT[input.sch.length];// 學校物件陣列
	// 上榜名單

	matching() {
		for (int i = 0; i < Stu.length; i++) {
			Stu[i] = new stuOPT();
		}
		for (int i = 0; i < Sch.length; i++) {
			Sch[i] = new schOPT();
		}
//		for (int j = 0; j < Sch.length; j++) {
//			System.out.println(input.sch[j].name + "只要" + input.sch[j].OPTnum + "人");// 確認內容是否正確
//		}
		Last();
	}

	public static void Last() {
		int j = 0;
		for (int i = 0; i < Stu.length; i++) {
			run: while (Stu[i].school.equals("-1"))// 目前他的志願為無
			{
//				System.out.println("學生陣列數"+Stu.length+" 索引值"+i);
//				System.out.println(" "+input.stu[i].name+"學生志願序 "+Stu[i].order+"陣列長度為"+input.stu[i].OPT.length);
				if (Stu[i].order == input.stu[i].OPT.length)// 志願為-1或學生志願序到底則跳過
					break;
//				System.out.println("學生志願為 "+input.stu[i].OPT[Stu[i].order]);
				if (input.stu[i].OPT[Stu[i].order] == (""))
					break;
				Stu[i].school = input.stu[i].OPT[Stu[i].order];// 符合條件則換下一位學生審核
				for (j = 0; j < Sch.length; j++) {
					if (input.sch[j].name.equals(input.stu[i].OPT[Stu[i].order])) {
						Sch[j].count++;// 已經有多少人進這個學校
						if (Sch[j].count > input.sch[j].OPTnum)// 當看到學校招募人數超過則進入下一步驟
						{
//							System.out.println(input.sch[j].name + "只要" + input.sch[j].OPTnum + "人  現在" + Sch[j].count + "人");// 確認招募人數
							equal(j);// 確定學校入選人數
//							System.out.println("equal(j)後:" + input.sch[j].name + "只要" + input.sch[j].OPTnum + "人  現在"+ Sch[j].count + "人");// 確認招募人數
						}

						break run;
					}
				}
			}

		}

	}

	// 最後確認每一個學校有沒有多上榜者
	public static void equal(int Sc) {
		String end = "";
		int num = 0;
		int remember_num = 0;

		// 此學校相對
		int j = 0;
		run: while (j < Stu.length)// 跑上榜名單
		{
			if (Stu[j].school.equals(input.sch[Sc].name))// 到相同則進入下一步，否則j++
			{
//				System.out.println(Stu[j].school+" "+num+" "+input.sch[Sc].OPT.length);//檢查是否進入迴圈;有出現
				while (num < input.sch[Sc].OPT.length)// 判斷學校志願中j同學的排名為多少
				{
//					System.out.println("找到的學生名"+input.stu[j].name+" 對照"+input.sch[Sc].OPT[num]);//檢查是否進入迴圈;有出現
					if (input.stu[j].name.equals(input.sch[Sc].OPT[num]))// 符合則跳過，否則num++
					{
//						System.out.println("找到的學生名"+input.stu[j].name);//檢查是否進入迴圈;有出現
						remember_num = num;
						for (int k = j; k < Stu.length; k++)// 找符合的人選
						{
							if (Stu[k].school.equals(input.sch[Sc].name))// 上榜學校和要找的學校相等則進入
							{
								num = 0;
								while (num < input.sch[Sc].OPT.length)// 判斷學校志願中誰先誰後
								{
									if (input.stu[k].name.equals(input.sch[Sc].OPT[num])) {
										if (remember_num > num)// 當學生j在學校的志願序比k還後面，例如第3位>第1位
										{
											end = input.stu[j].name;
											break;
										} else {
											end = input.stu[k].name;
											j = k;// 將比較者改為志願序後面的人
											remember_num = num;// 數值也改成志願序後面的數值
											break;
										}
									} else
										num++;
								}
							}
						}
						break run;
					} else
						num++;
				}
				if (num == input.sch[Sc].OPT.length) {
					end = input.stu[j].name;
					break run;
				}
			} else
				j++;
		}
		// 以下為確定每個人都檢查完後的處理
//		System.out.println(input.sch[Sc].name + "目前榜單中最後一位:" + end);
		who( end, Sc);
	}

	public static void who(String end, int Sc) {

			for (int N = 0; N < Stu.length; N++)// 找到此學生的個人編號
			{
				if (end.equals(input.stu[N].name)) {
//					System.out.println(input.stu[N].name);
					Sch[Sc].count--;
					Stu[N].school = "-1";
					Stu[N].order++;
					Last();
				}
			}
	}
}
