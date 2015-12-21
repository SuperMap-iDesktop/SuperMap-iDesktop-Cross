package com.supermap.scene;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Timer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import com.supermap.ui.Action3D;
import com.supermap.ui.SceneControl;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;

public class SceneFrame extends JFrame {
	 private JTable table;

	public SceneControl sceneControl = null;
	private JToolBar toolbar = null;
	// JScrollPane jScrollPaneChildWindow = null;
	public JButton buttonSelect;
	public JButton buttonSelectPan;
	public JButton buttonPan;

	public SceneFrame() {

		this.setSize(640, 480);
		table = new JTable();
		this.sceneControl = new SceneControl();
		
//		String path = "F:/iDesktopJava/Resources/MapView/Toolbar/MapOperator/MeasureAngle.png";
		String path = "F:/iDesktopJava/Resources/MapView/Menu/MapOperator/Measure.png";
		ImageIcon defaultIcon = new ImageIcon(path);
		
//		JFrame frame = new JFrame("JToolBar Example");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
        this.toolbar = new JToolBar();        
        
        ControlButton dropDownButton = new ControlButton();
        dropDownButton.setIcon(defaultIcon);
		
		DropDownComponent dropDownComponent = new DropDownComponent(dropDownButton, null);
		Dimension dropDownComponentSize = new Dimension(44, 26);
		dropDownComponent.setMaximumSize(dropDownComponentSize);
		dropDownComponent.setMinimumSize(dropDownComponentSize);
		toolbar.add(dropDownComponent);		
		
        JMenuBar menuBar1 = new JMenuBar();
		JMenu menu = new JMenu("Test");
		menu.setIcon(defaultIcon);	
		
		JMenuItem item1 = new JMenuItem("1111");
		menu.add(item1);
		JMenuItem item2 = new JMenuItem("2222");
		menu.add(item2);
		menuBar1.add(menu);
		toolbar.add(menuBar1);		
		
		JComboBox comboBox = new JComboBox();
		comboBox.setEditable(false);
		Dimension maxSize = new Dimension(44, 26);
		Dimension minSize = new Dimension(44, 26);
		comboBox.setMaximumSize(minSize);
		comboBox.setMaximumSize(maxSize);
		comboBox.addItem(defaultIcon);
		comboBox.addItem(defaultIcon);
		comboBox.addItem(defaultIcon);
		toolbar.add(comboBox);	
		
		JButton button1 = new JButton();
		button1.setIcon(defaultIcon);
		toolbar.add(button1);	
		
		JButton button2 = new JButton();
		String pathDropdown = "F:/iDesktopJava/Resources/MapView/Menu/MapOperator/download.png";
		
		ImageIcon dropIcon = new ImageIcon(pathDropdown);
		button2.setIcon(dropIcon);
		Dimension buttonSize = new Dimension(16, 28);
		button2.setMaximumSize(buttonSize);
		button2.setMaximumSize(buttonSize);
		toolbar.add(button2);	
        
//		buttonSelect = new JButton("选择");
		buttonSelect = new JButton();
		buttonSelect.setIcon(defaultIcon);
		buttonSelect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sceneControl.setAction(Action3D.SELECT);
			}
		});
		toolbar.add(buttonSelect);

//		buttonSelectPan = new JButton("选择漫游");
		buttonSelectPan = new JButton();
		buttonSelectPan.setIcon(defaultIcon);
		buttonSelectPan.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sceneControl.setAction(Action3D.PAN);
			}
		});
		toolbar.add(buttonSelectPan);

//		buttonPan = new JButton("漫游");
		buttonPan = new JButton();
		buttonPan.setIcon(defaultIcon);
		buttonPan.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sceneControl.setAction(Action3D.PAN2);
			}
		});
		toolbar.add(buttonPan);		
		
		this.getContentPane().add(this.toolbar, BorderLayout.PAGE_START);	
        JScrollPane pane = new JScrollPane(sceneControl);
//        JScrollPane pane = new JScrollPane(this.table);
        this.getContentPane().add(pane, BorderLayout.CENTER);

		// 需要球显示出来了才能绑定工作空间，加载数据
		// this.sceneControl.getScene().setWorkspace(Application.getActiveApplication().getWorkspace());

		Timer idleTimer = new Timer();
		idleTimer.schedule(new IdleTimerTask(), 2000);
	}

	public void refreshToolbar() {
		if (sceneControl.getAction() == Action3D.SELECT) {			
			buttonSelect.setSelected(true);
			buttonSelect.setBackground(Color.GREEN);
//			buttonSelect.setText("选择" + " Y");
		} else {
			buttonSelect.setSelected(false);
			buttonSelect.setBackground(Color.gray);
//			buttonSelect.setText("选择" + " N");
		}
		
		if (sceneControl.getAction() == Action3D.PAN) {
			buttonSelectPan.setSelected(true);
			buttonSelectPan.setBackground(Color.GREEN);
//			buttonSelectPan.setText("选择漫游" + " Y");
		} else {
			buttonSelectPan.setSelected(false);
			buttonSelectPan.setBackground(Color.gray);
//			buttonSelectPan.setText("选择漫游" + " N");
		}
		
		if (sceneControl.getAction() == Action3D.PAN2) {
			buttonPan.setSelected(true);
			buttonPan.setBackground(Color.GREEN);
//			buttonSelectPan.setText("选择漫游" + " Y");
		} else {
			buttonPan.setSelected(false);
			buttonPan.setBackground(Color.gray);
//			buttonSelectPan.setText("选择漫游" + " N");
		}

//		buttonSelect.repaint();
//		buttonSelect.invalidate();
//		
//		buttonSelectPan.repaint();
//		buttonSelectPan.invalidate();
//		
//		buttonPan.repaint();
//		buttonPan.invalidate();
	}
}
