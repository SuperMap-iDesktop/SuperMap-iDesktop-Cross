package com.supermap.desktop.ui.controls.smTables;

/**
 * Created by lixiaoyao on 2017/8/9.
 */
public interface ITableController {

	public void selectedAll(ITable iTable);

	public void selectedIInverse(ITable iTable);

	public void selectedSystemField(ITable iTable);

	public void selectedNonSystemField(ITable iTable);

	public void delete(ITable iTable);

}
