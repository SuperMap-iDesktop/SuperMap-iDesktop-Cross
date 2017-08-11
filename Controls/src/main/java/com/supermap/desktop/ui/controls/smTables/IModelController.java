package com.supermap.desktop.ui.controls.smTables;

/**
 * Created by lixiaoyao on 2017/8/10.
 */
public interface IModelController {
	public void selectedAll();

	public void selectedIInverse();

	public void delete(int row);

	public void selectAllOrNull(boolean value);

	public void selectedSystemField();

	public void selectedNonSystemField();
}
