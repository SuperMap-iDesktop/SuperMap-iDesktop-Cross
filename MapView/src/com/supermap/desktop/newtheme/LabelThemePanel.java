package com.supermap.desktop.newtheme;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;

import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.InternalImageIconFactory;

public class LabelThemePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel labelUniformTheme = new JLabel("");
	private JLabel labelRangeTheme = new JLabel("");

	public LabelThemePanel() {
		initComponents();
		initResources();
		registListener();
	}

	/**
	 * 界面布局入口
	 */
	private void initComponents() {
		// @formatter:off
		setBorder(new LineBorder(Color.LIGHT_GRAY));
		setBackground(Color.WHITE);

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
				groupLayout.createSequentialGroup()
						.addGap(26)
						.addComponent(labelUniformTheme)
						.addGap(13)
						.addComponent(labelRangeTheme)
						.addContainerGap(250, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
				groupLayout.createSequentialGroup().addGroup(
						groupLayout.createParallelGroup(Alignment.LEADING)
										.addGroup(groupLayout.createSequentialGroup().addGap(20).addComponent(labelUniformTheme))
										.addGroup(groupLayout.createSequentialGroup().addGap(21).addComponent(labelRangeTheme)))
						.addContainerGap(226, Short.MAX_VALUE)));
		setLayout(groupLayout);
		this.labelUniformTheme.setOpaque(true);
		this.labelUniformTheme.setBackground(Color.gray);
		// @formatter:on
	}

	/**
	 * 资源化
	 */
	private void initResources() {
		this.labelUniformTheme.setIcon(InternalImageIconFactory.THEMEGUIDE_UNIFORM);
		this.labelUniformTheme.setText(MapViewProperties.getString("String_ThemeLabelUniformItem"));
		this.labelUniformTheme.setVerticalTextPosition(JLabel.BOTTOM);
		this.labelUniformTheme.setHorizontalTextPosition(JLabel.CENTER);
		this.labelRangeTheme.setIcon(InternalImageIconFactory.THEMEGUIDE_RANGES);
		this.labelRangeTheme.setText(MapViewProperties.getString("String_ThemeLabelRangeItem"));
		this.labelRangeTheme.setVerticalTextPosition(JLabel.BOTTOM);
		this.labelRangeTheme.setHorizontalTextPosition(JLabel.CENTER);
	}

	/**
	 * 注册事件
	 */
	private void registListener() {
		// do nothing
	}

	/**
	 * 注销事件
	 */
	public void unregistListener() {
		// do nothing
	}

	public JLabel getLabelUniformTheme() {
		return labelUniformTheme;
	}

	public void setLabelUniformTheme(JLabel labelUniformTheme) {
		this.labelUniformTheme = labelUniformTheme;
	}

	public JLabel getLabelRangeTheme() {
		return labelRangeTheme;
	}

	public void setLabelRangeTheme(JLabel labelRangeTheme) {
		this.labelRangeTheme = labelRangeTheme;
	}

}
