package com.supermap.desktop.newtheme;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.FieldInfo;
import com.supermap.data.FieldType;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SQLExpressionDialog;
import com.supermap.desktop.ui.controls.SmDialog;

public class ThemeGraphAddItemDialog extends SmDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JScrollPane panelList;
	private JList<CheckableItem> listExpressions;
	private JButton buttonAddExpression;
	private JButton buttonSure;
	private JButton buttonCancel;
	private DatasetVector datasetVector;
	private MouseAdapter listExpressionListener = new ListExpressionListener();
	private ArrayList<String> resultList = new ArrayList<String>();
	private ActionListener buttonAction = new ButtonAction();
	private ArrayList<String> themeExpressionList = new ArrayList<String>();

	public ThemeGraphAddItemDialog(DatasetVector datasetVector, ArrayList<String> list) {
		this.datasetVector = datasetVector;
		setList(list);
		initComponents();
		initResources();
		registActionListener();
	}

	private void registActionListener() {
		this.listExpressions.addMouseListener(this.listExpressionListener);
		this.buttonSure.addActionListener(this.buttonAction);
		this.buttonCancel.addActionListener(this.buttonAction);
		this.buttonAddExpression.addActionListener(this.buttonAction);
	}

	private void unRegistActionListener() {
		this.listExpressions.removeMouseListener(this.listExpressionListener);
		this.buttonSure.removeActionListener(this.buttonAction);
		this.buttonCancel.removeActionListener(this.buttonAction);
		this.buttonAddExpression.removeActionListener(this.buttonAction);
	}

	private void initResources() {
		this.setTitle(MapViewProperties.getString("String_Title_Add"));
		this.buttonAddExpression.setText(MapViewProperties.getString("String_Combobox_Expression"));
		this.buttonSure.setText(CommonProperties.getString("String_Button_OK"));
		this.buttonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
	}

	private void initComponents() {
		this.panelList = new JScrollPane();
		this.buttonAddExpression = new JButton();
		this.buttonSure = new JButton();
		this.buttonCancel = new JButton();
		this.panelList.setViewportView(this.listExpressions);
		//@formatter:off
		this.setSize(260,300);
		this.setLayout(new GridBagLayout());
		this.add(this.panelList,           new GridBagConstraintsHelper(0, 0, 3, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(3).setWeight(3, 3));
		this.add(this.buttonAddExpression, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,3,0,5).setWeight(1, 0));
		this.add(this.buttonSure,          new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.EAST).setInsets(0,3,0,5).setWeight(1, 0));
		this.add(this.buttonCancel,        new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.EAST).setInsets(0,3,0,5).setWeight(1, 0));
		//@formatter:on
	}

	public void setList(ArrayList<String> list) {
		int count = datasetVector.getFieldCount();
		for (int j = 0; j < count; j++) {
			FieldInfo fieldInfo = datasetVector.getFieldInfos().get(j);
			if (fieldInfo.getType() == FieldType.INT16 || fieldInfo.getType() == FieldType.INT32 || fieldInfo.getType() == FieldType.INT64
					|| fieldInfo.getType() == FieldType.DOUBLE || fieldInfo.getType() == FieldType.SINGLE) {
				String item = datasetVector.getName() + "." + fieldInfo.getName();
				this.themeExpressionList.add(item);
			}
		}
		for (int i = 0; i < list.size(); i++) {
			this.themeExpressionList.remove(list.get(i));
		}
		initListExpressions();
	}

	private void initListExpressions() {
		DefaultListModel listModel = new DefaultListModel();
		this.listExpressions = new JList<CheckableItem>();
		this.listExpressions.setModel(listModel);
		addItemToList(listModel);
		this.listExpressions.setCellRenderer(new CheckListRenderer());
		this.listExpressions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.listExpressions.setBorder(new EmptyBorder(0, 4, 0, 0));
	}

	private void addItemToList(DefaultListModel listModel) {
		int n = themeExpressionList.size();
		for (int i = 0; i < n; i++) {
			CheckableItem item = new CheckableItem(themeExpressionList.get(i));
			listModel.addElement(item);
		}
	}

	class ButtonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttonSure) {
				setDialogResult(dialogResult.OK);
				disposeDialog();
			}
			if (e.getSource() == buttonCancel) {
				disposeDialog();
			}
			if (e.getSource() == buttonAddExpression) {
				getSqlExpression();
			}
		}

		private void disposeDialog() {
			unRegistActionListener();
			ThemeGraphAddItemDialog.this.setVisible(false);
			ThemeGraphAddItemDialog.this.dispose();
			return;
		}
	}

	class ListExpressionListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			int index = listExpressions.locationToIndex(e.getPoint());
			CheckableItem item = (CheckableItem) listExpressions.getModel().getElementAt(index);
			item.setSelected(!item.isSelected());
			if (item.isSelected) {
				resultList.add(item.toString());
			}else {
				resultList.remove(item.toString());
			}
			Rectangle rect = listExpressions.getCellBounds(index, index);
			listExpressions.repaint(rect);
		}
	}

	class CheckListRenderer extends JCheckBox implements ListCellRenderer<Object> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public CheckListRenderer() {
			setBackground(UIManager.getColor("List.textBackground"));
			setForeground(UIManager.getColor("List.textForeground"));
		}

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
			setEnabled(list.isEnabled());
			setSelected(((CheckableItem) value).isSelected());
			setFont(list.getFont());
			setText(value.toString());
			return this;
		}
	}


	class CheckableItem {
		private String str;
		private boolean isSelected;

		public CheckableItem(String str) {
			this.str = str;
			this.isSelected = false;
		}

		public void setSelected(boolean b) {
			this.isSelected = b;
		}

		public boolean isSelected() {
			return this.isSelected;
		}

		public String toString() {
			return str;
		}
	}

	public ArrayList<String> getResultList() {
		return resultList;
	}

	public void setResultList(ArrayList<String> resultList) {
		this.resultList = resultList;
	}

	/**
	 * 获取表达式项
	 *
	 * @param jComboBoxField
	 */
	private void getSqlExpression() {
		SQLExpressionDialog sqlDialog = new SQLExpressionDialog();
		Dataset[] datasets = new Dataset[1];
		datasets[0] = datasetVector;
		ArrayList<FieldType> fieldTypes = new ArrayList<FieldType>();
		fieldTypes.add(FieldType.INT16);
		fieldTypes.add(FieldType.INT32);
		fieldTypes.add(FieldType.INT64);
		fieldTypes.add(FieldType.DOUBLE);
		fieldTypes.add(FieldType.SINGLE);

		DialogResult dialogResult = sqlDialog.showDialog(datasets, fieldTypes, "");
		if (dialogResult == DialogResult.OK) {
			String filter = sqlDialog.getQueryParameter().getAttributeFilter();
			if (null != filter && !filter.isEmpty()) {
				CheckableItem item = new CheckableItem(filter);
				item.setSelected(true);
				resultList.add(filter);
				DefaultListModel model = (DefaultListModel) listExpressions.getModel();
				model.addElement(item);
			}
		}

	}
}
