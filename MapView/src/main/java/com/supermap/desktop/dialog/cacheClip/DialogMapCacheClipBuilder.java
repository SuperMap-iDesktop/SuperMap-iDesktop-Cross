package com.supermap.desktop.dialog.cacheClip;

import com.supermap.data.GeoRegion;
import com.supermap.data.Geometrist;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.processing.MapCacheBuilder;
import com.supermap.data.processing.MapCacheVersion;
import com.supermap.data.processing.MapTilingMode;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.dialog.SmOptionPane;
import com.supermap.desktop.dialog.cacheClip.cache.CacheUtilities;
import com.supermap.desktop.dialog.cacheClip.cache.LogWriter;
import com.supermap.desktop.dialog.cacheClip.cache.TaskBuilder;
import com.supermap.desktop.mapview.MapCache.CacheProgressCallable;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.ChooseTable.MultipleCheckboxItem;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.progress.FormProgress;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Layers;
import com.supermap.mapping.Map;
import com.supermap.tilestorage.TileStorageConnection;
import com.supermap.tilestorage.TileStorageType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by xie on 2017/4/26.
 * Main dialog for setting  map cache
 */
public class DialogMapCacheClipBuilder extends SmDialog {
	private static final int INDEX_MONGOTYPE = 2;
	private int cmdType;
	public static final int SingleProcessClip = 0;
	public static final int MultiProcessClip = 1;
	public static final int SingleUpdateProcessClip = 2;
	public static final int MultiUpdateProcessClip = 3;
	public static final int ResumeProcessClip = 4;
	private boolean firstStepEnabled = true;
	private boolean nextStepEnabled = true;
	private String tasksSize = "1";
	private String canudb = "1";
	private MapCacheBuilder mapCacheBuilder;
	public FirstStepPane firstStepPane;
	private NextStepPane nextStepPane;
	private Map currentMap;
	private JCheckBox checkBoxAutoClosed;
	private JCheckBox checkBoxShowProcessBar;
	private JButton buttonStep;
	public JButton buttonOk;
	private JButton buttonCancel;
	public static final String CacheTask = "CacheTask";

	private ActionListener cancelListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			removeEvents();
			dispose();
		}
	};
	private ActionListener buildListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (cmdType == ResumeProcessClip) {
				resume();
			} else {
				run();
			}
		}
	};

	private ActionListener stepListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			changePanel(buttonStep.getText().equals(ControlsProperties.getString("String_NextWay")));
			getContentPane().repaint();
		}
	};
	private EnabledListener firstStepEnabledListener = new EnabledListener() {
		@Override
		public void setEnabled(EnabledEvent event) {
			firstStepEnabled = event.isEnabled();
			buttonOk.setEnabled(firstStepEnabled && nextStepEnabled);
		}
	};
	private EnabledListener nextStepEnabledListener = new EnabledListener() {
		@Override
		public void setEnabled(EnabledEvent event) {
			nextStepEnabled = event.isEnabled();
			buttonOk.setEnabled(firstStepEnabled && nextStepEnabled);
		}
	};


	public DialogMapCacheClipBuilder(int cmdType, MapCacheBuilder mapCacheBuilder) {
		super();
		this.mapCacheBuilder = mapCacheBuilder;
		this.currentMap = this.mapCacheBuilder.getMap();
		this.cmdType = cmdType;
		init();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private void init() {
		initComponents();
		initResources();
		initLayout();
		registEvents();
		this.componentList.add(this.buttonStep);
		this.componentList.add(this.buttonOk);
		this.componentList.add(this.buttonCancel);
		this.setFocusTraversalPolicy(policy);
	}

	private void removeEvents() {
		this.buttonStep.removeActionListener(this.stepListener);
		this.buttonCancel.removeActionListener(this.cancelListener);
		this.buttonOk.removeActionListener(this.buildListener);
		this.firstStepPane.removeEnabledListener(this.firstStepEnabledListener);
		this.nextStepPane.removeEnabledListener(this.nextStepEnabledListener);
	}

	private void registEvents() {
		removeEvents();
		this.buttonStep.addActionListener(this.stepListener);
		this.buttonCancel.addActionListener(this.cancelListener);
		this.buttonOk.addActionListener(this.buildListener);
		this.firstStepPane.addEnabledListener(this.firstStepEnabledListener);
		this.nextStepPane.addEnabledListener(this.nextStepEnabledListener);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				removeEvents();
			}
		});
	}

	private void initLayout() {
		JPanel panelButton = new JPanel();
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(this.checkBoxAutoClosed, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 15, 10, 5));
		panelButton.add(this.checkBoxShowProcessBar, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 0, 10, 5));
		panelButton.add(this.buttonStep, new GridBagConstraintsHelper(4, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(0, 5, 10, 10).setWeight(1, 0));
		panelButton.add(this.buttonOk, new GridBagConstraintsHelper(5, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(0, 5, 10, 10).setWeight(0, 0));
		panelButton.add(this.buttonCancel, new GridBagConstraintsHelper(6, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 5, 10, 10).setWeight(0, 0));

		this.getContentPane().setLayout(new GridBagLayout());
		this.getContentPane().add(this.firstStepPane, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		this.getContentPane().add(panelButton, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0).setWeight(1, 0));
		Dimension dimension = new Dimension(843, 565);
		this.setSize(dimension);
		this.setLocationRelativeTo(null);
	}

	private void initResources() {
		String mapName;
		if (null != this.currentMap) {
			mapName = this.currentMap.getName();
		} else {
			mapName = this.mapCacheBuilder.getCacheName();
		}
		if (this.cmdType == SingleProcessClip || this.cmdType == ResumeProcessClip || this.cmdType == SingleUpdateProcessClip) {
			this.setTitle(MessageFormat.format(MapViewProperties.getString("MapCache_Title"), mapName));
			this.buttonOk.setText(MapViewProperties.getString("String_BatchAddColorTableOKButton"));
		} else if (this.cmdType == MultiProcessClip) {
			this.setTitle(MessageFormat.format(MapViewProperties.getString("MapCache_Title_TaskBuilder"), mapName));
			this.buttonOk.setText(ControlsProperties.getString("String_NextWay"));
			this.checkBoxAutoClosed.setVisible(false);
			this.checkBoxShowProcessBar.setVisible(false);
		} else if (this.cmdType == MultiUpdateProcessClip) {
			this.setTitle(MessageFormat.format(MapViewProperties.getString("MapCache_Title_TaskBuilder"), mapName));
			this.buttonOk.setText(ControlsProperties.getString("String_NextWay"));
			this.checkBoxAutoClosed.setVisible(false);
			this.checkBoxShowProcessBar.setVisible(false);
		}
		this.checkBoxAutoClosed.setText(MapViewProperties.getString("MapCache_AutoCloseDailog"));
		this.checkBoxShowProcessBar.setText(MapViewProperties.getString("MapCache_ShowProgressBar"));
		this.buttonStep.setText(ControlsProperties.getString("String_NextWay"));
	}

	private void initComponents() {
		if (null != this.currentMap) {
			this.mapCacheBuilder.setBounds(this.currentMap.getBounds());
			this.mapCacheBuilder.setIndexBounds(this.currentMap.getBounds());
		}
		this.firstStepPane = new FirstStepPane(this.mapCacheBuilder, this, this.cmdType);
		this.nextStepPane = new NextStepPane(this, this.mapCacheBuilder, this.cmdType);
		this.checkBoxAutoClosed = new JCheckBox();
		this.checkBoxShowProcessBar = new JCheckBox();
		this.buttonStep = new SmButton();
		this.buttonOk = new SmButton();
		this.buttonCancel = ComponentFactory.createButtonCancel();
		this.checkBoxAutoClosed.setSelected(true);
		this.checkBoxShowProcessBar.setSelected(true);
		this.buttonOk.setVisible(false);
	}

	private boolean validateCacheFolderSave() {
		boolean result = true;
		try {
			if (cmdType == MultiUpdateProcessClip || cmdType == SingleUpdateProcessClip
					|| cmdType == ResumeProcessClip) {
				return result;
			}
			String cacheRoot = firstStepPane.fileChooserControlFileCache.getPath();
			File rootFile = new File(cacheRoot);
			if (!rootFile.exists()) {
				rootFile.mkdir();
			}
			String cacheName = firstStepPane.textFieldCacheName.getText();
			String cachePath = CacheUtilities.replacePath(cacheRoot, cacheName);
			String taskPath = CacheUtilities.replacePath(cacheRoot, CacheTask);
			File cacheFile = new File(cachePath);
			File taskFile = new File(taskPath);
			if (cmdType == MultiProcessClip) {
				//多进程切图时判断缓存名称/CacheTask文件夹是否存在,如果不存在则新建(缓存目录,任务目录,缓存名称配置文件),存在则提示
				if (cacheFile.exists() || taskFile.exists()) {
					result = showError();
				} else {
					cacheFile.mkdir();
					taskFile.mkdir();
				}
				File propertyFile = new File(CacheUtilities.replacePath(cacheRoot, "Cache.property"));
				propertyFile.createNewFile();
				OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(propertyFile), "UTF-8");
				writer.write("CacheName=" + cacheName);
				writer.flush();
				writer.close();
			} else {
				if (cacheFile.exists()) {
					result = showError(cachePath);
				} else {
					cacheFile.mkdir();
				}
			}
		} catch (IOException e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	private boolean showError() {
		new SmOptionPane().showErrorDialog(MapViewProperties.getString("String_CachePathExistOrTaskPathExistError"));
		changePanel();
		return false;
	}

	private boolean showError(String cachePath) {
		new SmOptionPane().showErrorDialog(MessageFormat.format(MapViewProperties.getString("String_CachePathExistError"), cachePath));
		changePanel();
		return false;
	}

	private void changePanel() {
		changePanel(buttonStep.getText().equals(ControlsProperties.getString("String_NextWay")));
		firstStepPane.textFieldCacheName.requestFocus();
		getContentPane().repaint();
	}


	//Change panel
	private void changePanel(boolean flag) {
		buttonOk.setVisible(flag);
		buttonStep.setText(flag == true ? ControlsProperties.getString("String_LastWay") : ControlsProperties.getString("String_NextWay"));
		getContentPane().remove(flag == true ? firstStepPane : nextStepPane);
		getContentPane().add(flag == true ? nextStepPane : firstStepPane, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		if (flag && firstStepPane.importCacheConfigs) {
			nextStepPane.mapCacheBuilder = firstStepPane.mapCacheBuilder;
			nextStepPane.resetComponentsInfo();
		} else if (!flag) {
			firstStepPane.importCacheConfigs = false;
			if (null != nextStepPane.selectedGeometryAndLayer && nextStepPane.selectedGeometryAndLayer.size() > 0) {
				firstStepPane.checkBoxFilterSelectionObjectInLayer.setEnabled(true);
			} else {
				firstStepPane.checkBoxFilterSelectionObjectInLayer.setSelected(false);
				firstStepPane.checkBoxFilterSelectionObjectInLayer.setEnabled(false);
			}
			if (nextStepPane.imagePixelChanged) {
				firstStepPane.globalSplitScale = null;
				firstStepPane.globalSplitTable = null;
				firstStepPane.initGlobalCacheScales();
				if (!firstStepPane.addScaleDropDown.isEnabled()) {
					firstStepPane.scrollPane.setViewportView(firstStepPane.globalSplitTable);
				}
			}
		}

	}

	private void resume() {
		boolean result;
		try {
			String outputPath = this.mapCacheBuilder.getOutputFolder();
			Map map = this.currentMap;
			if (null == map) {
				map = MapUtilities.getActiveMap() == null ? CacheUtilities.getWorkspaceSelectedMap() : MapUtilities.getActiveMap();
			}
			this.mapCacheBuilder.setMap(map);
			this.mapCacheBuilder.setOutputFolder(outputPath);
			long startTime = System.currentTimeMillis();
			if (this.checkBoxShowProcessBar.isSelected()) {
				FormProgress formProgress = new FormProgress();
				formProgress.setTitle(MapViewProperties.getString("MapCache_On") + this.getTitle());
				CacheProgressCallable cacheProgressCallable = new CacheProgressCallable(this.mapCacheBuilder, false);
				formProgress.doWork(cacheProgressCallable);
				result = cacheProgressCallable.getResult();
			} else {
				this.mapCacheBuilder.setFillMargin(true);
//				this.mapCacheBuilder.setIsAppending(true);
				result = this.mapCacheBuilder.build();
			}
			printResultInfo(result, System.currentTimeMillis() - startTime);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void printResultInfo(boolean result, long totalTime) {
		Map map = MapUtilities.getActiveMap() == null ? CacheUtilities.getWorkspaceSelectedMap() : MapUtilities.getActiveMap();
		if (result) {
			Application.getActiveApplication().getOutput().output(MessageFormat.format(MapViewProperties.getString("MapCache_StartCreateSuccessed"), map.getName()));
			Application.getActiveApplication().getOutput().output(MessageFormat.format(MapViewProperties.getString("MapCache_FloderIs"), this.mapCacheBuilder.getOutputFolder()));
		} else {
			Application.getActiveApplication().getOutput().output(MessageFormat.format(MapViewProperties.getString("MapCache_StartCreateFailed"), map.getName()));
		}
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
		Application.getActiveApplication().getOutput().output(MessageFormat.format(MapViewProperties.getString("MapCache_Time"), hour, minutes, second));
		if (this.checkBoxAutoClosed.isSelected()) {
			dispose();
			this.mapCacheBuilder.dispose();
		}
	}

	private void run() {
		try {
			nextStepPane.dispose();
			if (cmdType == SingleProcessClip || cmdType == SingleUpdateProcessClip) {
				singleProcessBuilder();
			} else {
				if (!validateCacheFolderSave()) {
					return;
				}
				if (cmdType == MultiUpdateProcessClip) {
					mapCacheBuilder.setMap(((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().getMap());
				}
				String cachePath = firstStepPane.fileChooserControlFileCache.getPath();
				String sciPath = "";
				if (cmdType == MultiUpdateProcessClip) {
					//todo 多进程更新功能后续处理
//					tasksPath = CacheUtilities.replacePath(tasksPath, "update");
					sciPath = CacheUtilities.replacePath(cachePath, mapCacheBuilder.getCacheName() + ".sci");
				} else {
					sciPath = CacheUtilities.replacePath(cachePath, firstStepPane.textFieldCacheName.getText());
					File sciDirectory = new File(sciPath);
					if (!sciDirectory.exists()) {
						sciDirectory.mkdir();
					}
					if (firstStepPane.comboBoxSaveType.getSelectedIndex() == INDEX_MONGOTYPE) {
						sciPath = CacheUtilities.replacePath(sciPath, mapCacheBuilder.getCacheName() + "_mongo.sci");
					} else {
						sciPath = CacheUtilities.replacePath(sciPath, mapCacheBuilder.getCacheName() + ".sci");
					}
				}
				if (null != mapCacheBuilder.getMap().getVisibleScales() && 0 != mapCacheBuilder.getMap().getVisibleScales().length) {
					//地图存在固定比例从时的处理方式
					if (firstStepPane.addScaleDropDown.isEnabled()) {
						if (this.mapCacheBuilder.getMap().getVisibleScales().length < firstStepPane.currentMapCacheScale.size()) {
							new SmOptionPane().showErrorDialog(MapViewProperties.getString("String_WarningForTaskBuilder"));
							return;
						} else {
							int count = 0;
							for (int i = 0; i < this.mapCacheBuilder.getMap().getVisibleScales().length; i++) {
								for (int j = 0; j < firstStepPane.currentMapCacheScale.size(); j++) {
									if (Double.compare(this.mapCacheBuilder.getMap().getVisibleScales()[i], firstStepPane.currentMapCacheScale.get(j)) == 0) {
										count++;
										break;
									}
								}
							}
							if (count != firstStepPane.currentMapCacheScale.size()) {
								new SmOptionPane().showErrorDialog(MapViewProperties.getString("String_WarningForTaskBuilder"));
								return;
							}
						}

					} else {
						new SmOptionPane().showErrorDialog(MapViewProperties.getString("String_WarningForTaskBuilder"));
						return;
					}

				}
				setMapCacheBuilderValueBeforeRun();
				boolean result = true;
				//SaveType==MongoType,build some cache for creating a database
				this.buttonOk.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				if (firstStepPane.comboBoxSaveType.getSelectedIndex() == INDEX_MONGOTYPE) {
					//Mongo类型单独处理,调用组件接口返回正确的sci
					result = mapCacheBuilder.createMongoDB();
					if (!new File(sciPath).exists()) {
						new SmOptionPane().showErrorDialog(MapViewProperties.getString("String_ErrorForMongoInfo"));
					}
				} else {
					result = mapCacheBuilder.toConfigFile(sciPath);
				}
				if (result) {
					String[] params = {sciPath, CacheUtilities.replacePath(cachePath, CacheTask), tasksSize, canudb};
					boolean buildTaskResult = TaskBuilder.buildSci(params);
					if (!buildTaskResult) {
						this.buttonOk.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						return;
					}
					this.buttonOk.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					dispose();
					Application.getActiveApplication().getOutput().output(MessageFormat.format(MapViewProperties.getString("String_TargetCachePath"), cachePath));
					Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_StartBuildCacheExecute"));
					if (nextStepPane.checkBoxClipOnThisComputer.isSelected()) {
						String mapName = this.mapCacheBuilder.getCacheName();
						if (null != this.mapCacheBuilder.getMap()) {
							mapName = this.mapCacheBuilder.getMap().getName();
						}
						String[] tempParams = {cmdType == MultiUpdateProcessClip ? "Update" : "Multi", "zh-CN",
								Application.getActiveApplication().getWorkspace().getConnectionInfo().getServer(), mapName, cachePath};
						CacheUtilities.startProcess(tempParams, DialogCacheBuilder.class.getName(), LogWriter.BUILD_CACHE);
					}
				}
				if (this.checkBoxAutoClosed.isSelected()) {
					dispose();
					this.mapCacheBuilder.dispose();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Application.getActiveApplication().getOutput().output(e);
		}

	}

	private void singleProcessBuilder() {
		if (!validateCacheFolderSave()) {
			return;
		}
		if (null == mapCacheBuilder.getMap()) {
			Map map = MapUtilities.getActiveMap();
			if (null == map) {
				map = CacheUtilities.getWorkspaceSelectedMap();
			}
			mapCacheBuilder.setMap(map);
		}
		setMapCacheBuilderValueBeforeRun();
		boolean result;
		long startTime = System.currentTimeMillis();
		Date currentTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateNowStr = sdf.format(currentTime);
		Application.getActiveApplication().getOutput().output(dateNowStr + " " + MapViewProperties.getString("MapCache_StartCreateCache"));
		if (this.checkBoxShowProcessBar.isSelected()) {
			FormProgress formProgress = new FormProgress();
			formProgress.setTitle(MapViewProperties.getString("MapCache_On") + this.getTitle());
			CacheProgressCallable cacheProgressCallable = new CacheProgressCallable(this.mapCacheBuilder, cmdType == SingleUpdateProcessClip);
			formProgress.doWork(cacheProgressCallable);
			result = cacheProgressCallable.getResult();
		} else {
			this.mapCacheBuilder.setIsAppending(cmdType == SingleUpdateProcessClip);
			result = this.mapCacheBuilder.build();
		}
		printResultInfo(result, System.currentTimeMillis() - startTime);
	}

	@Override
	public void dispose() {
		disposeInfo();
		super.dispose();
	}

	private void disposeInfo() {
		this.firstStepPane.removeEvents();
		this.nextStepPane.removeEvents();
		this.nextStepPane.dispose();
	}

	public MapCacheBuilder setMapCacheBuilderValueBeforeRun() {
		try {
			double[] outputScalevalues;
			HashMap<Double, String> scaleNames = new HashMap<>();
			if (firstStepPane.addScaleDropDown.isEnabled()) {
				outputScalevalues = new double[firstStepPane.currentMapCacheScale.size()];
				for (int i = 0; i < outputScalevalues.length; i++) {
					outputScalevalues[i] = firstStepPane.currentMapCacheScale.get(i);
					String scaleTitleName = String.valueOf(firstStepPane.localSplitTable.getValueAt(i, firstStepPane.COLUMN_TITLE)).trim();
					scaleNames.put(outputScalevalues[i], scaleTitleName);
				}
			} else {
				ArrayList<Integer> selectedIndex = new ArrayList<>();
				for (int i = 0; i < firstStepPane.globalSplitTable.getRowCount(); i++) {
					MultipleCheckboxItem multipleCheckboxItem = (MultipleCheckboxItem) firstStepPane.globalSplitTable.getValueAt(i, firstStepPane.COLUMN_INDEX);
					if (multipleCheckboxItem.getSelected()) {
						selectedIndex.add(i);
					}
				}
				outputScalevalues = new double[selectedIndex.size()];
				for (int i = 0; i < selectedIndex.size(); i++) {
					outputScalevalues[i] = firstStepPane.globalScaleSortKeys[selectedIndex.get(i)];
					scaleNames.put(firstStepPane.globalScaleSortKeys[selectedIndex.get(i)], firstStepPane.globalSplitScale.get(firstStepPane.globalScaleSortKeys[selectedIndex.get(i)]));
				}
			}
			this.mapCacheBuilder.setOutputScales(outputScalevalues);
			this.mapCacheBuilder.setOutputScaleCaptions(scaleNames);
			this.mapCacheBuilder.setVersion(MapCacheVersion.VERSION_50);
			this.mapCacheBuilder.setCacheName(firstStepPane.textFieldCacheName.getText());
			this.mapCacheBuilder.setOutputFolder(firstStepPane.fileChooserControlFileCache.getPath());
			this.mapCacheBuilder.setBounds(nextStepPane.cacheRangeBounds);
			this.mapCacheBuilder.setIsDeleteLogFile(false);
			if (this.mapCacheBuilder.getTilingMode() == MapTilingMode.LOCAL) {
				this.mapCacheBuilder.setIndexBounds(nextStepPane.indexRangeBounds);
			}
			if (nextStepPane.smSpinnerImageCompressionRatio.isEnabled()) {
				//modify
				this.mapCacheBuilder.setImageCompress(Integer.valueOf(nextStepPane.smSpinnerImageCompressionRatio.getValue().toString()));
			}
			this.mapCacheBuilder.setTransparent(nextStepPane.checkBoxBackgroundTransparency.isSelected());
			if (firstStepPane.checkBoxFilterSelectionObjectInLayer.isEnabled() && firstStepPane.checkBoxFilterSelectionObjectInLayer.isSelected()) {
				Layers layers = this.mapCacheBuilder.getMap().getLayers();
				if (nextStepPane.selectedGeometryAndLayer != null && nextStepPane.selectedGeometryAndLayer.size() > 0) {
					for (Layer layer : nextStepPane.selectedGeometryAndLayer.keySet()) {
						layers.remove(layer.getName());
					}
				}
			}
			if (nextStepPane.checkBoxFullFillCacheImage.isEnabled()) {
				GeoRegion geoRegion = null;
				if (!nextStepPane.checkBoxFullFillCacheImage.isSelected() && nextStepPane.selectedGeometryAndLayer != null && nextStepPane.selectedGeometryAndLayer.size() > 0) {
					for (Layer layer : nextStepPane.selectedGeometryAndLayer.keySet()) {
						java.util.List<Geometry> selectedGeometry;
						selectedGeometry = nextStepPane.selectedGeometryAndLayer.get(layer);
						for (int i = 0; i < selectedGeometry.size(); i++) {
							if (selectedGeometry.get(i).getType() == GeometryType.GEOREGION) {
								if (geoRegion == null) {
									geoRegion = (GeoRegion) selectedGeometry.get(i).clone();
								} else {
									geoRegion = (GeoRegion) Geometrist.union(geoRegion, selectedGeometry.get(i).clone());
								}
							}
						}
					}
				}
				this.mapCacheBuilder.setFillMargin(nextStepPane.checkBoxFullFillCacheImage.isSelected());
				if (geoRegion != null) {
					this.mapCacheBuilder.setClipRegion(geoRegion);
				}
			}
			TileStorageConnection tileStorageConnection = null;
			if (firstStepPane.comboBoxSaveType.getSelectedItem().toString().equals(MapViewProperties.getString("MapCache_SaveType_Compact")) && !firstStepPane.textFieldUserPassword.getText().isEmpty()) {
				this.mapCacheBuilder.setPassword(firstStepPane.textFieldUserPassword.getText());
			} else if (firstStepPane.comboBoxSaveType.getSelectedItem().toString().equals(MapViewProperties.getString("MapCache_SaveType_MongoDB")) || firstStepPane.comboBoxSaveType.getSelectedItem().toString().equals(MapViewProperties.getString("MapCache_SaveType_MongoDBMuti"))) {
				tileStorageConnection = new TileStorageConnection();
				tileStorageConnection.setServer(firstStepPane.textFieldServerName.getText());
				tileStorageConnection.setName(firstStepPane.textFieldCacheName.getText());
				tileStorageConnection.setDatabase(firstStepPane.comboBoxDatabaseName.getEditor().getItem().toString());
				tileStorageConnection.setStorageType(TileStorageType.MONGO);
				tileStorageConnection.setUser(firstStepPane.textFieldUserName.getText());
				tileStorageConnection.setPassword(firstStepPane.textFieldUserPassword.getText());
			}
			if (tileStorageConnection != null) {
				this.mapCacheBuilder.setConnectionInfo(tileStorageConnection);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex.toString());
		}
		return mapCacheBuilder;
	}

	public void setComponentsEnabled(boolean enabled) {
		this.firstStepPane.setComponentsEnabled(enabled);
		this.nextStepPane.setComponentsEnabled(enabled);
	}
}
