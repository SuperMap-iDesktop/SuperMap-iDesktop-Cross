package com.supermap.desktop.progress.callable;

import java.awt.Component;
import java.text.MessageFormat;
import java.util.concurrent.CancellationException;

import javax.swing.JOptionPane;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetImage;
import com.supermap.data.SteppedEvent;
import com.supermap.data.SteppedListener;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit.DatasetWrap;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;

public class CreateImagePyramidCallable extends UpdateProgressCallable {

	private boolean isCancel = false;
	private int totalDatasetCount = 0;
	private int completedCount = 0;
	private Dataset[] datasets = null;

	private SteppedListener steppedListener = new SteppedListener() {

		@Override
		public void stepped(SteppedEvent arg0) {
			int totalPercent = (arg0.getPercent() + 100 * completedCount) / totalDatasetCount;
			try {
				updateProgressTotal(arg0.getPercent(), totalPercent, String.valueOf(arg0.getRemainTime()), arg0.getMessage());
			} catch (CancellationException e) {
				isCancel = true;
				arg0.setCancel(true);
			}
		}
	};

	public CreateImagePyramidCallable(Dataset[] datasets) {
		this.datasets = datasets;
		this.totalDatasetCount = this.datasets.length;
		this.completedCount = 0;
	}

	@Override
	public Boolean call() throws Exception {
		if (this.datasets == null || this.datasets.length == 0) {
			return false;
		}

		boolean isSucceeded = false;
		boolean isBuild = true;

		for (Dataset dataset : this.datasets) {
			boolean isDatasetGridOrImage = !(dataset instanceof DatasetGrid) && !(dataset instanceof DatasetImage);
			if (isCancel || isDatasetGridOrImage) {
				this.completedCount++;
				continue;
			}

			// 创建和删除金字塔前需要关闭数据集
			if (DatasetWrap.isDatasetOpened(dataset)) {
				String message = MessageFormat.format(CoreProperties.getString("String_InfoDatasetOpened"), dataset.getName());

				// 提示关闭数据集
				int result = showConfirmDialog(message);
				if (result == JOptionPane.YES_OPTION) {
					isBuild = true;
					dataset.close();
				} else {
					isBuild = false;
					continue;
				}
			}

			if (isBuild) {
				try {
					dataset.addSteppedListener(this.steppedListener);

					if (dataset instanceof DatasetImage) {
						isSucceeded = ((DatasetImage) dataset).buildPyramid();
						dataset.close();
					} else if (dataset instanceof DatasetGrid) {
						isSucceeded = ((DatasetGrid) dataset).buildPyramid();
						dataset.close();
					}

					if (isSucceeded) {
						String message = MessageFormat.format(CoreProperties.getString("String_CreateImagePyramid_Success"), dataset.getName());
						Application.getActiveApplication().getOutput().output(message);
					} else {
						String message = MessageFormat.format(CoreProperties.getString("String_CreateImagePyramid_Failed"), dataset.getName());
						Application.getActiveApplication().getOutput().output(message);
					}
				} finally {
					dataset.removeSteppedListener(this.steppedListener);
				}
			}

			this.completedCount++;
		}
		return true;
	}

	/**
	 * @param message
	 * @return 0 -- 是；1 -- 否；-1 -- 直接关闭。
	 */
	public static int showConfirmDialog(String message) {
		int result = 0;
		try {
			Component parent = (Component) Application.getActiveApplication().getMainFrame();
			result = JOptionPane.showConfirmDialog((java.awt.Component) parent, message, CoreProperties.getString("String_MessageBox_Title"),
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}
}
