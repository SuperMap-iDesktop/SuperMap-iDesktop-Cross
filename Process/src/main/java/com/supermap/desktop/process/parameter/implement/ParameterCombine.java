package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.ParameterPanels.DefaultParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 聚合IParameter的面板,聚合JComponent面板的请用ParameterUserDefine或自定义类型
 *
 * @author XiaJT
 */
public class ParameterCombine extends AbstractParameter {
	// Q: 添加的关联事件如何保存？
	ArrayList<IParameter> parameters = new ArrayList<>();
	public static final String HORIZONTAL = "PARAMETER_COMBINE_TYPE_HORIZONTAL";
	public static final String VERTICAL = "PARAMETER_COMBINE_TYPE_VERTICAL";
	private String combineType = VERTICAL;
	private int weightIndex = -1;

	public ParameterCombine() {

	}

	public ParameterCombine(String combineType) {
		this.combineType = combineType;
	}


	public ParameterCombine addParameters(IParameter... parameters) {
		for (IParameter parameter : parameters) {
			if (this.parameters.indexOf(parameter) == -1) {
				this.parameters.add(parameter);
			}
		}
		return this;
	}

	public void addParameters(int weightIndex, IParameter... parameters) {
		this.weightIndex = weightIndex;
		Collections.addAll(this.parameters, parameters);
	}

	public void setWeightIndex(int weightIndex) {
		this.weightIndex = weightIndex;
	}

	@Override
	public String getType() {
		return ParameterType.COMBINE;
	}

	@Override
	public IParameterPanel getParameterPanel() {
		if (panel == null) {
			buildPanel();
		}
		return panel;
	}

	@Override
	public String getDescribe() {
		return "Combine";
	}

	public void reBuildPanel() {
		buildPanel();
		((Component) panel.getPanel()).revalidate();
		((Component) panel.getPanel()).repaint();
	}

	public int removeParameter(IParameter parameter) {
		int index;
		if ((index = parameters.indexOf(parameter)) != -1) {
			if (panel != null) {
				((Container) panel.getPanel()).remove(((Component) parameter.getParameterPanel().getPanel()));
			}
			parameters.remove(parameter);
		}
		return index;
	}

	private void buildPanel() {
		int x = 0, y = 0;
		if (panel != null) {
			((Container) panel.getPanel()).removeAll();
		} else {
			panel = new DefaultParameterPanel();
			((JPanel) panel).setLayout(new GridBagLayout());
		}
		for (IParameter parameter : parameters) {
			int weightX = combineType.equals(VERTICAL) ? 1 : (weightIndex == -1 || weightIndex == x ? 1 : 0);
			int weightY = combineType.equals(HORIZONTAL) ? 1 : (weightIndex == -1 || weightIndex == y ? 1 : 0);
			((JPanel) panel).add(((JPanel) parameter.getParameterPanel().getPanel()), new GridBagConstraintsHelper(x, y, 1, 1).setWeight(weightX, weightY).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.BOTH).setInsets(y > 0 ? 5 : 0, x > 0 ? 5 : 0, 0, 0));
			if (combineType.equals(VERTICAL)) {
				y++;
			} else {
				x++;
			}
		}
	}

}
