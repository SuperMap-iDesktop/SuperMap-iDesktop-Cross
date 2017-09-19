package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.CalculationTerrain;
import com.supermap.analyst.spatialanalyst.ConversionAnalyst;
import com.supermap.analyst.spatialanalyst.DistanceAnalyst;
import com.supermap.analyst.spatialanalyst.GridAnalystSetting;
import com.supermap.analyst.spatialanalyst.MathAnalyst;
import com.supermap.analyst.spatialanalyst.TerrainAnalystSetting;
import com.supermap.desktop.Application;
import com.supermap.desktop.GridAnalystSettingInstance;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ipls.ParameterGridAnalystSetting;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author XiaJT
 */
public abstract class MetaProcessGridAnalyst extends MetaProcess {

	protected ParameterGridAnalystSetting parameterGridAnalystSetting;
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public MetaProcessGridAnalyst() {
		parameterGridAnalystSetting = new ParameterGridAnalystSetting();
		getParameters().addEnvironmentParameters(parameterGridAnalystSetting);
	}

	@Override
	public final boolean execute() {
		boolean executeResult = false;

		if (parameterGridAnalystSetting.isModified()) {
			try {
				lock.writeLock().lock();
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
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			} finally {
				lock.writeLock().unlock();
			}
		} else {
			ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
			try {
				readLock.lock();
				executeResult = childExecute();
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			} finally {
				readLock.unlock();
			}
		}
		return executeResult;
	}

	protected abstract boolean childExecute();
}
