package com.supermap.desktop.ui.controls.button;


import javax.swing.*;
import java.awt.*;

/**
 * 初始化时给一个默认大小
 */
public class SmButton extends JButton {

	private ISmButtonStyle smButtonStyle;
	private boolean isUseDefaultSize = true;

	public SmButton() {
		super();
		init();
	}

	public SmButton(String text) {
		super(text);
		init();
	}

	public SmButton(ImageIcon icon) {
		super(icon);
//		init();
	}

	private void init() {
		smButtonStyle = new SmButtonStyleImpl();
	}

	@Override
	public Dimension getPreferredSize() {
		return isUseDefaultSize && !isPreferredSizeSet() && smButtonStyle != null && smButtonStyle.getPreferredSize() != null ? smButtonStyle.getPreferredSize() : super.getPreferredSize();
	}

	@Override
	public Dimension getMaximumSize() {
		return isUseDefaultSize && !isMaximumSizeSet() && smButtonStyle != null && smButtonStyle.getMaximumSize() != null ? smButtonStyle.getMaximumSize() : super.getMaximumSize();
	}

	@Override
	public Dimension getMinimumSize() {
		return isUseDefaultSize && !isMinimumSizeSet() && smButtonStyle != null && smButtonStyle.getMinimumSize() != null ? smButtonStyle.getMinimumSize() : super.getMinimumSize();
	}

	public ISmButtonStyle getSmButtonStyle() {
		return smButtonStyle;
	}

	public void setSmButtonStyle(ISmButtonStyle smButtonStyle) {
		this.smButtonStyle = smButtonStyle;
	}

	@Override
	public void setIcon(Icon defaultIcon) {
		setUseDefaultSize(false);
		super.setIcon(defaultIcon);
	}

	public boolean isUseDefaultSize() {
		return isUseDefaultSize;
	}

	public void setUseDefaultSize(boolean useDefaultSize) {
		isUseDefaultSize = useDefaultSize;
	}
}
