package com.supermap.desktop.newtheme;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.supermap.data.ColorGradientType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetVector;
import com.supermap.data.GeoStyle;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.mapping.ThemeGridUnique;
import com.supermap.mapping.ThemeGridUniqueItem;
import com.supermap.mapping.ThemeType;
import com.supermap.mapping.ThemeUnique;
import com.supermap.mapping.ThemeUniqueItem;

public class AddItemPanel extends JPopupMenu {

	private static final long serialVersionUID = 1L;
	private JTextField textFieldCaption;
	private JScrollPane panelAddto = new JScrollPane();
	private JScrollPane panelRemoveFrom = new JScrollPane();
	private JList<String> listAddto = new JList<String>(new DefaultListModel<String>());
	private JList<String> listRemoveFrom = new JList<String>(new DefaultListModel<String>());
	private JButton buttonAddto = new JButton(">>");
	private JButton buttonRemoveFrom = new JButton("<<");
	private JButton buttonAdd = new JButton();

	private transient LocalActionListener actionListener = new LocalActionListener();

	private transient ThemeUnique themeUnique;
	private transient ThemeGridUnique themeGridUnique;
	private ArrayList<ThemeUniqueItem> deleteUniqueItems;
	private ArrayList<ThemeGridUniqueItem> deleteGridUniqueItems;
	private transient Dataset dataset;
	private ThemeType themeType;

	public AddItemPanel() {
		initComponent();
		fillComponent();
		initResources();
		registActionListener();
	}

	public AddItemPanel(ThemeType themeType) {
		super();
		this.themeType = themeType;
	}

	public void init() {
		initUnaddedItems();
		initComponent();
		fillComponent();
		initResources();
		registActionListener();
	}

	/**
	 * 初始化未添加的单值项
	 */
	private void initUnaddedItems() {
		if (themeType == ThemeType.UNIQUE) {
			ThemeUnique themeUniqueTemp = ThemeUnique.makeDefault((DatasetVector) dataset, themeUnique.getUniqueExpression(), ColorGradientType.YELLOWGREEN);
			List allItems = new ArrayList();
			for (int i = 0; i < themeUniqueTemp.getCount(); i++) {
				allItems.add(themeUniqueTemp.getItem(i).getUnique());
			}
			for (int i = deleteUniqueItems.size() - 1; i >= 0; i--) {
				if (!allItems.contains(deleteUniqueItems.get(i).getUnique())) {
					deleteUniqueItems.remove(i);
				}
			}
			List addedItems = new ArrayList<>();
			for (int i = 0; i < themeUnique.getCount(); i++) {
				addedItems.add(themeUnique.getItem(i).getUnique());
			}

			List deletedItemList = new ArrayList<>();
			for (int i = 0; i < deleteUniqueItems.size(); i++) {
				deletedItemList.add(deleteUniqueItems.get(i).getUnique());
			}

			for (int i = 0; i < allItems.size(); i++) {
				if (!addedItems.contains(allItems.get(i)) && !deletedItemList.contains(allItems.get(i))) {
					deleteUniqueItems.add(getNewItem(themeUniqueTemp.getItem(i).getUnique()));
				}
			}
		} else {
			ThemeGridUnique themeGridUniqueTemp = ThemeGridUnique.makeDefault((DatasetGrid) dataset);
			List allItems = new ArrayList();
			for (int i = 0; i < themeGridUniqueTemp.getCount(); i++) {
				allItems.add(themeGridUniqueTemp.getItem(i).getUnique());
			}
			for (int i = deleteGridUniqueItems.size() - 1; i >= 0; i--) {
				if (!allItems.contains(deleteGridUniqueItems.get(i).getUnique())) {
					deleteGridUniqueItems.remove(i);
				}
			}
			List addedItems = new ArrayList<>();
			for (int i = 0; i < themeGridUnique.getCount(); i++) {
				addedItems.add(themeGridUnique.getItem(i).getUnique());
			}

			List deletedItemList = new ArrayList<>();
			for (int i = 0; i < deleteGridUniqueItems.size(); i++) {
				deletedItemList.add(deleteGridUniqueItems.get(i).getUnique());
			}

			for (int i = 0; i < allItems.size(); i++) {
				if (!addedItems.contains(allItems.get(i)) && !deletedItemList.contains(allItems.get(i))) {
					deleteGridUniqueItems.add(getNewGridItem(themeGridUniqueTemp.getItem(i).getUnique()));
				}
			}
		}
	}

	/**
	 * 布局入口
	 */
	private void initComponent() {
		setSize(345, 345);
		this.textFieldCaption = new JTextField();
		this.textFieldCaption.setColumns(10);
		// @formatter:off
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(8)
							.addComponent(panelRemoveFrom, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(buttonRemoveFrom)
								.addComponent(buttonAddto))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panelAddto, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(27)
							.addComponent(textFieldCaption, GroupLayout.PREFERRED_SIZE, 223, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(buttonAdd)))
					.addContainerGap(9, Short.MAX_VALUE))
		);
		this.panelAddto.setBackground(Color.WHITE);
		this.panelRemoveFrom.setBackground(Color.WHITE);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(panelRemoveFrom, GroupLayout.PREFERRED_SIZE, 237, GroupLayout.PREFERRED_SIZE)
								.addComponent(panelAddto, GroupLayout.PREFERRED_SIZE, 235, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(48)
							.addComponent(buttonAddto)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(buttonRemoveFrom)))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textFieldCaption, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonAdd))
					.addContainerGap(24, Short.MAX_VALUE))
		);

		this.listRemoveFrom.setBorder(new LineBorder(Color.LIGHT_GRAY));
		this.listAddto.setBorder(new LineBorder(Color.LIGHT_GRAY));
		this.panelAddto.setViewportView(listAddto);
		this.panelRemoveFrom.setViewportView(listRemoveFrom);
		this.setLayout(groupLayout);
		//@formatter:on
	}

	/**
	 * 初始化面板
	 */
	private void fillComponent() {
		if (deleteUniqueItems != null && themeType == ThemeType.UNIQUE) {
			for (ThemeUniqueItem deleteItem : deleteUniqueItems) {
				((DefaultListModel<String>) listRemoveFrom.getModel()).add(listRemoveFrom.getModel().getSize(), deleteItem.getUnique());
			}
		}
		if (themeUnique != null && themeType == ThemeType.UNIQUE) {
			for (int i = 0; i < themeUnique.getCount(); i++) {
				((DefaultListModel<String>) listAddto.getModel()).add(listAddto.getModel().getSize(), themeUnique.getItem(i).getUnique());
			}
		}
		if (deleteGridUniqueItems != null && themeType == ThemeType.GRIDUNIQUE) {
			for (ThemeGridUniqueItem deleteGridItem : deleteGridUniqueItems) {
				((DefaultListModel<String>) listRemoveFrom.getModel()).add(listRemoveFrom.getModel().getSize(), String.valueOf(((int) deleteGridItem.getUnique())));
			}
		}
		if (themeGridUnique != null && themeType == ThemeType.GRIDUNIQUE) {
			for (int i = 0; i < themeGridUnique.getCount(); i++) {
				((DefaultListModel<String>) listAddto.getModel()).add(listAddto.getModel().getSize(), String.valueOf((int) themeGridUnique.getItem(i).getUnique()));
			}
		}
	}

	/**
	 * 资源化
	 */
	private void initResources() {
		this.buttonAdd.setText(MapViewProperties.getString("String_Title_Add"));
		this.panelAddto.setBorder(new TitledBorder(null, MapViewProperties.getString("String_hasAdded"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.panelRemoveFrom.setBorder(new TitledBorder(null, MapViewProperties.getString("String_hasNotAdded"), TitledBorder.LEADING, TitledBorder.TOP, null,
				null));
	}

	/**
	 * 注册事件
	 */
	private void registActionListener() {
		this.buttonAdd.addActionListener(this.actionListener);
		this.buttonAddto.addActionListener(this.actionListener);
		this.buttonRemoveFrom.addActionListener(this.actionListener);
		this.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				AddItemPanel.this.unregistActionListener();
			}
		});
	}

	/**
	 * 注销事件
	 */
	private void unregistActionListener() {
		this.buttonAdd.removeActionListener(this.actionListener);
		this.buttonAddto.removeActionListener(this.actionListener);
		this.buttonRemoveFrom.removeActionListener(this.actionListener);
	}

	private ThemeUniqueItem getDeletedItem(String itemName) {
		for (ThemeUniqueItem deleteItem : deleteUniqueItems) {
			if (deleteItem.getUnique().equals(itemName)) {
				return deleteItem;
			}
		}
		return null;
	}

	private ThemeGridUniqueItem getDeletedGridItem(String itemName) {
		double unique = Double.valueOf(itemName);
		for (ThemeGridUniqueItem deleteItem : deleteGridUniqueItems) {
			if (Double.compare(unique, deleteItem.getUnique()) == 0) {
				return deleteItem;
			}
		}
		return null;
	}

	/**
	 * 找到单值的对应关系
	 *
	 * @param selectList
	 * @return
	 */
	private HashMap<Integer, ThemeUniqueItem> getNeedDeleteItemHashMap(List<String> selectList) {
		HashMap<Integer, ThemeUniqueItem> result = new HashMap<>();

		for (int i = 0; i < themeUnique.getCount(); i++) {
			ThemeUniqueItem themeUniqueItemTemp = themeUnique.getItem(i);
			for (String selectItemName : selectList) {
				if (themeUniqueItemTemp.getUnique().equals(selectItemName)) {
					result.put(i, themeUniqueItemTemp);
				}
			}
		}
		return result;
	}

	/**
	 * 找到栅格单值的对应关系
	 *
	 * @param selectList
	 * @return
	 */
	private HashMap<Integer, ThemeGridUniqueItem> getNeedDeleteGridItemHashMap(List<String> selectList) {
		HashMap<Integer, ThemeGridUniqueItem> result = new HashMap<>();

		for (int i = 0; i < themeGridUnique.getCount(); i++) {
			ThemeGridUniqueItem themeGridUniqueItem = themeGridUnique.getItem(i);
			for (String selectItemName : selectList) {
				double tempUnique = Double.valueOf(selectItemName);
				if (Double.compare(themeGridUniqueItem.getUnique(), tempUnique) == 0) {
					result.put(i, themeGridUniqueItem);
				}
			}
		}
		return result;
	}

	class LocalActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttonAdd) {
				// 添加单值项
				addThemeItem();
			} else if (e.getSource() == buttonAddto) {
				if (themeType == ThemeType.UNIQUE) {
					// 从未添加项中添加到添加项中去
					List<String> selectList = listRemoveFrom.getSelectedValuesList();
					if (selectList != null && !selectList.isEmpty()) {
						for (String selectItem : selectList) {
							ThemeUniqueItem selectUniqueItem = getDeletedItem(selectItem);
							((DefaultListModel<String>) listRemoveFrom.getModel()).removeElement(selectUniqueItem.getUnique());
							((DefaultListModel<String>) listAddto.getModel()).add(listAddto.getModel().getSize(), selectUniqueItem.getUnique());
							themeUnique.add(selectUniqueItem);
							deleteUniqueItems.remove(selectUniqueItem);
						}
					}
				} else {
					// 从未添加项中添加到添加项中去
					List<String> selectList = listRemoveFrom.getSelectedValuesList();
					if (selectList != null && !selectList.isEmpty()) {
						for (String selectItem : selectList) {
							ThemeGridUniqueItem gridUniqueItem = getDeletedGridItem(selectItem);
							((DefaultListModel<String>) listRemoveFrom.getModel()).removeElement(String.valueOf((int) gridUniqueItem.getUnique()));
							((DefaultListModel<String>) listAddto.getModel()).add(listAddto.getModel().getSize(), String.valueOf(((int) gridUniqueItem.getUnique())));
							themeGridUnique.add(gridUniqueItem);
							deleteGridUniqueItems.remove(gridUniqueItem);
						}
					}
				}
			} else {
				if (themeType == ThemeType.UNIQUE) {
					// 从已添加项中移除某项
					List<String> selectList = listAddto.getSelectedValuesList();
					if (selectList != null && !selectList.isEmpty()) {
						HashMap<Integer, ThemeUniqueItem> result = getNeedDeleteItemHashMap(selectList);
						for (int i = themeUnique.getCount() - 1; i >= 0; i--) {
							if (result.get(i) != null) {
								((DefaultListModel<String>) listAddto.getModel()).removeElement(result.get(i).getUnique());
								((DefaultListModel<String>) listRemoveFrom.getModel()).add(listRemoveFrom.getModel().getSize(), result.get(i).getUnique());
								deleteUniqueItems.add(new ThemeUniqueItem(result.get(i)));
								themeUnique.remove(i);

							}
						}
					}
				} else {
					// 从已添加项中移除某项
					List<String> selectList = listAddto.getSelectedValuesList();
					if (selectList != null && !selectList.isEmpty()) {
						HashMap<Integer, ThemeGridUniqueItem> result = getNeedDeleteGridItemHashMap(selectList);
						for (int i = themeGridUnique.getCount() - 1; i >= 0; i--) {
							if (result.get(i) != null) {
								((DefaultListModel<String>) listAddto.getModel()).removeElement(String.valueOf(((int) result.get(i).getUnique())));
								((DefaultListModel<String>) listRemoveFrom.getModel()).add(listRemoveFrom.getModel().getSize(),
										String.valueOf(((int) result.get(i).getUnique())));
								deleteGridUniqueItems.add(new ThemeGridUniqueItem(result.get(i)));
								themeGridUnique.remove(i);

							}
						}
					}
				}
			}
		}

		private void addThemeItem() {
			String caption = textFieldCaption.getText();
			textFieldCaption.setText("");
			if (caption == null || caption.length() <= 0) {
				return;
			}
			if (themeType == ThemeType.UNIQUE) {
				// 先从已删除的子项找
				for (ThemeUniqueItem deleteItem : deleteUniqueItems) {
					if (caption.equals(deleteItem.getUnique())) {
						((DefaultListModel<String>) listRemoveFrom.getModel()).removeElement(deleteItem.getUnique());
						((DefaultListModel<String>) listAddto.getModel()).add(listAddto.getModel().getSize(), deleteItem.getUnique());
						themeUnique.add(deleteItem);
						deleteUniqueItems.remove(deleteItem);
						return;
					}
				}
				// 查看是否已存在
				for (int i = 0; i < themeUnique.getCount(); i++) {
					if (themeUnique.getItem(i).getUnique().equals(caption)) {
						return;
					}
				}
				for (int i = 0; i < deleteUniqueItems.size(); i++) {
					if (deleteUniqueItems.get(i).getUnique().equals(caption)) {
						return;
					}
				}
				themeUnique.add(getNewItem(caption));
			} else {
				// 先从已删除的子项找
				for (ThemeGridUniqueItem deleteGridItem : deleteGridUniqueItems) {
					if (Double.compare(deleteGridItem.getUnique(), Double.parseDouble(caption)) == 0) {
						((DefaultListModel<String>) listRemoveFrom.getModel()).removeElement(String.valueOf(((int) deleteGridItem.getUnique())));
						((DefaultListModel<String>) listAddto.getModel()).add(listAddto.getModel().getSize(), String.valueOf(((int) deleteGridItem.getUnique())));
						themeGridUnique.add(deleteGridItem);
						deleteGridUniqueItems.remove(deleteGridItem);
						return;
					}
				}
				double existValue = Double.parseDouble(caption);
				// 查看是否已存在
				for (int i = 0; i < themeGridUnique.getCount(); i++) {
					double gridUnique = themeGridUnique.getItem(i).getUnique();
					if (Double.compare(gridUnique, existValue) == 0) {
						return;
					}
				}
				for (int i = 0; i < deleteGridUniqueItems.size(); i++) {
					double gridUniqueValue = deleteGridUniqueItems.get(i).getUnique();
					if (Double.compare(gridUniqueValue, existValue) == 0) {
						return;
					}
				}
				themeGridUnique.add(getNewGridItem(Double.valueOf(caption)));
			}
			((DefaultListModel<String>) listAddto.getModel()).add(listAddto.getModel().getSize(), caption);

		}
	}

	private ThemeGridUniqueItem getNewGridItem(double value) {
		ThemeGridUniqueItem item = new ThemeGridUniqueItem();
		item.setCaption(String.valueOf(value));
		item.setUnique(value);
		item.setVisible(true);
		Color color = Color.black;
		item.setColor(color);
		return item;
	}

	private ThemeUniqueItem getNewItem(String value) {
		ThemeUniqueItem item = new ThemeUniqueItem();
		item.setCaption(value);
		item.setUnique(value);
		item.setVisible(true);
		GeoStyle geoStyle = themeUnique.getDefaultStyle();
		item.setStyle(geoStyle);
		return item;
	}

	public ThemeUnique getThemeUnique() {
		return themeUnique;
	}

	public void setThemeUnique(ThemeUnique themeUnique) {
		this.themeUnique = themeUnique;
	}

	public ThemeGridUnique getThemeGridUnique() {
		return themeGridUnique;
	}

	public void setThemeGridUnique(ThemeGridUnique themeGridUnique) {
		this.themeGridUnique = themeGridUnique;
	}

	public ArrayList<ThemeUniqueItem> getDeleteUniqueItems() {
		return deleteUniqueItems;
	}

	public void setDeleteUniqueItems(ArrayList<ThemeUniqueItem> deleteUniqueItems) {
		this.deleteUniqueItems = deleteUniqueItems;
	}

	public ArrayList<ThemeGridUniqueItem> getDeleteGridUniqueItems() {
		return deleteGridUniqueItems;
	}

	public void setDeleteGridUniqueItems(ArrayList<ThemeGridUniqueItem> deleteGridUniqueItems) {
		this.deleteGridUniqueItems = deleteGridUniqueItems;
	}

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public ThemeType getThemeType() {
		return themeType;
	}

	public void setThemeType(ThemeType themeType) {
		this.themeType = themeType;
	}

}
