package com.supermap.desktop.geometryoperation.control;

import com.supermap.data.GeoLine;
import com.supermap.data.GeoStyle;
import com.supermap.data.Recordset;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.ui.controls.ChooseTable.SmChooseTable;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.utilities.MapUtilities;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * @author huqing  fix by  lixiaoyao
 */
public class JDialogLineInterrruptSelect extends SmDialog {
    private JButton buttonOK = ComponentFactory.createButtonOK();
    private JButton buttonCancel = ComponentFactory.createButtonCancel();
    private JPanel panelWindow = new JPanel();
    private JScrollPane tableScrollpane = new JScrollPane();
    private JPanel panelButtons = null;
    private Recordset recordset = null;
    private Object data[][] = null;
    private ArrayList<Integer> idsArrayList = null;
    private final static int COLUMN_COUT = 4;
    private EditEnvironment editEnvironment;
    private SmChooseTable smChooseTable = null;
    private final String geometryTip1 = MapEditorProperties.getString("Srting_GeometryTip1");
    private final String geometryTip2 = MapEditorProperties.getString("Srting_GeometryTip2");
    private final String connectStr = "@";
    private static final int COLUMN_INDEX_CHECK = 0;
    private static final int COLUMN_INDEX_OBJECT = 1;
    private static final int COLUMN_INDEX_SMID = 2;
    private static final int COLUMN_INDEX_LAYER_OF_OBJECT = 3;
    private static final String TAG_LineInterruptByPoint = "TAG_LineInterrupt";
    private final int MAX_COLUMN_WIDTH=100;
    private GeoStyle styleRed = new GeoStyle();


    private Object[] tableHeadTitles = {MapEditorProperties.getString("String_Interrupt"),
            MapEditorProperties.getString("String_Object"),
            "SMID",
            MapEditorProperties.getString("String_LayersOfObject")};
    private ActionListener actionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == JDialogLineInterrruptSelect.this.buttonOK) {
                editEnvironment.getActiveEditableLayer().getSelection().clear();
                MapUtilities.clearTrackingObjects(editEnvironment.getMap(), TAG_LineInterruptByPoint);
                JDialogLineInterrruptSelect.this.setDialogResult(DialogResult.OK);
                JDialogLineInterrruptSelect.this.dispose();
            } else if (e.getSource() == JDialogLineInterrruptSelect.this.buttonCancel) {
                editEnvironment.getActiveEditableLayer().getSelection().clear();
                MapUtilities.clearTrackingObjects(editEnvironment.getMap(), TAG_LineInterruptByPoint);
                JDialogLineInterrruptSelect.this.dispose();
            }
        }
    };

    //  选中table中对象时，地图窗口的对应对象高亮
    private MouseListener mouseListener=new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            highLightLine();
        }

    };

    private MouseMotionListener mouseMotionListener=new MouseMotionAdapter() {
        @Override
        public void mouseDragged(MouseEvent e) {
            highLightLine();
        }
    };

    private TableModelListener tableModelListener=new TableModelListener() {
        @Override
        public void tableChanged(TableModelEvent e) {
            buttonStateSet();
        }
    };

    private void highLightLine(){
        this.recordset.moveFirst();
        int[] selectedRows = this.smChooseTable.getSelectedRows();
        this.editEnvironment.getActiveEditableLayer().getSelection().clear();
        MapUtilities.clearTrackingObjects(this.editEnvironment.getMap(), TAG_LineInterruptByPoint);
        for (int i = 0; i < JDialogLineInterrruptSelect.this.idsArrayList.size(); i++) {
            for (int j = 0; j < selectedRows.length; j++) {
                if (selectedRows[j] == i) {
                    this.editEnvironment.getActiveEditableLayer().getSelection().add(this.recordset.getID());
                    GeoLine geoLine=(GeoLine)this.recordset.getGeometry();
                    geoLine.setStyle(this.styleRed);
                    this.editEnvironment.getMap().getTrackingLayer().add(geoLine, TAG_LineInterruptByPoint);
                }
            }
            this.recordset.moveNext();
        }
        this.editEnvironment.getMap().refreshTrackingLayer();
        this.editEnvironment.getMap().refresh();
    }

    private void buttonStateSet(){
        if (this.getSelectedLineIds().size()>0 &&this.buttonCancel.isSelected()){
            this.buttonOK.setEnabled(true);
            this.buttonOK.setSelected(true);
            this.buttonCancel.setSelected(false);
        }else if (this.getSelectedLineIds().size()==0 && this.buttonOK.isEnabled()){
            this.buttonOK.setEnabled(false);
            this.buttonOK.setSelected(false);
            this.buttonCancel.setSelected(true);
        }
    }

    public JDialogLineInterrruptSelect(EditEnvironment environment, Recordset recordset, ArrayList<Integer> idsArrayListList, JFrame owner, boolean model) {
        super(owner, model);
        this.recordset = recordset;//查询的结果
        this.idsArrayList = idsArrayListList;
        this.editEnvironment = environment;
        InitComponents();
        unRegisterEvents();
        registerEvents();
        this.componentList.add(this.buttonOK);
        this.componentList.add(this.buttonCancel);
        this.setFocusTraversalPolicy(policy);
    }

    private void InitComponents() {
        this.buttonOK.setSelected(true);
        this.setTitle(MapEditorProperties.getString("String_DialogSelectedTitle"));
        this.setMinimumSize(new Dimension(440, 300));
        setLocationRelativeTo(null);

        this.data = this.getData();
        this.smChooseTable = new SmChooseTable(this.data, this.tableHeadTitles);
        this.tableScrollpane.getViewport().setView(this.smChooseTable);
        this.setLayout(new GridBagLayout());
        this.panelWindow.setLayout(new BorderLayout());
        this.panelWindow.add(this.tableScrollpane, BorderLayout.CENTER);

        this.panelButtons = new JPanel();
        this.panelButtons.setLayout(new GridBagLayout());
        panelButtons.add(this.buttonOK, new GridBagConstraintsHelper(0, 0, 1, 1).setInsets(2, 0, 2, 10));
        panelButtons.add(this.buttonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setInsets(2, 0, 2, 10));
        this.getContentPane().add(this.tableScrollpane, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setInsets(6, 10, 0, 10));
        this.getContentPane().add(this.panelButtons, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.EAST).setInsets(4, 20, 4, 0));
        this.smChooseTable.getColumnModel().getColumn(COLUMN_INDEX_OBJECT).setMaxWidth(MAX_COLUMN_WIDTH);
        this.smChooseTable.getColumnModel().getColumn(COLUMN_INDEX_SMID).setMaxWidth(MAX_COLUMN_WIDTH);
        this.smChooseTable.getColumnModel().getColumn(COLUMN_INDEX_OBJECT).setMinWidth(MAX_COLUMN_WIDTH);
        this.smChooseTable.getColumnModel().getColumn(COLUMN_INDEX_SMID).setMinWidth(MAX_COLUMN_WIDTH);
        this.styleRed.setLineWidth(0.6);
        this.styleRed.setLineColor(Color.RED);
    }

    private void registerEvents() {
        this.buttonOK.addActionListener(actionListener);
        this.buttonCancel.addActionListener(actionListener);
        this.smChooseTable.addMouseListener(this.mouseListener);
        this.smChooseTable.addMouseMotionListener(this.mouseMotionListener);
        this.smChooseTable.getModel().addTableModelListener(this.tableModelListener);
    }

    private void unRegisterEvents() {
        this.buttonOK.removeActionListener(actionListener);
        this.buttonCancel.removeActionListener(actionListener);
        this.smChooseTable.removeMouseListener(this.mouseListener);
        this.smChooseTable.removeMouseMotionListener(this.mouseMotionListener);
        this.smChooseTable.getModel().removeTableModelListener(this.tableModelListener);
    }

    //获取当前对话框填充到table中的数据
    private Object[][] getData() {
        Object data[][] = new Object[this.idsArrayList.size()][JDialogLineInterrruptSelect.COLUMN_COUT];
        this.recordset.moveFirst();
        for (int i = 0; i < this.idsArrayList.size(); i++) {
            recordset.seekID(idsArrayList.get(i));
            data[i][COLUMN_INDEX_CHECK] = false;
            data[i][COLUMN_INDEX_OBJECT] = this.geometryTip1 + (i + 1) + this.geometryTip2;
            data[i][COLUMN_INDEX_SMID] = this.recordset.getID() + "";
            data[i][COLUMN_INDEX_LAYER_OF_OBJECT] = this.recordset.getDataset().getName() + this.connectStr +
                    this.recordset.getDataset().getDatasource().getAlias();
        }
        return data;
    }

    //选择最终要被打断的线
    public ArrayList<Integer> getSelectedLineIds() {
        ArrayList<Integer> selectedResult = new ArrayList<>();
        this.recordset.moveFirst();
        for (int i = 0; i < JDialogLineInterrruptSelect.this.idsArrayList.size(); i++) {
            if (this.smChooseTable.getValueAt(i, COLUMN_INDEX_CHECK).equals(true)) {
                selectedResult.add(recordset.getID());
            }
            this.recordset.moveNext();
        }
        return selectedResult;
    }
}
