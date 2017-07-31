package com.supermap.desktop.CtrlAction.Dataset.SpatialIndex;

import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.Interface.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.utilities.DoubleUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 动态索引参数设置界面
 *
 * @author XiaJT
 */
public class JPanelDynamicIndex extends JPanel {
	private SpatialIndexInfoPropertyListener propertyListener;

	// 基准点
	private JPanel panelBasePoint = new JPanel();
	private JLabel labelBasePoint = new JLabel();
	private JLabel labelX = new JLabel();
	private JLabel labelY = new JLabel();
	private SmTextFieldLegit textFieldX = new SmTextFieldLegit();
	private SmTextFieldLegit textFieldY = new SmTextFieldLegit();

	// 第一层网格高度
	private JLabel labelFirstGridWidth = new JLabel();
	private SmTextFieldLegit textFieldFirstGridWidth = new SmTextFieldLegit();

	// 第二层网格高度
	private JLabel labelSecondGridWidth = new JLabel();
	private SmTextFieldLegit textFieldSecondGridWidth = new SmTextFieldLegit();

	// 第三次网格高度
	private JLabel labelThirdGridWidth = new JLabel();
	private SmTextFieldLegit textFieldThirdGridWidth = new SmTextFieldLegit();

	// 说明
	private JPanelDescribe panelDescribe = new JPanelDescribe();


	public JPanelDynamicIndex() {
		initComponent();
		initLayout();
		initResources();
		addListeners();
		initComponentState();
		this.setBorder(BorderFactory.createTitledBorder(CoreProperties.getString("String_GroupBoxParameter")));
	}

	private void addListeners() {
		FocusAdapter focusAdapter = new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				fireSpatialIndexPropertyChanged(getPropertyName(e.getSource()), getPropertyValue(e.getSource()));
			}
		};
		this.textFieldX.addFocusListener(focusAdapter);
		this.textFieldY.addFocusListener(focusAdapter);
		this.textFieldFirstGridWidth.addFocusListener(focusAdapter);
		this.textFieldSecondGridWidth.addFocusListener(focusAdapter);
		this.textFieldThirdGridWidth.addFocusListener(focusAdapter);
		KeyAdapter keyAdapter = new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char keyChar = e.getKeyChar();
				if (keyChar != '-' && keyChar != '.' && (keyChar > '9' || keyChar < '0')) {
					e.consume();
				}
			}
		};
		this.textFieldX.addKeyListener(keyAdapter);
		this.textFieldY.addKeyListener(keyAdapter);
		this.textFieldFirstGridWidth.addKeyListener(keyAdapter);
		this.textFieldSecondGridWidth.addKeyListener(keyAdapter);
		this.textFieldThirdGridWidth.addKeyListener(keyAdapter);

	}

	private void fireSpatialIndexPropertyChanged(String propertyName, Object value) {
		if (this.propertyListener != null) {
			propertyListener.propertyChanged(propertyName, value);
		}
	}

	private String getPropertyName(Object source) {
		if (source == textFieldX) {
			return SpatialIndexInfoPropertyListener.GRID_X;
		} else if (source == textFieldY) {
			return SpatialIndexInfoPropertyListener.GRID_Y;
		} else if (source == textFieldFirstGridWidth) {
			return SpatialIndexInfoPropertyListener.GRID_SIZE_0;
		} else if (source == textFieldSecondGridWidth) {
			return SpatialIndexInfoPropertyListener.GRID_SIZE_1;
		} else if (source == textFieldThirdGridWidth) {
			return SpatialIndexInfoPropertyListener.GRID_SIZE_2;
		} else return null;
	}

	private Object getPropertyValue(Object source) {
		if (source == textFieldX) {
			return textFieldX.getText();
		} else if (source == textFieldY) {
			return textFieldY.getText();
		} else if (source == textFieldFirstGridWidth) {
			return textFieldFirstGridWidth.getText();
		} else if (source == textFieldSecondGridWidth) {
			return textFieldSecondGridWidth.getText();
		} else if (source == textFieldThirdGridWidth) {
			return textFieldThirdGridWidth.getText();
		} else return null;
	}

	private void initComponent() {
		ISmTextFieldLegit smTextFieldLegitPoint = new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				return DoubleUtilities.isDoubleWithoutD(textFieldValue);
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		};

		ISmTextFieldLegit smTextFieldLegit = new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				Double aDouble;
				try {
					aDouble = Double.valueOf(textFieldValue);
				} catch (NumberFormatException e) {
					return false;
				}
				return aDouble >= 0;
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		};
		textFieldX.setSmTextFieldLegit(smTextFieldLegitPoint);
		textFieldY.setSmTextFieldLegit(smTextFieldLegitPoint);
		textFieldFirstGridWidth.setSmTextFieldLegit(smTextFieldLegit);
		textFieldSecondGridWidth.setSmTextFieldLegit(smTextFieldLegit);
		textFieldThirdGridWidth.setSmTextFieldLegit(smTextFieldLegit);

		this.panelDescribe.setDescirbe("    " + CoreProperties.getString("String_MultiLevelGridIndxDescription"));
	}

	//region 布局
	private void initLayout() {
		initBasePoint();

		this.setLayout(new GridBagLayout());
		// 基准点
		this.add(panelBasePoint, new GridBagConstraintsHelper(0, 0, 1, 2).setAnchor(GridBagConstraints.CENTER).setWeight(0, 0).setFill(GridBagConstraints.BOTH).setInsets(0, 0, 5, 5));
		this.add(textFieldX, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 0));
		this.add(textFieldY, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 0));

		// 第一层网格高度
		this.add(this.labelFirstGridWidth, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(0, 0).setFill(GridBagConstraints.NONE).setInsets(0, 0, 5, 5));
		this.add(this.textFieldFirstGridWidth, new GridBagConstraintsHelper(1, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 0));

		// 第二层网格高度
		this.add(this.labelSecondGridWidth, new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(0, 0).setFill(GridBagConstraints.NONE).setInsets(0, 0, 5, 5));
		this.add(this.textFieldSecondGridWidth, new GridBagConstraintsHelper(1, 4, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 0));

		// 第三层网格高度
		this.add(this.labelThirdGridWidth, new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(0, 0).setFill(GridBagConstraints.NONE).setInsets(0, 0, 5, 5));
		this.add(this.textFieldThirdGridWidth, new GridBagConstraintsHelper(1, 5, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 0));

		// 说明
		this.add(this.panelDescribe, new GridBagConstraintsHelper(0, 6, 2, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
	}

	private void initBasePoint() {
		this.panelBasePoint.setLayout(new GridBagLayout());
		this.panelBasePoint.add(this.labelBasePoint, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 5));
		this.panelBasePoint.add(this.labelX, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).setInsets(0, 0, 5, 0));
		this.panelBasePoint.add(new JPanel(), new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 0, 5));
		this.panelBasePoint.add(this.labelY, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST));
	}


	//endregion
	private void initResources() {
		this.labelBasePoint.setText(CoreProperties.getString("String_DatasetMultiLevelGridIndexControl_GridCenter"));
		this.labelX.setText(CoreProperties.getString("String_Label_X"));
		this.labelY.setText(CoreProperties.getString("String_Label_Y"));
		this.labelFirstGridWidth.setText(CoreProperties.getString("String_DatasetMultiLevelGridIndexControl_GridSize0"));
		this.labelSecondGridWidth.setText(CoreProperties.getString("String_DatasetMultiLevelGridIndexControl_GridSize1"));
		this.labelThirdGridWidth.setText(CoreProperties.getString("String_DatasetMultiLevelGridIndexControl_GridSize2"));

	}

	private void initComponentState() {
		//
	}

	public void setX(String spatialIndexInfoX) {
		this.textFieldX.setText(spatialIndexInfoX);
	}

	public void setY(String spatialIndexInfoY) {
		this.textFieldY.setText(spatialIndexInfoY);
	}

	public void setGrid0(String spatialIndexInfoGrid0) {
		this.textFieldFirstGridWidth.setText(spatialIndexInfoGrid0);
	}

	public void setGrid1(String spatialIndexInfoGrid1) {
		this.textFieldSecondGridWidth.setText(spatialIndexInfoGrid1);
	}

	public void setGrid2(String spatialIndexInfoGrid2) {
		this.textFieldThirdGridWidth.setText(spatialIndexInfoGrid2);
	}

	public void setPropertyListener(SpatialIndexInfoPropertyListener propertyListener) {
		this.propertyListener = propertyListener;
	}

}
