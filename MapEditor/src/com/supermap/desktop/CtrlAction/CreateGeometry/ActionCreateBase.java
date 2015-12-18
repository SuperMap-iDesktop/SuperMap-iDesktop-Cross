package com.supermap.desktop.CtrlAction.CreateGeometry;

import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.mapping.Layer;
import com.supermap.ui.Action;

abstract public class ActionCreateBase extends CtrlAction {

	public ActionCreateBase(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	public Action getAction() {
		return Action.NULL;
	}

	public boolean isSupportDatasetType(DatasetType datasetType) {
		return false;
	}

	@Override
	public void run() {
		try {
			IForm activeForm = Application.getActiveApplication().getActiveForm();
			if(activeForm instanceof IFormMap) {
				((IFormMap) activeForm).getMapControl().setAction(this.getAction());
				// 获取焦点
				((IFormMap) activeForm).getMapControl().requestFocusInWindow();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		try {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			if (formMap != null) {
				Layer layer = formMap.getMapControl().getActiveEditableLayer();
				if (layer != null && layer.getDataset() != null && layer.isEditable() && this.isSupportDatasetType(layer.getDataset().getType())) {
					enable = true;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return enable;
	}

	@Override
	public boolean check() {
		boolean check = false;

		try {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			if (formMap != null && formMap.getMapControl().getAction() == this.getAction()) {
				check = true;
			}

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return check;
	}

	// static Action CREATE_GEOLEGEND
	// 在布局或者地图中可编辑的CAD图层中绘制地图图例对象。
	// static Action CREATE_MAP_SCALE
	// 在布局或者地图中可编辑的CAD图层中绘制地图比例尺对象。
	// static Action CREATE_NORTH_ARROW
	// 在布局或者地图中可编辑的CAD图层中绘制指北针对象。
	// static Action CREATE_POLYGON2_CARDINAL
	// 在可编辑图层中绘制 Cardinal 曲线，该 Cardinal 曲线是由圆弧、曲线、直线组成的多段线，在编辑结束后，连接该 Cardinal 曲线的起始点和终止点构成多边形。
	// static Action CREATE_POLYGON2_FREE_POLYLINE
	// 在可编辑图层中绘制自由线，该自由线是由圆弧、曲线、直线组成的多段线，在编辑结束后，连接该多折线的起始点和终止点构成多边形。
	// static Action CREATE_POLYGON2_POLYLINE
	// 在可编辑图层中绘制多折线，该多折线是由圆弧、曲线、直线组成的多段线，在编辑结束后，连接该多折线的起始点和终止点构成多边形。
	// static Action CREATE_POLYLINE2_CARDINAL
	// 在可编辑图层中绘制 Cardinal 曲线，该 Cardinal 曲线是由圆弧、曲线、直线组成的多段线。
	// static Action CREATE_POLYLINE2_FREE_POLYLINE
	// 在可编辑图层中绘制自由线，该自由线是由圆弧、曲线、直线组成的多段线。
	// static Action CREATE_POLYLINE2_POLYLINE
	// 在可编辑图层中绘制多折线，该多折线是由圆弧、曲线、直线组成的多段线。

	// static Action MOVE_THEME_GRADUATE_SYMBOL
	// 移动等级符号专题图的专题图元素。
	// static Action MOVE_THEME_GRAPH
	// 移动统计专题图的专题图元素。
	// static Action MOVE_THEME_LABEL
	// 移动标签专题图的专题图元素。
	// static Action VECTORLIZE_LINE_BACKWARD
	// 在可编辑图层中回退半自动矢量化线操作。
	// static Action VECTORLIZELINE
	// 在可编辑图层中进行半自动矢量化线操作，用于栅格矢量化。
	// static Action VECTORLIZEREGION
	// 在可编辑图层中进行半自动矢量化面操作，生成对象保存到数据集，类似于魔术棒功能。
	// static Action VERTEXADD
	// 在可编辑图层中为对象添加节点。
	// static Action VERTEXEDIT
	// 在可编辑图层中编辑对象的节点。

}
