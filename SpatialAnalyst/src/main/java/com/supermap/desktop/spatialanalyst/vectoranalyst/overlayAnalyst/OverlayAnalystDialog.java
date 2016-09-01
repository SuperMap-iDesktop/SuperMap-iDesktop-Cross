package com.supermap.desktop.spatialanalyst.vectoranalyst.overlayAnalyst;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.enums.LengthUnit;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.utilities.DatasetUtilities;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by xie on 2016/8/30.
 */
public class OverlayAnalystDialog extends SmDialog {
    private JPanel panelSource;
    private JPanel panelOverlayAnalyst;
    private JPanel panelTarget;

    private JList listOverlayAnalystType;// 叠加分析方法
    private JLabel labelSourceDatasource;
    private DatasourceComboBox comboboxSourceDatasource;// 源数据源
    private JLabel labelSourceDataset;
    private DatasetComboBox comboboxSourceDataset;// 源数据集
    private JLabel labelOverlayAnalystDatasource;
    private DatasourceComboBox comboboxOverlayAnalystDatasource;//叠加数据源
    private JLabel labelOverlayAnalystDataset;
    private DatasetComboBox comboboxOverlayAnalystDataset;//叠加数据集
    private JLabel labelTargetDatasource;
    private DatasourceComboBox comboboxTargetDatasource;//目标数据源
    private JLabel labelTargetDataset;
    private JTextField textFieldTargetDataset;//目标数据集
    private JButton buttonFieldsSet;//字段设置
    private JLabel labelTolerance;
    private JTextField textFieldTolerance;//容限
    private JLabel labelToleranceUnity;//容限单位
    private JCheckBox checkboxResultAnalyst;
    private JButton buttonOK;
    private JButton buttonCancel;

    private OverlayAnalystType DEFUALTTYPE = OverlayAnalystType.CLIP;
    private final String clipResultDatasetName = "ClipResult";
    private final String unionResultDatasetName = "UnionResult";
    private final String eraseResultDatasetName = "EraseResult";
    private final String intersectResultDatasetName = "IntersectResult";
    private final String identityResultDatasetName = "IdentityResult";
    private final String xORResultDatasetName = "XORResult";
    private final String updateResultDatasetName = "UpdateResult";
    private final Color WORNINGCOLOR = Color.red;
    private final Color DEFUALTCOLOR = Color.black;


    private final int ALLTYPE = 0;
    private final int REGIONTYPE = 1;

    public OverlayAnalystDialog() {
        super();
        initComponents();
        initLayout();
        initResources();
        registEvents();
        setSize(new Dimension(500, 400));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initLayout() {
        initPanelSourceLayout();
        initPanelOverlayAnalystLayout();
        initPanelTargetLayout();
        JScrollPane scrollPane = new JScrollPane();
        listOverlayAnalystType.setCellRenderer(new ListCellRenderer<Object>() {

            @Override
            public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                ((DataCell) value).setSelected(isSelected);
                return (DataCell) value;
            }
        });
        scrollPane.setViewportView(listOverlayAnalystType);
        JPanel panelBasicAnalyst = new JPanel();
        this.setLayout(new GridBagLayout());
        panelBasicAnalyst.setLayout(new GridBagLayout());
        panelBasicAnalyst.add(this.panelSource, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(2).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
        panelBasicAnalyst.add(this.panelOverlayAnalyst, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(2).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
        panelBasicAnalyst.add(this.panelTarget, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(2).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
        JPanel panelButton = new JPanel();
        panelButton.setLayout(new GridBagLayout());
        panelButton.add(this.buttonOK, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(2, 0, 10, 10));
        panelButton.add(this.buttonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(2, 0, 10, 10));
        this.add(scrollPane, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(5).setFill(GridBagConstraints.BOTH));
        this.add(panelBasicAnalyst, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(5).setFill(GridBagConstraints.BOTH));
        this.add(panelButton, new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0));
    }

    private void initPanelTargetLayout() {
        this.panelTarget.setLayout(new GridBagLayout());
        this.panelTarget.add(this.labelTargetDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        this.panelTarget.add(this.comboboxTargetDatasource, new GridBagConstraintsHelper(1, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        this.panelTarget.add(this.labelTargetDataset, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        this.panelTarget.add(this.textFieldTargetDataset, new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        this.panelTarget.add(this.buttonFieldsSet, new GridBagConstraintsHelper(3, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        this.panelTarget.add(this.labelTolerance, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        this.panelTarget.add(this.textFieldTolerance, new GridBagConstraintsHelper(1, 2, 3, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        this.panelTarget.add(this.labelToleranceUnity, new GridBagConstraintsHelper(4, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        this.panelTarget.add(this.checkboxResultAnalyst, new GridBagConstraintsHelper(0, 3, 5, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(1, 1));
    }

    private void initPanelOverlayAnalystLayout() {
        this.panelOverlayAnalyst.setLayout(new GridBagLayout());
        this.panelOverlayAnalyst.add(this.labelOverlayAnalystDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        this.panelOverlayAnalyst.add(this.comboboxOverlayAnalystDatasource, new GridBagConstraintsHelper(1, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        this.panelOverlayAnalyst.add(this.labelOverlayAnalystDataset, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        this.panelOverlayAnalyst.add(this.comboboxOverlayAnalystDataset, new GridBagConstraintsHelper(1, 1, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
    }

    private void initPanelSourceLayout() {
        this.panelSource.setLayout(new GridBagLayout());
        this.panelSource.add(this.labelSourceDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        this.panelSource.add(this.comboboxSourceDatasource, new GridBagConstraintsHelper(1, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        this.panelSource.add(this.labelSourceDataset, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        this.panelSource.add(this.comboboxSourceDataset, new GridBagConstraintsHelper(1, 1, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
    }

    private void registEvents() {
        removeEvents();
        this.listOverlayAnalystType.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = listOverlayAnalystType.getSelectedIndex();
                switch (index) {
                    case 0:
                        // 裁剪设置
                        initComboboxsInfo(ALLTYPE);
                        initTextFieldTargetDataset(clipResultDatasetName);
                        buttonFieldsSet.setEnabled(false);
                        DEFUALTTYPE = OverlayAnalystType.CLIP;
                        break;
                    case 1:
                        // 合并设置
                        initComboboxsInfo(REGIONTYPE);
                        initTextFieldTargetDataset(unionResultDatasetName);
                        buttonFieldsSet.setEnabled(true);
                        DEFUALTTYPE = OverlayAnalystType.UNION;
                        break;
                    case 2:
                        // 擦除设置
                        initComboboxsInfo(ALLTYPE);
                        initTextFieldTargetDataset(eraseResultDatasetName);
                        buttonFieldsSet.setEnabled(false);
                        DEFUALTTYPE = OverlayAnalystType.ERASE;
                        break;
                    case 3:
                        // 求交设置
                        initComboboxsInfo(ALLTYPE);
                        initTextFieldTargetDataset(intersectResultDatasetName);
                        buttonFieldsSet.setEnabled(true);
                        DEFUALTTYPE = OverlayAnalystType.INTERSECT;
                        break;
                    case 4:
                        // 同一设置
                        initComboboxsInfo(ALLTYPE);
                        initTextFieldTargetDataset(identityResultDatasetName);
                        buttonFieldsSet.setEnabled(true);
                        DEFUALTTYPE = OverlayAnalystType.IDENTITY;
                        break;
                    case 5:
                        // 对称差设置
                        initComboboxsInfo(REGIONTYPE);
                        initTextFieldTargetDataset(xORResultDatasetName);
                        buttonFieldsSet.setEnabled(true);
                        DEFUALTTYPE = OverlayAnalystType.XOR;
                        break;
                    case 6:
                        // 更新设置
                        initComboboxsInfo(REGIONTYPE);
                        initTextFieldTargetDataset(updateResultDatasetName);
                        buttonFieldsSet.setEnabled(false);
                        DEFUALTTYPE = OverlayAnalystType.UPDATE;
                        break;
                    default:
                        break;
                }
            }
        });
        this.comboboxSourceDataset.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Dataset selectedDataset = comboboxSourceDataset.getSelectedDataset();
                    if (null != selectedDataset) {
                        resetTextFieldToleranceInfo(selectedDataset);
                    }
                }
            }
        });
        this.textFieldTargetDataset.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                Datasource datasource = comboboxTargetDatasource.getSelectedDatasource();
                String text = textFieldTargetDataset.getText();
                if (null != datasource && null != datasource.getDatasets()) {
                    Datasets datasets = datasource.getDatasets();
                    if (!datasets.getAvailableDatasetName(text).equals(text)) {
                        textFieldTargetDataset.setForeground(WORNINGCOLOR);
                        buttonOK.setEnabled(false);
                    } else {
                        textFieldTargetDataset.setForeground(DEFUALTCOLOR);
                        buttonOK.setEnabled(true);
                    }
                }
            }
        });
        this.buttonFieldsSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (null != comboboxSourceDataset.getSelectedDataset() && null != comboboxOverlayAnalystDataset.getSelectedDataset()) {
                    FieldsSetDialog fieldSetDialog = new FieldsSetDialog((DatasetVector) comboboxSourceDataset.getSelectedDataset(), (DatasetVector) comboboxOverlayAnalystDataset.getSelectedDataset());
                    if (fieldSetDialog.showDialog().equals(DialogResult.APPLY)) {

                    }
                }
            }
        });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                removeEvents();
                OverlayAnalystDialog.this.dispose();
            }
        });
    }

    private void removeEvents() {

    }

    /**
     * 添加叠加分析项
     */
    private void addListItem() {
        DefaultListModel<Object> listModel = new DefaultListModel<Object>();
        listModel.addElement(new DataCell(SpatialAnalystProperties.getString("String_OverlayAnalystMethod_Clip"), new ImageIcon(OverlayAnalystDialog.class.getResource("/OverlayAnalyst/Image_OverlayAnalyst_Clip.png"))));
        listModel.addElement(new DataCell(SpatialAnalystProperties.getString("String_OverlayAnalystMethod_Union"), new ImageIcon(OverlayAnalystDialog.class.getResource("/OverlayAnalyst/Image_OverlayAnalyst_Union.png"))));
        listModel.addElement(new DataCell(SpatialAnalystProperties.getString("String_OverlayAnalystMethod_Erase"), new ImageIcon(OverlayAnalystDialog.class.getResource("/OverlayAnalyst/Image_OverlayAnalyst_Erase.png"))));
        listModel.addElement(new DataCell(SpatialAnalystProperties.getString("String_OverlayAnalystMethod_Intersect"), new ImageIcon(OverlayAnalystDialog.class.getResource("/OverlayAnalyst/Image_OverlayAnalyst_Intersect.png"))));
        listModel.addElement(new DataCell(SpatialAnalystProperties.getString("String_OverlayAnalystMethod_Identity"), new ImageIcon(OverlayAnalystDialog.class.getResource("/OverlayAnalyst/Image_OverlayAnalyst_Identity.png"))));
        listModel.addElement(new DataCell(SpatialAnalystProperties.getString("String_OverlayAnalystMethod_XOR"), new ImageIcon(OverlayAnalystDialog.class.getResource("/OverlayAnalyst/Image_OverlayAnalyst_XOR.png"))));
        listModel.addElement(new DataCell(SpatialAnalystProperties.getString("String_OverlayAnalystMethod_Update"), new ImageIcon(OverlayAnalystDialog.class.getResource("/OverlayAnalyst/Image_OverlayAnalyst_Update.png"))));
        this.listOverlayAnalystType.setModel(listModel);
        this.listOverlayAnalystType.setSelectedIndex(0);
        this.listOverlayAnalystType.setCellRenderer(new CommonListCellRenderer());
    }

    private void initResources() {
        this.setTitle(SpatialAnalystProperties.getString("String_Form_OverlayAnalyst"));
        this.labelSourceDatasource.setText(CommonProperties.getString("String_Label_Datasource"));
        this.labelSourceDataset.setText(CommonProperties.getString("String_Label_Dataset"));
        this.labelOverlayAnalystDatasource.setText(CommonProperties.getString("String_Label_Datasource"));
        this.labelOverlayAnalystDataset.setText(CommonProperties.getString("String_Label_Dataset"));
        this.labelTargetDatasource.setText(CommonProperties.getString("String_Label_Datasource"));
        this.labelTargetDataset.setText(CommonProperties.getString("String_Label_Dataset"));
        this.buttonFieldsSet.setText(SpatialAnalystProperties.getString("String_Button_FieldsSetting"));
        this.labelTolerance.setText(SpatialAnalystProperties.getString("String_Label_Tolerance"));
        this.checkboxResultAnalyst.setText(SpatialAnalystProperties.getString("String_CheckBox_ResultComparison"));
        this.panelSource.setBorder(new TitledBorder(ControlsProperties.getString("String_GroupBox_SourceDataset")));
        this.panelOverlayAnalyst.setBorder(new TitledBorder(SpatialAnalystProperties.getString("String_GroupBox_OverlayDataset")));
        this.panelTarget.setBorder(new TitledBorder(SpatialAnalystProperties.getString("String_ResultSet")));
    }

    private void initComponents() {
        this.listOverlayAnalystType = new JList();
        this.labelSourceDatasource = new JLabel();
        this.comboboxSourceDatasource = new DatasourceComboBox();
        this.labelSourceDataset = new JLabel();
        this.comboboxSourceDataset = new DatasetComboBox(new Dataset[0]);
        this.labelOverlayAnalystDatasource = new JLabel();
        this.comboboxOverlayAnalystDatasource = new DatasourceComboBox();
        this.labelOverlayAnalystDataset = new JLabel();
        this.comboboxOverlayAnalystDataset = new DatasetComboBox(new Dataset[0]);
        this.comboboxTargetDatasource = new DatasourceComboBox();
        this.labelTargetDatasource = new JLabel();
        this.labelTargetDataset = new JLabel();
        this.textFieldTargetDataset = new JTextField();
        this.buttonFieldsSet = new JButton();
        this.labelTolerance = new JLabel();
        this.textFieldTolerance = new JTextField();
        this.labelToleranceUnity = new JLabel();
        this.checkboxResultAnalyst = new JCheckBox();
        this.buttonOK = ComponentFactory.createButtonOK();
        this.buttonCancel = ComponentFactory.createButtonCancel();
        this.panelSource = new JPanel();
        this.panelOverlayAnalyst = new JPanel();
        this.panelTarget = new JPanel();
        this.buttonFieldsSet.setEnabled(false);
        addListItem();
        initComboboxsInfo(ALLTYPE);
        initTextFieldTargetDataset(clipResultDatasetName);
    }

    private void initComboboxsInfo(int flag) {
        Datasource analystDatasource = null;
        if (null != Application.getActiveApplication().getActiveDatasources() && Application.getActiveApplication().getActiveDatasources().length > 0) {
            analystDatasource = Application.getActiveApplication().getActiveDatasources()[0];
            comboboxSourceDatasource.setSelectedDatasource(analystDatasource);
            comboboxOverlayAnalystDatasource.setSelectedDatasource(analystDatasource);
            comboboxTargetDatasource.setSelectedDatasource(analystDatasource);
        } else {
            analystDatasource = Application.getActiveApplication().getWorkspace().getDatasources().get(0);
        }
        resetItemToComboBox(comboboxSourceDataset, analystDatasource, flag);
        resetItemToComboBox(comboboxOverlayAnalystDataset, analystDatasource, REGIONTYPE);
        if (null != Application.getActiveApplication().getActiveDatasets() && Application.getActiveApplication().getActiveDatasets().length > 0) {
            Dataset sourceDataset = Application.getActiveApplication().getActiveDatasets()[0];
            comboboxSourceDataset.setSelectedDataset(sourceDataset);
            if (sourceDataset.getType().equals(DatasetType.REGION)) {
                comboboxOverlayAnalystDataset.removeDataset(sourceDataset);
            }
        } else {
            if (comboboxSourceDataset.getSelectedDataset().getType().equals(DatasetType.REGION)) {
                comboboxOverlayAnalystDataset.removeDataset(comboboxSourceDataset.getSelectedDataset());
            }
        }
    }

    private void initTextFieldTargetDataset(String targetDatasetName) {
        Datasource targetDatasource = comboboxTargetDatasource.getSelectedDatasource();
        textFieldTargetDataset.setText(targetDatasource.getDatasets().getAvailableDatasetName(targetDatasetName));
        if (null != comboboxSourceDataset.getSelectedDataset()) {
            resetTextFieldToleranceInfo(comboboxSourceDataset.getSelectedDataset());
        }
    }

    private void resetTextFieldToleranceInfo(Dataset dataset) {
        textFieldTolerance.setText(String.valueOf(DatasetUtilities.getDefaultTolerance((DatasetVector) dataset).getNodeSnap()));
        this.labelToleranceUnity.setText(LengthUnit.convertForm(dataset.getPrjCoordSys().getCoordUnit()).toString());
    }

    /**
     * 为DatasetCombobox插入选项
     *
     * @param sourceDataset
     * @param datasource
     * @param flag
     * @return
     */
    private boolean resetItemToComboBox(DatasetComboBox sourceDataset, Datasource datasource, int flag) {
        sourceDataset.removeAllItems();
        int count = 0;
        if (null != datasource) {
            Datasets datasets = datasource.getDatasets();
            if (flag == ALLTYPE) {
                for (int i = 0; i < datasets.getCount(); i++) {
                    if (datasets.get(i).getType() == DatasetType.LINE || datasets.get(i).getType() == DatasetType.POINT || datasets.get(i).getType() == DatasetType.REGION) {
                        DataCell cell = new DataCell();
                        cell.initDatasetType(datasets.get(i));
                        sourceDataset.addItem(cell);
                        count++;
                    }
                }
            } else {
                for (int i = 0; i < datasets.getCount(); i++) {
                    if (datasets.get(i).getType() == DatasetType.REGION) {
                        DataCell cell = new DataCell();
                        cell.initDatasetType(datasets.get(i));
                        sourceDataset.addItem(cell);
                        count++;
                    }
                }
            }
        }
        return 0 < count;
    }
}
