package com.supermap.desktop.ui.controls.CollectionDataset;

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
	private String dataBase;
	private String user;
	private String alias;

	public DatasetInfo() {

	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
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

	public String getDataBase() {
		return dataBase;
	}

	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
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
