package com.supermap.desktop.iml;

import com.supermap.data.conversion.*;
import com.supermap.desktop.Interface.IImportSetttingTransform;
import com.supermap.desktop.Interface.IPanelTransformFactory;
import com.supermap.desktop.baseUI.PanelTransform;
import com.supermap.desktop.importUI.*;

/**
 * Created by xie on 2016/10/13.
 * 转换参数界面工厂
 */
public class PanelTransformFactory implements IPanelTransformFactory {
    @Override
    public IImportSetttingTransform createPanelTransform(ImportSetting importSetting) {
        IImportSetttingTransform result = new PanelTransform(importSetting);
        if (importSetting instanceof ImportSettingDXF || importSetting instanceof ImportSettingDWG) {
            result = new PanelTransformForD(importSetting);
        } else if (importSetting instanceof ImportSettingKML || importSetting instanceof ImportSettingKMZ) {
            result = new PanelTransformKML(importSetting);
        } else if (importSetting instanceof ImportSettingMAPGIS) {
            result = new PanelTransformMapGIS(importSetting);
        } else if (importSetting instanceof ImportSettingBMP || importSetting instanceof ImportSettingPNG ||
                importSetting instanceof ImportSettingGIF || importSetting instanceof ImportSettingSIT ||
                importSetting instanceof ImportSettingTIF || importSetting instanceof ImportSettingJP2 ||
                importSetting instanceof ImportSettingJPG || importSetting instanceof ImportSettingMrSID ||
                importSetting instanceof ImportSettingECW || importSetting instanceof ImportSettingIMG) {
            //bmp,png,gif,sit,tif,tiff,sid,ecw,img,jpk,jpg,jp2,jpeg
            result = new PanelTransformForImage(importSetting);
        } else if (importSetting instanceof ImportSettingRAW || importSetting instanceof ImportSettingAiBinGrid ||
                importSetting instanceof ImportSettingBIP || importSetting instanceof ImportSettingBIL ||
                importSetting instanceof ImportSettingBSQ || importSetting instanceof ImportSettingGBDEM ||
                importSetting instanceof ImportSettingUSGSDEM || importSetting instanceof ImportSettingTEMSClutter) {
            //raw,bin,bip,bil,bsq,dem
            result = new PanelTransformForGrid(importSetting);
        } else if (importSetting instanceof ImportSettingSHP || importSetting instanceof ImportSettingGRD ||
                importSetting instanceof ImportSettingE00 || importSetting instanceof ImportSettingAiBinGrid ||
                importSetting instanceof ImportSettingDBF || importSetting instanceof ImportSettingLIDAR ||
                importSetting instanceof ImportSettingTAB || importSetting instanceof ImportSettingMIF) {
            //shp,txt,dem,e00,grd,dbf,雷达文件（txt）,tab,mif
            result = new PanelTransformForGRD(importSetting);
        } else if (importSetting instanceof ImportSettingCSV) {
            result = new PanelTransformMicrosoft(importSetting);
        } else if (importSetting instanceof ImportSettingModelOSG || importSetting instanceof ImportSettingModel3DS ||
                importSetting instanceof ImportSettingModelX || importSetting instanceof ImportSettingModelDXF ||
                importSetting instanceof ImportSettingModelFBX) {
            //osgb,osg,3ds,.x,.dxf,.obj,.ifc,.fbx,.dae
            result = new PanelTransformFor3D(importSetting);
        } else if (importSetting instanceof ImportSettingDGN) {
            result = new PanelTransformForDGN(importSetting);
        }
        return result;
    }
}
