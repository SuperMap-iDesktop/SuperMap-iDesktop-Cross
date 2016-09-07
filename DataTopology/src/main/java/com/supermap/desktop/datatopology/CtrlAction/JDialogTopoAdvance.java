package com.supermap.desktop.datatopology.CtrlAction;

import com.supermap.data.*;
import com.supermap.data.topology.TopologyProcessingOptions;
import com.supermap.desktop.Application;
import com.supermap.desktop.datatopology.DataTopologyProperties;
import com.supermap.desktop.enums.LengthUnit;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JDialogTopoAdvance extends SmDialog {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JTextField textFieldFilterExpression;
    private SmTextFieldLegit textFieldOvershootsTolerance = new SmTextFieldLegit();
    private SmTextFieldLegit textFieldUndershootsTolerance = new SmTextFieldLegit();
    private SmTextFieldLegit textFieldVertexTorance = new SmTextFieldLegit();
    private JLabel labelOvershootsToleranceUnit = new JLabel();
    private JLabel labelUndershootsToleranceUnit = new JLabel();
    private JLabel labelVertexToleranceUnit = new JLabel();
    private JLabel labelOvershootsTolerance = new JLabel("String_Label_OvershootsTolerance");
    private JLabel labelUndershootsTolerance = new JLabel("String_Label_UndershootsTolerance");
    private JLabel labelVertexTolerance = new JLabel("String_Label_VertexTolerance");
    private JLabel labelFilterExpression = new JLabel("String_FilterExpression");
    private JLabel labelNotCutting = new JLabel("String_NotCutting");
    private JPanel panelLinesIntersected = new JPanel();
    private JPanel panelToleranceSetting = new JPanel();
    private SmButton buttonMore = new SmButton("...");
    private SmButton buttonSure = new SmButton("String_Button_OK");
    private SmButton buttonQuite = new SmButton("String_Button_Cancel");
    private transient TopologyProcessingOptions topologyProcessingOptions = new TopologyProcessingOptions();
    private DatasetComboBox comboBoxNotCutting;
    private SQLExpressionDialog sqlExpressionDialog;

    private transient DatasetVector targetDataset;
    private transient Datasource datasource;
    private DialogResult dialogResult;

    private CommonButtonListener listener = new CommonButtonListener();

    /**
     * @wbp.parser.constructor
     */
    public JDialogTopoAdvance(JDialog owner, boolean model, Datasource datasource) {
        super(owner, model);
        this.datasource = datasource;
        setLocationRelativeTo(owner);
        initComponents();
        initResources();
        registActionListener();
        initTraversalPolicy();
    }

    public JDialogTopoAdvance(JDialog owner, boolean model, TopologyProcessingOptions topologyProcessingOptions, DatasetVector targetDataset,
                              Datasource datasource) {
        super(owner, model);
        setLocationRelativeTo(owner);
        this.datasource = datasource;
        this.topologyProcessingOptions = topologyProcessingOptions;
        this.targetDataset = targetDataset;
        initComponents();
        initResources();
        registActionListener();
        initTraversalPolicy();
    }

    private void initTraversalPolicy() {
        if (this.componentList.size() > 0) {
            componentList.clear();
        }
        this.componentList.add(this.buttonSure);
        this.componentList.add(this.buttonQuite);
        this.componentList.add(this.buttonMore);
        this.setFocusTraversalPolicy(policy);
    }

    private void registActionListener() {
        unregistActionListener();
        this.buttonSure.addActionListener(this.listener);
        this.buttonQuite.addActionListener(this.listener);
        this.buttonMore.addActionListener(this.listener);
    }

    private void unregistActionListener() {
        this.buttonSure.removeActionListener(this.listener);
        this.buttonQuite.removeActionListener(this.listener);
        this.buttonMore.removeActionListener(this.listener);
    }

    private void initResources() {
        setTitle(DataTopologyProperties.getString("String_Form_AdvanceSettings"));
        this.labelOvershootsTolerance.setText(DataTopologyProperties.getString("String_Label_OvershootsTolerance"));
        this.labelUndershootsTolerance.setText(DataTopologyProperties.getString("String_Label_UndershootsTolerance"));
        this.labelVertexTolerance.setText(DataTopologyProperties.getString("String_Label_VertexTolerance"));
        this.labelFilterExpression.setText(DataTopologyProperties.getString("String_FilterExpression"));
        this.labelNotCutting.setText(DataTopologyProperties.getString("String_NotCutting"));
        this.buttonSure.setText(CommonProperties.getString("String_Button_OK"));
        this.buttonQuite.setText(CommonProperties.getString("String_Button_Cancel"));
        this.panelLinesIntersected.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), DataTopologyProperties
                .getString("String_LinesIntersected"), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        this.panelToleranceSetting.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), DataTopologyProperties
                .getString("String_GroupBox_ToleranceSetting"), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
    }

    private void initComponents() {
        ISmTextFieldLegit smTextFieldLegit = new ISmTextFieldLegit() {
            @Override
            public boolean isTextFieldValueLegit(String textFieldValue) {
                if (StringUtilities.isNullOrEmpty(textFieldValue)) {
                    return false;
                }
                try {
                    Double integer = Double.valueOf(textFieldValue);
                    if (integer < 0) {
                        return false;
                    }
                } catch (Exception e) {
                    return false;
                }
                return true;
            }

            @Override
            public String getLegitValue(String currentValue, String backUpValue) {
                return StringUtilities.isNullOrEmpty(currentValue) ? "0" : backUpValue;
            }
        };
        textFieldOvershootsTolerance.setSmTextFieldLegit(smTextFieldLegit);
        textFieldUndershootsTolerance.setSmTextFieldLegit(smTextFieldLegit);
        textFieldVertexTorance.setSmTextFieldLegit(smTextFieldLegit);
        initComboBoxItem();
        setSize(320, 305);
        setMinimumSize(new Dimension(320, 305));
        initContentPane();
        initPanelToleranceSetting();
        initPanelLinesIntersected();
        getRootPane().setDefaultButton(buttonSure);
    }

    private void initPanelLinesIntersected() {
        // @formatter:off
        this.textFieldFilterExpression = new JTextField();
        this.textFieldFilterExpression.setColumns(10);
        this.panelLinesIntersected.setLayout(new GridBagLayout());
        this.panelLinesIntersected.add(this.labelFilterExpression, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setInsets(10, 10, 5, 5));
        this.panelLinesIntersected.add(this.textFieldFilterExpression, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(60, 0).setInsets(10, 0, 5, 5).setIpad(20, 0));
        this.panelLinesIntersected.add(this.buttonMore, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(5, 0).setInsets(10, 0, 5, 10));

        this.panelLinesIntersected.add(this.labelNotCutting, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setInsets(0, 10, 10, 5));
        this.panelLinesIntersected.add(this.comboBoxNotCutting, new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(0, 0, 10, 10));
        // @formatter:on
    }

    private void initPanelToleranceSetting() {
        // @formatter:off
        this.textFieldOvershootsTolerance.setColumns(10);
        this.textFieldUndershootsTolerance.setColumns(10);
        this.textFieldVertexTorance.setColumns(10);
        this.panelToleranceSetting.setLayout(new GridBagLayout());
        this.panelToleranceSetting.add(this.labelOvershootsTolerance, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setInsets(10, 10, 5, 5));
        this.panelToleranceSetting.add(this.textFieldOvershootsTolerance, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(60, 0).setInsets(10, 0, 5, 10));
        this.panelToleranceSetting.add(this.labelOvershootsToleranceUnit, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setInsets(10, 0, 5, 10));

        this.panelToleranceSetting.add(this.labelUndershootsTolerance, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setInsets(0, 10, 5, 5));
        this.panelToleranceSetting.add(this.textFieldUndershootsTolerance, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(60, 0).setInsets(0, 0, 5, 10));
        this.panelToleranceSetting.add(this.labelUndershootsToleranceUnit, new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setInsets(10, 0, 5, 10));

        this.panelToleranceSetting.add(this.labelVertexTolerance, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setInsets(0, 10, 10, 5));
        this.panelToleranceSetting.add(this.textFieldVertexTorance, new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(60, 0).setInsets(0, 0, 10, 10));
        this.panelToleranceSetting.add(this.labelVertexToleranceUnit, new GridBagConstraintsHelper(2, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setInsets(10, 0, 5, 10));

        // @formatter:on
    }

    private void initContentPane() {
        // @formatter:off
        getContentPane().setLayout(new GridBagLayout());
        getContentPane().add(this.panelLinesIntersected, new GridBagConstraintsHelper(0, 0, 4, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(3, 0).setInsets(10, 10, 5, 10));
        getContentPane().add(this.panelToleranceSetting, new GridBagConstraintsHelper(0, 1, 4, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(3, 0).setInsets(0, 10, 5, 10));
        getContentPane().add(new JPanel(), new GridBagConstraintsHelper(0, 2, 4, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1));

        getContentPane().add(this.buttonSure, new GridBagConstraintsHelper(2, 3, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(1, 0).setInsets(0, 0, 10, 10));
        getContentPane().add(this.buttonQuite, new GridBagConstraintsHelper(3, 3, 1, 1).setAnchor(GridBagConstraints.EAST).setInsets(0, 0, 10, 10));
        // @formatter:on
    }

    private void initComboBoxItem() {
        try {
            if (null != targetDataset) {
                this.comboBoxNotCutting = new DatasetComboBox(new Dataset[0]);
                DataCell item = new DataCell();
                item.setPreferredSize(new Dimension(20, 18));
                comboBoxNotCutting.addItem(item);
                for (int i = 0; i < targetDataset.getDatasource().getDatasets().getCount(); i++) {
                    Dataset tempDataset = targetDataset.getDatasource().getDatasets().get(i);
                    if (tempDataset.getType() == DatasetType.POINT) {
                        DataCell cell = new DataCell();
                        cell.initDatasetType(tempDataset);
                        this.comboBoxNotCutting.addItem(cell);
                    }
                }
            }
            resetTolerance();
        } catch (Exception e) {
            Application.getActiveApplication().getOutput().output(e);
        }
    }

    private void resetTolerance() {
        if (targetDataset != null) {
            Tolerance tolerance = targetDataset.getTolerance();
            double nodeSnap;
            if (Math.abs(tolerance.getNodeSnap()) < 1E-10) {
                nodeSnap = DatasetUtilities.getDefaultTolerance(targetDataset).getNodeSnap();
            } else {
                nodeSnap = tolerance.getNodeSnap();
            }
            textFieldVertexTorance.setText(String.valueOf(Math.abs(nodeSnap)));
            textFieldUndershootsTolerance.setText(String.valueOf(Math.abs(tolerance.getExtend()) < 1E-10 ? nodeSnap * 100 : tolerance.getExtend()));
            textFieldOvershootsTolerance.setText(String.valueOf(Math.abs(tolerance.getDangle()) < 1E-10 ? nodeSnap * 100 : tolerance.getDangle()));
            labelOvershootsToleranceUnit.setText(LengthUnit.convertForm(targetDataset.getPrjCoordSys().getCoordUnit()).toString());
            labelUndershootsToleranceUnit.setText(LengthUnit.convertForm(targetDataset.getPrjCoordSys().getCoordUnit()).toString());
            labelVertexToleranceUnit.setText(LengthUnit.convertForm(targetDataset.getPrjCoordSys().getCoordUnit()).toString());
        }
    }

    public DatasetVector getTargetDataset() {
        return targetDataset;
    }

    class CommonButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JComponent c = (JComponent) e.getSource();
            if (buttonSure == c) {
                buttonSureClicked();
            }
            if (buttonQuite == c) {
                quiteButtonClicked();
            }
            if (buttonMore == c) {
                addItemToTextFieldFilterExpression();
            }
        }

        private void addItemToTextFieldFilterExpression() {
            sqlExpressionDialog = new SQLExpressionDialog();
            Dataset[] datasets = new Dataset[1];
            datasets[0] = targetDataset;
            DialogResult dialogResult = sqlExpressionDialog.showDialog(textFieldFilterExpression.getText(), datasets);
            if (dialogResult == DialogResult.OK) {
                String filter = sqlExpressionDialog.getQueryParameter().getAttributeFilter();
                if (filter != null) {
                    textFieldFilterExpression.setText(filter);
                }
            }
        }
    }

    private void setTopologyInfo() {
        try {
            String arcFilterString = this.textFieldFilterExpression.getText();
            if (!StringUtilities.isNullOrEmpty(arcFilterString)) {
                this.topologyProcessingOptions.setArcFilterString(arcFilterString);
            }
            if (null != this.comboBoxNotCutting.getSelectedDataset()) {
                DatasetVector dataset = ((DatasetVector) this.comboBoxNotCutting.getSelectedDataset());
                Recordset recordset = dataset.getRecordset(false, CursorType.STATIC);
                this.topologyProcessingOptions.setVertexFilterRecordset(recordset);
                recordset.dispose();
            }

            double overshootsTolerance = Double.parseDouble(this.textFieldOvershootsTolerance.getText());
            this.topologyProcessingOptions.setOvershootsTolerance(overshootsTolerance);
            double undershootsTolerance = Double.parseDouble(this.textFieldUndershootsTolerance.getText());
            this.topologyProcessingOptions.setUndershootsTolerance(undershootsTolerance);
            double vertexTorance = Double.parseDouble(this.textFieldVertexTorance.getText());
            this.topologyProcessingOptions.setVertexTolerance(vertexTorance);
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
    }

    @Override
    public DialogResult getDialogResult() {
        return this.dialogResult;
    }

    @Override
    public void setDialogResult(DialogResult dialogResult) {
        this.dialogResult = dialogResult;
    }

    @Override
    public void setVisible(boolean b) {
        registActionListener();
        super.setVisible(b);
    }

    private void quiteButtonClicked() {
        setDialogResult(DialogResult.CANCEL);
        unregistActionListener();
        dispose();
    }

    private void buttonSureClicked() {
        setDialogResult(DialogResult.OK);
        setTopologyInfo();
        unregistActionListener();
        dispose();
    }
}
