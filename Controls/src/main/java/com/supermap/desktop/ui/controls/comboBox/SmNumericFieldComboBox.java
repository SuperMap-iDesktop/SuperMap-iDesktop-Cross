package com.supermap.desktop.ui.controls.comboBox;

import com.supermap.data.DatasetVector;
import com.supermap.data.FieldInfo;
import com.supermap.data.FieldType;
import com.supermap.data.JoinItems;
import com.supermap.desktop.utilities.StringUtilities;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;

/**
 * Created by hanyz on 2017/2/15.
 */
public class SmNumericFieldComboBox extends SmFieldInfoComboBox {
	public static final int DEFAULT_BUFFERRADIUS = 10;

	public SmNumericFieldComboBox() {
		super();
//		// 添加键盘监听事件，控制其键盘输入的字符内容--yuanR 2017.3.7
//		this.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyTyped(KeyEvent e) {
//				char keyChar = e.getKeyChar();
//				if ((keyChar != '.' && keyChar > '9') || (keyChar != '.' && keyChar < '0')) {
//					e.consume();
//				}
//			}
//		});

		// 添加焦点监听事件，当焦点离开时，若其内容为空，则给予默认值--yuanR 2017.3.3
		this.getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (StringUtilities.isNullOrEmpty(getEditor().getItem().toString())) {
					setSelectedItem(DEFAULT_BUFFERRADIUS);
				}
			}
		});
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

