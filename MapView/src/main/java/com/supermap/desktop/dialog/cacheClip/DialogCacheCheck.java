package com.supermap.desktop.dialog.cacheClip;

import com.supermap.desktop.Application;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.dialog.SmOptionPane;
import com.supermap.desktop.dialog.cacheClip.cache.CacheUtilities;
import com.supermap.desktop.dialog.cacheClip.cache.CheckCache;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.ComponentBorderPanel.CompTitledPane;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.JFileChooserControl;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.utilities.FileUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by xie on 2017/5/10.
 * Dialog for checking cache
 */
public class DialogCacheCheck extends SmDialog {
	private JLabel labelTotalSciPath;
	private JLabel labelCheckBounds;
	private JLabel labelSciPath;
	private JLabel labelProcessCount;
	private JLabel labelMergeSciCount;
	private JFileChooserControl fileChooseTotalSciPath;
	private JFileChooserControl fileChooseCheckBounds;
	private JFileChooserControl fileChooseSciPath;
	private JTextField textFieldProcessCount;
	private JTextField textFieldMergeSciCount;
	private JRadioButton radioButtonSingleCheck;
	private JRadioButton radioButtonMultiCheck;
	private JCheckBox checkBoxSaveErrorData;
	private JCheckBox checkBoxCacheBuild;
	private JButton buttonOK;
	private JButton buttonCancel;
	private ActionListener cancelListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			DialogCacheCheck.this.dispose();
		}
	};
	private ActionListener checkListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			run(e);
		}
	};
	private ChangeListener singleCheckListener = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			if (radioButtonSingleCheck.isSelected()) {
				textFieldProcessCount.setText("0");
				textFieldProcessCount.setEnabled(false);
			} else {
				textFieldProcessCount.setEnabled(true);
			}
		}
	};

	public DialogCacheCheck() {
		super();
		init();
	}

	private void init() {
		initComponents();
		initResources();
		initLayout();
		registEvents();
		this.componentList.add(this.buttonOK);
		this.componentList.add(this.buttonCancel);
		this.componentList.add(this.fileChooseTotalSciPath);
		this.componentList.add(this.fileChooseCheckBounds);
		this.componentList.add(this.fileChooseSciPath);
		this.componentList.add(this.textFieldProcessCount);
		this.componentList.add(this.textFieldMergeSciCount);
		this.setFocusTraversalPolicy(this.policy);
	}

	private void registEvents() {
		removeEvents();
		this.buttonCancel.addActionListener(this.cancelListener);
		this.buttonOK.addActionListener(this.checkListener);
		this.radioButtonSingleCheck.addChangeListener(singleCheckListener);
	}

	private void removeEvents() {
		this.buttonCancel.removeActionListener(this.cancelListener);
		this.buttonOK.removeActionListener(this.checkListener);
		this.radioButtonSingleCheck.addChangeListener(singleCheckListener);
	}

	private void initComponents() {
		this.labelTotalSciPath = new JLabel();
		this.labelCheckBounds = new JLabel();
		this.labelSciPath = new JLabel();
		this.labelProcessCount = new JLabel();
		this.labelMergeSciCount = new JLabel();
		String moduleNameForCachePath = "ChooseCachePath";
		if (!SmFileChoose.isModuleExist(moduleNameForCachePath)) {
			SmFileChoose.addNewNode("", System.getProperty("user.dir"), GlobalParameters.getDesktopTitle(),
					moduleNameForCachePath, "GetDirectories");
		}
		SmFileChoose fileChooserForCachePath = new SmFileChoose(moduleNameForCachePath);
		this.fileChooseTotalSciPath = new JFileChooserControl();
		this.fileChooseTotalSciPath.setFileChooser(fileChooserForCachePath);
		this.fileChooseTotalSciPath.setPath(System.getProperty("user.dir"));
		String moduleNameForCheckBounds = "ChooseCheckBoundsFile";
		if (!SmFileChoose.isModuleExist(moduleNameForCheckBounds)) {
			String fileFilters = SmFileChoose.bulidFileFilters(SmFileChoose.createFileFilter(MapViewProperties.getString("String_FileType_GeoJson"), "geojson"));
			SmFileChoose.addNewNode(fileFilters, System.getProperty("user.dir"), GlobalParameters.getDesktopTitle(),
					moduleNameForCheckBounds, "OpenOne");
		}
		SmFileChoose fileChooserForCheckBounds = new SmFileChoose(moduleNameForCheckBounds);
		this.fileChooseCheckBounds = new JFileChooserControl();
		this.fileChooseCheckBounds.setFileChooser(fileChooserForCheckBounds);
		this.fileChooseCheckBounds.setPath(System.getProperty("user.dir"));
		String moduleNameForSciPath = "ChooseSciPath";
		if (!SmFileChoose.isModuleExist(moduleNameForSciPath)) {
			SmFileChoose.addNewNode("", System.getProperty("user.dir"), GlobalParameters.getDesktopTitle(),
					moduleNameForSciPath, "GetDirectories");
		}
		SmFileChoose fileChooserForSciPath = new SmFileChoose(moduleNameForSciPath);
		this.fileChooseSciPath = new JFileChooserControl();
		this.fileChooseSciPath.setFileChooser(fileChooserForSciPath);
		this.fileChooseSciPath.setPath(System.getProperty("user.dir"));
		this.textFieldProcessCount = new JTextField();
		this.textFieldMergeSciCount = new JTextField();
		this.radioButtonSingleCheck = new JRadioButton();
		this.radioButtonMultiCheck = new JRadioButton();
		ButtonGroup group = new ButtonGroup();
		group.add(radioButtonSingleCheck);
		group.add(radioButtonMultiCheck);
		this.radioButtonMultiCheck.setSelected(true);
		this.checkBoxSaveErrorData = new JCheckBox();
		this.checkBoxSaveErrorData.setSelected(true);
		this.checkBoxCacheBuild = new JCheckBox();
		this.checkBoxCacheBuild.setSelected(true);
		this.buttonOK = ComponentFactory.createButtonOK();
		this.buttonCancel = ComponentFactory.createButtonCancel();
		this.setSize(520, 310);
		this.setLocationRelativeTo(null);
	}

	private void initResources() {
		this.labelTotalSciPath.setText(MapViewProperties.getString("String_TotalSciPath"));
		this.labelCheckBounds.setText(MapViewProperties.getString("String_CheckBounds"));
		this.labelSciPath.setText(MapViewProperties.getString("String_SciPath"));
		this.labelProcessCount.setText(MapViewProperties.getString("String_NewProcessCount"));
		this.labelMergeSciCount.setText(MapViewProperties.getString("String_MergeSciCount"));
		this.textFieldProcessCount.setText("3");
		this.textFieldMergeSciCount.setText("1");
		this.radioButtonSingleCheck.setText(MapViewProperties.getString("String_CacheCheck_Single"));
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
		panelMultiCheck.add(this.labelSciPath, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 10, 5, 10));
		panelMultiCheck.add(this.fileChooseSciPath, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 0, 5, 10).setWeight(1, 0));
		panelMultiCheck.add(this.labelProcessCount, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
		panelMultiCheck.add(this.textFieldProcessCount, new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));
		panelMultiCheck.add(this.labelMergeSciCount, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
		panelMultiCheck.add(this.textFieldMergeSciCount, new GridBagConstraintsHelper(1, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));
		CompTitledPane compTitledPane = new CompTitledPane(this.radioButtonMultiCheck, panelMultiCheck);
		JPanel panelButton = new JPanel();
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(this.buttonOK, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(0, 0, 10, 5));
		panelButton.add(this.buttonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(0, 0, 10, 10));
		panelContent.add(this.labelTotalSciPath, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(15, 20, 5, 10));
		panelContent.add(this.fileChooseTotalSciPath, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(15, 0, 5, 10).setWeight(1, 0));
		panelContent.add(this.labelCheckBounds, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 20, 5, 10));
		panelContent.add(this.fileChooseCheckBounds, new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));
		panelContent.add(this.radioButtonSingleCheck, new GridBagConstraintsHelper(0, 2, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 20, 0, 10));
		panelContent.add(compTitledPane, new GridBagConstraintsHelper(0, 3, 3, 3).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 10, 5, 10).setWeight(3, 0));
		panelContent.add(this.checkBoxSaveErrorData, new GridBagConstraintsHelper(0, 7, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 20, 0, 10));
		panelContent.add(this.checkBoxCacheBuild, new GridBagConstraintsHelper(2, 7, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 0, 10));
		panelContent.add(new JPanel(), new GridBagConstraintsHelper(0, 8, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		this.add(panelButton, new GridBagConstraintsHelper(0, 9, 3, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0));
	}

	private boolean validateValue(String sciPath, String processCount, String cachePath) {
		boolean result = true;
		File sciDirectory = new File(sciPath);
		if (StringUtilities.isNullOrEmpty(sciPath) || !FileUtilities.isFilePath(sciPath) || !hasSciFiles(sciDirectory)) {
			result = false;
		}
		if (StringUtilities.isNullOrEmpty(cachePath) || !FileUtilities.isFilePath(cachePath)) {
			result = false;
		}
		if (StringUtilities.isNullOrEmpty(processCount) || !(StringUtilities.isInteger(processCount) || processCount.equals("0"))) {
			result = false;
		}
		return result;
	}

	private boolean hasSciFiles(File sciDirectory) {
		return sciDirectory.list(CacheUtilities.getFilter()).length > 0 ? true : false;
	}

	private void run(ActionEvent e) {
		try {
			String cacheRoot = fileChooseTotalSciPath.getPath();
			String sciPath = fileChooseSciPath.getPath();
			String processCount = textFieldProcessCount.getText();
			String mergeTaskCount = textFieldMergeSciCount.getText();

			String saveErrorData = checkBoxSaveErrorData.isSelected() ? "true" : "false";
			String geoJsonFile = fileChooseCheckBounds.getPath();
			String[] params = {cacheRoot, sciPath, mergeTaskCount, saveErrorData, geoJsonFile};

//            CacheCheck.main(params);
			if (validateValue(sciPath, mergeTaskCount, cacheRoot)) {
				((JButton) e.getSource()).setCursor(new Cursor(Cursor.WAIT_CURSOR));
				CheckCache checkCache = new CheckCache();
				checkCache.startProcess(Integer.valueOf(processCount), params);
				((JButton) e.getSource()).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				dialogDispose();
				boolean cacheBuild = checkBoxCacheBuild.isSelected();
				if (cacheBuild) {
					File cacheFile = new File(cacheRoot);
					File sciFile = new File(sciPath);
					File sciBuildFile = sciFile.getParentFile();
					File[] files = sciBuildFile.listFiles();
					File errorFile = null;
					for (int i = 0; i < files.length; i++) {
						if (files[i].getName().equals("error")) {
							errorFile = files[i];
							break;
						}
					}
					if (null != errorFile && errorFile.listFiles().length > 0) {
						DialogCacheBuilder cacheBuilder = new DialogCacheBuilder();
						cacheBuilder.fileChooserTaskPath.setPath(errorFile.getAbsolutePath());
						cacheBuilder.textFieldMapName.setText(cacheFile.getName());
						cacheBuilder.showDialog();
					} else {
						Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_CacheCheckSuccess"));
					}

				}
			} else {
				new SmOptionPane().showConfirmDialog(MapViewProperties.getString("String_ParamsException"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void dialogDispose() {
		DialogCacheCheck.this.dispose();
	}
}
