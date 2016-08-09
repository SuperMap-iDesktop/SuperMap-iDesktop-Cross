package com.supermap.desktop.core.recordset;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.utilities.ArrayUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// 时间紧迫，先这样，理应有更为严谨美观的结构
public class RecordsetAddNew {
	private Recordset recordset;
	private EditHistory editHistory;
	private List<Integer> addHistoryIDs;

	// 用在增加了新纪录时候，ID开出去用于用户对新记录集的操作（比如添加到selection中）
	public List<Integer> getAddHistoryIDs() {
		return this.addHistoryIDs;
	}

	public RecordsetAddNew(Recordset recordset, EditHistory editHistory) {
		this.recordset = recordset;
		this.editHistory = editHistory;
		this.addHistoryIDs = new ArrayList<Integer>();
	}

	public void begin() {
		this.addHistoryIDs.clear();
		this.recordset.getBatch().setMaxRecordCount(2000);
		this.recordset.getBatch().begin();
	}

	public void update() {
		this.recordset.getBatch().update();
		if (this.addHistoryIDs.size() > 0) {
			int[] histories = ArrayUtilities.convertToInt(this.addHistoryIDs.toArray(new Integer[this.addHistoryIDs.size()]));
			Recordset addHistoryRecordset = this.recordset.getDataset().query(histories, CursorType.DYNAMIC);
			try {
				this.editHistory.add(EditType.ADDNEW, addHistoryRecordset, false);
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			} finally {
				if (addHistoryRecordset != null) {
					addHistoryRecordset.close();
					addHistoryRecordset.dispose();
					addHistoryRecordset = null;
				}
			}
		}
	}

	public boolean addNew(Geometry geometry) {
		boolean result = false;

		if (this.recordset != null) {
			if (this.recordset.addNew(geometry)) {
				result = true;
				int addID = this.recordset.getID();
				if (addID > -1) {
					this.addHistoryIDs.add(addID);
				}
			}
		}

		return result;
	}

	public boolean addNew(Geometry geometry, Map<String, Object> values) {
		Boolean result = false;

		if (this.recordset != null) {
			if (this.recordset.addNew(geometry, values)) {
				result = true;
				int addID = this.recordset.getID();
				if (addID > -1) {
					this.addHistoryIDs.add(addID);
				}
			}
		}

		return result;
	}
}
