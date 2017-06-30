package com.supermap.desktop.dialog.cacheClip;

import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.processing.MapCacheBuilder;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.dialog.SmOptionPane;
import com.supermap.desktop.dialog.cacheClip.cache.BuildCache;
import com.supermap.desktop.dialog.cacheClip.cache.CacheUtilities;
import com.supermap.desktop.dialog.cacheClip.cache.ProcessManager;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.ProviderLabel.WarningOrHelpProvider;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.FileUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by xie on 2017/5/3.
 * Multi cache builder dialog
 */
public class DialogCacheBuilder extends SmDialog {
	private JLabel labelTotalTaskPath;
	private JLabel labelTaskPath;
	private JLabel labelWorkspacePath;
	private JLabel labelMapName;
	private JLabel labelProcessCount;
	private JLabel labelCachePath;
	//	private JLabel labelTotalProcessCount;
	private JLabel labelTotalProgress;
	private JLabel labelDetailProgressInfo;
	public JFileChooserControl fileChooserTotalTaskPath;
	public JFileChooserControl fileChooserTaskPath;
	public JFileChooserControl fileChooserWorkspacePath;
	public JTextField textFieldMapName;
	private JTextField textFieldProcessCount;
	private JButton buttonApply;
	public JFileChooserControl fileChooserCachePath;
	//	private JLabel labelMergeSciCount;
//	private JTextField textFieldMergeSciCount;
	private JProgressBar progressBarTotal;
	private JScrollPane scrollPaneProgresses;
	private JButton buttonCreate;
	private JButton buttonClose;
	private JButton buttonRefresh;
	private WarningOrHelpProvider helpProviderForTaskPath;
	private WarningOrHelpProvider helpProviderForTotalTaskPath;
	private WarningOrHelpProvider helpProviderForProcessCount;
	//	private WarningOrHelpProvider helpProviderForMergeSciCount;
	//scipath for restore path of .sci file
	private String sciPath;
	private int totalSciLength;
	private long startTime;
	private int cmdType;

	private String[] params;
	private BuildCache buildCache;
	//Total sci file
	private File sciFile;
	private CopyOnWriteArrayList<JProgressBar> progressBars;
	private CopyOnWriteArrayList<String> captions;
	private CopyOnWriteArrayList<Integer> captionCount;
	private Thread updateThread;
//	private Thread totalUpdateThread;

	private ActionListener closeListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (hasTask()) {
				shutdownMapClip(true);
			} else {
				DialogCacheBuilder.this.dispose();
			}
		}
	};

	private void shutdownMapClip(boolean dispose) {
		SmOptionPane optionPane = new SmOptionPane();
		sciPath = fileChooserTaskPath.getPath();
		if (optionPane.showConfirmDialogYesNo(MapViewProperties.getString("String_FinishClipTaskOrNot")) == JOptionPane.OK_OPTION) {
			ProcessManager.getInstance().removeAllProcess(sciPath, "doing");
			if (dispose) {
				DialogCacheBuilder.this.dispose();
			}
			optionPane.showConfirmDialog(MessageFormat.format(MapViewProperties.getString("String_ProcessClipFinished"), sciPath));
		} else {
			return;
		}
	}

	private ActionListener createListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			buttonCreate.setEnabled(false);
			buildCache();
		}
	};
	private ActionListener refreshListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String taskPath = fileChooserTaskPath.getPath();
			if (!StringUtilities.isNullOrEmpty(taskPath)) {
				File taskFile = new File(taskPath);
				if (taskFile.exists()) {
					refreshProgress(taskFile.getParent(), totalSciLength);
				}
				if (taskFinished(taskPath)) {
					updateThread.interrupt();
					String cachePath = fileChooserCachePath.getPath();
					cachePath = CacheUtilities.replacePath(cachePath);
					getResult(cachePath, startTime);
				}
			}
		}
	};

	private boolean taskFinished(String taskPath) {
		File taskFile = new File(taskPath);
		int finised = 0;
		if (taskFile.exists() && null != taskFile.list(getFilter())) {
			finised = taskFile.list(getFilter()).length;
		}
		File doingFile = new File(CacheUtilities.replacePath(taskFile.getParent(), "doing"));
		if (doingFile.exists() && null != doingFile.list())
			finised += doingFile.list().length;
		return 0 == finised;
	}

	private ActionListener applyListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (null == params || null == buildCache) {
				return;
			}
			try {
				String newProcessStr = textFieldProcessCount.getText();
				if (StringUtilities.isInteger(newProcessStr) || newProcessStr.equals("0")) {
					SmOptionPane optionPane = new SmOptionPane();
					int newProcessCount = Integer.valueOf(newProcessStr);
					String logFolder = ".\\temp_log\\";
					if (CacheUtilities.isLinux()) {
						logFolder = "./temp_log/";
					}
					int nowProcessCount = -1;
					File logDirectory = new File(logFolder);
					if (logDirectory.exists() && logDirectory.isDirectory()) {
						nowProcessCount = logDirectory.list(new FilenameFilter() {
							@Override
							public boolean accept(File dir, String name) {
								return name.contains("BuildCache");
							}
						}).length;
					}

					if (newProcessCount > nowProcessCount) {
						//Add new process
						int newSize = newProcessCount - nowProcessCount;
						if (optionPane.showConfirmDialog(MessageFormat.format(MapViewProperties.getString("String_Process_message_Add"), String.valueOf(newSize))) == JOptionPane.OK_OPTION)
							for (int i = 0; i < newSize; i++) {
								buildCache.addProcess(params);
							}

					} else if (newProcessCount < nowProcessCount) {
						if (newProcessCount == 0) {
							shutdownMapClip(false);
						} else {
							int newSize = nowProcessCount - newProcessCount;
							if (optionPane.showConfirmDialog(MessageFormat.format(MapViewProperties.getString("String_Process_message_Stop"), String.valueOf(newSize))) == JOptionPane.OK_OPTION)
								ProcessManager.getInstance().removeProcess(params, newProcessCount, sciPath);
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	};
	private FileChooserPathChangedListener fileChangeListener = new FileChooserPathChangedListener() {
		@Override
		public void pathChanged() {
			String sciFilePath = fileChooserTotalTaskPath.getPath();
			if (FileUtilities.isFilePath(sciFilePath) && new File(sciFilePath).isDirectory()) {
				return;
			} else if (FileUtilities.isFilePath(sciFilePath) && sciFilePath.endsWith("sci")) {

				MapCacheBuilder builder = new MapCacheBuilder();
				boolean result = builder.fromConfigFile(sciFilePath);
				if (result) {
					sciFile = new File(sciFilePath);
					textFieldMapName.setText(builder.getCacheName());
					HashMap<Double, String> allScaleCaptions = new HashMap<Double, String>(builder.getOutputScaleCaptions());
					Set<Double> scales = allScaleCaptions.keySet();
					ArrayList<Double> scaleList = new ArrayList<Double>();
					scaleList.addAll(scales);
					Collections.sort(scaleList);
					CopyOnWriteArrayList<String> tempCaptions = new CopyOnWriteArrayList<>();
					for (double scale : scaleList) {
						tempCaptions.add(String.valueOf(Math.round(1 / scale)));
					}
					setCaptions(tempCaptions);
				}
			}
		}
	};

	public DialogCacheBuilder(int cmdType) {
		this.cmdType = cmdType;
		init();
	}

	private void init() {
		initComponents();
		initResources();
		initLayout();
		registEvents();
		Dimension dimension = new Dimension(723, 365);
		this.setSize(dimension);
		this.setLocationRelativeTo(null);
		this.componentList.add(this.buttonCreate);
		this.componentList.add(this.buttonClose);
		this.setFocusTraversalPolicy(policy);
	}

	private void initComponents() {
		this.progressBars = new CopyOnWriteArrayList<>();
		this.labelTotalTaskPath = new JLabel();
		this.labelTaskPath = new JLabel();
		this.labelWorkspacePath = new JLabel();
		this.labelMapName = new JLabel();
		this.labelCachePath = new JLabel();
//		this.labelTotalProcessCount = new JLabel();
		this.labelProcessCount = new JLabel();
		this.labelTotalProgress = new JLabel();
		this.labelDetailProgressInfo = new JLabel();

		String taskModuleName = "ChooseTotalTaskFile";
		if (!SmFileChoose.isModuleExist(taskModuleName)) {
			String sciFilter = SmFileChoose.bulidFileFilters(
					SmFileChoose.createFileFilter(MapViewProperties.getString("String_SciFileType"), "sci"));
			SmFileChoose.addNewNode(sciFilter, System.getProperty("user.dir"), GlobalParameters.getDesktopTitle(),
					taskModuleName, "OpenOne");
		}

		SmFileChoose fileChooseTotalTask = new SmFileChoose(taskModuleName);
		this.fileChooserTotalTaskPath = new JFileChooserControl();
		this.fileChooserTotalTaskPath.setFileChooser(fileChooseTotalTask);
		this.fileChooserTotalTaskPath.setPath(fileChooseTotalTask.getModuleLastPath());

		String moduleName = "ChooseTaskDirectories";
		if (!SmFileChoose.isModuleExist(moduleName)) {
			SmFileChoose.addNewNode("", System.getProperty("user.dir"), GlobalParameters.getDesktopTitle(),
					moduleName, "GetDirectories");
		}

		SmFileChoose fileChoose = new SmFileChoose(moduleName);
		this.fileChooserTaskPath = new JFileChooserControl();
		this.fileChooserTaskPath.setFileChooser(fileChoose);
		this.fileChooserTaskPath.setPath(fileChoose.getModuleLastPath());
		String modulenameForWorkspace = "ChooseWorkspaceFile";
		if (!SmFileChoose.isModuleExist(modulenameForWorkspace)) {
			String fileFilters = SmFileChoose.createFileFilter(MapViewProperties.getString("String_FileFilters_Workspace"), "smwu", "sxwu");
			SmFileChoose.addNewNode(fileFilters, System.getProperty("user.dir"),
					MapViewProperties.getString("String_OpenWorkspace"), modulenameForWorkspace, "OpenOne");
		}

		SmFileChoose fileChooserForWorkSpace = new SmFileChoose(modulenameForWorkspace);
		this.fileChooserWorkspacePath = new JFileChooserControl();
		this.fileChooserWorkspacePath.setFileChooser(fileChooserForWorkSpace);
		this.fileChooserWorkspacePath.setPath(fileChooserForWorkSpace.getModuleLastPath());
		this.textFieldMapName = new JTextField();
		this.textFieldProcessCount = new JTextField();
		this.textFieldProcessCount.setText("5");
		this.buttonApply = ComponentFactory.createButtonApply();
		String moduleNameForCachePath = "ChooseCacheDirectories";
		if (!SmFileChoose.isModuleExist(moduleNameForCachePath)) {
			SmFileChoose.addNewNode("", System.getProperty("user.dir"), GlobalParameters.getDesktopTitle(),
					moduleNameForCachePath, "GetDirectories");
		}
		SmFileChoose fileChooserForCachePath = new SmFileChoose(moduleNameForCachePath);
		this.fileChooserCachePath = new JFileChooserControl();
		this.fileChooserCachePath.setPath(fileChooserForCachePath.getModuleLastPath());
		this.fileChooserCachePath.setFileChooser(fileChooserForCachePath);
		this.progressBarTotal = new JProgressBar();
		this.progressBarTotal.setStringPainted(true);
		this.progressBarTotal.setPreferredSize(new Dimension(200, 23));
//		this.labelMergeSciCount = new JLabel();
//		this.textFieldMergeSciCount = new JTextField();
//		this.textFieldMergeSciCount.setText("1");
		this.scrollPaneProgresses = new JScrollPane();
		this.buttonRefresh = new SmButton();
		this.buttonCreate = new SmButton();
		this.buttonClose = ComponentFactory.createButtonClose();
		this.helpProviderForTotalTaskPath = new WarningOrHelpProvider(MapViewProperties.getString("String_TotalSciFile"), false);
		this.helpProviderForTaskPath = new WarningOrHelpProvider(MapViewProperties.getString("String_SciFilePath"), false);
		this.helpProviderForProcessCount = new WarningOrHelpProvider(MapViewProperties.getString("String_HelpForProcessCount"), false);
//		this.helpProviderForMergeSciCount = new WarningOrHelpProvider(MapViewProperties.getString("String_HelpForMergeSciCount"), false);
	}

	private void initResources() {
		this.setTitle(MapViewProperties.getString("String_MultiProcessClipMapCache"));
		this.labelTotalTaskPath.setText(MapViewProperties.getString("String_TotalTaskPath"));
		this.labelTaskPath.setText(MapViewProperties.getString("String_TaskPath"));
		this.labelWorkspacePath.setText(MapViewProperties.getString("String_WorkspacePath"));
		this.labelMapName.setText(MapViewProperties.getString("String_Label_MapName"));
		this.labelProcessCount.setText(MapViewProperties.getString("String_ProcessCount"));
		this.labelCachePath.setText(MapViewProperties.getString("String_CachePath"));
//		this.labelTotalProcessCount.setText(MapViewProperties.getString("String_TotalProcessCount"));
		this.labelTotalProgress.setText(ControlsProperties.getString("String_ProgressControl_TotalProgress"));
		this.labelDetailProgressInfo.setText(MapViewProperties.getString("String_DetailProcessInfo"));
//		this.labelMergeSciCount.setText(MapViewProperties.getString("String_MergeSciCount"));
		this.buttonCreate.setText(MapViewProperties.getString("String_BatchAddColorTableOKButton"));
		this.buttonRefresh.setText(ControlsProperties.getString("String_Refresh"));
	}


	private void initLayout() {
		JPanel panelClip = new JPanel();
		panelClip.setBorder(BorderFactory.createTitledBorder(MapViewProperties.getString("String_MultiProcessClip")));
		panelClip.setLayout(new GridBagLayout());

		panelClip.add(this.labelTotalTaskPath, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 10, 5, 10));
		panelClip.add(this.helpProviderForTotalTaskPath, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 0, 5, 10));
		panelClip.add(this.fileChooserTotalTaskPath, new GridBagConstraintsHelper(2, 0, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 0, 5, 10).setWeight(1, 0));
		panelClip.add(this.labelTaskPath, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
		panelClip.add(this.helpProviderForTaskPath, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 0, 5, 10));
		panelClip.add(this.fileChooserTaskPath, new GridBagConstraintsHelper(2, 1, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));
		panelClip.add(this.labelWorkspacePath, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
		panelClip.add(this.fileChooserWorkspacePath, new GridBagConstraintsHelper(2, 2, 4, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));
		panelClip.add(this.labelMapName, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
		panelClip.add(this.textFieldMapName, new GridBagConstraintsHelper(2, 3, 4, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));
		panelClip.add(this.labelProcessCount, new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
		panelClip.add(this.helpProviderForProcessCount, new GridBagConstraintsHelper(1, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 0, 5, 10));
		panelClip.add(this.textFieldProcessCount, new GridBagConstraintsHelper(2, 4, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 5).setWeight(1, 0));
		panelClip.add(this.buttonApply, new GridBagConstraintsHelper(4, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 0, 5, 10));
		panelClip.add(this.labelCachePath, new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
		panelClip.add(this.fileChooserCachePath, new GridBagConstraintsHelper(2, 5, 4, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));
//		panelClip.add(this.labelMergeSciCount, new GridBagConstraintsHelper(0, 6, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
//		panelClip.add(this.helpProviderForMergeSciCount, new GridBagConstraintsHelper(1, 6, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 0, 5, 10));
//		panelClip.add(this.textFieldMergeSciCount, new GridBagConstraintsHelper(2, 6, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));
		panelClip.add(new JPanel(), new GridBagConstraintsHelper(0, 7, 5, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setWeight(1, 1));

		JPanel panelClipProgress = new JPanel();
		panelClipProgress.setBorder(BorderFactory.createTitledBorder(MapViewProperties.getString("String_ClipBuilderProgress")));
		panelClipProgress.setLayout(new GridBagLayout());
//		panelClipProgress.add(this.labelTotalProcessCount, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 10, 5, 10));
//		panelClipProgress.add(this.textFieldTotalProcessCount, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 0, 5, 10).setWeight(1, 0));
		panelClipProgress.add(this.labelTotalProgress, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
		panelClipProgress.add(this.progressBarTotal, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));
		panelClipProgress.add(this.buttonRefresh, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 0, 5, 10));
		panelClipProgress.add(this.labelDetailProgressInfo, new GridBagConstraintsHelper(0, 2, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
		panelClipProgress.add(this.scrollPaneProgresses, new GridBagConstraintsHelper(0, 3, 3, 5).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(0, 10, 5, 10).setWeight(1, 1));
		JPanel panelContent = (JPanel) this.getContentPane();
		panelContent.setLayout(new GridBagLayout());
		panelContent.add(panelClip, new GridBagConstraintsHelper(0, 0, 4, 4).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(5, 10, 5, 10).setWeight(1, 1));
		panelContent.add(panelClipProgress, new GridBagConstraintsHelper(4, 0, 4, 4).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(5, 0, 5, 10).setWeight(1, 1));
		panelContent.add(this.buttonCreate, new GridBagConstraintsHelper(6, 4, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(0, 0, 10, 10).setWeight(1, 0));
		panelContent.add(this.buttonClose, new GridBagConstraintsHelper(7, 4, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(0, 0, 10, 10).setWeight(0, 0));
	}

	private void registEvents() {
		removeEvents();
		this.buttonCreate.addActionListener(this.createListener);
		this.buttonClose.addActionListener(this.closeListener);
		this.buttonApply.addActionListener(this.applyListener);
		this.fileChooserTotalTaskPath.addFileChangedListener(this.fileChangeListener);
		this.buttonRefresh.addActionListener(this.refreshListener);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (hasTask()) {
					setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
					shutdownMapClip(true);
				}
			}
		});

	}

	private boolean hasTask() {
		File taskFile = new File(fileChooserTaskPath.getPath());
		File doingFile = null;
		if (taskFile.exists()) {
			doingFile = new File(CacheUtilities.replacePath(taskFile.getParent(), "doing"));
		}
		return null != doingFile && hasSciFiles(doingFile);
	}

	private void removeEvents() {
		this.buttonCreate.removeActionListener(this.createListener);
		this.buttonClose.removeActionListener(this.closeListener);
		this.buttonApply.removeActionListener(this.applyListener);
		this.fileChooserTotalTaskPath.removePathChangedListener(this.fileChangeListener);
		this.buttonRefresh.removeActionListener(this.refreshListener);
	}

	private void buildCache() {
		try {
			String totalSciPath = fileChooserTotalTaskPath.getPath();
			String workspacePath = fileChooserWorkspacePath.getPath();
			workspacePath = CacheUtilities.replacePath(workspacePath);
			String mapName = textFieldMapName.getText();
			sciPath = fileChooserTaskPath.getPath();
			sciPath = CacheUtilities.replacePath(sciPath);
			String cachePath = fileChooserCachePath.getPath();
			cachePath = CacheUtilities.replacePath(cachePath);
			String processCount = textFieldProcessCount.getText();
//			String mergeSciCount = textFieldMergeSciCount.getText();
			boolean isAppending = this.cmdType == DialogMapCacheClipBuilder.MultiUpdateProcessClip;
			params = new String[]{sciPath, workspacePath, mapName, cachePath, processCount, String.valueOf(isAppending)};
//            final String[] params = {workspacePath, mapName, sciPath, cachePath, processCount, mergeSciCount};

			if (!validateValue(totalSciPath, sciPath, workspacePath, mapName, cachePath, processCount)) {
				buttonCreate.setEnabled(true);
			} else {
				doBuildCache(cachePath);
			}
		} catch (Exception ex) {
			new SmOptionPane().showConfirmDialog(ex.getMessage());
		}
	}

	private void doBuildCache(String cachePath) {
		String[] sciNames = null;
		String[] buildNames = null;
		String[] doingNames = null;
		File sciFile = new File(sciPath);
		if (sciFile.exists()) {
			sciNames = sciFile.list(getFilter());
		}
		String parentStr = sciFile.getParent();
		File buildFile = new File(CacheUtilities.replacePath(parentStr, "build"));
		File doingFile = new File(CacheUtilities.replacePath(parentStr, "doing"));

		if (buildFile.exists()) {
			buildNames = buildFile.list(getFilter());
		}
		if (doingFile.exists()) {
			doingNames = doingFile.list(getFilter());
		}
		if (null != sciNames) {
			totalSciLength = sciNames.length;
		}
		if (null != buildNames) {
			totalSciLength += buildNames.length;
		}
		if (null != doingNames) {
			totalSciLength += doingNames.length;
		}
		if (totalSciLength > 0) {
			captionCount = new CopyOnWriteArrayList<>();
			if (null != captions) {
				for (int i = 0; i < captions.size(); i++) {
					int captionSciSize = getSubSciCount(sciNames, captions.get(i));
					if (null != buildNames) {
						captionSciSize += getSubSciCount(buildNames, captions.get(i));
					}
					if (null != doingNames) {
						captionSciSize += getSubSciCount(buildNames, captions.get(i));
					}
					captionCount.add(captionSciSize);
				}
				updateProcesses(parentStr, cachePath, totalSciLength);
			}
		} else {
			System.out.println("No sci file");
		}
		buildCache = new BuildCache();
		buildCache.startProcess(Integer.valueOf(params[BuildCache.PROCESSCOUNT_INDEX]), params);
	}

	private int getSubSciCount(String[] sciNames, String s) {
		int result = 0;
		for (int i = 0; i < sciNames.length; i++) {
			if (sciNames[i].contains(s)) {
				result++;
			}
		}
		return result;
	}

	private boolean validateValue(String totalSciPath, String sciPath, String workspacePath, String mapName, String cachePath, String processCount) {
		boolean result = true;
		SmOptionPane optionPane = new SmOptionPane();
		if (StringUtilities.isNullOrEmpty(totalSciPath) || !new File(totalSciPath).exists() || !totalSciPath.endsWith("sci")) {
			optionPane.showErrorDialog(MapViewProperties.getString("String_SciFileNotExist"));
			return false;
		}
		File sciDirectory = new File(sciPath);
		if (StringUtilities.isNullOrEmpty(sciPath) || !sciDirectory.exists() || !hasSciFiles(sciDirectory)) {
			optionPane.showErrorDialog(MapViewProperties.getString("String_TaskPathError"));
			return false;
		}
		if (StringUtilities.isNullOrEmpty(workspacePath) || !new File(workspacePath).exists() || !(workspacePath.endsWith("smwu") || workspacePath.endsWith("sxwu"))) {
			optionPane.showErrorDialog(MapViewProperties.getString("String_WorkspaceNotExist"));
			return false;
		}
		if (StringUtilities.isNullOrEmpty(mapName)) {
			optionPane.showErrorDialog(MapViewProperties.getString("String_MapNameIsNull"));
			return false;
		}
		if (StringUtilities.isNullOrEmpty(cachePath) || !FileUtilities.isFilePath(cachePath)) {
			optionPane.showErrorDialog(MapViewProperties.getString("String_CachePathNotExist"));
			return false;
		}
		if (StringUtilities.isNullOrEmpty(processCount) || !(StringUtilities.isInteger(processCount) || "0".equals(processCount))) {
			optionPane.showErrorDialog(MapViewProperties.getString("String_ProcessCountError"));
			textFieldProcessCount.requestFocus();
			return false;
		}
		WorkspaceConnectionInfo connectionInfo = new WorkspaceConnectionInfo(workspacePath);
		Workspace workspace = new Workspace();
		workspace.open(connectionInfo);
		boolean hasMap = false;
		for (int i = 0; i < workspace.getMaps().getCount(); i++) {
			if (mapName.equals(workspace.getMaps().get(i))) {
				hasMap = true;
				break;
			}
		}
		if (!hasMap) {
			optionPane.showConfirmDialog(MapViewProperties.getString("String_MapIsNotExist"));
			textFieldMapName.requestFocus();
			return false;
		}
		return result;
	}

	private boolean hasSciFiles(File sciDirectory) {
		if (null == sciDirectory.list(getFilter())) {
			return false;
		}
		return sciDirectory.list(getFilter()).length > 0 ? true : false;
	}

	//Update single scale info process
	private void updateProcesses(final String parentPath, final String cachePath, final int totalSciLength) {

		final int finalTotalSciLength = totalSciLength;
		final String finalParentPath = parentPath;
		final String finalCachePath = cachePath;
		updateThread = new Thread() {
			@Override
			public void run() {
				try {
					startTime = System.currentTimeMillis();
					while (true) {
						refreshProgress(finalParentPath, finalTotalSciLength);
						if (taskFinished(CacheUtilities.replacePath(finalParentPath, "task"))) {
							break;
						}
						//Sleep 1 hour,then refresh progressBars
						TimeUnit.HOURS.sleep(1);
					}
					getResult(finalCachePath, startTime);
				} catch (Exception e) {
//					e.printStackTrace();
				}
			}
		};
		updateThread.start();

	}

	private void refreshProgress(String parentPath, int fianlTotalSciLength) {
		int totalPercent = 0;
		int currentTotalCount = 0;
		File buildFile = new File(CacheUtilities.replacePath(parentPath, "build"));
		//Ensure that component,count array have sorted as we want;
		if (buildFile.exists()) {
			File failedFile = new File(CacheUtilities.replacePath(parentPath, "failed"));
			String[] buildSciNames = null;
			String[] failedSciNames = null;
			if (null != buildFile.list(getFilter())) {
				buildSciNames = buildFile.list();
				currentTotalCount = buildSciNames.length;
			}
			if (failedFile.exists() && null != failedFile.list(getFilter())) {
				failedSciNames = failedFile.list();
				currentTotalCount += failedSciNames.length;
			}
			if (null != captions) {
				for (int i = 0; i < captions.size(); i++) {
					int currentCount = getSingleProcess(buildSciNames, failedSciNames, captions.get(i));
					int value = (int) (((currentCount + 0.0) / captionCount.get(i)) * 100);
					if (progressBars.get(i).getValue() != 100) {
						progressBars.get(i).setValue(value);
					}
				}
			}
			totalPercent = (int) (((currentTotalCount + 0.0) / fianlTotalSciLength) * 100);
			progressBarTotal.setValue(totalPercent);
		}
	}

	private int getSingleProcess(String[] builedScis, String[] failedScis, String caption) {
		int currentCount = 0;
		for (int i = 0; i < builedScis.length; i++) {
			if (builedScis[i].contains(caption)) {
				currentCount++;
			}
		}
		if (null != failedScis) {
			for (int i = 0; i < failedScis.length; i++) {
				if (failedScis[i].contains(caption)) {
					currentCount++;
				}
			}
		}
		return currentCount;
	}


	private FilenameFilter getFilter() {
		return new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".sci");
			}
		};
	}

	private FilenameFilter getFilter(String caption) {
		final String tempCaption = caption;
		return new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".sci") && name.contains(tempCaption);
			}
		};
	}

	private void disposeInfo() {
		removeEvents();
		this.captions = null;
		this.scrollPaneProgresses = null;
		this.captionCount = null;
		DialogCacheBuilder.this.dispose();
		//Dispose instance of process manager
		ProcessManager.getInstance().dispose();
	}

	public void setCaptions(CopyOnWriteArrayList<String> sourceCaptions) {
		captions = sourceCaptions;
		//Display scale info process
		JPanel panelScaleProcess = new JPanel();
		panelScaleProcess.setLayout(new GridBagLayout());
		int scaleCount = captions.size();
		progressBars.clear();
		for (int i = 0; i < scaleCount; i++) {
			JLabel labelProcess = new JLabel("1:" + captions.get(i));
			JProgressBar process = new JProgressBar();
			process.setStringPainted(true);
			progressBars.add(process);
			panelScaleProcess.add(labelProcess, new GridBagConstraintsHelper(0, i, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(i == 0 ? 10 : 0, 10, 5, 10));
			panelScaleProcess.add(process, new GridBagConstraintsHelper(1, i, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(i == 0 ? 10 : 0, 0, 5, 10).setWeight(1, 0));
		}
		panelScaleProcess.add(new JPanel(), new GridBagConstraintsHelper(0, scaleCount, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		this.scrollPaneProgresses.setViewportView(panelScaleProcess);
		this.repaint();
	}

	public synchronized void getResult(String cachePath, long startTime) {
		boolean result = false;
		File resultDir = new File(cachePath);
		String resultPath = "";
		if (resultDir.isDirectory()) {
			File[] files = resultDir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().equals(params[BuildCache.MAPNAME_INDEX])) {
					resultPath = files[i].getAbsolutePath();
					result = true;
					break;
				}
			}
		}
		if (result) {
			buttonClose.setEnabled(true);
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			long hour = 0;
			long minutes = 0;
			long second = 0;
			if (totalTime >= 3600000) {
				hour = totalTime / 3600000;
				minutes = (totalTime % 3600000) / 60000;
				second = ((totalTime % 3600000) % 60000) / 1000;
			} else if (totalTime >= 60000) {
				minutes = totalTime / 60000;
				second = (totalTime % 60000) / 1000;
			} else {
				second = totalTime / 1000;
			}

			File failedFile = new File(CacheUtilities.replacePath(sciFile.getParent(), "failed"));
			if (failedFile.exists()) {
				new SmOptionPane().showConfirmDialog(MessageFormat.format(MapViewProperties.getString("String_Process_message_Failed"), failedFile.list().length, failedFile.getPath()));
			}
			new SmOptionPane().showConfirmDialog(MessageFormat.format(MapViewProperties.getString("String_MultiCacheSuccess"), resultPath, hour, minutes, second));
		} else {
			new SmOptionPane().showConfirmDialog(MapViewProperties.getString("String_MultiCacheFailed"));
		}
		disposeInfo();
	}

	public static void main(String[] args) {
		if (args.length > 1) {
			String totalSciLength = args[1];
			String taskPath = args[2];
			String workspacePath = args[3];
			String mapName = args[4];
			String cachePath = args[5];
			DialogCacheBuilder dialogMapCacheBuilder = getDialog(args[0]);
			if (!"null".equals(mapName)) {
				dialogMapCacheBuilder.textFieldMapName.setText(mapName);
			}
			if (!"null".equals(totalSciLength)) {
				dialogMapCacheBuilder.fileChooserTotalTaskPath.setPath(totalSciLength);
			}
			if (!"null".equals(taskPath)) {
				dialogMapCacheBuilder.fileChooserTaskPath.setPath(taskPath);
			}
			if (!"null".equals(workspacePath)) {
				dialogMapCacheBuilder.fileChooserWorkspacePath.setPath(workspacePath);
			}
			if (!"null".equals(cachePath)) {
				dialogMapCacheBuilder.fileChooserCachePath.setPath(cachePath);
			}
			dialogMapCacheBuilder.showDialog();
		} else {
			getDialog(args[0]).showDialog();
		}
	}

	private static DialogCacheBuilder getDialog(String operationType) {
		DialogCacheBuilder dialogCacheBuilder = null;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			int tempCmdType = operationType.equals("Multi") ? DialogMapCacheClipBuilder.MultiProcessClip : DialogMapCacheClipBuilder.MultiUpdateProcessClip;
			dialogCacheBuilder = new DialogCacheBuilder(tempCmdType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dialogCacheBuilder;
	}
}
