package com.supermap.desktop.CtrlAction.CADStyle;

import com.supermap.data.*;
import com.supermap.mapping.ThemeCustom;
import com.supermap.ui.MapControl;

import javax.swing.*;

/**
 * Created by Administrator on 2016/9/23.
 */
public class ttt {
    public static void main(String[] args) {
        Workspace workspace = new Workspace();
        WorkspaceConnectionInfo workspaceConnectionInfo = new WorkspaceConnectionInfo();
        workspaceConnectionInfo.setType(WorkspaceType.SMWU);
        String file = "F:/SampleData711/City/Jingjin.smwu";
        workspaceConnectionInfo.setServer(file);
        workspace.open(workspaceConnectionInfo);
        Datasource datasource = workspace.getDatasources().get(0);
        DatasetVector dataset = (DatasetVector) datasource.getDatasets().get("Neighbor_L");
        MapControl mapControl = new MapControl(workspace);
        mapControl.getMap().getLayers().add(dataset, true);
        String expression = "SmID";
        ThemeCustom themeCustom = new ThemeCustom();
        themeCustom.setLineSymbolIDExpression(expression);
        mapControl.getMap().getLayers().insert(1, dataset, themeCustom);
        JFrame frame = new JFrame();
        frame.add(mapControl);
        frame.setSize(600, 400);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
