package com.supermap.desktop.CtrlAction.Dataset.CollectionDataset;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.CharsetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.utilities.CoreResources;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Created by xie on 2017/7/13.
 * 创建数据集集合主界面
 */
public class JDialogCreateCollectionDataset extends SmDialog {
	private JTable tableDatasetDisplay;
	private JLabel labelDatasource;
	private DatasourceComboBox datasourceComboBox;
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
	private JPanel panelTableInfo;
	private JPanel panelBasicInfo;
	private JPanel contentPane;
	private JScrollPane scrollPane;


	private final int COLUMN_CAPTION = 0;
	private final int COLUMN_NAME = 1;
	private final int COLUMN_STATE = 2;


	public JDialogCreateCollectionDataset() {
		super();
		init();
	}

	public void init() {
		initComponents();
		initResources();
		initLayout();
		registEvents();
		this.setSize(new Dimension(800, 450));
		this.setLocationRelativeTo(null);
	}

	private void initComponents() {
		this.panelTableInfo = new JPanel();
		this.panelBasicInfo = new JPanel();
		this.scrollPane = new JScrollPane();
		this.contentPane = new JPanel();
		this.setContentPane(contentPane);
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.tableDatasetDisplay = new JTable();
		this.labelDatasource = new JLabel();
		this.datasourceComboBox = new DatasourceComboBox();
		this.labelDatasetName = new JLabel();
		this.textFieldDatasetName = new JTextField();
		this.labelCharset = new JLabel();
		this.charsetComboBox = new CharsetComboBox();
		this.toolBar = new JToolBar();
		this.buttonDelete = new JButton();
		this.buttonAddDataset = new JButton();
		this.buttonSelectAll = new JButton();
		this.buttonInvertSelect = new JButton();
		this.buttonMoveToFirst = new JButton();
		this.buttonMoveToForeword = new JButton();
		this.buttonMoveToNext = new JButton();
		this.buttonMoveToLast = new JButton();
		this.buttonRefresh = new JButton();
		this.buttonOK = ComponentFactory.createButtonOK();
		this.buttonCancel = ComponentFactory.createButtonCancel();
		initToolBar();
	}

	private void initToolBar() {
		this.toolBar.setFloatable(false);
		this.toolBar.add(buttonAddDataset);
		this.toolBar.addSeparator();
		this.toolBar.add(buttonSelectAll);
		this.toolBar.add(buttonInvertSelect);
		this.toolBar.addSeparator();
		this.toolBar.add(buttonDelete);
		this.toolBar.addSeparator();
		this.toolBar.add(buttonMoveToFirst);
		this.toolBar.add(buttonMoveToForeword);
		this.toolBar.add(buttonMoveToNext);
		this.toolBar.add(buttonMoveToLast);
		this.toolBar.add(buttonRefresh);
	}

	public void setComponentName() {
		ComponentUIUtilities.setName(this.buttonAddDataset, "JDialogCreateCollectionDataset_buttonAddDataset");
		ComponentUIUtilities.setName(this.buttonDelete, "JDialogCreateCollectionDataset_buttonDelete");
		ComponentUIUtilities.setName(this.buttonSelectAll, "JDialogCreateCollectionDataset_buttonSelectAll");
		ComponentUIUtilities.setName(this.buttonInvertSelect, "JDialogCreateCollectionDataset_buttonInvertSelect");
		ComponentUIUtilities.setName(this.buttonRefresh, "JDialogCreateCollectionDataset_buttonRefresh");
		ComponentUIUtilities.setName(this.buttonMoveToFirst, "JDialogCreateCollectionDataset_buttonMoveToFirst");
		ComponentUIUtilities.setName(this.buttonMoveToForeword, "JDialogCreateCollectionDataset_buttonMoveToForeword");
		ComponentUIUtilities.setName(this.buttonMoveToNext, "JDialogCreateCollectionDataset_buttonMoveToNext");
		ComponentUIUtilities.setName(this.buttonMoveToLast, "JDialogCreateCollectionDataset_buttonMoveToLast");
		ComponentUIUtilities.setName(this.buttonOK, "JDialogCreateCollectionDataset_buttonOK");
		ComponentUIUtilities.setName(this.buttonCancel, "JDialogCreateCollectionDataset_buttonCancel");
		ComponentUIUtilities.setName(this.toolBar, "JDialogCreateCollectionDataset_toolBar");
		ComponentUIUtilities.setName(this.scrollPane, "JDialogCreateCollectionDataset_scrollPane");
		ComponentUIUtilities.setName(this.contentPane, "JDialogCreateCollectionDataset_contentPane");
	}

	private void initResources() {
		this.labelDatasource.setText(CommonProperties.getString(CommonProperties.Label_Datasource));
		this.labelDatasetName.setText(CommonProperties.getString(CommonProperties.Label_Dataset));
		this.labelCharset.setText(ControlsProperties.getString("String_LabelCharset"));
		this.buttonAddDataset.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_AddItem.png"));
		this.buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
		this.buttonInvertSelect.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
		this.buttonDelete.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Delete.png"));
		this.buttonMoveToFirst.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_MoveFirst.png"));
		this.buttonMoveToForeword.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_MoveUp.png"));
		this.buttonMoveToNext.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_MoveDown.png"));
		this.buttonMoveToLast.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_MoveLast.png"));
		this.buttonRefresh.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Refresh.png"));
	}

	private void initLayout() {
		initPanelTableInfoLayout();
		initPanelBasicInfoLayout();
		JPanel panelButton = new JPanel();
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(this.buttonOK, new GridBagConstraintsHelper(5, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(0, 5, 10, 10).setWeight(0, 0));
		panelButton.add(this.buttonCancel, new GridBagConstraintsHelper(6, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(0, 5, 10, 10).setWeight(0, 0));
		this.contentPane.add(this.panelTableInfo, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setWeight(0.7, 1));
		this.contentPane.add(this.panelBasicInfo, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setWeight(0.3, 1));
//		this.contentPane.add(panelButton, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
	}

	private void initPanelBasicInfoLayout() {
		this.panelBasicInfo.setLayout(new GridBagLayout());
		this.panelBasicInfo.add(this.labelDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
		this.panelBasicInfo.add(this.datasourceComboBox, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
		this.panelBasicInfo.add(this.labelDatasetName, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
		this.panelBasicInfo.add(this.textFieldDatasetName, new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
		this.panelBasicInfo.add(this.labelCharset, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
		this.panelBasicInfo.add(this.charsetComboBox, new GridBagConstraintsHelper(1, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
		this.panelBasicInfo.add(new JPanel(), new GridBagConstraintsHelper(0, 3, 3, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		this.panelBasicInfo.setBorder(new LineBorder(Color.GRAY));
	}

	private void initPanelTableInfoLayout() {
		this.panelTableInfo.setLayout(new GridBagLayout());
		this.panelTableInfo.add(this.toolBar, new GridBagConstraintsHelper(0, 0, 3, 1).setAnchor(GridBagConstraints.WEST).setInsets(5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
		this.panelTableInfo.add(this.scrollPane, new GridBagConstraintsHelper(0, 1, 3, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
		this.scrollPane.setViewportView(this.tableDatasetDisplay);
	}

	private void registEvents() {

	}

}
