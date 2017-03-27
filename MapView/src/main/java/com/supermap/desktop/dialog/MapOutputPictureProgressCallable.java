package com.supermap.desktop.dialog;

import com.supermap.data.Rectangle2D;
import com.supermap.desktop.Application;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.mapping.ImageType;
import com.supermap.mapping.Map;

/**
 * @author YuanR
 *         地图输出为图片进度条类
 */
public class MapOutputPictureProgressCallable extends UpdateProgressCallable {
	Map copyMap;
	String path;
	int dpi;
	ImageType imageType;
	private Rectangle2D outPutBounds;
	boolean isBackTransparent;
	boolean createMapOutputPictureProgress;


	public MapOutputPictureProgressCallable(Map map, String path, ImageType imageType, int dpi, Rectangle2D rectangle, boolean isBackTransparent) {
		this.copyMap = map;
		this.path = path;
		this.imageType = imageType;
		this.dpi = dpi;
		this.outPutBounds = rectangle;
		this.isBackTransparent = isBackTransparent;
	}

	@Override
	public Boolean call() throws Exception {
		Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_OutputImage_AvoidAndFlowEnabled"));
		try {
			this.createMapOutputPictureProgress = false;
			String resultMessage;
			this.copyMap.setDisableAutoAvoidEffect(true);
			// 设置是否在出图的时候关闭地图的动态效果
			this.copyMap.setDisableDynamicEffect(true);
			if (imageType.equals(imageType.GIF)) {
				if (copyMap.outputMapToGIF(path, isBackTransparent)) {
					this.createMapOutputPictureProgress = true;
				}
			} else if (imageType.equals(imageType.EPS)) {
				if (copyMap.outputMapToEPS(path)) {
					this.createMapOutputPictureProgress = true;
				}
			} else {
				if (copyMap.outputMapToFile(path, imageType, dpi, outPutBounds, isBackTransparent)) {
					this.createMapOutputPictureProgress = true;
				}
			}
			// 输出结果信息
			if (createMapOutputPictureProgress) {
				resultMessage = String.format(MapViewProperties.getString("String_OutputToFile_Successed"), path);
			} else {
				resultMessage = String.format(MapViewProperties.getString("String_OutputToFile_Failed"), path);
			}
			Application.getActiveApplication().getOutput().output(resultMessage);

		} catch (Exception e1) {
			Application.getActiveApplication().getOutput().output(e1);
		}
		return this.createMapOutputPictureProgress;
	}
}
