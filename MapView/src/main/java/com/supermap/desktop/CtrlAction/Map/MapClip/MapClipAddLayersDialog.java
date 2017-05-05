package com.supermap.desktop.CtrlAction.Map.MapClip;

import com.supermap.desktop.Application;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.borderPanel.PanelButton;
import com.supermap.desktop.ui.controls.mutiTable.DDLExportTableModel;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTable;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.mapping.Layer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;

import static com.supermap.desktop.CtrlAction.Map.MapClip.MapClipTableModel.COLUMN_INDEX_LAYERCAPTION;

/**
 * Created by lixiaoyao on 2017/4/10.
 */
public class MapClipAddLayersDialog extends SmDialog {
    private JToolBar toolBar;
    private JButton buttonSelectAll;
    private JButton buttonInterverseSelectAll;
    private JScrollPane scrollPane;
    private MutiTable mutiTable;
    private JPanel mainPanel;
    private PanelButton panelButton;
    private int rowHeightSize = 23;
    private static final int COLUMN_INDEX = 0;
    private static final int COLUMN_LAYER_TITLE = 1;
    private Vector notClipLayer;
    private Vector resultAddLayers;

    public MapClipAddLayersDialog(Vector isNotClipLayer) {
        this.notClipLayer = isNotClipLayer;
        initComponents();
        initLayout();
        initTable();
        setShowTableLayer();
        initResources();
        removeEvents();
        registerEvents();
        this.componentList.add(this.panelButton.getButtonOk());
        this.componentList.add(this.panelButton.getButtonCancel());
        this.setFocusTraversalPolicy(policy);
    }

    private void initComponents() {
        this.toolBar = new JToolBar();
        this.buttonSelectAll = new JButton();
        this.buttonInterverseSelectAll = new JButton();
        this.scrollPane = new JScrollPane();
        this.mutiTable = new MutiTable();
        this.mainPanel = new JPanel();
        this.panelButton = new PanelButton();
        this.mutiTable = new MutiTable();
        this.toolBar.add(this.buttonSelectAll);
        this.toolBar.add(this.buttonInterverseSelectAll);
    }

    private void initLayout() {
        Dimension dimension = new Dimension(500, 375);
        setSize(dimension);
        setMinimumSize(dimension);
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(this.panelButton.getButtonOk());
        GroupLayout groupLayout = new GroupLayout(this.mainPanel);
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);

        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(this.toolBar)
                .addComponent(this.scrollPane)
        );
        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addComponent(this.toolBar, 30, 30, 30)
                .addComponent(this.scrollPane)
        );

        this.scrollPane.setViewportView(this.mutiTable);
        this.mainPanel.setLayout(groupLayout);
        this.setLayout(new BorderLayout());
        this.add(this.mainPanel, BorderLayout.CENTER);
        this.add(this.panelButton, BorderLayout.SOUTH);
        this.toolBar.setFloatable(false);
    }

    private void initTable() {
        DDLExportTableModel tableModel = new DDLExportTableModel(new String[]{MapViewProperties.getString("String_MapClip_INDEX"), MapViewProperties.getString("String_MapClip_LayerCaption")}) {
            boolean[] columnEditables = new boolean[]{false, false, false};

            @Override
            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        };
        this.mutiTable.setModel(tableModel);
        this.mutiTable.setRowHeight(this.rowHeightSize);
        this.mutiTable.getColumnModel().getColumn(COLUMN_INDEX).setMaxWidth(40);
        this.mutiTable.getColumnModel().getColumn(COLUMN_LAYER_TITLE).setCellRenderer(new MapClipLayerCaptionTableRender());
    }

    private void initResources() {
        this.setTitle(MapViewProperties.getString("String_MapClip_AddClipLayer"));
        this.buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
        this.buttonSelectAll.setToolTipText(CommonProperties.getString("String_ToolBar_SelectAll"));
        this.buttonInterverseSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
        this.buttonInterverseSelectAll.setToolTipText(CommonProperties.getString("String_ToolBar_SelectInverse"));
    }

    private void registerEvents() {
        this.buttonSelectAll.addActionListener(this.actionListener);
        this.buttonInterverseSelectAll.addActionListener(this.actionListener);
        this.panelButton.getButtonOk().addActionListener(this.actionListener);
        this.panelButton.getButtonCancel().addActionListener(this.actionListener);
        this.mutiTable.addMouseListener(this.tableMouseClickListener);
    }

    private void removeEvents() {
        this.buttonSelectAll.removeActionListener(this.actionListener);
        this.buttonInterverseSelectAll.removeActionListener(this.actionListener);
        this.panelButton.getButtonOk().removeActionListener(this.actionListener);
        this.panelButton.getButtonCancel().removeActionListener(this.actionListener);
        this.mutiTable.removeMouseListener(this.tableMouseClickListener);
    }

    public void setShowTableLayer(Vector isNotClipLayer) {
        DDLExportTableModel model = (DDLExportTableModel) this.mutiTable.getModel();
        model.removeRows(0, model.getRowCount());
        for (int i = 0; i < isNotClipLayer.size(); i++) {
            Layer layer = (Layer) ((Vector) (isNotClipLayer.get(i))).get(COLUMN_INDEX_LAYERCAPTION);
            Object[] temp = new Object[2];
            temp[COLUMN_INDEX] = i + 1 + " ";
            temp[COLUMN_LAYER_TITLE] = layer.getCaption();
            this.mutiTable.addRow(temp);
        }
    }

    private void setShowTableLayer() {
        if (this.notClipLayer != null && this.notClipLayer.size() > 0) {
            for (int i = 0; i < this.notClipLayer.size(); i++) {
                Layer layer = (Layer) ((Vector) (this.notClipLayer.get(i))).get(COLUMN_INDEX_LAYERCAPTION);
                Object[] temp = new Object[2];
                temp[COLUMN_INDEX] = i + 1 + " ";
                temp[COLUMN_LAYER_TITLE] = layer;
                this.mutiTable.addRow(temp);
            }
            this.mutiTable.setRowSelectionInterval(0,0);
        }
        isCanOK();
    }

    public Vector getResultAddLayers(){
        return this.resultAddLayers;
    }

    private void isCanOK(){
        if (this.mutiTable.getRowCount()>0 && this.mutiTable.getSelectedRow()>=0 ){
            this.panelButton.getButtonOk().setEnabled(true);
        }else{
            this.panelButton.getButtonOk().setEnabled(false);
        }
    }

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(buttonSelectAll)) {
                if (mutiTable.getRowCount()>=1) {
                    mutiTable.setRowSelectionInterval(0, mutiTable.getRowCount() - 1);
                }
                isCanOK();
            } else if (e.getSource().equals(buttonInterverseSelectAll)) {
                try {
                    int[] temp = mutiTable.getSelectedRows();
                    ListSelectionModel selectionModel = mutiTable.getSelectionModel();
                    int allRowCount = mutiTable.getRowCount();
                    ArrayList<Integer> selectedRows = new ArrayList<Integer>();
                    for (int index = 0; index < temp.length; index++) {
                        selectedRows.add(temp[index]);
                    }
                    selectionModel.clearSelection();
                    for (int index = 0; index < allRowCount; index++) {
                        if (!selectedRows.contains(index)) {
                            selectionModel.addSelectionInterval(index, index);
                        }
                    }
                    isCanOK();
                } catch (Exception ex) {
                    Application.getActiveApplication().getOutput().output(ex);
                }
            } else if (e.getSource().equals(panelButton.getButtonOk())) {
                MapClipAddLayersDialog.this.setDialogResult(DialogResult.OK);
                int[] temp = mutiTable.getSelectedRows();
                resultAddLayers=new Vector(6);
                for (int i=0;i<temp.length;i++){
                    resultAddLayers.add(notClipLayer.get(temp[i]));
                }
                MapClipAddLayersDialog.this.dispose();
            } else if (e.getSource().equals(panelButton.getButtonCancel())) {
                MapClipAddLayersDialog.this.dispose();
            }
        }
    };

    private MouseListener tableMouseClickListener=new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            isCanOK();
        }
    };

    private class MapClipLayerCaptionTableRender extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
	        DataCell dataCell = new DataCell(value);
	        dataCell.setToolTipText(((Layer) value).getCaption());
	        if (isSelected) {
		        dataCell.setBackground(table.getSelectionBackground());
		        dataCell.setForeground(table.getSelectionForeground());
	        } else {
		        dataCell.setBackground(table.getBackground());
	        }
	        return dataCell;
        }
    }
}
