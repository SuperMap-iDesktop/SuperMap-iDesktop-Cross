package com.supermap.desktop.ui.controls;

import com.supermap.data.Enum;
import com.supermap.data.GeoPoint;
import com.supermap.data.GeoStyle;
import com.supermap.data.GeoText;
import com.supermap.data.Point2D;
import com.supermap.data.Rectangle2D;
import com.supermap.data.Size2D;
import com.supermap.data.TextAlignment;
import com.supermap.data.TextPart;
import com.supermap.data.TextStyle;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.layout.MapLayout;
import com.supermap.layout.MapLayoutDrawnEvent;
import com.supermap.layout.MapLayoutDrawnListener;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapDrawnEvent;
import com.supermap.mapping.MapDrawnListener;
import com.supermap.ui.Action;
import com.supermap.ui.InteractionMode;
import com.supermap.ui.MapControl;

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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.HashMap;

/**
 * 文本风格面板
 * 
 * @author xuzw modify by xie 2016-5-23
 */
public class TextStylePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel jPanelOperate;
	private JPanel jPanelView;
	private JPanel jPanelBasicFontInfo;
	private JPanel jPanelPreView;

	private JPanel jPanelFontStyleBottom;

	private JComboBox jComboBoxFontName;
	private JComboBox jComboBoxTextAlignment;
	private JComboBox jComboBoxFontSize;
	private JSpinner jSpinnerFontHeight;
	private JSpinner jSpinnerFontWidth;
	private JSpinner jSpinnerRotation;
	// private transient ControlButton jButtonBackgroundColor;
	// private transient ControlButton jButtonTextColor;
	// private transient DropDownColor jBackgroundColorDropDown;
	// private transient DropDownColor jTextColorDropDown;
	private transient ColorSelectButton buttonBackgroundColor;
	private transient ColorSelectButton buttonTextColor;

	private JCheckBox jCheckBoxBold;
	private JCheckBox jCheckBoxItalic;
	private JCheckBox jCheckBoxOutline;
	private JCheckBox jCheckBoxShadow;
	private JCheckBox jCheckBoxUnderline;
	private JCheckBox jCheckBoxStrikeout;
	private JCheckBox jCheckBoxBackOpaque;
	private JCheckBox jCheckBoxSizeFixed;

	private JPanel jPanelFontContent;
	private JPanel jPanelTextPart;
	private JPanel jPanelTextArea;
	private JComboBox jComboxContent;
	private JTextArea jTextArea;

	private JPanel jPanelForGeoText;

	private JPanel jPanelForTheme;
	private JPanel jPanelForThemeGeoText;
	private JPanel jPanelFontStyleAdvanceForTheme;
	private JSpinner jSpinnerItalicAngle;

	private JPanel jPanelForScene;
	private JPanel jPanelForSceneGeoText;
	private JPanel jPanelFontStyleAdvanceForScene;
	private JSpinner jSpinnerOpaqueRate;
	private JSpinner jSpinnerFontScale;

	private JPanel jPanelFontStyleAdvanceForSceneAndTheme;
	private JPanel jPanelForSceneAndTheme;
	private JPanel m_panelForSceneAndThemeGeoText;

	// 用户传入GeoText
	private transient GeoText tempGeoText;
	// 预览的GeoText
	private transient GeoText preViewGeoText;
	// 预览点
	private transient GeoPoint preViewGeoPoint;
	// 传入的GeoText具有的文本风格
	private transient TextStyle tempTextStyle;
	// 预览框中的MapControl
	private transient MapControl mapControl;
	// 对齐方式名称和对齐方式值构成的HashMap
	private static HashMap<String, Integer> hashMapTextAlignment = new HashMap<String, Integer>();
	// 对齐方式名称
	private static final String[] TEXTALIGNMENT_NAMES = { ControlsProperties.getString("String_TextAlignment_LeftTop"),
			ControlsProperties.getString("String_TextAlignment_MidTop"), ControlsProperties.getString("String_TextAlignment_RightTop"),
			ControlsProperties.getString("String_TextAlignment_LeftBaseline"), ControlsProperties.getString("String_TextAlignment_MidBaseline"),
			ControlsProperties.getString("String_TextAlignment_RightBaseline"), ControlsProperties.getString("String_TextAlignment_LeftBottom"),
			ControlsProperties.getString("String_TextAlignment_MidBottom"), ControlsProperties.getString("String_TextAlignment_RightBottom"),
			ControlsProperties.getString("String_TextAlignment_LeftMid"), ControlsProperties.getString("String_TextAlignment_Mid"),
			ControlsProperties.getString("String_TextAlignment_RightMid"), };
	// 默认的字号
	private final static String[] FONT_WEIGHT = { "1", "2", "3", "4", "5", "5.5", "6.5", "7.5", "8", "9", "10", "11", "12", "14", "16", "18", "20", "22", "24",
			"26", "28", "36", "48", "72" };
	// 字号框中只能填入的串
	private final static String VALID_FONTSIZE_STRING = ".0123456789";
	// 最大字高和字宽
	private final static double MAX_FONT_HEIGHT_WIDTH = Double.MAX_VALUE;
	// 单位转换
	private final static int UNIT_CONVERSION = 10;
	// 经验值
	private final static double EXPERIENCE = 0.283;
	// 判断是否是第一次显示，该变量将控制paint函数
	private boolean isInitialize = true;

	// 判断设置了TextStyle
	private boolean isOnlyTextStyle = false;
	// 判断设置了GeoText的文本风格
	private boolean isGeoTextTextStyle = false;
	// 判断设置了专题图的文本风格
	private boolean isThemeText = false;
	// 判断设置了三维场景的文本风格
	private boolean is3DText = false;
	// 地图或者是布局
	private transient Object mapObject;
	// 用于预览的示例文字
	private String sampleText;
	// 当前控件是否可编辑，false表示只能查看属性
	private boolean isEditable;

	// 是否是初始化的情况，该字段用于字号的全面处理，from .net
	private boolean isInInitialState;

	private JTextField comboBoxTextFieldFontSize;

	private String preFontSize = "";

	// private transient ColorSwatch m_colorSwatch;

	private transient MapDrawnListener mapDrawnListener = new MapDrawnListener() {
		@Override
		public void mapDrawn(MapDrawnEvent event) {
			if (!isInInitialState) {
				changeFontSizeWithMapObject();
			}
			isInInitialState = false;
		}

	};

	private transient MapLayoutDrawnListener mapLayoutDrawnListener = new MapLayoutDrawnListener() {
		@Override
		public void mapLayoutDrawn(MapLayoutDrawnEvent event) {
			if (!isInInitialState) {
				changeFontSizeWithMapObject();
			}
			isInInitialState = false;
		}

	};

	public TextStylePanel() {
		super();
		isInInitialState = true;
		sampleText = ControlsProperties.getString("String_SampleText");
	}

	public TextStylePanel(String sampleText) {
		super();
		isInInitialState = true;
		this.sampleText = sampleText;
	}

	/**
	 * 获取用户操作后的结果GeoText
	 * 
	 * @return
	 */
	public GeoText getGeoText() {
		return tempGeoText;
	}

	/**
	 * 根据用户传入GeoText的拷贝来初始化文本风格面板
	 * 
	 * @param geoText
	 */
	public void setGeoText(GeoText geoText) {
		this.tempGeoText = geoText.clone();
		tempTextStyle = tempGeoText.getTextStyle();
		this.isGeoTextTextStyle = true;
		this.initialize();
	}

	/**
	 * 获取用户操作后的结果TextStyle
	 * 
	 * @return
	 */
	public TextStyle getTextStyle() {
		return tempTextStyle;
	}

	/**
	 * 根据用户传入TextStyle的拷贝来初始化文本风格面板
	 * 
	 * @param textStyle
	 */
	public void setTextStyle(TextStyle textStyle) {
		this.tempTextStyle = textStyle.clone();
		this.isOnlyTextStyle = true;
		this.initialize();
	}

	/**
	 * 获取一个值表示是否是设置专题图的文本风格
	 * 
	 * @return
	 */
	public boolean isThemeText() {
		return isThemeText;
	}

	/**
	 * 设置一个值表示是否是设置专题图的文本风格
	 *
	 * @param value
	 */
	public void setThemeText(boolean value) {
		isThemeText = value;
		this.initialize();
		refreshPanelView();
	}

	/**
	 * 获取一个值表示是否是设置三维场景的文本风格
	 * 
	 * @return
	 */
	public boolean is3DText() {
		return is3DText;
	}

	/**
	 * 设置一个值表示是否是设置三维场景的文本风格
	 * 
	 * @param value
	 */
	public void set3DText(boolean value) {
		is3DText = value;
		this.initialize();
		refreshPanelView();
	}

	/**
	 * 获取地图或布局对象
	 * 
	 * @return
	 */
	public Object getMapObject() {
		return mapObject;
	}

	/**
	 * 设置地图或布局对象
	 *
	 * @param value
	 */
	public void setMapObject(Object value) {
		if (value != null && !value.equals(mapObject)) {

			this.removeMapDrawListener();

			mapObject = value;

			if (mapObject instanceof Map) {
				Map map = (Map) mapObject;
				if (map != null) {
					map.addDrawnListener(mapDrawnListener);
				}
			} else if (mapObject instanceof MapLayout) {
				MapLayout mapLayout = (MapLayout) mapObject;
				if (mapLayout != null) {
					mapLayout.removeDrawnListener(mapLayoutDrawnListener);
				}
			}

			onMapObjectChanged();
		}
	}

	/**
	 * 获取用于预览的文字
	 * 
	 * @return
	 */
	public String getSampleText() {
		return sampleText;
	}

	/**
	 * 设置用于预览的文字
	 * 
	 * @param text
	 */
	public void setSampleText(String text) {
		sampleText = text;
	}

	/**
	 * 返回当前控件是否可编辑，false表示只能查看属性
	 * 
	 * @return
	 */
	public boolean getEditable() {
		return isEditable;
	}

	/**
	 * 设置当前控件是否可编辑，false表示只能查看属性
	 * 
	 * @param value
	 */
	public void setEditable(boolean value) {
		getComboBoxFontName().setEnabled(value);
		getComboBoxTextAlignment().setEnabled(value);
		getComboBoxFontSize().setEnabled(value);
		getSpinnerFontHeight().setEnabled(value);
		if (!value) {
			getSpinnerFontWidth().setEnabled(value);
		} else {
			getSpinnerFontWidth().setEnabled(!getCheckBoxSizeFixed().isSelected());
		}
		getButtonTextColor().setEnabled(value);
		if (!value) {
			getButtonBackgroundColor().setEnabled(value);
		} else {
			getButtonBackgroundColor().setEnabled(!getCheckBoxBackOpaque().isSelected());
		}
		getSpinnerRotation().setEnabled(value);

		getCheckBoxBackOpaque().setEnabled(value);
		getCheckBoxBold().setEnabled(value);
		getCheckBoxItalic().setEnabled(value);
		getCheckBoxOutline().setEnabled(value);
		getCheckBoxShadow().setEnabled(value);
		getCheckBoxSizeFixed().setEnabled(value);
		getCheckBoxStrikeout().setEnabled(value);
		getCheckBoxUnderline().setEnabled(value);

		getTextArea().setEnabled(value);
		getSpinnerItalicAngle().setEnabled(value);
		getSpinnerOpaqueRate().setEnabled(value);
		getSpinnerFontScale().setEnabled(value);
		isEditable = value;
	}

	public void removeMapDrawListener() {
		if (mapObject != null) {
			if (mapObject instanceof Map) {
				Map map = (Map) mapObject;
				if (map != null) {
					map.removeDrawnListener(mapDrawnListener);
				}
			} else if (mapObject instanceof MapLayout) {
				MapLayout mapLayout = (MapLayout) mapObject;
				if (mapLayout != null) {
					mapLayout.removeDrawnListener(mapLayoutDrawnListener);
				}
			}
		}
	}

	/**
	 * 初始化面板
	 */
	protected void initialize() {
		int[] textAlignmentValues = Enum.getValues(TextAlignment.class);
		for (int i = 0; i < textAlignmentValues.length; i++) {
			hashMapTextAlignment.put(TEXTALIGNMENT_NAMES[i], textAlignmentValues[i]);
		}
		setLayout(new GridLayout(0, 2));

		add(getPanelView());
		add(getPanelOperate());
	}

	/**
	 * 获取展示面板
	 * 
	 * @return
	 */
	private JPanel getPanelView() {
		if (jPanelView == null) {
			jPanelView = new JPanel();
			jPanelView.setLayout(new BorderLayout());
			jPanelView.add(getPanelPreView(), BorderLayout.CENTER);
			if (isOnlyTextStyle) {
				jPanelView.add(getPanelFontStyleBottom(), BorderLayout.SOUTH);
			}
			if (isGeoTextTextStyle) {
				jPanelView.add(getPanelForGeoText(), BorderLayout.SOUTH);
			}
		}

		return jPanelView;
	}

	/**
	 * 获取GeoText的展示面板，增加一个子对象标签页
	 * 
	 * @return
	 */
	private JPanel getPanelForGeoText() {
		if (jPanelForGeoText == null) {
			jPanelForGeoText = new JPanel();
			jPanelForGeoText.setLayout(new BorderLayout());
			JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.addTab(ControlsProperties.getString("String_FontEffect"), getPanelFontStyleBottom());
			tabbedPane.addTab(ControlsProperties.getString("String_SubObject"), getPanelFontContent());
			jPanelForGeoText.add(tabbedPane, BorderLayout.CENTER);
		}
		return jPanelForGeoText;
	}

	/**
	 * 获取GeoText的专题图展示面板，增加一个高级标签页
	 * 
	 * @return
	 */
	private JPanel getPanelForThemeGeoText() {
		if (jPanelForThemeGeoText == null) {
			jPanelForThemeGeoText = new JPanel();
			jPanelForThemeGeoText.setLayout(new BorderLayout());
			JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.addTab(ControlsProperties.getString("String_FontEffect"), getPanelFontStyleBottom());
			tabbedPane.addTab(ControlsProperties.getString("String_SubObject"), getPanelFontContent());
			tabbedPane.addTab(ControlsProperties.getString("String_Senior"), getPanelFontStyleAdvanceForTheme());
			jPanelForThemeGeoText.add(tabbedPane, BorderLayout.CENTER);
		}
		return jPanelForThemeGeoText;
	}

	/**
	 * 获取TextStyle的专题图展示面板
	 * 
	 * @return
	 */
	private JPanel getPanelForTheme() {
		if (jPanelForTheme == null) {
			jPanelForTheme = new JPanel();
			jPanelForTheme.setLayout(new BorderLayout());
			JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.addTab(ControlsProperties.getString("String_SubObject"), getPanelFontStyleBottom());
			tabbedPane.addTab(ControlsProperties.getString("String_Senior"), getPanelFontStyleAdvanceForTheme());
			jPanelForTheme.add(tabbedPane, BorderLayout.CENTER);
		}
		return jPanelForTheme;
	}

	/**
	 * 获取专题图文本风格高级设置页标签面板
	 * 
	 * @return
	 */
	private JPanel getPanelFontStyleAdvanceForTheme() {
		if (jPanelFontStyleAdvanceForTheme == null) {
			jPanelFontStyleAdvanceForTheme = new JPanel();
			jPanelFontStyleAdvanceForTheme.setBorder(BorderFactory.createEmptyBorder(3, 5, 0, 0));
			GridBagLayout layout = new GridBagLayout();
			jPanelFontStyleAdvanceForTheme.setLayout(layout);

			JLabel label = new JLabel(ControlsProperties.getString("String_ItalicAngle"));
			GridBagConstraints constraints = getGridBagConstraints();
			addComponentToPanel(jPanelFontStyleAdvanceForTheme, label, constraints, 0, 0, 1, 1);
			addComponentToPanel(jPanelFontStyleAdvanceForTheme, getSpinnerItalicAngle(), constraints, 1, 0, 1, 1);
		}
		return jPanelFontStyleAdvanceForTheme;
	}

	/**
	 * 获取三维场景GeoText文本风格面板
	 * 
	 * @return
	 */
	private JPanel getPanelForSceneGeoText() {
		if (jPanelForSceneGeoText == null) {
			jPanelForSceneGeoText = new JPanel();
			jPanelForSceneGeoText.setLayout(new BorderLayout());
			JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.addTab(ControlsProperties.getString("String_FontEffect"), getPanelFontStyleBottom());
			tabbedPane.addTab(ControlsProperties.getString("String_SubObject"), getPanelFontContent());
			tabbedPane.addTab(ControlsProperties.getString("String_Senior"), getPanelFontStyleAdvanceForScene());
			jPanelForSceneGeoText.add(tabbedPane, BorderLayout.CENTER);
		}
		return jPanelForSceneGeoText;
	}

	/**
	 * 获取三维场景TextStyle文本风格面板
	 * 
	 * @return
	 */
	private JPanel getPanelForScene() {
		if (jPanelForScene == null) {
			jPanelForScene = new JPanel();
			jPanelForScene.setLayout(new BorderLayout());
			JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.addTab(ControlsProperties.getString("String_FontEffect"), getPanelFontStyleBottom());
			tabbedPane.addTab(ControlsProperties.getString("String_Senior"), getPanelFontStyleAdvanceForScene());
			jPanelForScene.add(tabbedPane, BorderLayout.CENTER);
		}
		return jPanelForScene;
	}

	/**
	 * 获取三维场景风格高级设置面板
	 * 
	 * @return
	 */
	private JPanel getPanelFontStyleAdvanceForScene() {
		if (jPanelFontStyleAdvanceForScene == null) {
			jPanelFontStyleAdvanceForScene = new JPanel();
			jPanelFontStyleAdvanceForScene.setBorder(BorderFactory.createEmptyBorder(3, 5, 0, 0));
			GridBagLayout layout = new GridBagLayout();
			jPanelFontStyleAdvanceForScene.setLayout(layout);

			JLabel labelOpaqueRate = new JLabel(ControlsProperties.getString("String_Opaqueness"));
			JLabel labelFontScale = new JLabel(ControlsProperties.getString("String_TextScaling"));
			GridBagConstraints constraints = getGridBagConstraints();
			addComponentToPanel(jPanelFontStyleAdvanceForScene, labelOpaqueRate, constraints, 0, 0, 1, 1);
			addComponentToPanel(jPanelFontStyleAdvanceForScene, labelFontScale, constraints, 0, 1, 1, 1);
			addComponentToPanel(jPanelFontStyleAdvanceForScene, getSpinnerOpaqueRate(), constraints, 1, 0, 1, 1);
			addComponentToPanel(jPanelFontStyleAdvanceForScene, getSpinnerFontScale(), constraints, 1, 1, 1, 1);
		}
		return jPanelFontStyleAdvanceForScene;
	}

	/**
	 * 获取场景和专题图的GeoText面板
	 * 
	 * @return
	 */
	private JPanel getPanelForSceneAndThemeGeoText() {
		if (m_panelForSceneAndThemeGeoText == null) {
			m_panelForSceneAndThemeGeoText = new JPanel();
			m_panelForSceneAndThemeGeoText.setLayout(new BorderLayout());
			JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.addTab(ControlsProperties.getString("String_FontEffect"), getPanelFontStyleBottom());
			tabbedPane.addTab(ControlsProperties.getString("String_SubObject"), getPanelFontContent());
			tabbedPane.addTab(ControlsProperties.getString("String_Senior"), getPanelFontStyleAdvanceForScene());
			m_panelForSceneAndThemeGeoText.add(tabbedPane, BorderLayout.CENTER);
		}
		return m_panelForSceneAndThemeGeoText;
	}

	/**
	 * 获取场景和专题图的TextStyle面板
	 * 
	 * @return
	 */
	private JPanel getPanelForSceneAndTheme() {
		if (jPanelForSceneAndTheme == null) {
			jPanelForSceneAndTheme = new JPanel();
			jPanelForSceneAndTheme.setLayout(new BorderLayout());
			JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.addTab(ControlsProperties.getString("String_FontEffect"), getPanelFontStyleBottom());
			tabbedPane.addTab(ControlsProperties.getString("String_Senior"), getPanelFontStyleAdvanceForSceneAndTheme());
			jPanelForSceneAndTheme.add(tabbedPane, BorderLayout.CENTER);
		}
		return jPanelForSceneAndTheme;
	}

	/**
	 * 获取场景和专题图高级设置页标签面板
	 * 
	 * @return
	 */
	private JPanel getPanelFontStyleAdvanceForSceneAndTheme() {
		if (jPanelFontStyleAdvanceForSceneAndTheme == null) {
			jPanelFontStyleAdvanceForSceneAndTheme = new JPanel();
			jPanelFontStyleAdvanceForSceneAndTheme.setBorder(BorderFactory.createEmptyBorder(3, 5, 0, 0));
			GridBagLayout layout = new GridBagLayout();
			jPanelFontStyleAdvanceForSceneAndTheme.setLayout(layout);

			JLabel label = new JLabel(ControlsProperties.getString("String_ItalicAngle"));
			JLabel labelOpaqueRate = new JLabel(ControlsProperties.getString("String_Opaqueness"));
			JLabel labelFontScale = new JLabel(ControlsProperties.getString("String_TextScaling"));
			GridBagConstraints constraints = getGridBagConstraints();
			addComponentToPanel(jPanelFontStyleAdvanceForSceneAndTheme, label, constraints, 0, 0, 1, 1);
			addComponentToPanel(jPanelFontStyleAdvanceForSceneAndTheme, labelOpaqueRate, constraints, 0, 1, 1, 1);
			addComponentToPanel(jPanelFontStyleAdvanceForSceneAndTheme, labelFontScale, constraints, 0, 2, 1, 1);
			addComponentToPanel(jPanelFontStyleAdvanceForSceneAndTheme, getSpinnerItalicAngle(), constraints, 1, 0, 1, 1);
			addComponentToPanel(jPanelFontStyleAdvanceForSceneAndTheme, getSpinnerOpaqueRate(), constraints, 1, 1, 1, 1);
			addComponentToPanel(jPanelFontStyleAdvanceForSceneAndTheme, getSpinnerFontScale(), constraints, 1, 2, 1, 1);
		}
		return jPanelFontStyleAdvanceForSceneAndTheme;
	}

	/**
	 * 获取字体倾斜角度
	 * 
	 * @return
	 */
	protected JSpinner getSpinnerItalicAngle() {
		if (jSpinnerItalicAngle == null) {
			SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(tempTextStyle.getItalicAngle(), -60.0, 60.0, 1.0);
			jSpinnerItalicAngle = new JSpinner(spinnerNumberModel);
			jSpinnerItalicAngle.setPreferredSize(new Dimension(150, 28));
			final NumberEditor numberEditor = (JSpinner.NumberEditor) jSpinnerItalicAngle.getEditor();
			numberEditor.getTextField().setHorizontalAlignment(JTextField.LEFT);
			numberEditor.getTextField().addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					abstractnumberEditorLisenter(numberEditor);
				}
			});
			numberEditor.getTextField().addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					try {
						Double italicAngle = Double.valueOf(numberEditor.getTextField().getText());
						if (Double.compare(italicAngle, 60.0) > 0) {
							numberEditor.getTextField().setText("60.0");
						}
						if (Double.compare(italicAngle, -60.0) < 0) {
							numberEditor.getTextField().setText("-60.0");
						}
					} catch (Exception ex) {
						Application.getActiveApplication().getOutput().output(ex);
					}
				}

			});
		}
		return jSpinnerItalicAngle;
	}

	private void abstractnumberEditorLisenter(final NumberEditor numberEditor) {
		if (tempTextStyle != null && !isInInitialState) {
			String value = numberEditor.getTextField().getText();
			if (value == null || "".equals(value)) {
				return;
			}
			try {
				tempTextStyle.setItalicAngle(Double.parseDouble(value));
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
			refreshPreViewMapControl();
		}
	}

	/**
	 * 获取不透明度
	 * 
	 * @return
	 */
	protected JSpinner getSpinnerOpaqueRate() {
		if (jSpinnerOpaqueRate == null) {
			SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(tempTextStyle.getOpaqueRate(), 0, 100, 1);
			jSpinnerOpaqueRate = new JSpinner(spinnerNumberModel);
			jSpinnerOpaqueRate.setPreferredSize(new Dimension(150, 28));
			final NumberEditor numberEditor = (JSpinner.NumberEditor) jSpinnerOpaqueRate.getEditor();
			numberEditor.getTextField().setHorizontalAlignment(JTextField.LEFT);
			numberEditor.getTextField().addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					if (tempTextStyle != null && !isInInitialState) {
						String value = numberEditor.getTextField().getText();
						if (value == null || "".equals(value)) {
							return;
						}
						try {
							tempTextStyle.setRotation(Double.valueOf(value));
						} catch (Exception ex) {
							Application.getActiveApplication().getOutput().output(ex);
						}
						refreshPreViewMapControl();
					}
				}
			});
			numberEditor.getTextField().addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					try {
						Integer opaqueRate = Integer.valueOf(numberEditor.getTextField().getText());
						if (opaqueRate > 100) {
							numberEditor.getTextField().setText("100");
						}
						if (opaqueRate < 0) {
							numberEditor.getTextField().setText("0");
						}
					} catch (Exception ex) {
						Application.getActiveApplication().getOutput().output(ex);
					}
				}

			});
		}
		return jSpinnerOpaqueRate;
	}

	/**
	 * 获取文字缩放比
	 * 
	 * @return
	 */
	protected JSpinner getSpinnerFontScale() {
		if (jSpinnerFontScale == null) {
			SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(tempTextStyle.getFontScale(), 0.01, 5.00, 0.1);
			jSpinnerFontScale = new JSpinner(spinnerNumberModel);
			jSpinnerFontScale.setPreferredSize(new Dimension(150, 28));
			final NumberEditor numberEditor = (JSpinner.NumberEditor) jSpinnerFontScale.getEditor();
			numberEditor.getTextField().setHorizontalAlignment(JTextField.LEFT);
			numberEditor.getTextField().addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					if (tempTextStyle != null && !isInInitialState) {
						String value = numberEditor.getTextField().getText();
						if (value == null || "".equals(value)) {
							return;
						}
						try {
							tempTextStyle.setRotation(Double.valueOf(value));
						} catch (Exception ex) {
							Application.getActiveApplication().getOutput().output(ex);
						}
						refreshPreViewMapControl();
					}
				}
			});
			numberEditor.getTextField().addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					try {
						Double fontScale = Double.valueOf(numberEditor.getTextField().getText());
						if (Double.compare(fontScale, 5.00) > 0) {
							numberEditor.getTextField().setText("5.00");
						}
						if (Double.compare(fontScale, 0.01) < 0) {
							numberEditor.getTextField().setText("0.01");
						}
					} catch (Exception ex) {
						Application.getActiveApplication().getOutput().output(ex);
					}
				}

			});
		}
		return jSpinnerFontScale;
	}

	/**
	 * 获取操作面板
	 * 
	 * @return
	 */
	private JPanel getPanelOperate() {
		if (jPanelOperate == null) {
			jPanelOperate = new JPanel(new BorderLayout());
			jPanelOperate.add(getPanelBasicFontInfo(), BorderLayout.CENTER);
		}
		return jPanelOperate;
	}

	/**
	 * 获取字体基础信息面板
	 * 
	 * @return
	 */
	protected JPanel getPanelBasicFontInfo() {
		if (jPanelBasicFontInfo == null) {
			jPanelBasicFontInfo = new JPanel();
			jPanelBasicFontInfo.setBorder(BorderFactory.createEmptyBorder(3, 5, 0, 0));
			GridBagLayout layout = new GridBagLayout();
			jPanelBasicFontInfo.setLayout(layout);
			GridBagConstraints constraints = getGridBagConstraints();

			JLabel labelFontName = new JLabel(ControlsProperties.getString("String_FontName"));
			JLabel labelTextAlignment = new JLabel(ControlsProperties.getString("String_AlignmentMethod"));
			JLabel labelWeight = new JLabel(ControlsProperties.getString("String_WordSize"));
			JLabel labelFontHeight = new JLabel(ControlsProperties.getString("String_FontHeight"));
			JLabel labelWidth = new JLabel(ControlsProperties.getString("String_FontWidth"));
			JLabel labelTextColor = new JLabel(ControlsProperties.getString("String_TextColor"));
			JLabel labelBackgroundColor = new JLabel(ControlsProperties.getString("String_BackgroundColor"));
			JLabel labelRotation = new JLabel(ControlsProperties.getString("String_RotationAngle"));

			addComponentToPanel(jPanelBasicFontInfo, labelFontName, constraints, 0, 0, 1, 1);
			addComponentToPanel(jPanelBasicFontInfo, labelTextAlignment, constraints, 0, 1, 1, 1);
			addComponentToPanel(jPanelBasicFontInfo, labelWeight, constraints, 0, 2, 1, 1);
			addComponentToPanel(jPanelBasicFontInfo, labelFontHeight, constraints, 0, 3, 1, 1);

			addComponentToPanel(jPanelBasicFontInfo, labelWidth, constraints, 0, 4, 1, 1);
			addComponentToPanel(jPanelBasicFontInfo, labelTextColor, constraints, 0, 5, 1, 1);
			addComponentToPanel(jPanelBasicFontInfo, labelBackgroundColor, constraints, 0, 6, 1, 1);
			addComponentToPanel(jPanelBasicFontInfo, labelRotation, constraints, 0, 7, 1, 1);

			addComponentToPanel(jPanelBasicFontInfo, getComboBoxFontName(), constraints, 1, 0, 1, 1);
			addComponentToPanel(jPanelBasicFontInfo, getComboBoxTextAlignment(), constraints, 1, 1, 1, 1);
			addComponentToPanel(jPanelBasicFontInfo, getComboBoxFontSize(), constraints, 1, 2, 1, 1);
			addComponentToPanel(jPanelBasicFontInfo, getSpinnerFontHeight(), constraints, 1, 3, 1, 1);

			addComponentToPanel(jPanelBasicFontInfo, getSpinnerFontWidth(), constraints, 1, 4, 1, 1);
			addComponentToPanel(jPanelBasicFontInfo, getButtonTextColor(), constraints, 1, 5, 1, 1);
			addComponentToPanel(jPanelBasicFontInfo, getButtonBackgroundColor(), constraints, 1, 6, 1, 1);
			addComponentToPanel(jPanelBasicFontInfo, getSpinnerRotation(), constraints, 1, 7, 1, 1);

		}
		return jPanelBasicFontInfo;
	}

	/**
	 * 获取字体名
	 * 
	 * @return
	 */
	private JComboBox getComboBoxFontName() {
		if (jComboBoxFontName == null) {
			jComboBoxFontName = new FontComboBox();
			jComboBoxFontName.setPreferredSize(new Dimension(150, 28));
			jComboBoxFontName.setMinimumSize(new Dimension(150, 28));
			jComboBoxFontName.setSelectedItem(tempTextStyle.getFontName());
			jComboBoxFontName.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (tempTextStyle != null && !isInInitialState) {
						tempTextStyle.setFontName(jComboBoxFontName.getSelectedItem().toString());
						refreshPreViewMapControl();
					}
				}
			});
		}
		return jComboBoxFontName;
	}

	/**
	 * 获取字体对齐方式
	 * 
	 * @return
	 */
	protected JComboBox getComboBoxTextAlignment() {
		if (jComboBoxTextAlignment == null) {
			jComboBoxTextAlignment = new JComboBox(TEXTALIGNMENT_NAMES);
			jComboBoxTextAlignment.setPreferredSize(new Dimension(150, 28));
			jComboBoxTextAlignment.setMinimumSize(new Dimension(150, 28));
			String name = ControlsProperties.getString("String_TopLeftCorner");
			Object[] textAlignmentValues = hashMapTextAlignment.values().toArray();
			Object[] textAlignmentNames = hashMapTextAlignment.keySet().toArray();
			for (int i = 0; i < textAlignmentValues.length; i++) {
				Integer temp = (Integer) textAlignmentValues[i];
				if (temp == tempTextStyle.getAlignment().value()) {
					name = (String) textAlignmentNames[i];
				}
			}
			jComboBoxTextAlignment.setSelectedItem(name);
			jComboBoxTextAlignment.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (tempTextStyle != null && !isInInitialState) {
						String textAlignmentName = jComboBoxTextAlignment.getSelectedItem().toString();
						int value = hashMapTextAlignment.get(textAlignmentName);
						TextAlignment textAlignment = (TextAlignment) Enum.parse(TextAlignment.class, value);
						tempTextStyle.setAlignment(textAlignment);
						refreshPreViewMapControl();
					}
				}

			});

		}
		return jComboBoxTextAlignment;
	}

	/**
	 * 获取字号
	 * 
	 * @return
	 */
	protected JComboBox getComboBoxFontSize() {
		if (jComboBoxFontSize == null) {
			jComboBoxFontSize = new JComboBox(FONT_WEIGHT);
			jComboBoxFontSize.setPreferredSize(new Dimension(150, 28));
			jComboBoxFontSize.setMinimumSize(new Dimension(150, 28));
			jComboBoxFontSize.setEditable(true);
			comboBoxTextFieldFontSize = (JTextField) jComboBoxFontSize.getEditor().getEditorComponent();

			jComboBoxFontSize.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					abstractjComboBoxFontSizeLisenter();
				}
			});

			// 注意JComboBox是组合控件，它不具备格式化输入的功能，直接加键盘监听也无效
			// 取JComboBox的第三个组成部分（MetalComboBoxEditor索引为2）加键盘监听
			jComboBoxFontSize.getComponent(2).addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					abstractjComboBoxFontSizeLisenter(e);
				}
			});

		}
		return jComboBoxFontSize;
	}

	private void abstractjComboBoxFontSizeLisenter() {
		try {
			if (tempTextStyle != null && !isInInitialState) {
				double size = 0.0;
				try {
					size = Double.valueOf(comboBoxTextFieldFontSize.getText());
				} catch (Exception ex) {
					Application.getActiveApplication().getOutput().output(ex);
				}

				// 解决字号和高度转换时小数点精度变化问题
				if (preFontSize.equals(comboBoxTextFieldFontSize.getText())) {
					return;
				}
				// 记录JSpinner值的改变
				preFontSize = comboBoxTextFieldFontSize.getText();

				isInInitialState = true;
				double fontHeight = fontSizeToMapHeight(size, mapObject, tempTextStyle.isSizeFixed());
				if (Double.doubleToRawLongBits(fontHeight) != 0) {
					tempTextStyle.setFontHeight(fontHeight / UNIT_CONVERSION);
					DecimalFormat decimalFormat = new DecimalFormat("#.00");
					String temp = decimalFormat.format(size / EXPERIENCE);
					jSpinnerFontHeight.setValue(Double.valueOf(temp));

					jSpinnerFontWidth.setValue(0.0);
					tempTextStyle.setFontWidth(0);

					refreshPreViewMapControl();
				}
				isInInitialState = false;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		isInInitialState = false;
	}

	private void abstractjComboBoxFontSizeLisenter(KeyEvent e) {
		try {
			// 按键之前JCombobox中的字符串
			String oldString = jComboBoxFontSize.getEditor().getItem().toString();
			// 用户按了"动作键"应该不弹出对话框，
			// 但是在JComboBox中ENTER，BACKSPAC，SPACE，SHIFT都被认为是非"动作键"，
			// Core2Java中介绍的不是这样，可能JComboBox较为特殊
			if (e.isActionKey() || e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_SPACE
					|| e.getKeyCode() == KeyEvent.VK_SHIFT) {

				return;
			}
			String currentString = oldString + e.getKeyChar();
			if (VALID_FONTSIZE_STRING.indexOf(e.getKeyChar()) == -1) {
				jComboBoxFontSize.getEditor().setItem(oldString);
				currentString = oldString;
			}
			// 用户输入的情况下获取当前JCombobox中的String，包括之前编辑好的和当前输入的
			// 如果"."在字符串的第一次出现位置和最后一次不一样，说明有两个以上的"."
			if (currentString.indexOf(".") != currentString.lastIndexOf(".")) {
				jComboBoxFontSize.getEditor().setItem(oldString);
				currentString = oldString;
			}

			refreshPreViewMapControl();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 获取字体高度
	 * 
	 * @return
	 */
	protected JSpinner getSpinnerFontHeight() {
		if (jSpinnerFontHeight == null) {
			SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(tempTextStyle.getFontHeight(), 0, MAX_FONT_HEIGHT_WIDTH, 1.0);
			jSpinnerFontHeight = new JSpinner(spinnerNumberModel);
			jSpinnerFontHeight.setPreferredSize(new Dimension(150, 28));
			jSpinnerFontHeight.setMinimumSize(new Dimension(150, 28));
			final NumberEditor numberEditor = (JSpinner.NumberEditor) jSpinnerFontHeight.getEditor();
			numberEditor.getTextField().setHorizontalAlignment(JTextField.LEFT);
			numberEditor.getTextField().addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					if (tempTextStyle != null && !isInInitialState) {
						double logicalHeight = 0.0;
						String value = numberEditor.getTextField().getText();
						if (value == null || "".equals(value)) {
							return;
						}
						try {
							logicalHeight = Double.valueOf(value);
							if (Double.doubleToRawLongBits(logicalHeight) != 0) {
								Double size = logicalHeight * EXPERIENCE;
								DecimalFormat decimalFormat = new DecimalFormat("#0.0");
								size = Double.valueOf(decimalFormat.format(size));
								preFontSize = decimalFormat.format(size);
								if (Double.compare(size, size.intValue()) == 0) {
									comboBoxTextFieldFontSize.setText(String.valueOf(size.intValue()));
								} else {
									comboBoxTextFieldFontSize.setText(decimalFormat.format(size));
								}
								isInInitialState = false;

								double fontHeight = fontSizeToMapHeight(size, mapObject, tempTextStyle.isSizeFixed());
								if (fontHeight > 0) {
									tempTextStyle.setFontHeight(fontHeight / UNIT_CONVERSION);
								}
							} else {
								double size = mapHeightToFontSize(tempTextStyle.getFontHeight() * UNIT_CONVERSION, mapObject, tempTextStyle.isSizeFixed());
								DecimalFormat decimalFormat = new DecimalFormat("#.00");
								final String temp = decimalFormat.format(size * UNIT_CONVERSION / EXPERIENCE);
								// add by xuzw 2010-10-25
								// 因为高度不能为0，所以如果为0的话需要通过字号算一次，然后再重新赋值，
								// m_spinnerFontHeight的值此时已经被锁住了，所以采用invokeLater
								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {
										jSpinnerFontHeight.setValue(Double.valueOf(temp));
									}

								});

							}
						} catch (Exception ex) {
							Application.getActiveApplication().getOutput().output(ex);
						}
						refreshPreViewMapControl();
					}
				}
			});

		}
		return jSpinnerFontHeight;
	}

	/**
	 * 获取字体宽度
	 * 
	 * @return
	 */
	protected JSpinner getSpinnerFontWidth() {
		if (jSpinnerFontWidth == null) {
			SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(tempTextStyle.getFontWidth(), 0.0, MAX_FONT_HEIGHT_WIDTH, 1.0);
			jSpinnerFontWidth = new JSpinner(spinnerNumberModel);
			jSpinnerFontWidth.setPreferredSize(new Dimension(150, 28));
			jSpinnerFontWidth.setMinimumSize(new Dimension(150, 28));
			jSpinnerFontWidth.setEnabled(!getCheckBoxSizeFixed().isSelected());
			final NumberEditor numberEditor = (JSpinner.NumberEditor) jSpinnerFontWidth.getEditor();
			numberEditor.getTextField().setHorizontalAlignment(JTextField.LEFT);
			numberEditor.getTextField().addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					if (tempTextStyle != null && !isInInitialState) {
						String value = numberEditor.getTextField().getText();
						if (value == null || "".equals(value)) {
							return;
						}
						try {
							double logicalWidth = Double.parseDouble(value);
							double fontWidth = fontWidthToMapWidth(logicalWidth, mapObject, tempTextStyle.isSizeFixed());
							tempTextStyle.setFontWidth(fontWidth / UNIT_CONVERSION);
						} catch (Exception ex) {
							Application.getActiveApplication().getOutput().output(ex);
						}
						refreshPreViewMapControl();
					}
				}
			});

		}
		return jSpinnerFontWidth;
	}

	/**
	 * 获取文本颜色对话框
	 * 
	 * @return
	 */
	protected ColorSelectButton getButtonTextColor() {
		// if (jButtonTextColor == null) {
		// jButtonTextColor = new ControlButton();
		// jButtonTextColor.setPreferredSize(new Dimension(134, 28));
		// final ColorSwatch colorSwatch = new ColorSwatch(tempTextStyle.getForeColor(), 20, 122);
		// jButtonTextColor.setIcon(colorSwatch);
		//
		// jTextColorDropDown = new DropDownColor(jButtonTextColor);
		// // 这里把期望宽度设置的略大一些，保证在网格中显示正确
		// jTextColorDropDown.setPreferredSize(new Dimension(150, 33));
		// jTextColorDropDown.addPropertyChangeListener("m_selectionColors", new PropertyChangeListener() {
		// @Override
		// public void propertyChange(PropertyChangeEvent evt) {
		// if (tempTextStyle != null && !isInInitialState) {
		// Color color = jTextColorDropDown.getColor();
		// if (color != null) {
		// tempTextStyle.setForeColor(color);
		// colorSwatch.setColor(color);
		// jButtonTextColor.repaint();
		// refreshPreViewMapControl();
		// }
		// }
		// }
		// });
		// }
		if (null == buttonTextColor && isOnlyTextStyle) {
			buttonTextColor = new ColorSelectButton(getTextStyle().getForeColor());
			buttonTextColor.setPreferredSize(new Dimension(150, 28));
			buttonTextColor.addPropertyChangeListener("m_selectionColors", new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					Color color = buttonTextColor.getColor();
					if (null != color) {
						tempTextStyle.setForeColor(color);
						refreshPreViewMapControl();
					}
				}
			});
		}
		if (null == buttonTextColor && isGeoTextTextStyle) {
			buttonTextColor = new ColorSelectButton(getGeoText().getTextStyle().getForeColor());
			buttonTextColor.setPreferredSize(new Dimension(150, 28));
			buttonTextColor.addPropertyChangeListener("m_selectionColors", new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					Color color = buttonTextColor.getColor();
					if (null != color) {
						tempGeoText.getTextStyle().setForeColor(color);
						refreshPreViewMapControl();
					}
				}
			});
		}
		return buttonTextColor;
	}

	/**
	 * 获取背景颜色对话框
	 * 
	 * @return
	 */
	protected ColorSelectButton getButtonBackgroundColor() {
		// if (jButtonBackgroundColor == null) {
		// jButtonBackgroundColor = new ControlButton();
		// jButtonBackgroundColor.setPreferredSize(new Dimension(134, 28));
		// jButtonBackgroundColor.setEnabled(!getCheckBoxBackOpaque().isSelected());
		// m_colorSwatch = new ColorSwatch(tempTextStyle.getBackColor(), 20, 122);
		// if (!tempTextStyle.getBackOpaque()) {
		// jButtonBackgroundColor.setIcon(null);
		// } else {
		// jButtonBackgroundColor.setIcon(m_colorSwatch);
		// }
		// jBackgroundColorDropDown = new DropDownColor(jButtonBackgroundColor);
		// jBackgroundColorDropDown.getArrowButton().setEnabled(!getCheckBoxBackOpaque().isSelected());
		// // 这里把期望宽度设置的略大一些，保证在网格中显示正确
		// jBackgroundColorDropDown.setPreferredSize(new Dimension(150, 33));
		// jBackgroundColorDropDown.addPropertyChangeListener("m_selectionColors", new PropertyChangeListener() {
		// @Override
		// public void propertyChange(PropertyChangeEvent evt) {
		// if (tempTextStyle != null && !isInInitialState) {
		// Color color = jBackgroundColorDropDown.getColor();
		// if (color != null) {
		// tempTextStyle.setBackColor(color);
		// m_colorSwatch.setColor(color);
		// jButtonBackgroundColor.repaint();
		// refreshPreViewMapControl();
		// }
		// }
		// }
		// });
		// }
		if (null == buttonBackgroundColor && isOnlyTextStyle) {
			buttonBackgroundColor = new ColorSelectButton(getTextStyle().getBackColor());
			buttonBackgroundColor.setPreferredSize(new Dimension(150, 28));
			buttonBackgroundColor.addPropertyChangeListener("m_selectionColors", new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					Color color = buttonBackgroundColor.getColor();
					if (null != color) {
						tempTextStyle.setBackColor(color);
						refreshPreViewMapControl();
					}
				}
			});
		}
		if (null == buttonBackgroundColor && isGeoTextTextStyle) {
			buttonBackgroundColor = new ColorSelectButton(getGeoText().getTextStyle().getBackColor());
			buttonBackgroundColor.setPreferredSize(new Dimension(150, 28));
			buttonBackgroundColor.addPropertyChangeListener("m_selectionColors", new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					Color color = buttonBackgroundColor.getColor();
					if (null != color) {
						tempGeoText.getTextStyle().setBackColor(color);
						refreshPreViewMapControl();
					}
				}
			});
		}
		return buttonBackgroundColor;
	}

	/**
	 * 获取旋转角度
	 * 
	 * @return
	 */
	protected JSpinner getSpinnerRotation() {
		if (jSpinnerRotation == null) {
			SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(tempTextStyle.getRotation(), 0, 360.0, 1.0);
			jSpinnerRotation = new JSpinner(spinnerNumberModel);
			jSpinnerRotation.setPreferredSize(new Dimension(150, 28));
			jSpinnerRotation.setMinimumSize(new Dimension(150, 28));

			final NumberEditor numberEditor = (JSpinner.NumberEditor) jSpinnerRotation.getEditor();
			numberEditor.getTextField().setHorizontalAlignment(JTextField.LEFT);
			numberEditor.getTextField().addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					if (tempTextStyle != null && !isInInitialState) {
						String value = numberEditor.getTextField().getText();
						if (value == null || "".equals(value)) {
							return;
						}
						try {
							tempTextStyle.setRotation(Double.valueOf(value));
						} catch (Exception ex) {
							Application.getActiveApplication().getOutput().output(ex);
						}
						refreshPreViewMapControl();
					}
				}
			});
			numberEditor.getTextField().addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					try {
						Double rotation = Double.valueOf(numberEditor.getTextField().getText());
						if (Double.compare(rotation, 360.0) > 0) {
							numberEditor.getTextField().setText("360.0");
						}
						if (Double.compare(rotation, 0.0) < 0) {
							numberEditor.getTextField().setText("0");
						}
					} catch (Exception ex) {
						Application.getActiveApplication().getOutput().output(ex);
					}
				}

			});
		}
		return jSpinnerRotation;
	}

	/**
	 * 字体效果基本面板
	 * 
	 * @return
	 */
	protected JPanel getPanelFontStyleBottom() {
		if (jPanelFontStyleBottom == null) {
			jPanelFontStyleBottom = new JPanel();
			jPanelFontStyleBottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			jPanelFontStyleBottom.setLayout(new GridLayout(0, 2));

			jPanelFontStyleBottom.add(getCheckBoxBold());
			jPanelFontStyleBottom.add(getCheckBoxUnderline());
			jPanelFontStyleBottom.add(getCheckBoxItalic());
			jPanelFontStyleBottom.add(getCheckBoxStrikeout());
			jPanelFontStyleBottom.add(getCheckBoxOutline());
			jPanelFontStyleBottom.add(getCheckBoxBackOpaque());
			jPanelFontStyleBottom.add(getCheckBoxShadow());
			jPanelFontStyleBottom.add(getCheckBoxSizeFixed());

		}
		return jPanelFontStyleBottom;
	}

	/**
	 * 字体加粗显示
	 * 
	 * @return
	 */
	protected JCheckBox getCheckBoxBold() {
		if (jCheckBoxBold == null) {
			jCheckBoxBold = new JCheckBox(ControlsProperties.getString("String_OverStriking"));
			jCheckBoxBold.setSelected(tempTextStyle.getBold());
			jCheckBoxBold.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (tempTextStyle != null && !isInInitialState) {
						tempTextStyle.setBold(jCheckBoxBold.isSelected());
						refreshPreViewMapControl();
					}
				}

			});
		}
		return jCheckBoxBold;
	}

	/**
	 * 斜体显示
	 * 
	 * @return
	 */
	protected JCheckBox getCheckBoxItalic() {
		if (jCheckBoxItalic == null) {
			jCheckBoxItalic = new JCheckBox(ControlsProperties.getString("String_Italic"));
			jCheckBoxItalic.setSelected(tempTextStyle.getItalic());
			jCheckBoxItalic.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (tempTextStyle != null && !isInInitialState) {
						tempTextStyle.setItalic(jCheckBoxItalic.isSelected());
						refreshPreViewMapControl();
					}
				}

			});
		}
		return jCheckBoxItalic;
	}

	/**
	 * 轮廓显示
	 * 
	 * @return
	 */
	protected JCheckBox getCheckBoxOutline() {
		if (jCheckBoxOutline == null) {
			jCheckBoxOutline = new JCheckBox(ControlsProperties.getString("String_Contour"));
			jCheckBoxOutline.setSelected(tempTextStyle.getOutline());
			jCheckBoxOutline.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (tempTextStyle != null && !isInInitialState) {
						tempTextStyle.setOutline(jCheckBoxOutline.isSelected());
						refreshPreViewMapControl();
					}
				}

			});
		}
		return jCheckBoxOutline;
	}

	/**
	 * 阴影显示
	 * 
	 * @return
	 */
	protected JCheckBox getCheckBoxShadow() {
		if (jCheckBoxShadow == null) {
			jCheckBoxShadow = new JCheckBox(ControlsProperties.getString("String_Shadow"));
			jCheckBoxShadow.setSelected(tempTextStyle.getShadow());
			jCheckBoxShadow.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (tempTextStyle != null && !isInInitialState) {
						tempTextStyle.setShadow(jCheckBoxShadow.isSelected());
						refreshPreViewMapControl();
					}
				}

			});
		}
		return jCheckBoxShadow;
	}

	/**
	 * 下划线显示
	 * 
	 * @return
	 */
	protected JCheckBox getCheckBoxUnderline() {
		if (jCheckBoxUnderline == null) {
			jCheckBoxUnderline = new JCheckBox(ControlsProperties.getString("String_Underline"));
			jCheckBoxUnderline.setSelected(tempTextStyle.getUnderline());
			jCheckBoxUnderline.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (tempTextStyle != null && !isInInitialState) {
						tempTextStyle.setUnderline(jCheckBoxUnderline.isSelected());
						refreshPreViewMapControl();
					}
				}

			});
		}
		return jCheckBoxUnderline;
	}

	/**
	 * 删除线显示
	 * 
	 * @return
	 */
	protected JCheckBox getCheckBoxStrikeout() {
		if (jCheckBoxStrikeout == null) {
			jCheckBoxStrikeout = new JCheckBox(ControlsProperties.getString("String_DeleteLine"));
			jCheckBoxStrikeout.setSelected(tempTextStyle.getStrikeout());
			jCheckBoxStrikeout.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (tempTextStyle != null && !isInInitialState) {
						tempTextStyle.setStrikeout(jCheckBoxStrikeout.isSelected());
						refreshPreViewMapControl();
					}
				}

			});
		}
		return jCheckBoxStrikeout;
	}

	/**
	 * 是否背景透明
	 * 
	 * @return
	 */
	protected JCheckBox getCheckBoxBackOpaque() {
		if (jCheckBoxBackOpaque == null) {
			jCheckBoxBackOpaque = new JCheckBox(ControlsProperties.getString("String_BackgroundTransparency"));
			jCheckBoxBackOpaque.setSelected(!tempTextStyle.getBackOpaque());
			jCheckBoxBackOpaque.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (tempTextStyle != null && !isInInitialState) {
						tempTextStyle.setBackOpaque(!jCheckBoxBackOpaque.isSelected());
						buttonBackgroundColor.setEnabled(!jCheckBoxBackOpaque.isSelected());
						// jButtonBackgroundColor.setEnabled(!jCheckBoxBackOpaque.isSelected());
						// jBackgroundColorDropDown.getArrowButton().setEnabled(!jCheckBoxBackOpaque.isSelected());
						// if (jCheckBoxBackOpaque.isSelected()) {
						// jButtonBackgroundColor.setIcon(null);
						// } else {
						// jButtonBackgroundColor.setIcon(m_colorSwatch);
						// }
						refreshPreViewMapControl();
					}
				}

			});
		}
		return jCheckBoxBackOpaque;
	}

	/**
	 * 是否固定大小
	 * 
	 * @return
	 */
	protected JCheckBox getCheckBoxSizeFixed() {
		if (jCheckBoxSizeFixed == null) {
			jCheckBoxSizeFixed = new JCheckBox(ControlsProperties.getString("String_FixedSize"));
			jCheckBoxSizeFixed.setSelected(tempTextStyle.isSizeFixed());
			jCheckBoxSizeFixed.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					abstractjCheckBoxSizeFixedLisenter();
				}

			});
		}
		return jCheckBoxSizeFixed;
	}

	private void abstractjCheckBoxSizeFixedLisenter() {
		if (tempTextStyle != null && !isInInitialState) {
			double height = 0.0;
			if (jCheckBoxSizeFixed.isSelected()) {
				double size = mapHeightToFontSize(tempTextStyle.getFontHeight() * UNIT_CONVERSION, mapObject, tempTextStyle.isSizeFixed());
				height = size / EXPERIENCE;
				if (height > 255) {
					isInInitialState = true;
					height = 255;
					jSpinnerFontHeight.setValue(height / UNIT_CONVERSION);
					comboBoxTextFieldFontSize.setText(String.valueOf(height * EXPERIENCE));
					isInInitialState = false;
				}
			} else {
				double size = tempTextStyle.getFontHeight() * UNIT_CONVERSION * EXPERIENCE;
				height = fontSizeToMapHeight(size, mapObject, false);
			}

			if (height / UNIT_CONVERSION > 0) {
				tempTextStyle.setFontHeight(height / UNIT_CONVERSION);
			}
			tempTextStyle.setSizeFixed(jCheckBoxSizeFixed.isSelected());
			jSpinnerFontWidth.setEnabled(!tempTextStyle.isSizeFixed());
			isInInitialState = true;

			double fontWidth = mapWidthToFontWidth(tempTextStyle.getFontWidth() * UNIT_CONVERSION, mapObject, tempTextStyle.isSizeFixed());
			jSpinnerFontWidth.setValue(fontWidth);
			isInInitialState = false;

			refreshPreViewMapControl();
		}
	}

	/**
	 * 获取预览面板
	 * 
	 * @return
	 */
	protected JPanel getPanelPreView() {
		if (jPanelPreView == null) {
			jPanelPreView = new JPanel();
			jPanelPreView.setLayout(new BorderLayout());
			jPanelPreView.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), ControlsProperties.getString("String_Preview"),
					TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.BLUE));
			jPanelPreView.add(getPreViewMapControl(), BorderLayout.CENTER);
		}
		return jPanelPreView;
	}

	/**
	 * 获取预览的MapControl
	 * 
	 * @return
	 */
	protected MapControl getPreViewMapControl() {
		if (mapControl == null) {
			mapControl = new MapControl();
			mapControl.getMap().setViewBounds(new Rectangle2D(new Point2D(0, 0), new Point2D(200, 200)));
			String text = this.getTextArea().getText();
			Double rotation = (Double) this.getSpinnerRotation().getValue();
			GeoText geoText = getPreViewGeoText(text, rotation);
			geoText.setTextStyle(tempTextStyle);
			// 设置为系统默认光标
			mapControl.setAction(Action.NULL);
			mapControl.setInteractionMode(InteractionMode.CUSTOMALL);
			Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
			MapControl.Cursors.setArrow(cursor);

			Map map = mapControl.getMap();
			for (int i = map.getTrackingLayer().getCount() - 1; i >= 0; i--) {
				if (map.getTrackingLayer().getTag(i).startsWith("TextStyle")) {
					map.getTrackingLayer().remove(i);
				}
			}
			map.getTrackingLayer().add(getPreGeoPoint(geoText.getInnerPoint()), "TextStyleGeoPoint");
			map.getTrackingLayer().add(geoText, "TextStyleGeoText");
			mapControl.getMap().refreshTrackingLayer();
		}
		return mapControl;
	}

	/**
	 * 获取文本内容面板
	 * 
	 * @return
	 */
	protected JPanel getPanelFontContent() {
		if (jPanelFontContent == null) {
			jPanelFontContent = new JPanel(new BorderLayout());
			jPanelFontContent.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			jPanelFontContent.add(getPanelTextPart(), BorderLayout.NORTH);
			jPanelFontContent.add(getPanelTextArea(), BorderLayout.CENTER);
		}
		return jPanelFontContent;
	}

	/**
	 * 获取文本子对象面板
	 * 
	 * @return
	 */
	protected JPanel getPanelTextPart() {
		if (jPanelTextPart == null) {
			jPanelTextPart = new JPanel(new BorderLayout());
			JLabel label = new JLabel(ControlsProperties.getString("String_SubObject"));
			jPanelTextPart.add(label, BorderLayout.WEST);
			jPanelTextPart.add(getComboxContent(), BorderLayout.CENTER);
		}
		return jPanelTextPart;
	}

	/**
	 * 获取子对象Combobox
	 * 
	 * @return
	 */
	private JComboBox getComboxContent() {
		if (jComboxContent == null) {
			jComboxContent = new JComboBox();
			jComboxContent.setPreferredSize(new Dimension(200, 20));
			int textCount = tempGeoText.getPartCount();
			for (int i = 0; i < textCount; i++) {
				jComboxContent.addItem(MessageFormat.format(ControlsProperties.getString("String_TheNumberSubObject"), i + 1));
			}
			jComboxContent.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (tempGeoText != null && !isInInitialState) {
						isInInitialState = true;
						int i = jComboxContent.getSelectedIndex();
						String text = tempGeoText.getPart(i).getText();
						getTextArea().setText(text);
						isInInitialState = false;
						refreshPreViewMapControl();
					}
				}

			});
		}
		return jComboxContent;
	}

	/**
	 * 获取文本内容面板
	 * 
	 * @return
	 */
	protected JPanel getPanelTextArea() {
		if (jPanelTextArea == null) {
			jPanelTextArea = new JPanel(new BorderLayout());
			JLabel labelText = new JLabel(ControlsProperties.getString("String_TextContent"));
			jPanelTextArea.add(labelText, BorderLayout.NORTH);
			jPanelTextArea.add(getTextArea(), BorderLayout.CENTER);
		}
		return jPanelTextArea;
	}

	/**
	 * 获取文本内容的文本框
	 * 
	 * @return
	 */
	protected JTextArea getTextArea() {
		if (jTextArea == null) {
			jTextArea = new JTextArea();
			jTextArea.setBorder(BorderFactory.createLineBorder(Color.black, 1));
			if (tempGeoText != null && tempGeoText.getPartCount() > 0) {
				jTextArea.setText(tempGeoText.getPart(0).getText());
			} else {
				jTextArea.setText(sampleText);
			}
			// 允许编辑GeoText子对象，并在预览中显示
			jTextArea.addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					if (tempGeoText != null && !isInInitialState) {
						int i = jComboxContent.getSelectedIndex();
						if (i > -1) {
							TextPart textPart = tempGeoText.getPart(i);
							textPart.setText(jTextArea.getText());
							refreshPreViewMapControl();
						}
					}
				}

			});
		}
		return jTextArea;
	}

	/**
	 * 获取要预览的GeoText
	 * 
	 * @return
	 */
	protected GeoText getPreViewGeoText(String currentText, double rotation) {
		if (preViewGeoText == null) {
			preViewGeoText = new GeoText();
		}
		preViewGeoText.setEmpty();
		TextPart textPart = new TextPart(currentText, mapControl.getMap().getCenter(), rotation);
		preViewGeoText.addPart(textPart);
		return preViewGeoText;
	}

	/**
	 * 刷新预览面板
	 */
	protected void refreshPreViewMapControl() {
		String text = this.getTextArea().getText();
		Double rotation = (Double) this.getSpinnerRotation().getValue();
		GeoText geoText = this.getPreViewGeoText(text, rotation);

		TextStyle textStyle = new TextStyle(tempTextStyle);
		double width = Double.parseDouble(jSpinnerFontWidth.getValue().toString());
		double height = Double.parseDouble(jSpinnerFontHeight.getValue().toString());
		textStyle.setFontWidth(fontWidthToMapWidth(width, mapControl.getMap(), textStyle.isSizeFixed()) / UNIT_CONVERSION);
		double fontHeight = fontSizeToMapHeight(height * EXPERIENCE, mapControl.getMap(), textStyle.isSizeFixed()) / UNIT_CONVERSION;
		if (fontHeight > 0) {
			textStyle.setFontHeight(fontHeight);
		}

		geoText.setTextStyle(textStyle);
		Map map = mapControl.getMap();
		map.getTrackingLayer().set(1, geoText);
		map.setCenter(geoText.getInnerPoint());
		map.refresh();
	}

	/**
	 * 获取预览点，界面还没有显示时，地图中心位置为(0,0)
	 * 
	 * @return
	 */
	protected GeoPoint getPreGeoPoint(Point2D point) {
		if (preViewGeoPoint == null) {
			preViewGeoPoint = new GeoPoint(point);
			GeoStyle geoStyle = new GeoStyle();
			geoStyle.setLineColor(Color.lightGray);
			geoStyle.setMarkerSize(new Size2D(2.5, 2.5));
			preViewGeoPoint.setStyle(geoStyle);
		}
		return preViewGeoPoint;
	}

	/**
	 * 用于初始化时刷新预览的MapControl，仅在paint中调用
	 */
	protected void initRefreshPreViewMapControl() {
		String text = this.getTextArea().getText();
		Double rotation = (Double) this.getSpinnerRotation().getValue();
		GeoText geoText = this.getPreViewGeoText(text, rotation);

		TextStyle textStyle = new TextStyle(tempTextStyle);
		double width = Double.parseDouble(jSpinnerFontWidth.getValue().toString());
		double height = Double.parseDouble(jSpinnerFontHeight.getValue().toString());
		textStyle.setFontWidth(fontWidthToMapWidth(width, mapControl.getMap(), textStyle.isSizeFixed()) / UNIT_CONVERSION);
		double fontHeight = fontSizeToMapHeight(height * EXPERIENCE, mapControl.getMap(), textStyle.isSizeFixed()) / UNIT_CONVERSION;
		if (fontHeight > 0) {
			textStyle.setFontHeight(fontHeight);
		}

		geoText.setTextStyle(textStyle);
		Map map = mapControl.getMap();
		for (int i = map.getTrackingLayer().getCount() - 1; i >= 0; i--) {
			if (map.getTrackingLayer().getTag(i).startsWith("TextStyle")) {
				map.getTrackingLayer().remove(i);
			}
		}
		map.getTrackingLayer().add(getPreGeoPoint(geoText.getInnerPoint()), "TextStyleGeoPoint");
		map.getTrackingLayer().add(geoText, "TextStyleGeoText");
		map.setCenter(geoText.getInnerPoint());
		map.refresh();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		// 仅在初始化的时候多执行一次refreshPreViewMapControl，
		// 并把m_preViewGeoPoint设置在中心位置
		if (isInitialize) {
			initRefreshPreViewMapControl();
			isInitialize = false;
		}
	}

	/**
	 * 根据用户不同的设置刷新预览面板
	 */
	private void refreshPanelView() {
		isInInitialState = true;
		if (isThemeText && !is3DText) {
			jPanelView.removeAll();
			jPanelView.setLayout(new BorderLayout());
			jPanelView.add(getPanelPreView(), BorderLayout.CENTER);
			if (isGeoTextTextStyle) {
				jPanelView.add(getPanelForThemeGeoText(), BorderLayout.SOUTH);
			} else {
				jPanelView.add(getPanelForTheme(), BorderLayout.SOUTH);
			}
		} else if (!isThemeText && is3DText) {
			jPanelView.removeAll();
			jPanelView.setLayout(new BorderLayout());
			jPanelView.add(getPanelPreView(), BorderLayout.CENTER);
			if (isGeoTextTextStyle) {
				jPanelView.add(getPanelForSceneGeoText(), BorderLayout.SOUTH);
			} else {
				jPanelView.add(getPanelForScene(), BorderLayout.SOUTH);
			}
		} else if (isThemeText && is3DText) {
			jPanelView.removeAll();
			jPanelView.setLayout(new BorderLayout());
			jPanelView.add(getPanelPreView(), BorderLayout.CENTER);
			if (isGeoTextTextStyle) {
				jPanelView.add(getPanelForSceneAndThemeGeoText(), BorderLayout.SOUTH);
			} else {
				jPanelView.add(getPanelForSceneAndTheme(), BorderLayout.SOUTH);
			}
		}
		jPanelView.validate();
		jPanelView.repaint();
		this.validate();
		this.repaint();
		isInInitialState = false;
	}

	/**
	 * 利用网格组布局向JPanel中添加组件
	 * 
	 * @param c
	 * @param constraints
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	protected void addComponentToPanel(JPanel panel, Component c, GridBagConstraints constraints, int x, int y, int w, int h) {
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = w;
		constraints.gridheight = h;
		panel.add(c, constraints);
	}

	/**
	 * 利用网格组布局时，获得一个GridBagConstraints
	 * 
	 * @return
	 */
	protected GridBagConstraints getGridBagConstraints() {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.WEST;
		constraints.weightx = 100;
		constraints.weighty = 100;

		return constraints;
	}

	static double fontSizeToMapHeight(double fontSize, Object mapObject, boolean isSizeFixed) {
		double mapHeight = 0;
		try {
			mapHeight = fontSize / EXPERIENCE;
			if (isSizeFixed) {
				mapHeight = fontSize / EXPERIENCE;
			} else {
				Double fontHeight = fontSize / EXPERIENCE;
				Point2D logicalPntEnd = new Point2D(fontHeight, fontHeight);
				Point2D logicalPntStart = new Point2D(0, 0);
				Point2D pointEnd = new Point2D();
				Point2D pointStart = new Point2D();
				if (mapObject instanceof Map) {
					Map map = (Map) mapObject;
					if (map != null) {
						pointEnd = map.logicalToMap(logicalPntEnd);
						pointStart = map.logicalToMap(logicalPntStart);
					}
				} else if (mapObject instanceof MapLayout) {
					MapLayout mapLayout = (MapLayout) mapObject;
					if (mapLayout != null) {
						pointEnd = mapLayout.logicalToLayout(logicalPntEnd);
						pointStart = mapLayout.logicalToLayout(logicalPntStart);
					}
				}
				mapHeight = Math.abs(pointEnd.getY() - pointStart.getY());
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return mapHeight;
	}

	static double mapHeightToFontSize(double mapHeight, Object mapObject, boolean isSizeFixed) {
		double fontSize = 0;
		try {
			fontSize = mapHeight * EXPERIENCE;
			if (isSizeFixed) {
				fontSize = mapHeight * EXPERIENCE;
			} else {
				Point2D pointEnd = new Point2D(mapHeight, mapHeight);
				Point2D pointStart = new Point2D(0, 0);
				Point2D logicalPntEnd = new Point2D();
				Point2D logicalPntStart = new Point2D();
				if (mapObject instanceof Map) {
					Map map = (Map) mapObject;
					if (map != null) {
						logicalPntEnd = map.mapToLogical(pointEnd);
						logicalPntStart = map.mapToLogical(pointStart);
					}
				} else if (mapObject instanceof MapLayout) {
					MapLayout mapLayout = (MapLayout) mapObject;
					if (mapLayout != null) {
						logicalPntEnd = mapLayout.layoutToLogical(pointEnd);
						logicalPntStart = mapLayout.layoutToLogical(pointStart);
					}
				}
				fontSize = Math.abs((logicalPntEnd.getY() - logicalPntStart.getY()) * EXPERIENCE);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return fontSize;
	}

	static double mapWidthToFontWidth(double mapWidth, Object mapObject, boolean isSizeFixed) {
		double fontWidth = 0;
		try {
			if (!isSizeFixed) {
				fontWidth = mapWidth;
				Point2D pointEnd = new Point2D(mapWidth, mapWidth);
				Point2D pointStart = new Point2D(0, 0);
				Point2D logicalPntEnd = new Point2D();
				Point2D logicalPntStart = new Point2D();
				if (mapObject instanceof Map) {
					Map map = (Map) mapObject;
					if (map != null) {
						logicalPntEnd = map.mapToLogical(pointEnd);
						logicalPntStart = map.mapToLogical(pointStart);
					}
				} else if (mapObject instanceof MapLayout) {
					MapLayout mapLayout = (MapLayout) mapObject;
					if (mapLayout != null) {
						logicalPntEnd = mapLayout.layoutToLogical(pointEnd);
						logicalPntStart = mapLayout.layoutToLogical(pointStart);
					}
				}
				fontWidth = Math.abs(logicalPntEnd.getY() - logicalPntStart.getY());
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return fontWidth;
	}

	// 文本风格中地图单位的字高、字宽与字号、逻辑字高、字宽之间的转换
	static double fontWidthToMapWidth(double fontWidth, Object mapObject, boolean isSizeFixed) {
		double mapWidth = 0;
		try {
			mapWidth = fontWidth;
			if (!isSizeFixed) {
				Point2D logicalPntEnd = new Point2D(fontWidth, fontWidth);
				Point2D logicalPntStart = new Point2D(0, 0);
				Point2D pointEnd = new Point2D();
				Point2D pointStart = new Point2D();
				if (mapObject instanceof Map) {
					Map map = (Map) mapObject;
					if (map != null) {
						pointEnd = map.logicalToMap(logicalPntEnd);
						pointStart = map.logicalToMap(logicalPntStart);
					}
				} else if (mapObject instanceof MapLayout) {
					MapLayout mapLayout = (MapLayout) mapObject;
					if (mapLayout != null) {
						pointEnd = mapLayout.logicalToLayout(logicalPntEnd);
						pointStart = mapLayout.logicalToLayout(logicalPntStart);
					}
				}
				mapWidth = Math.abs(pointEnd.getX() - pointStart.getX());
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return mapWidth;
	}

	private void changeFontSizeWithMapObject() {
		isInInitialState = true;
		// 非固定文本大小
		if (tempGeoText != null && tempTextStyle != null && !tempTextStyle.isSizeFixed()) {
			// 非固定时，地图中显示的字体在屏幕中显示的大小肯定发生了变化，所以需要重新计算现在的字体大小
			// 字体信息从现在的TextStyle属性中获取，经过计算后显示其字号大小
			double size = mapHeightToFontSize(tempTextStyle.getFontHeight() * UNIT_CONVERSION, mapObject, false);
			DecimalFormat decimalFormat = new DecimalFormat("#0.0");
			comboBoxTextFieldFontSize.setText(decimalFormat.format(size));

			decimalFormat = new DecimalFormat("#0.00");
			String temp = decimalFormat.format(size / EXPERIENCE);
			jSpinnerFontHeight.setValue(Double.valueOf(temp));
			double logicalWidth = mapWidthToFontWidth(tempTextStyle.getFontWidth() * UNIT_CONVERSION, mapObject, tempTextStyle.isSizeFixed());
			jSpinnerFontWidth.setValue(logicalWidth);
			refreshPreViewMapControl();
		} else if (tempGeoText != null && tempTextStyle != null && tempTextStyle.isSizeFixed()) {
			// 字体是固定大小时，字体显示的大小不发生变化，不需要更新任何控件内容
			double size = mapHeightToFontSize(tempTextStyle.getFontHeight() * UNIT_CONVERSION, mapObject, true);
			DecimalFormat decimalFormat = new DecimalFormat("#0.0");
			String formatString = decimalFormat.format(size);
			comboBoxTextFieldFontSize.setText(formatString);

			decimalFormat = new DecimalFormat("#0.00");
			String temp = decimalFormat.format(size / EXPERIENCE);
			jSpinnerFontHeight.setValue(Double.valueOf(temp));
			jSpinnerFontWidth.setValue(0.0);
		}
		isInInitialState = false;
	}

	private void onMapObjectChanged() {
		changeFontSizeWithMapObject();
	}
}
