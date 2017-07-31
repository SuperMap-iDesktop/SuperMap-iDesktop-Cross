package com.supermap.desktop.utilities;


import com.supermap.desktop.properties.CoreProperties;

/**
 * Created by xie on 2016/8/31.
 */
public enum OverlayAnalystType {
    CLIP,//裁剪
    UNION,//合并
    ERASE,//擦除
    IDENTITY,//同一
    INTERSECT,//求交
    UPDATE,//更新
    XOR;//对称差

    public String defaultResultName(){
        String result = "";
        switch (this) {
            case CLIP:
                result = "result_clip";
                break;
            case UNION:
                result = "result_union";
                break;
            case ERASE:
                result = "result_erase";
                break;
            case IDENTITY:
                result = "result_identity";
                break;
            case INTERSECT:
                result = "result_intersect";
                break;
            case UPDATE:
                result = "result_update";
                break;
            case XOR:
                result = "result_XOR";
                break;
            default:
                break;
        }
        return result;
    }

    public String toString() {
        String result = "";
        switch (this) {
            case CLIP:
                result = CoreProperties.getString("String_OverlayAnalystMethod_Clip");
                break;
            case UNION:
                result = CoreProperties.getString("String_OverlayAnalystMethod_Union");
                break;
            case ERASE:
                result = CoreProperties.getString("String_OverlayAnalystMethod_Erase");
                break;
            case IDENTITY:
                result = CoreProperties.getString("String_OverlayAnalystMethod_Identity");
                break;
            case INTERSECT:
                result = CoreProperties.getString("String_OverlayAnalystMethod_Intersect");
                break;
            case UPDATE:
                result = CoreProperties.getString("String_OverlayAnalystMethod_Update");
                break;
            case XOR:
                result = CoreProperties.getString("String_OverlayAnalystMethod_XOR");
                break;
            default:
                break;
        }
        return result;
    }
}
