package com.supermap.desktop;

import java.util.ArrayList;
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

	private HashMap<DatasetVector, ArrayList<Recordset>> finalizerMap = new HashMap<DatasetVector, ArrayList<Recordset>>();

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

		// 先做一下检查，把一些悄悄释放掉的 recordset 移除
		verifyRecordsets(datasetVector);

		// 初始一下该数据集的 recordset(s)
		if (!this.finalizerMap.containsKey(datasetVector)) {
			datasetVector.addClosingListener(this);
			this.finalizerMap.put(datasetVector, new ArrayList<Recordset>());
		}

		// 添加监控
		recordset = datasetVector.query(smIDs, cursorType);
		this.finalizerMap.get(datasetVector).add(recordset);
		return recordset;
	}

	/**
	 * 检查指定数据集的 Recordset(s) 是否有被释放
	 * 
	 * @param datasetVector
	 */
	public void verifyRecordsets(DatasetVector datasetVector) {
		if (this.finalizerMap.containsKey(datasetVector)) {
			ArrayList<Recordset> recordsets = this.finalizerMap.get(datasetVector);

			// 检查该数据集的 Recordset 是否已关闭，如果是，则移除它
			for (int i = recordsets.size() - 1; i >= 0; i--) {
				Recordset recordset = recordsets.get(i);
				if (recordset.isClosed()) {
					recordsets.remove(i);
				}
			}

			// 如果所有的 Recordset 均已关闭，那么就从 Finalizer 中移除该数据集
			if (recordsets.size() == 0) {
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
		try {
			if (recordset == null || recordset.isClosed() || recordset.getDataset() == null) {
				return;
			}

			if (recordset.getDataset() != null) {
				DatasetVector datasetVector = recordset.getDataset();

				if (this.finalizerMap.containsKey(datasetVector) && this.finalizerMap.get(datasetVector).size() > 0) {
					ArrayList<Recordset> recordsets = this.finalizerMap.get(datasetVector);

					for (int i = recordsets.size() - 1; i >= 0; i--) {
						Recordset item = recordsets.get(i);

						if (item == recordset) {
							recordsets.remove(i);
							break;
						}
					}

					// 如果所有的 Recordset 均已关闭，那么就从 Finalizer 中移除该数据集
					if (recordsets.size() == 0) {
						this.finalizerMap.remove(datasetVector);
						datasetVector.removeClosingListener(this);
					}
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			if (recordset != null && !recordset.isClosed()) {
				recordset.close();
				recordset.dispose();
			}
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
				ArrayList<Recordset> recordsets = this.finalizerMap.get(datasetVector);

				for (int i = recordsets.size() - 1; i >= 0; i--) {
					Recordset recordset = recordsets.get(i);
					recordsets.remove(i);
					if (!recordset.isClosed()) {
						recordset.close();
						recordset.dispose();
					}
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
