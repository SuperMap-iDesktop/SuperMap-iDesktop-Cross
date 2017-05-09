package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.data.DatasetVector;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.meta.metaProcessImplements.spatialStatistics.StatisticsField.StatisticsFieldPanel;
import com.supermap.desktop.process.parameter.events.ParameterUpdateValueEvent;
import com.supermap.desktop.process.parameter.events.UpdateValueListener;
import com.supermap.desktop.process.parameter.implement.ParameterStatisticsField;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by hanyz on 2017/5/5.
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.STATISTICS_FIELD)
public class ParameterStatisticsFieldPanel extends StatisticsFieldPanel implements IParameterPanel {
	private ParameterStatisticsField parameterStatisticsField;

	public ParameterStatisticsFieldPanel(IParameter parameterStatisticsField) {
		super(null);
		this.parameterStatisticsField = (ParameterStatisticsField) parameterStatisticsField;
		this.setPreferredSize(new Dimension(this.getWidth(), 200));
		this.setMinimumSize(new Dimension(this.getWidth(), 200));
		this.parameterStatisticsField.addUpdateValueListener(new UpdateValueListener() {
			@Override
			public void fireUpdateValue(ParameterUpdateValueEvent evt) {
				if (evt.getFieldName().equals(ParameterStatisticsField.STATISTICS_FIELDINFO)) {
					ParameterStatisticsFieldPanel.this.parameterStatisticsField.setStatisticsFieldInfos(ParameterStatisticsFieldPanel.this.getStatisticsFieldInfos());
				}
			}
		});
		this.parameterStatisticsField.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterStatisticsField.DATASET_FIELD_NAME)) {
					DatasetVector newValue = (DatasetVector) evt.getNewValue();
					ParameterStatisticsFieldPanel.this.setDataset(newValue);
					ParameterStatisticsFieldPanel.this.repaint();
				}
			}
		});
		if (this.parameterStatisticsField.getDataset() != null) {
			this.setDataset(this.parameterStatisticsField.getDataset());
			this.repaint();
		}
	}

	@Override
	public Object getPanel() {
		return this;
	}
}
