package com.supermap.desktop.ui.controls.smTables;

/**
 * Created by lixiaoyao on 2017/8/9.
 */
public interface ITableController {

	public void selectAll(ITable iTable);

	public void selectInverse(ITable iTable);

	public void selectSystemField(ITable iTable);

	public void selectUnSystemField(ITable iTable);

	public void delete(ITable iTable);

}
