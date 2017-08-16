package com.supermap.desktop.process.parameters.ParameterPanels.StatisticsField;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedEvent;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.parameter.ipls.ParameterStatisticsFieldGroupForRarefyPoints;
import com.supermap.desktop.process.parameters.ParameterPanels.SwingPanel;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.smTables.TableFactory;
import com.supermap.desktop.ui.controls.smTables.tables.TableRarefyPoints;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by lixiaoyao on 2017/8/15.
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.STATISTICS_FIELD_FOR_RAREFY_POINTS)
public class ParameterStatisticsFieldGroupForRarefyPointsPanel extends SwingPanel {
	private ParameterStatisticsFieldGroupForRarefyPoints parameterStatisticsFieldGroupForRarefyPoints;
	private JLabel label = new JLabel();
	private TableRarefyPoints tableRarefyPoints=(TableRarefyPoints) TableFactory.getTable("StatisticsFieldForRarefyPoints");

	public ParameterStatisticsFieldGroupForRarefyPointsPanel(IParameter parameter) {
		super(parameter);
		parameterStatisticsFieldGroupForRarefyPoints = (ParameterStatisticsFieldGroupForRarefyPoints) parameter;
		initComponent();
		initLayout();
		initListener();
		tableRarefyPoints.setDatasetVector(parameterStatisticsFieldGroupForRarefyPoints.getDataset());
	}

	private void initComponent() {
		label.setText(parameterStatisticsFieldGroupForRarefyPoints.getDescribe());
		label.setToolTipText(parameterStatisticsFieldGroupForRarefyPoints.getDescribe());
		tableRarefyPoints.setDatasetVector(null);
	}

	private void initListener() {
		parameterStatisticsFieldGroupForRarefyPoints.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterStatisticsFieldGroupForRarefyPoints.FIELD_DATASET)) {
					tableRarefyPoints.setDatasetVector(parameterStatisticsFieldGroupForRarefyPoints.getDataset());
				}else if (evt.getPropertyName().equals(ParameterStatisticsFieldGroupForRarefyPoints.STATISTICS_FIELD_TYPE)){
					tableRarefyPoints.setAllStatisticsFieldType(parameterStatisticsFieldGroupForRarefyPoints.getStatisticsFieldType());
				}
			}
		});

		tableRarefyPoints.getTablesModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE) {
					parameterStatisticsFieldGroupForRarefyPoints.setSelectedStatisticsFields(tableRarefyPoints.getStatisticsField());
				}
			}
		});
	}

	@Override
	public void fieldConstraintChanged(FieldConstraintChangedEvent event) {
		if (event.getFieldName().equals(ParameterStatisticsFieldGroupForRarefyPoints.FIELD_DATASET)) {
			tableRarefyPoints.setDatasetVector(parameterStatisticsFieldGroupForRarefyPoints.getDataset());
		}
	}

	private void initLayout() {
		panel.setPreferredSize(new Dimension(200, 200));
		panel.setLayout(new GridBagLayout());
		panel.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));
		JScrollPane scrollPane = new JScrollPane(tableRarefyPoints);
		scrollPane.setPreferredSize(new Dimension(200, 200));
		panel.add(scrollPane, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(5, 0, 0, 0));
	}
}
