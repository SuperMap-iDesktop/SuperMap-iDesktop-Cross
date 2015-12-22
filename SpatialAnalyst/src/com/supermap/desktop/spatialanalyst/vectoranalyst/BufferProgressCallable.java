package com.supermap.desktop.spatialanalyst.vectoranalyst;

import java.util.concurrent.CancellationException;

import com.supermap.analyst.spatialanalyst.BufferAnalyst;
import com.supermap.analyst.spatialanalyst.BufferAnalystParameter;
import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Recordset;
import com.supermap.data.SteppedEvent;
import com.supermap.data.SteppedListener;
import com.supermap.desktop.Application;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;

public class BufferProgressCallable extends UpdateProgressCallable {
	private Object sourceData;
	private DatasetVector resultDatasetVector;
	private BufferAnalystParameter bufferAnalystParameter;
	private boolean union;
	private boolean isAttributeRetained;
	private boolean createBufferAnalyst;

	private SteppedListener steppedListener = new SteppedListener() {

		@Override
		public void stepped(SteppedEvent arg0) {
			try {
				updateProgress(arg0.getPercent(), String.valueOf(arg0.getRemainTime()), arg0.getMessage());
			} catch (CancellationException e) {
				arg0.setCancel(true);
			}
		}
	};

	public BufferProgressCallable(Object sourceData, DatasetVector resultDatasetVector, BufferAnalystParameter bufferAnalystParameter, boolean union,
			boolean isAttributeRetained) {
		this.sourceData = sourceData;
		this.resultDatasetVector = resultDatasetVector;
		this.bufferAnalystParameter = bufferAnalystParameter;
		this.union = union;
		this.isAttributeRetained = isAttributeRetained;
	}

	@Override
	public Boolean call() throws Exception {
		Application.getActiveApplication().getOutput().output(SpatialAnalystProperties.getString("String_BufferCreating"));
		try {
			createBufferAnalyst = false;
			BufferAnalyst.addSteppedListener(steppedListener);
			if (this.sourceData instanceof DatasetVector) {
				createBufferAnalyst = BufferAnalyst.createBuffer((DatasetVector) sourceData, resultDatasetVector, bufferAnalystParameter, union,
						isAttributeRetained);
			} else if (this.sourceData instanceof Recordset) {
				createBufferAnalyst = BufferAnalyst.createBuffer((Recordset) sourceData, resultDatasetVector, bufferAnalystParameter, union,
						isAttributeRetained);
			}

			if (createBufferAnalyst) {
				Application.getActiveApplication().getOutput().output(SpatialAnalystProperties.getString("String_BufferCreatedSuccess"));
			} else {
				Application.getActiveApplication().getOutput().output(SpatialAnalystProperties.getString("String_BufferCreatedFailed"));
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(SpatialAnalystProperties.getString("String_BufferCreatedFailed"));
		} finally {
			BufferAnalyst.removeSteppedListener(steppedListener);
			if (this.sourceData instanceof Recordset && this.sourceData != null) {
				((Recordset) this.sourceData).dispose();
			}
		}
		return createBufferAnalyst;
	}
}
