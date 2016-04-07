package com.supermap.desktop.geometryoperation.CtrlAction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.supermap.desktop.Application;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.geometryoperation.EditAction;
import com.supermap.desktop.geometryoperation.EditActionChangeEvent;
import com.supermap.desktop.geometryoperation.EditActionChangeListener;
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

	protected EditAction getEditAction() {
		return EditAction.NONE;
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

	private EditActionChangeListener editActionChangeListener = new EditActionChangeListener() {

		@Override
		public void editActionChange(EditActionChangeEvent e) {
			editState_EditStateActionChanged(e);
		}
	};

	protected void startEdit() {
		IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();

		formMap.getMapControl().addActionChangedListener(this.actionChangedListener);
		formMap.getMapControl().addKeyListener(this.keyListener);
		formMap.getMapControl().addMouseListener(this.mouseListener);
		MapEditorEnv.getEditState().setEditAction(getEditAction());
		MapEditorEnv.getEditState().addEditActionChangeListener(this.editActionChangeListener);
	}

	protected void endEdit() {
		FormMap formMap = (FormMap) Application.getActiveApplication().getActiveForm();
		formMap.getMapControl().removeActionChangedListener(this.actionChangedListener);
		formMap.getMapControl().removeMouseListener(this.mouseListener);
		formMap.getMapControl().removeKeyListener(this.keyListener);
		MapEditorEnv.getEditState().removeEditActionChangeListener(this.editActionChangeListener);

		if (MapEditorEnv.getEditState().getEditAction() == getEditAction()) {
			MapEditorEnv.getEditState().setEditAction(EditAction.NONE);
		}
		formMap.getMapControl().setAction(Action.SELECT2);
		formMap.getMapControl().setTrackMode(TrackMode.EDIT);
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

	void editState_EditStateActionChanged(EditActionChangeEvent e) {
		endEdit();
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

	@Override
	public boolean check() {
		boolean checkState = false;
		try {
			if (MapEditorEnv.getEditState().getEditAction() == getEditAction()) {
				checkState = true;
			}
		} catch (Exception e) {
		}
		return checkState;
	}
}
