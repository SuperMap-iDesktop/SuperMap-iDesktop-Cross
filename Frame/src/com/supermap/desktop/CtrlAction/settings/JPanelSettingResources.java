package com.supermap.desktop.CtrlAction.settings;

import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.labels.SmURLLabel;

import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;

/**
 * 关于
 *
 * @author XiaJT
 */
public class JPanelSettingResources extends BaseSettingPanel {

	private JLabel labelProductName;
	private JLabel labelVersion;
	private JLabel labelCopyright;
	private SmURLLabel labelSupport;
	private SmURLLabel labelOnlineBuy;

	@Override
	protected void initComponents() {
		labelProductName = new JLabel();
		labelVersion = new JLabel();
		labelCopyright = new JLabel();
		labelSupport = new SmURLLabel("http://support.supermap.com.cn/", CoreProperties.getString("String_Label_TechnicSupport"));
		labelOnlineBuy = new SmURLLabel("http://istore.supermap.com.cn/Product/product.aspx", CoreProperties.getString("String_LinkLabel_ProductCenter"));
	}

	@Override
	protected void initLayout() {
		this.setLayout(new GridBagLayout());
		this.add(labelProductName, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 5, 0, 0));
		this.add(labelVersion, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 5, 0, 0));
		this.add(labelCopyright, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 5, 0, 0));
		this.add(labelSupport, new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 5, 0, 0).setIpad(0, 8));
		this.add(labelOnlineBuy, new GridBagConstraintsHelper(0, 4, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 5, 0, 0).setIpad(0, 8));
		this.add(new JPanel(), new GridBagConstraintsHelper(0, 5, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
	}


	@Override
	protected void initResources() {
		labelProductName.setText(MessageFormat.format(CoreProperties.getString("String_ProductName"), GlobalParameters.getDesktopTitle()));
		labelVersion.setText(CoreProperties.getString("String_ProductVersion"));
		labelCopyright.setText(CoreProperties.getString("String_Copyright"));
	}


	@Override
	public void apply() {

	}

	@Override
	public void dispose() {

	}
}
