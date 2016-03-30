package com.supermap.desktop.core.recordset;

import java.util.ArrayList;
import java.util.List;

import com.supermap.data.CursorType;
import com.supermap.data.EditHistory;
import com.supermap.data.EditType;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.utilties.ArrayUtilties;

// 时间紧迫，先这样，理应有更为严谨美观的结构
// 批量撤销记录集的时候，需要query子记录集添加到EditHistory。
public class RecordsetDelete {
	private Recordset recordset;
	private EditHistory editHistory;
	private List<Integer> deleteHistoryIDs;

	public RecordsetDelete(Recordset recordset, EditHistory editHistory) {
		this.recordset = recordset;
		this.editHistory = editHistory;
		this.deleteHistoryIDs = new ArrayList<Integer>();
	}

	public void begin() {
		this.deleteHistoryIDs.clear();
		this.recordset.getBatch().setMaxRecordCount(2000);
		this.recordset.getBatch().begin();
	}

	public void update() {
		if (this.deleteHistoryIDs.size() > 0) {
			int[] histories = ArrayUtilties.convertToInt(this.deleteHistoryIDs.toArray(new Integer[this.deleteHistoryIDs.size()]));
			Recordset deleteRecordset = this.recordset.getDataset().query(histories, CursorType.DYNAMIC);
			try {
				this.editHistory.add(EditType.DELETE, deleteRecordset, false);

				deleteRecordset.getBatch().begin();
				while (!deleteRecordset.isEOF()) {
					deleteRecordset.delete();
				}
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
