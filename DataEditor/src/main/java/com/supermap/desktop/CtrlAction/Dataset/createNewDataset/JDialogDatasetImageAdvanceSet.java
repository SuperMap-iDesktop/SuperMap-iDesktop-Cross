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
 * Created by yuanR on 2017/8/15 0015.
 * 新建影像数据集高级设置面板
 */
public class JDialogDatasetImageAdvanceSet extends SmDialog {

	private static final long serialVersionUID = 1L;

	private PanelBasicInfoSet basicInfoPanel;
	private PanelResolution resolutionPanel;
	private ImageDatasetPropertyPanel imagePropertyPanel;
	private PanelDatasetBounds datasetBoundsPanel;
	private SmButton buttonOk;
	private SmButton buttonCancel;

	private NewDatasetBean newDatasetBean;
	private GridImageExtraDatasetBean gridImageExtraDatasetBean;

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


	public JDialogDatasetImageAdvanceSet(NewDatasetBean newDatasetBean) {
		this.newDatasetBean = newDatasetBean;
		this.gridImageExtraDatasetBean = this.newDatasetBean.getGridImageExtraDatasetBean();
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

		basicInfoPanel.initStates(newDatasetBean);

		double x = this.gridImageExtraDatasetBean.getRectangle().getWidth() / this.gridImageExtraDatasetBean.getWidth();
		double y = this.gridImageExtraDatasetBean.getRectangle().getHeight() / this.gridImageExtraDatasetBean.getHeight();
		resolutionPanel.initStates(x, y, this.gridImageExtraDatasetBean.getWidth(), this.gridImageExtraDatasetBean.getHeight());

		datasetBoundsPanel.initStates(this.gridImageExtraDatasetBean.getRectangle());

		imagePropertyPanel.initStates(this.gridImageExtraDatasetBean.getBlockSizeOption(),
				this.gridImageExtraDatasetBean.getPixelFormat(),
				this.gridImageExtraDatasetBean.getBandCount());
	}


	private void initComponents() {

		basicInfoPanel = new PanelBasicInfoSet(DatasetType.IMAGE);
		resolutionPanel = new PanelResolution();
		resolutionPanel.setBorder(BorderFactory.createTitledBorder(DataEditorProperties.getString("String_NewDataset_RatioInfo")));
		imagePropertyPanel = new ImageDatasetPropertyPanel();
		datasetBoundsPanel = new PanelDatasetBounds();

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
								.addComponent(this.basicInfoPanel)
								.addComponent(this.resolutionPanel))
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(this.imagePropertyPanel)
								.addComponent(this.datasetBoundsPanel))));
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.basicInfoPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.imagePropertyPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.resolutionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.datasetBoundsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)));
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

		this.datasetBoundsPanel.getTextFieldCurrentViewLeft().getTextField().addCaretListener(caretListener);
		this.datasetBoundsPanel.getTextFieldCurrentViewRight().getTextField().addCaretListener(caretListener);
		this.datasetBoundsPanel.getTextFieldCurrentViewBottom().getTextField().addCaretListener(caretListener);
		this.datasetBoundsPanel.getTextFieldCurrentViewTop().getTextField().addCaretListener(caretListener);

		this.resolutionPanel.getTextFieldResolutionX().getTextField().addCaretListener(caretListener);
		this.resolutionPanel.getTextFieldResolutionY().getTextField().addCaretListener(caretListener);

		//  用于确定按钮是否可用的判断
		this.basicInfoPanel.getDatasetNameTextField().getDocument().addDocumentListener(textFieldChangedListener);
		this.imagePropertyPanel.getTextFieldImageDatasetbandCount().getTextField().getDocument().addDocumentListener(textFieldChangedListener);
		this.resolutionPanel.getTextFieldColumnCount().getDocument().addDocumentListener(textFieldChangedListener);
		this.resolutionPanel.getTextFieldRowCount().getDocument().addDocumentListener(textFieldChangedListener);

	}


	private CaretListener caretListener = new CaretListener() {
		@Override
		public void caretUpdate(CaretEvent e) {
			String resolutionX = resolutionPanel.getTextFieldResolutionX().getText();
			String resolutionY = resolutionPanel.getTextFieldResolutionY().getText();

			if (!StringUtilities.isNullOrEmpty(resolutionX) && !StringUtilities.isNullOrEmpty(resolutionY)) {
				double X = Double.valueOf(resolutionPanel.getTextFieldResolutionX().getText());
				double Y = Double.valueOf(resolutionPanel.getTextFieldResolutionY().getText());
				Rectangle2D rectangle2D = datasetBoundsPanel.getRangeBound();
				if (X != 0.0 && Y != 0.0 && rectangle2D != null) {
					int width = (int) (rectangle2D.getWidth() / X);
					int height = (int) (rectangle2D.getHeight() / Y);
					resolutionPanel.getTextFieldRowCount().setText(String.valueOf(height));
					resolutionPanel.getTextFieldColumnCount().setText(String.valueOf(width));
				} else {
					resolutionPanel.getTextFieldRowCount().setText("0");
					resolutionPanel.getTextFieldColumnCount().setText("0");
				}
			} else {
				resolutionPanel.getTextFieldRowCount().setText("0");
				resolutionPanel.getTextFieldColumnCount().setText("0");
			}
		}
	};

	private void buttonOk_Clicked() {
		// 当点击了确定按钮,给属性类设值
		String name = this.basicInfoPanel.getDatasetNameTextField().getText();
		Datasource datasource = this.basicInfoPanel.getDatasourceComboBox().getSelectedDatasource();
		EncodeType encodeType = this.basicInfoPanel.getEncodeType();

		int width = Integer.valueOf(resolutionPanel.getTextFieldColumnCount().getText());
		int height = Integer.valueOf(resolutionPanel.getTextFieldRowCount().getText());

		BlockSizeOption blockSizeOption = BlockSizeOptionUtilities.valueOf(((String) imagePropertyPanel.getComboboxBlockSizeOption().getSelectedItem()));
		PixelFormat pixelFormat = PixelFormatUtilities.valueOf(((String) imagePropertyPanel.getComboboxPixelFormat().getSelectedItem()));
		int bandCount = Integer.valueOf(imagePropertyPanel.getTextFieldImageDatasetbandCount().getTextField().getText());

		Rectangle2D rectangle2D = datasetBoundsPanel.getRangeBound();

		newDatasetBean.setDatasource(datasource);
		newDatasetBean.setDatasetName(name);
		newDatasetBean.setDatasetType(DatasetType.IMAGE);
		newDatasetBean.setEncodeType(encodeType);
		newDatasetBean.getGridImageExtraDatasetBean().setBlockSizeOption(blockSizeOption);
		newDatasetBean.getGridImageExtraDatasetBean().setPixelFormat(pixelFormat);
		newDatasetBean.getGridImageExtraDatasetBean().setHeight(height);
		newDatasetBean.getGridImageExtraDatasetBean().setWidth(width);
		newDatasetBean.getGridImageExtraDatasetBean().setBandCount(bandCount);
		if (rectangle2D != null) {
			newDatasetBean.getGridImageExtraDatasetBean().setRectangle(rectangle2D);
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
			if (isAvailableDatasetName(basicInfoPanel.getDatasetNameTextField().getText())) {
				setDatasetNameSuitable(true);
				basicInfoPanel.getDatasetNameTextField().setForeground(Color.BLACK);
			} else {
				setDatasetNameSuitable(false);
				basicInfoPanel.getDatasetNameTextField().setForeground(Color.RED);
			}

			// 波段
			if (isBandCountSuitable(imagePropertyPanel.getTextFieldImageDatasetbandCount().getTextField().getText())) {
				setBandCountSuitable(true);
				imagePropertyPanel.getTextFieldImageDatasetbandCount().getTextField().setForeground(Color.BLACK);
			} else {
				setBandCountSuitable(false);
				imagePropertyPanel.getTextFieldImageDatasetbandCount().getTextField().setForeground(Color.RED);
			}

			// 像素-行

			if (isWidthHeightSuitable(resolutionPanel.getTextFieldColumnCount().getText())) {
				setWidthHeightSuitable(true);
				resolutionPanel.getTextFieldColumnCount().setForeground(Color.BLACK);
			} else {
				setWidthHeightSuitable(false);
				resolutionPanel.getTextFieldColumnCount().setForeground(Color.RED);
			}

			// 像素-列
			if (isWidthHeightSuitable(resolutionPanel.getTextFieldRowCount().getText())) {
				setWidthHeightSuitable(true);
				resolutionPanel.getTextFieldRowCount().setForeground(Color.BLACK);
			} else {
				setWidthHeightSuitable(false);
				resolutionPanel.getTextFieldRowCount().setForeground(Color.RED);
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
			if (basicInfoPanel.getDatasourceComboBox().getSelectedDatasource() != null) {
				if (basicInfoPanel.getDatasourceComboBox().getSelectedDatasource().getDatasets().isAvailableDatasetName(name)) {
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
