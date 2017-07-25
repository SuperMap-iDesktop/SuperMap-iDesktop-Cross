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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
	private static final int DEFAULT_SMALL_WIDTH = 200;
	private static final int DEFAULT_SMALL_HEIGHT = 140;

	private final java.util.Timer timer;
	private final MoveTimer moveTime;
	private static final int nextExecutionTime = 6000;

	public Demo() {
		this(ProcessProperties.getString("String_Demo"), null, null);
	}


	private String videoPath = PathUtilities.getFullPathName("../DemoVideo", true);

	private ArrayList<DemoParameterButton> videoButtons = new ArrayList<>();
	private ArrayList<DemoParameterButton> buttons = new ArrayList<>();
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

	/**
	 * 左右按钮点击监听事件
	 */
	private ActionListener moveButtonActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			removeListener();
			if (e.getSource().equals(buttonRight)) {
				int next = currentIndex + 1;
				if (next >= videoButtons.size()) {
					next = 0;
				}
				moveTime.setDefaultStep(30);
				moveLeft(next);
				resetTimerTime();
			} else if (e.getSource().equals(buttonLeft)) {
				int next = currentIndex - 1;
				if (next < 0) {
					next = videoButtons.size() - 1;
				}
				moveTime.setDefaultStep(30);
				moveRight(next);
				resetTimerTime();
			}
		}
	};


	// 导航按钮监听
	MouseListener navigatorButtonMouseListener = new MouseListener() {


		@Override
		public void mouseExited(MouseEvent e) {
			((DemoParameterButton) e.getSource()).setContentAreaFilled(false);
//			for (DemoParameterButton button : buttons) {
//				button.setContentAreaFilled(false);
//			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			removeListener();
			// 当鼠标进入了其中一个导航按钮

			try {
				String name = ((DemoParameterButton) e.getSource()).getText();
//				DemoParameterButton currentvideoButtons = videoButtons.get(currentIndex);
//				if (!name.equals(currentvideoButtons.getText())) {
				int next = -1;
				String videoName;
				for (int i = 0; i < videoButtons.size(); i++) {
					videoName = videoButtons.get(i).getText();
					if (videoName.equals(name)) {
						next = i;
						break;
					}
				}
				if (next != -1) {
					moveTime.setDefaultStep(30);
					moveLeft(next);
					resetTimerTime();
				}
//				}
			} catch (Exception e1) {

			} finally {
				((DemoParameterButton) e.getSource()).setContentAreaFilled(true);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {

		}

		@Override
		public void mouseClicked(MouseEvent e) {
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
		moveTime = new MoveTimer();
		timer = new java.util.Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				int next = currentIndex + 1;
				if (next >= videoButtons.size()) {
					next = 0;
				}
				moveTime.setDefaultStep(1);
				moveLeft(next);
			}
		};

		this.setBackground(Color.WHITE);
		if (videoButtons.size() > 0) {
			timer.schedule(timerTask, 1000, nextExecutionTime);
		}
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
					videoButtons.add(new DemoParameterButton(videoFile.getName().split("\\.")[0], new ImageIcon(bigPic), videoFile.getPath()));
				}
				if (new File(smallPic).exists()) {
					String text = videoFile.getName().split("\\.")[0];
					buttons.add(new DemoParameterButton(text, new ImageIcon(smallPic), videoFile.getPath()));
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
			Dimension smallButtonSize = new Dimension(DEFAULT_SMALL_WIDTH, DEFAULT_SMALL_HEIGHT);

			buttons.get(i).setPreferredSize(smallButtonSize);
			buttons.get(i).setMaximumSize(smallButtonSize);
			buttons.get(i).setMinimumSize(smallButtonSize);
			jPanelButton.add(buttons.get(i), new GridBagConstraintsHelper(i, 0, 1, 1).setInsets(0, i == 0 ? 0 : 10, 0, 0));
			buttons.get(i).addMouseListener(navigatorButtonMouseListener);
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
		buttonLeft.addActionListener(moveButtonActionListener);
		buttonRight.addActionListener(moveButtonActionListener);
	}


	private void reLoadListener() {
		removeListener();
		for (DemoParameterButton button : buttons) {
			button.addMouseListener(navigatorButtonMouseListener);
		}
		buttonRight.addActionListener(moveButtonActionListener);
		buttonLeft.addActionListener(moveButtonActionListener);
	}

	private void removeListener() {
		for (DemoParameterButton button : buttons) {
			button.removeMouseListener(navigatorButtonMouseListener);
		}
		buttonRight.removeActionListener(moveButtonActionListener);
		buttonLeft.removeActionListener(moveButtonActionListener);
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
			this.DefaultStep = step;
		}

		private int DefaultStep = 100;

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
			step = DefaultStep;
			moveTime.getCurrentComponent().setLocation(0, 0);
			moveTime.getNextComponent().setLocation(DEFAULT_WIDTH, 0);
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
