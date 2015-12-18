package com.supermap.desktop.mapview.layer.propertycontrols;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.Border;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.event.ActiveFormChangedEvent;
import com.supermap.desktop.event.ActiveFormChangedListener;
import com.supermap.desktop.event.ActiveLayersChangedEvent;
import com.supermap.desktop.event.ActiveLayersChangedListener;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerCaptionChangedEvent;
import com.supermap.mapping.LayerCaptionChangedListener;
import com.supermap.mapping.LayerEditableChangedEvent;
import com.supermap.mapping.LayerEditableChangedListener;
import com.supermap.mapping.LayerEvent;
import com.supermap.mapping.LayerSelectableChangedEvent;
import com.supermap.mapping.LayerSelectableChangedListener;
import com.supermap.mapping.LayerSnapableChangedEvent;
import com.supermap.mapping.LayerSnapableChangedListener;
import com.supermap.mapping.LayerVisibleChangedEvent;
import com.supermap.mapping.LayerVisibleChangedListener;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapClosedEvent;
import com.supermap.mapping.MapClosedListener;

public class LayerPropertyContainer extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private JPanel panelContainer;
	private JCheckBox checkBoxIsAutoApply;
	private JButton buttonApply;
	private transient IFormMap formMap;

	// @formatter:off
	// 在面板之外修改图层属性，需要联动更新图层属性的显示。
	// 然而点击应用、与面板上的控件交互都会导致图层属性的修改，
	// 然后这种时候不需要重置面板状态，设置个标记
	// 静态变量是权宜之计，因为目前的结构无法知道是否是UI交互导致的更改
	// 因此，先用一个静态字段来让子面板们在更改的时候设置状态
	// @formatter:on
	private static boolean NEED_RESET = true;

	public static void setResetFlag(boolean needReset) {
		NEED_RESET = needReset;
	}

	private ArrayList<AbstractLayerPropertyControl> propertyControls = new ArrayList<AbstractLayerPropertyControl>();

	private transient LayerPropertyControlUtilties propertyControlUtilties = new LayerPropertyControlUtilties();

	private transient ActiveLayersChangedListener activeLayersChangedListener = new ActiveLayersChangedListener() {

		@Override
		public void acitiveLayersChanged(ActiveLayersChangedEvent e) {
			activeLayersChanged(e);
		}
	};

	private transient ChangedListener layerPropertyChangedListener = new ChangedListener() {

		@Override
		public void changed(ChangedEvent e) {
			buttonApply.setEnabled(!checkBoxIsAutoApply.isSelected() && e.getCurrentState() == ChangedEvent.CHANGED);
		}
	};

	private transient LayerCaptionChangedListener layerCaptionChangedListener = new LayerCaptionChangedListener() {

		@Override
		public void captionChanged(LayerCaptionChangedEvent arg0) {
			layerPropertyChange();
		}
	};

	private transient LayerEditableChangedListener layerEditableChangedListener = new LayerEditableChangedListener() {

		@Override
		public void editableChanged(LayerEditableChangedEvent arg0) {
			layerPropertyChange();
		}
	};

	private transient LayerVisibleChangedListener layerVisibleChangedListener = new LayerVisibleChangedListener() {

		@Override
		public void visibleChanged(LayerVisibleChangedEvent arg0) {
			if (!arg0.getLayer().isVisible() && null != arg0.getLayer().getSelection()) {
				arg0.getLayer().getSelection().clear();
			}
			layerPropertyChange();
		}
	};

	private transient LayerSelectableChangedListener layerSelectableChangedListener = new LayerSelectableChangedListener() {

		@Override
		public void selectableChanged(LayerSelectableChangedEvent arg0) {
			if (!arg0.getLayer().isSelectable()) {
				arg0.getLayer().getSelection().clear();
			}
			layerPropertyChange();
		}
	};

	private transient LayerSnapableChangedListener layerSnapableChangedListener = new LayerSnapableChangedListener() {

		@Override
		public void snapableChanged(LayerSnapableChangedEvent arg0) {
			layerPropertyChange();
		}
	};

	/**
	 * Create the panel.
	 */
	public LayerPropertyContainer() {
		initializeComponents();
		initializeResources();
		this.checkBoxIsAutoApply.setSelected(true);
		this.checkBoxIsAutoApply.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				checkBoxIsAutoApplyCheckedChanged();
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

	public IFormMap getFormMap() {
		return this.formMap;
	}

	public void setFormMap(IFormMap formMap) {
		if (this.formMap != null && this.formMap.getMapControl() != null) {
			unregisterEvents();
		}
		this.formMap = formMap;
		if (this.formMap != null) {
			setActiveLayers(this.formMap.getActiveLayers(), this.formMap);
			registerEvents();
		} else {
			clearPropertyControls();
			panelContainer.updateUI();
		}
	}

	private void initializeComponents() {
		this.panelContainer = new JPanel();
		this.panelContainer.setLayout(new BoxLayout(this.panelContainer, BoxLayout.Y_AXIS));
		JScrollPane scrollPaneContainer = new JScrollPane(this.panelContainer);
//		scrollPaneContainer.setVerticalScrollBar(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		scrollPaneContainer.setBorder(null);

		this.checkBoxIsAutoApply = new JCheckBox("IsAutoApply");
		this.buttonApply = new JButton("Apply");

		GroupLayout gl_mainContent = new GroupLayout(this);
		gl_mainContent.setAutoCreateContainerGaps(true);
		gl_mainContent.setAutoCreateGaps(true);
		this.setLayout(gl_mainContent);

		// @formatter:off
		gl_mainContent.setHorizontalGroup(gl_mainContent.createParallelGroup(Alignment.CENTER)
				.addComponent(scrollPaneContainer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(gl_mainContent.createSequentialGroup()
						.addComponent(this.checkBoxIsAutoApply)
						.addGap(GroupLayout.PREFERRED_SIZE, 15, Short.MAX_VALUE)
						.addComponent(this.buttonApply, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));
		
		gl_mainContent.setVerticalGroup(gl_mainContent.createSequentialGroup()
				.addComponent(scrollPaneContainer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(gl_mainContent.createParallelGroup(Alignment.CENTER)
						.addComponent(this.checkBoxIsAutoApply)
						.addComponent(this.buttonApply)));
		// @formatter:on
	}

	private void initializeResources() {
		this.checkBoxIsAutoApply.setText(ControlsProperties.getString("String_AutoApply"));
		this.buttonApply.setText(CommonProperties.getString("String_Button_Apply"));
	}

	private void registerEvents() {
		this.formMap.addActiveLayersChangedListener(this.activeLayersChangedListener);
		this.formMap.getMapControl().getMap().getLayers().addLayerCaptionChangedListener(this.layerCaptionChangedListener);
		this.formMap.getMapControl().getMap().getLayers().addLayerEditableChangedListener(this.layerEditableChangedListener);
		this.formMap.getMapControl().getMap().getLayers().addLayerVisibleChangedListener(this.layerVisibleChangedListener);
		this.formMap.getMapControl().getMap().getLayers().addLayerSelectableChangedListener(this.layerSelectableChangedListener);
		this.formMap.getMapControl().getMap().getLayers().addLayerSnapableChangedListener(this.layerSnapableChangedListener);
		this.formMap.getMapControl().getMap().addMapClosedListener(new MapClosedListener() {

			@Override
			public void mapClosed(MapClosedEvent arg0) {
				// TODO 没清空图层属性面板中的元素，其中监听器未移除可能有隐患
				unregisterEvents();
				if (null != formMap) {
					formMap.getMapControl().getMap().removeMapClosedListener(this);
				}
			}
		});
	}

	private void unregisterEvents() {
		if (null != this.formMap) {
			this.formMap.removeActiveLayersChangedListener(this.activeLayersChangedListener);
			this.formMap.getMapControl().getMap().getLayers().removeLayerCaptionChangedListener(this.layerCaptionChangedListener);
			this.formMap.getMapControl().getMap().getLayers().removeLayerEditableChangedListener(this.layerEditableChangedListener);
			this.formMap.getMapControl().getMap().getLayers().removeLayerVisibleChangedListener(this.layerVisibleChangedListener);
			this.formMap.getMapControl().getMap().getLayers().removeLayerSelectableChangedListener(this.layerSelectableChangedListener);
			this.formMap.getMapControl().getMap().getLayers().removeLayerSnapableChangedListener(this.layerSnapableChangedListener);
		}
	}

	private void activeLayersChanged(ActiveLayersChangedEvent e) {
		setActiveLayers(e.getNewActiveLayers(), this.formMap);
	}

	private void checkBoxIsAutoApplyCheckedChanged() {
		try {
			setResetFlag(false);
			if (this.propertyControls != null && !this.propertyControls.isEmpty()) {
				for (AbstractLayerPropertyControl abstractLayerPropertyControl : propertyControls) {
					abstractLayerPropertyControl.setAutoApply(this.checkBoxIsAutoApply.isSelected());
				}
				if (this.checkBoxIsAutoApply.isSelected()) {
					this.buttonApply.setEnabled(false);
				}
			} else {
				this.buttonApply.setEnabled(false);
			}
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		} finally {
			setResetFlag(true);
			UICommonToolkit.getLayersManager().getLayersTree().updateUI();
		}
	}

	private void buttonApplyClicked() {
		try {

			setResetFlag(false);
			for (AbstractLayerPropertyControl abstractLayerPropertyControl : this.propertyControls) {
				abstractLayerPropertyControl.apply();
			}
			this.buttonApply.setEnabled(false);
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		} finally {
			setResetFlag(true);
			UICommonToolkit.getLayersManager().getLayersTree().updateUI();
		}
	}

	private void setActiveLayers(Layer[] layers, IFormMap formMap) {
		clearPropertyControls();

		if (layers != null && layers.length > 0) {
			final AbstractLayerPropertyControl[] propertyControlsArray = this.propertyControlUtilties.getLayerPropertyControls(layers, formMap);

			for (AbstractLayerPropertyControl abstractLayerPropertyControl : propertyControlsArray) {
				addPropertyControl(abstractLayerPropertyControl);
			}
		}
		panelContainer.updateUI();
	}

	private void clearPropertyControls() {
		if (this.propertyControls != null && !this.propertyControls.isEmpty()) {
			for (AbstractLayerPropertyControl abstractLayerPropertyControl : this.propertyControls) {
				abstractLayerPropertyControl.removeLayerPropertyChangedListener(layerPropertyChangedListener);
			}

			this.propertyControls.clear();
		}
		this.panelContainer.removeAll();
	}

	private void addPropertyControl(AbstractLayerPropertyControl propertyControl) {
		if (propertyControl != null && this.propertyControls != null && !this.propertyControls.contains(propertyControl)) {
			this.propertyControls.add(propertyControl);
			this.panelContainer.add(propertyControl);
			propertyControl.addLayerPropertyChangedListener(layerPropertyChangedListener);
		}
	}

	private void layerPropertyChange() {
		if (this.checkBoxIsAutoApply.isSelected() && NEED_RESET) {
			setActiveLayers(this.formMap.getActiveLayers(), this.formMap);
		}
	}
}
