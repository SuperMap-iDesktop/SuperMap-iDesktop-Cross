package com.supermap.desktop.ui.controls;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import com.supermap.data.Datasets;
import com.supermap.desktop.CommonToolkit;
import com.supermap.mapping.Layers;

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
		return temp.getDatasetName();
	}

	private static JPanel[] initLayerComboBoxItem(Datasets datasets) {
		JPanel[] result = new JPanel[datasets.getCount()];
		for (int i = 0; i < datasets.getCount(); i++) {
			String filePath = CommonToolkit.LayerImageWrap.getImageIconPath(datasets.get(i).getType());
			String datasourceAlis = datasets.get(i).getName();
			result[i] = new DataCell(filePath, datasourceAlis);
		}
		return result;
	}

	private static JPanel[] initLayerComboBoxItem(Layers layers) {
		JPanel[] result = new JPanel[layers.getCount()];
		for (int i = 0; i < layers.getCount(); i++) {
			String filePath = CommonToolkit.LayerImageWrap.getImageIconPath(layers.get(i).getDataset().getType());
			String datasourceAlis = layers.get(i).getName();
			result[i] = new DataCell(filePath, datasourceAlis);
		}
		return result;
	}

}
