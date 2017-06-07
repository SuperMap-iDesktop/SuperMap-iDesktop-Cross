package com.supermap.desktop.utilties;

import com.supermap.data.FieldType;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.SmStatusbar;
import com.supermap.desktop.tabularview.TabularViewProperties;
import com.supermap.desktop.ui.FormBaseChild;

import javax.swing.*;
import java.text.MessageFormat;

/**
 * 属性表统计相关公共类
 *
 * @author XiaJT
 */
public class TabularStatisticUtilties {

	public static final int HIDDEN_SYSTEM_FIELD = 0;

	/**
	 * 记录数
	 */
	public static final int SELECTED_NUMBER = 1;

	/**
	 * 字段类型
	 */
	public static final int FIELD_TYPE = 3;

	/**
	 * 字段名称
	 */
	public static final int FIELD_NAME = 5;

	/**
	 * 统计结果数据
	 */
	public static final int STATISTIC_RESULT_INDEX = 6;

	private TabularStatisticUtilties() {

	}

	/**
	 * 将统计结果显示在状态栏
	 *
	 * @param result 要显示的统计结果
	 * @return 是否修改成功
	 */
	public static boolean updataStatisticsResult(String result) {
		try {
			IForm activeForm = Application.getActiveApplication().getActiveForm();
			if (activeForm.getWindowType() != WindowType.TABULAR) {
				return true;
			}
			JTextField jTextFieldStatisticResult = (JTextField) ((FormBaseChild) activeForm).getStatusbar()
					.getComponent(STATISTIC_RESULT_INDEX);
			jTextFieldStatisticResult.setText(result);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
			return false;
		}
		return true;
	}

	/**
	 * 返回统计信息是否可用
	 *
	 * @return 是否可用
	 */
	public static boolean isStatisticsEnable() {
		try {
			if (Application.getActiveApplication().getActiveForm() instanceof IFormTabular) {
				return ((IFormTabular) Application.getActiveApplication().getActiveForm()).getSelectColumnCount() == 1;
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return false;
	}

	/**
	 * 当前选中列是否为支持的统计类型
	 *
	 * @return 是否支持
	 */
	public static boolean isStatisticsSupportType(Recordset recordset, int columnIndex) {
		FieldType fieldType = recordset.getFieldInfos().get(columnIndex).getType();
		boolean isSup = true;
		if (fieldType.equals(FieldType.INT64)) {
			isSup = false;
		} else if (fieldType.equals(FieldType.TEXT)) {
			isSup = false;
		} else if (fieldType.equals(FieldType.DATETIME)) {
			isSup = false;
		} else if (fieldType.equals(FieldType.CHAR)) {
			isSup = false;
		} else if (fieldType.equals(FieldType.WTEXT)) {
			isSup = false;
		} else if (fieldType.equals(FieldType.LONGBINARY)) {
			isSup = false;
		}
		return isSup;
	}

	/**
	 * 更新状态栏
	 * 根据当前选中情况更新出统计结果外的数据
	 */
	public static void updateSatusbars(IFormTabular formTabular) {
		SmStatusbar smStatusbar = ((FormBaseChild) formTabular).getStatusbar();

		// 如果选中为空
		if (formTabular.getSelectColumnCount() == 0 || null == formTabular.getSelectedRows() || formTabular.getSelectedRows().length <= 0) {
			((JLabel) smStatusbar.get(SELECTED_NUMBER)).setText(MessageFormat.format(TabularViewProperties.getString("String_Record_Count"), "0", formTabular.getRowCount()));
			((JTextField) smStatusbar.get(FIELD_TYPE)).setText("");
			((JTextField) smStatusbar.get(FIELD_NAME)).setText("");
		} else {
			int columnIndex = formTabular.getSelectedColumns()[0];
			((JLabel) smStatusbar.get(SELECTED_NUMBER)).setText(MessageFormat.format(TabularViewProperties.getString("String_Record_Count"), formTabular.getSelectedRows().length, formTabular.getRowCount()));
			((JTextField) smStatusbar.get(FIELD_TYPE)).setText(formTabular.getSelectColumnType(columnIndex));
			((JTextField) smStatusbar.get(FIELD_NAME)).setText(formTabular.getSelectColumnName(columnIndex));
		}

		ToolbarUIUtilities.updataToolbarsState();
	}
}
