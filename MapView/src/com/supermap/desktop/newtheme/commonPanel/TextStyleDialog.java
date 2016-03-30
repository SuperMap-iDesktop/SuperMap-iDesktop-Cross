package com.supermap.desktop.newtheme.commonPanel;

import com.supermap.data.TextStyle;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.mapping.ThemeLabel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

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
		setSize(420, 560);
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

	@Override
	public JRootPane keyBoardPressed() {
		JRootPane rootPane = new JRootPane();
		KeyStroke strokForEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		rootPane.registerKeyboardAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		}, strokForEnter, JComponent.WHEN_IN_FOCUSED_WINDOW);
		KeyStroke strokForEsc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		rootPane.registerKeyboardAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		}, strokForEsc, JComponent.WHEN_IN_FOCUSED_WINDOW);
		return rootPane;
	}
}
