package com.supermap.desktop;

import com.supermap.analyst.spatialanalyst.*;
import com.supermap.data.*;

import javax.swing.event.EventListenerList;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author XiaJT
 */
public class GridAnalystSettingInstance {
	private GridAnalystSetting gridAnalystSetting;
	private TerrainAnalystSetting terrainAnalystSetting;

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

	private Object resultBounds = BoundsType.INTERSECTION;
	private Object clipBounds = null;
	private Object cellSize = CellSizeType.MIN;
	private static DatasetVector currentDataset = null;

	public static GridAnalystSettingInstance getInstance() {
		GridAnalystSettingInstance gridAnalystSettingInstance = new GridAnalystSettingInstance();
		return gridAnalystSettingInstance;
	}

	private boolean isChanged = false;

	private GridAnalystSettingInstance() {
		gridAnalystSetting = MathAnalyst.getAnalystSetting();
		terrainAnalystSetting = CalculationTerrain.getAnalystSetting();
		if (gridAnalystSetting == null) {
			gridAnalystSetting = new GridAnalystSetting();
		}
		if (terrainAnalystSetting == null) {
			terrainAnalystSetting = new TerrainAnalystSetting();
		}
		resultBounds = gridAnalystSetting.getBoundsType();
		if (resultBounds == BoundsType.CUSTOM) {
			resultBounds = gridAnalystSetting.getBounds();
		}
		clipBounds = gridAnalystSetting.getValidRegion();
		if (clipBounds != null) {
			clipBounds = currentDataset;
		}
		cellSize = gridAnalystSetting.getCellSizeType();
		if (cellSize == CellSizeType.CUSTOM) {
			cellSize = (Double) gridAnalystSetting.getCellSize();
		}
	}

	public void run() {
		if (isChanged) {
			if (clipBounds != null && clipBounds instanceof DatasetVector && clipBounds != currentDataset) {
				currentDataset = (DatasetVector) clipBounds;
				setSettingClipRegion();
			}
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
		if (resultBounds == null || resultBounds == this.resultBounds) {
			return;
		}
		isChanged = true;
		Object oldValue = this.resultBounds;
		this.resultBounds = resultBounds;
		firePropertyChangedListener(new PropertyChangeEvent(this, RESULT_BOUNDS, oldValue, this.resultBounds));
	}

	public void setCellSize(Object cellSize) {
		if (cellSize == null || cellSize == this.cellSize) {
			return;
		}
		isChanged = true;
		Object oldValue = this.cellSize;
		this.cellSize = cellSize;

		firePropertyChangedListener(new PropertyChangeEvent(this, CELL_SIZE, oldValue, this.cellSize));
	}

	public void setClipBounds(Object clipBounds) {
		if ((clipBounds != null && !(clipBounds instanceof DatasetVector)) || clipBounds == this.clipBounds) {
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
