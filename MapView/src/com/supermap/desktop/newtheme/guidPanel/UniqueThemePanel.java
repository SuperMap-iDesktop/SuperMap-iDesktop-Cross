package com.supermap.desktop.newtheme.guidPanel;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.newtheme.commonUtils.ThemeGuideFactory;
import com.supermap.desktop.newtheme.commonUtils.ThemeUtil;
import com.supermap.desktop.ui.controls.InternalImageIconFactory;

public class UniqueThemePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private ThemeLabelDecorator labelUniqueTheme;
	private transient LocalMouseListener localMouseListener = new LocalMouseListener();
	private transient ThemeGuidDialog themeGuidDialog;

	public UniqueThemePanel(ThemeGuidDialog themeGuidDialog) {
		this.themeGuidDialog = themeGuidDialog;
		initComponents();
		registListener();
	}

	private void initComponents() {
		this.setBackground(Color.WHITE);
		this.setBorder(new LineBorder(Color.LIGHT_GRAY));
		this.labelUniqueTheme = new ThemeLabelDecorator(InternalImageIconFactory.THEMEGUIDE_UNIQUE, MapViewProperties.getString("String_Default"));
		this.labelUniqueTheme.selected(true);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
				groupLayout.createSequentialGroup().addComponent(labelUniqueTheme).addContainerGap(160, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
				groupLayout.createSequentialGroup().addComponent(labelUniqueTheme).addContainerGap(240, Short.MAX_VALUE)));
		setLayout(groupLayout);
	}

	/**
	 * 注册事件
	 */
	private void registListener() {
		this.labelUniqueTheme.addMouseListener(this.localMouseListener);
	}

	public void unregistListener() {
		this.labelUniqueTheme.removeMouseListener(this.localMouseListener);
	}

	class LocalMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				// 单值专题图
				ThemeGuideFactory.buildUniqueTheme(ThemeUtil.getActiveLayer());
				themeGuidDialog.dispose();
				unregistListener();
			}
		}
	}
}
