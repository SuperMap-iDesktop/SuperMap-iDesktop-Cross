package com.supermap.desktop.process.parameter.interfaces.datas;

import com.supermap.desktop.utilities.StringUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by highsad on 2017/3/22.
 */
public class Outputs {
	private List<Data> datas;

	public Data getData(String name) {
		if (StringUtilities.isNullOrEmpty(name)) {
			return null;
		}

		Data result = null;

		for (int i = 0, size = this.datas.size(); i < size; i++) {
			Data data = this.datas.get(i);
			if (data.getName().equals(name)) {
				result = data;
				break;
			}
		}
		return result;
	}

	public void add(Data data) {
		if (data == null) {
			return;
		}

		if (isValid(data)) {
			this.datas.add(data);
		}
	}

	public Data[] getDatas(String type) {
		if (StringUtilities.isNullOrEmpty(type)) {
			return null;
		}

		ArrayList<Data> datas = new ArrayList<>();

		for (int i = 0, size = this.datas.size(); i < size; i++) {
			Data data = this.datas.get(i);
			if (data.getType().equals(type)) {
				datas.add(data);
			}
		}
		return datas.toArray(new Data[datas.size()]);
	}

	public boolean isValid(Data data) {
		if (this.datas.contains(data)) {
			return false;
		}

		boolean result = true;

		for (int i = 0, size = this.datas.size(); i < size; i++) {
			if (this.datas.get(i).getName().equals(data.getName())) {
				result = false;
				break;
			}
		}
		return result;
	}
}
