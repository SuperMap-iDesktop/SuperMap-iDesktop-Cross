package com.supermap.desktop.ui.controls.comboBox;

import com.supermap.data.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by hanyz on 2017/2/15.
 */
public class SmNumericFieldComboBox extends SmFieldInfoComboBox {

    public SmNumericFieldComboBox(DatasetVector dataset) {
        super(dataset);
    }

    public SmNumericFieldComboBox(DatasetVector dataset, JoinItems joinItems) {
        super(dataset, joinItems);
    }

    @Override
    protected ArrayList<FieldType> getFieldIntoTypes() {
        ArrayList<FieldType> fieldTypes = new ArrayList<>();
        fieldTypes.add(FieldType.INT16);
        fieldTypes.add(FieldType.INT32);
        fieldTypes.add(FieldType.INT64);
        fieldTypes.add(FieldType.DOUBLE);
        fieldTypes.add(FieldType.SINGLE);
        return fieldTypes;
    }

    @Override
    protected boolean isLegalField(FieldInfo fieldInfo) {
        return fieldInfo.getType().equals(FieldType.INT16)
                || fieldInfo.getType().equals(FieldType.INT32)
                || fieldInfo.getType().equals(FieldType.INT64)
                || fieldInfo.getType().equals(FieldType.DOUBLE)
                || fieldInfo.getType().equals(FieldType.SINGLE);
    }


    public static void main(String[] args) {
        DatasourceConnectionInfo connectionInfo = new DatasourceConnectionInfo();
        connectionInfo.setServer("C:\\Users\\hanyz\\Desktop\\2.udb");
        connectionInfo.setEngineType(EngineType.UDB);
        Datasource datasoure = new Datasource(EngineType.UDB);
        datasoure.open(connectionInfo);
        System.out.println(datasoure.isConnected());
        DatasetVector dataset = (DatasetVector) datasoure.getDatasets().get("BaseMap_R");

        DatasetVector dataset11 = (DatasetVector) datasoure.getDatasets().get("Geomor_R");

        JoinItem joinitem = new JoinItem();
        String foreignTableName = dataset11.getName();
        // 设置连接信息类
        joinitem.setForeignTable(foreignTableName);
        joinitem.setJoinFilter("BaseMap_R.SmID=" + foreignTableName + ".SmID");
        joinitem.setJoinType(JoinType.LEFTJOIN);
        joinitem.setName("Connect");
        // 设置连接信息集合类，将连接信息加入到连接信息集合中
        JoinItems joinitems = new JoinItems();
        joinitems.add(joinitem);
        DatasetFieldInfo datasetFieldInfo = new DatasetFieldInfo(dataset, dataset.getFieldInfos().get(0));

//Geomor_R
        JFrame jFrame = new JFrame();
        jFrame.setSize(new Dimension(300, 120));
        jFrame.setLocationRelativeTo(null);
        JButton jButton = new JButton("Action");
        final SmNumericFieldComboBox smNemericFieldComboBox = new SmNumericFieldComboBox(dataset, joinitems);
        smNemericFieldComboBox.setMaximumSize(new Dimension(80, jButton.getPreferredSize().height));
        Container contentPane = jFrame.getContentPane();
        GroupLayout groupLayout = new GroupLayout(contentPane);
        jFrame.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        GroupLayout.SequentialGroup hGroup = groupLayout.createSequentialGroup();
        hGroup.addComponent(smNemericFieldComboBox).addComponent(jButton);
        groupLayout.setHorizontalGroup(hGroup);
        GroupLayout.ParallelGroup vGroup = groupLayout.createParallelGroup();
        vGroup.addComponent(smNemericFieldComboBox).addComponent(jButton);
        groupLayout.setVerticalGroup(vGroup);
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("selectItem Class:" + smNemericFieldComboBox.getSelectedItem().getClass());
                System.out.println("selectItem String:" + smNemericFieldComboBox.getSelectedItem());
                System.out.println("selectedField Class:" + smNemericFieldComboBox.getSelectedFieldInfo().getClass());
                System.out.println("selectedField String:" + smNemericFieldComboBox.getSelectedFieldInfo());
            }
        });
        jFrame.setVisible(true);
    }

}

