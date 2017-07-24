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
import com.supermap.desktop.utilities.ThreadUtilties;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class UserExperienceManager {

	private static final Object lock = new Object();

	private static UserExperienceManager userExperienceManager;
	private DesktopRuntimeListener desktopRuntimeListener = new DesktopRuntimeListener() {
		@Override
		public void stateChanged(final DesktopRuntimeEvent event) {
			ThreadUtilties.execute(new Runnable() {
				@Override
				public void run() {
					desktopStateChanged(event);
				}
			});
		}
	};

	private static final String userExperienceDirectory = FileUtilities.getAppDataPath() + "UserExperience" + File.separator;
	private static final String executingFileName = "ExecutingFunctionInfo";
	private static final String defaultFileType = ".txt";

	private static final String userExperienceExecutedFileDirectory = userExperienceDirectory + "finished" + File.separator;
	private static final String executedFileName = "ExecutedFunctionInfo";

	private File executingFile = new File(userExperienceDirectory + executingFileName + defaultFileType);

	private FileLocker executedFunctionFile;
	private ArrayList<FileLocker> executedFiles = new ArrayList<>();

	private ArrayList<FunctionInfoCtrlAction> runningCtrlAction = new ArrayList<>();

	private static final long maxFileSize = 8 * 1024 * 1024 * 10;

	private Timer timer;

	private UserExperienceManager() {
		ThreadUtilties.execute(new Runnable() {
			@Override
			public void run() {
				postExistFiles();
				executedFunctionFile = getDefaultFile();
				if (executedFunctionFile != null) {
					initExceptionCtrlActions();
					initializeLicenseInfo();
					initializeLogsSendTimer();
					DesktopRuntimeManager.getInstance().addRuntimeStateListener(desktopRuntimeListener);
				}
			}
		});
	}

	private void initExceptionCtrlActions() {
		FileLocker fileLocker = new FileLocker(executingFile);
		try {
			if (fileLocker.tryLock()) {
				RandomAccessFile randomAccessFile = fileLocker.getRandomAccessFile();
				if (randomAccessFile.length() > 0) {
					randomAccessFile.seek(0);
					byte[] bytes = new byte[((int) randomAccessFile.length())];
					randomAccessFile.read(bytes);
					String value = new String(bytes, "UTF-8");
					String[] actions = value.split(System.getProperty("line.separator"));
					for (String action : actions) {
						try {
							FunctionInfoCtrlAction functionInfoCtrlAction = new FunctionInfoCtrlAction(action);
							addDoneJson(new UserExperienceBaseInfo(new DesktopUserExperienceInfo(functionInfoCtrlAction)).getJson());
						} catch (Exception e) {
							// ignore
						}
					}
				}
				randomAccessFile.setLength(0);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			fileLocker.release();
		}
	}


	private void postExistFiles() {
		File parentFile = new File(userExperienceExecutedFileDirectory);
		if (parentFile.exists()) {
			if (!GlobalParameters.isLaunchUserExperiencePlan()) {
				return;
			}
			File[] listFiles = parentFile.listFiles();
			if (listFiles != null) {
				for (File file : listFiles) {
					FileLocker fileLocker = new FileLocker(file);
					try {
						if (fileLocker.tryLock()) {
							boolean result = doPost(fileLocker);
							fileLocker.release();
							File lockFile = fileLocker.getLockFile();
							if (result) {
								lockFile.delete();
							}
						}
					} catch (Exception e) {
						fileLocker.release();
					}
				}
			}
		} else {
			parentFile.mkdirs();
		}
	}

	private boolean doPost(FileLocker fileLocker) {
		boolean result;
		synchronized (lock) {
			result = PostUserExperienceUtilties.postFile(fileLocker);
		}
		return result;

	}

	private FileLocker getDefaultFile() {
		if (!executingFile.exists()) {
			if (!executingFile.getParentFile().exists()) {
				executingFile.getParentFile().mkdirs();
			}
			try {
				executingFile.createNewFile();
			} catch (IOException e) {
				Application.getActiveApplication().getOutput().output(e);
			}
		}
		int i = 0;
		while (true) {
			File file = new File(userExperienceExecutedFileDirectory + executedFileName + (i == 0 ? "" : "_" + i) + defaultFileType);
			try {
				if (file.exists()) {
					FileLocker fileLocker = new FileLocker(file);
					if (fileLocker.tryLock()) {
						if (fileLocker.getRandomAccessFile().length() < maxFileSize) {
							return fileLocker;
						} else {
							fileLocker.release();
						}
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
		if (GlobalParameters.isLaunchUserExperiencePlan()) {
			LicenseInfo userLicenseInfo = new LicenseInfo(LicenseManager.getCurrentLicenseType());
			addDoneJson(new UserExperienceBaseInfo(new DesktopUserExperienceInfo(userLicenseInfo)).getJson());
		}
	}

	private void initializeLogsSendTimer() {
		timer = new Timer(2 * 60 * 1000, new ActionListener() {
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
	}

	private void doPost() {
		for (final FileLocker executedFile : executedFiles) {
			ThreadUtilties.execute(new Runnable() {
				@Override
				public void run() {
					doPost(executedFile);
					executedFile.release();
					executedFile.getLockFile().delete();
				}
			});
		}
		ThreadUtilties.execute(new Runnable() {
			@Override
			public void run() {
				doPost(executedFunctionFile);
				synchronized (lock) {
					try {
						executedFunctionFile.getRandomAccessFile().setLength(0);
					} catch (IOException e) {
						Application.getActiveApplication().getOutput().output(e);
					}
				}
			}
		});

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
				addDoneJson(new UserExperienceBaseInfo(new DesktopUserExperienceInfo(new FunctionInfoCtrlAction((Exception) event.getCurrentObject()))).getJson());
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
				runningCtrlAction.remove(functionInfoCtrlAction);
				String json = functionInfoCtrlAction.getJson();
				functionInfoCtrlAction.finished();
				removeDoingJson(json);
				addDoneJson(new UserExperienceBaseInfo(new DesktopUserExperienceInfo(functionInfoCtrlAction)).getJson());
				break;
			}
		}
	}

	private void addDoneJson(String json) {
		synchronized (lock) {
			try {
				RandomAccessFile randomAccessFile = executedFunctionFile.getRandomAccessFile();
				if (randomAccessFile.length() > 0) {
					randomAccessFile.seek(randomAccessFile.length());
				}
				randomAccessFile.write((json + System.getProperty("line.separator")).getBytes());
				if (randomAccessFile.length() > maxFileSize) {
					executedFiles.add(executedFunctionFile);
					executedFunctionFile = getDefaultFile();
				}
			} catch (IOException e) {
				Application.getActiveApplication().getOutput().output(e);
			}
		}
	}

	private void addDoingString(String json) {
		FileLocker fileLocker = new FileLocker(executingFile);
		try {
			while (!fileLocker.tryLock()) {
				Thread.sleep(1000);
			}
			if (fileLocker.getRandomAccessFile().length() > 0) {
				fileLocker.getRandomAccessFile().seek(fileLocker.getRandomAccessFile().length());
			}
			fileLocker.getRandomAccessFile().write((json + System.getProperty("line.separator")).getBytes());
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			fileLocker.release();
		}
	}


	private void removeDoingJson(String json) {
		FileLocker fileLocker = new FileLocker(executingFile);
		try {
			while (!fileLocker.tryLock()) {
				Thread.sleep(1000);
			}
			fileLocker.getRandomAccessFile().seek(0);
			byte[] bytes = new byte[(int) fileLocker.getRandomAccessFile().length()];
			fileLocker.getRandomAccessFile().read(bytes);
			String currentRows = new String(bytes, "UTF-8");
			currentRows = currentRows.replace(json + System.getProperty("line.separator"), "");
			fileLocker.getRandomAccessFile().setLength(0);
			fileLocker.getRandomAccessFile().write(currentRows.getBytes());
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			fileLocker.release();
		}
	}

	public static UserExperienceManager getInstance() {
		if (userExperienceManager == null) {
			userExperienceManager = new UserExperienceManager();
		}
		return userExperienceManager;
	}
}
