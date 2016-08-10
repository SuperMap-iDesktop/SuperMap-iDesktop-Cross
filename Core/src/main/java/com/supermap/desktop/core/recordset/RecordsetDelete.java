package com.supermap.desktop.core.recordset;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.utilities.ArrayUtilities;

import java.util.ArrayList;
import java.util.List;

// 时间紧迫，先这样，理应有更为严谨美观的结构
// 批量撤销记录集的时候，需要query子记录集添加到EditHistory。
public class RecordsetDelete {
	private DatasetVector datasetVector;
	private EditHistory editHistory;
	private List<Integer> deleteHistoryIDs;

	public RecordsetDelete(DatasetVector datasetVector, EditHistory editHistory) {
		this.datasetVector = datasetVector;
		this.editHistory = editHistory;
		this.deleteHistoryIDs = new ArrayList<Integer>();
	}

	public void begin() {
		this.deleteHistoryIDs.clear();
	}

	public void update() {
		if (this.deleteHistoryIDs.size() > 0) {
			int[] histories = ArrayUtilities.convertToInt(this.deleteHistoryIDs.toArray(new Integer[this.deleteHistoryIDs.size()]));
			Recordset deleteRecordset = this.datasetVector.query(histories, CursorType.DYNAMIC);
			try {
				this.editHistory.add(EditType.DELETE, deleteRecordset, false);

				deleteRecordset.getBatch().setMaxRecordCount(2000);
				deleteRecordset.getBatch().begin();

				// 可能是组件缺陷，待验证
				// while (!deleteRecordset.isEOF()) {
				// deleteRecordset.delete();
				// }
				deleteRecordset.deleteAll();
				deleteRecordset.getBatch().update();
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			} finally {
				if (deleteRecordset != null) {
					deleteRecordset.close();
					deleteRecordset.dispose();
					deleteRecordset = null;
				}
			}
		}
	}

	public void delete(int id) {
		if (id > -1) {
			this.deleteHistoryIDs.add(id);
		}
	}
}
