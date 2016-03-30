package com.supermap.desktop.CtrlAction.Map.MapMeasure.Measure;

import com.supermap.data.GeoArc;
import com.supermap.data.GeoCompound;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoStyle;
import com.supermap.data.GeoText;
import com.supermap.data.Geometry;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.TextAlignment;
import com.supermap.data.TextPart;
import com.supermap.data.TextStyle;
import com.supermap.desktop.Application;
import com.supermap.desktop.enums.AngleUnit;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.utilties.FontUtilties;
import com.supermap.desktop.utilties.SystemPropertyUtilties;
import com.supermap.ui.Action;
import com.supermap.ui.TrackedEvent;
import com.supermap.ui.TrackedListener;
import com.supermap.ui.TrackingEvent;
import com.supermap.ui.TrackingListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Administrator on 2016/1/28.
 */
public class MeasureAngle extends Measure {
	private static final String measureAngleTag = "measureAngleTag";
	private AngleUnit beforeUnit;
	private ArrayList<Double> angleList;
	private int beforePointsCount;
	private double beforeAzimuth;
	private double beforeAngle;
	private Stack<Double> stackAzimuth;
	private MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1 && currentGeometry != null && beforePointsCount != 0) {
				Point2Ds points = ((GeoLine) currentGeometry).getPart(0);

				int pointsCount = points.getCount();
				if (pointsCount >= 3) {
					if (angleList == null) {
						angleList = new ArrayList<Double>();
					}
					while (angleList.size() <= pointsCount - 3) {
						angleList.add(0.0);
					}
					angleList.set(pointsCount - 3, beforeAngle);
				}
				drawAngleText(points, true);
				outPutAngle(beforePointsCount);
			}
		}
	};

	public MeasureAngle() {
		textTagTitle = "AngleText";
		angleList = new ArrayList<Double>();
		stackAzimuth = new Stack<>();
		trackingListener = new TrackingListener() {
			@Override
			public void tracking(TrackingEvent e) {
				try {
					if (e.getGeometry() != null) {
						if (currentGeometry != null) {
							currentGeometry.dispose();
						}
						currentGeometry = e.getGeometry().clone();

						// 根据当前绘制的对象，构造辅助线
						GeoLine geoLine = ((GeoLine) e.getGeometry());
						Point2Ds points = geoLine.getPart(0);

						// 绘制正北方向的线段
						int pointsCount = points.getCount();
						Point2D startPoint = points.getItem(pointsCount - 2);
						Point startPointPixel = mapControl.getMap().mapToPixel(startPoint);
						Point endPointPixel = new Point(((int) startPointPixel.getX()), (int) (startPointPixel.getY() - 200));

						Point2D endPoint = mapControl.getMap().pixelToMap(endPointPixel);
						Point2Ds pnts = new Point2Ds();
						pnts.add(startPoint);
						pnts.add(endPoint);

						GeoLine geoLineAzimuth = new GeoLine(pnts);
						geoLineAzimuth.setStyle(getDefaultLineStyle());

						// 构造方位角弧线
						Point startArcPointPixel = new Point(((int) startPointPixel.getX()), (int) (startPointPixel.getY() - 50));

						Point2D startArcPoint = mapControl.getMap().pixelToMap(startArcPointPixel);

						Double radius = Math.abs(startArcPoint.getY() - startPoint.getY());
						if (radius > e.getLength()) {
							radius = e.getLength();
						}
						// 构造复合对象
						GeoCompound geoCompound = new GeoCompound();
						geoCompound.addPart(geoLineAzimuth);

						GeoArc geoArcAzimuth = null;
						Double angle = 90 - e.getCurrentAzimuth();
						if (e.getCurrentAzimuth() > 90 && radius != 0) {
							angle = 450 - e.getCurrentAzimuth();
							geoArcAzimuth = new GeoArc(startPoint, radius, angle, 450 - angle);
							geoArcAzimuth.setStyle(getDefaultLineStyle());
							geoCompound.addPart(geoArcAzimuth);
						} else if (e.getCurrentAzimuth() != 0 && radius > 0) {
							geoArcAzimuth = new GeoArc(startPoint, radius, angle, 90 - angle);
							geoArcAzimuth.setStyle(getDefaultLineStyle());
							geoCompound.addPart(geoArcAzimuth);
						}

						GeoArc geoArcAngle = null;

						// 输出夹角，方位角
						beforePointsCount = pointsCount;
						if (e.getLength() == 0.0) {
							beforePointsCount = 0;
						} else {
							beforeAzimuth = e.getCurrentAzimuth();
							beforeAngle = e.getAngle();
						}

						if (pointsCount >= 3) {

							Double secondLineAngle = calculateAngle(points.getItem(pointsCount - 2), points.getItem(pointsCount - 1));
							if (e.getAngle() != 0 && radius > 0) {
								geoArcAngle = new GeoArc(points.getItem(pointsCount - 2), radius * 2, secondLineAngle, e.getAngle());
								geoArcAngle.setStyle(getDefaultLineStyle());
								geoCompound.addPart(geoArcAngle);
							}
						}

						// 更新跟踪图层上的跟踪线信息
						geoCompound.rotate(mapControl.getMap().getCenter(), -mapControl.getMap().getAngle());
						updateTrackingObject(geoCompound, true);
						Point geoArcAzimuthInnerPixel = mapControl.getMap().mapToPixel(geoLineAzimuth.getInnerPoint());
						if (geoArcAzimuth != null) {
							geoArcAzimuthInnerPixel = mapControl.getMap().mapToPixel(geoArcAzimuth.getInnerPoint());
						}

						//设置角度文本框位置
						setAngleTextBox(e, points, geoArcAngle, geoArcAzimuthInnerPixel);
					}
				} catch (Exception ex) {
					Application.getActiveApplication().getOutput().output(ex);
				}

			}
		};

		trackedListener = new TrackedListener() {
			@Override
			public void tracked(TrackedEvent e) {
				Geometry geometry = e.getGeometry();
				if (geometry != null) {
					if (geometry.getStyle() == null) {
						geometry.setStyle(new GeoStyle());
					}
					geometry.getStyle().setLineWidth(0.1);
					geometry.getStyle().setFillSymbolID(1);
					geometry.getStyle().setLineColor(Color.BLUE);

					GeoLine geoLine = ((GeoLine) e.getGeometry());
					Point2Ds pnts = geoLine.getPart(0);
					if (pnts.getCount() < 3) {
						//只需要清除当前正在绘制的，不需要清除以前绘制好的
						int index = mapControl.getMap().getTrackingLayer().indexOf(TRAKCING_OBJECT_NAME);
						if (index > -1) {
							mapControl.getMap().getTrackingLayer().remove(index);
						}
					} else {
						mapControl.getMap().getTrackingLayer().add(geometry, measureAngleTag);
						drawAngleText(pnts, false);
					}
				}
				cancleEdit();
				refreshTrackingLayer();
			}
		};
	}

	private void outPutAngle(int pointsCount) {
		pointsCount++;
		if (pointsCount == 3) {
			String info = MessageFormat.format(CoreProperties.getString("String_MeasureAngle_Azimuth"), AngleUnit.getAngleInfo(getAngleUnit(), beforeAzimuth));
			stackAzimuth.push(beforeAzimuth);
			Application.getActiveApplication().getOutput().output(info);
		} else if (pointsCount > 3 && angleList != null && angleList.size() > 0) {
			double temp = angleList.get(angleList.size() - 1);
			stackAzimuth.push(beforeAzimuth);
			String info = MessageFormat.format(CoreProperties.getString("String_MeasureAngle_Azimuth_And_Angle"), AngleUnit.getAngleInfo(getAngleUnit(), beforeAzimuth), AngleUnit.getAngleInfo(getAngleUnit(), temp));
			Application.getActiveApplication().getOutput().output(info);
		}
		beforePointsCount = 0;
	}

	private void setAngleTextBox(TrackingEvent e, Point2Ds points, GeoArc geoArcAngle, Point geoArcAzimuthInnerPixel) {
		try {
			moveTextBox(labelTextBoxCurrent, MessageFormat.format(CoreProperties.getString("String_MeasureAngle_Azimuth"), AngleUnit.getAngleInfo(getAngleUnit(), e.getCurrentAzimuth())), geoArcAzimuthInnerPixel);
			if (points.getCount() >= 3) {
				Point2Ds firstLinePnts = new Point2Ds();
				firstLinePnts.add(points.getItem(points.getCount() - 2));
				firstLinePnts.add(points.getItem(points.getCount() - 3));
				GeoLine firstLine = new GeoLine(firstLinePnts);
				Point geoArcAngleInnerPixel = mapControl.getMap().mapToPixel(firstLine.getInnerPoint());
				if (geoArcAngle != null) {
					// 绘制夹角
					geoArcAngleInnerPixel = mapControl.getMap().mapToPixel(geoArcAngle.getInnerPoint());
				}
				geoArcAngleInnerPixel.setLocation(geoArcAngleInnerPixel.getX(), geoArcAngleInnerPixel.getY() + labelTextBoxTotle.getHeight() / 2);  /*- m_textBoxSecond.Width / 2*/

				if (labelTextBoxCurrent.getY() - geoArcAngleInnerPixel.getY() >= 0 &&
						labelTextBoxCurrent.getY() - geoArcAngleInnerPixel.getY() <= 3 * labelTextBoxCurrent.getHeight() / 2) {

					geoArcAngleInnerPixel.setLocation(geoArcAngleInnerPixel.getX(), geoArcAngleInnerPixel.getY() - labelTextBoxCurrent.getHeight());
				}
				if (geoArcAngleInnerPixel.getY() - labelTextBoxCurrent.getY() >= 0 &&
						geoArcAngleInnerPixel.getY() - labelTextBoxCurrent.getY() <= 3 * labelTextBoxCurrent.getHeight() / 2) {
					geoArcAngleInnerPixel.setLocation(geoArcAngleInnerPixel.getX(), geoArcAngleInnerPixel.getY() + labelTextBoxCurrent.getHeight());
				}

				String strAngle = MessageFormat.format(CoreProperties.getString("String_Map_MeasureAngle_Angle"), AngleUnit.getAngleInfo(getAngleUnit(), e.getAngle()));
				moveTextBox(labelTextBoxTotle, strAngle, geoArcAngleInnerPixel);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void moveTextBox(JLabel textBox, String text, Point location) {
		try {
			int defaultLength = 16;
			defaultLength += getSystemLength();
			if (textBox == labelTextBoxTotle) {
				defaultLength -= 5;
				if (Math.abs(location.getY() - labelTextBoxCurrent.getBounds().getY()) < labelTextBoxCurrent.getHeight()) {
					location.setLocation(location.getX(), location.getY() - labelTextBoxCurrent.getHeight() * 2);
				}
			}
			textBox.setLocation(location);
			textBox.setText(text);
			textBox.setSize((int) (((textBox.getText().length() << 3) + defaultLength) * SystemPropertyUtilties.getSystemSizeRate()), 23);
			textBox.setVisible(true);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public void startMeasure() {
		super.startMeasure();
		if (angleList != null) {
			angleList.clear();
		} else {
			angleList = new ArrayList<>();
		}
		beforeAngle = 0;
		beforeAzimuth = 0;
		beforePointsCount = 0;
		if (stackAzimuth != null) {
			stackAzimuth.clear();
		} else {
			stackAzimuth = new Stack<>();
		}
	}

	private void updateTrackingObject(GeoCompound geoCompound, boolean refreshTrackingLayer) {
		try {
			int indexRecorder = indexOfTrackingObject();
			if (indexRecorder >= 0) {
				mapControl.getMap().getTrackingLayer().set(indexRecorder, geoCompound);
			} else {
				mapControl.getMap().getTrackingLayer().add(geoCompound, TRAKCING_OBJECT_NAME);
			}

			if (refreshTrackingLayer) {
				refreshTrackingLayer();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private double calculateAngle(Point2D startPoint, Point2D endPoint) {
		double angle = 0;
		try {
			Point2Ds points = new Point2Ds();
			points.add(startPoint);
			points.add(endPoint);

			GeoLine geoLine = new GeoLine(points);

			points.clear();
			points.add(endPoint);
			points.add(new Point2D(startPoint.getX() + geoLine.getLength(), startPoint.getY()));

			GeoLine geoLine2 = new GeoLine(points);

			angle = Math.asin(geoLine2.getLength() / 2 / geoLine.getLength()) * 2;

			angle = angle * 180 / Math.PI;

			if (startPoint.getY() > endPoint.getY()) {
				angle = 360 - angle;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return angle;
	}


	private void drawAngleText(Point2Ds point2Ds, boolean isDrawing) {
		{
			try {
				if (addedTags == null) {
					addedTags = new ArrayList<>();
				}
				if (beforeUnit != null && beforeUnit != getAngleUnit()) {
					clearAddedTags();
				}
				for (int i = 0; i < angleList.size(); i++) {
					String tag = textTagTitle + i;
					if (!addedTags.contains(tag)) {
						addedTags.add(tag);

						String info = AngleUnit.getAngleInfo(getAngleUnit(), angleList.get(i));
						Point2D pntMid = point2Ds.getItem(i + 1);
						TextPart part = new TextPart(info, pntMid);
						GeoText geotext = new GeoText(part);

						TextStyle textStyle = geotext.getTextStyle();
						textStyle.setFontHeight(FontUtilties.fontSizeToMapHeight(textFontHeight * 0.283,
								mapControl.getMap(), textStyle.isSizeFixed()));
						textStyle.setAlignment(TextAlignment.BOTTOMLEFT);
						mapControl.getMap().getTrackingLayer().add(geotext, tag);
					} else {
						//如果已经绘制完成，需要替换以前对象的标签
						if (!isDrawing) {
							int index = mapControl.getMap().getTrackingLayer().indexOf(tag);
							if (index > -1) {
								mapControl.getMap().getTrackingLayer().setTag(index, tag + "FinishedMeasure");
							}
						}
					}
				}
				beforeUnit = getAngleUnit();
//				mapControl.getMap().refreshTrackingLayer();
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
		}
	}


	@Override
	protected Action getMeasureAction() {
		return Action.CREATEPOLYLINE;
	}


	@Override
	protected void addListeners() {
		super.addListeners();
		MouseListener[] mouseListeners = mapControl.getMouseListeners();
		mapControl.addMouseListener(mouseAdapter);
		for (MouseListener mouseListener : mouseListeners) {
			mapControl.removeMouseListener(mouseListener);
			mapControl.addMouseListener(mouseListener);
		}
	}

	@Override
	protected void removeListeners() {
		super.removeListeners();
		if (mapControl != null) {
			mapControl.removeMouseListener(mouseAdapter);
		}
	}

	@Override
	protected void removeLastAdded() {
		super.removeLastAdded();
		int index = angleList.size() - 1;
		if (index >= 0) {
			beforeAngle = angleList.get(angleList.size() - 1);
			beforePointsCount--;
			angleList.remove(angleList.size() - 1);
			if (index == 0) {
				labelTextBoxTotle.setVisible(false);
			}
			beforeAzimuth = stackAzimuth.pop();
		}
	}
}
