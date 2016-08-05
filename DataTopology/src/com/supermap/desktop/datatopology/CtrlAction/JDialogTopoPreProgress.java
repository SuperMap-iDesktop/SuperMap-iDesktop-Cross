package com.supermap.desktop.datatopology.CtrlAction;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.data.topology.TopologyPreprocessOptions;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.utilities.DatasetUIUtilities;
import com.supermap.desktop.datatopology.DataTopologyProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.CommonListCellRenderer;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.mutiTable.DDLExportTableModel;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTable;
import com.supermap.desktop.ui.controls.progress.FormProgress;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.TableUtilities;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class JDialogTopoPreProgress extends SmDialog {
	private static final long serialVersionUID = 1L;
	private MutiTable table;
	private SmTextFieldLegit textFieldTolerance;
	private DatasetComboBox comboBoxConsultDataset;
	private JToolBar toolBar = new JToolBar();
	private JScrollPane scrollPane = new JScrollPane();
	private SmButton buttonSure = new SmButton("String_Button_OK");
	private SmButton buttonQuite = new SmButton("String_Button_Cancel");
	private JLabel labelTolerance = new JLabel("String_Label_Tolerance");
	private SmButton buttonAdd = new SmButton();
	private SmButton buttonSelectAll = new SmButton();
	private SmButton buttonInvertSelect = new SmButton();
	private SmButton buttonDelete = new SmButton();
	private JCheckBox checkBoxVertexArcInserted = new JCheckBox("String_CheckBox_VertexArcInserted");
	private JCheckBox checkBoxVertexesSnapped = new JCheckBox("String_CheckBox_VertexesSnapped");
	private JCheckBox checkBoxArcsInserted = new JCheckBox("String_CheckBox_ArcsInserted");
	private JCheckBox checkBoxPolygonsChecked = new JCheckBox("String_CheckBox_PolygonsChecked");
	private JLabel labelConsultDataset = new JLabel("String_Label_ConsultDataset");
	private JPanel panel = new JPanel();
	private final int COLUMN_INDEX_COUNT = 0;
	private final int COLUMN_INDEX_DATASET = 1;
	private final int COLUMN_INDEX_DATASOURCE = 2;
	private final int DatasetType_All = 0;
	private final int DatasetType_Point = 1;
	private final int DatasetType_Line = 2;
	private final int DatasetType_Region = 3;

	public JDialogTopoPreProgress(JFrame owner, boolean model) {
		super(owner, model);
		initComponents();
		initResources();
		initTableInfo();
		this.componentList.add(this.buttonSure);
		this.componentList.add(this.buttonQuite);
		this.setFocusTraversalPolicy(policy);
	}

	/**
	 * 单击选中某个数据集时初始化table
	 */
	private void initTableInfo() {
		try {
			Dataset[] datasets = Application.getActiveApplication().getActiveDatasets();
			if (null != datasets) {
				for (Dataset dataset : datasets) {
					if (dataset.getType() == DatasetType.LINE || dataset.getType() == DatasetType.REGION || dataset.getType() == DatasetType.POINT) {
						Object[] temp = new Object[3];
						temp[COLUMN_INDEX_COUNT] = table.getRowCount() + 1;
						DataCell datasetCell = new DataCell();
						datasetCell.initDatasetType(dataset);
						getComboBoxConsultDataset().addItem(datasetCell);
						temp[COLUMN_INDEX_DATASET] = datasetCell;
						Datasource dataSource = dataset.getDatasource();
						if (dataset.getType() == DatasetType.REGION) {
							getCheckBoxArcsInserted().setEnabled(true);
							getCheckBoxPolygonsChecked().setEnabled(true);
							getCheckBoxVertexArcInserted().setEnabled(true);
							getCheckBoxVertexesSnapped().setEnabled(true);
						} else if (dataset.getType() == DatasetType.LINE) {
							getCheckBoxArcsInserted().setEnabled(true);
							getCheckBoxVertexArcInserted().setEnabled(true);
							getCheckBoxVertexesSnapped().setEnabled(true);
						} else if (dataset.getType() == DatasetType.POINT) {
							getCheckBoxVertexesSnapped().setEnabled(true);
						}
						DataCell cell = new DataCell();
						cell.initDatasourceType(dataSource);
						temp[COLUMN_INDEX_DATASOURCE] = cell;
						table.addRow(temp);
					}
				}
				if (table.getRowCount() > 0) {
					table.setRowSelectionInterval(0, table.getRowCount() - 1);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void initResources() {
		setTitle(DataTopologyProperties.getString("String_Text_Preprogress"));
		buttonAdd.setIcon(new ImageIcon(JDialogTopoPreProgress.class.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Add.png")));
		buttonSelectAll.setIcon(new ImageIcon(JDialogTopoPreProgress.class
				.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectAll.png")));
		buttonInvertSelect.setIcon(new ImageIcon(JDialogTopoPreProgress.class
				.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectInverse.png")));
		buttonDelete
				.setIcon(new ImageIcon(JDialogTopoPreProgress.class.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Delete.png")));
		labelTolerance.setText(CommonProperties.getString("String_Label_Tolerance") + ":");
		labelConsultDataset.setText(DataTopologyProperties.getString("String_Label_ConsultDataset"));
		table.getColumnModel().getColumn(COLUMN_INDEX_COUNT).setHeaderValue(CommonProperties.getString("String_ColumnHeader_Index"));
		table.getColumnModel().getColumn(COLUMN_INDEX_DATASET).setHeaderValue(CommonProperties.getString("String_ColumnHeader_SourceDataset"));
		table.getColumnModel().getColumn(COLUMN_INDEX_DATASOURCE).setHeaderValue(CommonProperties.getString("String_ColumnHeader_SourceDatasource"));
		checkBoxVertexArcInserted.setEnabled(false);
		checkBoxVertexArcInserted.setText(DataTopologyProperties.getString("String_CheckBox_VertexArcInserted"));
		checkBoxVertexesSnapped.setEnabled(false);
		checkBoxVertexesSnapped.setText(DataTopologyProperties.getString("String_CheckBox_VertexesSnapped"));
		checkBoxArcsInserted.setEnabled(false);
		checkBoxArcsInserted.setText(DataTopologyProperties.getString("String_CheckBox_ArcsInserted"));
		checkBoxPolygonsChecked.setEnabled(false);
		checkBoxPolygonsChecked.setText(DataTopologyProperties.getString("String_CheckBox_PolygonsChecked"));
		buttonSure.setEnabled(false);
		buttonSure.setText(CommonProperties.getString("String_Button_OK"));
		buttonQuite.setText(CommonProperties.getString("String_Button_Cancel"));
		panel.setBorder(new TitledBorder(null, CommonProperties.getString("String_FormEdgeCount_Text"), TitledBorder.LEADING, TitledBorder.TOP, null, null));

	}

	private void initComponents() {
		toolBar.setFloatable(false);
		Dimension size = new Dimension(500, 380);
		setSize(size);
		setMinimumSize(size);
		setLocationRelativeTo(null);
		getRootPane().setDefaultButton(buttonSure);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		//@formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
				.addComponent(toolBar, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(buttonSure)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(buttonQuite))
				);
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addComponent(toolBar)
				.addComponent(scrollPane)
				.addComponent(panel)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonSure)
						.addComponent(buttonQuite)));
		//@formatter:on
		textFieldTolerance = new SmTextFieldLegit();
		textFieldTolerance.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				if (StringUtilities.isNullOrEmpty(textFieldValue)) {
					return true;
				} else {
					try {
						Double aDouble = Double.valueOf(textFieldValue);
					} catch (NumberFormatException e) {
						return false;
					}
				}
				return true;
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		});
		textFieldTolerance.setText("0");
		textFieldTolerance.setColumns(10);

		Dataset[] datasets = new Dataset[0];
		comboBoxConsultDataset = new DatasetComboBox(datasets);
		comboBoxConsultDataset.setEnabled(false);
		//@formatter:off
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
										.addComponent(checkBoxVertexesSnapped)
										.addGroup(gl_panel.createSequentialGroup()
												.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
														.addComponent(labelConsultDataset)
														.addComponent(labelTolerance))
												.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
														.addComponent(textFieldTolerance, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
														.addComponent(comboBoxConsultDataset))))
								.addPreferredGap(ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(checkBoxArcsInserted)
										.addComponent(checkBoxVertexArcInserted)
										.addComponent(checkBoxPolygonsChecked))
								.addGap(15))
				);
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
										.addComponent(labelTolerance)
										.addComponent(checkBoxVertexArcInserted)
										.addComponent(textFieldTolerance, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
										.addComponent(checkBoxVertexesSnapped)
										.addComponent(checkBoxArcsInserted))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
												.addComponent(labelConsultDataset)
												.addComponent(comboBoxConsultDataset, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
												.addComponent(checkBoxPolygonsChecked))
								.addContainerGap(10, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);
		//@formatter:on
		table = new MutiTable();
		scrollPane.setViewportView(table);
		@SuppressWarnings("serial")
		DDLExportTableModel tableModel = new DDLExportTableModel(new String[]{"Count", "Dataset", "Datasource"}) {
			boolean[] columnEditables = new boolean[]{false, false, false};

			@Override
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		table.setModel(tableModel);
		table.getColumnModel().getColumn(COLUMN_INDEX_DATASET).setCellRenderer(new CommonListCellRenderer());
		table.getColumnModel().getColumn(COLUMN_INDEX_DATASOURCE).setCellRenderer(new CommonListCellRenderer());
		toolBar.add(buttonAdd);
		toolBar.add(buttonSelectAll);
		toolBar.add(buttonInvertSelect);
		toolBar.add(buttonDelete);
		getContentPane().setLayout(groupLayout);
		buttonAdd.addActionListener(new CommonButtonListener());
		buttonSelectAll.addActionListener(new CommonButtonListener());
		buttonInvertSelect.addActionListener(new CommonButtonListener());
		buttonDelete.addActionListener(new CommonButtonListener());
		buttonSure.addActionListener(new CommonButtonListener());
		buttonQuite.addActionListener(new CommonButtonListener());
		scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (2 == e.getClickCount()) {
					openDatasetChooserDialog();
				}
			}
		});

		checkBoxVertexesSnapped.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setSureButtonState();
				if (checkBoxVertexesSnapped.isSelected()) {
					comboBoxConsultDataset.setEnabled(true);
				} else {
					comboBoxConsultDataset.setEnabled(false);
				}
			}
		});
		checkBoxArcsInserted.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setSureButtonState();
			}
		});
		checkBoxPolygonsChecked.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setSureButtonState();
			}
		});
		checkBoxVertexArcInserted.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setSureButtonState();
			}
		});
	}

	/**
	 * 设置button的状态
	 */
	protected void setSureButtonState() {
		boolean isVertexesSnapped = checkBoxVertexesSnapped.isSelected();
		boolean isArcsInserted = checkBoxArcsInserted.isSelected();
		boolean isPolygonsChecked = checkBoxPolygonsChecked.isSelected();
		boolean isVertexArcInserted = checkBoxVertexArcInserted.isSelected();
		if (isVertexesSnapped || isArcsInserted || isPolygonsChecked || isVertexArcInserted) {
			buttonSure.setEnabled(true);
		} else {
			buttonSure.setEnabled(false);
		}
	}

	class CommonButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JComponent c = (JComponent) e.getSource();
			if (c == buttonAdd) {
				openDatasetChooserDialog();
				checkButtonStates();
			} else if (c == buttonSelectAll) {
				// 全选
				table.setRowSelectionAllowed(true);
				if (table != null && table.getRowCount() > 0) {
					table.setRowSelectionInterval(0, table.getRowCount() - 1);
				}
			} else if (c == buttonInvertSelect) {
				// 反选
				TableUtilities.invertSelection(table);
			} else if (c == buttonDelete) {
				deleteFromTable();
			} else if (c == buttonSure) {
				preProgressDataset();
				dispose();
			} else if (c == buttonQuite) {
				dispose();
			}
		}
	}

	private void deleteFromTable() {
		try {
			// 删除后设置第0行被选中
			if (0 < table.getRowCount()) {
				int[] selectRows = table.getSelectedRows();
				DDLExportTableModel tableModel = (DDLExportTableModel) table.getModel();
				tableModel.removeRows(selectRows);
				int[] rowCount = new int[table.getRowCount()];
				// 刷新序号
				Object[][] datas = new Object[rowCount.length][3];
				for (int i = 0; i < rowCount.length; i++) {
					Object[] temp = new Object[3];
					temp[COLUMN_INDEX_COUNT] = i + 1;
					DataCell datasetCell = (DataCell) tableModel.getRowValue(i).get(1);
					DataCell datasourceCell = (DataCell) tableModel.getRowValue(i).get(2);
					Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasourceCell.toString());
					temp[COLUMN_INDEX_DATASET] = datasetCell;
					Dataset dataset = DatasetUIUtilities.getDatasetFromDatasource(datasetCell.toString(), datasource);

					temp[COLUMN_INDEX_DATASOURCE] = datasourceCell;
					datas[i] = temp;
				}
				tableModel.refreshContents(datas);
				table.updateUI();
				checkButtonStates();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void checkButtonStates() {
		ArrayList<Object> datasetTypes = new ArrayList<>();
		DDLExportTableModel tableModel = ((DDLExportTableModel) table.getModel());
		for (int i = 0; i < table.getRowCount(); i++) {
			Object[] temp = new Object[3];
			temp[COLUMN_INDEX_COUNT] = i + 1;
			DataCell datasetCell = (DataCell) tableModel.getRowValue(i).get(1);
			DataCell datasourceCell = (DataCell) tableModel.getRowValue(i).get(2);
			Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasourceCell.toString());
			temp[COLUMN_INDEX_DATASET] = datasetCell;
			Dataset dataset = DatasetUIUtilities.getDatasetFromDatasource(datasetCell.toString(), datasource);
			datasetTypes.add(dataset.getType());

			temp[COLUMN_INDEX_DATASOURCE] = datasourceCell;
		}
		if (0 < table.getRowCount()) {
			if (datasetTypes.contains(DatasetType.REGION)) {
				checkBoxArcsInserted.setEnabled(true);
				checkBoxPolygonsChecked.setEnabled(true);
				checkBoxVertexArcInserted.setEnabled(true);
				checkBoxVertexesSnapped.setEnabled(true);
			} else if (datasetTypes.contains(DatasetType.LINE)) {
				checkBoxArcsInserted.setEnabled(true);
				checkBoxPolygonsChecked.setEnabled(false);
				checkBoxVertexArcInserted.setEnabled(true);
				checkBoxVertexesSnapped.setEnabled(true);
			} else if (datasetTypes.contains(DatasetType.POINT)) {
				checkBoxArcsInserted.setEnabled(false);
				checkBoxPolygonsChecked.setEnabled(false);
				checkBoxVertexArcInserted.setEnabled(false);
				checkBoxVertexesSnapped.setEnabled(true);
			}
			table.setRowSelectionInterval(0, 0);
		} else {
			checkBoxArcsInserted.setEnabled(false);
			checkBoxPolygonsChecked.setEnabled(false);
			checkBoxVertexArcInserted.setEnabled(false);
			checkBoxVertexesSnapped.setEnabled(false);
		}
	}

	/**
	 * 对table中的的数据集进行拓扑预处理
	 */
	public void preProgressDataset() {
		TopologyPreprocessOptions options = new TopologyPreprocessOptions();
		// 容限值
		String text = textFieldTolerance.getText();
		if (StringUtilities.isNullOrEmpty(text)) {
			text = "0";
		}
		double tolerance = Double.parseDouble(text);
		// 节点与线段间插入节点
		boolean isVertexArcInserted = checkBoxVertexArcInserted.isSelected();
		// 节点捕捉
		boolean isVertexesSnapped = checkBoxVertexesSnapped.isSelected();
		// 线段间求交插入节点
		boolean isArcsInserted = checkBoxArcsInserted.isSelected();
		// 多边形走向调整
		boolean isPolygonsChecked = checkBoxPolygonsChecked.isSelected();
		options.setArcsInserted(isArcsInserted);
		options.setVertexesSnapped(isVertexesSnapped);
		options.setVertexArcInserted(isVertexArcInserted);
		options.setPolygonsChecked(isPolygonsChecked);
		// 单进度条实现
		FormProgress totalProgress = new FormProgress();
		totalProgress.setTitle(DataTopologyProperties.getString("String_Text_Preprogress"));
		totalProgress.doWork(new TopoPregressCallable(table, tolerance, options));
	}

	public void openDatasetChooserDialog() {
		Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(0);
		if (0 < Application.getActiveApplication().getActiveDatasources().length) {
			datasource = Application.getActiveApplication().getActiveDatasources()[0];
		}
		DatasetType[] datasetTypes = new DatasetType[]{DatasetType.POINT, DatasetType.LINE, DatasetType.REGION};
		String[] datasetType = new String[4];
		datasetType[DatasetType_All] = CommonProperties.getString("String_DatasetType_All");
		datasetType[DatasetType_Point] = CommonProperties.getString("String_DatasetType_Point");
		datasetType[DatasetType_Line] = CommonProperties.getString("String_DatasetType_Line");
		datasetType[DatasetType_Region] = CommonProperties.getString("String_DatasetType_Region");
		DatasetChooserDataTopo chooser = new DatasetChooserDataTopo(this, true, datasource, table, datasetTypes);
		chooser = null;
		// chooser.setVisible(true);
	}

	public JTextField getTextFieldTolerance() {
		return textFieldTolerance;
	}

	public DatasetComboBox getComboBoxConsultDataset() {
		return comboBoxConsultDataset;
	}

	public void setComboBoxConsultDataset(DatasetComboBox comboBoxConsultDataset) {
		this.comboBoxConsultDataset = comboBoxConsultDataset;
	}

	public JCheckBox getCheckBoxVertexArcInserted() {
		return checkBoxVertexArcInserted;
	}

	public void setCheckBoxVertexArcInserted(JCheckBox checkBoxVertexArcInserted) {
		this.checkBoxVertexArcInserted = checkBoxVertexArcInserted;
	}

	public JCheckBox getCheckBoxVertexesSnapped() {
		return checkBoxVertexesSnapped;
	}

	public void setCheckBoxVertexesSnapped(JCheckBox checkBoxVertexesSnapped) {
		this.checkBoxVertexesSnapped = checkBoxVertexesSnapped;
	}

	public JCheckBox getCheckBoxArcsInserted() {
		return checkBoxArcsInserted;
	}

	public void setCheckBoxArcsInserted(JCheckBox checkBoxArcsInserted) {
		this.checkBoxArcsInserted = checkBoxArcsInserted;
	}

	public JCheckBox getCheckBoxPolygonsChecked() {
		return checkBoxPolygonsChecked;
	}

	public void setCheckBoxPolygonsChecked(JCheckBox checkBoxPolygonsChecked) {
		this.checkBoxPolygonsChecked = checkBoxPolygonsChecked;
	}

}
