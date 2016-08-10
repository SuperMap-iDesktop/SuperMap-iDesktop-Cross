package com.supermap.desktop.mapview.geometry.property;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.property.AbstractPropertyControl;
import com.supermap.desktop.enums.AreaUnit;
import com.supermap.desktop.enums.LengthUnit;
import com.supermap.desktop.enums.PropertyType;
import com.supermap.desktop.utilities.GeoTypeUtilities;
import com.supermap.desktop.utilities.PrjCoordSysUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;

public class GeometrySpatialPropertyControl extends AbstractPropertyControl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int BSpline_Convert_Segment = 20; // GeoArc 转换为 GeoLine 的默认转换段数
	private static final int Cardinal_Convert_Segment = 20; // Cardinal 转换为 GeoLine 的默认转换段数
	private static final int Chord_Convert_Segment = 72; // GeoChord 转换为 GeoRegion 的默认转换段数
	private static final int Circle_Convert_Segment = 72; // GeoCircle 转换为 GeoRegion 的默认转换段数
	private static final int Curve_Convert_Segment = 72; // GeoCurve 转换为 GeoLine 的默认转换段数
	private static final int Ellipse_Convert_Segment = 72; // GeoEllipse 转换为 GeoRegion 的默认转换段数
	private static final int EllipticArc_Convert_Segment = 72; // GeoEllipticArc 转换为 GeoLine 的默认转换段数
	private static final int Pie_Convert_Segment = 100; // GeoPie 转换为 GeoRegion 的默认转换段数
	private static final int RoundRectangle_Convert_Segment = 100; // GeoRoundRectangle 转换为 GeoRegion 的默认转换段数

	private JPanel panelBasic;
	private JLabel labelGeometryType;
	private JTextField textFieldGeometryType;
	private JLabel labelLength;
	private JTextField textFieldLength;
	private JComboBox<LengthUnit> comboBoxLengthUnit;
	private JLabel labelPerimeter;
	private JTextField textFieldPerimeter;
	private JComboBox<LengthUnit> comboBoxPerimeterUnit;
	private JLabel labelGeometryArea;
	private JTextField textFieldArea;
	private JComboBox<AreaUnit> comboBoxAreaUnit;

	private JPanel panelBounds;
	private JLabel labelLeft;
	private JTextField textFieldLeft;
	private JLabel labelTop;
	private JTextField textFieldTop;
	private JLabel labelRight;
	private JTextField textFieldRight;
	private JLabel labelBottom;
	private JTextField textFieldBottom;

	private PrjCoordSys prjCoordSys; // 数据集的投影，用来处理单位信息
	private Geometry geometry; // 地理对象
	private Length length = null;
	private Length perimeter = null;
	private Area area = null;

	private ItemListener itemListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				if (e.getSource() == GeometrySpatialPropertyControl.this.comboBoxLengthUnit) {
					comboBoxLengthSelectChange();
				} else if (e.getSource() == GeometrySpatialPropertyControl.this.comboBoxPerimeterUnit) {
					comboBoxPerimeterSelectChange();
				} else if (e.getSource() == GeometrySpatialPropertyControl.this.comboBoxAreaUnit) {
					comboBoxAreaSelectChange();
				}
			}
		}
	};

	public GeometrySpatialPropertyControl(Geometry geometry, PrjCoordSys prjCoordSys) {
		super(ControlsProperties.getString("String_SpatialInfo"));
		initializeComponents();
		initializeResources();
		setData(geometry, prjCoordSys);
	}

	@Override
	public void refreshData() {
		// do nothing
	}

	public void setData(Geometry geometry, PrjCoordSys prjCoordSys) {
		if (this.geometry != null) {
			this.geometry.dispose();
		}
		this.geometry = geometry;
		this.prjCoordSys = prjCoordSys;

		unregisterEvents();
		updateLengthAndArea(this.geometry, this.prjCoordSys);
		updateBasicInfo(this.geometry);
		updateBoundsInfo(this.geometry);
		setComponentEnabled();
		registerEvents();
	}

	public Geometry getGeometry() {
		return this.geometry;
	}

	public PrjCoordSys getPrjCoordSys() {
		return this.prjCoordSys;
	}

	@Override
	public PropertyType getPropertyType() {
		return PropertyType.GEOMETRY_SPATIAL;
	}

	private void initializeComponents() {
		this.panelBasic = new JPanel();
		this.panelBasic.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_BasicInfo")));
		this.panelBounds = new JPanel();
		this.panelBounds.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_Bounds")));

		GridBagLayout mainLayout = new GridBagLayout();
		this.setLayout(mainLayout);
		// @formatter:off
		add(this.panelBasic, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 10), 0, 0));
		add(this.panelBounds, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 10, 10), 0, 0));
		// @formatter:on

		// 基本信息
		this.labelGeometryType = new JLabel("GeometryType:");
		this.textFieldGeometryType = new JTextField();
		this.textFieldGeometryType.setEditable(false);
		this.textFieldGeometryType.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
		this.labelLength = new JLabel("Length:");
		this.textFieldLength = new JTextField();
		this.textFieldLength.setEditable(false);
		this.textFieldLength.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
		this.comboBoxLengthUnit = createComboBoxUnit();
		this.labelPerimeter = new JLabel("Perimeter:");
		this.textFieldPerimeter = new JTextField();
		this.textFieldPerimeter.setEditable(false);
		this.textFieldPerimeter.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
		this.comboBoxPerimeterUnit = createComboBoxUnit();
		this.labelGeometryArea = new JLabel("Area:");
		this.textFieldArea = new JTextField();
		this.textFieldArea.setEditable(false);
		this.textFieldArea.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
		this.comboBoxAreaUnit = createComboBoxAreaUnit();

		GridBagLayout basicLayout = new GridBagLayout();
		this.panelBasic.setLayout(basicLayout);
		// @formatter:off
		this.panelBasic.add(this.labelGeometryType, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0), 0, 0));
		this.panelBasic.add(this.textFieldGeometryType, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 5, 0, 0), 0, 0));
		this.panelBasic.add(this.labelLength, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10, 0, 0), 0, 0));
		this.panelBasic.add(this.textFieldLength, new GridBagConstraints(1, 1, 1, 1, 0.8, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 0, 0), 0, 0));
		this.panelBasic.add(this.comboBoxLengthUnit, new GridBagConstraints(2, 1, 1, 1, 0.2, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 0, 10), 0, 0));
		this.panelBasic.add(this.labelPerimeter, new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10, 0, 0), 0, 0));
		this.panelBasic.add(this.textFieldPerimeter, new GridBagConstraints(1, 2, 1, 1, 0.8, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 0, 0), 0, 0));
		this.panelBasic.add(this.comboBoxPerimeterUnit, new GridBagConstraints(2, 2, 1, 1, 0.2, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 0, 10), 0, 0));
		this.panelBasic.add(this.labelGeometryArea, new GridBagConstraints(0, 3, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10, 10, 0), 0, 0));
		this.panelBasic.add(this.textFieldArea, new GridBagConstraints(1, 3, 1, 1, 0.8, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 10, 0), 0, 0));
		this.panelBasic.add(this.comboBoxAreaUnit, new GridBagConstraints(2, 3, 1, 1, 0.2, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 10, 10), 0, 0));
		// @formatter:on

		// 外接矩形
		this.labelLeft = new JLabel("Left:");
		this.textFieldLeft = new JTextField();
		this.textFieldLeft.setEditable(false);
		this.textFieldLeft.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
		this.labelTop = new JLabel("Top:");
		this.textFieldTop = new JTextField();
		this.textFieldTop.setEditable(false);
		this.textFieldTop.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
		this.labelRight = new JLabel("Right:");
		this.textFieldRight = new JTextField();
		this.textFieldRight.setEditable(false);
		this.textFieldRight.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
		this.labelBottom = new JLabel("Bottom:");
		this.textFieldBottom = new JTextField();
		this.textFieldBottom.setEditable(false);
		this.textFieldBottom.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);

		GridBagLayout boundsLayout = new GridBagLayout();
		this.panelBounds.setLayout(boundsLayout);
		// @formatter:off
		this.panelBounds.add(this.labelLeft, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0), 0, 0));
		this.panelBounds.add(this.textFieldLeft, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 5, 0, 10), 0, 0));
		this.panelBounds.add(this.labelTop, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10, 0, 0), 0, 0));
		this.panelBounds.add(this.textFieldTop, new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 0, 10), 0, 0));
		this.panelBounds.add(this.labelRight, new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10, 0, 0), 0, 0));
		this.panelBounds.add(this.textFieldRight, new GridBagConstraints(1, 2, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 0, 10), 0, 0));
		this.panelBounds.add(this.labelBottom, new GridBagConstraints(0, 3, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10, 10, 0), 0, 0));
		this.panelBounds.add(this.textFieldBottom, new GridBagConstraints(1, 3, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 10, 10), 0, 0));
		// @formatter:on
	}

	private void initializeResources() {
		this.labelGeometryType.setText(ControlsProperties.getString("String_LabelGeometryType"));
		this.labelLength.setText(ControlsProperties.getString("String_LabelLength"));
		this.labelPerimeter.setText(ControlsProperties.getString("String_LabelPerimeter"));
		this.labelGeometryArea.setText(ControlsProperties.getString("String_LabelArea"));
		this.labelLeft.setText(ControlsProperties.getString(ControlsProperties.Label_Left));
		this.labelTop.setText(ControlsProperties.getString(ControlsProperties.Label_Top));
		this.labelRight.setText(ControlsProperties.getString(ControlsProperties.Label_Right));
		this.labelBottom.setText(ControlsProperties.getString(ControlsProperties.Label_Bottom));
	}

	private void registerEvents() {
		this.comboBoxLengthUnit.addItemListener(this.itemListener);
		this.comboBoxPerimeterUnit.addItemListener(this.itemListener);
		this.comboBoxAreaUnit.addItemListener(this.itemListener);
	}

	private void unregisterEvents() {
		this.comboBoxLengthUnit.removeItemListener(this.itemListener);
		this.comboBoxPerimeterUnit.removeItemListener(this.itemListener);
		this.comboBoxAreaUnit.removeItemListener(this.itemListener);
	}

	private void updateBasicInfo(Geometry geometry) {
		this.textFieldGeometryType.setText(GeoTypeUtilities.toString(this.geometry.getType()));
		this.textFieldLength.setText(BigDecimal.valueOf(this.length.getValue()).toString());
		this.textFieldPerimeter.setText(BigDecimal.valueOf(this.perimeter.getValue()).toString());
		this.textFieldArea.setText(BigDecimal.valueOf(this.area.getValue()).toString());
		this.comboBoxLengthUnit.setSelectedItem(this.length.getUnit());
		this.comboBoxPerimeterUnit.setSelectedItem(this.perimeter.getUnit());
		this.comboBoxAreaUnit.setSelectedItem(this.perimeter.getUnit());
	}

	private void updateBoundsInfo(Geometry geometry) {
		this.textFieldLeft.setText(BigDecimal.valueOf(geometry.getBounds().getLeft()).toString());
		this.textFieldTop.setText(BigDecimal.valueOf(geometry.getBounds().getTop()).toString());
		this.textFieldRight.setText(BigDecimal.valueOf(geometry.getBounds().getRight()).toString());
		this.textFieldBottom.setText(BigDecimal.valueOf(geometry.getBounds().getBottom()).toString());
	}

	// @formatter:off
	/**
	 * 不使用 Recordset 读取 SmLength/SmPerimeter/SmArea 是因为不是所有对象都有这三个系统字段
	 * 这里的长度选择单位指的是纬度，与经纬坐标下从 Geometry 里取出的长度单位并不同，
	 * 因此在经纬坐标系下先做一次投影转换，转换为单位米的长度
	 * 
	 * @param geometry
	 * @param prjCoordSys
	 */
	// @formatter:on
	private void updateLengthAndArea(Geometry geometry, PrjCoordSys prjCoordSys) {
		this.length = new Length(0, LengthUnit.METER);
		this.perimeter = new Length(0, LengthUnit.METER);
		this.area = new Area(0, AreaUnit.METER);

		if (geometry != null) {
			if (geometry.getType() == GeometryType.GEOBSPLINE) {

				// 二维B样条曲线几何对象
				getGeoBSplineProperty((GeoBSpline) geometry);
			} else if (geometry.getType() == GeometryType.GEOCARDINAL) {

				// 二维Cardinal样条曲线几何对象
				getGeoCardinalProperty((GeoCardinal) geometry);
			} else if (geometry.getType() == GeometryType.GEOCHORD) {

				// 弓形几何对象
				getGeoChordProperty((GeoChord) geometry);
			} else if (geometry.getType() == GeometryType.GEOCIRCLE) {

				// 圆几何对象
				getGeoCircleProperty((GeoCircle) geometry);
			} else if (geometry.getType() == GeometryType.GEOCOMPOUND) {

				// 复合几何对象
				this.length.setEnabled(false);
				this.perimeter.setEnabled(false);
				this.area.setEnabled(false);
			} else if (geometry.getType() == GeometryType.GEOCURVE) {

				// 二维曲线几何对象
				getGeoCurveProperty((GeoCurve) geometry);
			} else if (geometry.getType() == GeometryType.GEOELLIPSE) {

				// 椭圆几何对象
				getGeoEllipseProperty((GeoEllipse) geometry);
			} else if (geometry.getType() == GeometryType.GEOELLIPTICARC) {

				// 椭圆弧几何对象
				getGeoEllipticArcProperty((GeoEllipticArc) geometry);
			} else if (geometry.getType() == GeometryType.GEOLINE) {

				// 二维线几何对象
				getGeoLineProperty((GeoLine) geometry);
			} else if (geometry.getType() == GeometryType.GEOLINE3D) {

				// 三维线几何对象
				getGeoLine3DProperty((GeoLine3D) geometry);
			} else if (geometry.getType() == GeometryType.GEOLINEM) {

				// 路由对象
				getGeoLineMProperty((GeoLineM) geometry);
			} else if (geometry.getType() == GeometryType.GEOPICTURE) {

				// 二维图片几何对象
				this.length.setEnabled(false);
				this.perimeter.setEnabled(false);
				this.area.setEnabled(false);
			} else if (geometry.getType() == GeometryType.GEOPIE) {

				// 扇面几何对象
				getGeoPieProperty((GeoPie) geometry);
			} else if (geometry.getType() == GeometryType.GEOPOINT) {

				// 二维点几何对象
				this.length.setEnabled(false);
				this.perimeter.setEnabled(false);
				this.area.setEnabled(false);
			} else if (geometry.getType() == GeometryType.GEORECTANGLE) {

				// 二维矩形几何对象
				getGeoRectangleProperty((GeoRectangle) geometry);
			} else if (geometry.getType() == GeometryType.GEOREGION) {

				// 二维面几何对象
				getGeoRegionProperty((GeoRegion) geometry);
			} else if (geometry.getType() == GeometryType.GEOREGION3D) {

				// 三维面几何对象
				getGeoRegion3DProperty((GeoRegion3D) geometry);
			} else if (geometry.getType() == GeometryType.GEOROUNDRECTANGLE) {

				// 二维圆角矩形几何对象
				getGeoRoundRectangleProperty((GeoRoundRectangle) geometry);
			} else if (geometry.getType() == GeometryType.GEOTEXT) {

				// 文本类
				this.length.setEnabled(false);
				this.perimeter.setEnabled(false);
				this.area.setEnabled(false);
			} else if (geometry.getType() == GeometryType.GEOPOINT3D) {

				// 三维点几何对象
				this.length.setEnabled(false);
				this.perimeter.setEnabled(false);
				this.area.setEnabled(false);
			} else if (geometry.getType() == GeometryType.GEOPARAMETRICREGIONCOMPOUND) {

				// 复合参数化面几何对象
				getGeoParametricRegionCompoundProperty((GeoParametricRegionCompound) geometry);
			} else if (geometry.getType() == GeometryType.GEOPARAMETRICLINECOMPOUND) {

				// 复合参数化线几何对象
				getGeoParametricLineCompoundProperty((GeoParametricLineCompound) geometry);
			} else if (geometry.getType() == GeometryType.GEOMODEL) {

				// 三维模型几何对象
				this.length.setEnabled(false);
				this.perimeter.setEnabled(false);
				this.area.setEnabled(false);
			}
		}

		Unit unit = this.prjCoordSys.getType() == PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE ? Unit.METER : this.prjCoordSys.getCoordUnit();
		this.length.setUnit(LengthUnit.convertForm(unit));
		this.perimeter.setUnit(LengthUnit.convertForm(unit));
		this.area.setUnit(AreaUnit.convertFrom(unit));
	}

	// @formatter:off
	/**
	 * 获取 geoLine 在投影坐标下的长度
	 * 1.平面坐标系下，直接获取
	 * 2.投影坐标系统下，直接获取
	 * 3.地理坐标系下，使用地理坐标系构造一个默认的 Albers投影，进行转换
	 * @param geoLine
	 * @param prjCoordSys
	 */
	// @formatter:on
	private void getLineLength(GeoLine geoLine, PrjCoordSys prjCoordSys) {
		GeoLine line = null; // 转换之后不会重新计算长度，需要转换完成做一些处理

		try {
			if (geoLine != null && prjCoordSys != null && prjCoordSys.getType() == PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE) {
				PrjCoordSysUtilities.convertGeoLine(geoLine, prjCoordSys.getGeoCoordSys());
			}

			line = new GeoLine();
			for (int i = 0; i < geoLine.getPartCount(); i++) {
				line.addPart(geoLine.getPart(i));
			}
			this.length.setValue(line.getLength());
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			if (geoLine != null) {
				geoLine.dispose();
			}
			if (line != null) {
				line.dispose();
			}
		}
	}

	// @formatter:off
	/**
	 *  获取 geoRegion 在投影坐标下的周长和面积
	 * 1.平面坐标系下，直接获取
	 * 2.投影坐标系统下，直接获取
	 * 3.地理坐标系下，使用地理坐标系构造一个默认的 Albers投影，进行转换
	 * @param geoRegion
	 * @param prjCoordSys
	 */
	// @formatter:on
	private void getRegionPerimeterAndArea(GeoRegion geoRegion, PrjCoordSys prjCoordSys) {
		GeoRegion region = null; // 转换之后不会重新计算周长和面积，需要转换完成做一些处理

		try {
			if (geoRegion != null && prjCoordSys != null && prjCoordSys.getType() == PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE) {
				PrjCoordSysUtilities.convertGeoRegion(geoRegion, prjCoordSys.getGeoCoordSys());
			}

			region = new GeoRegion();
			for (int i = 0; i < geoRegion.getPartCount(); i++) {
				region.addPart(geoRegion.getPart(i));
			}
			this.perimeter.setValue(region.getPerimeter());
			this.area.setValue(region.getArea());
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			if (geoRegion != null) {
				geoRegion.dispose();
			}
			if (region != null) {
				region.dispose();
			}
		}
	}

	/**
	 * 二维B样条曲线几何对象
	 * 
	 * @param geoBSpline
	 */
	private void getGeoBSplineProperty(GeoBSpline geoBSpline) {
		this.length.setEnabled(true);
		this.perimeter.setEnabled(false);
		this.area.setEnabled(false);

		if (this.prjCoordSys != null && this.prjCoordSys.getType() != PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE) {
			this.length.setValue(geoBSpline.getLength());
		} else {
			getLineLength(geoBSpline.convertToLine(BSpline_Convert_Segment), this.prjCoordSys);
		}
	}

	/**
	 * 二维Cardinal样条曲线几何对象
	 * 
	 * @param geoCardinal
	 */
	private void getGeoCardinalProperty(GeoCardinal geoCardinal) {
		this.length.setEnabled(true);
		this.perimeter.setEnabled(false);
		this.area.setEnabled(false);

		if (this.prjCoordSys != null && this.prjCoordSys.getType() != PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE) {
			this.length.setValue(geoCardinal.getLength());
		} else {
			getLineLength(geoCardinal.convertToLine(Cardinal_Convert_Segment), this.prjCoordSys);
		}
	}

	/**
	 * 弓形几何对象
	 * 
	 * @param geoChord
	 */
	private void getGeoChordProperty(GeoChord geoChord) {
		this.length.setEnabled(false);
		this.perimeter.setEnabled(true);
		this.area.setEnabled(true);

		if (this.prjCoordSys != null && this.prjCoordSys.getType() != PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE) {
			this.perimeter.setValue(geoChord.getPerimeter());
			this.area.setValue(geoChord.getArea());
		} else {
			getRegionPerimeterAndArea(geoChord.convertToRegion(Chord_Convert_Segment), this.prjCoordSys);
		}
	}

	/**
	 * 圆几何对象
	 * 
	 * @param geoCircle
	 */
	private void getGeoCircleProperty(GeoCircle geoCircle) {
		this.length.setEnabled(false);
		this.perimeter.setEnabled(true);
		this.area.setEnabled(true);

		if (this.prjCoordSys != null && this.prjCoordSys.getType() != PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE) {
			this.perimeter.setValue(geoCircle.getPerimeter());
			this.area.setValue(geoCircle.getArea());
		} else {
			getRegionPerimeterAndArea(geoCircle.convertToRegion(Circle_Convert_Segment), prjCoordSys);
		}
	}

	/**
	 * 二维曲线几何对象
	 * 
	 * @param geoCurve
	 */
	private void getGeoCurveProperty(GeoCurve geoCurve) {
		this.length.setEnabled(true);
		this.perimeter.setEnabled(false);
		this.area.setEnabled(false);

		if (this.prjCoordSys != null && this.prjCoordSys.getType() != PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE) {
			this.length.setValue(geoCurve.getLength());
		} else {
			getLineLength(geoCurve.convertToLine(Curve_Convert_Segment), this.prjCoordSys);
		}
	}

	/**
	 * 椭圆几何对象
	 * 
	 * @param geoEllipse
	 */
	private void getGeoEllipseProperty(GeoEllipse geoEllipse) {
		this.length.setEnabled(false);
		this.perimeter.setEnabled(true);
		this.area.setEnabled(true);

		if (this.prjCoordSys != null && this.prjCoordSys.getType() != PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE) {
			this.perimeter.setValue(geoEllipse.getPerimeter());
			this.area.setValue(geoEllipse.getArea());
		} else {
			getRegionPerimeterAndArea(geoEllipse.convertToRegion(Ellipse_Convert_Segment), this.prjCoordSys);
		}
	}

	/**
	 * 椭圆弧几何对象
	 * 
	 * @param geoEllipticArc
	 */
	private void getGeoEllipticArcProperty(GeoEllipticArc geoEllipticArc) {
		this.length.setEnabled(true);
		this.perimeter.setEnabled(false);
		this.area.setEnabled(false);

		if (this.prjCoordSys != null && this.prjCoordSys.getType() != PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE) {
			this.length.setValue(geoEllipticArc.getLength());
		} else {
			getLineLength(geoEllipticArc.convertToLine(EllipticArc_Convert_Segment), this.prjCoordSys);
		}
	}

	/**
	 * 二维线几何对象
	 * 
	 * @param geoLine
	 */
	private void getGeoLineProperty(GeoLine geoLine) {
		this.length.setEnabled(true);
		this.perimeter.setEnabled(false);
		this.area.setEnabled(false);

		if (this.prjCoordSys != null && this.prjCoordSys.getType() != PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE) {
			this.length.setValue(geoLine.getLength());
		} else {
			getLineLength(geoLine.clone(), this.prjCoordSys);
		}
	}

	/**
	 * 三维线几何对象
	 * 
	 * @param geoLine3D
	 */
	private void getGeoLine3DProperty(GeoLine3D geoLine3D) {
		this.length.setEnabled(true);
		this.perimeter.setEnabled(false);
		this.area.setEnabled(false);
		this.length.setValue(geoLine3D.getLength());
	}

	/**
	 * 路由线几何对象
	 * 
	 * @param geoLineM
	 */
	private void getGeoLineMProperty(GeoLineM geoLineM) {
		this.length.setEnabled(true);
		this.perimeter.setEnabled(false);
		this.area.setEnabled(false);

		if (this.prjCoordSys != null && this.prjCoordSys.getType() != PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE) {
			this.length.setValue(geoLineM.getLength());
		} else {
			getLineLength(geoLineM.convertToLine(), this.prjCoordSys);
		}
	}

	/**
	 * 扇面几何对象
	 * 
	 * @param geoPie
	 */
	private void getGeoPieProperty(GeoPie geoPie) {
		this.length.setEnabled(false);
		this.perimeter.setEnabled(true);
		this.area.setEnabled(true);

		if (this.prjCoordSys != null && this.prjCoordSys.getType() != PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE) {
			this.perimeter.setValue(geoPie.getPerimeter());
			this.area.setValue(geoPie.getArea());
		} else {
			getRegionPerimeterAndArea(geoPie.convertToRegion(Pie_Convert_Segment), this.prjCoordSys);
		}
	}

	/**
	 * 矩形几何对象
	 * 
	 * @param geoRectangle
	 */
	private void getGeoRectangleProperty(GeoRectangle geoRectangle) {
		this.length.setEnabled(false);
		this.perimeter.setEnabled(true);
		this.area.setEnabled(true);

		if (this.prjCoordSys != null && this.prjCoordSys.getType() != PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE) {
			this.perimeter.setValue(geoRectangle.getPerimeter());
			this.area.setValue(geoRectangle.getArea());
		} else {
			getRegionPerimeterAndArea(geoRectangle.convertToRegion(), prjCoordSys);
		}
	}

	/**
	 * 二维面几何对象
	 * 
	 * @param geoRegion
	 */
	private void getGeoRegionProperty(GeoRegion geoRegion) {
		this.length.setEnabled(false);
		this.perimeter.setEnabled(true);
		this.area.setEnabled(true);

		if (this.prjCoordSys != null && this.prjCoordSys.getType() != PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE) {
			this.perimeter.setValue(geoRegion.getPerimeter());
			this.area.setValue(geoRegion.getArea());
		} else {
			getRegionPerimeterAndArea(geoRegion.clone(), this.prjCoordSys);
		}
	}

	/**
	 * 三维面几何对象
	 * 
	 * @param geoRegion3D
	 */
	private void getGeoRegion3DProperty(GeoRegion3D geoRegion3D) {
		this.length.setEnabled(false);
		this.perimeter.setEnabled(true);
		this.area.setEnabled(true);
		this.perimeter.setValue(geoRegion3D.getPerimeter());
		this.area.setValue(geoRegion3D.getArea());
	}

	/**
	 * 圆角矩形几何对象
	 * 
	 * @param geoRoundRectangle
	 */
	private void getGeoRoundRectangleProperty(GeoRoundRectangle geoRoundRectangle) {
		this.length.setEnabled(false);
		this.perimeter.setEnabled(true);
		this.area.setEnabled(true);

		if (this.prjCoordSys != null && this.prjCoordSys.getType() != PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE) {
			this.perimeter.setValue(geoRoundRectangle.getPerimeter());
			this.area.setValue(geoRoundRectangle.getArea());
		} else {
			getRegionPerimeterAndArea(geoRoundRectangle.convertToRegion(RoundRectangle_Convert_Segment), this.prjCoordSys);
		}
	}

	// @formatter:off
	/**
	 * 参数化面几何对象
	 * 支持的子对象有：GeoParametricRegionCompound、GeoParametricRegion、GeoRegion、GeoCircle
	 * 很复杂，非常少见，暂时不做转换，直接获取值
	 * @param geoParametricRegionCompound
	 */
	// @formatter:on
	private void getGeoParametricRegionCompoundProperty(GeoParametricRegionCompound geoParametricRegionCompound) {
		this.length.setEnabled(false);
		this.perimeter.setEnabled(true);
		this.area.setEnabled(true);
		this.perimeter.setValue(geoParametricRegionCompound.getPerimeter());
		this.area.setValue(geoParametricRegionCompound.getArea());
	}

	// @formatter:off
	/**
	 * 参数化线几何对象
	 * 支持的子对象有：GeoParametricLineCompound、GeoParametricLine、GeoLine、GeoArc
	 * 很复杂，非常少见，暂时不做转换，直接获取值
	 * @param geoParametricLineCompound
	 */
	// @formatter:on
	private void getGeoParametricLineCompoundProperty(GeoParametricLineCompound geoParametricLineCompound) {
		this.length.setEnabled(true);
		this.perimeter.setEnabled(false);
		this.area.setEnabled(false);
		this.length.setValue(geoParametricLineCompound.getLength());
	}

	private JComboBox<LengthUnit> createComboBoxUnit() {
		JComboBox<LengthUnit> comboBox = new JComboBox<LengthUnit>();

		comboBox.addItem(LengthUnit.MILLIMETER);
		comboBox.addItem(LengthUnit.CENTIMETER);
		comboBox.addItem(LengthUnit.DECIMETER);
		comboBox.addItem(LengthUnit.METER);
		comboBox.addItem(LengthUnit.KILOMETER);
		comboBox.addItem(LengthUnit.INCH);
		comboBox.addItem(LengthUnit.FOOT);
		comboBox.addItem(LengthUnit.MILE);
		comboBox.addItem(LengthUnit.DEGREE);
		comboBox.addItem(LengthUnit.YARD);
		comboBox.setSelectedItem(LengthUnit.METER);
		comboBox.setEditable(false);
		comboBox.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
		return comboBox;
	}

	private JComboBox<AreaUnit> createComboBoxAreaUnit() {
		JComboBox<AreaUnit> comboBox = new JComboBox<AreaUnit>();

		comboBox.addItem(AreaUnit.MILIMETER);
		comboBox.addItem(AreaUnit.CENTIMETER);
		comboBox.addItem(AreaUnit.DECIMETER);
		comboBox.addItem(AreaUnit.METER);
		comboBox.addItem(AreaUnit.KILOMETER);
		comboBox.addItem(AreaUnit.INCH);
		comboBox.addItem(AreaUnit.FOOT);
		comboBox.addItem(AreaUnit.MILE);
		comboBox.addItem(AreaUnit.YARD);
		comboBox.addItem(AreaUnit.ACRE);
		comboBox.setSelectedItem(AreaUnit.METER);
		comboBox.setEditable(false);
		comboBox.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
		return comboBox;
	}

	private void comboBoxLengthSelectChange() {
		try {
			LengthUnit selectedUnit = (LengthUnit) this.comboBoxLengthUnit.getSelectedItem();

			if (selectedUnit != this.length.getUnit()) {
				this.length.convertTo(selectedUnit);
				this.textFieldLength.setText(String.valueOf(this.length.getValue()));
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void comboBoxPerimeterSelectChange() {
		try {
			LengthUnit selectedUnit = (LengthUnit) this.comboBoxPerimeterUnit.getSelectedItem();

			if (selectedUnit != this.perimeter.getUnit()) {
				this.perimeter.convertTo(selectedUnit);
				this.textFieldPerimeter.setText(String.valueOf(this.perimeter.getValue()));
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void comboBoxAreaSelectChange() {
		try {
			AreaUnit selectedUnit = (AreaUnit) this.comboBoxAreaUnit.getSelectedItem();

			if (selectedUnit != this.area.getUnit()) {
				this.area.convertTo(selectedUnit);
				this.textFieldArea.setText(String.valueOf(this.area.getValue()));
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void setComponentEnabled() {
		this.comboBoxLengthUnit.setEnabled(this.length.isEnabled);
		this.comboBoxPerimeterUnit.setEnabled(this.perimeter.isEnabled);
		this.comboBoxAreaUnit.setEnabled(this.area.isEnabled);
	}

	private class Length {
		private boolean isEnabled = true;
		private double value = 0.0;
		private LengthUnit unit = LengthUnit.METER;

		public Length(double value, LengthUnit unit) {
			this.value = value;
			this.unit = unit;
		}

		public boolean isEnabled() {
			return this.isEnabled;
		}

		public void setEnabled(boolean isEnabled) {
			this.isEnabled = isEnabled;
		}

		public double getValue() {
			return this.value;
		}

		public void setValue(double value) {
			this.value = value;
		}

		public LengthUnit getUnit() {
			return this.unit;
		}

		public void setUnit(LengthUnit unit) {
			this.unit = unit;
		}

		public void convertTo(LengthUnit unit) {
			this.value = this.value * ((double) this.unit.getValue() / unit.getValue());
			setUnit(unit);
		}
	}

	private class Area {

		private boolean isEnabled = true;
		private double value = 0.0;
		private AreaUnit unit = AreaUnit.METER;

		public Area(double value, AreaUnit unit) {
			this.value = value;
			this.unit = unit;
		}

		public boolean isEnabled() {
			return this.isEnabled;
		}

		public void setEnabled(boolean isEnabled) {
			this.isEnabled = isEnabled;
		}

		public double getValue() {
			return this.value;
		}

		public void setValue(double value) {
			this.value = value;
		}

		public AreaUnit getUnit() {
			return this.unit;
		}

		public void setUnit(AreaUnit unit) {
			this.unit = unit;
		}

		public void convertTo(AreaUnit unit) {
			this.value = this.value * ((double) this.unit.getValue() / unit.getValue());
			setUnit(unit);
		}
	}
}
