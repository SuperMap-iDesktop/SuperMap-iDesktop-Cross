package com.supermap.desktop.controls.property;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IProperty;
import com.supermap.desktop.Interface.IPropertyManager;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.enums.PropertyType;
import com.supermap.desktop.ui.controls.Dockbar;
import com.supermap.desktop.ui.controls.DockbarManager;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import org.flexdock.view.View;

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
	private JTabbedPane tabbledPane = new JTabbedPane();

	private transient ChangeListener tabbledPaneChangeListener = new ChangeListener() {

		@Override
		public void stateChanged(ChangeEvent e) {
			tabbledPaneChange();
		}
	};

	/**
	 * Create the dialog.
	 */
	public DataPropertyContainer() {
		this.controls = new ArrayList<>();
		this.tabbledPane.addChangeListener(this.tabbledPaneChangeListener);
		ComponentUIUtilities.setName(this.tabbledPane, "DataPropertyContainer_tabbledPane");
		this.setLayout(new GridBagLayout());
		this.add(tabbledPane, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(10));
	}

	@Override
	public void setProperty(IProperty[] properties) {
		// 设置当前选中视图
		// Tab 子项不为0，表明上一个属性集合是 Tab 页，从中取出当前选中
		if (this.tabbledPane.getTabCount() != 0) {
			this.currentProperty = (IProperty) this.tabbledPane.getSelectedComponent();
		}

		for (AbstractPropertyControl control : this.controls) {
			control.hidden();
		}
		this.controls.clear();
		this.tabbledPane.removeAll();
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

	private void tabbledPaneChange() {
		int selectedIndex = this.tabbledPane.getSelectedIndex();

		for (int i = 0; i < tabbledPane.getTabCount(); i++) {
			if (!(tabbledPane.getComponentAt(i) instanceof AbstractPropertyControl)) {
				continue;
			}
			if (i != selectedIndex) {
				((AbstractPropertyControl) tabbledPane.getComponentAt(i)).hidden();
			} else {
				((AbstractPropertyControl) tabbledPane.getComponentAt(i)).refreshData();
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
				this.add(tabbledPane, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(10));
				this.tabbledPane.removeChangeListener(this.tabbledPaneChangeListener);
				for (IProperty property : properties) {
					this.controls.add((AbstractPropertyControl) property);
					this.tabbledPane.addTab(property.getPropertyName(), (AbstractPropertyControl) property);
				}
				this.tabbledPane.addChangeListener(this.tabbledPaneChangeListener);
				setTabbledPaneSelectedPropertyControl(this.currentProperty);
			}
		}
		tabbledPane.setVisible(tabbledPane.getTabCount() > 0);
	}

	/**
	 * 选中 TabbledPane 指定的 Tab 页
	 *
	 * @param property
	 */
	private void setTabbledPaneSelectedPropertyControl(IProperty property) {
		if (property != null) {
			for (int i = 0; i < this.tabbledPane.getTabCount(); i++) {
				AbstractPropertyControl control = (AbstractPropertyControl) this.tabbledPane.getComponentAt(i);

				if (control.getPropertyType() == property.getPropertyType()) {
					if (i == tabbledPane.getSelectedIndex()) {
						tabbledPaneChange();
					}
					this.tabbledPane.setSelectedIndex(i);
					return;
				}
			}
		}
		tabbledPaneChange();
	}

	@Override
	public boolean isUsable() {
		return this.isVisible();
	}

	@Override
	public PropertyType getPropertyType() {
		if (this.tabbledPane != null && this.tabbledPane.getTabCount() > 0) {
			return ((IProperty) this.tabbledPane.getSelectedComponent()).getPropertyType();
		} else {
			return null;
		}
	}

	@Override
	public int getPropertyCount() {
		return tabbledPane.getTabCount();
	}

	@Override
	public IProperty getPropertyByIndex(int index) {
		if (index < 0 || index > getPropertyCount()) {
			throw new IndexOutOfBoundsException(String.valueOf(index));
		}
		return ((IProperty) tabbledPane.getTabComponentAt(index));
	}

	@Override
	public void setSelectedProperty(int index) {
		if (index < 0 || index > getPropertyCount()) {
			throw new IndexOutOfBoundsException(String.valueOf(index));
		}
		tabbledPane.setSelectedIndex(index);
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
			if (tabbledPane.getComponentAt(i) == property) {
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
