package com.supermap.desktop.process.demo;

import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.ProcessResources;
import com.supermap.desktop.ui.FormBaseChild;
import com.supermap.desktop.utilities.PathUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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


	private static final int DEFAULT_DEMO_WIDTH = 1440;
	private static final int DEFAULT_DEMO_HEIGHT = 800;
	private static final int DEFAULT_BIGICON_WIDTH = 1440;
	private static final int DEFAULT_BIGICON_HEIGHT = 625;
	private static final int DEFAULT_SMALLICON_WIDTH = 200;
	private static final int DEFAULT_SMALLICON_HEIGHT = 140;
	private static final int DEFAULT_STEP = 45;
	private static final int NEXT_EXECUTION_TIME = 6000;

	private final java.util.Timer timer;
	private final MoveTimer moveTime;

	public Demo() {
		this(ProcessProperties.getString("String_Demo"), null, null);
	}


	private String videoPath = PathUtilities.getFullPathName("../DemoVideo", true);

	private ArrayList<DemoParameterButton> videoButtons = new ArrayList<>();
	private ArrayList<DemoParameterButton> buttons = new ArrayList<>();

	private JPanel mainPanel = new JPanel();
	private JPanel panelCenter = new JPanel();

	private JButton buttonLeft = new JButton();
	private JButton buttonRight = new JButton();
	private int currentIndex = 0;
	private TimerTask timerTask;

	/**
	 * 播放视频监听事件
	 */
	private ActionListener actionListener = new ActionListener() {
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

	/**
	 * 左右按钮点击监听事件
	 */
	private ActionListener moveButtonActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 重置自动轮播时间
			resetTimerTime();
			removeListener();
			if (videoButtons.size() > 0) {
				// 防止自动轮播被卡停
				if (moveTime.getCurrentComponent().getLocation().getX() < 0.0) {
					System.out.println(moveTime.getCurrentComponent().getLocation().getX());
					moveTime.getCurrentComponent().setLocation(-DEFAULT_DEMO_WIDTH, 0);
				}
				moveTime.setDefaultStep(DEFAULT_STEP);
				if (e.getSource().equals(buttonRight)) {
					int next = currentIndex + 1;
					if (next >= videoButtons.size()) {
						next = 0;
					}
					moveLeft(next);
				} else if (e.getSource().equals(buttonLeft)) {
					int next = currentIndex - 1;
					if (next < 0) {
						next = videoButtons.size() - 1;
					}
					moveRight(next);
				}
			}
		}
	};

	MouseListener mouseExitedListener = new MouseAdapter() {
		@Override
		public void mouseExited(MouseEvent e) {
			((DemoParameterButton) e.getSource()).setContentAreaFilled(false);
		}
	};
	MouseListener mouseEntered = new MouseAdapter() {
		@Override
		public void mouseEntered(MouseEvent e) {
			resetTimerTime();
			String name = ((DemoParameterButton) e.getSource()).getText();
			int next = -1;
			String videoName;
			for (int i = 0; i < videoButtons.size(); i++) {
				videoName = videoButtons.get(i).getName();
				if (videoName.equals(name)) {
					next = i;
					break;
				}
			}
			if (next != -1) {
				for (int i = 0; i < videoButtons.size(); i++) {
					if (i != next) {
						videoButtons.get(i).setLocation(-DEFAULT_DEMO_WIDTH, 0);
					} else {
						videoButtons.get(i).setLocation(0, 0);
					}
				}
			}
			clearButtonAreaFill(next);
		}
	};

	public Demo(String title, Icon icon, Component component) {
		super(title, icon, component);
		initParameter();
		initLayout();
		initListener();
		moveTime = new MoveTimer();
		timer = new java.util.Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				int next = currentIndex + 1;
				if (next >= videoButtons.size()) {
					next = 0;
				}
				moveTime.setDefaultStep(DEFAULT_STEP / 5);
				moveLeft(next);
			}
		};

		if (videoButtons.size() > 0) {
			timer.schedule(timerTask, 1000, NEXT_EXECUTION_TIME);
		}
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
			for (File videoFile : videoFiles) {
				String basePath = videoFile.getAbsolutePath().split("\\.")[0];
				String bigPic = basePath + ".png";
				String smallPic = basePath + "small.png";
//				String bigPic = basePath + "_big.png";
				if (new File(bigPic).exists()) {
					// 往button中添加图片之前先设置其大小
					ImageIcon icon = new ImageIcon(bigPic);
					icon = new ImageIcon(icon.getImage().getScaledInstance(DEFAULT_BIGICON_WIDTH, DEFAULT_BIGICON_HEIGHT, Image.SCALE_DEFAULT));
					String name = videoFile.getName().split("\\.")[0];
					videoButtons.add(new DemoParameterButton(icon, videoFile.getPath(), name));
				}
				if (new File(smallPic).exists()) {
					ImageIcon icon = new ImageIcon(smallPic);
					icon = new ImageIcon(icon.getImage().getScaledInstance(DEFAULT_SMALLICON_WIDTH, DEFAULT_SMALLICON_HEIGHT - 25, Image.SCALE_DEFAULT));
					String name = videoFile.getName().split("\\.")[0];
					buttons.add(new DemoParameterButton(name, icon, videoFile.getPath()));
				}
			}
			buttonLeft.setIcon(ProcessResources.getIcon("/UserMeetingPhotos/moveLeft.png"));
			buttonRight.setIcon(ProcessResources.getIcon("/UserMeetingPhotos/moveRight.png"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initLayout() {
		// 轮播区域
		panelCenter.setLayout(null);
		panelCenter.setOpaque(false);
		Dimension buttonSize = new Dimension(44, 62);
		buttonLeft.setBounds(0, (DEFAULT_BIGICON_HEIGHT - buttonSize.height) / 2, buttonSize.width, buttonSize.height);
		buttonRight.setBounds(DEFAULT_BIGICON_WIDTH - buttonSize.width, (DEFAULT_BIGICON_HEIGHT - buttonSize.height) / 2, buttonSize.width, buttonSize.height);
		panelCenter.add(buttonLeft);
		panelCenter.add(buttonRight);
		for (int i = 0; i < videoButtons.size(); i++) {
			if (i == 0) {
				videoButtons.get(i).setBounds(0, 0, DEFAULT_BIGICON_WIDTH, DEFAULT_BIGICON_HEIGHT);
			} else {
				videoButtons.get(i).setBounds(DEFAULT_BIGICON_WIDTH, 0, DEFAULT_BIGICON_WIDTH, DEFAULT_BIGICON_HEIGHT);
			}
			panelCenter.add(videoButtons.get(i));
		}
		panelCenter.setMinimumSize(new Dimension(DEFAULT_BIGICON_WIDTH, DEFAULT_BIGICON_HEIGHT));
		panelCenter.setMaximumSize(new Dimension(DEFAULT_BIGICON_WIDTH, DEFAULT_BIGICON_HEIGHT));
		panelCenter.setPreferredSize(new Dimension(DEFAULT_BIGICON_WIDTH, DEFAULT_BIGICON_HEIGHT));

		// 导航区域
		JPanel jPanelButton = new JPanel();
		jPanelButton.setOpaque(false);
		jPanelButton.setLayout(new FlowLayout());
		for (int i = 0; i < buttons.size(); i++) {
			Dimension smallButtonSize = new Dimension(DEFAULT_SMALLICON_WIDTH + 5, DEFAULT_SMALLICON_HEIGHT);
			buttons.get(i).setPreferredSize(smallButtonSize);
			buttons.get(i).setMaximumSize(smallButtonSize);
			buttons.get(i).setMinimumSize(smallButtonSize);
			jPanelButton.add(buttons.get(i));
		}

		Dimension mainPanelSize = new Dimension(DEFAULT_DEMO_WIDTH, DEFAULT_DEMO_HEIGHT);
		mainPanel.setPreferredSize(mainPanelSize);
		mainPanel.setMinimumSize(mainPanelSize);
		mainPanel.setMaximumSize(mainPanelSize);
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(panelCenter, BorderLayout.CENTER);
		mainPanel.add(jPanelButton, BorderLayout.SOUTH);

		this.add(mainPanel);
		this.setBackground(Color.WHITE);
	}


	private void initListener() {
		for (DemoParameterButton videoButton : videoButtons) {
			videoButton.addActionListener(actionListener);
		}
		for (DemoParameterButton button : buttons) {
			button.addActionListener(actionListener);
			button.addMouseListener(mouseEntered);
			// 把鼠标移出事件单独添加监听，确保每次都奏效
			button.addMouseListener(mouseExitedListener);
		}
		buttonLeft.addActionListener(moveButtonActionListener);
		buttonRight.addActionListener(moveButtonActionListener);
	}

	private void moveLeft(int next) {
		moveTime.setCurrentComponent(videoButtons.get(currentIndex));
		moveTime.setNextComponent(videoButtons.get(next));
		currentIndex = next;
		moveTime.startLeft();
	}

	private void moveRight(int next) {
		moveTime.setCurrentComponent(videoButtons.get(currentIndex));
		moveTime.setNextComponent(videoButtons.get(next));
		currentIndex = next;
		moveTime.startRight();
	}

	/**
	 * 去除所有导航按钮的选中渲染
	 */
	private void clearButtonAreaFill(int currentIndex) {
		for (DemoParameterButton button : buttons) {
			button.setContentAreaFilled(false);
			button.setSelected(false);
		}
		buttons.get(currentIndex).setContentAreaFilled(true);
		buttons.get(currentIndex).setSelected(true);
	}

	private void reLoadListener() {
		removeListener();
		buttonRight.addActionListener(moveButtonActionListener);
		buttonLeft.addActionListener(moveButtonActionListener);
	}

	private void removeListener() {
		buttonRight.removeActionListener(moveButtonActionListener);
		buttonLeft.removeActionListener(moveButtonActionListener);
	}

	private void resetTimerTime() {
		Class<TimerTask> clazz = TimerTask.class;
		try {
			Field period = clazz.getDeclaredField("nextExecutionTime");
			period.setAccessible(true);
			period.set(timerTask, System.currentTimeMillis() + NEXT_EXECUTION_TIME);
		} catch (Exception e) {
			// ignore
		}
	}

	class MoveTimer extends Timer {

		public JComponent getCurrentComponent() {
			return currentComponent;
		}

		public JComponent getNextComponent() {
			return nextComponent;
		}

		public JComponent currentComponent;


		public JComponent nextComponent;

		public void setDefaultStep(int step) {
			this.defaultStep = step;
		}

		private int defaultStep = DEFAULT_STEP;
		private int step = DEFAULT_STEP;

		public MoveTimer() {
			super(10, null);
			this.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (currentComponent != null && nextComponent != null) {
						currentComponent.setLocation(currentComponent.getX() - step, 0);
						nextComponent.setLocation(nextComponent.getX() - step, 0);
						if (step > 0) {
							if (nextComponent.getX() <= 0) {
								MoveTimer.this.stop();
								reLoadListener();
							}
						} else {
							if (nextComponent.getX() >= 0) {
								MoveTimer.this.stop();
								reLoadListener();
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
			step = defaultStep;
			moveTime.getCurrentComponent().setLocation(0, 0);
			moveTime.getNextComponent().setLocation(DEFAULT_BIGICON_WIDTH, 0);
			start();
		}

		public void startRight() {
			step = -defaultStep;
			currentComponent.setLocation(0, 0);
			nextComponent.setLocation(-DEFAULT_BIGICON_WIDTH, 0);
			start();
		}

		@Override
		public void start() {
			super.start();
			//设置导航按钮的选中渲染跟随
			clearButtonAreaFill(currentIndex);
		}
	}
}
