package com.supermap.desktop.iml;

import com.supermap.data.conversion.*;
import com.supermap.desktop.Interface.IImportSetttingTransform;
import com.supermap.desktop.Interface.IPanelTransformFactory;
import com.supermap.desktop.baseUI.PanelTransform;
import com.supermap.desktop.importUI.*;
import com.supermap.desktop.localUtilities.FiletypeUtilities;

import java.util.ArrayList;

/**
 * Created by xie on 2016/10/13.
 * 转换参数界面工厂
 */
public class PanelTransformFactory implements IPanelTransformFactory {
    private final int GRID_TYPE = 1;
    private final int VERTICAL_TYPE = 2;
    private final int GRID_AND_VERTICAL_TYPE = 3;
    private final int SAME_TYPE = 4;

    public PanelTransformFactory() {
        //工具类没有公共构造方法
    }

    @Override
    public IImportSetttingTransform createPanelTransform(ImportSetting importSetting) {
        IImportSetttingTransform result = new PanelTransform(importSetting);
        if (isCADType(importSetting)) {
            result = new PanelTransformForD(importSetting);
        } else if (isKMLType(importSetting)) {
            result = new PanelTransformForKML(importSetting);
        } else if (importSetting instanceof ImportSettingMAPGIS) {
            result = new PanelTransformForMapGIS(importSetting);
        } else if (isImageType(importSetting)) {
            //bmp,png,gif,sit,tif,tiff,sid,ecw,img,jpk,jpg,jp2,jpeg
            result = new PanelTransformForImage(importSetting);
        } else if (isClutterType(importSetting)) {
            //raw,bin,bip,bil,bsq,dem
            result = new PanelTransformForGrid(importSetting);
        } else if (isGRDType(importSetting)) {
            //shp,txt,dem,e00,grd,dbf,雷达文件（txt）,tab,mif
            result = new PanelTransformForGRD(importSetting);
        } else if (importSetting instanceof ImportSettingCSV) {
            result = new PanelTransformForMicrosoft(importSetting);
        } else if (is3DSType(importSetting)) {
            //osgb,osg,3ds,.x,.dxf,.obj,.ifc,.fbx,.dae
            result = new PanelTransformFor3D(importSetting);
        } else if (importSetting instanceof ImportSettingDGN) {
            result = new PanelTransformForDGN(importSetting);
        }
        return result;
    }

    @Override
    public IImportSetttingTransform createPanelTransform(ArrayList<PanelImport> panelImports) {
        int layoutType = getImportSettingsType(panelImports);
        IImportSetttingTransform result = new PanelTransform(panelImports, layoutType);
        ImportSetting tempSetting = panelImports.get(0).getImportInfo().getImportSetting();
        if (isSameTypeImportSetting(panelImports)) {
            if (isCADType(tempSetting)) {
                result = new PanelTransformForD(panelImports, layoutType);
            } else if (isKMLType(tempSetting)) {
                result = new PanelTransformForKML(panelImports, layoutType);
            } else if (tempSetting instanceof ImportSettingMAPGIS) {
                result = new PanelTransformForMapGIS(panelImports, layoutType);
            } else if (isImageType(tempSetting)) {
                //bmp,png,gif,sit,tif,tiff,sid,ecw,img,jpk,jpg,jp2,jpeg
                result = new PanelTransformForImage(panelImports, layoutType);
            } else if (isClutterType(tempSetting)) {
                //raw,bin,bip,bil,bsq,dem
                result = new PanelTransformForGrid(panelImports, layoutType);
            } else if (isGRDType(tempSetting)) {
                //shp,txt,dem,e00,grd,dbf,雷达文件（txt）,tab,mif
                result = new PanelTransformForGRD(panelImports, layoutType);
            } else if (tempSetting instanceof ImportSettingCSV) {
                result = new PanelTransformForMicrosoft(panelImports, layoutType);
            } else if (is3DSType(tempSetting)) {
                //osgb,osg,3ds,.x,.dxf,.obj,.ifc,.fbx,.dae
                result = new PanelTransformFor3D(panelImports, layoutType);
            } else if (tempSetting instanceof ImportSettingDGN) {
                result = new PanelTransformForDGN(panelImports, layoutType);
            }
        } else if (isGridTypes(panelImports)) {
            //是否为栅格数据集集合
            result = new PanelTransformForImage(panelImports, layoutType);
        } else if (isVectorTypes(panelImports)) {
            //是否为矢量数据集集合
            result = new PanelTransformForVector(panelImports, layoutType);
        } else {
            //是否为矢量和栅格数据集集合
            result = null;
        }
        return result;
    }

    @Override
    public int getImportSettingsType(ArrayList<PanelImport> panelImports) {
        int importSettingsType = 0;
        if (isSameTypeImportSetting(panelImports)) {
            importSettingsType = SAME_TYPE;
        } else if (isVectorTypes(panelImports)) {
            importSettingsType = VERTICAL_TYPE;
        } else if (isGridTypes(panelImports)) {
            importSettingsType = GRID_TYPE;
        } else {
            importSettingsType = GRID_AND_VERTICAL_TYPE;
        }
        return importSettingsType;
    }

    private boolean is3DSType(ImportSetting importSetting) {
        return importSetting instanceof ImportSettingModelOSG || importSetting instanceof ImportSettingModel3DS ||
                importSetting instanceof ImportSettingModelX || importSetting instanceof ImportSettingModelDXF ||
                importSetting instanceof ImportSettingModelFBX;
    }

    private boolean isGRDType(ImportSetting importSetting) {
        return importSetting instanceof ImportSettingSHP || importSetting instanceof ImportSettingGRD ||
                importSetting instanceof ImportSettingE00 || importSetting instanceof ImportSettingAiBinGrid ||
                importSetting instanceof ImportSettingDBF || importSetting instanceof ImportSettingLIDAR ||
                importSetting instanceof ImportSettingTAB || importSetting instanceof ImportSettingMIF ||
                importSetting instanceof ImportSettingFileGDBVector;
    }

    private boolean isClutterType(ImportSetting importSetting) {
        return importSetting instanceof ImportSettingRAW || importSetting instanceof ImportSettingAiBinGrid ||
                importSetting instanceof ImportSettingBIP || importSetting instanceof ImportSettingBIL ||
                importSetting instanceof ImportSettingBSQ || importSetting instanceof ImportSettingGBDEM ||
                importSetting instanceof ImportSettingUSGSDEM || importSetting instanceof ImportSettingTEMSClutter;
    }

    private boolean isImageType(ImportSetting importSetting) {
        return importSetting instanceof ImportSettingBMP || importSetting instanceof ImportSettingPNG ||
                importSetting instanceof ImportSettingGIF || importSetting instanceof ImportSettingSIT ||
                importSetting instanceof ImportSettingTIF || importSetting instanceof ImportSettingJPG ||
                importSetting instanceof ImportSettingMrSID || importSetting instanceof ImportSettingECW ||
                importSetting instanceof ImportSettingIMG;
    }

    private boolean isCADType(ImportSetting importSetting) {
        return importSetting instanceof ImportSettingDXF || importSetting instanceof ImportSettingDWG;
    }

    private boolean isKMLType(ImportSetting importSetting) {
        return importSetting instanceof ImportSettingKML || importSetting instanceof ImportSettingKMZ;
    }

    private boolean isVectorTypes(ArrayList<PanelImport> panelImports) {
        int count = 0;
        for (PanelImport tempPanelImport : panelImports) {
            for (Object tempFileType : FiletypeUtilities.getVectorValue()) {
                if (tempPanelImport.getImportInfo().getImportSetting().getSourceFileType() == tempFileType) {
                    count++;
                }
            }
        }

        return count == panelImports.size();
    }

    private boolean isGridTypes(ArrayList<PanelImport> panelImports) {
        int count = 0;
        for (PanelImport tempPanelImport : panelImports) {
            for (Object tempFileType : FiletypeUtilities.getGridValue()) {
                if (tempPanelImport.getImportInfo().getImportSetting().getSourceFileType() == tempFileType) {
                    count++;
                }
            }
        }

        return count == panelImports.size();
    }

    private boolean isSameTypeImportSetting(ArrayList<PanelImport> panelImports) {
        boolean isSame = true;
        FileType fileType = panelImports.get(0).getImportInfo().getImportSetting().getSourceFileType();
        for (PanelImport tempPanelImport : panelImports) {
            if (tempPanelImport.getImportInfo().getImportSetting().getSourceFileType() != fileType) {
                isSame = false;
                break;
            }
        }
        return isSame;
    }
}
