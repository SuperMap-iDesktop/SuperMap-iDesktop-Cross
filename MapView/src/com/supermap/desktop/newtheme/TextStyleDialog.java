package com.supermap.desktop.newtheme;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import com.supermap.data.TextStyle;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TextStyleContainer;
import com.supermap.mapping.Map;

public class TextStyleDialog extends SmDialog {

	private static final long serialVersionUID = 1L;
	private JButton buttonSure;
	private JButton buttonQuite;
	private JButton buttonApply;
	private TextStyle textStyle;
	private transient TextStyleContainer textStyleContainer;
	private transient Map map;
	private List<TextStyle> list;

	public TextStyleDialog(TextStyle textStyle, Map map) {
		this.textStyle = textStyle;
		this.map = map;
		this.textStyleContainer = new TextStyleContainer(textStyle, map);
		initComponents();
		initResources();
	}

	public TextStyleDialog(List<TextStyle> list, Map map) {
		this.map = map;
		this.list = list;
		this.textStyleContainer = new TextStyleContainer(list, map);
		initComponents();
		initResources();
	}

	/**
	 * 构建界面
	 */
	private void initComponents() {
		buttonQuite = new JButton();
		buttonSure = new JButton();
		buttonApply = new JButton();
		setTitle(ControlsProperties.getString("String_Form_SetTextStyle"));
		setSize(465, 450);
		//  @formatter:off
		getContentPane().setLayout(new GridBagLayout());
		getContentPane().add(textStyleContainer, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(2, 1));
//		getContentPane().add(buttonSure,         new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setWeight(50, 0).setInsets(0, 0, 5, 10));
//		getContentPane().add(buttonApply,        new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setWeight(1, 0).setInsets(0, 0, 5, 10));
//		getContentPane().add(buttonQuite,        new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setWeight(1, 0).setInsets(0, 0, 5, 20));
		// @formatter:on
	}

	/**
	 * 资源化
	 */
	private void initResources() {
		this.buttonQuite.setText(CommonProperties.getString("String_Button_Cancel"));
		this.buttonApply.setText(CommonProperties.getString("String_Button_Apply"));
		this.buttonSure.setText(CommonProperties.getString("String_Button_OK"));
	}

}
