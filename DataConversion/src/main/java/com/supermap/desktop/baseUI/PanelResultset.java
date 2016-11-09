package com.supermap.desktop.baseUI;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.EncodeType;
import com.supermap.data.conversion.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IImportSettingResultset;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.iml.ImportInfo;
import com.supermap.desktop.importUI.PanelImport;
import com.supermap.desktop.importUI.PanelTransformForImage;
import com.supermap.desktop.localUtilities.FileUtilities;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilities.EncodeTypeUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by xie on 2016/9/29.
 * 结果设置界面
 */
public class PanelResultset extends JPanel implements IImportSettingResultset {

    private PanelImport owner;
    private JLabel labelDatasource;
    private DatasourceComboBox comboBoxDatasource;
    private JLabel labelDatasetName;
    private JTextField textFieldDatasetName;
    private JLabel labelEncodeType;
    private JComboBox comboBoxEncodeType;
    private JLabel labelImportMode;
    private JComboBox comboBoxImportMode;
    private JLabel labelDatasetType;
    private DatasetComboBox comboBoxDatasetType;
    private JCheckBox checkBoxFieldIndex;
    private JCheckBox checkBoxSpatialIndex;
    private ImportInfo importInfo;
    private ImportSetting importSetting;
    private ArrayList<PanelImport> panelImports;
    private int layeroutType;

    private ItemListener datasourceListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (null != panelImports) {
                    for (PanelImport tempPanelImport : panelImports) {
                        tempPanelImport.getResultset().getComboBoxDatasource().setSelectedDatasource(comboBoxDatasource.getSelectedDatasource());
                    }
                } else {
                    importSetting.setTargetDatasource(comboBoxDatasource.getSelectedDatasource());
                }
            }
        }
    };

    private ItemListener encodeTypeListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (null != panelImports) {
                    for (PanelImport tempPanelImport : panelImports) {
                        tempPanelImport.getResultset().getComboBoxEncodeType().setSelectedItem(comboBoxEncodeType.getSelectedItem());
                    }
                } else {
                    String encodeType = comboBoxEncodeType.getSelectedItem().toString();
                    if (importSetting instanceof ImportSettingGRD && encodeType.equals("SGL")) {
                        Application.getActiveApplication().getOutput().output(MessageFormat.format(DataConversionProperties.getString("String_EncodingError"), importSetting.getSourceFilePath(),
                                encodeType, "LZW"));
                        importSetting.setTargetEncodeType(EncodeType.LZW);
                    } else if (importSetting instanceof ImportSettingSHP && encodeType.equals(DataConversionProperties.getString("string_comboboxitem_int32"))) {
                        Application.getActiveApplication().getOutput().output(MessageFormat.format(DataConversionProperties.getString("String_EncodingError"), importSetting.getSourceFilePath(),
                                encodeType, DataConversionProperties.getString("string_comboboxitem_nullcoding")));
                        importSetting.setTargetEncodeType(EncodeType.NONE);
                    } else {
                        importSetting.setTargetEncodeType(EncodeTypeUtilities.valueOf(encodeType));
                    }
                }
            }
        }
    };
    private ItemListener importModeListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (null != panelImports) {
                    for (PanelImport tempPanelImport : panelImports) {
                        tempPanelImport.getResultset().getComboBoxImportMode().setSelectedItem(comboBoxImportMode.getSelectedItem());
                    }
                } else {
                    int importModel = comboBoxImportMode.getSelectedIndex();
                    switch (importModel) {
                        case 0:
                            importSetting.setImportMode(ImportMode.NONE);
                            break;
                        case 1:
                            importSetting.setImportMode(ImportMode.APPEND);
                            break;
                        case 2:
                            importSetting.setImportMode(ImportMode.OVERWRITE);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    };
    private ItemListener datasetTypeListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String datasetType = comboBoxDatasetType.getSelectItem().toString();
                if (null != panelImports) {
                    for (PanelImport tempPanelImport : panelImports) {
                        if (datasetType.equalsIgnoreCase(CommonProperties.getString("String_DatasetType_CAD"))) {
                            tempPanelImport.getResultset().getComboBoxDatasetType().setSelectedIndex(0);
                        } else if (datasetType.equalsIgnoreCase(DataConversionProperties.getString("string_comboboxitem_sample"))) {
                            tempPanelImport.getResultset().getComboBoxDatasetType().setSelectedIndex(1);
                        } else if (datasetType.equalsIgnoreCase(DataConversionProperties.getString("string_comboboxitem_image"))) {
                            tempPanelImport.getResultset().getComboBoxDatasetType().setSelectedIndex(0);
                        } else if (datasetType.equalsIgnoreCase(DataConversionProperties.getString("string_comboboxitem_grid"))) {
                            tempPanelImport.getResultset().getComboBoxDatasetType().setSelectedIndex(1);
                        } else if (datasetType.equals(DataConversionProperties.getString("String_datasetType2D"))) {
                            tempPanelImport.getResultset().getComboBoxDatasetType().setSelectedIndex(0);
                        } else if (datasetType.equals(DataConversionProperties.getString("String_datasetType3D"))) {
                            tempPanelImport.getResultset().getComboBoxDatasetType().setSelectedIndex(1);
                        }
                    }
                } else {
                    if (datasetType.equals(CommonProperties.getString("String_DatasetType_CAD"))) {
                        if (importSetting instanceof ImportSettingTAB) {
                            ((ImportSettingTAB) importSetting).setImportingAsCAD(true);
                        } else if (importSetting instanceof ImportSettingMIF) {
                            ((ImportSettingMIF) importSetting).setImportingAsCAD(true);
                        } else if (importSetting instanceof ImportSettingDWG) {
                            ((ImportSettingDWG) importSetting).setImportingAsCAD(true);
                        } else if (importSetting instanceof ImportSettingDXF) {
                            ((ImportSettingDXF) importSetting).setImportingAsCAD(true);
                        } else if (importSetting instanceof ImportSettingKML) {
                            ((ImportSettingKML) importSetting).setImportingAsCAD(true);
                        } else if (importSetting instanceof ImportSettingKMZ) {
                            ((ImportSettingKMZ) importSetting).setImportingAsCAD(true);
                        } else if (importSetting instanceof ImportSettingMAPGIS) {
                            ((ImportSettingMAPGIS) importSetting).setImportingAsCAD(true);
                        } else if (importSetting instanceof ImportSettingDGN) {
                            ((ImportSettingDGN) importSetting).setImportingAsCAD(true);
                        }
                    } else if (datasetType.equals(DataConversionProperties.getString("string_comboboxitem_sample"))) {
                        if (importSetting instanceof ImportSettingTAB) {
                            ((ImportSettingTAB) importSetting).setImportingAsCAD(false);
                        } else if (importSetting instanceof ImportSettingMIF) {
                            ((ImportSettingMIF) importSetting).setImportingAsCAD(false);
                        } else if (importSetting instanceof ImportSettingDWG) {
                            ((ImportSettingDWG) importSetting).setImportingAsCAD(false);
                        } else if (importSetting instanceof ImportSettingDXF) {
                            ((ImportSettingDXF) importSetting).setImportingAsCAD(false);
                        } else if (importSetting instanceof ImportSettingKML) {
                            ((ImportSettingKML) importSetting).setImportingAsCAD(false);
                        } else if (importSetting instanceof ImportSettingKMZ) {
                            ((ImportSettingKMZ) importSetting).setImportingAsCAD(false);
                        } else if (importSetting instanceof ImportSettingMAPGIS) {
                            ((ImportSettingMAPGIS) importSetting).setImportingAsCAD(false);
                        } else if (importSetting instanceof ImportSettingDGN) {
                            ((ImportSettingDGN) importSetting).setImportingAsCAD(false);
                        }
                    } else if (datasetType.equals(DataConversionProperties.getString("string_comboboxitem_image"))) {
                        ((PanelTransformForImage) owner.getTransform()).getComboBoxBandImportModel().insertItemAt(DataConversionProperties.getString("string_compositeBand"), 2);
                        if (importSetting instanceof ImportSettingJPG) {
                            ((ImportSettingJPG) importSetting).setImportingAsGrid(false);
                        } else if (importSetting instanceof ImportSettingJP2) {
                            ((ImportSettingJP2) importSetting).setImportingAsGrid(false);
                        } else if (importSetting instanceof ImportSettingPNG) {
                            ((ImportSettingPNG) importSetting).setImportingAsGrid(false);
                        } else if (importSetting instanceof ImportSettingBMP) {
                            ((ImportSettingBMP) importSetting).setImportingAsGrid(false);
                        } else if (importSetting instanceof ImportSettingIMG) {
                            ((ImportSettingIMG) importSetting).setImportingAsGrid(false);
                        } else if (importSetting instanceof ImportSettingTIF) {
                            ((ImportSettingTIF) importSetting).setImportingAsGrid(false);
                        } else if (importSetting instanceof ImportSettingGIF) {
                            ((ImportSettingGIF) importSetting).setImportingAsGrid(false);
                        } else if (importSetting instanceof ImportSettingMrSID) {
                            ((ImportSettingMrSID) importSetting).setImportingAsGrid(false);
                        } else if (importSetting instanceof ImportSettingECW) {
                            ((ImportSettingECW) importSetting).setImportingAsGrid(false);
                        }
                    } else if (datasetType.equals(DataConversionProperties.getString("string_comboboxitem_grid"))) {
                        ((PanelTransformForImage) owner.getTransform()).getComboBoxBandImportModel().removeItem(DataConversionProperties.getString("string_compositeBand"));
                        if (importSetting instanceof ImportSettingJPG) {
                            ((ImportSettingJPG) importSetting).setImportingAsGrid(true);
                        } else if (importSetting instanceof ImportSettingJP2) {
                            ((ImportSettingJP2) importSetting).setImportingAsGrid(true);
                        } else if (importSetting instanceof ImportSettingPNG) {
                            ((ImportSettingPNG) importSetting).setImportingAsGrid(true);
                        } else if (importSetting instanceof ImportSettingBMP) {
                            ((ImportSettingBMP) importSetting).setImportingAsGrid(true);
                        } else if (importSetting instanceof ImportSettingIMG) {
                            ((ImportSettingIMG) importSetting).setImportingAsGrid(true);
                        } else if (importSetting instanceof ImportSettingTIF) {
                            ((ImportSettingTIF) importSetting).setImportingAsGrid(true);
                        } else if (importSetting instanceof ImportSettingGIF) {
                            ((ImportSettingGIF) importSetting).setImportingAsGrid(true);
                        } else if (importSetting instanceof ImportSettingMrSID) {
                            ((ImportSettingMrSID) importSetting).setImportingAsGrid(true);
                        } else if (importSetting instanceof ImportSettingECW) {
                            ((ImportSettingECW) importSetting).setImportingAsGrid(true);
                        }
                    } else if (datasetType.equals(DataConversionProperties.getString("String_datasetType2D"))) {
                        ((ImportSettingLIDAR) importSetting).setImportingAs3D(false);
                    } else if (datasetType.equals(DataConversionProperties.getString("String_datasetType3D"))) {
                        ((ImportSettingLIDAR) importSetting).setImportingAs3D(true);
                    }
                }
            }
        }
    };
    private ItemListener fieldIndexListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (null != panelImports) {
                    for (PanelImport tempPanelImport : panelImports) {
                        tempPanelImport.getResultset().getCheckBoxFieldIndex().setSelected(checkBoxFieldIndex.isSelected());
                    }
                } else {
                    importInfo.setFieldIndex(checkBoxFieldIndex.isSelected());
                }
            }
        }
    };
    private ItemListener spatialIndexListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (null != panelImports) {
                    for (PanelImport tempPanelImport : panelImports) {
                        tempPanelImport.getResultset().getCheckBoxSpatialIndex().setSelected(checkBoxSpatialIndex.isSelected());
                    }
                } else {
                    importInfo.setSpatialIndex(checkBoxSpatialIndex.isSelected());
                }
            }
        }
    };
    private DocumentListener documentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            setTargetDatasetName();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            setTargetDatasetName();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            setTargetDatasetName();
        }

        private void setTargetDatasetName() {
            Datasource targetDatasource = comboBoxDatasource.getSelectedDatasource();
            if (!StringUtilities.isNullOrEmpty(textFieldDatasetName.getText()) && !StringUtilities.isNullOrEmpty(targetDatasource.getDatasets().getAvailableDatasetName(textFieldDatasetName.getText()))) {
                String targetDatasetName = targetDatasource.getDatasets().getAvailableDatasetName(textFieldDatasetName.getText());
                if (null != panelImports) {
                    for (PanelImport tempPanelImport : panelImports) {
                        tempPanelImport.getResultset().getTextFieldDatasetName().setText(textFieldDatasetName.getText());
                    }
                } else {
                    importSetting.setTargetDatasetName(targetDatasetName);
                }
            }
        }
    };

    public PanelResultset(PanelImport owner, ImportInfo importInfo) {
        this.owner = owner;
        this.importInfo = importInfo;
        this.importSetting = importInfo.getImportSetting();
        initComponents();
        initLayerout();
        initResources();
        registEvents();
    }

    public PanelResultset(ArrayList<PanelImport> panelImports, int layeroutType) {
        this.panelImports = panelImports;
        this.importSetting = panelImports.get(panelImports.size() - 1).getImportInfo().getImportSetting();
        this.layeroutType = layeroutType;
        initComponents();
        if (this.layeroutType == PackageInfo.SAME_TYPE) {
            initLayerout();
        } else {
            resetLayout(layeroutType);
        }
        initResources();
        registEvents();
    }

    private void setDefaultImportSettingEncode() {
        importSetting.setTargetEncodeType(EncodeTypeUtilities.valueOf(comboBoxEncodeType.getSelectedItem().toString()));
    }

    private void resetLayout(int layeroutType) {
        this.setLayout(new GridBagLayout());
        if (layeroutType == PackageInfo.VERTICAL_TYPE) {
            initComboboxEncodeType(false);
            setDefaultImportSettingEncode();
            initDefaultLayout();
            this.add(this.checkBoxFieldIndex, new GridBagConstraintsHelper(0, 2, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            this.add(this.checkBoxSpatialIndex, new GridBagConstraintsHelper(4, 2, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        } else if (layeroutType == PackageInfo.GRID_TYPE) {
            initDefaultLayout();
            this.remove(labelDatasetName);
            this.remove(textFieldDatasetName);
            this.comboBoxDatasetType = new DatasetComboBox(new Dataset[]{});
            this.comboBoxDatasetType.setEnabled(false);
            this.add(this.labelDatasetType, new GridBagConstraintsHelper(4, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            this.add(this.comboBoxDatasetType, new GridBagConstraintsHelper(6, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
            this.comboBoxDatasource.setPreferredSize(PackageInfo.defaultSize);
            this.comboBoxEncodeType.setPreferredSize(PackageInfo.defaultSize);
            initComboboxEncodeType(true);
            this.comboBoxEncodeType.insertItemAt("SGL", 2);
            setDefaultImportSettingEncode();
        } else if (layeroutType == PackageInfo.GRID_AND_VERTICAL_TYPE) {
            this.add(this.labelDatasource, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 20).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            this.add(this.comboBoxDatasource, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
            this.add(this.labelImportMode, new GridBagConstraintsHelper(4, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            this.add(this.comboBoxImportMode, new GridBagConstraintsHelper(6, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        }
        setDefaultSize();
    }

    @Override
    public void initComponents() {
        this.labelDatasource = new JLabel();
        this.comboBoxDatasource = new DatasourceComboBox();
        initDatasource();
        this.labelDatasetName = new JLabel();
        this.textFieldDatasetName = new JTextField();
        initDatasetName();

        this.labelEncodeType = new JLabel();
        this.comboBoxEncodeType = new JComboBox();
        this.labelImportMode = new JLabel();
        this.comboBoxImportMode = new JComboBox();
        initImportMode();
        this.labelDatasetType = new JLabel();
        this.checkBoxFieldIndex = new JCheckBox();
        this.checkBoxSpatialIndex = new JCheckBox();
        this.comboBoxEncodeType.setEditable(true);
        ((JTextField) this.comboBoxEncodeType.getEditor().getEditorComponent()).setEditable(false);
        this.comboBoxImportMode.setEditable(true);
        ((JTextField) this.comboBoxImportMode.getEditor().getEditorComponent()).setEditable(false);
    }

    private void initImportMode() {
        this.comboBoxImportMode.setModel(new DefaultComboBoxModel(new String[]{
                DataConversionProperties.getString("String_FormImport_None"),
                DataConversionProperties.getString("String_FormImport_Append"),
                DataConversionProperties.getString("String_FormImport_OverWrite")
        }));
        if (null != importSetting.getImportMode()) {
            ImportMode mode = importSetting.getImportMode();
            if (mode.equals(ImportMode.NONE)) {
                this.comboBoxImportMode.setSelectedIndex(0);
            } else if (mode.equals(ImportMode.APPEND)) {
                this.comboBoxImportMode.setSelectedIndex(1);
            } else {
                this.comboBoxImportMode.setSelectedIndex(2);
            }
        } else {
            this.importSetting.setImportMode(ImportMode.NONE);
        }
    }

    private void initDatasetName() {
        if (!StringUtilities.isNullOrEmpty(importSetting.getTargetDatasetName())) {
            this.textFieldDatasetName.setText(importSetting.getTargetDatasetName());
        } else {
            String textInfo = FileUtilities.getFileAlias(this.importSetting.getSourceFilePath());
            String availableName = this.comboBoxDatasource.getSelectedDatasource().getDatasets().getAvailableDatasetName(textInfo);
            this.textFieldDatasetName.setText(availableName);
            this.textFieldDatasetName.setToolTipText(availableName);
            this.importSetting.setTargetDatasetName(availableName);
        }
    }

    private void initDatasource() {
        if (null != this.importSetting.getTargetDatasource()) {
            this.comboBoxDatasource.setSelectedDatasource(this.importSetting.getTargetDatasource());
        } else {
            Datasource targetDatasource = null;
            if (null != Application.getActiveApplication().getActiveDatasources() && Application.getActiveApplication().getActiveDatasources().length >= 0) {
                targetDatasource = Application.getActiveApplication().getActiveDatasources()[0];
                this.comboBoxDatasource.setSelectedDatasource(targetDatasource);
            }
            if (null != targetDatasource) {
                this.importSetting.setTargetDatasource(targetDatasource);
            }
        }
    }

    @Override
    public void initLayerout() {
        this.setLayout(new GridBagLayout());
        if (importSetting instanceof ImportSettingCSV) {
            this.removeAll();
            this.add(this.labelDatasource, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 20).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            this.add(this.comboBoxDatasource, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
            this.add(this.labelDatasetName, new GridBagConstraintsHelper(4, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            this.add(this.textFieldDatasetName, new GridBagConstraintsHelper(6, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
            setDefaultSize();
        } else if (importSetting instanceof ImportSettingWOR) {
            initComboboxEncodeType(false);
            setDefaultImportSettingEncode();
            this.removeAll();
            this.add(this.labelDatasource, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 20).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            this.add(this.comboBoxDatasource, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
            this.add(this.labelEncodeType, new GridBagConstraintsHelper(4, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            this.add(this.comboBoxEncodeType, new GridBagConstraintsHelper(6, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));

            this.add(this.labelImportMode, new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 20).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            this.add(this.comboBoxImportMode, new GridBagConstraintsHelper(2, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
            setDefaultSize();
        } else if (importSetting instanceof ImportSettingModel3DS || importSetting instanceof ImportSettingModelDXF
                || importSetting instanceof ImportSettingModelFBX || importSetting instanceof ImportSettingModelOSG
                || importSetting instanceof ImportSettingModelX) {
            this.removeAll();
            this.comboBoxDatasetType = new DatasetComboBox(new String[]{DataConversionProperties.getString("string_comboboxitem_model")});
            this.add(this.labelDatasource, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 20).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            this.add(this.comboBoxDatasource, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
            this.add(this.labelDatasetName, new GridBagConstraintsHelper(4, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            this.add(this.textFieldDatasetName, new GridBagConstraintsHelper(6, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));

            this.add(this.labelDatasetType, new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 20).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            this.add(this.comboBoxDatasetType, new GridBagConstraintsHelper(2, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
            this.add(this.labelImportMode, new GridBagConstraintsHelper(4, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            this.add(this.comboBoxImportMode, new GridBagConstraintsHelper(6, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
            this.comboBoxDatasetType.setEnabled(false);
            setDefaultSize();
        } else if (importSetting instanceof ImportSettingTAB || importSetting instanceof ImportSettingMIF
                || importSetting instanceof ImportSettingDWG || importSetting instanceof ImportSettingDXF
                || importSetting instanceof ImportSettingKML || importSetting instanceof ImportSettingKMZ
                || importSetting instanceof ImportSettingMAPGIS || importSetting instanceof ImportSettingDGN) {
            initComboboxEncodeType(false);
            setDefaultImportSettingEncode();
            this.comboBoxDatasetType = new DatasetComboBox(new String[]{CommonProperties.getString("String_DatasetType_CAD"), DataConversionProperties.getString("string_comboboxitem_sample")});
            setDefaultLayout();
            initTargetDatasetTypeForVector();
        } else if (importSetting instanceof ImportSettingJPG || importSetting instanceof ImportSettingJP2 ||
                importSetting instanceof ImportSettingPNG || importSetting instanceof ImportSettingBMP ||
                importSetting instanceof ImportSettingIMG || importSetting instanceof ImportSettingTIF ||
                importSetting instanceof ImportSettingGIF || importSetting instanceof ImportSettingMrSID
                || importSetting instanceof ImportSettingECW) {
            if (importSetting instanceof ImportSettingECW) {
                this.comboBoxEncodeType.setModel(new DefaultComboBoxModel(new String[]{DataConversionProperties.getString("string_comboboxitem_nullcoding")}));
            } else {
                initComboboxEncodeType(true);
            }
            if (importSetting instanceof ImportSettingJP2) {
                this.comboBoxEncodeType.insertItemAt("SGL", 2);
            }
            setDefaultImportSettingEncode();
            this.comboBoxDatasetType = new DatasetComboBox(new String[]{DataConversionProperties.getString("string_comboboxitem_image"), DataConversionProperties.getString("string_comboboxitem_grid")});
            setDefaultLayout();
            this.remove(this.checkBoxFieldIndex);
            this.remove(this.checkBoxSpatialIndex);
            initTargetDatasetTypeForImage();
            setDefaultSize();
        } else if (importSetting instanceof ImportSettingSIT || importSetting instanceof ImportSettingGRD ||
                importSetting instanceof ImportSettingGBDEM || importSetting instanceof ImportSettingUSGSDEM ||
                importSetting instanceof ImportSettingSHP || importSetting instanceof ImportSettingE00 ||
                importSetting instanceof ImportSettingDBF || importSetting instanceof ImportSettingBIL ||
                importSetting instanceof ImportSettingBSQ || importSetting instanceof ImportSettingBIP ||
                importSetting instanceof ImportSettingTEMSClutter || importSetting instanceof ImportSettingVCT ||
                importSetting instanceof ImportSettingRAW || importSetting instanceof ImportSettingGJB) {
            this.comboBoxDatasetType = new DatasetComboBox();
            if (importSetting instanceof ImportSettingGRD) {
                this.comboBoxEncodeType.setModel(new DefaultComboBoxModel(new String[]{DataConversionProperties.getString("string_comboboxitem_nullcoding"), "SGL", "LZW"}));
            } else if (importSetting instanceof ImportSettingSIT) {
                initComboboxEncodeType(true);
            } else {
                initComboboxEncodeType(false);
            }
            setDefaultImportSettingEncode();
            initDefaultLayout();
            if (importSetting instanceof ImportSettingSHP) {
                this.add(this.checkBoxFieldIndex, new GridBagConstraintsHelper(0, 2, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
                this.add(this.checkBoxSpatialIndex, new GridBagConstraintsHelper(4, 2, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            } else if (importSetting instanceof ImportSettingE00 || importSetting instanceof ImportSettingGJB) {
                this.add(this.checkBoxSpatialIndex, new GridBagConstraintsHelper(0, 2, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
                if (importSetting instanceof ImportSettingGJB) {
                    this.labelDatasetName.setEnabled(false);
                    this.textFieldDatasetName.setEnabled(false);
                }
            }
            setDefaultSize();
        } else if (importSetting instanceof ImportSettingLIDAR) {
            initComboboxEncodeType(false);
            setDefaultImportSettingEncode();
            this.comboBoxDatasetType = new DatasetComboBox(new String[]{DataConversionProperties.getString("String_datasetType2D"), DataConversionProperties.getString("String_datasetType3D")});
            initDefaultLayout();
            if (((ImportSettingLIDAR) importSetting).isImportingAs3D()) {
                this.comboBoxDatasetType.setSelectedIndex(1);
            } else {
                this.comboBoxDatasetType.setSelectedIndex(0);
            }
            this.add(this.labelDatasetType, new GridBagConstraintsHelper(0, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 20).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            this.add(this.comboBoxDatasetType, new GridBagConstraintsHelper(2, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
            this.add(this.checkBoxSpatialIndex, new GridBagConstraintsHelper(4, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            setDefaultSize();
        }
    }

    private void setDefaultSize() {
        this.textFieldDatasetName.setPreferredSize(PackageInfo.defaultSize);
        this.comboBoxDatasource.setPreferredSize(PackageInfo.defaultSize);
        if (null != comboBoxDatasetType) {
            this.comboBoxDatasetType.setPreferredSize(PackageInfo.defaultSize);
        }
        this.comboBoxImportMode.setPreferredSize(PackageInfo.defaultSize);
        this.comboBoxEncodeType.setPreferredSize(PackageInfo.defaultSize);
    }

    private void initTargetDatasetTypeForVector() {
        this.comboBoxDatasetType.setSelectedIndex(1);
        if (importSetting instanceof ImportSettingTAB && ((ImportSettingTAB) importSetting).isImportingAsCAD()) {
            this.comboBoxDatasetType.setSelectedIndex(0);
        } else if (importSetting instanceof ImportSettingMIF && ((ImportSettingMIF) importSetting).isImportingAsCAD()) {
            this.comboBoxDatasetType.setSelectedIndex(0);
        } else if (importSetting instanceof ImportSettingDWG && ((ImportSettingDWG) importSetting).isImportingAsCAD()) {
            this.comboBoxDatasetType.setSelectedIndex(0);
        } else if (importSetting instanceof ImportSettingDXF && ((ImportSettingDXF) importSetting).isImportingAsCAD()) {
            this.comboBoxDatasetType.setSelectedIndex(0);
        } else if (importSetting instanceof ImportSettingKML && ((ImportSettingKML) importSetting).isImportingAsCAD()) {
            this.comboBoxDatasetType.setSelectedIndex(0);
        } else if (importSetting instanceof ImportSettingKMZ && ((ImportSettingKMZ) importSetting).isImportingAsCAD()) {
            this.comboBoxDatasetType.setSelectedIndex(0);
        } else if (importSetting instanceof ImportSettingMAPGIS && ((ImportSettingMAPGIS) importSetting).isImportingAsCAD()) {
            this.comboBoxDatasetType.setSelectedIndex(0);
        } else if (importSetting instanceof ImportSettingDGN && ((ImportSettingDGN) importSetting).isImportingAsCAD()) {
            this.comboBoxDatasetType.setSelectedIndex(0);
        }
    }

    private void initTargetDatasetTypeForImage() {
        this.comboBoxDatasetType.setSelectedIndex(0);
        if (importSetting instanceof ImportSettingJPG && ((ImportSettingJPG) importSetting).isImportingAsGrid()) {
            this.comboBoxDatasetType.setSelectedIndex(1);
        } else if (importSetting instanceof ImportSettingJP2 && ((ImportSettingJP2) importSetting).isImportingAsGrid()) {
            this.comboBoxDatasetType.setSelectedIndex(1);
        } else if (importSetting instanceof ImportSettingPNG && ((ImportSettingPNG) importSetting).isImportingAsGrid()) {
            this.comboBoxDatasetType.setSelectedIndex(1);
        } else if (importSetting instanceof ImportSettingBMP && ((ImportSettingBMP) importSetting).isImportingAsGrid()) {
            this.comboBoxDatasetType.setSelectedIndex(1);
        } else if (importSetting instanceof ImportSettingIMG && ((ImportSettingIMG) importSetting).isImportingAsGrid()) {
            this.comboBoxDatasetType.setSelectedIndex(1);
        } else if (importSetting instanceof ImportSettingTIF && ((ImportSettingTIF) importSetting).isImportingAsGrid()) {
            this.comboBoxDatasetType.setSelectedIndex(1);
        } else if (importSetting instanceof ImportSettingGIF && ((ImportSettingGIF) importSetting).isImportingAsGrid()) {
            this.comboBoxDatasetType.setSelectedIndex(1);
        } else if (importSetting instanceof ImportSettingMrSID && ((ImportSettingMrSID) importSetting).isImportingAsGrid()) {
            this.comboBoxDatasetType.setSelectedIndex(1);
        } else if (importSetting instanceof ImportSettingECW && ((ImportSettingECW) importSetting).isImportingAsGrid()) {
            this.comboBoxDatasetType.setSelectedIndex(1);
        }
    }

    private void setDefaultLayout() {
        initDefaultLayout();
        this.add(this.labelDatasetType, new GridBagConstraintsHelper(0, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 20).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.comboBoxDatasetType, new GridBagConstraintsHelper(2, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        this.add(this.checkBoxFieldIndex, new GridBagConstraintsHelper(4, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.checkBoxSpatialIndex, new GridBagConstraintsHelper(6, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
    }

    private void initDefaultLayout() {
        this.removeAll();
        this.add(this.labelDatasource, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 20).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.comboBoxDatasource, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        this.add(this.labelDatasetName, new GridBagConstraintsHelper(4, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.textFieldDatasetName, new GridBagConstraintsHelper(6, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));

        this.add(this.labelEncodeType, new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 20).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.comboBoxEncodeType, new GridBagConstraintsHelper(2, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        this.add(this.labelImportMode, new GridBagConstraintsHelper(4, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.comboBoxImportMode, new GridBagConstraintsHelper(6, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
    }

    private void initComboboxEncodeType(boolean isGrid) {
        if (isGrid) {
            this.comboBoxEncodeType.setModel(new DefaultComboBoxModel(new String[]{DataConversionProperties.getString("string_comboboxitem_nullcoding"), "DCT",
                    "PNG", "LZW"}));
            this.comboBoxEncodeType.setSelectedItem("DCT");
        } else {
            this.comboBoxEncodeType.setModel(new DefaultComboBoxModel(new String[]{DataConversionProperties.getString("string_comboboxitem_nullcoding"), DataConversionProperties.getString("string_comboboxitem_byte"),
                    DataConversionProperties.getString("string_comboboxitem_int16"), DataConversionProperties.getString("string_comboboxitem_int24"), DataConversionProperties.getString("string_comboboxitem_int32")}));
        }
    }

    @Override
    public void registEvents() {
        removeEvents();
        this.comboBoxDatasource.addItemListener(this.datasourceListener);
        this.textFieldDatasetName.getDocument().addDocumentListener(this.documentListener);
        this.comboBoxEncodeType.addItemListener(this.encodeTypeListener);
        this.comboBoxImportMode.addItemListener(this.importModeListener);
        if (null != this.comboBoxDatasetType) {
            this.comboBoxDatasetType.addItemListener(this.datasetTypeListener);
        }
        this.checkBoxFieldIndex.addItemListener(this.fieldIndexListener);
        this.checkBoxSpatialIndex.addItemListener(this.spatialIndexListener);
    }

    @Override
    public void removeEvents() {
        this.comboBoxDatasource.removeItemListener(this.datasourceListener);
        this.textFieldDatasetName.getDocument().removeDocumentListener(this.documentListener);
        this.comboBoxEncodeType.removeItemListener(this.encodeTypeListener);
        this.comboBoxImportMode.removeItemListener(this.importModeListener);
        if (null != this.comboBoxDatasetType) {
            this.comboBoxDatasetType.removeItemListener(this.datasetTypeListener);
        }
        this.checkBoxFieldIndex.removeItemListener(this.fieldIndexListener);
        this.checkBoxSpatialIndex.removeItemListener(this.spatialIndexListener);
    }

    private void initResources() {
        this.labelImportMode.setText(DataConversionProperties.getString("string_label_lblImportType"));
        this.labelDatasource.setText(DataConversionProperties.getString("string_label_lblDatasource"));
        this.labelDatasetName.setText(DataConversionProperties.getString("string_label_targetDataset"));
        this.labelEncodeType.setText(DataConversionProperties.getString("string_label_lblCodingtype"));
        this.labelDatasetType.setText(DataConversionProperties.getString("string_label_lblDatasetType"));
        this.checkBoxFieldIndex.setText(DataConversionProperties.getString("string_checkbox_chckbxFieldIndex"));
        this.checkBoxSpatialIndex.setText(DataConversionProperties.getString("string_checkbox_chckbxSpatialIndex"));
        this.setBorder(new TitledBorder(DataConversionProperties.getString("string_border_panel")));
    }

    public DatasourceComboBox getComboBoxDatasource() {
        return comboBoxDatasource;
    }

    public JTextField getTextFieldDatasetName() {
        return textFieldDatasetName;
    }

    public JComboBox getComboBoxEncodeType() {
        return comboBoxEncodeType;
    }

    public JComboBox getComboBoxImportMode() {
        return comboBoxImportMode;
    }

    public DatasetComboBox getComboBoxDatasetType() {
        return comboBoxDatasetType;
    }

    public JCheckBox getCheckBoxFieldIndex() {
        return checkBoxFieldIndex;
    }

    public JCheckBox getCheckBoxSpatialIndex() {
        return checkBoxSpatialIndex;
    }
}
