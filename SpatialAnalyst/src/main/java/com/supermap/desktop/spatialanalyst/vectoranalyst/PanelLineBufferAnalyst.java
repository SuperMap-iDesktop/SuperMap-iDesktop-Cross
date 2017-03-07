package com.supermap.desktop.spatialanalyst.vectoranalyst;

import com.supermap.analyst.spatialanalyst.BufferAnalystParameter;
import com.supermap.analyst.spatialanalyst.BufferEndType;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.ui.controls.comboBox.ComboBoxLengthUnit;
import com.supermap.desktop.ui.controls.comboBox.SmNumericFieldComboBox;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
		//@formatter:off
		GroupLayout panelBufferTypeLayout = new GroupLayout(this.panelBufferType);
//		panelBufferTypeLayout.setAutoCreateContainerGaps(true);
		panelBufferTypeLayout.setAutoCreateGaps(true);
		this.panelBufferType.setLayout(panelBufferTypeLayout);

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

//		NumberFormatter numberFormatter = new NumberFormatter();
//		numberFormatter.setValueClass(Double.class);

		this.numericFieldComboBoxLeft = new SmNumericFieldComboBox();
		this.numericFieldComboBoxRight = new SmNumericFieldComboBox();
		this.numericFieldComboBoxLeft.setPreferredSize(ControlDefaultValues.BUFFERCOMPONT_PREFERREDSIZE);
		this.numericFieldComboBoxRight.setPreferredSize(ControlDefaultValues.BUFFERCOMPONT_PREFERREDSIZE);
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
						.addComponent(this.comboBoxUnit))
				.addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelLeftNumericFieldRadius)
						.addComponent(this.numericFieldComboBoxLeft))
				.addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelRightNumericFieldRadius)
						.addComponent(this.numericFieldComboBoxRight)));
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
	    				.addComponent(this.panelBasicLeft)
	    				.addComponent(this.panelBasicRight)));
	    //@formatter:on
	}

	private void setPanelBasicLeftLayout() {
		GroupLayout panelBasicLeftLayout = new GroupLayout(this.panelBasicLeft);
		panelBasicLeftLayout.setAutoCreateGaps(true);
//		panelBasicLeftLayout.setAutoCreateContainerGaps(true);
		this.panelBasicLeft.setLayout(panelBasicLeftLayout);

		//@formatter:off
		panelBasicLeftLayout.setHorizontalGroup(panelBasicLeftLayout.createSequentialGroup()
				.addGroup(panelBasicLeftLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.panelBufferData)
						.addComponent(this.panelResultSet)));
		panelBasicLeftLayout.setVerticalGroup(panelBasicLeftLayout.createSequentialGroup()
				.addComponent(this.panelBufferData,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE).addContainerGap()
				.addComponent(this.panelResultSet,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE));
		//@formatter:on

	}

	private void setPanelBasicRightLayout() {
		GroupLayout panelBasicRightLayout = new GroupLayout(this.panelBasicRight);
		panelBasicRightLayout.setAutoCreateGaps(true);
//		panelBasicRightLayout.setAutoCreateContainerGaps(true);
		this.panelBasicRight.setLayout(panelBasicRightLayout);

		//@formatter:off
		panelBasicRightLayout.setHorizontalGroup(panelBasicRightLayout.createSequentialGroup()
				.addGroup(panelBasicRightLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.panelBufferType,0,10,Short.MAX_VALUE)
						.addComponent(this.panelBufferRadius)
						.addComponent(this.panelResultData)));
		panelBasicRightLayout.setVerticalGroup(panelBasicRightLayout.createSequentialGroup()
				.addComponent(this.panelBufferType).addContainerGap()
				.addComponent(this.panelBufferRadius,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE).addContainerGap()
				.addComponent(this.panelResultData,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE));
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
				} else {
					setComboBoxDatasetNotNull(false);
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

				// 如果生成缓冲区失败，删除新建的数据集--yuanR
				if (!isBufferSucceed) {
					deleteResultDataset();
				}
			}
		}
		return isBufferSucceed;
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
	 * yuanR 2017.3.6
	 * 给“参数设置”JTextField添加光标改变事件，当值有误时，置灰确定按钮
	 */
	class SemicircleLineSegmentCaretListener implements CaretListener {
		@Override
		public void caretUpdate(CaretEvent e) {
			judgeArcSegmentNum();
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
}
