package com.supermap.desktop.ui.controls;

import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.implement.DefaultComboBoxUI;
import com.supermap.desktop.ui.controls.CellRenders.ListDataCellRender;

import javax.swing.*;
import java.util.ArrayList;

/**
 * 数据源下拉列表控件
 *
 * @author YuanR 2017.2.15
 */
public class DatasourceComboBox extends JComboBox<Datasource> {
	private static final long serialVersionUID = 1L;

	/**
	 * 覆盖原有的updateUI方法
	 * 2016.12.26
	 */
	@Override
	public void updateUI() {
		this.setUI(new DefaultComboBoxUI());
	}

	/**
	 * 根据工作空间中已经有的数据源集合类创建下拉选择框
	 */
	public DatasourceComboBox() {
		super(initDatasourceComboBoxItem(Application.getActiveApplication().getWorkspace().getDatasources()));
		//设置显示风格
		setDisplayStyle();
	}

	/**
	 * 根据给定的数据源集合类创建下拉选择框
	 *
	 * @param datasources
	 */
	public DatasourceComboBox(Datasources datasources) {
		super(initDatasourceComboBoxItem(datasources));
		//设置显示风格
		setDisplayStyle();
	}

	/**
	 * 根据给定的数据源集合创建下拉选择框
	 *
	 * @param datasource
	 */
	public DatasourceComboBox(Datasource[] datasource) {
		super(datasource);
		//设置显示风格
		setDisplayStyle();
	}

	/**
	 * 根据给定的数据源集合创建下拉选择框
	 *
	 * @param datasource
	 */
	public DatasourceComboBox(ArrayList<Datasource> datasource) {
		super(initDatasourceComboBoxItem(datasource));
		//设置显示风格
		setDisplayStyle();
	}

	/**
	 * 显示风格设置
	 */
	public void setDisplayStyle() {
		this.setBorder(BorderFactory.createEtchedBorder(1));
		this.setRenderer(new ListDataCellRender());
		this.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
	}


	/**
	 * 获得数据源名称（Alias）
	 *
	 * @return
	 */
	public String getSelectedItemAlias() {
		Datasource temp = (Datasource) getSelectedItem();
		return temp != null ? temp.getAlias() : "";
	}

	/**
	 * 获取选中的数据源
	 *
	 * @return
	 */
	public Datasource getSelectedDatasource() {
		Datasource result = null;
		if (getSelectedItem() instanceof Datasource) {
			result = (Datasource) getSelectedItem();
		}
		return result;
	}

	/**
	 * 通过数据源对象移除数据源
	 *
	 * @param currentDatasource
	 */
	public void removeDataSource(Datasource currentDatasource) {
		removeDatasource(currentDatasource.getAlias());
	}

	/**
	 * 通过alias移除数据源
	 *
	 * @param alias
	 */
	public void removeDatasource(String alias) {
		for (int i = 0; i < this.getItemCount(); i++) {
			if (this.getDatasourceAt(i).getAlias().equals(alias)) {
				this.removeItem(this.getItemAt(i));
			}
		}
	}

	/**
	 * 通过item序号获得数据源
	 *
	 * @param index
	 * @return
	 */
	public Datasource getDatasourceAt(int index) {
		Datasource dataset = null;
		if (index >= 0 && index < this.getItemCount() && this.getItemAt(index) instanceof Datasource) {
			dataset = (Datasource) this.getItemAt(index);
		}
		return dataset;
	}

	/**
	 * 选中指定数据源的项
	 *
	 * @param datasource
	 */
	public void setSelectedDatasource(Datasource datasource) {
		if (datasource == null) {
			setSelectedItem(null);
			return;
		}
		if (getItemCount() <= 0) {
			return;
		}
		int selectIndex = 0;

		for (int i = 0; i < getItemCount(); i++) {
			Datasource ComboBoxDatasource = getItemAt(i);
			if (ComboBoxDatasource == datasource) {
				selectIndex = i;
				break;
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
			this.addItem(datasource);
			if (datasource == selectedDatasource) {
				selectedIndex = getItemCount() - 1;
			}
		}

		if (getItemCount() > 0) {
			setSelectedIndex(selectedIndex);
		} else {
			setSelectedIndex(-1);
		}
	}

	/**
	 * @param datasources
	 * @return
	 */
	private static Datasource[] initDatasourceComboBoxItem(Datasources datasources) {
		Datasource[] result = new Datasource[datasources.getCount()];
		for (int i = 0; i < datasources.getCount(); i++) {
			result[i] = datasources.get(i);
		}
		return result;
	}

	private static Datasource[] initDatasourceComboBoxItem(ArrayList<Datasource> datasources) {
		Datasource[] result = new Datasource[datasources.size()];
		for (int i = 0; i < datasources.size(); i++) {
			result[i] = datasources.get(i);
		}
		return result;
	}

}
