package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.gridDistance;

import com.supermap.analyst.spatialanalyst.DistanceAnalyst;
import com.supermap.analyst.spatialanalyst.DistanceAnalystParameter;
import com.supermap.analyst.spatialanalyst.PathLineResult;
import com.supermap.analyst.spatialanalyst.SmoothMethod;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.DatasourceConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by yuanR on 2017/8/7.
 * 两点间最短地表路径
 */
public class MetaProcessSurfacePathLine extends MetaProcess {

	private final static String INPUT_DATA = CommonProperties.getString("String_GroupBox_SourceData");
	private final static String OUTPUT_DATA = "DEMPathLine";

	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset sourceDataset;

	// 原点和目标点的坐标值
	private ParameterNumber parameterSourcePointX;
	private ParameterNumber parameterSourcePointY;
	private ParameterNumber parameterTargetPointX;
	private ParameterNumber parameterTargetPointY;

	// 复制，粘贴按钮
	private ParameterButton parameterButtonCopy;
	private ParameterButton parameterButtonPaste;
	//  光滑方法
	private ParameterComboBox parameterPathLineSmoothMethod;
	// 光滑系数
	private ParameterNumber parameterPathLineSmoothDegree;
	// 最大上坡角度
	private ParameterNumber parameterMaxUpslopeDegree;
	// 最大下坡角度
	private ParameterNumber parameterMaxDownslopeDegree;

	private ParameterSaveDataset resultDataset;

	/**
	 * 复制和粘贴的监听响应事件
	 */
	public ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals(parameterButtonCopy.getDescribe())) {
				copyParameter();
			} else if (e.getActionCommand().equals(parameterButtonPaste.getDescribe())) {
				pasteParameter();
			}
		}
	};

	public MetaProcessSurfacePathLine() {
		initParameters();
		initParametersState();
		initParameterConstrint();
	}

	private void initParameters() {
		// 源数据
		this.sourceDatasource = new ParameterDatasourceConstrained();
		this.sourceDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		this.sourceDataset = new ParameterSingleDataset(DatasetType.GRID);
		this.sourceDataset.setDescribe(CommonProperties.getString("String_Label_Dataset"));

		ParameterCombine sourceData = new ParameterCombine();
		sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		sourceData.addParameters(this.sourceDatasource, this.sourceDataset);

		// 参数设置
		// 原点和目标点坐标值
		parameterSourcePointX = new ParameterNumber(ProcessProperties.getString("String_SourcePointX"));
		parameterSourcePointY = new ParameterNumber(ProcessProperties.getString("String_SourcePointY"));
		parameterTargetPointX = new ParameterNumber(ProcessProperties.getString("String_TargetPointX"));
		parameterTargetPointY = new ParameterNumber(ProcessProperties.getString("String_TargetPointY"));

		parameterSourcePointX.setToolTip(ProcessProperties.getString("String_SourcePointX").replace(":", ProcessProperties.getString("String_ZonalStatistic_ValueData")));
		parameterSourcePointY.setToolTip(ProcessProperties.getString("String_SourcePointY").replace(":", ProcessProperties.getString("String_ZonalStatistic_ValueData")));
		parameterTargetPointX.setToolTip(ProcessProperties.getString("String_TargetPointX").replace(":", ProcessProperties.getString("String_ZonalStatistic_ValueData")));
		parameterTargetPointY.setToolTip(ProcessProperties.getString("String_TargetPointY").replace(":", ProcessProperties.getString("String_ZonalStatistic_ValueData")));

		parameterSourcePointX.setRequisite(true);
		parameterSourcePointY.setRequisite(true);
		parameterTargetPointX.setRequisite(true);
		parameterTargetPointY.setRequisite(true);

		// 复制、粘贴
		parameterButtonCopy = new ParameterButton(ControlsProperties.getString("String_Copy"));
		parameterButtonPaste = new ParameterButton(ControlsProperties.getString("String_Paste"));

		parameterButtonCopy.setActionListener(actionListener);
		parameterButtonPaste.setActionListener(actionListener);
		ParameterCombine parameterCombineCopyPaste = new ParameterCombine(ParameterCombine.HORIZONTAL);
		parameterCombineCopyPaste.addParameters(new ParameterCombine(), parameterButtonCopy, parameterButtonPaste);
		parameterCombineCopyPaste.setWeightIndex(0);

		// 光滑方式
		parameterPathLineSmoothMethod = new ParameterComboBox(CommonProperties.getString("String_SmoothMethod"));
		parameterPathLineSmoothMethod.setItems(new ParameterDataNode(CommonProperties.getString("String_SmoothMethod_NONE"), SmoothMethod.NONE),
				new ParameterDataNode(CommonProperties.getString("String_SmoothMethod_BSLine"), SmoothMethod.BSPLINE),
				new ParameterDataNode(CommonProperties.getString("String_SmoothMethod_POLISH"), SmoothMethod.POLISH));
		// 光滑系数
		parameterPathLineSmoothDegree = new ParameterNumber(CommonProperties.getString("String_Smooth"));
		parameterPathLineSmoothDegree.setSelectedItem("2");
		parameterPathLineSmoothDegree.setMinValue(2);
		parameterPathLineSmoothDegree.setMaxValue(10);
		parameterPathLineSmoothDegree.setMaxBit(0);
		parameterPathLineSmoothDegree.setIncludeMax(true);
		parameterPathLineSmoothDegree.setIsIncludeMin(true);

		// 最大上坡角度
		parameterMaxUpslopeDegree = new ParameterNumber(ProcessProperties.getString("String_Label_MaxUpslopeDegree"));
		parameterMaxUpslopeDegree.setSelectedItem("90");
		parameterMaxUpslopeDegree.setMinValue(0);
		parameterMaxUpslopeDegree.setMaxValue(90);
		parameterMaxUpslopeDegree.setMaxBit(22);
		parameterMaxUpslopeDegree.setIncludeMax(true);
		parameterMaxUpslopeDegree.setIsIncludeMin(true);
		// 最大下坡角度
		parameterMaxDownslopeDegree = new ParameterNumber(ProcessProperties.getString("String_Label_MaxDownslopeDegree"));
		parameterMaxDownslopeDegree.setSelectedItem("90");
		parameterMaxDownslopeDegree.setMinValue(0);
		parameterMaxDownslopeDegree.setMaxValue(90);
		parameterMaxDownslopeDegree.setMaxBit(22);
		parameterMaxDownslopeDegree.setIncludeMax(true);
		parameterMaxDownslopeDegree.setIsIncludeMin(true);

		ParameterCombine parameterCombineSet = new ParameterCombine();
		parameterCombineSet.setDescribe(ProcessProperties.getString("String_setParameter"));
		parameterCombineSet.addParameters(
				parameterSourcePointX, parameterSourcePointY,
				parameterTargetPointX, parameterTargetPointY,
				parameterCombineCopyPaste,
				parameterPathLineSmoothMethod, parameterPathLineSmoothDegree,
				parameterMaxUpslopeDegree, parameterMaxDownslopeDegree
		);


		// 结果设置
		this.resultDataset = new ParameterSaveDataset();

		ParameterCombine resultData = new ParameterCombine();
		resultData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		resultData.addParameters(this.resultDataset);

		this.parameters.setParameters(sourceData, parameterCombineSet, resultData);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.GRID, sourceData);
		this.parameters.addOutputParameters(OUTPUT_DATA,
				ProcessOutputResultProperties.getString("String_ShortestPathLineResult"),
				DatasetTypes.LINE, resultData);

	}

	private void initParametersState() {
		DatasetGrid datasetGrid = DatasetUtilities.getDefaultDatasetGrid();
		if (datasetGrid != null) {
			sourceDatasource.setSelectedItem(datasetGrid.getDatasource());
			sourceDataset.setSelectedItem(datasetGrid);
			resultDataset.setResultDatasource(datasetGrid.getDatasource());
			resultDataset.setSelectedItem(datasetGrid.getDatasource().getDatasets().getAvailableDatasetName("result_DEMPastLine"));
		}
		parameterPathLineSmoothDegree.setEnabled(false);
	}

	private void initParameterConstrint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(this.sourceDatasource, ParameterDatasourceConstrained.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(this.sourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		DatasourceConstraint.getInstance().constrained(this.resultDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);

		parameterPathLineSmoothMethod.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (parameterPathLineSmoothMethod.getSelectedData().equals(SmoothMethod.NONE)) {
					parameterPathLineSmoothDegree.setEnabled(false);
				} else {
					parameterPathLineSmoothDegree.setEnabled(true);
				}
			}
		});
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		PathLineResult pathLineResult = null;
		DatasetGrid datasetGrid = null;
		Recordset resultRecordset = null;
		String resultDatasetName = null;
		try {

			if (this.getParameters().getInputs().getData(INPUT_DATA) != null
					&& this.getParameters().getInputs().getData(INPUT_DATA).getValue() instanceof DatasetGrid) {
				datasetGrid = (DatasetGrid) this.getParameters().getInputs().getData(INPUT_DATA).getValue();
			} else {
				datasetGrid = (DatasetGrid) sourceDataset.getSelectedItem();
			}
			// 原点以及目标点坐标值参数
			Point2D sourcePoint = new Point2D();
			Point2D targetPoint = new Point2D();
			sourcePoint.setX(Double.valueOf((String) parameterSourcePointX.getSelectedItem()));
			sourcePoint.setY(Double.valueOf((String) parameterSourcePointY.getSelectedItem()));
			targetPoint.setX(Double.valueOf((String) parameterTargetPointX.getSelectedItem()));
			targetPoint.setY(Double.valueOf((String) parameterTargetPointY.getSelectedItem()));

			// 其他参数
			DistanceAnalystParameter distanceAnalystParameter = new DistanceAnalystParameter();
			distanceAnalystParameter.setSurfaceGrid(datasetGrid);
			distanceAnalystParameter.setPathLineSmoothMethod((SmoothMethod) parameterPathLineSmoothMethod.getSelectedData());
			distanceAnalystParameter.setPathLineSmoothDegree(Integer.valueOf((String) parameterPathLineSmoothDegree.getSelectedItem()));
			distanceAnalystParameter.setMaxUpslopeDegree(Double.valueOf((String) parameterMaxUpslopeDegree.getSelectedItem()));
			distanceAnalystParameter.setMaxUpslopeDegree(Double.valueOf((String) parameterMaxDownslopeDegree.getSelectedItem()));

			// run
			DistanceAnalyst.addSteppedListener(this.steppedListener);
			pathLineResult = DistanceAnalyst.surfacePathLine(sourcePoint, targetPoint, distanceAnalystParameter);

			if (pathLineResult != null) {
				// 创建新的线数据集
				resultDatasetName = this.resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(this.resultDataset.getDatasetName());
				DatasetVectorInfo datasetVectorInfo = new DatasetVectorInfo();
				datasetVectorInfo.setName(resultDatasetName);
				datasetVectorInfo.setType(DatasetType.LINE);
				DatasetVector resultDataset = this.resultDataset.getResultDatasource().getDatasets().create(datasetVectorInfo);
				resultDataset.setPrjCoordSys(datasetGrid.getPrjCoordSys());
				// 给新建的数据集中添加分析出的结果
				resultRecordset = resultDataset.getRecordset(false, CursorType.DYNAMIC);
				resultRecordset.addNew(pathLineResult.getPathLine());
				resultRecordset.update();

				this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(resultDataset);
				isSuccessful = resultDataset != null;
			} else {
				isSuccessful = false;
			}

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
			// 当结果数据集不为空，并且tree中存在结果数据集时，进行删除操作
			if (!StringUtilities.isNullOrEmpty(resultDatasetName) &&
					!this.resultDataset.getResultDatasource().getDatasets().isAvailableDatasetName(resultDatasetName)) {
				this.resultDataset.getResultDatasource().getDatasets().delete(resultDatasetName);
			}
		} finally {
			resultRecordset.close();
			resultRecordset.dispose();
			DistanceAnalyst.removeSteppedListener(this.steppedListener);
		}
		return isSuccessful;
	}

	/**
	 * 粘贴参数
	 * 从系统粘贴板获得最新的文本，当文本内容符合范围条件时，才进行粘贴，当不符合时，不做任何操作
	 */
	private void pasteParameter() {
		String clipBoard = getSysClipboardText();

		String labelSourcePointX = ControlsProperties.getString("String_SourcePointX");
		String labelSourcePointY = ControlsProperties.getString("String_SourcePointY");
		String labelTargetPointX = ControlsProperties.getString("String_TargetPointX");
		String labelTargetPointY = ControlsProperties.getString("String_TargetPointY");

		if (clipBoard.contains(labelSourcePointX) && clipBoard.contains(labelSourcePointY) && clipBoard.contains(labelTargetPointX) && clipBoard.contains(labelTargetPointY)) {
			String sourceX = clipBoard.substring(clipBoard.indexOf(ControlsProperties.getString("String_SourcePointX")), clipBoard.indexOf(ControlsProperties.getString("String_SourcePointY")));
			String sourceY = clipBoard.substring(clipBoard.indexOf(ControlsProperties.getString("String_SourcePointY")), clipBoard.indexOf(ControlsProperties.getString("String_TargetPointX")));
			String targetX = clipBoard.substring(clipBoard.indexOf(ControlsProperties.getString("String_TargetPointX")), clipBoard.indexOf(ControlsProperties.getString("String_TargetPointY")));
			String targetY = clipBoard.substring(clipBoard.indexOf(ControlsProperties.getString("String_TargetPointY")));

			sourceX = (sourceX.replace(labelSourcePointX, "")).replace(",", "");
			sourceY = (sourceY.replace(labelSourcePointY, "")).replace(",", "");
			targetX = (targetX.replace(labelTargetPointX, "")).replace(",", "");
			targetY = (targetY.replace(labelTargetPointY, "")).replace(",", "");

			if (StringUtilities.isNumber(sourceX) && StringUtilities.isNumber(sourceY) && StringUtilities.isNumber(targetX) && StringUtilities.isNumber(targetY)) {
				this.parameterSourcePointX.setSelectedItem(sourceX);
				this.parameterSourcePointY.setSelectedItem(sourceY);
				this.parameterTargetPointX.setSelectedItem(targetX);
				this.parameterTargetPointY.setSelectedItem(targetY);
			}
		}
	}

	/**
	 * 调用windows的剪贴板
	 * 获得系统剪贴板内
	 *
	 * @return
	 */
	public static String getSysClipboardText() {
		Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable clipTf = sysClip.getContents(null);
		if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			try {
				String ret = (String) clipTf.getTransferData(DataFlavor.stringFlavor);
				return ret;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 复制参数
	 */
	private void copyParameter() {
		if ((!StringUtilities.isNullOrEmpty((String) this.parameterSourcePointX.getSelectedItem()))
				&& (!StringUtilities.isNullOrEmpty((String) this.parameterSourcePointY.getSelectedItem()))
				&& (!StringUtilities.isNullOrEmpty((String) this.parameterTargetPointX.getSelectedItem()))
				&& (!StringUtilities.isNullOrEmpty((String) this.parameterTargetPointY.getSelectedItem()))) {
			String clipBoardTextSourcePointX = ControlsProperties.getString("String_SourcePointX") + (String) this.parameterSourcePointX.getSelectedItem();
			String clipBoardTextSourcePointY = ControlsProperties.getString("String_SourcePointY") + (String) this.parameterSourcePointY.getSelectedItem();
			String clipBoardTextTargetPointX = ControlsProperties.getString("String_TargetPointX") + (String) this.parameterTargetPointX.getSelectedItem();
			String clipBoardTextTargetPointY = ControlsProperties.getString("String_TargetPointY") + (String) this.parameterTargetPointY.getSelectedItem();
			// 调用windows复制功能，将值复制到剪贴板
			setSysClipboardText(clipBoardTextSourcePointX + "," + clipBoardTextSourcePointY + "," + clipBoardTextTargetPointX + "," + clipBoardTextTargetPointY);
			Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_MapBounds_Has_Been_Copied"));
		}
	}

	/**
	 * 调用windows的剪贴板
	 *
	 * @param coypText
	 */
	public static void setSysClipboardText(String coypText) {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable Text = new StringSelection(coypText);
		clip.setContents(Text, null);
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	@Override
	public String getKey() {
		return MetaKeys.SURFACE_PATH_LINE;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_SurfacePathLine");
	}
}
