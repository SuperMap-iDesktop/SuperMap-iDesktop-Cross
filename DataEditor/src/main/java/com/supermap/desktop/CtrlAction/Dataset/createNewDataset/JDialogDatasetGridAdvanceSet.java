package com.supermap.desktop.CtrlAction.Dataset.createNewDataset;

import com.supermap.data.*;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.BlockSizeOptionUtilities;
import com.supermap.desktop.utilities.PixelFormatUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by yuanR on 2017/8/17 0017.
 * 新建栅格数据集高级设置面板
 */
public class JDialogDatasetGridAdvanceSet extends SmDialog {

	private static final long serialVersionUID = 1L;

	private PanelBasicInfoSet panelBasicInfoSet;
	private PanelResolution panelResolution;
	private PanelDatasetGridProperty panelDatasetGridProperty;
	private PanelDatasetBounds panelDatasetBounds;

	private SmButton buttonOk;
	private SmButton buttonCancel;

	private NewDatasetBean newDatasetBean;
	private GridImageExtraDatasetBean gridImageExtraDatasetBean;

	private boolean isDatasetNameSuitable = true;
	private boolean isWidthHeightSuitable = true;
	private boolean isMaxMinValueSuitable = true;

	/**
	 * ok按钮是否可用
	 *
	 * @param isDatasetNameSuitable
	 * @param isWidthHeightSuitable
	 * @param isMaxMinValueSuitable
	 */
	public void setOKButtonEnabled(boolean isDatasetNameSuitable, boolean isWidthHeightSuitable, boolean isMaxMinValueSuitable) {
		buttonOk.setEnabled(isDatasetNameSuitable && isWidthHeightSuitable && isMaxMinValueSuitable);
	}

	/**
	 * 设置数据集名称是否正确
	 *
	 * @param isDatasetNameSuitable
	 */
	public void setDatasetNameSuitable(Boolean isDatasetNameSuitable) {
		this.isDatasetNameSuitable = isDatasetNameSuitable;
		setOKButtonEnabled(isDatasetNameSuitable, this.isWidthHeightSuitable, this.isMaxMinValueSuitable);
	}

	/**
	 * 设置最大最小值是否正确
	 *
	 * @param isMaxMinValueSuitable
	 */
	public void setMaxMinValueSuitable(Boolean isMaxMinValueSuitable) {
		this.isMaxMinValueSuitable = isMaxMinValueSuitable;
		setOKButtonEnabled(isDatasetNameSuitable, isWidthHeightSuitable, isMaxMinValueSuitable);
	}

	/**
	 * 设置像素是否正确
	 *
	 * @param isWidthHeightSuitable
	 */
	public void setWidthHeightSuitable(Boolean isWidthHeightSuitable) {
		this.isWidthHeightSuitable = isWidthHeightSuitable;
		setOKButtonEnabled(isDatasetNameSuitable, isWidthHeightSuitable, this.isMaxMinValueSuitable);
	}

	public JDialogDatasetGridAdvanceSet(NewDatasetBean newDatasetBean) {
		this.newDatasetBean = newDatasetBean;
		this.gridImageExtraDatasetBean = this.newDatasetBean.getGridImageExtraDatasetBean();
		initComponents();
		initLayout();
		initStates();
		registerEvent();
		this.setTitle(DataEditorProperties.getString("String_NewDatasetGrid"));
		this.setModal(true);
		setSize(700, 400);
		this.setLocationRelativeTo(null);
	}

	private void initStates() {
		panelBasicInfoSet.initStates(newDatasetBean);

		double x = this.gridImageExtraDatasetBean.getRectangle().getWidth() / this.gridImageExtraDatasetBean.getWidth();
		double y = this.gridImageExtraDatasetBean.getRectangle().getHeight() / this.gridImageExtraDatasetBean.getHeight();
		panelResolution.initStates(x, y, this.gridImageExtraDatasetBean.getWidth(), this.gridImageExtraDatasetBean.getHeight());

		panelDatasetBounds.initStates(this.gridImageExtraDatasetBean.getRectangle());

		panelDatasetGridProperty.initStates(
				this.gridImageExtraDatasetBean.getBlockSizeOption(),
				this.gridImageExtraDatasetBean.getPixelFormatGrid(),
				this.gridImageExtraDatasetBean.getNoValue(),
				this.gridImageExtraDatasetBean.getMaxValue(),
				this.gridImageExtraDatasetBean.getMinValue());
	}

	private void initComponents() {

		panelBasicInfoSet = new PanelBasicInfoSet(DatasetType.GRID);
		panelResolution = new PanelResolution();
		panelResolution.setBorder(BorderFactory.createTitledBorder(DataEditorProperties.getString("String_NewDataset_RatioInfo")));
		panelDatasetGridProperty = new PanelDatasetGridProperty();
		panelDatasetBounds = new PanelDatasetBounds();

		// 按钮
		buttonOk = new SmButton(CommonProperties.getString(CommonProperties.OK));
		buttonCancel = new SmButton(CommonProperties.getString(CommonProperties.Cancel));
		this.getRootPane().setDefaultButton(this.buttonOk);
	}

	private void initLayout() {
		JPanel centerPanel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(centerPanel);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		centerPanel.setLayout(groupLayout);
		//@formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup()
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(this.panelBasicInfoSet)
								.addComponent(this.panelDatasetGridProperty))
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(this.panelDatasetBounds)
								.addComponent(this.panelResolution))));
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(this.panelBasicInfoSet)
						.addComponent(this.panelDatasetBounds, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(this.panelDatasetGridProperty)
						.addComponent(this.panelResolution))
		);
		//@formatter:on

		// 按钮面板
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());

		buttonPanel.add(buttonOk, new GridBagConstraintsHelper(0, 0).setAnchor(GridBagConstraints.EAST).setInsets(5).setWeight(1, 1));
		buttonPanel.add(buttonCancel, new GridBagConstraintsHelper(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(5, 0, 5, 5).setWeight(0, 1));

		this.setLayout(new GridBagLayout());
		this.add(centerPanel, new GridBagConstraintsHelper(0, 0).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setInsets(5));
		this.add(buttonPanel, new GridBagConstraintsHelper(0, 1).setWeight(1, 0).setFill(GridBagConstraints.BOTH).setInsets(5));
		// @formatter:on

	}

	private void registerEvent() {
		this.buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonOk_Clicked();
			}
		});
		this.buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonCancel_Clicked();
			}
		});

		this.panelDatasetBounds.getTextFieldCurrentViewLeft().getTextField().addCaretListener(caretListener);
		this.panelDatasetBounds.getTextFieldCurrentViewRight().getTextField().addCaretListener(caretListener);
		this.panelDatasetBounds.getTextFieldCurrentViewBottom().getTextField().addCaretListener(caretListener);
		this.panelDatasetBounds.getTextFieldCurrentViewTop().getTextField().addCaretListener(caretListener);

		this.panelResolution.getTextFieldResolutionX().getTextField().addCaretListener(caretListener);
		this.panelResolution.getTextFieldResolutionY().getTextField().addCaretListener(caretListener);

		//  用于确定按钮是否可用的判断
		this.panelBasicInfoSet.getDatasetNameTextField().getDocument().addDocumentListener(textFieldChangedListener);
		this.panelDatasetGridProperty.getTextFieldMaxValue().getTextField().getDocument().addDocumentListener(textFieldChangedListener);
		this.panelDatasetGridProperty.getTextFieldMinValue().getTextField().getDocument().addDocumentListener(textFieldChangedListener);
		this.panelResolution.getTextFieldColumnCount().getDocument().addDocumentListener(textFieldChangedListener);
		this.panelResolution.getTextFieldRowCount().getDocument().addDocumentListener(textFieldChangedListener);

	}


	private CaretListener caretListener = new CaretListener() {
		@Override
		public void caretUpdate(CaretEvent e) {
			String resolutionX = panelResolution.getTextFieldResolutionX().getText();
			String resolutionY = panelResolution.getTextFieldResolutionY().getText();

			if (!StringUtilities.isNullOrEmpty(resolutionX) && !StringUtilities.isNullOrEmpty(resolutionY)) {
				double X = Double.valueOf(panelResolution.getTextFieldResolutionX().getText());
				double Y = Double.valueOf(panelResolution.getTextFieldResolutionY().getText());
				Rectangle2D rectangle2D = panelDatasetBounds.getRangeBound();
				if (X != 0.0 && Y != 0.0 && rectangle2D != null) {
					int width = (int) (rectangle2D.getWidth() / X);
					int height = (int) (rectangle2D.getHeight() / Y);
					panelResolution.getTextFieldRowCount().setText(String.valueOf(height));
					panelResolution.getTextFieldColumnCount().setText(String.valueOf(width));
				} else {
					panelResolution.getTextFieldRowCount().setText("0");
					panelResolution.getTextFieldColumnCount().setText("0");
				}
			} else {
				panelResolution.getTextFieldRowCount().setText("0");
				panelResolution.getTextFieldColumnCount().setText("0");
			}
		}
	};

	private void buttonOk_Clicked() {
		// 当点击了确定按钮,给属性类设值
		String name = this.panelBasicInfoSet.getDatasetNameTextField().getText();
		Datasource datasource = this.panelBasicInfoSet.getDatasourceComboBox().getSelectedDatasource();
		EncodeType encodeType = this.panelBasicInfoSet.getEncodeType();

		int width = Integer.valueOf(panelResolution.getTextFieldColumnCount().getText());
		int height = Integer.valueOf(panelResolution.getTextFieldRowCount().getText());

		BlockSizeOption blockSizeOption = BlockSizeOptionUtilities.valueOf(((String) panelDatasetGridProperty.getComboboxBlockSizeOption().getSelectedItem()));
		PixelFormat pixelFormat = PixelFormatUtilities.valueOf(((String) panelDatasetGridProperty.getComboboxPixelFormat().getSelectedItem()));
		double maxValue = Double.valueOf(panelDatasetGridProperty.getTextFieldMaxValue().getTextField().getText());
		double minValue = Double.valueOf(panelDatasetGridProperty.getTextFieldMinValue().getTextField().getText());
		double noValue = Double.valueOf(panelDatasetGridProperty.getTextFieldNoValue().getTextField().getText());

		Rectangle2D rectangle2D = panelDatasetBounds.getRangeBound();

		newDatasetBean.setDatasource(datasource);
		newDatasetBean.setDatasetName(name);
		newDatasetBean.setDatasetType(DatasetType.GRID);
		newDatasetBean.setEncodeType(encodeType);
		newDatasetBean.getGridImageExtraDatasetBean().setBlockSizeOption(blockSizeOption);
		newDatasetBean.getGridImageExtraDatasetBean().setPixelFormatGrid(pixelFormat);
		newDatasetBean.getGridImageExtraDatasetBean().setHeight(height);
		newDatasetBean.getGridImageExtraDatasetBean().setWidth(width);
		newDatasetBean.getGridImageExtraDatasetBean().setMaxValue(maxValue);
		newDatasetBean.getGridImageExtraDatasetBean().setMinValue(minValue);
		newDatasetBean.getGridImageExtraDatasetBean().setNoValue(noValue);
		newDatasetBean.getGridImageExtraDatasetBean().setRectangle(rectangle2D);

		setDialogResult(DialogResult.OK);
		this.dispose();
	}

	private void buttonCancel_Clicked() {
		setDialogResult(DialogResult.CANCEL);
		this.dispose();
	}

	private DocumentListener textFieldChangedListener = new JTextFieldChangedListener();

	/**
	 * yuanR 2017.8.17
	 * 称文本框添加的改变事件，用以当名称输入错误、波段、像素值错误时，置灰确定按钮
	 */
	class JTextFieldChangedListener implements DocumentListener {
		@Override
		public void changedUpdate(DocumentEvent e) {
			newFilter();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			newFilter();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			newFilter();
		}

		/**
		 * 当文本框改变时
		 */
		private void newFilter() {

			// 数据名称
			if (isAvailableDatasetName(panelBasicInfoSet.getDatasetNameTextField().getText())) {
				setDatasetNameSuitable(true);
				panelBasicInfoSet.getDatasetNameTextField().setForeground(Color.BLACK);
			} else {
				setDatasetNameSuitable(false);
				panelBasicInfoSet.getDatasetNameTextField().setForeground(Color.RED);
			}

			// 最大值/最小值
			if (isMaxMinValueSuitable(panelDatasetGridProperty.getTextFieldMaxValue().getTextField().getText(),
					panelDatasetGridProperty.getTextFieldMinValue().getTextField().getText())) {
				setMaxMinValueSuitable(true);
				panelDatasetGridProperty.getTextFieldMaxValue().getTextField().setForeground(Color.BLACK);
				panelDatasetGridProperty.getTextFieldMinValue().getTextField().setForeground(Color.BLACK);
			} else {
				setMaxMinValueSuitable(false);
				panelDatasetGridProperty.getTextFieldMaxValue().getTextField().setForeground(Color.RED);
				panelDatasetGridProperty.getTextFieldMinValue().getTextField().setForeground(Color.RED);
			}


			// 像素-行

			if (isWidthHeightSuitable(panelResolution.getTextFieldColumnCount().getText())) {
				setWidthHeightSuitable(true);
				panelResolution.getTextFieldColumnCount().setForeground(Color.BLACK);
			} else {
				setWidthHeightSuitable(false);
				panelResolution.getTextFieldColumnCount().setForeground(Color.RED);
			}

			// 像素-列
			if (isWidthHeightSuitable(panelResolution.getTextFieldRowCount().getText())) {
				setWidthHeightSuitable(true);
				panelResolution.getTextFieldRowCount().setForeground(Color.BLACK);
			} else {
				setWidthHeightSuitable(false);
				panelResolution.getTextFieldRowCount().setForeground(Color.RED);
			}
		}

		/**
		 * 判断最大最小值是否正确
		 *
		 * @param maxValue
		 * @param minValue
		 * @return
		 */
		private boolean isMaxMinValueSuitable(String maxValue, String minValue) {
			if (!StringUtilities.isNullOrEmpty(maxValue) && !StringUtilities.isNullOrEmpty(minValue)) {
				double max = StringUtilities.getNumber(maxValue);
				double min = StringUtilities.getNumber(minValue);
				if (max <= min) {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		}


		/**
		 * 判断数据集名称是否合法
		 *
		 * @param name
		 * @return
		 */
		private boolean isAvailableDatasetName(String name) {
			if (panelBasicInfoSet.getDatasourceComboBox().getSelectedDatasource() != null) {
				if (panelBasicInfoSet.getDatasourceComboBox().getSelectedDatasource().getDatasets().isAvailableDatasetName(name)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}

		/**
		 * 判断像素值是否合法
		 *
		 * @param value
		 * @return
		 */
		public boolean isWidthHeightSuitable(String value) {
			if (!StringUtilities.isNullOrEmpty(value)) {
				int count = Integer.valueOf(value);
				if (count < 1) {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		}
	}


}
