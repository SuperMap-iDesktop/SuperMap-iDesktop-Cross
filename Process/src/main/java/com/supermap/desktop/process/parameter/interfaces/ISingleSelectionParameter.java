package com.supermap.desktop.process.parameter.interfaces;

import com.supermap.desktop.process.parameter.ParameterDataNode;

import java.util.ArrayList;

/**
 * @author XiaJT
 */
public interface ISingleSelectionParameter extends ISelectionParameter {
	int getItemCount();

	void setItems(ParameterDataNode... items);

	ArrayList<ParameterDataNode> getItems();

	int getItemIndex(Object item);

	ParameterDataNode getItemAt(int index);

}
