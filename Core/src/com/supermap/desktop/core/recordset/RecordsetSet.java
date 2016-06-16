package com.supermap.desktop.core.recordset;

import java.util.HashMap;
import java.util.Map;

import com.supermap.data.CursorType;
import com.supermap.data.EditHistory;
import com.supermap.data.EditType;
import com.supermap.data.Geometry;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.utilties.ArrayUtilities;

// 时间紧迫，先这样，理应有更为严谨美观的结构
public class RecordsetSet {
	private Recordset recordset;
	private EditHistory editHistory;
	private Map<Integer, Geometry> setHistories;

	public RecordsetSet(Recordset recordset, EditHistory editHistory) {
		this.recordset = recordset;
		this.editHistory = editHistory;
		this.setHistories = new HashMap<Integer, Geometry>();
	}

	public void begin() {
		this.setHistories.clear();
	}

	public void update() {
		if (this.setHistories.keySet().size() > 0) {
			int[] histories = ArrayUtilities.convertToInt(this.setHistories.keySet().toArray(new Integer[this.setHistories.keySet().size()]));
			Recordset setRecordset = this.recordset.getDataset().query(histories, CursorType.DYNAMIC);
			try {
				this.editHistory.add(EditType.MODIFY, setRecordset, false);
				setRecordset.getBatch().begin();
				setRecordset.edit();
				for (Integer key : this.setHistories.keySet()) {
					setRecordset.seekID(key);
					setRecordset.edit();
					setRecordset.setGeometry(this.setHistories.get(key));
				}
				setRecordset.getBatch().update();
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			} finally {
				if (setRecordset != null) {
					setRecordset.close();
					setRecordset.dispose();
					setRecordset = null;
				}
			}
		}
	}

	public void setGeometry(int id, Geometry geometry) {
		if (id > -1) {
			this.setHistories.put(id, geometry);
		}
	}
}
