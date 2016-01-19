package com.supermap.desktop.ui.controls;

import java.awt.FlowLayout;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.data.EngineType;
import com.supermap.desktop.CommonToolkit;

/**
 * @author xie 控件中用于显示数据集图标，可用于每一个有单元格的控件 如JComBox,JList等，使用时需要调用setRenderer()
 *         方法将渲染器设置为CommonListCellRenderer 不然不会添加图片效果，具体用法请参见DatasetComboBox类
 */
public class DataCell extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel imageLabel;
	private String dataName;
	private Object data;

	public DataCell() {
		// 公共类型
	}
	/**
	 * 根据数据源来创建
	 * @param datasource
	 */
	public void initDatasourceType(Datasource datasource) {
		setData(datasource);
		this.dataName = datasource.getAlias();
		String datasouceImagepath = CommonToolkit.DatasourceImageWrap.getImageIconPath(datasource.getEngineType());
		URL url = DataCell.class.getResource(datasouceImagepath);
		init(url, this.dataName);
	}
	/**
	 * 根据数据源引擎类型和名称创建
	 * @param engineType
	 * @param datasouceName
	 */
	public void initDatasourceType(EngineType engineType, String datasouceName) {
		setData(engineType);
		this.dataName = datasouceName;
		String datasourceImagepath = CommonToolkit.DatasourceImageWrap.getImageIconPath(engineType);
		URL url = DataCell.class.getResource(datasourceImagepath);
		init(url, this.dataName);
	}
	/**
	 * 根据数据集创建
	 * @param dataset
	 */
	public void initDatasetType(Dataset dataset) {
		setData(dataset);
		String datasetImagepath = CommonToolkit.DatasetImageWrap.getImageIconPath(dataset.getType());
		URL url = DataCell.class.getResource(datasetImagepath);
		this.dataName = dataset.getName();
		init(url, this.dataName);
	}
	/**
	 * 根据数据集类型和数据集名称创建
	 * @param datasetType
	 * @param datasetName
	 */
	public void initDatasetType(DatasetType datasetType, String datasetName) {
		setData(datasetType);
		this.dataName = datasetName;
		String datasetImagepath = CommonToolkit.DatasetImageWrap.getImageIconPath(datasetType);
		URL url = DataCell.class.getResource(datasetImagepath);
		init(url, this.dataName);
	}
	/**
	 * 直接根据图片路径和名称创建
	 * @param imagePath
	 * @param dataName
	 */
	public void initDataType(String imagePath, String dataName) {
		this.dataName = dataName;
		URL url = DataCell.class.getResource(imagePath);
		init(url, dataName);
	}
	/**
	 * 直接根据图片创建
	 * @param icon
	 * @param name
	 */
	public void initDataImage(ImageIcon icon, String name) {
		this.dataName = name;
		init(icon, name);
	}

	private void init(ImageIcon icon, String name) {
		this.dataName = name;
		this.imageLabel = new JLabel(dataName, icon, JLabel.LEADING);
		initComponents();
	}

	private void init(URL url, String dataName) {
		ImageIcon tempIcon = null;
		if (null != url) {
			tempIcon = new ImageIcon(url);
			this.imageLabel = new JLabel(dataName, tempIcon, JLabel.LEADING);
		} else {
			this.imageLabel = new JLabel(dataName);
		}
		initComponents();
	}

	private void initComponents() {
		this.setSize(300, 15);
		setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));
		add(imageLabel);
	}

	/**
	 * 获取显示的名称
	 * @return
	 */
	public String getDataName() {
		return dataName;
	}
	
	/**
	 * 获取显示的控件
	 * @return
	 */
	public Object getData() {
		return data;
	}
	/**
	 * 设置显示的控件
	 * @return
	 */
	public void setData(Object data) {
		this.data = data;
	}
	/**
	 * 重载toString
	 */
	@Override
	public String toString() {
		return getDataName();
	}
}