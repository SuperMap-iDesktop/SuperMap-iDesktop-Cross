package com.supermap.scene;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;

public class IdleTimerTask extends TimerTask {

	private static boolean initWorkspace = false;
	@Override
	public void run() {
		try {
			if (!initWorkspace) {
				Test.sceneFrame.sceneControl.getScene().setWorkspace(Test.workspace);
				initWorkspace = true;
			}
//			// 刷新主工具条
//			Test.sceneFrame.refreshToolbar();
		} catch (Exception ex) {
			
		} finally {
			// 执行完刷新后启动定时器，准备下一次刷新
	        new Timer().schedule(new IdleTimerTask(), 1000);  
		}
	}

}
