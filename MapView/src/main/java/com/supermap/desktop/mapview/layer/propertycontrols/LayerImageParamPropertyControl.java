package com.supermap.desktop.mapview.layer.propertycontrols;

import com.supermap.data.ColorSpaceType;
import com.supermap.desktop.DefaultValues;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.mapview.layer.propertymodel.LayerImageParamPropertyModel;
import com.supermap.desktop.ui.SMSpinner;
import com.supermap.desktop.ui.StateChangeEvent;
import com.supermap.desktop.ui.StateChangeListener;
import com.supermap.desktop.ui.TristateCheckBox;
import com.supermap.desktop.ui.controls.ComponentDropDown;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LayerImageParamPropertyControl extends AbstractLayerPropertyControl {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private JLabel labelBrightness; // 亮度
    private JLabel labelContrast; // 对比度
    private TristateCheckBox checkBoxIsTransparent; // 透明色
    private JLabel labelTransparentColorTolerance; // 透明色容限
    private JLabel labelDisplayColorSpace; // 颜色模式

    private SMSpinner spinnerBrightness;
    private SMSpinner spinnerContrast;
    private ComponentDropDown buttonTransparentColor;
    private SMSpinner spinnerTransColorTolerance;
    private JComboBox<ColorSpaceType> comboBoxDisplayColorSpace;

    private ChangeListener spinnerChangeListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            if (e.getSource() == spinnerBrightness) {
                spinnerBrightnessValueChanged();
            } else if (e.getSource() == spinnerContrast) {
                spinnerContrastValueChanged();
            } else if (e.getSource() == spinnerTransColorTolerance) {
                spinnerTransparentColorToleranceValueChanged();
            }
        }
    };
    private StateChangeListener checkBoxStateChangeListener = new StateChangeListener() {

        @Override
        public void stateChange(StateChangeEvent e) {
            if (e.getSource() == checkBoxIsTransparent) {
                checkBoxIsTransparentCheckedChanged();
            }
        }
    };
    private ItemListener comboBoxItemListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getSource() == comboBoxDisplayColorSpace) {
                comboBoxDisplayColorSpaceSelectedChanged(e);
            }
        }
    };

    private PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            colorButtonSelectedColorChange();
        }
    };

    public LayerImageParamPropertyControl() {
        // TODO
    }

    @Override
    public LayerImageParamPropertyModel getLayerPropertyModel() {
        return (LayerImageParamPropertyModel) super.getLayerPropertyModel();
    }

    @Override
    public LayerImageParamPropertyModel getModifiedLayerPropertyModel() {
        return (LayerImageParamPropertyModel) super.getModifiedLayerPropertyModel();
    }

    @Override
    protected void initializeComponents() {
        this.setBorder(BorderFactory.createTitledBorder("ImageParameter"));

        this.labelBrightness = new JLabel("Brightness:");
        this.spinnerBrightness = new SMSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        this.labelContrast = new JLabel("Contrast:");
        this.spinnerContrast = new SMSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        this.checkBoxIsTransparent = new TristateCheckBox("TransparentColor");
        this.buttonTransparentColor = new ComponentDropDown(ComponentDropDown.COLOR_TYPE);
        this.labelTransparentColorTolerance = new JLabel("TransparentColorTolerance:");
        this.spinnerTransColorTolerance = new SMSpinner(new SpinnerNumberModel(0, DefaultValues.NONE_VALUE, 255, 1));
        this.labelDisplayColorSpace = new JLabel("DisplayColorSpace:");
        this.comboBoxDisplayColorSpace = new JComboBox<ColorSpaceType>();

        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);
        this.setLayout(groupLayout);

        // @formatter:off
        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addComponent(this.labelBrightness, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, GroupLayout.PREFERRED_SIZE)
                        .addComponent(this.labelContrast, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, GroupLayout.PREFERRED_SIZE)
                        .addComponent(this.checkBoxIsTransparent, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, GroupLayout.PREFERRED_SIZE)
                        .addComponent(this.labelTransparentColorTolerance, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, GroupLayout.PREFERRED_SIZE)
                        .addComponent(this.labelDisplayColorSpace, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addComponent(this.spinnerBrightness, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)
                        .addComponent(this.spinnerContrast, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)
                        .addComponent(this.buttonTransparentColor, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)
                        .addComponent(this.spinnerTransColorTolerance, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)
                        .addComponent(this.comboBoxDisplayColorSpace, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)));

        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
                        .addComponent(this.labelBrightness)
                        .addComponent(this.spinnerBrightness, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
                        .addComponent(this.labelContrast)
                        .addComponent(this.spinnerContrast, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
                        .addComponent(this.checkBoxIsTransparent, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(this.buttonTransparentColor, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_HEIGHT, GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
                        .addComponent(this.labelTransparentColorTolerance)
                        .addComponent(this.spinnerTransColorTolerance, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
                        .addComponent(this.labelDisplayColorSpace)
                        .addComponent(this.comboBoxDisplayColorSpace, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));
        // @formatter:on
        setComponentName();
    }

    private void setComponentName() {
        ComponentUIUtilities.setName(this.labelBrightness, "LayerImageParamPropertyControl_labelBrightness");
        ComponentUIUtilities.setName(this.labelContrast, "LayerImageParamPropertyControl_labelContrast");
        ComponentUIUtilities.setName(this.checkBoxIsTransparent, "LayerImageParamPropertyControl_checkBoxIsTransparent");
        ComponentUIUtilities.setName(this.labelTransparentColorTolerance, "LayerImageParamPropertyControl_labelTransparentColorTolerance");
        ComponentUIUtilities.setName(this.labelDisplayColorSpace, "LayerImageParamPropertyControl_labelDisplayColorSpace");
        ComponentUIUtilities.setName(this.spinnerBrightness, "LayerImageParamPropertyControl_spinnerBrightness");
        ComponentUIUtilities.setName(this.spinnerContrast, "LayerImageParamPropertyControl_spinnerContrast");
        ComponentUIUtilities.setName(this.buttonTransparentColor, "LayerImageParamPropertyControl_buttonTransparentColor");
        ComponentUIUtilities.setName(this.spinnerTransColorTolerance, "LayerImageParamPropertyControl_spinnerTransColorTolerance");
        ComponentUIUtilities.setName(this.comboBoxDisplayColorSpace, "LayerImageParamPropertyControl_comboBoxDisplayColorSpace");
    }

    @Override
    protected void initializeResources() {
        ((TitledBorder) this.getBorder()).setTitle(MapViewProperties.getString("String_LayerProperty_Image"));
        this.labelBrightness.setText(MapViewProperties.getString("String_Brightness"));
        this.labelContrast.setText(MapViewProperties.getString("String_Contrast"));
        this.checkBoxIsTransparent.setText(MapViewProperties.getString("String_LayerControl_Grid_TransparentColor"));
        this.labelTransparentColorTolerance.setText(MapViewProperties.getString("String_LayerControl_Grid_TransparentColorTolerance"));
        this.labelDisplayColorSpace.setText(MapViewProperties.getString("String_Label_ColorMode"));
    }

    @Override
    protected void fillComponents() {
        if (getLayerPropertyModel() != null) {
            LayerPropertyControlUtilties.setSpinnerValue(this.spinnerBrightness, getLayerPropertyModel().getBrightness());
            LayerPropertyControlUtilties.setSpinnerValue(this.spinnerContrast, getLayerPropertyModel().getContrast());
            this.checkBoxIsTransparent.setSelectedEx(getLayerPropertyModel().isTransparent());
            this.buttonTransparentColor.setColor(getLayerPropertyModel().getTransparentColor());
            LayerPropertyControlUtilties.setSpinnerValue(this.spinnerTransColorTolerance, getLayerPropertyModel().getTransparentColorTolerance());
            fillComboBoxDisplayColorSpace();
            this.comboBoxDisplayColorSpace.setSelectedItem(getLayerPropertyModel().getDisplayColorSpace());
        }
    }

    @Override
    protected void registerEvents() {
        this.spinnerBrightness.addChangeListener(this.spinnerChangeListener);
        this.spinnerContrast.addChangeListener(this.spinnerChangeListener);
        this.spinnerTransColorTolerance.addChangeListener(this.spinnerChangeListener);
        this.checkBoxIsTransparent.addStateChangeListener(this.checkBoxStateChangeListener);
        this.comboBoxDisplayColorSpace.addItemListener(this.comboBoxItemListener);
        this.buttonTransparentColor.addPropertyChangeListener(ComponentDropDown.CHANGECOLOR, this.propertyChangeListener);
    }

    @Override
    protected void unregisterEvents() {
        this.spinnerBrightness.removeChangeListener(this.spinnerChangeListener);
        this.spinnerContrast.removeChangeListener(this.spinnerChangeListener);
        this.spinnerTransColorTolerance.removeChangeListener(this.spinnerChangeListener);
        this.checkBoxIsTransparent.removeStateChangeListener(this.checkBoxStateChangeListener);
        this.comboBoxDisplayColorSpace.removeItemListener(this.comboBoxItemListener);
        this.buttonTransparentColor.removePropertyChangeListener(ComponentDropDown.CHANGECOLOR, this.propertyChangeListener);
    }

    @Override
    protected void setControlEnabled(String propertyName, boolean enabled) {
        if (propertyName.equals(LayerImageParamPropertyModel.BRIGHTNESS)) {
            this.spinnerBrightness.setEnabled(enabled);
        } else if (propertyName.equals(LayerImageParamPropertyModel.CONTRAST)) {
            this.spinnerContrast.setEnabled(enabled);
        } else if (propertyName.equals(LayerImageParamPropertyModel.IS_TRANSPARENT)) {
            this.checkBoxIsTransparent.setEnabled(enabled);
        } else if (propertyName.equals(LayerImageParamPropertyModel.TRANSPARENT_COLOR)) {
            this.buttonTransparentColor.setEnabled(enabled);
        } else if (propertyName.equals(LayerImageParamPropertyModel.TRANSPARENT_COLOR_TOLERANCE)) {
            this.spinnerTransColorTolerance.setEnabled(enabled);
        } else if (propertyName.equals(LayerImageParamPropertyModel.DISPLAY_COLOR_SPACE)) {
            this.comboBoxDisplayColorSpace.setEnabled(enabled);
        }
    }

    private void fillComboBoxDisplayColorSpace() {
        this.comboBoxDisplayColorSpace.removeAllItems();
        this.comboBoxDisplayColorSpace.addItem(ColorSpaceType.CMY);
        this.comboBoxDisplayColorSpace.addItem(ColorSpaceType.CMYK);
        this.comboBoxDisplayColorSpace.addItem(ColorSpaceType.RGB);
        this.comboBoxDisplayColorSpace.addItem(ColorSpaceType.RGBA);
        this.comboBoxDisplayColorSpace.addItem(ColorSpaceType.YCC);
        this.comboBoxDisplayColorSpace.addItem(ColorSpaceType.YIQ);
        this.comboBoxDisplayColorSpace.addItem(ColorSpaceType.YUV);
    }

    private void spinnerBrightnessValueChanged() {
        getModifiedLayerPropertyModel().setBrightness(LayerPropertyControlUtilties.getSpinnerValue(this.spinnerBrightness));
        checkChanged();
    }

    private void spinnerContrastValueChanged() {
        getModifiedLayerPropertyModel().setContrast(LayerPropertyControlUtilties.getSpinnerValue(this.spinnerContrast));
        checkChanged();
    }

    private void spinnerTransparentColorToleranceValueChanged() {
        getModifiedLayerPropertyModel().setTransparentColorTolerance(LayerPropertyControlUtilties.getSpinnerValue(this.spinnerTransColorTolerance));
        checkChanged();
    }

    private void checkBoxIsTransparentCheckedChanged() {
        getModifiedLayerPropertyModel().setTransparent(this.checkBoxIsTransparent.isSelectedEx());
        checkChanged();
    }

    private void comboBoxDisplayColorSpaceSelectedChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            getModifiedLayerPropertyModel().setDisplayColorSpace((ColorSpaceType) this.comboBoxDisplayColorSpace.getSelectedItem());
            checkChanged();
        }
    }

    private void colorButtonSelectedColorChange() {
        getModifiedLayerPropertyModel().setTransparentColor(this.buttonTransparentColor.getColor());
        checkChanged();
    }
}
