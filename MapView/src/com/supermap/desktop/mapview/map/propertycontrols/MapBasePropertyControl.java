package com.supermap.desktop.mapview.map.propertycontrols;

import com.supermap.data.Enum;
import com.supermap.desktop.Application;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.SMFormattedTextField;
import com.supermap.desktop.ui.controls.ButtonColorSelector;
import com.supermap.desktop.ui.controls.CaretPositionListener;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilties.DoubleUtilties;
import com.supermap.desktop.utilties.MapColorModeUtilties;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapColorMode;
import com.supermap.mapping.MapOverlapDisplayedOptions;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

public class MapBasePropertyControl extends AbstractPropertyControl {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JLabel labelMapName;
	private JLabel labelAngle;
	private JLabel labelColorMode;
	private JLabel labelBackgroundColor;
	private JLabel labelMinVisibleTextSize;
	private JLabel labelMaxVisibleTextSize;
	private JLabel labelMaxVisibleVertex;
	private JLabel labelAngleTip;
	private JLabel labelEmpty;

	private JTextField textFieldMapName; // 地图名称
	private SMFormattedTextField textFieldAngle; // 地图旋转角度
	private JComboBox<String> comboBoxColorMode; // 颜色模式
	private ButtonColorSelector buttonBackgroundColor; // 背景颜色
	private SMFormattedTextField textFieldMinVisibleTextSize; // 文本最小尺寸
	private SMFormattedTextField textFieldMaxVisibleTextSize; // 文本最大尺寸
	private SMFormattedTextField textFieldMaxVisibleVertex; // 最大可见节点数
	private JCheckBox checkBoxIsOverlapDisplayed; // 显示压盖对象
	private SmButton buttonOverlapDisplayedOptions; // 压盖设置
	private JCheckBox checkBoxIsMarkerAngleFixed; // 固定符号角度
	private JCheckBox checkBoxIsLineAntialias; // 线型反走样
	private JCheckBox checkBoxIsTextAngleFixed; // 固定文本角度
	private JCheckBox checkBoxIsTextAntialias; // 文本反走样
	private JCheckBox checkBoxIsTextOrientationFixed; // 固定文本方向
	JPopupMenuOverlapsetting jPopupMenu; // 压盖设置弹出菜单

	private String name = "";
	private double angle = 0.0;
	private transient MapColorMode colorMode = MapColorMode.DEFAULT;
	private Color backgroundColor = Color.WHITE;
	private double minVisibleTextSize = 0.0;
	private double maxVisibleTextSize = 0.0;
	private int maxVisibleVertex = 0;
	private boolean isOverlapDisplayed = false;
	private boolean isMarkerAngleFixed = false;
	private boolean isLineAntialias = false;
	private boolean isTextAngleFixed = false;
	private boolean isTextAntialias = false;
	private boolean isTextOrientationFixed = false;
	private transient MapOverlapDisplayedOptions mapOverlapDisplayedOptions = null;

	private transient CaretPositionListener caretPositionListener;
	private transient PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getSource() == textFieldAngle) {
				textFieldAngleValueChange(evt);
			} else if (evt.getSource() == textFieldMinVisibleTextSize) {
				textFieldMinVisibleTextSizeValueChange(evt);
			} else if (evt.getSource() == textFieldMaxVisibleTextSize) {
				textFieldMaxVisibleTextSizeValueChange(evt);
			} else if (evt.getSource() == textFieldMaxVisibleVertex) {
				textFieldMaxVisibleVertexValueChange(evt);
			} else if (evt.getSource() == buttonBackgroundColor) {
				buttonBackgroundColorChange();
			}
		}
	};
	private transient ItemListener itemListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			absractItemStateChanged(e);
		}

	};

	private void absractItemStateChanged(ItemEvent e) {
		if (e.getSource() == checkBoxIsOverlapDisplayed) {
			checkBoxIsOverlapDisplayedCheckChange();
		} else if (e.getSource() == checkBoxIsMarkerAngleFixed) {
			checkBoxIsMarkerAngleFixedCheckChange();
		} else if (e.getSource() == checkBoxIsLineAntialias) {
			checkBoxIsLineAntialiasCheckChange();
		} else if (e.getSource() == checkBoxIsTextAngleFixed) {
			checkBoxIsTextAngleFixedCheckChange();
		} else if (e.getSource() == checkBoxIsTextAntialias) {
			checkBoxIsTextAntialiasCheckChange();
		} else if (e.getSource() == checkBoxIsTextOrientationFixed) {
			checkBoxIsTextOrientationFixedCheckChange();
		} else if (e.getSource() == comboBoxColorMode) {
			comboBoxColorModeSelectedchange(e);
		}
	}

	private transient MouseListener setMouseListener = new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			if (buttonOverlapDisplayedOptions.isEnabled()) {
				Rectangle Rectangle = e.getComponent().getBounds();
				jPopupMenu.setPreferredSize(new Dimension((int) Rectangle.getWidth(), 300));
				jPopupMenu.setMapOverlapDisplayedOptions(mapOverlapDisplayedOptions);
				jPopupMenu.show(e.getComponent(), 0, (int) (Rectangle.getHeight()));
			}
		}
	};

	private transient PropertyChangeListener changeListener = new PropertyChangeListener() {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(JPopupMenuOverlapsetting.ALLOW_POINT_OVER_LAP)
					|| evt.getPropertyName().equals(JPopupMenuOverlapsetting.ALLOW_POINT_WITH_TEXT_DISPLAY)
					|| evt.getPropertyName().equals(JPopupMenuOverlapsetting.ALLOW_TEXT_AND_POINT_OVER_LAP)
					|| evt.getPropertyName().equals(JPopupMenuOverlapsetting.ALLOW_TEXT_OVER_LAP)
					|| evt.getPropertyName().equals(JPopupMenuOverlapsetting.ALLOW_THEME_GRADUATED_SYMBOL_OVER_LAP)
					|| evt.getPropertyName().equals(JPopupMenuOverlapsetting.ALLOW_THEME_GRAPH_OVER_LAP)
					|| evt.getPropertyName().equals(JPopupMenuOverlapsetting.OVER_LAPPED_SPACE_SIZE)) {
				mapOverlapDisplayedOptions = jPopupMenu.getMapOverlapDisplayedOptions();
				verify();
			}
		}
	};

	// private JCheckBox checkBoxIsCompatibleFontHeight; // 兼容 Office 字号，Java 没有

	/**
	 * Create the panel.
	 */
	public MapBasePropertyControl() {
		super(MapViewProperties.getString("String_TabPage_BaseSetting"));
	}

	@Override
	protected void initializeComponents() {
		this.caretPositionListener = new CaretPositionListener();

		this.labelMapName = new JLabel("MapName:");
		this.labelAngle = new JLabel("MapAngle:");
		this.labelColorMode = new JLabel("ColorMode:");
		this.labelBackgroundColor = new JLabel("BackgroundColor:");
		this.labelMinVisibleTextSize = new JLabel("MinVisibleTextSize(mm):");
		this.labelMaxVisibleTextSize = new JLabel("MaxVisibleTextSize(mm):");
		this.labelMaxVisibleVertex = new JLabel("MaxVisibleVertex:");
		this.labelAngleTip = new JLabel();
		this.labelEmpty = new JLabel();

		this.textFieldMapName = new JTextField();
		this.textFieldMapName.setEditable(false);
		this.textFieldAngle = new SMFormattedTextField(NumberFormat.getNumberInstance());
		this.comboBoxColorMode = new JComboBox<String>();
		this.buttonBackgroundColor = new ButtonColorSelector();
		this.textFieldMinVisibleTextSize = new SMFormattedTextField(NumberFormat.getNumberInstance());
		this.textFieldMaxVisibleTextSize = new SMFormattedTextField(NumberFormat.getNumberInstance());
		this.textFieldMaxVisibleVertex = new SMFormattedTextField(NumberFormat.getIntegerInstance());
		this.buttonOverlapDisplayedOptions = new SmButton("OverlapDisplayedOptions");
		this.checkBoxIsOverlapDisplayed = new JCheckBox("IsOverlapDisplayed");
		this.checkBoxIsMarkerAngleFixed = new JCheckBox("IsMarkerAngleFixed");
		this.checkBoxIsLineAntialias = new JCheckBox("IsLineAntialias");
		this.checkBoxIsTextAngleFixed = new JCheckBox("IsTextAngleFixed");
		this.checkBoxIsTextAntialias = new JCheckBox("IsTextAntialias");
		this.checkBoxIsTextOrientationFixed = new JCheckBox("IsTextOrientationFixed");

		// 压盖面板设置
		jPopupMenu = new JPopupMenuOverlapsetting();

		JPanel panelDisplayedOptions = new JPanel();
		panelDisplayedOptions.setBorder(BorderFactory.createTitledBorder(MapViewProperties.getString("String_GroupBox_DisplaySetting")));
		GroupLayout gl_displayedOptions = new GroupLayout(panelDisplayedOptions);
		gl_displayedOptions.setAutoCreateContainerGaps(true);
		gl_displayedOptions.setAutoCreateGaps(true);
		panelDisplayedOptions.setLayout(gl_displayedOptions);

		// @formatter:off
		gl_displayedOptions.setHorizontalGroup(gl_displayedOptions.createSequentialGroup()
				.addGroup(gl_displayedOptions.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelMinVisibleTextSize)
						.addComponent(this.labelMaxVisibleTextSize)
						.addComponent(this.labelMaxVisibleVertex)
						.addComponent(this.checkBoxIsOverlapDisplayed)
						.addComponent(this.checkBoxIsMarkerAngleFixed)
						.addComponent(this.checkBoxIsTextAngleFixed)
						.addComponent(this.checkBoxIsTextOrientationFixed))
				.addGroup(gl_displayedOptions.createParallelGroup(Alignment.LEADING)
						.addComponent(this.textFieldMinVisibleTextSize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(this.textFieldMaxVisibleTextSize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(this.textFieldMaxVisibleVertex, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(this.buttonOverlapDisplayedOptions, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(this.checkBoxIsLineAntialias)
						.addComponent(this.checkBoxIsTextAntialias)));
		
		gl_displayedOptions.setVerticalGroup(gl_displayedOptions.createSequentialGroup()
				.addGroup(gl_displayedOptions.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelMinVisibleTextSize)
						.addComponent(this.textFieldMinVisibleTextSize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_displayedOptions.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelMaxVisibleTextSize)
						.addComponent(this.textFieldMaxVisibleTextSize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_displayedOptions.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelMaxVisibleVertex)
						.addComponent(this.textFieldMaxVisibleVertex, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_displayedOptions.createParallelGroup(Alignment.CENTER)
						.addComponent(this.checkBoxIsOverlapDisplayed)
						.addComponent(this.buttonOverlapDisplayedOptions, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_displayedOptions.createParallelGroup(Alignment.CENTER)
						.addComponent(this.checkBoxIsMarkerAngleFixed)
						.addComponent(this.checkBoxIsLineAntialias))
				.addGroup(gl_displayedOptions.createParallelGroup(Alignment.CENTER)
						.addComponent(this.checkBoxIsTextAngleFixed)
						.addComponent(this.checkBoxIsTextAntialias))
				.addComponent(this.checkBoxIsTextOrientationFixed));
		// @formatter:on

		GroupLayout gl_mainContent = new GroupLayout(this);
		gl_mainContent.setAutoCreateContainerGaps(true);
		gl_mainContent.setAutoCreateGaps(true);
		this.setLayout(gl_mainContent);

		// @formatter:off
		gl_mainContent.setHorizontalGroup(gl_mainContent.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainContent.createSequentialGroup()
						.addGroup(gl_mainContent.createParallelGroup(Alignment.LEADING)
								.addComponent(this.labelMapName)
								.addComponent(this.labelAngle)
								.addComponent(this.labelColorMode)
								.addComponent(this.labelBackgroundColor))
						.addGroup(gl_mainContent.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_mainContent.createSequentialGroup()
										.addComponent(this.labelEmpty,10,10,10)
										.addComponent(this.textFieldMapName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								)
								.addGroup(gl_mainContent.createSequentialGroup()
										.addComponent(this.labelAngleTip,10,10,10)
										.addComponent(this.textFieldAngle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addGroup(gl_mainContent.createSequentialGroup()
										.addComponent(this.labelEmpty,15,15,15)
										.addComponent(this.comboBoxColorMode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								)
								.addGroup(gl_mainContent.createSequentialGroup()
										.addComponent(this.labelEmpty,15,15,15)
										.addComponent(this.buttonBackgroundColor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								)))
				.addComponent(panelDisplayedOptions, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		
		gl_mainContent.setVerticalGroup(gl_mainContent.createSequentialGroup()
				.addGroup(gl_mainContent.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelMapName)
						.addComponent(this.labelEmpty)
						.addComponent(this.textFieldMapName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_mainContent.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelAngle)
						.addComponent(this.labelAngleTip)
						.addComponent(this.textFieldAngle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_mainContent.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelColorMode)
						.addComponent(this.labelEmpty)
						.addComponent(this.comboBoxColorMode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_mainContent.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelBackgroundColor)
						.addComponent(this.labelEmpty)
						.addComponent(this.buttonBackgroundColor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(panelDisplayedOptions));
		// @formatter:on
	}

	@Override
	public void apply() {
		if (getMap() != null) {
			getMap().setAngle(this.angle);
			getMap().setColorMode(this.colorMode);
			getMap().getBackgroundStyle().setFillForeColor(this.backgroundColor);
			getMap().setMinVisibleTextSize(this.minVisibleTextSize);
			getMap().setMaxVisibleTextSize(this.maxVisibleTextSize);
			getMap().setMaxVisibleVertex(this.maxVisibleVertex);
			getMap().setOverlapDisplayed(this.isOverlapDisplayed);
			getMap().setMarkerAngleFixed(this.isMarkerAngleFixed);
			getMap().setLineAntialias(this.isLineAntialias);
			getMap().setTextAngleFixed(this.isTextAngleFixed);
			getMap().setTextAntialias(this.isTextAntialias);
			getMap().setTextOrientationFixed(this.isTextOrientationFixed);
			getMap().setOverlapDisplayedOptions(this.mapOverlapDisplayedOptions);
			getMap().refresh();
		}
	}

	@Override
	protected void initializeResources() {
		this.labelMapName.setText(MapViewProperties.getString("String_Label_MapName"));
		this.labelAngle.setText(MapViewProperties.getString("String_Label_MapAngle"));
		this.labelColorMode.setText(MapViewProperties.getString("String_Label_ColorMode"));
		this.labelBackgroundColor.setText(MapViewProperties.getString("String_Label_BackColor"));
		this.labelMinVisibleTextSize.setText(MapViewProperties.getString("String_Label_MinVisibleTextSize"));
		this.labelMaxVisibleTextSize.setText(MapViewProperties.getString("String_Label_MaxVisibleTextSize"));
		this.labelMaxVisibleVertex.setText(MapViewProperties.getString("String_Label_MaxVisibleVertex"));
		this.checkBoxIsOverlapDisplayed.setText(MapViewProperties.getString("String_CheckBox_IsOverlapDisplayed"));
		this.checkBoxIsMarkerAngleFixed.setText(MapViewProperties.getString("String_CheckBox_IsMarkerAngleFixed"));
		this.checkBoxIsLineAntialias.setText(MapViewProperties.getString("String_CheckBox_IsLineAntialias"));
		this.checkBoxIsTextAngleFixed.setText(MapViewProperties.getString("String_CheckBox_IsTextAngleFixed"));
		this.checkBoxIsTextAntialias.setText(MapViewProperties.getString("String_CheckBox_IsTextAntialias"));
		this.checkBoxIsTextOrientationFixed.setText(MapViewProperties.getString("String_CheckBox_IsTextOrientationFixed"));
		this.buttonOverlapDisplayedOptions.setText(MapViewProperties.getString("String_FormOverlapSetting_Title"));
	}

	@Override
	protected void initializePropertyValues(Map map) {
		this.name = map.getName();
		this.angle = map.getAngle();
		this.colorMode = map.getColorMode();
		this.backgroundColor = map.getBackgroundStyle().getFillForeColor();
		this.minVisibleTextSize = map.getMinVisibleTextSize();
		this.maxVisibleTextSize = map.getMaxVisibleTextSize();
		this.maxVisibleVertex = map.getMaxVisibleVertex();
		this.isOverlapDisplayed = map.isOverlapDisplayed();
		this.isMarkerAngleFixed = map.isMarkerAngleFixed();
		this.isLineAntialias = map.isLineAntialias();
		this.isTextAngleFixed = map.isTextAngleFixed();
		this.isLineAntialias = map.isLineAntialias();
		this.isTextOrientationFixed = map.isTextOrientationFixed();
		this.mapOverlapDisplayedOptions = copyMapOverLapDisplayedOpitions(map.getOverlapDisplayedOptions());
	}

	private MapOverlapDisplayedOptions copyMapOverLapDisplayedOpitions(MapOverlapDisplayedOptions displayedOptions) {
		MapOverlapDisplayedOptions copyDisplayedOptions = new MapOverlapDisplayedOptions();
		copyDisplayedOptions.setAllowPointOverlap(displayedOptions.getAllowPointOverlap());
		copyDisplayedOptions.setAllowPointWithTextDisplay(displayedOptions.getAllowPointWithTextDisplay());
		copyDisplayedOptions.setAllowTextAndPointOverlap(displayedOptions.getAllowTextAndPointOverlap());
		copyDisplayedOptions.setAllowTextOverlap(displayedOptions.getAllowTextOverlap());
		copyDisplayedOptions.setAllowThemeGraduatedSymbolOverlap(displayedOptions.getAllowThemeGraduatedSymbolOverlap());
		copyDisplayedOptions.setAllowThemeGraphOverlap(displayedOptions.getAllowThemeGraphOverlap());
		copyDisplayedOptions.setOverlappedSpaceSize(displayedOptions.getOverlappedSpaceSize());
		return copyDisplayedOptions;
	}

	@Override
	protected void registerEvents() {

		super.registerEvents();
		this.caretPositionListener.registerComponent(textFieldMinVisibleTextSize, textFieldMaxVisibleTextSize, textFieldMaxVisibleVertex, textFieldAngle);
		this.textFieldAngle.addPropertyChangeListener(this.propertyChangeListener);
		this.comboBoxColorMode.addItemListener(this.itemListener);
		this.textFieldMinVisibleTextSize.addPropertyChangeListener(this.propertyChangeListener);
		this.textFieldMaxVisibleTextSize.addPropertyChangeListener(this.propertyChangeListener);
		this.textFieldMaxVisibleVertex.addPropertyChangeListener(this.propertyChangeListener);
		this.checkBoxIsOverlapDisplayed.addItemListener(this.itemListener);
		this.checkBoxIsMarkerAngleFixed.addItemListener(this.itemListener);
		this.checkBoxIsLineAntialias.addItemListener(this.itemListener);
		this.checkBoxIsTextAngleFixed.addItemListener(this.itemListener);
		this.checkBoxIsTextAntialias.addItemListener(this.itemListener);
		this.checkBoxIsTextOrientationFixed.addItemListener(this.itemListener);
		this.buttonBackgroundColor.addPropertyChangeListener(ButtonColorSelector.PROPERTY_COLOR, this.propertyChangeListener);
		this.buttonOverlapDisplayedOptions.addMouseListener(setMouseListener);
		this.jPopupMenu.addPropertyChangerListeners(changeListener);
	}

	@Override
	protected void unregisterEvents() {
		super.unregisterEvents();
		this.caretPositionListener.unRegisterComponent(textFieldMinVisibleTextSize, textFieldMaxVisibleTextSize, textFieldMaxVisibleVertex, textFieldAngle);
		this.textFieldAngle.removePropertyChangeListener(this.propertyChangeListener);
		this.comboBoxColorMode.removeItemListener(this.itemListener);
		this.textFieldMinVisibleTextSize.removePropertyChangeListener(this.propertyChangeListener);
		this.textFieldMaxVisibleTextSize.removePropertyChangeListener(this.propertyChangeListener);
		this.textFieldMaxVisibleVertex.removePropertyChangeListener(this.propertyChangeListener);
		this.checkBoxIsOverlapDisplayed.removeItemListener(this.itemListener);
		this.checkBoxIsMarkerAngleFixed.removeItemListener(this.itemListener);
		this.checkBoxIsLineAntialias.removeItemListener(this.itemListener);
		this.checkBoxIsTextAngleFixed.removeItemListener(this.itemListener);
		this.checkBoxIsTextAntialias.removeItemListener(this.itemListener);
		this.checkBoxIsTextOrientationFixed.removeItemListener(this.itemListener);
		this.buttonBackgroundColor.removePropertyChangeListener(ButtonColorSelector.PROPERTY_COLOR, this.propertyChangeListener);
		this.buttonOverlapDisplayedOptions.removeMouseListener(setMouseListener);
		this.jPopupMenu.removePropertyChangerListeners(changeListener);
	}

	@Override
	protected void fillComponents() {
		this.textFieldMapName.setText(this.name);
		this.textFieldAngle.setValue(this.angle);
		fillComboBoxColorMode();
		this.comboBoxColorMode.setSelectedItem(MapColorModeUtilties.toString(this.colorMode));
		this.buttonBackgroundColor.setColor(this.backgroundColor);
		this.textFieldMinVisibleTextSize.setValue(this.minVisibleTextSize);
		this.textFieldMaxVisibleTextSize.setValue(this.maxVisibleTextSize);
		this.textFieldMaxVisibleVertex.setValue(this.maxVisibleVertex);
		this.checkBoxIsOverlapDisplayed.setSelected(this.isOverlapDisplayed);
		this.checkBoxIsMarkerAngleFixed.setSelected(this.isMarkerAngleFixed);
		this.checkBoxIsLineAntialias.setSelected(this.isLineAntialias);
		this.checkBoxIsTextAngleFixed.setSelected(this.isTextAngleFixed);
		this.checkBoxIsTextAntialias.setSelected(this.isTextAngleFixed);
		this.checkBoxIsTextOrientationFixed.setSelected(this.isTextOrientationFixed);
		this.jPopupMenu.setMapOverlapDisplayedOptions(this.mapOverlapDisplayedOptions);
	}

	@Override
	protected void setComponentsEnabled() {
		this.textFieldMapName.setEnabled(false);
		this.buttonOverlapDisplayedOptions.setEnabled(!this.isOverlapDisplayed);
	}

	@Override
	protected boolean verifyChange() {
		return Double.compare(getMap().getAngle(), this.angle) != 0 || getMap().getColorMode() != this.colorMode
				|| !this.backgroundColor.equals(getMap().getBackgroundStyle().getFillBackColor())
				|| Double.compare(getMap().getMinVisibleTextSize(), this.minVisibleTextSize) != 0
				|| Double.compare(getMap().getMaxVisibleTextSize(), this.maxVisibleTextSize) != 0 || getMap().getMaxVisibleVertex() != this.maxVisibleVertex
				|| getMap().isOverlapDisplayed() != this.isOverlapDisplayed || getMap().isMarkerAngleFixed() != this.isMarkerAngleFixed
				|| getMap().isLineAntialias() != this.isLineAntialias || getMap().isTextAngleFixed() != this.isTextAngleFixed
				|| getMap().isTextAntialias() != this.isTextAntialias || getMap().isTextOrientationFixed() != this.isTextOrientationFixed
				|| isMapOverLapDisplayOptionsChanged(getMap().getOverlapDisplayedOptions());
	}

	private boolean isMapOverLapDisplayOptionsChanged(MapOverlapDisplayedOptions displayedOptions) {
		return this.mapOverlapDisplayedOptions.getAllowPointOverlap() != displayedOptions.getAllowPointOverlap()
				|| this.mapOverlapDisplayedOptions.getAllowPointWithTextDisplay() != displayedOptions.getAllowPointWithTextDisplay()
				|| this.mapOverlapDisplayedOptions.getAllowTextAndPointOverlap() != displayedOptions.getAllowTextAndPointOverlap()
				|| this.mapOverlapDisplayedOptions.getAllowTextOverlap() != displayedOptions.getAllowTextOverlap()
				|| this.mapOverlapDisplayedOptions.getAllowThemeGraduatedSymbolOverlap() != displayedOptions.getAllowThemeGraduatedSymbolOverlap()
				|| this.mapOverlapDisplayedOptions.getAllowThemeGraphOverlap() != displayedOptions.getAllowThemeGraphOverlap()
				|| !this.mapOverlapDisplayedOptions.getOverlappedSpaceSize().equals(displayedOptions.getOverlappedSpaceSize());
	}

	private void fillComboBoxColorMode() {
		this.comboBoxColorMode.removeAllItems();
		Enum[] colorModes = Enum.getEnums(MapColorMode.class);

		for (Enum enum1 : colorModes) {
			this.comboBoxColorMode.addItem(MapColorModeUtilties.toString((MapColorMode) enum1));
		}
	}

	private void textFieldAngleValueChange(PropertyChangeEvent e) {
		try {
			if ("value".equalsIgnoreCase(e.getPropertyName())) {
				Double newAngle = Double.valueOf(e.getNewValue().toString());
				if (Double.compare(-360.0, newAngle) < 0 && Double.compare(newAngle, 360.0) < 0) {
					this.angle = e.getNewValue() == null ? this.angle : newAngle;
					verify();
					this.labelAngleTip.setText("");
					this.labelAngleTip.setToolTipText(null);
				} else {
					this.labelAngleTip.setText("<html><font color='red' style='font-weight:bold'>!</font></html>");
					this.labelAngleTip.setToolTipText(MapViewProperties.getString("String_AngleTip"));
				}
			}
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		}
	}

	private void textFieldMinVisibleTextSizeValueChange(PropertyChangeEvent e) {
		try {
			if ("value".equalsIgnoreCase(e.getPropertyName())) {
				this.minVisibleTextSize = e.getNewValue() == null ? this.minVisibleTextSize : Double.valueOf(e.getNewValue().toString());
				verify();
			}
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		}
	}

	private void textFieldMaxVisibleTextSizeValueChange(PropertyChangeEvent e) {
		try {
			if ("value".equalsIgnoreCase(e.getPropertyName())) {
				this.maxVisibleTextSize = e.getNewValue() == null ? this.maxVisibleTextSize : Double.valueOf(e.getNewValue().toString());
				verify();
			}
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		}
	}

	private void textFieldMaxVisibleVertexValueChange(PropertyChangeEvent e) {
		try {
			if ("value".equalsIgnoreCase(e.getPropertyName())) {
				this.maxVisibleVertex = e.getNewValue() == null ? this.maxVisibleVertex : Integer.valueOf(e.getNewValue().toString());
				verify();
			}
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		}
	}

	private void comboBoxColorModeSelectedchange(ItemEvent e) {
		try {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				this.colorMode = MapColorModeUtilties.valueOf((String) e.getItem());
				verify();
			}
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		}
	}

	private void buttonBackgroundColorChange() {
		try {
			this.backgroundColor = this.buttonBackgroundColor.getColor();
			verify();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxIsOverlapDisplayedCheckChange() {
		try {
			this.isOverlapDisplayed = this.checkBoxIsOverlapDisplayed.isSelected();
			this.buttonOverlapDisplayedOptions.setEnabled(!isOverlapDisplayed);
			verify();
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		}
	}

	private void checkBoxIsMarkerAngleFixedCheckChange() {
		try {
			this.isMarkerAngleFixed = this.checkBoxIsMarkerAngleFixed.isSelected();
			verify();
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		}
	}

	private void checkBoxIsLineAntialiasCheckChange() {
		try {
			this.isLineAntialias = this.checkBoxIsLineAntialias.isSelected();
			verify();
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		}
	}

	private void checkBoxIsTextAngleFixedCheckChange() {
		try {
			this.isTextAngleFixed = this.checkBoxIsTextAngleFixed.isSelected();
			verify();
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		}
	}

	private void checkBoxIsTextAntialiasCheckChange() {
		try {
			this.isTextAntialias = this.checkBoxIsTextAntialias.isSelected();
			verify();
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		}
	}

	private void checkBoxIsTextOrientationFixedCheckChange() {
		try {
			this.isTextOrientationFixed = this.checkBoxIsTextOrientationFixed.isSelected();
			verify();
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		}
	}
}
