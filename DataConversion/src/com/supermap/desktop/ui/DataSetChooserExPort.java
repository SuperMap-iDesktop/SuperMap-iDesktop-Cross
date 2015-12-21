package com.supermap.desktop.ui;

import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.Workspace;
import com.supermap.data.conversion.ExportSetting;
import com.supermap.desktop.Application;
import com.supermap.desktop.ExportFileInfo;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.CommonListCellRenderer;
import com.supermap.desktop.ui.controls.DatasetChooser;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.util.ChildExportModel;
import com.supermap.desktop.util.CommonFunction;
import com.supermap.desktop.util.DatasetUtil;
import com.supermap.desktop.util.ExportFunction;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

/**
 * @author Administrator 数据导入主体界面
 */
public class DataSetChooserExPort extends JDialog {
	public DataSetChooserExPort(Frame owner, boolean modal) {
		super(owner, modal);
		initCompanent();
	}

	/**
	 * 
	 * @wbp.parser.constructor
	 */
	public DataSetChooserExPort(JDialog owner, JTable exportTable) {
		super(owner, true);
		this.frame = (DataExportFrame) owner;
		this.exportTable = exportTable;
		initCompanent();
	}

	public DataSetChooserExPort(JTable exportTable) {
		this.exportTable = exportTable;
		initCompanent();
	}

	/**
	
	 * 
	
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private transient ChildExportModel model;
	private JButton buttonSelectAll = new JButton();
	private JButton buttonInvertSelect = new JButton();
	private JButton buttonOk = new JButton("string_button_sure");
	private JButton buttonQuit = new JButton("string_button_quit");
	private JPanel panelTable = new JPanel();
	private JTextField textFieldShearch;
	private transient WorkspaceTree workspaceTree;
	private JTextField textFieldPath = new JTextField();
	private ArrayList<ExportFileInfo> childExports;
	private JTable exportTable;
	private transient DataExportFrame frame;
	private JLabel labelPath = new JLabel("String_FormDatasetBrowse_ToolStripLabelPath");
	private JLabel labelScense = new JLabel("String_FormDatasetBrowse_ToolStripLabelDisplayType");
	private transient DatasetComboBox comboBoxScense = new DatasetComboBox();
	private transient WorkspaceMouseAdapter mouseAdapter = new WorkspaceMouseAdapter();
	private JLabel labelSearch = new JLabel();

	/**
	 * 
	 * Create the frame.
	 */

	private ArrayList<ExportFileInfo> initExportFileInfo(Datasource datasource) {
		ArrayList<ExportFileInfo> tempExports = new ArrayList<ExportFileInfo>();
		DatasourceConnectionInfo dsci = datasource.getConnectionInfo();
		textFieldPath.setText(dsci.getServer());
		Datasets datasets = datasource.getDatasets();
		if (datasets.getCount() > 0) {
			for (int i = 0; i < datasets.getCount(); i++) {
				ExportFileInfo temp = new ExportFileInfo();
				temp.setDataset(datasets.get(i));
				temp.setDatasetName(datasets.get(i).getName());
				temp.setDatasource(datasource);
				String datasetType = datasets.get(i).getType().toString();
				temp.setDataType(DatasetUtil.getDatasetName(datasetType, "", 0));
				ExportSetting exportSetting = new ExportSetting();
				exportSetting.setSourceData(datasets.get(i));
				temp.setFileTypes(exportSetting.getSupportedFileType());
				temp.setFileName(datasets.get(i).getName());
				temp.setFilePath(DataConversionProperties.getString("String_ExportRootPath") + File.separator);
				temp.setState(DataConversionProperties.getString("string_change"));
				tempExports.add(temp);
			}
		}
		return tempExports;
	}

	public void initCompanent() {
		setResizable(false);
		Workspace workspace = Application.getActiveApplication().getWorkspace();
		if (Application.getActiveApplication().getActiveDatasources().length > 0) {
			childExports = initExportFileInfo(Application.getActiveApplication().getActiveDatasources()[0]);
		} else {
			childExports = initExportFileInfo(workspace.getDatasources().get(0));
		}
		workspaceTree = new WorkspaceTree(workspace);
		workspaceTree.setMapsNodeVisible(false);
		workspaceTree.setResourcesNodeVisible(false);
		workspaceTree.setScenesNodeVisible(false);
		workspaceTree.setLayoutsNodeVisible(false);
		workspaceTree.addMouseListener(mouseAdapter);
		// 删除不用显示的数据集节点

		DefaultTreeModel treeModel = (DefaultTreeModel) workspaceTree.getModel();
		MutableTreeNode treeNode = (MutableTreeNode) treeModel.getRoot();
		MutableTreeNode datasourceTreeNode = (MutableTreeNode) treeNode.getChildAt(0);
		workspaceTree.expandRow(1);
		for (int i = 0; i < datasourceTreeNode.getChildCount(); i++) {
			DefaultMutableTreeNode childDatasourceTreeNode = (DefaultMutableTreeNode) datasourceTreeNode.getChildAt(i);
			for (int j = 0; j < childDatasourceTreeNode.getChildCount(); j++) {
				childDatasourceTreeNode.removeAllChildren();
			}
		}
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(490, 280, 677, 456);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JToolBar toolBar = new JToolBar();
		toolBar.setBackground(UIManager.getColor("Button.light"));
		toolBar.setRollover(true);
		toolBar.setFloatable(false);

		toolBar.add(labelPath);

		toolBar.add(textFieldPath);
		textFieldPath.setColumns(10);

		JSeparator separator = new JSeparator();
		toolBar.add(separator);

		toolBar.add(buttonSelectAll);

		JSeparator separatorF = new JSeparator();
		separatorF.setOrientation(SwingConstants.VERTICAL);
		toolBar.add(separatorF);

		toolBar.add(buttonInvertSelect);
		toolBar.add(labelScense);
		buttonInvertSelect.addActionListener(new CommonButtonAction());
		buttonSelectAll.addActionListener(new CommonButtonAction());
		JScrollPane scrollPaneTree = new JScrollPane();
		//@formatter:off

		//toolBar,

		//scrollPaneTree,PanelTable

		//buttonOutport,buttonQuit

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addComponent(toolBar)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addComponent(scrollPaneTree, GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panelTable, GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
						.addComponent(buttonOk)
						.addComponent(buttonQuit)));
		
		gl_contentPane.setVerticalGroup(gl_contentPane.createSequentialGroup()
				.addComponent(toolBar)
				.addGroup(gl_contentPane.createParallelGroup()
						.addComponent(scrollPaneTree)
						.addComponent(panelTable))
				.addGroup(gl_contentPane.createParallelGroup()
						.addComponent(buttonOk)
						.addComponent(buttonQuit)));
		
		gl_contentPane.setAutoCreateContainerGaps(true);
		gl_contentPane.setAutoCreateGaps(true);
		scrollPaneTree.setViewportView(workspaceTree);
		//@formatter:on

		JSeparator separatorS = new JSeparator();
		separatorS.setOrientation(SwingConstants.VERTICAL);
		toolBar.add(separatorS);

		comboBoxScense.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 关联查询
				searchForContent();
			}
		});
		comboBoxScense.setMaximumRowCount(20);

		toolBar.add(comboBoxScense);

		JSeparator separatorT = new JSeparator();
		separatorT.setOrientation(SwingConstants.VERTICAL);
		toolBar.add(separatorT);
		initResources();
		textFieldShearch = new JTextField();
		toolBar.add(textFieldShearch);
		textFieldShearch.setColumns(10);
		textFieldShearch.getDocument().addDocumentListener(new DocumentListener() {
			// 关联查询
			@Override
			public void removeUpdate(DocumentEvent e) {
				searchForContent();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				searchForContent();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				searchForContent();
			}
		});
		//@formatter:off

		toolBar.add(labelSearch);

		JScrollPane scrollPaneTable = new JScrollPane();
		GroupLayout gl_panelTable = new GroupLayout(panelTable);
		gl_panelTable.setHorizontalGroup(
			gl_panelTable.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPaneTable, GroupLayout.PREFERRED_SIZE, 424, Short.MAX_VALUE)
		);
		gl_panelTable.setVerticalGroup(
			gl_panelTable.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPaneTable, GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
		);
		buttonOk.addActionListener(new CommonButtonAction());
		buttonQuit.addActionListener(new CommonButtonAction());

		table = new JTable();
		model = new ChildExportModel(childExports);
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		table.setModel(model);
		table.getColumnModel().getColumn(0).setCellRenderer(new CommonListCellRenderer());
		table.setRowHeight(20);
		scrollPaneTable.setViewportView(table);
		panelTable.setLayout(gl_panelTable);
		table.addMouseListener(new OutportMourseListener());

		contentPane.setLayout(gl_contentPane);
		//@formatter:on

	}

	private void initResources() {
		setTitle(CoreProperties.getString("String_FormDatasetBrowse_FormText"));
		labelPath.setText(CoreProperties.getString("String_FormDatasetBrowse_ToolStripLabelPath"));

		labelScense.setText(CoreProperties.getString("String_FormDatasetBrowse_ToolStripLabelDisplayType"));
		buttonSelectAll
				.setIcon(new ImageIcon(DataSetChooserExPort.class.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectAll.png")));
		buttonInvertSelect.setIcon(
				new ImageIcon(DataSetChooserExPort.class.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectInverse.png")));
		labelSearch.setIcon(new ImageIcon(DatasetChooser.class.getResource("/com/supermap/desktop/controlsresources/SortType/Image_FindFiles.png")));
		buttonOk.setText(DataConversionProperties.getString("string_button_sure"));
		buttonQuit.setText(DataConversionProperties.getString("string_button_quit"));
		buttonSelectAll.setToolTipText(DataConversionProperties.getString("string_button_selectAll"));
		buttonInvertSelect.setToolTipText(DataConversionProperties.getString("string_button_invertSelect"));
	}

	/**
	 * 联合查询的具体实现
	 * 
	 * @param arrayListForSearch
	 */
	private void searchForContent() {
		try {

			String selectItem = comboBoxScense.getSelectItem();
			String contentInfo = textFieldShearch.getText();
			ArrayList<ExportFileInfo> tempExports = new ArrayList<ExportFileInfo>();
			ArrayList<ExportFileInfo> resultExports = new ArrayList<ExportFileInfo>();
			if (selectItem.equals(CommonProperties.getString("String_DatasetType_All"))) {
				tempExports = childExports;
			} else {
				for (int i = 0; i < childExports.size(); i++) {
					ExportFileInfo dataInfo = childExports.get(i);
					if (dataInfo.getDataType().equalsIgnoreCase(selectItem)) {
						tempExports.add(dataInfo);
					}
				}
			}
			for (int i = 0; i < tempExports.size(); i++) {
				String datasetName = tempExports.get(i).getDatasetName();
				// 输入框中没有内容或者输入以空格开始
				if (datasetName.toLowerCase().contains(contentInfo) || datasetName.toUpperCase().contains(contentInfo)) {
					resultExports.add(tempExports.get(i));
				}
			}
			if (selectItem.equals(CommonProperties.getString("String_DatasetType_All")) && contentInfo.isEmpty()) {
				resultExports = childExports;
			}
			table.setModel(new ChildExportModel(resultExports));
			table.getColumnModel().getColumn(0).setCellRenderer(new CommonListCellRenderer());
			table.setRowHeight(20);
			table.updateUI();

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}

	}

	class WorkspaceMouseAdapter extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			if (1 == e.getClickCount()) {
				Datasource datasource = UICommonToolkit.getDatasource(workspaceTree);
				if (null != datasource) {
					childExports = initExportFileInfo(datasource);
					String path = datasource.getConnectionInfo().getServer();
					textFieldPath.setText(path);
					table.setModel(new ChildExportModel(childExports));
					table.getColumnModel().getColumn(0).setCellRenderer(new CommonListCellRenderer());
					table.setRowHeight(20);
					table.updateUI();
				}
			}
		}
	}

	class OutportMourseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (1 == e.getClickCount()) {
				table.setRowSelectionAllowed(true);
			}
			if (2 == e.getClickCount()) {
				int[] selects = table.getSelectedRows();
				for (int i = 0; i < selects.length; i++) {
					// 添加数据集，并刷新导出界面

					ExportFunction.updateMainTable(frame, table, exportTable);
					workspaceTree.removeMouseListener(mouseAdapter);
					dispose();
				}
			}
		}
	}

	// 通用的按钮响应事件

	class CommonButtonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			JComponent c = (JComponent) e.getSource();
			if (c == buttonSelectAll) {
				// 全选

				table.setRowSelectionAllowed(true);
				table.setRowSelectionInterval(0, table.getRowCount() - 1);
			} else if (c == buttonInvertSelect) {
				// 反选

				CommonFunction.selectInvert(table);
			} else if (c == buttonQuit) {
				// 关闭

				workspaceTree.removeMouseListener(mouseAdapter);
				dispose();
			} else if (c == buttonOk) {
				// 确定

				ExportFunction.updateMainTable(frame, table, exportTable);
				workspaceTree.removeMouseListener(mouseAdapter);
				dispose();
			}
		}

	}
}
