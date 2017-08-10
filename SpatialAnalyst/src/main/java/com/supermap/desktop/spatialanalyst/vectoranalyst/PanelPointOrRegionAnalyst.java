package com.supermap.desktop.spatialanalyst.vectoranalyst;

import com.supermap.analyst.spatialanalyst.BufferAnalystParameter;
import com.supermap.analyst.spatialanalyst.BufferEndType;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.utilities.MapViewUIUtilities;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.ui.controls.borderPanel.PanelBufferRadius;
import com.supermap.desktop.ui.controls.progress.FormProgress;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
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
import java.util.HashMap;

public class PanelPointOrRegionAnalyst extends JPanel {

	private static final long serialVersionUID = 1L;

	private JPanel panelBasic;
	private JPanel panelBasicLeft;
	private JPanel panelBasicRight;
	private PanelBufferRadius panelBufferRadius;
	private PanelBufferData panelBufferData;
	private PanelResultData panelResultData;
	private PanelParameterSet panelParameterSet;
	private MapControl mapControl;
	private String resultDatasetName;
	private Recordset recordset;
	private ArrayList<Recordset> recordsetList;
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
	private SemicircleLineSegmentCaretListener semicircleLineSegmentCaretListener = new SemicircleLineSegmentCaretListener();

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
		this.panelParameterSet.setPanelEnable(this.isComboBoxDatasetNotNull);
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
		this.panelBufferRadius = new PanelBufferRadius();
		this.panelBufferData = new PanelBufferData();
		this.panelResultData = new PanelResultData();
		this.panelParameterSet = new PanelParameterSet();

		GroupLayout panelBufferTypeLayout = new GroupLayout(this);
		this.setLayout(panelBufferTypeLayout);
		//@formatter:off
		panelBufferTypeLayout.setHorizontalGroup(panelBufferTypeLayout.createSequentialGroup()
				.addComponent(this.panelBasic));
		panelBufferTypeLayout.setVerticalGroup(panelBufferTypeLayout.createSequentialGroup()
				.addComponent(this.panelBasic, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE));
		//@formatter:on
//		this.setLayout(new BorderLayout());
//		this.add(this.panelBasic, BorderLayout.CENTER);

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
				.addComponent(this.panelBasicLeft, 0, 180, Short.MAX_VALUE)
				.addComponent(this.panelBasicRight, 0, 180, Short.MAX_VALUE));
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
		panelBasicLeftLayout.setHorizontalGroup(panelBasicLeftLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(this.panelBufferData)
				.addComponent(this.panelResultData));
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
		panelBasicRightLayout.setHorizontalGroup(panelBasicRightLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(this.panelBufferRadius)
				.addComponent(this.panelParameterSet));
		panelBasicRightLayout.setVerticalGroup(panelBasicRightLayout.createSequentialGroup()
				.addComponent(this.panelBufferRadius).addContainerGap()
				.addComponent(this.panelParameterSet));
		//@formatter:on
	}

	/**
	 * 当窗体界面打开时，且打开的窗体是地图时，如果数据集不是线或者网络数据集，设置选中数据集的数据源的第一个线或者网络数据集，否则设置数据集为选中地图的第一个数据集 如果窗体没有打开，获取工作空间树选中节点,得到选中的数据集，数据源
	 */
	private void setPanelBufferData() {
		setComboBoxDatasetType();
		int layersCount;
		// 窗体激活，且打开的窗体是地图,如果窗体没有激活，直接获取工作空间树节点，通过树节点数据
		if (Application.getActiveApplication().getActiveForm() != null && Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
			this.mapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
			// 通过HashMap使用，当进行选择集的缓冲区分析时，对其图层进行：“去重”、“去坏”处理--yuanR 2017.3.13
			// 获得mapContorl中所有图层，包括分组图层下的
			ArrayList<Layer> arrayList;
			arrayList = MapUtilities.getLayers(this.mapControl.getMap(), true);
			layersCount = arrayList.size();
			//是否存在图层
			if (layersCount > 0) {
				// 遍历所有图层存在的图层，进行去重，去坏处理
				HashMap<Dataset, Layer> layerMap = new HashMap<>();
				for (int i = 0; i < layersCount; i++) {
					if (arrayList.get(i).getDataset() == null) {
						continue;
					}
					layerMap.put(arrayList.get(i).getDataset(), arrayList.get(i));
				}
				layersCount = layerMap.size();
				//是否存在不重复的、未损坏的图层
				if (layersCount > 0) {
					this.recordsetList = new ArrayList<>();
					for (Layer layer : layerMap.values()) {
						// 对不重复，未损毁图层进行其数据集类型的筛选
						if (layer.getDataset().getType() == DatasetType.POINT || layer.getDataset().getType() == DatasetType.REGION) {
							if (layer.getSelection() != null && layer.getSelection().getCount() != 0) {
								this.recordsetList.add(layer.getSelection().toRecordset());

								this.panelBufferData.getComboBoxBufferDataDatasource().setSelectedDatasource(layer.getDataset().getDatasource());
								this.panelBufferData.getComboBoxBufferDataDataset().setDatasets(layer.getDataset().getDatasource().getDatasets());
								this.panelBufferData.getComboBoxBufferDataDataset().setSelectedDataset(layer.getDataset());
								this.panelBufferData.getCheckBoxGeometrySelect().setEnabled(true);
								this.panelBufferData.getCheckBoxGeometrySelect().setSelected(true);
								setComponentEnabled();
							}
						}
					}
					// 所有图层所指的数据都不存在或者图层对应的数据都不符合线缓冲区面板要求,此时选择tree节点进行缓冲区初始化--yuanR 2017.3.10
					if (this.recordsetList.size() <= 0) {
						setWorkspaceTreeNode();
					}
				} else {
					setWorkspaceTreeNode();
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
		this.panelParameterSet.getCheckBoxDisplayInMap().addItemListener(localItemListener);
		this.panelParameterSet.getCheckBoxDisplayInScene().addItemListener(localItemListener);
		this.panelParameterSet.getCheckBoxRemainAttributes().addItemListener(localItemListener);
		this.panelParameterSet.getCheckBoxUnionBuffer().addItemListener(localItemListener);
		this.panelParameterSet.getTextFieldSemicircleLineSegment().addCaretListener(semicircleLineSegmentCaretListener);
		// 给结果数据集名称文本框添加监听--yuanR 2017.3.2
		this.panelResultData.getTextFieldResultDataDataset().getDocument().addDocumentListener(localDocumentListener);
		//给“半径长度”comboBox控件添加监听--yuanR 2017.3.2
		((JTextField) this.panelBufferRadius.getNumericFieldComboBox().getEditor().getEditorComponent()).addCaretListener(numericFieldComboBoxCaretListener);
	}

	private void removeRegisterEvent() {
		this.panelBufferData.getComboBoxBufferDataDatasource().removeItemListener(localItemListener);
		this.panelBufferData.getComboBoxBufferDataDataset().removeItemListener(localItemListener);
		this.panelBufferData.getCheckBoxGeometrySelect().removeItemListener(localItemListener);
		this.panelParameterSet.getCheckBoxDisplayInMap().removeItemListener(localItemListener);
		this.panelParameterSet.getCheckBoxDisplayInScene().removeItemListener(localItemListener);
		this.panelParameterSet.getCheckBoxRemainAttributes().removeItemListener(localItemListener);
		this.panelParameterSet.getCheckBoxUnionBuffer().removeItemListener(localItemListener);
		this.panelParameterSet.getTextFieldSemicircleLineSegment().removeCaretListener(semicircleLineSegmentCaretListener);
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
		this.panelParameterSet.getCheckBoxRemainAttributes().setEnabled(!this.panelParameterSet.getCheckBoxUnionBuffer().isSelected());
	}

	/**
	 * 创建缓冲区分析
	 */
	public boolean createCurrentBuffer() {
		this.isBufferSucceed = false;
		if (this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() != null) {
			this.sourceDatasetVector = (DatasetVector) this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset();
			BufferAnalystParameter bufferAnalystParameter = new BufferAnalystParameter();

			// 创建缓冲数据集
			if (this.sourceDatasetVector.getRecordCount() > 0) {
				createResultDataset();
			}


			if (this.panelParameterSet.getCheckBoxDisplayInMap().isSelected()) {
				this.isShowInMap = true;
			} else {
				this.isShowInMap = false;
			}

			// TODO yuanR 2017.3.10
			// 暂时由我们桌面进行预处理，如果是可以转为数字的字符串，转换为数字
			// 因为当源数据集是记录集时，不接受：“10” 这样的字符串
			// 获得缓冲长度
			this.radius = this.panelBufferRadius.getNumericFieldComboBox().getSelectedItem().toString();
			if (DoubleUtilities.isDouble((String) this.radius)) {
				this.radius = DoubleUtilities.stringToValue(this.panelBufferRadius.getNumericFieldComboBox().getSelectedItem().toString());
			}
			// 设置缓冲区参数
			bufferAnalystParameter.setLeftDistance(this.radius);
			bufferAnalystParameter.setEndType(BufferEndType.ROUND);
			bufferAnalystParameter.setRadiusUnit(this.panelBufferRadius.getComboBoxUnit().getUnit());
			bufferAnalystParameter.setSemicircleLineSegment(Integer.valueOf(this.panelParameterSet.getTextFieldSemicircleLineSegment().getText()));

			// 当CheckBoxGeometrySelect()选中时，进行记录集缓冲分析，否则进行数据集缓冲分析
			FormProgress formProgress = new FormProgress();
			if (this.panelBufferData.getCheckBoxGeometrySelect().isSelected()) {
				for (int i = 0; i < this.recordsetList.size(); i++) {
					this.recordset = this.recordsetList.get(i);
					this.bufferProgressCallable = new BufferProgressCallable(this.recordset, this.resultDatasetVector, bufferAnalystParameter, this.panelParameterSet
							.getCheckBoxUnionBuffer().isSelected(), this.panelParameterSet.getCheckBoxRemainAttributes().isSelected(), this.isShowInMap);
					if (formProgress != null) {
						formProgress.doWork(this.bufferProgressCallable);
						this.isBufferSucceed = this.bufferProgressCallable.isSucceed();
						// 如果生成缓冲区失败，删除新建的数据集--yuanR 2017.3.13
						if (!this.isBufferSucceed) {
							deleteResultDataset();
						}
					}
				}
				// 将生成成功生成的数据集添加到地图-yuanR 2017.3.13
				if (this.isShowInMap && this.isBufferSucceed) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							addRecordsetDatasettoMap();
						}
					});
				}
			} else {
				this.bufferProgressCallable = new BufferProgressCallable(sourceDatasetVector, this.resultDatasetVector, bufferAnalystParameter, this.panelParameterSet
						.getCheckBoxUnionBuffer().isSelected(), this.panelParameterSet.getCheckBoxRemainAttributes().isSelected(), this.isShowInMap);
				if (formProgress != null) {
					formProgress.doWork(this.bufferProgressCallable);
					this.isBufferSucceed = this.bufferProgressCallable.isSucceed();
					// 如果生成缓冲区失败，删除新建的数据集--yuanR 2017.3.10
					if (!this.isBufferSucceed) {
						deleteResultDataset();
					}
				}
			}
		}
		return this.isBufferSucceed;
	}

	private void createResultDataset() {
		Datasource datasource = this.panelResultData.getComboBoxResultDataDatasource().getSelectedDatasource();
		DatasetVectorInfo resultDatasetVectorInfo = new DatasetVectorInfo();
		this.resultDatasetName = this.panelResultData.getTextFieldResultDataDataset().getText();
		String datasetName = datasource.getDatasets().getAvailableDatasetName(this.resultDatasetName);
		resultDatasetVectorInfo.setName(datasetName);
		resultDatasetVectorInfo.setType(DatasetType.REGION);
		this.resultDatasetVector = datasource.getDatasets().create(resultDatasetVectorInfo);
		this.resultDatasetVector.setPrjCoordSys(this.sourceDatasetVector.getPrjCoordSys());
	}

	/**
	 * 如果生成缓冲区失败，删除其生成的数据集--yuanR 2017.3.6
	 */
	private void deleteResultDataset() {
		Datasource datasource = this.panelResultData.getComboBoxResultDataDatasource().getSelectedDatasource();
		datasource.getDatasets().delete(this.resultDatasetName);
	}

	/**
	 * 添加由记录集生成的数据集到地图中
	 * yuanR 2017.3.13
	 */
	private void addRecordsetDatasettoMap() {
		Dataset[] datasets = Application.getActiveApplication().getActiveDatasets();
		IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
		Map map = formMap.getMapControl().getMap();
		MapViewUIUtilities.addDatasetsToMap(map, datasets, false);
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
			} else if (e.getSource() == panelParameterSet.getCheckBoxUnionBuffer()) {
				setComponentEnabled();
				panelParameterSet.getCheckBoxRemainAttributes().setSelected(false);
			}
			// 当下拉列表框改变时，对缓冲面板其他控件属性是否正确进行判断--yuanR 2017.3.10
			judgeOKButtonisEnabled();
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
			// 当文本框改变时，对缓冲面板其他控件属性是否正确进行判断--yuanR 2017.3.10
			judgeOKButtonisEnabled();
		}

		/**
		 * 判断数据集名称是否合法
		 *
		 * @param name
		 * @return
		 */
		private boolean isAvailableDatasetName(String name) {
			if (panelResultData.getComboBoxResultDataDatasource().getSelectedDatasource() != null) {
				if (panelResultData.getComboBoxResultDataDatasource().getSelectedDatasource().getDatasets().isAvailableDatasetName(name)) {
					return true;
				} else {
					return false;
				}
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
			// 当文本框改变时，对缓冲面板其他控件属性是否正确进行判断--yuanR 2017.3.10
			judgeOKButtonisEnabled();
		}
	}

	/**
	 * yuanR 2017.3.6
	 * 给“参数设置”JTextField添加光标改变事件，当值有误时，置灰确定按钮
	 */
	class SemicircleLineSegmentCaretListener implements CaretListener {
		@Override
		public void caretUpdate(CaretEvent e) {
			getButtonOkEnabled();
			// 当文本框改变时，对缓冲面板其他控件属性是否正确进行判断--yuanR 2017.3.10
			judgeOKButtonisEnabled();
		}
	}

	private void getButtonOkEnabled() {
		try {
			long value = Long.parseLong(panelParameterSet.getTextFieldSemicircleLineSegment().getValue().toString());
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

	/**
	 * 判断确定按钮是否可用--yuanR 2017.3.7
	 */

	public void judgeOKButtonisEnabled() {
		Boolean DatasourceisNotNull = false;
		Boolean DatasetisNotNull = false;
		Boolean ResulDatasourceisNotNull = false;
		Boolean ResulDatasetNameisAvailable = false;

		if (this.panelResultData.getComboBoxResultDataDatasource().getSelectedDatasource() != null) {
			DatasourceisNotNull = true;
		}
		if (this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() != null) {
			DatasetisNotNull = true;
		}
		if (this.panelResultData.getComboBoxResultDataDatasource().getSelectedDatasource() != null) {
			ResulDatasourceisNotNull = true;
			if (panelResultData.getComboBoxResultDataDatasource().getSelectedDatasource().getDatasets().isAvailableDatasetName(panelResultData.getTextFieldResultDataDataset().getText())) {
				ResulDatasetNameisAvailable = true;
			}
		}
		setOKButtonisEnabled(DatasourceisNotNull && DatasetisNotNull && ResulDatasourceisNotNull && ResulDatasetNameisAvailable);
	}
}
