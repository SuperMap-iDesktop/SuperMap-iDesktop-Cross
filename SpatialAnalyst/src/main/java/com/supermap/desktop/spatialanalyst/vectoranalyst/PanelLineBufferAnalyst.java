package com.supermap.desktop.spatialanalyst.vectoranalyst;

import com.supermap.analyst.spatialanalyst.BufferAnalystParameter;
import com.supermap.analyst.spatialanalyst.BufferEndType;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.utilities.MapViewUIUtilities;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.ui.controls.comboBox.ComboBoxLengthUnit;
import com.supermap.desktop.ui.controls.comboBox.SmNumericFieldComboBox;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;

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
	private ArrayList<Recordset> recordsetList;
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

	private LocalItemListener localItemListener = new LocalItemListener();
	private LocalActionListener localActionListener = new LocalActionListener();
	private LocalDocumentListener localDocumentListener = new LocalDocumentListener();
	private SemicircleLineSegmentCaretListener semicircleLineSegmentCaretListener = new SemicircleLineSegmentCaretListener();

	private NumericLeftCaretListener numericLeftCaretListener = new NumericLeftCaretListener();
	private NumericRightCaretListener numericRightCaretListener = new NumericRightCaretListener();

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

	/**
	 * 设置datasetComboBox否为空
	 *
	 * @param isComboBoxDatasetNotNull
	 */
	public void setComboBoxDatasetNotNull(boolean isComboBoxDatasetNotNull) {
		this.isComboBoxDatasetNotNull = isComboBoxDatasetNotNull;
		if (some != null) {
			some.doSome(isArcSegmentSuitable, this.isComboBoxDatasetNotNull, isRadiusNumSuitable, isHasResultDatasource);
		}
		// 当数据集为空时，即没有数据用于缓冲，设置其他控件不可用
		this.panelBufferData.setPanelEnable(this.isComboBoxDatasetNotNull);
		this.panelResultData.setPanelEnable(this.isComboBoxDatasetNotNull);
		this.panelResultSet.setPanelEnable(this.isComboBoxDatasetNotNull);

		this.radioButtonBufferTypeRound.setEnabled(this.isComboBoxDatasetNotNull);
		this.radioButtonBufferTypeFlat.setEnabled(this.isComboBoxDatasetNotNull);

		if (this.radioButtonBufferTypeFlat.isSelected()) {
			this.checkBoxBufferLeft.setEnabled(this.isComboBoxDatasetNotNull);
			this.checkBoxBufferRight.setEnabled(this.isComboBoxDatasetNotNull);
		}
		this.comboBoxUnit.setEnabled(this.isComboBoxDatasetNotNull);
		this.numericFieldComboBoxLeft.setEnabled(this.isComboBoxDatasetNotNull);
		this.numericFieldComboBoxRight.setEnabled(this.isComboBoxDatasetNotNull);
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

	/**
	 *
	 */
	public PanelLineBufferAnalyst(DoSome some) {
		setSome(some);
		initComponent();
		initResources();
		setPanelLineBufferAnalyst();

	}

	/**
	 *
	 */
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

		GroupLayout panelBufferTypeLayout = new GroupLayout(this);
		this.setLayout(panelBufferTypeLayout);
		//@formatter:off
		panelBufferTypeLayout.setHorizontalGroup(panelBufferTypeLayout.createSequentialGroup()
					.addComponent(this.panelBasic));
		panelBufferTypeLayout.setVerticalGroup(panelBufferTypeLayout.createSequentialGroup()
					.addComponent(this.panelBasic,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE));
		//@formatter:on

//		this.setLayout(new BorderLayout());
//		this.add(this.panelBasic, BorderLayout.CENTER);

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

	public void setPanelLineBufferAnalyst() {
		setPanelBufferType();
		setPanelBufferData();
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
//		panelBufferTypeLayout.setAutoCreateContainerGaps(true);
		panelBufferTypeLayout.setAutoCreateGaps(true);
		this.panelBufferType.setLayout(panelBufferTypeLayout);
		//@formatter:off
		panelBufferTypeLayout.setHorizontalGroup(panelBufferTypeLayout.createSequentialGroup()
					.addComponent(this.radioButtonBufferTypeRound)
					.addComponent(this.radioButtonBufferTypeFlat)
					.addComponent(this.checkBoxBufferLeft)
					.addComponent(this.checkBoxBufferRight));

		panelBufferTypeLayout.setVerticalGroup(panelBufferTypeLayout.createSequentialGroup()
				.addGroup(panelBufferTypeLayout.createParallelGroup(Alignment.CENTER)
					.addComponent(this.radioButtonBufferTypeRound)
					.addComponent(this.radioButtonBufferTypeFlat)
					.addComponent(this.checkBoxBufferLeft)
					.addComponent(this.checkBoxBufferRight)).addContainerGap());
		//@formatter:on
	}

	private void initComponentBufferRadius() {
		this.labelUnit = new JLabel("Unit");
		this.labelLeftNumericFieldRadius = new JLabel("LeftNumericFieldRadius");
		this.labelRightNumericFieldRadius = new JLabel("RightNumericFieldRadius");
		this.comboBoxUnit = new ComboBoxLengthUnit();
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
						.addComponent(this.comboBoxUnit,5,5,Short.MAX_VALUE)
						.addComponent(this.numericFieldComboBoxLeft,5,5,Short.MAX_VALUE)
						.addComponent(this.numericFieldComboBoxRight,5,5,Short.MAX_VALUE)));
		panelBufferRadiusLayout.setVerticalGroup(panelBufferRadiusLayout.createSequentialGroup()
				.addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelUnit)
						.addComponent(this.comboBoxUnit,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE))
				.addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelLeftNumericFieldRadius)
						.addComponent(this.numericFieldComboBoxLeft,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE))
				.addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelRightNumericFieldRadius)
						.addComponent(this.numericFieldComboBoxRight,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE))
				.addGap(5,5,Short.MAX_VALUE));
		//@formatter:on

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
					    // 0,0,400,限制左右面板纵向拉伸程度，当拉伸到一定程度时，不再拉伸
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
						.addComponent(this.panelResultSet)));
		panelBasicLeftLayout.setVerticalGroup(panelBasicLeftLayout.createSequentialGroup()
				.addComponent(this.panelBufferData).addContainerGap()
				.addComponent(this.panelResultSet));
		//@formatter:on

	}

	private void setPanelBasicRightLayout() {
		GroupLayout panelBasicRightLayout = new GroupLayout(this.panelBasicRight);
		panelBasicRightLayout.setAutoCreateGaps(true);
		this.panelBasicRight.setLayout(panelBasicRightLayout);
		//@formatter:off
		panelBasicRightLayout.setHorizontalGroup(panelBasicRightLayout.createSequentialGroup()
				.addGroup(panelBasicRightLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.panelBufferType,0,10,Short.MAX_VALUE)
						.addComponent(this.panelBufferRadius)
						.addComponent(this.panelResultData)));
		panelBasicRightLayout.setVerticalGroup(panelBasicRightLayout.createSequentialGroup()
				.addComponent(this.panelBufferType).addContainerGap()
				.addComponent(this.panelBufferRadius).addContainerGap()
				.addComponent(this.panelResultData));
		//@formatter:on

	}

	/**
	 * 当窗体界面打开时，且打开的窗体是地图时，如果数据集不是线或者网络数据集，设置选中数据集的数据源的第一个线或者网络数据集，否则设置数据集为选中地图的第一个数据集 如果窗体没有打开，获取工作空间树选中节点,得到选中的数据集，数据源
	 */
	private void setPanelBufferData() {
		setComboBoxDatasetType();
		int layersCount;
		// 判断mapControl是否打开--yuanR 2017.3.10
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
						if (layer.getDataset().getType() == DatasetType.LINE || layer.getDataset().getType() == DatasetType.NETWORK) {
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
			// 初始化半径长度值
			this.numericFieldComboBoxLeft.setSelectedItem("10");
			this.numericFieldComboBoxRight.setSelectedItem("10");
			setComponentEnabled();
		} else {
			this.numericFieldComboBoxLeft.setEnabled(false);
			this.numericFieldComboBoxRight.setEnabled(false);
		}
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
		this.checkBoxBufferLeft.addItemListener(localItemListener);
		this.checkBoxBufferRight.addItemListener(localItemListener);
		this.panelResultSet.getCheckBoxDisplayInMap().addItemListener(localItemListener);
		this.panelResultSet.getCheckBoxDisplayInScene().addItemListener(localItemListener);
		this.panelResultSet.getCheckBoxRemainAttributes().addItemListener(localItemListener);
		this.panelResultSet.getCheckBoxUnionBuffer().addItemListener(localItemListener);
		this.radioButtonBufferTypeFlat.addActionListener(localActionListener);
		this.radioButtonBufferTypeRound.addActionListener(localActionListener);
		this.numericFieldComboBoxLeft.addItemListener(localItemListener);
		this.numericFieldComboBoxRight.addItemListener(localItemListener);
		this.panelResultSet.getTextFieldSemicircleLineSegment().addCaretListener(semicircleLineSegmentCaretListener);
		// 给结果数据集名称文本框添加监听--yuanR 2017.3.2
		this.panelResultData.getTextFieldResultDataDataset().getDocument().addDocumentListener(localDocumentListener);
		//给“半径长度”左右comboBox控件添加监听--yuanR 2017.3.2
		((JTextField) this.numericFieldComboBoxLeft.getEditor().getEditorComponent()).addCaretListener(numericLeftCaretListener);
		((JTextField) this.numericFieldComboBoxRight.getEditor().getEditorComponent()).addCaretListener(numericRightCaretListener);
	}

	private void removeRegisterEvent() {
		this.panelBufferData.getComboBoxBufferDataDatasource().removeItemListener(localItemListener);
		this.panelBufferData.getComboBoxBufferDataDataset().removeItemListener(localItemListener);
		this.panelBufferData.getCheckBoxGeometrySelect().removeItemListener(localItemListener);
		this.checkBoxBufferLeft.removeItemListener(localItemListener);
		this.checkBoxBufferRight.removeItemListener(localItemListener);
		this.panelResultSet.getCheckBoxDisplayInMap().removeItemListener(localItemListener);
		this.panelResultSet.getCheckBoxDisplayInScene().removeItemListener(localItemListener);
		this.panelResultSet.getCheckBoxRemainAttributes().removeItemListener(localItemListener);
		this.panelResultSet.getCheckBoxUnionBuffer().removeItemListener(localItemListener);
		this.radioButtonBufferTypeFlat.removeActionListener(localActionListener);
		this.radioButtonBufferTypeRound.removeActionListener(localActionListener);
		this.numericFieldComboBoxLeft.removeItemListener(localItemListener);
		this.numericFieldComboBoxRight.removeItemListener(localItemListener);
		this.panelResultSet.getTextFieldSemicircleLineSegment().removeCaretListener(semicircleLineSegmentCaretListener);
		// 给结果数据集名称文本框添加监听--yuanR 2017.3.2c
		this.panelResultData.getTextFieldResultDataDataset().getDocument().removeDocumentListener(localDocumentListener);
		//给“半径长度”左右comboBox控件添加监听--yuanR 2017.3.2
		((JTextField) this.numericFieldComboBoxLeft.getEditor().getEditorComponent()).removeCaretListener(numericLeftCaretListener);
		((JTextField) this.numericFieldComboBoxRight.getEditor().getEditorComponent()).removeCaretListener(numericRightCaretListener);
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
			// TODO yuanR 2017.3.10 需要组件对其接口进行修改，满足传入的参数符合多种情况。
			this.radiusLeft = this.numericFieldComboBoxLeft.getSelectedItem().toString();
			this.radiusRight = this.numericFieldComboBoxRight.getSelectedItem().toString();
			// 暂时由我们桌面进行预处理，如果是可以转为数字的字符串，转换为数字
			// 因为当源数据集是记录集时，不接受：“10” 这样的字符串
			// 获得缓冲长度
			if (DoubleUtilities.isDouble((String) this.radiusRight) && (DoubleUtilities.isDouble((String) this.radiusLeft))) {
				this.radiusRight = DoubleUtilities.stringToValue(this.numericFieldComboBoxRight.getSelectedItem().toString());
				this.radiusLeft = DoubleUtilities.stringToValue(this.numericFieldComboBoxLeft.getSelectedItem().toString());
			}
//			if (DoubleUtilities.isDouble((String) this.radiusLeft)) {
//				this.radiusLeft = DoubleUtilities.stringToValue(this.numericFieldComboBoxLeft.getSelectedItem().toString());
//			}

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
				this.isShowInMap = true;
			} else {
				this.isShowInMap = false;
			}

			bufferAnalystParameter.setRadiusUnit(this.comboBoxUnit.getUnit());
			bufferAnalystParameter.setSemicircleLineSegment(Integer.parseInt(this.panelResultSet.getTextFieldSemicircleLineSegment().getText()));

			// 当CheckBoxGeometrySelect()选中时，进行记录集缓冲分析，否则进行数据集缓冲分析
			FormProgress formProgress = new FormProgress();
			if (this.panelBufferData.getCheckBoxGeometrySelect().isSelected()) {
				for (int i = 0; i < this.recordsetList.size(); i++) {
					this.recordset = this.recordsetList.get(i);
					this.bufferProgressCallable = new BufferProgressCallable(this.recordset, this.resultDatasetVector, bufferAnalystParameter, this.panelResultSet
							.getCheckBoxUnionBuffer().isSelected(), this.panelResultSet.getCheckBoxRemainAttributes().isSelected(), this.isShowInMap);
					if (formProgress != null) {
						formProgress.doWork(this.bufferProgressCallable);
						this.isBufferSucceed = this.bufferProgressCallable.isSucceed();
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
				this.bufferProgressCallable = new BufferProgressCallable(sourceDatasetVector, this.resultDatasetVector, bufferAnalystParameter, this.panelResultSet
						.getCheckBoxUnionBuffer().isSelected(), this.panelResultSet.getCheckBoxRemainAttributes().isSelected(), this.isShowInMap);
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

	/**
	 * 创建新的数据集，储存生成的缓冲结果
	 *
	 * @param sourceDatasetVector
	 */
	private void createResultDataset(DatasetVector sourceDatasetVector) {
		Datasource datasource = this.panelResultData.getComboBoxResultDataDatasource().getSelectedDatasource();
		DatasetVectorInfo resultDatasetVectorInfo = new DatasetVectorInfo();
		this.resultDatasetName = this.panelResultData.getTextFieldResultDataDataset().getText();
		resultDatasetVectorInfo.setName(this.resultDatasetName);
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
					if (panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() == null) {
						// 切换comboBoxDatasource时，如果comboBoxDataset为空时将字段选项置灰，默认选中数值型
						numericFieldComboBoxLeft.removeAllItems();
						numericFieldComboBoxRight.removeAllItems();
						setComponentEnabled();
						setComboBoxDatasetNotNull(false);
					} else {
						numericFieldComboBoxLeft.setDataset((DatasetVector) panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset());
						numericFieldComboBoxRight.setDataset((DatasetVector) panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset());
						numericFieldComboBoxLeft.setSelectedItem(10);
						numericFieldComboBoxRight.setSelectedItem(10);
						setComboBoxDatasetNotNull(true);
					}
				}
				// 切换数据源后，如果ComboBoxDataset为空时，清除字段选项


			} else if (e.getSource() == panelBufferData.getComboBoxBufferDataDataset()) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					numericFieldComboBoxLeft.removeAllItems();
					numericFieldComboBoxRight.removeAllItems();
					// 如果所选数据集不为空，创建字段ComboBox，否则不做操作
					if (panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() != null) {
						DatasetVector datasetItem = (DatasetVector) panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset();
						numericFieldComboBoxLeft.setDataset(datasetItem);
						numericFieldComboBoxRight.setDataset(datasetItem);
						numericFieldComboBoxLeft.setSelectedItem(10);
						numericFieldComboBoxRight.setSelectedItem(10);
						setComboBoxDatasetNotNull(true);
					} else {
						setComboBoxDatasetNotNull(false);
					}
				}
				setComponentEnabled();
			} else if (e.getSource() == panelBufferData.getCheckBoxGeometrySelect()) {
				setComponentEnabled();
			} else if (e.getSource() == panelResultSet.getCheckBoxUnionBuffer()) {
				setComponentEnabled();
				panelResultSet.getCheckBoxRemainAttributes().setSelected(false);
			} else if (e.getSource() == checkBoxBufferLeft || e.getSource() == checkBoxBufferRight) {
				//当操作左右半径复选框时,根据左右半径长度值的情况设置确定按钮是否可用--yuanR 2017.3.2

				if (!checkBoxBufferRight.isSelected() && !checkBoxBufferLeft.isSelected()) {
					setRadiusNumSuitable(false);
				} else {
					setRadiusNumSuitable(true);
				}
				setComponentEnabled();
			}
			// 当文本框改变时，对缓冲面板其他控件属性是否正确进行判断--yuanR 2017.3.10
			judgeOKButtonisEnabled();
		}
	}

	/**
	 *
	 */
	class LocalActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == radioButtonBufferTypeRound) {
				// 当点击圆头缓冲时
				numericFieldComboBoxRight.setSelectedItem(numericFieldComboBoxLeft.getSelectedItem());
				checkBoxBufferLeft.setSelected(true);
				checkBoxBufferRight.setSelected(true);
				setComponentEnabled();
			} else if (e.getSource() == radioButtonBufferTypeFlat) {
				checkBoxBufferLeft.setEnabled(!radioButtonBufferTypeRound.isSelected());
				checkBoxBufferRight.setEnabled(!radioButtonBufferTypeRound.isSelected());
			}
			// 当文本框改变时，对缓冲面板其他控件属性是否正确进行判断--yuanR 2017.3.10
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
	 * yuanR 2017.3.6
	 * 给“参数设置”JTextField添加光标改变事件，当值有误时，置灰确定按钮
	 */
	class SemicircleLineSegmentCaretListener implements CaretListener {
		@Override
		public void caretUpdate(CaretEvent e) {
			judgeArcSegmentNum();
			// 当文本框改变时，对缓冲面板其他控件属性是否正确进行判断--yuanR 2017.3.10
			judgeOKButtonisEnabled();
		}
	}

	/**
	 * yuanR 2017.3.2
	 * 给“缓冲右半径长度”JComboBox添加光标改变事件，当值改变时，相应控件属性也更随改变：置灰确定按钮等
	 */
	class NumericRightCaretListener implements CaretListener {
		@Override
		public void caretUpdate(CaretEvent e) {
			// 防止sql表达式面板弹出两次，当选中“表达式..”不进行同步--yuanR 2017.3.7
			String text = ((JTextField) e.getSource()).getText();
			if (radioButtonBufferTypeRound.isSelected() && !text.equals(CoreProperties.getString("String_ThemeGraphItemExpressionPicker_ButtonExpression"))) {
				// 当选择了圆头缓冲类型时，进行同步设置
				((JTextField) numericFieldComboBoxLeft.getEditor().getEditorComponent()).removeCaretListener(numericLeftCaretListener);
				numericFieldComboBoxLeft.setSelectedItem(text);
				((JTextField) numericFieldComboBoxLeft.getEditor().getEditorComponent()).addCaretListener(numericLeftCaretListener);
			}
			setOKButtonisEnabled(!StringUtilities.isNullOrEmpty(numericFieldComboBoxRight.getEditor().getItem().toString()));
			// 当文本框改变时，对缓冲面板其他控件属性是否正确进行判断--yuanR 2017.3.10
			judgeOKButtonisEnabled();
		}
	}

	/**
	 * yuanR 2017.3.2
	 * 给“缓冲左半径长度”JComboBox添加光标改变事件，当值改变时，相应控件属性也更随改变：置灰确定按钮等
	 */
	class NumericLeftCaretListener implements CaretListener {
		@Override
		public void caretUpdate(CaretEvent e) {
			// 防止sql表达式面板弹出两次，当选中“表达式..”不进行同步--yuanR 2017.3.7
			String text = ((JTextField) e.getSource()).getText();
			if (radioButtonBufferTypeRound.isSelected() && !text.equals(CoreProperties.getString("String_ThemeGraphItemExpressionPicker_ButtonExpression"))) {
				// 当选择了圆头缓冲类型时，进行同步设置
				((JTextField) numericFieldComboBoxRight.getEditor().getEditorComponent()).removeCaretListener(numericRightCaretListener);
				numericFieldComboBoxRight.setSelectedItem(text);
				((JTextField) numericFieldComboBoxRight.getEditor().getEditorComponent()).addCaretListener(numericRightCaretListener);
			}
			setOKButtonisEnabled(!StringUtilities.isNullOrEmpty(numericFieldComboBoxLeft.getEditor().getItem().toString()));
			// 当文本框改变时，对缓冲面板其他控件属性是否正确进行判断--yuanR 2017.3.10
			judgeOKButtonisEnabled();
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

	/**
	 * 判断“半径长度值”是否正确
	 */
	private void judgeRadiusNum() {
		// 其他情况的判断
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
		Boolean BufferCheckBoxisSelected = false;

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
		if (checkBoxBufferLeft.isSelected() || checkBoxBufferRight.isSelected()) {
			BufferCheckBoxisSelected = true;
		}
		setOKButtonisEnabled(DatasourceisNotNull && DatasetisNotNull && ResulDatasourceisNotNull && ResulDatasetNameisAvailable && BufferCheckBoxisSelected);
	}
}
