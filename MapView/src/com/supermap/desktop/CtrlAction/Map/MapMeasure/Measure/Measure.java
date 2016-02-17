package com.supermap.desktop.CtrlAction.Map.MapMeasure.Measure;

import com.supermap.data.Geometry;
import com.supermap.desktop.Application;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.enums.AreaUnit;
import com.supermap.desktop.enums.LengthUnit;
import com.supermap.mapping.TrackingLayer;
import com.supermap.ui.MapControl;
import com.supermap.ui.TrackMode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 量算基类
 */
public class Measure {


	private double currentLength;


	protected static final String TRAKCING_OBJECT_NAME = "MapMeasureTrackingObjectName";
	// 距离量算相关参数
	/**
	 * 辅助线距离量算线的距离，以像素为单位
	 */
	protected static int assistantLineInterval = 50;
	/**
	 * 显示总长度的编辑框相对鼠标向右偏移的像素值
	 */
	protected static int textBoxOffsetX = 20;
	/**
	 * 显示总长度的编辑框相对鼠标向下偏移的像素值
	 */
	protected static int textBoxOffsetY = 20;
	protected static double textFontHeight = 45;

	// 辅助线相关参数
	protected static int lineSymbolID = 2;
	protected static double lineWidth = 0.4;
	protected static Color lineColor = Color.BLUE;

	// 两个编辑框
	protected JLabel labelTextBoxCurrent;
	protected JLabel labelTextBoxTotle;
	// 地图控件
	protected MapControl mapControl = null;
//	private boolean measureNewMap = true;

	//用这个参数是因为在TrackingLayer上绘制对象，ActionChange的时候，之前绘制的会清空，右键结束又可以继续绘制
	//在ActionChanged的时候用这个对象来显示绘制好的部分
	private Geometry currentGeometry = null;

	private static String tempTag = "tempGeometryTag";

	public static final int ANGLE_MODE_DEGREE = 0;
	public static final int ANGLE_MODE_DEGREE_MINUTE_SECOND = 1;
	public static final int ANGLE_MODE_RADIAN = 2;
	protected DecimalFormat df = new DecimalFormat("0.0000");

	/**
	 * 存放已添加的tags
	 */
	protected java.util.List<String> addedTags = new ArrayList<>();
	private boolean inSetMeasure;
	protected String textTagTitle;

	public Measure() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof FormMap) {
			FormMap formMap = (FormMap) activeForm;
			this.mapControl = formMap.getMapControl();
			initTextBoxs();
		}
	}

	public static LengthUnit getLengthUnit() {
		return ((FormMap) Application.getActiveApplication().getActiveForm()).getLengthUnit();
	}

	public static AreaUnit getAreaUnit() {
		return ((FormMap) Application.getActiveApplication().getActiveForm()).getAreaUnit();

	}

	public static int getAngleMode() {
		return ((FormMap) Application.getActiveApplication().getActiveForm()).getAngleMode();

	}

	/**
	 * 开始量算入口
	 */
	public void startMeasure() {
		resetValue();
		mapControl.setTrackMode(TrackMode.TRACK);
		mapControl.setLayout(null);
		// 添加编辑框到地图空间中
		addTextBoxsToMapControl();
		addListeners();
	}

	private void addTextBoxsToMapControl() {
		try {
			this.labelTextBoxTotle.setVisible(false);
			this.mapControl.remove(this.labelTextBoxTotle);
			this.mapControl.add(this.labelTextBoxTotle);

			this.labelTextBoxCurrent.setVisible(false);
			this.mapControl.remove(this.labelTextBoxCurrent);
			this.mapControl.add(this.labelTextBoxCurrent);

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void initTextBoxs() {
		this.labelTextBoxCurrent = new JLabel();
		this.labelTextBoxCurrent.setOpaque(false);

		this.labelTextBoxTotle = new JLabel();
		this.labelTextBoxTotle.setOpaque(false);
	}

	/**
	 * 输出量算结果
	 */
	protected void outputMeasure() {
		// 子类实现输出方法
		Application.getActiveApplication().getOutput().output(this.getClass().getName() + "don't override outputMeasure()");
	}

	private void endMeasure(boolean isChangeAction) {
		try {
			removeTextBoxsFromMapCtrl();
			removeTrackingObject();
			removeLineAssistant();

			removeListeners();

			if (!isChangeAction) {
				resetValue();
//				if (!this.measureNewMap) {
//				this.mapControl.setAction(com.supermap.ui.Action.CREATEPOINT);
				this.mapControl.setAction(com.supermap.ui.Action.SELECT2);

				this.mapControl.setTrackMode(TrackMode.EDIT);
//				}
				RemoveTempMeasureText();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 移除正在量算的标签
	 */
	private void RemoveTempMeasureText() {
		try {
			if (mapControl != null) {
				for (int i = mapControl.getMap().getTrackingLayer().getCount() - 1; i >= 0; i--) {
					String tag = mapControl.getMap().getTrackingLayer().getTag(i);
					//不删除已经绘制好的标签
					if (tag != null && tag.contains(textTagTitle) && !tag.contains("FinishedMeasure")) {
						mapControl.getMap().getTrackingLayer().remove(i);
					}
				}
				//正在绘制
				if (addedTags != null) {
					addedTags.clear();
				}
				mapControl.getMap().refreshTrackingLayer();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 重置数值为初始状态
	 */
	protected void resetValue() {
		Application.getActiveApplication().getOutput().output(this.getClass().getName() + "don't override resetvalue().");
	}


	protected void addListeners() {
		this.mapControl.addMouseListener(this.mouseAdapter);
//		this.labelTextBoxTotle.addMouseListener(this.labelMouseListener);
//		this.labelTextBoxCurrent.addMouseListener(this.labelMouseListener);
	}

	protected void removeListeners() {
		this.mapControl.removeMouseListener(this.mouseAdapter);
//		this.labelTextBoxTotle.removeMouseListener(this.labelMouseListener);
//		this.labelTextBoxCurrent.removeMouseListener(this.labelMouseListener);
	}

	/**
	 * 移除正在编辑的对象
	 */
	private void removeLineAssistant() {
		try {
			int index = indexOfTrackingObject();

			if (index >= 0) {
				this.mapControl.getMap().getTrackingLayer().remove(index);
				this.mapControl.getMap().refreshTrackingLayer();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);

		}
	}

	protected int indexOfTrackingObject() {
		int indexOfTrackingObject = -1;

		try {
//			if (!this.measureNewMap) {
			indexOfTrackingObject = this.mapControl.getMap().getTrackingLayer().indexOf(TRAKCING_OBJECT_NAME);
//			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return indexOfTrackingObject;
	}

	// 从地图控件中移除掉编辑框
	private void removeTextBoxsFromMapCtrl() {
		try {
			this.labelTextBoxCurrent.setText("");
			this.labelTextBoxTotle.setText("");
//			!this.measureNewMap||
			if (this.mapControl != null) {
				this.mapControl.remove(this.labelTextBoxCurrent);
				this.mapControl.remove(this.labelTextBoxTotle);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);

		}
	}


	protected void cancleEdit() {
		labelTextBoxCurrent.setVisible(false);
		labelTextBoxTotle.setVisible(false);
		endMeasure(false);
	}

	/**
	 * 移除辅助线
	 */
	protected void removeTrackingObject() {
		TrackingLayer trackingLayer = mapControl.getMap().getTrackingLayer();
		for (int i = trackingLayer.getCount() - 1; i >= 0; i--) {
			if (trackingLayer.getTag(i).equals(TRAKCING_OBJECT_NAME)) {
				trackingLayer.remove(i);
			}
		}
	}

	//region 监听事件
	private final MouseAdapter labelMouseListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			sendToMapControl(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			sendToMapControl(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			sendToMapControl(e);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			sendToMapControl(e);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			sendToMapControl(e);
		}
	};

	/**
	 * 将label接受到的事件传给mapControl
	 */
	private void sendToMapControl(MouseEvent e) {
		if (mapControl != null) {
			mapControl.dispatchEvent(e);
		}
	}

	private MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				cancleEdit();
			}
		}
	};

	//endregion

}
