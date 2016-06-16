package com.supermap.desktop.CtrlAction.Datasource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.Datasources;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.utilities.DatasourceUtilities;

public class CtrlActionDatasourceNewFile extends CtrlAction {

	public CtrlActionDatasourceNewFile(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {

			String filePath = "";
			String alias = "";
			if (!SmFileChoose.isModuleExist("DatasourceNewFile")) {
				String fileFilters = SmFileChoose.bulidFileFilters(SmFileChoose.createFileFilter(DataEditorProperties.getString("String_UDBFileFilterName"),
						DataEditorProperties.getString("String_UDBFileFilters")));
				SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
						DataEditorProperties.getString("String_NewDatasourceFile"), "DatasourceNewFile", "SaveOne");
			}
			SmFileChoose smFileChoose = new SmFileChoose("DatasourceNewFile");
			int state = smFileChoose.showDefaultDialog();
			if (state != JFileChooser.CANCEL_OPTION) {
				filePath = smFileChoose.getFilePath();
				if (null != filePath && !filePath.isEmpty()) {
					int place = smFileChoose.getFileName().lastIndexOf(".");
					alias = smFileChoose.getFileName().substring(0, place);
				}
				// 文件存在时覆盖
				if (new File(filePath).exists()) {
					new File(filePath).delete();
					new File(filePath.substring(0, filePath.length() - 1) + "d").delete();
				}
				if (new File(filePath.substring(0, filePath.length() - 1) + "d").exists()) {
					new File(filePath.substring(0, filePath.length() - 1) + "d").delete();
				}

				Datasources a = Application.getActiveApplication().getWorkspace().getDatasources();
				List nameList = new ArrayList();
				int i = 0;
				int DataSoursesLength = a.getCount();
				for (i = 0; i < DataSoursesLength; i++) {
					nameList.add(a.get(i).getAlias());
				}
				boolean isNoSameName = false;
				// 如果当前工作空间存在同名数据源则加_1
				while (!isNoSameName) {
					isNoSameName = true;
					for (i = 0; i < DataSoursesLength; i++) {
						if (alias.equals(nameList.get(i))) {
							isNoSameName = false;
							alias += "_1";
						}
					}
				}
				DatasourceConnectionInfo info = new DatasourceConnectionInfo(filePath, alias, "");
				Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().create(info);

				String resultInfo = ""; //$NON-NLS-1$
				if (datasource != null) {
					DefaultMutableTreeNode treeRoot = (DefaultMutableTreeNode) UICommonToolkit.getWorkspaceManager().getWorkspaceTree().getModel().getRoot();
					DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treeRoot.getChildAt(0).getChildAt(treeRoot.getChildAt(0).getChildCount() - 1);
					TreePath path = new TreePath(treeNode.getPath());
					UICommonToolkit.getWorkspaceManager().getWorkspaceTree().setSelectionPath(path);
					DatasourceUtilities.addDatasourceToRecentFile(datasource);
					resultInfo = DataEditorProperties.getString("String_CreateDatasourseSuccessful");
				} else {
					resultInfo = DataEditorProperties.getString("String_CreateDatasourseFailed");
				}
				Application.getActiveApplication().getOutput().output(resultInfo);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		return true;
	}

}
