package com.supermap.desktop.CtrlAction.settings;

import com.supermap.data.CUDACapability;
import com.supermap.data.Environment;
import com.supermap.data.OpenCLCapability;
import com.supermap.desktop.Application;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.dialog.symbolDialogs.SymbolSpinnerUtilties;
import com.supermap.desktop.frame.FrameProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.FileChooserControl;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

/**
 * @author XiaJT
 */
public class JPanelSettingEnvironment extends BaseSettingPanel {

	private JPanel panelBasicSetting;

	private JLabel labelTitle;// 桌面标题
	private SmTextFieldLegit smTextFieldLegitTitle;

	private JLabel labelOMPNumThreads; // 并行计算线程数
	private JSpinner spinnerOMPNumThreads;

	private JLabel labelAnalystMemorySize;// 分析内存模式
	private JComboBox<String> comboBoxAnalystMemorySize;

	private JCheckBox checkBoxSceneAntialias;// 场景反走样系数
	private JSpinner spinnerSceneAntialiasValue;

	private JCheckBox checkBoxGPUComputingEnabled;//是否启用GPU

	private JLabel labelFileCache;
	private FileChooserControl fileChooserControlFileCache;

	private JPanel panelOutput;
	private JCheckBox checkBoxOutputInfo;
	private JCheckBox checkBoxOutputException;

	private JPanel panelRuntimeSetting;

	private static final String[] analystMemorySizeModels = new String[]{
			FrameProperties.getString("String_AnalystMemorySize_Normal"),
			FrameProperties.getString("String_AnalystMemorySize_Large")
	};
	private CUDACapability cudaCapability;
	private OpenCLCapability openCLCapability;
	private ItemListener itemListener;

	@Override
	protected void initComponents() {
		itemListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Component source = (Component) e.getSource();
				if (source == checkBoxGPUComputingEnabled && checkBoxGPUComputingEnabled.isSelected()) {
					if (cudaCapability == CUDACapability.CAPABLE) {
						Application.getActiveApplication().getOutput().output(FrameProperties.getString("String_SupportCUDA"));
					} else if (cudaCapability == CUDACapability.COMPUTECAPABILITYINSUFFICIENT) {
						Application.getActiveApplication().getOutput().output(FrameProperties.getString("String_CUDACOMPUTECAPABILITYINSUFFICIENT"));
					} else if (cudaCapability == CUDACapability.DRIVERINSUFFICIENT) {
						Application.getActiveApplication().getOutput().output(FrameProperties.getString("String_DRIVERINSUFFICIENT"));
					} else if (cudaCapability == CUDACapability.NODEVICE) {
						Application.getActiveApplication().getOutput().output(FrameProperties.getString("String_NODEVICE"));
					}

					if (openCLCapability == OpenCLCapability.CAPABLE) {
						Application.getActiveApplication().getOutput().output(FrameProperties.getString("String_SupportOpenCl"));
					} else if (openCLCapability == OpenCLCapability.NOAVAILABLEGPUDEVICE) {
						Application.getActiveApplication().getOutput().output(FrameProperties.getString("String_OpenClNOAVAILABLEGPUDEVICE"));
					} else if (openCLCapability == OpenCLCapability.NOPLATFORM) {
						Application.getActiveApplication().getOutput().output(FrameProperties.getString("String_OpenClNOPLATFORM"));
					}
				}
				if (changedValues.contains(source)) {
					changedValues.remove(source);
				} else {
					changedValues.add(source);
				}
			}
		};
		cudaCapability = Environment.checkCUDACapability();
		openCLCapability = Environment.checkOpenCLCapability();
		panelBasicSetting = new JPanel();
		panelBasicSetting.setBorder(BorderFactory.createTitledBorder(FrameProperties.getString("String_CaptionBaseSetting")));
		labelTitle = new JLabel();
		smTextFieldLegitTitle = new SmTextFieldLegit();
		labelOMPNumThreads = new JLabel();
		spinnerOMPNumThreads = new JSpinner(new SpinnerNumberModel(Environment.getOMPNumThreads(), 1, 16, 1));
		labelAnalystMemorySize = new JLabel();
		comboBoxAnalystMemorySize = new JComboBox<>(analystMemorySizeModels);
		checkBoxSceneAntialias = new JCheckBox();
		spinnerSceneAntialiasValue = new JSpinner(new SpinnerNumberModel(Environment.getSceneAntialiasValue(), 0, 16, 1));
		checkBoxGPUComputingEnabled = new JCheckBox();
		labelFileCache = new JLabel();
		fileChooserControlFileCache = new FileChooserControl();
		panelOutput = new JPanel();
		panelOutput.setBorder(BorderFactory.createTitledBorder(FrameProperties.getString("String_OutputLevel")));
		checkBoxOutputInfo = new JCheckBox();
		checkBoxOutputException = new JCheckBox();
		panelRuntimeSetting = new JPanel();
		panelRuntimeSetting.setBorder(BorderFactory.createTitledBorder(FrameProperties.getString("String_CaptionRunSetting")));

	}

	@Override
	protected void initLayout() {
		initPanelBasicSetting();
		initPanelRuntimeSetting();
		this.setLayout(new GridBagLayout());
		this.add(panelBasicSetting, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
		this.add(panelRuntimeSetting, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
		this.add(new JPanel(), new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
	}

	private void initPanelBasicSetting() {
		panelBasicSetting.setLayout(new GridBagLayout());
		panelBasicSetting.add(labelTitle, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setInsets(5, 5, 0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));
		panelBasicSetting.add(smTextFieldLegitTitle, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setInsets(5, 5, 0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL));

		panelBasicSetting.add(labelOMPNumThreads, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 0).setInsets(5, 5, 0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL));
		panelBasicSetting.add(spinnerOMPNumThreads, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 0).setInsets(5, 5, 0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL));

		panelBasicSetting.add(labelAnalystMemorySize, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(0, 0).setInsets(5, 5, 0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL));
		panelBasicSetting.add(comboBoxAnalystMemorySize, new GridBagConstraintsHelper(1, 2, 1, 1).setWeight(1, 0).setInsets(5, 5, 0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL));

		panelBasicSetting.add(checkBoxSceneAntialias, new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(0, 0).setInsets(5, 5, 0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL));
		panelBasicSetting.add(spinnerSceneAntialiasValue, new GridBagConstraintsHelper(1, 3, 1, 1).setWeight(1, 0).setInsets(5, 5, 0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL));

		panelBasicSetting.add(checkBoxGPUComputingEnabled, new GridBagConstraintsHelper(0, 4, 2, 1).setWeight(1, 0).setInsets(5, 5, 0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL));

	}

	private void initPanelRuntimeSetting() {
		panelOutput.setLayout(new GridBagLayout());
		panelOutput.add(checkBoxOutputInfo, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 0, 0));
		panelOutput.add(checkBoxOutputException, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 0, 0));
		panelOutput.add(new JPanel(), new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(2, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));

		panelRuntimeSetting.setLayout(new GridBagLayout());
		panelRuntimeSetting.add(labelFileCache, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 5, 0, 0));
		panelRuntimeSetting.add(fileChooserControlFileCache, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 0));

		panelRuntimeSetting.add(panelOutput, new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(5, 5, 0, 0));
	}

	@Override
	protected void initListeners() {
		smTextFieldLegitTitle.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				return !StringUtilities.isNullOrEmpty(textFieldValue);
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		});

		final JFormattedTextField OMPNumThreadsTextField = ((JSpinner.NumberEditor) spinnerOMPNumThreads.getEditor()).getTextField();
		OMPNumThreadsTextField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				String text = OMPNumThreadsTextField.getText();
				if (!SymbolSpinnerUtilties.isLegitNumber(1, 16, text)) {
					OMPNumThreadsTextField.setForeground(Color.RED);
					return;
				} else {
					OMPNumThreadsTextField.setForeground(Color.BLACK);
				}
				if (Integer.valueOf(text) != Environment.getOMPNumThreads()) {
					changedValues.add(spinnerOMPNumThreads);
				} else {
					changedValues.remove(spinnerOMPNumThreads);
				}
			}
		});
		final JFormattedTextField textField = ((JSpinner.NumberEditor) spinnerSceneAntialiasValue.getEditor()).getTextField();
		textField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				String text = textField.getText();
				if (!SymbolSpinnerUtilties.isLegitNumber(1, 16, text)) {
					textField.setForeground(Color.red);
					return;
				} else {
					textField.setForeground(Color.BLACK);
				}
				if (Integer.valueOf(text) != Environment.getSceneAntialiasValue()) {
					changedValues.add(spinnerSceneAntialiasValue);
				} else {
					changedValues.remove(spinnerSceneAntialiasValue);
				}
			}
		});

		comboBoxAnalystMemorySize.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					long value = comboBoxAnalystMemorySize.getSelectedIndex() == 0 ? 0 : -1;
					long analystMemorySize = Environment.getAnalystMemorySize() >= 0 ? 0 : -1;
					if (value != analystMemorySize) {
						changedValues.add(comboBoxAnalystMemorySize);
					} else {
						changedValues.remove(comboBoxAnalystMemorySize);
					}
				}
			}
		});
		checkBoxSceneAntialias.addItemListener(itemListener);
		checkBoxGPUComputingEnabled.addItemListener(itemListener);
		checkBoxOutputInfo.addItemListener(itemListener);
		checkBoxOutputException.addItemListener(itemListener);
		fileChooserControlFileCache.getButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String moduleName = "FileCacheDirectories";
				if (!SmFileChoose.isModuleExist(moduleName)) {
					SmFileChoose.addNewNode("", CommonProperties.getString("String_DefaultFilePath"), GlobalParameters.getDesktopTitle(),
							moduleName, "GetDirectories");
				}
				SmFileChoose fileChoose = new SmFileChoose(moduleName);
				int state = fileChoose.showDefaultDialog();
				if (state == JFileChooser.APPROVE_OPTION) {
					String directories = fileChoose.getFilePath();
					if (!directories.endsWith(File.separator)) {
						directories += File.separator;
					}
					fileChooserControlFileCache.getEditor().setText(directories);
//					changedValues.add(fileChooserControlFileCache);
				}
			}
		});
		fileChooserControlFileCache.getEditor().getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				changed();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				changed();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				changed();
			}

			private void changed() {
				changedValues.add(fileChooserControlFileCache);
			}
		});
	}

	@Override
	protected void initResources() {
		labelTitle.setText(FrameProperties.getString("String_DesktopName"));
		labelOMPNumThreads.setText(FrameProperties.getString("String_OMPThreadsNum"));
		labelAnalystMemorySize.setText(FrameProperties.getString("String_AnalystMemorySize"));
		checkBoxSceneAntialias.setText(FrameProperties.getString("String_UseSceneAntialias"));
		checkBoxGPUComputingEnabled.setText(FrameProperties.getString("String_StartGPU"));
		labelFileCache.setText(FrameProperties.getString("String_FileCacheFolder"));
		checkBoxOutputInfo.setText(FrameProperties.getString("String_OutputLevel"));
		checkBoxOutputException.setText(FrameProperties.getString("String_OutputLevelDebug"));

	}

	@Override
	protected void initComponentStates() {
		smTextFieldLegitTitle.setText(GlobalParameters.getDesktopTitle());
		comboBoxAnalystMemorySize.setSelectedIndex(Environment.getAnalystMemorySize() >= 0 ? 0 : 1);
		checkBoxSceneAntialias.setSelected(Environment.isSceneAntialias());
		if (cudaCapability != CUDACapability.CAPABLE && openCLCapability != OpenCLCapability.CAPABLE) {
			checkBoxGPUComputingEnabled.setEnabled(false);
		}
		checkBoxGPUComputingEnabled.setSelected(Environment.isCUDAComputingEnabled() || Environment.isOpenCLComputingEnabled());

		fileChooserControlFileCache.getEditor().setText(Environment.getFileCacheFolder());
		checkBoxOutputException.setSelected(GlobalParameters.isLogException());
		checkBoxOutputInfo.setSelected(GlobalParameters.isLogInformation());

	}

	@Override
	public void apply() {
		for (Component changedValue : changedValues) {
			if (changedValue == smTextFieldLegitTitle) {
				GlobalParameters.setDesktopTitle(smTextFieldLegitTitle.getBackUpValue());
				Application.getActiveApplication().getMainFrame().setText(GlobalParameters.getDesktopTitle());
			} else if (changedValue == spinnerOMPNumThreads) {
				Environment.setOMPNumThreads(((Number) spinnerOMPNumThreads.getValue()).intValue());
			} else if (changedValue == comboBoxAnalystMemorySize) {
				int selectedIndex = comboBoxAnalystMemorySize.getSelectedIndex();
				Environment.setAnalystMemorySize(selectedIndex == 0 ? 0 : -1);
			} else if (changedValue == spinnerSceneAntialiasValue) {
				Environment.setSceneAntialiasValue(((Number) spinnerSceneAntialiasValue.getValue()).intValue());
			} else if (changedValue == checkBoxGPUComputingEnabled) {
				if (cudaCapability == CUDACapability.CAPABLE) {
					Environment.setCUDAComputingEnabled(true);
				}
				if (openCLCapability == OpenCLCapability.CAPABLE) {
					Environment.setOpenCLComputingEnabled(true);
				}
			} else if (changedValue == fileChooserControlFileCache) {
				Environment.setFileCacheFolder(fileChooserControlFileCache.getEditor().getText());
			} else if (changedValue == checkBoxOutputInfo) {
				GlobalParameters.setLogInformation(checkBoxOutputInfo.isSelected());
			} else if (changedValue == checkBoxOutputException) {
				GlobalParameters.setLogException(checkBoxOutputException.isSelected());
			} else if (changedValue == checkBoxSceneAntialias) {
				Environment.setSceneAntialias(checkBoxSceneAntialias.isSelected());
			}
		}
	}

	@Override
	public void dispose() {

	}
}
