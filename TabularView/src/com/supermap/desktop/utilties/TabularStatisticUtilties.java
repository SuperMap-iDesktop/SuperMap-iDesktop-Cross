package com.supermap.desktop.utilties;

import java.text.MessageFormat;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.supermap.data.FieldType;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.controls.utilties.ToolbarUtilties;
import com.supermap.desktop.implement.SmStatusbar;
import com.supermap.desktop.tabularview.TabularViewProperties;
import com.supermap.desktop.ui.FormBaseChild;

/**
 * 属性表统计相关公共类
 * 
 * @author XiaJT
 *
 */
public class TabularStatisticUtilties {

	/**
	 * 记录数
	 */
	public static final int SELECTED_NUMBER = 0;
	
	/**
	 * 字段类型
	 */
	public static final int FIELD_TYPE = 2;
	
	/**
	 * 字段名称
	 */
	public static final int FIELD_NAME = 4;
	
	/**
	 * 统计结果数据
	 */
	public static final int STATISTIC_RESULT_INDEX = 5;

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
			JTextField jTextFieldStatisticResult = (JTextField) ((FormBaseChild) Application.getActiveApplication().getActiveForm()).getStatusbar()
					.getComponent(STATISTIC_RESULT_INDEX);
			jTextFieldStatisticResult.setText(result);
			((FormBaseChild)Application.getActiveApplication().getActiveForm()).getStatusbar().updateUI();
		} catch (Exception e) {
			throw e;
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
			if (((IFormTabular) Application.getActiveApplication().getActiveForm()).getSelectColumnCount() == 1) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 当前选中列是否为支持的统计类型
	 * 
	 * @return
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
		}else if (fieldType.equals(FieldType.WTEXT)){
			isSup = false;
		}else if(fieldType.equals(FieldType.LONGBINARY)){
			isSup = false;
		}
		return isSup;
	}
	
	/**
	 * 更新状态栏
	 * 根据当前选中情况更新出统计结果外的数据
	 */
	public static void updataSatusbars(IFormTabular formTabular ){
		SmStatusbar smStatusbar = ((FormBaseChild) formTabular).getStatusbar();
		
		// 如果选中为空
		if(formTabular.getSelectColumnCount() == 0 || null == formTabular.getSelectedRows()|| formTabular.getSelectedRows().length <= 0){
			((JLabel)smStatusbar.get(SELECTED_NUMBER)).setText(MessageFormat.format(TabularViewProperties.getString("String_Record_Count"), "0",formTabular.getRowCount()));
			((JTextField)smStatusbar.get(FIELD_TYPE)).setText("");
			((JTextField)smStatusbar.get(FIELD_NAME)).setText("");
		} else {
			int columnIndex = formTabular.getSelectedColumns()[0];
			((JLabel)smStatusbar.get(SELECTED_NUMBER)).setText(MessageFormat.format(TabularViewProperties.getString("String_Record_Count"), formTabular.getSelectedRows().length,formTabular.getRowCount()));
			((JTextField)smStatusbar.get(FIELD_TYPE)).setText(formTabular.getSelectColumnType(columnIndex));
			((JTextField)smStatusbar.get(FIELD_NAME)).setText(formTabular.getSelectColumnName(columnIndex));
		}

		smStatusbar.updateUI();
		ToolbarUtilties.updataToolbarsState();
	}
}
