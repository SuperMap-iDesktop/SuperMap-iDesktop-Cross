package com.supermap.desktop.mapview.MapCache;

import com.supermap.data.SteppedEvent;
import com.supermap.data.SteppedListener;
import com.supermap.data.processing.MapCacheBuilder;
import com.supermap.desktop.Application;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;

import java.util.concurrent.CancellationException;

/**
 * Created by lixiaoyao on 2017/3/23.
 */
public class CacheProgressCallable extends UpdateProgressCallable {
	private MapCacheBuilder mapCacheBuilder;
	private boolean result;
	private boolean isAppending;

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

	public CacheProgressCallable(MapCacheBuilder mapCacheBuilder, boolean isAppending) {
		this.mapCacheBuilder = mapCacheBuilder;
		this.isAppending = isAppending;
	}

	@Override
	public Boolean call() throws Exception {
		this.result = true;
		try {
			this.mapCacheBuilder.addSteppedListener(this.steppedListener);
//			this.mapCacheBuilder.setIsAppending(isAppending);
			this.result = this.mapCacheBuilder.build();
		} catch (Exception ex) {
			this.result = false;
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			return result;
		}
	}

	public boolean getResult() {
		return this.result;
	}

}
