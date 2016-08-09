package com.supermap.desktop.ui.controls.dialogs.dialogJoinItems;

import com.supermap.data.JoinType;
import com.supermap.desktop.properties.CoreProperties;

/**
 * 连接类型公共类
 */
public class JoinTypeUtilties {
	private JoinTypeUtilties() {

	}

	public static String getJoinTypeName(JoinType joinType) {
		if (joinType == JoinType.LEFTJOIN) {
			return CoreProperties.getString("String_JoinItem_Left");
		} else if (joinType == JoinType.INNERJOIN) {
			return CoreProperties.getString("String_JoinItem_Inner");
		}
		return null;
	}

	public static JoinType getJoinTypeValue(String joinTypeName) {
		if (CoreProperties.getString("String_JoinItem_Left").equals(joinTypeName)) {
			return JoinType.LEFTJOIN;
		} else if (CoreProperties.getString("String_JoinItem_Inner").equals(joinTypeName)) {
			return JoinType.INNERJOIN;
		}
		return null;
	}
}
