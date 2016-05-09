package com.supermap.desktop.geometryoperation.editor;

import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;

import com.supermap.ui.MapControl;

public class MapControlTip {
	private MapControl mapControl;
	private JPanel contentPanel;
	private LayoutManager originLayout;

	private MouseAdapter mouseListener = new MouseAdapter() {

		public void mouseEntered(MouseEvent e) {
			MapControlTip.this.contentPanel.setVisible(true);
		}

		public void mouseExited(MouseEvent e) {
			MapControlTip.this.contentPanel.setVisible(false);
		}
	};

	private MouseMotionAdapter mouseMotionListener = new MouseMotionAdapter() {

		public void mouseMoved(MouseEvent e) {
			MapControlTip.this.contentPanel.setLocation(e.getX() + 15, e.getY() + 15);
		}
	};

	public MapControlTip() {
		this.contentPanel = new JPanel();
	}

	public JPanel getContentPanel() {
		return this.contentPanel;
	}

	public void bind(MapControl mapControl) {
		unbind();
		this.mapControl = mapControl;
		this.mapControl.setLayout(null);
		this.mapControl.add(this.contentPanel);
		this.mapControl.addMouseListener(this.mouseListener);
		this.mapControl.addMouseMotionListener(this.mouseMotionListener);
	}

	public void unbind() {
		if (this.mapControl != null) {
			this.mapControl.remove(this.contentPanel);
			this.mapControl.setLayout(this.originLayout);
			this.mapControl.removeMouseListener(this.mouseListener);
			this.mapControl.removeMouseMotionListener(this.mouseMotionListener);
		}
	}
}
