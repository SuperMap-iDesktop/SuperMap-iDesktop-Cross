package com.supermap.desktop.task;

import java.util.concurrent.CancellationException;

import javax.swing.SwingUtilities;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.utilities.CommonUtilities;

public class KernelDensityTask extends CommonTask {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public KernelDensityTask(){
		super();
		labelTitle.setText(LBSClientProperties.getString("String_KernelDensity"));
		labelLogo.setIcon(CommonUtilities.getImageIcon("image_KernelDensity.png"));
	}
	@Override
	public void updateProgress(final int percent, final String remainTime, String message) throws CancellationException {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				progressBar.setValue(percent);
				labelStatus.setText("...");
				if (percent == 100) {
					labelTitle.setText(LBSClientProperties.getString("String_KernelDensityFinished"));
				}

			}
		});
	}

}
