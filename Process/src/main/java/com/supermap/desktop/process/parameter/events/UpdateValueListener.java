package com.supermap.desktop.process.parameter.events;

/**
 * Created by hanyz on 2017/5/9.
 */
// 会导致数据不能及时更新，与其他控件的联动失效
public interface UpdateValueListener {
	void fireUpdateValue(ParameterUpdateValueEvent event);
}
