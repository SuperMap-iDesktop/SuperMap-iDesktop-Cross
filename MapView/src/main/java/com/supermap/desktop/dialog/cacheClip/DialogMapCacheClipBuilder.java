package com.supermap.desktop.dialog.cacheClip;

import com.supermap.data.GeoRegion;
import com.supermap.data.Geometrist;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Rectangle2D;
import com.supermap.data.processing.MapCacheBuilder;
import com.supermap.data.processing.MapCacheVersion;
import com.supermap.data.processing.MapTilingMode;
import com.supermap.data.processing.TileSize;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.dialog.SmOptionPane;
import com.supermap.desktop.dialog.cacheClip.cache.CacheUtilities;
import com.supermap.desktop.dialog.cacheClip.cache.TaskBuilder;
import com.supermap.desktop.mapview.MapCache.CacheProgressCallable;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.ChooseTable.MultipleCheckboxItem;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.progress.FormProgress;
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
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimerTask;

/**
 * Created by xie on 2017/4/26.
 * Main dialog for setting  map cache
 */
public class DialogMapCacheClipBuilder extends SmDialog {
	private static final int INDEX_MONGOTYPE = 2;
	private boolean singleProcessClip;
	private boolean firstStepEnabled = true;
	private boolean nextStepEnabled = true;
	private boolean resumeAble = false;
	private String tasksSize = "5";
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
			if (resumeAble) {
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


	public DialogMapCacheClipBuilder(boolean singleProcessClip, MapCacheBuilder mapCacheBuilder) {
		super();
		this.mapCacheBuilder = mapCacheBuilder;
		this.currentMap = this.mapCacheBuilder.getMap();
		this.singleProcessClip = singleProcessClip;
		init();
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
		if (singleProcessClip) {
			this.setTitle(MessageFormat.format(MapViewProperties.getString("MapCache_Title"), this.currentMap.getName()));
			this.buttonOk.setText(MapViewProperties.getString("String_BatchAddColorTableOKButton"));
			this.checkBoxShowProcessBar.setVisible(false);
		} else {
			this.setTitle(MessageFormat.format(MapViewProperties.getString("MapCache_Title_TaskBuilder"), this.currentMap.getName()));
			this.buttonOk.setText(MapViewProperties.getString("String_Title_Split"));
			this.checkBoxShowProcessBar.setVisible(false);
		}
		this.checkBoxAutoClosed.setText(MapViewProperties.getString("MapCache_AutoCloseDailog"));
		this.checkBoxShowProcessBar.setText(MapViewProperties.getString("MapCache_ShowProgressBar"));
		this.buttonStep.setText(ControlsProperties.getString("String_NextWay"));
	}

	private void initComponents() {
		this.mapCacheBuilder.setBounds(this.currentMap.getBounds());
		this.mapCacheBuilder.setIndexBounds(this.currentMap.getBounds());
		this.firstStepPane = new FirstStepPane(this.mapCacheBuilder, this);
		this.nextStepPane = new NextStepPane(this, this.mapCacheBuilder, singleProcessClip);
		this.checkBoxAutoClosed = new JCheckBox();
		this.checkBoxShowProcessBar = new JCheckBox();
		this.buttonStep = new SmButton();
		this.buttonOk = new SmButton();
		this.buttonCancel = ComponentFactory.createButtonCancel();
		this.checkBoxAutoClosed.setSelected(true);
		this.checkBoxShowProcessBar.setSelected(true);
	}

	private boolean isCacheFolderSave() {
		boolean result = true;
		File file = new File(firstStepPane.fileChooserControlFileCache.getPath() + firstStepPane.textFieldCacheName.getText());
		if (file.exists() || file.isDirectory()) {
			SmOptionPane smOptionPane = new SmOptionPane();
			smOptionPane.showErrorDialog("\"" + firstStepPane.textFieldCacheName.getText() + "\"" + MapViewProperties.getString("MapCache_FileIsExitWarning"));
			result = false;
		}
		return result;
	}

	//Change panel
	private void changePanel(boolean flag) {
		buttonStep.setText(flag == true ? ControlsProperties.getString("String_LastWay") : ControlsProperties.getString("String_NextWay"));
		getContentPane().remove(flag == true ? firstStepPane : nextStepPane);
		getContentPane().add(flag == true ? nextStepPane : firstStepPane, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		if (flag && firstStepPane.importCacheConfigs) {
			nextStepPane.imagePixelChanged = false;
			nextStepPane.comboBoxImageType.setSelectedItem(firstStepPane.mapCacheBuilder.getTileFormat().toString());
			TileSize tempSize = firstStepPane.mapCacheBuilder.getTileSize();
			if (tempSize == TileSize.SIZE64) {
				nextStepPane.comboBoxPixel.setSelectedItem("64*64");
			} else if (tempSize == TileSize.SIZE128) {
				nextStepPane.comboBoxPixel.setSelectedItem("128*128");
			} else if (tempSize == TileSize.SIZE256) {
				nextStepPane.comboBoxPixel.setSelectedItem("256*256");
			} else if (tempSize == TileSize.SIZE512) {
				nextStepPane.comboBoxPixel.setSelectedItem("512*512");
			} else if (tempSize == TileSize.SIZE1024) {
				nextStepPane.comboBoxPixel.setSelectedItem("1024*1024");
			}
			nextStepPane.smSpinnerImageCompressionRatio.setValue(firstStepPane.mapCacheBuilder.getImageCompress());
			Rectangle2D indexBounds = null;
			if (mapCacheBuilder.getTilingMode() == MapTilingMode.LOCAL) {
				indexBounds = firstStepPane.mapCacheBuilder.getIndexBounds();
			} else {
				indexBounds = new Rectangle2D(-180, -90, 180, 90);
			}
			nextStepPane.panelCacheRange.setAsRectangleBounds(firstStepPane.mapCacheBuilder.getBounds());
			nextStepPane.panelIndexRange.setAsRectangleBounds(indexBounds);
			nextStepPane.checkBoxBackgroundTransparency.setSelected(firstStepPane.mapCacheBuilder.isTransparent());
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
			long startTime = System.currentTimeMillis();
			if (this.checkBoxShowProcessBar.isSelected()) {
				FormProgress formProgress = new FormProgress();
				formProgress.setTitle(MapViewProperties.getString("MapCache_On") + this.getTitle());
				CacheProgressCallable cacheProgressCallable = new CacheProgressCallable(this.mapCacheBuilder, true);
				formProgress.doWork(cacheProgressCallable);
				result = cacheProgressCallable.getResult();
			} else {
				this.mapCacheBuilder.setFillMargin(true);
				this.mapCacheBuilder.setIsAppending(true);
				result = this.mapCacheBuilder.build();
			}
			long endTime = System.currentTimeMillis();
			String time = String.valueOf((endTime - startTime) / 1000.0);
			printResultInfo(result, time);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void printResultInfo(boolean result, String time) {
		if (result) {
			Application.getActiveApplication().getOutput().output("\"" + this.mapCacheBuilder.getMap().getName() + "\"" + MapViewProperties.getString("MapCache_StartCreateSuccessed"));
			if (!firstStepPane.fileChooserControlFileCache.getPath().substring(firstStepPane.fileChooserControlFileCache.getPath().length() - 1).equals("\\")) {
				Application.getActiveApplication().getOutput().output(MapViewProperties.getString("MapCache_FloderIs") + " " + firstStepPane.fileChooserControlFileCache.getPath() + "\\" + firstStepPane.textFieldCacheName.getText());
			} else {
				Application.getActiveApplication().getOutput().output(MapViewProperties.getString("MapCache_FloderIs") + " " + firstStepPane.fileChooserControlFileCache.getPath() + firstStepPane.textFieldCacheName.getText());
			}
		} else {
			Application.getActiveApplication().getOutput().output("\"" + this.mapCacheBuilder.getMap().getName() + "\"" + MapViewProperties.getString("MapCache_StartCreateFailed"));
		}
		Application.getActiveApplication().getOutput().output(MapViewProperties.getString("MapCache_Time") + time + " " + MapViewProperties.getString("MapCache_ShowTime"));
		if (this.checkBoxAutoClosed.isSelected()) {
			disposeInfo();
			this.mapCacheBuilder.dispose();
		}
	}

	private void run() {
		try {
			if (singleProcessClip) {
				singleProcessBuilder();
			} else {
				String tasksPath = nextStepPane.fileChooserControlTaskPath.getPath();
				String filePath = firstStepPane.fileChooserControlFileCache.getPath();
				tasksPath = CacheUtilities.replacePath(tasksPath);
				String sciPath = CacheUtilities.replacePath(filePath, mapCacheBuilder.getCacheName() + ".sci");
				setMapCacheBuilderValueBeforeRun();
				//SaveType==MongoType,build some cache for creating a database
				if (firstStepPane.comboBoxSaveType.getSelectedIndex() == INDEX_MONGOTYPE) {
					final Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							mapCacheBuilder.buildWithoutConfigFile();
						}
					});
					thread.start();
					final java.util.Timer timer = new java.util.Timer();
					TimerTask task = new TimerTask() {
						@Override
						public void run() {
							thread.interrupt();
							timer.cancel();
						}
					};
					long delay = 20 * 1000;
					timer.schedule(task, delay);
				}
				boolean result = mapCacheBuilder.toConfigFile(sciPath);
				if (result) {
					String[] params = {sciPath, tasksPath, tasksSize, canudb};
					this.buttonOk.setCursor(new Cursor(Cursor.WAIT_CURSOR));
					boolean buildTaskResult = TaskBuilder.buildTask(params);
					if (!buildTaskResult) {
						this.buttonOk.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						return;
					}
					this.buttonOk.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					disposeInfo();
					DialogCacheBuilder dialogCacheBuilder = new DialogCacheBuilder();
					dialogCacheBuilder.textFieldMapName.setText(this.currentMap.getName());
					String targetTaskPath = CacheUtilities.replacePath(tasksPath,"task");
					File oldFile = new File(sciPath);
					dialogCacheBuilder.setSciFile(oldFile);
					dialogCacheBuilder.fileChooserTotalTaskPath.setPath(sciPath);
					dialogCacheBuilder.fileChooserTaskPath.setPath(targetTaskPath);
					dialogCacheBuilder.fileChooserWorkspacePath.setPath(Application.getActiveApplication().getWorkspace().getConnectionInfo().getServer());
					dialogCacheBuilder.showDialog();
				}
				if (this.checkBoxAutoClosed.isSelected()) {
					disposeInfo();
					this.mapCacheBuilder.dispose();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Application.getActiveApplication().getOutput().output(e);
		}

	}

	private void singleProcessBuilder() {
		if (!isCacheFolderSave()) {
			if (buttonStep.getText().equals(ControlsProperties.getString("String_NextWay"))) {
				changePanel(buttonStep.getText().equals(ControlsProperties.getString("String_NextWay")));
			}
			getContentPane().repaint();
			firstStepPane.textFieldCacheName.requestFocus();
			return;
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
			CacheProgressCallable cacheProgressCallable = new CacheProgressCallable(this.mapCacheBuilder, resumeAble);
			formProgress.doWork(cacheProgressCallable);
			result = cacheProgressCallable.getResult();
		} else {
			result = this.mapCacheBuilder.build();
		}
		long endTime = System.currentTimeMillis();
		String time = String.valueOf((endTime - startTime) / 1000.0);
		printResultInfo(result, time);
	}

	private void disposeInfo() {
		this.firstStepPane.removeEvents();
		this.nextStepPane.removeEvents();
		nextStepPane.dispose();
		this.dispose();
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
			this.mapCacheBuilder.setImageCompress(Integer.valueOf(nextStepPane.smSpinnerImageCompressionRatio.getValue().toString()));
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

	public void setResumeAble(boolean resumeAble) {
		this.resumeAble = resumeAble;
	}

}
