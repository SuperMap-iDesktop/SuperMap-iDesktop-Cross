package com.supermap.desktop.geometryoperation.editor;

import com.supermap.data.GeoLine;
import com.supermap.data.GeoRegion;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.desktop.Application;

/**
 * @author lixiaoyao
 */
public class ConcertEditAssist {
	private Geometry afaterChangeGeometry =null;
	private Geometry hasCommonPointGeometry=null;
	private Geometry resultGeometry=null;
	private Geometry beforeChangeGeometry=null;
	private Point2D beforePoint2D=new Point2D();

	public void setAfaterChangeGeometry(Geometry geometry){
		this.afaterChangeGeometry =geometry;
	}

	public void setHasCommonPointGeometry(Geometry geometry){
		this.hasCommonPointGeometry=geometry;
	}

	public void setBeforePoint2D(Point2D point2D){
		this.beforePoint2D=point2D;
	}

	public void setBeforeChangeGeometry(Geometry geometry){
		this.beforeChangeGeometry=geometry;
	}

	public Geometry getResultGeometry(Geometry geometry){
		return  this.resultGeometry;
	}

	private void startAssist(){

	}

	private boolean isCommonLineOrPoint(){
		boolean result=false;
		Point2Ds beforePoints=geometryToPoint2Ds(beforeChangeGeometry);
		Point2Ds hasPoints=geometryToPoint2Ds(hasCommonPointGeometry);



		return result;
	}

	private boolean findClosePoint(Point2Ds tempPoints){
		boolean result=false;
		int changePointID =0;
		double beforedistanceResult =0;
		double afterdistanceResult=0;
		int beforeClosePointID =0;
		Point2D beforeClosePoint=new Point2D();
		int afterClosePointID=0;
		Point2D afterClosePoint=new Point2D();

		for (int i = 0; i < tempPoints.getCount(); i++) {
			Point2D tempPoint2D = tempPoints.getItem(i);
			if (tempPoint2D.equals(this.beforePoint2D)){
				changePointID =i;
			}
			if (i==0){
				beforedistanceResult =distance(tempPoint2D,beforePoint2D);
				beforeClosePointID =i;
				beforeClosePoint=tempPoint2D;
				afterdistanceResult=distance(tempPoint2D,beforePoint2D);
				afterClosePointID=i;
				afterClosePoint=tempPoint2D;
			}
			else if (distance(tempPoint2D,beforePoint2D)< beforedistanceResult){
				beforedistanceResult =distance(tempPoint2D,beforePoint2D);
				beforeClosePointID =i;
				beforeClosePoint=tempPoint2D;
			}
		}
		return false;
	}

	private Point2Ds geometryToPoint2Ds(Geometry geometry) {
		Point2Ds resultPoint2Ds = new Point2Ds();
		try {
			if (geometry.getType() == GeometryType.GEOLINE) {
				for (int i = 0; i < ((GeoLine)geometry).getPartCount(); ++i) {
					Point2Ds tempPoint2Ds = ((GeoLine)geometry).getPart(i);
					for (int j = 0; j < tempPoint2Ds.getCount(); ++j) {
						resultPoint2Ds.add(tempPoint2Ds.getItem(j));
					}
				}
			} else if (geometry.getType() == GeometryType.GEOREGION) {
				for (int i = 0; i < ((GeoRegion)geometry).getPartCount(); ++i) {
					Point2Ds tempPoint2Ds = ((GeoRegion)geometry).getPart(i);
					for (int j = 0; j < tempPoint2Ds.getCount(); ++j) {
						resultPoint2Ds.add(tempPoint2Ds.getItem(j));
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex.toString());
		}
		return resultPoint2Ds;
	}

	private double distance(Point2D p1,Point2D p2){
		double result=Math.sqrt((p1.getX()-p2.getX())*(p1.getX()-p2.getX())+(p1.getY()-p2.getY())*(p1.getY()-p2.getY()));
		return result;
	}
}
