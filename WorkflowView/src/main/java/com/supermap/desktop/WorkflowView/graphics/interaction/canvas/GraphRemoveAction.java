package com.supermap.desktop.WorkflowView.graphics.interaction.canvas;

import com.supermap.desktop.WorkflowView.graphics.GraphCanvas;
import com.supermap.desktop.WorkflowView.graphics.graphs.IGraph;

import java.awt.event.KeyEvent;

/**
 * Created by highsad on 2017/3/25.
 */
public class GraphRemoveAction extends CanvasActionAdapter {
	private GraphCanvas canvas;

	public GraphRemoveAction(GraphCanvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public void clean() {

	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_DELETE) {
			IGraph[] selectedItems = this.canvas.getSelection().getSelectedItems();

			if (selectedItems == null || selectedItems.length == 0) {
				return;
			}

			for (int i = 0; i < selectedItems.length; i++) {
				IGraph graph = selectedItems[i];
				this.canvas.getGraphStorage().remove(graph);
			}

//			for (int i = 0; i < selectedItems.length; i++) {
//				IGraph graph = selectedItems[i];
//				if (graph instanceof ConnectionLineGraph) {
//					IConnectable start = ((ConnectionLineGraph) graph).getConnection().getStart();
//					IConnectable end = ((ConnectionLineGraph) graph).getConnection().getEnd();
//
//					if (start.getConnector() instanceof OutputGraph && end.getConnector() instanceof ProcessGraph) {
//						if (this.canvas.getSelection().isSelected(graph)) {
//							this.canvas.getSelection().deselectItem(graph);
//						}
//						this.canvas.getGraphStorage().getConnectionManager().removeConnection(((ConnectionLineGraph) graph).getConnection());
//					} else {
//						continue;
//					}
//				} else if (graph instanceof ProcessGraph) {
//					IGraph[] nextGraphs = this.canvas.getGraphStorage().getConnectionManager().getNextGraphs(graph);
//
//					this.canvas.getGraphStorage().getConnectionManager().removeConnection(graph);
//					if (this.canvas.getSelection().isSelected(graph)) {
//						this.canvas.getSelection().deselectItem(graph);
//					}
//					this.canvas.getGraphStorage().remove(graph);
//
//					if (nextGraphs != null && nextGraphs.length > 0) {
//						for (int j = 0; j < nextGraphs.length; j++) {
//							IGraph nextGraph = nextGraphs[j];
//							if (nextGraph instanceof OutputGraph) {
//								this.canvas.getGraphStorage().getConnectionManager().removeConnection(nextGraph);
//								this.canvas.getGraphStorage().remove(nextGraph);
//							}
//						}
//					}
//				} else if (graph instanceof OutputGraph) {
//					continue;
//				}
//			}
			this.canvas.repaint();
		}
	}
}
