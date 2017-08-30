package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.spatialStatistics;

import com.supermap.analyst.spatialstatistics.ConceptualizationModel;
import com.supermap.analyst.spatialstatistics.WeightsUtilities;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.ipls.ParameterCombine;
import com.supermap.desktop.process.parameter.ipls.ParameterFile;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.SmFileChoose;
import java.io.File;

/**
 * Created by yuanR on 2017/8/29 0029.
 * 生成空间权重矩阵文件
 */
public class MetaProcessCreateSpatialWeightMatrixFile extends MetaProcessAnalyzingPatterns {
	private ParameterFile parameterFile;

	@Override
	protected void initHook() {
		dataset.setDatasetTypes(DatasetType.REGION, DatasetType.POINT, DatasetType.LINE);
		// 生成空间权重矩阵文件功能提出空间权重矩阵文件模型的选项
		parameterPatternsParameter.getParameterComboBoxConceptModel().removeAllItems();
		parameterPatternsParameter.getParameterComboBoxConceptModel().setItems(
				new ParameterDataNode(ProcessProperties.getString("String_FIXEDDISTANCEBAND"), ConceptualizationModel.FIXEDDISTANCEBAND),
				new ParameterDataNode(ProcessProperties.getString("String_CONTIGUITYEDGESNODE"), ConceptualizationModel.CONTIGUITYEDGESNODE),
				new ParameterDataNode(ProcessProperties.getString("String_CONTIGUITYEDGESONLY"), ConceptualizationModel.CONTIGUITYEDGESONLY),
				new ParameterDataNode(ProcessProperties.getString("String_INVERSEDISTANCE"), ConceptualizationModel.INVERSEDISTANCE),
				new ParameterDataNode(ProcessProperties.getString("String_INVERSEDISTANCESQUARED"), ConceptualizationModel.INVERSEDISTANCESQUARED),
				new ParameterDataNode(ProcessProperties.getString("String_KNEARESTNEIGHBORS"), ConceptualizationModel.KNEARESTNEIGHBORS),
//				new ParameterDataNode(ProcessProperties.getString("String_SPATIALWEIGHTMATRIXFILE"), ConceptualizationModel.SPATIALWEIGHTMATRIXFILE),
				new ParameterDataNode(ProcessProperties.getString("String_ZONEOFINDIFFERENCE"), ConceptualizationModel.ZONEOFINDIFFERENCE));


		this.parameterFile = new ParameterFile(ProcessProperties.getString("String_Label_FilePath"));
		String modelName = "CreateSWMBFile";
		if (!SmFileChoose.isModuleExist(modelName)) {
			String fileFilters = SmFileChoose.createFileFilter(ProcessProperties.getString("String_SWMFilePath"), "swmb");
			SmFileChoose.addNewNode(fileFilters, System.getProperty("user.dir"),
					ControlsProperties.getString("String_Save"), modelName, "SaveOne");
		}
		parameterFile.setModuleName(modelName);
		String defaultPath = System.getProperty("user.dir") + File.separator + "newFile.swmb";
		parameterFile.setSelectedItem(defaultPath);
		parameterFile.setRequisite(true);

		ParameterCombine parameterCombine = new ParameterCombine();
		parameterCombine.addParameters(parameterFile);
		parameterCombine.setDescribe(CommonProperties.getString("String_ResultSet"));
		parameters.addParameters(parameterCombine);
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_CreateSpatialWeightMatrixFile");
	}

	@Override
	public String getKey() {
		return MetaKeys.CREATE_SPATIAL_WEIGHT_MATRIX_FILE;
	}

	@Override
	protected boolean doWork(DatasetVector datasetVector) {
		boolean isSuccessful = false;

		try {
			WeightsUtilities.addSteppedListener(steppedListener);
			// 唯一字段默认给SmID
			isSuccessful = WeightsUtilities.buildWeightMatrix(
					datasetVector, "SmID",
					(String) parameterFile.getSelectedItem(),
					parameterPatternsParameter.getPatternParameter());
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e.getMessage());
		} finally {
			WeightsUtilities.removeSteppedListener(steppedListener);
		}
		return isSuccessful;
	}
}
