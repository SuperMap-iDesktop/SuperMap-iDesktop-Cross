package com.supermap.desktop.ui.controls.borderPanel;

import com.supermap.analyst.spatialanalyst.BufferRadiusUnit;
import com.supermap.data.*;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.enums.LengthUnit;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.comboBox.ComboBoxLengthUnit;
import com.supermap.desktop.ui.controls.comboBox.SmNumericFieldComboBox;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by hanyz on 2017/2/20.
 */
public class SmRadiusPanel extends JPanel {
    private String panelTitle;
    private String labelFieldText;
    private String labelUnitText;
    private JLabel labelField;
    private JLabel labelUnit;
    private JPanel panelContainer;
    private JComboBox<LengthUnit> comboBoxLengthUnit;
    private SmNumericFieldComboBox numericFieldComboBox;
    public static int hgGapSize = 15;
    public static int vGapSize = 10;
    private DatasetVector dataset;
    private JoinItems joinItems;

    public SmRadiusPanel(DatasetVector dataset, String panelTitle) {
        this(dataset, null, panelTitle);
    }

    public SmRadiusPanel(DatasetVector dataset, JoinItems joinItems, String panelTitle) {
        super();
        this.dataset = dataset;
        this.panelTitle = panelTitle;
        this.joinItems = joinItems;
        init();
    }

    private void init() {
        initComponent();
        initLayout();
    }

    private void initComponent() {
        panelContainer = new JPanel();
        labelUnitText = ControlsProperties.getString("String_ProjectionInfoControl_LabelGeographyUnit");
        labelFieldText = ControlsProperties.getString("String_GeometryPropertySpacialInfoControl_LabelGeometryLength");
        labelField = new JLabel(labelFieldText);
        labelUnit = new JLabel(labelUnitText);
        numericFieldComboBox = new SmNumericFieldComboBox(dataset, joinItems);
        numericFieldComboBox.setMaximumSize(new Dimension(numericFieldComboBox.getWidth(), 23));
        comboBoxLengthUnit = new ComboBoxLengthUnit();
        comboBoxLengthUnit.setMaximumSize(new Dimension(comboBoxLengthUnit.getWidth(), 23));
        TitledBorder border = new TitledBorder(panelTitle);
        this.setBorder(border);
    }

    private void initLayout() {
        //内部JPanel布局
        GroupLayout groupLayout = new GroupLayout(panelContainer);
        panelContainer.setLayout(groupLayout);
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);
        GroupLayout.SequentialGroup vGroup = groupLayout.createSequentialGroup();
        vGroup
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(labelUnit)
                        .addComponent(comboBoxLengthUnit))
                .addGap(vGapSize, vGapSize, vGapSize)
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(labelField)
                        .addComponent(numericFieldComboBox));
        groupLayout.setVerticalGroup(vGroup);
        GroupLayout.SequentialGroup hGroup = groupLayout.createSequentialGroup();
        hGroup
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(labelUnit)
                        .addComponent(labelField))
                .addGap(hgGapSize, hgGapSize, hgGapSize)
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(comboBoxLengthUnit, 5, 5, Short.MAX_VALUE)
                        .addComponent(numericFieldComboBox, 5, 5, Short.MAX_VALUE));
        groupLayout.setHorizontalGroup(hGroup);
        //内部JPanel添加到当前带有border的JPanel
        this.setLayout(new GridBagLayout());
        this.add(panelContainer, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(3, 2, 3, 2));
    }

    /**
     * 获取选中的单位
     */
    public LengthUnit getUnit() {
        return ((LengthUnit) this.comboBoxLengthUnit.getSelectedItem());
    }

    /**
     * 获取缓冲半径单位
     * @return 组件类型BufferRadiusUnit 对象
     */
    public BufferRadiusUnit getBufferRadiusUnit() {
        LengthUnit lengthUnit = (LengthUnit) this.comboBoxLengthUnit.getSelectedItem();
        return getBufferRadiusUnit(lengthUnit.getUnit());
    }

    private BufferRadiusUnit getBufferRadiusUnit(Unit unitName) {
        if (Unit.MILIMETER.equals(unitName)) {
            return BufferRadiusUnit.MiliMeter;
        } else if (Unit.CENTIMETER.equals(unitName)) {
            return BufferRadiusUnit.CentiMeter;
        } else if (Unit.DECIMETER.equals(unitName)) {
            return BufferRadiusUnit.DeciMeter;
        } else if (Unit.METER.equals(unitName)) {
            return BufferRadiusUnit.Meter;
        } else if (Unit.KILOMETER.equals(unitName)) {
            return BufferRadiusUnit.KiloMeter;
        } else if (Unit.INCH.equals(unitName)) {
            return BufferRadiusUnit.Inch;
        } else if (Unit.FOOT.equals(unitName)) {
            return BufferRadiusUnit.Foot;
        } else if (Unit.MILE.equals(unitName)) {
            return BufferRadiusUnit.Mile;
        } else if (Unit.YARD.equals(unitName)) {
            return BufferRadiusUnit.Yard;
        }
        return null;
    }

    /**
     * 获取选中的长度字段
     *
     * @return FieldInfo or String表达式
     */
    public Object getFieldInfo() {
        return this.numericFieldComboBox.getSelectedFieldInfo();
    }

    /**
     * 获取选中的长度字段
     *
     * @return 原生的选中对象，可能是DatasetFieldInfo（其中包含了数据集信息）
     */
    public Object getFieldSelectedItem() {
        return this.numericFieldComboBox.getSelectedItem();
    }

	public void setDefaultFieldValue(Object value) {
		if (value == null)
			return;
		this.numericFieldComboBox.setSelectedObject(value);
	}

    public void setLabelFieldText(String labelFieldText) {
        this.labelFieldText = labelFieldText;
        if (labelField != null) {
            labelField.setText(labelFieldText);
        }
    }

    public void setLabelUnitText(String labelUnitText) {
        this.labelUnitText = labelUnitText;
        if (labelUnit != null) {
            labelUnit.setText(labelUnitText);
        }
    }

    public void setDataset(DatasetVector dataset) {
        this.dataset = dataset;
        numericFieldComboBox.setDataset(dataset);
    }

    public static void main(String[] args) {
        DatasourceConnectionInfo connectionInfo = new DatasourceConnectionInfo();
        connectionInfo.setServer("C:\\Users\\hanyz\\Desktop\\2.udb");
        connectionInfo.setEngineType(EngineType.UDB);
        Datasource datasoure = new Datasource(EngineType.UDB);
        datasoure.open(connectionInfo);
        System.out.println("UDB connected:" + datasoure.isConnected());
        DatasetVector dataset = (DatasetVector) datasoure.getDatasets().get("BaseMap_R");

        JFrame jFrame = new JFrame();
        jFrame.setSize(new Dimension(350, 200));
        jFrame.setLocationRelativeTo(null);
        final SmRadiusPanel radiusPanel = new SmRadiusPanel(dataset, "buffer radius");
	    radiusPanel.setDefaultFieldValue(null);
	    radiusPanel.setLabelUnitText("radius:");
        JButton jButton = new JButton("Get");
        jFrame.getContentPane().setLayout(new GridBagLayout());
        jFrame.getContentPane().add(radiusPanel, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.NORTH));
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LengthUnit unit = radiusPanel.getUnit();
                BufferRadiusUnit bufferRadiusUnit = radiusPanel.getBufferRadiusUnit();
                Object fieldInfo = radiusPanel.getFieldInfo();
                Object fieldSelectedItem = radiusPanel.getFieldSelectedItem();
                System.out.println(unit);
                System.out.println(bufferRadiusUnit);
                System.out.println(fieldInfo);
                System.out.println(fieldSelectedItem);
            }
        });
        final DatasetVector dataset11 = (DatasetVector) datasoure.getDatasets().get("Geomor_R");
        JButton jButton1 = new JButton("Change");
        jButton1.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {
                radiusPanel.setDataset(dataset11);
            }
        });
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridBagLayout());
        jPanel.add(jButton, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.SOUTHEAST).setInsets(3, 0, 3, 3));
        jPanel.add(jButton1, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.SOUTHEAST).setInsets(3, 0, 3, 3));
        jFrame.getContentPane().add(jPanel, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.SOUTHEAST).setInsets(3, 0, 3, 3));
        jFrame.setVisible(true);

    }
}
