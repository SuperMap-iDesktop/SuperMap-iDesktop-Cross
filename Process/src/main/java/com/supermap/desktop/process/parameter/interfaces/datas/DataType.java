package com.supermap.desktop.process.parameter.interfaces.datas;

/**
 * Created by highsad on 2017/3/29.
 */
public class DataType {
	public final static int UNKNOWN = 0;
	public final static int INTEGER = 1;
	public final static int DOUBLE = 2;
	public final static int FLOAT = 3;
	public final static int DATASOURCE = 4; // udb、oracle、sqlplus ...
	public final static int DATASET = 5; // vector、image ...
	public final static int WORKSPACE = 6;
	public final static int FILE = 7; // How to represent those files which own different suffixes.
}
