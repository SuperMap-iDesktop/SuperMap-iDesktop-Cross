package com.supermap.desktop.ui.controls.labels;

import com.supermap.desktop.utilities.BrowseUtilities;
import com.supermap.desktop.utilities.CursorUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author XiaJT
 */
public class SmURLLabel extends JLabel {

	private String url;

	public SmURLLabel(final String url, String text) {
		super();
		String s = "<HTML><U>" + text + "</U><HTML>";
		this.setText(s);
		this.url = url;
		this.setToolTipText(url);
		this.setForeground(Color.BLUE);
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				BrowseUtilities.openUrl(url);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				CursorUtilities.setHandCursor(SmURLLabel.this);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				CursorUtilities.setDefaultCursor(SmURLLabel.this);
			}
		});
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
