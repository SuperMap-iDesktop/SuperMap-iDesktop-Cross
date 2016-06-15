package com.supermap.desktop.dialog.symbolDialogs;

import com.supermap.data.Symbol;
import com.supermap.data.SymbolLibrary;
import com.supermap.data.SymbolType;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dialog.symbolDialogs.JpanelSymbols.JPanelSymbolsLine;
import com.supermap.desktop.dialog.symbolDialogs.JpanelSymbols.SymbolSelectedChangedListener;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.ButtonColorSelector;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilties.DoubleUtilties;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * 线符号面板
 *
 * @author XiaJt
 */
public class SymbolDialogLine extends SymbolDialog {

	private JPanel panelMain;


	private JLabel labelLineWidth;
	private JSpinner spinnerLineWidth;
	private JLabel labelLineWidthUnit;

	private JLabel labelLineColor;
	private ButtonColorSelector buttonSymbolColor;

	public SymbolDialogLine() {
		super();
	}

	public SymbolDialogLine(JDialog dialog) {
		super(dialog);
	}


	@Override
	protected void initComponentHook() {
//		int width = (int) (1000 / 1.25 * SystemPropertyUtilties.getSystemSizeRate());
//		int height = (int) (450 / 1.25 * SystemPropertyUtilties.getSystemSizeRate());
//		setMinimumSize(new Dimension(((int) (0.5 * width)), height));
		lineInit();
		this.setTitle(ControlsProperties.getString("String_LineGeoStyleChoose"));
	}

	private void lineInit() {
		lineInitComponents();
		lineAddListener();
		lineInitResources();
		lineInitLayout();
	}

	private void lineInitComponents() {
		panelMain = new JPanel();
		panelSymbols = new JPanelSymbolsLine();

		labelLineWidth = new JLabel();
		spinnerLineWidth = new JSpinner();
		labelLineWidthUnit = new JLabel();
		labelLineColor = new JLabel();
		buttonSymbolColor = new ButtonColorSelector();

		SymbolSpinnerUtilties.initSpinners(0.06, 20, 0.1, "##0.0", spinnerLineWidth);
	}

	private void lineAddListener() {
		final JFormattedTextField textFieldLineWidth = ((JSpinner.NumberEditor) spinnerLineWidth.getEditor()).getTextField();
		textFieldLineWidth.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				String text = textFieldLineWidth.getText();
				if (!SymbolSpinnerUtilties.isLegitNumber(0.1d, 20d, text)) {
					textFieldLineWidth.setForeground(wrongColor);
					return;
				} else {
					textFieldLineWidth.setForeground(defaultColor);
				}
				double lineWidth = Double.valueOf(text);
				if (!DoubleUtilties.equals(lineWidth, currentGeoStyle.getLineWidth(), pow)) {
					currentGeoStyle.setLineWidth(lineWidth);
					geoStylePropertyChange.propertyChange();
				}
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
				geoStylePropertyChange.propertyChange();
			}

			@Override
			public void SymbolSelectedDoubleClicked() {
				enterPressed();
			}
		});
	}

	private void lineInitResources() {
		this.labelLineWidth.setText(CoreProperties.getString("String_Label_LineWidth"));
		this.labelLineWidthUnit.setText(ControlsProperties.getString("String_mm"));
		this.labelLineColor.setText(ControlsProperties.getString("String_Label_LineColor"));
	}

	private void lineInitLayout() {
		panelMain.setLayout(new GridBagLayout());
		panelMain.add(labelLineWidth, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST));
		panelMain.add(spinnerLineWidth, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 5));
		panelMain.add(labelLineWidthUnit, new GridBagConstraintsHelper(2, 0, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER));

		panelMain.add(labelLineColor, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 0, 0));
		panelMain.add(buttonSymbolColor, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 0, 5));
		panelMain.add(new JPanel(), new GridBagConstraintsHelper(2, 1, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));

		panelMain.add(new JPanel(), new GridBagConstraintsHelper(0, 2, 3, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
	}

	@Override
	protected void prepareForShowDialogHook() {
		buttonSymbolColor.setColor(currentGeoStyle.getLineColor());
		spinnerLineWidth.setValue(currentGeoStyle.getLineWidth());
	}

	@Override
	protected SymbolType getSymbolType() {
		return SymbolType.LINE;
	}

	@Override
	protected JPanel getPanelMain() {
		return panelMain;
	}

	@Override
	protected SymbolLibrary getLibrary() {
		return currentResources.getLineLibrary();
	}

}
