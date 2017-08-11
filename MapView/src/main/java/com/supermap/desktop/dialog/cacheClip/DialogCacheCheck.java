package com.supermap.desktop.dialog.cacheClip;

import com.supermap.data.processing.CacheWriter;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.dialog.cacheClip.cache.CacheUtilities;
import com.supermap.desktop.dialog.cacheClip.cache.CheckCache;
import com.supermap.desktop.dialog.cacheClip.cache.LogWriter;
import com.supermap.desktop.dialog.cacheClip.cache.ProcessManager;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.JFileChooserControl;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.utilities.FileUtilities;
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

	private ActionListener cancelListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (hasTask()) {
				shutdownMapCheck();
			} else {
				DialogCacheCheck.this.dispose();
			}
		}
	};

	private boolean hasTask() {
		File doingFile = null;
		String buildPath = getTaskPath("build");
		if (null != buildPath) {
			File taskFile = new File(buildPath);
			if (taskFile.exists()) {
				doingFile = new File(getTaskPath("checking"));
			}
		}
		return null != doingFile && CacheUtilities.hasSciFiles(doingFile);
	}

	private void shutdownMapCheck() {
		String taskPath = getTaskPath("build");
		if (CacheUtilities.showConfirmDialog(DialogCacheCheck.this,MapViewProperties.getString("String_FinishClipTaskOrNot")) == JOptionPane.OK_OPTION) {
			ProcessManager.getInstance().removeAllProcess(taskPath, "checking");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

	public DialogCacheCheck() {
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
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (hasTask()) {
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
			String fileFilters = SmFileChoose.bulidFileFilters(SmFileChoose.createFileFilter(MapViewProperties.getString("String_FileType_GeoJson"), "geojson"));
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
		this.labelCachePath.setText(MapViewProperties.getString("MapCache_LabelCachePath"));
		this.labelCheckBounds.setText(MapViewProperties.getString("String_CheckBounds"));
		this.labelProcessCount.setText(MapViewProperties.getString("String_NewProcessCount"));
		this.textFieldProcessCount.setText("3");
		this.radioButtonMultiCheck.setText(MapViewProperties.getString("String_CacheCheck_Multi"));
		this.checkBoxSaveErrorData.setText(MapViewProperties.getString("String_SaveErrorData"));
		this.checkBoxCacheBuild.setText(MapViewProperties.getString("String_ClipErrorCache"));
		this.setTitle(MapViewProperties.getString("String_CacheCheck"));
		this.labelProgress.setText(ControlsProperties.getString("String_ProgressControl_TotalProgress"));
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

		this.add(panelButton, new GridBagConstraintsHelper(0, 6, 3, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0));
	}

	private boolean validateValue(String sciPath, String processCount) {
		boolean result = true;
		if (StringUtilities.isNullOrEmpty(processCount) || !(StringUtilities.isInteger(processCount) || processCount.equals("0"))) {
			CacheUtilities.showMessageDialog(DialogCacheCheck.this,MapViewProperties.getString("String_CheckProcessCountError"));
			return false;
		}
		File buildFile = new File(sciPath);
		File taskFile = new File(getTaskPath("task"));
		File failedDirectory = new File(getTaskPath("failed"));
		if (hasCheckFailedScis(buildFile)) {
			return false;
		}
		if (!buildFile.exists() || !CacheUtilities.hasSciFiles(buildFile)) {
			//build下不存在sci,faild下存在则提示有失败的文件，如果用户点击是则重切
			if (failedDirectory.exists() && CacheUtilities.hasSciFiles(failedDirectory)) {
				if (CacheUtilities.showConfirmDialog(DialogCacheCheck.this,MessageFormat.format(MapViewProperties.getString("String_WarningForFailed"), failedDirectory.list().length)) == JOptionPane.OK_OPTION) {
					File[] failedSci = failedDirectory.listFiles();
					for (int i = 0; i < failedSci.length; i++) {
						failedSci[i].renameTo(new File(taskFile, failedSci[i].getName()));
					}
					buildCache();
					killProcess();
				} else {
					return false;
				}
			} else if (!failedDirectory.exists() || !CacheUtilities.hasSciFiles(failedDirectory)) {
				//build下，failed下都没有则提示没有sci
				CacheUtilities.showMessageDialog(DialogCacheCheck.this,MapViewProperties.getString("String_CheckTaskNotExist"));
				result = false;
			}
		}

		return result;
	}

	private boolean hasCheckFailedScis(File buildFile) {
		boolean result = false;
		File checkFailedDirectory = new File(getTaskPath("checkFailed"));
		if (checkFailedDirectory.exists() && CacheUtilities.hasSciFiles(checkFailedDirectory)) {
			if (JOptionPane.showConfirmDialog(DialogCacheCheck.this, MapViewProperties.getString("String_WarningForMongoChecked"), "", JOptionPane.YES_NO_OPTION)
					== JOptionPane.OK_OPTION) {
				//有异常中断遗留的检查任务,提示是否重新检查
				File[] checkFailed = checkFailedDirectory.listFiles();
				for (int i = 0; i < checkFailed.length; i++) {
					checkFailed[i].renameTo(new File(buildFile, checkFailed[i].getName()));
				}
				result = true;
			} else {
				result = false;
			}
		}
		return result;
	}

	private void buildCache() {
		File cacheFile = new File(fileChooseCachePath.getPath());
		String[] tempParams = {"Multi", "null", cacheFile.getName(), fileChooseCachePath.getPath()};
		CacheUtilities.startProcess(tempParams, DialogCacheBuilder.class.getName(), LogWriter.BUILD_CACHE);
	}


	private void run() {
		try {
			String processCount = textFieldProcessCount.getText();
			String taskPath = getTaskPath("build");
			//先移动cheching下的sci
			CacheUtilities.renameDoingFile(getTaskPath("checking"), taskPath);
			if (validateValue(taskPath, processCount)) {
				checkCache(processCount, taskPath);
			}
		} catch (Exception ex) {
			if (null != ex.getMessage()) {
				CacheUtilities.showMessageDialog(DialogCacheCheck.this,ex.getMessage());
			}
		}
	}

	private void checkCache(String processCount, String taskPath) throws InterruptedException {
		String saveErrorData = checkBoxSaveErrorData.isSelected() ? "true" : "false";
		String geoJsonFile = fileChooseCheckBounds.getPath();
		geoJsonFile = CacheUtilities.replacePath(geoJsonFile);
		String[] params = {CacheUtilities.getCachePath(fileChooseCachePath.getPath()), taskPath, saveErrorData, geoJsonFile};
		String parentPath = null;
		double anchorLeft = -1;
		double anchorTop = -1;
		int tileSize = -1;
		String checkPath = getTaskPath("check");
		String datasourcePath = CacheUtilities.replacePath(checkPath, "check.udb");
		if (checkBoxSaveErrorData.isSelected()) {
			ArrayList<String> sciNames = CheckCache.getSciFileList(taskPath);
			File scifile = new File(sciNames.get(0));
			parentPath = scifile.getParentFile().getParent();

			File datasourceFile = new File(datasourcePath);
			if (!datasourceFile.exists()) {
				System.out.println(datasourcePath + " not exists!");
				return;
			}
			CacheWriter writer = new CacheWriter();
			writer.FromConfigFile(sciNames.get(0));
			anchorLeft = writer.getIndexBounds().getLeft();
			anchorTop = writer.getIndexBounds().getTop();
			tileSize = writer.getTileSize().value();
		}
//				CheckCache.main(params);
		setTotalSciCount();
		buttonOK.setEnabled(false);
		CheckCache checkCache = new CheckCache();
		checkCache.startProcess(Integer.valueOf(processCount), params);
		getResult(parentPath, anchorLeft, anchorTop, tileSize, datasourcePath);
	}

	private void setTotalSciCount() {
		String[] buildNames = null;
		String[] checkingNames = null;
		String[] checkedNames = null;
		String[] checkFailedNames = null;
		String[] failedNames = null;
		File buildFile = new File(getTaskPath("build"));
		if (buildFile.exists()) {
			buildNames = buildFile.list();
		}
		File checkingFile = new File(getTaskPath("checking"));
		if (checkingFile.exists()) {
			checkingNames = checkingFile.list();
		}
		File checkedFile = new File(getTaskPath("checked"));
		if (checkedFile.exists()) {
			checkedNames = checkedFile.list();
		}
		File checkFailedFile = new File(getTaskPath("checkFailed"));
		if (checkFailedFile.exists()) {
			checkFailedNames = checkFailedFile.list();
		}
		File failedFile = new File(getTaskPath("failed"));
		if (failedFile.exists()) {
			failedNames = failedFile.list();
		}
		totalSciCount = buildNames == null ? 0 : buildNames.length;
		totalSciCount += checkingNames == null ? 0 : checkingNames.length;
		totalSciCount += checkedNames == null ? 0 : checkedNames.length;
		totalSciCount += checkFailedNames == null ? 0 : checkFailedNames.length;
		totalSciCount += failedNames == null ? 0 : failedNames.length;
	}

	private String getTaskPath(String childPath) {
		//获取缓存任务根路径
		String cacheTask = CacheUtilities.replacePath(fileChooseCachePath.getPath(), DialogMapCacheClipBuilder.CacheTask);
		String result = CacheUtilities.replacePath(cacheTask, childPath);
		return result;
	}

	private void getResult(final String parentPath,
	                       final double anchorLeft, final double anchorTop, final int tileSize, final String datasourcePath) throws InterruptedException {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (true) {
						refreshProgress();
						if (taskFinished()) {
							break;
						}
						//Sleep,then refresh progressBars
						TimeUnit.SECONDS.sleep(30);
					}
					if (null != parentPath && checkBoxSaveErrorData.isSelected()) {
						try {
							CheckCache.error2Udb(anchorLeft, anchorTop, tileSize, parentPath, datasourcePath);
						} catch (Exception e) {
							//hanyzh:写入UDB有可能抛异常，不要影响后面的执行--error2Udb试图两次打开UDB异常
						}
					}
					File builedFile = new File(getTaskPath("build"));
					if (hasCheckFailedScis(builedFile)) {
						//有异常中断的检查任务且用户要重新检查则重新检查
						progressBar.setValue(0);
						buttonOK.setEnabled(true);
					} else {
						if (checkBoxCacheBuild.isSelected()) {
							//是否重切错误瓦片
							reBuiledCache(tileSize);
						}
					}
				} catch (Exception ex) {
					if (null != ex.getMessage()) {
						CacheUtilities.showMessageDialog(DialogCacheCheck.this,ex.getMessage());
					}
				}
			}
		}, "thread").start();
	}

	private void reBuiledCache(int tileSize) {
		File errorFile = new File(getTaskPath("failed"));
		File taskFile = new File(getTaskPath("task"));
		if (errorFile.exists() && null != errorFile.list() && errorFile.list().length > 0 && tileSize != -1) {
			if (CacheUtilities.showConfirmDialog(DialogCacheCheck.this,MessageFormat.format(MapViewProperties.getString("String_Process_message_Failed"), errorFile.list().length, errorFile.getPath()))
					== JOptionPane.OK_OPTION) {
				File[] failedSci = errorFile.listFiles();
				for (int i = 0; i < failedSci.length; i++) {
					failedSci[i].renameTo(new File(taskFile, failedSci[i].getName()));
				}
				//有检查错误的则重新切图
				buildCache();
			}
		} else {
			CacheUtilities.showMessageDialog(DialogCacheCheck.this,MapViewProperties.getString("String_CacheCheckSuccess"));
		}
	}

	private boolean taskFinished() {
		return FileUtilities.isDirEmpty(getTaskPath("build")) && FileUtilities.isDirEmpty(getTaskPath("checking"));
	}

	private void refreshProgress() {
		int currentCount = 0;
		File buildFile = new File(getTaskPath("build"));
		//Ensure that component,count array have sorted as we want;
		File checkingFile = new File(getTaskPath("checking"));
		String[] buildSciNames = null;
		String[] checkingSci = null;
		if (buildFile.exists() && null != buildFile.list()) {
			buildSciNames = buildFile.list();
			currentCount = buildSciNames.length;
		}
		if (checkingFile.exists() && null != checkingFile.list()) {
			checkingSci = checkingFile.list();
			currentCount += checkingSci.length;
		}
		progressBar.setValue((int) ((totalSciCount - currentCount + 0.0) / totalSciCount) * 100);
	}


	private void killProcess() {
		System.exit(1);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			DialogCacheCheck dialogCacheCheck = new DialogCacheCheck();
			dialogCacheCheck.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
