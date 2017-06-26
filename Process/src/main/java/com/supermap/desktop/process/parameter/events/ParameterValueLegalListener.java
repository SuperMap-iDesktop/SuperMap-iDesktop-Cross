package com.supermap.desktop.process.parameter.events;

import java.util.EventListener;

/**
 * @author XiaJT
 */
public interface ParameterValueLegalListener extends EventListener {
	String DO_NOT_CARE = new String("Don't Care");
	String NO = new String("NO");

	/**
	 * 当前值是否可选
	 *
	 * @param event
	 * @return
	 */
	boolean isValueLegal(ParameterValueLegalEvent event);

	/**
	 * 当前值是否选中
	 *
	 * @param event
	 * @return 有选中要求则返回对应对象，当前选中不符合则返回No，不关心则返回DO_NOT_CARE
	 */
	Object isValueSelected(ParameterValueSelectedEvent event);
}
