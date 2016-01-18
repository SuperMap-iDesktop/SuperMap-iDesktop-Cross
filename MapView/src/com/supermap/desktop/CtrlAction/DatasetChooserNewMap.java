package com.supermap.desktop.CtrlAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.utilties.MapViewUtilties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.DatasetChooser;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTableModel;
import com.supermap.ui.Action;

public class DatasetChooserNewMap extends DatasetChooser {

	private transient IFormMap formMap;

	public DatasetChooserNewMap(JFrame owner, IFormMap formMap, String[] datasetType) {
		super(owner, true, datasetType);
		this.formMap = formMap;
		setTitle(CoreProperties.getString("String_FormDatasetBrowse_FormText"));
		getDataset();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private void getDataset() {
		buttonOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 添加数据集到新地图窗口中
				addDatasetsToNewMap(formMap);
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (2 == e.getClickCount()) {
					addDatasetsToNewMap(formMap);
				}
			}
		});
		table.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					addDatasetsToNewMap(formMap);
				}
			}
		});
	}

	/**
	 * 将数据集添加到新地图窗口
	 */
	private void addDatasetsToNewMap(IFormMap formMap) {
		try {
			ArrayList<Dataset> datasetsToMap = new ArrayList<>(); // 将要添加到地图上的数据集
			MutiTableModel model = (MutiTableModel) table.getModel();
			int[] selectCount = table.getSelectedRows();
			for (int i = 0; i < selectCount.length; i++) {
				Vector<Object> tempVector = model.getTagValue(selectCount[i]);
				String datasetName = tempVector.get(COLUMN_INDEX_DATASET).toString();
				String datasourceName = tempVector.get(COLUMN_INDEX_CURRENT_DATASOURCE).toString();
				Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasourceName);
				Dataset dataset = CommonToolkit.DatasetWrap.getDatasetFromDatasource(datasetName, datasource);
				datasetsToMap.add(dataset);
			}

			MapViewUtilties.addDatasetsToMap(this.formMap.getMapControl().getMap(), datasetsToMap.toArray(new Dataset[datasetsToMap.size()]), true);
			// 新建的地图窗口，修改默认的Action为漫游
			formMap.getMapControl().setAction(Action.PAN);
			dispose();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

}
