package com.supermap.desktop.CtrlAction;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetImageCollection;
import com.supermap.data.SteppedEvent;
import com.supermap.data.SteppedListener;
import com.supermap.desktop.Application;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;

import java.text.MessageFormat;
import java.util.concurrent.CancellationException;

/**
 * @author XiaJt
 */
public class DatasetImageCollectionSynchronizeCallable extends UpdateProgressCallable {
	private boolean isCancel = false;
	private int totalDatasetCount = 0;
	private int completedCount = 0;
	private Dataset[] datasets = null;

	private SteppedListener steppedListener = new SteppedListener() {

		@Override
		public void stepped(SteppedEvent arg0) {
			int totalPercent = (arg0.getPercent() + 100 * completedCount) / totalDatasetCount;
			try {
				updateProgressTotal(arg0.getPercent(), totalPercent, String.valueOf(arg0.getRemainTime()), arg0.getMessage());
			} catch (CancellationException e) {
				isCancel = true;
				arg0.setCancel(true);
			}
		}
	};

	public DatasetImageCollectionSynchronizeCallable(Dataset[] datasets) {
		this.datasets = datasets;
		this.totalDatasetCount = this.datasets.length;
		this.completedCount = 0;
	}

	@Override
	public Boolean call() throws Exception {
		if (this.datasets == null || this.datasets.length == 0) {
			return false;
		}

		boolean isSucceeded;
		for (int i = 0; i < datasets.length; i++) {
			long startTime = System.currentTimeMillis();
			Dataset dataset = datasets[i];
			if (isCancel || !(dataset instanceof DatasetImageCollection)) {
				this.completedCount++;
				continue;
			}

//			dataset.addSteppedListener(this.steppedListener);

			try {
				isSucceeded = ((DatasetImageCollection) dataset).synchronize();
				dataset.close();

				if (isSucceeded) {
					String message = MessageFormat.format(DataViewProperties.getString("String_MSG_DatasetImageCollectionSynchronizeSuccess"), dataset.getName());
					Application.getActiveApplication().getOutput().output(message);
				} else {
					String message = MessageFormat.format(DataViewProperties.getString("String_MSG_DatasetImageCollectionSynchronizeFailed"), dataset.getName());
					Application.getActiveApplication().getOutput().output(message);
				}
			} catch (Exception e) {
				String message = MessageFormat.format(DataViewProperties.getString("String_MSG_DatasetImageCollectionSynchronizeFailed"), dataset.getName());
				Application.getActiveApplication().getOutput().output(message);
				Application.getActiveApplication().getOutput().output(e);
			} finally {
				this.completedCount++;
				String message = MessageFormat.format(DataViewProperties.getString("String_SynchronizeDatasetImageCollectioning"), completedCount, datasets.length);

				updateProgress(completedCount * 100 / (datasets.length), getRemainTime(i + 1, startTime), message);
//				dataset.removeSteppedListener(this.steppedListener);
			}
		}
		return true;

	}

	private String getRemainTime(int i, long startTime) {
		long currentTimeMillis = System.currentTimeMillis();
		long remainTime = (currentTimeMillis - startTime) / i * (completedCount - i);
		return String.valueOf(remainTime / 1000);
	}
}
