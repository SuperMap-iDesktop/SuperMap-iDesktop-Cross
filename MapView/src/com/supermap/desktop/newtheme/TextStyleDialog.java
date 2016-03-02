package com.supermap.desktop.newtheme;

import com.supermap.data.TextStyle;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.mapping.ThemeLabel;

import java.awt.*;

public class TextStyleDialog extends SmDialog {

	private static final long serialVersionUID = 1L;
	private transient TextStyleContainer textStyleContainer;

	public TextStyleDialog(TextStyle textStyle, Map map, Layer themeLabelLayer) {
		this.textStyleContainer = new TextStyleContainer(textStyle, map, themeLabelLayer);
		initComponents();
	}

	public TextStyleDialog(ThemeLabel themeLabel,int[] selectRow, Map map,Layer themeLabelLayer) {
		this.textStyleContainer = new TextStyleContainer(themeLabel,selectRow, map,themeLabelLayer);
		initComponents();
	}

	/**
	 * 构建界面
	 */
	private void initComponents() {
		setTitle(ControlsProperties.getString("String_Form_SetTextStyle"));
		setSize(420, 500);
		//  @formatter:off
		getContentPane().setLayout(new GridBagLayout());
		getContentPane().add(textStyleContainer, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(2, 1));
		// @formatter:on
	}

	/**
	 * 设置是否及时刷新
	 * 
	 * @param isRefreshAtOnce
	 */
	public void setRefreshAtOnce(boolean isRefreshAtOnce) {
		textStyleContainer.setRefreshAtOnce(isRefreshAtOnce);
	}
	
	public TextStyleContainer getTextStyleContainer() {
		return textStyleContainer;
	}

	/**
	 * 属性地图和图层
	 */
	public void refreshMapAndLayer() {
		textStyleContainer.refreshMapAndLayer();
	}
}
