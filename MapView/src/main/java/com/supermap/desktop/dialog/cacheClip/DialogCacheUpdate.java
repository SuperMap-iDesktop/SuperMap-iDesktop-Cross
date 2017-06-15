package com.supermap.desktop.dialog.cacheClip;

import com.supermap.data.processing.MapCacheBuilder;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.JFileChooserControl;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by xie on 2017/6/12.
 */
public class DialogCacheUpdate extends SmDialog {
	private JLabel labelTip;
	private JFileChooserControl sciFileChooserControl;
	private JButton buttonOK;
	private JButton buttonCancel;
	private ActionListener okListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String selectSciPath = sciFileChooserControl.getPath();
			MapCacheBuilder mapCacheBuilder = new MapCacheBuilder();
			mapCacheBuilder.fromConfigFile(selectSciPath);
			if (!StringUtilities.isNullOrEmpty(mapCacheBuilder.getCacheName())) {
				dispose();
				DialogMapCacheClipBuilder builder = new DialogMapCacheClipBuilder(DialogMapCacheClipBuilder.UpdateProcessClip, mapCacheBuilder);
//				builder.firstStepPane.fileChooserControlFileCache.setPath(selectSciPath.substring(0, selectSciPath.indexOf(mapCacheBuilder.getCacheName())));
				builder.firstStepPane.textFieldCacheName.setText(mapCacheBuilder.getCacheName());
				builder.firstStepPane.labelConfigValue.setText(mapCacheBuilder.getCacheName());
				builder.showDialog();
			}
		}
	};
	private ActionListener cancelListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	};

	public DialogCacheUpdate() {
		init();
	}

	private void registEvents() {
		removeEvents();
		this.buttonOK.addActionListener(okListener);
		this.buttonCancel.addActionListener(cancelListener);
	}

	private void removeEvents() {
		this.buttonOK.removeActionListener(okListener);
		this.buttonCancel.removeActionListener(cancelListener);
	}

	private void init() {
		initComponents();
		initLayout();
		registEvents();
		this.setSize(420, 120);
		this.setLocationRelativeTo(null);
	}

	private void initLayout() {
		JPanel panelContent = (JPanel) this.getContentPane();
		JPanel panelButton = new JPanel();
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(this.buttonOK, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(0, 0, 10, 5));
		panelButton.add(this.buttonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(0, 0, 10, 10));

		panelContent.setLayout(new GridBagLayout());
		panelContent.add(this.labelTip, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 10));
		panelContent.add(this.sciFileChooserControl, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 0, 0, 10).setWeight(1, 0));
		panelContent.add(new JPanel(), new GridBagConstraintsHelper(0, 2, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		this.add(panelButton, new GridBagConstraintsHelper(0, 3, 3, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0));
	}

	private void initComponents() {
		this.setTitle(MapViewProperties.getString("String_UpdateClip"));
		this.labelTip = new JLabel(MapViewProperties.getString("String_UpdateSci"));
		String sciFileChooseModule = "updateSciModule";
		if (!SmFileChoose.isModuleExist(sciFileChooseModule)) {
			String sciFilter = SmFileChoose.bulidFileFilters(
					SmFileChoose.createFileFilter(MapViewProperties.getString("String_SciFileType"), "sci"));
			SmFileChoose.addNewNode(sciFilter, System.getProperty("user.dir"), GlobalParameters.getDesktopTitle(),
					sciFileChooseModule, "OpenOne");
		}
		SmFileChoose fileChoose = new SmFileChoose(sciFileChooseModule);
		this.sciFileChooserControl = new JFileChooserControl();
		this.sciFileChooserControl.setFileChooser(fileChoose);
		this.buttonOK = ComponentFactory.createButtonOK();
		this.buttonCancel = ComponentFactory.createButtonCancel();
	}

	@Override
	public void dispose() {
		removeEvents();
		super.dispose();
	}
}
