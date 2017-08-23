package com.supermap.desktop.process.parameters.ParameterPanels.MultiBufferRadioList;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;

/**
 * Created by yuanR on 2017/8/22 0022.
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.MULTI_BUFFER_RADIOLIST)
public class ParameterMultiBufferRadioListPanel extends PanelMultiBufferRadioList implements IParameterPanel {

	private ParameterMultiBufferRadioList parameterMultiBufferRadioList = null;

	public ParameterMultiBufferRadioListPanel(IParameter parameterMultiBufferRadioList) {
		super();
		this.parameterMultiBufferRadioList = (ParameterMultiBufferRadioList) parameterMultiBufferRadioList;
		this.setPreferredSize(new Dimension(this.getWidth(), 200));
		this.setMinimumSize(new Dimension(this.getWidth(), 200));
		registerEvent();
	}

	private void registerEvent() {
		tableRadioList.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE) {
					parameterMultiBufferRadioList.setRadioList(radioList);
				}
			}
		});
	}

	@Override
	public Object getPanel() {
		return this;
	}

}
