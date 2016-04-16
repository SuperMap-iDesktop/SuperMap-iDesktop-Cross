package com.supermap.desktop.dialog.symbolDialogs;

import com.supermap.data.Symbol;
import com.supermap.data.SymbolLibrary;
import com.supermap.data.SymbolMarker;
import com.supermap.data.SymbolMarker3D;
import com.supermap.data.SymbolType;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dialog.symbolDialogs.JpanelSymbols.JPanelSymbolsPoint;
import com.supermap.desktop.dialog.symbolDialogs.JpanelSymbols.SymbolSelectedChangedListener;
import com.supermap.desktop.enums.SymbolMarkerType;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.ButtonColorSelector;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilties.DoubleUtilties;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * 点符号风格设置面板
 *
 * @author XiaJt
 */
public class SymbolDialogPoint extends SymbolDialog {

	private JPanel panelMain;

	// 显示大小
	private JPanel panelShowSize;
	private JLabel labelShowWidth;
	private JSpinner spinnerShowWidth;
	private JLabel labelShowWidthUnit;

	private JLabel labelShowHeight;
	private JSpinner spinnerShowHeight;
	private JLabel labelShowHeightUnit;

	// 符号大小
	private JPanel panelSymbolSize;
	private JLabel labelSymbolWidth;
	private JSpinner spinnerSymbolWidth;
	private JLabel labelSymbolWidthUnit;

	private JLabel labelSymbolHeight;
	private JSpinner spinnerSymbolHeight;
	private JLabel labelSymbolHeightUnit;

	// 锁定宽高比例
	private JCheckBox checkBoxLockWidthHeightRate;

	private JLabel labelSymbolColor;
	private ButtonColorSelector buttonSymbolColor;

	private JLabel labelSymbolAngle;
	private JSpinner spinnerSymbolAngle;
	private JLabel labelSymbolAngleUnit;


	private JLabel labelOpaqueRate;
	private JSpinner spinnerOpaqueRate;
	private JLabel labelOpaqueRateUnit;


	private SymbolMarkerSizeController symbolMarkerSizeController;
	private boolean isSizeListenersEnable = true;
	private CaretListener caretListener;

	public SymbolDialogPoint() {
		super();
	}

	public SymbolDialogPoint(JDialog dialog) {
		super(dialog);
	}

	@Override
	protected SymbolType getSymbolType() {
		return SymbolType.MARKER;
	}

	@Override
	protected JPanel getPanelMain() {
		return panelMain;
	}

	@Override
	protected void initComponentHook() {
		pointInit();
		this.setTitle(ControlsProperties.getString("String_PointStyleChoose"));
	}

	private void pointInit() {
		pointInitComponents();
		pointAddListeners();
		pointInitResources();
		pointInitLayout();
	}

	private void pointInitComponents() {
		panelMain = new JPanel();
		panelSymbols = new JPanelSymbolsPoint();
		panelShowSize = new JPanel();
		labelShowWidth = new JLabel();
		spinnerShowWidth = new JSpinner();
		labelShowWidthUnit = new JLabel();
		labelShowHeight = new JLabel();
		spinnerShowHeight = new JSpinner();
		labelShowHeightUnit = new JLabel();
		panelSymbolSize = new JPanel();
		labelSymbolWidth = new JLabel();
		spinnerSymbolWidth = new JSpinner();
		labelSymbolWidthUnit = new JLabel();
		labelSymbolHeight = new JLabel();
		spinnerSymbolHeight = new JSpinner();
		labelSymbolHeightUnit = new JLabel();
		checkBoxLockWidthHeightRate = new JCheckBox();
		labelSymbolColor = new JLabel();
		labelSymbolAngle = new JLabel();
		spinnerSymbolAngle = new JSpinner();
		labelSymbolAngleUnit = new JLabel();
		labelOpaqueRate = new JLabel();
		spinnerOpaqueRate = new JSpinner();
		labelOpaqueRateUnit = new JLabel();
		symbolMarkerSizeController = new SymbolMarkerSizeController();
		checkBoxLockWidthHeightRate.setSelected(true);
		buttonSymbolColor = new ButtonColorSelector();
		caretListener = new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				if (isSizeListenersEnable && e.getSource() instanceof JTextField) {
					JTextField source = (JTextField) e.getSource();
					String text = source.getText();
					if (!SymbolSpinnerUtilties.isLegitNumber(-1.0d, 500.0d, text)) {
						source.setForeground(wrongColor);
						return;
					} else {
						source.setForeground(defaultColor);
					}
					setSizeControllerSize(source, Double.valueOf(text));
				}
			}
		};

		SymbolSpinnerUtilties.initSpinners(-1, 500, 1, "##0.0", spinnerShowHeight, spinnerShowWidth, spinnerSymbolHeight, spinnerSymbolWidth);
		SymbolSpinnerUtilties.initSpinners(0, 360, 1, "##0", spinnerSymbolAngle);
		SymbolSpinnerUtilties.initSpinners(0, 100, 1, "##0", spinnerOpaqueRate);

	}

	private void setSizeControllerSize(JTextField source, double value) {
		if (source == ((JSpinner.NumberEditor) spinnerShowHeight.getEditor()).getTextField()) {
			symbolMarkerSizeController.setSymbolShowHeight(value);
		} else if (source == ((JSpinner.NumberEditor) spinnerShowWidth.getEditor()).getTextField()) {
			symbolMarkerSizeController.setSymbolShowWidth(value);
		} else if (source == ((JSpinner.NumberEditor) spinnerSymbolWidth.getEditor()).getTextField()) {
			symbolMarkerSizeController.setSymbolWidth(value);
		} else if (source == ((JSpinner.NumberEditor) spinnerSymbolHeight.getEditor()).getTextField()) {
			symbolMarkerSizeController.setSymbolHeight(value);
		}
		loadSizeFormSymbolMarkerSizeController(source);
	}

	private void pointAddListeners() {
		final JFormattedTextField textFieldShowHeight = ((JSpinner.NumberEditor) spinnerShowHeight.getEditor()).getTextField();
		textFieldShowHeight.addCaretListener(caretListener);

		final JFormattedTextField textFieldShowWidth = ((JSpinner.NumberEditor) spinnerShowWidth.getEditor()).getTextField();
		textFieldShowWidth.addCaretListener(caretListener);

		final JFormattedTextField textFieldSymbolHeight = ((JSpinner.NumberEditor) spinnerSymbolHeight.getEditor()).getTextField();
		textFieldSymbolHeight.addCaretListener(caretListener);

		final JFormattedTextField textFieldSymbolWidth = ((JSpinner.NumberEditor) spinnerSymbolWidth.getEditor()).getTextField();
		textFieldSymbolWidth.addCaretListener(caretListener);

		final JFormattedTextField textFieldSymbolAngle = ((JSpinner.NumberEditor) spinnerSymbolAngle.getEditor()).getTextField();
		textFieldSymbolAngle.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				String text = textFieldSymbolAngle.getText();
				if (!SymbolSpinnerUtilties.isLegitNumber(0d, 360d, text)) {
					textFieldSymbolAngle.setForeground(wrongColor);
					return;
				} else {
					textFieldSymbolAngle.setForeground(defaultColor);
				}
				double angle = Double.valueOf(text);
				if (!DoubleUtilties.equals(angle, currentGeoStyle.getMarkerAngle(), pow)) {
					currentGeoStyle.setMarkerAngle(angle);
					geoStylePropertyChange.propertyChange();
				}
			}
		});
		final JFormattedTextField textFieldOpaqueRate = ((JSpinner.NumberEditor) spinnerOpaqueRate.getEditor()).getTextField();
		textFieldOpaqueRate.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				String text = textFieldOpaqueRate.getText();
				if (!SymbolSpinnerUtilties.isLegitNumber(0, 100, text)) {
					textFieldOpaqueRate.setForeground(wrongColor);
					return;
				} else {
					textFieldOpaqueRate.setForeground(defaultColor);
				}
				Integer integer = Integer.valueOf(text);
				currentGeoStyle.setFillOpaqueRate(getUnOpaqueRate(integer));
				geoStylePropertyChange.propertyChange();
			}
		});

		checkBoxLockWidthHeightRate.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				checkBoxLockWidthHeightRate.setSelected(e.getStateChange() == ItemEvent.SELECTED);
				symbolMarkerSizeController.setLockSelected(e.getStateChange() == ItemEvent.SELECTED);
			}
		});

		buttonSymbolColor.addPropertyChangeListener(ButtonColorSelector.PROPERTY_COLOR, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Color color = buttonSymbolColor.getColor();
				if (color != null) {
					currentGeoStyle.setLineColor(color);
					geoStylePropertyChange.propertyChange();
				}
			}
		});

		panelSymbols.addSymbolSelectedChangedListener(new SymbolSelectedChangedListener() {
			@Override
			public void SymbolSelectedChangedEvent(Symbol symbol) {
				if (symbol == null) {
					spinnerShowWidth.setEnabled(true);
					spinnerShowHeight.setEnabled(true);
					checkBoxLockWidthHeightRate.setSelected(true);
					checkBoxLockWidthHeightRate.setEnabled(false);
					buttonSymbolColor.setToolTipText("");
					buttonSymbolColor.setEnabled(true);
					spinnerSymbolAngle.setEnabled(false);
					spinnerOpaqueRate.setToolTipText(ControlsProperties.getString("String_vectorSymbolCantSetOpaque"));
					spinnerOpaqueRate.setEnabled(false);
				} else if (symbol instanceof SymbolMarker3D) {
					spinnerShowWidth.setEnabled(true);
					spinnerShowHeight.setEnabled(true);
					checkBoxLockWidthHeightRate.setEnabled(true);
					buttonSymbolColor.setToolTipText(CoreProperties.getString("String_Label_ColorSettingTip"));
					buttonSymbolColor.setEnabled(false);
					spinnerSymbolAngle.setEnabled(true);
					spinnerOpaqueRate.setToolTipText("");
					spinnerOpaqueRate.setEnabled(true);
				} else {
					SymbolMarkerType markerType = SymbolMarkerType.getSymbolMarkerType(((SymbolMarker) symbol));
					if (markerType == SymbolMarkerType.Vector) {
						spinnerShowWidth.setEnabled(true);
						spinnerShowHeight.setEnabled(true);
						checkBoxLockWidthHeightRate.setEnabled(true);
						buttonSymbolColor.setToolTipText("");
						buttonSymbolColor.setEnabled(true);
						spinnerSymbolAngle.setEnabled(true);
						//spinnerOpaqueRate.setToolTipText(ControlsProperties.getString("String_vectorSymbolCantSetOpaque"));
						spinnerOpaqueRate.setToolTipText("");
						spinnerOpaqueRate.setEnabled(true);
					} else if (markerType == SymbolMarkerType.Raster) {
						spinnerShowWidth.setEnabled(true);
						spinnerShowHeight.setEnabled(true);
						checkBoxLockWidthHeightRate.setEnabled(true);
						buttonSymbolColor.setToolTipText(CoreProperties.getString("String_Label_ColorSettingTip"));
						buttonSymbolColor.setEnabled(false);
						spinnerSymbolAngle.setEnabled(true);
						spinnerOpaqueRate.setToolTipText("");
						spinnerOpaqueRate.setEnabled(true);
					}
				}
				reloadSizeFormSymbolMarkerSizeController();
				geoStylePropertyChange.propertyChange();
			}

			@Override
			public void SymbolSelectedDoubleClicked() {
				enterPressed();
			}
		});
	}

	private void loadSizeFormSymbolMarkerSizeController(JTextField source) {
		try {
			isSizeListenersEnable = false;
			if (((JSpinner.NumberEditor) spinnerShowHeight.getEditor()).getTextField() != source) {
				spinnerShowHeight.setValue(symbolMarkerSizeController.getSymbolShowHeight());
			}
			if (((JSpinner.NumberEditor) spinnerShowWidth.getEditor()).getTextField() != source) {
				spinnerShowWidth.setValue(symbolMarkerSizeController.getSymbolShowWidth());
			}
			if (((JSpinner.NumberEditor) spinnerSymbolWidth.getEditor()).getTextField() != source) {
				spinnerSymbolWidth.setValue(symbolMarkerSizeController.getSymbolWidth());
			}
			if (((JSpinner.NumberEditor) spinnerSymbolHeight.getEditor()).getTextField() != source) {
				spinnerSymbolHeight.setValue(symbolMarkerSizeController.getSymbolHeight());
			}
			geoStylePropertyChange.propertyChange();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			isSizeListenersEnable = true;
		}

	}


	private void reloadSizeFormSymbolMarkerSizeController() {
		symbolMarkerSizeController.reCalculateShowSize();
		loadSizeFormSymbolMarkerSizeController();
	}

	private void loadSizeFormSymbolMarkerSizeController() {
		try {
			isSizeListenersEnable = false;
			spinnerShowHeight.setValue(symbolMarkerSizeController.getSymbolShowHeight());
			spinnerShowWidth.setValue(symbolMarkerSizeController.getSymbolShowWidth());
			spinnerSymbolWidth.setValue(symbolMarkerSizeController.getSymbolWidth());
			spinnerSymbolHeight.setValue(symbolMarkerSizeController.getSymbolHeight());
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			isSizeListenersEnable = true;
		}
	}

	private void pointInitLayout() {
		initPanelShowSize();
		initPanelSymbolSize();

		panelMain.setLayout(new GridBagLayout());
		panelMain.add(panelShowSize, new GridBagConstraintsHelper(0, 0, 3, 1).setWeight(1, 0).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
		panelMain.add(panelSymbolSize, new GridBagConstraintsHelper(0, 1, 3, 1).setWeight(1, 0).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(5, 0, 0, 0));
		panelMain.add(checkBoxLockWidthHeightRate, new GridBagConstraintsHelper(0, 2, 3, 1).setWeight(1, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 0, 0));

		panelMain.add(labelSymbolColor, new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 0, 0));
		panelMain.add(buttonSymbolColor, new GridBagConstraintsHelper(1, 3, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 0, 0));
		panelMain.add(new JPanel(), new GridBagConstraintsHelper(2, 3, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER));

		panelMain.add(labelSymbolAngle, new GridBagConstraintsHelper(0, 4, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 0, 0));
		panelMain.add(spinnerSymbolAngle, new GridBagConstraintsHelper(1, 4, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 0, 0));
		panelMain.add(labelSymbolAngleUnit, new GridBagConstraintsHelper(2, 4, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 0, 0));

		panelMain.add(labelOpaqueRate, new GridBagConstraintsHelper(0, 5, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 0, 0));
		panelMain.add(spinnerOpaqueRate, new GridBagConstraintsHelper(1, 5, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 0, 0));
		panelMain.add(labelOpaqueRateUnit, new GridBagConstraintsHelper(2, 5, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 0, 0));

		panelMain.add(new JPanel(), new GridBagConstraintsHelper(0, 6, 3, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
	}

	private void initPanelShowSize() {
		panelShowSize.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(Color.gray, 1),
				ControlsProperties.getString("String_SymbolShowSize"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		//@formatter:off
		panelShowSize.setLayout(new GridBagLayout());
		panelShowSize.add(labelShowWidth, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setWeight(0, 1));
		panelShowSize.add(spinnerShowWidth, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setInsets(0, 5, 0, 5));
		panelShowSize.add(labelShowWidthUnit, new GridBagConstraintsHelper(2, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setWeight(0, 1));

		panelShowSize.add(labelShowHeight, new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setWeight(0, 1));
		panelShowSize.add(spinnerShowHeight, new GridBagConstraintsHelper(1, 1, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setInsets(0, 5, 0, 5));
		panelShowSize.add(labelShowHeightUnit, new GridBagConstraintsHelper(2, 1, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setWeight(0, 1));
		//@formatter:on

	}

	private void initPanelSymbolSize() {
		panelSymbolSize.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(Color.gray, 1),
				ControlsProperties.getString("String_SymbolShowSize"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		//@formatter:off

		panelSymbolSize.setLayout(new GridBagLayout());
		panelSymbolSize.add(labelSymbolWidth, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setWeight(0, 1));
		panelSymbolSize.add(spinnerSymbolWidth, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setInsets(0, 5, 0, 5));
		panelSymbolSize.add(labelSymbolWidthUnit, new GridBagConstraintsHelper(2, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setWeight(0, 1));

		panelSymbolSize.add(labelSymbolHeight, new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setWeight(0, 1));
		panelSymbolSize.add(spinnerSymbolHeight, new GridBagConstraintsHelper(1, 1, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setInsets(0, 5, 0, 5));
		panelSymbolSize.add(labelSymbolHeightUnit, new GridBagConstraintsHelper(2, 1, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setWeight(0, 1));
		//@formatter:on
	}

	@Override
	protected void prepareForShowDialogHook() {
		symbolMarkerSizeController.setResources(currentResources);
		symbolMarkerSizeController.setGeoStyle(currentGeoStyle);
		buttonSymbolColor.setColor(currentGeoStyle.getLineColor());
		spinnerSymbolAngle.setValue(currentGeoStyle.getMarkerAngle());
		spinnerOpaqueRate.setValue(Double.valueOf(getUnOpaqueRate(currentGeoStyle.getFillOpaqueRate())));// 不转double,linux抛异常
		loadSizeFormSymbolMarkerSizeController();
	}


	private void pointInitResources() {
		labelShowWidth.setText(ControlsProperties.getString("String_ShowWidth"));
		labelShowWidthUnit.setText(ControlsProperties.getString("String_mm"));

		labelShowHeight.setText(ControlsProperties.getString("String_ShowHeight"));
		labelShowHeightUnit.setText(ControlsProperties.getString("String_mm"));

		labelSymbolWidth.setText(ControlsProperties.getString("String_SymbolWidth"));
		labelSymbolWidthUnit.setText(ControlsProperties.getString("String_mm"));

		labelSymbolHeight.setText(ControlsProperties.getString("String_SymbolHeight"));
		labelSymbolHeightUnit.setText(ControlsProperties.getString("String_mm"));

		checkBoxLockWidthHeightRate.setText(ControlsProperties.getString("String_LockMarkerWidthAndHeight"));

		labelSymbolColor.setText(CoreProperties.getString("String_Label_SymbolColor"));
		labelSymbolAngle.setText(ControlsProperties.getString("String_Label_SymbolAngle"));
		labelSymbolAngleUnit.setText(CoreProperties.getString("String_Degree_Format_Degree"));

		labelOpaqueRate.setText(ControlsProperties.getString("String_Label_Transparence"));
		labelOpaqueRateUnit.setText(ControlsProperties.getString("String_precent"));

	}


	@Override
	protected SymbolLibrary getLibrary() {
		return currentResources.getMarkerLibrary();
	}
}
