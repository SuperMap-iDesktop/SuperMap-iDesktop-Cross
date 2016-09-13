package com.supermap.desktop.framemenus;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.EngineType;
import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.utilities.DatasourceUtilities;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.text.MessageFormat;

/**
 * Created by highsad on 2016/8/25.
 */
public class CtrlActionDatasourceMode extends CtrlAction {


	public CtrlActionDatasourceMode(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	public boolean isReadOnly() {
		return false;
	}

	@Override
	public void run() {
		try {
			try {
				Workspace workspace = Application.getActiveApplication().getWorkspace();
				Datasource[] datasources = Application.getActiveApplication().getActiveDatasources();

				// 重新打开之前先关闭地图
				for (int i = 0; i < datasources.length; i++) {
					Datasource datasource = datasources[i];
					DatasourceConnectionInfo info = DatasourceUtilities.cloneInfo(datasource.getConnectionInfo());
					String datasourceName = datasource.getAlias();

					if (datasource.isOpened()) {

						// 如果数据源已经打开，就先关闭数据源，此时数据源节点会从工作空间中删除
						// 如果不删除，在 Java 里能使用的判断数据源是否占用的方法都无法过滤掉自己
						DatasourceUtilities.closeDatasource(datasource);

						if (!DatasourceUtilities.isDatasourceOccupied(info.getServer())) {

							// 判断关闭之后的数据源是否仍然被占用，没有则更改独占/可读
							info.setReadOnly(isReadOnly());
						} else {

							// 仍然被占用则输出打开失败的信息
							Application.getActiveApplication().getOutput().output(MessageFormat.format(DataViewProperties.getString("String_DatasourceOccupied"), datasourceName));
						}
						datasources[i] = workspace.getDatasources().open(info);
					} else {
						info.setReadOnly(isReadOnly());
						// 如果数据源没有成功打开，工作空间中会有一个没有数据集的数据源节点，如果重新打开失败，需要保留这个节点，因此不能直接删除
						// 判断更改独占/可读之后是否可以正常打开，是则重新打开
						if (DatasourceUtilities.attemptToOpenDataosurce(info)) {
							DatasourceUtilities.closeDatasource(datasource);
							datasources[i] = workspace.getDatasources().open(info);
						} else {
							Application.getActiveApplication().getOutput().output(MessageFormat.format(DataViewProperties.getString("String_DatasourceOccupied"), datasourceName));
						}
					}

//					DatasourceConnectionInfo info = DatasourceUtilities.cloneInfo(datasource.getConnectionInfo());
//					info.setReadOnly(isReadOnly());
//					if (DatasourceUtilities.attemptToOpenDataosurce(info)) {
//						DatasourceUtilities.closeDatasource(datasource);
//						datasources[i] = workspace.getDatasources().open(info);
//					} else {
//						Application.getActiveApplication().getOutput().output(MessageFormat.format(DataViewProperties.getString("String_DatasourceOccupied"), datasource.getAlias()));
//					}
				}

				// 更改完成选中结果
				UICommonToolkit.getWorkspaceManager().selectDatasources(datasources);
				WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
				DefaultMutableTreeNode datasourcesNode = workspaceTree.getDatasourcesNode();

				if (datasourcesNode != null) {
					TreePath datasourcesPath = new TreePath(datasourcesNode.getPath());
					workspaceTree.expandPath(datasourcesPath);
				}
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enabled = true;
		Datasource[] datasources = Application.getActiveApplication().getActiveDatasources();

		if (datasources.length > 0) {
			for (int i = 0; i < datasources.length; i++) {
				Datasource datasource = datasources[i];

				if (datasource.getEngineType() != EngineType.UDB
						|| DatasourceUtilities.isMemoryDatasource(datasource)) {
					enabled = false;
				} else if (datasource.isReadOnly() == isReadOnly()) {
					enabled = false;
				}

				if (!enabled) {
					break;
				}
			}
		} else {
			enabled = false;
		}
		return enabled;
	}
}

