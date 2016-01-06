package com.supermap.desktop.ui.controls;

import java.util.EventListener;
/**
 * 对话框返回结果监听器
 * @author zhaosy
 *
 */
public interface DialogResultListener extends EventListener{
	
public void dialogResultChanged(DialogResultEvent event);

}
