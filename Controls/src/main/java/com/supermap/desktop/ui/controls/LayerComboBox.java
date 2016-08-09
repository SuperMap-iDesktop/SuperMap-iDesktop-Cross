package com.supermap.desktop.ui.controls;

import com.supermap.data.Datasets;
import com.supermap.desktop.CommonToolkit;
import com.supermap.mapping.Layers;

import javax.swing.*;

public class LayerComboBox extends JComboBox<Object> {
	private static final long serialVersionUID = 1L;

	/**
	 * 根据给定的数据集集合创建下拉选择框
	 * 
	 * @param datasets
	 */
	public LayerComboBox(Datasets datasets) {
		super(initLayerComboBoxItem(datasets));
		ListCellRenderer<Object> renderer = new CommonListCellRenderer();
		setRenderer(renderer);
	}

	/**
	 * 根据给定的图层集合类创建下拉选择框
	 * @param layers
	 */
	public LayerComboBox(Layers layers) {
		super(initLayerComboBoxItem(layers));
		ListCellRenderer<Object> renderer = new CommonListCellRenderer();
		setRenderer(renderer);
	}

	/**
	 * 由于填充的是DatasetCell 返回时要得到JLabel中显示的字符串
	 * 
	 * @return
	 */
	public String getSelectItem() {
		DataCell temp = (DataCell) getSelectedItem();
		return temp.getDataName();
	}

	private static JPanel[] initLayerComboBoxItem(Datasets datasets) {
		DataCell[] result = new DataCell[datasets.getCount()];
		for (int i = 0; i < datasets.getCount(); i++) {
			String filePath = CommonToolkit.LayerImageWrap.getImageIconPath(datasets.get(i).getType());
			String datasourceAlis = datasets.get(i).getName();
			result[i] = new DataCell();
			result[i].initDatasetType(datasets.get(i));
		}
		return result;
	}

	private static JPanel[] initLayerComboBoxItem(Layers layers) {
		DataCell[] result = new DataCell[layers.getCount()];
		for (int i = 0; i < layers.getCount(); i++) {
			result[i] = new DataCell();
			result[i].initDatasetType(layers.get(i).getDataset());
		}
		return result;
	}

}
