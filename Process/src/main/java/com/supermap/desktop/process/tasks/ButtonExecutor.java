package com.supermap.desktop.process.tasks;

import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by highsad on 2017/4/20.
 */
public class ButtonExecutor extends JButton implements MouseListener, MouseMotionListener {
	public final static int READY = 1;
	public final static int RUNNING = 2;
	public final static int CANCELLED = 3;
	public final static int COMPLETED = 4;

	private final static Icon ICON_READY_NORMAL = ControlsResources.getIcon("/controlsresources/ToolBar/Image_run_now.png");
	private final static Icon ICON_READY_HOT = ControlsResources.getIcon("/controlsresources/ToolBar/Image_Run.png");
	private final static Icon ICON_RUN_NORMAL = ControlsResources.getIcon("/controlsresources/ToolBar/Image_stop_now.png");
	private final static Icon ICON_RUN_HOT = ControlsResources.getIcon("/controlsresources/ToolBar/Image_Stop.png");
	private final static Icon ICON_CANCELLED = ControlsResources.getIcon("/controlsresources/ToolBar/Image_stop_pressed.png");
	private final static Icon ICON_COMPLETED_NOMAL = ControlsResources.getIcon("/controlsresources/ToolBar/Image_finish_now.png");
	private final static Icon ICON_COMPLETED_HOT = ControlsResources.getIcon("/controlsresources/ToolBar/Image_Run.png");

	private final static String TIP_READY = CommonProperties.getString(CommonProperties.Run);
	private final static String TIP_RUNNING = CommonProperties.getString(CommonProperties.Cancel);
	private final static String TIP_CANCELLED = CommonProperties.getString(CommonProperties.BeingCanceled);
	private final static String TIP_COMPLETED = CommonProperties.getString(CommonProperties.ReRun);

	public final static int NORMAL = 1;
	public final static int HOT = 2;

	private int procedure = READY;
	private int status = NORMAL;

	private Runnable run;
	private Runnable cancel;

	public ButtonExecutor(Runnable run, Runnable cancel) {
		this.run = run;
		this.cancel = cancel;
		setContentAreaFilled(false);
		addMouseListener(this);
		addMouseMotionListener(this);
		refresh();
	}

	public int getProcedure() {
		return this.procedure;
	}

	public void setProcedure(int procedure) {
		if (this.procedure != procedure) {
			this.procedure = procedure;
			refresh();
		}
	}

	private void setStatus(int status) {
		if (this.status != status) {
			this.status = status;
			refresh();
		}
	}

	private void refresh() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				switch (ButtonExecutor.this.procedure) {
					case READY: {
						if (ButtonExecutor.this.status == NORMAL) {
							setIcon(ICON_READY_NORMAL);
						} else if (ButtonExecutor.this.status == HOT) {
							setIcon(ICON_READY_HOT);
						}
						setToolTipText(TIP_READY);
					}
					break;
					case RUNNING: {
						if (ButtonExecutor.this.status == NORMAL) {
							setIcon(ICON_RUN_NORMAL);
						} else if (ButtonExecutor.this.status == HOT) {
							setIcon(ICON_RUN_HOT);
						}
						setToolTipText(TIP_RUNNING);
					}
					break;
					case CANCELLED: {
						setIcon(ICON_CANCELLED);
						setToolTipText(TIP_CANCELLED);
					}
					break;
					case COMPLETED: {
						if (ButtonExecutor.this.status == NORMAL) {
							setIcon(ICON_COMPLETED_NOMAL);
						} else if (ButtonExecutor.this.status == HOT) {
							setIcon(ICON_COMPLETED_HOT);
						}
						setToolTipText(TIP_COMPLETED);
					}
					break;
					default:
						break;
				}
			}
		});
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		setStatus(HOT);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		switch (this.procedure) {
			case READY:
			case COMPLETED: {
				setProcedure(RUNNING);
				this.run.run();
			}
			break;
			case RUNNING: {
				setProcedure(CANCELLED);
				this.cancel.run();
			}
			break;
			case CANCELLED:
			default:
				break;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		setStatus(HOT);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		setStatus(NORMAL);
	}

}
