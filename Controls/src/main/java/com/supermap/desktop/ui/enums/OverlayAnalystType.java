package com.supermap.desktop.ui.enums;

import com.supermap.desktop.controls.ControlsProperties;

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
                result = "ClipResult";
                break;
            case UNION:
                result = "UnionResult";
                break;
            case ERASE:
                result = "EraseResult";
                break;
            case IDENTITY:
                result = "IntersectResult";
                break;
            case INTERSECT:
                result = "IdentityResult";
                break;
            case UPDATE:
                result = "XORResult";
                break;
            case XOR:
                result = "UpdateResult";
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
                result = ControlsProperties.getString("String_OverlayAnalystMethod_Clip");
                break;
            case UNION:
                result = ControlsProperties.getString("String_OverlayAnalystMethod_Union");
                break;
            case ERASE:
                result = ControlsProperties.getString("String_OverlayAnalystMethod_Erase");
                break;
            case IDENTITY:
                result = ControlsProperties.getString("String_OverlayAnalystMethod_Identity");
                break;
            case INTERSECT:
                result = ControlsProperties.getString("String_OverlayAnalystMethod_Intersect");
                break;
            case UPDATE:
                result = ControlsProperties.getString("String_OverlayAnalystMethod_Update");
                break;
            case XOR:
                result = ControlsProperties.getString("String_OverlayAnalystMethod_XOR");
                break;
            default:
                break;
        }
        return result;
    }
}
