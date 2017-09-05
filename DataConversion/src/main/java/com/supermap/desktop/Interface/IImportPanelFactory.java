package com.supermap.desktop.Interface;

import com.supermap.desktop.iml.ImportInfo;
import com.supermap.desktop.importUI.PanelImport;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by xie on 2016/9/29.
 * 导入界面制造工厂接口
 */
public interface IImportPanelFactory {
	/**
	 * 创建导入信息panel
	 *
	 * @param owner
	 * @param importInfo
	 * @return
	 */
	IPanelImport createPanelImport(JDialog owner, ImportInfo importInfo);

	/**
	 * 创建导入信息panel
	 *
	 * @param panelImports
	 * @return
	 */
	IPanelImport createPanelImport(ArrayList<PanelImport> panelImports);
}
