package com.supermap.desktop.util;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.supermap.desktop.FileTypeLocale;

/**
 * @author Administrator 为JFileChooser类添加文件过滤器
 */
public class FileChooserFilter {

	private FileChooserFilter() {
		super();
	}

	public static void setFilterNew(JFileChooser chooser) {
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				FileTypeLocale.getDescriptionnew()[0], FileTypeLocale.getDescriptionnew());
		FileNameExtensionFilter filterCAD = new FileNameExtensionFilter(
				FileTypeLocale.getDescriptionnew()[1], "dxf", "dwg");
		FileNameExtensionFilter filterArcGIS = new FileNameExtensionFilter(
				FileTypeLocale.getDescriptionnew()[2], "shp", "grd", "txt", "e00", "dem");
		FileNameExtensionFilter filterMapInfo = new FileNameExtensionFilter(
				FileTypeLocale.getDescriptionnew()[3], "tab", "mif", "wor");
		FileNameExtensionFilter filterMapGIS = new FileNameExtensionFilter(
				FileTypeLocale.getDescriptionnew()[4], "wat", "wan", "wal", "wap");
		FileNameExtensionFilter filterMIC = new FileNameExtensionFilter(
				FileTypeLocale.getDescriptionnew()[5], "xlsx", "csv");
		FileNameExtensionFilter filterByteMap = new FileNameExtensionFilter(
				FileTypeLocale.getDescriptionnew()[6], "sit", "img", "tif", "tiff", "bmp", "png", "gif", "jpg", "jpeg");
		FileNameExtensionFilter filterModel = new FileNameExtensionFilter(
				FileTypeLocale.getDescriptionnew()[7], "scv", "osgb", "3ds", "dxf", "flt", "x");
		FileNameExtensionFilter filterGoogle = new FileNameExtensionFilter(
				FileTypeLocale.getDescriptionnew()[8], "kml", "kmz");
		FileNameExtensionFilter filterGride = new FileNameExtensionFilter(
				FileTypeLocale.getDescriptionnew()[9], "dem", "bil", "raw", "bsq", "bip", "sid", "b");
		FileNameExtensionFilter filterLidar = new FileNameExtensionFilter(
				FileTypeLocale.getDescriptionnew()[10], "txt");
		chooser.setFileFilter(filter);
		chooser.addChoosableFileFilter(filterCAD);
		chooser.addChoosableFileFilter(filterLidar);
		chooser.addChoosableFileFilter(filterArcGIS);
		chooser.addChoosableFileFilter(filterMapInfo);
		chooser.addChoosableFileFilter(filterMapGIS);
		chooser.addChoosableFileFilter(filterMIC);
		chooser.addChoosableFileFilter(filterByteMap);
		chooser.addChoosableFileFilter(filterModel);
		chooser.addChoosableFileFilter(filterGoogle);
		chooser.addChoosableFileFilter(filterGride);
	}

}
