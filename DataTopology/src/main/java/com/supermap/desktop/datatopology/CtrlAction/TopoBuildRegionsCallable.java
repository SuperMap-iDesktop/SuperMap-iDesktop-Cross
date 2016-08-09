package com.supermap.desktop.datatopology.CtrlAction;

import com.supermap.data.*;
import com.supermap.data.topology.TopologyProcessing;
import com.supermap.data.topology.TopologyProcessingOptions;
import com.supermap.desktop.Application;
import com.supermap.desktop.datatopology.DataTopologyProperties;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.ui.UICommonToolkit;

import java.text.MessageFormat;
import java.util.concurrent.CancellationException;

public class TopoBuildRegionsCallable extends UpdateProgressCallable {
	private Datasource datasource;
	private Dataset dataset;
	private String targetDatasetName;
	private String time;
	private DatasetVector resultDataset;
	private TopologyProcessingOptions topologyProcessingOptions;
	private PercentListener percentListener;

	public TopoBuildRegionsCallable(Datasource datasource, Dataset dataset, String targetDatasetName, TopologyProcessingOptions topologyProcessingOptions) {
		this.datasource = datasource;
		this.dataset = dataset;
		this.targetDatasetName = targetDatasetName;
		this.topologyProcessingOptions = topologyProcessingOptions;
	}

	@Override
	public Boolean call() throws Exception {
		boolean result = true;

		try {
			percentListener = new PercentListener();
			TopologyProcessing.addSteppedListener(percentListener);
			long startTime = System.currentTimeMillis();
			resultDataset = TopologyProcessing.buildRegions((DatasetVector) dataset, datasource, targetDatasetName, topologyProcessingOptions);
			long endTime = System.currentTimeMillis();
			time = String.valueOf((endTime - startTime) / 1000.0);

		} catch (Exception e) {
			result = false;
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			String info = "";
			if (null != resultDataset) {
				info = MessageFormat.format(DataTopologyProperties.getString("String_TopoRegionSucceed"), targetDatasetName,
						String.valueOf(resultDataset.getRecordCount()), time);
				UICommonToolkit.refreshSelectedDatasetNode(resultDataset);
			} else {
				info = "failed";
			}
			Application.getActiveApplication().getOutput().output(info);
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
