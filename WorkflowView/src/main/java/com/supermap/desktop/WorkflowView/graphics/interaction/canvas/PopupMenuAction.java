package com.supermap.desktop.WorkflowView.graphics.interaction.canvas;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IContextMenuManager;
import com.supermap.desktop.WorkflowView.graphics.GraphCanvas;
import com.supermap.desktop.WorkflowView.graphics.connection.LineGraph;
import com.supermap.desktop.WorkflowView.graphics.graphs.IGraph;
import com.supermap.desktop.WorkflowView.graphics.graphs.OutputGraph;
import com.supermap.desktop.WorkflowView.graphics.graphs.ProcessGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * @author XiaJT
 */
public class PopupMenuAction extends CanvasActionAdapter {
	private JPopupMenu workflowPopupMenu;
	private JPopupMenu processPopupMenu;
	private JPopupMenu outputPopupMenu;
	private JPopupMenu linePopupMenu;
	private JPopupMenu graphsPopupMenu;
	private GraphCanvas canvas;

	public PopupMenuAction(GraphCanvas graphCanvas) {
		this.canvas = graphCanvas;
		IContextMenuManager manager = Application.getActiveApplication().getMainFrame().getContextMenuManager();
		this.workflowPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.FormWorkflow.FormContextMenu");
		this.processPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.FormWorkflow.ProcessContextMenu");
		this.outputPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.FormWorkflow.OutputContextMenu");
		this.linePopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.FormWorkflow.LineContextMenu");
		this.graphsPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.FormWorkflow.GraphsContextMenu");

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1) {
			JPopupMenu menu = null;

			if (this.canvas.getSelection().size() > 0) {
				if (this.canvas.getSelection().size() == 1) {
					IGraph graph = this.canvas.getSelection().getItem(0);

					if (graph instanceof ProcessGraph) {
						menu = this.processPopupMenu;
					} else if (graph instanceof OutputGraph) {
						menu = this.outputPopupMenu;
					} else if (graph instanceof LineGraph) {
						menu = this.linePopupMenu;
					}
				} else if (this.canvas.getSelection().size() > 1) {
					menu = this.graphsPopupMenu;
				} else {
					throw new IndexOutOfBoundsException();
				}
			} else {
				menu = this.workflowPopupMenu;
			}

			if (menu != null) {
				menu.show((Component) e.getSource(), e.getX(), e.getY());
			}
		}
	}

	@Override
	public void clean() {

	}
}
