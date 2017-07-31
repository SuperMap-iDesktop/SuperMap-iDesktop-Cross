package com.supermap.desktop.process.messageBus;

/**
 * Created by highsad on 2017/7/26.
 */
public interface IOpenServerMap {
	void openMap(String iserverRestAddr, String datasourceName, final String datasetName);
}
