package com.supermap.desktop.ui.controls;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.data.EngineType;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.utilties.DatasetTypeUtilities;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * @author xie 控件中用于显示数据集图标，可用于每一个有单元格的控件 如JComBox,JList等，使用时需要调用setRenderer() 方法将渲染器设置为CommonListCellRenderer 不然不会添加图片效果，具体用法请参见DatasetComboBox类
 */
public class DataCell extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel imageLabel;
	private String dataName;
	private Object data;
	private final Color selectColor = new Color(185, 214, 255);

	public DataCell() {
		// 公共类型
	}

	public void setSelected(boolean isSelected) {
		if (isSelected) {
			this.setBackground(Color.WHITE);
			this.imageLabel.setOpaque(true);
			this.imageLabel.setBackground(selectColor);
		} else {
			this.setBackground(Color.WHITE);
			this.imageLabel.setOpaque(true);
			this.imageLabel.setBackground(Color.WHITE);
		}
	}

	/**
	 * 根据数据源来创建
	 *
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
	 *
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
	 *
	 * @param dataset
	 */
	public void initDatasetType(Dataset dataset) {
		if (dataset != null) {
			setData(dataset);
			String datasetImagePath = CommonToolkit.DatasetImageWrap.getImageIconPath(dataset.getType());
			URL url = DataCell.class.getResource(datasetImagePath);
			this.dataName = dataset.getName();
			init(url, this.dataName);
		}
	}

	/**
	 * 根据数据集类型和数据集名称创建
	 *
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
	 *
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
	 *
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
			this.imageLabel = new JLabel();
		}
		initComponents();
	}

	private void initComponents() {
		this.setSize(300, 15);
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));
		this.add(this.imageLabel);
	}

	/**
	 * 获取显示的名称
	 *
	 * @return
	 */
	public String getDataName() {
		return dataName;
	}

	/**
	 * 获取显示的控件
	 *
	 * @return
	 */
	public Object getData() {
		return data;
	}

	/**
	 * 设置显示的控件
	 *
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

	// region 构造函数

	/**
	 * 不支持传入图片路径方式构建
	 * <p/>
	 * 根据图片路径和数据文字请使用initDataType()
	 *
	 * @param objects 传入的参数
	 * @see DataCell#initDataType(String imagePath, String dataName)
	 */
	public DataCell(Object... objects) {
		EngineType engineType = null;
		DatasetType datasetType = null;
		ImageIcon icon = null;
		String name = null;

		for (Object object : objects) {
			if (object instanceof Datasource) {
				this.initDatasourceType(((Datasource) object));
				break;
			} else if (object instanceof Dataset) {
				this.initDatasetType(((Dataset) object));
				break;
			} else if (object instanceof EngineType) {
				if (name != null) {
					this.initDatasourceType(((EngineType) object), name);
					break;
				} else {
					engineType = ((EngineType) object);
				}
			} else if (object instanceof DatasetType) {
				if (name != null) {
					this.initDatasetType(((DatasetType) object), name);
					break;
				} else {
					datasetType = ((DatasetType) object);
				}
			} else if (object instanceof ImageIcon) {
				if (name != null) {
					this.initDataImage(((ImageIcon) object), name);
					break;
				} else {
					icon = ((ImageIcon) object);
				}
			} else if (object instanceof String) {
				String str = (String) object;
				if (engineType != null) {
					this.initDatasourceType(engineType, str);
					break;
				} else if (datasetType != null) {
					this.initDatasetType(datasetType, str);
					break;
				} else if (icon != null) {
					this.initDataImage(icon, str);
					break;
				} else {
					name = str;
				}
			} else {
				break;
			}
		}
		if (datasetType != null && name == null) {
			initDatasetType(datasetType, DatasetTypeUtilities.toString(datasetType));
		}
	}
	// endregion
}