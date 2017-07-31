package com.supermap.desktop.WorkflowView.tasks;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormWorkflow;
import com.supermap.desktop.WorkflowView.FormWorkflow;
import com.supermap.desktop.event.ActiveFormChangedEvent;
import com.supermap.desktop.event.ActiveFormChangedListener;
import com.supermap.desktop.event.FormClosedEvent;
import com.supermap.desktop.event.FormClosedListener;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TasksManagerContainer extends JPanel implements ActiveFormChangedListener, FormClosedListener {
	private Map<IFormWorkflow, TasksManagerPanel> managerPanelsMap = new ConcurrentHashMap<>();
	private TasksManagerPanel currentPanel;
	private JScrollPane scrollPane = new JScrollPane();

	public TasksManagerContainer() {
		initializeComponents();
		Application.getActiveApplication().getMainFrame().getFormManager().addActiveFormChangedListener(this);
		Application.getActiveApplication().getMainFrame().getFormManager().addFormClosedListener(this);
	}

	private void initializeComponents() {
		setLayout(new BorderLayout());
		add(this.scrollPane, BorderLayout.CENTER);
	}

	@Override
	public void activeFormChanged(ActiveFormChangedEvent e) {
		if (e.getNewActiveForm() instanceof FormWorkflow) {
			FormWorkflow formWorkflow = (FormWorkflow) e.getNewActiveForm();
			if (formWorkflow == null) {
				throw new NullPointerException("Actived Form can not be null.");
			}

			if (!this.managerPanelsMap.containsKey(formWorkflow)) {
				this.managerPanelsMap.put(formWorkflow, new TasksManagerPanel(formWorkflow.getTasksManager()));
			}

			TasksManagerPanel panel = this.managerPanelsMap.get(formWorkflow);
			if (panel == null) {
				throw new NullPointerException();
			}

			if (this.currentPanel != null) {
				remove(this.currentPanel);
			}

			this.currentPanel = panel;
			scrollPane.setViewportView(panel);
//			add(scrollPane, BorderLayout.CENTER);
		} else {
			if (this.currentPanel != null) {
//				remove(this.currentPanel);
//				remove(this.scrollPane);
				this.scrollPane.setViewportView(null);
			}

			this.currentPanel = null;
		}
		validate();
		repaint();
	}

	@Override
	public void formClosed(FormClosedEvent e) {
		if (e.getForm() instanceof FormWorkflow) {
			if (this.managerPanelsMap.containsKey(e.getForm())) {
				this.managerPanelsMap.get(e.getForm()).clear();
				this.managerPanelsMap.remove(e.getForm());
			}
		}
	}
}

