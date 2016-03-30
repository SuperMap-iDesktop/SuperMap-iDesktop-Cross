package com.supermap.desktop.newtheme.guidPanel;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.newtheme.commonUtils.ThemeGuideFactory;
import com.supermap.desktop.newtheme.commonUtils.ThemeUtil;
import com.supermap.desktop.ui.controls.InternalImageIconFactory;

public class UniqueThemePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel labelUniqueTheme = new JLabel("");
	private transient LocalMouseListener localMouseListener = new LocalMouseListener();
	private transient ThemeGuidDialog themeGuidDialog;

	public UniqueThemePanel(ThemeGuidDialog themeGuidDialog) {
		this.themeGuidDialog = themeGuidDialog;
		initComponents();
		initResources();
		registListener();
	}

	private void initComponents() {
		setBorder(new LineBorder(Color.LIGHT_GRAY));
		setBackground(Color.WHITE);

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
				groupLayout.createSequentialGroup().addGap(20).addComponent(labelUniqueTheme).addContainerGap(368, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
				groupLayout.createSequentialGroup().addGap(24).addComponent(labelUniqueTheme).addContainerGap(223, Short.MAX_VALUE)));
		setLayout(groupLayout);
	}

	private void initResources() {
		this.labelUniqueTheme.setIcon(InternalImageIconFactory.THEMEGUIDE_UNIQUE);
		this.labelUniqueTheme.setText(MapViewProperties.getString("String_Default"));
		this.labelUniqueTheme.setVerticalTextPosition(JLabel.BOTTOM);
		this.labelUniqueTheme.setHorizontalTextPosition(JLabel.CENTER);
		this.labelUniqueTheme.setOpaque(true);
		this.labelUniqueTheme.setBackground(Color.gray);
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
