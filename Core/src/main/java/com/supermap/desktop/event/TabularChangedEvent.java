package com.supermap.desktop.event;

import com.supermap.desktop.beans.EditHistoryBean;
import com.supermap.desktop.enums.TabularChangedType;

/**
 * @author XiaJT
 */
public class TabularChangedEvent {
	private EditHistoryBean editHistoryBean = new EditHistoryBean();
	private TabularChangedType tabularChangedType;

	public int getSmId() {
		return editHistoryBean.getSmId();
	}

	public void setSmId(int smId) {
		editHistoryBean.setSmId(smId);
	}

	public String getFieldName() {
		return editHistoryBean.getFieldName();
	}

	public void setFieldName(String fieldName) {
		editHistoryBean.setFieldName(fieldName);
	}

	public Object getBeforeValue() {
		return editHistoryBean.getBeforeValue();
	}

	public void setBeforeValue(Object beforeValue) {
		editHistoryBean.setBeforeValue(beforeValue);
	}

	public Object getAfterValue() {
		return editHistoryBean.getAfterValue();
	}

	public void setAfterValue(Object afterValue) {
		editHistoryBean.setAfterValue(afterValue);
	}

	public TabularChangedType getTabularChangedType() {
		return tabularChangedType;
	}

	public void setTabularChangedType(TabularChangedType tabularChangedType) {
		this.tabularChangedType = tabularChangedType;
	}

	public EditHistoryBean getEditHistoryBean() {
		return editHistoryBean;
	}
}
