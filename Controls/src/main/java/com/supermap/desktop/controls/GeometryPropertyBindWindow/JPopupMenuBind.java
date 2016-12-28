package com.supermap.desktop.controls.GeometryPropertyBindWindow;

import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.Interface.*;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.InternalImageIconFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

/**
 * Created by xie on 2016/11/10.
 * 关联浏览弹出窗口
 */
public class JPopupMenuBind extends JPopupMenu implements PopupMenuListener {
	private JScrollPane scrollPane;
	private JList listForms;
	private JButton buttonSelectAll;
	private JButton buttonSelectInverse;
	private JButton buttonOk;
	private JPanel panelButton;
	private JCheckBox checkBoxIsQueryWhileSelectedColumn;
	private JLabel labelTitle;
	private BindHandler handler = BindHandler.getInstance();
	private static JPopupMenuBind popupMenubind;

	private ActionListener selectAllListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			int size = listForms.getModel().getSize();
			for (int i = 0; i < size; i++) {
				CheckableItem item = (CheckableItem) listForms.getModel().getElementAt(i);
				item.setSelected(true);
				repaintListItem(i);
			}
		}
	};

	private ActionListener selectInverseListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			int size = listForms.getModel().getSize();
			for (int i = 0; i < size; i++) {
				CheckableItem item = (CheckableItem) listForms.getModel().getElementAt(i);
				item.setSelected(!item.isSelected());
				repaintListItem(i);
			}
		}
	};

	private ActionListener okListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			handler.removeFormMapsBind();
			handler.removeFormTabularsBind();
			handler.removeFormMapsAndFormTabularsBind();

			if (canRelated()) {
				addFormList();
				showView();
				BindUtilties.resetMDILayout();
			}
			JPopupMenuBind.this.setVisible(false);
		}
	};

	private void addFormList() {
		IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
		DefaultListModel model = (DefaultListModel) listForms.getModel();
		int size = model.getSize();
		for (int i = 0; i < size; i++) {
			CheckableItem item = (CheckableItem) model.getElementAt(i);
			if (item.isSelected()) {
				IForm form = formManager.get(i);
				if (form instanceof IFormMap && !handler.getFormMapList().contains(form)) {
					handler.getFormMapList().add(form);
				} else if (form instanceof IFormTabular && !handler.getFormTabularList().contains(form)) {
					handler.getFormTabularList().add(form);
				}
			}
		}
	}

	// 判断是否关联，当选中的关联窗口数量小于2的时候，不允许关联
	private boolean canRelated() {
		boolean canRelated = false;
		DefaultListModel model = (DefaultListModel) listForms.getModel();
		int count = 0;

		for (int i = 0; i < model.size(); i++) {
			CheckableItem item = (CheckableItem) model.getElementAt(i);
			if (item.isSelected()) {
				count++;
			}

			if (count >= 2) {
				canRelated = true;
				break;
			}
		}
		return canRelated;
	}

	private void showView() {
		final int formMapSize = handler.getFormMapList().size();
		int formTabularSize = handler.getFormTabularList().size();

		if (formMapSize > 0 && formTabularSize == 0) {
			handler.bindFormMaps();
		} else if (formTabularSize > 0 && formMapSize == 0) {
			handler.bindFormTabulars();
		} else if (formMapSize != 0 && formTabularSize != 0) {
			handler.bindFormMapsAndFormTabulars();
		}
	}

	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
		registEvents();
	}

	@Override
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		removeEvents();
	}

	@Override
	public void popupMenuCanceled(PopupMenuEvent e) {

	}


	public void dispose() {
		if (null != labelTitle) {
			labelTitle = null;
		}
		if (null != listForms) {
			listForms = null;
		}
		if (null != buttonOk) {
			buttonOk = null;
		}
		if (null != buttonSelectAll) {
			buttonSelectAll = null;
		}
		if (null != buttonSelectInverse) {
			buttonSelectInverse = null;
		}
		if (null != panelButton) {
			panelButton = null;
		}
		if (null != scrollPane) {
			scrollPane = null;
		}

	}

	private MouseListener listFormsMouseListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			int index = listForms.locationToIndex(e.getPoint());
			CheckableItem item = (CheckableItem) listForms.getModel().getElementAt(index);
			item.setSelected(!item.isSelected());
			repaintListItem(index);
		}
	};

	public static JPopupMenuBind instance() {
		if (null == popupMenubind) {
			popupMenubind = new JPopupMenuBind();
		}
		return popupMenubind;
	}

	public void init() {
		initComponents();
		initLayout();
		registEvents();
		initResources();
		addPopupMenuListener(this);
		this.setPopupSize(480, 270);
	}

	private void repaintListItem(int i) {
		Rectangle rect = listForms.getCellBounds(i, i);
		listForms.repaint(rect);
	}

	private void initComponents() {
		this.scrollPane = new JScrollPane();
		this.listForms = new JList();
		this.buttonSelectAll = ComponentFactory.createButtonSelectAll();
		this.buttonSelectInverse = ComponentFactory.createButtonSelectInverse();
		this.buttonOk = ComponentFactory.createButtonOK();
		this.checkBoxIsQueryWhileSelectedColumn = new JCheckBox();
		this.labelTitle = new JLabel();
		this.panelButton = new JPanel();
		initListForms();
	}

	private void initListForms() {
		DefaultListModel listModel = new DefaultListModel();
		this.listForms = new JList();
		this.listForms.setModel(listModel);
		addItemToList(listModel);
		this.listForms.setCellRenderer(new CheckListRenderer());
		this.listForms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.listForms.setBorder(new EmptyBorder(0, 4, 0, 0));
	}

	private void addItemToList(DefaultListModel listModel) {
		IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
		int size = formManager.getCount();
		for (int i = 0; i < size; i++) {
			IForm form = formManager.get(i);

			if (!(form instanceof IFormMap) && !(form instanceof IFormTabular)) {
				continue;
			}
			CheckableItem item = new CheckableItem();
			item.setStr(form.getText());
			item.setForm(form);
			item.setSelected(this.handler.getFormMapList().contains(form) || this.handler.getFormTabularList().contains(form));
			listModel.addElement(item);
		}
	}

	private void registEvents() {
		removeEvents();
		this.buttonSelectAll.addActionListener(this.selectAllListener);
		this.buttonSelectInverse.addActionListener(this.selectInverseListener);
		this.buttonOk.addActionListener(this.okListener);
		this.listForms.addMouseListener(this.listFormsMouseListener);
		checkBoxIsQueryWhileSelectedColumn.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				GlobalParameters.setIsHeadClickedSelectedColumn(checkBoxIsQueryWhileSelectedColumn.isSelected());
			}
		});
	}

	public void removeEvents() {
		if (null != buttonSelectInverse && null != buttonSelectAll && null != buttonOk && null != listForms) {
			this.buttonSelectAll.removeActionListener(this.selectAllListener);
			this.buttonSelectInverse.removeActionListener(this.selectInverseListener);
			this.buttonOk.removeActionListener(this.okListener);
			this.listForms.removeMouseListener(this.listFormsMouseListener);
		}
	}

	private void initLayout() {
		this.removeAll();
		this.panelButton.setLayout(new GridBagLayout());
		this.panelButton.add(this.checkBoxIsQueryWhileSelectedColumn, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 10, 0).setFill(GridBagConstraints.NONE).setWeight(1, 0));
		this.panelButton.add(this.buttonSelectAll, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 10, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
		this.panelButton.add(this.buttonSelectInverse, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 10, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
		this.panelButton.add(this.buttonOk, new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setInsets(5, 5, 10, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
		this.setLayout(new GridBagLayout());
		this.add(this.labelTitle, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setWeight(0, 0));
		this.add(this.scrollPane, new GridBagConstraintsHelper(0, 1, 1, 5).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		this.add(this.panelButton, new GridBagConstraintsHelper(0, 6, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
		this.scrollPane.setViewportView(listForms);
	}

	private void initResources() {
		this.labelTitle.setText(CoreProperties.getString("String_Bind"));
		checkBoxIsQueryWhileSelectedColumn.setText(ControlsProperties.getString("String_IsBindQueryWhileClickHead"));
	}

	class CheckListRenderer implements ListCellRenderer<Object> {

		public CheckListRenderer() {
		}

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
			setEnabled(list.isEnabled());
			return this.initRenderPanel(list, value);
		}

		private JPanel initRenderPanel(JList list, Object value) {
			JPanel panelContent = new JPanel();
			JCheckBox checkBox = new JCheckBox();
			DataCell dataCell = new DataCell();
			if (value instanceof CheckableItem) {
				checkBox.setSelected(((CheckableItem) value).isSelected());
				if (((CheckableItem) value).getForm() instanceof IFormMap) {
					dataCell.initDataImage(new ImageIcon(InternalImageIconFactory.MAPS.getImage()), value.toString());
				} else if (((CheckableItem) value).getForm() instanceof IFormTabular) {
					String path = CommonToolkit.DatasetImageWrap.getImageIconPath(DatasetType.TABULAR);
					URL url = JPopupMenuBind.class.getResource(path);
					dataCell.initDataImage(new ImageIcon(url), value.toString());
				}
			}

			panelContent.setLayout(new GridBagLayout());
			panelContent.setLayout(new FlowLayout(FlowLayout.LEFT));
			panelContent.add(checkBox);
			panelContent.add(dataCell);
			dataCell.setFont(list.getFont());
			setComponentTheme(panelContent);
			setComponentTheme(checkBox);
			setComponentTheme(dataCell);
			return panelContent;
		}

		private void setComponentTheme(JComponent component) {
			component.setBackground(UIManager.getColor("List.textBackground"));
			component.setForeground(UIManager.getColor("List.textForeground"));
		}
	}

	class CheckableItem {
		private String str;
		private boolean isSelected;
		private IForm form;

		public CheckableItem() {
		}

		public IForm getForm() {
			return form;
		}

		public void setForm(IForm form) {
			this.form = form;
		}

		public void setSelected(boolean b) {
			this.isSelected = b;
		}

		public boolean isSelected() {
			return this.isSelected;
		}

		public String getStr() {
			return str;
		}

		public void setStr(String str) {
			this.str = str;
		}

		public String toString() {
			return str;
		}
	}

}
