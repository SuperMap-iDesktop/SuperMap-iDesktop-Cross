package com.supermap.desktop.CtrlAction.Dataset.createNewDataset;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.Dataset.AddToWindowMode;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.utilities.StringUtilities;

import java.text.MessageFormat;

/**
 * @author XiaJT
 * 新建数据集参数信息类
 * 增加栅格、影像数据集的参数信息模块-yuanR2017.8.15
 * GridImageExtraDatasetBean 作为补充
 */
public class NewDatasetBean {
	private Datasource datasource;
	private String datasetName;
	private DatasetType datasetType;
	private EncodeType encodeType;
	private Charset charset;
	private AddToWindowMode addToWindowMode;
	private GridImageExtraDatasetBean gridImageExtraDatasetBean;

	public NewDatasetBean() {
		Datasource[] activeDatasources = Application.getActiveApplication().getActiveDatasources();
		datasource = activeDatasources.length > 0 ? activeDatasources[0] : Application.getActiveApplication().getWorkspace().getDatasources().get(0);
		datasetName = "";
		datasetType = DatasetType.POINT;
		encodeType = EncodeType.NONE;
		charset = Charset.UTF8;
		addToWindowMode = AddToWindowMode.NONEWINDOW;
		gridImageExtraDatasetBean = null;
	}


	public Datasource getDatasource() {
		return datasource;
	}

	public void setDatasource(Datasource datasource) {
		this.datasource = datasource;
	}

	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	public DatasetType getDatasetType() {
		return datasetType;
	}

	public void setDatasetType(DatasetType datasetType) {
		this.datasetType = datasetType;
		if (encodeType != EncodeType.NONE && datasetType != DatasetType.LINE && datasetType != DatasetType.REGION
				&& datasetType != DatasetType.IMAGE && datasetType != DatasetType.GRID) {
			encodeType = EncodeType.NONE;
		}
	}

	public AddToWindowMode getAddToWindowMode() {
		return addToWindowMode;
	}

	public void setAddToWindowMode(AddToWindowMode addToWindowMode) {
		this.addToWindowMode = addToWindowMode;
	}

	public EncodeType getEncodeType() {
		return encodeType;
	}

	public void setEncodeType(EncodeType encodeType) {
		if (datasetType == DatasetType.LINE || datasetType == DatasetType.REGION || datasetType == DatasetType.IMAGE || datasetType == DatasetType.GRID) {
			this.encodeType = encodeType;
		} else {
			this.encodeType = EncodeType.NONE;
		}
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public GridImageExtraDatasetBean getGridImageExtraDatasetBean() {
		if (gridImageExtraDatasetBean == null) {
			gridImageExtraDatasetBean = new GridImageExtraDatasetBean();
		}
		return gridImageExtraDatasetBean;
	}


	public boolean createDataset() {
		boolean result = false;
		// 创建dataset时根据不同的数据类型进行创建
		if (!StringUtilities.isNullOrEmpty(datasetName)) {
			if (datasetType.equals(DatasetType.IMAGE)) {
				if (gridImageExtraDatasetBean != null) {
					DatasetImageInfo info = new DatasetImageInfo(
							datasetName,
							gridImageExtraDatasetBean.getWidth(),
							gridImageExtraDatasetBean.getHeight(),
							gridImageExtraDatasetBean.getPixelFormatImage(),
							encodeType,
							gridImageExtraDatasetBean.getBlockSizeOption(),
							gridImageExtraDatasetBean.getBandCount()
					);
					info.setBounds(gridImageExtraDatasetBean.getRectangle());
					DatasetImage datasetImage = datasource.getDatasets().create(info);
					try {
						datasetImage = datasource.getDatasets().create(info);
					} catch (Exception e) {
						Application.getActiveApplication().getOutput().output(MessageFormat.format(DataEditorProperties.getString("String_CreateNewDT_Failed"), datasetName, datasource.getAlias()));
					}

					if (datasetImage != null) {
						result = true;
						String information = MessageFormat.format(DataEditorProperties.getString("String_CreateNewDT_Success"), datasetName,
								datasource.getAlias());
						Application.getActiveApplication().getOutput().output(information);
					}
				}

			} else if (datasetType.equals(DatasetType.GRID)) {
				if (gridImageExtraDatasetBean != null) {
					DatasetGridInfo info = new DatasetGridInfo(
							datasetName,
							gridImageExtraDatasetBean.getWidth(),
							gridImageExtraDatasetBean.getHeight(),
							gridImageExtraDatasetBean.getPixelFormatGrid(),
							encodeType,
							gridImageExtraDatasetBean.getBlockSizeOption()
					);
					info.setBounds(gridImageExtraDatasetBean.getRectangle());
					info.setMaxValue(gridImageExtraDatasetBean.getMaxValue());
					info.setMinValue(gridImageExtraDatasetBean.getMinValue());
					info.setNoValue(gridImageExtraDatasetBean.getNoValue());
					DatasetGrid datasetGrid = datasource.getDatasets().create(info);
					try {
						datasetGrid = datasource.getDatasets().create(info);
					} catch (Exception e) {
						Application.getActiveApplication().getOutput().output(MessageFormat.format(DataEditorProperties.getString("String_CreateNewDT_Failed"), datasetName, datasource.getAlias()));
					}

					if (datasetGrid != null) {
						result = true;
						String information = MessageFormat.format(DataEditorProperties.getString("String_CreateNewDT_Success"), datasetName,
								datasource.getAlias());
						Application.getActiveApplication().getOutput().output(information);
					}
				}

			} else {
				DatasetVectorInfo info = new DatasetVectorInfo(datasetName, datasetType);
				info.setEncodeType(encodeType);
				Dataset dataset = null;
				try {
					dataset = datasource.getDatasets().create(info);
				} catch (Exception e) {
					Application.getActiveApplication().getOutput().output(MessageFormat.format(DataEditorProperties.getString("String_CreateNewDT_Failed"), datasetName, datasource.getAlias()));
				}

				if (dataset != null) {
					result = true;
					String information = MessageFormat.format(DataEditorProperties.getString("String_CreateNewDT_Success"), datasetName,
							datasource.getAlias());
					Application.getActiveApplication().getOutput().output(information);
				}
			}
		}
		return result;
	}
}
