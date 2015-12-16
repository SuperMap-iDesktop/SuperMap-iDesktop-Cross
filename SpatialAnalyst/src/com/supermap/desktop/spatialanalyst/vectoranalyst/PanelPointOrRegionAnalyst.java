package com.supermap.desktop.spatialanalyst.vectoranalyst;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.concurrent.CancellationException;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.text.NumberFormatter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

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
import com.supermap.data.SteppedEvent;
import com.supermap.data.SteppedListener;
import com.supermap.data.Unit;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.ui.controls.progress.FormProgress;
import com.supermap.mapping.Selection;
import com.supermap.ui.MapControl;

public class PanelPointOrRegionAnalyst extends JPanel {

	/**
     *
     */
	private static final long serialVersionUID = 1L;
	private JPanel panelBufferRadius;
	private JLabel labelUnit;
	private JRadioButton radioButtonNumeric;
	private JRadioButton radioButtonField;
	private JFormattedTextField textFieldNumeric;
	private JComboBox<Object> comboBoxField;
	private JPanel panelBasic;
	private JPanel panelBasicLeft;
	private JPanel panelBasicRight;
	private PanelBufferData panelBufferData;

	private PanelResultData panelResultData;
	private PanelResultSet panelResultSet;
	private MapControl mapControl;
	private String resultDatasetName;
	private Recordset recordset;
	private Object radius;
	private boolean buttonEnabled = false;
	private boolean bufferSuccess;
	private LocalItemListener localItemListener = new LocalItemListener();
	private DatasetVector sourceDatasetVector;
	private InitComboBoxUnit initComboBoxUnit = new InitComboBoxUnit();
	
	private JComboBox<Unit> comboBoxUnitBox;

	public boolean isButtonEnabled() {
		return buttonEnabled;
	}

	public void setButtonEnabled(boolean buttonEnabled) {
		this.buttonEnabled = buttonEnabled;
	}

	public PanelBufferData getPanelBufferData() {
		return panelBufferData;
	}

	public void setPanelBufferData(PanelBufferData panelBufferData) {
		this.panelBufferData = panelBufferData;
	}

	public PanelPointOrRegionAnalyst() {
		initComponent();
		initResources();
		setPanelPointOrRegionAnalyst();
	}

	private void initComponent() {
		this.panelBasic = new JPanel();
		this.panelBasicLeft = new JPanel();
		this.panelBasicRight = new JPanel();
		this.setLayout(new BorderLayout());
		this.add(this.panelBasic, BorderLayout.CENTER);
		this.panelBufferRadius = new JPanel();
		this.panelBufferData = new PanelBufferData();
		this.panelResultData = new PanelResultData();
		this.panelResultSet = new PanelResultSet();
		initComponentBuffeRadius();
		setPanelBasicLayout();
		setPanelBasicLeftLayout();
		setPanelBasicRightLayout();
	}

	private void initResources() {
		this.panelBufferRadius.setBorder(BorderFactory.createTitledBorder(SpatialAnalystProperties.getString("String_BufferRadius")));
		this.labelUnit.setText(SpatialAnalystProperties.getString("String_BufferRadiusUnit"));
		this.radioButtonNumeric.setText(SpatialAnalystProperties.getString("String_NumericBufferRadius"));
		this.radioButtonField.setText(SpatialAnalystProperties.getString("String_FieldBufferRadius"));
	}

	private void setPanelPointOrRegionAnalyst() {
		setPanelBufferData();
		setPanelBufferRadius();
		setPanelResultSet();
		registerEvent();
	}

	private void initComponentBuffeRadius() {
		this.labelUnit = new JLabel("Unit");
		this.radioButtonNumeric = new JRadioButton("Numeric");
		this.radioButtonField = new JRadioButton("Field");
		this.comboBoxUnitBox = initComboBoxUnit.createComboBoxUnit();
		this.comboBoxUnitBox.setEditable(false);
		this.comboBoxField = new JComboBox<Object>();
		this.comboBoxField.setEditable(false);

		NumberFormatter numberFormatter = new NumberFormatter(NumberFormat.getInstance());
		numberFormatter.setValueClass(Integer.class);
		this.textFieldNumeric = new JFormattedTextField(numberFormatter);
		this.textFieldNumeric.setText("10");

		// 设置按钮组，只能点击一个按钮
		ButtonGroup bufferRadiusButtonGroup = new ButtonGroup();
		bufferRadiusButtonGroup.add(this.radioButtonNumeric);
		bufferRadiusButtonGroup.add(this.radioButtonField);
		this.radioButtonNumeric.setSelected(true);
		this.comboBoxField.setEnabled(false);

		GroupLayout panelBufferRadiusLayout = new GroupLayout(this.panelBufferRadius);
		this.panelBufferRadius.setLayout(panelBufferRadiusLayout);

		//@formatter:off
          panelBufferRadiusLayout.setHorizontalGroup(panelBufferRadiusLayout.createSequentialGroup()
                    .addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.LEADING)
                              .addComponent(this.labelUnit)
                              .addComponent(this.radioButtonNumeric)
                              .addComponent(this.radioButtonField)).addGap(35)
                    .addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.LEADING)
                              .addComponent(this.comboBoxUnitBox)
                              .addComponent(this.textFieldNumeric)
                              .addComponent(this.comboBoxField)));
         
          panelBufferRadiusLayout.setVerticalGroup(panelBufferRadiusLayout.createSequentialGroup()
                    .addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.LEADING)
                              .addComponent(this.labelUnit)
                              .addComponent(this.comboBoxUnitBox)).addGap(8)
                   .addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.LEADING)
                             .addComponent(this.radioButtonNumeric)
                             .addComponent(this.textFieldNumeric)).addGap(8)
                   .addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.LEADING)
                             .addComponent(this.radioButtonField)
                             .addComponent(this.comboBoxField)));
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
                             .addComponent(this.panelBasicRight)).addGap(50));
         //@formatter:on
		panelBasicLayout.linkSize(panelBasicLeft, panelBasicRight);
	}

	private void setPanelBasicLeftLayout() {

		GroupLayout panelBasicLeftLayout = new GroupLayout(this.panelBasicLeft);
		this.panelBasicLeft.setLayout(panelBasicLeftLayout);

		//@formatter:off
          panelBasicLeftLayout.setHorizontalGroup(panelBasicLeftLayout.createSequentialGroup()
                    .addGroup(panelBasicLeftLayout.createParallelGroup(Alignment.LEADING)
                              .addComponent(this.panelBufferData)
                              .addComponent(this.panelResultData)));
          panelBasicLeftLayout.setVerticalGroup(panelBasicLeftLayout.createSequentialGroup()
                    .addComponent(this.panelBufferData)
                    .addComponent(this.panelResultData).addContainerGap());
          //@formatter:on
	}

	private void setPanelBasicRightLayout() {

		GroupLayout panelBasicRightLayout = new GroupLayout(this.panelBasicRight);
		this.panelBasicRight.setLayout(panelBasicRightLayout);

		//@formatter:off
          panelBasicRightLayout.setHorizontalGroup(panelBasicRightLayout.createSequentialGroup()
                    .addGroup(panelBasicRightLayout.createParallelGroup(Alignment.LEADING)
                              .addComponent(this.panelBufferRadius)
                              .addComponent(this.panelResultSet)));
         
          panelBasicRightLayout.setVerticalGroup(panelBasicRightLayout.createSequentialGroup()
                    .addComponent(this.panelBufferRadius)
                    .addComponent(this.panelResultSet).addContainerGap());
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
			if (activeDataset.getType() == DatasetType.POINT || activeDataset.getType() == DatasetType.POINT3D || activeDataset.getType() == DatasetType.REGION
					|| activeDataset.getType() == DatasetType.REGION3D) {
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
					if (selectedDataset.getType() == DatasetType.POINT || selectedDataset.getType() == DatasetType.POINT3D
							|| selectedDataset.getType() == DatasetType.REGION || selectedDataset.getType() == DatasetType.REGION3D) {
						this.panelBufferData.getComboBoxBufferDataDataset().setSelectedDataset(selectedDataset);
					}
				} else {
					initDatasourceAndDataset();
				}
			} else {
				initDatasourceAndDataset();
			}
			this.panelBufferData.getCheckBoxGeometrySelect().setEnabled(hasSelectedGeometryProperty());
			setComponentEnabled();
		}

		// if (this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() == null) {
		// setButtonEnabled(false);
		// } else {
		// setEnabled(true);
		// }
	}

	/**
	 * 设置ComboBoxDataset的类型
	 */
	private void setComboBoxDatasetType() {
		ArrayList<DatasetType> datasetTypes = new ArrayList<DatasetType>();
		datasetTypes.add(DatasetType.POINT);
		datasetTypes.add(DatasetType.REGION);
		datasetTypes.add(DatasetType.POINT3D);
		datasetTypes.add(DatasetType.REGION3D);
		this.panelBufferData.getComboBoxBufferDataDataset().setDatasetTypes(datasetTypes.toArray(new DatasetType[datasetTypes.size()]));
	}

	/**
	 * 设置PanelBufferRadius,初始化comboBoxField，获得缓冲半径的初始化值
	 */
	private void setPanelBufferRadius() {
		if (this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() != null) {
			Dataset comboBoxDataset = this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset();
			createComboBoxField(comboBoxDataset);
			setButtonEnabled(true);
		} else {
			setButtonEnabled(false);
		}
		setComponentEnabled();
		this.radius = Integer.valueOf(this.textFieldNumeric.getText());
	}

	/**
	 * 设置PanelResultSet初始化勾选对象
	 */
	private void setPanelResultSet() {
		this.panelResultSet.getCheckBoxDisplayInMap().setSelected(true);
		this.panelResultSet.getCheckBoxRemainAttributes().setSelected(true);
	}

	private void registerEvent() {
		this.panelBufferData.getComboBoxBufferDataDatasource().addItemListener(new LocalItemListener());
		this.panelBufferData.getComboBoxBufferDataDataset().addItemListener(new LocalItemListener());
		this.panelBufferData.getCheckBoxGeometrySelect().addItemListener(new LocalItemListener());
		this.radioButtonNumeric.addActionListener(new LocalActionListener());
		this.radioButtonField.addActionListener(new LocalActionListener());
		this.textFieldNumeric.addActionListener(new LocalActionListener());
		this.comboBoxField.addActionListener(new LocalActionListener());
		this.panelResultSet.getCheckBoxDisplayInMap().addItemListener(new LocalItemListener());
		this.panelResultSet.getCheckBoxDisplayInScene().addItemListener(new LocalItemListener());
		this.panelResultSet.getCheckBoxRemainAttributes().addItemListener(new LocalItemListener());
		this.panelResultSet.getCheckBoxUnionBuffer().addItemListener(new LocalItemListener());

	}

	// 判断是否选中对象
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
				if (datasetVector.getType() == DatasetType.POINT || datasetVector.getType() == DatasetType.POINT3D
						|| datasetVector.getType() == DatasetType.REGION || datasetVector.getType() == DatasetType.REGION3D) {
					this.panelBufferData.getCheckBoxGeometrySelect().setSelected(true);
					return true;
				}
			}
		}
		this.panelBufferData.getCheckBoxGeometrySelect().setSelected(false);
		return false;
	}

	private void initDatasourceAndDataset() {
		Datasource defaultDatasource = Application.getActiveApplication().getWorkspace().getDatasources().get(0);
		this.panelBufferData.getComboBoxBufferDataDatasource().setSelectedDatasource(defaultDatasource);
		this.panelResultData.getComboBoxResultDataDatasource().setSelectedDatasource(defaultDatasource);
		this.panelBufferData.getComboBoxBufferDataDataset().setDatasets(defaultDatasource.getDatasets());
	}

	private void createComboBoxField(Dataset comboBoxDataset) {
		if (comboBoxDataset instanceof DatasetVector) {
			DatasetVector comboBoxDatasetVector = (DatasetVector) comboBoxDataset;
			for (int i = 0; i < comboBoxDatasetVector.getFieldCount(); i++) {
				FieldInfo fieldInfo = comboBoxDatasetVector.getFieldInfos().get(i);
				if (!fieldInfo.isSystemField()) {
					if (fieldInfo.getType() == FieldType.INT16 || fieldInfo.getType() == FieldType.INT32 || fieldInfo.getType() == FieldType.SINGLE
							|| fieldInfo.getType() == FieldType.DOUBLE)
						this.comboBoxField.addItem(comboBoxDatasetVector.getFieldInfos().get(i).getName());
				}
			}
		}
	}

	private void setComponentEnabled() {
		this.panelBufferData.getComboBoxBufferDataDataset().setEnabled(!this.panelBufferData.getCheckBoxGeometrySelect().isSelected());
		this.panelBufferData.getComboBoxBufferDataDatasource().setEnabled(!this.panelBufferData.getCheckBoxGeometrySelect().isSelected());
		this.comboBoxField.setEnabled(this.radioButtonField.isSelected());
		this.textFieldNumeric.setEnabled(this.radioButtonNumeric.isSelected());
		this.panelResultSet.getCheckBoxRemainAttributes().setEnabled(!this.panelResultSet.getCheckBoxUnionBuffer().isSelected());
	}

	/**
	 * 创建缓冲区分析
	 */
	public void CreateCurrentBuffer() {
		bufferSuccess = false;
		Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources()
				.get(this.panelResultData.getComboBoxResultDataDatasource().getSelectedDatasource().getAlias());
		DatasetVectorInfo resultDatasetVectorInfo = new DatasetVectorInfo();
		this.resultDatasetName = this.panelResultData.getTextFieldResultDataDataset().getText();
		String datasetName = datasource.getDatasets().getAvailableDatasetName(this.resultDatasetName);
		resultDatasetVectorInfo.setName(datasetName);
		resultDatasetVectorInfo.setType(DatasetType.REGION);

		DatasetVector resultDatasetVector = datasource.getDatasets().create(resultDatasetVectorInfo);
		if (this.panelBufferData.getComboBoxBufferDataDataset() != null) {
			sourceDatasetVector = (DatasetVector) this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset();
			BufferAnalystParameter bufferAnalystParameter = new BufferAnalystParameter();

			// radioButtonNumeric被选中，当数据集类型为点对象时，缓冲半径取绝对值
			if (this.radioButtonNumeric.isSelected()) {
				this.radius = Integer.valueOf(this.textFieldNumeric.getText());
				if (sourceDatasetVector.getType() == DatasetType.POINT || sourceDatasetVector.getType() == DatasetType.POINT3D) {
					if (this.radius instanceof Integer) {
						this.radius = Math.abs((Integer) this.radius);
					}
				}
			}
			bufferAnalystParameter.setLeftDistance(this.radius);
			bufferAnalystParameter.setEndType(BufferEndType.ROUND);


			bufferAnalystParameter.setRadiusUnit(initComboBoxUnit.getBufferRadiusUnit(this.comboBoxUnitBox.getSelectedItem().toString()));

			bufferAnalystParameter.setSemicircleLineSegment(Integer.valueOf(this.panelResultSet.getTextFieldSemicircleLineSegment().getText()));

			// 当CheckBoxGeometrySelect()选中时，进行记录集缓冲分析，否则进行数据集缓冲分析
			// if (this.panelBufferData.getCheckBoxGeometrySelect().isSelected()) {
			// bufferSuccess = BufferAnalyst.createBuffer(recordset, resultDatasetVector, bufferAnalystParameter, this.panelResultSet.getCheckBoxUnionBuffer()
			// .isSelected(), this.panelResultSet.getCheckBoxRemainAttributes().isSelected());
			// } else {
			// bufferSuccess = BufferAnalyst.createBuffer(sourceDatasetVector, resultDatasetVector, bufferAnalystParameter, this.panelResultSet
			// .getCheckBoxUnionBuffer().isSelected(), this.panelResultSet.getCheckBoxRemainAttributes().isSelected());
			// }

			// if (bufferSuccess) {
			// // 待实现
			// // 打开生成的缓冲区数据集，并将数据集添加到地图上展现
			// }

			FormProgress formProgress = new FormProgress(SpatialAnalystProperties.getString("String_SingleBufferAnalysis_Capital"));
			formProgress.doWork(new BufferProgressCallable(sourceDatasetVector, resultDatasetVector, bufferAnalystParameter, this.panelResultSet
					.getCheckBoxUnionBuffer().isSelected(), this.panelResultSet.getCheckBoxRemainAttributes().isSelected()));
		}
	}

	class BufferProgressCallable extends UpdateProgressCallable {
		DatasetVector sourceDatasetVector;
		DatasetVector resultDatasetVector;
		BufferAnalystParameter bufferAnalystParameter;
		boolean union;
		boolean isAttributeRetained;

		private SteppedListener steppedListener = new SteppedListener() {

			@Override
			public void stepped(SteppedEvent arg0) {
				try {
					updateProgress(arg0.getPercent(), String.valueOf(arg0.getRemainTime()), arg0.getMessage());
				} catch (CancellationException e) {
					int result = UICommonToolkit.showConfirmDialog(ControlsProperties.getString("String_Warning_DatasetPrjCoordSysTranslatorCancel"));

					if (result == 0) {
						arg0.setCancel(true);
					} else {
						update.setCancel(false);
						arg0.setCancel(false);
					}
				}
			}
		};

		public BufferProgressCallable(DatasetVector sourceDatasetVector, DatasetVector resultDatasetVector, BufferAnalystParameter bufferAnalystParameter,
				boolean union, boolean isAttributeRetained) {
			this.sourceDatasetVector = sourceDatasetVector;
			this.resultDatasetVector = resultDatasetVector;
			this.bufferAnalystParameter = bufferAnalystParameter;
			this.union = union;
			this.isAttributeRetained = isAttributeRetained;
		}

		@Override
		public Boolean call() throws Exception {

			Application.getActiveApplication().getOutput().output(SpatialAnalystProperties.getString("String_BufferCreating"));
			try {
				BufferAnalyst.addSteppedListener(steppedListener);
				bufferSuccess = BufferAnalyst.createBuffer(sourceDatasetVector, resultDatasetVector, bufferAnalystParameter, union, isAttributeRetained);
				if (bufferSuccess) {
					Application.getActiveApplication().getOutput().output(SpatialAnalystProperties.getString("String_BufferCreatedSuccess"));
				} else {
					Application.getActiveApplication().getOutput().output(SpatialAnalystProperties.getString("String_BufferCreatedFailed"));
				}
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(SpatialAnalystProperties.getString("String_BufferCreatedFailed"));
			}
			return bufferSuccess;
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
					comboBoxField.removeAllItems();
				}
				setComponentEnabled();
			} else if (e.getSource() == panelBufferData.getComboBoxBufferDataDataset()) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					comboBoxField.removeAllItems();
					// 如果所选数据集不为空，创建字段ComboBox，否则不做操作
					if (panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() != null) {
						Dataset datasetItem = panelBufferData.getComboBoxBufferDataDatasource().getSelectedDatasource().getDatasets()
								.get(e.getItem().toString());
						createComboBoxField(datasetItem);
						setButtonEnabled(true);
					} else {
						setButtonEnabled(false);
					}
				}
			} else if (e.getSource() == panelBufferData.getCheckBoxGeometrySelect()) {
				setComponentEnabled();
			} else if (e.getSource() == panelResultSet.getCheckBoxUnionBuffer()) {
				setComponentEnabled();
				panelResultSet.getCheckBoxRemainAttributes().setSelected(false);
			}

		}

	}

	class LocalActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == radioButtonField) {
				if (comboBoxField.getSelectedItem() != null) {
					radius = comboBoxField.getSelectedItem().toString();
				}
				setComponentEnabled();
			} else if (e.getSource() == radioButtonNumeric) {
				radius = Integer.valueOf(textFieldNumeric.getText());
				setComponentEnabled();
			} else if (e.getSource() == textFieldNumeric) {
				radius = Integer.valueOf(textFieldNumeric.getText());
			} else if (e.getSource() == comboBoxField) {
				if (comboBoxField.getSelectedItem() != null) {
					radius = comboBoxField.getSelectedItem().toString();
				}
			}
		}
	}
}
