package com.supermap.desktop.ui.controls;

import com.supermap.data.DatasetType;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

/**
 * 数据集类型选择器
 *
 * @author XiaJT
 */
public class DatasetTypeComboBox extends JComboBox<DataCell> {
	// 没有获取当前所有支持类型的方法，需要的时候再开
	private transient DatasetType[] datasetTypes;
	private boolean isAllShown = true;

	// 为了保证顺序，不能遍历
	public static final DatasetType[] ALL_DATASET_TYPE = new DatasetType[]{
			DatasetType.POINT, DatasetType.LINE, DatasetType.REGION, DatasetType.TEXT, DatasetType.CAD, DatasetType.TABULAR, DatasetType.NETWORK,
			DatasetType.NETWORK3D, DatasetType.GRID, DatasetType.IMAGE, DatasetType.LINKTABLE, DatasetType.LINEM, DatasetType.TOPOLOGY,
			DatasetType.WMS, DatasetType.WCS, DatasetType.POINT3D, DatasetType.LINE3D, DatasetType.REGION3D, DatasetType.PARAMETRICLINE,
			DatasetType.PARAMETRICREGION, DatasetType.IMAGECOLLECTION, DatasetType.GRIDCOLLECTION
	};

	/**
	 * 得到包含所有类型的数据集类型选择器
	 */
	public DatasetTypeComboBox() {
		datasetTypes = DatasetTypeComboBox.ALL_DATASET_TYPE;
		inits();
	}

	/**
	 * 得到包含选定类型的数据集类型选择器
	 *
	 * @param datasetTypes 选择的数据集类型数组
	 */
	public DatasetTypeComboBox(DatasetType[] datasetTypes) {
		this.datasetTypes = datasetTypes;
		inits();
	}

	/**
	 * 初始化
	 */
	private void inits() {
		this.setRenderer(new CommonListCellRenderer());
		initComponents();
	}

	private void initComponents() {
		if (datasetTypes == null || datasetTypes.length <= 0) {
			this.setModel(null);
		} else {
			List<String> datasetTypeModel = new LinkedList<>();
			if (isAllShown) {
				datasetTypeModel.add(CommonProperties.getString("String_DatasetType_All"));
			}
			for (DatasetType datasetType : datasetTypes) {
				datasetTypeModel.add(CommonToolkit.DatasetTypeWrap.findName(datasetType));
			}

			DataCell[] result = new DataCell[datasetTypeModel.size()];
			for (int i = 0; i < datasetTypeModel.size(); i++) {
				String name = datasetTypeModel.get(i);
				String filePath = CommonToolkit.DatasetImageWrap.getImageIconPath(name);
				result[i] = new DataCell();
				result[i].initDataType(filePath, name);
			}
			this.setModel(new DefaultComboBoxModel<>(result));
		}
	}

	public DatasetType[] getSelectedDatasetTypes() {
		String selectedItem = ((DataCell) this.getSelectedItem()).getDataName();
		if (selectedItem == null) {
			return null;
		} else if (selectedItem.equals(CommonProperties.getString("String_DatasetType_All"))) {
			return datasetTypes;
		} else {
			return new DatasetType[]{CommonToolkit.DatasetTypeWrap.findType(selectedItem)};
		}
	}

	/**
	 * 重新设置数据集类型
	 *
	 * @param datasetTypes 数据集类型
	 */

	public void setDatasetTypes(DatasetType[] datasetTypes) {
		this.datasetTypes = datasetTypes;
		initComponents();
	}

	/**
	 * 设置是否显示 “所有数据集”选项
	 *
	 * @param isAllShown true-显示/ false-不显示
	 */
	public void setAllShown(boolean isAllShown) {
		this.isAllShown = isAllShown;
		initComponents();
	}
}
