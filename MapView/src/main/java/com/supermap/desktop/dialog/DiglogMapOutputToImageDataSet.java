package com.supermap.desktop.dialog;

import com.supermap.data.DatasetImage;
import com.supermap.data.Datasource;
import com.supermap.data.EncodeType;
import com.supermap.data.Rectangle2D;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.mapview.map.propertycontrols.PanelGroupBoxViewBounds;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.ProviderLabel.WarningOrHelpProvider;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TextFields.WaringTextField;
import com.supermap.desktop.ui.controls.borderPanel.PanelButton;
import com.supermap.desktop.utilities.CursorUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.mapping.Map;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;

/**
 * Created by lixiaoyao on 2017/3/31.
 */
public class DiglogMapOutputToImageDataSet extends SmDialog {

    private JPanel panelResultSetting;
    private JPanel mainPanel;
    private PanelGroupBoxViewBounds panelOutPutRangeSetting;
    private PanelButton panelButton;
    private JLabel labelDataSource;
    private JLabel labelDataSet;
    private JLabel labelResolution;
    private JLabel labelRowCount;
    private JLabel labelColumnCount;
    private JLabel labelCodeType;
    private WarningOrHelpProvider warningProviderDataSet;
    private WarningOrHelpProvider warningProviderResolution;
    private DatasourceComboBox datasourceComboBox;
    private JTextField textFieldDataset;
    private JTextField textFieldResolution;
    private JTextField textFieldRowCount;
    private JTextField textFieldColumnCount;
    private JComboBox comboBoxCodeType;
    private WaringTextField outRangeWaringTextFieldLeft;
    private WaringTextField outRangeWaringTextFieldTop;
    private WaringTextField outRangeWaringTextFieldRight;
    private WaringTextField outRangeWaringTextFieldBottom;

    private static final int minSize = 23;
    private Map map;
    private boolean isValidRangeBounds = true;
    private Rectangle2D outputRangeBounds;
    private int initDialogWidth = 640;
//    private int initDialogHeight=318;
    private static final DecimalFormat COMMA_FORMAT = new DecimalFormat("#,###");

    public DiglogMapOutputToImageDataSet(JFrame owner, boolean model) {
        super(owner, model);
        IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
        this.map = formMap.getMapControl().getMap();
        initComponents();
        initLayout();
        initResources();
        removeEvents();
        registerEvents();
        initComponentValueAndState();
        this.componentList.add(this.panelButton.getButtonOk());
        this.componentList.add(this.panelButton.getButtonCancel());
        this.setFocusTraversalPolicy(policy);
    }

    private void initComponents() {
        this.panelResultSetting = new JPanel();
        this.mainPanel=new JPanel();
        this.labelDataSource = new JLabel();
        this.labelDataSet = new JLabel();
        this.labelResolution = new JLabel();
        this.labelRowCount = new JLabel();
        this.labelColumnCount = new JLabel();
        this.labelCodeType = new JLabel();
        this.datasourceComboBox = new DatasourceComboBox();
        this.textFieldDataset = new JTextField();
        this.textFieldResolution = new JTextField();
        this.textFieldRowCount = new JTextField();
        this.textFieldColumnCount = new JTextField();
        this.comboBoxCodeType = new JComboBox();
        this.warningProviderDataSet = new WarningOrHelpProvider(MapViewProperties.getString("String_OutputImageDataset_DatasetNameIsInvalid"), true);
        this.warningProviderResolution = new WarningOrHelpProvider(MapViewProperties.getString("String_OutputImageDataset_ResoltionIsNotEmpty"), true);

	    this.panelOutPutRangeSetting = new PanelGroupBoxViewBounds(this, MapViewProperties.getString("String_OutputImageDataset_OutPutRange"), map);
	    this.outRangeWaringTextFieldLeft = this.panelOutPutRangeSetting.getTextFieldCurrentViewLeft();
        this.outRangeWaringTextFieldTop = this.panelOutPutRangeSetting.getTextFieldCurrentViewTop();
        this.outRangeWaringTextFieldRight = this.panelOutPutRangeSetting.getTextFieldCurrentViewRight();
        this.outRangeWaringTextFieldBottom = this.panelOutPutRangeSetting.getTextFieldCurrentViewBottom();

        this.panelButton = new PanelButton();
    }

    private void initLayout() {
//        if (SystemPropertyUtilities.isLinux()){
//            this.initDialogHeight=290;
//        }
        Dimension dimension = new Dimension(this.initDialogWidth, 318);
        setTitle(MapViewProperties.getString("String_OutputImageDataset_Title"));
        setSize(dimension);
        setMinimumSize(dimension);
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(this.panelButton.getButtonOk());

        GroupLayout groupLayout = new GroupLayout(this.mainPanel);
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);

        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                        .addComponent(this.panelResultSetting, 290, 290, Short.MAX_VALUE)
                        .addGap(10, 10, 10)
                        .addComponent(this.panelOutPutRangeSetting, 290, 290, Short.MAX_VALUE))
//                .addComponent(this.panelButton)
        );
        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(this.panelResultSetting, 215, 215, 215)
                        .addComponent(this.panelOutPutRangeSetting, 215, 215, 215))
//                .addContainerGap(0,Short.MAX_VALUE)
//                .addComponent(this.panelButton, 40, 40, 40)
        );
        initPanelResultSettingLayout();
        this.mainPanel.setLayout(groupLayout);
        this.setLayout(new BorderLayout());
        this.add(this.mainPanel, BorderLayout.CENTER);
        this.add(this.panelButton, BorderLayout.SOUTH);
    }

    private void initPanelResultSettingLayout() {
        this.panelResultSetting.setBorder(BorderFactory.createTitledBorder(MapViewProperties.getString("String_OutputImageDataset_ResultSetting")));
        GroupLayout groupLayout = new GroupLayout(this.panelResultSetting);
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);

        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(this.labelDataSource)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(this.labelDataSet)
                                .addComponent(this.warningProviderDataSet))
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(this.labelResolution)
                                .addComponent(this.warningProviderResolution))
                        .addComponent(this.labelRowCount)
                        .addComponent(this.labelColumnCount)
                        .addComponent(this.labelCodeType))
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(this.datasourceComboBox)
                        .addComponent(this.textFieldDataset)
                        .addComponent(this.textFieldResolution)
                        .addComponent(this.textFieldRowCount)
                        .addComponent(this.textFieldColumnCount)
                        .addComponent(this.comboBoxCodeType)
                )
        );
        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(this.labelDataSource, minSize, minSize, minSize)
                        .addComponent(this.datasourceComboBox, minSize, minSize, minSize))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(this.labelDataSet, minSize, minSize, minSize)
                        .addComponent(this.warningProviderDataSet, minSize, minSize, minSize)
                        .addComponent(this.textFieldDataset, minSize, minSize, minSize))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(this.labelResolution, minSize, minSize, minSize)
                        .addComponent(this.warningProviderResolution, minSize, minSize, minSize)
                        .addComponent(this.textFieldResolution, minSize, minSize, minSize))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(this.labelRowCount, minSize, minSize, minSize)
                        .addComponent(this.textFieldRowCount, minSize, minSize, minSize))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(this.labelColumnCount, minSize, minSize, minSize)
                        .addComponent(this.textFieldColumnCount, minSize, minSize, minSize))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(this.labelCodeType, minSize, minSize, minSize)
                        .addComponent(this.comboBoxCodeType, minSize, minSize, minSize))
        );
        this.panelResultSetting.setLayout(groupLayout);
    }

    private void initResources() {
        this.labelDataSource.setText(MapViewProperties.getString("String_OutputImageDataset_Datasource"));
        this.labelDataSet.setText(MapViewProperties.getString("String_OutputImageDataset_Dataset"));
        this.labelResolution.setText(MapViewProperties.getString("String_OutputImageDataset_Resolution"));
        this.labelRowCount.setText(MapViewProperties.getString("String_OutputImageDataset_RowCount"));
        this.labelColumnCount.setText(MapViewProperties.getString("String_OutputImageDataset_ColumnCount"));
        this.labelCodeType.setText(MapViewProperties.getString("String_OutputImageDataset_CodeType"));
        Datasource currentDatasource = null;
        if (null != Application.getActiveApplication().getActiveDatasources() && Application.getActiveApplication().getActiveDatasources().length > 0) {
            currentDatasource = Application.getActiveApplication().getActiveDatasources()[0];
            datasourceComboBox.setSelectedDatasource(currentDatasource);
        }
    }

    private void initComponentValueAndState() {
        String initDatasetName = this.datasourceComboBox.getSelectedDatasource().getDatasets().getAvailableDatasetName(this.map.getName());
        this.textFieldResolution.setText(String.valueOf(this.map.getViewBounds().getWidth() / this.initDialogWidth));
        this.textFieldDataset.setText(initDatasetName);
        this.textFieldRowCount.setEnabled(false);
        this.textFieldColumnCount.setEnabled(false);
        this.warningProviderResolution.hideWarning();
        this.warningProviderDataSet.hideWarning();
        this.comboBoxCodeType.addItem("None");
        this.comboBoxCodeType.addItem("DCT");
        this.comboBoxCodeType.addItem("LZW");
        this.comboBoxCodeType.addItem("PNG");

        this.outputRangeBounds = this.map.getBounds();
        if (this.outputRangeBounds != null) {
            this.textFieldRowCount.setText(COMMA_FORMAT.format((int) (this.outputRangeBounds.getHeight() / Double.valueOf(this.textFieldResolution.getText()))));
            this.textFieldColumnCount.setText(COMMA_FORMAT.format((int) (this.outputRangeBounds.getWidth() / Double.valueOf(this.textFieldResolution.getText()))));
        }
    }

    private void isCanRun() {
        if (isValidDatasetName() && this.isValidRangeBounds && isValidResolution()) {
            this.panelButton.getButtonOk().setEnabled(true);
        } else {
            this.panelButton.getButtonOk().setEnabled(false);
        }
    }

    private void run(){
        CursorUtilities.setWaitCursor(this);
        Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_OutputImageDataset_StartCcreateImageDataset")+"\""+this.textFieldDataset.getText()+"\""+"。");
        long startTime = System.currentTimeMillis();
        boolean result=true;
        try {
            DatasetImage datasetImage;
            if (this.comboBoxCodeType.getSelectedItem().equals("None")){
                datasetImage=this.map.outputMapToDatasetImage(this.datasourceComboBox.getSelectedDatasource(),this.textFieldDataset.getText(),Double.valueOf(this.textFieldResolution.getText()),this.outputRangeBounds, EncodeType.NONE);
            }else if (this.comboBoxCodeType.getSelectedItem().equals("DCT")){
                datasetImage=this.map.outputMapToDatasetImage(this.datasourceComboBox.getSelectedDatasource(),this.textFieldDataset.getText(),Double.valueOf(this.textFieldResolution.getText()),this.outputRangeBounds, EncodeType.DCT);
            }else if (this.comboBoxCodeType.getSelectedItem().equals("LZW")){
                datasetImage=this.map.outputMapToDatasetImage(this.datasourceComboBox.getSelectedDatasource(),this.textFieldDataset.getText(),Double.valueOf(this.textFieldResolution.getText()),this.outputRangeBounds, EncodeType.LZW);
            }else if (this.comboBoxCodeType.getSelectedItem().equals("PNG")){
                datasetImage=this.map.outputMapToDatasetImage(this.datasourceComboBox.getSelectedDatasource(),this.textFieldDataset.getText(),Double.valueOf(this.textFieldResolution.getText()),this.outputRangeBounds, EncodeType.PNG);
            }
        }catch (Exception e){
            result=false;
        }
        long endTime= System.currentTimeMillis();
        String time = String.valueOf((endTime - startTime) / 1000.0);
        Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_OutputImageDataset_CcreateImageDataset") + "\"" + this.textFieldDataset.getText() + "\"" + MapViewProperties.getString("String_OutputImageDataset_CcreateImageDatasetEnd"));
        if (result) {
            Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_OutputImageDataset_CcreateImageDataset") + "\"" + this.textFieldDataset.getText() + "\"" + MapViewProperties.getString("String_OutputImageDataset_CcreateImageDatasetSucessed") + MapViewProperties.getString("String_OutputImageDataset_CcreateImageDatasetTime") + time + " " + MapViewProperties.getString("MapCache_ShowTime"));
        }else {
            Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_OutputImageDataset_CcreateImageDataset") + "\"" + this.textFieldDataset.getText() + "\"" + MapViewProperties.getString("String_OutputImageDataset_CcreateImageDatasetFailed") + MapViewProperties.getString("String_OutputImageDataset_CcreateImageDatasetTime") + time + " " + MapViewProperties.getString("MapCache_ShowTime"));
        }
        cancelAndCloseDailog();
        CursorUtilities.setDefaultCursor();
    }

    private void cancelAndCloseDailog() {
        this.dispose();
    }

    private boolean isValidDatasetName() {
        boolean result = true;
        if (this.textFieldDataset.getText().isEmpty()) {
            result = false;
            this.warningProviderDataSet.showWarning(MapViewProperties.getString("String_OutputImageDataset_DatasetNameIsNotEmpty"));
        } else {
            if (!this.datasourceComboBox.getSelectedDatasource().getDatasets().isAvailableDatasetName(this.textFieldDataset.getText())) {
                result = false;
                this.warningProviderDataSet.showWarning(MapViewProperties.getString("String_OutputImageDataset_DatasetNameIsInvalid"));
            } else {
                this.warningProviderDataSet.hideWarning();
            }
        }
        return result;
    }

    private boolean isValidResolution() {
        boolean result = true;
        if (this.textFieldResolution.getText().isEmpty()) {
            result = false;
            this.warningProviderResolution.showWarning();
        } else {
            if (StringUtilities.isNumber(this.textFieldResolution.getText()) && this.outputRangeBounds != null) {
                double resolutionValue = StringUtilities.getNumber(this.textFieldResolution.getText());
                int currentRowCount = (int) Math.round(this.outputRangeBounds.getHeight() / resolutionValue);
                int currentColumnCount = (int) Math.round(this.outputRangeBounds.getWidth() / resolutionValue);
                if (Double.compare(resolutionValue, 0) == -1) {
                    result = false;
                    this.warningProviderResolution.showWarning(MapViewProperties.getString("String_OutputImageDataset_ResoltionMostSmall"));
                } else if (Double.compare(currentColumnCount, Integer.MAX_VALUE) == 1 || Double.compare(currentRowCount, Integer.MAX_VALUE) == 1) {
                    result = false;
                    this.warningProviderResolution.showWarning(MapViewProperties.getString("String_OutputImageDataset_ResoltionTooSmall"));
                } else if (Double.compare(currentColumnCount, 10) == -1 || Double.compare(currentColumnCount, 10) == 0
                        || Double.compare(currentRowCount, 10) == -1 || Double.compare(currentRowCount, 10) == 0) {
                    result = false;
                    this.warningProviderResolution.showWarning(MapViewProperties.getString("String_OutputImageDataset_ResoltionTooLarge"));
                } else {
                    this.textFieldRowCount.setText(COMMA_FORMAT.format(currentRowCount));
                    this.textFieldColumnCount.setText(COMMA_FORMAT.format(currentColumnCount));
                    this.warningProviderResolution.hideWarning();
                }
            } else if (this.outputRangeBounds != null) {
                result = false;
                this.warningProviderResolution.showWarning(MapViewProperties.getString("String_OutputImageDataset_ResoltionIsInvalid"));
            }
        }
        return result;
    }

    private void registerEvents() {
        this.datasourceComboBox.addItemListener(this.datasourceChangeListener);
        this.outRangeWaringTextFieldBottom.getTextField().addCaretListener(this.outPutRangeListener);
        this.outRangeWaringTextFieldLeft.getTextField().addCaretListener(this.outPutRangeListener);
        this.outRangeWaringTextFieldRight.getTextField().addCaretListener(this.outPutRangeListener);
        this.outRangeWaringTextFieldTop.getTextField().addCaretListener(this.outPutRangeListener);
        this.textFieldDataset.getDocument().addDocumentListener(this.datasetNameChangeListener);
        this.textFieldResolution.getDocument().addDocumentListener(this.resolutionTextChangeListener);
        this.panelButton.getButtonOk().addActionListener(this.runListener);
        this.panelButton.getButtonCancel().addActionListener(this.cancelDialog);
    }

    private void removeEvents() {
        this.datasourceComboBox.removeItemListener(this.datasourceChangeListener);
        this.outRangeWaringTextFieldBottom.getTextField().removeCaretListener(this.outPutRangeListener);
        this.outRangeWaringTextFieldLeft.getTextField().removeCaretListener(this.outPutRangeListener);
        this.outRangeWaringTextFieldRight.getTextField().removeCaretListener(this.outPutRangeListener);
        this.outRangeWaringTextFieldTop.getTextField().removeCaretListener(this.outPutRangeListener);
        this.textFieldDataset.getDocument().removeDocumentListener(this.datasetNameChangeListener);
        this.textFieldResolution.getDocument().removeDocumentListener(this.resolutionTextChangeListener);
        this.panelButton.getButtonOk().removeActionListener(this.runListener);
        this.panelButton.getButtonCancel().removeActionListener(this.cancelDialog);

    }

    private CaretListener outPutRangeListener = new CaretListener() {
        @Override
        public void caretUpdate(CaretEvent e) {
            if (StringUtilities.isNumber(outRangeWaringTextFieldBottom.getText().replace(",", "")) && StringUtilities.isNumber(outRangeWaringTextFieldLeft.getText().replace(",", ""))
                    && StringUtilities.isNumber(outRangeWaringTextFieldRight.getText().replace(",", "")) && StringUtilities.isNumber(outRangeWaringTextFieldTop.getText().replace(",", ""))) {
                Rectangle2D rectangle2D = panelOutPutRangeSetting.getRangeBound();
                if (rectangle2D != null) {
                    isValidRangeBounds = true;
                    if (!outputRangeBounds.equals(rectangle2D)) {
                        outputRangeBounds = rectangle2D;
                    }
                } else {
                    isValidRangeBounds = false;
                }
            } else {
                isValidRangeBounds = false;
            }
            isCanRun();
        }
    };

    private DocumentListener datasetNameChangeListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            isCanRun();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            isCanRun();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            isCanRun();
        }
    };

    private DocumentListener resolutionTextChangeListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            isCanRun();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            isCanRun();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            isCanRun();
        }
    };

    private ActionListener runListener=new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            run();
        }
    };

    private ActionListener cancelDialog=new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            cancelAndCloseDailog();
        }
    };

    private ItemListener datasourceChangeListener=new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            isCanRun();
        }
    };

	@Override
	public void dispose() {
		panelOutPutRangeSetting.dispose();
		super.dispose();
	}
}
