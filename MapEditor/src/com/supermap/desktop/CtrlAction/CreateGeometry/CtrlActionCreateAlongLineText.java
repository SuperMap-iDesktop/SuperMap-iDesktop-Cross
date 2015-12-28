package com.supermap.desktop.CtrlAction.CreateGeometry;

import javax.swing.JOptionPane;

import com.supermap.data.DatasetType;
import com.supermap.data.GeoCompound;
import com.supermap.data.GeoText;
import com.supermap.data.TextPart;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.ui.Action;
import com.supermap.ui.ActionChangedEvent;
import com.supermap.ui.ActionChangedListener;
import com.supermap.ui.TrackedEvent;
import com.supermap.ui.TrackedListener;

public class CtrlActionCreateAlongLineText extends ActionCreateBase {

	private static final double DEFAULT_FONT_PIXEL_HEIGHT = 23;

	private TrackedListener trackedListener = new TrackedListener() {

		@Override
		public void tracked(TrackedEvent arg0) {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();

			GeoCompound geoCompound = (GeoCompound) arg0.getGeometry();
			String text = JOptionPane.showInputDialog(MapEditorProperties.getString("String_AlongLineText"));
			GeoText geoText = (GeoText) geoCompound.getPart(0);
			geoText.getTextStyle().setSizeFixed(false);
			// DEFAULT_FONT_PIXEL_HEIGHT 是一个经验值，使得不固定大小的时候，最后绘制到地图上的文本大小与输入的时候基本一致
			geoText.getTextStyle().setFontHeight(DEFAULT_FONT_PIXEL_HEIGHT * MapUtilties.PixelLength(formMap.getMapControl()));
			TextPart textPart = new TextPart();
			textPart.setText(text);
			geoText.addPart(textPart);
		}
	};
	private ActionChangedListener actionChangedListener = new ActionChangedListener() {

		@Override
		public void actionChanged(ActionChangedEvent arg0) {
			abstractActionChangedListener(arg0);
		}

	};

	private void abstractActionChangedListener(ActionChangedEvent arg0) {
		IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();

		if (arg0.getOldAction() == Action.CREATE_ALONG_LINE_TEXT) {

			// 绘制过程中，按住中键会切换为漫游，此时不希望结束绘制
			if (arg0.getNewAction() != Action.PAN) {
				formMap.getMapControl().removeActionChangedListener(actionChangedListener);
				formMap.getMapControl().removeTrackedListener(trackedListener);
			}
		} else if (arg0.getOldAction() == Action.PAN && arg0.getNewAction() != Action.CREATE_ALONG_LINE_TEXT) {

			// 在漫游状态，改变为其他 Action，触发这个事件，表明在绘制中进行的漫游，如果切换为CREATE_ALONG_LINE_TEXT 之外的 Action，那么就结束绘制
			formMap.getMapControl().removeActionChangedListener(actionChangedListener);
			formMap.getMapControl().removeTrackedListener(trackedListener);
		} else if (arg0.getNewAction() == Action.CREATE_ALONG_LINE_TEXT) {
			formMap.getMapControl().addTrackedListener(trackedListener);
		}
	}

	public CtrlActionCreateAlongLineText(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Action getAction() {
		return Action.CREATE_ALONG_LINE_TEXT;
	}

	@Override
	public void run() {
		IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
		formMap.getMapControl().addActionChangedListener(this.actionChangedListener);
		formMap.getMapControl().setAction(Action.CREATE_ALONG_LINE_TEXT);
	}

	@Override
	public boolean isSupportDatasetType(DatasetType datasetType) {
		return DatasetType.TEXT == datasetType || DatasetType.CAD == datasetType;
	}
}
