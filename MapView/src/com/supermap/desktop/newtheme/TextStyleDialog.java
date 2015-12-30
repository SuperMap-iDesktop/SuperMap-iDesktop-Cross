package com.supermap.desktop.newtheme;

import com.supermap.data.TextStyle;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.mapping.Map;

import java.awt.*;
import java.util.List;

public class TextStyleDialog extends SmDialog {

	private static final long serialVersionUID = 1L;
	private TextStyle textStyle;
	private transient TextStyleContainer textStyleContainer;
	private transient Map map;
	private List<TextStyle> list;

	public TextStyleDialog(TextStyle textStyle, Map map) {
		this.textStyle = textStyle;
		this.map = map;
		this.textStyleContainer = new TextStyleContainer(textStyle, map);
		initComponents();
	}

	public TextStyleDialog(List<TextStyle> list, Map map) {
		this.map = map;
		this.list = list;
		this.textStyleContainer = new TextStyleContainer(list, map);
		initComponents();
	}

	/**
	 * 构建界面
	 */
	private void initComponents() {
		setTitle(ControlsProperties.getString("String_Form_SetTextStyle"));
		setSize(465, 450);
		//  @formatter:off
		getContentPane().setLayout(new GridBagLayout());
		getContentPane().add(new TextStyleContainer(textStyle, map), new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(2, 1));
		// @formatter:on
	}

}
