package com.supermap.desktop.ui.controls.CollectionDataset;

import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.DatasetVectorInfo;
import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.properties.CommonProperties;
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
			DatasetVector vector = null;
			if (null != parent.getDatasetVector()) {
				vector = parent.getDatasetVector();
			} else {
				vector = datasource.getDatasets().create(info);
//				vector.setCharset(CharsetUtilities.valueOf(parent.charsetComboBox.getSelectedItem().toString()));
				parent.setDatasetVector(vector);
			}
			ArrayList<DatasetInfo> datasetInfos = parent.tableModel.getDatasetInfos();
			if (0 == datasetInfos.size()) {
				updateProgressTotal(100, "success", 100, "success");
			}
			int size = datasetInfos.size();
			int collecionSize = vector.getCollectionDatasetCount();
			int count = 0;
			for (int i = 0; i < size; i++) {
				if (null != vector && parent.hasDataset(vector, datasetInfos.get(i).getDataset())) {
					//需不需要添加已经存在的数据集？
//					Application.getActiveApplication().getOutput().output(MessageFormat.format(CommonProperties.getString("String_DatasetExistInCollection"), datasetInfos.get(i).getDataset().getName(), vector.getName()));
					continue;
				}
				if ((vector.GetSubCollectionDatasetType() == DatasetType.UNKNOWN) || (vector.GetSubCollectionDatasetType() == datasetInfos.get(i).getDataset().getType())) {

					boolean result = vector.addCollectionDataset((DatasetVector) datasetInfos.get(i).getDataset());
					String message = "";
					if (result) {
//						parent.tableDatasetDisplay.setValueAt(CommonProperties.getString("String_Status_Exist"), i, 1);
						message = MessageFormat.format(ControlsProperties.getString("String_CollectionDatasetAddSuccess"), vector.getName(), datasetInfos.get(i).getName());
					} else {
//						parent.tableDatasetDisplay.setValueAt(CommonProperties.getString("String_AppendFailed"), i, 1);
						message = MessageFormat.format(ControlsProperties.getString("String_CollectionDatasetAddFailed"), vector.getName(), datasetInfos.get(i).getName());
					}
					parent.tableDatasetDisplay.repaint();
					Application.getActiveApplication().getOutput().output(message);
					updateProgressTotal(100, message, (int) (((count + 1.0) / (size - collecionSize)) * 100), message);
					count++;
				}
			}
		} catch (Exception e) {
			//取消时异常
			Application.getActiveApplication().getOutput().output(e);
		} finally {
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
