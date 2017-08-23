package com.supermap.desktop.dialog.cacheClip;

import com.supermap.data.processing.MapCacheBuilder;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.dialog.SmOptionPane;
import com.supermap.desktop.dialog.cacheClip.cache.CacheUtilities;
import com.supermap.desktop.dialog.cacheClip.cache.LogWriter;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.JFileChooserControl;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.mapping.Map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by xie on 2017/8/22.
 * 单任务/多任务缓存任务选择界面
 */
public class DialogCacheOperationView extends SmDialog {

	private int operationType;
	private int missionType;
	private final int SINGLE_CACHE = 0;
	private final int UPDATE_CACHE = 1;
	private final int RESUME_CACHE = 2;
	private final int MULTI_CACHE_NEW = 3;
	private final int MULTI_CACHE_EXECUTE = 4;
	//单任务界面控件
	private JRadioButton radioButtonNewMission;
	private JLabel labelNewMission;
	private JRadioButton radioButtonUpdateSciFile;
	private JLabel labelSciFileForUpdate;
	private JFileChooserControl fileChooserControlSciFileForUpdate;
	private JRadioButton radioButtonResumeSciFile;
	private JLabel labelSciFileForResume;
	private JFileChooserControl fileChooserControlSciFileForResume;
	private SmButton smButtonNextStep;
	private SmButton smButtonCancel;
	//多任务界面控件
	private JRadioButton radioButtonMultiMissionNew;
	private JLabel labelMultiMissionNew;
	private JRadioButton radioButtonMultiMissionExecute;
	private JLabel labelMultiMissionExecute;

	private Map map;
	private ItemListener singleMissionListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				setRadioButtonEnable();
				missionType = SINGLE_CACHE;
			}
		}
	};
	private ItemListener updateMissionListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				setRadioButtonEnable();
				missionType = UPDATE_CACHE;
			}
		}
	};
	private ItemListener resumeMissionListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				setRadioButtonEnable();
				missionType = RESUME_CACHE;
			}
		}
	};
	private ActionListener nextStepListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				buildCacheMission();
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
		}
	};
	private ItemListener multiCacheNewListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				missionType = MULTI_CACHE_NEW;
			}
		}
	};
	private ItemListener multiCacheExecuteListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				missionType = MULTI_CACHE_EXECUTE;
			}
		}
	};

	private void buildCacheMission() {
		switch (missionType) {
			case SINGLE_CACHE:
				//单任务执行
				singleCacheMission();
				break;
			case UPDATE_CACHE:
				//单进程更新
				updateCacheMission();
				break;
			case RESUME_CACHE:
				//单任务续传
				resumeCacheMission();
				break;
			case MULTI_CACHE_NEW:
				//新建多任务缓存
				multiCacheNewMission();
				break;
			case MULTI_CACHE_EXECUTE:
				//执行多任务缓存
				mulitCacheExecuteMission();
				break;
			default:
				break;
		}
	}

	private void mulitCacheExecuteMission() {
		DialogCacheOperationView.this.dispose();
		Map map = getMap();
		if (CacheUtilities.voladateDatasource()) {
			DialogCacheOperationView.this.dispose();
			Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_StartBuildCacheExecute"));
			String[] params = {"Multi", "null", "null", map.getName(), "null"};
			CacheUtilities.startProcess(params, DialogCacheBuilder.class.getName(), LogWriter.BUILD_CACHE);
//			new DialogCacheBuilder(DialogMapCacheClipBuilder.MultiProcessClip,"null").setVisible(true);
		}
	}

	private void multiCacheNewMission() {
		DialogCacheOperationView.this.dispose();
		Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_StartBuildCacheNew"));
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Map map = getMap();
				if (!CacheUtilities.dynamicEffectClosed(map)) {
					return;
				}
				if (CacheUtilities.voladateDatasource()) {
					MapCacheBuilder mapCacheBuilder = new MapCacheBuilder();
					Map newMap = new Map(Application.getActiveApplication().getWorkspace());
					newMap.fromXML(map.toXML());
					mapCacheBuilder.setMap(newMap);
					new DialogMapCacheClipBuilder(DialogMapCacheClipBuilder.MultiProcessClip, mapCacheBuilder).showDialog();
				}
			}
		});
		thread.start();
	}

	private ActionListener cancelListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			DialogCacheOperationView.this.dispose();
		}
	};

	public DialogCacheOperationView(int operationType) {
		this.operationType = operationType;
		init();
	}

	private void init() {
		initComponents();
		initLayout();
		initResources();
		registerEvents();
	}

	private void registerEvents() {
		removeEvents();
		switch (operationType) {
			case DialogMapCacheClipBuilder.SingleProcessClip:
				this.radioButtonNewMission.addItemListener(singleMissionListener);
				this.radioButtonUpdateSciFile.addItemListener(updateMissionListener);
				this.radioButtonResumeSciFile.addItemListener(resumeMissionListener);
				break;
			case DialogMapCacheClipBuilder.MultiProcessClip:
				this.radioButtonMultiMissionNew.addItemListener(multiCacheNewListener);
				this.radioButtonMultiMissionExecute.addItemListener(multiCacheExecuteListener);
				break;
			default:
				break;
		}
		this.smButtonNextStep.addActionListener(nextStepListener);
		this.smButtonCancel.addActionListener(cancelListener);
	}

	private void removeEvents() {
		switch (operationType) {
			case DialogMapCacheClipBuilder.SingleProcessClip:
				this.radioButtonNewMission.removeItemListener(singleMissionListener);
				this.radioButtonUpdateSciFile.removeItemListener(updateMissionListener);
				this.radioButtonResumeSciFile.removeItemListener(resumeMissionListener);
				break;
			case DialogMapCacheClipBuilder.MultiProcessClip:
				this.radioButtonMultiMissionNew.removeItemListener(multiCacheNewListener);
				this.radioButtonMultiMissionExecute.removeItemListener(multiCacheExecuteListener);
				break;
			default:
				break;
		}
		this.smButtonNextStep.removeActionListener(nextStepListener);
		this.smButtonCancel.removeActionListener(cancelListener);
	}

	private void resumeCacheMission() {
		File file = new File(fileChooserControlSciFileForResume.getPath());
		if (file.exists()) {
			File fileParent = file.getParentFile();
			boolean hasLogFile = false;
			if (fileParent.isDirectory()) {
				String[] resumFile = fileParent.list(new FilenameFilter() {

					public boolean accept(File dir, String name) {
						return name.equalsIgnoreCase("resumable.log");
					}
				});
				if (null != resumFile && resumFile.length > 0) {
					hasLogFile = true;
				}
			}
			if (!hasLogFile) {
				SmOptionPane pane = new SmOptionPane();
				pane.showConfirmDialog(MapViewProperties.getString("String_Error_LogNotExist"));
				return;
			}
			DialogCacheOperationView.this.dispose();
			MapCacheBuilder mapCacheBuilder = new MapCacheBuilder();
			mapCacheBuilder.fromConfigFile(file.getPath());
			mapCacheBuilder.setMap(getMap());
			DialogMapCacheClipBuilder mapCacheClipBuilder = new DialogMapCacheClipBuilder(DialogMapCacheClipBuilder.ResumeProcessClip, mapCacheBuilder);
			mapCacheClipBuilder.setComponentsEnabled(false);
			mapCacheClipBuilder.buttonOk.setEnabled(true);
			mapCacheClipBuilder.firstStepPane.labelConfigValue.setText(mapCacheBuilder.getCacheName());
			mapCacheClipBuilder.firstStepPane.setSciPath(file.getPath());
			mapCacheClipBuilder.firstStepPane.fileChooserControlFileCache.setPath(file.getParent());
			mapCacheClipBuilder.firstStepPane.resetComponentsInfo();
			mapCacheClipBuilder.showDialog();
		} else {
			new SmOptionPane().showErrorDialog(MapViewProperties.getString("String_CacheFileNotExist"));
		}
	}

	private void updateCacheMission() {
		File sciFile = new File(fileChooserControlSciFileForUpdate.getPath());
		if (sciFile.exists()) {
			DialogCacheOperationView.this.dispose();
			MapCacheBuilder mapCacheBuilder = new MapCacheBuilder();
			mapCacheBuilder.fromConfigFile(sciFile.getPath());
			mapCacheBuilder.setMap(getMap());
			DialogMapCacheClipBuilder builder = new DialogMapCacheClipBuilder(DialogMapCacheClipBuilder.SingleUpdateProcessClip, mapCacheBuilder);
			builder.firstStepPane.textFieldCacheName.setText(mapCacheBuilder.getCacheName());
			builder.firstStepPane.labelConfigValue.setText(mapCacheBuilder.getCacheName());
			builder.firstStepPane.setSciPath(sciFile.getPath());
			builder.firstStepPane.fileChooserControlFileCache.setPath(sciFile.getParentFile().getParent());
			builder.firstStepPane.resetComponentsInfo();
			builder.showDialog();
		} else {
			new SmOptionPane().showErrorDialog(MapViewProperties.getString("String_CacheFileNotExist"));
		}
	}

	private void singleCacheMission() {
		Map map = getMap();
		if (!CacheUtilities.dynamicEffectClosed(map)) {
			return;
		}
		DialogCacheOperationView.this.dispose();
		MapCacheBuilder mapCacheBuilder = new MapCacheBuilder();
		Map newMap = new Map(Application.getActiveApplication().getWorkspace());
		newMap.fromXML(map.toXML());
		mapCacheBuilder.setMap(newMap);
		new DialogMapCacheClipBuilder(DialogMapCacheClipBuilder.SingleProcessClip, mapCacheBuilder).showDialog();
	}

	private void initResources() {
		this.setTitle(CommonProperties.getString("String_CacheType"));
		this.smButtonNextStep.setText(ControlsProperties.getString("String_NextWay"));
		switch (operationType) {
			case DialogMapCacheClipBuilder.SingleProcessClip:
				this.radioButtonNewMission.setText(CommonProperties.getString("String_NewCacheMission"));
				this.labelNewMission.setText(CommonProperties.getString("String_NewCacheMissionTip"));
				this.radioButtonUpdateSciFile.setText(CommonProperties.getString("String_UpdateCacheSciFile"));
				this.radioButtonResumeSciFile.setText(CommonProperties.getString("String_ResumeCacheSciFile"));
				this.labelSciFileForUpdate.setText(CommonProperties.getString("String_SciFilePath"));
				this.labelSciFileForResume.setText(CommonProperties.getString("String_SciFilePath"));
				break;
			case DialogMapCacheClipBuilder.MultiProcessClip:
				this.radioButtonMultiMissionNew.setText(CommonProperties.getString("String_MultiCacheNew"));
				this.labelMultiMissionNew.setText(CommonProperties.getString("String_MultiCacheNewTip"));
				this.radioButtonMultiMissionExecute.setText(CommonProperties.getString("String_MultiCacheExecute"));
				this.labelMultiMissionExecute.setText(CommonProperties.getString("String_MultiCahceExecuteTip"));
				break;
			default:
				break;
		}
	}

	private void initLayout() {
		JPanel panelContent = (JPanel) this.getContentPane();
		panelContent.setLayout(new GridBagLayout());
		JPanel panelButton = new JPanel();
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(this.smButtonNextStep, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(5, 0, 10, 5));
		panelButton.add(this.smButtonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(5, 0, 10, 10));
		switch (operationType) {
			case DialogMapCacheClipBuilder.SingleProcessClip:
				panelContent.add(this.radioButtonNewMission, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 20, 5, 0));
				panelContent.add(this.labelNewMission, new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 30, 20, 10));
				panelContent.add(this.radioButtonUpdateSciFile, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 20, 5, 0));
				panelContent.add(this.labelSciFileForUpdate, new GridBagConstraintsHelper(0, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 30, 15, 0));
				panelContent.add(this.fileChooserControlSciFileForUpdate, new GridBagConstraintsHelper(1, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 15, 10).setWeight(1, 0));
				panelContent.add(this.radioButtonResumeSciFile, new GridBagConstraintsHelper(0, 4, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 20, 5, 10));
				panelContent.add(this.labelSciFileForResume, new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 30, 15, 0));
				panelContent.add(this.fileChooserControlSciFileForResume, new GridBagConstraintsHelper(1, 5, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 15, 10).setWeight(1, 0));
				panelContent.add(new JPanel(), new GridBagConstraintsHelper(0, 6, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
				panelContent.add(panelButton, new GridBagConstraintsHelper(0, 7, 3, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0));
				break;
			case DialogMapCacheClipBuilder.MultiProcessClip:
				panelContent.add(this.radioButtonMultiMissionNew, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 20, 5, 0));
				panelContent.add(this.labelMultiMissionNew, new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 30, 25, 10));
				panelContent.add(this.radioButtonMultiMissionExecute, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 20, 5, 0));
				panelContent.add(this.labelMultiMissionExecute, new GridBagConstraintsHelper(0, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 30, 25, 10));
				panelContent.add(new JPanel(), new GridBagConstraintsHelper(0, 4, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
				panelContent.add(panelButton, new GridBagConstraintsHelper(0, 5, 3, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0));
				break;
			default:
				break;
		}

	}

	private void initComponents() {
		switch (operationType) {
			case DialogMapCacheClipBuilder.SingleProcessClip:
				initSingleCacheComponents();
				break;
			case DialogMapCacheClipBuilder.MultiProcessClip:
				initMultiCacheComponents();
				break;
			default:
				break;
		}
		this.smButtonNextStep = new SmButton();
		this.smButtonCancel = (SmButton) ComponentFactory.createButtonCancel();
		this.setSize(560, 280);
		this.setLocationRelativeTo(null);
	}

	private void initMultiCacheComponents() {
		this.radioButtonMultiMissionNew = new JRadioButton();
		this.labelMultiMissionNew = new JLabel();
		this.radioButtonMultiMissionExecute = new JRadioButton();
		this.labelMultiMissionExecute = new JLabel();
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(radioButtonMultiMissionNew);
		buttonGroup.add(radioButtonMultiMissionExecute);
		this.radioButtonMultiMissionNew.setSelected(true);
		this.missionType = MULTI_CACHE_NEW;
	}

	private void initSingleCacheComponents() {
		this.radioButtonNewMission = new JRadioButton();
		this.labelNewMission = new JLabel();
		this.radioButtonUpdateSciFile = new JRadioButton();
		this.labelSciFileForUpdate = new JLabel();
		this.fileChooserControlSciFileForUpdate = new JFileChooserControl();
		String moduleName = "GetCacheUpdateConfigFile";
		if (!SmFileChoose.isModuleExist(moduleName)) {
			String fileFilters = SmFileChoose.createFileFilter(MapViewProperties.getString("MapCache_CacheConfigFile"), "sci");
			SmFileChoose.addNewNode(fileFilters, System.getProperty("user.dir"),
					MapViewProperties.getString("String_OpenColorTable"), moduleName, "OpenOne");
		}
		SmFileChoose smFileChoose = new SmFileChoose(moduleName);
		this.fileChooserControlSciFileForUpdate.setFileChooser(smFileChoose);
		this.radioButtonResumeSciFile = new JRadioButton();
		this.labelSciFileForResume = new JLabel();
		this.fileChooserControlSciFileForResume = new JFileChooserControl();
		String moduleNameForResume = "GetCacheResumeConfigFile";
		if (!SmFileChoose.isModuleExist(moduleNameForResume)) {
			String fileFilters = SmFileChoose.createFileFilter(MapViewProperties.getString("MapCache_CacheConfigFile"), "sci");
			SmFileChoose.addNewNode(fileFilters, System.getProperty("user.dir"),
					MapViewProperties.getString("String_OpenColorTable"), moduleNameForResume, "OpenOne");
		}
		SmFileChoose smFileChooseForResume = new SmFileChoose(moduleNameForResume);
		this.fileChooserControlSciFileForResume.setFileChooser(smFileChooseForResume);
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(this.radioButtonNewMission);
		buttonGroup.add(this.radioButtonUpdateSciFile);
		buttonGroup.add(this.radioButtonResumeSciFile);
		this.radioButtonNewMission.setSelected(true);
		this.missionType = SINGLE_CACHE;
		setRadioButtonEnable();
	}

	private void setRadioButtonEnable() {
		fileChooserControlSciFileForUpdate.setEnabled(radioButtonUpdateSciFile.isSelected() && !radioButtonNewMission.isSelected());
		fileChooserControlSciFileForResume.setEnabled(radioButtonResumeSciFile.isSelected() && !radioButtonNewMission.isSelected());
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}
}
