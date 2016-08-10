package com.supermap.desktop.netservices.iserver;

import java.util.EventObject;

/**
 * @author highsad 发布服务时，更改文件选中状态时的事件消息
 */
public class FileSelectedChangeEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SelectableFile file;

	public FileSelectedChangeEvent(Object source, SelectableFile file) {
		super(source);
		this.file = file;
	}

	public SelectableFile getFile() {
		return this.file;
	}
}
