package com.supermap.desktop.task;

import java.text.MessageFormat;
import java.util.concurrent.CancellationException;

import javax.swing.SwingUtilities;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.utilities.CommonUtilities;

public class KernelDensityRealtimeTask extends CommonTask{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public KernelDensityRealtimeTask(){
		super();
		labelTitle.setText(LBSClientProperties.getString("String_KernelDensityRealtime"));
		labelLogo.setIcon(CommonUtilities.getImageIcon("image_KernelDensityRealtime.png"));
	}
	@Override
	public void updateProgress(final int percent, final String remainTime, final String message) throws CancellationException {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				progressBar.setValue(percent);
				labelStatus.setText("...");
				if (percent == 100) {
					labelTitle.setText(LBSClientProperties.getString("String_KernelDensityRealtimeFinished"));
				}

			}
		});
	}
}
