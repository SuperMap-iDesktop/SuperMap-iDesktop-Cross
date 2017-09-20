package com.supermap.desktop.ui.controls.CollectionDataset;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilities.DatasourceUtilities;

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
			DatasetVector vector = null;
			if (null != parent.getDatasetVector()) {
				vector = parent.getDatasetVector();
			} else {
				vector = datasource.getDatasets().create(info);
//				vector.setCharset(CharsetUtilities.valueOf(parent.charsetComboBox.getSelectedItem().toString()));
			}
			ArrayList<DatasetInfo> datasetInfos = parent.tableModel.getDatasetInfos();
			if (0 == datasetInfos.size()) {
				updateProgressTotal(100, "success", 100, "success");
			}
			int size = datasetInfos.size();
			int collecionSize = vector.getCollectionDatasetCount();
			int count = 0;
			for (int i = 0; i < size; i++) {
				if (null != vector && parent.hasDataset(vector, datasetInfos.get(i).getName())) {
					//需不需要添加已经存在的数据集？
//					Application.getActiveApplication().getOutput().output(MessageFormat.format(CommonProperties.getString("String_DatasetExistInCollection"), datasetInfos.get(i).getDataset().getName(), vector.getName()));
					continue;
				}
//				if ((vector.GetSubCollectionDatasetType() == DatasetType.UNKNOWN) || (vector.GetSubCollectionDatasetType() == datasetInfos.get(i).getDataset().getType())) {
				Datasource sourceDatasource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasetInfos.get(i).getDatasourceAlias());
				if (null==sourceDatasource){
					Application.getActiveApplication().getOutput().output(CoreProperties.getString("String_GetDatasourceFailed"));
				}
				Dataset dataset = DatasourceUtilities.getDataset(datasetInfos.get(i).getName(), sourceDatasource);
				boolean result = vector.addCollectionDataset((DatasetVector) dataset);
				String message = "";
				if (result) {
					message = MessageFormat.format(ControlsProperties.getString("String_CollectionDatasetAddSuccess"), vector.getName(), datasetInfos.get(i).getName());
				} else {
					message = MessageFormat.format(ControlsProperties.getString("String_CollectionDatasetAddFailed"), vector.getName(), datasetInfos.get(i).getName());
				}
				parent.tableDatasetDisplay.repaint();
				Application.getActiveApplication().getOutput().output(message);
				updateProgressTotal(100, message, (int) (((count + 1.0) / (size - collecionSize)) * 100), message);
				count++;
//				}
			}
		} catch (Exception e) {
			//取消时异常
			Application.getActiveApplication().getOutput().output(CoreProperties.getString("String_GetDatasourceFailed"));
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
