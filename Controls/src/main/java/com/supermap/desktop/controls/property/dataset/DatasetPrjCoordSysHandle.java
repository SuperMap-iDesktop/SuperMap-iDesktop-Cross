package com.supermap.desktop.controls.property.dataset;

import com.supermap.data.CoordSysTransMethod;
import com.supermap.data.CoordSysTransParameter;
import com.supermap.data.CoordSysTranslator;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.SteppedEvent;
import com.supermap.data.SteppedListener;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.property.PrjCoordSysHandle;
import com.supermap.desktop.controls.utilities.WorkspaceTreeManagerUIUtilities;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.progress.FormProgress;

import java.text.MessageFormat;
import java.util.concurrent.CancellationException;

public class DatasetPrjCoordSysHandle extends PrjCoordSysHandle {
	private Dataset dataset;

	public DatasetPrjCoordSysHandle(Dataset dataset) {
		super(dataset.getPrjCoordSys());
		this.dataset = dataset;
	}

	@Override
	public void change(PrjCoordSys targetPrj) {
		this.prj = targetPrj;
		this.dataset.setPrjCoordSys(this.prj);
	}

	@Override
	public PrjCoordSys getPrj() {
		return dataset.getPrjCoordSys();
	}

	@Override
	public void convert(CoordSysTransMethod method, CoordSysTransParameter parameter, PrjCoordSys targetPrj) {
		FormProgress formProgress = new FormProgress();
		formProgress.doWork(new ConvertProgressCallable(this.dataset, method, parameter, targetPrj));
	}

	private class ConvertProgressCallable extends UpdateProgressCallable {

		private CoordSysTransMethod method;
		private CoordSysTransParameter parameter;
		private PrjCoordSys targetPrj;
		private Dataset dataset;

		private SteppedListener steppedListener = new SteppedListener() {

			@Override
			public void stepped(SteppedEvent arg0) {
				setSteppedListenerStepped(arg0);
			}
		};

		private void setSteppedListenerStepped(SteppedEvent arg0) {
			try {
				updateProgress(arg0.getPercent(), String.valueOf(arg0.getRemainTime()), arg0.getMessage());
			} catch (CancellationException e) {
				if (dataset instanceof DatasetVector) {
					int result = UICommonToolkit.showConfirmDialog(ControlsProperties.getString("String_Warning_DatasetPrjCoordSysTranslatorCancel"));

					if (result == 0) {
						arg0.setCancel(true);
					} else {
						update.setCancel(false);
						arg0.setCancel(false);
					}
				} else {
					arg0.setCancel(true);
				}
			}
		}

		public ConvertProgressCallable(Dataset dataset, CoordSysTransMethod method, CoordSysTransParameter parameter, PrjCoordSys targetPrj) {
			this.dataset = dataset;
			this.method = method;
			this.parameter = parameter;
			this.targetPrj = targetPrj;
			if (this.dataset != null) {
				this.dataset.addSteppedListener(this.steppedListener);
			}
		}

		@Override
		public Boolean call() throws Exception {
			Boolean result = true;

			try {
				if (this.dataset == null) {
					return false;
				}

				Application
						.getActiveApplication()
						.getOutput()
						.output(MessageFormat.format(ControlsProperties.getString("String_BeginTrans_Dataset"), this.dataset.getDatasource().getAlias(),
								this.dataset.getName()));

				if (this.dataset instanceof DatasetVector) {
					result = CoordSysTranslator.convert(this.dataset, this.targetPrj, this.parameter, this.method);
					if (result) {
						Application
								.getActiveApplication()
								.getOutput()
								.output(MessageFormat.format(ControlsProperties.getString("String_CoordSysTrans_VectorSuccess"), this.dataset.getDatasource()
										.getAlias(), this.dataset.getName()));
					} else {
						Application
								.getActiveApplication()
								.getOutput()
								.output(MessageFormat.format(ControlsProperties.getString("String_CoordSysTrans_Failed"), this.dataset.getDatasource()
										.getAlias(), this.dataset.getName()));
					}
				} else {
					String targetDatasetName = this.dataset.getDatasource().getDatasets().getAvailableDatasetName(this.dataset.getName());
					Dataset targetDataset = CoordSysTranslator.convert(this.dataset, this.targetPrj, this.dataset.getDatasource(), targetDatasetName,
							this.parameter, this.method);
					result = targetDataset != null;
					if (result) {
						Application
								.getActiveApplication()
								.getOutput()
								.output(MessageFormat.format(ControlsProperties.getString("String_CoordSysTrans_RasterSuccess"), this.dataset.getDatasource()
										.getAlias(), this.dataset.getName(), this.dataset.getDatasource().getAlias(), targetDatasetName));
					} else {
						Application
								.getActiveApplication()
								.getOutput()
								.output(MessageFormat.format(ControlsProperties.getString("String_CoordSysTrans_Failed"), this.dataset.getDatasource()
										.getAlias(), this.dataset.getName()));
					}

					// 这种转换方式主要针对非矢量数据，转换之后会生成新的数据集，但是树的显示状态很诡异，这里对目标数据源的节点进行一次刷新
					WorkspaceTreeManagerUIUtilities.refreshNode(this.dataset.getDatasource());
				}
			} catch (Exception e) {
				result = false;
				Application.getActiveApplication().getOutput().output(e);
			} finally {
				if (this.dataset != null) {
					DatasetPrjCoordSysHandle.this.prj = this.dataset.getPrjCoordSys();
					this.dataset.removeSteppedListener(this.steppedListener);
				}

				if (this.parameter != null) {
					this.parameter.dispose();
				}
			}
			return result;
		}
	}
}
