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
import java.util.ArrayList;

/**
 * Created by yuanR on 2017/8/15 0015.
 * 新建影像数据集高级设置面板
 */
public class JDialogDatasetImageAdvanceSet extends SmDialog {

	private static final long serialVersionUID = 1L;

	private PanelBasicInfoSet panelBasicInfoSet;
	private PanelResolution panelResolution;
	private PanelDatasetImageProperty panelDatasetImageProperty;
	private PanelDatasetBounds panelDatasetBounds;
	private SmButton buttonOk;
	private SmButton buttonCancel;

	ArrayList<NewDatasetBean> datasetBeans;
	private NewDatasetBean datasetBeanFrist;

	private DatasetGridImageExtraBean gridImageExtraDatasetBean;

	private boolean isDatasetNameSuitable = true;
	private boolean isWidthHeightSuitable = true;
	private boolean isBandCountSuitable = true;

	/**
	 * ok按钮是否可用
	 *
	 * @param isDatasetNameSuitable
	 * @param isWidthHeightSuitable
	 * @param isBandCountSuitable
	 */
	public void setOKButtonEnabled(boolean isDatasetNameSuitable, boolean isWidthHeightSuitable, boolean isBandCountSuitable) {
		buttonOk.setEnabled(isDatasetNameSuitable && isWidthHeightSuitable && isBandCountSuitable);
	}


	/**
	 * 设置数据集名称是否正确
	 *
	 * @param isDatasetNameSuitable
	 */
	public void setDatasetNameSuitable(Boolean isDatasetNameSuitable) {
		this.isDatasetNameSuitable = isDatasetNameSuitable;
		setOKButtonEnabled(isDatasetNameSuitable, this.isWidthHeightSuitable, this.isBandCountSuitable);
	}

	/**
	 * 设置波段数是否正确
	 *
	 * @param isBandCountSuitable
	 */
	public void setBandCountSuitable(Boolean isBandCountSuitable) {
		this.isBandCountSuitable = isBandCountSuitable;
		setOKButtonEnabled(isDatasetNameSuitable, isWidthHeightSuitable, isBandCountSuitable);
	}

	/**
	 * 设置像素是否正确
	 *
	 * @param isWidthHeightSuitable
	 */
	public void setWidthHeightSuitable(Boolean isWidthHeightSuitable) {
		this.isWidthHeightSuitable = isWidthHeightSuitable;
		setOKButtonEnabled(isDatasetNameSuitable, isWidthHeightSuitable, this.isBandCountSuitable);
	}


	public JDialogDatasetImageAdvanceSet(ArrayList<NewDatasetBean> datasetBeans) {
		this.datasetBeans = datasetBeans;
		this.datasetBeanFrist = datasetBeans.get(0);
		this.gridImageExtraDatasetBean = this.datasetBeanFrist.getGridImageExtraDatasetBean();
		initComponents();
		initLayout();
		initStates();
		registerEvent();
		this.setTitle(DataEditorProperties.getString("String_NewDatasetImage"));
		this.setModal(true);
		setSize(700, 400);
		this.setLocationRelativeTo(null);
	}

	private void initStates() {

		panelBasicInfoSet.initStates(this.datasetBeanFrist);

		double x = this.gridImageExtraDatasetBean.getRectangle().getWidth() / this.gridImageExtraDatasetBean.getWidth();
		double y = this.gridImageExtraDatasetBean.getRectangle().getHeight() / this.gridImageExtraDatasetBean.getHeight();
		panelResolution.initStates(x, y, this.gridImageExtraDatasetBean.getWidth(), this.gridImageExtraDatasetBean.getHeight());

		panelDatasetBounds.initStates(this.gridImageExtraDatasetBean.getRectangle());

		panelDatasetImageProperty.initStates(this.gridImageExtraDatasetBean.getBlockSizeOption(),
				this.gridImageExtraDatasetBean.getPixelFormatImage(),
				this.gridImageExtraDatasetBean.getBandCount());
	}


	private void initComponents() {

		panelBasicInfoSet = new PanelBasicInfoSet(DatasetType.IMAGE);
		panelResolution = new PanelResolution();
		panelResolution.setBorder(BorderFactory.createTitledBorder(DataEditorProperties.getString("String_NewDataset_RatioInfo")));
		panelDatasetImageProperty = new PanelDatasetImageProperty();
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
								.addComponent(this.panelResolution))
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(this.panelDatasetImageProperty)
								.addComponent(this.panelDatasetBounds))));
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.panelBasicInfoSet)
						.addComponent(this.panelDatasetImageProperty))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.panelResolution)
						.addComponent(this.panelDatasetBounds, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)));
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
		this.panelDatasetImageProperty.getTextFieldImageDatasetbandCount().getTextField().getDocument().addDocumentListener(textFieldChangedListener);
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

		BlockSizeOption blockSizeOption = BlockSizeOptionUtilities.valueOf(((String) panelDatasetImageProperty.getComboboxBlockSizeOption().getSelectedItem()));
		PixelFormat pixelFormat = PixelFormatUtilities.valueOf(((String) panelDatasetImageProperty.getComboboxPixelFormat().getSelectedItem()));
		int bandCount = Integer.valueOf(panelDatasetImageProperty.getTextFieldImageDatasetbandCount().getTextField().getText());

		Rectangle2D rectangle2D = panelDatasetBounds.getRangeBound();
		for (int i = 0; i < datasetBeans.size(); i++) {
			datasetBeans.get(i).setDatasource(datasource);
			datasetBeans.get(i).setDatasetName(name);
			datasetBeans.get(i).setDatasetType(DatasetType.IMAGE);
			datasetBeans.get(i).setEncodeType(encodeType);
			datasetBeans.get(i).getGridImageExtraDatasetBean().setBlockSizeOption(blockSizeOption);
			datasetBeans.get(i).getGridImageExtraDatasetBean().setPixelFormatImage(pixelFormat);
			datasetBeans.get(i).getGridImageExtraDatasetBean().setHeight(height);
			datasetBeans.get(i).getGridImageExtraDatasetBean().setWidth(width);
			datasetBeans.get(i).getGridImageExtraDatasetBean().setBandCount(bandCount);
			datasetBeans.get(i).getGridImageExtraDatasetBean().setRectangle(rectangle2D);
		}


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

			// 波段
			if (isBandCountSuitable(panelDatasetImageProperty.getTextFieldImageDatasetbandCount().getTextField().getText())) {
				setBandCountSuitable(true);
				panelDatasetImageProperty.getTextFieldImageDatasetbandCount().getTextField().setForeground(Color.BLACK);
			} else {
				setBandCountSuitable(false);
				panelDatasetImageProperty.getTextFieldImageDatasetbandCount().getTextField().setForeground(Color.RED);
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
		 * 判断波段值是否正确
		 *
		 * @param value
		 * @return
		 */
		private boolean isBandCountSuitable(String value) {
			if (!StringUtilities.isNullOrEmpty(value)) {
				int bandCount = Integer.valueOf(value);
				if (100 < bandCount || bandCount < 1) {
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
