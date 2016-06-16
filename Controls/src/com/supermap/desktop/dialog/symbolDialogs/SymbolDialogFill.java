package com.supermap.desktop.dialog.symbolDialogs;

import com.supermap.data.FillGradientMode;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoStyle;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.Symbol;
import com.supermap.data.SymbolLibrary;
import com.supermap.data.SymbolType;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dialog.symbolDialogs.JpanelSymbols.JPanelSymbolsFill;
import com.supermap.desktop.dialog.symbolDialogs.JpanelSymbols.SymbolSelectedChangedListener;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.ButtonColorSelector;
import com.supermap.desktop.ui.controls.ComponentBorderPanel.CompTitledPane;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.InternalToolkitControl;
import com.supermap.desktop.utilties.FillGradientModeUtilities;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author XiaJt
 */
public class SymbolDialogFill extends SymbolDialog {
	private JPanel panelMain;
	private JLabel labelForceColor;
	private ButtonColorSelector buttonColorSelectorFore;

	private JLabel labelBackColor;
	private ButtonColorSelector buttonColorSelectorBack;

	private JCheckBox checkBoxBackOpaque;

	private JLabel labelLineType;
	private JButton buttonLineType;

	private JLabel labelOpaque;
	private JSpinner spinnerOpaque;
	private JLabel labelOpaqueUnit;


	private JPanel panelFill;
	private JCheckBox checkBoxFill;

	private JLabel labelFillType;
	private JComboBox<String> comboBoxFillType;

	private JLabel labelHorizontal;
	private JSpinner spinnerHorizontal;
	private JLabel labelHorizontalUnit;

	private JLabel labelVertical;
	private JSpinner spinnerVertical;
	private JLabel labelVerticalUnit;

	private JLabel labelAngle;
	private JSpinner spinnerAngle;
	private JLabel labelAngleUnit;

	private static final String[] fillTypeMode = new String[]{
			CoreProperties.getString("String_LINEAR"),
			CoreProperties.getString("String_RADIAL"),
			CoreProperties.getString("String_SQUARE")
	};

	private SymbolDialog symbolDialogLine;

	public SymbolDialogFill() {
		super();
	}

	public SymbolDialogFill(JDialog dialog) {
		super(dialog);
	}

	@Override
	protected void initComponentHook() {
		fillInit();
	}

	private void fillInit() {
		this.setTitle(ControlsProperties.getString("String_FillSymbolChoose"));
		fillInitComponents();
		fillInitLayout();
		fillAddListener();
		fillInitResources();
	}

	private void fillInitComponents() {
		panelMain = new JPanel();
		panelSymbols = new JPanelSymbolsFill();
		labelBackColor = new JLabel();
		buttonColorSelectorBack = new ButtonColorSelector();
		labelForceColor = new JLabel();
		buttonColorSelectorFore = new ButtonColorSelector();
		checkBoxBackOpaque = new JCheckBox();
		labelLineType = new JLabel();
		buttonLineType = new JButton();
		Dimension minimumSize = new Dimension(20, 23);
		buttonLineType.setMinimumSize(minimumSize);
		buttonLineType.setMaximumSize(minimumSize);
		buttonLineType.setPreferredSize(minimumSize);

		labelOpaque = new JLabel();
		spinnerOpaque = new JSpinner();
		labelOpaqueUnit = new JLabel();
		panelFill = new JPanel();
		checkBoxFill = new JCheckBox();
		labelFillType = new JLabel();
		comboBoxFillType = new JComboBox<>(new DefaultComboBoxModel<>(fillTypeMode));
		labelHorizontal = new JLabel();
		spinnerHorizontal = new JSpinner();
		labelHorizontalUnit = new JLabel();
		labelVertical = new JLabel();
		spinnerVertical = new JSpinner();
		labelVerticalUnit = new JLabel();
		labelAngle = new JLabel();
		spinnerAngle = new JSpinner();
		labelAngleUnit = new JLabel();

		checkBoxFill.setSelected(true);
		SymbolSpinnerUtilties.initSpinners(0, 100, 1, "##0", spinnerOpaque);
		SymbolSpinnerUtilties.initSpinners(-100, 100, 1, "##0.0", spinnerHorizontal, spinnerVertical);
		SymbolSpinnerUtilties.initSpinners(0, 360, 1, "##0", spinnerAngle);
	}

	/**
	 * 线型图片获取
	 *
	 * @return 图标
	 */
	private Icon getLineIcon() {
		Point2Ds point2Ds = new Point2Ds();
		point2Ds.add(new Point2D(9, 8));
		point2Ds.add(new Point2D(90, 8));
		GeoLine geoLine = new GeoLine(point2Ds);
		GeoStyle lineGeoStyle = currentGeoStyle;
		geoLine.setStyle(lineGeoStyle);
		BufferedImage bufferedImage = new BufferedImage(100, 16, BufferedImage.TYPE_INT_ARGB);
		geoLine.getStyle().setLineSymbolID(lineGeoStyle.getLineSymbolID());
		geoLine.getStyle().setLineWidth(0.1);
		InternalToolkitControl.internalDraw(geoLine, currentResources, bufferedImage.getGraphics());
		return new ImageIcon(flipVerticalJ2D(bufferedImage));
	}

	/**
	 * 使图像上下左右完全翻转，解决线颠倒问题
	 *
	 * @param bufferedImage 图片
	 * @return 图片
	 */
	private BufferedImage flipVerticalJ2D(BufferedImage bufferedImage) {
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		BufferedImage dstImage = new BufferedImage(width, height, bufferedImage.getType());
		AffineTransform affineTransform = new AffineTransform(1, 0, 0, -1, 0, height);
		AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return affineTransformOp.filter(bufferedImage, dstImage);
	}

	private void fillInitLayout() {
		initPanelFill();
		panelMain.setLayout(new GridBagLayout());
		panelMain.add(labelForceColor, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 5, 0, 0));
		panelMain.add(buttonColorSelectorFore, new GridBagConstraintsHelper(1, 0, 2, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 5));

		panelMain.add(labelBackColor, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 5, 0, 0));
		panelMain.add(buttonColorSelectorBack, new GridBagConstraintsHelper(1, 1, 2, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 5));

		panelMain.add(checkBoxBackOpaque, new GridBagConstraintsHelper(0, 2, 3, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 5, 0, 5));

		panelMain.add(labelLineType, new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 5, 0, 0));
		panelMain.add(buttonLineType, new GridBagConstraintsHelper(1, 3, 2, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 5));

		panelMain.add(labelOpaque, new GridBagConstraintsHelper(0, 4, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 5, 0, 0));
		panelMain.add(spinnerOpaque, new GridBagConstraintsHelper(1, 4, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 0));
		panelMain.add(labelOpaqueUnit, new GridBagConstraintsHelper(2, 4, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 5, 0, 5));

		panelMain.add(panelFill, new GridBagConstraintsHelper(0, 5, 3, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL));
		panelMain.add(new JPanel(), new GridBagConstraintsHelper(0, 6, 3, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));

	}

	private void initPanelFill() {
		JPanel panelCenter = new JPanel();
		panelCenter.setLayout(new GridBagLayout());
		panelCenter.add(labelFillType, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 5, 0, 0));
		panelCenter.add(comboBoxFillType, new GridBagConstraintsHelper(1, 0, 2, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 5, 0, 5));

		panelCenter.add(labelHorizontal, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 5, 0, 0));
		panelCenter.add(spinnerHorizontal, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 0));
		panelCenter.add(labelHorizontalUnit, new GridBagConstraintsHelper(2, 1, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.NONE).setInsets(5, 5, 0, 5));

		panelCenter.add(labelVertical, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 5, 0, 0));
		panelCenter.add(spinnerVertical, new GridBagConstraintsHelper(1, 2, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 0));
		panelCenter.add(labelVerticalUnit, new GridBagConstraintsHelper(2, 2, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.NONE).setInsets(5, 5, 0, 5));

		panelCenter.add(labelAngle, new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 5, 5, 0));
		panelCenter.add(spinnerAngle, new GridBagConstraintsHelper(1, 3, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 5, 0));
		panelCenter.add(labelAngleUnit, new GridBagConstraintsHelper(2, 3, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.NONE).setInsets(5, 5, 5, 5));

		panelFill = new CompTitledPane(this.checkBoxFill, panelCenter);
		Dimension minimumSize = new Dimension(20, (int) panelFill.getPreferredSize().getHeight());
		panelFill.setMinimumSize(minimumSize);
		panelFill.setMaximumSize(minimumSize);
		panelFill.setPreferredSize(minimumSize);
	}


	private void fillAddListener() {
		panelSymbols.addSymbolSelectedChangedListener(new SymbolSelectedChangedListener() {
			@Override
			public void SymbolSelectedChangedEvent(Symbol symbol) {
				if (symbol != null) {
					spinnerOpaque.setToolTipText(CommonProperties.getString(CommonProperties.UnSupport));
					spinnerOpaque.setEnabled(false);
				} else {
					spinnerOpaque.setToolTipText(null);
					spinnerOpaque.setEnabled(true);
				}
				geoStylePropertyChange.propertyChange();
			}

			@Override
			public void SymbolSelectedDoubleClicked() {
				enterPressed();
			}
		});

		buttonColorSelectorFore.addPropertyChangeListener(ButtonColorSelector.PROPERTY_COLOR, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				currentGeoStyle.setFillForeColor(buttonColorSelectorFore.getColor());
				geoStylePropertyChange.propertyChange();
			}
		});

		buttonColorSelectorBack.addPropertyChangeListener(ButtonColorSelector.PROPERTY_COLOR, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				currentGeoStyle.setFillBackColor(buttonColorSelectorBack.getColor());
				geoStylePropertyChange.propertyChange();
			}
		});

		checkBoxBackOpaque.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean isSelect = e.getStateChange() == ItemEvent.SELECTED;
				checkBoxBackOpaque.setSelected(isSelect);
				buttonColorSelectorBack.setEnabled(!isSelect);
				currentGeoStyle.setFillBackOpaque(!isSelect);
				geoStylePropertyChange.propertyChange();
			}
		});

		buttonLineType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getSymbolDialogLine();
				if (symbolDialogLine != null) {
					DialogResult result = symbolDialogLine.showDialog(currentGeoStyle, new ISymbolApply() {
						@Override
						public void apply(GeoStyle geoStyle) {
							currentGeoStyle.setLineSymbolID(geoStyle.getLineSymbolID());
							currentGeoStyle.setLineWidth(geoStyle.getLineWidth());
							currentGeoStyle.setLineColor(geoStyle.getLineColor());
							buttonLineType.setIcon(getLineIcon());
							geoStylePropertyChange.propertyChange();
						}
					});

					if (result == DialogResult.OK) {
						GeoStyle geoStyle = symbolDialogLine.getCurrentGeoStyle();
						SymbolDialogFill.this.currentGeoStyle.setLineSymbolID(geoStyle.getLineSymbolID());
						SymbolDialogFill.this.currentGeoStyle.setLineWidth(geoStyle.getLineWidth());
						SymbolDialogFill.this.currentGeoStyle.setLineColor(geoStyle.getLineColor());
						buttonLineType.setIcon(getLineIcon());
						geoStylePropertyChange.propertyChange();
					}
				}
			}
		});

		final JFormattedTextField textFieldOpaque = ((JSpinner.NumberEditor) spinnerOpaque.getEditor()).getTextField();
		textFieldOpaque.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				String text = textFieldOpaque.getText();
				if (!SymbolSpinnerUtilties.isLegitNumber(0, 100, text)) {
					textFieldOpaque.setForeground(wrongColor);
					return;
				} else {
					textFieldOpaque.setForeground(defaultColor);
				}
				Integer integer = Integer.valueOf(text);
				currentGeoStyle.setFillOpaqueRate(getUnOpaqueRate(integer));
				geoStylePropertyChange.propertyChange();
			}
		});

		checkBoxFill.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean isSelected = e.getStateChange() == ItemEvent.SELECTED;
				labelFillType.setEnabled(isSelected);
				comboBoxFillType.setEnabled(isSelected);
				labelHorizontal.setEnabled(isSelected);
				spinnerHorizontal.setEnabled(isSelected);
				labelHorizontalUnit.setEnabled(isSelected);
				labelVertical.setEnabled(isSelected);
				spinnerVertical.setEnabled(isSelected);
				labelVerticalUnit.setEnabled(isSelected);
				labelAngle.setEnabled(isSelected);
				spinnerAngle.setEnabled(isSelected);
				labelAngleUnit.setEnabled(isSelected);
				currentGeoStyle.setFillGradientMode(isSelected ? FillGradientModeUtilities.getFillGradientMode(String.valueOf(comboBoxFillType.getSelectedItem())) : FillGradientMode.NONE);
				geoStylePropertyChange.propertyChange();
			}
		});

		comboBoxFillType.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					currentGeoStyle.setFillGradientMode(FillGradientModeUtilities.getFillGradientMode(String.valueOf(comboBoxFillType.getSelectedItem())));
					geoStylePropertyChange.propertyChange();
				}
			}
		});

		final JFormattedTextField textFieldHorizontal = ((JSpinner.NumberEditor) spinnerHorizontal.getEditor()).getTextField();
		textFieldHorizontal.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				String text = textFieldHorizontal.getText();
				if (!SymbolSpinnerUtilties.isLegitNumber(-100d, 100d, text)) {
					textFieldHorizontal.setForeground(wrongColor);
					return;
				} else {
					textFieldHorizontal.setForeground(defaultColor);
				}
				Double aDouble = Double.valueOf(text);
				currentGeoStyle.setFillGradientOffsetRatioX(aDouble);
				geoStylePropertyChange.propertyChange();
			}
		});

		final JFormattedTextField textFieldVertical = ((JSpinner.NumberEditor) spinnerVertical.getEditor()).getTextField();
		textFieldVertical.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				String text = textFieldVertical.getText();
				if (!SymbolSpinnerUtilties.isLegitNumber(-100d, 100d, text)) {
					textFieldVertical.setForeground(wrongColor);
					return;
				} else {
					textFieldVertical.setForeground(defaultColor);
				}
				Double aDouble = Double.valueOf(text);
				currentGeoStyle.setFillGradientOffsetRatioY(aDouble);
				geoStylePropertyChange.propertyChange();
			}
		});

		final JFormattedTextField textFieldAngle = ((JSpinner.NumberEditor) spinnerAngle.getEditor()).getTextField();
		textFieldAngle.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				String text = textFieldAngle.getText();
				if (!SymbolSpinnerUtilties.isLegitNumber(-100d, 100d, text)) {
					textFieldAngle.setForeground(wrongColor);
					return;
				} else {
					textFieldAngle.setForeground(defaultColor);
				}
				Double aDouble = Double.valueOf(text);
				currentGeoStyle.setFillGradientAngle(aDouble);
				geoStylePropertyChange.propertyChange();
			}
		});
	}

	private void getSymbolDialogLine() {
		if (symbolDialogLine == null) {
			symbolDialogLine = new SymbolDialogLine(this);
			symbolDialogLine.setSize((int) (symbolDialogLine.getSize().getWidth() * 0.9), (int) (symbolDialogLine.getSize().getHeight() * 0.7));
			symbolDialogLine.setLocationRelativeTo(null);
		}
	}

	private void fillInitResources() {
		labelForceColor.setText(CoreProperties.getString("String_Label_ForeColor"));
		labelBackColor.setText(CoreProperties.getString("String_Label_BackColor"));
		checkBoxBackOpaque.setText(CoreProperties.getString("String_BackOpaque"));
		labelLineType.setText(ControlsProperties.getString("String_Label_SymbolLineSelect"));
		labelOpaque.setText(ControlsProperties.getString("String_Label_Transparence"));
		labelOpaqueUnit.setText(ControlsProperties.getString("String_precent"));
		checkBoxFill.setText(ControlsProperties.getString("String_FillGradient"));
		labelFillType.setText(ControlsProperties.getString("String_Label_Type"));
		labelHorizontal.setText(ControlsProperties.getString("String_Label_OffsetRatioX"));
		labelHorizontalUnit.setText(ControlsProperties.getString("String_precent"));
		labelVertical.setText(ControlsProperties.getString("String_Label_OffsetRatioY"));
		labelVerticalUnit.setText(ControlsProperties.getString("String_precent"));
		labelAngle.setText(ControlsProperties.getString("String_Label_OffsetAngle"));
		labelAngleUnit.setText(ControlsProperties.getString("String_angle"));
	}

	@Override
	protected void prepareForShowDialogHook() {
		buttonColorSelectorFore.setColor(currentGeoStyle.getFillForeColor());
		buttonColorSelectorBack.setColor(currentGeoStyle.getFillBackColor());
		checkBoxBackOpaque.setSelected(currentGeoStyle.getFillBackOpaque());
		buttonLineType.setIcon(getLineIcon());
		spinnerOpaque.setValue(Double.valueOf(getUnOpaqueRate(currentGeoStyle.getFillOpaqueRate())));//不转Double，linux抛异常
		checkBoxFill.setSelected(currentGeoStyle.getFillGradientMode() != FillGradientMode.NONE);
		comboBoxFillType.setSelectedItem(FillGradientModeUtilities.getFillGradientMode(currentGeoStyle.getFillGradientMode()));
		spinnerHorizontal.setValue(currentGeoStyle.getFillGradientOffsetRatioX());
		spinnerVertical.setValue(currentGeoStyle.getFillGradientOffsetRatioY());
		spinnerAngle.setValue(currentGeoStyle.getFillGradientAngle());
	}

	@Override
	protected SymbolType getSymbolType() {
		return SymbolType.FILL;
	}

	@Override
	protected JPanel getPanelMain() {
		return panelMain;
	}

	@Override
	protected SymbolLibrary getLibrary() {
		return currentResources.getFillLibrary();
	}
}
