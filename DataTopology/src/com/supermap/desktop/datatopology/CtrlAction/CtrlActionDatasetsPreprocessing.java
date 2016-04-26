package com.supermap.desktop.datatopology.CtrlAction;

import com.supermap.data.DatasetType;
import com.supermap.data.Datasets;
import com.supermap.data.Datasources;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

import javax.swing.*;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @author Administrator 拓扑预处理
 */
public class CtrlActionDatasetsPreprocessing extends CtrlAction {

	public CtrlActionDatasetsPreprocessing(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	public void run() {
		JDialogTopoPreProgress datasetsPreprocessing = new JDialogTopoPreProgress((JFrame) Application.getActiveApplication().getMainFrame(), true);
		datasetsPreprocessing.setVisible(true);
	}

	public boolean enable() {
		boolean result = false;
		HashSet<DatasetType> datasetTypes = new HashSet<DatasetType>();
		HashSet<DatasetType> referenceDatasetTypes = new HashSet<>();
		for (int i = 0; i < 4; i++) {
			referenceDatasetTypes.add(DatasetType.LINE);
			referenceDatasetTypes.add(DatasetType.POINT);
			referenceDatasetTypes.add(DatasetType.REGION);
			referenceDatasetTypes.add(DatasetType.NETWORK);
		}
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		if (null != datasources && 0 < datasources.getCount()) {
			for (int i = 0; i < datasources.getCount(); i++) {
				Datasets datasets = datasources.get(i).getDatasets();
				for (int j = 0; j < datasets.getCount(); j++) {
					datasetTypes.add(datasets.get(j).getType());
				}
			}
		}
		Iterator<DatasetType> datasetType = datasetTypes.iterator();
		while (datasetType.hasNext()) {
			if (referenceDatasetTypes.contains(datasetType.next())) {
				result = true;
			}
		}
		return result;
	}
}
