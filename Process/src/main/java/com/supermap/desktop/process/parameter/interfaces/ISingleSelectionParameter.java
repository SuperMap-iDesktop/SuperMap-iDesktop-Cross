package com.supermap.desktop.process.parameter.interfaces;

import com.supermap.desktop.process.parameter.ParameterDataNode;

/**
 * @author XiaJT
 */
public interface ISingleSelectionParameter extends ISelectionParameter {
	int getItemCount();

	void setItems(ParameterDataNode... items);

	ParameterDataNode[] getItems();

	int getItemIndex(Object item);

	ParameterDataNode getItemAt(int index);

}
