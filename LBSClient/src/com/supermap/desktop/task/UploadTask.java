package com.supermap.desktop.task;

import java.text.MessageFormat;
import java.util.concurrent.CancellationException;

import javax.swing.SwingUtilities;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.utilities.CommonUtilities;

public class UploadTask extends CommonTask{

	public UploadTask() {
		super();
		labelLogo.setIcon(CommonUtilities.getImageIcon("image_datasource.png"));
	}

	@Override
	public void updateProgress(final int percent, final String remainTime, final String message) throws CancellationException {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				labelTitle.setText(message);
				progressBar.setValue(percent);

				if (percent != 100) {
					labelStatus.setText(MessageFormat.format(ControlsProperties.getString("String_RemainTime"), remainTime));
				} else {
					labelStatus.setText("Upload finished.");
				}

			}
		});
	}

}