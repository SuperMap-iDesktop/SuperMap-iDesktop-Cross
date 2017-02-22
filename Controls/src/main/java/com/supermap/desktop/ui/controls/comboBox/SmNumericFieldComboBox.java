package com.supermap.desktop.ui.controls.comboBox;

import com.supermap.data.DatasetVector;
import com.supermap.data.FieldInfo;
import com.supermap.data.FieldType;
import com.supermap.data.JoinItems;

import java.util.ArrayList;

/**
 * Created by hanyz on 2017/2/15.
 */
public class SmNumericFieldComboBox extends SmFieldInfoComboBox {

	public SmNumericFieldComboBox() {
		super();
	}

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
}

