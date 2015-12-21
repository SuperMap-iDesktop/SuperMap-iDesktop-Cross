package com.supermap.desktop.newtheme;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.InternalImageIconFactory;

public class RangeThemePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel labelRangeTheme = new JLabel("");
	private transient LocalMouseListener mouserListener = new LocalMouseListener();
	private transient ThemeGuidDialog themeGuidDialog;

	public RangeThemePanel(ThemeGuidDialog themeGuidDialog) {
		this.themeGuidDialog = themeGuidDialog;
		initComponents();
		initResources();
		registListener();
	}

	private void initComponents() {
		setBorder(new LineBorder(Color.LIGHT_GRAY));
		setBackground(Color.WHITE);

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
								.addGap(20)
								.addComponent(labelRangeTheme)
								.addContainerGap(368, Short.MAX_VALUE))
				);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
								.addGap(24)
								.addComponent(labelRangeTheme)
								.addContainerGap(223, Short.MAX_VALUE))
				);
		setLayout(groupLayout);
	}

	private void initResources() {
		this.labelRangeTheme.setIcon(InternalImageIconFactory.THEMEGUIDE_RANGE);
		this.labelRangeTheme.setText(MapViewProperties.getString("String_Default"));
		this.labelRangeTheme.setVerticalTextPosition(JLabel.BOTTOM);
		this.labelRangeTheme.setHorizontalTextPosition(JLabel.CENTER);
		this.labelRangeTheme.setOpaque(true);
		this.labelRangeTheme.setBackground(Color.gray);
	}

	private void registListener() {
		this.labelRangeTheme.addMouseListener(this.mouserListener);
	}

	public void unregistListener() {
		this.labelRangeTheme.removeMouseListener(this.mouserListener);
	}

	class LocalMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				// 分段专题图
				ThemeGuideFactory.buildRangeTheme();
				themeGuidDialog.dispose();
				unregistListener();
			}
		}

	}

}
