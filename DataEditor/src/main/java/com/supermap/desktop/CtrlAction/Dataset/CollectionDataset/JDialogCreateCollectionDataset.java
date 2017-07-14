package com.supermap.desktop.CtrlAction.Dataset.CollectionDataset;

import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.CharsetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;

/**
 * Created by xie on 2017/7/13.
 * 创建数据集集合主界面
 */
public class JDialogCreateCollectionDataset extends SmDialog {
	private JTable tableDatasetDisplay;
	private JLabel labelDatasource;
	private DatasourceComboBox textFieldDatasource;
	private JLabel labelDatasetName;
	private JTextField textFieldDatasetName;
	private JLabel labelCharset;
	private CharsetComboBox charsetComboBox;
	private JToolBar toolBar;
	private JButton buttonAddDataset;
	private JButton buttonSelectAll;
	private JButton buttonInvertSelect;
	private JButton buttonDelete;
	private JButton buttonMoveToFirst;
	private JButton buttonMoveToForeword;
	private JButton buttonMoveToNext;
	private JButton buttonMoveToLast;
	private JButton buttonRefresh;
	private JButton buttonOK;
	private JButton buttonCancel;

	public JDialogCreateCollectionDataset() {
		super();
		init();
	}

	public void init() {
		initComponents();
		initResources();
		initLayout();
		registEvents();
	}

	private void initComponents() {
		this.tableDatasetDisplay = new JTable();
		this.labelDatasource = new JLabel();
		this.textFieldDatasource = new DatasourceComboBox();
		this.labelDatasetName = new JLabel();
		this.textFieldDatasetName = new JTextField();
		this.labelCharset = new JLabel();
		this.charsetComboBox = new CharsetComboBox();
		this.toolBar = new JToolBar();
		this.buttonAddDataset = new JButton();
		this.buttonSelectAll = new JButton();
		this.buttonInvertSelect = new JButton();
		this.buttonMoveToFirst = new JButton();
		this.buttonMoveToForeword = new JButton();
		this.buttonMoveToNext = new JButton();
		this.buttonMoveToLast= new JButton();
		this.buttonOK = ComponentFactory.createButtonOK();
		this.buttonCancel = ComponentFactory.createButtonCancel();
		initToolBar();
	}

	private void initToolBar() {
		this.buttonAddDataset.setIcon(null);
	}

	private void initResources() {
		this.labelDatasource.setText("");
		this.labelDatasetName.setText("");
		this.labelCharset.setText("");
	}

	private void registEvents() {

	}

	private void initLayout() {

	}
}
