package com.supermap.desktop.CtrlAction.transformationForm.CtrlAction;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetImage;
import com.supermap.data.DatasetVector;
import com.supermap.data.SteppedEvent;
import com.supermap.data.SteppedListener;
import com.supermap.data.Transformation;
import com.supermap.data.TransformationMode;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.transformationForm.beans.TransformationAddObjectBean;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.utilities.DoubleUtilities;

import java.text.MessageFormat;
import java.util.concurrent.CancellationException;

/**
 * @author XiaJT
 */
public class TransformCallable extends UpdateProgressCallable {

	private boolean isCancel = false;
	private TransformationAddObjectBean[] transformationAddObjectBeans;
	private Transformation transformation;
	private int totleSize = 0;
	private int finishedCount;

	public TransformCallable(Transformation transformation, TransformationAddObjectBean[] transformationAddObjectBeans) {
		this.transformation = transformation;
		this.transformationAddObjectBeans = transformationAddObjectBeans;
		if (transformationAddObjectBeans.length == 0) {
			totleSize = 1;
		} else {
			totleSize = transformationAddObjectBeans.length;
		}
	}

	@Override
	public Boolean call() throws Exception {
		TransformationMode transformMode = transformation.getTransformMode();
		if (transformMode == TransformationMode.LINEAR) {
			Application.getActiveApplication().getOutput().output(DataEditorProperties.getString("String_LinearTransformEquationX"));
			Application.getActiveApplication().getOutput().output(DataEditorProperties.getString("String_LinearTransformEquationY"));
			String transformEquation = transformation.getTransformEquation();
			String[] equations = transformEquation.split(",");
			String xValue = MessageFormat.format(DataEditorProperties.getString("String_LinearTransformEquationXValue"), getEquationNumber(equations[0]), getEquationNumber(equations[1]), getEquationNumber(equations[2]));
			String yValue = MessageFormat.format(DataEditorProperties.getString("String_LinearTransformEquationYValue"), getEquationNumber(equations[3]), getEquationNumber(equations[4]), getEquationNumber(equations[5]));
			Application.getActiveApplication().getOutput().output(xValue);
			Application.getActiveApplication().getOutput().output(yValue);
		} else if (transformMode == TransformationMode.SQUARE) {
			Application.getActiveApplication().getOutput().output(DataEditorProperties.getString("String_SquareTransformEquationX"));
			Application.getActiveApplication().getOutput().output(DataEditorProperties.getString("String_SquareTransformEquationY"));
			String transformEquation = transformation.getTransformEquation();
			String[] equations = transformEquation.split(",");
			String xValue = MessageFormat.format(DataEditorProperties.getString("String_SquareTransformEquationXValue"), getEquationNumber(equations[0]), getEquationNumber(equations[1]), getEquationNumber(equations[2]), getEquationNumber(equations[3]), getEquationNumber(equations[4]), getEquationNumber(equations[5]));
			String yValue = MessageFormat.format(DataEditorProperties.getString("String_SquareTransformEquationYValue"), getEquationNumber(equations[6]), getEquationNumber(equations[7]), getEquationNumber(equations[8]), getEquationNumber(equations[9]), getEquationNumber(equations[10]), getEquationNumber(equations[11]));
			Application.getActiveApplication().getOutput().output(xValue);
			Application.getActiveApplication().getOutput().output(yValue);
		}
		SteppedListener steppedListener = new SteppedListener() {
			@Override
			public void stepped(SteppedEvent arg0) {
				int totalPercent = (arg0.getPercent() + 100 * finishedCount) / totleSize;
				try {
					updateProgressTotal(arg0.getPercent(), totalPercent, String.valueOf(arg0.getRemainTime()), arg0.getMessage());
				} catch (CancellationException e) {
					isCancel = true;
					arg0.setCancel(true);
				}
			}
		};
		transformation.addSteppedListener(steppedListener);

		String resultDatasetName = null;
		for (TransformationAddObjectBean transformationAddObjectBean : transformationAddObjectBeans) {
			if (isCancel) {
				finishedCount++;
				continue;
			}
			try {
				Dataset dataset = transformationAddObjectBean.getDataset();
				if (dataset instanceof DatasetImage || dataset instanceof DatasetGrid) {
					if (transformationAddObjectBean.isSaveAs()) {
						if (transformationAddObjectBean.isResample()) {
							Dataset rectify = transformation.rectify(dataset, transformationAddObjectBean.getResultDatasource(),
									transformationAddObjectBean.getResultDatasource().getDatasets().getAvailableDatasetName(transformationAddObjectBean.getResultDatasetName()), transformationAddObjectBean.getTransformationResampleMode(), transformationAddObjectBean.getCellSize());
							resultDatasetName = rectify != null ? rectify.getName() + "@" + rectify.getDatasource().getAlias() : null;
						} else {
							Dataset rectify = transformation.rectify(dataset, transformationAddObjectBean.getResultDatasource(), transformationAddObjectBean.getResultDatasource().getDatasets().getAvailableDatasetName(transformationAddObjectBean.getResultDatasetName()));//,transformationAddObjectBean.getTransformationResampleMode(), transformationAddObjectBean.getCellSize()
							resultDatasetName = rectify != null ? rectify.getName() + "@" + rectify.getDatasource().getAlias() : null;
						}
					} else {
						transformation.rectify(dataset);
						resultDatasetName = null;
					}
				} else {
					if (transformationAddObjectBean.isSaveAs()) {
						DatasetVector transform = transformation.transform(((DatasetVector) dataset), transformationAddObjectBean.getResultDatasource(), transformationAddObjectBean.getResultDatasource().getDatasets().getAvailableDatasetName(transformationAddObjectBean.getResultDatasetName()));
						resultDatasetName = transform != null ? transform.getName() + "@" + transform.getDatasource().getAlias() : null;
					} else {
						transformation.transform(((DatasetVector) dataset));
						resultDatasetName = null;
					}
				}

				if (resultDatasetName == null) {
					Application.getActiveApplication().getOutput().output(MessageFormat.format(DataEditorProperties.getString("String_DatasetTransformationSuccess"), dataset.getName()));
				} else {
					String s = MessageFormat.format(DataEditorProperties.getString("String_DatasetTransformationResult"), dataset.getName(), resultDatasetName);
					Application.getActiveApplication().getOutput().output(s);
				}

			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			} finally {
				finishedCount++;
			}
		}
		transformation.removeSteppedListener(steppedListener);
		return null;
	}

	private String getEquationNumber(String equation) {
		Double aDouble = Double.valueOf(equation);
		return DoubleUtilities.toMaxLengthString(aDouble, 8);
	}
}
