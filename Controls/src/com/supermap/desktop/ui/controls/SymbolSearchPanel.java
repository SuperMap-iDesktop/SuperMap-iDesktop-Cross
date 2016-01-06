package com.supermap.desktop.ui.controls;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.supermap.data.GeoLine;
import com.supermap.data.GeoPoint;
import com.supermap.data.GeoRegion;
import com.supermap.data.Symbol;
import com.supermap.data.SymbolFill;
import com.supermap.data.SymbolLine;
import com.supermap.data.SymbolMarker;
import com.supermap.data.SymbolType;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;

/**
 * 符号搜索面板
 * 
 * @author xuzw
 *
 */
class SymbolSearchPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextField jTextFieldSymbolSearch;

	private transient SymbolPanel symbolsViewPanel;

	private static final String SYSTEM = "System";

	private static final String[] SYSTEM_SYMBOL_NAME = { SYSTEM + " 0", SYSTEM + " 1", SYSTEM + " 2", SYSTEM + " 3", SYSTEM + " 4", SYSTEM + " 5",
			SYSTEM + " 6" };

	private static final int SYMBOL_PANEL_WIDTH = 510;

	// add by xuzw 2010-10-11
	// 填充库显示面板的宽度定在420像素，因为填充库的设置太多，需要占用较大的空间
	private static final int SYMBOLFILL_PANEL_WIDTH = 420;

	private static final int SYMBOLMARKER_PANEL_HEIGHT_INCREMENT = 88;

	private static final int SYMBOLLINE_PANEL_HEIGHT_INCREMENT = 87;

	private static final int SYMBOLREGION_PANEL_HEIGHT_INCREMENT = 108;

	private transient SymbolType symbolType;

	// 浏览历史链表
	private LinkedList<String> prevSearches;

	private JPopupMenu prevSearchMenu;

	private static final int SYMBOLMARKER_AND_SYMBOLLINE_LABEL_SIZE = 80;

	private static final int SYMBOLFILL_LABEL_SIZE = 100;

	/**
	 * Create the panel
	 */
	public SymbolSearchPanel(SymbolPanel symbolsViewPanel) {
		super();
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(3, 2, 3, 2));
		this.symbolsViewPanel = symbolsViewPanel;
		symbolType = symbolsViewPanel.getType();
		prevSearches = new LinkedList<String>();
		final JLabel labelSymbolSearch = new JLabel();
		labelSymbolSearch.setText(ControlsProperties.getString("String_Label_SymbolSearch"));

		Box box = Box.createHorizontalBox();
		box.add(labelSymbolSearch);
		box.add(Box.createHorizontalStrut(5));
		box.add(getTextFieldSymbolSearch());
		add(box);
	}

	/**
	 * 获取符号搜索文本框
	 * 
	 * @return
	 */
	protected JTextField getTextFieldSymbolSearch() {
		if (jTextFieldSymbolSearch == null) {
			jTextFieldSymbolSearch = new JTextField();
			jTextFieldSymbolSearch.setPreferredSize(new Dimension(140, 25));
			jTextFieldSymbolSearch.setMinimumSize(new Dimension(140, 25));
			// 文本事件
			jTextFieldSymbolSearch.getDocument().addDocumentListener(new SetDocumentListener());
		}
		return jTextFieldSymbolSearch;
	}

	/**
	 * 获取历史搜索按钮
	 * 
	 * @return
	 */

	/**
	 * 弹出历史浏览菜单
	 * 
	 * @param x
	 * @param y
	 */
	public void popMenu() {
		prevSearchMenu = new JPopupMenu();
		Iterator<String> it = prevSearches.iterator();
		while (it.hasNext()) {
			prevSearchMenu.add(new PrevSearchAction(it.next()));
		}
	}

	/**
	 * 符号搜索方法
	 */
	protected void search() {
		if (symbolType.equals(SymbolType.MARKER)) {
			searchMarker(jTextFieldSymbolSearch.getText());
		} else if (symbolType.equals(SymbolType.LINE)) {
			searchLine(jTextFieldSymbolSearch.getText());
		} else if (symbolType.equals(SymbolType.FILL)) {
			searchFill(jTextFieldSymbolSearch.getText());
		}
	}

	/**
	 * 根据传入的字符串搜索点符号
	 * 
	 * @param filter
	 */
	protected void searchMarker(String filter) {
		// 空串的话就全画出来
		if (filter.trim().length() == 0) {
			symbolsViewPanel.paintMarkerSymbols();
			return;
		}
		ArrayList<Symbol> symbolList = new ArrayList<Symbol>();
		ArrayList<Integer> systemIDsList = new ArrayList<Integer>();
		searchPaintSymbol(filter, "0", symbolList, systemIDsList, SymbolType.MARKER);

		Symbol[] symbols = new Symbol[symbolList.size()];
		Integer[] symbolIDs = new Integer[systemIDsList.size()];
		paintSymbolsForMarkerSearch(48, 48, symbolsViewPanel.getPaintPoint(), symbolsViewPanel.getPanelSymbolsView(), symbolList.toArray(symbols),
				systemIDsList.toArray(symbolIDs));
	}

	/**
	 * 根据传入的字符串搜索线符号
	 * 
	 * @param filter
	 */
	protected void searchLine(String filter) {
		// 空串的话就全画出来
		if (filter.trim().length() == 0) {
			symbolsViewPanel.paintLineSymbols();
			return;
		}
		ArrayList<Symbol> symbolList = new ArrayList<Symbol>();
		ArrayList<Integer> systemIDsList = new ArrayList<Integer>();
		searchPaintSymbol(filter, "012345", symbolList, systemIDsList, SymbolType.LINE);

		Symbol[] symbols = new Symbol[symbolList.size()];
		Integer[] symbolIDs = new Integer[systemIDsList.size()];
		paintSymbolsForLineSearch(64, 64, symbolsViewPanel.getPaintLine(), symbolsViewPanel.getPanelSymbolsView(), symbolList.toArray(symbols),
				systemIDsList.toArray(symbolIDs));
	}

	/**
	 * 根据传入的字符串搜索面符号
	 * 
	 * @param filter
	 */
	protected void searchFill(String filter) {
		// 如果搜索栏是空串，就全画上去
		if (filter.trim().length() == 0) {
			symbolsViewPanel.paintFillSymbols();
			return;
		}
		ArrayList<Symbol> symbolList = new ArrayList<Symbol>();
		ArrayList<Integer> systemIDsList = new ArrayList<Integer>();
		searchPaintSymbol(filter, "0123456", symbolList, systemIDsList, SymbolType.FILL);

		Symbol[] symbols = new Symbol[symbolList.size()];
		Integer[] symbolIDs = new Integer[systemIDsList.size()];
		paintSymbolsForFillSearch(80, 80, symbolsViewPanel.getPaintRegion(), symbolsViewPanel.getPanelSymbolsView(), symbolList.toArray(symbols),
				systemIDsList.toArray(symbolIDs));
	}

	/**
	 * 根据搜索框的信息，查找要绘制的符号
	 * 
	 * @param filter 搜索框中的字符串
	 * @param systemSymbolID 系统符号串
	 * @param symbolList 要绘制的符号的数组
	 * @param systemIDsList 要绘制的系统符号ID的数组
	 * @param type 查找的类型
	 */
	private void searchPaintSymbol(String filter, String systemSymbolID, ArrayList<Symbol> symbolList, ArrayList<Integer> systemIDsList, SymbolType type) {
		String filterTemp = filter.trim().toLowerCase();
		// 如果输入了"System"串，那所有的系统符号都应当加进去
		// add by xuzw 2010-09-08 不是根组就不画系统符号了
		if (SYSTEM.toLowerCase().indexOf(filterTemp) != -1) {
			Object[] objects = symbolsViewPanel.getSymbolsTree().getSelectionPath().getPath();
			if ((symbolsViewPanel.getSymbolGroup(objects)).equals(symbolsViewPanel.getRootGroup())) {
				for (int i = 0; i < systemSymbolID.length(); i++) {
					systemIDsList.add(i);
				}
			}
		}

		// 如果数字是systemSymbolID中的一个，那必然要加一个系统符号
		if (systemSymbolID.indexOf(filterTemp) != -1) {
			Integer symbolID = Integer.valueOf(filterTemp);
			if (systemIDsList.indexOf(symbolID) == -1) {
				systemIDsList.add(symbolID);
			}
		}
		// 根据输入的名称查找符号
		ArrayList<LabelInfo> arrayList = symbolsViewPanel.getAllLabelInfo();
		CharSequence temp = filterTemp.subSequence(0, filterTemp.length());
		if (type.equals(SymbolType.MARKER)) {
			if (SYSTEM_SYMBOL_NAME[0].equalsIgnoreCase(filterTemp) && systemIDsList.indexOf(0) == -1) {
				systemIDsList.add(0);
			}
			for (int i = 0; i < arrayList.size(); i++) {
				LabelInfo labelInfo = arrayList.get(i);
				String symbolName = labelInfo.getSymbolName();
				if (symbolName.toLowerCase().contains(temp)) {
					int id = labelInfo.getSymbolID();
					Symbol symbol = symbolsViewPanel.getResources().getMarkerLibrary().findSymbol(id);
					if (symbol != null) {
						symbolList.add(symbol);
					}
				}
			}
		} else if (type.equals(SymbolType.LINE)) {
			for (int i = 0; i < 6; i++) {
				if (SYSTEM_SYMBOL_NAME[i].equalsIgnoreCase(filterTemp) && systemIDsList.indexOf(i) == -1) {
					systemIDsList.add(i);
					break;
				}
			}
			for (int i = 0; i < arrayList.size(); i++) {
				LabelInfo labelInfo = arrayList.get(i);
				String symbolName = labelInfo.getSymbolName();
				if (symbolName.toLowerCase().contains(temp)) {
					int id = labelInfo.getSymbolID();
					Symbol symbol = symbolsViewPanel.getResources().getLineLibrary().findSymbol(id);
					if (symbol != null) {
						symbolList.add(symbol);
					}
				}
			}
		} else if (type.equals(SymbolType.FILL)) {
			for (int i = 0; i < 7; i++) {
				if (SYSTEM_SYMBOL_NAME[i].equalsIgnoreCase(filterTemp) && systemIDsList.indexOf(i) == -1) {
					systemIDsList.add(i);
					break;
				}
			}
			for (int i = 0; i < arrayList.size(); i++) {
				LabelInfo labelInfo = arrayList.get(i);
				String symbolName = labelInfo.getSymbolName();
				if (symbolName.toLowerCase().contains(temp)) {
					int id = labelInfo.getSymbolID();
					Symbol symbol = symbolsViewPanel.getResources().getFillLibrary().findSymbol(id);
					if (symbol != null) {
						symbolList.add(symbol);
					}
				}
			}
		}
	}

	/**
	 * 将m_symbolGroup中的符号，通过大小和几何对象绘制到panel中
	 */
	protected void paintSymbolsForMarkerSearch(int width, int height, GeoPoint geometry, JPanel panel, Symbol[] symbols, Integer[] systemSymbolIDs) {
		try {
			panel.removeAll();
			int count = symbols.length;
			int row = 0;

			// 列数，也就是一行能放几个JLabel
			int columnCount = SYMBOL_PANEL_WIDTH / SYMBOLMARKER_AND_SYMBOLLINE_LABEL_SIZE;
			ArrayList<LabelInfo> arrayList = symbolsViewPanel.getLabelInfoArray();
			arrayList.clear();
			// 循环得到符号并将符号添加到Label，后将Label添加到JPanel
			for (int i = 0; i < systemSymbolIDs.length; i++) {
				// 这里减4主要是为了下面的文本显示
				BufferedImage bufferedImage = new BufferedImage(width, height - 4, BufferedImage.TYPE_INT_ARGB);
				GeoPoint geoPoint = (GeoPoint) geometry;
				geoPoint.getStyle().setMarkerSymbolID(systemSymbolIDs[i]);
				InternalToolkitControl.internalDraw(geoPoint, symbolsViewPanel.getResources(), bufferedImage.getGraphics());

				String symbolName = "System " + systemSymbolIDs[i];
				JLabel label = symbolsViewPanel.getSymbolLabel(symbolName, bufferedImage);
				label.setName(String.valueOf(systemSymbolIDs[i]));

				if (i != 0 && i % (columnCount) == 0) {
					row++;
				}

				panel.setPreferredSize(new Dimension(SYMBOL_PANEL_WIDTH, (row + 1) * SYMBOLMARKER_PANEL_HEIGHT_INCREMENT));
				panel.add(label);

				LabelInfo labelInfo = new LabelInfo(label, row, i % columnCount, systemSymbolIDs[i], symbolName);
				arrayList.add(labelInfo);
			}
			for (int i = systemSymbolIDs.length; i < count + systemSymbolIDs.length; i++) {
				Symbol symbol = symbols[i - systemSymbolIDs.length];
				BufferedImage bufferedImage = new BufferedImage(width, height - 4, BufferedImage.TYPE_INT_ARGB);

				SymbolMarker marker = (SymbolMarker) symbol;
				Point point = marker.getOrigin();
				int x = point.x * width / UIEnvironment.symbolPointMax;
				int y = point.y * height / UIEnvironment.symbolPointMax;
				GeoPoint geoPoint = (GeoPoint) geometry;
				geoPoint.setX(x);
				geoPoint.setY(y);
				geoPoint.getStyle().setMarkerSymbolID(symbol.getID());
				InternalToolkitControl.internalDraw(geoPoint, symbolsViewPanel.getResources(), bufferedImage.getGraphics());

				JLabel label = symbolsViewPanel.getSymbolLabel(symbol.getName(), bufferedImage);
				label.setName(String.valueOf(symbol.getID()));

				if (i != 0 && i % (columnCount) == 0) {
					row++;
				}
				panel.setPreferredSize(new Dimension(SYMBOL_PANEL_WIDTH, (row + 1) * SYMBOLMARKER_PANEL_HEIGHT_INCREMENT));
				panel.add(label);

				LabelInfo labelInfo = new LabelInfo(label, row, i % columnCount, symbol.getID(), symbol.getName());
				arrayList.add(labelInfo);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			symbolsViewPanel.getSymbolBasicInfoPanel().refreshComboBox(symbolsViewPanel.getLabelInfoArray());
			panel.getParent().validate();
			panel.getParent().repaint();
			panel.validate();
			panel.repaint();
		}
	}

	/**
	 * 根据线搜索框提供的信息绘制线符号
	 * 
	 * @param width
	 * @param height
	 * @param geometry
	 * @param panel
	 * @param symbols
	 * @param systemSymbolIDs
	 */
	protected void paintSymbolsForLineSearch(int width, int height, GeoLine geometry, JPanel panel, Symbol[] symbols, Integer[] systemSymbolIDs) {
		try {
			panel.removeAll();
			int count = symbols.length;
			int row = 0;

			// 列数，也就是一行能放几个JLabel
			int columnCount = SYMBOL_PANEL_WIDTH / SYMBOLMARKER_AND_SYMBOLLINE_LABEL_SIZE;
			ArrayList<LabelInfo> arrayList = symbolsViewPanel.getLabelInfoArray();
			arrayList.clear();
			// 循环得到符号并将符号添加到Label，后将Label添加到JPanel
			for (int i = 0; i < systemSymbolIDs.length; i++) {
				// 这里减8主要是为了下面的文本显示
				BufferedImage bufferedImage = new BufferedImage(width, height - 8, BufferedImage.TYPE_INT_ARGB);
				GeoLine geoLine = (GeoLine) geometry;
				geoLine.getStyle().setLineSymbolID(systemSymbolIDs[i]);
				InternalToolkitControl.internalDraw(geoLine, symbolsViewPanel.getResources(), bufferedImage.getGraphics());

				String symbolName = "System " + systemSymbolIDs[i];
				// 第5个系统线符号是空的，画个NULL上去做标示
				if (systemSymbolIDs[i] == 5) {
					Graphics graphics = bufferedImage.getGraphics();
					Font font = new Font("Dialog", 0, 14);
					graphics.setFont(font);
					graphics.setColor(Color.blue);
					graphics.drawString("NULL", 16, 38);
				}
				JLabel label = symbolsViewPanel.getSymbolLabel(symbolName, bufferedImage);
				label.setName(String.valueOf(systemSymbolIDs[i]));

				if (i != 0 && i % (columnCount) == 0) {
					row++;
				}
				panel.setPreferredSize(new Dimension(SYMBOL_PANEL_WIDTH, (row + 1) * SYMBOLLINE_PANEL_HEIGHT_INCREMENT));
				panel.add(label);

				LabelInfo labelInfo = new LabelInfo(label, row, i % columnCount, systemSymbolIDs[i], symbolName);
				arrayList.add(labelInfo);
			}
			for (int i = systemSymbolIDs.length; i < count + systemSymbolIDs.length; i++) {
				Symbol symbol = symbols[i - systemSymbolIDs.length];
				BufferedImage bufferedImage = new BufferedImage(width, height - 8, BufferedImage.TYPE_INT_ARGB);

				SymbolLine line = (SymbolLine) symbol;
				GeoLine geoLine = (GeoLine) geometry;
				geoLine.getStyle().setLineSymbolID(line.getID());
				InternalSymbolLine.internalDraw((SymbolLine) symbol, bufferedImage.getGraphics(), geoLine);

				JLabel label = symbolsViewPanel.getSymbolLabel(symbol.getName(), bufferedImage);
				label.setName(String.valueOf(symbol.getID()));

				if (i != 0 && i % (columnCount) == 0) {
					row++;
				}
				panel.setPreferredSize(new Dimension(SYMBOL_PANEL_WIDTH, (row + 1) * SYMBOLLINE_PANEL_HEIGHT_INCREMENT));
				panel.add(label);

				LabelInfo labelInfo = new LabelInfo(label, row, i % columnCount, symbol.getID(), symbol.getName());
				arrayList.add(labelInfo);
			}

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			symbolsViewPanel.getSymbolBasicInfoPanel().refreshComboBox(symbolsViewPanel.getLabelInfoArray());
			panel.getParent().validate();
			panel.getParent().repaint();
			panel.validate();
			panel.repaint();
		}
	}

	/**
	 * 根据面搜索框提供的信息绘制面符号
	 * 
	 * @param width
	 * @param height
	 * @param geometry
	 * @param panel
	 * @param symbols 要绘制的符号数组
	 * @param systemSymbolIDs 要绘制的系统符号ID数组
	 */
	protected void paintSymbolsForFillSearch(int width, int height, GeoRegion geometry, JPanel panel, Symbol[] symbols, Integer[] systemSymbolIDs) {
		try {
			panel.removeAll();
			int count = symbols.length;
			int row = 0;

			// 列数，也就是一行能放几个JLabel
			int columnCount = SYMBOLFILL_PANEL_WIDTH / SYMBOLFILL_LABEL_SIZE;
			ArrayList<LabelInfo> arrayList = symbolsViewPanel.getLabelInfoArray();
			arrayList.clear();
			// 绘制系统符号
			for (int i = 0; i < systemSymbolIDs.length; i++) {
				// 面的系统符号有7个
				BufferedImage bufferedImage = new BufferedImage(width, height - 2, BufferedImage.TYPE_INT_ARGB);
				GeoRegion geoRegion = (GeoRegion) geometry;
				geoRegion.getStyle().setFillSymbolID(systemSymbolIDs[i]);
				InternalToolkitControl.internalDraw(geoRegion, symbolsViewPanel.getResources(), bufferedImage.getGraphics());

				String symbolName = "System " + systemSymbolIDs[i];
				// 第1个系统面符号是空的，画个NULL上去做标示
				if (systemSymbolIDs[i] == 1) {
					Graphics graphics = bufferedImage.getGraphics();
					Font font = new Font("Dialog", 0, 14);
					graphics.setFont(font);
					graphics.setColor(Color.blue);
					graphics.drawString("NULL", 25, 45);
				}
				JLabel label = symbolsViewPanel.getSymbolLabelForRegion(symbolName, bufferedImage);
				label.setName(String.valueOf(systemSymbolIDs[i]));

				if (i != 0 && i % (columnCount) == 0) {
					row++;
				}
				panel.setPreferredSize(new Dimension(SYMBOLFILL_PANEL_WIDTH, (row + 1) * SYMBOLREGION_PANEL_HEIGHT_INCREMENT));
				panel.add(label);

				LabelInfo labelInfo = new LabelInfo(label, row, i % columnCount, systemSymbolIDs[i], symbolName);
				arrayList.add(labelInfo);
			}
			for (int i = systemSymbolIDs.length; i < count + systemSymbolIDs.length; i++) {
				Symbol symbol = symbols[i - systemSymbolIDs.length];
				BufferedImage bufferedImage = new BufferedImage(width, height - 2, BufferedImage.TYPE_INT_ARGB);

				SymbolFill fill = (SymbolFill) symbol;
				GeoRegion geoRegion = (GeoRegion) geometry;
				geoRegion.getStyle().setFillSymbolID(fill.getID());
				InternalToolkitControl.internalDraw(geoRegion, symbolsViewPanel.getResources(), bufferedImage.getGraphics());

				JLabel label = symbolsViewPanel.getSymbolLabelForRegion(symbol.getName(), bufferedImage);
				label.setName(String.valueOf(symbol.getID()));

				if (i != 0 && i % (columnCount) == 0) {
					row++;
				}
				panel.setPreferredSize(new Dimension(SYMBOLFILL_PANEL_WIDTH, (row + 1) * SYMBOLREGION_PANEL_HEIGHT_INCREMENT));
				panel.add(label);

				LabelInfo labelInfo = new LabelInfo(label, row, i % columnCount, symbol.getID(), symbol.getName());
				arrayList.add(labelInfo);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			symbolsViewPanel.getSymbolBasicInfoPanel().refreshComboBox(symbolsViewPanel.getLabelInfoArray());
			panel.getParent().validate();
			panel.getParent().repaint();
			panel.validate();
			panel.repaint();
		}
	}

	/**
	 * 刷新浏览历史
	 * 
	 * @param text
	 */
	protected void refreshPrevSearches(String text) {
		if (!"".equals(text) && !prevSearches.contains(text)) {
			prevSearches.addFirst(text);
		}
		if (prevSearches.size() > 10) {
			prevSearches.removeLast();
		}
	}

	/**
	 * 浏览历史Action对象
	 * 
	 * @author hmily
	 *
	 */
	class PrevSearchAction extends AbstractAction {
		private String menuItemName;

		public PrevSearchAction(String s) {
			menuItemName = s;
			putValue(Action.NAME, menuItemName);
		}

		@Override
		public String toString() {
			return menuItemName;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			getTextFieldSymbolSearch().setText(menuItemName);
		}
	}

	class SetDocumentListener implements DocumentListener {

		@Override
		public void changedUpdate(DocumentEvent e) {
			// 默认实现，后续进行初始化操作
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			search();
			String text = jTextFieldSymbolSearch.getText();
			refreshPrevSearches(text);
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			search();
			String text = jTextFieldSymbolSearch.getText();
			refreshPrevSearches(text);
		}

	}
}
