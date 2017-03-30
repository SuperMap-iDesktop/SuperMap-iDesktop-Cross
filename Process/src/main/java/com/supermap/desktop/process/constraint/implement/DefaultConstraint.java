package com.supermap.desktop.process.constraint.implement;

import com.supermap.desktop.process.constraint.interfaces.IConstraint;
import com.supermap.desktop.process.parameter.events.ParameterValueLegalEvent;
import com.supermap.desktop.process.parameter.events.ParameterValueLegalListener;
import com.supermap.desktop.process.parameter.events.ParameterValueSelectedEvent;
import com.supermap.desktop.process.parameter.implement.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameter;

import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class DefaultConstraint implements IConstraint, ParameterValueLegalListener {

	protected ArrayList<ParameterNode> parameterNodes = new ArrayList<>();

	@Override
	public final boolean isValueLegal(ParameterValueLegalEvent event) {
		return true;
	}

	@Override
	public Object isValueSelected(ParameterValueSelectedEvent event) {
		return AbstractParameter.DO_NOT_CARE;
	}

	protected boolean isValueLegalHook(ParameterValueLegalEvent event) {
		return true;
	}

	@Override
	public final void constrained(IParameter parameter, String name) {
		parameterNodes.add(new ParameterNode(parameter, name));
		parameter.addValueLegalListener(this);
		constrainedHook(parameter, name);
	}

	protected void constrainedHook(IParameter parameter, String name) {

	}

	protected class ParameterNode {
		private IParameter parameter;
		private String name;

		public ParameterNode(IParameter parameter, String name) {
			this.parameter = parameter;
			this.name = name;
		}

		public IParameter getParameter() {
			return parameter;
		}

		public void setParameter(IParameter parameter) {
			this.parameter = parameter;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
