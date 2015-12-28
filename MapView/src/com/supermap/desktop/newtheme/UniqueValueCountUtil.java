package com.supermap.desktop.newtheme;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Recordset;

/**
 * 通过sql表达式来得到记录集个数的相关方法类
 * 
 * @author xie
 *
 */
public class UniqueValueCountUtil {

	/**
	 * 判断单值项是否大于3000条
	 * 
	 * @return
	 */

	private UniqueValueCountUtil() {
		// 工具类不提供构造函数
	}

	public static boolean isCountBeyond(DatasetVector datasetVector, String expression) {
		boolean countBeyond = false;
		String tempExpression = expression;
		Recordset recordset = datasetVector.query(tempExpression, CursorType.STATIC);
		if (recordset.getRecordCount() > 3000) {
			countBeyond = true;
		}
		recordset.dispose();
		return countBeyond;
	}

	/**
	 * 判断字段中是否有小于0的数据
	 * 
	 * @param datasetVector
	 * @param expression
	 * @return
	 */
	public static boolean hasNegative(DatasetVector datasetVector, String expression) {
		boolean hasNegative = false;
		String tempExpression = expression;
		tempExpression = tempExpression.substring(tempExpression.lastIndexOf(".") + 1, tempExpression.length());
		Recordset recordset = datasetVector.query(tempExpression + "<0", CursorType.STATIC);
		if (recordset.getRecordCount() > 0) {
			hasNegative = true;
		}
		recordset.dispose();
		return hasNegative;
	}
}
