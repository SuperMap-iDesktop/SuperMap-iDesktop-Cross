package com.supermap.desktop.geometryoperation.editor;

import com.supermap.data.GeometryType;
import com.supermap.desktop.geometryoperation.EditControllerAdapter;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.IEditController;
import com.supermap.desktop.geometryoperation.NullEditController;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.ui.Action;
import com.supermap.ui.ActionChangedEvent;

public class AddNodeEditor extends AbstractEditor {

	private IEditController addNodeEditController = new EditControllerAdapter() {

		@Override
		public void actionChanged(EditEnvironment environment, ActionChangedEvent e) {
			if (e.getOldAction() == Action.VERTEXADD) {

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
		environment.getMapControl().setAction(Action.VERTEXADD);

		if (environment.getMapControl().getAction() != Action.VERTEXADD) {

			// 因为这个功能是组件控制的，有一些导致 Action 设置失败的原因我们的封装无法知道，因此在这里处理一下漏网之鱼
			environment.stopEditor();
		} else {
			environment.setEditController(this.addNodeEditController);
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
		return environment.getMapControl().getAction() == Action.VERTEXADD;
	}
}
