package com.supermap.desktop.dialog.cacheClip;

import com.supermap.data.Dataset;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.processing.MapCacheBuilder;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.dialog.cacheClip.cache.BuildCache;
import com.supermap.desktop.dialog.cacheClip.cache.CacheUtilities;
import com.supermap.desktop.dialog.cacheClip.cache.ProcessManager;
import com.supermap.desktop.properties.CacheProperties;
import com.supermap.desktop.ui.controls.FileChooserPathChangedListener;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.JFileChooserControl;
import com.supermap.desktop.ui.controls.ProviderLabel.WarningOrHelpProvider;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;

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
public class DialogCacheBuilder extends JFrame {
	private JLabel labelWorkspacePath;
	private JLabel labelMapName;
	private JLabel labelProcessCount;
	private JLabel labelCachePath;
	private JLabel labelTotalProgress;
	private JLabel labelDetailProgressInfo;
	public JFileChooserControl fileChooserCachePath;
	public JFileChooserControl fileChooserWorkspacePath;
	public JTextField textFieldMapName;
	private JTextField textFieldProcessCount;
	private JButton buttonApply;
	private JProgressBar progressBarTotal;
	private JScrollPane scrollPaneProgresses;
	private JButton buttonCreate;
	private JButton buttonClose;
	private JButton buttonRefresh;
	private WarningOrHelpProvider helpProviderForProcessCount;
	//scipath for restore path of .sci file
//	private String taskPath;
	private int totalSciLength;
	private long startTime;
	private int cmdType;

	private String[] params;
	private BuildCache buildCache;
	//Total sci file
	private CopyOnWriteArrayList<JProgressBar> progressBars;
	private CopyOnWriteArrayList<String> captions;
	private CopyOnWriteArrayList<Integer> captionCount;
	private Thread updateThread;
	private String locale;
	private CacheProperties cacheProperties;
	private String cachePath;

	private static Object resultLock = new Object();

	private ActionListener closeListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (hasDoingTask()) {
				shutdownMapClip();
			} else {
				killProcess();
			}
		}
	};


	private void shutdownMapClip() {
		ArrayList<String> taskFiles = CacheUtilities.getTaskPath("task", cachePath);
		if (CacheUtilities.showConfirmDialog(DialogCacheBuilder.this, cacheProperties.getString("String_FinishClipTaskOrNot")) == JOptionPane.OK_OPTION) {
			for (int i = 0, size = taskFiles.size(); i < size; i++) {
				ProcessManager.getInstance().removeAllProcess(taskFiles.get(i), "doing");
			}
			killProcess();
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
			ArrayList<String> taskPaths = CacheUtilities.getTaskPath("task", cachePath);
			for (String taskPath : taskPaths) {
				File taskFile = new File(taskPath);
				if (taskFile.exists()) {
					refreshProgress(totalSciLength);
				}
			}
			if (CacheUtilities.taskFinished(taskPaths)) {
				updateThread.interrupt();
				String cachePath = fileChooserCachePath.getPath();
				cachePath = CacheUtilities.replacePath(cachePath);
				getResult(cachePath, startTime);
			}
		}
	};


	private ActionListener applyListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (null == params || null == buildCache) {
				return;
			}
			try {
				String newProcessStr = textFieldProcessCount.getText();
				if (StringUtilities.isInteger(newProcessStr) && Integer.valueOf(newProcessStr) < 0) {
					CacheUtilities.showMessageDialog(DialogCacheBuilder.this, cacheProperties.getString("String_ProcessCountError"));
					return;
				}
				if (StringUtilities.isInteger(newProcessStr) || "0".equals(newProcessStr)) {
					int newProcessCount = Integer.valueOf(newProcessStr);
					String logFolder = ".\\temp_log\\";
					if (CacheUtilities.isLinux()) {
						logFolder = "./temp_log/";
					}
					int nowProcessCount = 0;
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
						if (CacheUtilities.showConfirmDialog(DialogCacheBuilder.this, MessageFormat.format(cacheProperties.getString("String_Process_message_Add"), String.valueOf(newSize))) == JOptionPane.OK_OPTION) {
							for (int i = 0; i < newSize; i++) {
								buildCache.addProcess(params);
							}
						} else {
							textFieldProcessCount.setText(String.valueOf(nowProcessCount));
						}
					} else if (newProcessCount < nowProcessCount) {
						if (newProcessCount == 0) {
							shutdownMapClip();
						} else {
							int newSize = nowProcessCount - newProcessCount;
							if (CacheUtilities.showConfirmDialog(DialogCacheBuilder.this, MessageFormat.format(cacheProperties.getString("String_Process_message_Stop"), String.valueOf(newSize))) == JOptionPane.OK_OPTION) {
								ArrayList<String> taskPaths = CacheUtilities.getTaskPath("task", cachePath);
								for (String taskPath : taskPaths) {
									ProcessManager.getInstance().removeProcess(params, newProcessCount, taskPath);
								}
							} else {
								textFieldProcessCount.setText(String.valueOf(nowProcessCount));
							}
						}
					}
				}
			} catch (Exception ex) {
				if (null != ex.getMessage()) {
					CacheUtilities.showMessageDialog(DialogCacheBuilder.this, ex.getMessage());
				}
			}
		}
	};
	private FileChooserPathChangedListener fileChangeListener = new FileChooserPathChangedListener() {
		@Override
		public void pathChanged() {
			resetPathInfo();
		}
	};

	private void resetPathInfo() {
		cachePath = fileChooserCachePath.getPath();
		ArrayList<String> sciFilePath = CacheUtilities.getTotalCacheSci(cachePath);
		MapCacheBuilder builder = new MapCacheBuilder();
		CopyOnWriteArrayList<String> captions = new CopyOnWriteArrayList<>();
		boolean result;
		for (int i = 0, sciSize = sciFilePath.size(); i < sciSize; i++) {
			result = builder.fromConfigFile(sciFilePath.get(i));
			if (result) {
				HashMap<Double, String> allScaleCaptions = new HashMap<>(builder.getOutputScaleCaptions());
				Set<Double> scales = allScaleCaptions.keySet();
				ArrayList<Double> scaleList = new ArrayList<>();
				scaleList.addAll(scales);
				Collections.sort(scaleList);
				for (double scale : scaleList) {
					if (!captions.contains(String.valueOf(Math.round(1 / scale)))) {
						captions.add(String.valueOf(Math.round(1 / scale)));
					}
				}
			}
		}
		setCaptions(captions);
		progressBarTotal.setValue(0);
		if (StringUtilities.isNullOrEmpty(textFieldMapName.getText())) {
			textFieldMapName.setText(builder.getCacheName());
		}
	}

	public DialogCacheBuilder(int cmdType, String locale) {
		this.cmdType = cmdType;
		this.locale = locale;
		init();
	}

	private void init() {
		initComponents();
		initResources();
		initLayout();
		registEvents();
		resetPathInfo();
		Dimension dimension = new Dimension(723, 365);
		this.setSize(dimension);
		this.setLocationRelativeTo(null);
	}


	private void initComponents() {
		if ("null".equals(locale)) {
			cacheProperties = new CacheProperties();
		} else {
			cacheProperties = new CacheProperties(locale);
		}
		this.progressBars = new CopyOnWriteArrayList<>();
		this.labelWorkspacePath = new JLabel();
		this.labelMapName = new JLabel();
		this.labelCachePath = new JLabel();
		this.labelProcessCount = new JLabel();
		this.labelTotalProgress = new JLabel();
		this.labelDetailProgressInfo = new JLabel();
		String modulenameForWorkspace = "ChooseWorkspaceFile";
		if (!SmFileChoose.isModuleExist(modulenameForWorkspace)) {
			String fileFilters = SmFileChoose.createFileFilter(cacheProperties.getString("String_FileFilters_Workspace"), "smwu", "sxwu");
			SmFileChoose.addNewNode(fileFilters, System.getProperty("user.dir"),
					cacheProperties.getString("String_OpenWorkspace"), modulenameForWorkspace, "OpenOne");
		}

		SmFileChoose fileChooserForWorkSpace = new SmFileChoose(modulenameForWorkspace);
		fileChooserForWorkSpace.setOwer(DialogCacheBuilder.this);
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
		fileChooserForCachePath.setOwer(DialogCacheBuilder.this);
		this.fileChooserCachePath = new JFileChooserControl();
		this.fileChooserCachePath.setPath(fileChooserForCachePath.getModuleLastPath());
		this.fileChooserCachePath.setFileChooser(fileChooserForCachePath);
		this.progressBarTotal = new JProgressBar();
		this.progressBarTotal.setStringPainted(true);
		this.progressBarTotal.setPreferredSize(new Dimension(200, 23));
		this.scrollPaneProgresses = new JScrollPane();
		this.buttonRefresh = new SmButton();
		this.buttonCreate = new SmButton();
		this.buttonClose = ComponentFactory.createButtonClose();
		this.helpProviderForProcessCount = new WarningOrHelpProvider(cacheProperties.getString("String_HelpForProcessCount"), false);
	}

	private void initResources() {
		this.setIconImages(CacheUtilities.getIconImages());
		this.setTitle(cacheProperties.getString("String_MultiProcessClipMapCache"));
		this.labelWorkspacePath.setText(cacheProperties.getString("String_WorkspacePath"));
		this.labelMapName.setText(cacheProperties.getString("String_Label_MapName"));
		this.labelProcessCount.setText(cacheProperties.getString("String_ProcessCount"));
		this.labelCachePath.setText(cacheProperties.getString("MapCache_LabelCachePath"));
		this.labelTotalProgress.setText(cacheProperties.getString("String_ProgressControl_TotalProgress"));
		this.labelDetailProgressInfo.setText(cacheProperties.getString("String_DetailProcessInfo"));
		this.buttonRefresh.setText(cacheProperties.getString("String_Refresh"));
		this.buttonCreate.setText(cacheProperties.getString("String_OK"));
		this.buttonClose.setText(cacheProperties.getString("String_Close"));
		this.buttonApply.setText(cacheProperties.getString("String_Apply"));
	}


	private void initLayout() {
		JPanel panelClip = new JPanel();
		panelClip.setBorder(BorderFactory.createTitledBorder(cacheProperties.getString("String_MultiProcessClip")));
		panelClip.setLayout(new GridBagLayout());


		panelClip.add(this.labelWorkspacePath, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
		panelClip.add(this.fileChooserWorkspacePath, new GridBagConstraintsHelper(2, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));
		panelClip.add(this.labelMapName, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
		panelClip.add(this.textFieldMapName, new GridBagConstraintsHelper(2, 1, 4, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));
		panelClip.add(this.labelProcessCount, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
		panelClip.add(this.helpProviderForProcessCount, new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 0, 5, 10));
		panelClip.add(this.textFieldProcessCount, new GridBagConstraintsHelper(2, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 5).setWeight(1, 0));
		panelClip.add(this.buttonApply, new GridBagConstraintsHelper(4, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 0, 5, 10));
		panelClip.add(this.labelCachePath, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
		panelClip.add(this.fileChooserCachePath, new GridBagConstraintsHelper(2, 3, 4, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));
		panelClip.add(new JPanel(), new GridBagConstraintsHelper(0, 4, 5, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setWeight(1, 1));

		JPanel panelClipProgress = new JPanel();
		panelClipProgress.setBorder(BorderFactory.createTitledBorder(cacheProperties.getString("String_ClipBuilderProgress")));
		panelClipProgress.setLayout(new GridBagLayout());
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
		this.fileChooserCachePath.addFileChangedListener(this.fileChangeListener);
		this.buttonRefresh.addActionListener(this.refreshListener);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (hasDoingTask()) {
					setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					shutdownMapClip();
				} else {
					killProcess();
				}
			}
		});

	}

	private boolean hasDoingTask() {
		ArrayList<String> taskPaths = CacheUtilities.getTaskPath("task", cachePath);
		int doingSciSize = 0;
		for (String taskPath : taskPaths) {
			File taskFile = new File(taskPath);
			File doingFile = new File(taskFile.getParent(), "doing");
			if (doingFile.exists() && null != doingFile.list()) {
				doingSciSize += doingFile.list().length;
			}
		}
		return doingSciSize != 0;
	}

	private void removeEvents() {
		this.buttonCreate.removeActionListener(this.createListener);
		this.buttonClose.removeActionListener(this.closeListener);
		this.buttonApply.removeActionListener(this.applyListener);
		this.fileChooserCachePath.removePathChangedListener(this.fileChangeListener);
		this.buttonRefresh.removeActionListener(this.refreshListener);
	}

	private void buildCache() {
		try {
			String workspacePath = fileChooserWorkspacePath.getPath();
			workspacePath = CacheUtilities.replacePath(workspacePath);
			String mapName = textFieldMapName.getText();
			String processCount = textFieldProcessCount.getText();
			params = new String[]{workspacePath, mapName, CacheUtilities.getCachePath(fileChooserCachePath.getPath()), processCount};
			//先移动doing下的sci
			ArrayList<String> taskPaths = CacheUtilities.getTaskPath("task", cachePath);
			for (String tempTaskPath : taskPaths) {
				CacheUtilities.renameDoingFile(CacheUtilities.replacePath(new File(tempTaskPath).getParent(), "doing"), tempTaskPath);
			}
			if (!validateValue(taskPaths, workspacePath, mapName, processCount)) {
				buttonCreate.setEnabled(true);
			} else {
				doBuildCache();
			}
		} catch (Exception ex) {
			if (null != ex.getMessage()) {
				CacheUtilities.showMessageDialog(DialogCacheBuilder.this, ex.getMessage());
			}
		}
	}

	private void doBuildCache() {
		ArrayList<String> sciNames = new ArrayList<>();
		ArrayList<String> buildNames = new ArrayList<>();
		ArrayList<String> doingNames = new ArrayList<>();
		ArrayList<String> failedNames = new ArrayList<>();
		ArrayList<String> taskPaths = CacheUtilities.getTaskPath("task", cachePath);
		for (String taskPath : taskPaths) {
			File taksFile = new File(taskPath);
			String parentStr = taksFile.getParent();
			File buildFile = new File(CacheUtilities.replacePath(parentStr, "build"));
			File doingFile = new File(CacheUtilities.replacePath(parentStr, "doing"));
			File failedFile = new File(CacheUtilities.replacePath(parentStr, "failed"));
			CacheUtilities.addSciToArray(sciNames, taksFile);
			CacheUtilities.addSciToArray(buildNames, buildFile);
			CacheUtilities.addSciToArray(doingNames, doingFile);
			CacheUtilities.addSciToArray(failedNames, failedFile);
		}
		totalSciLength += sciNames.size();
		totalSciLength += buildNames.size();
		totalSciLength += doingNames.size();
		totalSciLength += failedNames.size();
		if (totalSciLength > 0) {
			captionCount = new CopyOnWriteArrayList<>();
			if (null != captions) {
				for (int i = 0; i < captions.size(); i++) {
					int captionSciSize = getSubSciCount(sciNames, captions.get(i));
					if (null != buildNames) {
						captionSciSize += getSubSciCount(buildNames, captions.get(i));
					}
					if (null != doingNames) {
						captionSciSize += getSubSciCount(doingNames, captions.get(i));
					}
					if (null != failedNames) {
						captionSciSize += getSubSciCount(failedNames, captions.get(i));
					}
					captionCount.add(captionSciSize);
				}
				updateProcesses();
			}
		} else {
			CacheUtilities.showMessageDialog(DialogCacheBuilder.this, "No sci file");
		}
		buildCache = new BuildCache();
		buildCache.startProcess(Integer.valueOf(params[BuildCache.PROCESSCOUNT_INDEX]), params);
//		BuildCache.main(params);
	}


	private int getSubSciCount(ArrayList<String> sciNames, String s) {
		int result = 0;
		for (int i = 0, size = sciNames.size(); i < size; i++) {
			if (sciNames.get(i).contains(s)) {
				result++;
			}
		}
		return result;
	}

	private boolean validateValue(ArrayList<String> taskPaths, String workspacePath, String mapName, String processCount) {
		boolean result = true;
		try {
			if (StringUtilities.isNullOrEmpty(workspacePath) || !new File(workspacePath).exists()
					|| !(workspacePath.endsWith("smwu") || workspacePath.endsWith("sxwu"))) {
				CacheUtilities.showMessageDialog(DialogCacheBuilder.this, cacheProperties.getString("String_WorkspaceNotExist"));
				return false;
			}
			if (StringUtilities.isNullOrEmpty(mapName)) {
				CacheUtilities.showMessageDialog(DialogCacheBuilder.this, cacheProperties.getString("String_MapNameIsNull"));
				return false;
			}

			if (StringUtilities.isNullOrEmpty(processCount) ||
					!(StringUtilities.isInteger(processCount) && Integer.valueOf(processCount) > 0)) {
				CacheUtilities.showMessageDialog(DialogCacheBuilder.this, cacheProperties.getString("String_ProcessCountError"));
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
				CacheUtilities.showMessageDialog(DialogCacheBuilder.this, cacheProperties.getString("String_MapIsNotExist"));
				textFieldMapName.requestFocus();
				return false;
			}
			if (taskPaths.size() == 0 || CacheUtilities.getTaskSci(taskPaths) == 0) {
				int failedSciSize = CacheUtilities.getTaskSci(CacheUtilities.getTaskPath("failed", cachePath));
				if (failedSciSize > 0) {
					if (CacheUtilities.showConfirmDialog(DialogCacheBuilder.this, MessageFormat.format(cacheProperties.getString("String_WarningForFailed"), failedSciSize)) == JOptionPane.OK_OPTION) {
						CacheUtilities.renameFailedFiles(taskPaths, "failed");
					} else {
						return false;
					}
				} else if (failedSciSize == 0) {
					CacheUtilities.showMessageDialog(DialogCacheBuilder.this, cacheProperties.getString("String_TaskNotExist"));
					return false;
				}
			}
			//最后检验数据集是否存在
			if (hasMap) {
				Map map = new Map(workspace);
				map.open(mapName);
				ArrayList<Layer> layers = MapUtilities.getLayers(map);
				for (int i = 0; i < layers.size(); i++) {
					Dataset tempDataset = layers.get(i).getDataset();
					if (null == tempDataset) {
						if (CacheUtilities.showConfirmDialog(DialogCacheBuilder.this, MessageFormat.format(cacheProperties.getString("String_DatasetIsOpened"), layers.get(i).getCaption())) == JOptionPane.OK_OPTION) {
							return true;
						}
						return false;
					}
				}
			}
		} catch (Exception e) {
			if (null != e.getMessage()) {
				CacheUtilities.showMessageDialog(DialogCacheBuilder.this, e.getMessage());
			}
		}
		return result;
	}


	//Update single scale info process
	private void updateProcesses() {
		final int finalTotalSciLength = totalSciLength;
		final String finalCachePath = CacheUtilities.getCachePath(fileChooserCachePath.getPath());
		updateThread = new Thread() {
			@Override
			public void run() {
				try {
					startTime = System.currentTimeMillis();
					while (true) {
						refreshProgress(finalTotalSciLength);
						if (CacheUtilities.taskFinished(CacheUtilities.getTaskPath("task", cachePath))) {
							break;
						}
						//Sleep,then refresh progressBars
						if (finalTotalSciLength > 1000) {
							TimeUnit.MINUTES.sleep(15);
						} else {
							TimeUnit.MINUTES.sleep(10);
						}
					}
					getResult(finalCachePath, startTime);
				} catch (Exception e) {
//					e.printStackTrace();
				}
			}
		};
		updateThread.start();

	}

	private void refreshProgress(int fianlTotalSciLength) {
		int currentTotalCount = 0;
		ArrayList<String> buildSciNames = new ArrayList<>();
		ArrayList<String> failedSciNames = new ArrayList<>();
		ArrayList<String> taskPaths = CacheUtilities.getTaskPath("task", cachePath);
		for (String taskPath : taskPaths) {
			File taksFile = new File(taskPath);
			String parentStr = taksFile.getParent();
			File buildFile = new File(CacheUtilities.replacePath(parentStr, "build"));
			File failedFile = new File(CacheUtilities.replacePath(parentStr, "failed"));
			CacheUtilities.addSciToArray(buildSciNames, buildFile);
			CacheUtilities.addSciToArray(failedSciNames, failedFile);
		}
		currentTotalCount += buildSciNames.size();
		currentTotalCount += failedSciNames.size();

		if (null != captions && null != captionCount) {
			for (int i = 0; i < captions.size(); i++) {
				int currentCount = getSingleProcess(buildSciNames, failedSciNames, captions.get(i));
				int value = (int) (((currentCount + 0.0) / captionCount.get(i)) * 100);
				if (progressBars.get(i).getValue() != 100) {
					progressBars.get(i).setValue(value);
				}
			}
		}
		progressBarTotal.setValue((int) (((currentTotalCount + 0.0) / fianlTotalSciLength) * 100));
	}

	private int getSingleProcess(ArrayList<String> builedScis, ArrayList<String> failedScis, String caption) {
		int currentCount = 0;
		if (null != builedScis) {
			for (int i = 0, size = builedScis.size(); i < size; i++) {
				if (builedScis.get(i).contains(caption)) {
					currentCount++;
				}
			}
		}
		if (null != failedScis) {
			if (null != failedScis) {
				for (int i = 0, size = failedScis.size(); i < size; i++) {
					if (failedScis.get(i).contains(caption)) {
						currentCount++;
					}
				}
			}
		}
		return currentCount;
	}


	private void disposeInfo() {
		removeEvents();
		this.captions = null;
		this.scrollPaneProgresses = null;
		this.captionCount = null;
		killProcess();
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

	public void getResult(String cachePath, long startTime) {
		synchronized (resultLock) {
			try {
				boolean result = false;
				File resultDir = new File(cachePath);
				if (resultDir.list().length > 1) {
					result = true;
				}
				if (result) {
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

					int failedSciLength = CacheUtilities.getTaskSci(CacheUtilities.getTaskPath("failed", cachePath));
					if (failedSciLength > 0) {
						if (CacheUtilities.showConfirmDialog(DialogCacheBuilder.this, MessageFormat.format(cacheProperties.getString("String_Process_message_Failed"), failedSciLength)) == JOptionPane.OK_OPTION) {
							buttonCreate.setEnabled(true);
							resetPathInfo();
							CacheUtilities.renameFailedFiles(CacheUtilities.getTaskPath("task", cachePath), "failed");
							buildCache.startProcess(Integer.valueOf(params[BuildCache.PROCESSCOUNT_INDEX]), params);
							return;
						}
					}
					CacheUtilities.showMessageDialog(DialogCacheBuilder.this, MessageFormat.format(cacheProperties.getString("String_MultiCacheSuccess"), cachePath, hour, minutes, second));

				} else {
					CacheUtilities.showMessageDialog(DialogCacheBuilder.this, cacheProperties.getString("String_MultiCacheFailed"));
				}
				disposeInfo();
			} catch (Exception e) {
				if (null != e.getMessage()) {
					CacheUtilities.showMessageDialog(DialogCacheBuilder.this, e.getMessage());
				}
			}
		}
	}

	public static void main(String[] args) {
		if (args.length == 5) {
			DialogCacheBuilder dialogMapCacheBuilder = setParameters(args);
			dialogMapCacheBuilder.setVisible(true);
		} else {
			getDialog(args[0], args[1]).setVisible(true);
		}
	}

	private static DialogCacheBuilder setParameters(String[] args) {
		DialogCacheBuilder dialogMapCacheBuilder = getDialog(args[0], args[1]);
		String workspacePath = args[2];
		String mapName = args[3];
		String cachePath = args[4];
		if (!"null".equals(workspacePath)) {
			dialogMapCacheBuilder.fileChooserWorkspacePath.setPath(workspacePath);
		}
		if (!"null".equals(mapName)) {
			dialogMapCacheBuilder.textFieldMapName.setText(mapName);
		}
		if (!"null".equals(cachePath)) {
			dialogMapCacheBuilder.fileChooserCachePath.setPath(cachePath);
		}
		return dialogMapCacheBuilder;
	}

	private static DialogCacheBuilder getDialog(String operationType, String locale) {
		DialogCacheBuilder dialogCacheBuilder = null;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			int tempCmdType = operationType.equals("Multi") ? DialogMapCacheClipBuilder.MultiProcessClip : DialogMapCacheClipBuilder.MultiUpdateProcessClip;
			dialogCacheBuilder = new DialogCacheBuilder(tempCmdType, locale);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dialogCacheBuilder;
	}

	private void killProcess() {
		System.exit(1);
	}

}
