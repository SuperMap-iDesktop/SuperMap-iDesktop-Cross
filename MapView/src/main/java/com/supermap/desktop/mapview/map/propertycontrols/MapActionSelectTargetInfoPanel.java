package com.supermap.desktop.mapview.map.propertycontrols;

import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilities.StringUtilities;
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

	private String text;

	public MapActionSelectTargetInfoPanel(String text) {
		if (!StringUtilities.isNullOrEmpty(text)) {
			this.text = text;
		} else {
			this.text = MapViewProperties.getString("String_SelectOneOrMoreRegion");
		}
		if (SystemPropertyUtilities.isWindows()) {
			this.setSize(220, 30);
		} else {
			this.setSize(280, 30);
		}
		this.setBorder(new LineBorder(Color.LIGHT_GRAY));
		this.setBackground(new Color(255, 255, 255, 220));

		this.labelSelectTargetInfo = new JLabel("Select one or more geoRegion.");
		this.labelSelectTargetInfo.setText(text);

		this.setLayout(new GridBagLayout());
		this.add(this.labelSelectTargetInfo, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
	}
}
