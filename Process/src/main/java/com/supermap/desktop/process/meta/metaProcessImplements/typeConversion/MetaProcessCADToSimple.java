package com.supermap.desktop.process.meta.metaProcessImplements.typeConversion;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetTypeUtilities;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.RecordsetUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created By Chens on 2017/7/21 0021
 */
public class MetaProcessCADToSimple extends MetaProcessTypeConversion {
    private static final String INPUT_DATA = "InputData";
    private static final String OUTPUT_DATA = "CADToSimpleResult";

    private ParameterDatasourceConstrained inputDatasource;
    private ParameterSingleDataset inputDataset;
    private ParameterSaveDataset outputData;
    private ParameterComboBox comboBoxType;

    private ArrayList<DatasetType> datasetTypeArrayList = new ArrayList<>();

    public MetaProcessCADToSimple() {
        initParameters();
        initParameterConstraint();
        registerListener();
    }

    private void initParameters() {
        inputDatasource = new ParameterDatasourceConstrained();
        inputDataset = new ParameterSingleDataset(DatasetType.CAD);
        outputData = new ParameterSaveDataset();
        comboBoxType = new ParameterComboBox(ProcessProperties.getString("String_ComboBox_NumberType"));

        Dataset dataset = DatasetUtilities.getDefaultDataset(DatasetType.CAD);
        if (dataset != null) {
            inputDatasource.setSelectedItem(dataset.getDatasource());
            inputDataset.setSelectedItem(dataset);
        }
        initDatasetTypesByQuery();
        resetComboBoxItem();
        outputData.setSelectedItem("result_CADToSimple");

        ParameterCombine inputCombine = new ParameterCombine();
        inputCombine.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
        inputCombine.addParameters(inputDatasource, inputDataset);
        ParameterCombine outputCombine = new ParameterCombine();
        outputCombine.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
        outputCombine.addParameters(outputData);
        ParameterCombine settingCombine = new ParameterCombine();
        settingCombine.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
        settingCombine.addParameters(comboBoxType);

        parameters.setParameters(inputCombine,settingCombine,outputCombine);
        parameters.addInputParameters(INPUT_DATA, DatasetTypes.CAD,inputCombine);
        parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.VECTOR,outputCombine);
    }

    private void registerListener() {
        inputDataset.addPropertyListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                initDatasetTypesByQuery();
                resetComboBoxItem();
            }
        });
    }

    private void initParameterConstraint() {
        EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
        equalDatasourceConstraint.constrained(inputDatasource,ParameterDatasourceConstrained.DATASOURCE_FIELD_NAME);
        equalDatasourceConstraint.constrained(inputDataset,ParameterSingleDataset.DATASOURCE_FIELD_NAME);
    }

    @Override
    public IParameters getParameters() {
        return parameters;
    }

    @Override
    public boolean execute() {
        boolean isSuccessful = false;
        Recordset recordsetResult = null;

        try {
            fireRunning(new RunningEvent(this,0,"start"));

            DatasetVector src = null;
            if (parameters.getInputs().getData(INPUT_DATA).getValue() != null) {
                src = (DatasetVector) parameters.getInputs().getData(INPUT_DATA).getValue();
            } else {
                src = (DatasetVector) inputDataset.getSelectedDataset();
            }
            DatasetVectorInfo datasetVectorInfo = new DatasetVectorInfo();
            datasetVectorInfo.setName(outputData.getResultDatasource().getDatasets().getAvailableDatasetName(outputData.getDatasetName()));
            datasetVectorInfo.setType((DatasetType) comboBoxType.getSelectedData());
            DatasetVector resultDataset = outputData.getResultDatasource().getDatasets().create(datasetVectorInfo);
            resultDataset.setPrjCoordSys(src.getPrjCoordSys());
            for (int i = 0; i < src.getFieldInfos().getCount(); i++) {
                FieldInfo fieldInfo = src.getFieldInfos().get(i);
                if (!fieldInfo.isSystemField() && !fieldInfo.getName().toLowerCase().equals("smuserid")) {
                    resultDataset.getFieldInfos().add(fieldInfo);
                }
            }
            recordsetResult = resultDataset.getRecordset(false, CursorType.DYNAMIC);
            recordsetResult.addSteppedListener(steppedListener);
            recordsetResult.getBatch().setMaxRecordCount(2000);
            recordsetResult.getBatch().begin();

            Recordset recordsetInput = src.getRecordset(false, CursorType.DYNAMIC);
            while (!recordsetInput.isEOF()) {
                Geometry geometry = null;
                try {
                    geometry = recordsetInput.getGeometry();
                    Map<String, Object> value = mergePropertyData(resultDataset, recordsetInput.getFieldInfos(), RecordsetUtilities.getFieldValuesIgnoreCase(recordsetInput));
                    isSuccessful = convert(recordsetResult, geometry, value);
                }finally {
                    if (geometry != null) {
                        geometry.dispose();
                    }
                }
                recordsetInput.moveNext();
            }
            recordsetResult.getBatch().update();
            recordsetInput.close();
            recordsetInput.dispose();
            this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(resultDataset);
            fireRunning(new RunningEvent(this,100,"finish"));
        } catch (Exception e) {
            Application.getActiveApplication().getOutput().output(e);
        } finally {
            if (recordsetResult != null) {
                recordsetResult.removeSteppedListener(steppedListener);
                recordsetResult.close();
                recordsetResult.dispose();
            }
        }

        return isSuccessful;
    }

    @Override
    public String getKey() {
        return MetaKeys.Conversion_CADToSimple;
    }

    @Override
    public String getTitle() {
        return ProcessProperties.getString("String_Title_CADToSimple");
    }

    private void initDatasetTypesByQuery() {
        ArrayList<GeometryType> geometryTypes = new ArrayList<>();
        try {
            DatasetVector datasetVector = (DatasetVector) inputDataset.getSelectedDataset();
            if (datasetVector != null && datasetVector.getType().equals(DatasetType.CAD)) {
                QueryParameter queryParameter = new QueryParameter();
                queryParameter.setHasGeometry(false);
                queryParameter.setCursorType(CursorType.STATIC);
                queryParameter.setGroupBy(new String[] { "SmGeoType" });
                Recordset recordsetGroup = datasetVector.query(queryParameter);
                if (recordsetGroup != null && recordsetGroup.getRecordCount() > 0)
                {
                    recordsetGroup.moveFirst();
                    while (!recordsetGroup.isEOF())
                    {
                        if (geometryTypes.size() <= 7)
                        {
                            short value = recordsetGroup.getInt16("SmGeoType");
                            GeometryType geometryType = GeometryType.newInstance(value);
                            if (geometryType != GeometryType.GEOCOMPOUND && !geometryTypes.contains(geometryType))
                            {
                                geometryTypes.add(geometryType);
                            }
                            recordsetGroup.moveNext();
                        }
                        else
                        {
                            break;
                        }
                    }
                    recordsetGroup.close();
                    recordsetGroup.dispose();
                    //查询复合对象进行遍历
                    if (geometryTypes.size() <= 7)
                    {
                        queryParameter.setHasGeometry(false);
                        queryParameter.setCursorType(CursorType.STATIC);
                        queryParameter.setAttributeFilter("SmGeoType='1000'");
                        queryParameter.setGroupBy(null);
                        queryParameter.setHasGeometry(true);
                        Recordset recordset = datasetVector.query(queryParameter);
                        recordset.moveFirst();
                        while (!recordset.isEOF())
                        {
                            Geometry geoItem = recordset.getGeometry();
                            if (geoItem != null)
                            {
                                Geometry[] geometrys = null;
                                geometrys = ((GeoCompound)geoItem).divide(false);
                                if (geometryTypes.size() <= 7)
                                {
                                    for (int i = 0; i < geometrys.length; i++)
                                    {
                                        if (!geometryTypes.contains(geometrys[i].getType()))
                                        {
                                            geometryTypes.add(geometrys[i].getType());
                                        }
                                        geometrys[i].dispose();
                                    }
                                    geoItem.dispose();
                                    recordset.moveNext();
                                }
                                else
                                {
                                    break;
                                }
                            }
                        }
                        recordset.close();
                        recordset.dispose();
                    }
                    addDatasetType(datasetTypeArrayList, geometryTypes);
                }
            }
        } catch (Exception e) {
            Application.getActiveApplication().getOutput().output(e.getMessage());
        }
    }

    private void addDatasetType(ArrayList<DatasetType> datasetTypes, ArrayList<GeometryType> geometryTypes) {
        DatasetType datasetType = DatasetType.CAD;
        for (GeometryType geometryType : geometryTypes) {
            if (geometryType.equals(GeometryType.GEOLINEM) || geometryType.equals(GeometryType.GEOLINE)
                    || geometryType.equals(GeometryType.GEOARC) || geometryType.equals(GeometryType.GEOBSPLINE)
                    || geometryType.equals(GeometryType.GEOCARDINAL) || geometryType.equals(GeometryType.GEOCURVE)
                    || geometryType.equals(GeometryType.GEOELLIPTICARC) || geometryType.equals(GeometryType.GEOLINE)) {
                datasetType = DatasetType.LINE;
            } else if (geometryType.equals(GeometryType.GEOCIRCLE) || geometryType.equals(GeometryType.GEORECTANGLE)
                    || geometryType.equals(GeometryType.GEOROUNDRECTANGLE) || geometryType.equals(GeometryType.GEOELLIPSE)
                    || geometryType.equals(GeometryType.GEOPIE) || geometryType.equals(GeometryType.GEOCHORD)
                    || geometryType.equals(GeometryType.GEOREGION)) {
                datasetType = DatasetType.REGION;
            } else if (geometryType.equals(GeometryType.GEOPOINT)) {
                datasetType = DatasetType.POINT;
            } else if (geometryType.equals(GeometryType.GEOTEXT)) {
                datasetType = DatasetType.TEXT;
            } else if (geometryType.equals(GeometryType.GEOPOINT3D)) {
                datasetType = DatasetType.POINT3D;
            } else if (geometryType.equals(GeometryType.GEOLINE3D)) {
                datasetType = DatasetType.LINE3D;
            } else if (geometryType.equals(GeometryType.GEOREGION3D)) {
                datasetType = DatasetType.REGION3D;
            } else if (geometryType.equals(GeometryType.GEOMODEL)) {
                datasetType = DatasetType.MODEL;
            }

            if (!datasetTypes.contains(datasetType))
            {
                datasetTypes.add(datasetType);
            }
        }
    }

    private void resetComboBoxItem() {
        comboBoxType.removeAllItems();
        for (DatasetType type : datasetTypeArrayList) {
            comboBoxType.addItem(new ParameterDataNode(DatasetTypeUtilities.toString(type), type));
        }
        comboBoxType.setSelectedItem(comboBoxType.getItemIndex(0));
    }

    private boolean convert(Recordset recordset,Geometry geometry, Map<String, Object> value) {
        boolean isConvert = false;
        DatasetType datasetType = (DatasetType) comboBoxType.getSelectedData();
        GeometryType geometryType = geometry.getType();

        if (((geometryType.equals(GeometryType.GEOLINEM) || geometryType.equals(GeometryType.GEOLINE)
                  || geometryType.equals(GeometryType.GEOARC) || geometryType.equals(GeometryType.GEOBSPLINE)
                  || geometryType.equals(GeometryType.GEOCARDINAL) || geometryType.equals(GeometryType.GEOCURVE)
                  || geometryType.equals(GeometryType.GEOELLIPTICARC) || geometryType.equals(GeometryType.GEOLINE))
                  && datasetType.equals(DatasetType.LINE))
                || ((geometryType.equals(GeometryType.GEOCIRCLE) || geometryType.equals(GeometryType.GEORECTANGLE)
                  || geometryType.equals(GeometryType.GEOROUNDRECTANGLE) || geometryType.equals(GeometryType.GEOELLIPSE)
                  || geometryType.equals(GeometryType.GEOPIE) || geometryType.equals(GeometryType.GEOCHORD)
                  || geometryType.equals(GeometryType.GEOREGION)) && datasetType.equals(DatasetType.REGION))
                || (geometryType.equals(GeometryType.GEOPOINT) && datasetType.equals(DatasetType.POINT))
                || (geometryType.equals(GeometryType.GEOTEXT) && datasetType.equals(DatasetType.TEXT))
                || (geometryType.equals(GeometryType.GEOPOINT3D) && datasetType.equals(DatasetType.POINT3D))
                || (geometryType.equals(GeometryType.GEOLINE3D) && datasetType.equals(DatasetType.LINE3D))
                || (geometryType.equals(GeometryType.GEOREGION3D) && datasetType.equals(DatasetType.REGION3D))
                || (geometryType.equals(GeometryType.GEOMODEL) && datasetType.equals(DatasetType.MODEL))) {
            recordset.addNew(geometry, value);
            isConvert = true;
        } else {
            isConvert = false;
        }
        return isConvert;
    }
}
