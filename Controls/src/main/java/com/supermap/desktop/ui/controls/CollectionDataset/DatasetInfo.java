package com.supermap.desktop.ui.controls.CollectionDataset;

import com.supermap.data.Dataset;

/**
 * Created by xie on 2017/7/19.
 * 数据集信息类
 */
public class DatasetInfo {
	private Dataset dataset;
	private String capiton;
	private String name;
	private String state;

	public DatasetInfo() {

	}

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
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
