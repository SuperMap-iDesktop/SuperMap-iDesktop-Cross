package com.supermap.desktop.dataeditor;

import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.mapping.Map;

/**
 * Created by xie on 2017/7/22.
 */
public class Test {
	public static void main(String[] args) {
		WorkspaceConnectionInfo connectionInfo = new WorkspaceConnectionInfo();
		connectionInfo.setServer("C:\\Users\\Administrator\\Desktop\\Jingjin\\Jingjin.SMWU");
		connectionInfo.setType(WorkspaceType.SMWU);
		Workspace workspace = new Workspace();
		workspace.open(connectionInfo);
		Map map = new Map(workspace);
		map.open("京津地区地图");
		System.out.println(map.getVisibleScales().length);
	}
}
