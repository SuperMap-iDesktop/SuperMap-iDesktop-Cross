package com.supermap.desktop.ui.controls.comboBox;

import com.supermap.data.Datasource;
import com.supermap.data.EngineType;
import com.supermap.desktop.Application;

public class ComboBoxDatasource extends UIComboBox {

	/**
	 * 
	 */
	private transient static final long serialVersionUID = 1L;
	private transient EngineType[] engineTypes;
	private boolean includeReadOnly;
	private boolean autoSelect;

	public ComboBoxDatasource() {
		this.engineTypes = new EngineType[0];
		this.includeReadOnly = true;
		this.autoSelect = true;
		initialize();
	}

	/**
	 * 获取或设置支持的数据源类型，默认全部支持。
	 */
	public EngineType[] getEnginTypes() {
		return this.engineTypes;
	}

	public void setEnginTypes(EngineType[] value) {
		this.engineTypes = value;
		updateItems();
	}

	/**
	 * 获取或设置是否支持只读数据源，默认为false
	 */
	public boolean getIncludeReadOnly() {
		return this.includeReadOnly;
	}

	public void setIncludeReadOnly(boolean value) {
		this.includeReadOnly = value;
		if (this.getItemCount() != 0) {
			this.removeAllItems();
		}
		initialize();
	}

	/**
	 * 获取或设置 ComboBox 选中的数据源。
	 */
	public Datasource getSelectedDatasource() {
		Datasource datasource = null;

		try {
			datasource = itemObjectToDatasource((ComboBoxItem) this.getSelectedItem());
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return datasource;
	}

	public void setSelectedDatasource(Datasource value) {
		try {
			if (value != null) {
				this.setSelectedItem(datasourceToItemObject(value));
			} else {
				this.setSelectedItem(null);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 获取或设置是否自动选中一个子项。如果为true，则会自动选中工作空间中的当前激活数据源中的第一个，默认值为true。
	 */
	public boolean getAutoSelectActiveDatasource() {
		return this.autoSelect;
	}

	public void setAutoSelectActiveDatasource(boolean value) {
		this.autoSelect = value;
	}


	/**
	 * 从ComboBox下拉列表中，根据名称，移除指定的数据源
	 * 
	 * @param datasourceName
	 *            待移除数据源的名称
	 */
	public void remove(String datasourceName) {
		try {
			for (int i = 0; i < this.getItemCount(); i++) {
				ComboBoxItem itemObject = (ComboBoxItem) this.getItemAt(i);
				if (itemObject.getName().equals(datasourceName)) {
					this.remove(i);
					break;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 从下拉列表中移除指定的数据源。
	 * 
	 * @param datasource
	 *            待移除数据源
	 */
	public void remove(Datasource datasource) {
		this.remove(datasource.getAlias());
	}

	/**
	 * 向下拉列表添加一个数据源。
	 * 
	 * @param datasource
	 *            待添加数据源
	 */
	public int add(Datasource datasource) {
		int index = -1;
		try {
			boolean exist = false;
			for (int i = 0; i < this.getItemCount(); i++) {
				ComboBoxItem item = (ComboBoxItem) this.getItemAt(i);
				if (item.getName().equals(datasource.getAlias())) {
					exist = true;
					break;
				}
			}
			if (!exist) {
				this.addItem(buildItemObject(datasource));
				index = this.getItemCount() - 1;
				if (this.autoSelect && this.getSelectedDatasource() == null) {
					this.setSelectedIndex(index);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return index;
	}

	/**
	 * 按照用户之前设置的，支持的数据源类型，以及是否只读数据源等信息，初始化组合框
	 * 
	 * @return
	 */
	protected boolean initialize() {
		try {
			if (this.getItemCount() == 0) {
				Datasource activeDatasource = getActiveDatasource();
				int activeItemIndex = -1;

				for (int i = 0; i < Application.getActiveApplication().getWorkspace().getDatasources().getCount(); i++) {
					Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(i);
					if (isSupportDatasource(datasource)) {
						int index = this.add(datasource);
						if (datasource == activeDatasource) {
							activeItemIndex = index;
						}
					}
				}

				if (activeItemIndex >= 0) {
					this.setSelectedItem(this.getItemAt(activeItemIndex));
				} else if (this.getItemCount() > 0 && this.autoSelect) {
					this.setSelectedIndex(0);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return true;
	}

	/**
	 * 重新填充控件
	 */
	public void resetCombobox(){
		Datasource activeDatasource = getActiveDatasource();
		int activeItemIndex = -1;

		this.removeAllItems();
		for (int i = 0; i < Application.getActiveApplication().getWorkspace().getDatasources().getCount(); i++) {
			Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(i);
			if (isSupportDatasource(datasource)) {
				int index = this.add(datasource);
				if (datasource == activeDatasource) {
					activeItemIndex = index;
				}
			}
		}

		if (activeItemIndex >= 0) {
			this.setSelectedItem(this.getItemAt(activeItemIndex));
		} else if (this.getItemCount() > 0 && this.autoSelect) {
			this.setSelectedIndex(0);
		}
	}
	
	/**
	 * 判断控件是否包含了指定的数据源。
	 * 
	 * @param datasource
	 * @return
	 */
	public boolean containsDatasource(Datasource datasource) {
		boolean isContained = false;
		try {
			ComboBoxItem itemObject = datasourceToItemObject(datasource);
			isContained = itemObject != null;
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return isContained;
	}

	/**
	 * 是否支持该数据源类型
	 */
	public boolean isSupportDatasourceType(EngineType type) {
		boolean result = false;
		try {
			if (this.engineTypes.length > 0) {
				for (int i = 0; i < this.engineTypes.length; i++) {
					EngineType item = this.engineTypes[i];
					if (item == type) {
						result = true;
						break;
					}
				}
			} else {
				result = true;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	public boolean isSupportDatasource(Datasource datasource) {
		boolean result = true;
		try {
			if (!this.includeReadOnly) {
				result = (!datasource.isReadOnly()) ? true : false;
			}

			if (result) {
				result = isSupportDatasourceType(datasource.getEngineType());
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	/**
	 * 更新组合框的子项
	 */
	private void updateItems() {
		try {
			super.removeAllItems();
			int selectedIndex = 0;
			Datasource activeDatasource = getActiveDatasource();
			for (int i = 0; i < Application.getActiveApplication().getWorkspace().getDatasources().getCount(); i++) {
				Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(i);
				if (this.getEnginTypes() != null && this.getEnginTypes().length > 0 && !isSupportDatasource(datasource)) {
					continue;
				}

				int index = this.add(datasource);
				if (datasource == activeDatasource) {
					selectedIndex = index;
				}
			}

			if (this.getItemCount() > 0 && this.autoSelect) {
				this.setSelectedIndex(selectedIndex);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 获取ComboBox下拉列表中某个数据源对应的ComboBoxItem。
	 * 
	 * @param datasource
	 */
	public void UpdateComboBoxItem(Datasource datasource) {
		ComboBoxItem itemObject = null;
		try {
			itemObject = datasourceToItemObject(datasource);
			if (itemObject != null) {
				itemObject.setName(datasource.getAlias());
				this.invalidate();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 获取ComboBox下拉列表中某个数据源对应的ComboBoxItem。
	 * 
	 * @param datasource
	 * @return
	 */
	public ComboBoxItem datasourceToItemObject(Datasource datasource) {
		ComboBoxItem itemObject = null;

		try {
			for (int i = 0; i < this.getItemCount(); i++) {
				ComboBoxItem item = (ComboBoxItem) this.getItemAt(i);
				Datasource itemDatasource = itemObjectToDatasource(item);
				if (itemDatasource == datasource) {
					itemObject = item;
					break;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return itemObject;
	}

	private Datasource itemObjectToDatasource(ComboBoxItem itemObject) {
		Datasource datasource = null;

		try {
			if(itemObject != null){
				datasource = (Datasource) itemObject.getData();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return datasource;
	}

	private ComboBoxItem buildItemObject(Datasource datasource) {
		return new ComboBoxItem(datasource, datasource.getAlias());
	}

	private Datasource getActiveDatasource() {
		Datasource datasource = null;
		try {
			if (Application.getActiveApplication().getActiveDatasources() != null && Application.getActiveApplication().getActiveDatasources().length > 0) {
				datasource = Application.getActiveApplication().getActiveDatasources()[0];
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return datasource;
	}
}
