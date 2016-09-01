package com.supermap.desktop.ui.controls.toolTip;

import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.mapping.Map;

import javax.swing.*;
import java.awt.*;

/**
 * @author XiaJT
 */
public class MapToolTip extends SmToolTip {
	private Map map;
	private JLabel labelMapImage;
	private JLabel labelMapName;

	public MapToolTip(Map map) {
		this.map = map;
		this.setPreferredSize(new Dimension(200, 200));
		initComponent();
		initLayout();
	}

	private void initComponent() {
		labelMapName = new JLabel();
		labelMapImage = new JLabel();
		map.setImageSize(new Dimension(200, 180));
		map.viewEntire();
		this.labelMapImage.setIcon(new ImageIcon(map.outputMapToBitmap()));
		labelMapName.setText(map.getName());
	}

	private void initLayout() {
		this.setLayout(new GridBagLayout());
		this.add(labelMapName, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER));
		this.add(labelMapImage, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
	}

	@Override
	public void setTipText(String tipText) {

	}
}
