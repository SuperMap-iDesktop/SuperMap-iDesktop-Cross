package com.supermap.desktop.WorkflowView;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.event.ActiveFormChangedEvent;
import com.supermap.desktop.event.ActiveFormChangedListener;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.WorkflowView.graphics.graphs.IGraph;
import com.supermap.desktop.WorkflowView.graphics.graphs.ProcessGraph;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * @author XiaJT
 */
public class ParameterManager extends JPanel {
	private JPanel mainPanel = new JPanel();
	private TitledBorder titledBorder = new TitledBorder("");

	public ParameterManager() {
		this.setLayout(new GridBagLayout());
		this.mainPanel.setLayout(new GridBagLayout());
		this.titledBorder.setTitleJustification(TitledBorder.CENTER);
		this.mainPanel.setBorder(titledBorder);

		this.add(this.mainPanel, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
		Application.getActiveApplication().getMainFrame().getFormManager().addActiveFormChangedListener(new ActiveFormChangedListener() {
			@Override
			public void activeFormChanged(ActiveFormChangedEvent e) {
				IForm newActiveForm = e.getNewActiveForm();
				if (newActiveForm instanceof FormWorkflow) {
					IGraph[] selectedItems = ((FormWorkflow) newActiveForm).getCanvas().getSelection().getSelectedItems();
					for (IGraph selectedItem : selectedItems) {
						if (selectedItem instanceof ProcessGraph) {
							setProcess(((ProcessGraph) selectedItem).getProcess());
							return;
						}
					}
				}
				setProcess(null);
			}
		});
	}

	public void setProcess(IProcess process) {
		this.mainPanel.removeAll();

		if (process != null && process.getComponent().getPanel() instanceof Component) {
			this.titledBorder.setTitle(process.getTitle());
			JComponent component = (JComponent) process.getComponent().getPanel();
			JScrollPane jScrollPane = new JScrollPane(component);
			jScrollPane.setBorder(BorderFactory.createEmptyBorder());
			mainPanel.add(jScrollPane, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
		} else {
			this.titledBorder.setTitle("");
		}
		mainPanel.revalidate();
		mainPanel.repaint();
		this.revalidate();
		this.repaint();
	}
}
