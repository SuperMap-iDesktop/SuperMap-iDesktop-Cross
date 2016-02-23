package com.supermap.desktop.CtrlAction.Map.MapMeasure.Measure;

import com.supermap.data.Geometry;
import com.supermap.desktop.Application;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.enums.AreaUnit;
import com.supermap.desktop.enums.LengthUnit;
import com.supermap.mapping.TrackingLayer;
import com.supermap.ui.Action;
import com.supermap.ui.ActionChangedEvent;
import com.supermap.ui.ActionChangedListener;
import com.supermap.ui.MapControl;
import com.supermap.ui.TrackMode;
import com.supermap.ui.UndoneListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EventObject;

/**
 * 量算基类
 */
public abstract class Measure {



	protected static final String TRAKCING_OBJECT_NAME = "MapMeasureTrackingObjectName";
	// 距离量算相关参数
	/**
	 * 辅助线距离量算线的距离，以像素为单位
	 */
	protected static int assistantLineInterval = 50;
	/**
	 * 显示总长度的编辑框相对鼠标向右偏移的像素值
	 */
	protected static int textBoxOffsetX = 1;
	/**
	 * 显示总长度的编辑框相对鼠标向下偏移的像素值
	 */
	protected static int textBoxOffsetY = 1;
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

	private boolean inPan = false;
	//用这个参数是因为在TrackingLayer上绘制对象，ActionChange的时候，之前绘制的会清空，右键结束又可以继续绘制
	//在ActionChanged的时候用这个对象来显示绘制好的部分
	protected Geometry currentGeometry = null;

	private static String tempTag = "tempGeometryTag";

	public static final int ANGLE_MODE_DEGREE = 0;
	public static final int ANGLE_MODE_DEGREE_MINUTE_SECOND = 1;
	public static final int ANGLE_MODE_RADIAN = 2;
	protected DecimalFormat decimalFormat = new DecimalFormat("0.0000");

	/**
	 * 存放已添加的tags
	 */
	protected java.util.List<String> addedTags = new ArrayList<>();
	private boolean inSetMeasure;
	protected String textTagTitle;
	protected static boolean isEditing = false;
	private ActionChangedListener actionChangedListener = new ActionChangedListener() {
		@Override
		public void actionChanged(ActionChangedEvent e) {
			Action oldAction = e.getOldAction();
			Action newAction = e.getNewAction();

			if (oldAction == getMeasureAction()) {
				if (newAction == Action.PAN || newAction == Action.ZOOMIN || newAction == Action.ZOOMOUT || newAction == Action.ZOOMFREE || newAction == Action.ZOOMFREE2) {
					// 画->漫游
					inPan = true;
					labelTextBoxTotle.setVisible(false);
					labelTextBoxCurrent.setVisible(false);
					removeLineAssistant();
					actionTempGeometry(true);
				} else {
					// 画->其他
					labelTextBoxTotle.setVisible(false);
					labelTextBoxCurrent.setVisible(false);
					endMeasure(true);
				}
			} else if (oldAction == Action.PAN || oldAction == Action.ZOOMIN || oldAction == Action.ZOOMOUT || oldAction == Action.ZOOMFREE || oldAction == Action.ZOOMFREE2) {
				if (inPan && newAction != getMeasureAction()) {
					labelTextBoxTotle.setVisible(false);
					labelTextBoxCurrent.setVisible(false);
					endMeasure(true);
					inPan = false;
				}
			}
		}
	};


	protected void clearAddedTags() {
		TrackingLayer trackingLayer = mapControl.getMap().getTrackingLayer();
		for (int count = trackingLayer.getCount() - 1; count >= 0; count--) {
			if (addedTags.contains(trackingLayer.getTag(count))) {
				trackingLayer.remove(count);
			}
		}
		addedTags.clear();
	}

	public Measure() {

	}

	protected LengthUnit getLengthUnit() {
		return ((FormMap) Application.getActiveApplication().getActiveForm()).getLengthUnit();
	}

	protected AreaUnit getAreaUnit() {
		return ((FormMap) Application.getActiveApplication().getActiveForm()).getAreaUnit();

	}

	protected int getAngleMode() {
		return ((FormMap) Application.getActiveApplication().getActiveForm()).getAngleMode();

	}

	/**
	 * 开始量算入口
	 */
	public void startMeasure() {
		removeListeners();
		getMapControl();
		mapControl.setTrackMode(TrackMode.TRACK);
		mapControl.setLayout(null);
		// 添加编辑框到地图空间中
		addTextBoxsToMapControl();
		addListeners();
		isEditing = true;
		setMapAction();
	}

	private void getMapControl() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof FormMap) {
			FormMap formMap = (FormMap) activeForm;
			this.mapControl = formMap.getMapControl();
			initTextBoxs();
		}
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
		this.labelTextBoxCurrent.setBackground(Color.WHITE);
		this.labelTextBoxCurrent.setOpaque(true);

		this.labelTextBoxTotle = new JLabel();
		this.labelTextBoxTotle.setBackground(Color.WHITE);

		this.labelTextBoxTotle.setOpaque(true);
	}

	private void setMapAction() {
		this.mapControl.setAction(getMeasureAction());
	}


	private void endMeasure(boolean isChangeAction) {
		try {
			isEditing = false;
			actionTempGeometry(false);
			removeTextBoxsFromMapCtrl();
			removeTrackingObject();
			removeLineAssistant();
			removeListeners();
			this.mapControl.setTrackMode(TrackMode.EDIT);
			removeTempMeasureText();

			if (!isChangeAction) {
				this.mapControl.setAction(com.supermap.ui.Action.SELECT2);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 移除正在量算的标签
	 */
	private void removeTempMeasureText() {
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




	protected abstract Action getMeasureAction();
	protected void addListeners() {
		removeListeners();
		this.mapControl.addMouseListener(this.mouseAdapter);

		this.mapControl.removeKeyListener(this.escClearKeyAdapt);// 只防止添加2次，不在退出时清除
		this.mapControl.addKeyListener(this.escClearKeyAdapt);
		this.mapControl.addKeyListener(this.keyAdapter);
		this.mapControl.addActionChangedListener(actionChangedListener);
		this.mapControl.addUndoneListener(undoneListener);

	}

	private void removeLastAdded() {
		if (addedTags.size() > 0) {
			String tag = addedTags.get(addedTags.size() - 1);
			TrackingLayer trackingLayer = mapControl.getMap().getTrackingLayer();
			for (int i = 0; i < trackingLayer.getCount(); i++) {
				if (trackingLayer.getTag(i).equals(tag)) {
					trackingLayer.remove(i);
				}
			}
			addedTags.remove(tag);
		} else {
			labelTextBoxCurrent.setVisible(false);
			labelTextBoxTotle.setVisible(false);
			removeLineAssistant();
		}
	}



	private void actionTempGeometry(boolean isAdd) {
		TrackingLayer trackingLayer = mapControl.getMap().getTrackingLayer();
		int index = trackingLayer.indexOf(tempTag);
		if (isAdd) {
			if (currentGeometry != null) {
				if (index > -1) {
					trackingLayer.set(index, currentGeometry);
				} else {
					trackingLayer.add(currentGeometry, tempTag);
				}
			}
		} else {
			if (index > -1) {
				trackingLayer.remove(index);
			}
		}
		mapControl.getMap().refresh();
	}

	private boolean isEditing() {
		return isEditing;
	}

	protected void removeListeners() {
		if (mapControl != null) {
			this.mapControl.removeMouseListener(this.mouseAdapter);
			this.mapControl.removeKeyListener(this.keyAdapter);
			this.mapControl.removeActionChangedListener(actionChangedListener);
			this.mapControl.removeUndoneListener(undoneListener);
		}
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
	protected void removeTextBoxsFromMapCtrl() {
		try {
			this.labelTextBoxCurrent.setText("");
			this.labelTextBoxTotle.setText("");
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


	private MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				if (inPan) {
					inPan = false;
					setMapAction();
					actionTempGeometry(false);
				} else {
					cancleEdit();
				}
			}

		}
	};

	private KeyAdapter keyAdapter = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
				cancleEdit();
			}
		}
	};

	private final KeyAdapter escClearKeyAdapt = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyChar() == KeyEvent.VK_ESCAPE && !isEditing()) {
				mapControl.getMap().getTrackingLayer().clear();
				mapControl.removeKeyListener(this);
			}
		}
	};

	private final UndoneListener undoneListener = new UndoneListener() {
		@Override
		public void undone(EventObject eventObject) {
			removeLastAdded();
		}
	};
	//endregion

}
