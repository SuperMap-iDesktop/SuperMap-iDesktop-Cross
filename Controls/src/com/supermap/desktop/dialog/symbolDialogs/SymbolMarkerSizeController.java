package com.supermap.desktop.dialog.symbolDialogs;

import com.supermap.data.GeoStyle;
import com.supermap.data.Resources;
import com.supermap.data.Size2D;
import com.supermap.data.Symbol;
import com.supermap.data.SymbolMarker;

/**
 * @author XiaJt
 */
public class SymbolMarkerSizeController {

	private boolean isLockSelected = true;
	private double WidthHeightRate = 1;

	private double symbolShowWidth = -1;
	private double symbolShowHeight = -1;
	private double symbolWidth = -1;
	private double symbolHeight = -1;

	private GeoStyle currentGeoStyle;
	private Resources resources;
	/**
	 * 数字精度
	 */
	private java.text.DecimalFormat df = new java.text.DecimalFormat("#.0");


	public SymbolMarkerSizeController() {

	}

	public void setGeoStyle(GeoStyle geoStyle) {
		this.currentGeoStyle = geoStyle;
		Size2D markerSize = geoStyle.getMarkerSize();
		symbolWidth = getFormatDouble(markerSize.getWidth());
		symbolHeight = getFormatDouble(markerSize.getHeight());
		symbolShowWidth = getShowSizeBySymbolSize(symbolWidth);
		symbolShowHeight = getShowSizeBySymbolSize(symbolHeight);
		isLockSelected = true;
		WidthHeightRate = 1;
	}


	public void setLockSelected(boolean lockSelected) {
		isLockSelected = lockSelected;
		if (isLockSelected) {
			WidthHeightRate = -1;
			// 有0归0
			if (symbolHeight == 0) {
				symbolWidth = 0;
				WidthHeightRate = 1;
			} else if (symbolWidth == 0) {
				symbolHeight = 0;
				WidthHeightRate = 1;
			}
			// 有负归-1
			if (symbolHeight < 0 || symbolWidth < 0) {
				symbolWidth = -1;
				symbolHeight = -1;
				WidthHeightRate = 1;
			}
			// 结果仍为-1，计算结果
			if (WidthHeightRate == -1) {
				WidthHeightRate = symbolWidth / symbolHeight;
			}
		}
	}


	public double getSymbolShowWidth() {
		return symbolShowWidth;
	}

	public void setSymbolShowWidth(double symbolShowWidth) {
		double formatDouble = getFormatDouble(symbolShowWidth);
		if (formatDouble == this.symbolShowWidth) {
			return;
		}
		this.symbolShowWidth = formatDouble;
		if (isLockSelected) {
			this.symbolShowHeight = getFormatDouble(this.symbolShowWidth / WidthHeightRate);
			this.symbolHeight = getSymbolSizeByShowSize(symbolShowHeight);
		}
		this.symbolWidth = getSymbolSizeByShowSize(this.symbolShowWidth);
		write();
	}

	public double getSymbolShowHeight() {
		return symbolShowHeight;
	}

	public void setSymbolShowHeight(double symbolShowHeight) {
		double formatDouble = getFormatDouble(symbolShowHeight);
		if (formatDouble == this.symbolShowHeight) {
			return;
		}
		this.symbolShowHeight = formatDouble;
		if (isLockSelected) {
			this.symbolShowWidth = getFormatDouble(this.symbolShowHeight * WidthHeightRate);
			this.symbolWidth = getSymbolSizeByShowSize(symbolShowWidth);
		}
		this.symbolHeight = getSymbolSizeByShowSize(this.symbolShowHeight);
		write();
	}



	public void setResources(Resources resources) {
		this.resources = resources;
	}

	private void write() {
		if (currentGeoStyle != null) {
			Size2D size2D = new Size2D();
			size2D.setWidth(symbolWidth);
			size2D.setHeight(symbolHeight);
			if (size2D.getWidth() < 0 && size2D.getHeight() < 0) {
				size2D.setWidth(0);
			}
			currentGeoStyle.setMarkerSize(size2D);
		}
	}

	private double getFormatDouble(double value) {

		Double aDouble = Double.valueOf(df.format(value));
		if (aDouble > 500.0) {
			aDouble = 500.0;
		}
		return aDouble;
	}

	private double getShowSizeBySymbolSize(double symbolSize) {
		if (symbolSize <= 0) {
			return symbolSize;
		}
		Symbol symbol = resources.getMarkerLibrary().findSymbol(currentGeoStyle.getMarkerSymbolID());
		if (symbol != null && symbol instanceof SymbolMarker) {
			int value = ((SymbolMarker) symbol).computeDisplaySize((int) symbolSize);
			if (value > 500) {
				return 500;
			} else {
				return value;
			}
		} else {
			return symbolSize;
		}
	}

	private double getSymbolSizeByShowSize(double showSize) {
		if (showSize <= 0) {
			return showSize;
		}
		Symbol symbol = resources.getMarkerLibrary().findSymbol(currentGeoStyle.getMarkerSymbolID());
		if (symbol != null && symbol instanceof SymbolMarker) {

//			if (value > 500) {
//				return 500;
//			} else {
			return ((SymbolMarker) symbol).computeSymbolSize(500) / 500 * showSize;
//			}
		} else {
			return showSize;
		}
	}


	/**
	 * 重新计算符号大小，用于符号改变后的重新计算
	 */
	public void reCalculateShowSize() {
		this.symbolHeight = getSymbolSizeByShowSize(symbolShowHeight);
		this.symbolWidth = getSymbolSizeByShowSize(symbolShowWidth);
		write();
	}

	//	public double getSymbolWidth() {
//		return symbolWidth;
//	}
//
//	public void setSymbolWidth(double symbolWidth) {
//		double formatDouble = getFormatDouble(symbolWidth);
//		if (formatDouble == this.symbolWidth) {
//			// 相等直接返回
//			return;
//		}
//
//		this.symbolWidth = formatDouble;
//		if (isLockSelected) {
//			this.symbolHeight = getFormatDouble(this.symbolWidth / WidthHeightRate);
//			this.symbolShowHeight = getShowSizeBySymbolSize(symbolHeight);
//		}
//		this.symbolShowWidth = getShowSizeBySymbolSize(this.symbolWidth);
//		write();
//	}

//	public double getSymbolHeight() {
//		return symbolHeight;
//	}
//
//	public void setSymbolHeight(double symbolHeight) {
//		double formatDouble = getFormatDouble(symbolHeight);
//		if (formatDouble == this.symbolHeight) {
//			return;
//		}
//		this.symbolHeight = formatDouble;
//		if (isLockSelected) {
//			this.symbolWidth = getFormatDouble(this.symbolHeight * WidthHeightRate);
//			this.symbolShowWidth = getShowSizeBySymbolSize(symbolWidth);
//		}
//		this.symbolShowHeight = getShowSizeBySymbolSize(this.symbolHeight);
//		write();
//	}
}
