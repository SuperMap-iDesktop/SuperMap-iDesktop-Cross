package com.supermap.desktop.process.parameter.interfaces.datas.types;

/**
 * Created by highsad on 2017/4/13.
 */
public class Type {
	public final static Type UNKOWN = new Type("unkown", 0x00);

	private String name;
	private long value;

	public Type(String name, long value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public long getValue() {
		return value;
	}

	public boolean contains(Type type) {
		if (type == null) {
			return false;
		}

		if (getClass() != type.getClass()) {
			return false;
		}

		return (this.value & type.getValue()) == type.getValue();
	}

	public boolean intersects(Type type) {
		if (type == null) {
			return false;
		}

		if (getClass() != type.getClass()) {
			return false;
		}

		return (this.value & type.getValue()) != 0;
	}

	public boolean equals(Type type) {
		if (type == null) {
			return false;
		}

		if (getClass() != type.getClass()) {
			return false;
		}

		return this.value == type.getValue();
	}
}
