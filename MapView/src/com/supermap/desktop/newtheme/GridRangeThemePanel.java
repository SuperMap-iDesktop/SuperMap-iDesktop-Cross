package com.supermap.desktop.newtheme;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.InternalImageIconFactory;

public class GridRangeThemePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private transient ThemeGuidDialog themeGuidDialog;
	private JLabel labelGridRangeTheme = new JLabel();
	private LocalMouseListener localMouseListener = new LocalMouseListener();
	
	public GridRangeThemePanel(ThemeGuidDialog themeGuidDialog) {
		this.themeGuidDialog = themeGuidDialog;
		initComponents();
		initResources();
		registListener();
	}
	private void initComponents() {
		// @formatter:off
		setBorder(new LineBorder(Color.LIGHT_GRAY));
		setBackground(Color.WHITE);

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
				groupLayout.createSequentialGroup().addGap(20).addComponent(labelGridRangeTheme).addContainerGap(368, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
				groupLayout.createSequentialGroup().addGap(24).addComponent(labelGridRangeTheme).addContainerGap(223, Short.MAX_VALUE)));
		setLayout(groupLayout);
		// @formatter:on
	}

	private void initResources() {
		this.labelGridRangeTheme.setIcon(InternalImageIconFactory.THEMEGUIDE_GRIDRANGE);
		this.labelGridRangeTheme.setText(MapViewProperties.getString("String_Default"));
		this.labelGridRangeTheme.setVerticalTextPosition(JLabel.BOTTOM);
		this.labelGridRangeTheme.setHorizontalTextPosition(JLabel.CENTER);
		this.labelGridRangeTheme.setOpaque(true);
		this.labelGridRangeTheme.setBackground(Color.gray);
	}

	/**
	 * 注册事件
	 */
	private void registListener() {
		this.labelGridRangeTheme.addMouseListener(this.localMouseListener );
	}

	public void unregistListener() {
		this.labelGridRangeTheme.removeMouseListener(this.localMouseListener);
	}

	class LocalMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				// 单值专题图
				ThemeGuideFactory.buildGridRangeTheme();
				themeGuidDialog.dispose();
				unregistListener();
			}
		}
	}
}
