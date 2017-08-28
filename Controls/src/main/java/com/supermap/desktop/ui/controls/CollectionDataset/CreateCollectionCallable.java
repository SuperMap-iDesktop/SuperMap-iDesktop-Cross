package com.supermap.desktop.ui.controls.CollectionDataset;

import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.DatasetVectorInfo;
import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilities.CharsetUtilities;

import javax.swing.*;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by xie on 2017/8/16.
 */
public class CreateCollectionCallable extends UpdateProgressCallable {
	private JDialogCreateCollectionDataset parent;

	public CreateCollectionCallable(JDialogCreateCollectionDataset parent) {
		this.parent = parent;
	}

	@Override
	public Boolean call() throws Exception {
		final Datasource datasource = parent.datasourceComboBox.getSelectedDatasource();
		try {
			DatasetVectorInfo info = new DatasetVectorInfo();
			String datasetName = datasource.getDatasets().getAvailableDatasetName(parent.textFieldDatasetName.getText());
			info.setName(datasetName);
			info.setType(DatasetType.VECTORCOLLECTION);
			DatasetVector vector = datasource.getDatasets().create(info);
			vector.setCharset(CharsetUtilities.valueOf(parent.charsetComboBox.getSelectedItem().toString()));
			ArrayList<DatasetInfo> datasetInfos = parent.tableModel.getDatasetInfos();
			if (0 == datasetInfos.size()) {
				updateProgressTotal(100, "success", 100, "success");
			}
			for (int i = 0, size = datasetInfos.size(); i < size; i++) {
				if ((vector.GetSubCollectionDatasetType() == DatasetType.UNKNOWN) || (vector.GetSubCollectionDatasetType() == datasetInfos.get(i).getDataset().getType())) {
					boolean result = vector.addCollectionDataset((DatasetVector) datasetInfos.get(i).getDataset());
					String message = "";
					if (result) {
						message = MessageFormat.format(ControlsProperties.getString("String_CollectionDatasetAddSuccess"), vector.getName(), datasetInfos.get(i).getName());

					} else {
						message = MessageFormat.format(ControlsProperties.getString("String_CollectionDatasetAddFailed"), vector.getName(), datasetInfos.get(i).getName());
					}
					Application.getActiveApplication().getOutput().output(message);
					updateProgressTotal(100, message, (int) (((i + 1.0) / size) * 100), message);
				}
			}
		} catch (Exception e) {
			//取消时异常
//			Application.getActiveApplication().getOutput().output(e);
		} finally {
			if (parent.checkBoxCloseDialog.isSelected()) {
				parent.dispose();
			}
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					UICommonToolkit.refreshSelectedDatasourceNode(datasource.getAlias());
				}
			});
		}
		return null;
	}
}
