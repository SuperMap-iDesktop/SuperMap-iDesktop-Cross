package com.supermap.desktop.CtrlAction.SQLQuery.components;

import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.borderPanel.SmComponentPanel;
import com.supermap.desktop.ui.controls.borderPanel.StateTransmitter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Administrator on 2015/12/7.
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

	class InsidePanel extends JPanel implements StateTransmitter {
		private JLabel labelDatasource = new JLabel("datasource");
		private JLabel labelDataset = new JLabel("dataset");
		private DatasourceComboBox datasourceComboBox = new DatasourceComboBox();
		private JTextField textFieldDataset = new JTextField();


		public InsidePanel() {
			initComponents();
			initLayout();
			initComponentStates();
			registListeners();
			initResources();
		}

		private void initComponents() {
			for (int i = datasourceComboBox.getItemCount() - 1; i >= 0; i--) {
				if (datasourceComboBox.getItemAt(i) instanceof DataCell) {
					DataCell dataCell = (DataCell) datasourceComboBox.getItemAt(i);
					if (dataCell.getData() instanceof Datasource && ((Datasource) dataCell.getData()).isReadOnly()) {
						datasourceComboBox.removeItemAt(i);
					}
				}
			}
			if (Application.getActiveApplication().getActiveDatasources().length > 0) {
				datasourceComboBox.setSelectedDatasource(Application.getActiveApplication().getActiveDatasources()[0]);
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
			textFieldDataset.setText(datasourceComboBox.getSelectedDatasource().getDatasets().getAvailableDatasetName("QueryResult"));
		}

		private void registListeners() {
			datasourceComboBox.addItemListener(itemListener);
			textFieldDataset.addKeyListener(textFieldDatasetKeyListener);

			textFieldDataset.addFocusListener(focusListener);
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
		private KeyListener textFieldDatasetKeyListener = new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (textFieldDataset.getText().length() > 0 && datasourceComboBox.getSelectedDatasource() != null && !datasourceComboBox.getSelectedDatasource().getDatasets().isAvailableDatasetName(textFieldDataset.getText())) {
					textFieldDataset.setForeground(Color.red);
				} else {
					textFieldDataset.setForeground(Color.black);
				}
			}
		};

		private FocusListener focusListener = new FocusListener() {
			private String backUps = "";

			@Override
			public void focusGained(FocusEvent e) {
				backUps = textFieldDataset.getText();
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (!datasourceComboBox.getSelectedDatasource().getDatasets().isAvailableDatasetName(textFieldDataset.getText())) {
					textFieldDataset.setText(backUps);
					textFieldDataset.setForeground(Color.black);
				}
			}
		};

		public Datasource getSelectDatasource() {
			return this.datasourceComboBox.getSelectedDatasource() != null ? this.datasourceComboBox.getSelectedDatasource() : null;
		}

		public String getDatasetName() {
			return this.textFieldDataset.getText();
		}
		//endregion

	}
}
