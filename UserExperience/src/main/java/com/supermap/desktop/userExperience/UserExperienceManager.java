package com.supermap.desktop.userExperience;

import com.supermap.desktop.Application;
import com.supermap.desktop.DesktopRuntimeManager;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.event.DesktopRuntimeEvent;
import com.supermap.desktop.event.DesktopRuntimeListener;
import com.supermap.desktop.icloud.LicenseManager;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.utilities.FileLocker;
import com.supermap.desktop.utilities.FileUtilities;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class UserExperienceManager {

	private static UserExperienceManager userExperienceManager;
	private DesktopRuntimeListener desktopRuntimeListener = new DesktopRuntimeListener() {
		@Override
		public void stateChanged(DesktopRuntimeEvent event) {
			desktopStateChanged(event);
		}
	};

	private static final String userExperienceDirectory = FileUtilities.getAppDataPath() + "UserExperience" + File.separator;
	private static final String executingFileName = "ExecutingFunctionInfo";
	private static final String defaultFileType = ".txt";

	private static final String userExperienceFullFileDirectory = userExperienceDirectory + "finished" + File.separator;

	private FileLocker executingFunctionFile;
	private ArrayList<File> fulledFiles = new ArrayList<>();

	private ArrayList<FunctionInfoCtrlAction> runningCtrlAction = new ArrayList<>();

	private Timer timer;

	private UserExperienceManager() {
		postExistFiles();
		executingFunctionFile = getDefaultFile();
		if (executingFunctionFile != null) {
			// TODO: 2017/7/11 添加本次启动记录
			initializeLicenseInfo();
			initializeLogsSendTimer();
		}
	}


	private void postExistFiles() {
		File parentFile = new File(userExperienceDirectory);
		if (parentFile.exists()) {
			if (!GlobalParameters.isLaunchUserExperiencePlan()) {
				return;
			}
			File[] listFiles = parentFile.listFiles();
			if (listFiles != null) {
				for (File file : listFiles) {
					// TODO: 2017/7/11  doPost
				}
			}
		} else {
			parentFile.mkdirs();
		}
	}

	private FileLocker getDefaultFile() {
		int i = 0;
		while (true) {
			File file = new File(userExperienceDirectory + executingFileName + (i == 0 ? "" : "_" + i) + defaultFileType);
			try {
				if (file.exists()) {
					FileLocker fileLocker = new FileLocker(file);
					if (fileLocker.tryLock() && fileLocker.getRandomAccessFile().length() == 0) {
						return fileLocker;
					}
				} else {
					if (file.createNewFile()) {
						FileLocker fileLocker = new FileLocker(file);
						if (fileLocker.tryLock()) {
							return fileLocker;
						}
					}
				}
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
				return null;
			}
			i++;
		}
	}

	private void initializeLicenseInfo() {
		LicenseInfo userLicenseInfo = new LicenseInfo(LicenseManager.getCurrentLicenseType());

	}

	private void initializeLogsSendTimer() {
		timer = new Timer(60 * 60 * 1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doPost();
			}
		});
		if (GlobalParameters.isLaunchUserExperiencePlan()) {
			timer.start();
		}
	}

	public void start() {
		if (executingFunctionFile != null) {
			DesktopRuntimeManager.getInstance().addRuntimeStateListener(desktopRuntimeListener);
		}
	}

	private void doPost() {
		// todo 发送数据到服务器
	}

	public void stop() {
		DesktopRuntimeManager.getInstance().removeRuntimeStateListener(desktopRuntimeListener);
	}

	private void desktopStateChanged(DesktopRuntimeEvent event) {
		if (!GlobalParameters.isLaunchUserExperiencePlan()) {
			if (timer.isRunning()) {
				timer.stop();
				doPost();
			}
			return;
		}
		if (!timer.isRunning()) {
			doPost();
			timer.start();
		}
		if (event.getCurrentObject() instanceof CtrlAction) {
			ctrlActionStateChanged(event);
		}
	}

	private void ctrlActionStateChanged(DesktopRuntimeEvent event) {
		switch (event.getType()) {
			case DesktopRuntimeEvent.START:
				addRunningCtrlAction(new FunctionInfoCtrlAction(event));
				break;
			case DesktopRuntimeEvent.CANCLE:
				// 暂不支持取消
				break;
			case DesktopRuntimeEvent.EXCEPTION:
				addDoneJson(new FunctionInfoCtrlAction(event).getJson());
				break;
			case DesktopRuntimeEvent.STOP:
				ctrlActionFinished(event);
				break;
		}
	}

	private void addRunningCtrlAction(FunctionInfoCtrlAction functionInfoCtrlAction) {
		runningCtrlAction.add(functionInfoCtrlAction);
		addDoingString(functionInfoCtrlAction.getJson());
	}

	private void ctrlActionFinished(DesktopRuntimeEvent event) {
		for (FunctionInfoCtrlAction functionInfoCtrlAction : runningCtrlAction) {
			if (functionInfoCtrlAction.getCtrlAction() == event.getCurrentObject()) {
				String json = functionInfoCtrlAction.getJson();
				functionInfoCtrlAction.finished();
				removeDoingJson(json);
				addDoneJson(functionInfoCtrlAction.getJson());
				break;
			}
		}
	}

	private void addDoneJson(String json) {
		// TODO: 2017/7/12
	}

	private void addDoingString(String json) {
		// TODO: 2017/7/12  加到doing文件中
	}

	private void removeDoingJson(String json) {
// TODO: 2017/7/12
	}

	public static UserExperienceManager getInstance() {
		if (userExperienceManager == null) {
			userExperienceManager = new UserExperienceManager();
		}
		return userExperienceManager;
	}
}
