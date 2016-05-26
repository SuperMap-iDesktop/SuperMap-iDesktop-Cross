package com.supermap.desktop.mapview.geometry.property.geometryNode;

import com.supermap.data.GeoLine;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ILine3DFeature;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IMultiPartFeature;
import com.supermap.desktop.geometry.Abstract.IRegion3DFeature;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;
import com.supermap.desktop.mapview.geometry.property.geometryNode.vectorTableModels.VectorTableModel;
import com.supermap.desktop.mapview.geometry.property.geometryNode.vectorTableModels.VectorTableModelFactory;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.utilties.GeometryTypeUtilties;
import com.supermap.mapping.Map;
import com.supermap.ui.MapControl;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 矢量的节点信息面板
 *
 * @author XiaJT
 */
public class JPanelGeometryNodeVector extends JPanel implements IGeometryNode {

	private IGeometry geometry = null;
	// 对象信息面板
	private JPanel panelGeometryProperty = new JPanel();

	private JLabel labelGeometryType = new JLabel();
	private SmTextFieldLegit textFieldGeometryType = new SmTextFieldLegit();

	private JLabel labelSubGeometryCount = new JLabel();
	private SmTextFieldLegit textFieldSubGeometryCount = new SmTextFieldLegit();

	private JLabel labelCurrentSubGeometry = new JLabel();
	private JComboBox<Object> comboBoxCurrentSubGeometry = new JComboBox<>();

	private JLabel labelNodeCount = new JLabel();
	private SmTextFieldLegit textFieldNodeCount = new SmTextFieldLegit();

	// 节点信息面板
	private JPanel panelNodeInfo = new JPanel();
	private JScrollPane jScrollPaneNodeInfo = new JScrollPane();
	private JTable tableNodeInfo = new JTable();

	private JButton buttonAdd = new JButton();
	private JButton buttonInsert = new JButton();
	private JButton buttonDel = new JButton();
	private GeometryNodeVectorTableModel currentTableModel;
	private java.util.List<VectorTableModel> tableModels;
	private List<ModifiedChangedListener> listModifiedChangedListeners = new ArrayList<>();
	private static final String tag = "NodeInfoTag";
	private Map currentMap;
	private Window parent = null;
	private WindowAdapter windowAdapter;


	public JPanelGeometryNodeVector(IGeometry geometry) {
		this.geometry = geometry;
		init();
	}

	@Override
	public void refreshData() {
		initComponentState();
	}

	private void init() {
		initComponents();
		initLayout();
		initListener();
		initResources();
		initComponentState();
	}

	private void initComponents() {
		windowAdapter = new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent e) {
				hideClean();
			}
		};
		tableModels = new ArrayList<>();
		if (geometry instanceof IMultiPartFeature) {
			for (int i = 0; i < ((IMultiPartFeature) geometry).getPartCount(); i++) {
				VectorTableModel vectorTableModel = VectorTableModelFactory.getVectorTableModel(((IMultiPartFeature) geometry).getPart(i));
				vectorTableModel.setId(geometry.getGeometry().getID());
				vectorTableModel.setPartIndex(i);
				tableModels.add(vectorTableModel);
			}
		} else {
			tableModels.add(VectorTableModelFactory.getVectorTableModel(geometry));
		}
		textFieldGeometryType.setEditable(false);
		textFieldNodeCount.setEditable(false);
		textFieldSubGeometryCount.setEditable(false);
		currentTableModel = new GeometryNodeVectorTableModel();
		tableNodeInfo.setModel(currentTableModel);
		comboBoxCurrentSubGeometry.setRenderer(new ListCellRenderer<Object>() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel label = new JLabel();
				label.setOpaque(true);
				if (index == -1) {
					index = comboBoxCurrentSubGeometry.getSelectedIndex();
				}
				label.setText(MessageFormat.format(ControlsProperties.getString("String_TheNumberSubObject"), index + 1));
				if (isSelected) {
					label.setBackground(list.getSelectionBackground());
				} else {
					label.setBackground(list.getBackground());
				}
				return label;
			}
		});
	}

	//region 初始化布局
	private void initLayout() {
		initPanelGeometryProperty();
		initPanelNodeInfo();
		this.setLayout(new GridBagLayout());
		this.add(panelGeometryProperty, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(0, 1));
		this.add(panelNodeInfo, new GridBagConstraintsHelper(1, 0, 4, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setInsets(0, 5, 0, 0));

		this.add(new JPanel(), new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(0, 0).setFill(GridBagConstraints.BOTH));
		this.add(buttonAdd, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(0, 0).setFill(GridBagConstraints.NONE).setInsets(5, 5, 20, 0));
		this.add(buttonInsert, new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(0, 0).setFill(GridBagConstraints.NONE).setInsets(5, 5, 20, 0));
		this.add(buttonDel, new GridBagConstraintsHelper(3, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(0, 0).setFill(GridBagConstraints.NONE).setInsets(5, 5, 20, 10));
		this.add(new JPanel(), new GridBagConstraintsHelper(4, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0).setFill(GridBagConstraints.BOTH));
	}

	private void initPanelGeometryProperty() {
		panelGeometryProperty.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_GeometryPropertyVertexInfoControl_GroupBoxGeometryInfo"))));
		panelGeometryProperty.setLayout(new GridBagLayout());
		panelGeometryProperty.add(labelGeometryType, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 0, 10));
		panelGeometryProperty.add(textFieldGeometryType, new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 0, 10));

		panelGeometryProperty.add(labelSubGeometryCount, new GridBagConstraintsHelper(0, 2, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 0, 10));
		panelGeometryProperty.add(textFieldSubGeometryCount, new GridBagConstraintsHelper(0, 3, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 0, 10));

		panelGeometryProperty.add(labelCurrentSubGeometry, new GridBagConstraintsHelper(0, 4, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 0, 10));
		panelGeometryProperty.add(comboBoxCurrentSubGeometry, new GridBagConstraintsHelper(0, 5, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 0, 10));

		panelGeometryProperty.add(labelNodeCount, new GridBagConstraintsHelper(0, 6, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 0, 10));
		panelGeometryProperty.add(textFieldNodeCount, new GridBagConstraintsHelper(0, 7, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 0, 10));

		panelGeometryProperty.add(new JPanel(), new GridBagConstraintsHelper(0, 8, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setInsets(5, 10, 10, 10));

	}

	private void initPanelNodeInfo() {
		panelNodeInfo.setBorder(BorderFactory.createTitledBorder(CoreProperties.getString("String_NodeInfo")));
		jScrollPaneNodeInfo.setViewportView(tableNodeInfo);
		panelNodeInfo.setLayout(new GridBagLayout());
		panelNodeInfo.add(jScrollPaneNodeInfo, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER));
	}
	//endregion

	private void initListener() {
		comboBoxCurrentSubGeometry.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					currentTableModel.setModel(tableModels.get(comboBoxCurrentSubGeometry.getSelectedIndex()));
					tableNodeInfo.getColumnModel().getColumn(0).setMaxWidth(50);
					textFieldNodeCount.setText(String.valueOf(currentTableModel.getRowCount()));
				}
			}
		});
		buttonAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = tableNodeInfo.getSelectedRow();
				if (selectedRow == -1) {
					selectedRow = tableNodeInfo.getRowCount() - 1;
				}
				currentTableModel.addPoint(selectedRow);
				tableNodeInfo.setRowSelectionInterval(selectedRow + 1, selectedRow + 1);
				tableNodeInfo.scrollRectToVisible(tableNodeInfo.getCellRect(selectedRow + 1, 0, true));
				textFieldNodeCount.setText(String.valueOf(currentTableModel.getRowCount()));
			}
		});

		buttonInsert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = tableNodeInfo.getSelectedRow();
				if (selectedRow == -1) {
					selectedRow = tableNodeInfo.getRowCount();
				}
				currentTableModel.insertPoint(selectedRow);
				tableNodeInfo.setRowSelectionInterval(selectedRow + 1, selectedRow + 1);
				tableNodeInfo.scrollRectToVisible(tableNodeInfo.getCellRect(selectedRow + 1, 0, true));
				textFieldNodeCount.setText(String.valueOf(currentTableModel.getRowCount()));
			}
		});

		buttonDel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = tableNodeInfo.getSelectedRow();
				currentTableModel.removeRows(tableNodeInfo.getSelectedRows());
				if (selectedRow == -1 && tableNodeInfo.getRowCount() > 0) {
					selectedRow = 0;

				} else if (selectedRow >= tableNodeInfo.getRowCount()) {
					selectedRow = tableNodeInfo.getRowCount() - 1;
				}
				tableNodeInfo.setRowSelectionInterval(selectedRow, selectedRow);
				tableNodeInfo.scrollRectToVisible(tableNodeInfo.getCellRect(0, 0, true));

				textFieldNodeCount.setText(String.valueOf(currentTableModel.getRowCount()));
			}
		});

		tableNodeInfo.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int selectedRowCount = tableNodeInfo.getSelectedRowCount();
				buttonDel.setEnabled(isButtonEnable() && selectedRowCount > 0 && currentTableModel.getRowCount() - selectedRowCount >= getMinRowCount());
				buttonInsert.setEnabled(isButtonEnable() && selectedRowCount == 1);
				if (selectedRowCount == 1) {
					showPointInMap();
				} else {
					removeBeforeTag();
				}
			}
		});

		tableNodeInfo.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				fireModifiedChanged();
			}
		});

	}

	private void showPointInMap() {
		if (Application.getActiveApplication().getActiveForm() instanceof FormMap) {
			double x = (double) tableNodeInfo.getValueAt(tableNodeInfo.getSelectedRow(), 1);
			double y = (double) tableNodeInfo.getValueAt(tableNodeInfo.getSelectedRow(), 2);
			MapControl mapControl = ((FormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
			Point2Ds point2Ds = new Point2Ds();
			double offset = mapControl.getMap().getViewBounds().getWidth() * 30 / mapControl.getSize().getWidth();
			point2Ds.add(new Point2D(x - offset, y));
			point2Ds.add(new Point2D(x + offset, y));
			GeoLine paintLine = new GeoLine(point2Ds);
			Point2Ds point2Ds1 = new Point2Ds();
			point2Ds1.add(new Point2D(x, y - offset));
			point2Ds1.add(new Point2D(x, y + offset));
			paintLine.addPart(point2Ds1);
			removeBeforeTag();
			currentMap = mapControl.getMap();
			currentMap.getTrackingLayer().add(paintLine, tag);
			currentMap.refreshTrackingLayer();
		}
	}

	private void removeBeforeTag() {
		if (currentMap != null) {
			for (int count = currentMap.getTrackingLayer().getCount() - 1; count >= 0; count--) {
				if (currentMap.getTrackingLayer().getTag(count).equals(tag)) {
					currentMap.getTrackingLayer().remove(count);
				}
			}
			currentMap.refreshTrackingLayer();
		}

	}

	private int getMinRowCount() {
		if (geometry instanceof ILineFeature || geometry instanceof ILine3DFeature) {
			return 2;
		}
		if (geometry instanceof IRegionFeature || geometry instanceof IRegion3DFeature) {
			return 3;
		}
		return 1;
	}

	private void initResources() {
		labelGeometryType.setText(ControlsProperties.getString("String_LabelGeometryType"));
		labelSubGeometryCount.setText(ControlsProperties.getString("String_Label_PartCount_S"));
		labelCurrentSubGeometry.setText(ControlsProperties.getString("String_GeometryPropertyVertexInfoControl_LabelCurrentPart"));
		labelNodeCount.setText(ControlsProperties.getString("String_Label_TotalNodeCount_T"));

		buttonAdd.setText(ControlsProperties.getString("String_GeometryPropertyVertexInfoControl_ButtonAdd"));
		buttonInsert.setText(ControlsProperties.getString("String_GeometryPropertyVertexInfoControl_ButtonInsert"));
		buttonDel.setText(ControlsProperties.getString("String_DeleteNode"));
	}

	private void initComponentState() {
		initComboBoxCurrentSubGeometry();
		if (comboBoxCurrentSubGeometry.getItemCount() <= 0) {
			currentTableModel.setModel(tableModels.get(0));
		}
		buttonAdd.setEnabled(isButtonEnable());
		textFieldGeometryType.setText(GeometryTypeUtilties.toString(geometry.getGeometry().getType()));
		textFieldSubGeometryCount.setText(String.valueOf(getSubPartCount()));
		textFieldNodeCount.setText(String.valueOf(currentTableModel.getRowCount()));
		tableNodeInfo.getColumnModel().getColumn(0).setMaxWidth(50);
		if (tableNodeInfo.getRowCount() > 0) {
			tableNodeInfo.setRowSelectionInterval(0, 0);
		}
	}

	private boolean isButtonEnable() {
		return getMinRowCount() > 1;
	}

	private int getSubPartCount() {
		if (geometry instanceof IMultiPartFeature) {
			return ((IMultiPartFeature) geometry).getPartCount();
		}
		return 1;
	}


	private void initComboBoxCurrentSubGeometry() {
		comboBoxCurrentSubGeometry.removeAllItems();
		if (geometry instanceof IMultiPartFeature) {
			int partCount = ((IMultiPartFeature) geometry).getPartCount();
			for (int i = 0; i < partCount; i++) {
				Object part = ((IMultiPartFeature) geometry).getPart(i);
				if (part != null) {
					comboBoxCurrentSubGeometry.addItem(part);
				}
			}
		}
		comboBoxCurrentSubGeometry.setEnabled(comboBoxCurrentSubGeometry.getItemCount() > 0);
	}


	@Override
	public JPanel getPanel() {
		return this;
	}

	@Override
	public void dispose() {
		for (VectorTableModel tableModel : tableModels) {
			tableModel.dispose();
		}
		hideClean();
		geometry.dispose();
	}

	public void hideClean() {
		removeBeforeTag();
	}

	@Override
	public boolean isModified() {
		for (VectorTableModel tableModel : tableModels) {
			if (tableModel.isModified()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void reset() {
		currentTableModel.reset();
	}

	@Override
	public void apply(Recordset recordset) {
		stopEdit();
		currentTableModel.apply(recordset);
	}

	private void stopEdit() {
		TableCellEditor cellEditor = tableNodeInfo.getCellEditor();
		if (cellEditor != null) {
			cellEditor.stopCellEditing();
		}
	}

	@Override
	public void addModifiedChangedListener(ModifiedChangedListener modifiedChangedListener) {
		this.listModifiedChangedListeners.add(modifiedChangedListener);
	}

	@Override
	public void removeModifiedChangedListener(ModifiedChangedListener modifiedChangedListener) {
		this.listModifiedChangedListeners.remove(modifiedChangedListener);
	}

	private void fireModifiedChanged() {
		for (ModifiedChangedListener listModifiedChangedListener : listModifiedChangedListeners) {
			listModifiedChangedListener.modified(isModified());
		}
	}

	@Override
	public void paint(Graphics g) {
		if (parent == null) {
			initParent();
		}
		super.paint(g);
	}


	private void initParent() {
		parent = SwingUtilities.getWindowAncestor(this);
		parent.removeWindowListener(windowAdapter);
		parent.addWindowListener(windowAdapter);
	}

	@Override
	public void hidden() {
		hideClean();
	}
}
