package com.supermap.desktop.process.demo;

import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.ui.FormBaseChild;
import com.supermap.desktop.utilities.CoreResources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by yuanR on 2017/7/19.
 * 用户大会展台演示
 *
 *
 */
public class Demo extends FormBaseChild {


	public Demo() {
		this(ProcessProperties.getString("String_Demo"), null, null);
	}

	// 视频文件默认目录为桌面 demoView文件夹
	String path = "C://Users//Administrator//Desktop//demoView//";
	String iconFileName = "/用户大会图片库/";

	String  iDesktopCross = "Cross桌面";
	String hotSpotAnalystName = "热点分析";


	DemoParameterButton demoParameterButton1;
	DemoParameterButton demoParameterButton2;
	DemoParameterButton demoParameterButton3;
	DemoParameterButton demoParameterButton4;
	DemoParameterButton demoParameterButton5;
	DemoParameterButton demoParameterButton6;
	DemoParameterButton demoParameterButton7;
	DemoParameterButton demoParameterButton8;


	ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String filePath = ((DemoParameterButton) e.getSource()).getFilePath();
			try {
				Desktop.getDesktop().open(new File(filePath));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	};

	public Demo(String title, Icon icon, Component component) {
		super(title, icon, component);
		initParameter();
		initLayout();
		initListener();

		this.setBackground(Color.WHITE);
	}

	private void initParameter() {

		demoParameterButton1 = new DemoParameterButton(hotSpotAnalystName,
				CoreResources.getIcon(iconFileName + hotSpotAnalystName + ".png"), path + hotSpotAnalystName + ".mp4");
		demoParameterButton2 = new DemoParameterButton("未知",
				CoreResources.getIcon(iconFileName + iDesktopCross + ".png"), "");
		demoParameterButton3 = new DemoParameterButton("未知",
				CoreResources.getIcon(iconFileName + iDesktopCross + ".png"), "");
		demoParameterButton4 = new DemoParameterButton("未知",
				CoreResources.getIcon(iconFileName + iDesktopCross + ".png"), "");
		demoParameterButton5 = new DemoParameterButton("未知",
				CoreResources.getIcon(iconFileName + iDesktopCross + ".png"), "");
		demoParameterButton6 = new DemoParameterButton("未知",
				CoreResources.getIcon(iconFileName + iDesktopCross + ".png"), "");
		demoParameterButton7 = new DemoParameterButton("未知",
				CoreResources.getIcon(iconFileName + iDesktopCross + ".png"), "");
		demoParameterButton8 = new DemoParameterButton("未知",
				CoreResources.getIcon(iconFileName + iDesktopCross + ".png"), "");

	}

	private void initLayout() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		this.add(demoParameterButton1);
		this.add(demoParameterButton2);
		this.add(demoParameterButton3);
		this.add(demoParameterButton4);
		this.add(demoParameterButton5);
		this.add(demoParameterButton6);
		this.add(demoParameterButton7);
		this.add(demoParameterButton8);

	}

	private void initListener() {
		demoParameterButton1.addActionListener(actionListener);
		demoParameterButton2.addActionListener(actionListener);
		demoParameterButton3.addActionListener(actionListener);
		demoParameterButton4.addActionListener(actionListener);
		demoParameterButton5.addActionListener(actionListener);
		demoParameterButton6.addActionListener(actionListener);
		demoParameterButton7.addActionListener(actionListener);
		demoParameterButton8.addActionListener(actionListener);
	}
}
