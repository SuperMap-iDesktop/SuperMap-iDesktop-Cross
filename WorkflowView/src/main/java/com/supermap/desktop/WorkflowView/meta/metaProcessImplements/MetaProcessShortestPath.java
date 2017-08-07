package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetType;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.DatasourceConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

/**
 * Created by lixiaoyao on 2017/8/7.
 */
public class MetaProcessShortestPath extends MetaProcess {
	private final static String INPUT_DATA = CommonProperties.getString("String_GroupBox_SourceData");
	private final static String DISTANCE_DATA = CommonProperties.getString("String_GroupBox_DistanceData");
	private final static String DIRECTION_DATA = CommonProperties.getString("String_GroupBox_DirectionData");
	private final static String OUTPUT_DATA = "ShortestPathResult";

	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset sourceDataset;
	private ParameterDatasourceConstrained distanceDatasource;
	private ParameterSingleDataset distanceDataset;
	private ParameterDatasourceConstrained directionDatasource;
	private ParameterSingleDataset directionDataset;
	private ParameterRadioButton parameterRadioButton;
	private ParameterDataNode parameterDataNodeCell;
	private ParameterDataNode parameterDataNodeAll;
	private ParameterDataNode parameterDataNodeZone;
	private ParameterSaveDataset resultDataset;

	public MetaProcessShortestPath(){
		initParameters();
		initParametersState();
		initParameterConstrint();
		//registerListener();
	}

	private void initParameters(){
		this.sourceDatasource = new ParameterDatasourceConstrained();
		this.sourceDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		this.sourceDataset = new ParameterSingleDataset(DatasetType.GRID,DatasetType.POINT, DatasetType.LINE, DatasetType.REGION);
		this.sourceDataset.setDescribe(CommonProperties.getString("String_Label_Dataset"));
		this.distanceDatasource=new ParameterDatasourceConstrained();
		this.distanceDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		this.distanceDataset = new ParameterSingleDataset(DatasetType.GRID);
		this.distanceDataset.setDescribe(CommonProperties.getString("String_Label_Dataset"));
		this.directionDatasource=new ParameterDatasourceConstrained();
		this.directionDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		this.directionDataset = new ParameterSingleDataset(DatasetType.GRID);
		this.directionDataset.setDescribe(CommonProperties.getString("String_Label_Dataset"));
		this.parameterRadioButton=new ParameterRadioButton();
		this.parameterDataNodeCell=new ParameterDataNode(ProcessProperties.getString("String_ComputeType_Cell"),null);
		this.parameterDataNodeAll=new ParameterDataNode(ProcessProperties.getString("String_ComputeType_All"),null);
		this.parameterDataNodeZone=new ParameterDataNode(ProcessProperties.getString("String_ComputeType_Zone"),null);
		this.parameterRadioButton.setItems(new ParameterDataNode[]{parameterDataNodeCell, parameterDataNodeAll, parameterDataNodeZone});
		this.resultDataset = new ParameterSaveDataset();
		this.resultDataset.setDatasourceDescribe(CommonProperties.getString("String_TargetDatasource"));
		this.resultDataset.setDatasetDescribe(CommonProperties.getString("String_TargetDataset"));

		ParameterCombine sourceData = new ParameterCombine();
		sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		sourceData.addParameters(this.sourceDatasource, this.sourceDataset);
		ParameterCombine distanceData=new ParameterCombine();
		distanceData.setDescribe(ProcessProperties.getString("String_GroupBox_DistanceData"));
		distanceData.addParameters(this.distanceDatasource,this.distanceDataset);
		ParameterCombine directionData=new ParameterCombine();
		directionData.setDescribe(ProcessProperties.getString("String_GroupBox_DirectionData"));
		directionData.addParameters(this.directionDatasource,this.directionDataset);
		ParameterCombine computeType=new ParameterCombine();
		computeType.setDescribe(ProcessProperties.getString("String_ComputeType"));
		computeType.addParameters(this.parameterRadioButton);
		ParameterCombine targetData = new ParameterCombine();
		targetData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		targetData.addParameters(this.resultDataset);

		this.parameters.setParameters(sourceData,directionData,directionData,computeType,targetData);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.SIMPLE_VECTOR_AND_GRID, sourceData);
		this.parameters.addInputParameters(DISTANCE_DATA, DatasetTypes.GRID, distanceData);
		this.parameters.addInputParameters(DIRECTION_DATA, DatasetTypes.GRID, directionData);
		this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.GRID, targetData);
	}

	private void initParametersState() {
		DatasetGrid datasetGrid = DatasetUtilities.getDefaultDatasetGrid();
		if (datasetGrid!=null){

		}
	}

	private void initParameterConstrint(){
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(this.sourceDatasource, ParameterDatasourceConstrained.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(this.sourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		DatasourceConstraint.getInstance().constrained(this.resultDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint distanceConstraint = new EqualDatasourceConstraint();
		distanceConstraint.constrained(this.distanceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		distanceConstraint.constrained(this.distanceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint directionConstraint = new EqualDatasourceConstraint();
		directionConstraint.constrained(this.directionDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		directionConstraint.constrained(this.directionDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		return isSuccessful;
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	@Override
	public String getKey() {
		return MetaKeys.SHORTEST_PATH;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Calculator_ShortestPath");
	}
}
