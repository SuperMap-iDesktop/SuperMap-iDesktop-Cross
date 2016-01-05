package com.supermap.desktop.CtrlAction.Dataset.SpatialIndex;

import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;

/**
 * 动态索引参数设置界面
 *
 * @author XiaJT
 */
public class JPanelDynamicIndex extends JPanel {
	// 基准点
	private JPanel panelBasePoint = new JPanel();
	private JLabel labelBasePoint = new JLabel();
	private JLabel labelX = new JLabel();
	private JLabel labelY = new JLabel();
	private JTextField textFieldX = new JTextField();
	private JTextField textFieldY = new JTextField();

	// 第一层网格高度
	private JLabel labelFirstGridWidth = new JLabel();
	private JTextField textFieldFirstGridWidth = new JTextField();

	// 第二层网格高度
	private JLabel labelSecondGridWidth = new JLabel();
	private JTextField textFieldSecondGridWidth = new JTextField();

	// 第三次网格高度
	private JLabel labelThirdGridWidth = new JLabel();
	private JTextField textFieldThirdGridWidth = new JTextField();

	// 说明
	private JPanelDescribe panelDescribe = new JPanelDescribe();

	public JPanelDynamicIndex() {
		initComponent();
		initLayout();
		initResources();
		initComponentState();
		this.setBorder(BorderFactory.createTitledBorder(CoreProperties.getString("String_GroupBoxParameter")));
	}

	private void initComponent() {
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
		// TODO 加锁
		this.textFieldX.setText(spatialIndexInfoX);
	}

	public void setY(String spatialIndexInfoY) {
		// TODO 加锁
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
}
