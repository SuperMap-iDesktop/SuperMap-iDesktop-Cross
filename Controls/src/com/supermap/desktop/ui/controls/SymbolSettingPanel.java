package com.supermap.desktop.ui.controls;

import com.supermap.data.Enum;
import com.supermap.data.FillGradientMode;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoRegion;
import com.supermap.data.GeoStyle;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.Size2D;
import com.supermap.data.SymbolType;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CoreProperties;

import javax.swing.*;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

/**
 * 符号设置面板
 *
 * @author xuzw
 */
class SymbolSettingPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel jLabelMarkerColor;

	private transient ControlButton colorButtonMarker;

	private transient ControlButton controlButtonFillLine;

	private JLabel jLabelMarkerWidth;

	private JLabel jLabelMarkerHeight;

	private JLabel jLabelMarkerAngle;

	private JSpinner jSpinnerMarkerAngle;

	private JCheckBox jCheckBoxMarkerSize;

	private JLabel jLabelLineWidth;

	private JLabel jLabelLineColor;

	private transient ControlButton controlButtonLine;

	private JSpinner jSpinnerLineWidth;

	private JLabel jLabelFillForeColor;

	private JLabel jLabelFillBackColor;

	private transient ControlButton controlButtonFillForeColor;

	private transient ControlButton controlButtonFillBackColor;

	private JCheckBox jCheckBoxFillBackOpaque;

	private JCheckBox jCheckBoxFillGradient;

	private JSpinner jSpinnerFillOpaqueRate;

	private JLabel jLabelFillGradientMode;

	private JLabel jLabelFillGradientAngle;

	private JLabel jLabelFillGradientOffsetRatioX;

	private JLabel jLabelFillGradientOffsetRatioY;

	private JComboBox jComboBoxFillGradientMode;

	private JSpinner jSpinnerFillGradientAngle;

	private JSpinner jSpinnerFillGradientOffsetRatioX;

	private JSpinner jSpinnerFillGradientOffsetRatioY;

	private JLabel jLabelFillOpaqueRate;

	private JSpinner jSpinnerMarkerWidth;

	private JSpinner jSpinnerMarkerHeight;

	private transient SymbolType currentSymbolType;

	private double times;

	private transient Icon lineIcon;

	// 操作过程中被赋值的GeoStyle，该GeoStyle经过用户一系列的赋值之后返回给用户
	private transient GeoStyle activeStyle;

	private transient SymbolPanel currentSymbolPanel;

	private transient ColorSwatch markerColorSwatch;

	private transient ColorSwatch lineColorSwatch;

	private transient ColorSwatch fillForeColorSwatch;

	private transient ColorSwatch fillBackColorSwatch;

	private transient DropDownColor markerDropDownColor;

	private transient DropDownColor lineDropDownColor;

	private transient DropDownColor fillForeDropDownColor;

	private transient DropDownColor fillBackDropDownColor;

	private String markerWidth = "";

	private String markerHeight = "";

	private String markerAngle = "";

	private String lineWidth = "";

	private String fillOpaque = "";

	private String fillGradienAngle = "";

	private String fillGradientX = "";

	private String fillGradientY = "";

	private JPanel jPanelFillTop;

	private JPanel jPanelFillColor;

	private JPanel jPanelFillBackOpaque;

	private JPanel jPanelFillLine;

	private JPanel jPanelFillBottom;

	private transient FocusAdapter selectAllAdapter;

	private ImageIcon[] icons = new ImageIcon[5];

	private transient FillGradientMode fillGradientMode = FillGradientMode.NONE;

	private transient SymbolDialog symbolLineDialog = null;

	/**
	 * 构造符号设置面板，
	 *
	 * @param symbolPanel 预览面板
	 * @param geoStyle
	 * @param symbolType
	 */
	public SymbolSettingPanel(SymbolPanel symbolPanel, GeoStyle geoStyle, SymbolType symbolType) {
		super();
		this.currentSymbolPanel = symbolPanel;
		this.currentSymbolType = symbolType;
		this.activeStyle = geoStyle;
		initialize();
	}

	/**
	 * 初始化
	 */
	protected void initialize() {
		if (currentSymbolType.equals(SymbolType.MARKER)) {
			setLayout(new GridBagLayout());
			setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(Color.gray, 1),
					ControlsProperties.getString("String_Settings"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
			initializeMarker();
		} else if (currentSymbolType.equals(SymbolType.LINE)) {
			setLayout(new GridBagLayout());
			setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(Color.gray, 1),
					ControlsProperties.getString("String_Settings"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
			initializeLine();
		} else if (currentSymbolType.equals(SymbolType.FILL)) {
			setLayout(null);
			initializeFill();
		}
	}

	/**
	 * 初始化点符号面板
	 */
	protected void initializeMarker() {
		jLabelMarkerAngle = new JLabel();
		jLabelMarkerAngle.setText(ControlsProperties.getString("String_Label_OffsetAngle"));
		jLabelMarkerWidth = new JLabel();
		jLabelMarkerWidth.setText(ControlsProperties.getString("String_labelWidth"));

		jLabelMarkerHeight = new JLabel();
		jLabelMarkerHeight.setText(ControlsProperties.getString("String_labelAltitude"));

		jLabelMarkerColor = new JLabel();
		jLabelMarkerColor.setText(ControlsProperties.getString("String_Label_Color"));
		jLabelFillOpaqueRate = new JLabel();
		jLabelFillOpaqueRate.setText(CoreProperties.getString("String_Label_OpaqueRate"));

		JLabel labelWidth = new JLabel(ControlsProperties.getString("String_Label_WidthMeasuringUnit"));
		JLabel labelHeight = new JLabel(ControlsProperties.getString("String_Label_HightMeasuringUnit"));
		JLabel labelAngle = new JLabel(ControlsProperties.getString("String_angle"));
		JLabel labelPrecent = new JLabel(ControlsProperties.getString("String_precent"));

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 100;
		constraints.weighty = 100;
		addMarkerComponent(jLabelMarkerColor, constraints, 0, 0, 1, 1);
		addMarkerComponent(jLabelMarkerWidth, constraints, 0, 1, 1, 1);
		addMarkerComponent(jLabelMarkerHeight, constraints, 0, 2, 1, 1);
		addMarkerComponent(jLabelMarkerAngle, constraints, 0, 3, 1, 1);
		addMarkerComponent(jLabelFillOpaqueRate, constraints, 0, 4, 1, 1);

		addMarkerComponent(getSymbolMarkerColorButton(), constraints, 1, 0, 1, 1);
		addMarkerComponent(getSymbolMarkerWidthJSpinner(), constraints, 1, 1, 1, 1);
		addMarkerComponent(getSymbolMarkerHeightJSpinner(), constraints, 1, 2, 1, 1);
		addMarkerComponent(getSymbolMarkerAngleSpinner(), constraints, 1, 3, 1, 1);
		addMarkerComponent(getSymbolFillOpaqueRateSpinner(), constraints, 1, 4, 1, 1);
		addMarkerComponent(getSymbolMarkerSizeBox(), constraints, 0, 5, 2, 1);

		addMarkerComponent(labelWidth, constraints, 2, 1, 1, 1);
		addMarkerComponent(labelHeight, constraints, 2, 2, 1, 1);
		addMarkerComponent(labelAngle, constraints, 2, 3, 1, 1);
		addMarkerComponent(labelPrecent, constraints, 2, 4, 1, 1);
	}

	/**
	 * 初始化线符号面板
	 */
	protected void initializeLine() {
		jLabelLineWidth = new JLabel();
		jLabelLineWidth.setText(ControlsProperties.getString("String_labelWidth"));
		jLabelLineColor = new JLabel();
		jLabelLineColor.setText(ControlsProperties.getString("String_Label_Color"));
		JLabel labelWidth = new JLabel(ControlsProperties.getString("String_Label_WidthMeasuringUnit"));

		GridBagConstraints constrains = new GridBagConstraints();
		constrains.weightx = 100;
		constrains.weighty = 100;
		constrains.fill = GridBagConstraints.WEST;
		constrains.anchor = GridBagConstraints.CENTER;
		constrains.gridx = 0;
		constrains.gridy = 0;
		add(jLabelLineColor, constrains);
		constrains.gridx = 0;
		constrains.gridy = 1;
		add(jLabelLineWidth, constrains);
		constrains.gridx = 1;
		constrains.gridy = 0;
		add(getSymbolLineColorButton(), constrains);
		constrains.gridx = 1;
		constrains.gridy = 1;
		add(getSymbolLineWidthSpinner(), constrains);
		constrains.gridx = 2;
		constrains.gridy = 1;
		add(labelWidth, constrains);
	}

	/**
	 * 初始化面符号面板
	 */
	protected void initializeFill() {
		this.add(getFillTopPanel());
		this.add(getFillLinePanel());
		this.add(getFillGradientCheckBox());
		this.add(getFillBottomPanel());
	}

	/**
	 * 填充设置上方面板，包括颜色设置和透明设置
	 *
	 * @return
	 */
	protected JPanel getFillTopPanel() {
		if (jPanelFillTop == null) {
			jPanelFillTop = new JPanel();
			jPanelFillTop.setBounds(0, 0, 248, 76);
			jPanelFillTop.setLayout(new BoxLayout(jPanelFillTop, BoxLayout.X_AXIS));
			jPanelFillTop.add(getFillColorPanel());
			jPanelFillTop.add(getFillBackOpaquePanel());
		}
		return jPanelFillTop;
	}

	/**
	 * 颜色设置面板
	 *
	 * @return
	 */
	protected JPanel getFillColorPanel() {
		if (jPanelFillColor == null) {
			jPanelFillColor = new JPanel();
			jPanelFillColor.setPreferredSize(new Dimension(112, 76));
			jPanelFillColor.setMaximumSize(new Dimension(112, 76));
			jPanelFillColor.setMinimumSize(new Dimension(112, 76));
			jPanelFillColor.setLayout(new GridBagLayout());
			jPanelFillColor.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(Color.gray, 1),
					ControlsProperties.getString("String_ColorSetting"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));

			GridBagConstraints constraints = new GridBagConstraints();
			constraints.weightx = 100;
			constraints.weighty = 100;
			constraints.fill = GridBagConstraints.WEST;
			constraints.anchor = GridBagConstraints.CENTER;
			constraints.gridx = 0;
			constraints.gridy = 0;
			jLabelFillForeColor = new JLabel();
			jLabelFillForeColor.setText(ControlsProperties.getString("String_Label_ForColor"));
			jLabelFillBackColor = new JLabel();
			jLabelFillBackColor.setText(ControlsProperties.getString("String_Label_backColor"));

			addComponentFillColorPanel(jLabelFillForeColor, constraints, 0, 0, 1, 1);
			addComponentFillColorPanel(jLabelFillBackColor, constraints, 0, 1, 1, 1);
			addComponentFillColorPanel(getSymbolFillForeColor(), constraints, 1, 0, 1, 1);
			addComponentFillColorPanel(getSymbolFillBackColor(), constraints, 1, 1, 1, 1);
		}
		return jPanelFillColor;
	}

	/**
	 * 透明设置面板
	 *
	 * @return
	 */
	protected JPanel getFillBackOpaquePanel() {
		if (jPanelFillBackOpaque == null) {
			jPanelFillBackOpaque = new JPanel();
			jPanelFillBackOpaque.setPreferredSize(new Dimension(138, 76));
			jPanelFillBackOpaque.setMaximumSize(new Dimension(138, 76));
			jPanelFillBackOpaque.setMinimumSize(new Dimension(138, 76));
			jPanelFillBackOpaque.setLayout(new GridLayout(0, 1));
			jPanelFillBackOpaque
					.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(Color.gray, 1),
							ControlsProperties.getString("String_TransparenceSettings"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
							null, null));

			jPanelFillBackOpaque.add(getSymbolFillBackOpaqueCheckBox());
			JPanel panel = new JPanel();
			jLabelFillOpaqueRate = new JLabel();
			jLabelFillOpaqueRate.setText(ControlsProperties.getString("String_TransparenceSettings"));
			panel.add(jLabelFillOpaqueRate);
			panel.add(getSymbolFillOpaqueRateSpinner());
			JLabel labelOpaque = new JLabel(CoreProperties.getString("String_Percent"));
			panel.add(labelOpaque);
			jPanelFillBackOpaque.add(panel);
		}
		return jPanelFillBackOpaque;
	}

	protected JCheckBox getFillGradientCheckBox() {
		if (jCheckBoxFillGradient == null) {
			jCheckBoxFillGradient = new JCheckBox();
			jCheckBoxFillGradient.setBounds(5, 110, 100, 20);
			jCheckBoxFillGradient.setText(ControlsProperties.getString("String_FillGradient"));
			jCheckBoxFillGradient.setSelected(true);
			jCheckBoxFillGradient.setOpaque(true);
			if (activeStyle.getFillGradientMode().equals(FillGradientMode.NONE)) {
				jCheckBoxFillGradient.setSelected(false);
			}
			jCheckBoxFillGradient.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					abstractjCheckBoxFillGradient();
				}

			});
		}
		return jCheckBoxFillGradient;
	}

	private void abstractjCheckBoxFillGradient() {
		if (jCheckBoxFillGradient.isSelected()) {
			refreshFillGradientModelIcon();
			getSymbolFillGradientAngleSpinner().setEnabled(true);
			getSymbolFillGradientOffsetRatioXSpinner().setEnabled(true);
			getSymbolFillGradientOffsetRatioYSpinner().setEnabled(true);
			jComboBoxFillGradientMode.setEnabled(true);
			activeStyle.setFillGradientMode(fillGradientMode);
			currentSymbolPanel.SetStyleAndRefresh(activeStyle);
		} else {
			// 非渐变填充时，对应的属性框为不可编辑并设置渐变模式为NONE
			getSymbolFillGradientAngleSpinner().setEnabled(false);
			getSymbolFillGradientOffsetRatioXSpinner().setEnabled(false);
			getSymbolFillGradientOffsetRatioYSpinner().setEnabled(false);
			jComboBoxFillGradientMode.setEnabled(false);
			activeStyle.setFillGradientMode(FillGradientMode.NONE);
			currentSymbolPanel.SetStyleAndRefresh(activeStyle);
			refreshFillGradientModelIcon();
		}
	}

	protected JPanel getFillLinePanel() {
		if (jPanelFillLine == null) {
			jPanelFillLine = new JPanel();
			jPanelFillLine.setBounds(5, 78, 200, 30);
			// 这里再用一个JPanel，防止填充的太满
			JPanel panel = new JPanel();
			panel.add(new JLabel(ControlsProperties.getString("String_Label_SymbolLineSelect")));
			panel.add(getSymbolFillLine());
			jPanelFillLine.add(panel);
		}
		return jPanelFillLine;
	}

	protected JPanel getFillBottomPanel() {
		if (jPanelFillBottom == null) {
			jPanelFillBottom = new JPanel();
			jPanelFillBottom.setBounds(0, 110, 250, 97);
			jPanelFillBottom.setBorder(new TitledBorder(javax.swing.BorderFactory.createLineBorder(Color.gray, 1), " ", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, null));

			jPanelFillBottom.setLayout(new GridBagLayout());
			jLabelFillGradientMode = new JLabel();
			jLabelFillGradientMode.setText(ControlsProperties.getString("String_Label_Type"));
			jLabelFillGradientAngle = new JLabel();
			jLabelFillGradientAngle.setText(ControlsProperties.getString("String_Label_OffsetAngle"));
			jLabelFillGradientOffsetRatioX = new JLabel();
			jLabelFillGradientOffsetRatioX.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
			jLabelFillGradientOffsetRatioX.setText(ControlsProperties.getString("String_Label_OffsetRatioX"));
			jLabelFillGradientOffsetRatioY = new JLabel();
			jLabelFillGradientOffsetRatioY.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
			jLabelFillGradientOffsetRatioY.setText(ControlsProperties.getString("String_Label_OffsetRatioY"));
			JLabel labelRatioX = new JLabel(CoreProperties.getString("String_Percent"));
			JLabel labelRatioY = new JLabel(CoreProperties.getString("String_Percent"));
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.anchor = GridBagConstraints.WEST;
			constraints.weightx = 100;
			constraints.weighty = 100;
			addComponentFillBottomPanel(jLabelFillGradientMode, constraints, 0, 0, 1, 1);
			addComponentFillBottomPanel(getSymbolFillGradientModelComboBox(), constraints, 1, 0, 1, 1);
			addComponentFillBottomPanel(labelRatioX, constraints, 4, 0, 1, 1);
			addComponentFillBottomPanel(jLabelFillGradientAngle, constraints, 0, 1, 1, 1);
			addComponentFillBottomPanel(getSymbolFillGradientAngleSpinner(), constraints, 1, 1, 1, 1);
			constraints.anchor = GridBagConstraints.EAST;
			addComponentFillBottomPanel(jLabelFillGradientOffsetRatioX, constraints, 2, 0, 1, 1);
			addComponentFillBottomPanel(getSymbolFillGradientOffsetRatioXSpinner(), constraints, 3, 0, 1, 1);
			addComponentFillBottomPanel(jLabelFillGradientOffsetRatioY, constraints, 2, 1, 1, 1);
			addComponentFillBottomPanel(getSymbolFillGradientOffsetRatioYSpinner(), constraints, 3, 1, 1, 1);
			addComponentFillBottomPanel(labelRatioY, constraints, 4, 1, 1, 1);
		}
		return jPanelFillBottom;
	}

	/**
	 *
	 */
	private FocusAdapter getSelectAllAdapter(final JSpinner spinner) {
		selectAllAdapter = new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						((JSpinner.NumberEditor) spinner.getEditor()).getTextField().selectAll();
					}
				});
			}
		};
		return selectAllAdapter;
	}

	/**
	 * 点符号宽度值
	 *
	 * @return
	 */
	private JSpinner getSymbolMarkerWidthJSpinner() {
		if (jSpinnerMarkerWidth == null) {
			jSpinnerMarkerWidth = new JSpinner();
			jSpinnerMarkerWidth.setPreferredSize(new Dimension(85, 22));
			jSpinnerMarkerWidth.setMinimumSize(new Dimension(85, 22));
			jSpinnerMarkerWidth.setToolTipText(ControlsProperties.getString("String_SymbolMarkerWidthRange"));
			SpinnerNumberModel model = new SpinnerNumberModel(activeStyle.getMarkerSize().getWidth(), 0, 999, 0.1);
			jSpinnerMarkerWidth.setModel(model);
			NumberEditor numberEditor = (JSpinner.NumberEditor) jSpinnerMarkerWidth.getEditor();
			final JTextField widthTextField = numberEditor.getTextField();
			numberEditor.getFormat().applyPattern("##0.0");
			// 如果初始化为1.0，实际显示的是1，此处再次设置是为了让显示为1.0
			widthTextField.setText(String.valueOf(activeStyle.getMarkerSize().getWidth()));
			widthTextField.addFocusListener(getSelectAllAdapter(jSpinnerMarkerWidth));
			widthTextField.addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					try {
						String widthString = widthTextField.getText();
						if (widthString.contains("d")) {
							return;
						}
						try {
							Double aDouble = Double.valueOf(widthString);
						} catch (Exception e1) {
							return;
						}
						if (widthString == null || "".equals(widthString) || markerWidth.equals(widthString) || Double.valueOf(widthString) > 999) {
							return;
						}

						if (jCheckBoxMarkerSize.isSelected()) {
							Double height = Double.valueOf(widthString) / times;
							// 注销高度监听器，设置高度值后再加上，可以解决重画的问题
							NumberEditor numberEditorHeight = (JSpinner.NumberEditor) jSpinnerMarkerHeight.getEditor();
							JTextField heightTextField = numberEditorHeight.getTextField();
							CaretListener[] caretListener = heightTextField.getCaretListeners();
							heightTextField.removeCaretListener(caretListener[0]);
							DecimalFormat decimalFormat = new DecimalFormat("#.0");
							numberEditorHeight.getTextField().setText(decimalFormat.format(height));
							heightTextField.addCaretListener(caretListener[0]);
							markerHeight = decimalFormat.format(height);
						}
						markerWidth = widthString;
						setSymbolMarkerSize();
					} catch (Exception ex) {
						Application.getActiveApplication().getOutput().output(ex);
					}
				}
			});

		}
		return jSpinnerMarkerWidth;
	}

	/**
	 * 设置点符号大小
	 */
	private void setSymbolMarkerSize() {
		try {
			String widthString = ((JSpinner.NumberEditor) jSpinnerMarkerWidth.getEditor()).getTextField().getText();
			String heightString = ((JSpinner.NumberEditor) jSpinnerMarkerHeight.getEditor()).getTextField().getText();
			if (widthString == null || "".equals(widthString) || heightString == null || "".equals(heightString)) {
				return;
			}
			double width = Double.parseDouble(widthString);
			double height = Double.parseDouble(heightString);
			Size2D size2D = new Size2D(width, height);
			activeStyle.setMarkerSize(size2D);
			currentSymbolPanel.SetStyleAndRefresh(activeStyle);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 设置点符号高度
	 *
	 * @return
	 */
	private JSpinner getSymbolMarkerHeightJSpinner() {
		if (jSpinnerMarkerHeight == null) {
			jSpinnerMarkerHeight = new JSpinner();
			jSpinnerMarkerHeight.setPreferredSize(new Dimension(85, 22));
			jSpinnerMarkerHeight.setMinimumSize(new Dimension(85, 22));
			jSpinnerMarkerHeight.setToolTipText(ControlsProperties.getString("String_SymbolMarkerWidthRange"));
			SpinnerNumberModel model = new SpinnerNumberModel(activeStyle.getMarkerSize().getHeight(), 0, 999, 0.1);
			jSpinnerMarkerHeight.setModel(model);
			// 格式化输入值
			NumberEditor numberEditor = (JSpinner.NumberEditor) jSpinnerMarkerHeight.getEditor();
			final JTextField heightTextField = numberEditor.getTextField();
			numberEditor.getFormat().applyPattern("##0.0");
			// 如果初始化为1.0，实际显示的是1，此处再次设置是为了让显示为1.0
			heightTextField.setText(String.valueOf(activeStyle.getMarkerSize().getHeight()));
			heightTextField.addFocusListener(getSelectAllAdapter(jSpinnerMarkerHeight));
			heightTextField.addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					abstractHeightTextFieldLisenter(heightTextField);
				}
			});

		}
		return jSpinnerMarkerHeight;
	}

	private void abstractHeightTextFieldLisenter(final JTextField heightTextField) {
		try {
			String heightString = heightTextField.getText();
			if (heightString.contains("d")) {
				return;
			}
			try {
				Double aDouble = Double.valueOf(heightString);
			} catch (Exception e) {
				return;
			}
			if (heightString == null || "".equals(heightString) || markerHeight.equals(heightString) || Double.valueOf(heightString) > 900) {
				return;
			}

			if (jCheckBoxMarkerSize.isSelected()) {
				Double width = Double.valueOf(heightString) * times;
				// 注销高度监听器，设置高度值后再加上，可以解决重画的问题
				NumberEditor numberEditorWidth = (JSpinner.NumberEditor) jSpinnerMarkerWidth.getEditor();
				JTextField widthTextField = numberEditorWidth.getTextField();

				CaretListener[] caretListener = widthTextField.getCaretListeners();
				widthTextField.removeCaretListener(caretListener[0]);
				DecimalFormat decimalFormat = new DecimalFormat("#.0");
				numberEditorWidth.getTextField().setText(decimalFormat.format(width));
				widthTextField.addCaretListener(caretListener[0]);
				// 此处赋值解决不能及时更新问题
				markerWidth = decimalFormat.format(width);
			}
			markerHeight = heightString;
			setSymbolMarkerSize();
		} catch (Exception e2) {
			// do nothing
		}
	}

	/**
	 * 锁定宽高比
	 *
	 * @return
	 */
	private JCheckBox getSymbolMarkerSizeBox() {
		if (jCheckBoxMarkerSize == null) {
			jCheckBoxMarkerSize = new JCheckBox();
			jCheckBoxMarkerSize.setText(ControlsProperties.getString("String_LockMarkerWidthAndHeight"));
			jCheckBoxMarkerSize.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (jCheckBoxMarkerSize.isSelected()) {
						Double width = Double.valueOf(jSpinnerMarkerWidth.getValue().toString());
						Double height = Double.valueOf(jSpinnerMarkerHeight.getValue().toString());
						times = ((double) width) / ((double) height);
					} else {
						times = 1.0;
					}
				}
			});
			jCheckBoxMarkerSize.setSelected(true);
		}
		return jCheckBoxMarkerSize;
	}

	/**
	 * 符号旋转角度
	 *
	 * @return
	 */
	private JSpinner getSymbolMarkerAngleSpinner() {
		if (jSpinnerMarkerAngle == null) {
			jSpinnerMarkerAngle = new JSpinner();
			jSpinnerMarkerAngle.setPreferredSize(new Dimension(85, 22));
			jSpinnerMarkerAngle.setMinimumSize(new Dimension(85, 22));
			jSpinnerMarkerAngle.setToolTipText(ControlsProperties.getString("String_SymbolMarkerAngleRange"));
			SpinnerNumberModel model = new SpinnerNumberModel(activeStyle.getMarkerAngle(), -180, 180, 1);
			jSpinnerMarkerAngle.setModel(model);
			NumberEditor numberEditor = (JSpinner.NumberEditor) jSpinnerMarkerAngle.getEditor();
			final JTextField angleTextField = numberEditor.getTextField();
			// 如果初始化为1.0，实际显示的是1，此处再次设置是为了让显示为1.0
			angleTextField.setText(String.valueOf(activeStyle.getMarkerAngle()));
			numberEditor.getFormat().applyPattern("##0.0");
			angleTextField.addFocusListener(getSelectAllAdapter(jSpinnerMarkerAngle));
			angleTextField.addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					try {
						String angleString = angleTextField.getText();
						if (angleString.contains("d")) {
							return;
						}
						try {
							Double aDouble = Double.valueOf(angleString);
						} catch (NumberFormatException e1) {
							return;
						}
						if (angleString == null || "".equals(angleString) || markerAngle.equals(angleString) || Double.valueOf(angleString) > 180
								|| Double.valueOf(angleString) < -180) {
							return;
						}
						double angle = Double.parseDouble(angleString);
						markerAngle = angleString;

						activeStyle.setMarkerAngle(angle);
						currentSymbolPanel.SetStyleAndRefresh(activeStyle);
					} catch (Exception ex) {
						Application.getActiveApplication().getOutput().output(ex);
					}
				}
			});
		}
		return jSpinnerMarkerAngle;
	}

	/**
	 * 符号颜色
	 *
	 * @return
	 */
	protected DropDownColor getSymbolMarkerColorButton() {
		if (markerDropDownColor == null) {
			colorButtonMarker = new ControlButton();
			colorButtonMarker.setEnabled(false);
			markerColorSwatch = new ColorSwatch(activeStyle.getLineColor(), 14, 60);
			colorButtonMarker.setIcon(markerColorSwatch);
			colorButtonMarker.setPreferredSize(new Dimension(69, 20));
			markerDropDownColor = new DropDownColor(colorButtonMarker);
			// 这里把期望宽度设置的略大一些，保证在网格中显示正确
			markerDropDownColor.setPreferredSize(new Dimension(85, 28));
			markerDropDownColor.addPropertyChangeListener("m_selectionColors", new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					Color color = markerDropDownColor.getColor();
					if (color != null) {
						activeStyle.setLineColor(color);
						markerColorSwatch.setColor(color);
						colorButtonMarker.repaint();
						currentSymbolPanel.SetStyleAndRefresh(activeStyle);
					}
				}
			});
		}
		return markerDropDownColor;
	}

	/**
	 * 线符号颜色
	 *
	 * @return
	 */
	protected DropDownColor getSymbolLineColorButton() {
		if (lineDropDownColor == null) {
			controlButtonLine = new ControlButton();
			controlButtonLine.setPreferredSize(new Dimension(65, 20));
			controlButtonLine.setEnabled(false);
			lineColorSwatch = new ColorSwatch(activeStyle.getLineColor(), 14, 56);
			controlButtonLine.setIcon(lineColorSwatch);
			lineDropDownColor = new DropDownColor(controlButtonLine);
			lineDropDownColor.addPropertyChangeListener("m_selectionColors", new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					Color color = lineDropDownColor.getColor();
					if (color != null) {
						activeStyle.setLineColor(color);
						lineColorSwatch.setColor(color);
						controlButtonLine.repaint();
						currentSymbolPanel.SetStyleAndRefresh(activeStyle);
					}
				}
			});
		}
		return lineDropDownColor;
	}

	/**
	 * 线符号宽度
	 *
	 * @return
	 */
	private JSpinner getSymbolLineWidthSpinner() {
		if (jSpinnerLineWidth == null) {
			jSpinnerLineWidth = new JSpinner();
			jSpinnerLineWidth.setToolTipText(ControlsProperties.getString("String_SymbolLineWidthRange"));
			jSpinnerLineWidth.setPreferredSize(new Dimension(80, 22));
			jSpinnerLineWidth.setMinimumSize(new Dimension(80, 22));
			SpinnerNumberModel model = new SpinnerNumberModel(activeStyle.getLineWidth(), 0.1, 20, 0.1);
			jSpinnerLineWidth.setModel(model);
			NumberEditor numberEditor = (JSpinner.NumberEditor) jSpinnerLineWidth.getEditor();
			final JTextField widthTextField = numberEditor.getTextField();
			// 如果初始化为1.0，实际显示的是1，鼠标点击后会变为1.0
			// 解决此问题，此处再次设置是为了让显示为1.0

			// 默认将线宽设置为0.1
			widthTextField.setText(String.valueOf(activeStyle.getLineWidth()));
			numberEditor.getFormat().applyPattern("##0.0");
			widthTextField.addFocusListener(getSelectAllAdapter(jSpinnerLineWidth));
			widthTextField.addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					abstractWidthTextField(widthTextField);
				}
			});
		}
		return jSpinnerLineWidth;
	}

	private void abstractWidthTextField(final JTextField widthTextField) {
		try {
			String lineWidthString = widthTextField.getText();
			if (lineWidthString.contains("d")) {
				return;
			}
			try {
				Double aDouble = Double.valueOf(lineWidthString);
			} catch (NumberFormatException e) {
				return;
			}
			if (lineWidthString == null || "".equals(lineWidthString) || lineWidth.equals(lineWidthString) || Double.valueOf(lineWidthString) > 20) {
				return;
			}
			lineWidth = lineWidthString;
			double lineWidthTemp = Double.parseDouble(lineWidthString);
			activeStyle.setLineWidth(lineWidthTemp);
			currentSymbolPanel.SetStyleAndRefresh(activeStyle);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 面符号前景色
	 *
	 * @return
	 */
	protected DropDownColor getSymbolFillForeColor() {
		if (fillForeDropDownColor == null) {
			controlButtonFillForeColor = new ControlButton();
			controlButtonFillForeColor.setEnabled(false);
			controlButtonFillForeColor.setPreferredSize(new Dimension(30, 15));
			fillForeColorSwatch = new ColorSwatch(activeStyle.getFillForeColor(), 9, 20);
			controlButtonFillForeColor.setIcon(fillForeColorSwatch);

			fillForeDropDownColor = new DropDownColor(controlButtonFillForeColor);
			// 这里把期望宽度设置的略大一些，保证在网格中显示正确
			fillForeDropDownColor.setPreferredSize(new Dimension(50, 24));
			fillForeDropDownColor.addPropertyChangeListener("m_selectionColors", new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					Color color = fillForeDropDownColor.getColor();
					if (color != null) {
						activeStyle.setFillForeColor(color);
						fillForeColorSwatch.setColor(color);
						controlButtonFillForeColor.repaint();
						currentSymbolPanel.SetStyleAndRefresh(activeStyle);
						if (jCheckBoxFillGradient.isSelected()) {
							refreshFillGradientModelIcon();
						}
					}
				}
			});
		}
		return fillForeDropDownColor;
	}

	/**
	 * 面符号背景色
	 *
	 * @return
	 */
	protected DropDownColor getSymbolFillBackColor() {
		if (fillBackDropDownColor == null) {
			controlButtonFillBackColor = new ControlButton();
			controlButtonFillBackColor.setEnabled(false);
			controlButtonFillBackColor.setPreferredSize(new Dimension(30, 15));
			fillBackColorSwatch = new ColorSwatch(activeStyle.getFillBackColor(), 9, 20);
			fillBackDropDownColor = new DropDownColor(controlButtonFillBackColor);
			fillBackDropDownColor.getArrowButton().setEnabled(activeStyle.getFillBackOpaque());
			if (!activeStyle.getFillBackOpaque()) {
				controlButtonFillBackColor.setIcon(null);
			} else {
				controlButtonFillBackColor.setIcon(fillBackColorSwatch);
			}
			// 这里把期望宽度设置的略大一些，保证在网格中显示正确
			fillBackDropDownColor.setPreferredSize(new Dimension(50, 26));
			fillBackDropDownColor.addPropertyChangeListener("m_selectionColors", new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					Color color = fillBackDropDownColor.getColor();
					if (color != null) {
						activeStyle.setFillBackColor(color);
						fillBackColorSwatch.setColor(color);
						controlButtonFillBackColor.repaint();
						currentSymbolPanel.SetStyleAndRefresh(activeStyle);
						if (jCheckBoxFillGradient.isSelected()) {
							refreshFillGradientModelIcon();
						}
					}
				}
			});
		}
		return fillBackDropDownColor;
	}

	/**
	 * 获取背景透明
	 *
	 * @return
	 */
	private JCheckBox getSymbolFillBackOpaqueCheckBox() {
		if (jCheckBoxFillBackOpaque == null) {
			jCheckBoxFillBackOpaque = new JCheckBox();
			jCheckBoxFillBackOpaque.setSelected(!activeStyle.getFillBackOpaque());
			jCheckBoxFillBackOpaque.setText(ControlsProperties.getString("String_BackgroundTransparent"));

			jCheckBoxFillBackOpaque.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (jCheckBoxFillBackOpaque.isSelected()) {
						activeStyle.setFillBackOpaque(false);
						getSymbolFillBackColor().getArrowButton().setEnabled(false);
						controlButtonFillBackColor.setIcon(null);
					} else {
						activeStyle.setFillBackOpaque(true);
						getSymbolFillBackColor().getArrowButton().setEnabled(true);
						controlButtonFillBackColor.setIcon(fillBackColorSwatch);
					}
					currentSymbolPanel.SetStyleAndRefresh(activeStyle);
					refreshFillGradientModelIcon();
				}

			});

		}
		return jCheckBoxFillBackOpaque;
	}

	/**
	 * 面符号设置线型
	 *
	 * @return
	 */
	private JButton getSymbolFillLine() {
		if (controlButtonFillLine == null) {
			controlButtonFillLine = new ControlButton();
			controlButtonFillLine.setPreferredSize(new Dimension(130, 20));
			controlButtonFillLine.setIcon(getLineIcon());
			controlButtonFillLine.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// 面符号中进行线型选择
					symbolLineDialog = new SymbolDialog();
					DialogResult dialogResult = symbolLineDialog.showDialog(currentSymbolPanel.getResources(), activeStyle, SymbolType.LINE);
					if (dialogResult.equals(DialogResult.OK)) {
						// 如果用户在线型面板中点击了确定的话那么要刷新一次预览框
						activeStyle = symbolLineDialog.getStyle();
						currentSymbolPanel.SetStyleAndRefresh(activeStyle);
						controlButtonFillLine.setIcon(getLineIcon());
					}
					symbolLineDialog.dispose();
				}
			});
		}
		return controlButtonFillLine;
	}

	/**
	 * 线型图片获取
	 *
	 * @return
	 */
	private Icon getLineIcon() {
		Point2Ds point2Ds = new Point2Ds();
		point2Ds.add(new Point2D(9, 8));
		point2Ds.add(new Point2D(90, 8));
		GeoLine geoLine = new GeoLine(point2Ds);
		GeoStyle lineGeoStyle = activeStyle;
		geoLine.setStyle(lineGeoStyle);
		BufferedImage bufferedImage = new BufferedImage(100, 16, BufferedImage.TYPE_INT_ARGB);
		geoLine.getStyle().setLineSymbolID(lineGeoStyle.getLineSymbolID());
		geoLine.getStyle().setLineWidth(0.1);
		InternalToolkitControl.internalDraw(geoLine, currentSymbolPanel.getResources(), bufferedImage.getGraphics());
		lineIcon = new ImageIcon(flipVerticalJ2D(bufferedImage));
		return lineIcon;
	}

	/**
	 * 面填充图片获取
	 */
	private ImageIcon getFillGradientModelIcon(FillGradientMode fillGradientMode) {
		Point2Ds point2DsRegion = new Point2Ds();
		point2DsRegion.add(new Point2D(0, 27));
		point2DsRegion.add(new Point2D(27, 27));
		point2DsRegion.add(new Point2D(27, 0));
		point2DsRegion.add(new Point2D(1, 1));
		GeoRegion geoRegion = new GeoRegion(point2DsRegion);
		GeoStyle fillStyle = new GeoStyle();
		BufferedImage bufferedImage = new BufferedImage(28, 28, BufferedImage.TYPE_INT_ARGB);
		fillStyle.setFillGradientMode(fillGradientMode);
		fillStyle.setLineSymbolID(5);
		fillStyle.setFillForeColor(activeStyle.getFillForeColor());
		fillStyle.setFillBackColor(activeStyle.getFillBackColor());
		fillStyle.setFillBackOpaque(!jCheckBoxFillBackOpaque.isSelected());
		geoRegion.setStyle(fillStyle);
		if (!jCheckBoxFillGradient.isSelected()) {
			geoRegion.getStyle().setFillBackColor(Color.gray);
			geoRegion.getStyle().setFillForeColor(Color.gray);
		}
		InternalToolkitControl.internalDraw(geoRegion, currentSymbolPanel.getResources(), bufferedImage.getGraphics());
		return new ImageIcon(bufferedImage);
	}

	/**
	 * 刷新渐变填充下拉框的图片
	 */
	private void refreshFillGradientModelIcon() {
		icons[0] = getFillGradientModelIcon(FillGradientMode.NONE);
		icons[0].setDescription(CoreProperties.getString("String_None"));
		icons[1] = getFillGradientModelIcon(FillGradientMode.LINEAR);
		icons[1].setDescription(CoreProperties.getString("String_LINEAR"));
		icons[2] = getFillGradientModelIcon(FillGradientMode.RADIAL);
		icons[2].setDescription(CoreProperties.getString("String_RADIAL"));
		icons[3] = getFillGradientModelIcon(FillGradientMode.CONICAL);
		icons[3].setDescription(CoreProperties.getString("String_CONICAL"));
		icons[4] = getFillGradientModelIcon(FillGradientMode.SQUARE);
		icons[4].setDescription(CoreProperties.getString("String_SQUARE"));
		jComboBoxFillGradientMode.repaint();
	}

	/**
	 * 不透明度
	 *
	 * @return
	 */
	private JSpinner getSymbolFillOpaqueRateSpinner() {
		if (jSpinnerFillOpaqueRate == null) {
			jSpinnerFillOpaqueRate = new JSpinner();
			jSpinnerFillOpaqueRate.setToolTipText(ControlsProperties.getString("String_SymbolFillOpaqueRateRange"));
			jSpinnerFillOpaqueRate.setPreferredSize(new Dimension(48, 18));
			int tempFillOpaqueRate = Math.abs(activeStyle.getFillOpaqueRate() - 100);
			SpinnerNumberModel model = new SpinnerNumberModel(tempFillOpaqueRate, 0, 100, 1);
			jSpinnerFillOpaqueRate.setModel(model);
			NumberEditor numberEditor = (JSpinner.NumberEditor) jSpinnerFillOpaqueRate.getEditor();
			final JTextField opaqueTextField = numberEditor.getTextField();
			opaqueTextField.addFocusListener(getSelectAllAdapter(jSpinnerFillOpaqueRate));
			opaqueTextField.addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					String rateString = opaqueTextField.getText();
					if (!isUseableNumber(rateString)) {
						return;
					}
					fillOpaque = rateString;
					int opaqueRate = Math.abs(Integer.valueOf(rateString) - 100);
					activeStyle.setFillOpaqueRate(opaqueRate);
					currentSymbolPanel.SetStyleAndRefresh(activeStyle);
				}
			});
		}
		return jSpinnerFillOpaqueRate;
	}

	/**
	 * 判断输入数字是否在1~100之间
	 *
	 * @param rateString
	 * @return
	 */
	private boolean isUseableNumber(String rateString) {
		if (rateString == null) {
			return false;
		}
		if (rateString.contains("d")) {
			return false;
		}
		try {
			Double aDouble = Double.valueOf(rateString);
		} catch (NumberFormatException e1) {
			return false;
		}
		if (rateString == null || "".equals(rateString) || fillOpaque.equals(rateString) || Double.valueOf(rateString) > 100
				|| Double.valueOf(rateString) < 0 || rateString.indexOf(".") != -1) {
			return false;
		}
		return true;
	}

	/**
	 * 初始化渐变模式选择框
	 */
	private JComboBox getSymbolFillGradientModelComboBox() {
		if (jComboBoxFillGradientMode == null) {
			jComboBoxFillGradientMode = new JComboBox();
			Enum[] enums = Enum.getEnums(FillGradientMode.class);
			for (int i = 0; i < enums.length; i++) {
				// 圆锥渐变没效果先屏蔽掉。
				if (i != 3) {
					jComboBoxFillGradientMode.addItem((FillGradientMode) enums[i]);
				}
			}
			if (activeStyle.getFillGradientMode().equals(FillGradientMode.NONE)) {
				jComboBoxFillGradientMode.setEnabled(false);
			}
			refreshFillGradientModelIcon();

			jComboBoxFillGradientMode.setPreferredSize(new Dimension(80, 22));
			jComboBoxFillGradientMode.setRenderer(new FillGradientModelRenderer());
			int selectRow = activeStyle.getFillGradientMode().value();
			if (selectRow >= 3) {
				// 圆锥已死，四角上位。
				selectRow--;
			}
			jComboBoxFillGradientMode.setSelectedIndex(selectRow);
			jComboBoxFillGradientMode.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					abstractjComboBoxFillGradientModeLisenter();
				}
			});
		}
		return jComboBoxFillGradientMode;
	}

	private void abstractjComboBoxFillGradientModeLisenter() {
		// 根据选择的不同设置，设置面的渐变模式
		int index = jComboBoxFillGradientMode.getSelectedIndex();
		if (index >= 3) {
			// 圆锥跳过了 所以加1
			index++;
		}
		String item = icons[index].getDescription();
		if (item.equals(CoreProperties.getString("String_None"))) {
			activeStyle.setFillGradientMode(FillGradientMode.NONE);
		}
		if (item.equals(CoreProperties.getString("String_CONICAL"))) {
			activeStyle.setFillGradientMode(FillGradientMode.CONICAL);
		}
		if (item.equals(CoreProperties.getString("String_LINEAR"))) {
			activeStyle.setFillGradientMode(FillGradientMode.LINEAR);
		}
		if (item.equals(CoreProperties.getString("String_RADIAL"))) {
			activeStyle.setFillGradientMode(FillGradientMode.RADIAL);
		}
		if (item.equals(CoreProperties.getString("String_SQUARE"))) {
			activeStyle.setFillGradientMode(FillGradientMode.SQUARE);
		}
		fillGradientMode = activeStyle.getFillGradientMode();
		currentSymbolPanel.SetStyleAndRefresh(activeStyle);
	}

	/**
	 * 旋转角度
	 *
	 * @return
	 */
	private JSpinner getSymbolFillGradientAngleSpinner() {
		if (jSpinnerFillGradientAngle == null) {
			jSpinnerFillGradientAngle = new JSpinner();
			jSpinnerFillGradientAngle.setToolTipText(ControlsProperties.getString("String_SymbolFillGradientAngleRange"));
			SpinnerNumberModel model = new SpinnerNumberModel(activeStyle.getFillGradientAngle(), 0, 360, 1);
			jSpinnerFillGradientAngle.setPreferredSize(new Dimension(80, 20));
			jSpinnerFillGradientAngle.setModel(model);
			if (activeStyle.getFillGradientMode().equals(FillGradientMode.NONE)) {
				jSpinnerFillGradientAngle.setEnabled(false);
			}
			NumberEditor numberEditor = (JSpinner.NumberEditor) jSpinnerFillGradientAngle.getEditor();
			final JTextField angleTextField = numberEditor.getTextField();
			// 如果初始化为1.0，实际显示的是1，鼠标点击后会变为1.0
			// 解决此问题，此处再次设置是为了让显示为1.0
			angleTextField.setText(String.valueOf(activeStyle.getFillGradientAngle()));
			numberEditor.getFormat().applyPattern("##0.0");
			angleTextField.addFocusListener(getSelectAllAdapter(jSpinnerFillGradientAngle));
			angleTextField.addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					try {
						String angleString = angleTextField.getText();
						if (angleString.contains("d")) {
							return;
						}
						try {
							Double aDouble = Double.valueOf(angleString);
						} catch (NumberFormatException e1) {
							return;
						}
						if (angleString == null || "".equals(angleString) || fillGradienAngle.equals(angleString) || Double.valueOf(angleString) > 360) {
							return;
						}
						fillGradienAngle = angleString;
						double angle = Double.parseDouble(angleString);
						activeStyle.setFillGradientAngle(angle);
						currentSymbolPanel.SetStyleAndRefresh(activeStyle);
					} catch (Exception ex) {
						Application.getActiveApplication().getOutput().output(ex);
					}
				}
			});

		}
		return jSpinnerFillGradientAngle;
	}

	/**
	 * 水平偏移
	 *
	 * @return m_fillGradientOffsetRatioXSpinner
	 */
	private JSpinner getSymbolFillGradientOffsetRatioXSpinner() {
		if (jSpinnerFillGradientOffsetRatioX == null) {
			jSpinnerFillGradientOffsetRatioX = new JSpinner();
			jSpinnerFillGradientOffsetRatioX.setToolTipText(ControlsProperties.getString("String_SymbolFillGradientOffsetRatioXRange"));
			jSpinnerFillGradientOffsetRatioX.setPreferredSize(new Dimension(40, 20));
			SpinnerNumberModel model = new SpinnerNumberModel(activeStyle.getFillGradientOffsetRatioX(), 0, 100, 1);
			jSpinnerFillGradientOffsetRatioX.setModel(model);
			if (activeStyle.getFillGradientMode().equals(FillGradientMode.NONE)) {
				jSpinnerFillGradientOffsetRatioX.setEnabled(false);
			}
			NumberEditor numberEditor = (JSpinner.NumberEditor) jSpinnerFillGradientOffsetRatioX.getEditor();
			final JTextField ratioXTextField = numberEditor.getTextField();
			// 如果初始化为1.0，实际显示的是1，鼠标点击后会变为1.0
			// 解决此问题，此处再次设置是为了让显示为1.0
			ratioXTextField.setText(String.valueOf(activeStyle.getFillGradientOffsetRatioX()));
			numberEditor.getFormat().applyPattern("##0.0");
			ratioXTextField.addFocusListener(getSelectAllAdapter(jSpinnerFillGradientOffsetRatioX));
			ratioXTextField.addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					try {

						String offsetXString = ratioXTextField.getText();
						if (offsetXString.contains("d")) {
							return;
						}
						try {
							Double aDouble = Double.valueOf(offsetXString);
						} catch (NumberFormatException e1) {
							return;
						}
						if (offsetXString == null || "".equals(offsetXString) || fillGradientX.equals(offsetXString) || Double.valueOf(offsetXString) > 100) {
							return;
						}
						fillGradientX = offsetXString;
						double offsetX = Double.parseDouble(offsetXString);
						activeStyle.setFillGradientOffsetRatioX(offsetX);
						currentSymbolPanel.SetStyleAndRefresh(activeStyle);
					} catch (Exception ex) {
						Application.getActiveApplication().getOutput().output(ex);
					}
				}

			});

		}
		return jSpinnerFillGradientOffsetRatioX;
	}

	/**
	 * 垂直偏移
	 *
	 * @return m_fillGradientOffsetRatioYSpinner
	 */
	private JSpinner getSymbolFillGradientOffsetRatioYSpinner() {
		if (jSpinnerFillGradientOffsetRatioY == null) {
			jSpinnerFillGradientOffsetRatioY = new JSpinner();
			jSpinnerFillGradientOffsetRatioY.setToolTipText(ControlsProperties.getString("String_SymbolFillGradientOffsetRatioYRange"));
			jSpinnerFillGradientOffsetRatioY.setPreferredSize(new Dimension(40, 20));
			SpinnerNumberModel model = new SpinnerNumberModel(activeStyle.getFillGradientOffsetRatioY(), 0, 100, 1);
			jSpinnerFillGradientOffsetRatioY.setModel(model);
			if (activeStyle.getFillGradientMode().equals(FillGradientMode.NONE)) {
				jSpinnerFillGradientOffsetRatioY.setEnabled(false);
			}
			NumberEditor numberEditor = (JSpinner.NumberEditor) jSpinnerFillGradientOffsetRatioY.getEditor();
			final JTextField ratioYTextField = numberEditor.getTextField();
			numberEditor.getFormat().applyPattern("##0.0");
			// 如果初始化为1.0，实际显示的是1，鼠标点击后会变为1.0
			// 解决此问题，此处再次设置是为了让显示为1.0
			ratioYTextField.setText(String.valueOf(activeStyle.getFillGradientOffsetRatioY()));
			ratioYTextField.addFocusListener(getSelectAllAdapter(jSpinnerFillGradientOffsetRatioY));
			ratioYTextField.addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					try {
						String offsetYString = ratioYTextField.getText();
						if (offsetYString.contains("d")) {
							return;
						}
						try {
							Double aDouble = Double.valueOf(offsetYString);
						} catch (NumberFormatException e1) {
							return;
						}

						if (offsetYString == null || "".equals(offsetYString) || fillGradientY.equals(offsetYString) || Double.valueOf(offsetYString) > 100) {
							return;
						}
						fillGradientY = offsetYString;
						double offsetY = Double.parseDouble(offsetYString);
						activeStyle.setFillGradientOffsetRatioY(offsetY);
						currentSymbolPanel.SetStyleAndRefresh(activeStyle);
					} catch (Exception ex) {
						Application.getActiveApplication().getOutput().output(ex);
					}
				}

			});

		}

		return jSpinnerFillGradientOffsetRatioY;

	}

	/**
	 *
	 */
	/**
	 * 向点符号操作面板中加组件
	 *
	 * @param c
	 * @param constraints
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	protected void addMarkerComponent(Component c, GridBagConstraints constraints, int x, int y, int w, int h) {
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = w;
		constraints.gridheight = h;
		add(c, constraints);
	}

	/**
	 * 向面符号操作面板中加组件
	 *
	 * @param c
	 * @param constraints
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	protected void addComponentFillBottomPanel(Component c, GridBagConstraints constraints, int x, int y, int w, int h) {
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = w;
		constraints.gridheight = h;
		jPanelFillBottom.add(c, constraints);
	}

	/**
	 * 向面符号操作面板中加组件
	 *
	 * @param c
	 * @param constraints
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	protected void addComponentFillColorPanel(Component c, GridBagConstraints constraints, int x, int y, int w, int h) {
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = w;
		constraints.gridheight = h;
		jPanelFillColor.add(c, constraints);
	}

	/**
	 * 使图像上下左右完全翻转，解决线颠倒问题
	 *
	 * @param bufferedImage
	 * @return
	 */
	private static BufferedImage flipVerticalJ2D(BufferedImage bufferedImage) {
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		BufferedImage dstImage = new BufferedImage(width, height, bufferedImage.getType());
		AffineTransform affineTransform = new AffineTransform(1, 0, 0, -1, 0, height);
		AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return affineTransformOp.filter(bufferedImage, dstImage);
	}

	class FillGradientModelRenderer extends JLabel implements ListCellRenderer {
		public FillGradientModelRenderer() {
			setOpaque(true);
			setHorizontalAlignment(JLabel.LEFT);
			setVerticalAlignment(CENTER);
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

			final int selectedIndex = ((FillGradientMode) value).value();
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			ImageIcon icon = icons[selectedIndex];
			setIcon(icon);
			setText(icon.getDescription());

			setIconTextGap(0);
			setBorder(BorderFactory.createEmptyBorder(1, 2, 2, 2));
			setBorder(BorderFactory.createLineBorder(Color.white, 1));

			return this;
		}
	}

	public String getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(String lineWidth) {
		this.lineWidth = lineWidth;
	}
}
