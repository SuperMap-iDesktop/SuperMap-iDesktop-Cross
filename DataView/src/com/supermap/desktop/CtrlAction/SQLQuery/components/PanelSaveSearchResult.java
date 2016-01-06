package com.supermap.desktop.CtrlAction.SQLQuery.components;

import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.ui.controls.borderPanel.SmComponentPanel;
import com.supermap.desktop.ui.controls.borderPanel.StateTransmitter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * SQL查询中的保存查询结果类
 * @author xiajt
 */
public class PanelSaveSearchResult extends SmComponentPanel {

	private JCheckBox checkBoxSaveResult = new JCheckBox("saveResult");
	private InsidePanel insidePanel = new InsidePanel();

	public PanelSaveSearchResult() {
		super(new JCheckBox());
		checkBoxSaveResult.setText(DataViewProperties.getString("String_SQLQueryLabelSaveResult"));
		checkBoxSaveResult.setSelected(false);
		this.setTitleComponent(checkBoxSaveResult);
		this.setTransmittingAllowed(true);
		this.setTransmitter(insidePanel);
		JPanel contentPane = this.getContentPane();
		contentPane.setLayout(new GridBagLayout());
		contentPane.add(insidePanel, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1));
		checkBoxSaveResult.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				PanelSaveSearchResult.this.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
			}
		});

	}

	public boolean isSaveResult() {
		return checkBoxSaveResult.isSelected();
	}

	public Datasource getSelectDatasouce() {
		return this.insidePanel.getSelectDatasource();
	}

	public String getDatasetName() {
		return this.insidePanel.getDatasetName();
	}

	public void setSelectedDatasources(Datasource datasource) {
		this.insidePanel.setSelectedDatasources(datasource);
	}

	class InsidePanel extends JPanel implements StateTransmitter {
		private JLabel labelDatasource = new JLabel("datasource");
		private JLabel labelDataset = new JLabel("dataset");
		private DatasourceComboBox datasourceComboBox = new DatasourceComboBox();
		private SmTextFieldLegit textFieldDataset = new SmTextFieldLegit();


		public InsidePanel() {
			initComponents();
			initLayout();
			initComponentStates();
			registListeners();
			initResources();
		}

		private void initComponents() {
			textFieldDataset.setSmTextFieldLegit(new ISmTextFieldLegit() {
				@Override
				public boolean isTextFieldValueLegit(String textFieldVaue) {
					return datasourceComboBox.getSelectedDatasource() == null || (textFieldVaue != null && textFieldVaue.length() > 0 && datasourceComboBox.getSelectedDatasource().getDatasets().isAvailableDatasetName(textFieldVaue));
				}

				@Override
				public String getLegitValue(String currentValue, String backUpValue) {
					return datasourceComboBox.getSelectedDatasource().getDatasets().getAvailableDatasetName(currentValue);
				}
			});
			for (int i = datasourceComboBox.getItemCount() - 1; i >= 0; i--) {
				if (datasourceComboBox.getItemAt(i) instanceof DataCell) {
					DataCell dataCell = (DataCell) datasourceComboBox.getItemAt(i);
					if (dataCell.getData() instanceof Datasource && ((Datasource) dataCell.getData()).isReadOnly()) {
						datasourceComboBox.removeItemAt(i);
					}
				}
			}
		}

		private void initLayout() {
			this.setLayout(new GridBagLayout());
			this.add(labelDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER));
			this.add(datasourceComboBox, new GridBagConstraintsHelper(1, 0, 2, 1).setWeight(2, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER));

			this.add(labelDataset, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER));
			this.add(textFieldDataset, new GridBagConstraintsHelper(1, 1, 2, 1).setWeight(2, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER));
		}

		private void initComponentStates() {
			textFieldDataset.setEnabled(false);
			datasourceComboBox.setEnabled(false);
			Datasource selecedDatasource = null;
			if (Application.getActiveApplication().getActiveDatasets().length > 0) {
				selecedDatasource = Application.getActiveApplication().getActiveDatasets()[0].getDatasource();
			} else if (Application.getActiveApplication().getActiveDatasources().length > 0) {
				selecedDatasource = Application.getActiveApplication().getActiveDatasources()[0];
			}
			if (selecedDatasource != null) {
				datasourceComboBox.setSelectedDatasource(selecedDatasource);
			}
			textFieldDataset.setText(datasourceComboBox.getSelectedDatasource() != null ? datasourceComboBox.getSelectedDatasource().getDatasets().getAvailableDatasetName("QueryResult") : "QueryResult");
		}

		private void registListeners() {
			datasourceComboBox.addItemListener(itemListener);
		}

		private void initResources() {
			this.labelDatasource.setText(DataViewProperties.getString("String_SQLQueryLabelDatasource"));
			this.labelDataset.setText(DataViewProperties.getString("String_SQLQueryLabelDataset"));
		}

		@Override
		public void setChildrenEnabled(boolean enable) {
			this.datasourceComboBox.setEnabled(enable);
			this.textFieldDataset.setEnabled(enable);
		}

		//region 监听事件
		private ItemListener itemListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				textFieldDataset.setText(datasourceComboBox.getSelectedDatasource().getDatasets().getAvailableDatasetName(textFieldDataset.getText()));
			}
		};

		//endregion

		/**
		 * 获得当前选中的数据源
		 *
		 * @return 选中的数据源
		 */
		public Datasource getSelectDatasource() {
			return this.datasourceComboBox.getSelectedDatasource() != null ? this.datasourceComboBox.getSelectedDatasource() : null;
		}

		/**
		 * 获得当前输入的数据集名称
		 *
		 * @return
		 */
		public String getDatasetName() {
			return this.textFieldDataset.getText();
		}

		/**
		 * 设置当前选择的数据源
		 *
		 * @param datasource 数据源
		 */
		public void setSelectedDatasources(Datasource datasource) {
			this.datasourceComboBox.setSelectedDatasource(datasource);
		}

	}
}
