package com.supermap.desktop.controls.property.datasource;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.property.AbstractPropertyControl;
import com.supermap.desktop.enums.PropertyType;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilities.DatasetTypeUtilities;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author XiaJT
 */
public class DatasourceInfoControl extends AbstractPropertyControl {

	private Datasource datasource;
	private JTable table;
	private HashMap<DatasetType, Integer> statisticMap;
	private HashMap<DatasetType, String> strDatasetTypeMap;
	private ArrayList<DatasetType> excludeDatasets;

	private String TOTAL = "Total";
	private String OTHERS = "Others";

	private static final int DEFAULT_COLUMN_WIDTH = 130;

	/**
	 * Create the panel.
	 */
	public DatasourceInfoControl(Datasource datasource) {
		super(ControlsProperties.getString("String_Statistic"));
		this.datasource = datasource;
		init();
		initializeExcludes();
	}


	private void init() {
		this.statisticMap = new HashMap<DatasetType, Integer>();
		this.strDatasetTypeMap = new HashMap<DatasetType, String>();
		initializeExcludes();
		JScrollPane scrollPaneStatisticValue = new JScrollPane();
//		scrollPaneStatisticValue.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_StatisticsInfo")));
		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][]{,}, new String[]{ControlsProperties.getString("String_DatasetType"),
				ControlsProperties.getString("String_StatisticResult")}) {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(DEFAULT_COLUMN_WIDTH);
		table.getColumnModel().getColumn(1).setPreferredWidth(DEFAULT_COLUMN_WIDTH);
		scrollPaneStatisticValue.setPreferredSize(table.getPreferredSize());
		scrollPaneStatisticValue.setViewportView(table);

		fillTableStatisticValue();
		fillStrDatasetTypeMap();
		TOTAL = CommonProperties.getString(CommonProperties.Total);
		OTHERS = CoreProperties.getString(CoreProperties.Other);
		fillComponents();
		this.setLayout(new GridBagLayout());
		this.add(scrollPaneStatisticValue, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setInsets(10));
	}

	private void fillComponents() {
		if (datasource != null) {
			fillTableStatisticValue();
		}
	}

	private void fillStrDatasetTypeMap() {
		com.supermap.data.Enum[] enums = DatasetType.getEnums(DatasetType.class);

		for (int i = 0; i < enums.length; i++) {
			DatasetType datasetType = (DatasetType) enums[i];
			if (datasetType != DatasetType.TEXTURE && datasetType != DatasetType.VOLUME) {
				strDatasetTypeMap.put(datasetType, DatasetTypeUtilities.toString(datasetType));
				statisticMap.put(datasetType, 0);
			}
		}
	}

	private void initializeExcludes() {
		this.excludeDatasets = new ArrayList<>();
		this.excludeDatasets.add(DatasetType.TEXTURE);
		this.excludeDatasets.add(DatasetType.VOLUME);
		this.excludeDatasets.add(DatasetType.POINTEPS);
		this.excludeDatasets.add(DatasetType.LINEEPS);
		this.excludeDatasets.add(DatasetType.REGIONEPS);
		this.excludeDatasets.add(DatasetType.TEXTEPS);
	}

	@Override
	public PropertyType getPropertyType() {
		return PropertyType.DATASOURCE;
	}

	@Override
	public void refreshData() {
		setDatasource(this.datasource);
	}

	public void setDatasource(Datasource datasource) {
		this.datasource = datasource;
		fillTableStatisticValue();
	}

	private void fillTableStatisticValue() {
		fillStatisticMap();
		statistic();

		DefaultTableModel tableModel = (DefaultTableModel) this.table.getModel();

		// clear tableModel
		while (tableModel.getRowCount() > 0) {
			tableModel.removeRow(tableModel.getRowCount() - 1);
		}
		//  屏蔽texture
		int count = datasource.getDatasets().getCount();
		int excludeCount = 0;
		for (int i = 0; i < datasource.getDatasets().getCount(); i++) {
			DatasetType type = datasource.getDatasets().get(i).getType();
			if (this.excludeDatasets.contains(type)) {
				excludeCount++;
			}
		}
		tableModel.addRow(new Object[]{TOTAL, count});
		com.supermap.data.Enum[] enums = DatasetType.getEnums(DatasetType.class);
		for (int i = 0; i < enums.length; i++) {
			DatasetType datasetType = (DatasetType) enums[i];
			// 排除掉 exclude 的数据集类型
			if (!excludeDatasets.contains(datasetType)) {
				tableModel.addRow(new Object[]{this.strDatasetTypeMap.get(datasetType), this.statisticMap.get(datasetType)});
			}
		}
		tableModel.addRow(new Object[]{OTHERS, excludeCount});
	}

	private void fillStatisticMap() {
		com.supermap.data.Enum[] enums = DatasetType.getEnums(DatasetType.class);

		for (int i = 0; i < enums.length; i++) {
			this.statisticMap.put((DatasetType) enums[i], 0);
		}
	}

	private void statistic() {
		if (this.datasource != null) {
			for (int i = 0; i < datasource.getDatasets().getCount(); i++) {
				Dataset dataset = datasource.getDatasets().get(i);

				if (dataset != null && dataset.getType() != DatasetType.TEXTURE && dataset.getType() != DatasetType.VOLUME) {
					this.statisticMap.put(dataset.getType(), this.statisticMap.get(dataset.getType()) + 1);
				}
			}
		}
	}

}
