package com.supermap.desktop.importUI;


import com.supermap.data.conversion.ImportSetting;
import com.supermap.desktop.Interface.*;
import com.supermap.desktop.baseUI.PanelResultset;
import com.supermap.desktop.baseUI.PanelSourceInfo;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.iml.ImportInfo;
import com.supermap.desktop.iml.PanelTransformFactory;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by xie on 2016/9/30.
 */
public class PanelImport extends JPanel implements IPanelImport {

	private ArrayList<PanelImport> panelImports;
	private ImportSetting importSetting;
	private IImportSettingResultset resultset;
	private IImportSettingSourceInfo sourceInfo;
	private IImportSetttingTransform transform;
	private JDialog owner;
	private ImportInfo importInfo;
	private int panelImportType;

	@Override
	public ImportInfo getImportInfo() {
		return importInfo;
	}

	@Override
	public void dispose() {
		if (null != importInfo) {
			importInfo = null;
		}
		if (null != panelImports) {
			panelImports = null;
		}
		resultset = null;
		sourceInfo = null;
		transform = null;
	}

	public PanelImport(JDialog owner, ImportInfo importInfo) {
		this.owner = owner;
		this.importInfo = importInfo;
		initComponents();
		initLayerout();
	}

	public PanelImport(ArrayList<PanelImport> panelImports) {
		this.panelImports = panelImports;
		initComponents();
		initLayerout();
	}


	@Override
	public void initComponents() {
		IPanelTransformFactory transformFactory = new PanelTransformFactory();
		if (null != this.importInfo) {
			this.importSetting = importInfo.getImportSetting();
			if (null == importSetting) {
				return;
			}
			this.transform = transformFactory.createPanelTransform(importSetting);
			this.resultset = new PanelResultset(PanelImport.this, importInfo);
			this.sourceInfo = new PanelSourceInfo(owner, importSetting);
		} else if (null != this.panelImports) {
			panelImportType = transformFactory.getImportSettingsType(panelImports);
			this.transform = transformFactory.createPanelTransform(panelImports);
			this.resultset = new PanelResultset(PanelImport.this, panelImports, panelImportType);
			this.sourceInfo = new PanelSourceInfo(panelImports, panelImportType);
		}
		ComponentUIUtilities.setName(this.owner, "PanelImport_owner");
	}

	@Override
	public void initLayerout() {
		JPanel panelContents = new JPanel();
		this.setLayout(new GridBagLayout());
		this.add(panelContents, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL));
		panelContents.setLayout(new GridBagLayout());

		panelContents.add((Component) this.resultset, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0).setInsets(2, 10, 2, 10).setFill(GridBagConstraints.HORIZONTAL));
		if (null != transform) {
			panelContents.add((Component) this.transform, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0).setInsets(2, 10, 2, 10).setFill(GridBagConstraints.HORIZONTAL));
		}
		panelContents.add((Component) this.sourceInfo, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0).setInsets(2, 10, 2, 10).setFill(GridBagConstraints.HORIZONTAL));
	}

	@Override
	public void registEvents() {
	}

	@Override
	public void removeEvents() {
		this.transform.removeEvents();
		this.resultset.removeEvents();
		this.sourceInfo.removeEvents();
	}

	public IImportSettingResultset getResultset() {
		return resultset;
	}

	public IImportSettingSourceInfo getSourceInfo() {
		return sourceInfo;
	}

	public IImportSetttingTransform getTransform() {
		return transform;
	}

	public int getPanelImportType() {
		return panelImportType;
	}
}
