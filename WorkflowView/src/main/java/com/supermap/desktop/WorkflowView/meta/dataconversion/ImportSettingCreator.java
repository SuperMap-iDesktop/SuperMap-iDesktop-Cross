package com.supermap.desktop.WorkflowView.meta.dataconversion;

import com.supermap.data.conversion.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.implement.UserDefineType.ImportSettingGPX;

/**
 * Created by xie on 2017/3/31.
 */
public class ImportSettingCreator implements IImportSettingCreator {
	private final String importSetting = "com.supermap.data.conversion.ImportSetting";

	@Override
	public ImportSetting create(Object o) {
		ImportSetting result = null;
		// o is an import file type
		try {
			String type = o.toString();
			if ("GPS".equalsIgnoreCase(type)) {
				return new ImportSettingGPX();
			}
			if ("B".equalsIgnoreCase(type)) {
				type = "TEMSClutter";
			} else if ("DEM".equalsIgnoreCase(type)) {
				type = "GBDEM";
			} else if ("3DS".equalsIgnoreCase(type) || "X".equalsIgnoreCase(type)) {
				type = "Model" + type.toUpperCase();
			} else if ("OSGB".equalsIgnoreCase(type)) {
				type = "ModelOSG";
			} else if ("TXT".equalsIgnoreCase(type)) {
				type = "GRD";
			} else if ("WAL".equalsIgnoreCase(type) || "WAP".equalsIgnoreCase(type) || "WAT".equalsIgnoreCase(type) || "WAN".equalsIgnoreCase(type)) {
				type = "MAPGIS";
			} else if ("JPK".equalsIgnoreCase(type)) {
				type = "JP2";
			} else if ("SID".equalsIgnoreCase(type)) {
				type = "MrSID";
			} else if ("TIFF".equalsIgnoreCase(type)) {
				type = "TIF";
			} else if ("JPEG".equalsIgnoreCase(type)) {
				type = "JPG";
			} else if ("GRD_DEM".equalsIgnoreCase(type)) {
				type = "GRD";
			} else if ("GEOJSON".equalsIgnoreCase(type)) {
				type = "GeoJson";
			}
			Class importClass = Class.forName(importSetting + type);
			result = (ImportSetting) importClass.newInstance();
			if (result instanceof ImportSettingWOR) {
				((ImportSettingWOR) result).setTargetWorkspace(Application.getActiveApplication().getWorkspace());
			}else if(result instanceof ImportSettingKML){
				((ImportSettingKML) result).setImportEmptyDataset(true);
			}else if(result instanceof ImportSettingKMZ){
				((ImportSettingKMZ) result).setImportEmptyDataset(true);
			}else if(result instanceof ImportSettingCSV){
				((ImportSettingCSV) result).setImportEmptyDataset(true);
			}else if(result instanceof ImportSettingDGN){
				((ImportSettingDGN) result).setImportEmptyDataset(true);
			}else if(result instanceof ImportSettingGeoJson){
				((ImportSettingGeoJson) result).setImportEmptyDataset(true);
			}
		} catch (ClassNotFoundException e) {
			Application.getActiveApplication().getOutput().output(e);
		} catch (IllegalAccessException e) {
		} catch (InstantiationException e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

}
