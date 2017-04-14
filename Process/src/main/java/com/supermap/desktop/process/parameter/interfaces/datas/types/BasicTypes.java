package com.supermap.desktop.process.parameter.interfaces.datas.types;

/**
 * Created by highsad on 2017/4/13.
 */
public final class BasicTypes extends Type {
	public final static BasicTypes SHORT = new BasicTypes("short", 0x01);
	public final static BasicTypes INTEGER = new BasicTypes("integer", 0x02);
	public final static BasicTypes LONG = new BasicTypes("long", 0x04);
	public final static BasicTypes FLOAT = new BasicTypes("float", 0x08);
	public final static BasicTypes DOUBLE = new BasicTypes("double", 0x10);
	public final static BasicTypes STRING = new BasicTypes("string", 0x20);
	public final static BasicTypes BOOLEAN = new BasicTypes("boolean", 0x40);
	public final static BasicTypes CHAR = new BasicTypes("char", 0x80);

	public final static BasicTypes INT_ALL = new BasicTypes("AllInteger", SHORT.getValue() | INTEGER.getValue() | LONG.getValue());

	public final static BasicTypes NUMBER = new BasicTypes("number",
			SHORT.getValue() | INTEGER.getValue() | LONG.getValue() | FLOAT.getValue() | DOUBLE.getValue());

	public BasicTypes(String name, long value) {
		super(name, value);
	}
}
