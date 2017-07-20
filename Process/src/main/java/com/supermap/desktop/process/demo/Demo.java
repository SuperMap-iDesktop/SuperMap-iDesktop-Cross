package com.supermap.desktop.process.demo;

import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.ProcessResources;
import com.supermap.desktop.ui.FormBaseChild;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by yuanR on 2017/7/19.
 * 用户大会展台演示
 */
public class Demo extends FormBaseChild {


	public Demo() {
		this(ProcessProperties.getString("String_Demo"), null, null);
	}

	// 视频文件默认目录为桌面 demoView文件夹
	String path = "C://Users//Administrator//Desktop//demoView//";
	// 图片库文件夹名
	String iconFileName = "/UserMeetingPhotos/";
	// 热点分析
	String hotSpotAnalystPath = path + ProcessProperties.getString("String_hotSpotAnalyst") + ".avi";
	// 地统计分析
	String spatialPath = path + ProcessProperties.getString("String_SpatialStatistics") + ".avi";
	// 多进程切图
	String multiProcessClipPath = path + ProcessProperties.getString("String_MultiProcessClip") + ".avi";


	private DemoParameterButton hotSpotAnalystButton;
	private DemoParameterButton spatialButton;
	private DemoParameterButton multiProcessClipButton;


//	private DemoParameterButton demoParameterButton4;
//	private DemoParameterButton demoParameterButton5;
//	private DemoParameterButton demoParameterButton6;
//	private DemoParameterButton demoParameterButton7;
//	private DemoParameterButton demoParameterButton8;


	ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String filePath = ((DemoParameterButton) e.getSource()).getFilePath();
			try {
				if (!StringUtilities.isNullOrEmpty(filePath)) {
					Desktop.getDesktop().open(new File(filePath));
				}
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

		// 热点分析
		File hotSpotAnalystFile = new File(hotSpotAnalystPath);
		hotSpotAnalystButton = new DemoParameterButton(
				ProcessProperties.getString("String_hotSpotAnalyst"),
				ProcessResources.getIcon(iconFileName + ProcessProperties.getString("String_hotSpotAnalyst") + ".png"),
				hotSpotAnalystFile.getPath()
		);

		// 地统计分析
		File spatialStatisticsFile = new File(spatialPath);
		spatialButton = new DemoParameterButton(
				ProcessProperties.getString("String_SpatialStatistics"),
				ProcessResources.getIcon(iconFileName + ProcessProperties.getString("String_SpatialStatistics") + ".png"),
				spatialStatisticsFile.getPath()
		);

		// 多进程切图
		File multiProcessClipFile = new File(multiProcessClipPath);
		multiProcessClipButton = new DemoParameterButton(
				ProcessProperties.getString("String_MultiProcessClip"),
				ProcessResources.getIcon(iconFileName + ProcessProperties.getString("String_MultiProcessClip") + ".png"),
				multiProcessClipFile.getPath()
		);

	}

	private void initLayout() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		this.add(hotSpotAnalystButton);
		this.add(spatialButton);
		this.add(multiProcessClipButton);

//		this.add(demoParameterButton4);
//		this.add(demoParameterButton5);
//		this.add(demoParameterButton6);
//		this.add(demoParameterButton7);
//		this.add(demoParameterButton8);

	}

	private void initListener() {
		hotSpotAnalystButton.addActionListener(actionListener);
		spatialButton.addActionListener(actionListener);
		multiProcessClipButton.addActionListener(actionListener);

//		demoParameterButton4.addActionListener(actionListener);
//		demoParameterButton5.addActionListener(actionListener);
//		demoParameterButton6.addActionListener(actionListener);
//		demoParameterButton7.addActionListener(actionListener);
//		demoParameterButton8.addActionListener(actionListener);
	}
}
