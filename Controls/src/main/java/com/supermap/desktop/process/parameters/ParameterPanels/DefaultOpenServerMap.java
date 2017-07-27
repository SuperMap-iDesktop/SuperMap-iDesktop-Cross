package com.supermap.desktop.process.parameters.ParameterPanels;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.process.messageBus.IOpenServerMap;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.mapping.Map;

import javax.swing.*;

/**
 * Created by highsad on 2017/7/26.
 */
public class DefaultOpenServerMap implements IOpenServerMap {
	public final static IOpenServerMap INSTANCE = new DefaultOpenServerMap();

	@Override
	public void openMap(String iserverRestAddr, String datasourceName, final String datasetName) {
		DatasourceConnectionInfo connectionInfo = new DatasourceConnectionInfo();
		connectionInfo.setEngineType(EngineType.ISERVERREST);
		connectionInfo.setServer(iserverRestAddr);
		connectionInfo.setAlias(datasourceName);
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		if (null != datasources) {
			final Datasource datasource = datasources.open(connectionInfo);
			if (null == datasource) {
				Application.getActiveApplication().getOutput().output(CoreProperties.getString("String_OpenDatasourceFaild"));
			} else {
				if (null == datasource.getDatasets().get(datasetName)) {
					return;
				} else {
					Application.getActiveApplication().getOutput().output(CoreProperties.getString("String_OpenDatasourceSuccessful"));
					final Dataset finalDataset = datasource.getDatasets().get(datasetName);
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							UICommonToolkit.refreshSelectedDatasourceNode(datasource.getAlias());
							//打开新的地图
							IFormMap newMap = (IFormMap) CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.MAP, datasetName + "@" + datasource.getAlias());
							Map map = newMap.getMapControl().getMap();
							map.getLayers().add(finalDataset, true);
							map.refresh();
							UICommonToolkit.getLayersManager().getLayersTree().reload();
						}
					});
				}
			}
		}
	}
}
