package com.supermap.desktop.ui.controls.scrollPanel;

import com.supermap.desktop.GlobalParameters;
import com.supermap.ui.MapControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author XiaJT
 */
public class SmMapControlScrollPanel extends JScrollPane {
	private Integer delay;

	public SmMapControlScrollPanel(MapControl mapControl) {
		this.setViewport(new MyViewPort());
		this.setViewportView(mapControl);
	}

	public Integer getDelay() {
		return delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	class MyViewPort extends JViewport {
		private Dimension newSize;
		private Timer timer;
		private boolean isFirstTime = true;

		public MyViewPort() {

		}

		@Override
		public void setViewSize(Dimension newSize) {
			this.newSize = newSize;
			if (isFirstTime || (getDelay() == null && GlobalParameters.getMapRefreshDelayWhileResize() == 0)) {
				doResize();
				isFirstTime = false;
				return;
			}
			if (timer != null) {
				timer.stop();
				timer = null;
			}
			timer = new Timer(getDelay() != null ? getDelay() : GlobalParameters.getMapRefreshDelayWhileResize(), new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (MyViewPort.this.newSize != null) {
						doResize();
						MyViewPort.this.newSize = null;
						timer.stop();
						timer = null;
					}
				}
			});
			timer.start();
		}

		private void doResize() {
			super.setViewSize(newSize);
		}
	}
}
