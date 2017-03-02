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
import com.supermap.desktop.ui.controls.borderPanel.PanelBufferRadius;
import com.supermap.desktop.ui.controls.progress.FormProgress;
import com.supermap.mapping.Layer;
import com.supermap.ui.MapControl;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class PanelPointOrRegionAnalyst extends JPanel {

	private static final long serialVersionUID = 1L;

	private JPanel panelBasic;
	private JPanel panelBasicLeft;
	private JPanel panelBasicRight;
	private PanelBufferRadius panelBufferRadius;
	private PanelBufferData panelBufferData;
	private PanelResultData panelResultData;
	private PanelResultSet panelResultSet;
	private MapControl mapControl;
	private String resultDatasetName;
	private Recordset recordset;
	private Object radius;
	private DatasetVector sourceDatasetVector;
	private DatasetVector resultDatasetVector;
	private boolean isArcSegmentNumSuitable = true;
	private boolean isComboBoxDatasetNotNull = true;
	private boolean isBufferSucceed;
	private boolean isShowInMap;
	private boolean isRadiusNumSuitable = true;
	private boolean isHasResultDatasource = true;
	private DoSome some;
	private BufferProgressCallable bufferProgressCallable;
	private final static int DEFAULT_MIN = 4;
	private final static int DEFAULT_MAX = 200;
	private final static Object DEFAULT_VALUE = 10;
	private LocalItemListener localItemListener = new LocalItemListener();

	public void setSome(DoSome some) {
		this.some = some;
	}

	public boolean isArcSegmentNumSuitable() {
		return isArcSegmentNumSuitable;
	}

	public void setArcSegmentNumSuitable(boolean isArcSegmentNumSuitable) {
		this.isArcSegmentNumSuitable = isArcSegmentNumSuitable;
		if (some != null) {
			some.doSome(isArcSegmentNumSuitable, isComboBoxDatasetNotNull, isRadiusNumSuitable, isHasResultDatasource);
		}
	}

	public boolean isComboBoxDatasetNotNull() {
		return isComboBoxDatasetNotNull;

	}

	public void setComboBoxDatasetNotNull(boolean isComboBoxDatasetNotNull) {
		this.isComboBoxDatasetNotNull = isComboBoxDatasetNotNull;
		if (some != null) {
			some.doSome(isArcSegmentNumSuitable, isComboBoxDatasetNotNull, isRadiusNumSuitable, isHasResultDatasource);
		}
		// 当数据集为空时，即没有数据用于缓冲，设置其他控件不可用
		this.panelBufferData.setPanelEnable(isComboBoxDatasetNotNull);
		this.panelBufferRadius.setPanelEnable(isComboBoxDatasetNotNull);
		this.panelResultData.setPanelEnable(isComboBoxDatasetNotNull);
		this.panelResultSet.setPanelEnable(isComboBoxDatasetNotNull);
	}

	public boolean isRadiusNumSuitable() {
		return isRadiusNumSuitable;
	}

	public void setRadiusNumSuitable(boolean isRadiusNumSuitable) {
		this.isRadiusNumSuitable = isRadiusNumSuitable;
		if (some != null) {
			some.doSome(isArcSegmentNumSuitable, isComboBoxDatasetNotNull, isRadiusNumSuitable, isHasResultDatasource);
		}
	}

	public boolean isHasResultDatasource() {
		return isHasResultDatasource;
	}

	public void setHasResultDatasource(boolean isHasResultDatasource) {
		this.isHasResultDatasource = isHasResultDatasource;
		if (some != null) {
			some.doSome(isArcSegmentNumSuitable, isComboBoxDatasetNotNull, isRadiusNumSuitable, isHasResultDatasource);
		}
	}

	public PanelPointOrRegionAnalyst() {
		initComponent();
		setPanelPointOrRegionAnalyst();
	}

	private void initComponent() {
		this.panelBasic = new JPanel();
		this.panelBasicLeft = new JPanel();
		this.panelBasicRight = new JPanel();
		this.setLayout(new BorderLayout());
		this.add(this.panelBasic, BorderLayout.CENTER);

		this.panelBufferRadius = new PanelBufferRadius();
		this.panelBufferData = new PanelBufferData();
		this.panelResultData = new PanelResultData();
		this.panelResultSet = new PanelResultSet();

		setPanelBasicLayout();
		setPanelBasicLeftLayout();
		setPanelBasicRightLayout();
	}

	private void setPanelPointOrRegionAnalyst() {
		setPanelBufferData();
		setPanelBufferRadius();
		setPanelResultData();
		registerEvent();
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
					if (activeLayer[i].getDataset().getType() == DatasetType.POINT || activeLayer[i].getDataset().getType() == DatasetType.REGION) {
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
					setComboBoxDatasetNotNull(false);

				}
			} else if (nodeData.getData() instanceof Dataset) {
				Dataset selectedDataset = (Dataset) nodeData.getData();
				this.panelBufferData.getComboBoxBufferDataDatasource().setSelectedDatasource(selectedDataset.getDatasource());
				this.panelBufferData.getComboBoxBufferDataDataset().setDatasets(selectedDataset.getDatasource().getDatasets());
				if (selectedDataset.getType() == DatasetType.POINT || selectedDataset.getType() == DatasetType.REGION) {
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
		// 暂不支持三维
		ArrayList<DatasetType> datasetTypes = new ArrayList<DatasetType>();
		datasetTypes.add(DatasetType.POINT);
		datasetTypes.add(DatasetType.REGION);
		// datasetTypes.add(DatasetType.POINT3D);
		// datasetTypes.add(DatasetType.REGION3D);
		this.panelBufferData.getComboBoxBufferDataDataset().setSupportedDatasetTypes(datasetTypes.toArray(new DatasetType[datasetTypes.size()]));
	}

	/**
	 * 设置PanelBufferRadius,初始化comboBoxField，获得缓冲半径的初始化值
	 * 由于封装了缓冲半径面板，因此进行下拉列表框的初始化，获得缓冲半径的初始化值
	 */
	private void setPanelBufferRadius() {
		if (this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() != null) {
			DatasetVector comboBoxDataset = (DatasetVector) this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset();
			this.panelBufferRadius.getNumericFieldComboBox().setDataset(comboBoxDataset);
			this.panelBufferRadius.getNumericFieldComboBox().setSelectedItem("10");
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


			if (this.panelResultSet.getCheckBoxDisplayInMap().isSelected()) {
				isShowInMap = true;
			} else {
				isShowInMap = false;
			}
			// 获得缓冲长度
			this.radius = this.panelBufferRadius.getNumericFieldComboBox().getSelectedItem().toString();
			// 设置缓冲区参数
			bufferAnalystParameter.setLeftDistance(this.radius);
			bufferAnalystParameter.setEndType(BufferEndType.ROUND);
			bufferAnalystParameter.setRadiusUnit(this.panelBufferRadius.getComboBoxUnit().getUnit());
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
//		this.textFieldNumeric.addPropertyChangeListener(ControlDefaultValues.PROPERTYNAME_VALUE, new PropertyChangeListener() {
//			@Override
//			public void propertyChange(PropertyChangeEvent evt) {
//				judgeRadiusNum();
//			}
//		});

		this.panelBufferData.getComboBoxBufferDataDataset().addItemListener(localItemListener);
		this.panelBufferData.getComboBoxBufferDataDatasource().addItemListener(localItemListener);

	}

	class LocalItemListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == panelBufferData.getComboBoxBufferDataDatasource()) {
				panelBufferData.getComboBoxBufferDataDataset().removeAllItems();
				Datasource datasource = (Datasource) e.getItem();
				if (e.getStateChange() == ItemEvent.SELECTED) {
					panelBufferData.getComboBoxBufferDataDataset().setDatasets(datasource.getDatasets());
				}
				// 切换数据源后，如果ComboBoxDataset为空时，清除字段选项
				if (panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() != null) {
					panelBufferRadius.getNumericFieldComboBox().setSelectedItem("10");
					setComboBoxDatasetNotNull(true);
					setComponentEnabled();
				} else {
					panelBufferRadius.getNumericFieldComboBox().removeAllItems();
					setComboBoxDatasetNotNull(false);
				}
			} else if (e.getSource() == panelBufferData.getComboBoxBufferDataDataset()) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					panelBufferRadius.getNumericFieldComboBox().removeAllItems();
					// 如果所选数据集不为空，创建字段ComboBox，否则不做操作
					if (panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() != null) {
						Dataset datasetItem = panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset();
						panelBufferRadius.getNumericFieldComboBox().setDataset((DatasetVector) datasetItem);
						panelBufferRadius.getNumericFieldComboBox().setSelectedItem("10");
						setComboBoxDatasetNotNull(true);
					} else {
						setComboBoxDatasetNotNull(false);
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

//	class LocalActionListener implements ActionListener {
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			if (e.getSource() == radioButtonField) {
//				if (comboBoxFieldControl.getSelectedItem() != null) {
//					radius = comboBoxFieldControl.getSelectedItem().toString();
//				}
//				setComponentEnabled();
//			} else if (e.getSource() == radioButtonNumeric) {
//				radius = Double.parseDouble(textFieldNumeric.getValue().toString());
//				setComponentEnabled();
//			} else if (e.getSource() == textFieldNumeric) {
//				radius = Double.parseDouble(textFieldNumeric.getValue().toString());
//			} else if (e.getSource() == comboBoxFieldControl) {
//				if (comboBoxFieldControl.getSelectedItem() != null) {
//					radius = comboBoxFieldControl.getSelectedItem().toString();
//				}
//			}
//		}
//	}

	private void getButtonOkEnabled() {
		try {
			long value = Long.parseLong(panelResultSet.getTextFieldSemicircleLineSegment().getValue().toString());
			if (value < DEFAULT_MIN || value > DEFAULT_MAX) {
				setArcSegmentNumSuitable(false);
			} else {
				setArcSegmentNumSuitable(true);
			}
		} catch (Exception e) {
			setArcSegmentNumSuitable(false);
		}
	}

//	private void judgeRadiusNum() {
//		double num = Double.parseDouble(textFieldNumeric.getValue().toString());
//		setRadiusNumSuitable(true);
//		if (this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() != null) {
//			if (this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset().getType() == DatasetType.POINT) {
//				if (num <= 0) {
//					setRadiusNumSuitable(false);
//				} else {
//					setRadiusNumSuitable(true);
//				}
//			} else if (this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset().getType() == DatasetType.REGION) {
//				if (num == 0) {
//					setRadiusNumSuitable(false);
//				} else {
//					setRadiusNumSuitable(true);
//				}
//			}
//		}
//	}
}
