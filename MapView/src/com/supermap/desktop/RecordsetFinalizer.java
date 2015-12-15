package com.supermap.desktop;

import java.util.HashMap;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetClosingEvent;
import com.supermap.data.DatasetClosingListener;
import com.supermap.data.DatasetVector;
import com.supermap.data.Recordset;

// @formatter:off
/**
 * Recordset 的统一管理终结器，后续完善可以封装一个专门的 Recordset 类，用来替代 Recordset 在桌面中的操作
 * Recordset 需要在其 Dataset 关闭之前关闭，否则会导致对象内存混乱，从而导致崩溃
 * 
 * @author highsad
 *
 */
// @formatter:on
public class RecordsetFinalizer implements DatasetClosingListener {

	public static final RecordsetFinalizer INSTANCE = new RecordsetFinalizer();

	private HashMap<DatasetVector, Recordset> finalizerMap = new HashMap<DatasetVector, Recordset>();

	private RecordsetFinalizer() {

	}

	/**
	 * 查询一个 Recordset，并交给 RecordsetFinalizer 管理
	 * 
	 * @param datasetVector
	 * @param smIDs
	 * @param cursorType
	 * @return
	 */
	public Recordset queryRecordset(DatasetVector datasetVector, int[] smIDs, CursorType cursorType) {
		Recordset recordset = null;

		if (datasetVector == null || smIDs == null || smIDs.length == 0) {
			return null;
		}

		if (!this.finalizerMap.containsKey(datasetVector)) {
			datasetVector.addClosingListener(this);
			this.finalizerMap.put(datasetVector, null);
		}

		Recordset oldRecordset = this.finalizerMap.get(datasetVector);
		if (oldRecordset != null && !oldRecordset.isClosed()) {
			oldRecordset.close();
			oldRecordset.dispose();
		}

		recordset = datasetVector.query(smIDs, cursorType);
		this.finalizerMap.put(datasetVector, recordset);
		return recordset;
	}

	/**
	 * 检查指定数据集的 Recordset 是否有被释放
	 * 
	 * @param datasetVector
	 */
	public void verifyRecordsets(DatasetVector datasetVector) {
		if (this.finalizerMap.containsKey(datasetVector)) {

			Recordset recordset = this.finalizerMap.get(datasetVector);

			if (recordset.isClosed()) {
				this.finalizerMap.remove(datasetVector);
				datasetVector.removeClosingListener(this);
			}
		}
	}

	/**
	 * 关闭指定的 Recordset
	 * 
	 * @param recordset
	 */
	public void closeRecordset(Recordset recordset) {
		if (recordset == null || recordset.isClosed() || recordset.getDataset() == null) {
			return;
		}

		DatasetVector datasetVector = recordset.getDataset();
		if (this.finalizerMap.containsKey(datasetVector) && this.finalizerMap.get(datasetVector) == recordset) {
			this.finalizerMap.remove(datasetVector);
			datasetVector.removeClosingListener(this);
		}

		if (!recordset.isClosed()) {
			recordset.close();
			recordset.dispose();
		}
	}

	/*
	 * 关闭数据集之前需要释放该数据集的所有 Recordset
	 * 
	 * @see com.supermap.data.DatasetClosingListener#datasetClosing(com.supermap.data.DatasetClosingEvent)
	 */
	@Override
	public void datasetClosing(DatasetClosingEvent arg0) {
		if (arg0.getCancel()) {
			return;
		}

		DatasetVector datasetVector = (DatasetVector) arg0.getDataset();
		try {
			if (this.finalizerMap.containsKey(datasetVector)) {
				Recordset recordset = this.finalizerMap.get(datasetVector);

				if (!recordset.isClosed()) {
					recordset.close();
					recordset.dispose();
				}
				this.finalizerMap.remove(datasetVector);
			}

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			datasetVector.removeClosingListener(this);
		}
	}
}
