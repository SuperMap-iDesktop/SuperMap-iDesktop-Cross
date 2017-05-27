package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;

import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class MetaProcessHydrologicalAnalysis extends MetaProcessGroup {

	private ArrayList<MetaProcess> processes = new ArrayList<>();
	private EmptyMetaProcess fillingPseudoDepressions = new EmptyMetaProcess(ProcessProperties.getString("String_FillingPseudoDepressions"));
	private EmptyMetaProcess calculatedFlowDirection = new EmptyMetaProcess(ProcessProperties.getString("String_CalculatedFlowDirection"));
	private EmptyMetaProcess computationalFlowLength = new EmptyMetaProcess(ProcessProperties.getString("String_ComputationalFlowLength"));
	private EmptyMetaProcess calculatedWaterFlow = new EmptyMetaProcess(ProcessProperties.getString("String_CalculatedWaterFlow"));
	private EmptyMetaProcess catchmentPointCalculation = new EmptyMetaProcess(ProcessProperties.getString("String_CatchmentPointCalculation"));
	private EmptyMetaProcess watershedSegmentation = new EmptyMetaProcess(ProcessProperties.getString("String_WatershedSegmentation"));
	private EmptyMetaProcess watershedBasin = new EmptyMetaProcess(ProcessProperties.getString("String_WatershedBasin"));
	private EmptyMetaProcess stringExtractingGridWaterSystem = new EmptyMetaProcess(ProcessProperties.getString("String_ExtractingGridWaterSystem"));
	private EmptyMetaProcess riverClassification = new EmptyMetaProcess(ProcessProperties.getString("String_RiverClassification"));
	private EmptyMetaProcess drainageVectorization = new EmptyMetaProcess(ProcessProperties.getString("String_DrainageVectorization"));
	private EmptyMetaProcess connectedDrainage = new EmptyMetaProcess(ProcessProperties.getString("String_ConnectedDrainage"));

	public MetaProcessHydrologicalAnalysis() {
		processes.add(fillingPseudoDepressions);
		processes.add(calculatedFlowDirection);
		processes.add(computationalFlowLength);
		processes.add(calculatedWaterFlow);
		processes.add(catchmentPointCalculation);
		processes.add(watershedSegmentation);
		processes.add(watershedBasin);
		processes.add(stringExtractingGridWaterSystem);
		processes.add(riverClassification);
		processes.add(drainageVectorization);
		processes.add(connectedDrainage);

	}

	@Override
	public int getProcessCount() {
		return processes.size();
	}

	@Override
	public MetaProcess getMetaProcess(int index) {
		return processes.get(index);
	}

	@Override
	public ArrayList<MetaProcess> getSubMetaProcess(MetaProcess process) {
		ArrayList<MetaProcess> metaProcesses = new ArrayList<>();
		if (process == fillingPseudoDepressions) {
			metaProcesses.add(calculatedFlowDirection);
		} else if (process == calculatedFlowDirection) {
			metaProcesses.add(computationalFlowLength);
			metaProcesses.add(calculatedWaterFlow);
			metaProcesses.add(catchmentPointCalculation);
			metaProcesses.add(watershedSegmentation);
			metaProcesses.add(watershedBasin);
		} else if (process == calculatedWaterFlow) {
			metaProcesses.add(catchmentPointCalculation);
			metaProcesses.add(stringExtractingGridWaterSystem);
		} else if (process == stringExtractingGridWaterSystem) {
			metaProcesses.add(riverClassification);
			metaProcesses.add(drainageVectorization);
			metaProcesses.add(connectedDrainage);
		}
		return metaProcesses;
	}

	@Override
	public ArrayList<MetaProcess> getParentMetaProcess(MetaProcess process) {
		return null;
	}

	@Override
	public void run() {

	}

	@Override
	public String getKey() {
		return MetaKeys.HydrologicalAnalysis;
	}
}
