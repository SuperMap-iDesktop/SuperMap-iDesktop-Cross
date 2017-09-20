package com.supermap.desktop.dialog.cacheClip;

import com.supermap.data.processing.CacheWriter;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.dialog.cacheClip.cache.CacheUtilities;
import com.supermap.desktop.dialog.cacheClip.cache.CheckCache;
import com.supermap.desktop.dialog.cacheClip.cache.LogWriter;
import com.supermap.desktop.dialog.cacheClip.cache.ProcessManager;
import com.supermap.desktop.properties.CacheProperties;
import com.supermap.desktop.ui.controls.FileChooserPathChangedListener;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.JFileChooserControl;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by xie on 2017/5/10.
 * Dialog for checking cache
 */
public class DialogCacheCheck extends JFrame {
	private JLabel labelCachePath;
	private JLabel labelCheckBounds;
	private JLabel labelProcessCount;
	private JFileChooserControl fileChooseCachePath;
	private JFileChooserControl fileChooseCheckBounds;
	private JTextField textFieldProcessCount;
	private JRadioButton radioButtonMultiCheck;
	private JCheckBox checkBoxSaveErrorData;
	private JCheckBox checkBoxCacheBuild;
	private JLabel labelProgress;
	private JProgressBar progressBar;
	private JButton buttonOK;
	private JButton buttonCancel;
	private int totalSciCount;
	private String locale;
	private CacheProperties cacheProperties;
	private String cachePath;

	private ActionListener cancelListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (hasCheckingTask()) {
				shutdownMapCheck();
			} else {
				DialogCacheCheck.this.dispose();
			}
		}
	};
	private FileChooserPathChangedListener fileChangeListener = new FileChooserPathChangedListener() {
		@Override
		public void pathChanged() {
			cachePath = fileChooseCachePath.getPath();
		}
	};

	private boolean hasCheckingTask() {
		ArrayList<String> taskPaths = CacheUtilities.getTaskPath("build", cachePath);
		int checkingSciSize = 0;
		for (String taskPath : taskPaths) {
			File taskFile = new File(taskPath);
			File doingFile = new File(taskFile.getParent(), "checking");
			if (doingFile.exists() && null != doingFile.list()) {
				checkingSciSize += doingFile.list().length;
			}
		}
		return checkingSciSize != 0;
	}

	private void shutdownMapCheck() {
		ArrayList<String> taskFiles = CacheUtilities.getTaskPath("build", cachePath);
		if (CacheUtilities.showConfirmDialog(DialogCacheCheck.this, cacheProperties.getString("String_FinishCheckTaskOrNot")) == JOptionPane.OK_OPTION) {
			for (int i = 0, size = taskFiles.size(); i < size; i++) {
				ProcessManager.getInstance().removeAllProcess(taskFiles.get(i), "checking");
			}
			killProcess();
		} else {
			return;
		}
	}

	private ActionListener checkListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			run();
		}
	};

	public DialogCacheCheck(String locale) {
		this.locale = locale;
		init();
	}

	private void init() {
		initComponents();
		initResources();
		initLayout();
		registEvents();
	}

	private void registEvents() {
		removeEvents();
		this.buttonCancel.addActionListener(this.cancelListener);
		this.buttonOK.addActionListener(this.checkListener);
		this.fileChooseCachePath.addFileChangedListener(this.fileChangeListener);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (hasCheckingTask()) {
					setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					shutdownMapCheck();
				} else {
					killProcess();
				}
			}
		});
	}

	private void removeEvents() {
		this.buttonCancel.removeActionListener(this.cancelListener);
		this.buttonOK.removeActionListener(this.checkListener);
	}

	private void initComponents() {
		if ("null".equals(locale)) {
			cacheProperties = new CacheProperties();
		} else {
			cacheProperties = new CacheProperties(locale);
		}
		this.labelCachePath = new JLabel();
		this.labelCheckBounds = new JLabel();
		this.labelProcessCount = new JLabel();
		this.labelProgress = new JLabel();
		this.progressBar = new JProgressBar();
		this.progressBar.setStringPainted(true);
//		this.progressBar.setPreferredSize(new Dimension(200, 26));
		String moduleNameForCachePath = "ChooseCachePathForCheck";
		if (!SmFileChoose.isModuleExist(moduleNameForCachePath)) {
			SmFileChoose.addNewNode("", System.getProperty("user.dir"), GlobalParameters.getDesktopTitle(),
					moduleNameForCachePath, "GetDirectories");
		}
		SmFileChoose fileChooserForCachePath = new SmFileChoose(moduleNameForCachePath);
		fileChooserForCachePath.setOwer(DialogCacheCheck.this);
		this.fileChooseCachePath = new JFileChooserControl();
		this.fileChooseCachePath.setFileChooser(fileChooserForCachePath);
		String moduleNameForCheckBounds = "ChooseCheckBoundsFileForCheck";
		if (!SmFileChoose.isModuleExist(moduleNameForCheckBounds)) {
			String fileFilters = SmFileChoose.buildFileFilters(SmFileChoose.createFileFilter(cacheProperties.getString("String_FileType_GeoJson"), "geojson"));
			SmFileChoose.addNewNode(fileFilters, System.getProperty("user.dir"), GlobalParameters.getDesktopTitle(),
					moduleNameForCheckBounds, "OpenOne");
		}
		SmFileChoose fileChooserForCheckBounds = new SmFileChoose(moduleNameForCheckBounds);
		fileChooserForCheckBounds.setOwer(DialogCacheCheck.this);
		this.fileChooseCheckBounds = new JFileChooserControl();
		this.fileChooseCheckBounds.setFileChooser(fileChooserForCheckBounds);
		this.textFieldProcessCount = new JTextField();
		this.radioButtonMultiCheck = new JRadioButton();
		this.radioButtonMultiCheck.setSelected(true);
		this.checkBoxSaveErrorData = new JCheckBox();
		this.checkBoxSaveErrorData.setSelected(true);
		this.checkBoxCacheBuild = new JCheckBox();
		this.checkBoxCacheBuild.setSelected(true);
		this.buttonOK = ComponentFactory.createButtonOK();
		this.buttonCancel = ComponentFactory.createButtonCancel();
		this.setSize(520, 210);
		this.setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initResources() {
		this.setIconImages(CacheUtilities.getIconImages());
		this.labelCachePath.setText(cacheProperties.getString("MapCache_LabelCachePath"));
		this.labelCheckBounds.setText(cacheProperties.getString("String_CheckBounds"));
		this.labelProcessCount.setText(cacheProperties.getString("String_NewProcessCount"));
		this.textFieldProcessCount.setText("3");
		this.radioButtonMultiCheck.setText(cacheProperties.getString("String_CacheCheck_Multi"));
		this.checkBoxSaveErrorData.setText(cacheProperties.getString("String_SaveErrorData"));
		this.checkBoxCacheBuild.setText(cacheProperties.getString("String_ClipErrorCache"));
		this.setTitle(cacheProperties.getString("String_CacheCheck"));
		this.labelProgress.setText(cacheProperties.getString("String_ProgressControl_TotalProgress"));
		this.buttonOK.setText(cacheProperties.getString("String_Check"));
		this.buttonCancel.setText(cacheProperties.getString("String_Cancel"));
	}

	private void initLayout() {
		JPanel panelContent = (JPanel) this.getContentPane();
		panelContent.setLayout(new GridBagLayout());
		JPanel panelButton = new JPanel();
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(this.buttonOK, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(5, 0, 10, 5));
		panelButton.add(this.buttonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(5, 0, 10, 10));
		panelContent.add(this.labelCachePath, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 20, 5, 0));
		panelContent.add(this.fileChooseCachePath, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 0, 5, 10).setWeight(1, 0));
		panelContent.add(this.labelCheckBounds, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 20, 5, 0));
		panelContent.add(this.fileChooseCheckBounds, new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));
		panelContent.add(this.labelProcessCount, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 20, 5, 0));
		panelContent.add(this.textFieldProcessCount, new GridBagConstraintsHelper(1, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));

		panelContent.add(this.labelProgress, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 20, 0, 0));
		panelContent.add(this.progressBar, new GridBagConstraintsHelper(1, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));

		panelContent.add(this.checkBoxSaveErrorData, new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 20, 0, 10));
		panelContent.add(this.checkBoxCacheBuild, new GridBagConstraintsHelper(1, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 0, 10));
		panelContent.add(new JPanel(), new GridBagConstraintsHelper(0, 5, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setWeight(1, 1));

		panelContent.add(panelButton, new GridBagConstraintsHelper(0, 6, 3, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0));
	}

	private boolean validateValue(ArrayList<String> taskPaths, String processCount) {
		boolean result = true;
		if (StringUtilities.isNullOrEmpty(processCount) || !(StringUtilities.isInteger(processCount) || processCount.equals("0"))) {
			CacheUtilities.showMessageDialog(DialogCacheCheck.this, cacheProperties.getString("String_CheckProcessCountError"));
			return false;
		}

		if (hasCheckFailedScis()) {
			return false;
		}
		if (taskPaths.size() == 0 || CacheUtilities.getTaskSci(taskPaths) == 0) {
			//build下不存在sci,faild下存在则提示有失败的文件，如果用户点击是则重切

			int failedScis = CacheUtilities.getTaskSci(CacheUtilities.getTaskPath("failed", cachePath));
			if (failedScis > 0) {
				if (CacheUtilities.showConfirmDialog(DialogCacheCheck.this, MessageFormat.format(cacheProperties.getString("String_WarningForFailed"), failedScis)) == JOptionPane.OK_OPTION) {
					CacheUtilities.renameFailedFiles(taskPaths, "failed");
					buildCache();
					killProcess();
				} else {
					return false;
				}
			} else if (failedScis == 0) {
				//build下，failed下都没有则提示没有sci
				CacheUtilities.showMessageDialog(DialogCacheCheck.this, cacheProperties.getString("String_CheckTaskNotExist"));
				result = false;
			}
		}

		return result;
	}

	private boolean hasCheckFailedScis() {
		boolean result = false;
		ArrayList<String> checkFaileds = CacheUtilities.getTaskPath("checkFailed", cachePath);
		if (CacheUtilities.getTaskSci(checkFaileds) > 0) {
			if (JOptionPane.showConfirmDialog(DialogCacheCheck.this, cacheProperties.getString("String_WarningForMongoChecked"), "", JOptionPane.YES_NO_OPTION)
					== JOptionPane.OK_OPTION) {
				//有异常中断遗留的检查任务,提示是否重新检查
				CacheUtilities.renameFailedFiles(CacheUtilities.getTaskPath("build", cachePath), "checkFailed");
				result = true;
			} else {
				result = false;
			}
		}
		return result;
	}


	private void buildCache() {
		String[] tempParams = {"Multi", "zh-CN"};
		CacheUtilities.startProcess(tempParams, DialogCacheBuilder.class.getName(), LogWriter.BUILD_CACHE);
	}


	private void run() {
		try {
			String processCount = textFieldProcessCount.getText();
			ArrayList<String> taskPaths = CacheUtilities.getTaskPath("build", cachePath);
			//先移动cheching下的sci
			for (String taskPath : taskPaths) {
				CacheUtilities.renameDoingFile(CacheUtilities.replacePath(new File(taskPath).getParent(), "checking"), taskPath);
			}
			if (validateValue(taskPaths, processCount)) {
				checkCache(processCount);
			}
		} catch (Exception ex) {
			if (null != ex.getMessage()) {
				CacheUtilities.showMessageDialog(DialogCacheCheck.this, ex.getMessage());
			}
		}
	}

	private void checkCache(String processCount) throws InterruptedException {
		String saveErrorData = checkBoxSaveErrorData.isSelected() ? "true" : "false";
		String geoJsonFile = fileChooseCheckBounds.getPath();
		geoJsonFile = CacheUtilities.replacePath(geoJsonFile);
		String[] params = {CacheUtilities.getCachePath(fileChooseCachePath.getPath()), saveErrorData, geoJsonFile};
//		CheckCache.main(params);
		setTotalSciCount();
		buttonOK.setEnabled(false);
		CheckCache checkCache = new CheckCache();
		checkCache.startProcess(Integer.valueOf(processCount), params);
		getResult();
	}

	private void setTotalSciCount() {
		ArrayList<String> buildNames = new ArrayList<>();
		ArrayList<String> checkingNames = new ArrayList<>();
		ArrayList<String> checkedNames = new ArrayList<>();
		ArrayList<String> checkFailedNames = new ArrayList<>();
		ArrayList<String> failedNames = new ArrayList<>();
		ArrayList<String> buildPaths = CacheUtilities.getTaskPath("build", cachePath);
		for (String buildPath : buildPaths) {
			File buildFile = new File(buildPath);
			String parentStr = buildFile.getParent();
			File checkingdFile = new File(CacheUtilities.replacePath(parentStr, "checking"));
			File checkedFile = new File(CacheUtilities.replacePath(parentStr, "checked"));
			File checkFailedFile = new File(CacheUtilities.replacePath(parentStr, "checkFailed"));
			File FailedFile = new File(CacheUtilities.replacePath(parentStr, "failed"));
			CacheUtilities.addSciToArray(buildNames, buildFile);
			CacheUtilities.addSciToArray(checkingNames, checkingdFile);
			CacheUtilities.addSciToArray(checkedNames, checkedFile);
			CacheUtilities.addSciToArray(checkFailedNames, checkFailedFile);
			CacheUtilities.addSciToArray(failedNames, FailedFile);

		}
		totalSciCount += buildNames.size();
		totalSciCount += checkingNames.size();
		totalSciCount += checkedNames.size();
		totalSciCount += checkFailedNames.size();
		totalSciCount += failedNames.size();
	}


	private void getResult() throws InterruptedException {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (true) {
						refreshProgress();
						if (CacheUtilities.taskFinished(CacheUtilities.getTaskPath("build", cachePath))) {
							break;
						}
						//Sleep,then refresh progressBars
						TimeUnit.SECONDS.sleep(30);
					}
					if (checkBoxSaveErrorData.isSelected()) {
						try {
							String parentPath = null;
							double anchorLeft = -1;
							double anchorTop = -1;
							int tileSize = -1;
							String checkPath = CacheUtilities.replacePath(cachePath, "CacheTask");
							String datasourcePath = CacheUtilities.replacePath(CacheUtilities.replacePath(checkPath, "check"), "check.udb");
							File datasourceFile = new File(datasourcePath);
							if (!datasourceFile.exists()) {
								CacheUtilities.showMessageDialog(DialogCacheCheck.this, datasourcePath + " not exists!");
								return;
							}
							ArrayList<String> taskPaths = CacheUtilities.getTaskPath("task", cachePath);
							for (String taskPath : taskPaths) {
								ArrayList<String> sciNames = CheckCache.getSciFileList(taskPath);
								File scifile = new File(sciNames.get(0));
								parentPath = scifile.getParentFile().getParent();
								CacheWriter writer = new CacheWriter();
								writer.FromConfigFile(sciNames.get(0));
								anchorLeft = writer.getIndexBounds().getLeft();
								anchorTop = writer.getIndexBounds().getTop();
								tileSize = writer.getTileSize().value();
								CheckCache.error2Udb(anchorLeft, anchorTop, tileSize, parentPath, datasourcePath);
							}
						} catch (Exception e) {
							//hanyzh:写入UDB有可能抛异常，不要影响后面的执行--error2Udb试图两次打开UDB异常
						}
					}
					if (hasCheckFailedScis()) {
						//有异常中断的检查任务且用户要重新检查则重新检查
						progressBar.setValue(0);
						buttonOK.setEnabled(true);
					} else {
						if (checkBoxCacheBuild.isSelected()) {
							//是否重切错误瓦片
							reBuiledCache();
						}
					}
				} catch (Exception ex) {
					if (null != ex.getMessage()) {
						CacheUtilities.showMessageDialog(DialogCacheCheck.this, ex.getMessage());
					}
				}
			}
		}, "thread").start();
	}

	private void reBuiledCache() {
		int failedSciLength = CacheUtilities.getTaskSci(CacheUtilities.getTaskPath("failed", cachePath));
		if (failedSciLength > 0) {
			if (CacheUtilities.showConfirmDialog(DialogCacheCheck.this, MessageFormat.format(cacheProperties.getString("String_Process_message_Failed"), failedSciLength)) == JOptionPane.OK_OPTION) {
				CacheUtilities.renameFailedFiles(CacheUtilities.getTaskPath("task", cachePath), "failed");
				buildCache();
				killProcess();
				return;
			}
		} else {
			CacheUtilities.showMessageDialog(DialogCacheCheck.this, cacheProperties.getString("String_CacheCheckSuccess"));
		}
		killProcess();
	}


	private void refreshProgress() {
		int currentTotalCount = 0;
		ArrayList<String> buildSciNames = new ArrayList<>();
		ArrayList<String> checkingSciNames = new ArrayList<>();
		ArrayList<String> taskPaths = CacheUtilities.getTaskPath("task", cachePath);
		for (String taskPath : taskPaths) {
			File taksFile = new File(taskPath);
			String parentStr = taksFile.getParent();
			File buildFile = new File(CacheUtilities.replacePath(parentStr, "build"));
			File checkingFile = new File(CacheUtilities.replacePath(parentStr, "checking"));
			CacheUtilities.addSciToArray(buildSciNames, buildFile);
			CacheUtilities.addSciToArray(checkingSciNames, checkingFile);
		}
		currentTotalCount += buildSciNames.size();
		currentTotalCount += checkingSciNames.size();

		progressBar.setValue((int) ((totalSciCount - currentTotalCount + 0.0) / totalSciCount) * 100);
	}


	private void killProcess() {
		System.exit(1);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			DialogCacheCheck dialogCacheCheck = new DialogCacheCheck(args[0]);
			dialogCacheCheck.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
