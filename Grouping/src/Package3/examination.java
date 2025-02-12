package Package3;

/*考試部分GUI(下一輪考試BUTTON,結束考試BUTTON)
 * 變數:輪round 亂數ran
 * 08130546羅辛亜
 * 2020/8/7 完成
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class examination extends JFrame {
	private static int round; // 輪數
	private static int ran; // 亂數
	private static String[][] student; // 出席學生名單

	private static JFrame frame;
	private static JTextArea textArea;
	private static String[] jbName = { "下一題", "結束考試" };
	private static JButton[] jb = new JButton[jbName.length];
	private static JScrollPane panelCenter; // 裝textArea
	private static JPanel panelSouth; // 裝兩個按鈕

	// 基本格式設定
	public examination() {
		round = 0;
		ran = (int) (Math.random() * 100) + 1;

		// JFrame表單設計
		frame = new JFrame("小組考試");
		frame.setSize(1200, 700);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setAlwaysOnTop(true);

		// JTextArea文字方塊設定
		textArea = new JTextArea();
		
		student=attend.attend();//呼叫學生出席
		
		textArea.setText(output());
		textArea.paintImmediately(textArea.getBounds());
		textArea.setEditable(false);
		textArea.setFont(new Font(null, Font.PLAIN, 18));
		panelCenter = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		frame.add(panelCenter, BorderLayout.CENTER);

		// JButton按鈕設計
		panelSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));
		for (int i = 0; i < jb.length; i++) {
			jb[i] = new JButton(jbName[i]);
			jb[i].addActionListener(new InnerListener());
			panelSouth.add(jb[i]);
		}
		jb[0].setBackground(Color.green);// 綠色
		jb[1].setBackground(Color.red);// 紅色
		frame.add(panelSouth, BorderLayout.SOUTH);
		frame.setVisible(true);

	}

	

	// 回傳當輪應考試的學生的學生
	public static String output() {
		int num = ran + round; // 原始亂數加上已輪過的次數
		round++;
		StringBuffer sb = new StringBuffer("第" + round + "輪考試\r\n");
		for (int i = 0; i < student.length; i++) {
			sb.append("第" + (i + 1) + "組	");
			int count = num % student[i].length;
			sb.append(student[i][count]);
			sb.append("\r\n");
		}
		return sb.toString();
	}

	// 按鈕事件
	public class InnerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int num = -999;// 預設序號為-999

			for (int i = 0; i < jb.length; i++) {
				if (e.getSource() == jb[i])
					num = i;
			}
			if (num == 0) {// 下一輪
				textArea.append(output());
				textArea.paintImmediately(textArea.getBounds());
			} else {// 結束程式
				frame.setVisible(false);
				
				end();
		        
				
				try {
					new writableworkbook(round,ran, student);
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				// round是總共考了幾次試
				// ran是原始亂數
				// student陣列是出席學生的String[][]陣列
				// 承耘你看你會用到什麼 加油~~
				System.exit(0);
			}

		}

	}
	
	public static void end() {
		final JFrame jf = new JFrame();
        jf.setSize(400, 400);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JOptionPane.showMessageDialog(jf,"考試結束，感謝使用本系統","結束",JOptionPane.PLAIN_MESSAGE);
	}
	
}
