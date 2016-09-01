package com.supermap.desktop.Action;

import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.spatialanalyst.vectoranalyst.overlayAnalyst.OverlayAnalystDialog;

/**
 * Created by xie on 2016/8/30.
 */
public class CtrlActionOverlayAnalyst extends CtrlAction {
    public CtrlActionOverlayAnalyst(IBaseItem caller, IForm formClass) {
        super(caller, formClass);
    }

    @Override
    public void run() {
        try {
            OverlayAnalystDialog overlayAnalystDialog = new OverlayAnalystDialog();
        } catch (Exception e) {
            Application.getActiveApplication().getOutput().output(e);
        }
    }

    @Override
    public boolean enable() {
        // 只有存在矢量数据集时才能进行叠加分析
        boolean enable = false;
        if (null != Application.getActiveApplication().getWorkspace().getDatasources() && Application.getActiveApplication().getWorkspace().getDatasources().getCount() > 0) {
            Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
            for (int i = 0; i < datasources.getCount(); i++) {
                Datasource tempDatasource = datasources.get(i);
                if (null != tempDatasource.getDatasets() && tempDatasource.getDatasets().getCount() > 0) {
                    for (int j = 0; j < tempDatasource.getDatasets().getCount(); j++) {
                        if (tempDatasource.getDatasets().get(j) instanceof DatasetVector) {
                            enable = true;
                            break;
                        }
                    }
                }
            }
        }
        return enable;
    }
}
