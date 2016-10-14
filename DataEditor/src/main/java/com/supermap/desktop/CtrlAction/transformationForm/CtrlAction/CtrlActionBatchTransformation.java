package com.supermap.desktop.CtrlAction.transformationForm.CtrlAction;

import com.supermap.data.Dataset;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.transformationForm.Dialogs.JDialogTransformation;
import com.supermap.desktop.CtrlAction.transformationForm.TransformationUtilties;
import com.supermap.desktop.CtrlAction.transformationForm.beans.TransformationAddObjectBean;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

import java.util.ArrayList;

/**
 * 批量配准
 * @author XiaJT
 */
public class CtrlActionBatchTransformation extends CtrlAction {
	public CtrlActionBatchTransformation(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		JDialogTransformation jDialogTransformation = new JDialogTransformation();
		Dataset[] activeDatasets = Application.getActiveApplication().getActiveDatasets();
		ArrayList<TransformationAddObjectBean> transformationAddObjectBeen = new ArrayList<>();

		if (activeDatasets.length > 0) {
			Datasource defaultDatasource = TransformationUtilties.getDefaultDatasource(activeDatasets[0].getDatasource());
			for (Dataset activeDataset : activeDatasets) {
				if (TransformationUtilties.isSupportDatasetType(activeDataset.getType())) {
					String datasetName = null;
					if (defaultDatasource != null) {
						datasetName = defaultDatasource.getDatasets().getAvailableDatasetName(activeDataset.getName() + "_adjust");
					}
					transformationAddObjectBeen.add(new TransformationAddObjectBean(activeDataset, defaultDatasource,
							datasetName));
				}
			}
		} else {
			Datasource[] activeDatasources = Application.getActiveApplication().getActiveDatasources();
			if (activeDatasources.length > 0) {
				for (Datasource activeDatasource : activeDatasources) {
					Datasource defaultDatasource = TransformationUtilties.getDefaultDatasource(activeDatasource);
					Datasets datasets = activeDatasource.getDatasets();
					for (int i = 0; i < datasets.getCount(); i++) {
						Dataset activeDataset = datasets.get(i);
						String datasetName = null;
						if (defaultDatasource != null) {
							datasetName = defaultDatasource.getDatasets().getAvailableDatasetName(activeDataset.getName() + "_adjust");
						}
						if (TransformationUtilties.isSupportDatasetType(activeDataset.getType())) {
							transformationAddObjectBeen.add(new TransformationAddObjectBean(activeDataset, defaultDatasource, datasetName));
						}
					}
				}
			}
		}
		if (transformationAddObjectBeen.size() > 0) {
			jDialogTransformation.addBeans(transformationAddObjectBeen.toArray(new TransformationAddObjectBean[transformationAddObjectBeen.size()]));
		}
		jDialogTransformation.showDialog();
	}

	@Override
	public boolean enable() {
		Workspace workspace = Application.getActiveApplication().getWorkspace();
		Datasources datasources = workspace.getDatasources();
		for (int count = datasources.getCount() - 1; count >= 0; count--) {
			if (datasources.get(count).isOpened() && !datasources.get(count).isReadOnly()) {
				return true;
			}
		}
		return false;
	}
}
