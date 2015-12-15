package com.supermap.desktop.controls.property;

import java.util.ArrayList;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetImage;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IProperty;
import com.supermap.desktop.controls.utilties.NodeDataTypeUtilties;
import com.supermap.desktop.controls.property.dataset.DatasetPrjCoordSysHandle;
import com.supermap.desktop.controls.property.dataset.DatasetPropertyControl;
import com.supermap.desktop.controls.property.dataset.GridPropertyControl;
import com.supermap.desktop.controls.property.dataset.ImagePropertyControl;
import com.supermap.desktop.controls.property.dataset.RecordsetPropertyControl;
import com.supermap.desktop.controls.property.dataset.VectorPropertyControl;
import com.supermap.desktop.controls.property.datasource.DatasourcePrjCoordSysHandle;
import com.supermap.desktop.controls.property.datasource.DatasourcePropertyControl;
import com.supermap.desktop.controls.property.workspace.WorkspacePropertyControl;
import com.supermap.desktop.ui.controls.NodeDataType;
import com.supermap.desktop.ui.controls.TreeNodeData;

public class WorkspaceTreeDataPropertyFactory {
	private static WorkspacePropertyControl workspacePropertyControl;
	private static DatasourcePropertyControl datasourcePropertyControl;
	private static PrjCoordSysPropertyControl prjCoordSysPropertyControl;
	private static DatasetPropertyControl datasetPropertyControl;
	private static VectorPropertyControl vectorPropertyControl;
	private static GridPropertyControl gridPropertyControl;
	private static ImagePropertyControl imagePropertyControl;
	private static RecordsetPropertyControl recordsetPropertyControl;

	private WorkspaceTreeDataPropertyFactory() {
		// 不提供构造函数
	}

	public static IProperty[] createProperties(TreeNodeData data) {
		ArrayList<IProperty> properties = new ArrayList<IProperty>();

		try {
			if (data != null) {
				if (data.getType() == NodeDataType.WORKSPACE) {
					properties.add(getWorkspacePropertyControl((Workspace) data.getData()));
				} else if (data.getType() == NodeDataType.DATASOURCE) {
					Datasource tempDatasource = (Datasource) data.getData();
					boolean covert = tempDatasource != null && !tempDatasource.isReadOnly();
					properties.add(getDatasourcePropertyControl((Datasource) data.getData()));
					properties.add(getPrjCoordSysPropertyControl(new DatasourcePrjCoordSysHandle((Datasource) data.getData()), covert));
				} else if (NodeDataTypeUtilties.isNodeDataset(data.getType())) {
					properties.add(getDatasetPropertyControl((Dataset) data.getData()));
					if (data.getType() == NodeDataType.DATASET_VECTOR) {
						properties.add(getVectorPropertyControl((DatasetVector) data.getData()));
						properties.add(getRecordsetPropertyControl((DatasetVector) data.getData()));
					} else if (data.getType() == NodeDataType.DATASET_GRID) {
						properties.add(getGridPropertyControl((DatasetGrid) data.getData()));
					} else if (data.getType() == NodeDataType.DATASET_IMAGE) {
						properties.add(getImagePropertyControl((DatasetImage) data.getData()));
					}
					boolean covert = !((Dataset) data.getData()).isReadOnly();
					if (!DatasetType.TABULAR.equals(((Dataset) data.getData()).getType())) {
						properties.add(getPrjCoordSysPropertyControl(new DatasetPrjCoordSysHandle((Dataset) data.getData()), covert));
					}
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return properties.toArray(new AbstractPropertyControl[properties.size()]);
	}

	private static WorkspacePropertyControl getWorkspacePropertyControl(Workspace workspace) {
		if (workspacePropertyControl == null) {
			workspacePropertyControl = new WorkspacePropertyControl(workspace);
		} else {
			workspacePropertyControl.setWorkspace(workspace);
		}

		return workspacePropertyControl;
	}

	private static DatasourcePropertyControl getDatasourcePropertyControl(Datasource datasource) {
		if (datasourcePropertyControl == null) {
			datasourcePropertyControl = new DatasourcePropertyControl(datasource);
		} else {
			datasourcePropertyControl.setDatasource(datasource);
		}

		return datasourcePropertyControl;
	}

	private static PrjCoordSysPropertyControl getPrjCoordSysPropertyControl(PrjCoordSysHandle prjHandle, boolean covertFlag) {
		if (prjCoordSysPropertyControl == null) {
			prjCoordSysPropertyControl = new PrjCoordSysPropertyControl(prjHandle, covertFlag);
		} else {
			prjCoordSysPropertyControl.setPrjCoordSys(prjHandle, covertFlag);
		}

		return prjCoordSysPropertyControl;
	}

	private static DatasetPropertyControl getDatasetPropertyControl(Dataset dataset) {
		if (datasetPropertyControl == null) {
			datasetPropertyControl = new DatasetPropertyControl(dataset);
		} else {
			datasetPropertyControl.setDataset(dataset);
		}

		return datasetPropertyControl;
	}

	private static VectorPropertyControl getVectorPropertyControl(DatasetVector datasetVector) {
		if (vectorPropertyControl == null) {
			vectorPropertyControl = new VectorPropertyControl(datasetVector);
		} else {
			vectorPropertyControl.setDatasetVector(datasetVector);
		}

		return vectorPropertyControl;
	}

	private static GridPropertyControl getGridPropertyControl(DatasetGrid datasetGrid) {
		if (gridPropertyControl == null) {
			gridPropertyControl = new GridPropertyControl(datasetGrid);
		} else {
			gridPropertyControl.setDatasetGrid(datasetGrid);
		}

		return gridPropertyControl;
	}

	private static ImagePropertyControl getImagePropertyControl(DatasetImage datasetImage) {
		if (imagePropertyControl == null) {
			imagePropertyControl = new ImagePropertyControl(datasetImage);
		} else {
			imagePropertyControl.setDatasetImage(datasetImage);
		}

		return imagePropertyControl;
	}

	private static RecordsetPropertyControl getRecordsetPropertyControl(DatasetVector datasetVector) {
		if (recordsetPropertyControl == null) {
			recordsetPropertyControl = new RecordsetPropertyControl(datasetVector);
		} else {
			recordsetPropertyControl.setDatasetVector(datasetVector);
		}

		return recordsetPropertyControl;
	}
}
