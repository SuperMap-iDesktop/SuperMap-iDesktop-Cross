package com.supermap.desktop.mapview.layer.propertycontrols;

import com.supermap.desktop.DefaultValues;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.event.ColorTableChangeEvent;
import com.supermap.desktop.event.ColorTableChangeListener;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.mapview.layer.propertymodel.LayerGridParamPropertyModel;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.*;
import com.supermap.desktop.ui.controls.ComponentDropDown;
import com.supermap.desktop.ui.controls.DialogResult;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

public class LayerGridParamPropertyControl extends AbstractLayerPropertyControl {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final String PROPERTY_VALUE = "value";

    private JLabel labelBrightness;
    private SMSpinner spinnerBrightness;
    private JLabel labelContrast;
    private SMSpinner spinnerContrast;
    private JLabel labelColorTable;
    private JLabel labelSpecialValue;
    private SMFormattedTextField textFieldSpecialValue;
    private JLabel labelSpecialValueColor;
    private ComponentDropDown buttonSpecialValueColor;
    private TristateCheckBox checkBoxIsSpecialValueTransparent;

    private JButton buttonTable;

    private ChangeListener spinnerValueChangeListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            if (e.getSource() == spinnerBrightness) {
                spinnerBrightnessValueChanged();
            } else if (e.getSource() == spinnerContrast) {
                spinnerContrastValueChanged();
            }
        }
    };
    private PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getSource() == textFieldSpecialValue) {
                textFieldValuePropertyChanged();
            } else if (evt.getSource() == buttonSpecialValueColor) {
                buttonSpecialValueColorColorChange();
            }
        }
    };
    private StateChangeListener checkBoxStateChangeListener = new StateChangeListener() {

        @Override
        public void stateChange(StateChangeEvent e) {
            if (e.getSource() == checkBoxIsSpecialValueTransparent) {
                checkBoxIsSpecialValueTransCheckedChanged();
            }
        }
    };
    private ActionListener buttonTableListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            LayerGridParamColorTableDialog layerGridParamColorTableDialog = new LayerGridParamColorTableDialog(getModifiedLayerPropertyModel());
            layerGridParamColorTableDialog.addColorTableChangeListener(new ColorTableChangeListener() {
                @Override
                public void colorTableChange(ColorTableChangeEvent event) {
                    checkChanged();
                }
            });
            DialogResult dialogResult = layerGridParamColorTableDialog.showDialog();
            if (dialogResult != DialogResult.OK) {
                layerGridParamColorTableDialog.actionCanceled();
            }
        }
    };

    public LayerGridParamPropertyControl() {
        // TODO
    }

    @Override
    public LayerGridParamPropertyModel getLayerPropertyModel() {
        return (LayerGridParamPropertyModel) super.getLayerPropertyModel();
    }

    @Override
    public LayerGridParamPropertyModel getModifiedLayerPropertyModel() {
        return (LayerGridParamPropertyModel) super.getModifiedLayerPropertyModel();
    }

    @Override
    protected void initializeComponents() {
        this.setBorder(BorderFactory.createTitledBorder("GridParamater"));

        this.labelBrightness = new JLabel("Brightness:");
        this.labelBrightness.setToolTipText(this.labelBrightness.getText());
        this.spinnerBrightness = new SMSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        this.labelContrast = new JLabel("Contrast:");
        this.labelContrast.setToolTipText(this.labelContrast.getText());
        this.spinnerContrast = new SMSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        this.labelColorTable = new JLabel("ColorTable:");
        this.labelColorTable.setToolTipText(this.labelColorTable.getText());
        this.labelSpecialValue = new JLabel("SpecialValue:");
        this.labelSpecialValue.setToolTipText(this.labelSpecialValue.getText());
        this.textFieldSpecialValue = new SMFormattedTextField(NumberFormat.getInstance());
        this.labelSpecialValueColor = new JLabel("SpecialValueColor:");
        this.labelSpecialValueColor.setToolTipText(this.labelSpecialValueColor.getText());
        this.buttonSpecialValueColor = new ComponentDropDown(ComponentDropDown.COLOR_TYPE);
        this.checkBoxIsSpecialValueTransparent = new TristateCheckBox("IsSpecialValueTransparent");
        this.buttonTable = new JButton();

        GroupLayout grouplayout = new GroupLayout(this);
        grouplayout.setAutoCreateContainerGaps(true);
        grouplayout.setAutoCreateGaps(true);
        this.setLayout(grouplayout);

        // @formatter:off
        grouplayout.setHorizontalGroup(grouplayout.createParallelGroup(Alignment.LEADING)
                .addGroup(grouplayout.createSequentialGroup()
                        .addGroup(grouplayout.createParallelGroup(Alignment.LEADING)
                                .addComponent(this.labelBrightness, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, DefaultValues.DEFAULT_LABEL_WIDTH)
                                .addComponent(this.labelContrast, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, DefaultValues.DEFAULT_LABEL_WIDTH)
                                .addComponent(this.labelColorTable, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, DefaultValues.DEFAULT_LABEL_WIDTH)
                                .addComponent(this.labelSpecialValue, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, DefaultValues.DEFAULT_LABEL_WIDTH)
                                .addComponent(this.labelSpecialValueColor, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, DefaultValues.DEFAULT_LABEL_WIDTH))
                        .addGroup(grouplayout.createParallelGroup(Alignment.LEADING)
                                .addComponent(this.spinnerBrightness, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(this.spinnerContrast, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(this.buttonTable, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(this.textFieldSpecialValue, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(this.buttonSpecialValueColor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(this.checkBoxIsSpecialValueTransparent));

        grouplayout.setVerticalGroup(grouplayout.createSequentialGroup()
                .addGroup(grouplayout.createParallelGroup(Alignment.CENTER)
                        .addComponent(this.labelBrightness)
                        .addComponent(this.spinnerBrightness, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(grouplayout.createParallelGroup(Alignment.CENTER)
                        .addComponent(this.labelContrast)
                        .addComponent(this.spinnerContrast, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(grouplayout.createParallelGroup(Alignment.CENTER)
                        .addComponent(this.labelColorTable)
                        .addComponent(this.buttonTable, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_HEIGHT, GroupLayout.PREFERRED_SIZE))
                .addGroup(grouplayout.createParallelGroup(Alignment.CENTER)
                        .addComponent(this.labelSpecialValue)
                        .addComponent(this.textFieldSpecialValue, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(grouplayout.createParallelGroup(Alignment.CENTER)
                        .addComponent(this.labelSpecialValueColor)
                        .addComponent(this.buttonSpecialValueColor, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_HEIGHT, GroupLayout.PREFERRED_SIZE))
                .addComponent(this.checkBoxIsSpecialValueTransparent));
        // @formatter:on
        setComponentName();
    }

    private void setComponentName() {
        ComponentUIUtilities.setName(this.labelBrightness, "LayerGridParamPropertyControl_labelBrightness");
        ComponentUIUtilities.setName(this.spinnerBrightness, "LayerGridParamPropertyControl_spinnerBrightness");
        ComponentUIUtilities.setName(this.labelContrast, "LayerGridParamPropertyControl_labelContrast");
        ComponentUIUtilities.setName(this.spinnerContrast, "LayerGridParamPropertyControl_spinnerContrast");
        ComponentUIUtilities.setName(this.labelColorTable, "LayerGridParamPropertyControl_labelColorTable");
        ComponentUIUtilities.setName(this.labelSpecialValue, "LayerGridParamPropertyControl_labelSpecialValue");
        ComponentUIUtilities.setName(this.textFieldSpecialValue, "LayerGridParamPropertyControl_textFieldSpecialValue");
        ComponentUIUtilities.setName(this.labelSpecialValueColor, "LayerGridParamPropertyControl_labelSpecialValueColor");
        ComponentUIUtilities.setName(this.buttonSpecialValueColor, "LayerGridParamPropertyControl_buttonSpecialValueColor");
        ComponentUIUtilities.setName(this.checkBoxIsSpecialValueTransparent, "LayerGridParamPropertyControl_checkBoxIsSpecialValueTransparent");
        ComponentUIUtilities.setName(this.buttonTable, "LayerGridParamPropertyControl_buttonTable");
    }

    @Override
    protected void initializeResources() {
        ((TitledBorder) this.getBorder()).setTitle(MapViewProperties.getString("String_LayerProperty_Grid"));
        this.labelBrightness.setText(MapViewProperties.getString("String_Brightness"));
        this.labelContrast.setText(MapViewProperties.getString("String_Contrast"));
        this.labelColorTable.setText(MapViewProperties.getString("String_LayerControl_Grid_ColorTable"));
        this.labelSpecialValue.setText(MapViewProperties.getString("String_LayerControl_Grid_SpacialValue"));
        this.labelSpecialValueColor.setText(MapViewProperties.getString("String_LayerControl_Grid_SetSpecialRasterColor"));
        this.checkBoxIsSpecialValueTransparent.setText(MapViewProperties.getString("String_LayerControl_Grid_SpecialValueTransparency"));
        this.buttonTable.setText(CommonProperties.getString("String_Button_Setting"));
    }

    @Override
    protected void fillComponents() {
        if (getLayerPropertyModel() != null) {
            LayerPropertyControlUtilties.setSpinnerValue(this.spinnerBrightness, getLayerPropertyModel().getBrightness());
            LayerPropertyControlUtilties.setSpinnerValue(this.spinnerContrast, getLayerPropertyModel().getContrast());
            this.textFieldSpecialValue.setValue(getLayerPropertyModel().getSpecialValue());
            this.buttonSpecialValueColor.setColor(getLayerPropertyModel().getSpecialValueColor());
            this.checkBoxIsSpecialValueTransparent.setSelectedEx(getLayerPropertyModel().isSpecialValueTransparent());
        }
    }

    @Override
    protected void registerEvents() {
        this.spinnerBrightness.addChangeListener(this.spinnerValueChangeListener);
        this.spinnerContrast.addChangeListener(this.spinnerValueChangeListener);
        this.textFieldSpecialValue.addPropertyChangeListener(PROPERTY_VALUE, this.propertyChangeListener);
        this.checkBoxIsSpecialValueTransparent.addStateChangeListener(this.checkBoxStateChangeListener);
        this.buttonSpecialValueColor.addPropertyChangeListener(ComponentDropDown.CHANGECOLOR, propertyChangeListener);
        this.buttonTable.addActionListener(this.buttonTableListener);
    }

    @Override
    protected void unregisterEvents() {
        this.spinnerBrightness.removeChangeListener(this.spinnerValueChangeListener);
        this.spinnerContrast.removeChangeListener(this.spinnerValueChangeListener);
        this.textFieldSpecialValue.removePropertyChangeListener(PROPERTY_VALUE, this.propertyChangeListener);
        this.checkBoxIsSpecialValueTransparent.removeStateChangeListener(this.checkBoxStateChangeListener);
        this.buttonSpecialValueColor.removePropertyChangeListener(ComponentDropDown.CHANGECOLOR, propertyChangeListener);
        this.buttonTable.removeActionListener(this.buttonTableListener);
    }

    private void spinnerBrightnessValueChanged() {
        getModifiedLayerPropertyModel().setBrightness(LayerPropertyControlUtilties.getSpinnerValue(this.spinnerBrightness));
        checkChanged();
    }

    private void spinnerContrastValueChanged() {
        getModifiedLayerPropertyModel().setContrast(LayerPropertyControlUtilties.getSpinnerValue(this.spinnerContrast));
        checkChanged();
    }

    private void textFieldValuePropertyChanged() {
        getModifiedLayerPropertyModel().setSpecialValue(Double.valueOf(this.textFieldSpecialValue.getValue().toString()));
        checkChanged();
    }

    private void checkBoxIsSpecialValueTransCheckedChanged() {
        getModifiedLayerPropertyModel().setSpecialValueTransparent(this.checkBoxIsSpecialValueTransparent.isSelectedEx());
        checkChanged();
    }

    private void buttonSpecialValueColorColorChange() {
        getModifiedLayerPropertyModel().setSpecialValueColor(this.buttonSpecialValueColor.getColor());
        checkChanged();
    }

    @Override
    protected void setControlEnabled(String propertyName, boolean enabled) {
        // TODO Auto-generated method stub

    }
}
