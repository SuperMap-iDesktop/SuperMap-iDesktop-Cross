package com.supermap.desktop.spatialanalyst.vectoranalyst;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.text.NumberFormatter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import net.infonode.properties.propertymap.ref.ThisPropertyMapRef;

import com.supermap.analyst.spatialanalyst.BufferAnalyst;
import com.supermap.analyst.spatialanalyst.BufferAnalystParameter;
import com.supermap.analyst.spatialanalyst.BufferEndType;
import com.supermap.analyst.spatialanalyst.BufferRadiusUnit;
import com.supermap.data.CursorType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.DatasetVectorInfo;
import com.supermap.data.Datasource;
import com.supermap.data.FieldInfo;
import com.supermap.data.FieldType;
import com.supermap.data.Recordset;
import com.supermap.data.Unit;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.implement.SmTextField;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.mapping.Selection;
import com.supermap.ui.MapControl;

public class PanelLineBufferAnalyst extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panelBasic;
	private JPanel panelBufferRadius;
	private JPanel panelBufferType;
	private JPanel panelBasicLeft;
	private JPanel panelBasicRight;
	private JLabel labelUnit;
	private JLabel labelLeftNumericRadius;
	private JLabel labelRightNumericRadius;
	private JLabel labelLeftFieldRadius;
	private JLabel labelRightFieldRadius;

	private JRadioButton radioButtonNumeric;
	private JRadioButton radioButtonField;
	private JComboBox<Unit> comboBoxUnit;
	private JTextField textFieldNumericLeft;
	private JTextField textFieldNumericRight;
	private JComboBox<Object> comboBoxFieldLeft;
	private JComboBox<Object> comboBoxFieldright;

	private JRadioButton radioButtonBufferTypeRound;
	private JRadioButton radioButtonBufferTypeFlat;
	private JCheckBox checkBoxBufferLeft;
	private JCheckBox checkBoxBufferRight;
	private PanelBufferData panelBufferData;
	private PanelResultData panelResultData;
	private PanelResultSet panelResultSet;
	private MapControl mapControl;
	private Recordset recordset;
	private Object radiusLeft;
	private Object radiusRight;
	private InitComboBoxUnit initComboBoxUnit = new InitComboBoxUnit();
	private LocalKeylistener localKeylistener = new LocalKeylistener();

	private boolean buttonEnabled = false;
	private String resultDatasetName;

	public boolean isButtonEnabled() {
		return buttonEnabled;
	}

	public void setButtonEnabled(boolean buttonEnabled) {
		this.buttonEnabled = buttonEnabled;
	}

	public PanelLineBufferAnalyst() {
		initComponent();
		initResources();
		setPanelLineBufferAnalyst();

	}

	private void initComponent() {
		this.panelBasic = new JPanel();
		this.panelBasicLeft = new JPanel();
		this.panelBasicRight = new JPanel();
		this.panelBufferRadius = new JPanel();
		this.panelBufferType = new JPanel();
		this.panelBufferData = new PanelBufferData();
		this.panelResultData = new PanelResultData();
		this.panelResultSet = new PanelResultSet();
		this.add(this.panelBasic);
		initComponentBufferType();
		initComponentBufferRadius();
		setPanelBasicLayout();
		setPanelBasicLeftLayout();
		setPanelBasicRightLayout();

	}

	private void initResources() {
		this.panelBufferRadius.setBorder(BorderFactory.createTitledBorder(SpatialAnalystProperties.getString("String_BufferRadius")));
		this.panelBufferType.setBorder(BorderFactory.createTitledBorder(SpatialAnalystProperties.getString("String_BufferType")));
		this.labelUnit.setText(SpatialAnalystProperties.getString("String_BufferRadiusUnit"));
		this.labelLeftNumericRadius.setText(SpatialAnalystProperties.getString("String_Label_LeftBufferRadius"));
		this.labelRightNumericRadius.setText(SpatialAnalystProperties.getString("String_Label_RightBufferRadius"));
		this.labelLeftFieldRadius.setText(SpatialAnalystProperties.getString("String_Label_LeftBufferRadius"));
		this.labelRightFieldRadius.setText(SpatialAnalystProperties.getString("String_Label_RightBufferRadius"));
		this.radioButtonNumeric.setText(SpatialAnalystProperties.getString("String_NumericBufferRadius"));
		this.radioButtonField.setText(SpatialAnalystProperties.getString("String_FieldBufferRadius"));
		this.radioButtonBufferTypeRound.setText(SpatialAnalystProperties.getString("String_BufferTypeRound"));
		this.radioButtonBufferTypeFlat.setText(SpatialAnalystProperties.getString("String_BufferTypeFlat"));
		this.checkBoxBufferLeft.setText(SpatialAnalystProperties.getString("String_BufferTypeLeft"));
		this.checkBoxBufferRight.setText(SpatialAnalystProperties.getString("String_BufferTypeRight"));
	}

	private void setPanelLineBufferAnalyst() {
		setPanelBufferData();
		setPanelResultSet();
		setPanelBufferType();
		setPanelBuffeRadius();
		registerEvent();
	}

	private void initComponentBufferType() {
		this.radioButtonBufferTypeRound = new JRadioButton("BufferTypeRound");
		this.radioButtonBufferTypeFlat = new JRadioButton("BufferTypeFlat");
		this.checkBoxBufferLeft = new JCheckBox("BufferTypeLeft");
		this.checkBoxBufferRight = new JCheckBox("BufferTypeRight");

		ButtonGroup bufferTypeButtonGroup = new ButtonGroup();
		bufferTypeButtonGroup.add(this.radioButtonBufferTypeRound);
		bufferTypeButtonGroup.add(this.radioButtonBufferTypeFlat);

		GroupLayout panelBufferTypeLayout = new GroupLayout(this.panelBufferType);
		this.panelBufferType.setLayout(panelBufferTypeLayout);

		//@formatter:off
		panelBufferTypeLayout.setHorizontalGroup(panelBufferTypeLayout.createSequentialGroup()
				.addGroup(panelBufferTypeLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.radioButtonBufferTypeRound)
						.addComponent(this.radioButtonBufferTypeFlat)).addGap(50)
				.addGroup(panelBufferTypeLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.checkBoxBufferLeft)
						.addComponent(this.checkBoxBufferRight)).addGap(75));
		
		panelBufferTypeLayout.setVerticalGroup(panelBufferTypeLayout.createSequentialGroup()
				.addGroup(panelBufferTypeLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.radioButtonBufferTypeRound)
						.addComponent(this.checkBoxBufferLeft)).addGap(10)
				.addGroup(panelBufferTypeLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.radioButtonBufferTypeFlat)
						.addComponent(this.checkBoxBufferRight)));
		//@formatter:on

	}

	private void initComponentBufferRadius() {
		this.labelUnit = new JLabel("Unit");
		this.labelLeftNumericRadius = new JLabel("LeftNumericRadius");
		this.labelRightNumericRadius = new JLabel("RightNumericRadius");
		this.labelLeftFieldRadius = new JLabel("RightFieldRadius");
		this.labelRightFieldRadius = new JLabel("RightFieldRadius");
		this.radioButtonNumeric = new JRadioButton("Numeric");
		this.radioButtonField = new JRadioButton("Field");
		this.comboBoxUnit = initComboBoxUnit.createComboBoxUnit();

		// NumberFormatter numberFormatter = new NumberFormatter();
		// numberFormatter.setValueClass(Integer.class);
		this.textFieldNumericLeft = new JFormattedTextField(NumberFormat.getInstance());
		this.textFieldNumericRight = new JFormattedTextField(NumberFormat.getInstance());
		this.textFieldNumericLeft.setText("10");
		this.textFieldNumericRight.setText("10");

		this.comboBoxFieldLeft = new JComboBox<Object>();
		this.comboBoxFieldright = new JComboBox<Object>();

		ButtonGroup bufferRadiusButtonGroup = new ButtonGroup();
		bufferRadiusButtonGroup.add(this.radioButtonNumeric);
		bufferRadiusButtonGroup.add(this.radioButtonField);

		//@formatter:off
		GroupLayout panelBufferRadiusLayout = new GroupLayout(this.panelBufferRadius);
		this.panelBufferRadius.setLayout(panelBufferRadiusLayout);
		
		panelBufferRadiusLayout.setHorizontalGroup(panelBufferRadiusLayout.createSequentialGroup()
				.addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelUnit)
						.addComponent(this.radioButtonNumeric)
						.addComponent(this.labelLeftNumericRadius)
						.addComponent(this.labelRightNumericRadius)
						.addComponent(this.radioButtonField)
						.addComponent(this.labelLeftFieldRadius)
						.addComponent(this.labelRightFieldRadius)).addGap(10)
				.addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.comboBoxUnit)
						.addComponent(this.textFieldNumericLeft)
						.addComponent(this.textFieldNumericRight)
						.addComponent(this.comboBoxFieldLeft)
						.addComponent(this.comboBoxFieldright)).addContainerGap());
		
		panelBufferRadiusLayout.setVerticalGroup(panelBufferRadiusLayout.createSequentialGroup()
				.addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelUnit)
						.addComponent(this.comboBoxUnit)).addGap(8)
			    .addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.radioButtonNumeric)).addGap(5)
			    .addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.LEADING)
					   .addComponent(this.labelLeftNumericRadius)
					   .addComponent(this.textFieldNumericLeft)).addGap(15)
				.addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelRightNumericRadius)
						.addComponent(this.textFieldNumericRight)).addGap(5)
				.addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.radioButtonField)).addGap(5)
				.addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelLeftFieldRadius)
						.addComponent(this.comboBoxFieldLeft)).addGap(15)
				.addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelRightFieldRadius)
						.addComponent(this.comboBoxFieldright)).addContainerGap().addGap(5));
		//@formatter:on

	}

	private void setPanelBasicLayout() {
		GroupLayout panelBasicLayout = new GroupLayout(this.panelBasic);
		this.panelBasic.setLayout(panelBasicLayout);

		//@formatter:off
	    panelBasicLayout.setHorizontalGroup(panelBasicLayout.createSequentialGroup()
	    		.addContainerGap()
	    		.addComponent(this.panelBasicLeft)
	    		.addComponent(this.panelBasicRight).addContainerGap());
	    
	    panelBasicLayout.setVerticalGroup(panelBasicLayout.createSequentialGroup()
	    		.addContainerGap()
	    		.addGroup(panelBasicLayout.createParallelGroup(Alignment.CENTER)
	    				.addComponent(this.panelBasicLeft)
	    				.addComponent(this.panelBasicRight)).addContainerGap());
	    //@formatter:on
		panelBasicLayout.linkSize(this.panelBasicLeft, this.panelBasicRight);

	}

	private void setPanelBasicLeftLayout() {
		GroupLayout panelBasicLeftLayout = new GroupLayout(this.panelBasicLeft);
		this.panelBasicLeft.setLayout(panelBasicLeftLayout);

		//@formatter:off
		panelBasicLeftLayout.setHorizontalGroup(panelBasicLeftLayout.createSequentialGroup()
				.addGroup(panelBasicLeftLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.panelBufferData)
						.addComponent(this.panelResultSet)
						.addComponent(this.panelResultData)));
		
		panelBasicLeftLayout.setVerticalGroup(panelBasicLeftLayout.createSequentialGroup()
				.addComponent(this.panelBufferData)
				.addComponent(this.panelResultSet)
				.addComponent(this.panelResultData).addContainerGap());
		//@formatter:on

	}

	private void setPanelBasicRightLayout() {
		GroupLayout panelBasicRightLayout = new GroupLayout(this.panelBasicRight);
		this.panelBasicRight.setLayout(panelBasicRightLayout);

		//@formatter:off
		panelBasicRightLayout.setHorizontalGroup(panelBasicRightLayout.createSequentialGroup()
				.addGroup(panelBasicRightLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.panelBufferType)
						.addComponent(this.panelBufferRadius)));
		
		panelBasicRightLayout.setVerticalGroup(panelBasicRightLayout.createSequentialGroup()
				.addComponent(this.panelBufferType)
				.addComponent(this.panelBufferRadius).addContainerGap());
		//@formatter:on

	}

	/**
	 * 当窗体界面打开时，且打开的窗体是地图时，如果数据集不是线或者网络数据集，设置选中数据集的数据源的第一个线或者网络数据集，否则设置数据集为选中地图的第一个数据集 如果窗体没有打开，获取工作空间树选中节点,得到选中的数据集，数据源
	 */
	private void setPanelBufferData() {
		setComboBoxDatasetType();
		// 窗体激活，且打开的窗体是地图,如果窗体没有激活，直接获取工作空间树节点，通过树节点数据
		if (Application.getActiveApplication().getActiveForm() != null && Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
			this.mapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
			Dataset activeDataset = this.mapControl.getMap().getLayers().get(0).getDataset();
			this.panelBufferData.getComboBoxBufferDataDatasource().setSelectedDatasource(activeDataset.getDatasource());
			this.panelResultData.getComboBoxResultDataDatasource().setSelectedDatasource(activeDataset.getDatasource());
			this.panelBufferData.getComboBoxBufferDataDataset().setDatasets(activeDataset.getDatasource().getDatasets());
			if (activeDataset.getType() == DatasetType.LINE || activeDataset.getType() == DatasetType.LINE3D || activeDataset.getType() == DatasetType.NETWORK
					|| activeDataset.getType() == DatasetType.NETWORK3D) {
				this.panelBufferData.getComboBoxBufferDataDataset().setSelectedDataset(activeDataset);
			}
			this.panelBufferData.getCheckBoxGeometrySelect().setEnabled(hasSelectedGeometryProperty());
			setComponentEnabled();
		} else {
			WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
			TreePath selectedPath = workspaceTree.getSelectionPath();
			if (selectedPath != null && selectedPath.getLastPathComponent() instanceof DefaultMutableTreeNode) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
				TreeNodeData nodeData = (TreeNodeData) selectedNode.getUserObject();
				if (nodeData.getData() instanceof Datasource) {
					Datasource selectedDatasource = (Datasource) nodeData.getData();
					this.panelBufferData.getComboBoxBufferDataDatasource().setSelectedDatasource(selectedDatasource);
					this.panelResultData.getComboBoxResultDataDatasource().setSelectedDatasource(selectedDatasource);
					this.panelBufferData.getComboBoxBufferDataDataset().setDatasets(selectedDatasource.getDatasets());
				} else if (nodeData.getData() instanceof Dataset) {
					Dataset selectedDataset = (Dataset) nodeData.getData();
					this.panelBufferData.getComboBoxBufferDataDatasource().setSelectedDatasource(selectedDataset.getDatasource());
					this.panelResultData.getComboBoxResultDataDatasource().setSelectedDatasource(selectedDataset.getDatasource());
					this.panelBufferData.getComboBoxBufferDataDataset().setDatasets(selectedDataset.getDatasource().getDatasets());
					if (selectedDataset.getType() == DatasetType.LINE || selectedDataset.getType() == DatasetType.LINE3D
							|| selectedDataset.getType() == DatasetType.NETWORK || selectedDataset.getType() == DatasetType.NETWORK3D) {
						this.panelBufferData.getComboBoxBufferDataDataset().setSelectedDataset(selectedDataset);

					}
				} else {
					initDataSourceAndDataSet();
				}
			} else {
				initDataSourceAndDataSet();
			}
			this.panelBufferData.getCheckBoxGeometrySelect().setEnabled(hasSelectedGeometryProperty());
			setComponentEnabled();
		}
	}

	/**
	 * 对结果面板进行设置
	 */
	private void setPanelResultSet() {
		this.panelResultSet.getCheckBoxDisplayInMap().setSelected(true);
		this.panelResultSet.getCheckBoxRemainAttributes().setSelected(true);
	}

	private void setPanelBufferType() {
		this.radioButtonBufferTypeRound.setSelected(true);
		this.checkBoxBufferLeft.setSelected(true);
		this.checkBoxBufferRight.setSelected(true);
		setComponentEnabled();
	}

	private void setPanelBuffeRadius() {
		this.radioButtonNumeric.setSelected(true);
		if (this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() != null) {
			Dataset comboBoxDataset = this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset();
			createComboBoxField(comboBoxDataset);
			setButtonEnabled(true);
		} else {
			setButtonEnabled(false);
		}
		setComponentEnabled();

	}

	private void registerEvent() {
		this.panelBufferData.getComboBoxBufferDataDatasource().addItemListener(new LocalItemListener());
		this.panelBufferData.getComboBoxBufferDataDataset().addItemListener(new LocalItemListener());
		this.panelBufferData.getCheckBoxGeometrySelect().addItemListener(new LocalItemListener());
		this.checkBoxBufferLeft.addItemListener(new LocalItemListener());
		this.checkBoxBufferRight.addItemListener(new LocalItemListener());
		this.panelResultSet.getCheckBoxDisplayInMap().addItemListener(new LocalItemListener());
		this.panelResultSet.getCheckBoxDisplayInScene().addItemListener(new LocalItemListener());
		this.panelResultSet.getCheckBoxRemainAttributes().addItemListener(new LocalItemListener());
		this.panelResultSet.getCheckBoxUnionBuffer().addItemListener(new LocalItemListener());
		this.radioButtonBufferTypeFlat.addActionListener(new LocalActionListener());
		this.radioButtonBufferTypeRound.addActionListener(new LocalActionListener());
		this.radioButtonNumeric.addActionListener(new LocalActionListener());
		this.radioButtonField.addActionListener(new LocalActionListener());
		this.comboBoxFieldLeft.addActionListener(new LocalActionListener());
		this.comboBoxFieldright.addActionListener(new LocalActionListener());
		this.textFieldNumericLeft.addKeyListener(localKeylistener);
		this.textFieldNumericRight.addKeyListener(localKeylistener);
	}

	private void setComboBoxDatasetType() {
		ArrayList<DatasetType> datasetTypes = new ArrayList<DatasetType>();
		datasetTypes.add(DatasetType.LINE);
		datasetTypes.add(DatasetType.LINE3D);
		datasetTypes.add(DatasetType.NETWORK);
		datasetTypes.add(DatasetType.NETWORK3D);
		this.panelBufferData.getComboBoxBufferDataDataset().setDatasetTypes(datasetTypes.toArray(new DatasetType[datasetTypes.size()]));
	}

	private boolean hasSelectedGeometryProperty() {
		if (this.mapControl != null && this.mapControl.getMap() != null) {
			// 默认取第一个选择集的多个对象
			Selection[] selections = this.mapControl.getMap().findSelection(true);
			if (selections.length > 0) {
				Selection selection = selections[0];
				int[] selectionCount = new int[selection.getCount()];
				for (int i = 0; i < selection.getCount(); i++) {
					selectionCount[i] = selection.get(i);
				}
				DatasetVector datasetVector = selection.getDataset();
				this.recordset = datasetVector.query(selectionCount, CursorType.DYNAMIC);
				if (datasetVector.getType() == DatasetType.LINE || datasetVector.getType() == DatasetType.LINE3D
						|| datasetVector.getType() == DatasetType.NETWORK || datasetVector.getType() == DatasetType.NETWORK3D) {
					this.panelBufferData.getCheckBoxGeometrySelect().setSelected(true);
					return true;
				}
			}
		}
		this.panelBufferData.getCheckBoxGeometrySelect().setSelected(false);
		return false;

	}

	private void initDataSourceAndDataSet() {
		Datasource defaultDatasource = Application.getActiveApplication().getWorkspace().getDatasources().get(0);
		this.panelBufferData.getComboBoxBufferDataDatasource().setSelectedDatasource(defaultDatasource);
		this.panelResultData.getComboBoxResultDataDatasource().setSelectedDatasource(defaultDatasource);
		this.panelBufferData.getComboBoxBufferDataDataset().setDatasets(defaultDatasource.getDatasets());
	}

	private void setComponentEnabled() {
		this.panelBufferData.getComboBoxBufferDataDataset().setEnabled(!this.panelBufferData.getCheckBoxGeometrySelect().isSelected());
		this.panelBufferData.getComboBoxBufferDataDatasource().setEnabled(!this.panelBufferData.getCheckBoxGeometrySelect().isSelected());
		this.comboBoxFieldLeft.setEnabled(this.radioButtonField.isSelected() && this.checkBoxBufferLeft.isSelected());
		this.comboBoxFieldright.setEnabled(this.radioButtonField.isSelected() && this.checkBoxBufferRight.isSelected());
		this.textFieldNumericLeft.setEnabled(this.radioButtonNumeric.isSelected() && this.checkBoxBufferLeft.isSelected());
		this.textFieldNumericRight.setEnabled(this.radioButtonNumeric.isSelected() && this.checkBoxBufferRight.isSelected());
		this.panelResultSet.getCheckBoxRemainAttributes().setEnabled(!this.panelResultSet.getCheckBoxUnionBuffer().isSelected());
		this.checkBoxBufferLeft.setEnabled(!this.radioButtonBufferTypeRound.isSelected());
		this.checkBoxBufferRight.setEnabled(!this.radioButtonBufferTypeRound.isSelected());
	}

	private void createComboBoxField(Dataset comboBoxDataset) {
		if (comboBoxDataset instanceof DatasetVector) {
			DatasetVector comboBoxDatasetVector = (DatasetVector) comboBoxDataset;
			for (int i = 0; i < comboBoxDatasetVector.getFieldCount(); i++) {
				FieldInfo fieldInfo = comboBoxDatasetVector.getFieldInfos().get(i);
				if (!fieldInfo.isSystemField()) {
					if (fieldInfo.getType() == FieldType.INT16 || fieldInfo.getType() == FieldType.INT32 || fieldInfo.getType() == FieldType.SINGLE
							|| fieldInfo.getType() == FieldType.DOUBLE) {
						this.comboBoxFieldLeft.addItem(comboBoxDatasetVector.getFieldInfos().get(i).getName());
						this.comboBoxFieldright.addItem(comboBoxDatasetVector.getFieldInfos().get(i).getName());
					}
				}
			}
		}
	}

	/**
	 * 创建缓冲区分析
	 */
	public void CreateCurrentBuffer() {
		Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources()
				.get(this.panelResultData.getComboBoxResultDataDatasource().getSelectedDatasource().getAlias());
		DatasetVectorInfo resultDatasetVectorInfo = new DatasetVectorInfo();
		this.resultDatasetName = this.panelResultData.getTextFieldResultDataDataset().getText();
		String datasetName = datasource.getDatasets().getAvailableDatasetName(this.resultDatasetName);
		resultDatasetVectorInfo.setName(datasetName);
		resultDatasetVectorInfo.setType(DatasetType.REGION);

		DatasetVector resultDatasetVector = datasource.getDatasets().create(resultDatasetVectorInfo);
		if (this.panelBufferData.getComboBoxBufferDataDataset() != null) {
			DatasetVector sourceDatasetVector = (DatasetVector) this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset();
			BufferAnalystParameter bufferAnalystParameter = new BufferAnalystParameter();

			// radioButtonNumeric被选中，当数据集类型为点对象时，缓冲半径取绝对值
			if (this.radioButtonNumeric.isSelected()) {
				this.radiusLeft =  Math.abs(Integer.parseInt(this.textFieldNumericLeft.getText().replaceAll(",", "")));
				this.radiusRight =  Math.abs(Integer.parseInt(this.textFieldNumericRight.getText().replaceAll(",", "")));
			}

			if (this.radioButtonBufferTypeRound.isSelected()) {
				bufferAnalystParameter.setEndType(BufferEndType.ROUND);
				bufferAnalystParameter.setLeftDistance(this.radiusLeft);
				bufferAnalystParameter.setRightDistance(this.radiusRight);
			} else if (this.radioButtonBufferTypeFlat.isSelected()) {
				bufferAnalystParameter.setEndType(BufferEndType.FLAT);
				if (this.checkBoxBufferLeft.isSelected()) {
					bufferAnalystParameter.setLeftDistance(this.radiusLeft);
				}
				if (this.checkBoxBufferRight.isSelected()) {
					bufferAnalystParameter.setRightDistance(this.radiusRight);
				}
			}
			bufferAnalystParameter.setRadiusUnit(initComboBoxUnit.getBufferRadiusUnit(this.comboBoxUnit.getSelectedItem().toString()));
			bufferAnalystParameter.setSemicircleLineSegment( Integer.parseInt(this.panelResultSet.getTextFieldSemicircleLineSegment().getText()));

			// 当CheckBoxGeometrySelect()选中时，进行记录集缓冲分析，否则进行数据集缓冲分析
			if (this.panelBufferData.getCheckBoxGeometrySelect().isSelected()) {
				BufferAnalyst.createBuffer(recordset, resultDatasetVector, bufferAnalystParameter, this.panelResultSet.getCheckBoxUnionBuffer().isSelected(),
						this.panelResultSet.getCheckBoxRemainAttributes().isSelected());
			} else {
				BufferAnalyst.createBuffer(sourceDatasetVector, resultDatasetVector, bufferAnalystParameter, this.panelResultSet.getCheckBoxUnionBuffer()
						.isSelected(), this.panelResultSet.getCheckBoxRemainAttributes().isSelected());
			}
		}
	}

	class LocalItemListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {

			if (e.getSource() == panelBufferData.getComboBoxBufferDataDatasource()) {
				panelBufferData.getComboBoxBufferDataDataset().removeAllItems();
				String datasourceAlis = e.getItem().toString();
				Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasourceAlis);
				if (e.getStateChange() == ItemEvent.SELECTED) {
					panelBufferData.getComboBoxBufferDataDataset().setDatasets(datasource.getDatasets());
				}
				// 切换数据源后，如果ComboBoxDataset为空时，清除字段选项
				if (panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() != null) {
					setButtonEnabled(true);
				} else {
					// 切换comboBoxDatasource时，如果comboBoxDataset为空时将字段选项置灰，默认选中数值型
					setButtonEnabled(false);
					comboBoxFieldLeft.removeAllItems();
					comboBoxFieldright.removeAllItems();
					// radioButtonNumeric.setSelected(true);
				}
				setComponentEnabled();
			} else if (e.getSource() == panelBufferData.getComboBoxBufferDataDataset()) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					comboBoxFieldLeft.removeAllItems();
					comboBoxFieldright.removeAllItems();
					// 如果所选数据集不为空，创建字段ComboBox，否则不做操作
					if (panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() != null) {
						Dataset datasetItem = panelBufferData.getComboBoxBufferDataDatasource().getSelectedDatasource().getDatasets()
								.get(e.getItem().toString());
						createComboBoxField(datasetItem);
					}
				}
			} else if (e.getSource() == panelBufferData.getCheckBoxGeometrySelect()) {
				setComponentEnabled();
			} else if (e.getSource() == panelResultSet.getCheckBoxUnionBuffer()) {
				setComponentEnabled();
				panelResultSet.getCheckBoxRemainAttributes().setSelected(false);
			} else if (e.getSource() == panelResultSet.getCheckBoxUnionBuffer()) {
				setComponentEnabled();
				panelResultSet.getCheckBoxRemainAttributes().setSelected(false);
			} else if (e.getSource() == checkBoxBufferLeft) {
				setComponentEnabled();
			} else if (e.getSource() == checkBoxBufferRight) {
				setComponentEnabled();
			}
		}
	}

	class LocalActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == radioButtonBufferTypeRound) {

				textFieldNumericLeft.addKeyListener(localKeylistener);
				textFieldNumericRight.addKeyListener(localKeylistener);
				setComponentEnabled();
				checkBoxBufferLeft.setSelected(true);
				checkBoxBufferRight.setSelected(true);
			} else if (e.getSource() == radioButtonBufferTypeFlat) {
				textFieldNumericLeft.removeKeyListener(localKeylistener);
				textFieldNumericRight.removeKeyListener(localKeylistener);

				setComponentEnabled();
			} else if (e.getSource() == radioButtonField) {
				if (comboBoxFieldLeft.getSelectedItem() != null) {
					radiusLeft = comboBoxFieldLeft.getSelectedItem().toString();
				}
				if (comboBoxFieldright.getSelectedItem() != null) {
					radiusRight = comboBoxFieldright.getSelectedItem().toString();
				}
				setComponentEnabled();
			} else if (e.getSource() == radioButtonNumeric) {
				radiusLeft = Math.abs(Integer.parseInt(textFieldNumericLeft.getText().replaceAll(",", "")));
				radiusRight =  Math.abs(Integer.parseInt(textFieldNumericRight.getText().replaceAll(",", "")));
				setComponentEnabled();
			} else if (e.getSource() == comboBoxFieldLeft) {
				if (comboBoxFieldLeft.getSelectedItem() != null) {
					radiusLeft = comboBoxFieldLeft.getSelectedItem().toString();
				}
			} else if (e.getSource() == comboBoxFieldright) {
				if (comboBoxFieldright.getSelectedItem() != null) {
					radiusRight = comboBoxFieldright.getSelectedItem().toString();
				}
			}
		}
	}

	class LocalKeylistener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			

		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyReleased(KeyEvent e) {
			textFieldNumericRight.setText(((JTextField) e.getSource()).getText());
			textFieldNumericLeft.setText(((JTextField) e.getSource()).getText());
		}

	}

}
