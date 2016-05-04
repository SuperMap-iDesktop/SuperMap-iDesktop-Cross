package com.supermap.desktop.newtheme.guidPanel;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.LineBorder;

import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.InternalImageIconFactory;

public class GraduatedSymbolThemePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private ThemeLabelDecorator graduatedSymbolTheme;
	private transient ThemeGuidDialog themeGuidDialog;
	private transient LocalMouseListener mouseListener = new LocalMouseListener();

	public GraduatedSymbolThemePanel(ThemeGuidDialog themeGuidDialog) {
		this.themeGuidDialog = themeGuidDialog;
		initComponents();
		registListener();
	}

	/**
	 * 界面布局入口
	 */
	private void initComponents() {
		// @formatter:off
		this.graduatedSymbolTheme = new ThemeLabelDecorator(InternalImageIconFactory.THEMEGUIDE_GRADUATEDSYMBOL, MapViewProperties.getString("String_ThemeDefaultItem"));
		this.graduatedSymbolTheme.selected(true);
		this.setBackground(Color.WHITE);
		this.setBorder(new LineBorder(Color.LIGHT_GRAY));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
								.addGap(16)
								.addComponent(graduatedSymbolTheme)
								.addContainerGap(300, Short.MAX_VALUE))
				);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
								.addGap(16)
								.addComponent(graduatedSymbolTheme)
								.addContainerGap(200, Short.MAX_VALUE))
				);
		setLayout(groupLayout);
		// @formatter:on
	}

	/**
	 * 注册事件
	 */
	private void registListener() {
		this.graduatedSymbolTheme.addMouseListener(this.mouseListener);
	}

	/**
	 * 注销事件
	 */
	public void unregistListener() {
		this.graduatedSymbolTheme.removeMouseListener(this.mouseListener);
	}

	class LocalMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				// 等级符号专题图
				
				
				themeGuidDialog.dispose();
				unregistListener();
			}
		}

	}
}
