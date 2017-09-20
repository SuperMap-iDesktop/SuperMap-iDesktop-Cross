package com.supermap.desktop.controls.property;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IProperty;
import com.supermap.desktop.Interface.IPropertyManager;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.enums.PropertyType;
import com.supermap.desktop.ui.controls.Dockbar;
import com.supermap.desktop.ui.controls.DockbarManager;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class DataPropertyContainer extends JPanel implements IPropertyManager {

	private static final long serialVersionUID = 1L;
	private ArrayList<AbstractPropertyControl> controls = new ArrayList<>();
	private transient IProperty currentProperty; // 当前选中的属性窗口，切换节点的时候，当然是希望保持当前选中的视图（如果有的话）
	private JTabbedPane tabbedPane = new JTabbedPane();

	private transient ChangeListener tabbedPaneChangeListener = new ChangeListener() {

		@Override
		public void stateChanged(ChangeEvent e) {
			tabbedPaneChange();
		}
	};

	/**
	 * Create the dialog.
	 */
	public DataPropertyContainer() {
		this.controls = new ArrayList<>();
		this.tabbedPane.addChangeListener(this.tabbedPaneChangeListener);
		ComponentUIUtilities.setName(this.tabbedPane, "DataPropertyContainer_tabbledPane");
		this.setLayout(new GridBagLayout());
		this.add(tabbedPane, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(10));
	}

	@Override
	public void setProperty(IProperty[] properties) {
		// 设置当前选中视图
		// Tab 子项不为0，表明上一个属性集合是 Tab 页，从中取出当前选中
		if (this.tabbedPane.getTabCount() != 0) {
			this.currentProperty = (IProperty) this.tabbedPane.getSelectedComponent();
		}

		for (AbstractPropertyControl control : this.controls) {
			control.hidden();
		}
		this.controls.clear();
		this.tabbedPane.removeAll();
		removeAll();

		updateControls(properties);

		this.revalidate();
		this.repaint();
	}

	@Override
	public void refreshData() {
		for (AbstractPropertyControl control : controls) {
			control.refreshData();
		}
	}

	private void tabbedPaneChange() {
		int selectedIndex = this.tabbedPane.getSelectedIndex();

		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			if (!(tabbedPane.getComponentAt(i) instanceof AbstractPropertyControl)) {
				continue;
			}
			if (i != selectedIndex) {
				((AbstractPropertyControl) tabbedPane.getComponentAt(i)).hidden();
			} else {
				((AbstractPropertyControl) tabbedPane.getComponentAt(i)).refreshData();
			}
		}
	}

	private void updateControls(IProperty[] properties) {
		if (properties != null) {
			if (properties.length == 1) {
				this.currentProperty = properties[0];
				this.controls.add((AbstractPropertyControl) this.currentProperty);
				this.add((AbstractPropertyControl) this.currentProperty, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
			} else if (properties.length > 1) {
				this.add(tabbedPane, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(10));
				this.tabbedPane.removeChangeListener(this.tabbedPaneChangeListener);
				for (IProperty property : properties) {
					this.controls.add((AbstractPropertyControl) property);
					this.tabbedPane.addTab(property.getPropertyName(), (AbstractPropertyControl) property);
				}
				this.tabbedPane.addChangeListener(this.tabbedPaneChangeListener);
				setTabbedPaneSelectedPropertyControl(this.currentProperty);
			}
		}
		tabbedPane.setVisible(tabbedPane.getTabCount() > 0);
	}

	/**
	 * 选中 TabbledPane 指定的 Tab 页
	 *
	 * @param property
	 */
	private void setTabbedPaneSelectedPropertyControl(IProperty property) {
		if (property != null) {
			for (int i = 0; i < this.tabbedPane.getTabCount(); i++) {
				AbstractPropertyControl control = (AbstractPropertyControl) this.tabbedPane.getComponentAt(i);

				if (control.getPropertyType() == property.getPropertyType()) {
					if (i == tabbedPane.getSelectedIndex()) {
						tabbedPaneChange();
					}
					this.tabbedPane.setSelectedIndex(i);
					return;
				}
			}
		}
		tabbedPaneChange();
	}

	@Override
	public boolean isUsable() {
		return this.isVisible();
	}

	@Override
	public PropertyType getPropertyType() {
		if (this.tabbedPane != null && this.tabbedPane.getTabCount() > 0) {
			return ((IProperty) this.tabbedPane.getSelectedComponent()).getPropertyType();
		} else {
			return null;
		}
	}

	@Override
	public int getPropertyCount() {
		return tabbedPane.getTabCount();
	}

	@Override
	public IProperty getPropertyByIndex(int index) {
		if (index < 0 || index > getPropertyCount()) {
			throw new IndexOutOfBoundsException(String.valueOf(index));
		}
		return ((IProperty) tabbedPane.getTabComponentAt(index));
	}

	@Override
	public void setSelectedProperty(int index) {
		if (index < 0 || index > getPropertyCount()) {
			throw new IndexOutOfBoundsException(String.valueOf(index));
		}
		tabbedPane.setSelectedIndex(index);
	}

	@Override
	public void setSelectedProperty(IProperty property) {
		int propertyIndex = getPropertyIndex(property);
		if (propertyIndex != -1) {
			setSelectedProperty(propertyIndex);
		}
	}

	@Override
	public int getPropertyIndex(IProperty property) {
		for (int i = 0; i < getPropertyCount(); i++) {
			if (tabbedPane.getComponentAt(i) == property) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public IProperty getCurrentProperty() {
		return currentProperty;
	}

	@Override
	public void setPropertyVisible(boolean visible) {
//		Component parentDocker = this;
//		while (parentDocker != null && !(parentDocker instanceof View)) {
//			parentDocker = parentDocker.getParent();
//		}
//		if (parentDocker != null) {
//			parentDocker.setVisible(true);
//		}
		Dockbar propertyDock = ((DockbarManager) Application.getActiveApplication().getMainFrame().getDockbarManager()).findDockbar(this);
		if (propertyDock != null) {
			propertyDock.setVisible(visible);
		}
	}
}
