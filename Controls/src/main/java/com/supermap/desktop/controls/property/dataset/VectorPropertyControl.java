package com.supermap.desktop.controls.property.dataset;

import com.supermap.data.*;
import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.property.AbstractPropertyControl;
import com.supermap.desktop.enums.PropertyType;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.SMFormattedTextField;
import com.supermap.desktop.ui.controls.CaretPositionListener;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.comboBox.ComboBoxCharset;
import com.supermap.desktop.utilities.SpatialIndexTypeUtilities;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

public class VectorPropertyControl extends AbstractPropertyControl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_LABEL_WIDTH = 100;
	private static final int DEFAULT_COMPONENT_WIDTH = 150;

	private transient CaretPositionListener caretPositionListener = new CaretPositionListener();


	private JLabel labelRecordCount;
	private JTextField textFieldRecordCount;
	private JLabel labelSpatialIndexType;
	private JTextField textFieldSpatialIndexType;
	private JLabel labelCharset;
	private ComboBoxCharset comboBoxCharset;
	private JCheckBox checkBoxIsReadOnly;
	private JCheckBox checkBoxIsFileCache;
	private JButton buttonClearCache;

	private JLabel labelNodeSnap;
	private SMFormattedTextField textFieldNodeSnap;
	private JLabel labelDangle;
	private SMFormattedTextField textFieldDangle;
	private JLabel labelGrain;
	private SMFormattedTextField textFieldGrain;
	private JLabel labelExtend;
	private SMFormattedTextField textFieldExtend;
	private JLabel labelSmallPolygon;
	private SMFormattedTextField textFieldSmallPolygon;
	private SmButton buttonDefaultTolerance;
	private SmButton buttonClearTolerance;

	private SmButton buttonReset;
	private SmButton buttonApply;

	private transient DatasetVector datasetVector;

	private transient Charset charset = Charset.DEFAULT; // 字符集
	private boolean isReadOnly = false; // 只读
	private boolean isFileCache = false; // 用户缓存
	private double nodeSnap = 0.0; // 节点容限
	private double grain = 0.0; // 颗粒容限
	private double smallPolygon = 0.0; // 多边形容限
	private double dangle = 0.0; // 短悬线容限
	private double extend = 0.0; // 长悬线容限

	private transient ItemListener itemListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == comboBoxCharset) {
				comboBoxCharsetSelectedChange(e);
			} else if (e.getSource() == checkBoxIsReadOnly) {
				checkBoxIsReadOnlyCheckedChange();
			} else if (e.getSource() == checkBoxIsFileCache) {
				checkBoxIsFileCacheCheckedChange();
			}
		}
	};
	private transient PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getSource() == textFieldNodeSnap) {
				textFieldNodeSnapValueChange();
			} else if (evt.getSource() == textFieldGrain) {
				textFieldGrainValueChange();
			} else if (evt.getSource() == textFieldSmallPolygon) {
				textFieldSmallPolygonValueChange();
			} else if (evt.getSource() == textFieldDangle) {
				textFieldDangleValueChange();
			} else if (evt.getSource() == textFieldExtend) {
				textFieldExtendValueChange();
			}
		}
	};
	private transient ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttonClearCache) {
				buttonClearCacheClicked();
			} else if (e.getSource() == buttonDefaultTolerance) {
				buttonDefaultToleranceClicked();
			} else if (e.getSource() == buttonClearTolerance) {
				buttonClearToleranceClicked();
			} else if (e.getSource() == buttonReset) {
				buttonResetClicked();
			} else if (e.getSource() == buttonApply) {
				buttonApplyClicked();
			}
		}
	};

	public VectorPropertyControl(DatasetVector datasetVector) {
		super(CommonProperties.getString(CommonProperties.DatasetVector));
		initializeComponents();
		initializeResources();
		setDatasetVector(datasetVector);
	}

	public DatasetVector getDatasetVector() {
		return datasetVector;
	}

	public void setDatasetVector(DatasetVector datasetVector) {
		this.datasetVector = datasetVector;
		unregisterEvents();
		reset();
		fillComponents();
		setComponentsEnabled();
		registerEvents();
	}

	@Override
	public void refreshData() {
		setDatasetVector(this.datasetVector);
	}

	@Override
	public PropertyType getPropertyType() {
		return PropertyType.VECTOR;
	}

	private void initializeComponents() {
		this.labelRecordCount = new JLabel("RecordCount:");
		this.textFieldRecordCount = new JTextField();
		this.textFieldRecordCount.setEditable(false);
		this.labelSpatialIndexType = new JLabel("SpatialIndexType:");
		this.textFieldSpatialIndexType = new JTextField();
		this.textFieldSpatialIndexType.setEditable(false);
		this.labelCharset = new JLabel("Charset:");
		this.comboBoxCharset = new ComboBoxCharset();
		this.checkBoxIsReadOnly = new JCheckBox("IsReadOnly");
		this.checkBoxIsFileCache = new JCheckBox("IsFileCache");
		this.buttonClearCache = new JButton("ClearCache");

		JPanel panelVectorParam = new JPanel();
		panelVectorParam.setBorder(BorderFactory.createTitledBorder(CommonProperties.getString(CommonProperties.DatasetVector)));
		GroupLayout gl_panelVectorParam = new GroupLayout(panelVectorParam);
		gl_panelVectorParam.setAutoCreateContainerGaps(true);
		gl_panelVectorParam.setAutoCreateGaps(true);
		panelVectorParam.setLayout(gl_panelVectorParam);
		// @formatter:off
		gl_panelVectorParam.setHorizontalGroup(gl_panelVectorParam.createSequentialGroup()
				.addGroup(gl_panelVectorParam.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelRecordCount, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH)
						.addComponent(this.labelCharset, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH))
				.addGroup(gl_panelVectorParam.createParallelGroup(Alignment.LEADING)
						.addComponent(this.textFieldRecordCount, DEFAULT_COMPONENT_WIDTH, DEFAULT_COMPONENT_WIDTH, DEFAULT_COMPONENT_WIDTH)
						.addComponent(this.comboBoxCharset, DEFAULT_COMPONENT_WIDTH, DEFAULT_COMPONENT_WIDTH, DEFAULT_COMPONENT_WIDTH))
				.addGroup(gl_panelVectorParam.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelSpatialIndexType, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH)
						.addComponent(this.checkBoxIsReadOnly, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH))
				.addGroup(gl_panelVectorParam.createParallelGroup(Alignment.LEADING)
						.addComponent(this.textFieldSpatialIndexType, GroupLayout.PREFERRED_SIZE, DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)
						.addGroup(gl_panelVectorParam.createSequentialGroup()
								.addComponent(this.checkBoxIsFileCache)
								.addGap(10, 20, Short.MAX_VALUE)
								.addComponent(this.buttonClearCache))));
		
		gl_panelVectorParam.setVerticalGroup(gl_panelVectorParam.createSequentialGroup()
				.addGroup(gl_panelVectorParam.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelRecordCount)
						.addComponent(this.textFieldRecordCount, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.labelSpatialIndexType)
						.addComponent(this.textFieldSpatialIndexType, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panelVectorParam.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelCharset)
						.addComponent(this.comboBoxCharset, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.checkBoxIsReadOnly)
						.addComponent(this.checkBoxIsFileCache)
						.addComponent(this.buttonClearCache)));
		// @formatter:on

		NumberFormat numberInstance = NumberFormat.getNumberInstance();
		numberInstance.setMaximumFractionDigits(20);
		NumberFormatter numberFormatter = new NumberFormatter(numberInstance);
		numberFormatter.setValueClass(Double.class);
		numberFormatter.setMinimum(0.0);
		this.labelNodeSnap = new JLabel("NodeSnap:");
		this.textFieldNodeSnap = new SMFormattedTextField(numberFormatter);
		this.labelDangle = new JLabel("Dangle");
		this.textFieldDangle = new SMFormattedTextField(numberFormatter);
		this.labelExtend = new JLabel("Extend");
		this.textFieldExtend = new SMFormattedTextField(numberFormatter);
		this.labelGrain = new JLabel("Grain:");
		this.textFieldGrain = new SMFormattedTextField(numberFormatter);
		this.labelSmallPolygon = new JLabel("SmallPolygon:");
		this.textFieldSmallPolygon = new SMFormattedTextField(numberFormatter);
		this.buttonDefaultTolerance = new SmButton("Default");
		this.buttonClearTolerance = new SmButton("Clear");

		JPanel panelTolerance = new JPanel();
		panelTolerance.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_DatasetTolerance")));
		GroupLayout gl_panelTolerance = new GroupLayout(panelTolerance);
		gl_panelTolerance.setAutoCreateContainerGaps(true);
		gl_panelTolerance.setAutoCreateGaps(true);
		panelTolerance.setLayout(gl_panelTolerance);
		// @formatter:off
		gl_panelTolerance.setHorizontalGroup(gl_panelTolerance.createSequentialGroup()
				.addGroup(gl_panelTolerance.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelNodeSnap, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH)
						.addComponent(this.labelGrain, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH)
						.addComponent(this.labelSmallPolygon, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH))
				.addGroup(gl_panelTolerance.createParallelGroup(Alignment.LEADING)
						.addComponent(this.textFieldNodeSnap, DEFAULT_COMPONENT_WIDTH, DEFAULT_COMPONENT_WIDTH, DEFAULT_COMPONENT_WIDTH)
						.addComponent(this.textFieldGrain, DEFAULT_COMPONENT_WIDTH, DEFAULT_COMPONENT_WIDTH, DEFAULT_COMPONENT_WIDTH)
						.addComponent(this.textFieldSmallPolygon, DEFAULT_COMPONENT_WIDTH, DEFAULT_COMPONENT_WIDTH, DEFAULT_COMPONENT_WIDTH))
				.addGroup(gl_panelTolerance.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelDangle, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH)
						.addComponent(this.labelExtend, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH))
				.addGroup(gl_panelTolerance.createParallelGroup(Alignment.LEADING)
						.addComponent(this.textFieldDangle, GroupLayout.PREFERRED_SIZE, DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)
						.addComponent(this.textFieldExtend, GroupLayout.PREFERRED_SIZE, DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)
						.addGroup(gl_panelTolerance.createSequentialGroup()
								.addGap(10, 10, Short.MAX_VALUE)
								.addComponent(this.buttonDefaultTolerance, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(this.buttonClearTolerance, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))));
		
		gl_panelTolerance.setVerticalGroup(gl_panelTolerance.createSequentialGroup()
				.addGroup(gl_panelTolerance.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelNodeSnap)
						.addComponent(this.textFieldNodeSnap, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.labelDangle)
						.addComponent(this.textFieldDangle, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panelTolerance.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelGrain)
						.addComponent(this.textFieldGrain, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.labelExtend)
						.addComponent(this.textFieldExtend, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panelTolerance.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelSmallPolygon)
						.addComponent(this.textFieldSmallPolygon, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.buttonDefaultTolerance)
						.addComponent(this.buttonClearTolerance)));
		// |@formatter:on

		this.buttonReset = new SmButton("Reset");
		this.buttonApply = new SmButton("Apply");
		GroupLayout gl_mainContent = new GroupLayout(this);
		gl_mainContent.setAutoCreateContainerGaps(true);
		gl_mainContent.setAutoCreateGaps(true);
		this.setLayout(gl_mainContent);
		// @formatter:off
		gl_mainContent.setHorizontalGroup(gl_mainContent.createParallelGroup(Alignment.LEADING)
				.addComponent(panelVectorParam)
				.addComponent(panelTolerance)
				.addGroup(gl_mainContent.createSequentialGroup()
						.addGap(10, 10, Short.MAX_VALUE)
						.addComponent(this.buttonReset)
						.addComponent(this.buttonApply)));
		
		gl_mainContent.setVerticalGroup(gl_mainContent.createSequentialGroup()
				.addComponent(panelVectorParam, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addComponent(panelTolerance, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addGap(10, 10, Short.MAX_VALUE)
				.addGroup(gl_mainContent.createParallelGroup(Alignment.CENTER)
						.addComponent(this.buttonReset)
						.addComponent(this.buttonApply)));
		// @formatter:on
	}

	private void initializeResources() {
		this.labelRecordCount.setText(ControlsProperties.getString("String_LabelRecordsetCount"));
		this.labelSpatialIndexType.setText(ControlsProperties.getString("String_LabelSpatialIndexType"));
		this.labelCharset.setText(ControlsProperties.getString("String_LabelCharset"));
		this.checkBoxIsReadOnly.setText(CoreProperties.getString(CoreProperties.ReadOnly));
		this.checkBoxIsFileCache.setText(ControlsProperties.getString("String_Property_IsFileCache"));
		this.buttonClearCache.setText(ControlsProperties.getString("String_ClearCache"));
		this.labelNodeSnap.setText(ControlsProperties.getString("String_LabelToleranceFuzzy"));
		this.labelDangle.setText(ControlsProperties.getString("String_LabelToleranceDangle"));
		this.labelGrain.setText(ControlsProperties.getString("String_LabelToleranceGrain"));
		this.labelExtend.setText(ControlsProperties.getString("String_LabelToleranceExtend"));
		this.labelSmallPolygon.setText(ControlsProperties.getString("String_LabelToleranceSmallPolygon"));
		this.buttonDefaultTolerance.setText(CoreProperties.getString(CoreProperties.Default));
		this.buttonClearTolerance.setText(CoreProperties.getString(CoreProperties.Clear));
		this.buttonReset.setText(CommonProperties.getString(CommonProperties.Reset));
		this.buttonApply.setText(CommonProperties.getString(CommonProperties.Apply));
	}

	private void reset() {
		this.charset = this.datasetVector.getCharset();
		this.isReadOnly = this.datasetVector.isReadOnly();
		this.isFileCache = this.datasetVector.isFileCache();
		this.nodeSnap = this.datasetVector.getTolerance().getNodeSnap();
		this.grain = this.datasetVector.getTolerance().getGrain();
		this.smallPolygon = this.datasetVector.getTolerance().getSmallPolygon();
		this.dangle = this.datasetVector.getTolerance().getDangle();
		this.extend = this.datasetVector.getTolerance().getExtend();
	}

	private void fillComponents() {
		this.textFieldRecordCount.setText(Integer.toString(this.datasetVector.getRecordCount()));
		this.textFieldSpatialIndexType.setText(SpatialIndexTypeUtilities.toString(this.datasetVector.getSpatialIndexType()));
		this.comboBoxCharset.setSelectedItem(this.charset);
		this.checkBoxIsReadOnly.setSelected(this.isReadOnly);
		this.checkBoxIsFileCache.setSelected(this.isFileCache);
		this.textFieldNodeSnap.setValue(this.nodeSnap);
		this.textFieldGrain.setValue(this.grain);
		this.textFieldSmallPolygon.setValue(this.smallPolygon);
		this.textFieldDangle.setValue(this.dangle);
		this.textFieldExtend.setValue(this.extend);
	}

	private void registerEvents() {
		caretPositionListener.registerComponent(textFieldNodeSnap, textFieldDangle, textFieldGrain, textFieldExtend, textFieldSmallPolygon);
		this.comboBoxCharset.addItemListener(this.itemListener);
		this.checkBoxIsReadOnly.addItemListener(this.itemListener);
		this.checkBoxIsFileCache.addItemListener(this.itemListener);
		this.textFieldNodeSnap.addPropertyChangeListener(ControlDefaultValues.PROPERTYNAME_VALUE, this.propertyChangeListener);
		this.textFieldGrain.addPropertyChangeListener(ControlDefaultValues.PROPERTYNAME_VALUE, this.propertyChangeListener);
		this.textFieldSmallPolygon.addPropertyChangeListener(ControlDefaultValues.PROPERTYNAME_VALUE, this.propertyChangeListener);
		this.textFieldDangle.addPropertyChangeListener(ControlDefaultValues.PROPERTYNAME_VALUE, this.propertyChangeListener);
		this.textFieldExtend.addPropertyChangeListener(ControlDefaultValues.PROPERTYNAME_VALUE, this.propertyChangeListener);
		this.buttonClearCache.addActionListener(this.actionListener);
		this.buttonDefaultTolerance.addActionListener(this.actionListener);
		this.buttonClearTolerance.addActionListener(this.actionListener);
		this.buttonReset.addActionListener(this.actionListener);
		this.buttonApply.addActionListener(this.actionListener);
	}

	private void unregisterEvents() {
		caretPositionListener.unRegisterComponent(textFieldNodeSnap, textFieldDangle, textFieldGrain, textFieldExtend, textFieldSmallPolygon);
		this.comboBoxCharset.removeItemListener(this.itemListener);
		this.checkBoxIsReadOnly.removeItemListener(this.itemListener);
		this.checkBoxIsFileCache.removeItemListener(this.itemListener);
		this.textFieldNodeSnap.removePropertyChangeListener(ControlDefaultValues.PROPERTYNAME_VALUE, this.propertyChangeListener);
		this.textFieldGrain.removePropertyChangeListener(ControlDefaultValues.PROPERTYNAME_VALUE, this.propertyChangeListener);
		this.textFieldSmallPolygon.removePropertyChangeListener(ControlDefaultValues.PROPERTYNAME_VALUE, this.propertyChangeListener);
		this.textFieldDangle.removePropertyChangeListener(ControlDefaultValues.PROPERTYNAME_VALUE, this.propertyChangeListener);
		this.textFieldExtend.removePropertyChangeListener(ControlDefaultValues.PROPERTYNAME_VALUE, this.propertyChangeListener);
		this.buttonClearCache.removeActionListener(this.actionListener);
		this.buttonDefaultTolerance.removeActionListener(this.actionListener);
		this.buttonClearTolerance.removeActionListener(this.actionListener);
		this.buttonReset.removeActionListener(this.actionListener);
		this.buttonApply.removeActionListener(this.actionListener);
	}

	private void setComponentsEnabled() {
		this.checkBoxIsFileCache.setEnabled(this.datasetVector.getDatasource().getEngineType() == EngineType.ORACLEPLUS
				&& this.datasetVector.getSpatialIndexType() == SpatialIndexType.TILE);
		this.checkBoxIsReadOnly.setEnabled(!datasetVector.getDatasource().isReadOnly());
		this.textFieldNodeSnap.setEnabled(!isReadOnly);
		this.textFieldGrain.setEnabled(!isReadOnly);
		this.textFieldSmallPolygon.setEnabled(!isReadOnly);
		this.textFieldDangle.setEnabled(!isReadOnly);
		this.textFieldExtend.setEnabled(!isReadOnly);
		this.comboBoxCharset.setEnabled(!isReadOnly);
		this.buttonDefaultTolerance.setEnabled(!isReadOnly);
		this.buttonClearTolerance.setEnabled(!isReadOnly);

		this.buttonApply.setEnabled(verifyChange());
		this.buttonReset.setEnabled(verifyChange());
	}

	private boolean verifyChange() {
		return this.charset != this.datasetVector.getCharset() || this.isReadOnly != this.datasetVector.isReadOnly()
				|| this.isFileCache != this.datasetVector.isFileCache() || Double.compare(this.nodeSnap, this.datasetVector.getTolerance().getNodeSnap()) != 0
				|| Double.compare(this.grain, this.datasetVector.getTolerance().getGrain()) != 0
				|| Double.compare(this.smallPolygon, this.datasetVector.getTolerance().getSmallPolygon()) != 0
				|| Double.compare(this.dangle, this.datasetVector.getTolerance().getDangle()) != 0
				|| Double.compare(this.extend, this.datasetVector.getTolerance().getExtend()) != 0;
	}

	private void comboBoxCharsetSelectedChange(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			this.charset = this.comboBoxCharset.getSelectedItem();
			setComponentsEnabled();
		}
	}

	private void checkBoxIsReadOnlyCheckedChange() {
		this.isReadOnly = this.checkBoxIsReadOnly.isSelected();
		setComponentsEnabled();
	}

	private void checkBoxIsFileCacheCheckedChange() {
		this.isFileCache = this.checkBoxIsFileCache.isSelected();
		setComponentsEnabled();
	}

	private void textFieldNodeSnapValueChange() {
		this.nodeSnap = (Double) this.textFieldNodeSnap.getValue();
		setComponentsEnabled();
	}

	private void textFieldGrainValueChange() {
		this.grain = (Double) this.textFieldGrain.getValue();
		setComponentsEnabled();
	}

	private void textFieldSmallPolygonValueChange() {
		this.smallPolygon = (Double) this.textFieldSmallPolygon.getValue();
		setComponentsEnabled();
	}

	private void textFieldDangleValueChange() {
		this.dangle = (Double) this.textFieldDangle.getValue();
		setComponentsEnabled();
	}

	private void textFieldExtendValueChange() {
		this.extend = (Double) this.textFieldExtend.getValue();
		setComponentsEnabled();
	}

	private void buttonClearCacheClicked() {
		// 暂未实现
	}

	private void buttonDefaultToleranceClicked() {
		Tolerance datasetTolerance = this.datasetVector.getTolerance();
		Tolerance tolerance = new Tolerance(datasetTolerance);
		datasetTolerance.setDefault();
		this.nodeSnap = datasetTolerance.getNodeSnap();
		this.grain = datasetTolerance.getGrain();
		this.smallPolygon = datasetTolerance.getSmallPolygon();
		this.dangle = datasetTolerance.getDangle();
		this.extend = datasetTolerance.getExtend();
		this.datasetVector.setTolerance(tolerance);
		fillComponents();
		setComponentsEnabled();
	}

	private void buttonClearToleranceClicked() {
		this.nodeSnap = 0.0;
		this.grain = 0.0;
		this.smallPolygon = 0.0;
		this.dangle = 0.0;
		this.extend = 0.0;
		fillComponents();
		setComponentsEnabled();
	}

	private void buttonResetClicked() {
		reset();
		fillComponents();
		setComponentsEnabled();
	}

	private void buttonApplyClicked() {
		if (this.datasetVector.getCharset() != this.charset) {
			this.datasetVector.setCharset(this.charset);
		}
		if (this.datasetVector.isReadOnly() != this.isReadOnly) {
			this.datasetVector.setReadOnly(this.isReadOnly);
		}
		if (this.datasetVector.isFileCache() != this.isFileCache) {
			this.datasetVector.setFileCache(this.isFileCache);
		}
		if (Double.compare(this.datasetVector.getTolerance().getNodeSnap(), this.nodeSnap) != 0) {
			this.datasetVector.getTolerance().setNodeSnap(this.nodeSnap);
		}
		if (Double.compare(this.datasetVector.getTolerance().getGrain(), this.grain) != 0) {
			this.datasetVector.getTolerance().setGrain(this.grain);
		}
		if (Double.compare(this.datasetVector.getTolerance().getSmallPolygon(), this.smallPolygon) != 0) {
			this.datasetVector.getTolerance().setSmallPolygon(this.smallPolygon);
		}
		if (Double.compare(this.datasetVector.getTolerance().getDangle(), this.dangle) != 0) {
			this.datasetVector.getTolerance().setDangle(this.dangle);
		}
		if (Double.compare(this.datasetVector.getTolerance().getExtend(), this.extend) != 0) {
			this.datasetVector.getTolerance().setExtend(this.extend);
		}
		setComponentsEnabled();
	}
}
