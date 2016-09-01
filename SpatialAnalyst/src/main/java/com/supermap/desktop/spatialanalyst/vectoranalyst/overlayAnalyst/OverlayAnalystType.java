package com.supermap.desktop.spatialanalyst.vectoranalyst.overlayAnalyst;

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
}
