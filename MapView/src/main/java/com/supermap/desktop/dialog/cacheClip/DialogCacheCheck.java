package com.supermap.desktop.dialog.cacheClip;

import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.EngineType;
import com.supermap.data.processing.CacheWriter;
import com.supermap.desktop.Application;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.dialog.SmOptionPane;
import com.supermap.desktop.dialog.cacheClip.cache.CacheUtilities;
import com.supermap.desktop.dialog.cacheClip.cache.CheckCache;
import com.supermap.desktop.dialog.cacheClip.cache.LogWriter;
import com.supermap.desktop.dialog.cacheClip.cache.ProcessManager;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.ComponentBorderPanel.CompTitledPane;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.JFileChooserControl;
import com.supermap.desktop.ui.controls.ProviderLabel.WarningOrHelpProvider;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.utilities.FileLocker;
import com.supermap.desktop.utilities.FileUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by xie on 2017/5/10.
 * Dialog for checking cache
 */
public class DialogCacheCheck extends JFrame {
	private JLabel labelCachePath;
	//	private JLabel labelTotalSciPath;
	private JLabel labelCheckBounds;
	//	private JLabel labelSciPath;
	private JLabel labelProcessCount;
	//	private JLabel labelMergeSciCount;
//	private JFileChooserControl fileChooseTotalSciPath;
	private JFileChooserControl fileChooseCachePath;
	private JFileChooserControl fileChooseCheckBounds;
	//	private JFileChooserControl fileChooseSciPath;
	private JTextField textFieldProcessCount;
	//	private JTextField textFieldMergeSciCount;
//	private JRadioButton radioButtonSingleCheck;
	private JRadioButton radioButtonMultiCheck;
	private JCheckBox checkBoxSaveErrorData;
	private JCheckBox checkBoxCacheBuild;
	private WarningOrHelpProvider helpProviderForTotalSciPath;
	private WarningOrHelpProvider helpProviderForSciPath;
	private JButton buttonOK;
	private JButton buttonCancel;
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
		String taskPath = CacheUtilities.replacePath(fileChooseCachePath.getPath(), "CacheTask");
		File taskFile = new File(taskPath, "build");
		File doingFile = null;
		if (taskFile.exists()) {
			doingFile = new File(CacheUtilities.replacePath(taskPath, "checking"));
		}
		return null != doingFile && hasSciFiles(doingFile);
	}

	private void shutdownMapCheck() {
		SmOptionPane optionPane = new SmOptionPane();
		String taskPath = CacheUtilities.replacePath(fileChooseCachePath.getPath(), "CacheTask");
		taskPath = CacheUtilities.replacePath(taskPath, "build");
		if (optionPane.showConfirmDialogYesNo(MapViewProperties.getString("String_FinishClipTaskOrNot")) == JOptionPane.OK_OPTION) {
			ProcessManager.getInstance().removeAllProcess(taskPath, "checking");
			new SmOptionPane().showConfirmDialog(MessageFormat.format(MapViewProperties.getString("String_ProcessCheckFinished"), taskPath));
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			DialogCacheCheck.this.dispose();
			System.exit(1);
		} else {
			return;
		}
	}

	private ActionListener checkListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			run(e);
		}
	};
	private ChangeListener singleCheckListener = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
//			if (radioButtonSingleCheck.isSelected()) {
//				textFieldProcessCount.setText("0");
//				textFieldProcessCount.setEnabled(false);
//			} else {
			textFieldProcessCount.setEnabled(true);
//			}
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
//		this.radioButtonSingleCheck.addChangeListener(singleCheckListener);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (hasTask()) {
					setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					shutdownMapCheck();
				} else {
					DialogCacheCheck.this.dispose();
					System.exit(1);
				}
			}
		});
	}

	private void removeEvents() {
		this.buttonCancel.removeActionListener(this.cancelListener);
		this.buttonOK.removeActionListener(this.checkListener);
//		this.radioButtonSingleCheck.addChangeListener(singleCheckListener);
	}

	private void initComponents() {
//		this.labelTotalSciPath = new JLabel();
		this.labelCachePath = new JLabel();
		this.labelCheckBounds = new JLabel();
//		this.labelSciPath = new JLabel();
		this.labelProcessCount = new JLabel();
//		this.labelMergeSciCount = new JLabel();
		String moduleNameForCachePath = "ChooseCachePathForCheck";
		if (!SmFileChoose.isModuleExist(moduleNameForCachePath)) {
			SmFileChoose.addNewNode("", System.getProperty("user.dir"), GlobalParameters.getDesktopTitle(),
					moduleNameForCachePath, "GetDirectories");
		}
		SmFileChoose fileChooserForCachePath = new SmFileChoose(moduleNameForCachePath);
		this.fileChooseCachePath = new JFileChooserControl();
		this.fileChooseCachePath.setFileChooser(fileChooserForCachePath);
		String moduleNameForCheckBounds = "ChooseCheckBoundsFileForCheck";
		if (!SmFileChoose.isModuleExist(moduleNameForCheckBounds)) {
			String fileFilters = SmFileChoose.bulidFileFilters(SmFileChoose.createFileFilter(MapViewProperties.getString("String_FileType_GeoJson"), "geojson"));
			SmFileChoose.addNewNode(fileFilters, System.getProperty("user.dir"), GlobalParameters.getDesktopTitle(),
					moduleNameForCheckBounds, "OpenOne");
		}
		SmFileChoose fileChooserForCheckBounds = new SmFileChoose(moduleNameForCheckBounds);
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
		this.helpProviderForTotalSciPath = new WarningOrHelpProvider(MapViewProperties.getString("String_TipForCachePath"), false);
		this.helpProviderForSciPath = new WarningOrHelpProvider(MapViewProperties.getString("String_TipForFinishedSciPath"), false);
		this.setSize(520, 240);
		this.setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initResources() {
		this.setIconImages(CacheUtilities.getIconImages());
		this.labelCachePath.setText(MapViewProperties.getString("MapCache_LabelCachePath"));
		this.labelCheckBounds.setText(MapViewProperties.getString("String_CheckBounds"));
		this.labelProcessCount.setText(MapViewProperties.getString("String_NewProcessCount"));
//		this.labelMergeSciCount.setText(MapViewProperties.getString("String_MergeSciCount"));
		this.textFieldProcessCount.setText("3");
//		this.textFieldMergeSciCount.setText("1");
//		this.radioButtonSingleCheck.setText(MapViewProperties.getString("String_CacheCheck_Single"));
		this.radioButtonMultiCheck.setText(MapViewProperties.getString("String_CacheCheck_Multi"));
		this.checkBoxSaveErrorData.setText(MapViewProperties.getString("String_SaveErrorData"));
		this.checkBoxCacheBuild.setText(MapViewProperties.getString("String_ClipErrorCache"));
		this.setTitle(MapViewProperties.getString("String_CacheCheck"));
	}

	private void initLayout() {
		JPanel panelContent = (JPanel) this.getContentPane();
		panelContent.setLayout(new GridBagLayout());
		JPanel panelMultiCheck = new JPanel();
		panelMultiCheck.setLayout(new GridBagLayout());
		panelMultiCheck.add(this.labelProcessCount, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
		panelMultiCheck.add(this.textFieldProcessCount, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));
//		panelMultiCheck.add(this.labelMergeSciCount, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
//		panelMultiCheck.add(this.textFieldMergeSciCount, new GridBagConstraintsHelper(1, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));
		CompTitledPane compTitledPane = new CompTitledPane(this.radioButtonMultiCheck, panelMultiCheck);
		JPanel panelButton = new JPanel();
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(this.buttonOK, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(0, 0, 10, 5));
		panelButton.add(this.buttonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(0, 0, 10, 10));
		panelContent.add(this.labelCachePath, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 20, 5, 0));
		panelContent.add(this.fileChooseCachePath, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 0, 5, 10).setWeight(1, 0));
		panelContent.add(this.labelCheckBounds, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 20, 5, 0));
		panelContent.add(this.fileChooseCheckBounds, new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));
//		panelContent.add(this.radioButtonSingleCheck, new GridBagConstraintsHelper(0, 2, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 20, 0, 10));
		panelContent.add(compTitledPane, new GridBagConstraintsHelper(0, 2, 3, 3).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 10, 5, 10).setWeight(3, 0));
		panelContent.add(this.checkBoxSaveErrorData, new GridBagConstraintsHelper(0, 6, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 20, 0, 10));
		panelContent.add(this.checkBoxCacheBuild, new GridBagConstraintsHelper(2, 6, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 0, 10));
		panelContent.add(new JPanel(), new GridBagConstraintsHelper(0, 7, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		this.add(panelButton, new GridBagConstraintsHelper(0, 8, 3, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0));
	}

	private boolean validateValue(String sciPath, String processCount) {
		boolean result = true;
		SmOptionPane optionPane = new SmOptionPane();
		File sciDirectory = new File(sciPath);
		if (StringUtilities.isNullOrEmpty(sciPath) || !FileUtilities.isFilePath(sciPath) || !hasSciFiles(sciDirectory)) {
			optionPane.showConfirmDialog(MapViewProperties.getString("String_TaskNotExist"));
			return false;
		}
		if (StringUtilities.isNullOrEmpty(processCount) || !(StringUtilities.isInteger(processCount) || processCount.equals("0"))) {
			optionPane.showConfirmDialog(MapViewProperties.getString("String_CheckProcessCountError"));
			return false;
		}
		return result;
	}

	private boolean hasSciFiles(File sciDirectory) {
		int size = 0;
		if (null != sciDirectory.list(CacheUtilities.getFilter())) {
			size = sciDirectory.list(CacheUtilities.getFilter()).length;
		}
		return size > 0 ? true : false;
	}

	private void run(ActionEvent e) {
		try {
			String cachePath = fileChooseCachePath.getPath();
			File cache = new File(cachePath);
			String cacheRoot = null;
			if (cache.exists()) {
				File[] files = cache.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (!files[i].getName().contains("CacheTask")) {
						cacheRoot = files[i].getAbsolutePath();
						break;
					}
				}
			}
			if (null == cacheRoot) {
				new SmOptionPane().showErrorDialog("String_TaskNotExist");
				return;
			}
			String processCount = textFieldProcessCount.getText();
			String saveErrorData = checkBoxSaveErrorData.isSelected() ? "true" : "false";
			String geoJsonFile = fileChooseCheckBounds.getPath();
			String cacheTaskPath = CacheUtilities.replacePath(cachePath, "CacheTask");
			String taskPath = CacheUtilities.replacePath(cacheTaskPath, "build");
			geoJsonFile = CacheUtilities.replacePath(geoJsonFile);
			String[] params = {cacheRoot, taskPath, saveErrorData, geoJsonFile};

			if (validateValue(taskPath, processCount)) {
				String parentPath = null;
				double anchorLeft = -1;
				double anchorTop = -1;
				int tileSize = -1;
				String checkPath = CacheUtilities.replacePath(cacheTaskPath, "check");
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
				CheckCache.main(params);
//				CheckCache checkCache = new CheckCache();
//				checkCache.startProcess(Integer.valueOf(processCount), params);
				getResult(cachePath, cacheRoot, cacheTaskPath, taskPath, parentPath, anchorLeft, anchorTop, tileSize, datasourcePath);
			}
		} catch (Exception ex) {
			if (null != ex.getMessage()) {
				new SmOptionPane().showConfirmDialog(ex.getMessage());
			}
		}
	}

	private void getResult(final String cachePath, final String cacheRoot, final String cacheTaskPath, final String taskPath, final String parentPath,
	                       final double anchorLeft, final double anchorTop, final int tileSize, final String datasourcePath) throws InterruptedException {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						//Ensure all sci files has been checked
						String checkingPath = CacheUtilities.replacePath(cacheTaskPath, "checking");
						while (!FileUtilities.isDirEmpty(taskPath)) {
							Thread.sleep(2000);
						}
						while (!FileUtilities.isDirEmpty(checkingPath)) {
							//实时检查checking目录下的文件是否加锁,如果文件已经加锁则表示有进程正在使用,否则表示为以前进程挂了没有处理的
							File doingDirectory = new File(CacheUtilities.replacePath(cacheTaskPath, "checking"));
							if (doingDirectory.exists() && hasSciFiles(doingDirectory)) {
								File[] doingFailedSci = doingDirectory.listFiles();
								for (File doingSci : doingFailedSci) {
									//文件加了锁说明文件正在被用于检查任务
									FileLocker locker = new FileLocker(doingSci);
									if (locker.tryLock()) {
										//文件未加锁则判断该文件为上一次任务执行失败时遗留的任务,则将该任务移到build目录下,重新切图
										locker.release();
										doingSci.renameTo(new File(taskPath, doingSci.getName()));
									}
								}
							}
							Thread.sleep(2000);
						}

						if (null != parentPath && checkBoxSaveErrorData.isSelected()) {
							CheckCache.error2Udb(anchorLeft, anchorTop, tileSize, parentPath, datasourcePath);
						}
						DatasourceConnectionInfo info = new DatasourceConnectionInfo();
						info.setServer(datasourcePath);
						info.setAlias("check");
						info.setEngineType(EngineType.UDB);
						Application.getActiveApplication().getWorkspace().getDatasources().open(info);
						boolean cacheBuild = checkBoxCacheBuild.isSelected();
						if (cacheBuild) {
							File cacheFile = new File(cacheRoot);
							File errorFile = new File(CacheUtilities.replacePath(cacheTaskPath, "failed"));
							//有错误则重新切图
							if (null != errorFile && errorFile.list(CacheUtilities.getFilter()).length > 0 && tileSize != -1) {
								String[] tempParams = {"Multi", "null", cacheFile.getName(), cachePath};
								CacheUtilities.startProcess(tempParams, DialogCacheBuilder.class.getName(), LogWriter.BUILD_CACHE);
							} else {
								new SmOptionPane().showErrorDialog(MapViewProperties.getString("String_CacheCheckSuccess"));
							}
						}
						dialogDispose();
					} catch (Exception ex) {
						if (null != ex.getMessage()) {
							new SmOptionPane().showConfirmDialog(ex.getMessage());
						}
					}
				}
			}, "thread").start();
	}

	private void dialogDispose() {
		removeEvents();
		DialogCacheCheck.this.dispose();
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
