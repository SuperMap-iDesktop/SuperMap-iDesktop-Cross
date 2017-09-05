package com.supermap.desktop.process.parameters.ParameterPanels.RasterReclass;

import com.supermap.analyst.spatialanalyst.ReclassMappingTable;
import com.supermap.analyst.spatialanalyst.ReclassPixelFormat;

/**
 * Created by lixiaoyao on 2017/9/4.
 */
public interface IReclassValueChange {

	public void reclassMappingTableChange(ReclassMappingTable reclassMappingTable);

	public void reClassPixelFormat(ReclassPixelFormat reclassPixelFormat);
}
