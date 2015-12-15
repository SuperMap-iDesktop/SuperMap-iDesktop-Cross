import java.awt.*;

import javax.swing.*;
import javax.swing.table.*;

import com.supermap.desktop.ui.controls.InternalImageIconFactory;

import java.awt.event.*;
import java.util.Random;

public class Ocar extends JFrame {
	String[] columnNames = { "", "", "" };// 这些都是必须的！！！虽然都是打酱油内容
	Object[][] data = { { "", "", "" },// 这些都是必须的！！！虽然都是打酱油内容
			{ "", "", "" }, // 这些都是必须的！！！虽然都是打酱油内容
			{ "", "", "" } };// 这些都是必须的！！！虽然都是打酱油内容
	JTable table = null;

	boolean isvisible = false;

	public Ocar() {
		DefaultTableModel model = new DefaultTableModel(data, columnNames);

		table = new JTable(model) {
			public Class getColumnClass(int column) {// 要这样定义table，要重写这个方法0，0的意思就是别的格子的类型都跟0,0的一样。
				return getValueAt(0, 0).getClass();
			}
		};
		init();
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getSource() == table && e.getClickCount() == 1) {
//					init();
					int c = table.getSelectedColumn();
					int r = table.getSelectedRow();
//					if (isvisible) {
//						isvisible = false;
						table.setValueAt(InternalImageIconFactory.INVISIBLE, r, c);
//					} else {
//						isvisible = true;
//						table.setValueAt(InternalImageIconFactory.VISIBLE, r, c);
//					}

					int random = new Random().nextInt(9);
					// if (r != random / 3 || c != random % 3)
					// table.setValueAt(InternalImageIconFactory.INVISIBLE,
					// random / 3, random % 3);
					// else
					// JOptionPane.showMessageDialog(null, "鱼被猫吃了");
				}
			}
		});
		table.setRowHeight(70);
		table.setCellSelectionEnabled(false);
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		getContentPane().add(table);
	}

	public void init() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				table.setValueAt(InternalImageIconFactory.VISIBLE, i, j);
	}

	public static void main(String[] args) {
		Ocar frame = new Ocar();
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}