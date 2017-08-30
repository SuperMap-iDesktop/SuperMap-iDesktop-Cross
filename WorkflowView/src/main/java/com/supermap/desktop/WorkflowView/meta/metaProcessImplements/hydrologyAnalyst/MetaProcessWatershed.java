package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.hydrologyAnalyst;

import com.supermap.analyst.terrainanalyst.HydrologyAnalyst;
import com.supermap.data.*;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.EqualDatasetConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.utilities.DatasetUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created By Chens on 2017/8/29 0029
 */
public class MetaProcessWatershed extends MetaProcessHydrology {
	private static final String POUR_POINTS_DATA = ProcessProperties.getString("String_GroupBox_PourPointsData");

	private ParameterDatasourceConstrained pointsDatasource;
	private ParameterSingleDataset pointsDataset;
	private ParameterTextArea textAreaSQL;
	private ParameterSQLExpression buttonSQL;

	@Override
	protected void initField() {
		INPUT_DATA = ProcessProperties.getString("String_GroupBox_FlowDirectionData");
		OUTPUT_DATA = "WatershedResult";
	}

	@Override
	protected void initParaComponent() {
		pointsDatasource = new ParameterDatasourceConstrained();
		pointsDataset = new ParameterSingleDataset(DatasetType.GRID, DatasetType.POINT);
		textAreaSQL = new ParameterTextArea(ProcessProperties.getString("String_ExpressionForTextArea"));
		buttonSQL = new ParameterSQLExpression(ControlsProperties.getString("String_SQLExpression"));

		EqualDatasourceConstraint constraintSource1 = new EqualDatasourceConstraint();
		constraintSource1.constrained(pointsDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		constraintSource1.constrained(pointsDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
		EqualDatasetConstraint datasetConstraintSource = new EqualDatasetConstraint();
		datasetConstraintSource.constrained(sourceDataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		datasetConstraintSource.constrained(buttonSQL, ParameterSQLExpression.DATASET_FIELD_NAME);

		ParameterCombine weightCombine = new ParameterCombine();
		weightCombine.setDescribe(POUR_POINTS_DATA);
		weightCombine.addParameters(pointsDatasource, pointsDataset);
		ParameterCombine settingCombine = new ParameterCombine();
		settingCombine.setDescribe(ProcessProperties.getString("String_setParameter"));
		ParameterCombine emptyCombine = new ParameterCombine(ParameterCombine.HORIZONTAL);
		emptyCombine.addParameters(new ParameterLabel(), new ParameterLabel(), buttonSQL);
		settingCombine.addParameters(textAreaSQL, emptyCombine);

		Dataset dataset = DatasetUtilities.getDefaultDataset(DatasetType.GRID,DatasetType.POINT);
		if (dataset != null) {
			pointsDatasource.setSelectedItem(dataset.getDatasource());
			pointsDataset.setSelectedItem(dataset);
			if (dataset instanceof DatasetGrid) {
				textAreaSQL.setEnabled(false);
				buttonSQL.setEnabled(false);
			} else {
				buttonSQL.setSelectDataset(dataset);
			}
		}
		pointsDataset.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (pointsDataset.getSelectedItem() != null && evt.getNewValue() instanceof DatasetGrid) {
					textAreaSQL.setEnabled(false);
					buttonSQL.setEnabled(false);
				} else {
					buttonSQL.setSelectDataset((Dataset) evt.getNewValue());
				}
			}
		});
		buttonSQL.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (null != evt.getNewValue()) {
					textAreaSQL.setSelectedItem(buttonSQL.getSelectedItem());
				}
			}
		});

		parameters.setParameters(sourceCombine, weightCombine, resultCombine);
		parameters.addInputParameters(POUR_POINTS_DATA, new DatasetTypes("",DatasetTypes.GRID.getValue()|DatasetTypes.POINT.getValue()), weightCombine);
	}

	@Override
	protected String getResultName() {
		return "result_watershed";
	}

	@Override
	protected String getOutputText() {
		return ProcessOutputResultProperties.getString("String_Result_Watershed");
	}

	@Override
	protected Dataset doWork(DatasetGrid src) {
		Dataset srcPourPoints = null;
		if (parameters.getInputs().getData(POUR_POINTS_DATA).getValue() != null) {
			srcPourPoints = (Dataset) parameters.getInputs().getData(POUR_POINTS_DATA).getValue();
		} else if (pointsDataset.getSelectedItem() != null) {
			srcPourPoints = pointsDataset.getSelectedItem();
		}

		DatasetGrid result = null;
		if (srcPourPoints instanceof DatasetGrid) {
			result= HydrologyAnalyst.watershed(src, (DatasetGrid) srcPourPoints, resultDataset.getResultDatasource(),
					resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(resultDataset.getDatasetName()));
		} else {
			Recordset recordset;
			if (textAreaSQL.getSelectedItem() != null || !textAreaSQL.isEnabled) {
				recordset = ((DatasetVector)srcPourPoints).query(textAreaSQL.getSelectedItem().toString(), CursorType.STATIC);
			} else {
				recordset = ((DatasetVector)srcPourPoints).getRecordset(false, CursorType.STATIC);
			}
			Point2Ds point2Ds = new Point2Ds();
			while (!recordset.isEOF()) {
				GeoPoint geoPoint = (GeoPoint) recordset.getGeometry();
				point2Ds.add(new Point2D(geoPoint.getX(),geoPoint.getY()));
				geoPoint.dispose();
				recordset.moveNext();
			}
			recordset.close();
			recordset.dispose();

			result= HydrologyAnalyst.watershed(src, point2Ds, resultDataset.getResultDatasource(),
					resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(resultDataset.getDatasetName()));
		}

		return result;
	}

	@Override
	public IParameters getParameters() {
		return super.getParameters();
	}

	@Override
	public String getKey() {
		return MetaKeys.WATERSHED;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Title_Watershed");
	}
}
