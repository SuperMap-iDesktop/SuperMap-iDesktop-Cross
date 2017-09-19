package com.supermap.desktop.ui.controls.CollectionDataset;

import com.supermap.data.Dataset;

/**
 * Created by xie on 2017/7/19.
 * 数据集信息类
 */
public class DatasetInfo {
	private String capiton;
	private String name;
	private String state;
	private String server;
	private String engineType;
	private String datasourceAlias;
	private String user;

	public DatasetInfo() {

	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getEngineType() {
		return engineType;
	}

	public void setEngineType(String engineType) {
		this.engineType = engineType;
	}

	public String getDatasourceAlias() {
		return datasourceAlias;
	}

	public void setDatasourceAlias(String datasourceAlias) {
		this.datasourceAlias = datasourceAlias;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getCapiton() {
		return capiton;
	}

	public void setCapiton(String capiton) {
		this.capiton = capiton;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
