package com.supermap.desktop.mapview.geometry.property.geometryNode;

import com.supermap.data.Recordset;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Implements.DGeoBSpline;
import com.supermap.desktop.geometry.Implements.DGeoCardinal;
import com.supermap.desktop.geometry.Implements.DGeoCurve;
import com.supermap.desktop.mapview.geometry.property.geometryNode.parameterizationModels.GeometryNodeParameterTableModel;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilties.GeometryTypeUtilties;
import com.supermap.desktop.utilties.StringUtilties;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * @author XiaJT
 */
public class JPanelGeometryNodeParameterization extends JPanel implements IGeometryNode {
	private IGeometry geometry;
	private JLabel labelGeometryType = new JLabel();
	private JTextField textFieldGeometryType = new JTextField();
	private JTable table = new JTable();
	private GeometryNodeParameterTableModel tableModel;
	private DecimalFormat df = new DecimalFormat("0.0000");
	private JLabel labelControlInfo = new JLabel();
	private DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel label = new JLabel();
			label.setOpaque(true);
			if (row == 0) {
				label.setText(String.valueOf(tableModel.getValueAtRow0(row, column, isSelected)));
			} else {
				if (isSelected) {
					label.setText(String.valueOf(value));
				} else {
					label.setText(df.format(value));
				}
			}
			if (isSelected) {
				label.setBackground(table.getSelectionBackground());
			} else {
				label.setBackground(table.getBackground());
			}
			return label;
		}
	};

	public JPanelGeometryNodeParameterization(IGeometry geometry) {
		this.geometry = geometry;
		init();
	}

	private void init() {
		initComponents();
		initListener();
		initLayout();
		initResources();
		initComponentState();
	}

	private void initComponents() {
		tableModel = new GeometryNodeParameterTableModel(geometry);
		table.setModel(tableModel);
		initTable();
		textFieldGeometryType.setMinimumSize(new Dimension(250, 23));
		textFieldGeometryType.setPreferredSize(new Dimension(250, 23));
		textFieldGeometryType.setEditable(false);
		labelControlInfo.setVisible(isLabelControlVisible());
	}

	private boolean isLabelControlVisible() {
		return geometry instanceof DGeoBSpline || geometry instanceof DGeoCardinal || geometry instanceof DGeoCurve;
	}

	private void initTable() {
		if (StringUtilties.isNullOrEmpty(table.getColumnName(0))) {
			DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
			defaultTableCellRenderer.setPreferredSize(new Dimension(0, 0));
			table.getTableHeader().setDefaultRenderer(defaultTableCellRenderer);
		}
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setRowHeight(23);
		table.setColumnSelectionAllowed(true);
		table.setRowSelectionAllowed(true);
		table.setDefaultRenderer(Double.class, cellRenderer);
		if (table.getColumnCount() > 2) {
			table.getColumnModel().getColumn(0).setMaxWidth(120);
		}
	}

	private void initLayout() {
		this.setLayout(new GridBagLayout());
		this.add(labelGeometryType, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setWeight(0, 0).setInsets(10, 10, 5, 0));
		this.add(textFieldGeometryType, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setWeight(0, 0).setInsets(10, 20, 5, 0));
		this.add(new JPanel(), new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 0).setInsets(10, 0, 5, 10));

		this.add(labelControlInfo, new GridBagConstraintsHelper(0, 1, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setWeight(1, 0).setInsets(15, 10, 0, 10));
		this.add(new JScrollPane(table), new GridBagConstraintsHelper(0, 2, 3, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setInsets(0, 10, 0, 10));

	}

	private void initListener() {
		table.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				initTable();
			}
		});
	}

	private void initResources() {
		labelControlInfo.setText(ControlsProperties.getString("String_labelControlPointsInfo"));
		labelGeometryType.setText(ControlsProperties.getString("String_LabelGeometryType"));
	}

	private void initComponentState() {
		textFieldGeometryType.setText(GeometryTypeUtilties.toString(geometry.getGeometry().getType()));
	}

	@Override
	public void refreshData() {
		initComponentState();
		tableModel.refreshData();
	}

	@Override
	public JPanel getPanel() {
		return this;
	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isModified() {
		return false;
	}

	@Override
	public void reset() {
		// 不需要
	}

	@Override
	public void apply(Recordset recordset) {
		// 不需要
	}

	@Override
	public void addModifiedChangedListener(ModifiedChangedListener modifiedChangedListener) {
		// 不需要
	}

	@Override
	public void removeModifiedChangedListener(ModifiedChangedListener modifiedChangedListener) {
		// 不需要
	}

	@Override
	public void hidden() {
		// 不需要
	}

	@Override
	public void setIsCellEditable(boolean isCellEditable) {
		// 不关我事
	}
}
