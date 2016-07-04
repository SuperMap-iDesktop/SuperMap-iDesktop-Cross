package com.supermap.desktop.task;

import java.util.concurrent.CancellationException;

import javax.swing.SwingUtilities;

import com.supermap.desktop.lbsclient.LBSClientProperties;

public class AttributeQueryTask extends CommonTask{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AttributeQueryTask() {
		super();
//		progressBar.setString(LBSClientProperties.getString("String_AttributeQuery"));
	}
	
	@Override
	public void updateProgress(final int percent, String remainTime, String message) throws CancellationException {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				progressBar.setValue(percent);
				if (100 == percent) {
					progressBar.setString(LBSClientProperties.getString("String_AttributeQueryFinished"));
				}

			}
		});
	}

}
