package com.supermap.desktop.ui.controls;

import com.supermap.data.Geometry;
import com.supermap.data.Resources;
import com.supermap.data.Toolkit;

import java.awt.*;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: SuperMap GIS Technologies Inc.</p>
 *
 * @author 孔令亮
 * @version 2.0
 */
public class InternalToolkitControl extends Toolkit {
    public InternalToolkitControl() {
    	//默认实现，后续进行初始化操作
    }

	public static boolean internalDraw(Geometry geometry,
	                                   Resources resources, Graphics graphics){
        return Toolkit.internalDraw(geometry, resources, graphics);
    }
    
    static double Internal_DBL_MAX_VALUE = Toolkit.DBL_MAX_VALUE;
    static double Internal_DBL_MIN_VALUE = Toolkit.DBL_MIN_VALUE;
    static float Internal_FLT_MAX_VALUE = Toolkit.FLT_MAX_VALUE;
    static float Internal_FLT_MIN_VALUE = Toolkit.FLT_MIN_VALUE;
    
}
