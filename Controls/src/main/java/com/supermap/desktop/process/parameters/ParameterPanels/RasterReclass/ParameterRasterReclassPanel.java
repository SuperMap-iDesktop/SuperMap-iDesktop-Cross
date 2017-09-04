package com.supermap.desktop.process.parameters.ParameterPanels.RasterReclass;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedEvent;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.parameters.ParameterPanels.SwingPanel;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by lixiaoyao on 2017/9/1.
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.RASTER_RECLASS)
public class ParameterRasterReclassPanel extends SwingPanel{

	private ParameterRasterReclass parameterRasterReclass;
	private RasterReclassValuePanel rasterReclassValuePanel;

	public ParameterRasterReclassPanel(IParameter parameter){
		super(parameter);
		parameterRasterReclass=(ParameterRasterReclass)parameter;
		initComponent();
		initLayout();
		initListener();
		rasterReclassValuePanel.setDataset(parameterRasterReclass.getDataset());
	}

	private void initComponent(){
		rasterReclassValuePanel=new RasterReclassValuePanel();
	}

	private void initLayout(){
		panel.setLayout(new GridBagLayout());
		panel.add(rasterReclassValuePanel, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(5, 0, 0, 0));
	}

	private void initListener() {
		parameterRasterReclass.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				System.out.println(1111);
				if (evt.getPropertyName().equals(ParameterRasterReclass.FIELD_DATASET)) {
					rasterReclassValuePanel.setDataset(parameterRasterReclass.getDataset());
				}
//				else if (evt.getPropertyName().equals(ParameterStatisticsFieldGroupForRarefyPoints.STATISTICS_FIELD_TYPE)){
//					tableRarefyPoints.setAllStatisticsFieldType(parameterStatisticsFieldGroupForRarefyPoints.getStatisticsFieldType());
//				}
			}
		});
	}

	@Override
	public void fieldConstraintChanged(FieldConstraintChangedEvent event) {
		if (event.getFieldName().equals(ParameterRasterReclass.FIELD_DATASET)) {
			System.out.println(parameterRasterReclass.getDataset().getName());

			rasterReclassValuePanel.setDataset(parameterRasterReclass.getDataset());
		}
	}

}
