package com.supermap.desktop.newtheme.guidPanel;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.LineBorder;

import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.newtheme.commonUtils.ThemeGuideFactory;
import com.supermap.desktop.newtheme.commonUtils.ThemeUtil;
import com.supermap.desktop.ui.controls.InternalImageIconFactory;

public class RangeThemePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private ThemeLabelDecorator labelRangeTheme;
	private transient LocalMouseListener mouseListener = new LocalMouseListener();
	private transient ThemeGuidDialog themeGuidDialog;

	public RangeThemePanel(ThemeGuidDialog themeGuidDialog) {
		this.themeGuidDialog = themeGuidDialog;
		initComponents();
		registListener();
	}

	private void initComponents() {
		this.labelRangeTheme = new ThemeLabelDecorator(InternalImageIconFactory.THEMEGUIDE_RANGE, MapViewProperties.getString("String_Default"));
		this.labelRangeTheme.selected(true);
		this.setBackground(Color.WHITE);
		this.setBorder(new LineBorder(Color.LIGHT_GRAY));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
				groupLayout.createSequentialGroup().addGap(16).addComponent(labelRangeTheme).addContainerGap(300, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
				groupLayout.createSequentialGroup().addComponent(labelRangeTheme).addContainerGap(200, Short.MAX_VALUE)));
		setLayout(groupLayout);
	}

	private void registListener() {
		this.labelRangeTheme.addMouseListener(this.mouseListener);
	}

	public void unregistListener() {
		this.labelRangeTheme.removeMouseListener(this.mouseListener);
	}

	class LocalMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				// 分段专题图
				ThemeGuideFactory.buildRangeTheme(ThemeUtil.getActiveLayer());
				themeGuidDialog.dispose();
				unregistListener();
			}
		}

	}

}
