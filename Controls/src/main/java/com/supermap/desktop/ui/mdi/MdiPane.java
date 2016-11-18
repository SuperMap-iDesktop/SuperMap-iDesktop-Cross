package com.supermap.desktop.ui.mdi;

import com.supermap.desktop.ui.mdi.plaf.MdiPaneUI;

import java.util.ArrayList;
import java.util.List;

import javax.accessibility.Accessible;
import javax.swing.JComponent;
import javax.swing.UIManager;

// @formatter:off
/**
 * 
 * @author highsad
 *
 */
// @formatter:on
public class MdiPane extends JComponent implements Accessible {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @see #getUIClassID
	 */
	private static final String uiClassID = "MdiPaneUI";
	private List<MdiGroup> groups;
	private MdiGroup selectedGroup;

	public MdiPane() {
		this.groups = new ArrayList<MdiGroup>();
		updateUI();
	}

	/**
	 * 创建一个属于该 MdiPane 的 Group
	 * 
	 * @return
	 */
	public MdiGroup createGroup() {
		MdiGroup group = new MdiGroup(this);
		this.groups.add(group);
		return group;
	}

	// L&F

	public MdiPaneUI getUI() {
		return (MdiPaneUI) ui;
	}

	public void setUI(MdiPaneUI ui) {
		if (this.ui != ui) {
			super.setUI(ui);
			repaint();
		}
	}

	@Override
	public void updateUI() {
		setUI((MdiPaneUI) UIManager.getUI(this));
	}

	// MdiPane

	public MdiPage getSelectedPage() {
		return this.selectedGroup == null ? null : this.selectedGroup.getActivePage();
	}

	/**
	 * 如果当前 pane 只有一个 group，就添加到这个 group 里，如果没有或者有多个，则添加为新的 group
	 * 
	 * @param content
	 * @return
	 */
	public MdiGroup add(MdiPage content) {
		MdiGroup group = this.groups.size() == 1 ? this.groups.get(0) : createGroup();

		if (group != null) {
			group.addPage(content);
		}
		return group;
	}

	/**
	 * @param content
	 * @param group
	 */
	public void add(MdiPage content, MdiGroup group) {
		if (!this.groups.contains(group)) {
			this.groups.add(group);
			group.addPage(content);
		}
	}
}
