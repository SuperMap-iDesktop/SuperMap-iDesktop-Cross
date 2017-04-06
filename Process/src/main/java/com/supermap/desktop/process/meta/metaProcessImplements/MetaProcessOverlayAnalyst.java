package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.OverlayAnalyst;
import com.supermap.data.DatasetVector;
import com.supermap.data.DatasetVectorInfo;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.SteppedEvent;
import com.supermap.data.SteppedListener;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterOverlayAnalystInfo;
import com.supermap.desktop.process.parameter.implement.DefaultParameters;
import com.supermap.desktop.process.parameter.implement.ParameterCheckBox;
import com.supermap.desktop.process.parameter.implement.ParameterCombine;
import com.supermap.desktop.process.parameter.implement.ParameterDatasource;
import com.supermap.desktop.process.parameter.implement.ParameterFieldSetDialog;
import com.supermap.desktop.process.parameter.implement.ParameterLabel;
import com.supermap.desktop.process.parameter.implement.ParameterOverlayAnalyst;
import com.supermap.desktop.process.parameter.implement.ParameterSingleDataset;
import com.supermap.desktop.process.parameter.implement.ParameterTextField;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.tasks.ProcessTask;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.enums.OverlayAnalystType;

import javax.swing.*;
import java.text.MessageFormat;

/**
 * Created by xie on 2017/2/14.
 * 叠加分析
 */
public class MetaProcessOverlayAnalyst extends MetaProcess {
	private OverlayAnalystType analystType;
	private ParameterOverlayAnalyst parameterOverlayAnalyst;
	private ParameterDatasource parameterSourceDatasource = new ParameterDatasource();
	private ParameterSingleDataset parameterSourceDataset = new ParameterSingleDataset();
	private ParameterDatasource parameterOverlayDatasource = new ParameterDatasource();
	private ParameterSingleDataset parameterOverlayDataset = new ParameterSingleDataset();
	private ParameterDatasource parameterResultDatasource = new ParameterDatasource();
	private ParameterTextField parameterSaveDataset = new ParameterTextField();
	private ParameterFieldSetDialog parameterFieldSetDialog = new ParameterFieldSetDialog();
	private ParameterTextField parameterTolerance = new ParameterTextField();
	private ParameterLabel parameterUnit = new ParameterLabel();
	private ParameterCheckBox parameterCheckBoxIsCompareResult = new ParameterCheckBox();

    private SteppedListener steppedListener = new SteppedListener() {
        @Override
        public void stepped(SteppedEvent steppedEvent) {
            fireRunning(new RunningEvent(MetaProcessOverlayAnalyst.this, steppedEvent.getPercent(), steppedEvent.getMessage()));
        }
    };

	public MetaProcessOverlayAnalyst(OverlayAnalystType analystType) {
		this.analystType = analystType;
		initParameters();
		initParameterLayout();
	}

	private void initParameters() {
		parameterSourceDatasource.setDescribe(CommonProperties.getString(CommonProperties.Label_Datasource));
		parameterOverlayDatasource.setDescribe(CommonProperties.getString(CommonProperties.Label_Datasource));
		parameterResultDatasource.setDescribe(CommonProperties.getString(CommonProperties.Label_Datasource));
		parameterSaveDataset.setDescribe(CommonProperties.getString(CommonProperties.Label_Dataset));
		parameterUnit.setDescribe(CommonProperties.getString("String_DistanceUnit_Meter"));//单位和数据集有关系
		parameterCheckBoxIsCompareResult.setDescribe(CommonProperties.getString("String_CheckBox_ResultComparison"));

	}

	private void initParameterLayout() {

		parameters = new DefaultParameters();
		ParameterCombine parameterCombineSource = new ParameterCombine();
		parameterCombineSource.setDescribe(ControlsProperties.getString("String_GroupBox_SourceDataset"));
		parameterCombineSource.addParameters(parameterSourceDatasource, parameterSourceDataset);

		ParameterCombine parameterCombineResult = new ParameterCombine();
		parameterCombineResult.setDescribe(CommonProperties.getString("String_GroupBox_OverlayDataset"));
		parameterCombineResult.addParameters(parameterOverlayDatasource, parameterOverlayDataset);

		ParameterCombine parameterCombineResultSet = new ParameterCombine();
		parameterCombineResultSet.setDescribe(CommonProperties.getString("String_ResultSet"));
		ParameterCombine parameterCombineParent = new ParameterCombine(ParameterCombine.HORIZONTAL);
		parameterCombineParent.addParameters(
				new ParameterCombine().addParameters(parameterSaveDataset, parameterTolerance),
				new ParameterCombine().addParameters(parameterFieldSetDialog, parameterUnit));
		parameterCombineResult.addParameters(parameterResultDatasource, parameterCombineParent, parameterCheckBoxIsCompareResult);

//		parameters.setParameters(parameterCombineSource, parameterCombineResult, parameterCombineResultSet);


		parameterOverlayAnalyst = new ParameterOverlayAnalyst();
		parameterOverlayAnalyst.setOverlayAnalystType(analystType);
		parameters.setParameters(parameterOverlayAnalyst);
	}

    public OverlayAnalystType getAnalystType() {
        return analystType;
    }

    @Override
    public String getTitle() {
        String title = "";
        switch (analystType) {
            case CLIP:
                title = ControlsProperties.getString("String_OverlayAnalyst_CLIP");
                break;
            case ERASE:
                title = ControlsProperties.getString("String_OverlayAnalyst_ERASE");
                break;
            case IDENTITY:
                title = ControlsProperties.getString("String_OverlayAnalyst_IDENTITY");
                break;
            case INTERSECT:
                title = ControlsProperties.getString("String_OverlayAnalyst_INTERSECT");
                break;
            case UNION:
                title = ControlsProperties.getString("String_OverlayAnalyst_UNION");
                break;
            case XOR:
                title = ControlsProperties.getString("String_OverlayAnalyst_XOR");
                break;
            case UPDATE:
                title = ControlsProperties.getString("String_OverlayAnalyst_UPDATE");
                break;
            default:
                break;
        }
        return title;
    }

    @Override
    public IParameterPanel getComponent() {
        return parameters.getPanel();
    }

    @Override
    public void run() {
        fireRunning(new RunningEvent(this, 0, "start"));
        ParameterOverlayAnalystInfo info = (ParameterOverlayAnalystInfo) parameterOverlayAnalyst.getSelectedItem();
        if (inputs.getData() != null && inputs.getData() instanceof DatasetVector) {
            info.sourceDataset = (DatasetVector) inputs.getData();
            info.sourceDatatsource = ((DatasetVector) inputs.getData()).getDatasource();
        }
        if (null == info.sourceDataset || null == info.overlayAnalystDataset
                || null == info.targetDataset || null == info.analystParameter) {
            return;
        }
        if (null != info.sourceDataset && null != info.overlayAnalystDataset && !isSameProjection(info.sourceDataset.getPrjCoordSys(), info.overlayAnalystDataset.getPrjCoordSys())) {
            Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_PrjCoordSys_Different") + "\n" + ControlsProperties.getString("String_Parameters"));
            Application.getActiveApplication().getOutput().output(MessageFormat.format(ControlsProperties.getString("String_OverlayAnalyst_Failed"), info.sourceDataset.getName() + "@" + info.sourceDataset.getDatasource().getAlias()
                    , info.overlayAnalystDataset.getName() + "@" + info.overlayAnalystDataset.getDatasource().getAlias(), analystType.toString()));
            return;
        }
        OverlayAnalyst.addSteppedListener(this.steppedListener);
        DatasetVectorInfo datasetVectorInfo = new DatasetVectorInfo();
        datasetVectorInfo.setType(info.sourceDataset.getType());
        datasetVectorInfo.setEncodeType(info.sourceDataset.getEncodeType());
        if (info.targetDatasource.getDatasets().getAvailableDatasetName(info.targetDataset).equals(info.targetDataset)) {
            // 名称合法时可以设置名称
            datasetVectorInfo.setName(info.targetDataset);
        }
        DatasetVector targetDataset = info.targetDatasource.getDatasets().create(datasetVectorInfo);
        targetDataset.setPrjCoordSys(info.sourceDataset.getPrjCoordSys());

        switch (analystType) {
            case CLIP:
                OverlayAnalyst.clip(info.sourceDataset, info.overlayAnalystDataset, targetDataset, info.analystParameter);
                break;
            case ERASE:
                OverlayAnalyst.erase(info.sourceDataset, info.overlayAnalystDataset, targetDataset, info.analystParameter);
                break;
            case IDENTITY:
                OverlayAnalyst.identity(info.sourceDataset, info.overlayAnalystDataset, targetDataset, info.analystParameter);
                break;
            case INTERSECT:
                OverlayAnalyst.intersect(info.sourceDataset, info.overlayAnalystDataset, targetDataset, info.analystParameter);
                break;
            case UNION:
                OverlayAnalyst.union(info.sourceDataset, info.overlayAnalystDataset, targetDataset, info.analystParameter);
                break;
            case XOR:
                OverlayAnalyst.xOR(info.sourceDataset, info.overlayAnalystDataset, targetDataset, info.analystParameter);
                break;
            case UPDATE:
                OverlayAnalyst.update(info.sourceDataset, info.overlayAnalystDataset, targetDataset, info.analystParameter);
                break;
            default:
                break;
        }
        OverlayAnalyst.removeSteppedListener(this.steppedListener);
        fireRunning(new RunningEvent(this, 100, "finished"));
        setFinished(true);
//		ProcessData processData = new ProcessData();
//		processData.setData(info.sourceDataset);
//		outPuts.add(0, processData);
    }

    @Override
    public String getKey() {
        return MetaKeys.OVERLAY_ANALYST;
    }

    private boolean isSameProjection(PrjCoordSys prjCoordSys, PrjCoordSys prjCoordSys1) {
        if (prjCoordSys.getType() != prjCoordSys1.getType()) {
            return false;
        }
        if (prjCoordSys.getGeoCoordSys() == prjCoordSys1.getGeoCoordSys()) {
            return true;
        }
        if (prjCoordSys.getGeoCoordSys() == null || prjCoordSys1.getGeoCoordSys() == null) {
            return false;
        }
        if (prjCoordSys.getGeoCoordSys().getType() != prjCoordSys1.getGeoCoordSys().getType()) {
            return false;
        }
        return true;
    }

    @Override
    public Icon getIcon() {
        return getIconByPath("/processresources/Process/OverlayAnalyst.png");
    }


}
