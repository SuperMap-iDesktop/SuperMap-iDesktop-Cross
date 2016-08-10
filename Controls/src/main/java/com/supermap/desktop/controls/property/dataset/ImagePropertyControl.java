package com.supermap.desktop.controls.property.dataset;

import com.supermap.data.DatasetImage;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IPropertyManager;
import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.property.AbstractPropertyControl;
import com.supermap.desktop.enums.PropertyType;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.SMFormattedTextField;
import com.supermap.desktop.ui.controls.CaretPositionListener;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.PixelFormatUtilities;
import com.supermap.mapping.Layers;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.table.DefaultTableModel;
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
	private transient CaretPositionListener caretPositionListener = new CaretPositionListener();

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
		// 图像信息
		this.labelBandCount = new JLabel("BandCount:");
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
		GroupLayout gl_panelPixelProperty = new GroupLayout(panelImageProperty);
		gl_panelPixelProperty.setAutoCreateContainerGaps(true);
		gl_panelPixelProperty.setAutoCreateGaps(true);
		panelImageProperty.setLayout(gl_panelPixelProperty);
		// @formatter:off
		gl_panelPixelProperty.setHorizontalGroup(gl_panelPixelProperty.createSequentialGroup()
				.addGroup(gl_panelPixelProperty.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelBandCount, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH)
						.addComponent(this.labelX, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH)
						.addComponent(this.labelY, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH))
				.addGroup(gl_panelPixelProperty.createParallelGroup(Alignment.LEADING)
						.addComponent(this.textFieldBandCount, DEFUALT_COMPONENT_WIDTH, DEFUALT_COMPONENT_WIDTH, DEFUALT_COMPONENT_WIDTH)
						.addComponent(this.textFieldX, DEFUALT_COMPONENT_WIDTH, DEFUALT_COMPONENT_WIDTH, DEFUALT_COMPONENT_WIDTH)
						.addComponent(this.textFieldY, DEFUALT_COMPONENT_WIDTH, DEFUALT_COMPONENT_WIDTH, DEFUALT_COMPONENT_WIDTH))
				.addGap(40)
				.addGroup(gl_panelPixelProperty.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelNoValue, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH)
						.addComponent(this.labelHeight, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH)
						.addComponent(this.labelWidth, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH))
				.addGroup(gl_panelPixelProperty.createParallelGroup(Alignment.LEADING)
						.addComponent(this.textFieldNoValue, GroupLayout.PREFERRED_SIZE, DEFUALT_COMPONENT_WIDTH, Short.MAX_VALUE)
						.addComponent(this.textFieldHeight, GroupLayout.PREFERRED_SIZE, DEFUALT_COMPONENT_WIDTH, Short.MAX_VALUE)
						.addComponent(this.textFieldWidth, GroupLayout.PREFERRED_SIZE, DEFUALT_COMPONENT_WIDTH, Short.MAX_VALUE)));
		
		gl_panelPixelProperty.setVerticalGroup(gl_panelPixelProperty.createSequentialGroup()
				.addGroup(gl_panelPixelProperty.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelBandCount)
						.addComponent(this.textFieldBandCount, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.labelNoValue)
						.addComponent(this.textFieldNoValue, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panelPixelProperty.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelX)
						.addComponent(this.textFieldX, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.labelHeight)
						.addComponent(this.textFieldHeight, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panelPixelProperty.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelY)
						.addComponent(this.textFieldY, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.labelWidth)
						.addComponent(this.textFieldWidth, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)));
		// @formatter:on

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
		JScrollPane scrollPaneBandInfo = new JScrollPane(this.tableBandInfo);
		scrollPaneBandInfo.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_BandsInfo")));

		// 其他
		this.labelHasPyramid = new JLabel("HasPyramid:");
		this.textFieldHasPyramid = new JTextField();
		this.textFieldHasPyramid.setEditable(false);
		this.labelClipRegion = new JLabel("ClipRegion:");
		this.buttonSetClipRegion = new SmButton("Setting...");
		this.buttonSetClipRegion.setUseDefaultSize(false);
		this.buttonClearClipRegion = new SmButton("Clear");

		JPanel panelOther = new JPanel();
		panelOther.setBorder(BorderFactory.createTitledBorder(CoreProperties.getString(CoreProperties.Other)));
		GroupLayout gl_panelOther = new GroupLayout(panelOther);
		gl_panelOther.setAutoCreateContainerGaps(true);
		gl_panelOther.setAutoCreateGaps(true);
		panelOther.setLayout(gl_panelOther);

		// @formatter:off
		gl_panelOther.setHorizontalGroup(gl_panelOther.createSequentialGroup()
				.addComponent(this.labelHasPyramid, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH)
				.addComponent(this.textFieldHasPyramid, DEFUALT_COMPONENT_WIDTH, DEFUALT_COMPONENT_WIDTH, DEFUALT_COMPONENT_WIDTH)
				.addComponent(this.labelClipRegion, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH)
				.addComponent(this.buttonSetClipRegion)
				.addComponent(this.buttonClearClipRegion));
		
		gl_panelOther.setVerticalGroup(gl_panelOther.createParallelGroup(Alignment.CENTER)
				.addComponent(this.labelHasPyramid)
				.addComponent(this.textFieldHasPyramid, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addComponent(this.labelClipRegion)
				.addComponent(this.buttonSetClipRegion)
				.addComponent(this.buttonClearClipRegion));
		// @formatter:on

		// 整体布局
		this.buttonReset = new SmButton("Reset");
		this.buttonApply = new SmButton("Apply");

		GroupLayout gl_mainContent = new GroupLayout(this);
		gl_mainContent.setAutoCreateContainerGaps(true);
		gl_mainContent.setAutoCreateGaps(true);
		this.setLayout(gl_mainContent);

		// @formatter:off
		gl_mainContent.setHorizontalGroup(gl_mainContent.createParallelGroup(Alignment.LEADING)
				.addComponent(panelImageProperty, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
				.addComponent(scrollPaneBandInfo, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
				.addComponent(panelOther, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
				.addGroup(gl_mainContent.createSequentialGroup()
						.addGap(10, 10, Short.MAX_VALUE)
						.addComponent(this.buttonReset)
						.addComponent(this.buttonApply)));
		
		gl_mainContent.setVerticalGroup(gl_mainContent.createSequentialGroup()
				.addComponent(panelImageProperty, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addComponent(scrollPaneBandInfo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
				.addComponent(panelOther, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addGap(10, 10, Short.MAX_VALUE)
				.addGroup(gl_mainContent.createParallelGroup(Alignment.CENTER)
						.addComponent(this.buttonReset)
						.addComponent(this.buttonApply)));
		// @formatter:on
	}

	private void initializeResources() {
		this.labelBandCount.setText(ControlsProperties.getString("String_LabelBandsSize"));
		this.labelX.setText(ControlsProperties.getString("String_LabelPixelFormat"));
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
		this.caretPositionListener.registerComponent(textFieldNoValue);
		this.textFieldNoValue.addPropertyChangeListener(ControlDefaultValues.PROPERTYNAME_VALUE, this.propertyChangeListener);
		this.buttonSetClipRegion.addActionListener(this.actionListener);
		this.buttonClearClipRegion.addActionListener(this.actionListener);
		this.buttonReset.addActionListener(this.actionListener);
		this.buttonApply.addActionListener(this.actionListener);
	}

	private void unregisterEvents() {
		this.caretPositionListener.unRegisterComponent(textFieldNoValue);
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
		IPropertyManager propertyManager = Application.getActiveApplication().getMainFrame().getPropertyManager();
		JDialogSetClipRegion dialog = new JDialogSetClipRegion((JDialog) propertyManager);
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
