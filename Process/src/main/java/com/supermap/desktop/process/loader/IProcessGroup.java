package com.supermap.desktop.process.loader;

/**
 * Created by highsad on 2017/8/4.
 */
public interface IProcessGroup {
	String getID();

	String getName();

	IProcessGroup getGroup(String id);

	IProcessGroup getGroup(int index);


}
