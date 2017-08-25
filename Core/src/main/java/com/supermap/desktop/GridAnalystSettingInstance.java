package com.supermap.desktop;

import com.supermap.analyst.spatialanalyst.BoundsType;
import com.supermap.analyst.spatialanalyst.CalculationTerrain;
import com.supermap.analyst.spatialanalyst.CellSizeType;
import com.supermap.analyst.spatialanalyst.ConversionAnalyst;
import com.supermap.analyst.spatialanalyst.DistanceAnalyst;
import com.supermap.analyst.spatialanalyst.GridAnalystSetting;
import com.supermap.analyst.spatialanalyst.MathAnalyst;
import com.supermap.analyst.spatialanalyst.TerrainAnalystSetting;
import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.GeoRegion;
import com.supermap.data.Geometrist;
import com.supermap.data.Geometry;
import com.supermap.data.Recordset;
import com.supermap.data.Rectangle2D;

import javax.swing.event.EventListenerList;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author XiaJT
 */
public class GridAnalystSettingInstance {
	private static GridAnalystSettingInstance ourInstance = new GridAnalystSettingInstance();
	private GridAnalystSetting gridAnalystSetting = new GridAnalystSetting();
	private TerrainAnalystSetting terrainAnalystSetting = new TerrainAnalystSetting();

	public static final String RESULT_BOUNDS = "resultBounds";
	public static final String CLIP_BOUNDS = "clipBounds";
	public static final String CELL_SIZE = "cellSize";

	public static final String RESULT_BOUNDS_INTERSECTION = "resultBoundsIntersection";
	public static final String RESULT_BOUNDS_UNION = "resultBoundsUnion";
	public static final String RESULT_BOUNDS_CUSTOM = "resultBoundsCustom";

	public static final String CELL_SIZE_MAX = "cellSizeMax";
	public static final String CELL_SIZE_MIN = "cellSizeMin";
	public static final String CELL_SIZE_CUSTOM = "cellSizeCustom";

	private EventListenerList eventListenerList = new EventListenerList();

	private Object resultBounds = RESULT_BOUNDS_INTERSECTION;
	private Object clipBounds = null;
	private Object cellSize = CELL_SIZE_MIN;
	private DatasetVector currentDataset = null;

	public static GridAnalystSettingInstance getInstance() {
		return ourInstance;
	}

	private boolean isChanged = false;

	private GridAnalystSettingInstance() {

	}

	public void run() {
		if (isChanged) {
			if (clipBounds != null && clipBounds instanceof DatasetVector && clipBounds != currentDataset) {
				this.currentDataset = (DatasetVector) clipBounds;
				setSettingClipRegion();
			}
			MathAnalyst.setAnalystSetting(gridAnalystSetting);
			CalculationTerrain.setAnalystSetting(terrainAnalystSetting);
			DistanceAnalyst.setAnalystSetting(gridAnalystSetting);
			ConversionAnalyst.setAnalystSetting(gridAnalystSetting);
			isChanged = false;
		}
	}

	private void setSettingClipRegion() {
		GeoRegion clipRegion;
		Recordset recordset = null;
		try {
			if (clipBounds != null && clipBounds instanceof DatasetVector) {
				DatasetVector datasetVector = (DatasetVector) this.clipBounds;
				recordset = datasetVector.getRecordset(false, CursorType.STATIC);
				Geometry geometry = recordset.getGeometry();
				clipRegion = (GeoRegion) geometry;
				recordset.moveNext();
				while (!recordset.isEOF()) {
					geometry = recordset.getGeometry();
					GeoRegion geoRegion = (GeoRegion) geometry;
					GeoRegion temp = (GeoRegion) Geometrist.union(clipRegion, geoRegion);
					clipRegion.dispose();
					geoRegion.dispose();

					clipRegion = temp;
					recordset.moveNext();
				}
				if (clipRegion.getPartCount() == 0) {
					clipRegion.dispose();
				} else {
					gridAnalystSetting.setValidRegion(clipRegion);
					terrainAnalystSetting.setValidRegion(clipRegion);
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			if (recordset != null) {
				recordset.dispose();
			}
		}
	}

	public void setResultBounds(Object resultBounds) {
		if (resultBounds == null) {
			return;
		}
		isChanged = true;
		Object oldValue = this.resultBounds;
		this.resultBounds = resultBounds;
		if (resultBounds instanceof String) {
			if (resultBounds.equals(RESULT_BOUNDS_CUSTOM)) {
				gridAnalystSetting.setBoundsType(BoundsType.CUSTOM);
				terrainAnalystSetting.setBoundsType(BoundsType.CUSTOM);
			} else if (resultBounds.equals(RESULT_BOUNDS_INTERSECTION)) {
				gridAnalystSetting.setBoundsType(BoundsType.INTERSECTION);
				terrainAnalystSetting.setBoundsType(BoundsType.INTERSECTION);
			} else if (resultBounds.equals(RESULT_BOUNDS_UNION)) {
				gridAnalystSetting.setBoundsType(BoundsType.UNION);
				terrainAnalystSetting.setBoundsType(BoundsType.UNION);
			}
		} else if (resultBounds instanceof Rectangle2D) {
			gridAnalystSetting.setBoundsType(BoundsType.CUSTOM);
			terrainAnalystSetting.setBoundsType(BoundsType.CUSTOM);
			gridAnalystSetting.setBounds((Rectangle2D) resultBounds);
			terrainAnalystSetting.setBounds((Rectangle2D) resultBounds);
		} else if (resultBounds instanceof DatasetVector) {
			gridAnalystSetting.setBoundsType(BoundsType.CUSTOM);
			terrainAnalystSetting.setBoundsType(BoundsType.CUSTOM);
			gridAnalystSetting.setBounds(((DatasetVector) resultBounds).getBounds());
			terrainAnalystSetting.setBounds(((DatasetVector) resultBounds).getBounds());
		}
		firePropertyChangedListener(new PropertyChangeEvent(this, RESULT_BOUNDS, oldValue, this.resultBounds));
	}

	public void setCellSize(Object cellSize) {
		if (cellSize == null) {
			return;
		}
		isChanged = true;
		Object oldValue = this.cellSize;
		this.cellSize = cellSize;
		if (cellSize instanceof String) {
			if (cellSize.equals(CELL_SIZE_MAX)) {
				gridAnalystSetting.setCellSizeType(CellSizeType.MAX);
				terrainAnalystSetting.setCellSizeType(CellSizeType.MAX);
			} else if (cellSize.equals(CELL_SIZE_MIN)) {
				gridAnalystSetting.setCellSizeType(CellSizeType.MIN);
				terrainAnalystSetting.setCellSizeType(CellSizeType.MIN);
			} else if (cellSize.equals(CELL_SIZE_CUSTOM)) {
				gridAnalystSetting.setCellSizeType(CellSizeType.CUSTOM);
				terrainAnalystSetting.setCellSizeType(CellSizeType.CUSTOM);
			}
		} else if (cellSize instanceof Double) {
			gridAnalystSetting.setCellSizeType(CellSizeType.CUSTOM);
			terrainAnalystSetting.setCellSizeType(CellSizeType.CUSTOM);
			gridAnalystSetting.setCellSize((Double) cellSize);
			terrainAnalystSetting.setCellSize((Double) cellSize);
		}
		firePropertyChangedListener(new PropertyChangeEvent(this, CELL_SIZE, oldValue, this.cellSize));
	}

	public void setClipBounds(Object clipBounds) {
		if (clipBounds != null && !(clipBounds instanceof DatasetVector)) {
			return;
		}
		isChanged = true;
		firePropertyChangedListener(new PropertyChangeEvent(this, CLIP_BOUNDS, this.clipBounds, this.clipBounds = clipBounds));
	}

	public Object getResultBounds() {
		return resultBounds;
	}

	public Object getClipBounds() {
		return clipBounds;
	}

	public Object getCellSize() {
		return cellSize;
	}

	public void addPropertyChangedListener(PropertyChangeListener propertyChangeListener) {
		eventListenerList.add(PropertyChangeListener.class, propertyChangeListener);
	}

	public void removePropertyChangedListener(PropertyChangeListener propertyChangeListener) {
		eventListenerList.remove(PropertyChangeListener.class, propertyChangeListener);
	}

	public void firePropertyChangedListener(PropertyChangeEvent propertyChangeEvent) {
		PropertyChangeListener[] listeners = eventListenerList.getListeners(PropertyChangeListener.class);
		for (PropertyChangeListener listener : listeners) {
			listener.propertyChange(propertyChangeEvent);
		}
	}
}
