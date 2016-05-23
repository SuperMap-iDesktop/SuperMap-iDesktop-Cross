package com.supermap.desktop.geometryoperation.editor;

import java.util.ArrayList;

import javax.swing.JLabel;

import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Recordset;
import com.supermap.desktop.geometryoperation.EditControllerAdapter;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.IEditController;
import com.supermap.desktop.geometryoperation.IEditModel;
import com.supermap.desktop.geometryoperation.NullEditController;
import com.supermap.desktop.geometryoperation.control.MapControlTip;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.utilties.MapControlUtilties;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.mapping.Layer;
import com.supermap.ui.Action;
import com.supermap.ui.GeometrySelectedEvent;
import com.supermap.ui.TrackMode;

public class SplitByGeometryEditor extends AbstractEditor {

	private static final Action MAP_CONTROL_ACTION = Action.SELECT;

	private IEditController splitByGeometryEditController = new EditControllerAdapter() {

		@Override
		public void geometrySelected(EditEnvironment environment, GeometrySelectedEvent arg0) {

		}
	};

	@Override
	public void activate(EditEnvironment environment) {
		SplitByGeometryEditModel editModel;
		if (environment.getEditModel() instanceof SplitByGeometryEditModel) {
			editModel = (SplitByGeometryEditModel) environment.getEditModel();
		} else {
			editModel = new SplitByGeometryEditModel();
			environment.setEditModel(editModel);
		}
		environment.setEditController(this.splitByGeometryEditController);

		editModel.oldMapControlAction = environment.getMapControl().getAction();
		editModel.oldTrackMode = environment.getMapControl().getTrackMode();
		environment.getMapControl().setAction(MAP_CONTROL_ACTION);
		environment.getMapControl().setTrackMode(TrackMode.TRACK);
		editModel.tip.bind(environment.getMapControl());
	}

	@Override
	public void deactivate(EditEnvironment environment) {
		if (environment.getEditModel() instanceof SplitByGeometryEditModel) {
			SplitByGeometryEditModel editModel = (SplitByGeometryEditModel) environment.getEditModel();

			try {
				environment.getMapControl().setAction(editModel.oldMapControlAction);
				environment.getMapControl().setTrackMode(editModel.oldTrackMode);
				clear(environment);
			} finally {
				editModel.tip.unbind();
				environment.setEditController(NullEditController.instance());
				environment.setEditModel(null);
			}
		}
	}

	private void splitByGeometry(EditEnvironment environment){
		// boolean result = false;
		// Recordset recordset = null;
		// SplitByGeometryEditModel editModel=(SplitByGeometryEditModel)environment.getEditModel();
		//
		// try
		// {
		// // 获取用于分割的对象
		// ArrayList<Layer> layers = MapUtilties.getLayers(environment.getMap());
		// for (Layer layer : layers)
		// {
		// if (layer.getSelection().getCount()== 1)
		// {
		// Recordset selectrecordset = layer.getSelection().toRecordset();
		// Geometry selecteometry = null;
		//
		// try
		// {
		// selecteometry = selectrecordset.getGeometry();// 单选
		// selectrecordset.close();
		// selectrecordset.dispose();
		//
		// if (editModel.splitGeometry == null && selecteometry != null)
		// {
		// if (selecteometry.getType() == GeometryType.GEOPOINT)
		// {
		// editModel.sp = selecteometry;
		// }
		// else if (_Toolkit.IsLineGeometry(selecteometry))
		// {
		// if (selecteometry.Type == GeometryType.GeoLine)
		// {
		// m_splitGeometry = selecteometry;
		// }
		// else // 把数学对象折线化转成简单对象
		// {
		// m_splitGeometry = ConvertToLine(selecteometry);
		// }
		// }
		// else if (_Toolkit.IsRegionGeometry(selecteometry))
		// {
		// if (selecteometry.Type == GeometryType.GeoRegion)
		// {
		// m_splitGeometry = selecteometry;
		// }
		// else // 把数学对象折线化转成简单对象
		// {
		// m_splitGeometry = ConvertToRegion(selecteometry);
		// }
		// }
		// else
		// {
		// m_splitGeometry = null;
		// }
		//
		// if (m_splitGeometry != null)
		// {
		// m_SelectedLayer = layer;
		// break;
		// }
		// }
		// }
		// catch (Exception ex)
		// {
		// Application.ActiveApplication.Output.Output(ex.StackTrace, InfoType.Exception);
		// }
		// finally
		// {
		// CommonToolkit.ReleaseRecordset(ref recordset);
		// if (m_splitGeometry != selecteometry)
		// {
		// CommonToolkit.ReleaseGeometry(ref selecteometry);
		// }
		// }
		// }
		// }
		//
		// if (m_splitGeometry != null)
		// {
		// try
		// {
		// formMap.MapControl.EditHistory.BatchBegin();
		// Dictionary<Geometry, Dictionary<String, Object>> resultGeometrys = new Dictionary<Geometry, Dictionary<String, Object>>();
		// Geometry geometry;//被分割的每一个对象
		// m_selectionCount = 0;
		// foreach (Layer layer in m_forEraseGeometryIDs.Keys)
		// {
		// resultGeometrys.Clear();
		// DatasetVector datasetVector = layer.Dataset as DatasetVector;
		// recordset = datasetVector.GetRecordset(false, CursorType.Dynamic);
		// GeoStyle geoStyle = null;
		// RecordsetDelete delete = new RecordsetDelete(recordset, formMap.MapControl.EditHistory);
		// delete.Begin();
		// foreach (Int32 id in m_forEraseGeometryIDs[layer])
		// {
		// // 如果不在一个图层上有可能ID一样的
		// if ((m_SelectedLayer != layer) || id != m_splitGeometry.ID)
		// {
		// Geometry dynamicGeometry = null;
		// // 判断下地图是经过投影转换的，
		// // 当前图层的数据集投影和地图不一致，则需要将基线对象投影到被分割数据集一致才能进行分割
		// if (formMap.MapControl.Map.IsDynamicProjection
		// && !layer.Dataset.PrjCoordSys.Equals(m_SelectedLayer.Dataset.PrjCoordSys))
		// {
		// if (m_splitGeometry.Type == GeometryType.GeoLine)
		// {
		// dynamicGeometry = new GeoLine((GeoLine)m_splitGeometry);
		// }
		// else if (m_splitGeometry.Type == GeometryType.GeoRegion)
		// {
		// dynamicGeometry = new GeoRegion((GeoRegion)m_splitGeometry);
		// }
		//
		// using (var param = new CoordSysTransParameter())
		// {
		// CoordSysTranslator.Convert(dynamicGeometry, m_SelectedLayer.Dataset.PrjCoordSys,
		// layer.Dataset.PrjCoordSys, param, CoordSysTransMethod.CoordinateFrame);
		// }
		// }
		//
		// recordset.SeekID(id);
		// geometry = recordset.GetGeometry();
		// geoStyle = null;
		// // CAD上面弧面，弧线等，需要被转换成面线对象才能被分割
		// if (layer.Dataset.Type == DatasetType.CAD)
		// {
		// Geometry oldGeo = geometry;
		// if (_Toolkit.IsLineGeometry(geometry))
		// {
		// if (geometry.Type != GeometryType.GeoLine)
		// {
		// geometry = ConvertToLine(geometry);
		// }
		// }
		// else if (_Toolkit.IsRegionGeometry(geometry))
		// {
		// if (geometry.Type != GeometryType.GeoRegion)
		// {
		// geometry = ConvertToRegion(geometry);
		// }
		// }
		// else
		// {
		// geometry = null;
		// geoStyle = null;
		// }
		// if (geometry != null)
		// {
		// geoStyle = oldGeo.Style.Clone();
		// if (geometry != oldGeo)
		// {
		// oldGeo.Dispose();//转换前的几何对象应该及时释放掉。
		// }
		// }
		// }
		//
		// if (geometry != null)
		// {
		// Dictionary<string, object> values = new Dictionary<string, object>();
		// FieldInfos fieldInfos = recordset.GetFieldInfos();
		// Object[] fieldValues = recordset.GetValues();
		// for (int i = 0; i < fieldValues.Length; i++)
		// {
		// if (!fieldInfos[i].IsSystemField)
		// {
		// values.Add(fieldInfos[i].Name, fieldValues[i]);
		// }
		// }
		//
		// if (geometry.Type == GeometryType.GeoRegion)// 面
		// {
		// if (dynamicGeometry != null)
		// {
		// result = SplitRegion(geometry as GeoRegion, dynamicGeometry, resultGeometrys, values, geoStyle);
		// }
		// else
		// {
		// result = SplitRegion(geometry as GeoRegion, m_splitGeometry, resultGeometrys, values, geoStyle);
		// }
		//
		// if (result)
		// {
		// delete.Delete(recordset.GetID());
		// }
		// }
		// else if (geometry.Type == GeometryType.GeoLine)// 线
		// {
		// if (recordset.Dataset.Tolerance.NodeSnap == 0)
		// {
		// recordset.Dataset.Tolerance.SetDefault();
		// }
		//
		// if (dynamicGeometry != null)
		// {
		// result = SplitLine(geometry as GeoLine, dynamicGeometry,
		// resultGeometrys, values, recordset.Dataset.Tolerance.NodeSnap, geoStyle);
		// }
		// else
		// {
		// result = SplitLine(geometry as GeoLine, m_splitGeometry,
		// resultGeometrys, values, recordset.Dataset.Tolerance.NodeSnap, geoStyle);
		// }
		//
		// if (result)
		// {
		// delete.Delete(recordset.GetID());
		// }
		// }
		// }
		// // 将临时投影的基线释放
		// CommonToolkit.ReleaseGeometry(ref dynamicGeometry);
		// CommonToolkit.ReleaseGeometry(ref geometry);
		// }
		// }
		//
		// delete.Update();
		// CommonToolkit.ReleaseRecordset(ref recordset);
		//
		// // 向数据集追加结果对象
		// if (resultGeometrys.Count > 0)
		// {
		// recordset = null;
		// layer.Selection.Clear();
		// List<Int32> addHistoryIDs = new List<Int32>();
		// recordset = (layer.Dataset as DatasetVector).GetRecordset(true, CursorType.Dynamic);
		// RecordsetAddNew addNew = new RecordsetAddNew(recordset, (Application.ActiveForm as FormMap).MapControl.EditHistory);
		// addNew.Begin();
		// foreach (Geometry g in resultGeometrys.Keys)
		// {
		// //对于分割线对象与被分割线对象邻接的情况，分割会成功但是分割的结果有一个空对象
		// //追加要数据集失败，所有要判断一下结果对象是否能够追加
		// Boolean isCanApend = true;
		// Geometry geometryClone = g.Clone();
		// if (geometryClone.Type == GeometryType.GeoLine)
		// {
		// using (GeoLine geoline = geometryClone as GeoLine)
		// {
		// if (geoline != null)
		// {
		// isCanApend = geoline.PartCount > 0;
		// }
		// }
		// }
		// if (isCanApend)
		// {
		// addNew.AddNew(g, resultGeometrys[g]);
		// }
		// }
		// addNew.Update();
		// addHistoryIDs = addNew.AddHistoryIDs;
		// SuperMap.Desktop.UI.CommonToolkit.RefreshTabularForm(recordset.Dataset);
		// if (addHistoryIDs.Count > 0)
		// {
		// layer.Selection.AddRange(addHistoryIDs.ToArray());
		// }
		// m_selectionCount += addHistoryIDs.Count;
		// }
		// }
		// formMap.MapControl.EditHistory.BatchEnd();
		// formMap.MapControl.Map.Refresh();
		// formMap.MapControl.Refresh();
		// CommonToolkit.ReleaseRecordset(ref recordset);
		// CommonToolkit.ReleaseGeometry(ref m_splitGeometry);
		// EndEdit();
		//
		// (Application.ActiveForm as Form).Cursor = Cursors.Default;
		// Application.ActiveApplication.UserInfoManage.FunctionSuccess(m_funID);
		// if (result)
		// {
		// Application.ActiveApplication.Output.Output(Properties.MapEditorResources.String_Successed_Message);
		// }
		// else
		// {
		// Application.ActiveApplication.Output.Output(Properties.MapEditorResources.String_Failed_Message);
		// }
		// }
		// catch (Exception ex)
		// {
		// SuperMap.Desktop.Application.ActiveApplication.Output.Output(ex.StackTrace, InfoType.Exception);
		// }
		// }
		// else
		// {
		// Application.ActiveApplication.Output.Output(Properties.MapEditorResources.String_Failed_Message);
		// Application.ActiveApplication.Output.Output(Properties.MapEditorResources.String_NotCorrectGeometry);
		// Application.ActiveApplication.UserInfoManage.FunctionFailed(m_funID);
		// }
		//
		//
		// }
		// catch (Exception ex)
		// {
		// Application.ActiveApplication.Output.Output(ex, InfoType.Exception, m_funID);
		// }
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		return false;
	}

	private void clear(EditEnvironment environment) {
		if (environment.getEditModel() instanceof SplitByGeometryEditModel) {
			((SplitByGeometryEditModel) environment.getEditModel()).clear();
		}
	}

	private class SplitByGeometryEditModel implements IEditModel {

		public Action oldMapControlAction = Action.SELECT2;
		public TrackMode oldTrackMode = TrackMode.EDIT;

		public Geometry splitGeometry = null;
		public MapControlTip tip = new MapControlTip();
		public JLabel labelTip = new JLabel(MapEditorProperties.getString("String_SplitByGeometry_SelectTarget"));

		public void clear() {

		}
	}
}
