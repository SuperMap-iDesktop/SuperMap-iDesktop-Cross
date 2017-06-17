package com.supermap.desktop.process.parameter.events;

import java.util.EventListener;

/**
 * @author XiaJT
 */
public interface ParameterValueLegalListener extends EventListener {
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
	 * @return
	 */
	Object isValueSelected(ParameterValueSelectedEvent event);
}
