package com.supermap.desktop.controls.property;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetImage;
import com.supermap.data.DatasetImageCollection;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IProperty;
import com.supermap.desktop.controls.property.dataset.DatasetPrjCoordSysHandle;
import com.supermap.desktop.controls.property.dataset.DatasetPropertyControl;
import com.supermap.desktop.controls.property.dataset.GridPropertyControl;
import com.supermap.desktop.controls.property.dataset.ImageCollectionPropertyControl;
import com.supermap.desktop.controls.property.dataset.ImagePropertyControl;
import com.supermap.desktop.controls.property.dataset.RecordsetPropertyControl;
import com.supermap.desktop.controls.property.dataset.VectorPropertyControl;
import com.supermap.desktop.controls.property.datasource.DatasourceInfoControl;
import com.supermap.desktop.controls.property.datasource.DatasourcePrjCoordSysHandle;
import com.supermap.desktop.controls.property.datasource.DatasourcePropertyControl;
import com.supermap.desktop.controls.property.workspace.WorkspacePropertyControl;
import com.supermap.desktop.controls.utilities.NodeDataTypeUtilities;
import com.supermap.desktop.ui.controls.NodeDataType;
import com.supermap.desktop.ui.controls.TreeNodeData;

import java.util.ArrayList;

public class WorkspaceTreeDataPropertyFactory {
	private static WorkspacePropertyControl workspacePropertyControl;
	private static DatasourcePropertyControl datasourcePropertyControl;
	private static DatasourceInfoControl datasourceInfoControl;
	private static PrjCoordSysPropertyControl prjCoordSysPropertyControl;
	private static DatasetPropertyControl datasetPropertyControl;
	private static VectorPropertyControl vectorPropertyControl;
	private static GridPropertyControl gridPropertyControl;
	private static ImagePropertyControl imagePropertyControl;
	private static ImageCollectionPropertyControl imageCollectionPropertyControl;
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
					properties.add(getDatasourceInfoControl((Datasource) data.getData()));
					properties.add(getPrjCoordSysPropertyControl(new DatasourcePrjCoordSysHandle((Datasource) data.getData()), covert));
				} else if (NodeDataTypeUtilities.isNodeDataset(data.getType()) && data.getData() instanceof Dataset) {
					Dataset dataset = (Dataset) data.getData();
					properties = getDatasetProperties(dataset);
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return properties.toArray(new AbstractPropertyControl[properties.size()]);
	}

	public static ArrayList<IProperty> getDatasetProperties(Dataset dataset) {
		ArrayList<IProperty> propertiesTemp = new ArrayList<>();

		propertiesTemp.add(getDatasetPropertyControl(dataset));
		if (dataset instanceof DatasetVector) {
			DatasetVector datasetVector = (DatasetVector) dataset;
			propertiesTemp.add(getVectorPropertyControl(datasetVector));
			propertiesTemp.add(getRecordsetPropertyControl(datasetVector));

		} else if (dataset instanceof DatasetGrid) {
			propertiesTemp.add(getGridPropertyControl((DatasetGrid) dataset));
		} else if (dataset instanceof DatasetImageCollection) {
			propertiesTemp.add(getImageCollectionPropertyControl((DatasetImageCollection) dataset));
		} else if (dataset instanceof DatasetImage) {

			// 过滤掉 WMS 以及 WCS的影像数据集属性
			if (dataset.getType() != DatasetType.WMS && dataset.getType() != DatasetType.WCS) {
				propertiesTemp.add(getImagePropertyControl((DatasetImage) dataset));
			}
		}
		boolean covert = !dataset.isReadOnly();
		if (!DatasetType.TABULAR.equals(dataset.getType())) {
			propertiesTemp.add(getPrjCoordSysPropertyControl(new DatasetPrjCoordSysHandle(dataset), covert));
		}
		return propertiesTemp;
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

	private static DatasourceInfoControl getDatasourceInfoControl(Datasource datasource) {
		if (datasourceInfoControl == null) {
			datasourceInfoControl = new DatasourceInfoControl(datasource);
		} else {
			datasourceInfoControl.setDatasource(datasource);
		}
		return datasourceInfoControl;
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

	private static ImageCollectionPropertyControl getImageCollectionPropertyControl(DatasetImageCollection datasetImageCollection) {
		if (imageCollectionPropertyControl == null) {
			imageCollectionPropertyControl = new ImageCollectionPropertyControl(datasetImageCollection);
		} else {
			imageCollectionPropertyControl.setDatasetImageCollection(datasetImageCollection);
		}

		return imageCollectionPropertyControl;
	}


	public static RecordsetPropertyControl getRecordsetPropertyControl(DatasetVector datasetVector) {
		if (recordsetPropertyControl == null) {
			recordsetPropertyControl = new RecordsetPropertyControl(datasetVector);
		} else {
			recordsetPropertyControl.setDatasetVector(datasetVector);
		}

		return recordsetPropertyControl;
	}
}
