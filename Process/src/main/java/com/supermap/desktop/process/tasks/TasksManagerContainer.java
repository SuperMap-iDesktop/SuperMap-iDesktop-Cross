package com.supermap.desktop.process.tasks;

import com.sun.org.apache.bcel.internal.generic.RET;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormWorkflow;
import com.supermap.desktop.event.ActiveFormChangedEvent;
import com.supermap.desktop.event.ActiveFormChangedListener;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.Group;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class TasksManagerContainer extends JPanel implements ActiveFormChangedListener {
	private Map<IFormWorkflow, TasksManagerPanel> managerPanelsMap = new ConcurrentHashMap<>();

	public TasksManagerContainer() {
		Application.getActiveApplication().getMainFrame().getFormManager().addActiveFormChangedListener(this);
	}

	@Override
	public void activeFormChanged(ActiveFormChangedEvent e) {

	}
}

