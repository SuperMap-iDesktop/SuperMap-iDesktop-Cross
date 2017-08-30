package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;

import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class MetaProcessHydrologicalAnalysis extends MetaProcessGroup {

	private ArrayList<MetaProcess> processes = new ArrayList<>();
	private EmptyMetaProcess fillSink = new EmptyMetaProcess(ProcessProperties.getString("String_Title_FillSink"));
	private EmptyMetaProcess flowDirection = new EmptyMetaProcess(ProcessProperties.getString("String_Title_FlowDirection"));
	private EmptyMetaProcess flowLength = new EmptyMetaProcess(ProcessProperties.getString("String_Title_FlowLength"));
	private EmptyMetaProcess flowAccumulation = new EmptyMetaProcess(ProcessProperties.getString("String_Title_FlowAccumulation"));
	private EmptyMetaProcess pourPoints = new EmptyMetaProcess(ProcessProperties.getString("String_Title_PourPoints"));
	private EmptyMetaProcess watershed = new EmptyMetaProcess(ProcessProperties.getString("String_Title_Watershed"));
	private EmptyMetaProcess basin = new EmptyMetaProcess(ProcessProperties.getString("String_Title_Basin"));
	private EmptyMetaProcess streamGrid = new EmptyMetaProcess(ProcessProperties.getString("String_Title_StreamGrid"));
	private EmptyMetaProcess streamOrder = new EmptyMetaProcess(ProcessProperties.getString("String_Title_StreamOrder"));
	private EmptyMetaProcess streamToLine = new EmptyMetaProcess(ProcessProperties.getString("String_Title_StreamToLine"));
	private EmptyMetaProcess streamLink = new EmptyMetaProcess(ProcessProperties.getString("String_Title_StreamLink"));

	public MetaProcessHydrologicalAnalysis() {
		processes.add(fillSink);
		processes.add(flowDirection);
		processes.add(flowLength);
		processes.add(flowAccumulation);
		processes.add(pourPoints);
		processes.add(watershed);
		processes.add(basin);
		processes.add(streamGrid);
		processes.add(streamOrder);
		processes.add(streamToLine);
		processes.add(streamLink);

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
		if (process == fillSink) {
			metaProcesses.add(flowDirection);
		} else if (process == flowDirection) {
			metaProcesses.add(flowLength);
			metaProcesses.add(flowAccumulation);
			metaProcesses.add(pourPoints);
			metaProcesses.add(watershed);
			metaProcesses.add(basin);
		} else if (process == flowAccumulation) {
			metaProcesses.add(pourPoints);
			metaProcesses.add(streamGrid);
		} else if (process == streamGrid) {
			metaProcesses.add(streamOrder);
			metaProcesses.add(streamToLine);
			metaProcesses.add(streamLink);
		}
		return metaProcesses;
	}

	@Override
	public ArrayList<MetaProcess> getParentMetaProcess(MetaProcess process) {
		return null;
	}

	@Override
	public boolean execute() {
		return true;
	}

	@Override
	public String getKey() {
		return MetaKeys.HYDROLOGICAL_ANALYST;
	}
}
