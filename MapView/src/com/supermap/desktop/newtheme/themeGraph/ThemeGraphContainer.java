package com.supermap.desktop.newtheme.themeGraph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.enums.UnitValue;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.newtheme.commonPanel.TextStyleDialog;
import com.supermap.desktop.newtheme.commonPanel.ThemeChangePanel;
import com.supermap.desktop.newtheme.commonUtils.ThemeGuideFactory;
import com.supermap.desktop.newtheme.commonUtils.ThemeItemLabelDecorator;
import com.supermap.desktop.newtheme.commonUtils.ThemeUtil;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.ComponentBorderPanel.CompTitledPane;
import com.supermap.desktop.utilties.*;
import com.supermap.mapping.*;
import com.supermap.ui.MapControl;

/**
 * @author Administrator 统计专题图实现类
 */
public class ThemeGraphContainer extends ThemeChangePanel {

	private static final long serialVersionUID = 1L;

	private JPanel panelProperty = new JPanel();
	private JPanel panelAdvance = new JPanel();

	private JLabel labelColorStyle = new JLabel();
	private JLabel labelGraphType = new JLabel();
	private JLabel labelMethod = new JLabel();
	private ColorsComboBox comboBoxColor = new ColorsComboBox();
	private JComboBox<String> comboBoxGraphType = new JComboBox<String>();
	private JComboBox<String> comboBoxMethod = new JComboBox<String>();
	private JScrollPane scollPane = new JScrollPane();
	private JTable tableGraphInfo = new JTable();
	private JToolBar toolbar = new JToolBar();
	private JButton buttonDelete = new JButton();
	private JButton buttonAdd = new JButton();
	private JButton buttonStyle = new JButton();
	private JButton buttonMoveToFrist = new JButton();
	private JButton buttonMoveToForward = new JButton();
	private JButton buttonMoveToNext = new JButton();
	private JButton buttonMoveToLast = new JButton();
	private JTabbedPane tabbedPaneInfo = new JTabbedPane();

	private JCheckBox checkBoxRemark = new JCheckBox();
	private JCheckBox checkBoxAxis = new JCheckBox();
	// panelOptions
	private JCheckBox checkBoxShowFlow = new JCheckBox();
	private JCheckBox checkBoxShowNegative = new JCheckBox();
	private JCheckBox checkBoxAutoAvoid = new JCheckBox();
	private JCheckBox checkBoxAutoScale = new JCheckBox();
	private JCheckBox checkboxDraftLine = new JCheckBox();
	private JButton buttonDraftLine = new JButton();
	// panelSizeLimite
	private JLabel labelMaxValue = new JLabel();
	private JTextField textFieldMaxValue = new JTextField();
	private JLabel labelMinValue = new JLabel();
	private JTextField textFieldMinValue = new JTextField();
	// panelParameterSetting
	private JLabel labelOffsetUnity = new JLabel();
	private JComboBox<String> comboBoxOffsetUnity = new JComboBox<String>();
	private JLabel labelOffsetX = new JLabel();
	private JComboBox<String> comboBoxOffsetX = new JComboBox<String>();
	private JLabel labelOffsetY = new JLabel();
	private JComboBox<String> comboBoxOffsetY = new JComboBox<String>();
	private JLabel labelOffsetXUnity = new JLabel();
	private JLabel labelOffsetYUnity = new JLabel();
	// panelRemark
	private JLabel labelRemarkFormat = new JLabel();
	private JComboBox<String> comboBoxRemarkFormat = new JComboBox<String>();
	private JLabel labelRemarkStyle = new JLabel();
	private JButton buttonRemarkStyle = new JButton("...");
	// panelAxis
	private JLabel labelAxisColor = new JLabel();
	private ColorSelectButton buttonAxisColor;
	private JLabel labelAxisModel = new JLabel();
	private JComboBox<String> comboBoxAxisModel = new JComboBox<String>();
	private JLabel labelAxisStyle = new JLabel();
	private JButton buttonAxisStyle = new JButton("...");
	private JCheckBox checkBoxShowAxisGrid = new JCheckBox();
	// panelStyleOfBAR
	private JLabel labelBarWidth = new JLabel();
	private JSpinner spinnerBarWidth = new JSpinner();
	// panelStyleOfRoseAndPIE
	private JLabel labelStartAngle = new JLabel();
	private JSpinner spinnerStartAngle = new JSpinner();
	private JLabel labelRoseRAngle = new JLabel();
	private JSpinner spinnerRoseAngle = new JSpinner();

	private JPanel panelOptions = new JPanel();
	private JPanel panelSizeLimite = new JPanel();
	private JPanel panelParameterSetting = new JPanel();
	private JPanel panelRemark = new JPanel();
	private JPanel panelAxis = new JPanel();
	private JPanel panelStyleOfBAR = new JPanel();
	private JPanel panelStyleOfRoseAndPIE = new JPanel();

	private ThemeGraph themeGraph;
	private boolean isRefreshAtOnce;

	private transient DatasetVector datasetVector;
	private transient Map map;
	private transient Layer themeGraphLayer;
	private String layerName;
	private String[] nameStrings = new String[] { MapViewProperties.getString("String_ThemeGraphItemManager_ClmExpression"),
			MapViewProperties.getString("String_Title_Sytle"), MapViewProperties.getString("String_ThemeGraphTextFormat_Caption") };
	private final int TABLE_COLUMN_EXPRESSION = 0;
	private final int TABLE_COLUMN_STYLE = 1;
	private final int TABLE_COLUMN_CAPTION = 2;
	private int graphCount;
	private transient SteppedComboBox fieldComboBox;
	private transient LayersTree layersTree = UICommonToolkit.getLayersManager().getLayersTree();

	private MouseListener localMouseListener = new LocalMouseListener();
	private ItemListener graphTypeChangeListener = new GraphTypeChangeListener();
	private ItemListener graphModeChangeListener = new GraphModeChangeListener();
	private ItemListener graphColorChangeListener = new GraphItemColorChangeListener();
	private TableModelListener graphCaptionChangeListener = new CaptionChangeListener();
	private ItemListener OptionsChangeListener = new OptionsChangeListener();
	private ActionListener showLeaderLineAction = new ShowLeaderLineAction();
	private DocumentListener graphSizeChangeListener = new GraphSizeChangeListener();
	private ItemListener offsetFixedListener = new OffsetFixedListener();
	private ItemListener offsetxListener = new OffsetXListener();
	private ItemListener offsetYListener = new OffsetYListener();
	private ItemListener graphTextDisplayedListener = new GraphTextDisplayedListener();
	private ItemListener graphTextFormatListener = new GraphTextFormatListener();
	private ActionListener remarkStyleListener = new RemarkStyleListener();
	private ItemListener showAxisTextListener = new ShowAxisTextListener();
	private PropertyChangeListener axisColorListener = new AxisColorListener();
	private ItemListener axisModelListener = new AxisModelListener();
	private ActionListener axisStyleListener = new AxisStyleListener();
	private ItemListener showAxisGridListener = new ShowAxisGridListener();
	private ChangeListener barWidthListener = new BarWidthListener();
	private ChangeListener startAngleListener = new StartAngleListener();
	private ChangeListener roseAngleListener = new RoseAngleListener();
	private ActionListener toolbarAction = new ToolBarAction();
	private PropertyChangeListener layersTreePropertyChangeListener = new LayersTreeChangeListener();
	private PropertyChangeListener layerPropertyChangeListener = new LayerPropertyChangeListener();
	private MouseListener tableMouseListener = new TableMouseListener();
	protected TextStyleDialog textStyleDialog;
	private ArrayList<String> comboBoxArray = new ArrayList<String>();
	private ArrayList<String> comboBoxArrayForOffsetX = new ArrayList<String>();
	private ArrayList<String> comboBoxArrayForOffsetY = new ArrayList<String>();

	/**
	 * @wbp.parser.constructor
	 */
	public ThemeGraphContainer(DatasetVector datasetVector, ThemeGraph themeGraph, Layer layer) {
		this.datasetVector = datasetVector;
		this.themeGraph = new ThemeGraph(themeGraph);
		this.map = initCurrentTheme(datasetVector, layer);
		initComponents();
		initResources();
		registActionListener();
	}

	public ThemeGraphContainer(Layer layer) {
		this.themeGraphLayer = layer;
		this.datasetVector = (DatasetVector) layer.getDataset();
		this.themeGraph = new ThemeGraph((ThemeGraph) layer.getTheme());
		this.layerName = this.themeGraphLayer.getName();
		this.map = ThemeGuideFactory.getMapControl().getMap();
		initComponents();
		initResources();
		registActionListener();
	}

	private Map initCurrentTheme(DatasetVector datasetVector, Layer layer) {
		MapControl mapControl = ThemeGuideFactory.getMapControl();
		if (null != mapControl) {
			this.themeGraphLayer = mapControl.getMap().getLayers().add(datasetVector, themeGraph, true);
			this.themeGraphLayer.getDisplayFilter().setJoinItems(layer.getDisplayFilter().getJoinItems());
			this.layerName = this.themeGraphLayer.getName();
			UICommonToolkit.getLayersManager().getLayersTree().setSelectionRow(0);
			mapControl.getMap().refresh();
		}
		return mapControl.getMap();
	}

	private void initResources() {
		this.labelColorStyle.setText(MapViewProperties.getString("String_Label_ColorScheme"));
		this.labelGraphType.setText(MapViewProperties.getString("String_ThemeGraphProperty_LabelGraphType"));
		this.labelMethod.setText(MapViewProperties.getString("String_ThemeGraphProperty_LabelStatisticMode"));
		this.buttonDelete.setToolTipText(MapViewProperties.getString("String_Title_Delete"));
		this.buttonAdd.setToolTipText(MapViewProperties.getString("String_Title_Add"));
		this.buttonStyle.setToolTipText(MapViewProperties.getString("String_Title_Sytle"));
		this.buttonMoveToFrist.setToolTipText(MapViewProperties.getString("String_Title_MoveToFrist"));
		this.buttonMoveToForward.setToolTipText(MapViewProperties.getString("String_Title_MoveToForward"));
		this.buttonMoveToNext.setToolTipText(MapViewProperties.getString("String_Title_MoveToNext"));
		this.buttonMoveToLast.setToolTipText(MapViewProperties.getString("String_Title_MoveToLast"));
		this.checkBoxRemark.setText(MapViewProperties.getString("String_ThemeGraphAdvance_CheckBoxDisplayLabel"));
		this.checkBoxAxis.setText(MapViewProperties.getString("String_ThemeGraphAdvance_CheckBoxAxe"));
		this.checkBoxShowFlow.setText(MapViewProperties.getString("String_CheckBox_ShowFlow"));
		this.checkBoxShowNegative.setText(MapViewProperties.getString("String_ThemeGraduatedSymbolProperty_CheckBoxNegative"));
		this.checkBoxAutoAvoid.setText(MapViewProperties.getString("String_CheckBox_AutoAvoid"));
		this.checkBoxAutoScale.setText(MapViewProperties.getString("String_ThemeGraphAdvance_CheckBoxAutoScale"));
		this.checkboxDraftLine.setText(MapViewProperties.getString("String_ShowLeaderLine"));
		this.buttonDraftLine.setText(MapViewProperties.getString("String_Button_LineStyle"));
		this.labelMaxValue.setText(MapViewProperties.getString("String_ThemeGraphAdvance_LabelMaxSize"));
		this.labelMinValue.setText(MapViewProperties.getString("String_ThemeGraphAdvance_LabelMinSize"));
		this.labelOffsetUnity.setText(MapViewProperties.getString("String_Label_GetoffUnit"));
		this.labelOffsetX.setText(MapViewProperties.getString("String_Label_GetoffX"));
		this.labelOffsetY.setText(MapViewProperties.getString("String_Label_GetoffY"));
		this.labelRemarkFormat.setText(MapViewProperties.getString("String_ThemeGraphAdvance_LabelGraphLabelFormat"));
		this.labelRemarkStyle.setText(MapViewProperties.getString("String_ThemeGraphAdvance_LabelGraphStyle"));
		this.labelAxisColor.setText(MapViewProperties.getString("String_ThemeGraphAdvance_LabelAxeColor"));
		this.labelAxisModel.setText(MapViewProperties.getString("String_AxesTextDisplayMode"));
		this.labelAxisStyle.setText(MapViewProperties.getString("String_ThemeGraphAdvance_LabelGraphStyle"));
		this.checkBoxShowAxisGrid.setText(MapViewProperties.getString("String_ThemeGraphAdvance_CheckBoxAxeGrid"));
		this.labelBarWidth.setText(MapViewProperties.getString("String_ThemeGraphAdvance_LabelBarWidth"));
		this.labelStartAngle.setText(MapViewProperties.getString("String_ThemeGraphAdvance_LabelStartAngle"));
		this.labelRoseRAngle.setText(MapViewProperties.getString("String_ThemeGraphAdvance_LabelRoseAngle"));
		this.panelOptions.setBorder(new TitledBorder(MapViewProperties.getString("String_ThemeGraphAdvance_GroupBoxOption")));
		this.panelSizeLimite.setBorder(new TitledBorder(MapViewProperties.getString("String_ThemeGraphAdvance_GroupBoxLimited")));
		this.panelParameterSetting.setBorder(new TitledBorder(MapViewProperties.getString("String_GroupBoxOffset")));
		this.panelStyleOfBAR.setBorder(new TitledBorder(MapViewProperties.getString("String_ThemeGraphAdvance_GroupBoxBarStyle")));
		this.panelStyleOfRoseAndPIE.setBorder(new TitledBorder(MapViewProperties.getString("String_ThemeGraphAdvance_GroupBoxPie")));
	}

	@Override
	public Theme getCurrentTheme() {
		return this.themeGraph;
	}

	private void initComponents() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		this.setLayout(gridBagLayout);
		this.tabbedPaneInfo.add(MapViewProperties.getString("String_Theme_Property"), this.panelProperty);
		this.tabbedPaneInfo.add(MapViewProperties.getString("String_Theme_Advanced"), this.panelAdvance);
		this.panelProperty.setLayout(new GridBagLayout());
		this.add(tabbedPaneInfo, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		initPanelProperty();
		initPanelAdvance();
	}

	private void initPanelAdvance() {
		this.panelAdvance.setLayout(new GridBagLayout());
		CompTitledPane paneRemark = new CompTitledPane(this.checkBoxRemark, panelRemark);
		CompTitledPane paneAxis = new CompTitledPane(this.checkBoxAxis, panelAxis);
		initPanelOptions(this.panelOptions);
		initpanelSizeLimite(this.panelSizeLimite);
		initpanelParameterSetting(this.panelParameterSetting);
		initpanelRemark(this.panelRemark);
		initpanelAxis(this.panelAxis);
		initPanelStyleOfBAR(this.panelStyleOfBAR);
		initPanelStyleOfRoseAndPIE(this.panelStyleOfRoseAndPIE);
		JPanel panelAdvanceContent = new JPanel();
		this.panelAdvance.add(
				panelAdvanceContent,
				new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL)
						.setInsets(5, 10, 5, 10));
		panelAdvanceContent.setLayout(new GridBagLayout());
		// @formatter:off
		panelAdvanceContent.add(this.panelOptions,           new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setInsets(2, 10, 2, 10).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));
		panelAdvanceContent.add(this.panelSizeLimite,        new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.NORTH).setInsets(2, 10, 2, 10).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));
		panelAdvanceContent.add(this.panelParameterSetting,  new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.NORTH).setInsets(2, 10, 2, 10).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));
		panelAdvanceContent.add(paneRemark,                  new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.NORTH).setInsets(2, 10, 2, 10).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));
		panelAdvanceContent.add(paneAxis,                    new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.NORTH).setInsets(2, 10, 2, 10).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));
		panelAdvanceContent.add(this.panelStyleOfBAR,        new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.NORTH).setInsets(2, 10, 2, 10).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));
		panelAdvanceContent.add(this.panelStyleOfRoseAndPIE, new GridBagConstraintsHelper(0, 6, 1, 1).setAnchor(GridBagConstraints.NORTH).setInsets(2, 10, 2, 10).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));
		// @formatter:on
	}

	private void initPanelStyleOfRoseAndPIE(JPanel panelStyleOfRoseAndPIE) {
		spinnerStartAngle.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(1)));
		// @formatter:off
		this.spinnerStartAngle.setEnabled(false);
		spinnerRoseAngle.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(1)));
		this.spinnerRoseAngle.setEnabled(false);
		panelStyleOfRoseAndPIE.setLayout(new GridBagLayout());
		panelStyleOfRoseAndPIE.add(this.labelStartAngle,  new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 10).setWeight(30, 1));
		panelStyleOfRoseAndPIE.add(this.spinnerStartAngle,new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 10).setWeight(50, 1).setFill(GridBagConstraints.HORIZONTAL));
		panelStyleOfRoseAndPIE.add(this.labelRoseRAngle,  new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(30, 1));
		panelStyleOfRoseAndPIE.add(this.spinnerRoseAngle, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(50, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.spinnerStartAngle.setPreferredSize(new Dimension((int) this.buttonDraftLine.getPreferredSize().getWidth(), 23));
		this.spinnerRoseAngle.setPreferredSize(new Dimension((int) this.buttonDraftLine.getPreferredSize().getWidth(), 23));
		initSpinnerStartAngleState();
		initSpinnerRoseAngleState();
		// @formatter:on
	}

	private void initSpinnerRoseAngleState() {
		if (themeGraph.getGraphType() == ThemeGraphType.ROSE || themeGraph.getGraphType() == ThemeGraphType.ROSE3D) {
			this.spinnerRoseAngle.setEnabled(true);
		}
	}

	private void initSpinnerStartAngleState() {
		if (themeGraph.getGraphType() == ThemeGraphType.PIE || themeGraph.getGraphType() == ThemeGraphType.PIE3D
				|| themeGraph.getGraphType() == ThemeGraphType.ROSE || themeGraph.getGraphType() == ThemeGraphType.ROSE3D) {
			this.spinnerStartAngle.setEnabled(true);
		}
	}

	private void initPanelStyleOfBAR(JPanel panelStyleOfBAR) {
		// @formatter:off
		this.spinnerBarWidth.setEnabled(false);
		panelStyleOfBAR.setLayout(new GridBagLayout());
		panelStyleOfBAR.add(this.labelBarWidth,   new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 10).setWeight(30, 1).setIpad(20, 0));
		this.spinnerBarWidth.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(0.01)));
		panelStyleOfBAR.add(this.spinnerBarWidth, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setWeight(50, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.spinnerBarWidth.setPreferredSize(new Dimension((int) this.buttonDraftLine.getPreferredSize().getWidth(), 23));
		initSpinnerBarWidthState();
		// @formatter:on
	}

	private void initSpinnerBarWidthState() {
		if (themeGraph.getGraphType() == ThemeGraphType.BAR || themeGraph.getGraphType() == ThemeGraphType.BAR3D
				|| themeGraph.getGraphType() == ThemeGraphType.STACK_BAR || themeGraph.getGraphType() == ThemeGraphType.STACK_BAR3D) {
			this.spinnerBarWidth.setEnabled(true);
		}
	}

	private void initpanelAxis(JPanel panelAxis) {
		initComboxAxisModel();
		// @formatter:off
		panelAxis.setLayout(new GridBagLayout());
		this.buttonAxisColor = new ColorSelectButton(Color.gray);
		panelAxis.add(this.labelAxisColor,       new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 2, 10).setWeight(30, 1));
		panelAxis.add(this.buttonAxisColor,      new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 2, 10).setWeight(70, 1).setFill(GridBagConstraints.HORIZONTAL));
		panelAxis.add(this.labelAxisModel,       new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 2, 10).setWeight(30, 1));
		panelAxis.add(this.comboBoxAxisModel,    new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 2, 10).setWeight(70, 1).setFill(GridBagConstraints.HORIZONTAL));
		panelAxis.add(this.labelAxisStyle,       new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 2, 10).setWeight(30, 1));
		panelAxis.add(this.buttonAxisStyle,      new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(70, 1).setFill(GridBagConstraints.HORIZONTAL));
		panelAxis.add(this.checkBoxShowAxisGrid, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(30, 1));
		this.checkBoxShowAxisGrid.setSelected(themeGraph.isAxesGridDisplayed());
		initCheckBoxAxisState();
		setPanelAxisEnabled(false);
		this.buttonAxisStyle.setEnabled(false);
		if (themeGraph.isAxesDisplayed()) {
			this.checkBoxAxis.setSelected(true);
			this.checkBoxAxis.setEnabled(true);
			setPanelAxisEnabled(true);
			if (themeGraph.getAxesTextDisplayMode() != GraphAxesTextDisplayMode.NONE) {
				this.buttonAxisStyle.setEnabled(true);
			}
		}
		// @formatter:on
	}

	private void initCheckBoxAxisState() {
		if (themeGraph.getGraphType() != ThemeGraphType.PIE || themeGraph.getGraphType() != ThemeGraphType.PIE3D
				|| themeGraph.getGraphType() != ThemeGraphType.ROSE || themeGraph.getGraphType() != ThemeGraphType.ROSE3D
				|| themeGraph.getGraphType() != ThemeGraphType.RING) {
			this.checkBoxAxis.setEnabled(false);
		} else {
			this.checkBoxAxis.setEnabled(true);
		}
	}

	private void setPanelAxisEnabled(boolean b) {
		this.buttonAxisColor.setEnabled(b);
		this.comboBoxAxisModel.setEnabled(b);
		this.checkBoxShowAxisGrid.setEnabled(b);
	}

	private void initComboxAxisModel() {
		this.comboBoxAxisModel.setModel(new DefaultComboBoxModel<String>(new String[] { MapViewProperties.getString("String_AxesUnDisplay"),
				MapViewProperties.getString("String_YAxesDisplay"), MapViewProperties.getString("String_XYAcesDisplay") }));
		if (themeGraph.getAxesTextDisplayMode() == GraphAxesTextDisplayMode.NONE) {
			this.comboBoxAxisModel.setSelectedIndex(0);
			return;
		}
		if (themeGraph.getAxesTextDisplayMode() == GraphAxesTextDisplayMode.YAXES) {
			this.comboBoxAxisModel.setSelectedIndex(1);
			return;
		}
		if (themeGraph.getAxesTextDisplayMode() == GraphAxesTextDisplayMode.ALL) {
			this.comboBoxAxisModel.setSelectedIndex(2);
			return;
		}
	}

	private void initpanelRemark(JPanel panelRemark) {
		initComboBoxRemarkFormat();
		// @formatter:off
		panelRemark.setLayout(new GridBagLayout());
		panelRemark.add(this.labelRemarkFormat,   new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 2, 10).setWeight(40, 1).setIpad(20, 0));
		panelRemark.add(this.comboBoxRemarkFormat,new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 2, 10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		panelRemark.add(this.labelRemarkStyle,    new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(40, 1).setIpad(20, 0));
		panelRemark.add(this.buttonRemarkStyle,   new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		if (themeGraph.isGraphTextDisplayed()) {
			this.checkBoxRemark.setSelected(true);
			this.checkBoxRemark.setEnabled(true);
			this.comboBoxRemarkFormat.setEnabled(true);
			this.buttonRemarkStyle.setEnabled(true);
		}
		// @formatter:on
	}

	private void initComboBoxRemarkFormat() {
		this.comboBoxRemarkFormat.setModel(new DefaultComboBoxModel<String>(new String[] { MapViewProperties.getString("String_ThemeGraphTextFormat_Percent"),
				MapViewProperties.getString("String_ThemeGraphTextFormat_Value"), MapViewProperties.getString("String_ThemeGraphTextFormat_Caption"),
				MapViewProperties.getString("String_ThemeGraphTextFormat_CaptionPercent"),
				MapViewProperties.getString("String_ThemeGraphTextFormat_CaptionValue"), }));
		this.comboBoxRemarkFormat.setEnabled(false);
		this.buttonRemarkStyle.setEnabled(false);
		if (themeGraph.getGraphTextFormat() == ThemeGraphTextFormat.PERCENT) {
			this.comboBoxRemarkFormat.setSelectedIndex(0);
			return;
		}
		if (themeGraph.getGraphTextFormat() == ThemeGraphTextFormat.VALUE) {
			this.comboBoxRemarkFormat.setSelectedIndex(1);
			return;
		}
		if (themeGraph.getGraphTextFormat() == ThemeGraphTextFormat.CAPTION) {
			this.comboBoxRemarkFormat.setSelectedIndex(2);
			return;
		}
		if (themeGraph.getGraphTextFormat() == ThemeGraphTextFormat.CAPTION_PERCENT) {
			this.comboBoxRemarkFormat.setSelectedIndex(3);
			return;
		}
		if (themeGraph.getGraphTextFormat() == ThemeGraphTextFormat.CAPTION_VALUE) {
			this.comboBoxRemarkFormat.setSelectedIndex(4);
			return;
		}
	}

	private void initpanelParameterSetting(JPanel panelParameterSetting) {
		initComboBoxOffsetUnity();
		initComboBoxOffsetX();
		initComboBoxOffsetY();
		// @formatter:off
		GridBagLayout layout = new GridBagLayout();
		panelParameterSetting.setLayout(layout);
		this.comboBoxOffsetUnity.setPreferredSize(new Dimension(140,23));
		Dimension textDimension = new Dimension(100,23);
		this.comboBoxOffsetX.setPreferredSize(textDimension);
		this.comboBoxOffsetY.setPreferredSize(textDimension);
		panelParameterSetting.add(this.labelOffsetUnity,    new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(50, 0).setInsets(5,10,5,0));
		panelParameterSetting.add(this.comboBoxOffsetUnity, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(50, 0).setInsets(5,10,5,10).setFill(GridBagConstraints.HORIZONTAL));
		panelParameterSetting.add(this.labelOffsetX,        new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(50, 0).setInsets(0,10,5,0));
		panelParameterSetting.add(this.comboBoxOffsetX,     new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(45, 0).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		panelParameterSetting.add(this.labelOffsetXUnity,   new GridBagConstraintsHelper(3, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(5, 0).setInsets(0,10,5,10));
		panelParameterSetting.add(this.labelOffsetY,        new GridBagConstraintsHelper(0, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(50, 0).setInsets(0,10,5,0));
		panelParameterSetting.add(this.comboBoxOffsetY,     new GridBagConstraintsHelper(2, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(45, 0).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		panelParameterSetting.add(this.labelOffsetYUnity,   new GridBagConstraintsHelper(3, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(5, 0).setInsets(0,10,5,10));
		// @formatter:on
	}

	/**
	 * 初始化偏移量单位
	 */
	private void initComboBoxOffsetUnity() {
		this.comboBoxOffsetUnity.setModel(new DefaultComboBoxModel<String>(new String[] {
				MapViewProperties.getString("String_MapBorderLineStyle_LabelDistanceUnit"), MapViewProperties.getString("String_ThemeLabelOffsetUnit_Map") }));
		if (this.themeGraph.isOffsetFixed()) {
			this.comboBoxOffsetUnity.setSelectedIndex(0);
		} else {
			this.comboBoxOffsetUnity.setSelectedIndex(1);
			this.labelOffsetXUnity.setText(UnitValue.parseToString(map.getCoordUnit()));
			this.labelOffsetYUnity.setText(UnitValue.parseToString(map.getCoordUnit()));
		}
	}

	/**
	 * 初始化水平偏移量
	 */
	private void initComboBoxOffsetX() {
		this.comboBoxOffsetX.setEditable(true);
		ThemeUtil.getFieldComboBox(comboBoxOffsetX, datasetVector, themeGraphLayer.getDisplayFilter().getJoinItems(), comboBoxArrayForOffsetX, true);
		this.comboBoxOffsetX.addItem("0");
		String offsetX = themeGraph.getOffsetX();
		if (StringUtilties.isNullOrEmpty(offsetX)) {
			offsetX = "0";
		}
		this.comboBoxOffsetX.setSelectedItem(offsetX);
		if (!offsetX.equals(this.comboBoxOffsetX.getSelectedItem())) {
			this.comboBoxOffsetX.addItem(offsetX);
			this.comboBoxOffsetX.setSelectedItem(offsetX);
		}

	}

	/**
	 * 初始化垂直偏移量
	 */
	private void initComboBoxOffsetY() {
		this.comboBoxOffsetY.setEditable(true);
		ThemeUtil.getFieldComboBox(comboBoxOffsetY, datasetVector, themeGraphLayer.getDisplayFilter().getJoinItems(), comboBoxArrayForOffsetY, true);
		this.comboBoxOffsetY.addItem("0");
		String offsetY = themeGraph.getOffsetY();
		if (StringUtilties.isNullOrEmpty(offsetY)) {
			offsetY = "0";
		}
		this.comboBoxOffsetY.setSelectedItem(offsetY);
		if (!offsetY.equals(this.comboBoxOffsetY.getSelectedItem())) {
			this.comboBoxOffsetY.addItem(offsetY);
			this.comboBoxOffsetY.setSelectedItem(offsetY);
		}

	}

	private void initpanelSizeLimite(JPanel panelSizeLimite) {
		this.textFieldMaxValue.setText(String.valueOf(this.themeGraph.getMaxGraphSize()));
		this.textFieldMinValue.setText(String.valueOf(this.themeGraph.getMinGraphSize()));
		// @formatter:off
		panelSizeLimite.setLayout(new GridBagLayout());
		panelSizeLimite.add(this.labelMaxValue,    new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 10).setWeight(20, 1).setIpad(20, 0));
		panelSizeLimite.add(this.textFieldMaxValue,new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		panelSizeLimite.add(this.labelMinValue,    new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(20, 1).setIpad(20, 0));
		panelSizeLimite.add(this.textFieldMinValue,new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.textFieldMaxValue.setPreferredSize(new Dimension((int) this.buttonDraftLine.getPreferredSize().getWidth(), 23));
		this.textFieldMinValue.setPreferredSize(new Dimension((int) this.buttonDraftLine.getPreferredSize().getWidth(), 23));
		// @formatter:on
	}

	private void initPanelOptions(JPanel panelOptions) {
		this.buttonDraftLine.setEnabled(false);
		initCheckBoxStation();
		// @formatter:off
		panelOptions.setLayout(new GridBagLayout());
		panelOptions.add(this.checkBoxShowFlow,     new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 10).setWeight(20, 1).setIpad(20, 0));
		panelOptions.add(this.checkBoxShowNegative, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 10).setWeight(60, 1));
		panelOptions.add(this.checkBoxAutoAvoid,    new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(20, 1).setIpad(20, 0));
		panelOptions.add(this.checkBoxAutoScale,    new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(60, 1));
		panelOptions.add(this.checkboxDraftLine,    new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(20, 1).setIpad(20, 0));
		panelOptions.add(this.buttonDraftLine,      new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		// @formatter:on
	}

	private void initCheckBoxStation() {
		this.checkBoxShowFlow.setSelected(this.themeGraph.isFlowEnabled());
		this.checkBoxShowNegative.setSelected(this.themeGraph.isNegativeDisplayed());
		this.checkBoxAutoAvoid.setSelected(this.themeGraph.isOverlapAvoided());
		this.checkBoxAutoScale.setSelected(!this.themeGraph.isGraphSizeFixed());
		this.checkboxDraftLine.setSelected(this.themeGraph.isLeaderLineDisplayed());
		this.buttonDraftLine.setEnabled(this.themeGraph.isLeaderLineDisplayed());
	}

	private void initPanelProperty() {
		this.panelProperty.setLayout(new GridBagLayout());
		initToolbar();
		initComboBoxGraphType();
		initComboBoxMethod();
		// @formatter:off
		this.panelProperty.add(this.labelColorStyle,  new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 10).setWeight(20, 0).setIpad(40, 0));
		this.panelProperty.add(this.comboBoxColor,    new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelProperty.add(this.labelGraphType,   new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(20, 0).setIpad(40, 0));
		this.panelProperty.add(this.comboBoxGraphType,new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelProperty.add(this.labelMethod,      new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(20, 0).setIpad(40, 0));
		this.panelProperty.add(this.comboBoxMethod,   new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelProperty.add(this.toolbar,          new GridBagConstraintsHelper(0, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(100, 0));
		this.panelProperty.add(this.scollPane,        new GridBagConstraintsHelper(0, 4, 2, 1).setAnchor(GridBagConstraints.NORTH).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.BOTH).setWeight(100, 3));
		getTable();
		this.scollPane.setViewportView(this.tableGraphInfo);
		// @formatter:on
	}

	private JTable getTable() {
		this.graphCount = this.themeGraph.getCount();
		this.tableGraphInfo.setModel(new LocalDefualTableModel(new Object[this.graphCount][3], this.nameStrings));
		initTableColumns();
		this.tableGraphInfo.setRowHeight(25);
		this.tableGraphInfo.getColumn(MapViewProperties.getString("String_ThemeGraphItemManager_ClmExpression")).setMaxWidth(200);
		this.tableGraphInfo.getColumn(MapViewProperties.getString("String_Title_Sytle")).setMaxWidth(100);
		this.tableGraphInfo.getColumn(MapViewProperties.getString("String_ThemeGraphTextFormat_Caption")).setMaxWidth(200);
		this.tableGraphInfo.getModel().removeTableModelListener(this.graphCaptionChangeListener);
		this.tableGraphInfo.getModel().addTableModelListener(this.graphCaptionChangeListener);
		return this.tableGraphInfo;
	}

	@SuppressWarnings("unchecked")
	private void initTableColumns() {
		TableRowCellEditor rowEditor = new TableRowCellEditor(tableGraphInfo);
		for (int i = 0; i < this.graphCount; i++) {
			ThemeGraphItem item = themeGraph.getItem(i);
			String expression = item.getGraphExpression();
			if (expression.indexOf(".") > 0 && expression.substring(0, expression.indexOf(".")).equals(datasetVector.getName())
					&& expression.split("\\.").length == 2) {
				expression = expression.substring(expression.indexOf(".") + 1, expression.length());
			}
			this.tableGraphInfo.setValueAt(expression, i, TABLE_COLUMN_EXPRESSION);
			GeoStyle geoStyle = item.getUniformStyle();
			this.tableGraphInfo.setValueAt(ThemeItemLabelDecorator.buildGraphIcon(geoStyle), i, TABLE_COLUMN_STYLE);
			this.tableGraphInfo.setValueAt(item.getCaption(), i, TABLE_COLUMN_CAPTION);
			fieldComboBox = new SteppedComboBox(new String[] {});
			fieldComboBox.removeAllItems();
			ThemeUtil.getFieldComboBox(fieldComboBox, datasetVector, themeGraphLayer.getDisplayFilter().getJoinItems(), comboBoxArray, true);
			removeRepeatItem(fieldComboBox, expression);
			Dimension d = fieldComboBox.getPreferredSize();
			fieldComboBox.setPreferredSize(new Dimension(d.width, d.height));
			fieldComboBox.setPopupWidth(d.width);
			rowEditor.setEditorAt(i, new DefaultCellEditor(fieldComboBox));
			TableColumn expressionColumn = tableGraphInfo.getColumn(MapViewProperties.getString("String_ThemeGraphItemManager_ClmExpression"));
			expressionColumn.setCellEditor(rowEditor);
		}
	}

	private void removeRepeatItem(SteppedComboBox comboBox, String expression) {
		for (int i = 0; i < themeGraph.getCount(); i++) {
			String tempExpression = themeGraph.getItem(i).getGraphExpression();
			if (expression.indexOf(".") > 0 && tempExpression.substring(0, tempExpression.indexOf(".")).equals(datasetVector.getName())) {
				comboBox.removeItem(tempExpression.substring(tempExpression.indexOf(".") + 1, tempExpression.length()));
			} else {
				comboBox.removeItem(tempExpression);
			}
		}
		comboBox.addItem(expression);
		comboBox.removeItem(MapViewProperties.getString("String_Combobox_Expression"));
	}

	class CaptionChangeListener implements TableModelListener {

		@Override
		public void tableChanged(TableModelEvent e) {
			int selectRow = e.getFirstRow();
			int selectColumn = e.getColumn();
			if (selectColumn == TABLE_COLUMN_CAPTION) {
				String caption = tableGraphInfo.getValueAt(selectRow, selectColumn).toString();
				if (!StringUtilties.isNullOrEmptyString(caption)) {
					// 如果输入为数值且段值合法时修改段值
					setGraphItemCaption(selectRow, caption);
				} else {
					caption = themeGraph.getItem(selectRow).getCaption();
					tableGraphInfo.setValueAt(caption, selectRow, TABLE_COLUMN_CAPTION);
				}
			}
			if (selectColumn == TABLE_COLUMN_EXPRESSION) {
				String graphExpression = themeGraph.getItem(selectRow).getGraphExpression();
				String expression = tableGraphInfo.getValueAt(selectRow, selectColumn).toString();
				if (!StringUtilties.isNullOrEmptyString(expression) && !expression.equalsIgnoreCase(graphExpression)) {
					ThemeGraphItem item = themeGraph.getItem(selectRow);
					if (expression.contains(".")) {
						item.setGraphExpression(expression);
					} else {
						item.setGraphExpression(datasetVector.getName() + "." + expression);
					}
					item.setCaption(expression);
					resetMapSize();
					getTable();
					refreshMapAtOnce();
					tableGraphInfo.addRowSelectionInterval(selectRow, selectRow);
				}
			}
		}
	}

	private void resetMapSize() {
		// 重置最大最小值来适应地图
		Point pointStart = new Point(0, 0);
		Point pointEnd = new Point(0, (int) (ThemeGuideFactory.getMapControl().getSize().getWidth() / 5));
		Point2D point2DStart = ThemeGuideFactory.getMapControl().getMap().pixelToMap(pointStart);
		Point2D point2DEnd = ThemeGuideFactory.getMapControl().getMap().pixelToMap(pointEnd);
		this.themeGraph.setMaxGraphSize(Math.sqrt(Math.pow(point2DEnd.getX() - point2DStart.getX(), 2) + Math.pow(point2DEnd.getY() - point2DStart.getY(), 2)));
		this.themeGraph.setBarWidth(themeGraph.getMaxGraphSize() / 10);
		this.textFieldMaxValue.setText(String.valueOf(themeGraph.getMaxGraphSize()));
		this.spinnerBarWidth.setValue(themeGraph.getBarWidth());
	}

	class LocalDefualTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;

		public LocalDefualTableModel(Object[][] obj, String[] name) {
			super(obj, name);
		}

		@Override
		public Class getColumnClass(int column) {
			// 要这样定义table，要重写这个方法(0,0)的意思就是别的格子的类型都跟(0,0)的一样。
			if (TABLE_COLUMN_STYLE == column) {
				return ImageIcon.class;
			}
			return String.class;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if (columnIndex == TABLE_COLUMN_EXPRESSION || columnIndex == TABLE_COLUMN_CAPTION) {
				return true;
			}
			return false;
		}
	}

	private void initComboBoxMethod() {
		this.comboBoxMethod.setModel(new DefaultComboBoxModel<String>(new String[] { MapViewProperties.getString("String_GraduatedMode_Constant"),
				MapViewProperties.getString("String_GraduatedMode_Logarithm"), MapViewProperties.getString("String_GraduatedMode_SquareRoot") }));
		if (this.themeGraph.getGraduatedMode() == GraduatedMode.CONSTANT) {
			this.comboBoxMethod.setSelectedIndex(0);
			return;
		}
		if (this.themeGraph.getGraduatedMode() == GraduatedMode.LOGARITHM) {
			this.comboBoxMethod.setSelectedIndex(1);
			return;
		}
		if (this.themeGraph.getGraduatedMode() == GraduatedMode.SQUAREROOT) {
			this.comboBoxMethod.setSelectedIndex(2);
			return;
		}
	}

	private void setGraphItemCaption(int selectRow, String caption) {
		ThemeGraphItem item = themeGraph.getItem(selectRow);
		item.setCaption(caption);
		refreshMapAtOnce();
	}

	private void initComboBoxGraphType() {
		this.comboBoxGraphType.setModel(new DefaultComboBoxModel<String>(new String[] { MapViewProperties.getString("String_GraphType_Area"),
				MapViewProperties.getString("String_GraphType_Step"), MapViewProperties.getString("String_GraphType_Line"),
				MapViewProperties.getString("String_GraphType_Point"), MapViewProperties.getString("String_GraphType_Bar"),
				MapViewProperties.getString("String_GraphType_Bar3D"), MapViewProperties.getString("String_GraphType_Pie"),
				MapViewProperties.getString("String_GraphType_Pie3D"), MapViewProperties.getString("String_GraphType_Rose"),
				MapViewProperties.getString("String_GraphType_Rose3D"), MapViewProperties.getString("String_GraphType_StackedBar"),
				MapViewProperties.getString("String_GraphType_StackedBar3D"), MapViewProperties.getString("String_GraphType_Ring") }));
		if (this.themeGraph.getGraphType() == ThemeGraphType.AREA) {
			this.comboBoxGraphType.setSelectedIndex(0);
		}
		if (this.themeGraph.getGraphType() == ThemeGraphType.STEP) {
			this.comboBoxGraphType.setSelectedIndex(1);
		}
		if (this.themeGraph.getGraphType() == ThemeGraphType.LINE) {
			this.comboBoxGraphType.setSelectedIndex(2);
		}
		if (this.themeGraph.getGraphType() == ThemeGraphType.POINT) {
			this.comboBoxGraphType.setSelectedIndex(3);
		}
		if (this.themeGraph.getGraphType() == ThemeGraphType.BAR) {
			this.comboBoxGraphType.setSelectedIndex(4);
		}
		if (this.themeGraph.getGraphType() == ThemeGraphType.BAR3D) {
			this.comboBoxGraphType.setSelectedIndex(5);
		}
		if (this.themeGraph.getGraphType() == ThemeGraphType.PIE) {
			this.comboBoxGraphType.setSelectedIndex(6);
		}
		if (this.themeGraph.getGraphType() == ThemeGraphType.PIE3D) {
			this.comboBoxGraphType.setSelectedIndex(7);
		}
		if (this.themeGraph.getGraphType() == ThemeGraphType.ROSE) {
			this.comboBoxGraphType.setSelectedIndex(8);
		}
		if (this.themeGraph.getGraphType() == ThemeGraphType.ROSE3D) {
			this.comboBoxGraphType.setSelectedIndex(9);
		}
		if (this.themeGraph.getGraphType() == ThemeGraphType.STACK_BAR) {
			this.comboBoxGraphType.setSelectedIndex(10);
		}
		if (this.themeGraph.getGraphType() == ThemeGraphType.STACK_BAR3D) {
			this.comboBoxGraphType.setSelectedIndex(11);
		}
		if (this.themeGraph.getGraphType() == ThemeGraphType.RING) {
			this.comboBoxGraphType.setSelectedIndex(12);
		}
	}

	private void initToolbar() {
		this.toolbar.setFloatable(false);
		this.toolbar.add(this.buttonDelete);
		this.toolbar.add(this.buttonAdd);
		this.toolbar.addSeparator();
		this.toolbar.add(this.buttonStyle);
		this.toolbar.addSeparator();
		this.toolbar.add(this.buttonMoveToFrist);
		this.toolbar.add(this.buttonMoveToForward);
		this.toolbar.add(this.buttonMoveToNext);
		this.toolbar.add(this.buttonMoveToLast);
		this.buttonDelete.setIcon(new ImageIcon(ThemeGraphContainer.class
				.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Delete.png")));
		this.buttonMoveToFrist.setIcon(InternalImageIconFactory.MOVE_TO_FRIST);
		this.buttonMoveToForward.setIcon(InternalImageIconFactory.MOVE_TO_FORWARD);
		this.buttonMoveToNext.setIcon(InternalImageIconFactory.MOVE_TO_NEXT);
		this.buttonMoveToLast.setIcon(InternalImageIconFactory.MOVE_TO_LAST);
		this.buttonAdd.setIcon(InternalImageIconFactory.ADD_ITEM);
		this.buttonStyle.setIcon(InternalImageIconFactory.REGION_STYLE);
		setToolBarButtonEnable(false);
		this.buttonAdd.setEnabled(true);
	}

	private void setToolBarButtonEnable(boolean enable) {
		this.buttonDelete.setEnabled(enable);
		this.buttonAdd.setEnabled(enable);
		this.buttonStyle.setEnabled(enable);
		this.buttonMoveToFrist.setEnabled(enable);
		this.buttonMoveToForward.setEnabled(enable);
		this.buttonMoveToLast.setEnabled(enable);
		this.buttonMoveToNext.setEnabled(enable);
	}

	@Override
	public void registActionListener() {
		unregistActionListener();
		this.tableGraphInfo.addMouseListener(this.localMouseListener);
		this.comboBoxGraphType.addItemListener(this.graphTypeChangeListener);
		this.comboBoxMethod.addItemListener(this.graphModeChangeListener);
		this.comboBoxColor.addItemListener(this.graphColorChangeListener);
		this.checkBoxShowFlow.addItemListener(this.OptionsChangeListener);
		this.checkBoxShowNegative.addItemListener(this.OptionsChangeListener);
		this.checkBoxAutoAvoid.addItemListener(this.OptionsChangeListener);
		this.checkBoxAutoScale.addItemListener(this.OptionsChangeListener);
		this.checkboxDraftLine.addItemListener(this.OptionsChangeListener);
		this.buttonDraftLine.addActionListener(this.showLeaderLineAction);
		this.textFieldMaxValue.getDocument().addDocumentListener(this.graphSizeChangeListener);
		this.textFieldMinValue.getDocument().addDocumentListener(this.graphSizeChangeListener);
		this.comboBoxOffsetUnity.addItemListener(this.offsetFixedListener);
		this.comboBoxOffsetX.addItemListener(this.offsetxListener);
		this.comboBoxOffsetY.addItemListener(this.offsetYListener);
		this.checkBoxRemark.addItemListener(this.graphTextDisplayedListener);
		this.comboBoxRemarkFormat.addItemListener(this.graphTextFormatListener);
		this.buttonRemarkStyle.addActionListener(this.remarkStyleListener);
		this.checkBoxAxis.addItemListener(this.showAxisTextListener);
		this.buttonAxisColor.addPropertyChangeListener("m_selectionColors", this.axisColorListener);
		this.comboBoxAxisModel.addItemListener(this.axisModelListener);
		this.buttonAxisStyle.addActionListener(this.axisStyleListener);
		this.checkBoxShowAxisGrid.addItemListener(this.showAxisGridListener);
		this.spinnerBarWidth.addChangeListener(this.barWidthListener);
		this.spinnerStartAngle.addChangeListener(this.startAngleListener);
		this.spinnerRoseAngle.addChangeListener(this.roseAngleListener);
		this.buttonDelete.addActionListener(this.toolbarAction);
		this.buttonAdd.addActionListener(this.toolbarAction);
		this.buttonStyle.addActionListener(this.toolbarAction);
		this.buttonMoveToFrist.addActionListener(this.toolbarAction);
		this.buttonMoveToForward.addActionListener(this.toolbarAction);
		this.buttonMoveToNext.addActionListener(this.toolbarAction);
		this.buttonMoveToLast.addActionListener(this.toolbarAction);
		this.layersTree.addPropertyChangeListener("LayerChange", this.layersTreePropertyChangeListener);
		this.tableGraphInfo.addMouseListener(this.tableMouseListener);
		this.layersTree.addPropertyChangeListener("LayerPropertyChanged", this.layerPropertyChangeListener);
	}

	protected void setTextStyle(int x, int y, int styleType) {
		textStyleDialog.getTextStyleContainer().setTextStyleType(styleType);
		textStyleDialog.getTextStyleContainer().addPropertyChangeListener("ThemeChange", new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				firePropertyChange("ThemeChange", null, null);
			}
		});
		textStyleDialog.setRefreshAtOnce(isRefreshAtOnce);
		textStyleDialog.setLocation(x, y);
		textStyleDialog.setVisible(true);
	}

	private void refreshColor() {
		int colorCount = ((Colors) this.comboBoxColor.getSelectedItem()).getCount();
		Colors colors = (Colors) this.comboBoxColor.getSelectedItem();
		int themeGraphCount = this.themeGraph.getCount();
		if (themeGraphCount > 0) {
			float ratio = (1f * colorCount) / (1f * themeGraphCount);
			this.themeGraph.getItem(0).getUniformStyle().setFillForeColor(colors.get(0));
			this.themeGraph.getItem(themeGraphCount - 1).getUniformStyle().setFillForeColor(colors.get(colorCount - 1));
			for (int i = 1; i < themeGraphCount - 1; i++) {
				int colorIndex = Math.round(i * ratio);
				if (colorIndex == colorCount) {
					colorIndex--;
				}
				this.themeGraph.getItem(i).getUniformStyle().setFillForeColor(colors.get(colorIndex));
			}
		}

	}

	private void refreshMapAtOnce() {
		firePropertyChange("ThemeChange", null, null);
		if (isRefreshAtOnce) {
			refreshMapAndLayer();
		}
	}

	class LayerPropertyChangeListener implements PropertyChangeListener{

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (!themeGraphLayer.isDisposed()&&((Layer)evt.getNewValue()).getName().equals(themeGraphLayer.getName())) {
				getTable();
			}
		}
		
	}
	
	class ToolBarAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttonDelete) {
				int[] selectRows = tableGraphInfo.getSelectedRows();
				int selectRow = tableGraphInfo.getSelectedRow();
				for (int i = selectRows.length - 1; i >= 0; i--) {
					themeGraph.remove(selectRows[i]);
				}
				getTable();
				refreshMapAtOnce();
				if (tableGraphInfo.getRowCount() == 0) {
					setToolBarButtonEnable(false);
					// 重置最大，最小和柱宽度
					textFieldMaxValue.setText("0");
					textFieldMinValue.setText("0");
					spinnerBarWidth.setValue(0.0);
					buttonAdd.setEnabled(true);
				}
				if (selectRow != tableGraphInfo.getRowCount() && tableGraphInfo.getRowCount() > 0) {
					tableGraphInfo.addRowSelectionInterval(selectRow, selectRow);
				} else if (tableGraphInfo.getRowCount() > 0) {
					tableGraphInfo.addRowSelectionInterval(tableGraphInfo.getRowCount() - 1, tableGraphInfo.getRowCount() - 1);
				}
				return;
			}
			if (e.getSource() == buttonAdd) {
				addGraphItem();
			}
			if (e.getSource() == buttonStyle) {
				setItemGeoSytle();
				refreshMapAtOnce();
				return;
			}
			if (e.getSource() == buttonMoveToFrist) {
				int[] selectRow = tableGraphInfo.getSelectedRows();
				if (selectRow.length == 1) {
					themeGraph.exchangeItem(selectRow[0], 0);
				} else {
					for (int i = 0; i < selectRow.length; i++) {
						themeGraph.exchangeItem(selectRow[i], i);
					}
				}
				getTable();
				refreshMapAtOnce();
				for (int i = 0; i < selectRow.length; i++) {
					tableGraphInfo.addRowSelectionInterval(i, i);
				}
				return;
			}
			if (e.getSource() == buttonMoveToForward) {
				int[] selectRows = tableGraphInfo.getSelectedRows();
				for (int i = 0; i < selectRows.length; i++) {
					if (selectRows[i] != 0) {
						themeGraph.exchangeItem(selectRows[i], selectRows[i] - 1);
					}
				}
				getTable();
				refreshMapAtOnce();
				for (int i = 0; i < selectRows.length; i++) {
					if (selectRows[i] != 0) {
						tableGraphInfo.addRowSelectionInterval(selectRows[i] - 1, selectRows[i] - 1);
					} else {
						tableGraphInfo.addRowSelectionInterval(selectRows[i], selectRows[i]);
					}
				}
				return;
			}
			if (e.getSource() == buttonMoveToNext) {
				int[] selectRows = tableGraphInfo.getSelectedRows();
				for (int i = selectRows.length - 1; i >= 0; i--) {
					if (selectRows[i] != tableGraphInfo.getRowCount() - 1) {
						themeGraph.exchangeItem(selectRows[i], selectRows[i] + 1);
					}
				}
				getTable();
				refreshMapAtOnce();
				for (int i = 0; i < selectRows.length; i++) {
					if (selectRows[i] != tableGraphInfo.getRowCount() - 1) {
						tableGraphInfo.addRowSelectionInterval(selectRows[i] + 1, selectRows[i] + 1);
					} else {
						tableGraphInfo.addRowSelectionInterval(selectRows[i], selectRows[i]);
					}
				}
				return;
			}
			if (e.getSource() == buttonMoveToLast) {
				int[] selectRow = tableGraphInfo.getSelectedRows();
				if (selectRow.length == 1) {
					themeGraph.exchangeItem(selectRow[0], tableGraphInfo.getRowCount() - 1);
				} else {
					for (int i = 0; i < selectRow.length; i++) {
						themeGraph.exchangeItem(selectRow[i], tableGraphInfo.getRowCount() - selectRow.length + i);
					}
				}
				getTable();
				refreshMapAtOnce();
				for (int i = 0; i < selectRow.length; i++) {
					tableGraphInfo.addRowSelectionInterval(tableGraphInfo.getRowCount() - i - 1, tableGraphInfo.getRowCount() - i - 1);
				}
				return;
			}
		}

		private void addGraphItem() {
			int selectRow = tableGraphInfo.getSelectedRow();
			int x = (int) (buttonAdd.getLocationOnScreen().getX());
			int y = (int) (buttonAdd.getLocationOnScreen().getY() + buttonAdd.getHeight());
			ArrayList<String> existItems = new ArrayList<String>();
			for (int i = 0; i < themeGraph.getCount(); i++) {
				existItems.add(themeGraph.getItem(i).getGraphExpression());
			}
			ThemeGraphAddItemDialog addItemDialog = new ThemeGraphAddItemDialog(datasetVector,themeGraphLayer.getDisplayFilter().getJoinItems(),existItems);
			addItemDialog.setLocation(x, y);
			addItemDialog.setVisible(true);
			if (addItemDialog.getDialogResult() == DialogResult.OK) {
				ArrayList<String> tempList = addItemDialog.getResultList();
				for (int i = 0; i < tempList.size(); i++) {
					ThemeGraphItem item = new ThemeGraphItem();
					String graphExpression = tempList.get(i);
					if (!graphExpression.contains(".")) {
						graphExpression = datasetVector.getName()+"."+graphExpression;
					}
					String caption = getCaption(graphExpression);
					item.setGraphExpression(graphExpression);
					item.setCaption(caption);
					GeoStyle newGeoStyle = new GeoStyle();
					Colors colors = (Colors) comboBoxColor.getSelectedItem();
					int colorCount = colors.getCount();
					Random random = new Random();
					int randomCount = random.nextInt(colorCount);
					newGeoStyle.setFillForeColor(colors.get((randomCount)));
					newGeoStyle.setLineWidth(0.1);
					item.setUniformStyle(newGeoStyle);
					if (!itemExist(item, existItems)) {
						themeGraph.add(item);
						setToolBarButtonEnable(true);
					}
				}
				resetMapSize();
				getTable();
				refreshMapAtOnce();
				if (selectRow >= 0) {
					tableGraphInfo.addRowSelectionInterval(selectRow, selectRow);
				} else {
					tableGraphInfo.addRowSelectionInterval(0, 0);
				}

			}
		}

		private boolean itemExist(ThemeGraphItem item, ArrayList<String> existItems) {
			for (int i = 0; i < existItems.size(); i++) {
				if (existItems.get(i).equals(item.getGraphExpression())) {
					return true;
				}
			}
			return false;
		}

		private String getCaption(String graphExpression) {
			String caption = graphExpression;
			if (graphExpression.indexOf(".") > 0) {
				String[] info = graphExpression.split("\\.");
				if (info.length == 2) {
					caption = graphExpression.substring(graphExpression.indexOf(".") + 1, graphExpression.length());
				}
			}
			return caption;
		}
	}

	class TableMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 1 && tableGraphInfo.getSelectedRow() >= 0) {
				setToolBarButtonEnable(true);

			}
		}
	}

	class RoseAngleListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			double roseAngle = (double) spinnerRoseAngle.getValue();
			themeGraph.setRoseAngle(roseAngle);
			refreshMapAtOnce();
		}
	}

	class StartAngleListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			double startAngle = (double) spinnerStartAngle.getValue();
			themeGraph.setStartAngle(startAngle);
			refreshMapAtOnce();
		}
	}

	class BarWidthListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			double barWidth = (double) spinnerBarWidth.getValue();
			themeGraph.setBarWidth(barWidth);
			refreshMapAtOnce();
		}
	}

	class ShowAxisGridListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			boolean showAxisGrid = checkBoxShowAxisGrid.isSelected();
			themeGraph.setAxesGridDisplayed(showAxisGrid);
			refreshMapAtOnce();
		}
	}

	class AxisStyleListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int width = buttonAxisStyle.getWidth();
			int height = buttonAxisStyle.getHeight();
			int x = (int) (buttonAxisStyle.getLocationOnScreen().x - 0.8 * width);
			int y = buttonAxisStyle.getLocationOnScreen().y - 8 * height;
			textStyleDialog = new TextStyleDialog(themeGraph.getAxesTextStyle(), map, themeGraphLayer);
			textStyleDialog.setTitle(MapViewProperties.getString("String_SetAxisStyle"));
			setTextStyle(x, y, 1);
		}
	}

	class AxisModelListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			int axisModel = comboBoxAxisModel.getSelectedIndex();
			switch (axisModel) {
			case 0:
				themeGraph.setAxesTextDisplayMode(GraphAxesTextDisplayMode.NONE);
				buttonAxisStyle.setEnabled(false);
				refreshMapAtOnce();
				break;
			case 1:
				themeGraph.setAxesTextDisplayMode(GraphAxesTextDisplayMode.YAXES);
				buttonAxisStyle.setEnabled(true);
				refreshMapAtOnce();
				break;
			case 2:
				themeGraph.setAxesTextDisplayMode(GraphAxesTextDisplayMode.ALL);
				buttonAxisStyle.setEnabled(true);
				refreshMapAtOnce();
				break;
			default:
				break;
			}
		}
	}

	class AxisColorListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			Color axisColor = buttonAxisColor.getColor();
			themeGraph.setAxesColor(axisColor);
			refreshMapAtOnce();
		}
	}

	class ShowAxisTextListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			try {
				boolean showAxis = checkBoxAxis.isSelected();
				themeGraph.setAxesDisplayed(showAxis);
				setPanelAxisEnabled(showAxis);
				if (!showAxis) {
					buttonAxisStyle.setEnabled(false);
				} else if (0 != comboBoxAxisModel.getSelectedIndex()) {
					buttonAxisStyle.setEnabled(true);
				}
				refreshMapAtOnce();
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
		}
	}

	class RemarkStyleListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int width = buttonRemarkStyle.getWidth();
			int height = buttonRemarkStyle.getHeight();
			int x = (int) (buttonRemarkStyle.getLocationOnScreen().x - width);
			int y = buttonRemarkStyle.getLocationOnScreen().y - 3 * height;
			textStyleDialog = new TextStyleDialog(themeGraph.getGraphTextStyle(), map, themeGraphLayer);
			textStyleDialog.setTitle(MapViewProperties.getString("String_SetRemarkStyle"));
			setTextStyle(x, y, 0);
		}
	}

	class GraphTextFormatListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			setRemarkFormat();
			refreshMapAtOnce();
		}

		private void setRemarkFormat() {
			int remarkFormat = comboBoxRemarkFormat.getSelectedIndex();
			switch (remarkFormat) {
			case 0:
				themeGraph.setGraphTextFormat(ThemeGraphTextFormat.PERCENT);
				break;
			case 1:
				themeGraph.setGraphTextFormat(ThemeGraphTextFormat.VALUE);
				break;
			case 2:
				themeGraph.setGraphTextFormat(ThemeGraphTextFormat.CAPTION);
				break;
			case 3:
				themeGraph.setGraphTextFormat(ThemeGraphTextFormat.CAPTION_PERCENT);
				break;
			case 4:
				themeGraph.setGraphTextFormat(ThemeGraphTextFormat.CAPTION_VALUE);
				break;
			default:
				break;
			}
		}

	}

	class GraphTextDisplayedListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			boolean selected = checkBoxRemark.isSelected();
			themeGraph.setGraphTextDisplayed(selected);
			comboBoxRemarkFormat.setEnabled(selected);
			buttonRemarkStyle.setEnabled(selected);
			refreshMapAtOnce();
		}
	}

	class OffsetYListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				String tempOffsetY = themeGraph.getOffsetY();
				if (StringUtilties.isNullOrEmptyString(tempOffsetY)) {
					tempOffsetY = "0";
				}
				Dataset[] datasets = ThemeUtil.getDatasets(themeGraphLayer, datasetVector);
				ThemeUtil.getSqlExpression(comboBoxOffsetY, datasets, comboBoxArrayForOffsetY, tempOffsetY, true);
				String offsetY = comboBoxOffsetY.getSelectedItem().toString();
				if (offsetY.contains(".")) {
					themeGraph.setOffsetY(offsetY);
				} else {
					themeGraph.setOffsetY(datasetVector.getName() + "." + offsetY);
				}
				refreshMapAtOnce();
			}
		}
	}

	class OffsetXListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				String tempOffsetX = themeGraph.getOffsetX();
				if (StringUtilties.isNullOrEmptyString(tempOffsetX)) {
					tempOffsetX = "0";
				}
				Dataset[] datasets = ThemeUtil.getDatasets(themeGraphLayer, datasetVector);
				ThemeUtil.getSqlExpression(comboBoxOffsetX, datasets, comboBoxArrayForOffsetX, tempOffsetX, true);
				String offsetX = comboBoxOffsetX.getSelectedItem().toString();
				if (offsetX.contains(".")) {
					themeGraph.setOffsetY(offsetX);
				} else {
					themeGraph.setOffsetY(datasetVector.getName() + "." + offsetX);
				}
				refreshMapAtOnce();
			}
		}
	}

	class OffsetFixedListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				// 修改偏移量单位
				setOffsetUnity();
				refreshMapAtOnce();
			}
		}

		private void setOffsetUnity() {
			int offsetUnity = comboBoxOffsetUnity.getSelectedIndex();
			switch (offsetUnity) {
			case 0:
				themeGraph.setOffsetFixed(true);
				labelOffsetXUnity.setText(MapViewProperties.getString("String_Combobox_MM"));
				labelOffsetYUnity.setText(MapViewProperties.getString("String_Combobox_MM"));
				break;
			case 1:
				themeGraph.setOffsetFixed(false);
				labelOffsetXUnity.setText(UnitValue.parseToString(map.getCoordUnit()));
				labelOffsetYUnity.setText(UnitValue.parseToString(map.getCoordUnit()));
				break;
			default:
				break;
			}
		}
	}

	class GraphSizeChangeListener implements DocumentListener {
		private void setGraphMaxValue() {
			String maxValue = textFieldMaxValue.getText();
			if (StringUtilties.isNumber(maxValue) && Double.compare(Double.parseDouble(maxValue), 0.0) > 0) {
				themeGraph.setMaxGraphSize(Double.parseDouble(maxValue));
				refreshMapAtOnce();
			}
		}

		private void setGraphMinValue() {
			String minValue = textFieldMinValue.getText();
			if (StringUtilties.isNumber(minValue) && Double.compare(Double.parseDouble(minValue), 0.0) > 0) {
				themeGraph.setMinGraphSize(Double.parseDouble(minValue));
				refreshMapAtOnce();
			}
		}

		private void documentChange(DocumentEvent e) {
			if (e.getDocument() == textFieldMaxValue.getDocument()) {
				setGraphMaxValue();
				return;
			}
			if (e.getDocument() == textFieldMinValue.getDocument()) {
				setGraphMinValue();
				return;
			}
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			documentChange(e);
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			documentChange(e);
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			documentChange(e);
		}
	}

	class ShowLeaderLineAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			SymbolDialog textStyleDialog = new SymbolDialog();
			Resources resources = Application.getActiveApplication().getWorkspace().getResources();
			SymbolType symbolType = SymbolType.LINE;
			GeoStyle geoStyle = themeGraph.getLeaderLineStyle();
			DialogResult dialogResult = null;
			if (null != geoStyle) {
				dialogResult = textStyleDialog.showDialog(resources, geoStyle, symbolType);
			} else {
				dialogResult = textStyleDialog.showDialog(resources, new GeoStyle(), symbolType);
			}
			if (dialogResult.equals(DialogResult.OK)) {
				GeoStyle nowGeoStyle = textStyleDialog.getStyle();
				themeGraph.setLeaderLineStyle(nowGeoStyle);
				refreshMapAtOnce();
			}
		}
	}

	class OptionsChangeListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == checkBoxShowFlow) {
				boolean isShowFlow = checkBoxShowFlow.isSelected();
				boolean isShowLeaderLine = checkboxDraftLine.isSelected();
				if (isShowLeaderLine) {
					if (isShowFlow) {
						buttonDraftLine.setEnabled(true);
					} else {
						buttonDraftLine.setEnabled(false);
					}
				}
				themeGraph.setFlowEnabled(isShowFlow);
				refreshMapAtOnce();
				return;
			}
			if (e.getSource() == checkBoxShowNegative) {
				boolean isShowNegative = checkBoxShowNegative.isSelected();
				themeGraph.setNegativeDisplayed(isShowNegative);
				refreshMapAtOnce();
				return;
			}
			if (e.getSource() == checkBoxAutoAvoid) {
				boolean isAutoAvoid = checkBoxAutoAvoid.isSelected();
				themeGraph.setOverlapAvoided(isAutoAvoid);
				refreshMapAtOnce();
				return;
			}
			if (e.getSource() == checkBoxAutoScale) {
				setMaxGraphSize();
				refreshMapAtOnce();
				return;
			}
			if (e.getSource() == checkboxDraftLine) {
				boolean isShowLeaderLine = checkboxDraftLine.isSelected();
				if (isShowLeaderLine) {
					buttonDraftLine.setEnabled(true);
				} else {
					buttonDraftLine.setEnabled(false);
				}
				themeGraph.setLeaderLineDisplayed(isShowLeaderLine);
				refreshMapAtOnce();
				return;
			}
		}

		private void setMaxGraphSize() {
			boolean isAutoScale = checkBoxAutoScale.isSelected();
			if (!isAutoScale) {
				Point2D pointStart = new Point2D(0, 0);
				Point2D pointEnd = new Point2D(themeGraph.getMaxGraphSize(), themeGraph.getMaxGraphSize());
				Point2D point2DStart = map.mapToLogical(pointStart);
				Point2D point2DEnd = map.mapToLogical(pointEnd);
				themeGraph.setMaxGraphSize(Math.abs(point2DEnd.getX() - point2DStart.getX()));
				Point2D pointMinEnd = new Point2D(themeGraph.getMinGraphSize(), themeGraph.getMinGraphSize());
				Point2D point2DMinEnd = map.mapToLogical(pointMinEnd);
				if (Math.abs(point2DMinEnd.getX() - point2DStart.getX()) > 0) {
					themeGraph.setMinGraphSize(Math.abs(point2DMinEnd.getX() - point2DStart.getX()));
				}
				Point2D pointStart1 = new Point2D(0, 0);
				Point2D pointEnd1 = new Point2D(themeGraph.getBarWidth(), themeGraph.getBarWidth());
				Point2D point2DStart1 = map.mapToLogical(pointStart1);
				Point2D point2DEnd1 = map.mapToLogical(pointEnd1);

				themeGraph.setBarWidth(10 * Math.abs(point2DEnd1.getX() - point2DStart1.getX()));
			} else {
				Point2D pointStart = new Point2D(0, 0);
				Point2D pointEnd = new Point2D(themeGraph.getMaxGraphSize(), themeGraph.getMaxGraphSize());
				Point2D point2DStart = map.logicalToMap(pointStart);
				Point2D point2DEnd = map.logicalToMap(pointEnd);
				themeGraph.setMaxGraphSize(Math.abs(point2DEnd.getX() - point2DStart.getX()));
				Point2D pointMinEnd = new Point2D(themeGraph.getMinGraphSize(), themeGraph.getMinGraphSize());
				Point2D point2DMinEnd = map.logicalToMap(pointMinEnd);
				if (Math.abs(point2DMinEnd.getX() - point2DStart.getX()) > 0) {
					themeGraph.setMinGraphSize(Math.abs(point2DMinEnd.getX() - point2DStart.getX()));
				}
				Point2D pointStart1 = new Point2D(0, 0);
				Point2D pointEnd1 = new Point2D(themeGraph.getBarWidth(), themeGraph.getBarWidth());
				Point2D point2DStart1 = map.logicalToMap(pointStart1);
				Point2D point2DEnd1 = map.logicalToMap(pointEnd1);
				themeGraph.setBarWidth(Math.abs(point2DEnd1.getX() - point2DStart1.getX()) / 10);
			}
			themeGraph.setGraphSizeFixed(!isAutoScale);
			textFieldMaxValue.setText(String.valueOf(themeGraph.getMaxGraphSize()));
			textFieldMinValue.setText(String.valueOf(themeGraph.getMinGraphSize()));
			spinnerBarWidth.setValue(themeGraph.getBarWidth());
		}
	}

	class GraphItemColorChangeListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			// 修改颜色方案
			int[] selectRows = tableGraphInfo.getSelectedRows();
			refreshColor();
			getTable();
			if (selectRows.length > 0) {
				for (int i = 0; i < selectRows.length; i++) {
					tableGraphInfo.addRowSelectionInterval(selectRows[i], selectRows[i]);
				}
			}
			refreshMapAtOnce();
		}
	}

	class GraphModeChangeListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				int selectIndex = comboBoxMethod.getSelectedIndex();
				switch (selectIndex) {
				case 0:
					themeGraph.setGraduatedMode(GraduatedMode.CONSTANT);
					refreshMapAtOnce();
					break;
				case 1:
					themeGraph.setGraduatedMode(GraduatedMode.LOGARITHM);
					refreshMapAtOnce();
					break;
				case 2:
					themeGraph.setGraduatedMode(GraduatedMode.SQUAREROOT);
					refreshMapAtOnce();
				default:
					break;
				}
			}
		}
	}

	class GraphTypeChangeListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				int selectIndex = comboBoxGraphType.getSelectedIndex();
				switch (selectIndex) {
				case 0:
					themeGraph.setGraphType(ThemeGraphType.AREA);
					spinnerBarWidth.setEnabled(false);
					spinnerRoseAngle.setEnabled(false);
					spinnerStartAngle.setEnabled(false);
					setPanelEnableAndRefresh(true);
					break;
				case 1:
					themeGraph.setGraphType(ThemeGraphType.STEP);
					spinnerBarWidth.setEnabled(false);
					spinnerRoseAngle.setEnabled(false);
					spinnerStartAngle.setEnabled(false);
					setPanelEnableAndRefresh(true);
					break;
				case 2:
					themeGraph.setGraphType(ThemeGraphType.LINE);
					spinnerBarWidth.setEnabled(false);
					spinnerRoseAngle.setEnabled(false);
					spinnerStartAngle.setEnabled(false);
					setPanelEnableAndRefresh(true);
					break;
				case 3:
					themeGraph.setGraphType(ThemeGraphType.POINT);
					spinnerBarWidth.setEnabled(false);
					spinnerRoseAngle.setEnabled(false);
					spinnerStartAngle.setEnabled(false);
					setPanelEnableAndRefresh(true);
					break;
				case 4:
					themeGraph.setGraphType(ThemeGraphType.BAR);
					spinnerBarWidth.setEnabled(true);
					spinnerRoseAngle.setEnabled(false);
					spinnerStartAngle.setEnabled(false);
					setPanelEnableAndRefresh(true);
					break;
				case 5:
					themeGraph.setGraphType(ThemeGraphType.BAR3D);
					spinnerBarWidth.setEnabled(true);
					spinnerRoseAngle.setEnabled(false);
					spinnerStartAngle.setEnabled(false);
					setPanelEnableAndRefresh(true);
					break;
				case 6:
					themeGraph.setGraphType(ThemeGraphType.PIE);
					spinnerBarWidth.setEnabled(false);
					spinnerStartAngle.setEnabled(true);
					spinnerRoseAngle.setEnabled(false);
					spinnerStartAngle.setValue(themeGraph.getStartAngle());
					setPanelEnableAndRefresh(false);
					break;
				case 7:
					themeGraph.setGraphType(ThemeGraphType.PIE3D);
					spinnerBarWidth.setEnabled(false);
					spinnerStartAngle.setEnabled(true);
					spinnerRoseAngle.setEnabled(false);
					spinnerStartAngle.setValue(themeGraph.getStartAngle());
					setPanelEnableAndRefresh(false);
					break;
				case 8:
					themeGraph.setGraphType(ThemeGraphType.ROSE);
					spinnerBarWidth.setEnabled(false);
					spinnerStartAngle.setEnabled(true);
					spinnerRoseAngle.setEnabled(true);
					spinnerRoseAngle.setValue(themeGraph.getRoseAngle());
					setPanelEnableAndRefresh(false);
					break;
				case 9:
					themeGraph.setGraphType(ThemeGraphType.ROSE3D);
					spinnerBarWidth.setEnabled(false);
					spinnerStartAngle.setEnabled(true);
					spinnerRoseAngle.setEnabled(true);
					spinnerRoseAngle.setValue(themeGraph.getRoseAngle());
					setPanelEnableAndRefresh(false);
					break;
				case 10:
					themeGraph.setGraphType(ThemeGraphType.STACK_BAR);
					spinnerBarWidth.setEnabled(true);
					spinnerRoseAngle.setEnabled(false);
					spinnerStartAngle.setEnabled(false);
					setPanelEnableAndRefresh(true);
					break;
				case 11:
					themeGraph.setGraphType(ThemeGraphType.STACK_BAR3D);
					spinnerBarWidth.setEnabled(true);
					spinnerRoseAngle.setEnabled(false);
					spinnerStartAngle.setEnabled(false);
					spinnerBarWidth.setValue(themeGraph.getBarWidth());
					setPanelEnableAndRefresh(true);
					break;
				case 12:
					themeGraph.setGraphType(ThemeGraphType.RING);
					spinnerBarWidth.setEnabled(false);
					spinnerRoseAngle.setEnabled(false);
					spinnerStartAngle.setEnabled(false);
					setPanelEnableAndRefresh(false);
					break;
				default:
					break;
				}
			}
		}

		private void setPanelEnableAndRefresh(boolean enable) {
			checkBoxAxis.setEnabled(enable);
			resetMapSize();
			refreshMapAtOnce();
		}
	}

	class LocalMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (2 == e.getClickCount() && tableGraphInfo.getSelectedColumn() == TABLE_COLUMN_STYLE) {
				int selectRow = tableGraphInfo.getSelectedRow();
				setItemGeoSytle();
				tableGraphInfo.setRowSelectionInterval(selectRow, selectRow);
				refreshMapAtOnce();
			}
		}
	}

	class LayersTreeChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			int[] selectRows = tableGraphInfo.getSelectedRows();
			// 属性修改后原有的map，themeUniqueLayer,themeUnique已经不存在，需要重新赋值
			map = ThemeGuideFactory.getMapControl().getMap();
			themeGraphLayer = MapUtilties.findLayerByName(map, layerName);
			if (null != themeGraphLayer && null != themeGraphLayer.getTheme() && themeGraphLayer.getTheme() instanceof ThemeGraph) {
				themeGraph = new ThemeGraph((ThemeGraph) themeGraphLayer.getTheme());
				getTable();
				map.refresh();
				for (int i = 0; i < selectRows.length; i++) {
					tableGraphInfo.addRowSelectionInterval(selectRows[i], selectRows[i]);
				}
			}
		}
	}

	@Override
	public void unregistActionListener() {
		this.tableGraphInfo.removeMouseListener(this.localMouseListener);
		this.comboBoxGraphType.removeItemListener(this.graphTypeChangeListener);
		this.comboBoxMethod.removeItemListener(this.graphModeChangeListener);
		this.comboBoxColor.removeItemListener(this.graphColorChangeListener);
		this.checkBoxShowFlow.removeItemListener(this.OptionsChangeListener);
		this.checkBoxShowNegative.removeItemListener(this.OptionsChangeListener);
		this.checkBoxAutoAvoid.removeItemListener(this.OptionsChangeListener);
		this.checkBoxAutoScale.removeItemListener(this.OptionsChangeListener);
		this.checkboxDraftLine.removeItemListener(this.OptionsChangeListener);
		this.buttonDraftLine.removeActionListener(this.showLeaderLineAction);
		this.textFieldMaxValue.getDocument().removeDocumentListener(this.graphSizeChangeListener);
		this.textFieldMinValue.getDocument().removeDocumentListener(this.graphSizeChangeListener);
		this.comboBoxOffsetUnity.removeItemListener(this.offsetFixedListener);
		this.comboBoxOffsetX.removeItemListener(this.offsetxListener);
		this.comboBoxOffsetY.removeItemListener(this.offsetYListener);
		this.checkBoxRemark.removeItemListener(this.graphTextDisplayedListener);
		this.comboBoxRemarkFormat.removeItemListener(this.graphTextFormatListener);
		this.buttonRemarkStyle.removeActionListener(this.remarkStyleListener);
		this.checkBoxAxis.removeItemListener(this.showAxisTextListener);
		this.buttonAxisColor.removePropertyChangeListener("m_selectionColors", this.axisColorListener);
		this.comboBoxAxisModel.removeItemListener(this.axisModelListener);
		this.buttonAxisStyle.removeActionListener(this.axisStyleListener);
		this.checkBoxShowAxisGrid.removeItemListener(this.showAxisGridListener);
		this.spinnerBarWidth.removeChangeListener(this.barWidthListener);
		this.spinnerStartAngle.removeChangeListener(this.startAngleListener);
		this.spinnerRoseAngle.removeChangeListener(this.roseAngleListener);
		this.buttonDelete.removeActionListener(this.toolbarAction);
		this.buttonAdd.removeActionListener(this.toolbarAction);
		this.buttonStyle.removeActionListener(this.toolbarAction);
		this.buttonMoveToFrist.removeActionListener(this.toolbarAction);
		this.buttonMoveToForward.removeActionListener(this.toolbarAction);
		this.buttonMoveToNext.removeActionListener(this.toolbarAction);
		this.buttonMoveToLast.removeActionListener(this.toolbarAction);
		this.layersTree.removePropertyChangeListener("LayerChange", this.layersTreePropertyChangeListener);
		this.tableGraphInfo.removeMouseListener(this.tableMouseListener);
		this.layersTree.removePropertyChangeListener("LayerPropertyChanged", this.layerPropertyChangeListener);
	}

	public void setItemGeoSytle() {
		int[] selectedRow = this.tableGraphInfo.getSelectedRows();
		SymbolDialog textStyleDialog = new SymbolDialog();
		String name = this.tableGraphInfo.getColumnName(TABLE_COLUMN_EXPRESSION);
		int width = this.tableGraphInfo.getColumn(name).getWidth();
		int height = this.tableGraphInfo.getTableHeader().getHeight();
		int x = this.tableGraphInfo.getLocationOnScreen().x + width;
		int y = this.tableGraphInfo.getLocationOnScreen().y - height;
		textStyleDialog.setLocation(x, y);
		Resources resources = Application.getActiveApplication().getWorkspace().getResources();
		SymbolType symbolType = SymbolType.FILL;

		if (selectedRow.length == 1) {
			GeoStyle geoStyle = this.themeGraph.getItem(selectedRow[0]).getUniformStyle();
			DialogResult dialogResult = textStyleDialog.showDialog(resources, geoStyle, symbolType);
			if (dialogResult.equals(DialogResult.OK)) {
				GeoStyle nowGeoStyle = textStyleDialog.getStyle();
				resetGeoSytle(selectedRow[0], nowGeoStyle);

			}
		} else if (selectedRow.length > 1) {
			java.util.List<GeoStyle> geoStyleList = new ArrayList<>();
			for (int i = 0; i < selectedRow.length; i++) {
				geoStyleList.add(this.themeGraph.getItem(selectedRow[i]).getUniformStyle());
			}
			JDialogSymbolsChange jDialogSymbolsChange = new JDialogSymbolsChange(symbolType, geoStyleList);
			jDialogSymbolsChange.showDialog();
		}
		getTable();
		if (selectedRow.length > 0) {
			for (int i = 0; i < selectedRow.length; i++) {
				this.tableGraphInfo.addRowSelectionInterval(selectedRow[i], selectedRow[i]);
			}
		}
	}

	/**
	 * 重置风格
	 *
	 * @param selectRow
	 *            要重置风格的行
	 * @param nowGeoStyle
	 *            新的风格
	 */
	private void resetGeoSytle(int selectRow, GeoStyle nowGeoStyle) {
		ThemeGraphItem item = themeGraph.getItem(selectRow);
		item.setUniformStyle(nowGeoStyle);
	}

	public Layer getThemeGraphLayer() {
		return themeGraphLayer;
	}

	@Override
	public void setRefreshAtOnce(boolean isRefreshAtOnce) {
		this.isRefreshAtOnce = isRefreshAtOnce;
	}

	@Override
	public void refreshMapAndLayer() {
		this.map = ThemeGuideFactory.getMapControl().getMap();
		this.themeGraphLayer = MapUtilties.findLayerByName(this.map, this.layerName);
		if (null != themeGraphLayer && null != themeGraphLayer.getTheme()) {
			ThemeGraph nowGraph = ((ThemeGraph) this.themeGraphLayer.getTheme());
			nowGraph.fromXML(this.themeGraph.toXML());
			UICommonToolkit.getLayersManager().getLayersTree().refreshNode(this.themeGraphLayer);
			this.map.refresh();
		}
	}

	@Override
	public Layer getCurrentLayer() {
		return themeGraphLayer;
	}

}
