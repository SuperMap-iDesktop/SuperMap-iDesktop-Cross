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
import com.supermap.desktop.controls.utilities.MapViewUIUtilities;
import com.supermap.desktop.ui.controls.borderPanel.PanelBufferRadius;
import com.supermap.desktop.ui.controls.progress.FormProgress;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.mapping.Map;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class PanelPointOrRegionAnalyst extends JPanel {

	private static final long serialVersionUID = 1L;

	private JPanel panelBasic;
	private JPanel panelBasicLeft;
	private JPanel panelBasicRight;

	private PanelBufferRadius panelBufferRadius = new PanelBufferRadius();
	private PanelBufferData panelBufferData = new PanelBufferData();
	private PanelResultData panelResultData = new PanelResultData();
	private PanelParameterSet panelParameterSet = new PanelParameterSet();

	private Recordset recordset;
	private Object radius;
	private String resultDatasetName;

	private boolean isArcSegmentSuitable = true;
	private boolean isComboBoxDatasetNotNull = true;
	private boolean isRadiusNumSuitable = true;
	private boolean isHasResultDataset = true;

	private DoSome some;
	private DatasetVector resultDatasetVector;
	private BufferProgressCallable bufferProgressCallable;
	private boolean isBufferSucceed;
	private final static int DEFAULT_MIN = 4;
	private final static int DEFAULT_MAX = 200;

	// 原数据监听类
	private DatasetChangedListener datasetChangedListener = new DatasetChangedListener();
	// 结果数据集名称监听
	private JTextFieldChangedListener textFieldChangedListener = new JTextFieldChangedListener();
	// 半圆弧线段数监听
	private SemicircleLineSegmentCaretListener semicircleLineSegmentCaretListener = new SemicircleLineSegmentCaretListener();


	public PanelPointOrRegionAnalyst(DoSome some) {
		this.some = some;
		initComponent();
		setPanelPointOrRegionAnalyst();
		registerEvent();
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

		intPanelBasicLayout();
		intPanelBasicLeftLayout();
		intPanelBasicRightLayout();
	}

	private void intPanelBasicLayout() {
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

	private void intPanelBasicLeftLayout() {
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

	private void intPanelBasicRightLayout() {

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


	public void setPanelPointOrRegionAnalyst() {
		// 根据数据集初始化各个面板种需要用到数据集的地方
		this.panelBufferData.initDataset(DatasetType.POINT);
		this.panelResultData.getComboBoxResultDataDatasource().setSelectedDatasource(
				this.panelBufferData.getComboBoxBufferDataDatasource().getSelectedDatasource());
		this.panelBufferRadius.initDataset((DatasetVector) this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset());
	}


	private void registerEvent() {
		removeRegisterEvent();
		this.panelBufferData.getComboBoxBufferDataDatasource().addItemListener(datasetChangedListener);
		this.panelBufferData.getComboBoxBufferDataDataset().addItemListener(datasetChangedListener);
		this.panelParameterSet.getTextFieldSemicircleLineSegment().getTextField().addCaretListener(semicircleLineSegmentCaretListener);
		this.panelResultData.getTextFieldResultDataDataset().getDocument().addDocumentListener(textFieldChangedListener);

	}

	private void removeRegisterEvent() {
		this.panelBufferData.getComboBoxBufferDataDatasource().removeItemListener(datasetChangedListener);
		this.panelBufferData.getComboBoxBufferDataDataset().removeItemListener(datasetChangedListener);
		this.panelParameterSet.getTextFieldSemicircleLineSegment().getTextField().removeCaretListener(semicircleLineSegmentCaretListener);
		this.panelResultData.getTextFieldResultDataDataset().getDocument().removeDocumentListener(textFieldChangedListener);
	}


	/**
	 * 创建缓冲区分析
	 */
	public boolean createCurrentBuffer() {
		this.isBufferSucceed = false;
		if (this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() != null) {
			DatasetVector sourceDatasetVector = (DatasetVector) this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset();
			BufferAnalystParameter bufferAnalystParameter = new BufferAnalystParameter();

			// 创建缓冲数据集
			if (sourceDatasetVector.getRecordCount() > 0) {
				createResultDataset(sourceDatasetVector);
			}

			Boolean isShowInMap = this.panelParameterSet.getCheckBoxDisplayInMap().isSelected();

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
				for (int i = 0; i < this.panelBufferData.getRecordsetList().size(); i++) {
					this.recordset = this.panelBufferData.getRecordsetList().get(i);
					this.bufferProgressCallable = new BufferProgressCallable(this.recordset, this.resultDatasetVector, bufferAnalystParameter, this.panelParameterSet
							.getCheckBoxUnionBuffer().isSelected(), this.panelParameterSet.getCheckBoxRemainAttributes().isSelected(), isShowInMap);
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
				if (isShowInMap && this.isBufferSucceed) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							addRecordsetDatasettoMap();
						}
					});
				}
			} else {
				this.bufferProgressCallable = new BufferProgressCallable(sourceDatasetVector, this.resultDatasetVector, bufferAnalystParameter, this.panelParameterSet
						.getCheckBoxUnionBuffer().isSelected(), this.panelParameterSet.getCheckBoxRemainAttributes().isSelected(), isShowInMap);
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

	private void createResultDataset(DatasetVector sourceDatasetVector) {
		Datasource datasource = this.panelResultData.getComboBoxResultDataDatasource().getSelectedDatasource();
		DatasetVectorInfo resultDatasetVectorInfo = new DatasetVectorInfo();
		this.resultDatasetName = this.panelResultData.getTextFieldResultDataDataset().getText();
		String datasetName = datasource.getDatasets().getAvailableDatasetName(this.resultDatasetName);
		resultDatasetVectorInfo.setName(datasetName);
		resultDatasetVectorInfo.setType(DatasetType.REGION);
		this.resultDatasetVector = datasource.getDatasets().create(resultDatasetVectorInfo);
		this.resultDatasetVector.setPrjCoordSys(sourceDatasetVector.getPrjCoordSys());
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
	class DatasetChangedListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == panelBufferData.getComboBoxBufferDataDatasource()) {
				panelBufferData.getComboBoxBufferDataDataset().removeAllItems();
				Datasource datasource = (Datasource) e.getItem();
				if (e.getStateChange() == ItemEvent.SELECTED) {
					panelBufferData.getComboBoxBufferDataDataset().setDatasets(datasource.getDatasets());
					if (panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() == null) {
						// 切换comboBoxDatasource时，如果comboBoxDataset为空时将字段选项置灰，默认选中数值型
						panelBufferRadius.getNumericFieldComboBox().removeAll();
						setComboBoxDatasetNotNull(false);
					} else {
						panelBufferRadius.getNumericFieldComboBox().setDataset((DatasetVector) panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset());
						panelBufferRadius.getNumericFieldComboBox().setSelectedItem(10);
						setComboBoxDatasetNotNull(true);
					}
				}
				// 切换数据源后，如果ComboBoxDataset为空时，清除字段选项


			} else if (e.getSource() == panelBufferData.getComboBoxBufferDataDataset()) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					panelBufferRadius.getNumericFieldComboBox().removeAllItems();
					// 如果所选数据集不为空，创建字段ComboBox，否则不做操作
					if (panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() != null) {
						panelBufferRadius.getNumericFieldComboBox().setDataset((DatasetVector) panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset());
						panelBufferRadius.getNumericFieldComboBox().setSelectedItem(10);
						setComboBoxDatasetNotNull(true);
					} else {
						setComboBoxDatasetNotNull(false);
					}
				}
			}
		}
	}

	/**
	 * yuanR 2017.3.2
	 * 给结果数据集名称文本框添加的改变事件，用以当名称输入错误时，置灰确定按钮
	 */
	class JTextFieldChangedListener implements DocumentListener {
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
				setHasResultDataset(true);
				panelResultData.getTextFieldResultDataDataset().setForeground(Color.BLACK);
			} else {
				setHasResultDataset(false);
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
	 *
	 */
	class SemicircleLineSegmentCaretListener implements CaretListener {
		@Override
		public void caretUpdate(CaretEvent e) {
			try {
				Integer value = Integer.valueOf(panelParameterSet.getTextFieldSemicircleLineSegment().getText());
				if (value < DEFAULT_MIN || value > DEFAULT_MAX) {
					setArcSegmentSuitable(false);
				} else {
					setArcSegmentSuitable(true);
				}
			} catch (Exception ex) {
				setArcSegmentSuitable(false);
			}
		}
	}

	/**
	 * 设置数据集参数是否为空
	 *
	 * @param isComboBoxDatasetNotNull
	 */
	public void setComboBoxDatasetNotNull(boolean isComboBoxDatasetNotNull) {
		this.isComboBoxDatasetNotNull = isComboBoxDatasetNotNull;
		if (some != null) {
			some.doSome(isArcSegmentSuitable, isComboBoxDatasetNotNull, isRadiusNumSuitable, isHasResultDataset);
		}
		// 当数据集为空时，即没有数据用于缓冲，设置其他控件不可用
		this.panelBufferData.setPanelEnable(this.isComboBoxDatasetNotNull);
		this.panelResultData.setPanelEnable(this.isComboBoxDatasetNotNull);
		this.panelParameterSet.setPanelEnable(this.isComboBoxDatasetNotNull);
		this.panelBufferRadius.setEnabled(this.isComboBoxDatasetNotNull);
	}


	/**
	 * 设置“半圆弧线段数”是否正确
	 *
	 * @param isArcSegmentSuitable
	 */
	public void setArcSegmentSuitable(boolean isArcSegmentSuitable) {
		this.isArcSegmentSuitable = isArcSegmentSuitable;
		if (some != null) {
			some.doSome(isArcSegmentSuitable, isComboBoxDatasetNotNull, isRadiusNumSuitable, isHasResultDataset);
		}
	}

	/**
	 * 设置结果数据集名称是否正确
	 *
	 * @param isHasResultDatasource
	 */
	public void setHasResultDataset(boolean isHasResultDatasource) {
		this.isHasResultDataset = isHasResultDatasource;
		if (some != null) {
			some.doSome(isArcSegmentSuitable, isComboBoxDatasetNotNull, isRadiusNumSuitable, isHasResultDatasource);
		}
	}

	/**
	 * 对于半径值是否合法暂不做要求
	 */
//	public void setRadiusNumSuitable(boolean isRadiusNumSuitable) {
//		this.isRadiusNumSuitable = isRadiusNumSuitable;
//		if (some != null) {
//			some.doSome(isArcSegmentSuitable, isComboBoxDatasetNotNull, isRadiusNumSuitable, isHasResultDataset);
//		}
//	}
}
