package com.supermap.desktop.localUtilities;

import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.iml.FileTypeLocale;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.SystemPropertyUtilities;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created by xie on 2016/10/14.
 * 文件操作工具类
 */
public class LocalFileUtilities {
    public LocalFileUtilities() {
        // 工具类不提供公共的构造函数
    }

    public static String LastFileFilter = "";// 上一次打开的保留的文件过滤节点

    public static SmFileChoose createExportFileChooser(String filePath) {
        if (!SmFileChoose.isModuleExist("DataExportFrame_OutPutDirectories")) {
            SmFileChoose.addNewNode("", CommonProperties.getString("String_DefaultFilePath"), DataConversionProperties.getString("String_Export"),
                    "DataExportFrame_OutPutDirectories", "GetDirectories");
        }
        SmFileChoose tempfileChooser = new SmFileChoose("DataExportFrame_OutPutDirectories");

        if (!StringUtilities.isNullOrEmpty(filePath)) {
            tempfileChooser.setCurrentDirectory(new File(filePath));
        }
        tempfileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        return tempfileChooser;
    }

    /**
     * 创建导入的文件选择器
     *
     * @return
     */
    public static SmFileChoose createImportFileChooser() {
        if (!SmFileChoose.isModuleExist("CommonFunction")) {
            if (SystemPropertyUtilities.isWindows()) {
                String fileFilters = SmFileChoose.bulidFileFilters(
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[0], FileTypeLocale.getExtensionsnew()),
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[1], "dxf", "dwg"),
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[2], "shp", "grd", "txt", "e00", "dem", "dbf"),
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[3], "tab", "mif", "wor"),
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[4], "wat", "wan", "wal", "wap"),
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[5], "csv", "dbf"),
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[6], "sit", "img", "tif", "tiff", "bmp", "png", "gif", "jpg", "jpeg", "jp2", "jpk", "sid", "ecw"),
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[7], "osgb", "3ds", "dxf", "x", "fbx"),
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[8], "kml", "kmz"),
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[9], "dem", "bil", "raw", "bsq", "bip", "b"),
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[10], "txt"),
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[11], "dgn", "vct"),
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[12], "json"),
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[13], "gpx")
                );
                SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
                        DataConversionProperties.getString("String_FileType"), "CommonFunction", "OpenMany");
            } else {
                String fileFilters = SmFileChoose.bulidFileFilters(
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnewforlinux()[0], FileTypeLocale.getExtensionsnewforlinux()),
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnewforlinux()[1], "shp", "grd", "txt", "e00", "dem", "dbf"),
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnewforlinux()[2], "tab", "mif", "wor"),
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnewforlinux()[3], "csv"),
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnewforlinux()[4], "sit", "img", "tif", "tiff", "bmp", "png", "gif", "jpg", "jpeg", "jp2", "jpk", "sid", "ecw"),
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnewforlinux()[5], "3ds"),
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnewforlinux()[6], "kml", "kmz"),
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnewforlinux()[7], "dem", "bil", "raw", "bsq", "bip"),
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnewforlinux()[8], "vct"),
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[9], "json"),
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnewforlinux()[10], "gpx")
                );
                SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
                        DataConversionProperties.getString("String_FileType"), "CommonFunction", "OpenMany");
            }

        }
        SmFileChoose fileChoose = new SmFileChoose("CommonFunction");

        if (LastFileFilter != null) {
            // 设置过滤器为当前选中
            for (int i = 0; i < fileChoose.getChoosableFileFilters().length; i++) {
                FileFilter tempFileFilter = fileChoose.getChoosableFileFilters()[i];
                if (tempFileFilter.getDescription().equals(LastFileFilter)) {
                    fileChoose.setFileFilter(tempFileFilter);
                }
            }
        }
        return fileChoose;
    }
}
