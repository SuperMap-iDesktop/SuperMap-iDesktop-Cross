package com.supermap.desktop.controls.property.dataset;

import com.supermap.data.DatasetImageCollection;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.property.AbstractPropertyControl;
import com.supermap.desktop.enums.PropertyType;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import java.awt.*;

/**
 * @author XiaJt
 */
public class ImageCollectionPropertyControl extends AbstractPropertyControl {
	private DatasetImageCollection datasetImageCollection;

	private JLabel labelBandCount = new JLabel();
	private SmTextFieldLegit smTextFieldBandCount = new SmTextFieldLegit();
	private JLabel labelNoValue = new JLabel();
	private SmTextFieldLegit smTextFieldNoValue = new SmTextFieldLegit();

	private JLabel labelHasPyramid = new JLabel();
	private SmTextFieldLegit smTextFieldHasPyramid = new SmTextFieldLegit();
	private JLabel labelDatasetCount = new JLabel();
	private SmTextFieldLegit smTextFieldDatasetCount = new SmTextFieldLegit();
	private SmButton smButtonSetDatasets = new SmButton();

	private JLabel labelPixelType = new JLabel();
	private SmTextFieldLegit smTextFieldPixelType = new SmTextFieldLegit();
	private JLabel labelShowBounds = new JLabel();
	private SmButton smButtonSetShowBounds = new SmButton();
	private SmButton smButtonClear = new SmButton();

	private SmButton smButtonReset = new SmButton();
	private SmButton smButtonApply = new SmButton();

	private double noValue = 0.0;

	public ImageCollectionPropertyControl(DatasetImageCollection datasetImageCollection) {
		super(CommonProperties.getString("String_DatasetType_ImageCollection"));
		initializeComponents();
		initLayout();
		initializeResources();
	}

	private void initializeComponents() {
		smButtonSetDatasets.setVisible(false);
	}

	private void initLayout() {
		this.setLayout(new GridBagLayout());
		//@formatter:off
		this.add(labelBandCount,       new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(20, 20, 0, 0));
		this.add(smTextFieldBandCount, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(20, 10, 0, 0));
		this.add(labelNoValue,         new GridBagConstraintsHelper(2, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(20, 20, 0, 0));
		this.add(smTextFieldNoValue,   new GridBagConstraintsHelper(3, 0, 2, 1).setWeight(0, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(20, 10, 0, 20));

		this.add(labelHasPyramid,         new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 20, 0, 0));
		this.add(smTextFieldHasPyramid,   new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 10, 0, 0));
		this.add(labelDatasetCount,       new GridBagConstraintsHelper(2, 1, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 20, 0, 0));
		this.add(smTextFieldDatasetCount, new GridBagConstraintsHelper(3, 1, 2, 1).setWeight(0, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 10, 0, 20));
//		this.add(smButtonSetDatasets,     new GridBagConstraintsHelper(4, 1, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 5, 0, 10));

		this.add(labelPixelType,        new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 20, 20, 0));
		this.add(smTextFieldPixelType,  new GridBagConstraintsHelper(1, 2, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 10, 20, 0));
		this.add(labelShowBounds,       new GridBagConstraintsHelper(2, 2, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 20, 20, 0));
		this.add(smButtonSetShowBounds, new GridBagConstraintsHelper(3, 2, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 20, 0).setIpad(10,0));
		this.add(smButtonClear,         new GridBagConstraintsHelper(4, 2, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 20, 20));

		this.add(new JPanel(), new GridBagConstraintsHelper(0,3,5,1).setWeight(1,1).setFill(GridBagConstraints.BOTH));

		JPanel jPanelButton = new JPanel();
		jPanelButton.setLayout(new GridBagLayout());
		jPanelButton.add(smButtonReset, new GridBagConstraintsHelper(0,0,1,1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).setWeight(1,1).setInsets(0,10,10,5));
		jPanelButton.add(smButtonApply, new GridBagConstraintsHelper(1,0,1,1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).setWeight(0,1).setInsets(0,0,10,20));

		this.add(jPanelButton, new GridBagConstraintsHelper(0,4,5,1).setWeight(1,0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER));
		//@formatter:on

	}

	private void initializeResources() {
		this.labelBandCount.setText(ControlsProperties.getString("String_LabelBandsSize"));
		this.labelNoValue.setText(ControlsProperties.getString("String_LabelNoValue"));
		this.labelHasPyramid.setText(ControlsProperties.getString("String_LabelPyramid"));
		this.labelDatasetCount.setText(ControlsProperties.getString("String_Label_DatasetCount"));
		this.smButtonSetDatasets.setText(CommonProperties.getString(CommonProperties.Button_Setting));
		this.labelPixelType.setText(ControlsProperties.getString("String_LabelPixelFormat"));
		this.labelShowBounds.setText(ControlsProperties.getString("String_LabelClipRegion"));
		this.smButtonSetShowBounds.setText(CommonProperties.getString(CommonProperties.Button_Setting));
		this.smButtonClear.setText(CoreProperties.getString(CoreProperties.Clear));
		this.smButtonReset.setText(CommonProperties.getString(CommonProperties.Reset));
		this.smButtonApply.setText(CommonProperties.getString(CommonProperties.Apply));
	}

	@Override
	public void refreshData() {

	}

	@Override
	public PropertyType getPropertyType() {
		return null;
	}

	public void setDatasetImageCollection(DatasetImageCollection datasetImageCollection) {
		this.datasetImageCollection = datasetImageCollection;
	}
}
