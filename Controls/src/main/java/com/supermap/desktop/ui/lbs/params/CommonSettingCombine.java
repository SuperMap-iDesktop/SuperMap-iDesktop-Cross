package com.supermap.desktop.ui.lbs.params;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-06-26.
 */
public class CommonSettingCombine {
	private String name;
	private String value;
	private ArrayList<CommonSettingCombine> values = new ArrayList<>();

	public CommonSettingCombine(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public void add(CommonSettingCombine... commonSettingCombines) {
		for (CommonSettingCombine commonSettingCombine : commonSettingCombines) {
			values.add(commonSettingCombine);
		}
	}

	public String getJson() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("\"");
		stringBuffer.append(name);
		stringBuffer.append("\":");
		stringBuffer.append(getJsonValue());
		return stringBuffer.toString();
	}

	private String getJsonValue() {
		StringBuffer stringBuffer = new StringBuffer();

		if (values.size() <= 0) {
			stringBuffer.append("\"");
			stringBuffer.append(value);
			stringBuffer.append("\"");

		} else {
			stringBuffer.append("{");
			for (int i = 0; i < values.size(); i++) {
				CommonSettingCombine commonSettingCombine = values.get(i);
				stringBuffer.append(commonSettingCombine.getJson());
				if (i != values.size() - 1) {
					stringBuffer.append(",");
				}
			}
			stringBuffer.append("}");
		}
		return stringBuffer.toString();

	}

	public String getFinalJSon() {
		return getJsonValue();
	}

	public static void main(String[] args) {
		CommonSettingCombine input = new CommonSettingCombine("input", "");
		CommonSettingCombine datasource = new CommonSettingCombine("datasetSource", "samples_zone_zone");
		input.add(datasource);

		CommonSettingCombine analyst = new CommonSettingCombine("a nalyst", "");
		CommonSettingCombine datasetOverlay = new CommonSettingCombine("datasetOverlay", "aaa_test_zone");
		CommonSettingCombine mode = new CommonSettingCombine("mode", "identity");
		analyst.add(datasetOverlay, mode);

		CommonSettingCombine commonSettingCombine = new CommonSettingCombine("", "");
		commonSettingCombine.add(input, analyst);
		System.out.println(commonSettingCombine.getFinalJSon());
	}
}
