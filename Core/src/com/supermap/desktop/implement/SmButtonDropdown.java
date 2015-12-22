package com.supermap.desktop.implement;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.plaf.metal.MetalComboBoxIcon;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.ICtrlAction;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.XMLButtonDropdown;
import com.supermap.desktop.ui.XMLCommand;
import com.supermap.desktop.utilties.PathUtilties;

public class SmButtonDropdown extends JComponent implements IBaseItem {

	private static final long serialVersionUID = 1L;
	private transient IForm formClass = null;
	private transient XMLCommand xmlCommand = null;

	private transient ControlButton displayButton;
	private transient ControlButton arrowButton;
	private transient SmPopupMenu popupMenu;

	public SmButtonDropdown(IForm formClass, XMLCommand xmlCommand, JComponent parent) {
		super.setToolTipText(xmlCommand.getTooltip());

		displayButton = new ControlButton(this);
		displayButton.setPreferredSize(new Dimension(32, 24));
		String[] pathPrams = new String[] { PathUtilties.getRootPathName(), xmlCommand.getImageFile() };
		String path = PathUtilties.combinePath(pathPrams, false);
		File file = new File(path);
		if (file.exists() && file.isFile()) {
			displayButton.setIcon(new ImageIcon(path));
		} else {
			displayButton.setText(xmlCommand.getLabel());
		}
		displayButton.setToolTipText(xmlCommand.getLabel());

		arrowButton = new ControlButton(this);
		arrowButton.setIcon(new MetalComboBoxIcon());
		arrowButton.setPreferredSize(new Dimension(16, displayButton.getPreferredSize().height));
		Insets insets = arrowButton.getMargin();
		arrowButton.setMargin(new Insets(insets.top, 1, insets.bottom, 1));

		XMLButtonDropdown xmlButtonDropdown = (XMLButtonDropdown) xmlCommand;
		popupMenu = new SmPopupMenu(xmlButtonDropdown.getDropdownMenu());

		arrowButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showPopupMenu();
			}

		});
		setupLayout();

		this.formClass = formClass;
		this.xmlCommand = xmlCommand;

		try {
			ICtrlAction ctrlAction = Application.getActiveApplication().getCtrlAction(xmlCommand.getPluginInfo().getBundleName(),
					xmlCommand.getCtrlActionClass());
			if (ctrlAction == null) {
				ctrlAction = CommonToolkit.CtrlActionWrap.getCtrlAction(xmlCommand, this, this.formClass);
			}

			if (ctrlAction != null) {
				ctrlAction.setFormClass(this.formClass);
				setCtrlAction(ctrlAction);
				Application.getActiveApplication().setCtrlAction(xmlCommand.getPluginInfo().getBundleName(), xmlCommand.getCtrlActionClass(), ctrlAction);
			} else {
				this.setToolTipText(this.getToolTipText() + CommonProperties.getString("String_UnDo"));
			}

			displayButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					item_ActionPerformed();
				}
			});

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 获取m_popupMenu
	 * 
	 * @return
	 */
	protected SmPopupMenu getPopupMenu() {
		return this.popupMenu;
	}

	/**
	 * 获取m_arrow
	 * 
	 * @return
	 */
	protected ControlButton getDisplayButton() {
		return this.displayButton;
	}

	/**
	 * 获取m_arrow
	 * 
	 * @return
	 */
	protected ControlButton getArrowButton() {
		return this.arrowButton;
	}

	/** 按钮布局 */
	protected void setupLayout() {
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(gbl);

		c.weightx = 100;
		c.weighty = 100;
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		gbl.setConstraints(displayButton, c);
		add(displayButton);

		c.weightx = 0;
		c.gridx++;
		gbl.setConstraints(arrowButton, c);
		add(arrowButton);
	}

	private void showPopupMenu() {
		try {
			int y = (int) displayButton.getLocation().getY() + displayButton.getHeight();
			popupMenu.show(displayButton, 0, y);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void item_ActionPerformed() {
		try {
			if (this.getCtrlAction() != null) {
				this.getCtrlAction().setCaller(this);
				this.getCtrlAction().run();
			} else if (this.getPopupMenu() != null) {
				showPopupMenu();
			} else {
				Application.getActiveApplication().getOutput().output("CtrlAction Unimplemented!");
				JOptionPane.showMessageDialog(null, this.xmlCommand.getCtrlActionClass() + " Unimplemented!");
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public void Button_PropertyChange(PropertyChangeEvent evt) {
		// do nothing
	}

	@Override
	public boolean isEnabled() {
		return this.getDisplayButton().isEnabled();
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.getDisplayButton().setEnabled(enabled);
		this.getArrowButton().setEnabled(enabled);
	}

	@Override
	public boolean isChecked() {
		return this.getDisplayButton().isSelected();
	}

	@Override
	public void setChecked(boolean checked) {
		this.getDisplayButton().setSelected(checked);
		this.getArrowButton().setSelected(checked);
	}

	@Override
	public boolean isVisible() {
		return super.isVisible();
	}

	@Override
	public void setVisible(boolean enabled) {
		super.setVisible(enabled);
	}

	@Override
	public int getIndex() {
		return this.xmlCommand.getIndex();
	}

	@Override
	public void setIndex(int index) {
		this.xmlCommand.setIndex(index);
	}

	@Override
	public String getID() {
		return this.xmlCommand.getID();
	}

	@Override
	public ICtrlAction getCtrlAction() {
		return this.xmlCommand.getCtrlAction();
	}

	@Override
	public void setCtrlAction(ICtrlAction ctrlAction) {
		this.xmlCommand.setCtrlAction(ctrlAction);
	}

}
