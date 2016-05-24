package com.supermap.desktop.geometryoperation.editor;

import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.IEditModel;
import com.supermap.desktop.geometryoperation.control.MapControlTip;
import com.supermap.ui.Action;
import com.supermap.ui.TrackMode;

public class MirrorEditor extends AbstractEditor {

	private static final Action MAP_CONTROL_ACTION = Action.CREATELINE;
	private static final TrackMode MAP_CONTROL_TRACKMODE = TrackMode.TRACK;

	@Override
	public void activate(EditEnvironment environment) {
		// 按需重写
	}

	@Override
	public void deactivate(EditEnvironment environment) {
		// 按需重写
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		return false;
	}

	@Override
	public boolean check(EditEnvironment environment) {
		return environment.getEditor() instanceof MirrorEditor;
	}

	private class MirrorEditModel implements IEditModel {

		public Action oldMapControlAction = Action.SELECT2;
		public TrackMode oldTrackMode = TrackMode.EDIT;
		public MapControlTip tip = new MapControlTip();
	}
}
