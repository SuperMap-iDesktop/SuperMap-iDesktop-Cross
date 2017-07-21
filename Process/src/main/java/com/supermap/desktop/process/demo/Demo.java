package com.supermap.desktop.process.demo;

import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.ProcessResources;
import com.supermap.desktop.ui.FormBaseChild;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
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

	private static final int DEFAULT_WIDTH = 1440;
	private static final int DEFAULT_HEIGHT = 700;

	private final Timer timer;
	private final LeftTimer moveTime;

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

	private JLabel labelHotSpotAnalyst = new JLabel();
	private JLabel labelSpatial = new JLabel();
	private JLabel labelMultiProcessClip = new JLabel();

	private JLabel[] currentPics;

	private JPanel panelCenter = new JPanel();
	private int currentIndex = 0;

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

		timer = new Timer(5000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveLeft();
			}
		});
		timer.setInitialDelay(1000);

		moveTime = new LeftTimer();
		this.setBackground(Color.WHITE);
		timer.start();
	}

	private void moveLeft() {
		int next = currentIndex + 1;
		if (next == currentPics.length) {
			next = 0;
		}
		moveTime.setCurrentComponent(currentPics[currentIndex]);
		moveTime.setNextComponent(currentPics[next]);
		currentIndex = next;
		moveTime.start();

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

		labelHotSpotAnalyst.setIcon(ProcessResources.getIcon(iconFileName + ProcessProperties.getString("String_hotSpotAnalyst") + ".png"));
		labelSpatial.setIcon(ProcessResources.getIcon(iconFileName + ProcessProperties.getString("String_SpatialStatistics") + ".png"));
		labelMultiProcessClip.setIcon(ProcessResources.getIcon(iconFileName + "duoJinChengQieTu.png"));
		currentPics = new JLabel[]{
				labelHotSpotAnalyst, labelSpatial, labelMultiProcessClip
		};
	}

	private void initLayout() {
		panelCenter.setLayout(null);
		panelCenter.setOpaque(false);
		labelHotSpotAnalyst.setBounds(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		labelSpatial.setBounds(DEFAULT_WIDTH, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		labelMultiProcessClip.setBounds(DEFAULT_WIDTH, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);


		panelCenter.add(labelHotSpotAnalyst);
		panelCenter.add(labelSpatial);
		panelCenter.add(labelMultiProcessClip);
		panelCenter.setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		panelCenter.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

		JPanel jPanelButton = new JPanel();
		jPanelButton.setOpaque(false);
		jPanelButton.setLayout(new GridBagLayout());
		jPanelButton.add(hotSpotAnalystButton, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.EAST));
		jPanelButton.add(spatialButton, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 0).setInsets(0, 5, 0, 0));
		jPanelButton.add(multiProcessClipButton, new GridBagConstraintsHelper(2, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 0, 0));


		this.setLayout(new GridBagLayout());
		this.add(panelCenter, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.NONE).setInsets(10, 0, 0, 0));
		this.add(jPanelButton, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.NORTH));


	}

	private void initListener() {
		hotSpotAnalystButton.addActionListener(actionListener);
		spatialButton.addActionListener(actionListener);
		multiProcessClipButton.addActionListener(actionListener);
	}

	class LeftTimer extends Timer {

		private JComponent currentComponent;
		private JComponent nextComponent;
		private int step = 20;

		public LeftTimer() {
			super(1, null);
			this.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (currentComponent != null && nextComponent != null) {
						currentComponent.setLocation(currentComponent.getX() - step, 0);
						nextComponent.setLocation(nextComponent.getX() - step, 0);
						if (nextComponent.getX() <= 0) {
							LeftTimer.this.stop();
						}
					}
				}
			});
		}


		public void setCurrentComponent(JComponent currentComponent) {
			this.currentComponent = currentComponent;
		}

		public void setNextComponent(JComponent nextComponent) {
			this.nextComponent = nextComponent;
		}

		@Override
		public void start() {
			currentComponent.setLocation(0, 0);
			nextComponent.setLocation(DEFAULT_WIDTH, 0);
			super.start();
		}
	}

}
