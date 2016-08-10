package com.supermap.desktop.netservices.iserver;

import java.util.EventListener;

/**
 * @author highsad 发布服务时，更改文件选中状态时的事件消息
 */
public interface FileSelectedChangeListener extends EventListener {
	void FileSelectedChange(FileSelectedChangeEvent e);
}
