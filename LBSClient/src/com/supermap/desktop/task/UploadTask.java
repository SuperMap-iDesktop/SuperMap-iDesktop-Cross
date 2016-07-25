package com.supermap.desktop.task;

import java.util.concurrent.CancellationException;

import javax.swing.SwingUtilities;

import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.utilities.CommonUtilities;

public class UploadTask extends Task{

	public UploadTask() {
		super();
		labelTitle.setText(LBSClientProperties.getString("String_Uploading"));
		labelLogo.setIcon(CommonUtilities.getImageIcon("image_datasource.png"));
	}

	@Override
	public void updateProgress(final int percent, final String remainTime, final String message) throws CancellationException {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				progressBar.setValue(percent);
				labelStatus.setText("...");
				if (percent == 100) {
					labelTitle.setText(LBSClientProperties.getString("String_UploadEnd"));
				}

			}
		});
	}

}