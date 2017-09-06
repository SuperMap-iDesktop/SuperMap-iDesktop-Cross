package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.CalculationTerrain;
import com.supermap.analyst.spatialanalyst.ConversionAnalyst;
import com.supermap.analyst.spatialanalyst.DistanceAnalyst;
import com.supermap.analyst.spatialanalyst.GridAnalystSetting;
import com.supermap.analyst.spatialanalyst.MathAnalyst;
import com.supermap.analyst.spatialanalyst.TerrainAnalystSetting;
import com.supermap.desktop.GridAnalystSettingInstance;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ipls.ParameterGridAnalystSetting;

/**
 * @author XiaJT
 */
public abstract class MetaProcessGridAnalyst extends MetaProcess {

	private ParameterGridAnalystSetting parameterGridAnalystSetting;

	public MetaProcessGridAnalyst() {
		parameterGridAnalystSetting = new ParameterGridAnalystSetting();
		getParameters().addEnvironmentParameters(parameterGridAnalystSetting);
	}

	@Override
	public final boolean execute() {
		boolean executeResult;
		if (parameterGridAnalystSetting.isModified()) {
			GridAnalystSettingInstance result = parameterGridAnalystSetting.getResult();
			result.calculateValue();
			GridAnalystSetting gridAnalystSetting = result.getGridAnalystSetting();
			TerrainAnalystSetting terrainAnalystSetting = result.getTerrainAnalystSetting();
			GridAnalystSetting oldGridAnalystSetting = MathAnalyst.getAnalystSetting();
			TerrainAnalystSetting oldTerrainAnalystSetting = CalculationTerrain.getAnalystSetting();

			MathAnalyst.setAnalystSetting(gridAnalystSetting);
			CalculationTerrain.setAnalystSetting(terrainAnalystSetting);
			DistanceAnalyst.setAnalystSetting(gridAnalystSetting);
			ConversionAnalyst.setAnalystSetting(gridAnalystSetting);
			executeResult = childExecute();
			MathAnalyst.setAnalystSetting(oldGridAnalystSetting);
			CalculationTerrain.setAnalystSetting(oldTerrainAnalystSetting);
			DistanceAnalyst.setAnalystSetting(oldGridAnalystSetting);
			ConversionAnalyst.setAnalystSetting(oldGridAnalystSetting);
		} else {
			executeResult = childExecute();
		}
		return executeResult;
	}

	protected abstract boolean childExecute();
}
