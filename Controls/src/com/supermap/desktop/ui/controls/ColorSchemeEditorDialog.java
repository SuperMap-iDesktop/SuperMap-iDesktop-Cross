package com.supermap.desktop.ui.controls;

import com.supermap.data.Colors;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 自定义颜色编辑对话框
 * 
 * @author xuzw
 *
 */
class ColorSchemeEditorDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JLabel jLabelPreViewLabel;

	private SmButton jButtonMoveBottomButton;
	private SmButton jButtonMoveDownButton;
	private SmButton jButtonMoveUpButton;
	private SmButton jButtonMoveTopButton;
	private SmButton jButtonRemoveColorButton;
	private SmButton jButtonEditColorButton;
	private SmButton jButtonAddColorButton;
	private SmButton jButtonClearSelectionButton;
	private SmButton jButtonCancelButton;
	private SmButton jButtonConfirmButton;
	private SmButton jButtonSelectAllButton;

	private ColorsTableModel colorsTableModel;

	private JTable jTableColorsTable;

	private LinkedList<Color> colorLists;

	private JScrollPane jScrollPaneColors;

	private JPanel jPanelPreView;
	private JPanel jPanelColors;
	private JPanel jPanelSource;
	private JPanel jPanelCenter;

	private JToolBar toolBar;

	private transient DialogResult dialogResult = DialogResult.CANCEL;

	// 用户传入的Colors
	private transient Colors preColors;
	// 颜色列的序号是1
	private static final int COLORCOLUMNINDEX = 1;
	// 预览框中颜色分段数，当对话框大小为500时，预览面板的大小是484，分成121段比较合适
	private static final int COLORSCOUNT = 121;

	/**
	 * 构造函数
	 */
	public ColorSchemeEditorDialog() {
		super();
		setBounds(100, 100, 500, 350);
		setResizable(false);
	}

	/**
	 * 构造函数
	 */
	public ColorSchemeEditorDialog(Colors colors) {
		super();
		setBounds(100, 100, 500, 350);
		setModal(true);
		setResizable(false);
		preColors = new Colors(colors);
		colorLists = new LinkedList<>();
		for (int i = 0; i < colors.getCount(); i++) {
			colorLists.add(colors.get(i));
		}
		initialize();

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		try {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension frameSize = this.getSize();
			if (frameSize.height > screenSize.height) {
				frameSize.height = screenSize.height;
			}
			if (frameSize.width > screenSize.width) {
				frameSize.width = screenSize.width;
			}
			this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		// 启动后刷新一下预览框
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				refreshViewLabel();
			}
		});
	}

	/**
	 * 获取对话框显示结果
	 * 
	 * @return
	 */
	public DialogResult getResult() {
		return dialogResult;
	}

	/**
	 * 获取对话框结果颜色集
	 * 
	 * @return
	 */
	public Colors getResultColors() {
		if (dialogResult.equals(DialogResult.APPLY)) {
			Color[] temp = new Color[colorLists.size()];
			return new Colors(colorLists.toArray(temp));
		} else {
			return preColors;
		}
	}

	/**
	 * 初始化
	 */
	private void initialize() {
		setTitle(ControlsProperties.getString("String_ColorEditor"));
		getContentPane().add(getToolBar(), BorderLayout.NORTH);
		getContentPane().add(getCenterPanel(), BorderLayout.CENTER);
		getContentPane().add(getSourcePanel(), BorderLayout.SOUTH);
	}

	/**
	 * 工具条
	 * 
	 * @return
	 */
	protected JToolBar getToolBar() {
		if (toolBar == null) {
			toolBar = new JToolBar();
			toolBar.add(getSelectAllButton());
			toolBar.add(getClearSelectionButton());
			toolBar.add(getAddColorButton());
			toolBar.add(getEditColorButton());
			toolBar.add(getRemoveColorButton());
			toolBar.add(getMoveTopButton());
			toolBar.add(getMoveUpButton());
			toolBar.add(getMoveDownButton());
			toolBar.add(getMoveBottomButton());
		}
		return toolBar;
	}

	/**
	 * 全选按钮
	 * 
	 * @return
	 */
	protected JButton getSelectAllButton() {
		if (jButtonSelectAllButton == null) {
			ImageIcon icon = new ImageIcon();
			BufferedImage bufferedImage = new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(InternalImageIconFactory.SELECT_ALL.getImage(), 0, 0, null);
			icon.setImage(bufferedImage);

			jButtonSelectAllButton = new SmButton(icon);
			jButtonSelectAllButton.setToolTipText(ControlsProperties.getString("String_SelectAll"));
			jButtonSelectAllButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					jTableColorsTable.selectAll();
				}

			});
		}
		return jButtonSelectAllButton;
	}

	/**
	 * 中央面板，该面板包含了颜色集合表格和预览
	 * 
	 * @return
	 */
	protected JPanel getCenterPanel() {
		if (jPanelCenter == null) {
			jPanelCenter = new JPanel();
			jPanelCenter.setLayout(new BorderLayout());
			jPanelCenter.add(getColorsPanel(), BorderLayout.CENTER);
			jPanelCenter.add(getPreViewPanel(), BorderLayout.SOUTH);
		}
		return jPanelCenter;
	}

	/**
	 * 南部面板，包含确定和取消按钮
	 * 
	 * @return
	 */
	protected JPanel getSourcePanel() {
		if (jPanelSource == null) {
			jPanelSource = new JPanel();
			jPanelSource.add(getConfirmButton());
			jPanelSource.add(getCancelButton());
		}
		return jPanelSource;
	}

	/**
	 * 确定按钮
	 * 
	 * @return
	 */
	protected JButton getConfirmButton() {
		if (jButtonConfirmButton == null) {
			jButtonConfirmButton = new SmButton();
			jButtonConfirmButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dialogResult = DialogResult.APPLY;
					setVisible(false);
				}
			});
			jButtonConfirmButton.setText(CommonProperties.getString(CommonProperties.OK));
		}
		return jButtonConfirmButton;
	}

	/**
	 * 取消按钮
	 * 
	 * @return
	 */
	protected JButton getCancelButton() {
		if (jButtonCancelButton == null) {
			jButtonCancelButton = new SmButton();
			jButtonCancelButton.setText(CommonProperties.getString(CommonProperties.Cancel));
			jButtonCancelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dialogResult = DialogResult.CANCEL;
					setVisible(false);
				}

			});
		}
		return jButtonCancelButton;
	}

	/**
	 * 颜色集合面板
	 * 
	 * @return
	 */
	protected JPanel getColorsPanel() {
		if (jPanelColors == null) {
			jPanelColors = new JPanel();
			jPanelColors.setBorder(new TitledBorder(null, ControlsProperties.getString("String_Title_ColorCollections"), TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, null));
			jPanelColors.setLayout(new BorderLayout());
			jPanelColors.add(getColorsScrollPane(), BorderLayout.CENTER);
		}
		return jPanelColors;
	}

	/**
	 * 颜色集合预览面板
	 * 
	 * @return
	 */
	protected JPanel getPreViewPanel() {
		if (jPanelPreView == null) {
			jPanelPreView = new JPanel();
			jPanelPreView.setLayout(new BorderLayout());
			jPanelPreView.setBorder(new TitledBorder(null, ControlsProperties.getString("String_Title_Preview"), TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, null));
			jPanelPreView.add(getPreViewLabel(), BorderLayout.CENTER);
		}
		return jPanelPreView;
	}

	/**
	 * 滚动面板
	 * 
	 * @return
	 */
	protected JScrollPane getColorsScrollPane() {
		if (jScrollPaneColors == null) {
			jScrollPaneColors = new JScrollPane();
			jScrollPaneColors.setViewportView(getColorsTable());
		}
		return jScrollPaneColors;
	}

	/**
	 * 颜色集合表格
	 * 
	 * @return
	 */
	protected JTable getColorsTable() {
		if (jTableColorsTable == null) {
			jTableColorsTable = new JTable();
			jTableColorsTable.setRowHeight(25);
			colorsTableModel = new ColorsTableModel();
			jTableColorsTable.setModel(colorsTableModel);

			// 设置表格颜色列的显示效果为指定的颜色
			TableColumn colorColumn = jTableColorsTable.getColumn(ControlsProperties.getString("String_Color"));
			colorColumn.setCellRenderer(new DefaultTableCellRenderer() {
				@Override
				public void setValue(Object value) {
					if (value instanceof Color) {
						Color c = (Color) value;
						setBackground(c);
					} else {
						super.setValue(value);
					}
				}
			});
		}
		return jTableColorsTable;
	}

	/**
	 * 反选按钮
	 * 
	 * @return
	 */
	protected JButton getClearSelectionButton() {
		if (jButtonClearSelectionButton == null) {
			ImageIcon icon = new ImageIcon();
			BufferedImage bufferedImage = new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(InternalImageIconFactory.SELECT_PREVIOUS.getImage(), 0, 0, null);
			icon.setImage(bufferedImage);

			jButtonClearSelectionButton = new SmButton(icon);
			jButtonClearSelectionButton.setToolTipText(ControlsProperties.getString("String_SelectReverse"));
			jButtonClearSelectionButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					abstractClearColorButton();
				}

			});
		}
		return jButtonClearSelectionButton;
	}

	private void abstractClearColorButton() {
		int[] selectedRows = jTableColorsTable.getSelectedRows();
		ArrayList<Integer> selected = new ArrayList<Integer>();
		for (int i = 0; i < selectedRows.length; i++) {
			selected.add(selectedRows[i]);
		}
		int size = colorLists.size();
		ArrayList<Integer> all = new ArrayList<Integer>();
		for (int j = 0; j < size; j++) {
			all.add(j);
		}
		all.removeAll(selected);
		// 清空选择，然后将获得的反选列表加到选择中
		jTableColorsTable.clearSelection();
		for (int k = 0; k < all.size(); k++) {
			jTableColorsTable.addRowSelectionInterval(all.get(k), all.get(k));
		}
	}

	/**
	 * 添加按钮
	 * 
	 * @return
	 */
	protected JButton getAddColorButton() {
		if (jButtonAddColorButton == null) {
			ImageIcon icon = new ImageIcon();
			BufferedImage bufferedImage = new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(InternalImageIconFactory.COLOR_SCHEME_EDITOR_ADD_KEY_COLOR.getImage(), 0, 0, null);
			icon.setImage(bufferedImage);

			jButtonAddColorButton = new SmButton(icon);
			jButtonAddColorButton.setToolTipText(ControlsProperties.getString("String_Add"));
			jButtonAddColorButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Color color = JColorChooser.showDialog(null, ControlsProperties.getString("String_ChooseColor"), null);
					if (color != null) {
						colorLists.add(color);
						colorsTableModel.fireTableDataChanged();
						refreshViewLabel();

						// 将当前的表格选择位置放在新添加的列上
						jTableColorsTable.setRowSelectionInterval(colorLists.size() - 1, colorLists.size() - 1);
					}
				}
			});
		}
		return jButtonAddColorButton;
	}

	/**
	 * 编辑按钮
	 * 
	 * @return
	 */
	protected JButton getEditColorButton() {
		if (jButtonEditColorButton == null) {
			ImageIcon icon = new ImageIcon();
			BufferedImage bufferedImage = new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(InternalImageIconFactory.COLOR_SCHEME_EDITOR_EDIT_KEY_COLOER.getImage(), 0, 0, null);
			icon.setImage(bufferedImage);

			jButtonEditColorButton = new SmButton(icon);
			jButtonEditColorButton.setToolTipText(ControlsProperties.getString("String_Editor"));
			jButtonEditColorButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int row = jTableColorsTable.getSelectedRow();
					Object object = colorsTableModel.getValueAt(row, COLORCOLUMNINDEX);
					if (object != null && object instanceof Color) {
						Color color = JColorChooser.showDialog(null, ControlsProperties.getString("String_ChooseColor"), null);
						if (color != null) {
							colorLists.set(row, color);
							colorsTableModel.fireTableDataChanged();
							refreshViewLabel();

							// 将当前的表格选择位置放在编辑的列上
							jTableColorsTable.setRowSelectionInterval(row, row);
						}
					}
				}
			});
		}
		return jButtonEditColorButton;
	}

	/**
	 * 移除按钮
	 * 
	 * @return
	 */
	protected JButton getRemoveColorButton() {
		if (jButtonRemoveColorButton == null) {
			ImageIcon icon = new ImageIcon();
			BufferedImage bufferedImage = new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(InternalImageIconFactory.COLOR_SCHEME_EDITOR_REMOVE_KEY_COLOR.getImage(), 0, 0, null);
			icon.setImage(bufferedImage);

			jButtonRemoveColorButton = new SmButton(icon);
			jButtonRemoveColorButton.setToolTipText(ControlsProperties.getString("String_Remove"));
			jButtonRemoveColorButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					removeColorButton();
				}

			});
		}
		return jButtonRemoveColorButton;
	}

	private void removeColorButton() {
		int row = jTableColorsTable.getSelectedRow();
		int[] selectedRows = jTableColorsTable.getSelectedRows();
		int length = selectedRows.length;
		if (length > 0) {
			while (length > 0) {
				colorLists.remove(length - 1);
				length--;
			}
			colorsTableModel.fireTableDataChanged();
			refreshViewLabel();

			// 将当前的表格选择位置放在移除列的上一列上
			if (row - 1 != -1) {
				jTableColorsTable.setRowSelectionInterval(row - 1, row - 1);
			}
		}
	}

	/**
	 * 置顶按钮
	 * 
	 * @return
	 */
	protected JButton getMoveTopButton() {
		if (jButtonMoveTopButton == null) {
			ImageIcon icon = new ImageIcon();
			BufferedImage bufferedImage = new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(InternalImageIconFactory.COLOR_SCHEME_EDITOR_MOVE_FIRST.getImage(), 0, 0, null);
			icon.setImage(bufferedImage);

			jButtonMoveTopButton = new SmButton(icon);
			jButtonMoveTopButton.setToolTipText(ControlsProperties.getString("String_MoveFirst"));
			jButtonMoveTopButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (jTableColorsTable.getSelectedRow() == 0) {
						return;
					}
					Object object = colorsTableModel.getValueAt(jTableColorsTable.getSelectedRow(), COLORCOLUMNINDEX);
					if (object != null) {
						colorLists.remove(jTableColorsTable.getSelectedRow());
						colorLists.addFirst((Color) object);
						colorsTableModel.fireTableDataChanged();
						refreshViewLabel();

						jTableColorsTable.setRowSelectionInterval(0, 0);
					}
				}
			});
		}
		return jButtonMoveTopButton;
	}

	/**
	 * 上移按钮
	 * 
	 * @return
	 */
	protected JButton getMoveUpButton() {
		if (jButtonMoveUpButton == null) {
			ImageIcon icon = new ImageIcon();
			BufferedImage bufferedImage = new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(InternalImageIconFactory.COLOR_SCHEME_EDITOR_MOVE_UP.getImage(), 0, 0, null);
			icon.setImage(bufferedImage);

			jButtonMoveUpButton = new SmButton(icon);
			jButtonMoveUpButton.setToolTipText(ControlsProperties.getString("String_MoveUp"));
			jButtonMoveUpButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int row = jTableColorsTable.getSelectedRow();
					if (row == 0) {
						return;
					}
					Object object = colorsTableModel.getValueAt(row, COLORCOLUMNINDEX);
					Object object2 = colorsTableModel.getValueAt(row - 1, COLORCOLUMNINDEX);
					if (object != null && object2 != null) {
						colorLists.set(row - 1, (Color) object);
						colorLists.set(row, (Color) object2);
						colorsTableModel.fireTableDataChanged();
						refreshViewLabel();

						jTableColorsTable.setRowSelectionInterval(row - 1, row - 1);
					}
				}
			});
		}
		return jButtonMoveUpButton;
	}

	/**
	 * 下移按钮
	 * 
	 * @return
	 */
	protected JButton getMoveDownButton() {
		if (jButtonMoveDownButton == null) {
			ImageIcon icon = new ImageIcon();
			BufferedImage bufferedImage = new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(InternalImageIconFactory.COLOR_SCHEME_EDITOR_MOVE_DOWN.getImage(), 0, 0, null);
			icon.setImage(bufferedImage);

			jButtonMoveDownButton = new SmButton(icon);
			jButtonMoveDownButton.setToolTipText(ControlsProperties.getString("String_MoveDown"));
			jButtonMoveDownButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int row = jTableColorsTable.getSelectedRow();
					if (row == colorLists.size() - 1) {
						return;
					}
					Object object = colorsTableModel.getValueAt(row, COLORCOLUMNINDEX);
					Object object2 = colorsTableModel.getValueAt(row + 1, COLORCOLUMNINDEX);
					if (object != null && object2 != null) {
						colorLists.set(row + 1, (Color) object);
						colorLists.set(row, (Color) object2);
						colorsTableModel.fireTableDataChanged();
						refreshViewLabel();

						jTableColorsTable.setRowSelectionInterval(row + 1, row + 1);
					}
				}
			});
		}
		return jButtonMoveDownButton;
	}

	/**
	 * 置底
	 * 
	 * @return
	 */
	protected JButton getMoveBottomButton() {
		if (jButtonMoveBottomButton == null) {
			ImageIcon icon = new ImageIcon();
			BufferedImage bufferedImage = new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(InternalImageIconFactory.COLOR_SCHEME_EDITOR_MOVE_LAST.getImage(), 0, 0, null);
			icon.setImage(bufferedImage);

			jButtonMoveBottomButton = new SmButton(icon);
			jButtonMoveBottomButton.setToolTipText(ControlsProperties.getString("String_MoveLast"));
			jButtonMoveBottomButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (jTableColorsTable.getSelectedRow() == colorLists.size() - 1) {
						return;
					}
					Object object = colorsTableModel.getValueAt(jTableColorsTable.getSelectedRow(), COLORCOLUMNINDEX);
					if (object != null) {
						colorLists.remove(jTableColorsTable.getSelectedRow());
						colorLists.addLast((Color) object);
						colorsTableModel.fireTableDataChanged();
						refreshViewLabel();

						jTableColorsTable.setRowSelectionInterval(colorLists.size() - 1, colorLists.size() - 1);
					}
				}
			});
		}
		return jButtonMoveBottomButton;
	}

	/**
	 * 预览标签
	 *
	 * @return 预览标签
	 */
	protected JLabel getPreViewLabel() {
		if (jLabelPreViewLabel == null) {
			jLabelPreViewLabel = new JLabel();
			jLabelPreViewLabel.setPreferredSize(new Dimension(10, 35));
		}
		return jLabelPreViewLabel;
	}

	/**
	 * 刷新预览标签
	 */
	protected void refreshViewLabel() {
		int imageWidth = jLabelPreViewLabel.getSize().width;
		int imageHeight = jLabelPreViewLabel.getSize().height;
		BufferedImage bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();

		// 根据当前渲染单元格的宽度和颜色数计算出每个颜色应当渲染的步长
		int size = colorLists.size();
		Color[] temp = new Color[size];
		if (size == 0) {
			graphics.setColor(Color.white);
			graphics.fillRect(0, 0, imageWidth, imageHeight);
		} else if (size == 1) {
			graphics.setColor(colorLists.get(0));
			graphics.fillRect(0, 0, imageWidth, imageHeight);
		} else {
			Colors colors = Colors.makeGradient(COLORSCOUNT, colorLists.toArray(temp));
			int colorsCount = colors.getCount();
			int step = imageWidth / colorsCount;
			for (int i = 0; i < colorsCount; i++) {
				graphics.setColor(colors.get(i));
				graphics.fillRect(step * i, 0, step * (i + 1), imageHeight);
			}
		}
		jLabelPreViewLabel.setIcon(new ImageIcon(bufferedImage));
	}

	/**
	 * 颜色表格模型
	 */
	class ColorsTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;
		private final String[] m_columns = new String[] { ControlsProperties.getString("String_identifier"), ControlsProperties.getString("String_Color") };

		public ColorsTableModel() {
			// 默认实现，后续进行初始化
		}

		@Override
		public int getRowCount() {
			return colorLists.size();
		}

		@Override
		public int getColumnCount() {
			return m_columns.length;
		}

		@Override
		public String getColumnName(int column) {
			return m_columns[column];
		}

		@Override
		public Object getValueAt(int row, int column) {
			Object result = null;
			if (row >= 0 && row < colorLists.size()) {
				if (column == 0) {
					result = row + 1;
				} else {
					result = colorLists.get(row);
				}
			}
			return result;
		}
	}

}
