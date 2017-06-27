package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.Type;
import com.supermap.desktop.ui.lbs.Interface.IServerService;

/**
 * Created by caolp on 2017-05-26.
 * 多边形聚合分析
 */
public class MetaProcessPolygonAggregation extends MetaProcess {
	private ParameterIServerLogin parameterIServerLogin = new ParameterIServerLogin();
	private ParameterHDFSPath parameterHDFSPath = new ParameterHDFSPath();
	private ParameterTextField parameterTextFieldXIndex = new ParameterTextField(ProcessProperties.getString("String_XIndex"));
	private ParameterTextField parameterTextFieldYIndex = new ParameterTextField(ProcessProperties.getString("String_YIndex"));
	private ParameterTextField parameterTextFieldSeparator = new ParameterTextField(ProcessProperties.getString("String_Separator"));
	private ParameterComboBox parameterAggregationType = new ParameterComboBox().setDescribe(ProcessProperties.getString("String_AggregationType"));
    private ParameterBigDatasourceDatasource parameterBigDatasourceDatasource = new ParameterBigDatasourceDatasource();
	private ParameterSingleDataset parameterSingleDataset = new ParameterSingleDataset();
	private ParameterTextField parameterStaticModel = new ParameterTextField().setDescribe(ProcessProperties.getString("String_StaticModel"));
	private ParameterTextField parameterWeightIndex = new ParameterTextField().setDescribe(ProcessProperties.getString("String_Index"));

	public MetaProcessPolygonAggregation() {
		initComponents();
		initComponentLayout();
		initConstraint();
	}

   private void initComponents(){
	   parameterHDFSPath.setSelectedItem("hdfs://192.168.20.189:9000/data/newyork_taxi_2013-01_14k.csv");
	   parameterTextFieldXIndex.setSelectedItem("10");
	   parameterTextFieldYIndex.setSelectedItem("11");
	   parameterTextFieldSeparator.setSelectedItem(",");
	   parameterTextFieldSeparator.setEnabled(false);

	   ParameterDataNode parameterDataNode = new ParameterDataNode(ProcessProperties.getString("String_PolygonAggregationType"), "SummaryRegionMain ");
	   parameterAggregationType.setItems(parameterDataNode);
	   parameterAggregationType.setSelectedItem(parameterDataNode);

	   parameterBigDatasourceDatasource.setDescribe(ProcessProperties.getString("String_BigDataSource"));
	   parameterSingleDataset.setDescribe(ProcessProperties.getString("String_AggregateDataset"));

	   parameterStaticModel.setSelectedItem("max");
	   parameterWeightIndex.setSelectedItem("7");
   }

   private void initComponentLayout(){
	   ParameterCombine parameterCombineSetting = new ParameterCombine();
	   parameterCombineSetting.setDescribe(ProcessProperties.getString("String_setParameter"));
	   parameterCombineSetting.addParameters(
			   parameterHDFSPath,
			   parameterTextFieldXIndex,
			   parameterTextFieldYIndex,
			   parameterTextFieldSeparator,
			   parameterAggregationType,
			   parameterBigDatasourceDatasource,
			   parameterSingleDataset,
			   parameterStaticModel,
			   parameterWeightIndex);
	   parameters.setParameters(
			   parameterIServerLogin,
			   parameterCombineSetting
	   );
	   parameters.getOutputs().addData("PolygonAggregationResult", Type.UNKOWN);
   }

   private void initConstraint(){
	   EqualDatasourceConstraint equalSourceDatasource = new EqualDatasourceConstraint();
	   equalSourceDatasource.constrained(parameterBigDatasourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
	   equalSourceDatasource.constrained(parameterSingleDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
   }




	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_PolygonAggregation");
	}

	@Override
	public IParameterPanel getComponent() {
		return this.parameters.getPanel();
	}

	@Override
	public boolean execute() {
		try {
			fireRunning(new RunningEvent(this, 0, "start"));
			IServerService service = parameterIServerLogin.login();
//			BigDataParameterSetting bigDataParameterSetting = new BigDataParameterSetting();
//			bigDataParameterSetting.filePath = parameterHDFSPath.getSelectedItem().toString();
//			bigDataParameterSetting.xIndex = parameterTextFieldXIndex.getSelectedItem().toString();
//			bigDataParameterSetting.yIndex = parameterTextFieldYIndex.getSelectedItem().toString();
//			bigDataParameterSetting.separator = parameterTextFieldSeparator.getSelectedItem().toString();

		} catch (Exception e) {
			e.printStackTrace();
		}













		return true;
	}

	@Override
	public String getKey() {
		return MetaKeys.POLYGON_AGGREGATION;
	}
}
