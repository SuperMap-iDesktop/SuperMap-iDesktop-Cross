package com.supermap.desktop.core.recordset;

import java.util.HashMap;
import java.util.Map;

import com.supermap.data.CursorType;
import com.supermap.data.EditHistory;
import com.supermap.data.EditType;
import com.supermap.data.Geometry;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.utilties.ArrayUtilties;

// 时间紧迫，先这样，理应有更为严谨美观的结构
public class RecordsetSet {
	private Recordset m_recordset;
	private EditHistory m_editHistory;
	private Map<Integer, Geometry> m_setHistorys;

	public RecordsetSet(Recordset recordset, EditHistory editHistory) {
		this.m_recordset = recordset;
		this.m_editHistory = editHistory;
		this.m_setHistorys = new HashMap<Integer, Geometry>();
	}

	public void begin() {
		this.m_setHistorys.clear();
	}

	public void update() {
		if (this.m_setHistorys.keySet().size() > 0) {
			int[] histories = ArrayUtilties.convertToInt(this.m_setHistorys.keySet().toArray(new Integer[this.m_setHistorys.keySet().size()]));
			Recordset setRecordset = this.m_recordset.getDataset().query(histories, CursorType.DYNAMIC);
			try {
				this.m_editHistory.add(EditType.MODIFY, setRecordset, false);
				setRecordset.getBatch().begin();
				setRecordset.edit();
				for (Integer key : this.m_setHistorys.keySet()) {
					setRecordset.seekID(key);
					setRecordset.edit();
					setRecordset.setGeometry(this.m_setHistorys.get(key));
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
			this.m_setHistorys.put(id, geometry);
		}
	}
}
