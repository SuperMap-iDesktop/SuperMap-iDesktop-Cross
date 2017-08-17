package com.supermap.desktop.process.parameter.ipls;

import com.supermap.analyst.spatialanalyst.NeighbourShapeType;
import com.supermap.analyst.spatialanalyst.NeighbourStatisticsParameter;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.beans.PropertyChangeEvent;

/**
 * Created By Chens on 2017/8/16 0016
 */
public class ParameterShapeType extends AbstractParameter implements ISelectionParameter {
	@ParameterField(name = "value")
	private NeighbourStatisticsParameter selectedItem;

	@Override
	public void setSelectedItem(Object item) {
		if (item instanceof NeighbourStatisticsParameter) {
			NeighbourStatisticsParameter oldValue = selectedItem;
			selectedItem = (NeighbourStatisticsParameter) item;
			firePropertyChangeListener(new PropertyChangeEvent(ParameterShapeType.this, "value", oldValue, selectedItem));
		}
	}

	@Override
	public Object getSelectedItem() {
		return selectedItem;
	}

	@Override
	public String getType() {
		return ParameterType.SHAPE_TYPE;
	}

	@Override
	public void dispose() {

	}

	@Override
	public String getDescribe() {
		return "ShapeType";
	}
}
