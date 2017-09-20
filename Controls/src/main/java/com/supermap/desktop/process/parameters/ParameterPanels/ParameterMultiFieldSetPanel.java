package com.supermap.desktop.process.parameters.ParameterPanels;

import com.supermap.data.DatasetVector;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedEvent;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedListener;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.parameter.ipls.ParameterMultiFieldSet;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.ChooseTable.SmMultiFieldsChooseTable;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by xie on 2017/8/7.
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.MULTIFIELDSET)
public class ParameterMultiFieldSetPanel extends SwingPanel {
	private ParameterMultiFieldSet multiFieldSet;
	private SmMultiFieldsChooseTable multiFieldsChooseTable;
	private JScrollPane scrollPane;
	private boolean isSelectionChanged = false;

	public ParameterMultiFieldSetPanel(IParameter parameter) {
		super(parameter);
		this.multiFieldSet = (ParameterMultiFieldSet) parameter;
		init();
	}

	private void init() {
		scrollPane = new JScrollPane();
		multiFieldsChooseTable = new SmMultiFieldsChooseTable(this.multiFieldSet.getDatasetVector());
		initLayout();
		registEvents();
	}

	private void initLayout() {
		scrollPane.setPreferredSize(new Dimension(300, 200));
		panel.setLayout(new GridBagLayout());
		panel.add(scrollPane, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		scrollPane.setViewportView(multiFieldsChooseTable);
		panel.setBorder(new TitledBorder(CommonProperties.getString("String_AddFields")));
	}

	private void registEvents() {
		final JTableHeader tableHeader = this.multiFieldsChooseTable.getTableHeader();
		tableHeader.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 0) {
					//获得选中列
					int selectColumn = tableHeader.columnAtPoint(e.getPoint());
					if (selectColumn == 0) {
						setFieldInfo();
					}
				}
			}
		});
		this.multiFieldsChooseTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				setFieldInfo();
			}
		});
		this.multiFieldsChooseTable.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				setFieldInfo();
			}
		});
		this.multiFieldSet.addFieldConstraintChangedListener(new FieldConstraintChangedListener() {
			@Override
			public void fieldConstraintChanged(FieldConstraintChangedEvent event) {
				DatasetVector datasetVector = ((ParameterMultiFieldSet) event.getParameter()).getDatasetVector();
				if (null != datasetVector) {
					multiFieldsChooseTable.init(datasetVector);
				}
			}
		});
	}

	private void setFieldInfo() {
		if (!isSelectionChanged) {
			isSelectionChanged = true;
			int rowCount = this.multiFieldsChooseTable.getRowCount();
			ArrayList<String> sourceFields = new ArrayList<>();
			ArrayList<String> targetFields = new ArrayList<>();
			for (int i = 0; i < rowCount; i++) {
				boolean selected = (boolean) this.multiFieldsChooseTable.getValueAt(i, multiFieldsChooseTable.TABLE_COLUMN_CHECKABLE);
				if (selected) {
					sourceFields.add((String) this.multiFieldsChooseTable.getValueAt(i, multiFieldsChooseTable.TABLE_COLUMN_SOURCEFIELDNAME));
					targetFields.add((String) this.multiFieldsChooseTable.getValueAt(i, multiFieldsChooseTable.TABLE_COLUMN_TARGETFIELDNAME));
				}
			}
			ParameterMultiFieldSet.DatasetFieldInfo fieldInfo = new ParameterMultiFieldSet.DatasetFieldInfo();
			fieldInfo.setSourceFields(sourceFields.toArray(new String[sourceFields.size()]));
			fieldInfo.setTargetFields(targetFields.toArray(new String[targetFields.size()]));
			multiFieldSet.setDatasetFieldInfo(fieldInfo);
			isSelectionChanged = false;
		}
	}

}
