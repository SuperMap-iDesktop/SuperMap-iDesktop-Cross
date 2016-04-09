package com.supermap.desktop.geometryoperation.CtrlAction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.geometryoperation.GeometryEditEnv;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.mapeditor.MapEditorEnv;
import com.supermap.ui.Action;
import com.supermap.ui.ActionChangedEvent;
import com.supermap.ui.ActionChangedListener;
import com.supermap.ui.TrackMode;

public class CtrlActionGeometryEditBase extends CtrlAction {

	public CtrlActionGeometryEditBase(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	protected boolean getEditEnable() {
		return false;
	}

	@Override
	public void run() {
		try {
			if (!this.check()) {
				startEdit();
			} else {
				endEdit();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private ActionChangedListener actionChangedListener = new ActionChangedListener() {

		@Override
		public void actionChanged(ActionChangedEvent arg0) {
			mapControl_ActionChanged(arg0);
		}
	};

	private KeyListener keyListener = new KeyListener() {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {
			mapControl_KeyUp(e);
		}
	};

	private MouseAdapter mouseListener = new MouseAdapter() {

		@Override
		public void mousePressed(MouseEvent e) {
			mapControl_MouseClick(e);
		}
	};

	protected void startEdit() {
		GeometryEditEnv geometryEdit = MapEditorEnv.getGeometryEditManager().instance();

		geometryEdit.getMapControl().addActionChangedListener(this.actionChangedListener);
		geometryEdit.getMapControl().addKeyListener(this.keyListener);
		geometryEdit.getMapControl().addMouseListener(this.mouseListener);
	}

	protected void endEdit() {
		GeometryEditEnv geometryEdit = MapEditorEnv.getGeometryEditManager().instance();

		geometryEdit.getMapControl().removeActionChangedListener(this.actionChangedListener);
		geometryEdit.getMapControl().removeMouseListener(this.mouseListener);
		geometryEdit.getMapControl().removeKeyListener(this.keyListener);

		geometryEdit.getMapControl().setAction(Action.SELECT2);
		geometryEdit.getMapControl().setTrackMode(TrackMode.EDIT);
	}

	protected void mapControl_ActionChanged(ActionChangedEvent e) {
		try {
			// if (System.Windows.Forms.Control.MouseButtons != System.Windows.Forms.MouseButtons.Middle &&
			// e.OldAction != SuperMap.UI.Action.Pan)
			// {
			// EndEdit();
			// (Application.ActiveForm as IFormMap).MapControl.Action = e.NewAction;
			// }
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}


	protected void mapControl_MouseClick(MouseEvent e) {
		// if (e.Button == System.Windows.Forms.MouseButtons.Right)
		// {
		// EndEdit();
		// }
	}

	protected void mapControl_KeyUp(KeyEvent e) {
		// if (e.KeyData == System.Windows.Forms.Keys.Escape)
		// {
		// EndEdit();
		// }
	}

	@Override
	public boolean enable() {
		boolean enable = getEditEnable();
		if (!enable && this.check()) {
			endEdit();
		}
		return enable;
	}
}
