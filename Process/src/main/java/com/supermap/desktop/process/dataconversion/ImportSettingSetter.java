package com.supermap.desktop.process.dataconversion;

import com.supermap.data.Point3D;
import com.supermap.data.conversion.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.parameter.implement.ParameterCheckBox;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by xie on 2017/4/6.
 */
public class ImportSettingSetter {
    //Utilities class
    private ImportSettingSetter() {
    }

    public static DataImport setParameter(ImportSetting importSetting, ArrayList<ReflectInfo> basicInfos, ArrayList<ReflectInfo> otherInfo) {
        try {
            Class importSettingClass = importSetting.getClass();
            int basicSize = basicInfos.size();
            for (int i = 0; i < basicSize; i++) {
                Method method = importSettingClass.getMethod(basicInfos.get(i).methodName, Object.class);
                if (basicInfos.get(i).parameter instanceof ISelectionParameter) {
                    if (basicInfos.get(i).parameter instanceof ParameterCheckBox) {
                        method.invoke(importSettingClass, "true".equals(((ISelectionParameter) basicInfos.get(i).parameter).getSelectedItem()) ? true : false);
                    } else {
                        method.invoke(importSettingClass, ((ISelectionParameter) basicInfos.get(i).parameter).getSelectedItem());
                    }
                }
            }
            if (importSetting instanceof ImportSettingModelOSG || importSetting instanceof ImportSettingModelX
                    || importSetting instanceof ImportSettingModelDXF || importSetting instanceof ImportSettingModelFBX
                    || importSetting instanceof ImportSettingModelFLT) {
                Point3D point3D = new Point3D();
                point3D.setX(Double.valueOf((Double) ((ISelectionParameter) otherInfo.get(0).parameter).getSelectedItem()));
                point3D.setY(Double.valueOf((Double) ((ISelectionParameter) otherInfo.get(1).parameter).getSelectedItem()));
                point3D.setZ(Double.valueOf((Double) ((ISelectionParameter) otherInfo.get(2).parameter).getSelectedItem()));
            } else {
                int otherInfoSize = otherInfo.size();
                for (int i = 0; i < otherInfoSize; i++) {
                    Method method = importSettingClass.getMethod(otherInfo.get(i).methodName, Object.class);
                    if (otherInfo.get(i).parameter instanceof ISelectionParameter) {
                        if (otherInfo.get(i).parameter instanceof ParameterCheckBox) {
                            method.invoke(importSettingClass, "true".equals(((ISelectionParameter) otherInfo.get(i).parameter).getSelectedItem()) ? true : false);
                        } else {
                            method.invoke(importSettingClass, ((ISelectionParameter) otherInfo.get(i).parameter).getSelectedItem());
                        }
                    }
                }
            }
        } catch (Exception e) {
            Application.getActiveApplication().getOutput().output(e);
        }
        DataImport dataImport = new DataImport();
        dataImport.getImportSettings().add(importSetting);
        return dataImport;
    }
}
