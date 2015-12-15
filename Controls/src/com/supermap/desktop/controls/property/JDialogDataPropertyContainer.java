package com.supermap.desktop.controls.property;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Window;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.supermap.desktop.Interface.IProperty;
import com.supermap.desktop.Interface.IPropertyManager;
import com.supermap.desktop.controls.ControlsProperties;

public class JDialogDataPropertyContainer extends JDialog implements IPropertyManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<AbstractPropertyControl> controls = new ArrayList<AbstractPropertyControl>();
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
	public JDialogDataPropertyContainer(Window mainFrame) {
		super(mainFrame);
		setBounds(100, 100, 750, 450);
		this.setContentPane(new JPanel(new BorderLayout()));
		this.setLocationRelativeTo(null);
		setModal(false);
		this.controls = new ArrayList<AbstractPropertyControl>();
		this.tabbledPane.addChangeListener(this.tabbledPaneChangeListener);
	}

	@Override
	public void setProperty(IProperty[] properties) {
		// 设置当前选中视图
		// Tab 子项不为0，表明上一个属性集合是 Tab 页，从中取出当前选中
		if (this.tabbledPane.getTabCount() != 0) {
			this.currentProperty = (IProperty) this.tabbledPane.getSelectedComponent();
		}

		this.setTitle(ControlsProperties.getString("String_Property"));
		this.controls.clear();
		this.getContentPane().removeAll();
		this.tabbledPane.removeAll();

		updateControls(properties);

		if (isShowing()) {
			((JPanel) this.getContentPane()).updateUI();
		}
	}

	@Override
	public void refreshData() {
		for (AbstractPropertyControl control : controls) {
			control.refreshData();
		}
	}

	private void tabbledPaneChange() {
		int selectedIndex = this.tabbledPane.getSelectedIndex();

		if (selectedIndex > -1) {
			Component component = this.tabbledPane.getComponentAt(selectedIndex);

			if (component instanceof AbstractPropertyControl) {
				((AbstractPropertyControl) component).refreshData();
			}
		}
	}

	private void updateControls(IProperty[] properties) {
		if (properties != null) {
			if (properties.length == 1) {
				this.currentProperty = properties[0];
				this.controls.add((AbstractPropertyControl) this.currentProperty);
				this.setTitle(this.currentProperty.getPropertyName());
				this.getContentPane().add((AbstractPropertyControl) this.currentProperty, BorderLayout.CENTER);
			} else if (properties.length > 1) {
				for (IProperty property : properties) {
					this.controls.add((AbstractPropertyControl) property);
					this.tabbledPane.addTab(property.getPropertyName(), (AbstractPropertyControl) property);
					this.getContentPane().add(this.tabbledPane, BorderLayout.CENTER);
				}

				setTabbledPaneSelectedPropertyControl(this.currentProperty);
			}
		}
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
					this.tabbledPane.setSelectedIndex(i);
					break;
				}
			}
		}
	}
}
