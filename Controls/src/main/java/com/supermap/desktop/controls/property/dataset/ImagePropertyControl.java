package com.supermap.desktop.controls.property.dataset;

import com.supermap.data.DatasetImage;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.property.AbstractPropertyControl;
import com.supermap.desktop.enums.PropertyType;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.SMFormattedTextField;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.PixelFormatUtilities;
import com.supermap.mapping.Layers;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;

public class ImagePropertyControl extends AbstractPropertyControl {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_LABEL_WIDTH = 80;
	private static final int DEFUALT_COMPONENT_WIDTH = 150;
	private static final int COLUMN_INDEX_WIDTH = 80;

	private JLabel labelBandCount;
	private JTextField textFieldBandCount;
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

	private JTable tableBandInfo;
	private JLabel labelHasPyramid;
	private JTextField textFieldHasPyramid;
	private JLabel labelClipRegion;
	private SmButton buttonSetClipRegion;
	private SmButton buttonClearClipRegion;

	private SmButton buttonReset;
	private SmButton buttonApply;

	private DatasetImage datasetImage;

	private double noValue = 0.0;

	private PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			textFieldNoValueValueChange(evt);
		}
	};
	private ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttonSetClipRegion) {
				buttonSetClipRegionClicked();
			} else if (e.getSource() == buttonClearClipRegion) {
				buttonClearClipRegionClicked();
			} else if (e.getSource() == buttonReset) {
				buttonResetClicked();
			} else if (e.getSource() == buttonApply) {
				buttonApplyClicked();
			}
		}
	};

	public ImagePropertyControl(DatasetImage datasetImage) {
		super(ControlsProperties.getString("String_ImageProperty"));
		initializeComponents();
		initializeResources();
		setDatasetImage(datasetImage);
	}

	public DatasetImage getDatasetImage() {
		return datasetImage;
	}

	public void setDatasetImage(DatasetImage datasetImage) {
		this.datasetImage = datasetImage;
		unregisterEvents();
		reset();
		fillComponents();
		setComponentsEnabled();
		registerEvents();
	}

	@Override
	public void refreshData() {
		setDatasetImage(this.datasetImage);
	}

	@Override
	public PropertyType getPropertyType() {
		return PropertyType.IMAGE;
	}

	private void initializeComponents() {
		Dimension labelSize = new Dimension(60, 23);
		// 图像信息
		this.labelBandCount = new JLabel("BandCount:");
		labelBandCount.setPreferredSize(labelSize);
		this.textFieldBandCount = new JTextField();
		this.textFieldBandCount.setEditable(false);
		this.labelX = new JLabel("ResolutionX:");
		this.textFieldX = new JTextField();
		this.textFieldX.setEditable(false);
		this.labelY = new JLabel("ResolutionY:");
		this.textFieldY = new JTextField();
		this.textFieldY.setEditable(false);
		this.labelNoValue = new JLabel("NoValue:");
		this.textFieldNoValue = new SMFormattedTextField(NumberFormat.getNumberInstance());
		this.labelHeight = new JLabel("RowCount:");
		this.textFieldHeight = new JTextField();
		this.textFieldHeight.setEditable(false);
		this.labelWidth = new JLabel("ColumnCount:");
		this.textFieldWidth = new JTextField();
		this.textFieldWidth.setEditable(false);

		JPanel panelImageProperty = new JPanel();
		panelImageProperty.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_ImageProperty")));
		panelImageProperty.setLayout(new GridBagLayout());
		panelImageProperty.add(labelBandCount, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 0, 0));
		panelImageProperty.add(textFieldBandCount, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 10));

		panelImageProperty.add(labelNoValue, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 0, 0));
		panelImageProperty.add(textFieldNoValue, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 10));

		panelImageProperty.add(labelX, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 0, 0));
		panelImageProperty.add(textFieldX, new GridBagConstraintsHelper(1, 2, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 10));

		panelImageProperty.add(labelY, new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 0, 0));
		panelImageProperty.add(textFieldY, new GridBagConstraintsHelper(1, 3, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 10));

		panelImageProperty.add(labelHeight, new GridBagConstraintsHelper(0, 4, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 0, 0));
		panelImageProperty.add(textFieldHeight, new GridBagConstraintsHelper(1, 4, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 10));

		panelImageProperty.add(labelWidth, new GridBagConstraintsHelper(0, 5, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 10, 0));
		panelImageProperty.add(textFieldWidth, new GridBagConstraintsHelper(1, 5, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 10, 10));



		// 波段信息
		this.tableBandInfo = new JTable();
		this.tableBandInfo.setModel(new DefaultTableModel(new String[]{CommonProperties.getString(CommonProperties.Index),
				CommonProperties.getString(CommonProperties.Name), CommonProperties.getString(CommonProperties.PixelFormat)}, 0) {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		this.tableBandInfo.getColumnModel().getColumn(0).setMaxWidth(COLUMN_INDEX_WIDTH);
		JPanel panelBandInfo = new JPanel();
		JScrollPane scrollPaneBandInfo = new JScrollPane(this.tableBandInfo);
		panelBandInfo.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_BandsInfo")));
		panelBandInfo.setLayout(new GridBagLayout());
		panelBandInfo.add(scrollPaneBandInfo, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(10, 10, 10, 10));

		// 其他
		this.labelHasPyramid = new JLabel("HasPyramid:");
		labelHasPyramid.setPreferredSize(labelSize);
		this.textFieldHasPyramid = new JTextField();
		this.textFieldHasPyramid.setEditable(false);
		this.labelClipRegion = new JLabel("ClipRegion:");
		this.buttonSetClipRegion = new SmButton("Setting...");
		this.buttonSetClipRegion.setUseDefaultSize(false);
		this.buttonClearClipRegion = new SmButton("Clear");

		JPanel panelOther = new JPanel();
		panelOther.setBorder(BorderFactory.createTitledBorder(CoreProperties.getString(CoreProperties.Other)));
		panelOther.setLayout(new GridBagLayout());
		panelOther.add(labelHasPyramid, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 0).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0));
		panelOther.add(textFieldHasPyramid, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 10));

		panelOther.add(labelClipRegion, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 0).setFill(GridBagConstraints.NONE).setInsets(5, 10, 10, 0));
		panelOther.add(buttonSetClipRegion, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(1, 0).setFill(GridBagConstraints.NONE).setInsets(5, 5, 10, 0));
		panelOther.add(buttonClearClipRegion, new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setFill(GridBagConstraints.NONE).setInsets(5, 5, 10, 10));


		// 整体布局
		this.buttonReset = new SmButton("Reset");
		this.buttonApply = new SmButton("Apply");

		JPanel panelButton = new JPanel();
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(buttonReset, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 10, 0));
		panelButton.add(buttonApply, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(10, 5, 10, 10));

		this.setLayout(new GridBagLayout());
		this.add(panelImageProperty, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 10, 0, 10));
		this.add(panelBandInfo, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(5, 10, 0, 10));
		this.add(panelOther, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 0, 10));
		this.add(panelButton, new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 10, 10));

	}

	private void initializeResources() {
		this.labelBandCount.setText(ControlsProperties.getString("String_LabelBandsSize"));
		this.labelX.setText(ControlsProperties.getString("String_LabelXPixelFormat"));
		this.labelY.setText(ControlsProperties.getString("String_LabelYPixelFormat"));
		this.labelNoValue.setText(ControlsProperties.getString("String_LabelNoValue"));
		this.labelHeight.setText(ControlsProperties.getString("String_LabelRowsSize"));
		this.labelWidth.setText(ControlsProperties.getString("String_LabelColumnsSize"));
		this.labelHasPyramid.setText(ControlsProperties.getString("String_LabelPyramid"));
		this.labelClipRegion.setText(ControlsProperties.getString("String_LabelClipRegion"));
		this.buttonSetClipRegion.setText(CommonProperties.getString(CommonProperties.Button_Setting));
		this.buttonClearClipRegion.setText(CoreProperties.getString(CoreProperties.Clear));
		this.buttonReset.setText(CommonProperties.getString(CommonProperties.Reset));
		this.buttonApply.setText(CommonProperties.getString(CommonProperties.Apply));
	}

	private void registerEvents() {
		this.textFieldNoValue.addPropertyChangeListener(ControlDefaultValues.PROPERTYNAME_VALUE, this.propertyChangeListener);
		this.buttonSetClipRegion.addActionListener(this.actionListener);
		this.buttonClearClipRegion.addActionListener(this.actionListener);
		this.buttonReset.addActionListener(this.actionListener);
		this.buttonApply.addActionListener(this.actionListener);
	}

	private void unregisterEvents() {
		this.textFieldNoValue.removePropertyChangeListener(ControlDefaultValues.PROPERTYNAME_VALUE, this.propertyChangeListener);
		this.buttonSetClipRegion.removeActionListener(this.actionListener);
		this.buttonClearClipRegion.removeActionListener(this.actionListener);
		this.buttonReset.removeActionListener(this.actionListener);
		this.buttonApply.removeActionListener(this.actionListener);
	}

	private void reset() {
		this.noValue = this.getNoData(0);

	}

	private void fillComponents() {
		this.textFieldBandCount.setText(Integer.toString(this.datasetImage.getBandCount()));
		this.textFieldX.setText(BigDecimal.valueOf(this.datasetImage.getBounds().getWidth() / this.datasetImage.getWidth()).toString());
		this.textFieldY.setText(BigDecimal.valueOf(this.datasetImage.getBounds().getHeight() / this.datasetImage.getHeight()).toString());
		this.textFieldNoValue.setText(BigDecimal.valueOf(this.getNoData(0)).toString());
		this.textFieldWidth.setText(BigDecimal.valueOf(this.datasetImage.getWidth()).toString());
		this.textFieldHeight.setText(BigDecimal.valueOf(this.datasetImage.getHeight()).toString());
		if (this.datasetImage.getHasPyramid()) {
			this.textFieldHasPyramid.setText(CommonProperties.getString(CommonProperties.True));
		} else {
			this.textFieldHasPyramid.setText(CommonProperties.getString(CommonProperties.False));
		}

		DefaultTableModel tableModel = (DefaultTableModel) this.tableBandInfo.getModel();

		for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
			tableModel.removeRow(i);
		}
		for (int i = 0; i < this.datasetImage.getBandCount(); i++) {
			tableModel.addRow(new Object[]{i, this.datasetImage.get(i), PixelFormatUtilities.toString(this.datasetImage.getPixelFormat(i))});
		}
	}

	private void setComponentsEnabled() {
		this.buttonSetClipRegion.setEnabled(!this.datasetImage.isReadOnly());
		this.buttonClearClipRegion.setEnabled(!this.datasetImage.isReadOnly() && this.datasetImage.getClipRegion() != null);
		this.buttonReset.setEnabled(verifyChange());
		this.buttonApply.setEnabled(verifyChange());
	}

	private boolean verifyChange() {
		return Double.compare(this.noValue, this.getNoData(0)) != 0;
	}

	private void textFieldNoValueValueChange(PropertyChangeEvent e) {
		this.noValue = Double.valueOf(e.getNewValue().toString());
		setComponentsEnabled();
	}

	/**
	 * 获取空值
	 *
	 * @param i 第i个波段的空值
	 */
	private double getNoData(int i) {
		if (i < 0 || i >= this.datasetImage.getBandCount()) {
			return 0;
		} else {
			return this.datasetImage.getNoData(i);
		}
	}

	private void buttonSetClipRegionClicked() {
		JDialogSetClipRegion dialog = new JDialogSetClipRegion();
		if (dialog.showDialog() == DialogResult.OK) {
			this.datasetImage.setClipRegion(dialog.getRegion());
			dialog.disposeRegion();
			refreshMap();
			Application.getActiveApplication().getOutput()
					.output(MessageFormat.format(ControlsProperties.getString("String_Message_SetClipRegionSuccess"), this.datasetImage.getName()));
		}
		setComponentsEnabled();
	}

	private void buttonClearClipRegionClicked() {
		try {
			if (this.datasetImage.getClipRegion() != null) {
				this.datasetImage.setClipRegion(null);
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
		for (int i = 0; i < this.datasetImage.getBandCount(); i++) {
			this.datasetImage.setNoData(this.noValue, i);
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
				if (layers.get(i).getDataset() == this.datasetImage) {
					formMap.getMapControl().getMap().refresh();
					break;
				}
			}
		}
	}
}
