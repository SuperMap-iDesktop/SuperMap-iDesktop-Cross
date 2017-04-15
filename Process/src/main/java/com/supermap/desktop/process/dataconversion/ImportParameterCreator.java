package com.supermap.desktop.process.dataconversion;

import com.supermap.data.EncodeType;
import com.supermap.data.conversion.*;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.parameter.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.util.EnumParser;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.utilities.EncodeTypeUtilities;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xie on 2017/3/31.
 */
public class ImportParameterCreator implements IParameterCreator {

    private ParameterCombine parameterCombineResultSet;
    private ParameterCombine parameterCombineParamSet;
    private ParameterFile parameterFile;
    private ParameterTextField datasetName;

    @Override
    public CopyOnWriteArrayList<ReflectInfo> create(Object importSetting) {
        parameterCombineParamSet = null;
        CopyOnWriteArrayList<ReflectInfo> result = new CopyOnWriteArrayList<>();
        if (importSetting instanceof ImportSettingRAW || importSetting instanceof ImportSettingTEMSClutter
                || importSetting instanceof ImportSettingBIP || importSetting instanceof ImportSettingBSQ
                || importSetting instanceof ImportSettingGBDEM || importSetting instanceof ImportSettingUSGSDEM
                || importSetting instanceof ImportSettingBIL || importSetting instanceof ImportSettingGRD) {
            ReflectInfo pyramidBuiltInfo = new ReflectInfo();
            pyramidBuiltInfo.methodName = "setPyramidBuilt";
            pyramidBuiltInfo.parameter = new ParameterCheckBox(ControlsProperties.getString("String_Form_BuildDatasetPyramid"));
            result.add(pyramidBuiltInfo);
            parameterCombineParamSet = new ParameterCombine();
            parameterCombineParamSet.setDescribe(ProcessProperties.getString("String_ParamSet"));
            parameterCombineParamSet.addParameters(pyramidBuiltInfo.parameter);
            return result;
        }
        if (importSetting instanceof ImportSettingSHP || importSetting instanceof ImportSettingE00
                || importSetting instanceof ImportSettingLIDAR || importSetting instanceof ImportSettingTAB
                || importSetting instanceof ImportSettingMIF || importSetting instanceof ImportSettingFileGDBVector) {
            ReflectInfo setAttributeIgnored = new ReflectInfo();
            setAttributeIgnored.methodName = "setAttributeIgnored";
            setAttributeIgnored.parameter = new ParameterCheckBox(CommonProperties.getString("String_IngoreProperty"));
            result.add(setAttributeIgnored);
            parameterCombineParamSet = new ParameterCombine();
            parameterCombineParamSet.setDescribe(ProcessProperties.getString("String_ParamSet"));
            parameterCombineParamSet.addParameters(setAttributeIgnored.parameter);
            return result;
        }
        if (importSetting instanceof ImportSettingDGN) {
            ReflectInfo importCellAsPoint = new ReflectInfo();
            importCellAsPoint.methodName = "setImportingCellAsPoint";
            ParameterCheckBox parameterImportingCellAsPoint = new ParameterCheckBox(CommonProperties.getString("String_ImportCellAsPoint"));
            parameterImportingCellAsPoint.setSelectedItem(((ImportSettingDGN) importSetting).isImportingCellAsPoint() ? "true" : "false");
            importCellAsPoint.parameter = parameterImportingCellAsPoint;

            ReflectInfo setImportingByLayer = new ReflectInfo();
            setImportingByLayer.methodName = "setImportingByLayer";
            ParameterCheckBox parameterImportingByLayer = new ParameterCheckBox(CommonProperties.getString("String_MergeLayer"));
            parameterImportingByLayer.setSelectedItem(((ImportSettingDGN) importSetting).isImportingByLayer() ? "true" : "false");
            setImportingByLayer.parameter = parameterImportingByLayer;

            result.add(importCellAsPoint);
            result.add(setImportingByLayer);
            parameterCombineParamSet = new ParameterCombine();
            parameterCombineParamSet.setDescribe(ProcessProperties.getString("String_ParamSet"));
            parameterCombineParamSet.addParameters(parameterImportingCellAsPoint, parameterImportingByLayer);
            return result;
        }
        if (importSetting instanceof ImportSettingDXF || importSetting instanceof ImportSettingDWG) {
            ReflectInfo setCurveSegment = new ReflectInfo();
            setCurveSegment.methodName = "setCurveSegment";
            ReflectInfo setImportingExternalData = new ReflectInfo();
            setImportingExternalData.methodName = "setImportingExternalData";
            ReflectInfo setImportingXRecord = new ReflectInfo();
            setImportingXRecord.methodName = "setImportingXRecord";
            ReflectInfo setImporttingAs3D = new ReflectInfo();
            setImporttingAs3D.methodName = "setImporttingAs3D";
            ReflectInfo setImportingInvisibleLayer = new ReflectInfo();
            setImportingInvisibleLayer.methodName = "setImportingInvisibleLayer";
            ReflectInfo setLWPLineWidthIgnored = new ReflectInfo();
            setLWPLineWidthIgnored.methodName = "setLWPLineWidthIgnored";
            ReflectInfo setImportingByLayer = new ReflectInfo();
            setImportingByLayer.methodName = "setImportingByLayer";
            ReflectInfo setBlockAttributeIgnored = new ReflectInfo();
            setBlockAttributeIgnored.methodName = "setBlockAttributeIgnored";
            ReflectInfo setKeepingParametricPart = new ReflectInfo();
            setKeepingParametricPart.methodName = "setKeepingParametricPart";
            ReflectInfo setImportingBlockAsPoint = new ReflectInfo();
            setImportingBlockAsPoint.methodName = "setImportingBlockAsPoint";

            if (importSetting instanceof ImportSettingDXF) {
                ParameterTextField parameterTextField = new ParameterTextField(CommonProperties.getString("String_CurveSegment"));
                parameterTextField.setSelectedItem(((ImportSettingDXF) importSetting).getCurveSegment());
                setCurveSegment.parameter = parameterTextField;

                ParameterCheckBox parameterImportExternalData = new ParameterCheckBox(CommonProperties.getString("string_ImportExtendsData"));
                parameterImportExternalData.setSelectedItem(((ImportSettingDXF) importSetting).isImportingExternalData() ? "true" : "false");
                setImportingExternalData.parameter = parameterImportExternalData;

                ParameterCheckBox parameterImportingXRecord = new ParameterCheckBox(CommonProperties.getString("String_ImportExtendsRecord"));
                parameterImportingXRecord.setSelectedItem(((ImportSettingDXF) importSetting).isImportingXRecord() ? "true" : "false");
                setImportingXRecord.parameter = parameterImportingXRecord;

                ParameterCheckBox parameterImporttingAs3D = new ParameterCheckBox(CommonProperties.getString("String_SaveHeight"));
                parameterImporttingAs3D.setSelectedItem(((ImportSettingDXF) importSetting).isImporttingAs3D() ? "true" : "false");
                setImporttingAs3D.parameter = parameterImporttingAs3D;

                ParameterCheckBox parameterImportingInvisibleLayer = new ParameterCheckBox(CommonProperties.getString("String_ImportInvisibleLayer"));
                parameterImportingInvisibleLayer.setSelectedItem(((ImportSettingDXF) importSetting).isImportingInvisibleLayer() ? "true" : "false");
                setImportingInvisibleLayer.parameter = parameterImportingInvisibleLayer;

                ParameterCheckBox parameterLWPLineWidthIgnored = new ParameterCheckBox(CommonProperties.getString("String_SaveWPLineWidth"));
                parameterLWPLineWidthIgnored.setSelectedItem(((ImportSettingDXF) importSetting).isLWPLineWidthIgnored() ? "false" : "true");
                setLWPLineWidthIgnored.parameter = parameterLWPLineWidthIgnored;

                ParameterCheckBox parameterImportingByLayer = new ParameterCheckBox(CommonProperties.getString("String_MergeLayer"));
                parameterImportingByLayer.setSelectedItem(((ImportSettingDXF) importSetting).isImportingByLayer() ? "false" : "true");
                setImportingByLayer.parameter = parameterImportingByLayer;

                ParameterCheckBox parameterBlockAttributeIgnored = new ParameterCheckBox(CommonProperties.getString("String_ImportProperty"));
                parameterBlockAttributeIgnored.setSelectedItem(((ImportSettingDXF) importSetting).isBlockAttributeIgnored() ? "false" : "true");
                setBlockAttributeIgnored.parameter = parameterBlockAttributeIgnored;

                ParameterCheckBox parameterKeepingParametricPart = new ParameterCheckBox(CommonProperties.getString("String_SaveField"));
                parameterKeepingParametricPart.setSelectedItem(((ImportSettingDXF) importSetting).isKeepingParametricPart() ? "true" : "false");
                setKeepingParametricPart.parameter = parameterKeepingParametricPart;

                ParameterCheckBox parameterImportingBlockAsPoint = new ParameterCheckBox(CommonProperties.getString("String_ImportingSymbol"));
                parameterImportingBlockAsPoint.setSelectedItem(((ImportSettingDXF) importSetting).isImportingBlockAsPoint() ? "false" : "true");
                setImportingBlockAsPoint.parameter = parameterImportingBlockAsPoint;
            } else {
                ParameterTextField parameterTextField = new ParameterTextField(CommonProperties.getString("String_CurveSegment"));
                parameterTextField.setSelectedItem(((ImportSettingDWG) importSetting).getCurveSegment());
                setCurveSegment.parameter = parameterTextField;

                ParameterCheckBox parameterImportExternalData = new ParameterCheckBox(CommonProperties.getString("string_ImportExtendsData"));
                parameterImportExternalData.setSelectedItem(((ImportSettingDWG) importSetting).isImportingExternalData() ? "true" : "false");
                setImportingExternalData.parameter = parameterImportExternalData;

                ParameterCheckBox parameterImportingXRecord = new ParameterCheckBox(CommonProperties.getString("String_ImportExtendsRecord"));
                parameterImportingXRecord.setSelectedItem(((ImportSettingDWG) importSetting).isImportingXRecord() ? "true" : "false");
                setImportingXRecord.parameter = parameterImportingXRecord;

                ParameterCheckBox parameterImporttingAs3D = new ParameterCheckBox(CommonProperties.getString("String_SaveHeight"));
                parameterImporttingAs3D.setSelectedItem(((ImportSettingDWG) importSetting).isImporttingAs3D() ? "true" : "false");
                setImporttingAs3D.parameter = parameterImporttingAs3D;

                ParameterCheckBox parameterImportingInvisibleLayer = new ParameterCheckBox(CommonProperties.getString("String_ImportInvisibleLayer"));
                parameterImportingInvisibleLayer.setSelectedItem(((ImportSettingDWG) importSetting).isImportingInvisibleLayer() ? "true" : "false");
                setImportingInvisibleLayer.parameter = parameterImportingInvisibleLayer;

                ParameterCheckBox parameterLWPLineWidthIgnored = new ParameterCheckBox(CommonProperties.getString("String_SaveWPLineWidth"));
                parameterLWPLineWidthIgnored.setSelectedItem(((ImportSettingDWG) importSetting).isLWPLineWidthIgnored() ? "false" : "true");
                setLWPLineWidthIgnored.parameter = parameterLWPLineWidthIgnored;

                ParameterCheckBox parameterImportingByLayer = new ParameterCheckBox(CommonProperties.getString("String_MergeLayer"));
                parameterImportingByLayer.setSelectedItem(((ImportSettingDWG) importSetting).isImportingByLayer() ? "false" : "true");
                setImportingByLayer.parameter = parameterImportingByLayer;

                ParameterCheckBox parameterBlockAttributeIgnored = new ParameterCheckBox(CommonProperties.getString("String_ImportProperty"));
                parameterBlockAttributeIgnored.setSelectedItem(((ImportSettingDWG) importSetting).isBlockAttributeIgnored() ? "false" : "true");
                setBlockAttributeIgnored.parameter = parameterBlockAttributeIgnored;

                ParameterCheckBox parameterKeepingParametricPart = new ParameterCheckBox(CommonProperties.getString("String_SaveField"));
                parameterKeepingParametricPart.setSelectedItem(((ImportSettingDWG) importSetting).isKeepingParametricPart() ? "true" : "false");
                setKeepingParametricPart.parameter = parameterKeepingParametricPart;

                ParameterCheckBox parameterImportingBlockAsPoint = new ParameterCheckBox(CommonProperties.getString("String_ImportingSymbol"));
                parameterImportingBlockAsPoint.setSelectedItem(((ImportSettingDWG) importSetting).isImportingBlockAsPoint() ? "false" : "true");
                setImportingBlockAsPoint.parameter = parameterImportingBlockAsPoint;
            }
            result.add(setCurveSegment);
            result.add(setImportingByLayer);
            result.add(setImportingInvisibleLayer);
            result.add(setImporttingAs3D);
            result.add(setImportingBlockAsPoint);
            result.add(setBlockAttributeIgnored);
            result.add(setKeepingParametricPart);
            result.add(setImportingExternalData);
            result.add(setImportingXRecord);
            result.add(setLWPLineWidthIgnored);
            parameterCombineParamSet = new ParameterCombine();
            parameterCombineParamSet.setDescribe(ProcessProperties.getString("String_ParamSet"));
            parameterCombineParamSet.addParameters(
                    setCurveSegment.parameter,
                    new ParameterCombine(ParameterCombine.HORIZONTAL).addParameters(
                            new ParameterCombine().addParameters(setImportingByLayer.parameter, setImportingBlockAsPoint.parameter, setImportingExternalData.parameter),
                            new ParameterCombine().addParameters(setImportingInvisibleLayer.parameter, setBlockAttributeIgnored.parameter, setImportingXRecord.parameter),
                            new ParameterCombine().addParameters(setImporttingAs3D.parameter, setKeepingParametricPart.parameter, setLWPLineWidthIgnored.parameter)));
            return result;
        }
        if (importSetting instanceof ImportSettingSIT) {
            ReflectInfo password = new ReflectInfo();
            password.methodName = "setPassword";
            password.parameter = new ParameterTextField(CoreProperties.getString("String_FormLogin_Password"));
            result.add(password);
            parameterCombineParamSet = new ParameterCombine();
            parameterCombineParamSet.setDescribe(ProcessProperties.getString("String_ParamSet"));
            parameterCombineParamSet.addParameters(password.parameter);
            return result;
        }
        if (importSetting instanceof ImportSettingTIF) {
            ReflectInfo importBandMode = new ReflectInfo();
            importBandMode.methodName = "setMultiBandImportMode";
            ParameterEnum parameterBandMode = new ParameterEnum(new EnumParser(MultiBandImportMode.class, new String[]{"SINGLEBAND", "MULTIBAND", "COMPOSITE"},
                    new String[]{CommonProperties.getString("String_MultiBand_SingleBand"), CommonProperties.getString("String_MultiBand_MultiBand"),
                            CommonProperties.getString("String_MultiBand_Composite")}));
            parameterBandMode.setSelectedItem(MultiBandImportMode.COMPOSITE);
            parameterBandMode.setDescribe(ProcessProperties.getString("String_BandImportMode"));
            importBandMode.parameter = parameterBandMode;

            ReflectInfo pyramidBuiltInfo = new ReflectInfo();
            pyramidBuiltInfo.methodName = "setPyramidBuilt";
            pyramidBuiltInfo.parameter = new ParameterCheckBox(ControlsProperties.getString("String_Form_BuildDatasetPyramid"));

            ReflectInfo setWorldFilePath = new ReflectInfo();
            setWorldFilePath.methodName = "setWorldFilePath";
            setWorldFilePath.parameter = new ParameterFile(CommonProperties.getString("String_WorldFile"));

            result.add(importBandMode);
            result.add(pyramidBuiltInfo);
            result.add(setWorldFilePath);
            parameterCombineParamSet = new ParameterCombine();
            parameterCombineParamSet.setDescribe(ProcessProperties.getString("String_ParamSet"));
            parameterCombineParamSet.addParameters(new ParameterCombine(ParameterCombine.HORIZONTAL).addParameters(importBandMode.parameter, pyramidBuiltInfo.parameter), setWorldFilePath.parameter);
            return result;
        }
        if (importSetting instanceof ImportSettingIMG) {
            ReflectInfo importBandMode = new ReflectInfo();
            importBandMode.methodName = "setMultiBandImportMode";
            ParameterEnum parameterBandMode = new ParameterEnum(new EnumParser(MultiBandImportMode.class, new String[]{"SINGLEBAND", "MULTIBAND", "COMPOSITE"},
                    new String[]{CommonProperties.getString("String_MultiBand_SingleBand"), CommonProperties.getString("String_MultiBand_MultiBand"),
                            CommonProperties.getString("String_MultiBand_Composite")}));
            parameterBandMode.setDescribe(ProcessProperties.getString("String_BandImportMode"));
            parameterBandMode.setSelectedItem(MultiBandImportMode.COMPOSITE);
            importBandMode.parameter = parameterBandMode;

            ReflectInfo pyramidBuiltInfo = new ReflectInfo();
            pyramidBuiltInfo.methodName = "setPyramidBuilt";
            pyramidBuiltInfo.parameter = new ParameterCheckBox(ControlsProperties.getString("String_Form_BuildDatasetPyramid"));
            result.add(importBandMode);
            result.add(pyramidBuiltInfo);
            parameterCombineParamSet = new ParameterCombine();
            parameterCombineParamSet.setDescribe(ProcessProperties.getString("String_ParamSet"));
            parameterCombineParamSet.addParameters(importBandMode.parameter, pyramidBuiltInfo.parameter);
            return result;
        }
        if (importSetting instanceof ImportSettingMrSID || importSetting instanceof ImportSettingECW) {
            ReflectInfo importBandMode = new ReflectInfo();
            importBandMode.methodName = "setMultiBandImportMode";
            ParameterEnum parameterBandMode = new ParameterEnum(new EnumParser(MultiBandImportMode.class, new String[]{"SINGLEBAND", "MULTIBAND", "COMPOSITE"},
                    new String[]{CommonProperties.getString("String_MultiBand_SingleBand"), CommonProperties.getString("String_MultiBand_MultiBand"),
                            CommonProperties.getString("String_MultiBand_Composite")}));
            parameterBandMode.setSelectedItem(MultiBandImportMode.MULTIBAND);
            importBandMode.parameter = parameterBandMode;
            result.add(importBandMode);
            parameterCombineParamSet = new ParameterCombine();
            parameterCombineParamSet.setDescribe(ProcessProperties.getString("String_ParamSet"));
            parameterCombineParamSet.addParameters(parameterBandMode);
            return result;
        }
        if (importSetting instanceof ImportSettingBMP || importSetting instanceof ImportSettingPNG
                || importSetting instanceof ImportSettingJPG || importSetting instanceof ImportSettingGIF) {
            ReflectInfo pyramidBuiltInfo = new ReflectInfo();
            pyramidBuiltInfo.methodName = "setPyramidBuilt";
            pyramidBuiltInfo.parameter = new ParameterCheckBox(ControlsProperties.getString("String_Form_BuildDatasetPyramid"));

            ReflectInfo setWorldFilePath = new ReflectInfo();
            setWorldFilePath.methodName = "setWorldFilePath";
            setWorldFilePath.parameter = new ParameterFile(CommonProperties.getString("String_WorldFile"));
            result.add(pyramidBuiltInfo);
            result.add(setWorldFilePath);
            parameterCombineParamSet = new ParameterCombine();
            parameterCombineParamSet.setDescribe(ProcessProperties.getString("String_ParamSet"));
            parameterCombineParamSet.addParameters(pyramidBuiltInfo.parameter, setWorldFilePath.parameter);
            return result;
        }
        if (importSetting instanceof ImportSettingKML || importSetting instanceof ImportSettingKMZ) {
            ReflectInfo setUnvisibleObjectIgnored = new ReflectInfo();
            setUnvisibleObjectIgnored.methodName = "setUnvisibleObjectIgnored";
            setUnvisibleObjectIgnored.parameter = new ParameterCheckBox(CommonProperties.getString("String_ImportUnvisibleObject"));
            result.add(setUnvisibleObjectIgnored);
            parameterCombineParamSet = new ParameterCombine();
            parameterCombineParamSet.setDescribe(ProcessProperties.getString("String_ParamSet"));
            parameterCombineParamSet.addParameters(setUnvisibleObjectIgnored.parameter);
            return result;
        }
        if (importSetting instanceof ImportSettingMAPGIS) {
            ReflectInfo setColorIndexFilePath = new ReflectInfo();
            setColorIndexFilePath.methodName = "setColorIndexFilePath";
            setColorIndexFilePath.parameter = new ParameterFile(CommonProperties.getString("String_ColorIndexFile"));
            result.add(setColorIndexFilePath);
            parameterCombineParamSet = new ParameterCombine();
            parameterCombineParamSet.setDescribe(ProcessProperties.getString("String_ParamSet"));
            parameterCombineParamSet.addParameters(setColorIndexFilePath.parameter);
            return result;
        }
        if (importSetting instanceof ImportSettingCSV) {
            ReflectInfo setSeparator = new ReflectInfo();
            setSeparator.methodName = "setSeparator";
            ParameterTextField parameterSeparator = new ParameterTextField(CommonProperties.getString("String_Separator"));
            parameterSeparator.setSelectedItem(",");
            setSeparator.parameter = parameterSeparator;
            ReflectInfo setFirstRowIsField = new ReflectInfo();
            setFirstRowIsField.methodName = "setFirstRowIsField";
            setFirstRowIsField.parameter = new ParameterCheckBox(CommonProperties.getString("String_FirstRowisField"));

            result.add(setSeparator);
            result.add(setFirstRowIsField);
            parameterCombineParamSet = new ParameterCombine();
            parameterCombineParamSet.setDescribe(ProcessProperties.getString("String_ParamSet"));
            parameterCombineParamSet.addParameters(setSeparator.parameter, setFirstRowIsField.parameter);
            return result;
        }
        if (importSetting instanceof ImportSettingModelOSG || importSetting instanceof ImportSettingModelX
                || importSetting instanceof ImportSettingModelDXF || importSetting instanceof ImportSettingModelFBX
                || importSetting instanceof ImportSettingModelFLT || importSetting instanceof ImportSettingModel3DS) {

            ReflectInfo setX = new ReflectInfo();
            setX.methodName = "setX";
            ParameterTextField textFieldX = new ParameterTextField(CommonProperties.getString("string_longitude"));
            textFieldX.setSelectedItem("0");
            setX.parameter = textFieldX;

            ReflectInfo setY = new ReflectInfo();
            setY.methodName = "setY";
            ParameterTextField textFieldY = new ParameterTextField(CommonProperties.getString("string_latitude"));
            textFieldY.setSelectedItem("0");
            setY.parameter = textFieldY;

            ReflectInfo setZ = new ReflectInfo();
            setZ.methodName = "setZ";
            ParameterTextField textFieldZ = new ParameterTextField(CommonProperties.getString("string_elevation"));
            textFieldZ.setSelectedItem("0");
            setZ.parameter = textFieldZ;
            result.add(setX);
            result.add(setY);
            result.add(setZ);
            parameterCombineParamSet = new ParameterCombine();
            parameterCombineParamSet.setDescribe(ProcessProperties.getString("String_ParamSet"));
            parameterCombineParamSet.setCombineType(ParameterCombine.HORIZONTAL);
            parameterCombineParamSet.addParameters(setX.parameter, setY.parameter, setZ.parameter);
            return result;
        }
        return null;
    }

    public ParameterTextField getDatasetName() {
        return datasetName;
    }

    @Override
    public CopyOnWriteArrayList<ReflectInfo> createDefault(Object o, final String importType) {

        CopyOnWriteArrayList<ReflectInfo> result = new CopyOnWriteArrayList<>();
        //Target dataset reflect info
        ReflectInfo targetDatasource = new ReflectInfo();
        targetDatasource.methodName = "setTargetDatasource";
        ParameterDatasource datasource = new ParameterDatasource();
        datasource.setDescribe(CommonProperties.getString(CommonProperties.Label_Datasource));
        targetDatasource.parameter = datasource;
        result.add(targetDatasource);

        final ReflectInfo targetDatasetName = new ReflectInfo();
        targetDatasetName.methodName = "setTargetDatasetName";
        datasetName = new ParameterTextField(CommonProperties.getString(CommonProperties.Label_Dataset));
        if (o instanceof ImportSetting) {
            datasetName.setSelectedItem(((ImportSetting) o).getTargetDatasetName());
        }
        targetDatasetName.parameter = datasetName;
        result.add(targetDatasetName);

        //EncodeType reflect info
        ParameterEnum encodeType = createEnumParser((ImportSetting) o);
        if (null != encodeType) {
            ReflectInfo setEncodeType = new ReflectInfo();
            setEncodeType.methodName = "setEncodeType";
            setEncodeType.parameter = encodeType;
            encodeType.setDescribe(ProcessProperties.getString("label_encodingType"));
            result.add(setEncodeType);
        }

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
        result.add(importModeInfo);
        parameterCombineResultSet = new ParameterCombine();
        parameterCombineResultSet.setDescribe(CommonProperties.getString("String_ResultSet"));
        ParameterCombine parameterCombineSecond = encodeType != null ? new ParameterCombine(ParameterCombine.HORIZONTAL).addParameters(encodeType, importMode) : new ParameterCombine().addParameters(importMode);
        parameterCombineResultSet.addParameters(
                new ParameterCombine(ParameterCombine.HORIZONTAL).addParameters(datasource, datasetName),
                parameterCombineSecond
        );
        //
        ReflectInfo parameterFileInfo = new ReflectInfo();
        parameterFileInfo.methodName = "setSourceFilePath";
        parameterFile = new ParameterFile(ProcessProperties.getString("label_ChooseFile"));
        parameterFile.setImportType(importType);
        parameterFileInfo.parameter = parameterFile;
        result.add(parameterFileInfo);
        return result;
    }

    @Override
    public ParameterFile getParameterFile() {
        return parameterFile;
    }

    @Override
    public IParameter getParameterCombineResultSet() {
        return parameterCombineResultSet;
    }

    @Override
    public IParameter getParameterCombineParamSet() {
        return parameterCombineParamSet;
    }


    private ParameterEnum createEnumParser(ImportSetting importSetting) {
        ParameterEnum result = null;
        EnumParser parser = new EnumParser();
        if (importSetting instanceof ImportSettingWOR || importSetting instanceof ImportSettingTAB
                || importSetting instanceof ImportSettingMIF || importSetting instanceof ImportSettingDWG
                || importSetting instanceof ImportSettingDXF || importSetting instanceof ImportSettingKML
                || importSetting instanceof ImportSettingKMZ || importSetting instanceof ImportSettingMAPGIS
                || importSetting instanceof ImportSettingDGN || importSetting instanceof ImportSettingLIDAR
                || importSetting instanceof ImportSettingSHP || importSetting instanceof ImportSettingE00
                || importSetting instanceof ImportSettingDBF || importSetting instanceof ImportSettingBIL
                || importSetting instanceof ImportSettingBSQ || importSetting instanceof ImportSettingBIP
                || importSetting instanceof ImportSettingTEMSClutter || importSetting instanceof ImportSettingVCT
                || importSetting instanceof ImportSettingRAW || importSetting instanceof ImportSettingGJB
                || importSetting instanceof ImportSettingTEMSVector || importSetting instanceof ImportSettingTEMSBuildingVector
                || importSetting instanceof ImportSettingFileGDBVector) {
            parser.setEnumNames(new String[]{"NONE", "BYTE", "INT16", "INT24", "INT32"});
            parser.setChName(new String[]{
                    EncodeTypeUtilities.toString(EncodeType.NONE),
                    EncodeTypeUtilities.toString(EncodeType.BYTE),
                    EncodeTypeUtilities.toString(EncodeType.INT16),
                    EncodeTypeUtilities.toString(EncodeType.INT24),
                    EncodeTypeUtilities.toString(EncodeType.INT32)
            });
            parser.setEnumClass(EncodeType.class);
            parser.parse();
            result = new ParameterEnum(parser);
            result.setSelectedItem(EncodeType.NONE);
        }
        if (importSetting instanceof ImportSettingGRD
                || importSetting instanceof ImportSettingGBDEM || importSetting instanceof ImportSettingUSGSDEM) {
            parser.setEnumNames(new String[]{"NONE", "SGL", "LZW"});
            parser.setChName(new String[]{CommonProperties.getString("String_EncodeType_None"), "SGL", "LZW"});
            parser.setEnumClass(EncodeType.class);
            parser.parse();
            result = new ParameterEnum(parser);
            result.setSelectedItem(EncodeType.NONE);
        }
        if (importSetting instanceof ImportSettingJPG || importSetting instanceof ImportSettingJP2 ||
                importSetting instanceof ImportSettingPNG || importSetting instanceof ImportSettingBMP ||
                importSetting instanceof ImportSettingIMG || importSetting instanceof ImportSettingTIF ||
                importSetting instanceof ImportSettingGIF || importSetting instanceof ImportSettingMrSID
                || importSetting instanceof ImportSettingSIT) {
            parser.setChName(new String[]{CommonProperties.getString("String_EncodeType_None"), "DCT", "PNG", "LZW"});
            parser.setEnumNames(new String[]{"NONE", "DCT", "PNG", "LZW"});
            parser.setEnumClass(EncodeType.class);
            parser.parse();
            result = new ParameterEnum(parser);
            result.setSelectedItem(EncodeType.DCT);
        }
        if (importSetting instanceof ImportSettingECW) {
            parser.setEnumNames(new String[]{"NONE"});
            parser.setChName(new String[]{CommonProperties.getString("String_EncodeType_None")});
            parser.setEnumClass(EncodeType.class);
            parser.parse();
            result = new ParameterEnum(parser);
            result.setSelectedItem(EncodeType.NONE);
        }
        return result;
    }

}