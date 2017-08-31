package com.supermap.desktop.controls.colorsTable;

import com.supermap.data.ColorDictionary;

/**
 * Created by lixiaoyao on 2017/3/8.
 */
public interface ColorTableXmlInterface {
    /**
     * 建立XML文档
     * @param fileName 文件全路径名称
     * @param colorDictionary 要输出的颜色表
     */
    public boolean createXml(String fileName, ColorDictionary colorDictionary);
    /**
     * 解析XML文档
     * @param fileName 文件全路径名称
     */
    public ColorDictionary parserXml(String fileName);
}
