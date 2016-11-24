package com.supermap.desktop.geometryoperation.editor;

/**
 * @author lixiaoyao
 */

import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Geometrist;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometryoperation.EditControllerAdapter;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.IEditController;
import com.supermap.desktop.geometryoperation.NullEditController;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.mapping.Layer;
import com.supermap.ui.Action;
import com.supermap.ui.ActionChangedEvent;

import java.util.ArrayList;
import java.util.List;

public class ConcertEditor extends AbstractEditor {
	ArrayList hasCommenPointGeometry=new ArrayList();
	private IEditController concertEditController = new EditControllerAdapter() {
		@Override
		public void actionChanged(EditEnvironment environment, ActionChangedEvent e) {
			if (e.getOldAction() == Action.VERTEXEDIT) {

				// @formatter:off
				// 组件在很多情况下会自动结束编辑状态，比如右键，比如框选一堆对象，
				// 比如当前操作对象所在图层变为不可编辑状态，这时候桌面自定义的 Editor 还没有结束编辑，处理一下
				// @formatter:on
				environment.stopEditor();
			}
		}
	};

	@Override
	public void activate(EditEnvironment environment) {
		List<Layer> layers = environment.getEditProperties().getSelectedLayers();
		boolean result=false;
		for (Layer layer : layers) {
			Recordset recordset = layer.getSelection().toRecordset();
			Recordset newRecordset = ((DatasetVector) layer.getDataset()).getRecordset(false, CursorType.DYNAMIC);
			result= isCanConcertEdit(recordset,newRecordset);
		}
		if (result)
		{
			Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_GeometryOperation_SucessResultTip"));
		}
		environment.getMapControl().setAction(Action.VERTEXEDIT);

		if (environment.getMapControl().getAction() != Action.VERTEXEDIT) {

			// 因为这个功能是组件控制的，有一些导致 Action 设置失败的原因我们的封装无法知道，因此在这里处理一下漏网之鱼
			environment.stopEditor();
		} else {
			environment.setEditController(this.concertEditController);
			environment.getMap().refresh();
		}
	}

	@Override
	public void deactivate(EditEnvironment environment) {
		environment.getMapControl().setAction(Action.SELECT2);
		environment.setEditController(NullEditController.instance());
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		return environment.getEditProperties().getSelectedGeometryCount() == 1
				&& environment.getEditProperties().getEditableSelectedGeometryCount() == 1
				&& ListUtilities.isListOnlyContain(environment.getEditProperties().getSelectedGeometryTypes(), GeometryType.GEOLINE, GeometryType.GEOLINE3D,
				GeometryType.GEOREGION);
	}

	@Override
	public boolean check(EditEnvironment environment) {
		return environment.getMapControl().getAction() == Action.VERTEXEDIT;
	}

	private void runConcertEdit(EditEnvironment environment)
	{

	}
	/**
	 * @author lixiaoyao
	 * @description 判断当前选中的对象有没有共同节点的对象，如果有就保存到数组里，
	 */
	private boolean isCanConcertEdit(Recordset selectRecordset,Recordset sourceRecordset)
	{
		boolean result = false;
		Geometry selectGeometry=selectRecordset.getGeometry();
		int selectID=selectGeometry.getID();
		hasCommenPointGeometry.clear();
		sourceRecordset.moveFirst();
		try {
			for (int i = 0; i < sourceRecordset.getRecordCount(); ++i) {
				Geometry tempGeometry = sourceRecordset.getGeometry();
				int sourceID = tempGeometry.getID();
				if (sourceID == selectID) {
					sourceRecordset.moveNext();
					continue;
				}
				boolean resultHasCommenPoint = Geometrist.hasCommonPoint(selectGeometry, tempGeometry);
				if (result == false && resultHasCommenPoint == true) {
					result = true;
				}
				if (resultHasCommenPoint) {
					hasCommenPointGeometry.add(tempGeometry);
				}
				tempGeometry.dispose();
				sourceRecordset.moveNext();
			}
		}
		catch (Exception ex)
		{
			Application.getActiveApplication().getOutput().output(ex);
		}
		finally {
			selectGeometry.dispose();
			if (selectRecordset != null) {
				selectRecordset.close();
				selectRecordset.dispose();
			}
			if (sourceRecordset != null) {
				sourceRecordset.close();
				sourceRecordset.dispose();
			}
		}
		return result;
	}
}
