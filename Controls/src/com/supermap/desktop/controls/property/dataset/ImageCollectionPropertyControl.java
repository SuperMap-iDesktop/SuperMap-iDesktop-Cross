package com.supermap.desktop.controls.property.dataset;

import com.supermap.data.DatasetImageCollection;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IPropertyManager;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.property.AbstractPropertyControl;
import com.supermap.desktop.enums.PropertyType;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilties.DoubleUtilities;
import com.supermap.desktop.utilties.PixelFormatUtilities;
import com.supermap.desktop.utilties.StringUtilities;
import com.supermap.mapping.Layers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;

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

	private double noValue = 0;

	private ActionListener showBoundsListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			buttonSetClipRegionClicked();
		}
	};
	private ActionListener buttonResetListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			buttonResetClicked();
		}


	};
	private ActionListener buttonApplyListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			buttonApplyClicked();
		}
	};
	private ActionListener buttonClearListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			buttonClearClipRegionClicked();
		}
	};
	;


	public ImageCollectionPropertyControl(DatasetImageCollection datasetImageCollection) {
		super(CommonProperties.getString("String_DatasetType_ImageCollection"));
		initializeComponents();
		initLayout();
		initializeResources();
		setDatasetImageCollection(datasetImageCollection);
	}

	private void initializeComponents() {
		smTextFieldNoValue.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				String currentNoValue = smTextFieldNoValue.getText();
				if (StringUtilities.isNullOrEmpty(currentNoValue)) {
					return false;
				}
				if (currentNoValue.contains("d")) {
					return false;
				} else
					try {
						Double aDouble = Double.valueOf(currentNoValue);
					} catch (Exception e) {
						return false;
					}
				noValueChanged();
				return true;
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		});
		smButtonSetDatasets.setVisible(false);
		smTextFieldBandCount.setEditable(false);
		smTextFieldHasPyramid.setEditable(false);
		smTextFieldDatasetCount.setEditable(false);
		smTextFieldPixelType.setEditable(false);
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
//		this.add(smButtonSetDatasets,     new GridBagConstraintsHelper(4, 1, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 5, 0, 10).setIpad(10,0));

		this.add(labelPixelType,        new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 20, 20, 0));
		this.add(smTextFieldPixelType,  new GridBagConstraintsHelper(1, 2, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 10, 20, 0));
		this.add(labelShowBounds,       new GridBagConstraintsHelper(2, 2, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 20, 20, 0));
		this.add(smButtonSetShowBounds, new GridBagConstraintsHelper(3, 2, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 20, 0).setIpad(10,0));
		this.add(smButtonClear,         new GridBagConstraintsHelper(4, 2, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 20, 20).setIpad(10,0));

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
		setDatasetImageCollection(datasetImageCollection);
	}

	@Override
	public PropertyType getPropertyType() {
		return null;
	}

	public void setDatasetImageCollection(DatasetImageCollection datasetImageCollection) {
		this.datasetImageCollection = datasetImageCollection;
		unregisterEvents();
		reset();
		fillComponents();
		setComponentsEnabled();
		registerEvents();
	}

	private void reset() {
		this.noValue = this.getNoData(0);
	}

	private void fillComponents() {
		this.smTextFieldBandCount.setText(Integer.toString(this.datasetImageCollection.getBandCount()));
		this.smTextFieldNoValue.setText(DoubleUtilities.toString(noValue, 6));
		this.smTextFieldHasPyramid.setText(CommonProperties.getString(this.datasetImageCollection.getHasPyramid() ? CommonProperties.True : CommonProperties.False));
		this.smTextFieldDatasetCount.setText(String.valueOf(datasetImageCollection.getCount()));
		this.smTextFieldPixelType.setText(PixelFormatUtilities.toString(datasetImageCollection.getPixelFormat()));

	}

	private void setComponentsEnabled() {
		this.smButtonSetShowBounds.setEnabled(!this.datasetImageCollection.isReadOnly());
		this.smButtonClear.setEnabled(!this.datasetImageCollection.isReadOnly() && this.datasetImageCollection.getClipRegion() != null);
		this.smButtonReset.setEnabled(verifyChange());
		this.smButtonApply.setEnabled(verifyChange());
	}

	private void registerEvents() {
		smButtonSetShowBounds.addActionListener(showBoundsListener);
		smButtonClear.addActionListener(buttonClearListener);
		smButtonReset.addActionListener(buttonResetListener);
		smButtonApply.addActionListener(buttonApplyListener);
	}


	private void unregisterEvents() {
		smButtonSetShowBounds.removeActionListener(showBoundsListener);
		smButtonClear.removeActionListener(buttonClearListener);
		smButtonReset.removeActionListener(buttonResetListener);
		smButtonApply.removeActionListener(buttonApplyListener);
	}

	private void noValueChanged() {
		String currentNoValue = smTextFieldNoValue.getText();
		smTextFieldNoValue.setForeground(Color.black);
		noValue = Double.valueOf(currentNoValue);
		setComponentsEnabled();
	}

	private boolean verifyChange() {
		return Double.compare(this.noValue, this.getNoData(0)) != 0;
	}

	/**
	 * 获取空值
	 *
	 * @param i 第i个波段的空值
	 */
	private double getNoData(int i) {
		if (i < 0 || i >= this.datasetImageCollection.getBandCount()) {
			return 0;
		} else {
			return this.datasetImageCollection.getNoData(i);
		}
	}

	private void buttonSetClipRegionClicked() {
		IPropertyManager propertyManager = Application.getActiveApplication().getMainFrame().getPropertyManager();
		JDialogSetClipRegion dialog = new JDialogSetClipRegion((JDialog) propertyManager);
		if (dialog.showDialog() == DialogResult.OK) {
			this.datasetImageCollection.setClipRegion(dialog.getRegion());
			dialog.disposeRegion();
			dialog.dispose();
			refreshMap();
			Application.getActiveApplication().getOutput()
					.output(MessageFormat.format(ControlsProperties.getString("String_Message_SetClipRegionSuccess"), this.datasetImageCollection.getName()));
		}
		setComponentsEnabled();
	}

	/**
	 * 设置裁剪显示范围等之后需要刷新已打开的地图
	 */
	private void refreshMap() {
		IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
		if (formMap != null) {
			Layers layers = formMap.getMapControl().getMap().getLayers();

			for (int i = 0; i < layers.getCount(); i++) {
				if (layers.get(i).getDataset() == this.datasetImageCollection) {
					formMap.getMapControl().getMap().refresh();
					break;
				}
			}
		}
	}

	private void buttonClearClipRegionClicked() {
		try {
			if (this.datasetImageCollection.getClipRegion() != null) {
				this.datasetImageCollection.setClipRegion(null);
				refreshMap();
			}
			setComponentsEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void buttonApplyClicked() {
		for (int i = 0; i < this.datasetImageCollection.getBandCount(); i++) {
			this.datasetImageCollection.setNoData(this.noValue, i);
		}
		setComponentsEnabled();
	}

	private void buttonResetClicked() {
		reset();
		fillComponents();
		setComponentsEnabled();
	}
}
