package com.supermap.desktop.geometryoperation.editor;

import com.supermap.data.GeoCompound;
import com.supermap.data.GeoRegion;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Selection;

public class ProtectedDecompose extends AbstractEditor {

	@Override
	public void activate(EditEnvironment environment) {
		// 按需重写
	}

	@Override
	public boolean enble(EditEnvironment environment) {
        boolean result = false;
        Geometry geometry = null;
        Recordset recordset = null;
        IFormMap formMap = environment.getFormMap();
        
        try
        {
            if (formMap != null)
            {
                Layer layer = formMap.getMapControl().getActiveEditableLayer();
                if (layer != null)
                {
                    Selection selection = layer.getSelection();
                    if (selection.getCount() >= 1)
                    {
                        recordset = selection.toRecordset();
                        recordset.moveFirst();
                        while (!recordset.isEOF())
                        {
                            geometry = recordset.getGeometry();
                            if (geometry != null && (geometry.getType() == GeometryType.GEOREGION ||
                                geometry.getType() == GeometryType.GEOCOMPOUND))
                            {
                                if (geometry instanceof GeoRegion)
                                {
                                    result =  ((GeoRegion)geometry).getPartCount() >= 2;
                                    break;
                                }
                                if (geometry instanceof GeoCompound
                      )
                                {
                                    result = ((GeoCompound)geometry).getPartCount() >= 2;
                                    break;
                                }
                            }
//                            CommonToolkit.ReleaseGeometry(ref geometry);//不释放对象，大数据容易崩溃UGDC-1240
                            recordset.moveNext();
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Application.getActiveApplication().getOutput().output(ex);
        }
        finally
        {
if (geometry!=null) {
	geometry.dispose();
}

if (recordset!=null) {
	recordset.close();
	recordset.dispose();
}
        }
        return result;
	}
}
