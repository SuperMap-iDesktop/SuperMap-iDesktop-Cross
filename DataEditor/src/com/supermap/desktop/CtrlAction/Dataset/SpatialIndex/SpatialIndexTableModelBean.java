package com.supermap.desktop.CtrlAction.Dataset.SpatialIndex;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Point2D;
import com.supermap.data.Rectangle2D;
import com.supermap.data.SpatialIndexInfo;
import com.supermap.data.SpatialIndexType;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.utilties.StringUtilties;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 空间分析表格中的单行数据
 *
 * @author XiaJT.
 */
public class SpatialIndexTableModelBean {
	private Dataset dataset;
	private SpatialIndexType spatialIndexType;
	private SpatialIndexInfo spatialIndexInfo;

	public static final String[] SUPPORT_DATASET_TYPES = new String[]{
			CommonToolkit.DatasetTypeWrap.findName(DatasetType.POINT),
			CommonToolkit.DatasetTypeWrap.findName(DatasetType.LINE),
			CommonToolkit.DatasetTypeWrap.findName(DatasetType.REGION),
			CommonToolkit.DatasetTypeWrap.findName(DatasetType.TEXT),
			CommonToolkit.DatasetTypeWrap.findName(DatasetType.CAD),
			CommonToolkit.DatasetTypeWrap.findName(DatasetType.LINEM),
			CommonToolkit.DatasetTypeWrap.findName(DatasetType.NETWORK),
			CommonToolkit.DatasetTypeWrap.findName(DatasetType.NETWORK3D),
			CommonToolkit.DatasetTypeWrap.findName(DatasetType.POINT3D)
	};

	public SpatialIndexTableModelBean(Dataset dataset) {
		this.setDataset(dataset);
		this.setSpatialIndexInfo(new SpatialIndexInfo());
		this.setSpatialIndexType(((DatasetVector) dataset).getSpatialIndexType());
		this.initSpatialIndexInfo();
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

	private void initSpatialIndexInfo() {
		// 初始一次，再不修改
		// 动态
		int objectCount = ((DatasetVector) dataset).getRecordCount();
		Rectangle2D rec = dataset.getBounds();
		int gridCount = objectCount / 200;
		if (gridCount != 0) {
			this.spatialIndexInfo.setGridSize0(Math.sqrt(rec.getWidth() * rec.getHeight() / gridCount));
		}

		if (dataset.getBounds().getHeight() / 30 > 0) {
			this.spatialIndexInfo.setTileHeight(dataset.getBounds().getHeight() / 30);
		}
		if (dataset.getBounds().getWidth() / 30 > 0) {
			this.spatialIndexInfo.setTileWidth(dataset.getBounds().getWidth() / 30);
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

	public void updateVaule(String propetName, Object value) {
		if (StringUtilties.isNullOrEmpty(String.valueOf(value))) {
			value = "0";
		}
		Double doubleValue = Double.valueOf(String.valueOf(value));
		if (doubleValue <= 0) {
			return;
		}
		if (this.getSpatialIndexInfo().getType() == SpatialIndexType.MULTI_LEVEL_GRID) {
			switch (propetName) {
				case SpatialIndexInfoPropertyListener.GRID_X:
					this.getSpatialIndexInfo().setGridCenter(new Point2D(doubleValue, this.getSpatialIndexInfo().getGridCenter().getY()));
					break;
				case SpatialIndexInfoPropertyListener.GRID_Y:
					this.getSpatialIndexInfo().setGridCenter(new Point2D(this.getSpatialIndexInfo().getGridCenter().getX(), doubleValue));
					break;
				case SpatialIndexInfoPropertyListener.GRID_SIZE_0:
					this.getSpatialIndexInfo().setGridSize0(doubleValue);
					break;
				case SpatialIndexInfoPropertyListener.GRID_SIZE_1:
					this.getSpatialIndexInfo().setGridSize1(doubleValue);
					break;
				case SpatialIndexInfoPropertyListener.GRID_SIZE_2:
					this.getSpatialIndexInfo().setGridSize2(doubleValue);
					break;
			}
		} else {
			switch (propetName) {
				case SpatialIndexInfoPropertyListener.TILE_FIELD:
					this.getSpatialIndexInfo().setTileField(String.valueOf(value));
					break;
				case SpatialIndexInfoPropertyListener.TILE_WIDTH:
					this.getSpatialIndexInfo().setTileWidth(doubleValue);
					break;
				case SpatialIndexInfoPropertyListener.TILE_HEIGHT:
					this.getSpatialIndexInfo().setTileHeight(doubleValue);
					break;
			}
		}
	}
}
