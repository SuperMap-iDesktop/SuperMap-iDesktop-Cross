package com.supermap.desktop.newtheme.themeDotDensity;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.GeoStyle;
import com.supermap.data.SymbolType;
import com.supermap.desktop.controls.utilities.SymbolDialogFactory;
import com.supermap.desktop.dialog.SmOptionPane;
import com.supermap.desktop.dialog.symbolDialogs.ISymbolApply;
import com.supermap.desktop.dialog.symbolDialogs.SymbolDialog;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.newtheme.commonPanel.ThemeChangePanel;
import com.supermap.desktop.newtheme.commonUtils.ThemeGuideFactory;
import com.supermap.desktop.newtheme.commonUtils.ThemeUtil;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.LayersTree;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.mapping.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 点密度专题图
 * 
 * @author xie
 *
 */
public class ThemeDotDensityContainer extends ThemeChangePanel {

	private static final long serialVersionUID = 1L;

	private JTabbedPane tabbedPaneInfo;
	private JPanel panelProperty;

	private JLabel labelExpression;
	private JLabel labelValue;
	private JLabel labelDotDensityValue;
	private JLabel labelDotDensityStyle;
	private JComboBox<String> comboBoxExpression;// 表达式
	private JTextField textFieldValue;// 单点代表的值
	private JSpinner spinnerDotDensityValue;// 最大值包含的点个数
	private JButton buttonDotDensityStyle;// 点风格

	private ThemeDotDensity themeDotDensity;
	private Layer themeDotDensityLayer;
	private DatasetVector datasetVector;
	private boolean isRefreshAtOnce;
	private Map map;
	private String layerName;

	private double maxValue;

	private ArrayList<String> comboBoxArray;
	private LayersTree layersTree = UICommonToolkit.getLayersManager().getLayersTree();
	private DecimalFormat format = new DecimalFormat("#.####");

	private ItemListener expressionListener = new ExpressionListener();
	private ActionListener buttonActionListener = new ButtonActionListener();
	private ChangeListener changeListener = new SpinnerListener();
	private FocusAdapter textFieldListener = new TextFieldListener();
	private MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			// 动态刷新表达式
			initComboBoxExpression();
		}
	};

	public ThemeDotDensityContainer(Layer layer) {
		this.themeDotDensityLayer = layer;
		this.layerName = layer.getName();
		this.themeDotDensity = new ThemeDotDensity((ThemeDotDensity) layer.getTheme());
		this.datasetVector = (DatasetVector) layer.getDataset();
		this.map = ThemeGuideFactory.getMapControl().getMap();
		initComponents();
		initResources();
		registActionListener();
	}

	private void initComponents() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		this.setLayout(gridBagLayout);
		this.tabbedPaneInfo = new JTabbedPane();
		this.panelProperty = new JPanel();
		this.tabbedPaneInfo.add(MapViewProperties.getString("String_Theme_Property"), this.panelProperty);
		this.add(tabbedPaneInfo, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		initPanelProperty();
	}

	private void initResources() {
		this.labelExpression.setText(MapViewProperties.getString("String_Label_Expression"));
		this.labelValue.setText(MapViewProperties.getString("String_ThemeDotDensityProperty_LabelDotValue"));
		this.labelDotDensityValue.setText(MapViewProperties.getString("String_ThemeDotDensityProperty_LabelMaxCount"));
		this.labelDotDensityStyle.setText(MapViewProperties.getString("String_ThemeDotDensityProperty_LabelDotStyle"));
	}

	private void initPanelProperty() {
		this.labelExpression = new JLabel();
		this.comboBoxExpression = new JComboBox<String>();
		this.labelValue = new JLabel();
		this.textFieldValue = new JTextField();
		this.labelDotDensityValue = new JLabel();
		this.spinnerDotDensityValue = new JSpinner();
		this.labelDotDensityStyle = new JLabel();
		this.buttonDotDensityStyle = new JButton("...");
		this.spinnerDotDensityValue.setModel(new SpinnerNumberModel(1, 1, null, 1));
		initComboBoxExpression();
		initTextFieldValue();
		Dimension dimension = new Dimension(120, 23);
		this.comboBoxExpression.setPreferredSize(dimension);
		this.textFieldValue.setPreferredSize(dimension);
		this.spinnerDotDensityValue.setPreferredSize(dimension);
		this.buttonDotDensityStyle.setPreferredSize(dimension);
		//@formatter:off
		JPanel panelContent = new JPanel();
		panelContent.setLayout(new GridBagLayout());
		panelContent.add(labelExpression,        new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5,10,5,10).setWeight(40, 1).setIpad(20, 0));
		panelContent.add(comboBoxExpression,     new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		panelContent.add(labelValue,             new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1).setIpad(20, 0));
		panelContent.add(textFieldValue,         new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		panelContent.add(labelDotDensityValue,   new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1).setIpad(20, 0));
		panelContent.add(spinnerDotDensityValue, new GridBagConstraintsHelper(1, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		panelContent.add(labelDotDensityStyle,   new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1).setIpad(20, 0));
		panelContent.add(buttonDotDensityStyle,  new GridBagConstraintsHelper(1, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelProperty.setLayout(new GridBagLayout());
		this.panelProperty.add(panelContent,  new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 5, 10));
		//@formatter:on
	}

	private void initComboBoxExpression() {
		this.comboBoxArray = new ArrayList<String>();
		ThemeUtil.initComboBox(comboBoxExpression, themeDotDensity.getDotExpression(), datasetVector, themeDotDensityLayer.getDisplayFilter().getJoinItems(),
				comboBoxArray, true, false);
	}

	private void initTextFieldValue() {
		this.textFieldValue.setText(String.valueOf(format.format(themeDotDensity.getValue())));
		String expression = themeDotDensity.getDotExpression();
		setThemeDotDensityInfo(expression);
		if (Double.compare(themeDotDensity.getValue(), 0.0) != 0) {
			int splinnerValue = Integer.parseInt(new DecimalFormat("#").format(maxValue / themeDotDensity.getValue()));
			this.spinnerDotDensityValue.setValue(splinnerValue);
		}
	}

	private void setThemeDotDensityInfo(String expression) {
		String tempExpression = expression;
		DatasetVector dataset = this.datasetVector;
		if (expression.contains(".")) {
			String datasetName = expression.substring(0, expression.indexOf("."));
			dataset = (DatasetVector) dataset.getDatasource().getDatasets().get(datasetName);
		}
		maxValue = ThemeGuideFactory.getMaxValue(dataset, tempExpression, this.themeDotDensityLayer.getDisplayFilter().getJoinItems());
	}

	@Override
	public Theme getCurrentTheme() {
		return this.themeDotDensity;
	}

	@Override
	public Layer getCurrentLayer() {
		return this.themeDotDensityLayer;
	}

	@Override
	public void setCurrentLayer(Layer layer) {
		this.themeDotDensityLayer = layer;
	}

	@Override
	public void registActionListener() {
		this.textFieldValue.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int keyChar = e.getKeyChar();
				if ((keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9) || keyChar == '.') {
					return;
				} else {
					e.consume();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {

				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					resetValue();
				}
			}

		});
		unregistActionListener();
		this.comboBoxExpression.addItemListener(this.expressionListener);
		this.buttonDotDensityStyle.addActionListener(this.buttonActionListener);
		this.textFieldValue.addFocusListener(this.textFieldListener);
		this.spinnerDotDensityValue.addChangeListener(this.changeListener);
		this.comboBoxExpression.getComponent(0).addMouseListener(this.mouseAdapter);
	}

	@Override
	public void unregistActionListener() {
		this.comboBoxExpression.removeItemListener(this.expressionListener);
		this.buttonDotDensityStyle.removeActionListener(this.buttonActionListener);
		this.textFieldValue.removeFocusListener(this.textFieldListener);
		this.spinnerDotDensityValue.removeChangeListener(this.changeListener);
		this.comboBoxExpression.getComponent(0).removeMouseListener(this.mouseAdapter);
	}

	class TextFieldListener extends FocusAdapter {

		@Override
		public void focusLost(FocusEvent e) {
			resetValue();
		}

	}

	private void resetValue() {
		String strValue = textFieldValue.getText();
		if (!StringUtilities.isNullOrEmpty(strValue) && StringUtilities.isNumber(strValue)) {
			themeDotDensity.setValue(Double.parseDouble(strValue));
			double newValue = maxValue / themeDotDensity.getValue();
			// 为了触发spinner的ChangeListener事件而设置两次
			spinnerDotDensityValue.setValue(0);
			if (Double.compare(newValue, 1.0) < 0) {
				newValue = 1.0;
			}
			int splinnerValue = Integer.parseInt(new DecimalFormat("#").format(newValue));
			spinnerDotDensityValue.setValue(splinnerValue);
			refreshAtOnce();
		} else {
			textFieldValue.setText(format.format(themeDotDensity.getValue()));
		}
	}

	class ExpressionListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			Dataset[] datasets = ThemeUtil.getDatasets(themeDotDensityLayer, datasetVector);
			if (e.getStateChange() == ItemEvent.SELECTED) {
				// sql表达式
				String tempExpression = themeDotDensity.getDotExpression();
				if (!comboBoxArray.contains(tempExpression)) {
					tempExpression = tempExpression.substring(tempExpression.indexOf(".") + 1, tempExpression.length());
				}
				boolean itemHasChanged = ThemeUtil.getSqlExpression(comboBoxExpression, datasets, comboBoxArray, tempExpression, true);
				// 修改表达式
				if (itemHasChanged) {
					// 如果sql表达式中修改了选项
					tempExpression = comboBoxExpression.getSelectedItem().toString();
					setThemeDotDensityInfo(tempExpression);
					if (maxValue > 0) {
						themeDotDensity.setDotExpression(tempExpression);
						int dotValue = (int) spinnerDotDensityValue.getValue();
						themeDotDensity.setValue(maxValue / dotValue);
						textFieldValue.setText(String.valueOf(format.format(maxValue / dotValue)));
						refreshAtOnce();
					} else {
						SmOptionPane smOptionPane = new SmOptionPane();
						smOptionPane.showMessageDialog(MapViewProperties.getString("String_MaxValue"));
						resetThemeItem();
					}
				}
			}
		}
	}

	private void resetThemeItem() {
		String tempExpression = themeDotDensity.getDotExpression();
		if (comboBoxArray.contains(tempExpression)) {
			comboBoxExpression.setSelectedItem(tempExpression);
		} else {
			comboBoxExpression.setSelectedItem(tempExpression.substring(tempExpression.indexOf(".") + 1, tempExpression.length()));
		}
	}

	class ButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			SymbolDialog symbolStyle = SymbolDialogFactory.getSymbolDialog(SymbolType.MARKER);
			GeoStyle geoStyle = themeDotDensity.getStyle();
			ISymbolApply symbolApply = new ISymbolApply() {
				@Override
				public void apply(GeoStyle geoStyle) {
					themeDotDensity.setStyle(geoStyle);
					refreshAtOnce();
					return;
				}
			};
			DialogResult dialogResult;
			if (null != geoStyle) {
				dialogResult = symbolStyle.showDialog(geoStyle, symbolApply);
			} else {
				dialogResult = symbolStyle.showDialog(new GeoStyle(), symbolApply);
			}
			if (DialogResult.OK.equals(dialogResult)) {
				GeoStyle nowGeoStyle = symbolStyle.getCurrentGeoStyle();
				themeDotDensity.setStyle(nowGeoStyle);
				refreshAtOnce();
				symbolStyle = null;
				return;
			}
		}
	}

	class SpinnerListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			double value = Double.parseDouble(spinnerDotDensityValue.getValue().toString());
			final double dotValue = maxValue / value;
			themeDotDensity.setValue(dotValue);
			textFieldValue.setText(String.valueOf(format.format(dotValue)));
			refreshAtOnce();
		}
	}


	@Override
	public void setRefreshAtOnce(boolean isRefreshAtOnce) {
		this.isRefreshAtOnce = isRefreshAtOnce;
	}

	private void refreshAtOnce() {
		firePropertyChange("ThemeChange", null, null);
		if (isRefreshAtOnce) {
			refreshMapAndLayer();
		}
	}

	@Override
	public void refreshMapAndLayer() {
		if (null != ThemeGuideFactory.getMapControl()) {
			this.map = ThemeGuideFactory.getMapControl().getMap();
		}
		this.themeDotDensityLayer = MapUtilities.findLayerByName(map, layerName);
		if (null != themeDotDensityLayer && null != themeDotDensityLayer.getTheme() && themeDotDensityLayer.getTheme().getType() == ThemeType.DOTDENSITY) {
			ThemeDotDensity nowThemeDotDensity = ((ThemeDotDensity) this.themeDotDensityLayer.getTheme());
			nowThemeDotDensity.fromXML(themeDotDensity.toXML());
			this.map.refresh();
		}
	}

}
