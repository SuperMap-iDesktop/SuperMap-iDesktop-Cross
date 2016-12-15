package com.supermap.desktop.editHistory;


import com.supermap.data.Recordset;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.Interface.ITabularEditHistory;
import com.supermap.desktop.beans.EditHistoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XiaJT
 */
public class TabularEditHistory implements ITabularEditHistory {
	private List<EditHistoryBean> editHistoryBeanJList = new ArrayList<>();

	@Override
	public void addEditHistoryBean(EditHistoryBean editHistoryBean) {
		editHistoryBeanJList.add(editHistoryBean);
	}

	@Override
	public void redo(IFormTabular tabular) {
		if (editHistoryBeanJList.size() <= 0) {
			return;
		}
		Recordset recordset = tabular.getRecordset();
		int id = recordset.getID();
		recordset.getBatch().setMaxRecordCount(5000);
		recordset.getBatch().begin();
		int[] smIds = new int[editHistoryBeanJList.size()];
		for (int i = 0; i < editHistoryBeanJList.size(); i++) {
			EditHistoryBean editHistoryBean = editHistoryBeanJList.get(i);
			smIds[i] = editHistoryBean.getSmId();
			if (recordset.seekID(editHistoryBean.getSmId())) {
				recordset.setFieldValue(editHistoryBean.getFieldName(), editHistoryBean.getAfterValue());
			}
		}
		recordset.getBatch().update();
		recordset.seekID(id);
		tabular.setSelectedCellBySmIDs(smIds, editHistoryBeanJList.get(0).getFieldName());
	}

	@Override
	public void undo(IFormTabular tabular) {
		Recordset recordset = tabular.getRecordset();
		int id = recordset.getID();
		recordset.getBatch().setMaxRecordCount(5000);
		recordset.getBatch().begin();
		int[] smIds = new int[editHistoryBeanJList.size()];
		for (int i = 0; i < editHistoryBeanJList.size(); i++) {
			EditHistoryBean editHistoryBean = editHistoryBeanJList.get(i);
			smIds[i] = editHistoryBean.getSmId();
			if (recordset.seekID(editHistoryBean.getSmId())) {
				recordset.setFieldValue(editHistoryBean.getFieldName(), editHistoryBean.getBeforeValue());
			}
		}
		recordset.getBatch().update();
		recordset.seekID(id);
		tabular.setSelectedCellBySmIDs(smIds, editHistoryBeanJList.get(0).getFieldName());
	}

	@Override
	public int getCount() {
		return editHistoryBeanJList.size();
	}
}
