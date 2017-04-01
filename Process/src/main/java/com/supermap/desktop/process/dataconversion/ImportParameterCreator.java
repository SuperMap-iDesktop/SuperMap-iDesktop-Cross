package com.supermap.desktop.process.dataconversion;

import com.supermap.data.EncodeType;
import com.supermap.data.conversion.*;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.parameter.implement.ParameterCheckBox;
import com.supermap.desktop.process.parameter.implement.ParameterDatasource;
import com.supermap.desktop.process.parameter.implement.ParameterEnum;
import com.supermap.desktop.process.parameter.implement.ParameterTextField;
import com.supermap.desktop.process.util.EnumParser;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.EncodeTypeUtilities;

import java.util.ArrayList;

/**
 * Created by xie on 2017/3/31.
 */
public class ImportParameterCreator implements IParameterCreator {

    @Override
    public ArrayList<ReflectInfo> create(Object importSetting) {
        ArrayList<ReflectInfo> result = new ArrayList<>();
        if (importSetting instanceof ImportSettingRAW || importSetting instanceof ImportSettingTEMSClutter
                || importSetting instanceof ImportSettingBIP || importSetting instanceof ImportSettingBSQ
                || importSetting instanceof ImportSettingGBDEM || importSetting instanceof ImportSettingUSGSDEM
                || importSetting instanceof ImportSettingBIL) {
            ReflectInfo pyramidBuiltInfo = new ReflectInfo();
            pyramidBuiltInfo.methodName = "setPyramidBuilt";
            pyramidBuiltInfo.parameter = new ParameterCheckBox(ControlsProperties.getString("String_Form_BuildDatasetPyramid"));
            result.add(pyramidBuiltInfo);
        }
        return result;
    }

    @Override
    public ArrayList<ReflectInfo> createDefault(Object o) {
        ArrayList<ReflectInfo> result = new ArrayList<>();
        //Target dataset reflect info
        ReflectInfo targetDatasource = new ReflectInfo();
        targetDatasource.methodName = "setTargetDatasource";
        ParameterDatasource datasource = new ParameterDatasource();
        datasource.setDescribe(CommonProperties.getString(CommonProperties.Label_Datasource));
        targetDatasource.parameter = datasource;

        ReflectInfo targetDatasetName = new ReflectInfo();
        targetDatasetName.methodName = "setTargetDatasource";
        ParameterTextField datasetName = new ParameterTextField(CommonProperties.getString(CommonProperties.Label_Dataset));
        if (o instanceof ImportSetting) {
            datasetName.setSelectedItem(((ImportSetting) o).getTargetDatasetName());
        }
        targetDatasetName.parameter = datasetName;
        //todo
        //different type has it's own encode type,so replace these code after
        ReflectInfo setEncodeType = new ReflectInfo();
        setEncodeType.methodName = "setEncodeType";
        String[] encodingValue = new String[]{"NONE", "BYTE", "INT16", "INT24", "INT32"};
        String[] encoding = new String[]{
                EncodeTypeUtilities.toString(EncodeType.NONE),
                EncodeTypeUtilities.toString(EncodeType.BYTE),
                EncodeTypeUtilities.toString(EncodeType.INT16),
                EncodeTypeUtilities.toString(EncodeType.INT24),
                EncodeTypeUtilities.toString(EncodeType.INT32)
        };
        ParameterEnum encodeType = new ParameterEnum(new EnumParser(EncodeType.class, encodingValue, encoding)).setDescribe(ProcessProperties.getString("label_encodingType"));
        encodeType.setSelectedItem(EncodeType.NONE);
        setEncodeType.parameter = encodeType;

        //ImportMode reflect info
        String[] importModelValue = new String[]{"NONE", "APPEND", "OVERWRITE"};
        String[] importModel = new String[]{
                ProcessProperties.getString("String_FormImport_None"),
                ProcessProperties.getString("String_FormImport_Append"),
                ProcessProperties.getString("String_FormImport_OverWrite")
        };

        ReflectInfo importModeInfo = new ReflectInfo();
        importModeInfo.methodName = "setImportMode";
        ParameterEnum importMode = new ParameterEnum(new EnumParser(ImportMode.class, importModelValue, importModel)).setDescribe(ProcessProperties.getString("Label_ImportMode"));
        importMode.setSelectedItem(ImportMode.NONE);
        importModeInfo.parameter = importMode;

        result.add(targetDatasource);
        result.add(targetDatasetName);
        result.add(setEncodeType);
        result.add(importModeInfo);
        return result;
    }
}
