package com.supermap.desktop.task;

import com.supermap.desktop.lbsclient.LBSClientProperties;

import javax.swing.*;
import java.util.concurrent.CancellationException;
/**
 * 属性查询
 * @author xie
 *
 */
public class AttributeQueryTask extends Task{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AttributeQueryTask() {
		super();
		this.labelStatus.setVisible(false);
		this.progressBar.setString("");
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
