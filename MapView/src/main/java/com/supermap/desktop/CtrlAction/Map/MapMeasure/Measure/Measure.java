package com.supermap.desktop.CtrlAction.Map.MapMeasure.Measure;

import com.supermap.data.GeoStyle;
import com.supermap.data.Geometry;
import com.supermap.desktop.Application;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.enums.AngleUnit;
import com.supermap.desktop.enums.AreaUnit;
import com.supermap.desktop.enums.LengthUnit;
import com.supermap.desktop.utilities.SystemPropertyUtilities;
import com.supermap.mapping.TrackingLayer;
import com.supermap.ui.Action;
import com.supermap.ui.ActionChangedEvent;
import com.supermap.ui.ActionChangedListener;
import com.supermap.ui.MapControl;
import com.supermap.ui.TrackMode;
import com.supermap.ui.TrackedListener;
import com.supermap.ui.TrackingListener;
import com.supermap.ui.UndoneListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EventObject;

/**
 * 量算基类
 */
public abstract class Measure implements IMeasureAble {


	protected static final String TRAKCING_OBJECT_NAME = "MapMeasureTrackingObjectName";
	// 距离量算相关参数
	/**
	 * 辅助线距离量算线的距离，以像素为单位
	 */
	protected static int assistantLineInterval = 50;

	protected static double textFontHeight = 1.5;


	// 辅助线相关参数
	protected static int lineSymbolID = 2;
	protected static double lineWidth = 0.4;
	protected static Color lineColor = Color.BLUE;


	// 两个编辑框
	protected static JLabel labelTextBoxCurrent = new JLabel();
	protected static JLabel labelTextBoxTotle = new JLabel();

	protected boolean isMeasureAble = true;

	public MapControl getMapControl() {
		return mapControl;
	}

	@Override
	public void setMapControl(MapControl mapControl) {
		this.mapControl = mapControl;
	}

	// 地图控件
	protected MapControl mapControl = null;

	private boolean inPan = false;
	//用这个参数是因为在TrackingLayer上绘制对象，ActionChange的时候，之前绘制的会清空，右键结束又可以继续绘制
	//在ActionChanged的时候用这个对象来显示绘制好的部分
	protected Geometry currentGeometry = null;

	private static final String tempTag = "tempGeometryTag";

	protected DecimalFormat decimalFormat = new DecimalFormat("0.0000");

	/**
	 * 在量算的时候点了鼠标左键不会马上就添加标签，而是在鼠标移动之后才会添加
	 * 所以这里加一个变量用在撤销时判断，如果点完直接撤销则跳过一次删除标签的操作
	 */
	private boolean isMouseDown = false;
	private final MouseMotionListener mouseMotionListener = new MouseMotionListener() {
		@Override
		public void mouseDragged(MouseEvent e) {
			isMouseDown = false;
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			isMouseDown = false;
		}
	};
	/**
	 * 存放已添加的tags
	 */
	protected java.util.List<String> addedTags = new ArrayList<>();
	protected String textTagTitle;
	/**
	 * 防止在编辑时把整个屏幕清除了
	 */
	protected static boolean isEditing = false;
	private ActionChangedListener actionChangedListener = new ActionChangedListener() {
		@Override
		public void actionChanged(ActionChangedEvent e) {
			Action oldAction = e.getOldAction();
			Action newAction = e.getNewAction();

			if (oldAction == getMeasureAction()) {
				if (isPanAction(newAction)) {
					// 画->漫游
					inPan = true;
					actionTempGeometry(true);
				} else {
					// 画->其他
					setTextBoxVisible(false);
					endMeasure(false);
				}
			} else if (isPanAction(oldAction)) {
				if (isPanAction(newAction)) {
					// 漫游->漫游 不动
					inPan = true;
				} else {
					inPan = false;
					if (newAction != getMeasureAction()) {
						// 漫游->其他
						setTextBoxVisible(false);
						endMeasure(false);
					} else {
						actionTempGeometry(false);
					}
				}
			}
		}
	};

	private boolean isPanAction(Action action) {
		return action == Action.PAN || action == Action.ZOOMIN || action == Action.ZOOMOUT || action == Action.ZOOMFREE || action == Action.ZOOMFREE2;
	}

	@Override
	public void stopMeasure() {
		endMeasure(false);
		mapControl.setWaitCursorEnabled(true);
	}

	protected void setTextBoxVisible(boolean isVisible) {
		labelTextBoxTotle.setVisible(isVisible);
		labelTextBoxCurrent.setVisible(isVisible);
	}

	protected TrackedListener trackedListener;
	protected TrackingListener trackingListener;


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

	protected AngleUnit getAngleUnit() {
		return ((FormMap) Application.getActiveApplication().getActiveForm()).getAngleUnit();

	}

	/**
	 * 开始量算入口
	 */
	public void startMeasure() {
		if (this.mapControl == null) {
			return;
		}
		isMeasureAble = false;
		mapControl.setWaitCursorEnabled(false);
//		removeListeners();
//		cancleEdit();
		this.mapControl.setTrackMode(TrackMode.TRACK);
		this.mapControl.setLayout(null);
		// 添加编辑框到地图空间中
		addTextBoxsToMapControl();
		setMapAction();
		addListeners();
		isEditing = true;
		// 获取焦点响应按键
		mapControl.requestFocusInWindow();
	}

//	private void getMapControl() {
//		IForm activeForm = Application.getActiveApplication().getActiveForm();
//		if (activeForm instanceof FormMap) {
//			formMap = (FormMap) activeForm;
//			this.mapControl = formMap.getMapControl();
//			initTextBoxs();
//		}
//	}

	private void addTextBoxsToMapControl() {
		try {
			setTextBoxVisible(false);
			this.mapControl.remove(labelTextBoxTotle);
			this.mapControl.add(labelTextBoxTotle);
			this.mapControl.remove(labelTextBoxCurrent);
			this.mapControl.add(labelTextBoxCurrent);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void initTextBoxs() {
		labelTextBoxCurrent.setBackground(Color.WHITE);
		labelTextBoxCurrent.setOpaque(true);

		labelTextBoxTotle.setBackground(Color.WHITE);
		labelTextBoxTotle.setOpaque(true);
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
			if (currentGeometry != null) {
				currentGeometry.dispose();
			}
			if (!isChangeAction && this.mapControl != null) {
				removeTempMeasureText();
				this.mapControl.setAction(com.supermap.ui.Action.SELECT2);
			}
			if (mapControl != null) {
				this.mapControl.setTrackMode(TrackMode.EDIT);
			}
			isMeasureAble = true;
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
					if (tag != null && textTagTitle != null && tag.contains(textTagTitle) && !tag.contains("FinishedMeasure")) {
						mapControl.getMap().getTrackingLayer().remove(i);
					}
				}
				//正在绘制
				if (addedTags != null) {
					addedTags.clear();
				}
				refreshTrackingLayer();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	protected void refreshTrackingLayer() {
		TrackMode trackMode = mapControl.getTrackMode();
		if (trackMode == TrackMode.TRACK) {
			mapControl.getMap().refreshTrackingLayer();
		} else {
			mapControl.setTrackMode(TrackMode.TRACK);
			mapControl.getMap().refreshTrackingLayer();
			mapControl.setTrackMode(trackMode);

		}
	}


	protected abstract Action getMeasureAction();


	/**
	 * 撤销时需要移除最后一次添加的标签
	 */
	protected void removeLastAdded() {
		if (isMouseDown) {
			isMouseDown = false;
		} else if (addedTags.size() > 0) {
			String tag = addedTags.get(addedTags.size() - 1);
			TrackingLayer trackingLayer = mapControl.getMap().getTrackingLayer();
			for (int i = 0; i < trackingLayer.getCount(); i++) {
				if (trackingLayer.getTag(i).equals(tag)) {
					trackingLayer.remove(i);
				}
			}
			addedTags.remove(tag);
		} else {
			setTextBoxVisible(false);
			removeLineAssistant();
		}
	}


	/**
	 * action改变时添加当前对象到跟踪层
	 *
	 * @param isAdd
	 */
	private void actionTempGeometry(boolean isAdd) {
		if (mapControl != null) {
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
//			mapControl.getMap().refreshTrackingLayer();
		}
	}

	private boolean isEditing() {
		return isEditing;
	}

	protected int getSystemLength() {
		if (SystemPropertyUtilities.isWindows()) {
			return 0;
		} else {
			return 20;
		}
	}

	protected void addListeners() {
		removeListeners();
		this.mapControl.addKeyListener(this.keyAdapter);
		this.mapControl.removeKeyListener(this.escClearKeyAdapt);// 只防止添加2次，不在退出时清除而在添加时删除
		this.mapControl.addKeyListener(this.escClearKeyAdapt);

		KeyListener[] keyListeners = mapControl.getKeyListeners();
		for (KeyListener keyListener : keyListeners) {
			if (keyListener != keyAdapter && keyListener != escClearKeyAdapt) {
				mapControl.removeKeyListener(keyListener);
				mapControl.addKeyListener(keyListener);
			}
		}
		this.mapControl.addMouseListener(this.mouseAdapter);
		this.mapControl.addActionChangedListener(this.actionChangedListener);
		this.mapControl.addUndoneListener(this.undoneListener);
		this.mapControl.addTrackedListener(this.trackedListener);
		this.mapControl.addTrackingListener(this.trackingListener);
		this.mapControl.addMouseMotionListener(mouseMotionListener);
	}

	protected void removeListeners() {
		if (mapControl != null) {
			this.mapControl.removeMouseListener(this.mouseAdapter);
			this.mapControl.removeKeyListener(this.keyAdapter);
			this.mapControl.removeActionChangedListener(actionChangedListener);
			this.mapControl.removeUndoneListener(undoneListener);
			this.mapControl.removeTrackedListener(trackedListener);
			this.mapControl.removeTrackingListener(trackingListener);
			this.mapControl.removeMouseMotionListener(mouseMotionListener);
		}
	}

	/**
	 * 移除正在编辑的对象
	 */
	protected void removeLineAssistant() {
		try {
			int index = indexOfTrackingObject();

			if (index >= 0) {
				this.mapControl.getMap().getTrackingLayer().remove(index);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);

		}
	}

	protected int indexOfTrackingObject() {
		int indexOfTrackingObject = -1;
		if (mapControl != null) {
			try {
				indexOfTrackingObject = this.mapControl.getMap().getTrackingLayer().indexOf(TRAKCING_OBJECT_NAME);
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
		}
		return indexOfTrackingObject;
	}

	// 从地图控件中移除掉编辑框
	protected void removeTextBoxsFromMapCtrl() {
		try {
			labelTextBoxCurrent.setText("");
			labelTextBoxTotle.setText("");
			if (this.mapControl != null) {
				this.mapControl.remove(labelTextBoxCurrent);
				this.mapControl.remove(labelTextBoxTotle);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);

		}
	}


	protected void cancleEdit() {
		setTextBoxVisible(false);
		endMeasure(false);
	}

	/**
	 * 移除辅助线
	 */
	protected void removeTrackingObject() {
		if (mapControl != null) {
			TrackingLayer trackingLayer = mapControl.getMap().getTrackingLayer();
			for (int i = trackingLayer.getCount() - 1; i >= 0; i--) {
				if (trackingLayer.getTag(i).equals(TRAKCING_OBJECT_NAME)) {
					trackingLayer.remove(i);
				}
			}
		}
	}

	protected GeoStyle getDefaultLineStyle() {
		GeoStyle geoStyle = null;
		try {
			geoStyle = new GeoStyle();
			geoStyle.setLineColor(lineColor);
			geoStyle.setLineSymbolID(lineSymbolID);
			geoStyle.setLineWidth(lineWidth);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return geoStyle;
	}

	@Override
	public boolean isMeasureAble() {
		return isMeasureAble;
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
			} else if (e.getButton() == MouseEvent.BUTTON1) {
				isMouseDown = true;
			}
		}
	};

	private KeyAdapter keyAdapter = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			if (!e.isConsumed() && e.getKeyChar() == KeyEvent.VK_ESCAPE) {
				cancleEdit();
				e.consume();
			}
		}
	};

	private final KeyAdapter escClearKeyAdapt = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			if (!e.isConsumed() && e.getKeyChar() == KeyEvent.VK_ESCAPE && !isEditing()) {
				if (mapControl.getMap().getTrackingLayer().getCount() > 0) {
					e.consume();
				}
				mapControl.getMap().getTrackingLayer().clear();
				refreshTrackingLayer();
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
