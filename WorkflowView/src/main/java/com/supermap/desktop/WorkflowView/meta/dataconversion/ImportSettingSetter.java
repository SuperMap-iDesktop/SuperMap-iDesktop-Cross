package com.supermap.desktop.WorkflowView.meta.dataconversion;

import com.supermap.data.Charset;
import com.supermap.data.DatasetType;
import com.supermap.data.Point3D;
import com.supermap.data.conversion.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;
import com.supermap.desktop.process.parameter.ipls.*;

import java.lang.reflect.Method;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xie on 2017/4/6.
 */
public class ImportSettingSetter {
	private static final int X = 0;
	private static final int Y = 1;
	private static final int Z = 2;

	//Utilities class
	private ImportSettingSetter() {
	}

	public static DataImport setParameter(ImportSetting importSetting,
	                                      CopyOnWriteArrayList<ReflectInfo> sourceInfo,
	                                      CopyOnWriteArrayList<ReflectInfo> resultInfo,
	                                      CopyOnWriteArrayList<ReflectInfo> otherInfo) {
		if (sourceInfo == null || importSetting == null) {
			return null;
		}
		Class importSettingClass = importSetting.getClass();
		for (int i = 0; i < sourceInfo.size(); i++) {
			if (sourceInfo.get(i).methodName == null) continue;
			try {
				if (sourceInfo.get(i).parameter instanceof ParameterFile) {
					Method method = importSettingClass.getMethod(sourceInfo.get(i).methodName, String.class);
					method.invoke(importSetting, ((ParameterFile) sourceInfo.get(i).parameter).getSelectedItem());
				} else if (sourceInfo.get(i).parameter instanceof ParameterCharset) {
					Method method = importSettingClass.getMethod(sourceInfo.get(i).methodName, Charset.class);
					method.invoke(importSetting, ((ParameterCharset) sourceInfo.get(i).parameter).getSelectedData());
				}
			} catch (Exception e) {
				continue;
			}

		}
		try {
			int basicSize = resultInfo.size();
			for (int i = 0; i < basicSize; i++) {
				//// FIXME: 2017/4/27 空间索引和字段索引参数不是importSetting的，methodName为空
				if (resultInfo.get(i).methodName == null) continue;
				if (resultInfo.get(i).parameter instanceof ParameterDatasetType) {
					Object datasetType = ((ParameterDatasetType) resultInfo.get(i).parameter).getSelectedItem();
					boolean methodValue = false;
					if (datasetType instanceof DatasetType) {
						if (datasetType.equals(DatasetType.CAD)) {
							methodValue = true;
						}
						if (datasetType.equals(DatasetType.GRID)) {
							methodValue = true;
						}
					} else {
						String type = datasetType.toString();
						if (type.equals(ProcessProperties.getString("String_datasetType3D"))) {
							methodValue = true;
						}
						if (type.equals(ProcessProperties.getString("String_DatasetType_CAD"))) {
							methodValue = true;
						}
						if (type.equals(ProcessProperties.getString("string_comboboxitem_grid"))) {
							methodValue = true;
						}
					}
					Method method = importSettingClass.getMethod(resultInfo.get(i).methodName, boolean.class);
					invokeMethod(method, importSetting, methodValue);
				} else if (resultInfo.get(i).parameter instanceof ParameterEnum) {
					Method method = importSettingClass.getMethod(resultInfo.get(i).methodName, ((ParameterEnum) resultInfo.get(i).parameter).getSelectedData().getClass());
					invokeMethod(method, importSetting, ((ParameterEnum) resultInfo.get(i).parameter).getSelectedData());
				} else if (resultInfo.get(i).parameter instanceof ISelectionParameter) {
					if (resultInfo.get(i).parameter instanceof ParameterCheckBox) {
						Method method = importSettingClass.getMethod(resultInfo.get(i).methodName, boolean.class);
						invokeMethod(method, importSetting, "true".equals(((ISelectionParameter) resultInfo.get(i).parameter).getSelectedItem()) ? true : false);
					} else if (null != ((ISelectionParameter) resultInfo.get(i).parameter).getSelectedItem()) {
						Method method = importSettingClass.getMethod(resultInfo.get(i).methodName, ((ISelectionParameter) resultInfo.get(i).parameter).getSelectedItem().getClass());
						invokeMethod(method, importSetting, ((ISelectionParameter) resultInfo.get(i).parameter).getSelectedItem());
					}
				}
			}
			if (importSetting instanceof ImportSettingModelOSG || importSetting instanceof ImportSettingModelX
					|| importSetting instanceof ImportSettingModelDXF || importSetting instanceof ImportSettingModelFBX
					|| importSetting instanceof ImportSettingModelFLT || importSetting instanceof ImportSettingModel3DS) {
				Point3D point3D = new Point3D();
				point3D.setX(Double.valueOf(((ISelectionParameter) otherInfo.get(X).parameter).getSelectedItem().toString()));
				point3D.setY(Double.valueOf(((ISelectionParameter) otherInfo.get(Y).parameter).getSelectedItem().toString()));
				point3D.setZ(Double.valueOf(((ISelectionParameter) otherInfo.get(Z).parameter).getSelectedItem().toString()));
				Method setPosition = importSettingClass.getMethod("setPosition", point3D.getClass());
				setPosition.invoke(importSetting, point3D);
			} else if (null != otherInfo) {
				int otherInfoSize = otherInfo.size();
				for (int i = 0; i < otherInfoSize; i++) {
					if (otherInfo.get(i).parameter instanceof ISelectionParameter) {
						if (otherInfo.get(i).parameter instanceof ParameterCheckBox) {
							Method method = importSettingClass.getMethod(otherInfo.get(i).methodName, boolean.class);
							//合并图层参数设置问题
							boolean value = otherInfo.get(i).methodName.equals("setImportingByLayer")
									? ("false".equals(((ISelectionParameter) otherInfo.get(i).parameter).getSelectedItem()) ? true : false)
									: ("true".equals(((ISelectionParameter) otherInfo.get(i).parameter).getSelectedItem()) ? true : false);
							method.invoke(importSetting, value);
						} else if (null != ((ISelectionParameter) otherInfo.get(i).parameter).getSelectedItem()) {
							Method method = null;
							if ("setCurveSegment".equals(otherInfo.get(i).methodName)) {
								method = importSettingClass.getMethod(otherInfo.get(i).methodName, int.class);
								method.invoke(importSetting, Integer.valueOf(((ISelectionParameter) otherInfo.get(i).parameter).getSelectedItem().toString()));
							} else {
								method = importSettingClass.getMethod(otherInfo.get(i).methodName, ((ISelectionParameter) otherInfo.get(i).parameter).getSelectedItem().getClass());
								method.invoke(importSetting, ((ISelectionParameter) otherInfo.get(i).parameter).getSelectedItem());
							}

						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Application.getActiveApplication().getOutput().output(e);
		}

		DataImport dataImport = new DataImport();
		dataImport.getImportSettings().add(importSetting);
		return dataImport;
	}

	public static void invokeMethod(Method method, Object object, Object... args) {
		try {
			method.invoke(object, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
