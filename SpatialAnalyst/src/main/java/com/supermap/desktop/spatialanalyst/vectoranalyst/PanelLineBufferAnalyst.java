package com.supermap.desktop.spatialanalyst.vectoranalyst;

import com.supermap.analyst.spatialanalyst.BufferAnalystParameter;
import com.supermap.analyst.spatialanalyst.BufferEndType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.DatasetVectorInfo;
import com.supermap.data.Datasource;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.ui.controls.comboBox.ComboBoxLengthUnit;
import com.supermap.desktop.ui.controls.comboBox.SmNumericFieldComboBox;
import com.supermap.desktop.ui.controls.progress.FormProgress;
import com.supermap.desktop.utilities.DoubleUtilities;
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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class PanelLineBufferAnalyst extends JPanel {

	/**
	 * 对生成缓冲区.线数据类型.缓冲半径面板进行重构-yuanR 2017.2.23
	 */
	private static final long serialVersionUID = 1L;


	private JPanel panelBasic;
	private JPanel panelBufferRadius;
	private JPanel panelBufferType;
	private JPanel panelBasicLeft;
	private JPanel panelBasicRight;
	private JLabel labelUnit;
	private ComboBoxLengthUnit comboBoxUnit;
	private JLabel labelLeftNumericFieldRadius;
	private JLabel labelRightNumericFieldRadius;
	private SmNumericFieldComboBox numericFieldComboBoxLeft;
	private SmNumericFieldComboBox numericFieldComboBoxRight;
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
	private String resultDatasetName;
	private boolean isArcSegmentSuitable = true;
	private boolean isComboBoxDatasetNotNull = true;
	private boolean isShowInMap = true;
	private boolean isRadiusNumSuitable = true;
	private boolean isHasResultDatasource = true;
	private DoSome some;
	private DatasetVector resultDatasetVector;
	private BufferProgressCallable bufferProgressCallable;
	private boolean isBufferSucceed;
	private final static int DEFAULT_MIN = 4;
	private final static int DEFAULT_MAX = 200;
	private InitComboBoxUnit initComboBoxUnit = new InitComboBoxUnit();
	private LocalKeylistener localKeylistener = new LocalKeylistener();
	private LocalItemListener localItemListener = new LocalItemListener();

	public void setSome(DoSome some) {
		this.some = some;
	}

	public boolean isArcSegmentSuitable() {
		return isArcSegmentSuitable;
	}

	public void setArcSegmentSuitable(boolean isArcSegmentSuitable) {
		this.isArcSegmentSuitable = isArcSegmentSuitable;
		if (some != null) {
			some.doSome(isArcSegmentSuitable, isComboBoxDatasetNotNull, isRadiusNumSuitable, isHasResultDatasource);
		}
	}

	public boolean isComboBoxDatasetNotNull() {
		return isComboBoxDatasetNotNull;
	}

	public void setComboBoxDatasetNotNull(boolean isComboBoxDatasetNotNull) {
		this.isComboBoxDatasetNotNull = isComboBoxDatasetNotNull;
		if (some != null) {
			some.doSome(isArcSegmentSuitable, isComboBoxDatasetNotNull, isRadiusNumSuitable, isHasResultDatasource);
		}
	}

	public boolean isRadiusNumSuitable() {
		return isRadiusNumSuitable;
	}

	public void setRadiusNumSuitable(boolean isRadiusNumSuitable) {
		this.isRadiusNumSuitable = isRadiusNumSuitable;
		if (some != null) {
			some.doSome(isArcSegmentSuitable, isComboBoxDatasetNotNull, isRadiusNumSuitable, isHasResultDatasource);
		}
	}

	public boolean isHasResultDatasource() {
		return isHasResultDatasource;
	}

	public void setHasResultDatasource(boolean isHasResultDatasource) {
		this.isHasResultDatasource = isHasResultDatasource;
		if (some != null) {
			some.doSome(isArcSegmentSuitable, isComboBoxDatasetNotNull, isRadiusNumSuitable, isHasResultDatasource);
		}
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
		this.panelResultSet.setOtherPanelResultSetLayout();
		this.setLayout(new BorderLayout());
		this.add(this.panelBasic, BorderLayout.CENTER);

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
		this.labelLeftNumericFieldRadius.setText(SpatialAnalystProperties.getString("String_Label_LeftBufferRadius"));
		this.labelRightNumericFieldRadius.setText(SpatialAnalystProperties.getString("String_Label_RightBufferRadius"));
		this.radioButtonBufferTypeRound.setText(SpatialAnalystProperties.getString("String_BufferTypeRound"));
		this.radioButtonBufferTypeFlat.setText(SpatialAnalystProperties.getString("String_BufferTypeFlat"));
		this.checkBoxBufferLeft.setText(SpatialAnalystProperties.getString("String_BufferTypeLeft"));
		this.checkBoxBufferRight.setText(SpatialAnalystProperties.getString("String_BufferTypeRight"));
	}

	private void setPanelLineBufferAnalyst() {
		setPanelBufferData();
		setPanelBufferType();
		setPanelBuffeRadius();
		setPanelResultData();
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
		panelBufferTypeLayout.setAutoCreateGaps(true);
		panelBufferTypeLayout.setAutoCreateContainerGaps(true);
		this.panelBufferType.setLayout(panelBufferTypeLayout);

		//@formatter:off
		panelBufferTypeLayout.setVerticalGroup(panelBufferTypeLayout.createSequentialGroup()
				.addGroup(panelBufferTypeLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.radioButtonBufferTypeRound)
						.addComponent(this.radioButtonBufferTypeFlat)
						.addComponent(this.checkBoxBufferLeft)
						.addComponent(this.checkBoxBufferRight)));
		panelBufferTypeLayout.setHorizontalGroup(panelBufferTypeLayout.createSequentialGroup()
				.addGroup(panelBufferTypeLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.radioButtonBufferTypeRound))
				.addGroup(panelBufferTypeLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.radioButtonBufferTypeFlat))
				.addGroup(panelBufferTypeLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.checkBoxBufferLeft))
				.addGroup(panelBufferTypeLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.checkBoxBufferRight)));
		//@formatter:on

	}

	private void initComponentBufferRadius() {
		this.labelUnit = new JLabel("Unit");
		this.labelLeftNumericFieldRadius = new JLabel("LeftNumericFieldRadius");
		this.labelRightNumericFieldRadius = new JLabel("RightNumericFieldRadius");
		this.comboBoxUnit = new ComboBoxLengthUnit();

		NumberFormatter numberFormatter = new NumberFormatter();
		numberFormatter.setValueClass(Double.class);

		this.numericFieldComboBoxLeft = new SmNumericFieldComboBox();
		this.numericFieldComboBoxRight = new SmNumericFieldComboBox();

		//@formatter:off
		GroupLayout panelBufferRadiusLayout = new GroupLayout(this.panelBufferRadius);
		panelBufferRadiusLayout.setAutoCreateContainerGaps(true);
		panelBufferRadiusLayout.setAutoCreateGaps(true);
		this.panelBufferRadius.setLayout(panelBufferRadiusLayout);
		
		panelBufferRadiusLayout.setHorizontalGroup(panelBufferRadiusLayout.createSequentialGroup()
				.addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelUnit)
						.addComponent(this.labelLeftNumericFieldRadius)
						.addComponent(this.labelRightNumericFieldRadius))

				.addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.comboBoxUnit)
						.addComponent(this.numericFieldComboBoxLeft)
						.addComponent(this.numericFieldComboBoxRight)));
		panelBufferRadiusLayout.setVerticalGroup(panelBufferRadiusLayout.createSequentialGroup()
				.addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelUnit)
						.addComponent(this.comboBoxUnit,20,20,20))
				.addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelLeftNumericFieldRadius)
						.addComponent(this.numericFieldComboBoxLeft,20,20,20))
				.addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelRightNumericFieldRadius)
						.addComponent(this.numericFieldComboBoxRight,20,20,20)));
		//@formatter:on

	}

	private void setPanelBasicLayout() {
		GroupLayout panelBasicLayout = new GroupLayout(this.panelBasic);
		this.panelBasic.setLayout(panelBasicLayout);

		//@formatter:off
	    panelBasicLayout.setHorizontalGroup(panelBasicLayout.createSequentialGroup()
	    		.addComponent(this.panelBasicLeft,0,200, Short.MAX_VALUE)
	    		.addPreferredGap(ComponentPlacement.RELATED)
	    		.addComponent(this.panelBasicRight,0,200, Short.MAX_VALUE));
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
						.addComponent(this.panelResultSet)));
		panelBasicLeftLayout.setVerticalGroup(panelBasicLeftLayout.createSequentialGroup()
				.addComponent(this.panelBufferData)
				.addComponent(this.panelResultSet));
		//@formatter:on

	}

	private void setPanelBasicRightLayout() {
		GroupLayout panelBasicRightLayout = new GroupLayout(this.panelBasicRight);
		this.panelBasicRight.setLayout(panelBasicRightLayout);

		//@formatter:off
		panelBasicRightLayout.setHorizontalGroup(panelBasicRightLayout.createSequentialGroup()
				.addGroup(panelBasicRightLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.panelBufferType )
						.addComponent(this.panelBufferRadius )
						.addComponent(this.panelResultData )));
		panelBasicRightLayout.setVerticalGroup(panelBasicRightLayout.createSequentialGroup()
				.addComponent(this.panelBufferType )
				.addComponent(this.panelBufferRadius )
				.addComponent(this.panelResultData ));
		//@formatter:on

	}

	/**
	 * 当窗体界面打开时，且打开的窗体是地图时，如果数据集不是线或者网络数据集，设置选中数据集的数据源的第一个线或者网络数据集，否则设置数据集为选中地图的第一个数据集 如果窗体没有打开，获取工作空间树选中节点,得到选中的数据集，数据源
	 */
	private void setPanelBufferData() {
		setComboBoxDatasetType();
		int layersCount;
		if (Application.getActiveApplication().getActiveForm() != null && Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
			this.mapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
			layersCount = this.mapControl.getMap().getLayers().getCount();
			if (layersCount > 0) {
				for (int i = 0; i < layersCount; i++) {
					Layer[] activeLayer = new Layer[layersCount];
					activeLayer[i] = mapControl.getMap().getLayers().get(i);
					if (activeLayer[i].getDataset().getType() == DatasetType.LINE || activeLayer[i].getDataset().getType() == DatasetType.NETWORK) {
						if (activeLayer[i].getSelection() != null && activeLayer[i].getSelection().getCount() != 0) {
							this.panelBufferData.getComboBoxBufferDataDatasource().setSelectedDatasource(activeLayer[i].getDataset().getDatasource());
							this.panelBufferData.getComboBoxBufferDataDataset().setDatasets(activeLayer[i].getDataset().getDatasource().getDatasets());
							this.panelBufferData.getComboBoxBufferDataDataset().setSelectedDataset(activeLayer[i].getDataset());
							recordset = activeLayer[i].getSelection().toRecordset();
							this.panelBufferData.getCheckBoxGeometrySelect().setEnabled(true);
							this.panelBufferData.getCheckBoxGeometrySelect().setSelected(true);
							setComponentEnabled();
						} else {
							setWorkspaceTreeNode();
						}
						return;
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
					setComboBoxDatasetNotNull(false);
				}
			} else if (nodeData.getData() instanceof Dataset) {
				Dataset selectedDataset = (Dataset) nodeData.getData();
				this.panelBufferData.getComboBoxBufferDataDatasource().setSelectedDatasource(selectedDataset.getDatasource());
				this.panelBufferData.getComboBoxBufferDataDataset().setDatasets(selectedDataset.getDatasource().getDatasets());
				if (selectedDataset.getType() == DatasetType.LINE || selectedDataset.getType() == DatasetType.NETWORK) {
					this.panelBufferData.getComboBoxBufferDataDataset().setSelectedDataset(selectedDataset);
				}
			} else {
				initDatasourceAndDataSet();
			}
		} else {
			initDatasourceAndDataSet();
		}
		this.panelBufferData.getCheckBoxGeometrySelect().setEnabled(false);

	}

	private void setPanelBufferType() {
		this.radioButtonBufferTypeRound.setSelected(true);
		this.checkBoxBufferLeft.setSelected(true);
		this.checkBoxBufferRight.setSelected(true);
		setComponentEnabled();
	}

	private void setPanelBuffeRadius() {
		if (this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() != null) {
			DatasetVector selectedDataset = ((DatasetVector) this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset());
			this.numericFieldComboBoxLeft.setDataset(selectedDataset);
			this.numericFieldComboBoxRight.setDataset(selectedDataset);
		}
		setComponentEnabled();
	}

	private void setPanelResultData() {
		this.panelResultData.getComboBoxResultDataDatasource().setSelectedDatasource(
				this.panelBufferData.getComboBoxBufferDataDatasource().getSelectedDatasource());
		if (this.panelResultData.getComboBoxResultDataDatasource().getSelectedDatasource() != null) {
			setHasResultDatasource(true);
		} else {
			setHasResultDatasource(false);
		}

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
		this.numericFieldComboBoxLeft.addItemListener(localItemListener);
		this.numericFieldComboBoxRight.addItemListener(localItemListener);
		this.numericFieldComboBoxLeft.addKeyListener(localKeylistener);
		this.numericFieldComboBoxRight.addKeyListener(localKeylistener);
	}

	private void setComboBoxDatasetType() {
		// 暂不支持三维
		ArrayList<DatasetType> datasetTypes = new ArrayList<DatasetType>();
		datasetTypes.add(DatasetType.LINE);
		// datasetTypes.add(DatasetType.LINE3D);
		datasetTypes.add(DatasetType.NETWORK);
		// datasetTypes.add(DatasetType.NETWORK3D);
		this.panelBufferData.getComboBoxBufferDataDataset().setSupportedDatasetTypes(datasetTypes.toArray(new DatasetType[datasetTypes.size()]));
	}

	private void initDatasourceAndDataSet() {
		Datasource defaultDatasource = Application.getActiveApplication().getWorkspace().getDatasources().get(0);
		this.panelBufferData.getComboBoxBufferDataDatasource().setSelectedDatasource(defaultDatasource);
		this.panelBufferData.getComboBoxBufferDataDataset().setDatasets(defaultDatasource.getDatasets());
	}

	private void setComponentEnabled() {
		this.panelBufferData.getComboBoxBufferDataDataset().setEnabled(!this.panelBufferData.getCheckBoxGeometrySelect().isSelected());
		this.panelBufferData.getComboBoxBufferDataDatasource().setEnabled(!this.panelBufferData.getCheckBoxGeometrySelect().isSelected());
		this.numericFieldComboBoxLeft.setEnabled(this.checkBoxBufferLeft.isSelected());
		this.numericFieldComboBoxRight.setEnabled(this.checkBoxBufferRight.isSelected());
		this.panelResultSet.getCheckBoxRemainAttributes().setEnabled(!this.panelResultSet.getCheckBoxUnionBuffer().isSelected());
		this.checkBoxBufferLeft.setEnabled(!this.radioButtonBufferTypeRound.isSelected());
		this.checkBoxBufferRight.setEnabled(!this.radioButtonBufferTypeRound.isSelected());
	}

	public void addListener() {
		this.panelResultSet.getTextFieldSemicircleLineSegment().addPropertyChangeListener(ControlDefaultValues.PROPERTYNAME_VALUE,
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						judgeArcSegmentNum();
					}
				});

//		this.textFieldNumericLeft.addPropertyChangeListener(ControlDefaultValues.PROPERTYNAME_VALUE, new PropertyChangeListener() {
//			@Override
//			public void propertyChange(PropertyChangeEvent evt) {
//				judgeRadiusNum();
//			}
//		});
//		this.textFieldNumericRight.addPropertyChangeListener(ControlDefaultValues.PROPERTYNAME_VALUE, new PropertyChangeListener() {
//			@Override
//			public void propertyChange(PropertyChangeEvent evt) {
//				judgeRadiusNum();
//			}
//		});

		this.panelBufferData.getComboBoxBufferDataDataset().addItemListener(localItemListener);
		this.panelBufferData.getComboBoxBufferDataDatasource().addItemListener(localItemListener);
	}

	/**
	 * 创建缓冲区分析
	 */
	public boolean CreateCurrentBuffer() {
		isBufferSucceed = false;
		if (this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() != null) {
			DatasetVector sourceDatasetVector = (DatasetVector) this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset();
			BufferAnalystParameter bufferAnalystParameter = new BufferAnalystParameter();

			// 创建缓冲区数据集
			if (sourceDatasetVector.getRecordCount() > 0) {
				createResultDataset(sourceDatasetVector);
			}

			this.radiusLeft = numericFieldComboBoxLeft.getSelectedItem().toString();
			this.radiusRight = numericFieldComboBoxRight.getSelectedItem().toString();

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

			if (this.panelResultSet.getCheckBoxDisplayInMap().isSelected()) {
				isShowInMap = true;
			} else {
				isShowInMap = false;
			}

			bufferAnalystParameter.setRadiusUnit(this.comboBoxUnit.getUnit());
			bufferAnalystParameter.setSemicircleLineSegment(Integer.parseInt(this.panelResultSet.getTextFieldSemicircleLineSegment().getText()));

			FormProgress formProgress = new FormProgress();
			// 当CheckBoxGeometrySelect()选中时，进行记录集缓冲分析，否则进行数据集缓冲分析

			if (this.panelBufferData.getCheckBoxGeometrySelect().isSelected()) {
				bufferProgressCallable = new BufferProgressCallable(recordset, resultDatasetVector, bufferAnalystParameter, this.panelResultSet
						.getCheckBoxUnionBuffer().isSelected(), this.panelResultSet.getCheckBoxRemainAttributes().isSelected(), isShowInMap);
			} else {
				bufferProgressCallable = new BufferProgressCallable(sourceDatasetVector, resultDatasetVector, bufferAnalystParameter, this.panelResultSet
						.getCheckBoxUnionBuffer().isSelected(), this.panelResultSet.getCheckBoxRemainAttributes().isSelected(), isShowInMap);
			}
			if (formProgress != null) {
				formProgress.doWork(bufferProgressCallable);
				isBufferSucceed = bufferProgressCallable.isSucceed();
			}
		}
		return isBufferSucceed;
	}

	private void createResultDataset(DatasetVector sourceDatasetVector) {
		Datasource datasource = this.panelResultData.getComboBoxResultDataDatasource().getSelectedDatasource();
		DatasetVectorInfo resultDatasetVectorInfo = new DatasetVectorInfo();
		this.resultDatasetName = this.panelResultData.getTextFieldResultDataDataset().getText();
		resultDatasetVectorInfo.setName(this.resultDatasetName);
		resultDatasetVectorInfo.setType(DatasetType.REGION);
		resultDatasetVector = datasource.getDatasets().create(resultDatasetVectorInfo);
		resultDatasetVector.setPrjCoordSys(sourceDatasetVector.getPrjCoordSys());
	}

	class LocalItemListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {

			if (e.getSource() == panelBufferData.getComboBoxBufferDataDatasource()) {
				panelBufferData.getComboBoxBufferDataDataset().removeAllItems();
				Datasource datasource = (Datasource) e.getItem();
				if (e.getStateChange() == ItemEvent.SELECTED) {
					panelBufferData.getComboBoxBufferDataDataset().setDatasets(datasource.getDatasets());
					if (panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() == null) {
						// 切换comboBoxDatasource时，如果comboBoxDataset为空时将字段选项置灰，默认选中数值型
						numericFieldComboBoxLeft.removeAllItems();
						numericFieldComboBoxRight.removeAllItems();
						setComboBoxDatasetNotNull(false);
					} else {
						numericFieldComboBoxLeft.setDataset((DatasetVector) panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset());
						numericFieldComboBoxRight.setDataset((DatasetVector) panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset());
						setComboBoxDatasetNotNull(true);
					}
				}
				// 切换数据源后，如果ComboBoxDataset为空时，清除字段选项

				setComponentEnabled();
			} else if (e.getSource() == panelBufferData.getComboBoxBufferDataDataset()) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					numericFieldComboBoxLeft.removeAllItems();
					numericFieldComboBoxRight.removeAllItems();
					// 如果所选数据集不为空，创建字段ComboBox，否则不做操作
					if (panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() != null) {
						DatasetVector datasetItem = (DatasetVector) panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset();
						numericFieldComboBoxLeft.setDataset(datasetItem);
						numericFieldComboBoxRight.setDataset(datasetItem);
						setComboBoxDatasetNotNull(true);
					} else {
						setComboBoxDatasetNotNull(false);
					}
				}

			} else if (e.getSource() == numericFieldComboBoxLeft) {
				if (numericFieldComboBoxLeft.getSelectedItem() != null && (numericFieldComboBoxLeft.getSelectedIndex() + 1) != numericFieldComboBoxLeft.getItemCount()) {
					numericFieldComboBoxRight.removeItemListener(localItemListener);
					numericFieldComboBoxRight.setSelectedItem(numericFieldComboBoxLeft.getSelectedItem());
					numericFieldComboBoxRight.addItemListener(localItemListener);
					radiusLeft = numericFieldComboBoxLeft.getSelectedItem().toString();
				}

			} else if (e.getSource() == numericFieldComboBoxRight && (numericFieldComboBoxLeft.getSelectedIndex() + 1) != numericFieldComboBoxLeft.getItemCount()) {
				if (numericFieldComboBoxRight.getSelectedItem() != null) {
					numericFieldComboBoxLeft.removeItemListener(localItemListener);
					numericFieldComboBoxLeft.setSelectedItem(numericFieldComboBoxRight.getSelectedItem());
					numericFieldComboBoxLeft.addItemListener(localItemListener);
					radiusRight = numericFieldComboBoxRight.getSelectedItem().toString();
				}
			} else if (e.getSource() == panelBufferData.getCheckBoxGeometrySelect()) {
				setComponentEnabled();
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
				numericFieldComboBoxRight.setSelectedItem(numericFieldComboBoxLeft.getSelectedItem());
				numericFieldComboBoxLeft.addKeyListener(localKeylistener);
				numericFieldComboBoxRight.addKeyListener(localKeylistener);
				numericFieldComboBoxLeft.addItemListener(localItemListener);
				numericFieldComboBoxRight.addItemListener(localItemListener);
				if (numericFieldComboBoxLeft.getSelectedItem() != null) {
					numericFieldComboBoxLeft.setSelectedIndex(0);
					numericFieldComboBoxRight.setSelectedIndex(0);
				}
				checkBoxBufferLeft.setSelected(true);
				checkBoxBufferRight.setSelected(true);
				setComponentEnabled();
			} else if (e.getSource() == radioButtonBufferTypeFlat) {
				// 当点击平台缓冲时，左右半径可以不同，移除监听设置不同步
				numericFieldComboBoxLeft.removeKeyListener(localKeylistener);
				numericFieldComboBoxRight.removeKeyListener(localKeylistener);
				numericFieldComboBoxLeft.removeItemListener(localItemListener);
				numericFieldComboBoxRight.removeItemListener(localItemListener);
				setComponentEnabled();
			}
		}
	}

	class LocalKeylistener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyReleased(KeyEvent e) {
			String text = ((JTextField) e.getSource()).getText();
			Double value = DoubleUtilities.stringToValue(text);
			if (value == null) {
				return;
			}
			if (e.getSource() == numericFieldComboBoxRight) {
				numericFieldComboBoxLeft.setSelectedItem(DoubleUtilities.getFormatString(value));
			} else {
				numericFieldComboBoxRight.setSelectedItem(DoubleUtilities.getFormatString(value));
			}
//			updateUI();
		}
	}

	private void judgeArcSegmentNum() {
		try {
			long value = Long.parseLong(panelResultSet.getTextFieldSemicircleLineSegment().getValue().toString());
			if (value < DEFAULT_MIN || value > DEFAULT_MAX) {
				setArcSegmentSuitable(false);
			} else {
				setArcSegmentSuitable(true);
			}
		} catch (Exception e) {
			setArcSegmentSuitable(false);
		}

	}

	private void judgeRadiusNum() {
//		double leftNum = Double.parseDouble(textFieldNumericLeft.getValue().toString());
//		double rightNum = Double.parseDouble(textFieldNumericRight.getValue().toString());
//		if (leftNum <= 0 || rightNum <= 0) {
//			setRadiusNumSuitable(false);
//		} else {
//			setRadiusNumSuitable(true);
//		}
	}
}
