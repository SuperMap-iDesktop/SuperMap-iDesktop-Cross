package com.supermap.desktop.process.parameter.ipls;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.events.PanelPropertyChangedListener;
import com.supermap.desktop.process.parameter.events.ParameterCombineBuildPanelListener;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameters;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * 聚合IParameter的面板,聚合JComponent面板的请用ParameterUserDefine或自定义类型
 * To aggregate IParameter panels。
 * use the ParameterUserDefine or custom type for the JComponent panel
 *
 * @author XiaJT
 */
public class ParameterCombine extends AbstractParameter {
	// Q: 添加的关联事件如何保存？
	public static final String HORIZONTAL = "PARAMETER_COMBINE_TYPE_HORIZONTAL";
	public static final String VERTICAL = "PARAMETER_COMBINE_TYPE_VERTICAL";
	private String describe;
	private ArrayList<IParameter> parameterList = new ArrayList<>();
	private ArrayList<ParameterCombineBuildPanelListener> parameterCombineBuildPanelListeners = new ArrayList<>();
	private String combineType = VERTICAL;
	private int weightIndex = -1;
	private boolean rebuildEveryTime;
	private ArrayList<PropertyChangeListener> propertyChangedListeners = new ArrayList<>();

	public ParameterCombine() {

	}

	public ParameterCombine(String combineType) {
		this.combineType = combineType;
	}


	public ParameterCombine addParameters(IParameter... parameters) {
		boolean isNeedRebuild = false;
		for (IParameter parameter : parameters) {
			if (this.parameterList.indexOf(parameter) == -1) {
				addParameterToList(parameter);
				isNeedRebuild = true;
			}
		}
		if (isNeedRebuild) {
			firePanelRebuildListener();
		}
		return this;
	}

	public void addParameters(int weightIndex, IParameter... parameters) {
		this.weightIndex = weightIndex;
		for (IParameter parameter : parameters) {
			addParameterToList(parameter);
		}
		firePanelRebuildListener();
	}

	public ParameterCombine setWeightIndex(int weightIndex) {
		this.weightIndex = weightIndex;
		return this;
	}

	@Override
	public String getType() {
		return ParameterType.COMBINE;
	}

	@Override
	public void setParameters(IParameters parameters) {
		super.setParameters(parameters);
		for (IParameter iParameter : parameterList) {
			iParameter.setParameters(parameters);
		}
	}

	public void reBuildPanel() {
		buildPanel();
	}

	private void buildPanel() {
		firePanelRebuildListener();
	}

	@Override
	public String getDescribe() {
		return describe;
	}


	public void setDescribe(String describe) {
		this.describe = describe;
	}

	private void firePanelRebuildListener() {
		for (ParameterCombineBuildPanelListener parameterCombineBuildPanelListener : parameterCombineBuildPanelListeners) {
			parameterCombineBuildPanelListener.rebuild();
		}
	}

	public int removeParameter(IParameter parameter) {
		int index;
		if ((index = parameterList.indexOf(parameter)) != -1) {
			removeParameterFormList(parameter);
			firePanelRebuildListener();
		}
		return index;
	}

	private void removeParameterFormList(IParameter parameter) {
		for (PropertyChangeListener propertyChangedListener : propertyChangedListeners) {
			parameter.removePropertyListener(propertyChangedListener);
		}
		parameterList.remove(parameter);
	}

	private void addParameterToList(IParameter parameter) {
		parameterList.add(parameter);
		parameter.setRequisite(parameter.isRequisite() || isRequisite());
		for (PropertyChangeListener propertyChangedListener : propertyChangedListeners) {
			parameter.addPropertyListener(propertyChangedListener);
		}
	}

	public ArrayList<IParameter> getParameterList() {
		return parameterList;
	}

	public void setCombineType(String combineType) {
		this.combineType = combineType;
	}

	public String getCombineType() {
		return combineType;
	}

	public int getWeightIndex() {
		return weightIndex;
	}

	public void addParameterCombineBuildPanelListeners(ParameterCombineBuildPanelListener parameterCombineBuildPanelListener) {
		if (!parameterCombineBuildPanelListeners.contains(parameterCombineBuildPanelListener)) {
			parameterCombineBuildPanelListeners.add(parameterCombineBuildPanelListener);
		}
	}

	public boolean isRebuildEveryTime() {
		return rebuildEveryTime;
	}

	public void setRebuildEveryTime(boolean rebuildEveryTime) {
		this.rebuildEveryTime = rebuildEveryTime;
	}

	@Override
	public void setEnabled(boolean enabled) {
		boolean oldValue = this.isEnabled;
		if (enabled != this.isEnabled) {
			this.isEnabled = enabled;
		}
		for (IParameter parameter : parameterList) {
			parameter.setEnabled(this.isEnabled);
		}

		firePanelPropertyChangedListener(new PropertyChangeEvent(this, PanelPropertyChangedListener.ENABLE, oldValue, isEnabled));
	}

	@Override
	public void addPropertyListener(PropertyChangeListener propertyChangeListener) {
		for (IParameter parameter : parameterList) {
			parameter.addPropertyListener(propertyChangeListener);
		}
		propertyChangedListeners.add(propertyChangeListener);
	}

	@Override
	public void removePropertyListener(PropertyChangeListener propertyChangeListener) {
		for (IParameter parameter : parameterList) {
			parameter.removePropertyListener(propertyChangeListener);
		}
		propertyChangedListeners.remove(propertyChangeListener);
	}

	@Override
	public void setRequisite(boolean isRequisite) {
		super.setRequisite(isRequisite);
		for (IParameter parameter : parameterList) {
			parameter.setRequisite(parameter.isRequisite() || isRequisite);
		}
	}
}
