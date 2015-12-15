package com.supermap.desktop.ui.controls;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.ListCellRenderer;

import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;

/**
 * 太乱，需要重构 by wuxb
 *
 * @author highsad
 *
 */
public class DatasourceComboBox extends JComboBox<Object> {
	private static final long serialVersionUID = 1L;

	/**
	 * 根据工作空间中已经有的数据源集合类创建下拉选择框
	 *
	 */
	public DatasourceComboBox() {
		super(initDatasourceComboBoxItem());
		this.setBorder(BorderFactory.createEtchedBorder(1));
		ListCellRenderer<Object> renderer = new CommonListCellRenderer();
		setRenderer(renderer);
	}

	/**
	 * 根据给定的数据源集合类创建下拉选择框
	 *
	 * @param datasources
	 */
	public DatasourceComboBox(Datasources datasources) {
		super(initDatasourceComboBoxItem(datasources));
		this.setBorder(BorderFactory.createEtchedBorder(1));
		ListCellRenderer<Object> renderer = new CommonListCellRenderer();
		setRenderer(renderer);
	}

	/**
	 * 根据给定的数据源集合创建下拉选择框
	 *
	 * @param datasources
	 */
	public DatasourceComboBox(Datasource[] datasources) {
		super(initDatasourceComboBoxItem(datasources));
		this.setBorder(BorderFactory.createEtchedBorder(1));
		ListCellRenderer<Object> renderer = new CommonListCellRenderer();
		setRenderer(renderer);
	}

	/**
	 * 由于填充的是DatasetCell 返回时需要得到DatasetCell中JLabel中显示的字符串
	 *
	 * @return
	 */
	public String getSelectItem() {
		DataCell temp = (DataCell) getSelectedItem();
		return temp.getDatasetName();
	}

	/**
	 * 获取选中的数据源
	 *
	 * @return
	 */
	public Datasource getSelectedDatasource() {
		Datasource result = null;

		if (getSelectedItem() instanceof DataCell) {
			DataCell selected = (DataCell) getSelectedItem();

			if (selected.getData() instanceof Datasource) {
				result = (Datasource) selected.getData();
			}
		}
		return result;
	}

	/**
	 * 选中指定数据源的项
	 *
	 * @param datasource
	 */
	public void setSelectedDatasource(Datasource datasource) {
		int selectIndex = 0;

		if (datasource != null) {
			for (int i = 0; i < getItemCount(); i++) {
				DataCell dataCell = (DataCell) getItemAt(i);

				if (dataCell.getData() == datasource) {
					selectIndex = i;
					break;
				}
			}
		}
		setSelectedIndex(selectIndex);
	}

	/**
	 * 重置控件
	 *
	 * @param datasources
	 * @param selectedDatasource
	 */
	public void resetComboBox(Datasources datasources, Datasource selectedDatasource) {
		removeAllItems();
		int selectedIndex = 0;

		for (int i = 0; i < datasources.getCount(); i++) {
			Datasource datasource = datasources.get(i);
			String filePath = CommonToolkit.DatasourceImageWrap.getImageIconPath(datasource.getEngineType());
			String datasourceAlis = datasource.getAlias();
			DataCell dataCell = new DataCell(filePath, datasourceAlis, datasource);

			this.addItem(dataCell);
			if (datasource == selectedDatasource) {
				selectedIndex = getItemCount() - 1;
			}
		}

		if (getItemCount() > 0) {
			setSelectedIndex(selectedIndex);
		}else{
			setSelectedIndex(-1);
		}
	}

	private static DataCell[] initDatasourceComboBoxItem(Datasources datasources) {
		DataCell[] result = new DataCell[datasources.getCount()];
		for (int i = 0; i < datasources.getCount(); i++) {
			Datasource datasource = datasources.get(i);

			String filePath = CommonToolkit.DatasourceImageWrap.getImageIconPath(datasources.get(i).getEngineType());
			String datasourceAlis = datasource.getAlias();
			result[i] = new DataCell(filePath, datasourceAlis, datasource);
		}
		return result;
	}

	private static DataCell[] initDatasourceComboBoxItem() {
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		return initDatasourceComboBoxItem(datasources);
	}

	private static DataCell[] initDatasourceComboBoxItem(Datasource[] datasources) {
		DataCell[] result = new DataCell[datasources.length];
		for (int i = 0; i < datasources.length; i++) {
			Datasource datasource = datasources[i];

			String filePath = CommonToolkit.DatasourceImageWrap.getImageIconPath(datasources[i].getEngineType());
			String datasourceAlis = datasource.getAlias();
			result[i] = new DataCell(filePath, datasourceAlis, datasource);
		}
		return result;
	}
}
