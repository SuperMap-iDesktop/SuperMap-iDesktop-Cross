package com.supermap.desktop.spatialanalyst.vectoranalyst;

import com.supermap.analyst.spatialanalyst.BufferAnalystParameter;
import com.supermap.analyst.spatialanalyst.BufferEndType;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.ui.controls.borderPanel.PanelBufferRadius;
import com.supermap.desktop.ui.controls.progress.FormProgress;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.mapping.Layer;
import com.supermap.ui.MapControl;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
	private LocalDocumentListener localDocumentListener = new LocalDocumentListener();
	private NumericFieldComboBoxCaretListener numericFieldComboBoxCaretListener = new NumericFieldComboBoxCaretListener();
	private SemicircleLineSegmentCaretListener semicircleLineSegmentCaretListener=new SemicircleLineSegmentCaretListener();

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
		this.panelBufferData.setPanelEnable(this.isComboBoxDatasetNotNull);
		this.panelBufferRadius.setPanelEnable(this.isComboBoxDatasetNotNull);
		this.panelResultData.setPanelEnable(this.isComboBoxDatasetNotNull);
		this.panelResultSet.setPanelEnable(this.isComboBoxDatasetNotNull);
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

	public PanelPointOrRegionAnalyst(DoSome some) {
		setSome(some);
		initComponent();
		setPanelPointOrRegionAnalyst();
	}

	/**
	 *
	 */
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

	public boolean isBufferSucceed() {
		return isBufferSucceed;
	}

	public void setPanelPointOrRegionAnalyst() {
		setPanelBufferData();
		setPanelBufferRadius();
		setPanelResultData();
		registerEvent();
	}


	private void setPanelBasicLayout() {
		GroupLayout panelBasicLayout = new GroupLayout(this.panelBasic);
		panelBasicLayout.setAutoCreateContainerGaps(true);
		panelBasicLayout.setAutoCreateGaps(true);
		this.panelBasic.setLayout(panelBasicLayout);

		//@formatter:off
         panelBasicLayout.setHorizontalGroup(panelBasicLayout.createSequentialGroup()
                   .addComponent(this.panelBasicLeft,0,180,Short.MAX_VALUE)
                   .addComponent(this.panelBasicRight,0,180,Short.MAX_VALUE));
         panelBasicLayout.setVerticalGroup(panelBasicLayout.createSequentialGroup()
                   .addGroup(panelBasicLayout.createParallelGroup(Alignment.LEADING)
                             .addComponent(this.panelBasicLeft)
                             .addComponent(this.panelBasicRight)));
         //@formatter:on
	}

	private void setPanelBasicLeftLayout() {

		GroupLayout panelBasicLeftLayout = new GroupLayout(this.panelBasicLeft);
		panelBasicLeftLayout.setAutoCreateGaps(true);
		this.panelBasicLeft.setLayout(panelBasicLeftLayout);

		//@formatter:off
          panelBasicLeftLayout.setHorizontalGroup(panelBasicLeftLayout.createSequentialGroup()
                    .addGroup(panelBasicLeftLayout.createParallelGroup(Alignment.LEADING)
                              .addComponent(this.panelBufferData)
                              .addComponent(this.panelResultData)));
          panelBasicLeftLayout.setVerticalGroup(panelBasicLeftLayout.createSequentialGroup()
                    .addComponent(this.panelBufferData).addContainerGap()
                    .addComponent(this.panelResultData));
          //@formatter:on
	}

	private void setPanelBasicRightLayout() {

		GroupLayout panelBasicRightLayout = new GroupLayout(this.panelBasicRight);
		panelBasicRightLayout.setAutoCreateGaps(true);
		this.panelBasicRight.setLayout(panelBasicRightLayout);
		//@formatter:off
          panelBasicRightLayout.setHorizontalGroup(panelBasicRightLayout.createSequentialGroup()
                    .addGroup(panelBasicRightLayout.createParallelGroup(Alignment.LEADING)
                              .addComponent(this.panelBufferRadius)
                              .addComponent(this.panelResultSet)));
          panelBasicRightLayout.setVerticalGroup(panelBasicRightLayout.createSequentialGroup()
		          .addComponent(this.panelBufferRadius).addContainerGap()
		          .addComponent(this.panelResultSet));
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
		removeRegisterEvent();
		this.panelBufferData.getComboBoxBufferDataDatasource().addItemListener(localItemListener);
		this.panelBufferData.getComboBoxBufferDataDataset().addItemListener(localItemListener);
		this.panelBufferData.getCheckBoxGeometrySelect().addItemListener(localItemListener);
		this.panelResultSet.getCheckBoxDisplayInMap().addItemListener(localItemListener);
		this.panelResultSet.getCheckBoxDisplayInScene().addItemListener(localItemListener);
		this.panelResultSet.getCheckBoxRemainAttributes().addItemListener(localItemListener);
		this.panelResultSet.getCheckBoxUnionBuffer().addItemListener(localItemListener);
		this.panelResultSet.getTextFieldSemicircleLineSegment().addCaretListener(semicircleLineSegmentCaretListener);
		// 给结果数据集名称文本框添加监听--yuanR 2017.3.2
		this.panelResultData.getTextFieldResultDataDataset().getDocument().addDocumentListener(localDocumentListener);
		//给“半径长度”comboBox控件添加监听--yuanR 2017.3.2
		((JTextField) this.panelBufferRadius.getNumericFieldComboBox().getEditor().getEditorComponent()).addCaretListener(numericFieldComboBoxCaretListener);
	}

	private void removeRegisterEvent() {
		this.panelBufferData.getComboBoxBufferDataDatasource().removeItemListener(localItemListener);
		this.panelBufferData.getComboBoxBufferDataDataset().removeItemListener(localItemListener);
		this.panelBufferData.getCheckBoxGeometrySelect().removeItemListener(localItemListener);
		this.panelResultSet.getCheckBoxDisplayInMap().removeItemListener(localItemListener);
		this.panelResultSet.getCheckBoxDisplayInScene().removeItemListener(localItemListener);
		this.panelResultSet.getCheckBoxRemainAttributes().removeItemListener(localItemListener);
		this.panelResultSet.getCheckBoxUnionBuffer().removeItemListener(localItemListener);
		this.panelResultSet.getTextFieldSemicircleLineSegment().removeCaretListener(semicircleLineSegmentCaretListener);
		// 给结果数据集名称文本框添加监听--yuanR 2017.3.2
		this.panelResultData.getTextFieldResultDataDataset().getDocument().removeDocumentListener(localDocumentListener);
		//给“半径长度”comboBox控件添加监听--yuanR 2017.3.2
		((JTextField) this.panelBufferRadius.getNumericFieldComboBox().getEditor().getEditorComponent()).removeCaretListener(numericFieldComboBoxCaretListener);
	}

	private void initDatasourceAndDataset() {
		Datasource defaultDatasource = Application.getActiveApplication().getWorkspace().getDatasources().get(0);
		this.panelBufferData.getComboBoxBufferDataDatasource().setSelectedDatasource(defaultDatasource);
		this.panelResultData.getComboBoxResultDataDatasource().setSelectedDatasource(defaultDatasource);
		this.panelBufferData.getComboBoxBufferDataDataset().setDatasets(defaultDatasource.getDatasets());
	}

	private void setComponentEnabled() {
		// 当数据集为空时，不进行是否可用设置--yuanR 2017.3.2
		if (this.panelBufferData.getComboBoxBufferDataDataset().getItemCount() > 0) {
			this.panelBufferData.getComboBoxBufferDataDataset().setEnabled(!this.panelBufferData.getCheckBoxGeometrySelect().isSelected());
			this.panelBufferData.getComboBoxBufferDataDatasource().setEnabled(!this.panelBufferData.getCheckBoxGeometrySelect().isSelected());
		}
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
				isBufferSucceed = bufferProgressCallable.isSucceed();
				// 如果生成缓冲区失败，删除新建的数据集--yuanR
				if (!isBufferSucceed) {
					deleteResultDataset();
				}
			}
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

	/**
	 * 如果生成缓冲区失败，删除其生成的数据集--yuanR 2017.3.6
	 */
	private void deleteResultDataset() {
		Datasource datasource = this.panelResultData.getComboBoxResultDataDatasource().getSelectedDatasource();
		datasource.getDatasets().delete(this.resultDatasetName);
	}

	/**
	 *
	 */
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

	/**
	 * yuanR 2017.3.2
	 * 给结果数据集名称文本框添加的改变事件，用以当名称输入错误时，置灰确定按钮
	 */
	class LocalDocumentListener implements DocumentListener {
		@Override
		public void changedUpdate(DocumentEvent e) {
			newFilter();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			newFilter();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			newFilter();
		}

		/**
		 * 当文本框改变时
		 */
		private void newFilter() {
			if (isAvailableDatasetName(panelResultData.getTextFieldResultDataDataset().getText())) {
				setOKButtonisEnabled(true);
				panelResultData.getTextFieldResultDataDataset().setForeground(Color.BLACK);
			} else {
				setOKButtonisEnabled(false);
				panelResultData.getTextFieldResultDataDataset().setForeground(Color.RED);
			}
		}

		/**
		 * 判断数据集名称是否合法
		 *
		 * @param name
		 * @return
		 */
		private boolean isAvailableDatasetName(String name) {
			if (panelResultData.getComboBoxResultDataDatasource().getSelectedDatasource().getDatasets().isAvailableDatasetName(name)) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * yuanR 2017.3.2
	 * 给“缓冲半径长度”JComboBox添加光标改变事件，当值有误时，置灰确定按钮
	 */
	class NumericFieldComboBoxCaretListener implements CaretListener {
		@Override
		public void caretUpdate(CaretEvent e) {
			judgeRadiusNum();
		}
	}

	/**
	 *  yuanR 2017.3.6
	 * 给“参数设置”JTextField添加光标改变事件，当值有误时，置灰确定按钮
	 */
	class SemicircleLineSegmentCaretListener implements CaretListener {
		@Override
		public void caretUpdate(CaretEvent e) {
			getButtonOkEnabled();
		}
	}

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

	/**
	 * 判断“半径长度值”是否正确
	 */
	private void judgeRadiusNum() {
		// 为空时，设置确定按钮不可用
		setOKButtonisEnabled(!StringUtilities.isNullOrEmpty(this.panelBufferRadius.getNumericFieldComboBox().getEditor().getItem().toString()));
		// 情况的判断
		// TODO something
	}

	/**
	 * 设置确定按钮是否可用
	 * yuanR 2017.3.3
	 */
	public void setOKButtonisEnabled(Boolean okButtonisEnabled) {
		if (some != null) {
			some.doSome(okButtonisEnabled, okButtonisEnabled, okButtonisEnabled, okButtonisEnabled);
		}
	}
}
