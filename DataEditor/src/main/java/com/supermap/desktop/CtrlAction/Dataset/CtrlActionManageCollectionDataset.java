package com.supermap.desktop.CtrlAction.Dataset;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.CollectionDataset.DatasetInfo;
import com.supermap.desktop.ui.controls.CollectionDataset.JDialogCreateCollectionDataset;
import com.supermap.desktop.utilities.DatasourceUtilities;

import java.util.ArrayList;

/**
 * Created by xie on 2017/8/22.
 * 管理矢量数据集集合功能
 */
public class CtrlActionManageCollectionDataset extends CtrlAction {
	public CtrlActionManageCollectionDataset(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	protected void run() {
		DatasetVector datasetVector = (DatasetVector) Application.getActiveApplication().getActiveDatasets()[0];
		ArrayList<DatasetInfo> datasetInfos = new ArrayList<>();
		ArrayList<CollectionDatasetInfo> collectionDatasetInfos = datasetVector.getCollectionDatasetInfos();
		for (int i = 0; i < collectionDatasetInfos.size(); i++) {
			CollectionDatasetInfo collectionDatasetInfo = collectionDatasetInfos.get(i);
			DatasourceConnectionInfo info = collectionDatasetInfo.getDatasourceConnectInfo();
			DatasetInfo datasetInfo = new DatasetInfo();
			datasetInfo.setName(collectionDatasetInfo.getDatasetName());
			datasetInfo.setCapiton(collectionDatasetInfo.getDatasetName());
			datasetInfo.setServer(info.getServer());
			datasetInfo.setEngineType(info.getEngineType().name());
			datasetInfo.setDatasourceAlias(info.getAlias());
			datasetInfo.setUser(info.getUser());
			datasetInfos.add(datasetInfo);
		}
		JDialogCreateCollectionDataset createCollectionDataset = new JDialogCreateCollectionDataset(0, datasetVector, datasetInfos);
		createCollectionDataset.isSetDatasetCollectionCount(true);
		createCollectionDataset.showDialog();
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		if (null != Application.getActiveApplication().getActiveDatasets() && Application.getActiveApplication().getActiveDatasets().length > 0) {
			DatasetVector datasetVector = (DatasetVector) Application.getActiveApplication().getActiveDatasets()[0];
			if (datasetVector.getType() == DatasetType.VECTORCOLLECTION) {
				enable = true;
			}
		}
		return enable;
	}
}
