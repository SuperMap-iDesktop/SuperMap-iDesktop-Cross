package com.supermap.desktop.process.parameters.ParameterPanels.StatisticsField;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedEvent;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedListener;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.parameter.ipls.ParameterDatasetChooseTable;
import com.supermap.desktop.process.parameters.ParameterPanels.JPanelDatasetChooseForParameter;
import com.supermap.desktop.process.parameters.ParameterPanels.SwingPanel;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by xie on 2017/8/9.
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.DATASET_CHOOSE_TABLE)
public class ParameterDatasetChooseTablePanel extends SwingPanel {
	private ParameterDatasetChooseTable datasetChooseTable;
	private JPanelDatasetChooseForParameter datasetChoosePanel;
	private final String[] columnNames = {"", CommonProperties.getString("String_ColumnHeader_Dataset"), CommonProperties.getString("String_ColumnHeader_Datasource")};
	private final boolean[] enables = {false, false, false};

	public ParameterDatasetChooseTablePanel(IParameter parameter) {
		super(parameter);
		this.datasetChooseTable = (ParameterDatasetChooseTable) parameter;
		init();
	}

	private void init() {
		initComponents();
		initLayout();
		registerEvents();
	}

	private void registerEvents() {
		datasetChooseTable.addFieldConstraintChangedListener(new FieldConstraintChangedListener() {
			@Override
			public void fieldConstraintChanged(FieldConstraintChangedEvent event) {
				Dataset dataset = ((ParameterDatasetChooseTable) event.getParameter()).getDataset();
				if (null != dataset) {
					datasetChoosePanel.setIllegalDataset(dataset);
					datasetChoosePanel.setSupportDatasetTypes(new DatasetType[]{dataset.getType()});
				}
			}
		});
		this.datasetChoosePanel.getTableModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				datasetChooseTable.setSelectedItem(datasetChoosePanel.getDatasets());
			}
		});
	}

	private void initLayout() {
		panel.setLayout(new GridBagLayout());
		panel.add(this.datasetChoosePanel, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		panel.setBorder(new TitledBorder(CommonProperties.getString("String_ColumnHeader_SourceData")));
	}

	private void initComponents() {
		this.datasetChoosePanel = new JPanelDatasetChooseForParameter((ArrayList<Dataset>) datasetChooseTable.getSelectedItem(), columnNames, enables);
		this.datasetChoosePanel.setIllegalDataset(this.datasetChooseTable.getDataset());
		if (null != this.datasetChooseTable.getDataset()) {
			this.datasetChoosePanel.setSupportDatasetTypes(new DatasetType[]{this.datasetChooseTable.getDataset().getType()});
		}
	}
}
