package com.supermap.desktop.task;

import java.util.concurrent.CancellationException;

import javax.swing.SwingUtilities;

import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.utilities.CommonUtilities;

public class SpatialQueryTask extends Task{

	public SpatialQueryTask() {
		super();
		labelTitle.setText(LBSClientProperties.getString("String_CreateSpatialIndex"));
		labelLogo.setIcon(CommonUtilities.getImageIcon("image_createSpatialIndex.png"));
	}
	
	@Override
	public void updateProgress(final int percent, String remainTime, String message) throws CancellationException {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				progressBar.setValue(percent);
				labelStatus.setText("...");
				if (percent == 100) {
					labelTitle.setText(LBSClientProperties.getString("String_CreateSpatialIndexFinished"));
				}

			}
		});
	}

}
