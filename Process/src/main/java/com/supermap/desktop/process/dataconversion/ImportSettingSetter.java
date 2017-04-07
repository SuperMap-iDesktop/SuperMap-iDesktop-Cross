package com.supermap.desktop.process.dataconversion;

import com.supermap.data.Point3D;
import com.supermap.data.conversion.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.parameter.implement.ParameterCheckBox;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.lang.reflect.Method;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xie on 2017/4/6.
 */
public class ImportSettingSetter {
    //Utilities class
    private ImportSettingSetter() {
    }

    public static DataImport setParameter(ImportSetting importSetting, CopyOnWriteArrayList<ReflectInfo> basicInfos, CopyOnWriteArrayList<ReflectInfo> otherInfo) {
        try {
            Class importSettingClass = importSetting.getClass();
            int basicSize = basicInfos.size();
            for (int i = 0; i < basicSize; i++) {
                if (basicInfos.get(i).parameter instanceof ISelectionParameter) {
                    if (basicInfos.get(i).parameter instanceof ParameterCheckBox) {
                        Method method = importSettingClass.getMethod(basicInfos.get(i).methodName, boolean.class);
                        method.invoke(importSetting, "true".equals(((ISelectionParameter) basicInfos.get(i).parameter).getSelectedItem()) ? true : false);
                    } else if (null != ((ISelectionParameter) basicInfos.get(i).parameter).getSelectedItem()) {
                        Method method = importSettingClass.getMethod(basicInfos.get(i).methodName, ((ISelectionParameter) basicInfos.get(i).parameter).getSelectedItem().getClass());
                        method.invoke(importSetting, ((ISelectionParameter) basicInfos.get(i).parameter).getSelectedItem());
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
                Method setPosition = importSettingClass.getMethod("setPosition", point3D.getClass());
                setPosition.invoke(setPosition, point3D);
            } else {
                int otherInfoSize = otherInfo.size();
                for (int i = 0; i < otherInfoSize; i++) {
                    if (otherInfo.get(i).parameter instanceof ISelectionParameter) {
                        if (otherInfo.get(i).parameter instanceof ParameterCheckBox) {
                            Method method = importSettingClass.getMethod(otherInfo.get(i).methodName, boolean.class);
                            method.invoke(importSetting, "true".equals(((ISelectionParameter) otherInfo.get(i).parameter).getSelectedItem()) ? true : false);
                        } else if (null != ((ISelectionParameter) otherInfo.get(i).parameter).getSelectedItem()){
                            Method method = importSettingClass.getMethod(otherInfo.get(i).methodName, ((ISelectionParameter) otherInfo.get(i).parameter).getSelectedItem().getClass());
                            method.invoke(importSetting, ((ISelectionParameter) otherInfo.get(i).parameter).getSelectedItem());
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
