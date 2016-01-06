package com.supermap.desktop.controls.property;

import java.awt.Component;
import javax.swing.JPanel;

public class JPanelDataPropertyContainer extends JPanel {
	private Component propertyControl;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public JPanelDataPropertyContainer() {
		// TODO 目前默认实现，后续会增加一些初始化操作
	}

	public Component getPropertyControl() {
		return this.propertyControl;
	}

	public void setPropertyControl(Component propertyControl) {
		this.propertyControl = propertyControl;
		if (propertyControl != null && this.getComponentCount() > 0) {
			if (propertyControl == this.getComponent(0)) {
				return;
			}

			// 先执行清理工作
			// ……
			// 再移除
			this.removeAll();
			this.add(propertyControl);
		}
	}
}
