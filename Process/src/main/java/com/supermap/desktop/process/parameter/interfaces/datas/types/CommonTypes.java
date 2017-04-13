package com.supermap.desktop.process.parameter.interfaces.datas.types;

/**
 * Created by highsad on 2017/4/13.
 */
public class CommonTypes extends Type {
	private final static CommonTypes DATASOURCE = new CommonTypes("datasource", 0x01);
	private final static CommonTypes WORKSPACE = new CommonTypes("workspace", 0x02);
	private final static CommonTypes RECORDSET = new CommonTypes("recordset", 0x04);

	public CommonTypes(String name, long value) {
		super(name, value);
	}
}
