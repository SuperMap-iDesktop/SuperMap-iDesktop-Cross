package com.supermap.desktop.spatialanalyst.vectoranalyst;

import com.supermap.analyst.spatialanalyst.BufferAnalystParameter;
import com.supermap.analyst.spatialanalyst.BufferEndType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.DatasetVectorInfo;
import com.supermap.data.Datasource;
import com.supermap.data.Recordset;
import com.supermap.data.Unit;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.SMFormattedTextField;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.ui.controls.progress.FormProgress;
import com.supermap.mapping.Layer;
import com.supermap.ui.MapControl;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.text.NumberFormatter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.ArrayList;

public class PanelPointOrRegionAnalyst extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panelBufferRadius;
	private JLabel labelUnit;
	private JRadioButton radioButtonNumeric;
	private JRadioButton radioButtonField;
	private SMFormattedTextField textFieldNumeric;
	private JComboBox<Object> comboBoxFieldControl;
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
	private DatasetVector sourceDatasetVector;
	private JComboBox<Unit> comboBoxUnitBox;
	private DatasetVector resultDatasetVector;
	private ComboBoxField comboBoxField;
	private boolean isButtonOKEnabled = true;
	private boolean isButtonEnabled = true;
	private boolean isBufferSucceed;
	private boolean isShowInMap;
	private boolean isRadiusNumSuitable;
	private boolean isDatasourceReadOnly;
	private boolean isTextFieldDatasetNotNull = true;

	private DoSome some;
	private BufferProgressCallable bufferProgressCallable;
	private final static int DEFAULT_MIN = 4;
	private final static int DEFAULT_MAX = 200;
	private final static Object DEFAULT_VALUE = 10;
	private InitComboBoxUnit initComboBoxUnit = new InitComboBoxUnit();
	private LocalItemListener localItemListener = new LocalItemListener();

	public void setSome(DoSome some) {
		this.some = some;
	}

	public boolean isButtonOKEnabled() {
		return isButtonOKEnabled;
	}

	public void setButtonOKEnabled(boolean isButtonOKEnabled) {
		this.isButtonOKEnabled = isButtonOKEnabled;
		if (some != null) {
			some.doSome(isButtonOKEnabled, isButtonEnabled, isRadiusNumSuitable);
		}
	}

	public boolean isButtonEnabled() {
		return isButtonEnabled;
	}

	public void setButtonEnabled(boolean isButtonEnabled) {
		this.isButtonEnabled = isButtonEnabled;
		if (some != null) {
			some.doSome(isButtonOKEnabled, isButtonEnabled, isRadiusNumSuitable);
		}
	}

	public boolean isRadiusNumSuitable() {
		return isRadiusNumSuitable;
	}

	public void setRadiusNumSuitable(boolean isRadiusNumSuitable) {
		this.isRadiusNumSuitable = isRadiusNumSuitable;
		if (some != null) {
			some.doSome(isButtonOKEnabled, isButtonEnabled, isRadiusNumSuitable);
		}
	}

	public boolean isDatasourceReadOnly() {
		return isDatasourceReadOnly;
	}

	public void setDatasourceReadOnly(boolean isDatasourceReadOnly) {
		this.isDatasourceReadOnly = isDatasourceReadOnly;
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
		setPanelResultData();
		registerEvent();
	}

	private void initComponentBuffeRadius() {
		this.labelUnit = new JLabel("Unit");
		this.radioButtonNumeric = new JRadioButton("Numeric");
		this.radioButtonField = new JRadioButton("Field");
		this.comboBoxUnitBox = initComboBoxUnit.createComboBoxUnit();
		this.comboBoxUnitBox.setEditable(false);
		this.comboBoxFieldControl = new JComboBox<Object>();
		this.comboBoxFieldControl.setEditable(false);

		NumberFormatter numberFormatter = new NumberFormatter(NumberFormat.getInstance());
		numberFormatter.setValueClass(Double.class);
		this.textFieldNumeric = new SMFormattedTextField(numberFormatter);
		this.textFieldNumeric.setValue(DEFAULT_VALUE);

		// 设置按钮组，只能点击一个按钮
		ButtonGroup bufferRadiusButtonGroup = new ButtonGroup();
		bufferRadiusButtonGroup.add(this.radioButtonNumeric);
		bufferRadiusButtonGroup.add(this.radioButtonField);
		this.radioButtonNumeric.setSelected(true);
		this.comboBoxFieldControl.setEnabled(false);

		GroupLayout panelBufferRadiusLayout = new GroupLayout(this.panelBufferRadius);
		panelBufferRadiusLayout.setAutoCreateGaps(true);
		panelBufferRadiusLayout.setAutoCreateContainerGaps(true);
		this.panelBufferRadius.setLayout(panelBufferRadiusLayout);

		//@formatter:off
          panelBufferRadiusLayout.setHorizontalGroup(panelBufferRadiusLayout.createSequentialGroup()
                    .addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.CENTER)
                              .addComponent(this.labelUnit)
                              .addComponent(this.radioButtonNumeric)
                              .addComponent(this.radioButtonField))
                    .addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.LEADING)
                              .addComponent(this.comboBoxUnitBox)
                              .addComponent(this.textFieldNumeric)
                              .addComponent(this.comboBoxFieldControl)));
         
          panelBufferRadiusLayout.setVerticalGroup(panelBufferRadiusLayout.createSequentialGroup()
                    .addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.CENTER)
                              .addComponent(this.labelUnit)
                              .addComponent(this.comboBoxUnitBox)).addGap(5)
                   .addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.CENTER)
                             .addComponent(this.radioButtonNumeric)
                             .addComponent(this.textFieldNumeric)).addGap(5)
                   .addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.CENTER)
                             .addComponent(this.radioButtonField)
                             .addComponent(this.comboBoxFieldControl)));
          //@formatter:on

	}

	private void setPanelBasicLayout() {
		GroupLayout panelBasicLayout = new GroupLayout(this.panelBasic);
		this.panelBasic.setLayout(panelBasicLayout);

		//@formatter:off
         panelBasicLayout.setHorizontalGroup(panelBasicLayout.createSequentialGroup()
                   .addComponent(this.panelBasicLeft,0, 180, Short.MAX_VALUE)
                   .addPreferredGap(ComponentPlacement.RELATED)
                   .addComponent(this.panelBasicRight,0, 180, Short.MAX_VALUE));
        
         panelBasicLayout.setVerticalGroup(panelBasicLayout.createSequentialGroup()
                   .addGroup(panelBasicLayout.createParallelGroup(Alignment.CENTER)
                             .addComponent(this.panelBasicLeft)
                             .addComponent(this.panelBasicRight)));
         //@formatter:on
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
                    .addComponent(this.panelBufferData).addGap(10)
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
		setDatasourceAndDataset();
	}

	private void setDatasourceAndDataset() {
		int layersCount;
		setComboBoxDatasetType();
		// 窗体激活，且打开的窗体是地图,如果窗体没有激活，直接获取工作空间树节点，通过树节点数据
		if (Application.getActiveApplication().getActiveForm() != null && Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
			this.mapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
			layersCount = this.mapControl.getMap().getLayers().getCount();
			if (layersCount > 0) {
				for (int i = 0; i < layersCount; i++) {
					Layer[] activeLayer = new Layer[layersCount];
					activeLayer[i] = mapControl.getMap().getLayers().get(i);
					if (activeLayer[i].getDataset().getType() == DatasetType.POINT || activeLayer[i].getDataset().getType() == DatasetType.POINT3D
							|| activeLayer[i].getDataset().getType() == DatasetType.REGION || activeLayer[i].getDataset().getType() == DatasetType.REGION3D) {
						if (activeLayer[i].getSelection() != null && activeLayer[i].getSelection().getCount() != 0) {
							this.panelBufferData.getComboBoxBufferDataDatasource().setSelectedDatasource(activeLayer[i].getDataset().getDatasource());
							this.panelBufferData.getComboBoxBufferDataDataset().setDatasets(activeLayer[i].getDataset().getDatasource().getDatasets());
							this.panelBufferData.getComboBoxBufferDataDataset().setSelectedDataset(activeLayer[i].getDataset());
							recordset = activeLayer[i].getSelection().toRecordset();
							this.panelBufferData.getCheckBoxGeometrySelect().setEnabled(true);
							this.panelBufferData.getCheckBoxGeometrySelect().setSelected(true);
							setComponentEnabled();
							return;
						} else {
							setWorkspaceTreeNode();
						}
					} else {
						setWorkspaceTreeNode();
					}
				}
			} else {
				setWorkspaceTreeNode();
			}
		} else {
			setWorkspaceTreeNode();
		}
	}

	private void setWorkspaceTreeNode() {
		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		TreePath selectedPath = workspaceTree.getSelectionPath();
		if (selectedPath != null && selectedPath.getLastPathComponent() instanceof DefaultMutableTreeNode) {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
			TreeNodeData nodeData = (TreeNodeData) selectedNode.getUserObject();
			if (nodeData.getData() instanceof Datasource) {
				Datasource selectedDatasource = (Datasource) nodeData.getData();
				this.panelBufferData.getComboBoxBufferDataDatasource().setSelectedDatasource(selectedDatasource);
				this.panelBufferData.getComboBoxBufferDataDataset().setDatasets(selectedDatasource.getDatasets());
				if (this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() == null) {
					setButtonEnabled(false);
				}
			} else if (nodeData.getData() instanceof Dataset) {
				Dataset selectedDataset = (Dataset) nodeData.getData();
				this.panelBufferData.getComboBoxBufferDataDatasource().setSelectedDatasource(selectedDataset.getDatasource());
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
		this.panelBufferData.getCheckBoxGeometrySelect().setEnabled(false);
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
			this.comboBoxField = new ComboBoxField(comboBoxDataset, comboBoxFieldControl);
			this.comboBoxField.createComboBoxField(comboBoxDataset, comboBoxFieldControl);
		}
		setComponentEnabled();
		this.radius = Double.parseDouble(this.textFieldNumeric.getValue().toString());
	}

	private void setPanelResultData() {
		this.panelResultData.getComboBoxResultDataDatasource().setSelectedDatasource(
				this.panelBufferData.getComboBoxBufferDataDatasource().getSelectedDatasource());

	}

	private void registerEvent() {
		this.panelBufferData.getComboBoxBufferDataDatasource().addItemListener(new LocalItemListener());
		this.panelBufferData.getComboBoxBufferDataDataset().addItemListener(new LocalItemListener());
		this.panelBufferData.getCheckBoxGeometrySelect().addItemListener(new LocalItemListener());
		this.radioButtonNumeric.addActionListener(new LocalActionListener());
		this.radioButtonField.addActionListener(new LocalActionListener());
		this.textFieldNumeric.addActionListener(new LocalActionListener());
		this.comboBoxFieldControl.addActionListener(new LocalActionListener());
		this.panelResultSet.getCheckBoxDisplayInMap().addItemListener(new LocalItemListener());
		this.panelResultSet.getCheckBoxDisplayInScene().addItemListener(new LocalItemListener());
		this.panelResultSet.getCheckBoxRemainAttributes().addItemListener(new LocalItemListener());
		this.panelResultSet.getCheckBoxUnionBuffer().addItemListener(new LocalItemListener());
	}

	private void initDatasourceAndDataset() {
		Datasource defaultDatasource = Application.getActiveApplication().getWorkspace().getDatasources().get(0);
		this.panelBufferData.getComboBoxBufferDataDatasource().setSelectedDatasource(defaultDatasource);
		this.panelResultData.getComboBoxResultDataDatasource().setSelectedDatasource(defaultDatasource);
		this.panelBufferData.getComboBoxBufferDataDataset().setDatasets(defaultDatasource.getDatasets());
	}

	private void setComponentEnabled() {
		this.panelBufferData.getComboBoxBufferDataDataset().setEnabled(!this.panelBufferData.getCheckBoxGeometrySelect().isSelected());
		this.panelBufferData.getComboBoxBufferDataDatasource().setEnabled(!this.panelBufferData.getCheckBoxGeometrySelect().isSelected());
		this.comboBoxFieldControl.setEnabled(this.radioButtonField.isSelected());
		this.textFieldNumeric.setEnabled(this.radioButtonNumeric.isSelected());
		this.panelResultSet.getCheckBoxRemainAttributes().setEnabled(!this.panelResultSet.getCheckBoxUnionBuffer().isSelected());
	}

	/**
	 * 创建缓冲区分析
	 */
	public boolean createCurrentBuffer() {
		isBufferSucceed = false;
		if (this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() != null) {
			sourceDatasetVector = (DatasetVector) this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset();
			BufferAnalystParameter bufferAnalystParameter = new BufferAnalystParameter();

			// 创建缓冲数据集
			if (sourceDatasetVector.getRecordCount() > 0) {
				createResultDataset();
			}

			// radioButtonNumeric被选中，当数据集类型为点对象时，缓冲半径取绝对值

			if (this.radioButtonNumeric.isSelected()) {
				this.radius = Double.parseDouble(this.textFieldNumeric.getValue().toString());
				if (sourceDatasetVector.getType() == DatasetType.POINT || sourceDatasetVector.getType() == DatasetType.POINT3D) {
					this.radius = Math.abs((Double) this.radius);
				}
			}

			if (this.panelResultSet.getCheckBoxDisplayInMap().isSelected()) {
				isShowInMap = true;
			} else {
				isShowInMap = false;
			}

			// 设置缓冲区参数
			bufferAnalystParameter.setLeftDistance(this.radius);
			bufferAnalystParameter.setEndType(BufferEndType.ROUND);
			bufferAnalystParameter.setRadiusUnit(initComboBoxUnit.getBufferRadiusUnit((Unit) this.comboBoxUnitBox.getSelectedItem()));
			bufferAnalystParameter.setSemicircleLineSegment(Integer.valueOf(this.panelResultSet.getTextFieldSemicircleLineSegment().getText()));

			// 当CheckBoxGeometrySelect()选中时，进行记录集缓冲分析，否则进行数据集缓冲分析
			FormProgress formProgress = new FormProgress(SpatialAnalystProperties.getString("String_SingleBufferAnalysis_Capital"));

			if (this.panelBufferData.getCheckBoxGeometrySelect().isSelected()) {
				bufferProgressCallable = new BufferProgressCallable(recordset, resultDatasetVector, bufferAnalystParameter, this.panelResultSet
						.getCheckBoxUnionBuffer().isSelected(), this.panelResultSet.getCheckBoxRemainAttributes().isSelected(), isShowInMap);
			} else {
				bufferProgressCallable = new BufferProgressCallable(sourceDatasetVector, resultDatasetVector, bufferAnalystParameter, this.panelResultSet
						.getCheckBoxUnionBuffer().isSelected(), this.panelResultSet.getCheckBoxRemainAttributes().isSelected(), isShowInMap);
			}
			if (formProgress != null) {
				formProgress.doWork(bufferProgressCallable);
			}
			isBufferSucceed = bufferProgressCallable.isSucceed();
		}
		return isBufferSucceed;
	}

	private void createResultDataset() {
		Datasource datasource = this.panelResultData.getComboBoxResultDataDatasource().getSelectedDatasource();
		DatasetVectorInfo resultDatasetVectorInfo = new DatasetVectorInfo();
		this.resultDatasetName = this.panelResultData.getTextFieldResultDataDataset().getText();
		String datasetName = datasource.getDatasets().getAvailableDatasetName(this.resultDatasetName);
		resultDatasetVectorInfo.setName(datasetName);
		resultDatasetVectorInfo.setType(DatasetType.REGION);
		resultDatasetVector = datasource.getDatasets().create(resultDatasetVectorInfo);
		resultDatasetVector.setPrjCoordSys(sourceDatasetVector.getPrjCoordSys());
	}

	public void addListener() {
		this.panelResultSet.getTextFieldSemicircleLineSegment().addPropertyChangeListener(ControlDefaultValues.PROPERTYNAME_VALUE,
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						getButtonOkEnabled();
					}
				});
		this.textFieldNumeric.addPropertyChangeListener(ControlDefaultValues.PROPERTYNAME_VALUE, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				judgeRadiusNum();
			}
		});

		this.panelBufferData.getComboBoxBufferDataDataset().addItemListener(localItemListener);
		this.panelBufferData.getComboBoxBufferDataDatasource().addItemListener(localItemListener);

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
					comboBoxFieldControl.removeAllItems();
				}
				setComponentEnabled();
			} else if (e.getSource() == panelBufferData.getComboBoxBufferDataDataset()) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					comboBoxFieldControl.removeAllItems();
					// 如果所选数据集不为空，创建字段ComboBox，否则不做操作
					if (panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() != null) {
						Dataset datasetItem = panelBufferData.getComboBoxBufferDataDatasource().getSelectedDatasource().getDatasets()
								.get(e.getItem().toString());
						comboBoxField = new ComboBoxField(datasetItem, comboBoxFieldControl);
						comboBoxField.createComboBoxField(datasetItem, comboBoxFieldControl);
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
				if (comboBoxFieldControl.getSelectedItem() != null) {
					radius = comboBoxFieldControl.getSelectedItem().toString();
				}
				setComponentEnabled();
			} else if (e.getSource() == radioButtonNumeric) {
				radius = Double.parseDouble(textFieldNumeric.getValue().toString());
				setComponentEnabled();
			} else if (e.getSource() == textFieldNumeric) {
				radius = Double.parseDouble(textFieldNumeric.getValue().toString());
			} else if (e.getSource() == comboBoxFieldControl) {
				if (comboBoxFieldControl.getSelectedItem() != null) {
					radius = comboBoxFieldControl.getSelectedItem().toString();
				}
			}
		}
	}

	private void getButtonOkEnabled() {
		try {
			long value = Long.parseLong(panelResultSet.getTextFieldSemicircleLineSegment().getValue().toString());
			if (value < DEFAULT_MIN || value > DEFAULT_MAX) {
				setButtonOKEnabled(false);
			} else {
				setButtonOKEnabled(true);
			}
		} catch (Exception e) {
			setButtonOKEnabled(false);
		}
	}

	private void judgeRadiusNum() {

		double num = Double.parseDouble(textFieldNumeric.getValue().toString());

		if (num <= 0) {
			setRadiusNumSuitable(false);
		} else {
			setRadiusNumSuitable(true);
		}
	}
}
