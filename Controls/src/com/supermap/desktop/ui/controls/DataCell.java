package com.supermap.desktop.ui.controls;

import java.awt.FlowLayout;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author xie 控件中用于显示数据集图标，可用于每一个有单元格的控件 如JComBox,JList等，使用时需要调用setRenderer() 方法将渲染器设置为CommonListCellRenderer 不然不会添加图片效果，具体用法请参见DatasetComboBox类
 */
public class DataCell extends JPanel {
	private static final long serialVersionUID = 1L;
	private String path;
	private JLabel imageLabel;
	private String datasetName;
	private Object data;

	public DataCell(ImageIcon icon, String datasetName) {
		this.datasetName = datasetName;
		imageLabel = new JLabel(datasetName, icon, JLabel.LEADING);
		this.setSize(300, 15);
		setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));
		add(imageLabel);
	}

	public DataCell(ImageIcon icon, String datasetName, Object data) {
		this(icon, datasetName);
		this.data = data;
	}

	public DataCell(String path, String datasetName) {
		this.path = path;
		this.datasetName = datasetName;
		URL url = DatasetComboBox.class.getResource(path);
		ImageIcon tempIcon = null;
		if (null != url) {
			tempIcon = new ImageIcon(url);
			imageLabel = new JLabel(datasetName, tempIcon, JLabel.LEADING);
		} else {
			imageLabel = new JLabel(datasetName);
		}

		this.setSize(300, 15);
		setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));
		add(imageLabel);
	}

	public DataCell(String path, String datasetName, Object data) {
		this(path, datasetName);
		this.data = data;

	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return getDatasetName();
	}
}