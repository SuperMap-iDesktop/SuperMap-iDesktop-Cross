package com.supermap.desktop.controls.property.dataset;

import com.supermap.data.Colors;
import com.supermap.data.DatasetGrid;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.colorScheme.ColorsComboBox;
import com.supermap.desktop.controls.property.AbstractPropertyControl;
import com.supermap.desktop.enums.PropertyType;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.SMFormattedTextField;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.ColorsUtilities;
import com.supermap.desktop.utilities.PixelFormatUtilities;
import com.supermap.mapping.Layers;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;

public class GridPropertyControl extends AbstractPropertyControl {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_LABEL_WIDTH = 80;
	private static final int DEFUALT_COMPONENT_WIDTH = 150;



	private JLabel labelPixelFormat;
	private JTextField textFieldPixelFormat;
	private JLabel labelX;
	private JTextField textFieldX;
	private JLabel labelY;
	private JTextField textFieldY;
	private JLabel labelNoValue;
	private SMFormattedTextField textFieldNoValue;
	private JLabel labelHeight;
	private JTextField textFieldHeight;
	private JLabel labelWidth;
	private JTextField textFieldWidth;
	private JLabel labelMaxValue;
	private JTextField textFieldMaxValue;
	private JLabel labelMinValue;
	private JTextField textFieldMinValue;
	private JLabel labelHasPyramid;
	private JTextField textFieldHasPyramid;
	private JLabel labelClipRegion;
	private SmButton buttonSetClipRegion;
	private SmButton buttonClearClipRegion;
	private JLabel labelColorTable;
	private ColorsComboBox comboBoxColors;
	private SmButton buttonReset;
	private SmButton buttonApply;

	private JLabel labelGridCount;
	private JTextField textFieldGridCount;

	private DatasetGrid datasetGrid;

	private double noValue = 0.0;
	private Colors colors = null;

	private ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttonSetClipRegion) {
				buttonSettingClipRegionClicked();
			} else if (e.getSource() == buttonClearClipRegion) {
				buttonClearClipRegionClicked();
			} else if (e.getSource() == buttonReset) {
				buttonResetClicked();
			} else if (e.getSource() == buttonApply) {
				buttonApplyClicked();
			}
		}
	};
	private PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			textFieldNoValueValueChange(evt);
		}
	};
	private DocumentListener documentListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			textFieldNoValueTextChange();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			textFieldNoValueTextChange();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			// 什么都不做
		}
	};
	private ItemListener itemListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				colorsComboBoxSelectedChanged();
			}
		}
	};

	public GridPropertyControl(DatasetGrid datasetGrid) {
		super(ControlsProperties.getString("String_Grid"));
		initializeComponents();
		initializeResources();
		setDatasetGrid(datasetGrid);
	}

	public DatasetGrid getDatasetGrid() {
		return datasetGrid;
	}

	public void setDatasetGrid(DatasetGrid datasetGrid) {
		this.datasetGrid = datasetGrid;
		unregisterEvents();
		reset();
		fillComponents();
		setComponentsEnabled();
		registerEvents();
	}

	@Override
	public void refreshData() {
		setDatasetGrid(this.datasetGrid);
	}

	@Override
	public PropertyType getPropertyType() {
		return PropertyType.GRID;
	}

	private void initializeComponents() {
		Dimension labelSize = new Dimension(60, 23);
		this.labelPixelFormat = new JLabel("PixelFormat:");
		labelPixelFormat.setPreferredSize(labelSize);
		this.textFieldPixelFormat = new JTextField();
		this.textFieldPixelFormat.setEditable(false);
		this.labelX = new JLabel("ResolutionX:");
		labelX.setPreferredSize(labelSize);
		this.textFieldX = new JTextField();
		this.textFieldX.setEditable(false);
		this.labelY = new JLabel("ResolutionY:");
		labelY.setPreferredSize(labelSize);
		this.textFieldY = new JTextField();
		this.textFieldY.setEditable(false);
		this.labelNoValue = new JLabel("NoValue:");
		labelNoValue.setPreferredSize(labelSize);
		this.textFieldNoValue = new SMFormattedTextField(NumberFormat.getNumberInstance());
		this.labelHeight = new JLabel("RowCount:");
		labelHeight.setPreferredSize(labelSize);
		this.textFieldHeight = new JTextField();
		this.textFieldHeight.setEditable(false);
		this.labelWidth = new JLabel("ColumnCount:");
		labelWidth.setPreferredSize(labelSize);
		this.textFieldWidth = new JTextField();
		this.textFieldWidth.setEditable(false);
		this.labelGridCount = new JLabel();
		this.textFieldGridCount = new JTextField();
		textFieldGridCount.setEditable(false);

		JPanel panelPixelProperty = new JPanel();
		panelPixelProperty.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_ImageProperty")));
		panelPixelProperty.setLayout(new GridBagLayout());
		panelPixelProperty.add(labelPixelFormat, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 0, 0));
		panelPixelProperty.add(textFieldPixelFormat, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(10, 5, 0, 10));

		panelPixelProperty.add(labelX, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 0, 0));
		panelPixelProperty.add(textFieldX, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 0, 10));

		panelPixelProperty.add(labelY, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 0, 0));
		panelPixelProperty.add(textFieldY, new GridBagConstraintsHelper(1, 2, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 0, 10));

		panelPixelProperty.add(labelNoValue, new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 0, 0));
		panelPixelProperty.add(textFieldNoValue, new GridBagConstraintsHelper(1, 3, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 0, 10));

		panelPixelProperty.add(labelHeight, new GridBagConstraintsHelper(0, 4, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 0, 0));
		panelPixelProperty.add(textFieldHeight, new GridBagConstraintsHelper(1, 4, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 0, 10));

		panelPixelProperty.add(labelWidth, new GridBagConstraintsHelper(0, 5, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 0, 0));
		panelPixelProperty.add(textFieldWidth, new GridBagConstraintsHelper(1, 5, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 0, 10));

		panelPixelProperty.add(labelGridCount, new GridBagConstraintsHelper(0, 6, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 10, 0));
		panelPixelProperty.add(textFieldGridCount, new GridBagConstraintsHelper(1, 6, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 10, 10));
		// @formatter:on

		this.labelMaxValue = new JLabel("MaxValue:");
		labelMaxValue.setPreferredSize(labelSize);
		this.textFieldMaxValue = new JTextField();
		this.textFieldMaxValue.setEditable(false);
		this.labelMinValue = new JLabel("MinValue:");
		this.textFieldMinValue = new JTextField();
		this.textFieldMinValue.setEditable(false);

		JPanel panelExtreme = new JPanel();
		panelExtreme.setBorder(BorderFactory.createTitledBorder(CommonProperties.getString(CommonProperties.Extremum)));
		panelExtreme.setLayout(new GridBagLayout());
		panelExtreme.add(labelMaxValue, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 0, 0));
		panelExtreme.add(textFieldMaxValue, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(10, 5, 0, 10));

		panelExtreme.add(labelMinValue, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 0, 0));
		panelExtreme.add(textFieldMinValue, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 0, 10));



		this.labelHasPyramid = new JLabel("HasPyramid:");
		labelHasPyramid.setPreferredSize(labelSize);
		this.textFieldHasPyramid = new JTextField();
		this.textFieldHasPyramid.setEditable(false);
		this.labelClipRegion = new JLabel("ClipRegion:");
		this.buttonSetClipRegion = new SmButton("Set...");
		this.buttonClearClipRegion = new SmButton("Clear");

		JPanel panelOther = new JPanel();
		panelOther.setBorder(BorderFactory.createTitledBorder(CoreProperties.getString(CoreProperties.Other)));
		panelOther.setLayout(new GridBagLayout());
		panelOther.add(labelHasPyramid, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 0, 0));
		panelOther.add(textFieldHasPyramid, new GridBagConstraintsHelper(1, 0, 2, 1).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(10, 5, 0, 10));

		panelOther.add(labelClipRegion, new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.NONE).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 10, 0));
		panelOther.add(buttonSetClipRegion, new GridBagConstraintsHelper(1, 1, 1, 1).setFill(GridBagConstraints.NONE).setWeight(1, 0).setAnchor(GridBagConstraints.EAST).setInsets(5, 5, 10, 0));
		panelOther.add(buttonClearClipRegion, new GridBagConstraintsHelper(2, 1, 1, 1).setFill(GridBagConstraints.NONE).setWeight(0, 0).setAnchor(GridBagConstraints.EAST).setInsets(5, 5, 10, 10));


		this.labelColorTable = new JLabel("ColorTable:");
		labelColorTable.setPreferredSize(labelSize);
		this.comboBoxColors = new ColorsComboBox(ControlsProperties.getString("String_ColorSchemeManager_Grid_DEM"));

		JPanel panelColors = new JPanel();
		panelColors.setLayout(new GridBagLayout());
		panelColors.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_ColorTable")));
		panelColors.add(labelColorTable, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 10, 0).setWeight(0, 0));
		panelColors.add(comboBoxColors, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setInsets(10, 5, 10, 10).setAnchor(GridBagConstraints.EAST));


		JPanel panelButton = new JPanel();
		panelButton.setLayout(new GridBagLayout());
		this.buttonReset = new SmButton("Reset");
		this.buttonApply = new SmButton("Apply");
		panelButton.add(buttonReset, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.EAST).setInsets(5, 10, 10, 0));
		panelButton.add(buttonApply, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.EAST).setInsets(5, 5, 10, 10));

		this.setLayout(new GridBagLayout());
		this.add(panelPixelProperty, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(10, 10, 0, 10));
		this.add(panelExtreme, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(5, 10, 0, 10));
		this.add(panelColors, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(5, 10, 0, 10));
		this.add(panelOther, new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(5, 10, 0, 10));
		this.add(new JPanel(), new GridBagConstraintsHelper(0, 4, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER));
		this.add(panelButton, new GridBagConstraintsHelper(0, 5, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL));

	}

	private void initializeResources() {
		this.labelPixelFormat.setText(ControlsProperties.getString("String_LabelPixelFormat"));
		this.labelX.setText(ControlsProperties.getString("String_LabelXPixelFormat"));
		this.labelY.setText(ControlsProperties.getString("String_LabelYPixelFormat"));
		this.labelNoValue.setText(ControlsProperties.getString("String_LabelNoValue"));
		this.labelHeight.setText(ControlsProperties.getString("String_LabelRowsSize"));
		this.labelWidth.setText(ControlsProperties.getString("String_LabelColumnsSize"));
		this.labelMaxValue.setText(ControlsProperties.getString("String_LabelMaxValue"));
		this.labelMinValue.setText(ControlsProperties.getString("String_LabelMinValue"));
		this.labelGridCount.setText(ControlsProperties.getString("String_GridCount"));
		this.labelHasPyramid.setText(ControlsProperties.getString("String_LabelPyramid"));
		this.labelClipRegion.setText(ControlsProperties.getString("String_LabelClipRegion"));
		this.buttonSetClipRegion.setText(ControlsProperties.getString("String_Button_Setting"));
		this.buttonClearClipRegion.setText(CoreProperties.getString(CoreProperties.Clear));
		this.labelColorTable.setText(ControlsProperties.getString("String_LabelColorScheme"));
		this.buttonReset.setText(CommonProperties.getString(CommonProperties.Reset));
		this.buttonApply.setText(CommonProperties.getString(CommonProperties.Apply));
	}

	private void reset() {
		this.noValue = this.datasetGrid.getNoValue();
		this.colors = this.datasetGrid.getColorTable();
	}

	private void registerEvents() {
		this.textFieldNoValue.addPropertyChangeListener(ControlDefaultValues.PROPERTYNAME_VALUE, this.propertyChangeListener);
		this.textFieldNoValue.getDocument().addDocumentListener(this.documentListener);
		this.buttonSetClipRegion.addActionListener(this.actionListener);
		this.buttonClearClipRegion.addActionListener(this.actionListener);
		this.buttonReset.addActionListener(this.actionListener);
		this.buttonApply.addActionListener(this.actionListener);
		this.comboBoxColors.addItemListener(this.itemListener);
		this.comboBoxColors.addColorChangedListener();
	}

	private void unregisterEvents() {
		this.textFieldNoValue.removePropertyChangeListener(ControlDefaultValues.PROPERTYNAME_VALUE, this.propertyChangeListener);
		this.textFieldNoValue.getDocument().removeDocumentListener(this.documentListener);
		this.buttonSetClipRegion.removeActionListener(this.actionListener);
		this.buttonClearClipRegion.removeActionListener(this.actionListener);
		this.buttonReset.removeActionListener(this.actionListener);
		this.buttonApply.removeActionListener(this.actionListener);
		this.comboBoxColors.removeItemListener(this.itemListener);
		this.comboBoxColors.removeColorChangedListener();

	}

	private void fillComponents() {
		this.textFieldPixelFormat.setText(PixelFormatUtilities.toString(this.datasetGrid.getPixelFormat()));
		this.textFieldX.setText(BigDecimal.valueOf(this.datasetGrid.getBounds().getWidth() / this.datasetGrid.getWidth()).toString());
		this.textFieldY.setText(BigDecimal.valueOf(this.datasetGrid.getBounds().getHeight() / this.datasetGrid.getHeight()).toString());
		this.textFieldNoValue.setValue(this.noValue);
		int blockCount = datasetGrid.getColumnBlockCount();
		int rowBlockCount = datasetGrid.getRowBlockCount();
		textFieldGridCount.setText(MessageFormat.format("{0}*{1}", blockCount, rowBlockCount));
		this.textFieldHeight.setText(BigDecimal.valueOf(this.datasetGrid.getHeight()).toString());
		this.textFieldWidth.setText(BigDecimal.valueOf(this.datasetGrid.getWidth()).toString());
		this.textFieldMaxValue.setText(BigDecimal.valueOf(this.datasetGrid.getMaxValue()).toString());
		this.textFieldMinValue.setText(BigDecimal.valueOf(this.datasetGrid.getMinValue()).toString());
		if (this.datasetGrid.getHasPyramid()) {
			this.textFieldHasPyramid.setText(CommonProperties.getString(CommonProperties.True));
		} else {
			this.textFieldHasPyramid.setText(CommonProperties.getString(CommonProperties.False));
		}
		this.comboBoxColors.setSelectedItem(this.colors);
	}

	private void setComponentsEnabled() {
		this.textFieldNoValue.setEnabled(!this.datasetGrid.isReadOnly());
		this.comboBoxColors.setEnabled(!this.datasetGrid.isReadOnly());
		this.buttonSetClipRegion.setEnabled(!this.datasetGrid.isReadOnly());
		this.buttonClearClipRegion.setEnabled(!this.datasetGrid.isReadOnly() && this.datasetGrid.getClipRegion() != null);
		this.buttonReset.setEnabled(verifyChange());
		this.buttonApply.setEnabled(verifyChange());
	}

	private boolean verifyChange() {
		return Double.compare(this.noValue, this.datasetGrid.getNoValue()) != 0 || !ColorsUtilities.isColorsEqual(this.colors, this.datasetGrid.getColorTable());
	}

	private void textFieldNoValueValueChange(PropertyChangeEvent e) {
		this.noValue = Double.valueOf(e.getNewValue().toString());
		setComponentsEnabled();
	}

	private void textFieldNoValueTextChange() {
		try {
			textFieldNoValue.commitEdit();
		} catch (ParseException e1) {
			// TODO 如果提交失败，则表示输入不合法，什么都不做。以后在这里添加错误提示的代码
		}
	}

	private void colorsComboBoxSelectedChanged() {
		this.colors = this.comboBoxColors.getSelectedItem();
		setComponentsEnabled();
	}

	private void buttonSettingClipRegionClicked() {
		JDialogSetClipRegion dialog = new JDialogSetClipRegion();
		if (dialog.showDialog() == DialogResult.OK) {
			this.datasetGrid.setClipRegion(dialog.getRegion());
			dialog.disposeRegion();
			refreshMap();
			Application.getActiveApplication().getOutput()
					.output(MessageFormat.format(ControlsProperties.getString("String_Message_SetClipRegionSuccess"), this.datasetGrid.getName()));
		}
		setComponentsEnabled();
	}

	private void buttonClearClipRegionClicked() {
		try {
			if (this.datasetGrid.getClipRegion() != null) {
				this.datasetGrid.setClipRegion(null);
				refreshMap();
			}
			setComponentsEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void buttonResetClicked() {
		reset();
		fillComponents();
		setComponentsEnabled();
	}

	private void buttonApplyClicked() {
		this.datasetGrid.setNoValue(this.noValue);
		if (this.colors != null) {
			this.datasetGrid.setColorTable(this.colors);
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
				if (layers.get(i).getDataset() == this.datasetGrid) {
					formMap.getMapControl().getMap().refresh();
					break;
				}
			}
		}
	}
}
