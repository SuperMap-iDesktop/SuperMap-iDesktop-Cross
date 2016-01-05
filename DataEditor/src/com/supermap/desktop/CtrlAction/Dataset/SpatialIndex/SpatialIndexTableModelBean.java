package com.supermap.desktop.CtrlAction.Dataset.SpatialIndex;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.SpatialIndexInfo;
import com.supermap.data.SpatialIndexType;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.dataeditor.DataEditorProperties;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 空间分析表格中的单行数据
 *
 * @author XiaJT.
 */
public class SpatialIndexTableModelBean {
	Dataset dataset;
	SpatialIndexType spatialIndexType;
	SpatialIndexInfo spatialIndexInfo;

	public static final String[] SUPPORT_DATASET_TYPES = new String[]{
			CommonToolkit.DatasetTypeWrap.findName(DatasetType.POINT),
			CommonToolkit.DatasetTypeWrap.findName(DatasetType.LINE),
			CommonToolkit.DatasetTypeWrap.findName(DatasetType.REGION),
			CommonToolkit.DatasetTypeWrap.findName(DatasetType.TEXT),
			CommonToolkit.DatasetTypeWrap.findName(DatasetType.CAD),
			CommonToolkit.DatasetTypeWrap.findName(DatasetType.LINEM),
			CommonToolkit.DatasetTypeWrap.findName(DatasetType.NETWORK),
			CommonToolkit.DatasetTypeWrap.findName(DatasetType.NETWORK3D),
			CommonToolkit.DatasetTypeWrap.findName(DatasetType.POINT3D)};

	public SpatialIndexTableModelBean(Dataset dataset) {
		this.setDataset(dataset);
		this.setSpatialIndexType(((DatasetVector) dataset).getSpatialIndexType());
		this.setSpatialIndexInfo(new SpatialIndexInfo());
	}

	private static List<DatasetType> getSupportDatasetTypeList() {
		List<DatasetType> result = new ArrayList<>();
		for (String supportDatasetType : SpatialIndexTableModelBean.SUPPORT_DATASET_TYPES) {
			result.add(CommonToolkit.DatasetTypeWrap.findType(supportDatasetType));
		}
		return result;
	}

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public SpatialIndexType getSpatialIndexType() {
		return spatialIndexType;
	}

	public void setSpatialIndexType(SpatialIndexType spatialIndexType) {
		this.spatialIndexType = spatialIndexType;
		if (spatialIndexType == SpatialIndexType.MULTI_LEVEL_GRID || spatialIndexType == SpatialIndexType.TILE) {
			this.spatialIndexInfo.setType(spatialIndexType);
		}
	}

	public SpatialIndexInfo getSpatialIndexInfo() {
		return spatialIndexInfo;
	}

	public void setSpatialIndexInfo(SpatialIndexInfo spatialIndexInfo) {
		this.spatialIndexInfo = spatialIndexInfo;
	}

	public static boolean isSupportDatasetType(DatasetType type) {

		return getSupportDatasetTypeList().contains(type);
	}

	public void dispose() {
		if (this.spatialIndexInfo != null) {
			this.spatialIndexInfo.dispose();
		}
		this.dataset = null;
		this.spatialIndexType = null;
		this.spatialIndexInfo = null;
	}

	/**
	 * 写入
	 */
	public void bulid() {
		boolean result;
		if (spatialIndexType == SpatialIndexType.NONE || spatialIndexType == SpatialIndexType.RTREE || spatialIndexType == SpatialIndexType.QTREE) {
			result = ((DatasetVector) dataset).buildSpatialIndex(spatialIndexType);
		} else {
			result = ((DatasetVector) dataset).buildSpatialIndex(spatialIndexInfo);
		}

		String message;
		if (result) {
			if (spatialIndexType == SpatialIndexType.NONE) {
				message = MessageFormat.format(DataEditorProperties.getString("String_DatasetDeleteIndex_Success"), dataset.getName());
			} else {
				message = MessageFormat.format(DataEditorProperties.getString("String_DatasetCreateIndex_Success"), dataset.getName());

			}
		} else if (((DatasetVector) dataset).getRecordCount() < 1000) {
			message = MessageFormat.format(DataEditorProperties.getString("String_BuildSpatialIndex_Error"), dataset.getName());
		} else {
			if (spatialIndexType == SpatialIndexType.NONE) {
				message = MessageFormat.format(DataEditorProperties.getString("String_Message_DeleteSpatialIndexFailed"), dataset.getName());
			} else {
				message = MessageFormat.format(DataEditorProperties.getString("String_Message_CreateSpatialIndexFailed"), dataset.getName());
			}
		}
		Application.getActiveApplication().getOutput().output(message);
	}
}
