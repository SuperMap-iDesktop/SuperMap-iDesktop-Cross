package com.supermap.desktop.controls.property.dataset;

import com.supermap.data.Colors;
import com.supermap.data.DatasetGrid;
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
import com.supermap.desktop.ui.controls.ColorsComboBox;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilties.ColorsUtilties;
import com.supermap.desktop.utilties.PixelFormatUtilties;
import com.supermap.mapping.Layers;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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

	private transient CaretPositionListener caretPositionListener = new CaretPositionListener();


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
		super(CommonProperties.getString(CommonProperties.DatasetGrid));
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
		this.labelPixelFormat = new JLabel("PixelFormat:");
		this.textFieldPixelFormat = new JTextField();
		this.textFieldPixelFormat.setEditable(false);
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

		JPanel panelPixelProperty = new JPanel();
		panelPixelProperty.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_ImageProperty")));
		GroupLayout gl_panelPixelProperty = new GroupLayout(panelPixelProperty);
		gl_panelPixelProperty.setAutoCreateContainerGaps(true);
		gl_panelPixelProperty.setAutoCreateGaps(true);
		panelPixelProperty.setLayout(gl_panelPixelProperty);
		// @formatter:off
		gl_panelPixelProperty.setHorizontalGroup(gl_panelPixelProperty.createSequentialGroup()
				.addGroup(gl_panelPixelProperty.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelPixelFormat, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH)
						.addComponent(this.labelX, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH)
						.addComponent(this.labelY, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH))
				.addGroup(gl_panelPixelProperty.createParallelGroup(Alignment.LEADING)
						.addComponent(this.textFieldPixelFormat, DEFUALT_COMPONENT_WIDTH, DEFUALT_COMPONENT_WIDTH, DEFUALT_COMPONENT_WIDTH)
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
						.addComponent(this.labelPixelFormat)
						.addComponent(this.textFieldPixelFormat, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
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

		this.labelMaxValue = new JLabel("MaxValue:");
		this.textFieldMaxValue = new JTextField();
		this.textFieldMaxValue.setEditable(false);
		this.labelMinValue = new JLabel("MinValue:");
		this.textFieldMinValue = new JTextField();
		this.textFieldMinValue.setEditable(false);

		JPanel panelExtreme = new JPanel();
		panelExtreme.setBorder(BorderFactory.createTitledBorder(CommonProperties.getString(CommonProperties.Extremum)));
		GroupLayout gl_panelExtreme = new GroupLayout(panelExtreme);
		gl_panelExtreme.setAutoCreateContainerGaps(true);
		gl_panelExtreme.setAutoCreateGaps(true);
		panelExtreme.setLayout(gl_panelExtreme);
		// @formatter:off
		gl_panelExtreme.setHorizontalGroup(gl_panelExtreme.createSequentialGroup()
				.addGroup(gl_panelExtreme.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelMaxValue, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH)
						.addComponent(this.labelMinValue, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH))
				.addGroup(gl_panelExtreme.createParallelGroup(Alignment.LEADING)
						.addComponent(this.textFieldMaxValue, DEFUALT_COMPONENT_WIDTH, DEFUALT_COMPONENT_WIDTH, DEFUALT_COMPONENT_WIDTH)
						.addComponent(this.textFieldMinValue, DEFUALT_COMPONENT_WIDTH, DEFUALT_COMPONENT_WIDTH, DEFUALT_COMPONENT_WIDTH)));
		
		gl_panelExtreme.setVerticalGroup(gl_panelExtreme.createSequentialGroup()
				.addGroup(gl_panelExtreme.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelMaxValue)
						.addComponent(this.textFieldMaxValue, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panelExtreme.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelMinValue)
						.addComponent(this.textFieldMinValue, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)));
		// @formatter:on

		this.labelHasPyramid = new JLabel("HasPyramid:");
		this.textFieldHasPyramid = new JTextField();
		this.textFieldHasPyramid.setEditable(false);
		this.labelClipRegion = new JLabel("ClipRegion:");
		this.buttonSetClipRegion = new SmButton("Set...");
		this.buttonClearClipRegion = new SmButton("Clear");

		JPanel panelOther = new JPanel();
		panelOther.setBorder(BorderFactory.createTitledBorder(CoreProperties.getString(CoreProperties.Other)));
		GroupLayout gl_panelOther = new GroupLayout(panelOther);
		gl_panelOther.setAutoCreateContainerGaps(true);
		gl_panelOther.setAutoCreateGaps(true);
		panelOther.setLayout(gl_panelOther);
		// @formatter:off
		gl_panelOther.setHorizontalGroup(gl_panelOther.createSequentialGroup()
				.addGroup(gl_panelOther.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelHasPyramid, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH)
						.addComponent(this.labelClipRegion, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH))
				.addGroup(gl_panelOther.createParallelGroup(Alignment.LEADING)
						.addComponent(this.textFieldHasPyramid, GroupLayout.PREFERRED_SIZE, DEFUALT_COMPONENT_WIDTH, Short.MAX_VALUE)
						.addGroup(gl_panelOther.createSequentialGroup()
								.addGap(10, 10, Short.MAX_VALUE)
								.addComponent(this.buttonSetClipRegion)
								.addComponent(this.buttonClearClipRegion))));
		
		gl_panelOther.setVerticalGroup(gl_panelOther.createSequentialGroup()
				.addGroup(gl_panelOther.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelHasPyramid)
						.addComponent(this.textFieldHasPyramid, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panelOther.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelClipRegion)
						.addComponent(this.buttonSetClipRegion)
						.addComponent(this.buttonClearClipRegion)));
		// @formatter:on

		this.labelColorTable = new JLabel("ColorTable:");
		this.comboBoxColors = new ColorsComboBox();

		JPanel panelColors = new JPanel();
		panelColors.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_ColorTable")));
		GroupLayout gl_panelColors = new GroupLayout(panelColors);
		gl_panelColors.setAutoCreateContainerGaps(true);
		gl_panelColors.setAutoCreateGaps(true);
		panelColors.setLayout(gl_panelColors);
		// @formatter:off
		gl_panelColors.setHorizontalGroup(gl_panelColors.createSequentialGroup()
				.addComponent(this.labelColorTable, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH)
				.addComponent(this.comboBoxColors, GroupLayout.PREFERRED_SIZE, DEFUALT_COMPONENT_WIDTH, Short.MAX_VALUE));
		
		gl_panelColors.setVerticalGroup(gl_panelColors.createParallelGroup(Alignment.CENTER)
				.addComponent(this.labelColorTable)
				.addComponent(this.comboBoxColors, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE));
		// @formatter:on

		this.buttonReset = new SmButton("Reset");
		this.buttonApply = new SmButton("Apply");

		GroupLayout gl_mainContent = new GroupLayout(this);
		gl_mainContent.setAutoCreateContainerGaps(true);
		gl_mainContent.setAutoCreateGaps(true);
		this.setLayout(gl_mainContent);
		// @formatter:off
		gl_mainContent.setHorizontalGroup(gl_mainContent.createParallelGroup(Alignment.LEADING)
				.addComponent(panelPixelProperty, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
				.addGroup(gl_mainContent.createSequentialGroup()
						.addComponent(panelExtreme, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(panelOther, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
				.addComponent(panelColors, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
				.addGroup(gl_mainContent.createSequentialGroup()
						.addGap(10, 10, Short.MAX_VALUE)
						.addComponent(this.buttonReset)
						.addComponent(this.buttonApply)));
		
		gl_mainContent.setVerticalGroup(gl_mainContent.createSequentialGroup()
				.addComponent(panelPixelProperty, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addGroup(gl_mainContent.createParallelGroup(Alignment.CENTER)
						.addComponent(panelExtreme, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(panelOther, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(panelColors, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addGap(10, 10, Short.MAX_VALUE)
				.addGroup(gl_mainContent.createParallelGroup(Alignment.CENTER)
						.addComponent(this.buttonReset)
						.addComponent(this.buttonApply)));
		// @formatter:on
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
		caretPositionListener.registerComponent(textFieldNoValue);
		this.textFieldNoValue.addPropertyChangeListener(ControlDefaultValues.PROPERTYNAME_VALUE, this.propertyChangeListener);
		this.textFieldNoValue.getDocument().addDocumentListener(this.documentListener);
		this.buttonSetClipRegion.addActionListener(this.actionListener);
		this.buttonClearClipRegion.addActionListener(this.actionListener);
		this.buttonReset.addActionListener(this.actionListener);
		this.buttonApply.addActionListener(this.actionListener);
		this.comboBoxColors.addItemListener(this.itemListener);
	}

	private void unregisterEvents() {
		caretPositionListener.unRegisterComponent(textFieldNoValue);
		this.textFieldNoValue.removePropertyChangeListener(ControlDefaultValues.PROPERTYNAME_VALUE, this.propertyChangeListener);
		this.textFieldNoValue.getDocument().removeDocumentListener(this.documentListener);
		this.buttonSetClipRegion.removeActionListener(this.actionListener);
		this.buttonClearClipRegion.removeActionListener(this.actionListener);
		this.buttonReset.removeActionListener(this.actionListener);
		this.buttonApply.removeActionListener(this.actionListener);
		this.comboBoxColors.removeItemListener(this.itemListener);
	}

	private void fillComponents() {
		this.textFieldPixelFormat.setText(PixelFormatUtilties.toString(this.datasetGrid.getPixelFormat()));
		this.textFieldX.setText(BigDecimal.valueOf(this.datasetGrid.getBounds().getWidth() / this.datasetGrid.getWidth()).toString());
		this.textFieldY.setText(BigDecimal.valueOf(this.datasetGrid.getBounds().getHeight() / this.datasetGrid.getHeight()).toString());
		this.textFieldNoValue.setValue(this.noValue);
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
		this.buttonClearClipRegion.setEnabled(!this.datasetGrid.isReadOnly());
		this.buttonClearClipRegion.setEnabled(!this.datasetGrid.isReadOnly() && this.datasetGrid.getClipRegion() != null);
		this.buttonReset.setEnabled(verifyChange());
		this.buttonApply.setEnabled(verifyChange());
	}

	private boolean verifyChange() {
		return Double.compare(this.noValue, this.datasetGrid.getNoValue()) != 0 || !ColorsUtilties.isColorsEqual(this.colors, this.datasetGrid.getColorTable());
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
		this.colors = (Colors) this.comboBoxColors.getSelectedItem();
		setComponentsEnabled();
	}

	private void buttonSettingClipRegionClicked() {
		IPropertyManager propertyManager = Application.getActiveApplication().getMainFrame().getPropertyManager();
		JDialogSetClipRegion dialog = new JDialogSetClipRegion((JDialog) propertyManager);
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
