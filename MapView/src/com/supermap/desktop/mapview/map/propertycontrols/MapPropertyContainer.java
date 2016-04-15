package com.supermap.desktop.mapview.map.propertycontrols;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.event.ActiveFormChangedEvent;
import com.supermap.desktop.event.ActiveFormChangedListener;
import com.supermap.desktop.mapview.layer.propertycontrols.ChangedEvent;
import com.supermap.desktop.mapview.layer.propertycontrols.ChangedListener;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapClosedEvent;
import com.supermap.mapping.MapClosedListener;
import com.supermap.mapping.MapOpenedEvent;
import com.supermap.mapping.MapOpenedListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class MapPropertyContainer extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JCheckBox checkBoxAutoApply;
	private SmButton buttonApply;
	private JTabbedPane tabbledPane;
	private ArrayList<AbstractPropertyControl> propertyControls;
	private transient IFormMap formMap = null;
	private transient ChangedListener propertyChangedListener = new ChangedListener() {

		@Override
		public void changed(ChangedEvent e) {
			buttonApply.setEnabled(!checkBoxAutoApply.isSelected() && e.getCurrentState() == ChangedEvent.CHANGED);
		}
	};

	private transient MapOpenedListener mapOpenedListener = new MapOpenedListener() {

		/*
		 * 打开的地图窗口，重新打开指定地图的时候，同时更新地图属性面板的内容
		 * 
		 * @see
		 * com.supermap.mapping.MapOpenedListener#mapOpened(com.supermap.mapping
		 * .MapOpenedEvent)
		 */
		@Override
		public void mapOpened(MapOpenedEvent arg0) {
			setMap(arg0.getMap());
		}
	};

	/**
	 * Create the panel.
	 */
	public MapPropertyContainer() {
		initializeComponents();
		initializeResources();

		this.checkBoxAutoApply.setSelected(true);
		this.checkBoxAutoApply.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				checkBoxAutoApply();
			}
		});
		this.buttonApply.setEnabled(false);
		this.buttonApply.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonApplyClicked();
			}
		});
		Application.getActiveApplication().getMainFrame().getFormManager().addActiveFormChangedListener(new ActiveFormChangedListener() {

			@Override
			public void activeFormChanged(ActiveFormChangedEvent e) {
				if (e.getNewActiveForm() instanceof IFormMap) {
					setFormMap((IFormMap) e.getNewActiveForm());
				} else {
					setFormMap(null);
				}
			}
		});
	}

	public void setFormMap(IFormMap formMap) {
		if (propertyControls == null) {
			propertyControls = new ArrayList<>();
			propertyControls.add(new MapBasePropertyControl());
			propertyControls.add(new MapBoundsPropertyControl());
			propertyControls.add(new MapPrjCoordSysControl());
			for (AbstractPropertyControl abstractPropertyControl : propertyControls) {
				this.tabbledPane.addTab(abstractPropertyControl.getPropertyTitle(), new JScrollPane(abstractPropertyControl));
				abstractPropertyControl.addChangedListener(this.propertyChangedListener);
			}
		}

		if (this.formMap != null && this.formMap.getMapControl() != null && this.formMap.getMapControl().getMap() != null) {
			this.formMap.getMapControl().getMap().removeMapOpenedListener(this.mapOpenedListener);
		}
		this.formMap = formMap;

		if (formMap == null) {
			setMap(null);
		} else {
			setMap(this.formMap.getMapControl().getMap());
			this.formMap.getMapControl().getMap().addMapOpenedListener(this.mapOpenedListener);
			this.formMap.getMapControl().getMap().addMapClosedListener(new MapClosedListener() {
				@Override
				public void mapClosed(MapClosedEvent arg0) {
					if (null != MapPropertyContainer.this.formMap && null != MapPropertyContainer.this.formMap.getMapControl()) {
						MapPropertyContainer.this.formMap.getMapControl().getMap().removeMapOpenedListener(mapOpenedListener);
						MapPropertyContainer.this.formMap.getMapControl().getMap().removeMapClosedListener(this);
					}
				}
			});
		}
	}

	private void setMap(Map map) {
		if (map == null) {
			this.tabbledPane.setVisible(false);
		} else {
			this.tabbledPane.setVisible(true);
			for (AbstractPropertyControl abstractPropertyControl : propertyControls) {
				abstractPropertyControl.setMap(formMap.getMapControl().getMap());
			}
		}
		this.updateUI();
	}

	private void initializeComponents() {
		this.checkBoxAutoApply = new JCheckBox("AutoApply");
		this.buttonApply = new SmButton("Apply");
		this.tabbledPane = new JTabbedPane();
		JPanel panelTabContainer = new JPanel();
		panelTabContainer.setLayout(new BorderLayout());
		panelTabContainer.add(this.tabbledPane, BorderLayout.CENTER);


		this.setLayout(new GridBagLayout());
		this.add(panelTabContainer, new GridBagConstraintsHelper(0, 0, 2, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(10, 10, 5, 10));
		this.add(this.checkBoxAutoApply, new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.NONE).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0));
		this.add(this.buttonApply, new GridBagConstraintsHelper(1, 1, 1, 1).setFill(GridBagConstraints.NONE).setWeight(0, 0).setAnchor(GridBagConstraints.EAST).setInsets(0, 10, 5, 10));

//		GroupLayout groupLayout = new GroupLayout(this);
//		groupLayout.setAutoCreateContainerGaps(true);
//		groupLayout.setAutoCreateGaps(true);
//		this.setLayout(groupLayout);
//
//		// @formatter:off
//		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
//				.addComponent(panelTabContainer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//				.addGroup(groupLayout.createSequentialGroup()
//						.addComponent(this.checkBoxAutoApply)
//						.addGap(10, 20, Short.MAX_VALUE)
//						.addComponent(this.buttonApply, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));
//
//		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
//				.addComponent(panelTabContainer, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
//						.addComponent(this.checkBoxAutoApply)
//						.addComponent(this.buttonApply)));
		// @formatter:on
	}

	private void initializeResources() {
		this.checkBoxAutoApply.setText(ControlsProperties.getString("String_AutoApply"));
		this.buttonApply.setText(CommonProperties.getString("String_Button_Apply"));
	}

	private void checkBoxAutoApply() {
		if (propertyControls != null && !propertyControls.isEmpty()) {
			for (AbstractPropertyControl abstractPropertyControl : propertyControls) {
				abstractPropertyControl.setAutoApply(this.checkBoxAutoApply.isSelected());
			}
			if (this.checkBoxAutoApply.isSelected()) {
				this.buttonApply.setEnabled(false);
			}
		} else {
			this.buttonApply.setEnabled(false);
		}
	}

	private void buttonApplyClicked() {
		if (propertyControls != null && !propertyControls.isEmpty()) {
			for (AbstractPropertyControl abstractPropertyControl : propertyControls) {
				abstractPropertyControl.apply();
			}
		}
		this.buttonApply.setEnabled(false);
	}
}
