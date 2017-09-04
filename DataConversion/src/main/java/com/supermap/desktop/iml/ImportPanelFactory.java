package com.supermap.desktop.iml;

import com.supermap.desktop.Interface.IImportPanelFactory;
import com.supermap.desktop.Interface.IPanelImport;
import com.supermap.desktop.importUI.PanelImport;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by xie on 2016/10/13.
 */
public class ImportPanelFactory implements IImportPanelFactory {

	@Override
	public IPanelImport createPanelImport(JDialog owner, ImportInfo importInfo) {
		return new PanelImport(owner, importInfo);
	}

	public IPanelImport createPanelImport(ArrayList<PanelImport> panelImports) {
		return new PanelImport(panelImports);
	}

}
