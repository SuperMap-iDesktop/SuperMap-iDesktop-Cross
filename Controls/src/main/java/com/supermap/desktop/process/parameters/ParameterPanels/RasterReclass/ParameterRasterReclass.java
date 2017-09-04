package com.supermap.desktop.process.parameters.ParameterPanels.RasterReclass;

import com.supermap.analyst.spatialanalyst.ReclassMappingTable;
import com.supermap.analyst.spatialanalyst.ReclassPixelFormat;
import com.supermap.data.DatasetGrid;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.IMultiSelectionParameter;

import java.beans.PropertyChangeEvent;

/**
 * Created by lixiaoyao on 2017/9/1.
 */
public class ParameterRasterReclass extends AbstractParameter implements IMultiSelectionParameter {
	public static final String FIELD_DATASET="dataset";
	public static final String MAPPING_TABLE="MappingTable";
	public static final String PXIEL_FORMAT="PixelFormat";

	@ParameterField(name = FIELD_DATASET)
	private DatasetGrid dataset;
	private ReclassMappingTable reclassMappingTable;
	private ReclassPixelFormat reclassPixelFormat;
	private String describe;

	public ParameterRasterReclass(){}

	public ParameterRasterReclass(String describe){
		this.describe = describe;
	}

	@Override
	public void setSelectedItem(Object item) {

	}

	@Override
	public Object getSelectedItem() {
		return null;
	}

	@Override
	public String getType() {
		return ParameterType.RASTER_RECLASS;
	}

	@Override
	public String getDescribe() {
		return describe;
	}

	public ReclassMappingTable getReclassMappingTable() {
		return this.reclassMappingTable;
	}

	public void setReclassMappingTable(ReclassMappingTable reclassMappingTable) {
		this.reclassMappingTable = reclassMappingTable;
	}

	public ReclassPixelFormat getReclassPixelFormat() {
		return this.reclassPixelFormat;
	}

	public void setReclassPixelFormat(ReclassPixelFormat reclassPixelFormat) {
		this.reclassPixelFormat = reclassPixelFormat;
	}

	public DatasetGrid getDataset() {
		return this.dataset;
	}

	public void setDataset(DatasetGrid dataset) {
		DatasetGrid oldValue=this.dataset;
		this.dataset = dataset;
		firePropertyChangeListener(new PropertyChangeEvent(this, FIELD_DATASET, oldValue, dataset));
	}

}
