package com.supermap.desktop.geometryoperation.control;

import com.supermap.ui.MapControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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
		this.contentPanel.setLayout(new BoxLayout(this.contentPanel, BoxLayout.Y_AXIS));
		this.contentPanel.setBackground(new Color(255, 255, 255, 150));
		this.contentPanel.setVisible(false);
	}

	public void addLabel(final JComponent component) {
		if (component != null) {
			this.contentPanel.add(component);
			component.addPropertyChangeListener("text", new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					resize();
				}
			});

			resize();
		}
	}

	private void resize() {
		int width = 0;
		int height = 0;

		for (int i = 0; i < this.contentPanel.getComponentCount(); i++) {
			Component cop = this.contentPanel.getComponent(i);
			width = Math.max(width, cop.getPreferredSize().width);
			height += cop.getPreferredSize().height + 2;
		}
		this.contentPanel.setSize(new Dimension(width, height));
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
