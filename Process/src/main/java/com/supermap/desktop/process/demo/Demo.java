package com.supermap.desktop.process.demo;

import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.ProcessResources;
import com.supermap.desktop.ui.FormBaseChild;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilities.PathUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.TimerTask;

/**
 * Created by yuanR on 2017/7/19.
 * 用户大会展台演示
 */
public class Demo extends FormBaseChild {

	private static final int DEFAULT_WIDTH = 1200;
	private static final int DEFAULT_HEIGHT = 500;

	private final java.util.Timer timer;
	private final MoveTimer moveTime;
	private static final int nextExecutionTime = 6000;

	public Demo() {
		this(ProcessProperties.getString("String_Demo"), null, null);
	}


	private String videoPath = PathUtilities.getFullPathName("../DemoVideo", true);

	private ArrayList<DemoParameterButton> videoButtons = new ArrayList<>();
	private ArrayList<NavigationButton> buttons = new ArrayList<>();
	private ArrayList<JLabel> currentPics;

	private JPanel panelCenter = new JPanel();

	private JButton buttonLeft = new JButton();
	private JButton buttonRight = new JButton();
	private int currentIndex = 0;
	private TimerTask timerTask;

	// 轮播按钮点击监听
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

	// 导航按钮监听
	// yuanR
	ActionListener buttonActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 当点击了其中一个导航按钮
			String name = ((NavigationButton) e.getSource()).getText();
			DemoParameterButton currentComponent = (DemoParameterButton) moveTime.getCurrentComponent();
			if (!name.equals(currentComponent.getName())) {
				int next = ((NavigationButton) e.getSource()).getID() - 1;

				moveTime.setCurrentComponent(videoButtons.get(currentIndex));
				moveTime.setNextComponent(videoButtons.get(((NavigationButton) e.getSource()).getID() - 1));
				currentIndex = next;
				moveTime.startLeft();
				resetTimerTime();

			}

		}
	};

	public Demo(String title, Icon icon, Component component) {
		super(title, icon, component);
		initParameter();
		initLayout();
		initListener();
		timer = new java.util.Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				moveLeft();
			}
		};

		moveTime = new MoveTimer();
		this.setBackground(Color.WHITE);
		if (videoButtons.size() > 0) {
			timer.schedule(timerTask, 1000, nextExecutionTime);
		}
	}

	private void moveLeft() {
		int next = currentIndex + 1;
		if (next >= videoButtons.size()) {
			next = 0;
		}
		moveTime.setCurrentComponent(videoButtons.get(currentIndex));
		moveTime.setNextComponent(videoButtons.get(next));
		currentIndex = next;
		moveTime.startLeft();
	}

	private void moveRight() {
		int next = currentIndex - 1;
		if (next < 0) {
			next = videoButtons.size() - 1;
		}
		moveTime.setCurrentComponent(videoButtons.get(currentIndex));
		moveTime.setNextComponent(videoButtons.get(next));
		currentIndex = next;
		moveTime.startRight();
	}


	private void initParameter() {
		try {
			File file = new File(videoPath);
			File[] files = file.listFiles();
			ArrayList<File> videoFiles = new ArrayList<>();
			if (file.exists() && files != null && files.length > 0) {
				for (File currentFile : files) {
					if (!currentFile.getName().endsWith(".png")) {
						videoFiles.add(currentFile);
					}
				}
			}
			int num = 1;
			for (File videoFile : videoFiles) {
				String basePath = videoFile.getAbsolutePath().split("\\.")[0];
				String smallPic = basePath + ".png";
//				String bigPic = basePath + "_big.png";
				if (new File(smallPic).exists()) {
					videoButtons.add(new DemoParameterButton(videoFile.getName().split("\\.")[0], new ImageIcon(smallPic), videoFile.getPath()));
					buttons.add(new NavigationButton(videoFile.getName().split("\\.")[0], num));
					num++;
//					if (new File(bigPic).exists()) {
//						currentPics.add(new JLabel(new ImageIcon(bigPic)));
//					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initLayout() {
		panelCenter.setLayout(null);
		panelCenter.setOpaque(false);
		Dimension buttonSize = new Dimension(44, 62);
		buttonLeft.setBounds(5, (DEFAULT_HEIGHT - buttonSize.height) / 2, buttonSize.width, buttonSize.height);
		buttonRight.setBounds(DEFAULT_WIDTH - buttonSize.width - 5, (DEFAULT_HEIGHT - buttonSize.height) / 2, buttonSize.width, buttonSize.height);
		panelCenter.add(buttonLeft);
		panelCenter.add(buttonRight);
		for (int i = 0; i < videoButtons.size(); i++) {
			if (i == 0) {
				videoButtons.get(i).setBounds(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
			} else {
				videoButtons.get(i).setBounds(DEFAULT_WIDTH, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
			}
			panelCenter.add(videoButtons.get(i));
		}
		panelCenter.setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		panelCenter.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));


		JPanel jPanelButton = new JPanel();
		jPanelButton.setOpaque(false);
		jPanelButton.setLayout(new GridBagLayout());

		for (int i = 0; i < videoButtons.size(); i++) {

//			jPanelButton.add(new JButton(), new GridBagConstraintsHelper(i, 0, 1, 1).setWeight(i == 0 || i == videoButtons.size() ? 1 : 0, 0)
//					.setAnchor(i == 0 ? GridBagConstraints.EAST : (i == videoButtons.size() ? GridBagConstraints.WEST : GridBagConstraints.CENTER)).setInsets(0, i == 0 ? 0 : 5, 0, 0));
			buttons.get(i).addActionListener(buttonActionListener);
			jPanelButton.add(buttons.get(i), new GridBagConstraintsHelper(i, 0, 1, 1));
		}

		buttonLeft.setIcon(ProcessResources.getIcon("/UserMeetingPhotos/moveLeft.png"));
		buttonRight.setIcon(ProcessResources.getIcon("/UserMeetingPhotos/moveRight.png"));

		this.setLayout(new GridBagLayout());
//		this.add(buttonLeft, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).setInsets(10, 0, 0, -100).setIpad(0, 100));
//		this.add(buttonRight, new GridBagConstraintsHelper(2, 0, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(10, -100, 0, 0).setIpad(0, 100));
		this.add(panelCenter, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER));
		this.add(jPanelButton, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.NORTH));


	}

	private void initListener() {
		for (DemoParameterButton videoButton : videoButtons) {
			videoButton.addActionListener(actionListener);
		}
		buttonLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveLeft();
				resetTimerTime();
			}
		});

		buttonRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveRight();
				resetTimerTime();
			}
		});
	}

	private void resetTimerTime() {
		Class<TimerTask> clazz = TimerTask.class;
		try {
			Field period = clazz.getDeclaredField("nextExecutionTime");
			period.setAccessible(true);
			period.set(timerTask, System.currentTimeMillis() + nextExecutionTime);
		} catch (Exception e) {
			// ignore
		}
	}

	/**
	 * 轮播台下方，导航按钮，主要用作指定内容的打开
	 * yuanR
	 */
	class NavigationButton extends JButton {
		int ID;
		public NavigationButton(String text, int ID) {
			super(text);
			this.ID = ID;
		}
		public Integer getID() {
			return ID;
		}
	}

	class MoveTimer extends Timer {

		public JComponent getCurrentComponent() {
			return currentComponent;
		}

		public JComponent currentComponent;

		public JComponent getNextComponent() {
			return nextComponent;
		}

		public JComponent nextComponent;
		private static final int DefaultStep = 10;
		private int step = DefaultStep;

		public MoveTimer() {
			super(1, null);
			this.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (currentComponent != null && nextComponent != null) {
						currentComponent.setLocation(currentComponent.getX() - step, 0);
						nextComponent.setLocation(nextComponent.getX() - step, 0);
						if (step > 0) {
							if (nextComponent.getX() <= 0) {
								MoveTimer.this.stop();
							}
						} else {
							if (nextComponent.getX() >= 0) {
								MoveTimer.this.stop();
							}
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

		public void startLeft() {
			step = DefaultStep;
			currentComponent.setLocation(0, 0);
			nextComponent.setLocation(DEFAULT_WIDTH, 0);
			start();
		}

		public void startRight() {
			step = -DefaultStep;
			currentComponent.setLocation(0, 0);
			nextComponent.setLocation(-DEFAULT_WIDTH, 0);
			start();
		}

		@Override
		public void start() {
			super.start();
		}
	}
}
