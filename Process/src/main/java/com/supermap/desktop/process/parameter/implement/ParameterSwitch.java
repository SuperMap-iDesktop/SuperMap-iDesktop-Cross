package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.utilities.StringUtilities;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class ParameterSwitch extends AbstractParameter {
	public static final String CURRENT_PARAMETER = "currentParameter";
	private ArrayList<ParameterSwitchNode> nodes = new ArrayList<>();
	@ParameterField(name = CURRENT_PARAMETER)
	private IParameter currentParameter = null;
	private String describe;

	public void add(String tag, IParameter parameter) {
		if (parameters != null) {
			parameter.setParameters(parameters);
		}
		nodes.add(new ParameterSwitchNode(tag, parameter));
		if (nodes.size() == 1) {
			currentParameter = parameter;
		}
	}

	public IParameter getParameterByIndex(int index) {
		return nodes.get(index).getParameter();
	}

	public IParameter getParameterByTag(String tag) {
		if (StringUtilities.isNullOrEmpty(tag)) {
			return null;
		}
		for (ParameterSwitchNode node : nodes) {
			if (tag.equals(node.getTag())) {
				return node.getParameter();
			}
		}
		return null;
	}


	public void switchParameter(IParameter parameter) {
		IParameter oldValue = this.currentParameter;
		currentParameter = parameter;
		firePropertyChangeListener(new PropertyChangeEvent(this, CURRENT_PARAMETER, oldValue, currentParameter));
	}

	public void switchParameter(int index) {
		switchParameter(nodes.get(index).getParameter());
	}

	public void switchParameter(String tag) {
		if (StringUtilities.isNullOrEmpty(tag)) {
			switchParameter((IParameter) null);
		}
		for (ParameterSwitchNode node : nodes) {
			if (tag.equals(node.getTag())) {
				switchParameter(node.getParameter());
				return;
			}
		}
		switchParameter((IParameter) null);
	}

	@Override
	public String getType() {
		return ParameterType.SWITCH;
	}

	@Override
	public String getDescribe() {
		return describe;
	}

	@Override
	public void setParameters(IParameters parameters) {
		super.setParameters(parameters);
		for (ParameterSwitchNode node : nodes) {
			if (node.getParameter() != null) {
				node.getParameter().setParameters(parameters);
			}
		}
	}

	public IParameter getCurrentParameter() {
		return currentParameter;
	}

	private class ParameterSwitchNode {
		private String tag;
		private IParameter parameter;

		ParameterSwitchNode(String tag, IParameter parameter) {
			this.tag = tag;
			this.parameter = parameter;
		}

		public String getTag() {
			return tag;
		}

		public void setTag(String tag) {
			this.tag = tag;
		}

		public IParameter getParameter() {
			return parameter;
		}

		public void setParameter(IParameter parameter) {
			this.parameter = parameter;
		}
	}

}
