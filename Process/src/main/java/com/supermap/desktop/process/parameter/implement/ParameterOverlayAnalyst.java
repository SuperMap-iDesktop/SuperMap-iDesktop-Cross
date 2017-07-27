package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.ParameterOverlayAnalystInfo;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;
import com.supermap.desktop.utilities.OverlayAnalystType;

import java.beans.PropertyChangeEvent;

/**
 * Created by xie on 2017/2/14.
 */
public class ParameterOverlayAnalyst extends AbstractParameter implements ISelectionParameter {

	private OverlayAnalystType overlayAnalystType;
    private ParameterOverlayAnalystInfo overlayAnalystInfo = new ParameterOverlayAnalystInfo();

    public OverlayAnalystType getOverlayAnalystType() {
        return overlayAnalystType;
    }

    public void setOverlayAnalystType(OverlayAnalystType overlayAnalystType) {
        this.overlayAnalystType = overlayAnalystType;
    }

    public ParameterOverlayAnalystInfo getOverlayAnalystInfo() {
        return overlayAnalystInfo;
    }

    @Override
    public String getType() {
	    return ParameterType.OVERLAY_ANALYST;
    }


	@Override
    public void setSelectedItem(Object value) {
        if (value instanceof ParameterOverlayAnalystInfo) {
            ParameterOverlayAnalystInfo oldValue = this.overlayAnalystInfo;
            this.overlayAnalystInfo = (ParameterOverlayAnalystInfo) value;
            firePropertyChangeListener(new PropertyChangeEvent(this, AbstractParameter.PROPERTY_VALE, oldValue, overlayAnalystInfo));
        }
    }

    @Override
    public Object getSelectedItem() {
        return this.overlayAnalystInfo;
    }

    @Override
    public void dispose() {

    }

    @Override
    public String getDescribe() {
        return "OverlayAnalyst";
    }
}
