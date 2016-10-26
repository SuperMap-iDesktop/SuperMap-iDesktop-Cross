package com.supermap.desktop.localUtilities;

import com.supermap.desktop.FileTypeLocale;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.utilities.SystemPropertyUtilities;

import javax.swing.filechooser.FileFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xie on 2016/10/14.
 * 文件操作工具类
 */
public class FileUtilities {
    public FileUtilities() {
        // 工具类不提供公共的构造函数
    }

    private static String LastFileFilter = "";// 上一次打开的保留的文件过滤节点

    /**
     * 获取文件类型，默认以最后一个“.”作为分隔
     *
     * @param filePath
     * @return
     */
    public static String getFileType(String filePath) {
        return isFilePath(filePath) && filePath.contains(".") ? filePath.substring(filePath.lastIndexOf("."), filePath.length()) : "";
    }

    public static String getFileAlias(String filePath) {
        String fileName = isFilePath(filePath) ? filePath.substring(filePath.lastIndexOf("\\"), filePath.length()) : null;
        if (null == fileName || !fileName.contains(".")) {
            return null;
        }
        return null != fileName ? fileName.substring(0, fileName.lastIndexOf(".")) : "";
    }

    private static boolean isFilePath(String filePath) {
        String regex = "[a-zA-Z]:(?:[/\\\\][^/\\\\:*?\"<>|]{1,255})+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(filePath);
        return matcher.matches();
    }

    private static boolean isSameFile(String... filePaths) {
        boolean isSame = true;
        String tempPath = filePaths[0];
        for (String filePath : filePaths) {
            if (!filePath.equals(tempPath)) {
                isSame = false;
            }
        }
        return isSame;
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
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[11], "dgn", "vct"));
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
                        SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnewforlinux()[8], "vct"));
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
        LastFileFilter = fileChoose.getFileFilter().getDescription();
        return fileChoose;
    }
}
