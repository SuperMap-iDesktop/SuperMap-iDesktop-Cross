package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.dataRun;

import com.supermap.analyst.spatialanalyst.MathAnalyst;
import com.supermap.data.DatasetGrid;
import com.supermap.data.PixelFormat;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.metaProcessImplements.MetaProcessGridAnalyst;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.DatasourceConstraint;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.ParameterButton;
import com.supermap.desktop.process.parameter.ipls.ParameterCheckBox;
import com.supermap.desktop.process.parameter.ipls.ParameterCombine;
import com.supermap.desktop.process.parameter.ipls.ParameterComboBox;
import com.supermap.desktop.process.parameter.ipls.ParameterSaveDataset;
import com.supermap.desktop.process.parameter.ipls.ParameterTextArea;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.RasterAlgebraExpressionXml;
import com.supermap.desktop.ui.RasterAlgebraOperationDialog;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.utilities.PixelFormatUtilities;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by lixiaoyao on 2017/8/29.
 * 栅格代数运算
 */
public class MetaProcessAlgebraOperation extends MetaProcessGridAnalyst {
	private final static String OUTPUT_DATA = "AlgebraOperationResult";


	private ParameterComboBox comboBoxPixelFormat;
	private ParameterCheckBox checkBoxCompress;
	private ParameterCheckBox checkBoxIgnoreNoValueCell;
	private ParameterTextArea textAreaExpression;
	private ParameterButton buttonExpression;
	private ParameterSaveDataset resultDataset;
	private ParameterButton buttonImport;
	private ParameterButton buttonExport;

	private ParameterDataNode parameterDataNodeSingle = new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.SINGLE), PixelFormat.SINGLE);
	private ParameterDataNode parameterDataNodeDouble = new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.DOUBLE), PixelFormat.DOUBLE);
	private ParameterDataNode parameterDataNodeBit8 = new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.BIT8), PixelFormat.BIT8);
	private ParameterDataNode parameterDataNodeBit16 = new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.BIT16), PixelFormat.BIT16);
	private ParameterDataNode parameterDataNodeBit32 = new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.BIT32), PixelFormat.BIT32);
	private ParameterDataNode parameterDataNodeBit64 = new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.BIT64), PixelFormat.BIT64);
	private ParameterDataNode parameterDataNodeUbit1 = new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.UBIT1), PixelFormat.UBIT1);
	private ParameterDataNode parameterDataNodeUbit4 = new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.UBIT4), PixelFormat.UBIT4);
	private ParameterDataNode parameterDataNodeUbit8 = new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.UBIT8), PixelFormat.UBIT8);
	private ParameterDataNode parameterDataNodeUbit16 = new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.UBIT16), PixelFormat.UBIT16);
	private ParameterDataNode parameterDataNodeUbit32 = new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.UBIT32), PixelFormat.UBIT32);

	public MetaProcessAlgebraOperation() {
		initParameters();
		initParameterConstraint();
		initParametersState();
		registerListener();
	}

	private void initParameters() {
		initEnvironment();
		this.comboBoxPixelFormat = new ParameterComboBox(CommonProperties.getString("String_PixelFormat"));
		this.checkBoxCompress = new ParameterCheckBox(ControlsProperties.getString("String_DatasetCompress"));
		this.checkBoxIgnoreNoValueCell = new ParameterCheckBox(ControlsProperties.getString("String_IgnoreNoValueRasterCell"));
		this.textAreaExpression = new ParameterTextArea(ProcessProperties.getString("String_AlgebraOperationExpression"));
		this.buttonExpression = new ParameterButton(ProcessProperties.getString("String_SetAlgebraOperationExpression"));
		this.buttonImport = new ParameterButton(ControlsProperties.getString("string_button_import"));
		this.buttonExport = new ParameterButton(ControlsProperties.getString("String_Button_Export"));

		ParameterCombine parameterCombine = new ParameterCombine(ParameterCombine.HORIZONTAL);
		parameterCombine.addParameters(this.buttonExpression, this.buttonImport, this.buttonExport);
		parameterCombine.setWeightIndex(3);

		ParameterCombine setting = new ParameterCombine();
		setting.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
		setting.addParameters(this.comboBoxPixelFormat, this.checkBoxCompress, this.checkBoxIgnoreNoValueCell, this.textAreaExpression, parameterCombine);
		this.resultDataset = new ParameterSaveDataset();
		ParameterCombine resultData = new ParameterCombine();
		resultData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		resultData.addParameters(resultDataset);

		this.parameters.setParameters(setting, resultData);
		this.parameters.addOutputParameters(OUTPUT_DATA, ProcessOutputResultProperties.getString("String_AlgebraOperationResult"), DatasetTypes.GRID, resultData);
	}

	private void initEnvironment() {
		parameterGridAnalystSetting.setCellSizeEnable(false);
	}

	private void initParameterConstraint() {
		DatasourceConstraint.getInstance().constrained(resultDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
	}

	private void initParametersState() {
		this.checkBoxIgnoreNoValueCell.setSelectedItem(true);
		this.textAreaExpression.setLineWrap(true);
		this.textAreaExpression.setWrapStyleWord(true);
		this.resultDataset.setDefaultDatasetName("result_AlgebraOperation");
		this.comboBoxPixelFormat.setItems(parameterDataNodeSingle, parameterDataNodeDouble, parameterDataNodeBit8,
				parameterDataNodeBit16, parameterDataNodeBit32, parameterDataNodeBit64, parameterDataNodeUbit1,
				parameterDataNodeUbit4, parameterDataNodeUbit8, parameterDataNodeUbit16, parameterDataNodeUbit32);
		this.comboBoxPixelFormat.setSelectedItem(parameterDataNodeUbit32);
	}

	private void registerListener() {
		this.buttonExpression.setActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editExpression();
			}
		});
		this.buttonImport.setActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				importXML();
			}
		});
		this.buttonExport.setActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exportXML();
			}
		});
//		this.buttonExpression.addPropertyListener(new PropertyChangeListener() {
//			@Override
//			public void propertyChange(PropertyChangeEvent evt) {
//				if (evt.getNewValue() == null) {
////					comboBoxPixelFormat.removeAllItems();
////					ParameterDataNode parameterDataNode = new ParameterDataNode(PixelFormatUtilities.toString(buttonExpression.getPixelFormat()), buttonExpression.getPixelFormat());
////					comboBoxPixelFormat.addItem(parameterDataNode);
////					comboBoxPixelFormat.setSelectedItem(parameterDataNode);
////					checkBoxCompress.setSelectedItem(buttonExpression.isZip());
////					checkBoxIgnoreNoValueCell.setSelectedItem(buttonExpression.isIgnoreNoValue());
////					textAreaExpression.setSelectedItem(buttonExpression.getExpression());
//				}
//			}
//		});
	}

	private void editExpression() {
		RasterAlgebraOperationDialog rasterAlgebraOperationDialog = new RasterAlgebraOperationDialog(expressionConvert());
		if (rasterAlgebraOperationDialog.showDialog() == DialogResult.OK) {
			this.textAreaExpression.setSelectedItem(rasterAlgebraOperationDialog.getExpression());
			if (rasterAlgebraOperationDialog.getPixelFormat() != null) {
				this.comboBoxPixelFormat.setSelectedItem(rasterAlgebraOperationDialog.getPixelFormat());
			}
		}
	}

	private void exportXML() {
		String moduleName = "ExportRasterAlgebraExpression";
		if (!SmFileChoose.isModuleExist(moduleName)) {
			String fileFilters = SmFileChoose.createFileFilter(ControlsProperties.getString("String_RasterAlgebraExpression"), "xml");
			SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
					ControlsProperties.getString("String_SaveAsFile"), moduleName, "SaveOne");
		}
		SmFileChoose smFileChoose = new SmFileChoose(moduleName);
		smFileChoose.setSelectedFile(new File("GridMathAnalystInfo.xml"));
		int state = smFileChoose.showDefaultDialog();
		String filePath = "";
		if (state == JFileChooser.APPROVE_OPTION) {
			filePath = smFileChoose.getFilePath();
			File oleFile = new File(filePath);
			filePath = filePath.substring(0, filePath.lastIndexOf(".")) + ".xml";
			File NewFile = new File(filePath);
			oleFile.renameTo(NewFile);
			if (oleFile.isFile() && oleFile.exists()) {
				oleFile.delete();
			}
			if (MathAnalyst.toXMLFile(filePath, expressionConvert(), null, (PixelFormat) this.comboBoxPixelFormat.getSelectedData()
					, Boolean.valueOf(this.checkBoxCompress.getSelectedItem().toString()), Boolean.valueOf(this.checkBoxIgnoreNoValueCell.getSelectedItem().toString()))) {
				Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_RasterAlgebraExpressionExportSuccess") + filePath);
			} else {
				Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_RasterAlgebraExpressionExportFailed"));
			}
		}
	}

	private void importXML() {
		String moduleName = "InputRasterAlgebraExpression";
		if (!SmFileChoose.isModuleExist(moduleName)) {
			String fileFilters = SmFileChoose.createFileFilter(ControlsProperties.getString("String_RasterAlgebraExpression"), "xml");
			SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
					ControlsProperties.getString("String_OpenRasterAlgebraExpressionFile"), moduleName, "OpenMany");
		}
		SmFileChoose smFileChoose = new SmFileChoose(moduleName);
		int state = smFileChoose.showDefaultDialog();
		String filePath = "";
		if (state == JFileChooser.APPROVE_OPTION) {
			filePath = smFileChoose.getFilePath();
			RasterAlgebraExpressionXml rasterAlgebraExpressionXml = new RasterAlgebraExpressionXml();
			rasterAlgebraExpressionXml.parserXml(filePath);
			if (rasterAlgebraExpressionXml.isImportResult()) {
				this.textAreaExpression.setSelectedItem(rasterAlgebraExpressionXml.getExpression());
				ArrayList<ParameterDataNode> parameterDataNodes = this.comboBoxPixelFormat.getItems();
				for (int i = 0; i < parameterDataNodes.size(); i++) {
					if (parameterDataNodes.get(i).getData().equals(rasterAlgebraExpressionXml.getPixelFormat())) {
						this.comboBoxPixelFormat.setSelectedItem(parameterDataNodes.get(i));
					}
				}
				this.checkBoxCompress.setSelectedItem(rasterAlgebraExpressionXml.isZip());
				this.checkBoxIgnoreNoValueCell.setSelectedItem(rasterAlgebraExpressionXml.isIgnoreNoValue());
				Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_RasterAlgebraExpressionInputSuccess") + filePath);
			} else {
				Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_RasterAlgebraExpressionInputFailed"));
			}
		}
	}

	private String expressionConvert() {
		String strExpression = "";
		if (this.textAreaExpression.getSelectedItem() != null) {
			strExpression = this.textAreaExpression.getSelectedItem().toString();
		}
		return strExpression;
	}

	@Override
	public boolean childExecute() {
		boolean isSuccessful = false;
		try {
			String datasetName = resultDataset.getDatasetName();
			datasetName = resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(datasetName);

			MathAnalyst.addSteppedListener(steppedListener);
			DatasetGrid result = MathAnalyst.execute(this.textAreaExpression.getSelectedItem().toString(), null, (PixelFormat) this.comboBoxPixelFormat.getSelectedData(),
					Boolean.valueOf(this.checkBoxCompress.getSelectedItem().toString()), Boolean.valueOf(this.checkBoxIgnoreNoValueCell.getSelectedItem().toString()), resultDataset.getResultDatasource(), datasetName);
			this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(result);
			isSuccessful = result != null;
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e.getMessage());
			e.printStackTrace();
		} finally {
			MathAnalyst.removeSteppedListener(steppedListener);
		}
		return isSuccessful;
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_RasterAlgebraOperation");
	}

	@Override
	public String getKey() {
		return MetaKeys.ALGEBRA_OPERATION;
	}
}
