package com.supermap.desktop.mapview.map.propertycontrols;

import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilities.SystemPropertyUtilities;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * @author YuanR
 * 2017.3.21
 * 地图选择对象事件，提示信息面板
 */
public class MapActionSelectTargetInfoPanel extends JPanel {
	private JLabel labelSelectTargetInfo;

	public MapActionSelectTargetInfoPanel(String text) {
		if (SystemPropertyUtilities.isWindows()) {
			this.setSize(new Dimension(265, 30));
		} else {
			this.setSize(new Dimension(325, 30));
		}

		this.setBorder(new LineBorder(Color.LIGHT_GRAY));
		this.setBackground(new Color(255, 255, 255, 220));

		this.labelSelectTargetInfo = new JLabel("Select one or more geoRegion.");
		this.labelSelectTargetInfo.setText(text);

		this.setLayout(new GridBagLayout());
		this.add(this.labelSelectTargetInfo, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH));

	}

	/**
	 * 实时改变显示
	 * yuanR2017.9.15
	 *
	 * @param text
	 */
	public void setShowedText(String text) {
		this.labelSelectTargetInfo.setText(text);
	}

}
