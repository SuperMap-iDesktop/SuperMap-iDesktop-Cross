package com.supermap.desktop.dialog.cacheClip.cache;

import java.util.ArrayList;
import java.util.EventObject;

public class ProcessBuildEvent extends EventObject {

	private ArrayList<String> m_args;
	private boolean m_active;
	
	public ProcessBuildEvent(Object source) {
		super(source);
		// TODO Auto-generated constructor stub
		m_args = new ArrayList<String>();
		m_active = false;
	}

	/**
	 *  此次进程启动参数(空格分隔)
	 */
	public void setArgs(ArrayList<String> args){
		m_args.addAll(args);
	}
	
	public ArrayList<String> getArgs(){
		return m_args;
	}
	
	/**
	 *  是否构建激活此次进程
	 */
	public void setActive(boolean active){
		m_active = active;
	}
	
	public boolean getActive(){
		return m_active;
	}
}
