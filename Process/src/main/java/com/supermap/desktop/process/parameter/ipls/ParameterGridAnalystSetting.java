package com.supermap.desktop.process.parameter.ipls;

import com.supermap.analyst.spatialanalyst.BoundsType;
import com.supermap.analyst.spatialanalyst.CellSizeType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetType;
import com.supermap.data.Rectangle2D;
import com.supermap.desktop.Application;
import com.supermap.desktop.GridAnalystSettingInstance;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.utilities.DatasetUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class ParameterGridAnalystSetting extends ParameterCombine {
	private ParameterComboBox parameterComboBoxResultBounds = new ParameterComboBox();
	private ParameterSwitch parameterSwitchResultBounds = new ParameterSwitch();
	private ParameterSwitch parameterSwitchResultBoundsDataset = new ParameterSwitch();
	private ParameterDatasource parameterDatasourceResultBounds = new ParameterDatasourceConstrained();
	private ParameterSingleDataset parameterDatasetResultBounds = new ParameterSingleDataset();
	private ParameterBounds parameterBounds = new ParameterBounds();

	private ParameterDatasource parameterDatasource = new ParameterDatasourceConstrained();
	private ParameterSingleDataset parameterDataset = new ParameterSingleDataset(DatasetType.REGION);

	private ParameterComboBox parameterComboBoxCellSize = new ParameterComboBox();
	private ParameterSwitch parameterSwitchCellSize = new ParameterSwitch();
	private ParameterSwitch parameterSwitchCellSizeDataset = new ParameterSwitch();
	private ParameterNumber parameterCellSize = new ParameterNumber();
	private ParameterDatasource parameterDatasourceCellSize = new ParameterDatasourceConstrained();
	private ParameterSingleDataset parameterDatasetCellSize = new ParameterSingleDataset(DatasetType.GRID);


	private GridAnalystSettingInstance gridAnalystSettingInstance;
	private boolean isSelectedItem = false;

	public ParameterGridAnalystSetting() {
		gridAnalystSettingInstance = GridAnalystSettingInstance.getInstance();
		initComponents();
		initLayouts();
		initComponentState();
		initListener();
		initResources();
	}

	private void initComponents() {
		ArrayList<ParameterDataNode> resultBoundsNodes = new ArrayList<>();
		resultBoundsNodes.add(new ParameterDataNode(ProcessProperties.getString("String_Setting_BoundsIntersection"), BoundsType.INTERSECTION));
		resultBoundsNodes.add(new ParameterDataNode(ProcessProperties.getString("String_Setting_BoundsUnion"), BoundsType.UNION));
		resultBoundsNodes.add(new ParameterDataNode(ProcessProperties.getString("String_Setting_BoundsCustom"), BoundsType.CUSTOM));
		resultBoundsNodes.add(new ParameterDataNode(ProcessProperties.getString("String_Setting_BoundsDataset"), ""));
		parameterComboBoxResultBounds.setItems(resultBoundsNodes.toArray(new ParameterDataNode[resultBoundsNodes.size()]));

		ArrayList<ParameterDataNode> cellSizeNodes = new ArrayList<>();
		cellSizeNodes.add(new ParameterDataNode(ProcessProperties.getString("String_Setting_CellSizeMax"), CellSizeType.MAX));
		cellSizeNodes.add(new ParameterDataNode(ProcessProperties.getString("String_Setting_CellSizeMin"), CellSizeType.MIN));
		cellSizeNodes.add(new ParameterDataNode(ProcessProperties.getString("String_Setting_CellSizeCustom"), CellSizeType.CUSTOM));
		cellSizeNodes.add(new ParameterDataNode(ProcessProperties.getString("String_Setting_CellSizeDataset"), ""));
		parameterComboBoxCellSize.setItems(cellSizeNodes.toArray(new ParameterDataNode[cellSizeNodes.size()]));

		parameterDataset.setShowNullValue(true);
		parameterCellSize.setMinValue(0);
		parameterCellSize.setIsIncludeMin(false);
	}

	private void initLayouts() {
		ParameterCombine parameterCombineResultBounds = new ParameterCombine();
		parameterCombineResultBounds.setDescribe(ProcessProperties.getString("String_Setting_Bounds"));
		parameterSwitchResultBounds.add("bounds", parameterBounds);
		ParameterCombine parameterCombineResultBoundsDataset = new ParameterCombine();
		parameterCombineResultBoundsDataset.addParameters(parameterDatasourceResultBounds, parameterDatasetResultBounds);
		parameterSwitchResultBoundsDataset.add("dataset", parameterCombineResultBoundsDataset);
		parameterCombineResultBounds.addParameters(parameterComboBoxResultBounds, parameterSwitchResultBoundsDataset, parameterSwitchResultBounds);

		ParameterCombine parameterCombineClipSize = new ParameterCombine();
		parameterCombineClipSize.setDescribe(ProcessProperties.getString("String_Setting_ClipBounds"));
		parameterCombineClipSize.addParameters(parameterDatasource, parameterDataset);

		ParameterCombine parameterCombineCellSize = new ParameterCombine();
		parameterCombineCellSize.setDescribe(ProcessProperties.getString("String_Setting_DefaultCellSize"));
		parameterSwitchCellSize.add("CustomCellSize", parameterCellSize);
		ParameterCombine parameterCombineCellSizeDataset = new ParameterCombine();
		parameterCombineCellSizeDataset.addParameters(parameterDatasourceCellSize, parameterDatasetCellSize);
		parameterSwitchCellSizeDataset.add("dataset", parameterCombineCellSizeDataset);
		parameterCombineCellSize.addParameters(parameterComboBoxCellSize, parameterSwitchCellSizeDataset, parameterSwitchCellSize);

		this.addParameters(parameterCombineResultBounds, parameterCombineClipSize, parameterCombineCellSize);
	}

	private void initListener() {
		parameterComboBoxResultBounds.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectedItem && evt.getPropertyName().equals(ParameterComboBox.comboBoxValue)) {
					isSelectedItem = true;
					try {
						Object selectedData = parameterComboBoxResultBounds.getSelectedData();
						if (selectedData == BoundsType.INTERSECTION || selectedData == BoundsType.UNION) {
							parameterSwitchResultBounds.switchParameter(((IParameter) null));
							parameterSwitchResultBoundsDataset.switchParameter(((IParameter) null));
							gridAnalystSettingInstance.setResultBounds(selectedData);
						} else if (selectedData == BoundsType.CUSTOM) {
							parameterSwitchResultBoundsDataset.switchParameter(((IParameter) null));
							parameterSwitchResultBounds.switchParameter("bounds");
							parameterBounds.setEnabled(true);
							gridAnalystSettingInstance.setResultBounds(parameterBounds.getSelectedItem());
						} else if (selectedData == "") {
							parameterSwitchResultBoundsDataset.switchParameter("dataset");
							parameterSwitchResultBounds.switchParameter("bounds");
							parameterBounds.setEnabled(false);
							Dataset selectedItem = parameterDatasetResultBounds.getSelectedItem();
							gridAnalystSettingInstance.setResultBounds(selectedItem);
							parameterBounds.setSelectedItem(selectedItem);
						}
					} catch (Exception e) {
						Application.getActiveApplication().getOutput().output(e);
					} finally {
						isSelectedItem = false;
					}
				}
			}
		});
		parameterComboBoxCellSize.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectedItem && evt.getPropertyName().equals(ParameterComboBox.comboBoxValue)) {
					isSelectedItem = true;
					try {
						Object selectedData = parameterComboBoxCellSize.getSelectedData();
						if (selectedData == CellSizeType.MAX || selectedData == CellSizeType.MIN) {
							parameterSwitchCellSize.switchParameter(((IParameter) null));
							parameterSwitchCellSizeDataset.switchParameter(((IParameter) null));
							gridAnalystSettingInstance.setResultBounds(selectedData);
						} else if (selectedData == CellSizeType.CUSTOM) {
							parameterSwitchCellSize.switchParameter("CustomCellSize");
							parameterCellSize.setEnabled(true);
							parameterSwitchCellSizeDataset.switchParameter(((IParameter) null));
							gridAnalystSettingInstance.setResultBounds(Double.valueOf(parameterCellSize.getSelectedItem()));
						} else if (selectedData == "") {
							parameterSwitchCellSizeDataset.switchParameter("dataset");
							parameterSwitchCellSize.switchParameter("CustomCellSize");
							parameterCellSize.setEnabled(false);
							DatasetGrid dataset = ((DatasetGrid) parameterDatasetCellSize.getSelectedItem());
							if (dataset != null) {
								double cellSize = dataset.getBounds().getWidth() / dataset.getWidth();
								gridAnalystSettingInstance.setResultBounds(cellSize);
								parameterCellSize.setSelectedItem(cellSize);
							}
						}
					} catch (Exception e) {
						Application.getActiveApplication().getOutput().output(e);
					} finally {
						isSelectedItem = false;
					}
				}
			}
		});

		parameterDataset.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectedItem && evt.getPropertyName().equals(ParameterSingleDataset.PROPERTY_VALE)) {
					gridAnalystSettingInstance.setClipBounds(parameterDataset.getSelectedItem());
				}
			}
		});

		parameterDatasetResultBounds.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectedItem && evt.getPropertyName().equals(ParameterSingleDataset.PROPERTY_VALE) && parameterSwitchCellSizeDataset.getCurrentParameter() != null) {
					isSelectedItem = true;
					gridAnalystSettingInstance.setResultBounds(parameterDatasetResultBounds.getSelectedItem());
					parameterBounds.setSelectedItem(parameterDatasetResultBounds.getSelectedItem());
					isSelectedItem = false;
				}
			}
		});

		parameterCellSize.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectedItem && evt.getPropertyName().equals(ParameterTextField.PROPERTY_VALE)) {
					isSelectedItem = true;
					gridAnalystSettingInstance.setCellSize(Double.valueOf(parameterCellSize.getSelectedItem()));
					isSelectedItem = false;
				}
			}
		});
		gridAnalystSettingInstance.addPropertyChangedListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (isSelectedItem) {
					return;
				}
				String propertyName = evt.getPropertyName();
				if (propertyName.equals(GridAnalystSettingInstance.RESULT_BOUNDS)) {
					freshResultBoundsView();
				} else if (propertyName.equals(GridAnalystSettingInstance.CELL_SIZE)) {
					freshCellSizeView();
				} else if (propertyName.equals(GridAnalystSettingInstance.CLIP_BOUNDS)) {
					freshClipBounds();
				}
			}
		});

		parameterDatasetCellSize.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectedItem && evt.getPropertyName().equals(ParameterSingleDataset.DATASET_FIELD_NAME) && parameterSwitchCellSizeDataset.getCurrentParameter() != null) {
					isSelectedItem = true;
					DatasetGrid dataset = ((DatasetGrid) parameterDatasetCellSize.getSelectedItem());
					double cellSize = dataset.getBounds().getWidth() / dataset.getWidth();
					gridAnalystSettingInstance.setCellSize(cellSize);
					parameterCellSize.setSelectedItem(cellSize);
					isSelectedItem = false;
				}
			}
		});

		EqualDatasourceConstraint equalDatasourceConstraintResultBounds = new EqualDatasourceConstraint();
		equalDatasourceConstraintResultBounds.constrained(parameterDatasourceResultBounds, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraintResultBounds.constrained(parameterDatasetResultBounds, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint equalDatasourceConstraintClipBounds = new EqualDatasourceConstraint();
		equalDatasourceConstraintClipBounds.constrained(parameterDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraintClipBounds.constrained(parameterDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint equalDatasourceConstraintCellSize = new EqualDatasourceConstraint();
		equalDatasourceConstraintCellSize.constrained(parameterDatasourceCellSize, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraintCellSize.constrained(parameterDatasetCellSize, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
	}

	private void initComponentState() {
		isSelectedItem = true;
		try {
			parameterCellSize.setSelectedItem("0.5");
			Dataset defaultDataset = DatasetUtilities.getDefaultDataset();
			if (defaultDataset != null) {
				parameterDatasourceResultBounds.setSelectedItem(defaultDataset.getDatasource());
				parameterDatasetResultBounds.setDatasource(defaultDataset.getDatasource());
				parameterDatasource.setSelectedItem(defaultDataset.getDatasource());
				parameterDataset.setDatasource(defaultDataset.getDatasource());
				parameterDatasourceCellSize.setSelectedItem(defaultDataset.getDatasource());
				parameterDatasetCellSize.setDatasource(defaultDataset.getDatasource());
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			isSelectedItem = false;
		}
		freshResultBoundsView();
		freshClipBounds();
		freshCellSizeView();
	}

	private void freshResultBoundsView() {
		isSelectedItem = true;
		try {
			Object resultBounds = gridAnalystSettingInstance.getResultBounds();
			if (resultBounds == BoundsType.INTERSECTION || resultBounds == BoundsType.UNION) {
				parameterComboBoxResultBounds.setSelectedItem(resultBounds);
				parameterSwitchResultBounds.switchParameter(((IParameter) null));
				parameterSwitchResultBoundsDataset.switchParameter(((IParameter) null));
			} else if (resultBounds instanceof Rectangle2D) {
				parameterComboBoxResultBounds.setSelectedItem(BoundsType.CUSTOM);
				parameterBounds.setEnabled(true);
				parameterSwitchResultBounds.switchParameter("bounds");
				parameterSwitchResultBoundsDataset.switchParameter(((IParameter) null));
				parameterBounds.setSelectedItem(resultBounds);
			} else if (resultBounds instanceof Dataset) {
				parameterComboBoxResultBounds.setSelectedItem("");
				parameterSwitchResultBounds.switchParameter("bounds");
				parameterBounds.setEnabled(false);
				parameterSwitchResultBoundsDataset.switchParameter("dataset");
				parameterDatasourceResultBounds.setSelectedItem(((Dataset) resultBounds).getDatasource());
				parameterDatasourceResultBounds.setSelectedItem(resultBounds);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			isSelectedItem = false;
		}
	}

	private void freshClipBounds() {
		isSelectedItem = true;
		try {
			Object clipBounds = gridAnalystSettingInstance.getClipBounds();
			if (clipBounds == null) {
				parameterDataset.setSelectedItem(null);
			} else if (clipBounds instanceof Dataset) {
				parameterDatasource.setSelectedItem(((Dataset) clipBounds).getDatasource());
				parameterDataset.setSelectedItem(clipBounds);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			isSelectedItem = false;
		}
	}

	private void freshCellSizeView() {
		isSelectedItem = true;
		try {
			Object cellSize = gridAnalystSettingInstance.getCellSize();
			if (cellSize == CellSizeType.MAX || cellSize == CellSizeType.MIN) {
				parameterComboBoxCellSize.setSelectedItem(cellSize);
				parameterSwitchCellSize.switchParameter(((IParameter) null));
				parameterSwitchCellSizeDataset.switchParameter(((IParameter) null));
			} else if (cellSize instanceof Double) {
				parameterComboBoxCellSize.setSelectedItem(CellSizeType.CUSTOM);
				parameterCellSize.setSelectedItem(String.valueOf(cellSize));
				parameterCellSize.setEnabled(true);
				parameterSwitchCellSize.switchParameter("CustomCellSize");
				parameterSwitchCellSizeDataset.switchParameter((IParameter) null);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			isSelectedItem = false;
		}
	}

	private void initResources() {
		parameterComboBoxResultBounds.setDescribe(ProcessProperties.getString("String_Setting_BoundsType"));
		parameterComboBoxCellSize.setDescribe(ProcessProperties.getString("String_Setting_BoundsType"));
		parameterCellSize.setDescribe(ProcessProperties.getString("String_Resolution"));
	}

	public void run() {
		isSelectedItem = true;
		gridAnalystSettingInstance.run();
		isSelectedItem = false;
	}

	public GridAnalystSettingInstance getResult() {
		return gridAnalystSettingInstance;
	}
}
