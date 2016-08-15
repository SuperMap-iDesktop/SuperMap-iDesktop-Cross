package com.supermap.desktop.CtrlAction.CADStyle;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.geometryoperation.CtrlAction.CtrlActionEditorBase;
import com.supermap.desktop.geometryoperation.editor.IEditor;
import com.supermap.desktop.implement.CtrlAction;

/**
 * Created by xie on 2016/8/10.
 */
public class CtrlActionCADStyle extends CtrlActionEditorBase {
    IEditor editor = new CADStyleEditor();

    public CtrlActionCADStyle(IBaseItem caller, IForm formClass) {
        super(caller, formClass);
    }
    public IEditor getEditor() {
        return this.editor;
    }
}
