package com.supermap.desktop.CtrlAction.SQLQuery.components;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.utilities.StringUtilities;

import java.text.MessageFormat;
import java.util.concurrent.CancellationException;

/**
 * Created by lixiaoyao on 2017/4/6.
 */
public class SQLQueryProgressCallable extends UpdateProgressCallable {
    private Datasource resultDatasource;
    private Recordset resultRecord;
    private DatasetVector resultDataset;
    private String datasetName = "";

    private SteppedListener steppedListener = new SteppedListener() {
        @Override
        public void stepped(SteppedEvent arg0) {
            try {
                updateProgress(arg0.getPercent(), String.valueOf(arg0.getRemainTime()), arg0.getMessage());
            } catch (CancellationException e) {
                arg0.setCancel(true);
            }
        }
    };

    public SQLQueryProgressCallable(Datasource resultDatasource, Recordset inputRecordset, String datasetName) {
        this.resultDatasource = resultDatasource;
        this.resultRecord = inputRecordset;
        this.datasetName = datasetName;
    }

    @Override
    public Boolean call() throws Exception {
        boolean result = true;
        this.resultDatasource.removeSteppedListener(this.steppedListener);
        this.resultDatasource.addSteppedListener(this.steppedListener);
        if (this.resultDatasource != null && !StringUtilities.isNullOrEmpty(this.datasetName)) {
            try {
                this.resultDataset=this.resultDatasource.recordsetToDataset(this.resultRecord,this.datasetName);
            } catch (Exception e) {
                this.resultDataset = null;
                result = false;
            }
            resultRecord.moveFirst();
            if (resultDataset == null) {
                result = false;
                Application.getActiveApplication().getOutput().output(DataViewProperties.getString("String_SQLQuerySaveAsResultFaield"));
            } else {
                Application.getActiveApplication().getOutput()
                        .output(MessageFormat.format(DataViewProperties.getString("String_SQLQuerySavaAsResultSucces"), resultDataset.getName()));
            }
        }
        return result;
    }
}
