package com.supermap.desktop.ui.controls.comboBox;

public class ComboBoxItem {

	private Object data;
	private String name;
	private java.awt.Image image;

	public ComboBoxItem() {
		initialize(null, null, null);
	}

	/**
	 * 根据指定的参数来构造一个新的 ComboBoxItem 对象。
	 * 
	 * @param data
	 *            子项绑定的数据对象
	 * @param name
	 *            子项名称
	 */
	public ComboBoxItem(Object data, String name) {
		initialize(data, name);
	}

	/**
	 * 根据指定的参数来构造一个新的 ComboBoxItem 对象。
	 * 
	 * @param data
	 *            子项绑定的数据对象
	 * @param name
	 *            子项名称
	 * @param image
	 *            子项显示图像
	 */
	public ComboBoxItem(Object data, String name, java.awt.Image image) {
		initialize(data, name, image);
	}

	/**
	 * 获取或设置 ComboBox 要绑定的数据对象
	 * 
	 * @return
	 */
	public Object getData() {
		return this.data;
	}

	public void setData(Object value) {
		this.data = value;
	}

	/**
	 * 获取或设置 ComboBox 要绑定的数据名称
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	public void setName(String value) {
		this.name = value;
	}

	// / <summary>
	// / 获取或设置 ComboBox 要绑定的数据显示图片
	// / </summary>
	public java.awt.Image getImage() {
		return this.image;
	}

	public void setImage(java.awt.Image value) {
		this.image = value;
	}

	private void initialize(Object data, String name) {
		initialize(data, name, null);
	}

	private void initialize(Object data, String name, java.awt.Image image) {
		this.data = data;
		this.name = name;
		this.image = image;
	}

	/**
	 * 返回 ComboBoxItem 对象的字符串表示形式。
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return this.name;
	}
}
