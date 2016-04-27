package com.supermap.desktop.datatopology.CtrlAction;

import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.data.SteppedEvent;
import com.supermap.data.SteppedListener;
import com.supermap.data.topology.TopologyProcessing;
import com.supermap.data.topology.TopologyProcessingOptions;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.utilties.DatasetUtilties;
import com.supermap.desktop.datatopology.DataTopologyProperties;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;

import java.text.MessageFormat;
import java.util.concurrent.CancellationException;

public class LineTopoCleanCallable extends UpdateProgressCallable {
	private String datasetName;
	private TopologyProcessingOptions topologyProcessingOptions;
	private Datasource datasource;
	private PercentListener percentListener;

	public LineTopoCleanCallable(String datasetName, TopologyProcessingOptions topologyProcessingOptions, Datasource datasource) {
		this.datasetName = datasetName;
		this.datasource = datasource;
		this.topologyProcessingOptions = topologyProcessingOptions;
	}

	@Override
	public Boolean call() throws Exception {
		boolean result = true;

		try {
			DatasetVector dataset = (DatasetVector) DatasetUtilties.getDatasetFromDatasource(datasetName, datasource);
			String topoInfo = "";
			percentListener = new PercentListener();
			TopologyProcessing.addSteppedListener(percentListener);
			long startTime = System.currentTimeMillis();
			boolean topologyProcessResult = TopologyProcessing.clean(dataset, topologyProcessingOptions);
			long endTime = System.currentTimeMillis();
			String time = String.valueOf((endTime - startTime) / 1000.0);
			if (topologyProcessResult) {
				topoInfo = MessageFormat.format(DataTopologyProperties.getString("String_TopoLineSucceed"), datasetName, time);
			} else {
				topoInfo = MessageFormat.format(DataTopologyProperties.getString("String_TopoLineFailed"), datasetName);
			}
			Application.getActiveApplication().getOutput().output(topoInfo);
		} catch (Exception e) {
			result = false;
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			TopologyProcessing.removeSteppedListener(percentListener);
		}
		return result;
	}

	class PercentListener implements SteppedListener {

		@Override
		public void stepped(SteppedEvent arg0) {
			try {
				updateProgress(arg0.getPercent(), String.valueOf(arg0.getRemainTime()), arg0.getMessage());
			} catch (CancellationException e) {
				arg0.setCancel(true);
			}

		}

	}

}
