package com.supermap.desktop.process.parameter.events;

/**
 * @author XiaJT
 */
public interface ParameterValueLegalListener {
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
