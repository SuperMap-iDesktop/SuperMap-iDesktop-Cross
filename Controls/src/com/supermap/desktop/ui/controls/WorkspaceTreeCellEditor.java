package com.supermap.desktop.ui.controls;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormLayout;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilties.SceneUtilties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilties.DatasetUtilities;
import com.supermap.desktop.utilties.LayoutUtilities;
import com.supermap.desktop.utilties.MapUtilities;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.MessageFormat;
import java.util.EventObject;

/**
 * 工作空间树单元格编辑器
 *
 * @author xuzw
 */
class WorkspaceTreeCellEditor extends DefaultTreeCellEditor {
	private boolean isCommitting = false;
	private TreePath lastEditingPath;
	private String stringTextField = null;

	private Workspace currentWorkspace = null;

	public WorkspaceTreeCellEditor(JTree tree, WorkspaceTreeCellRenderer renderer, Workspace workspace) {
		super(tree, renderer);
		currentWorkspace = workspace;
	}

	@Override
	protected void determineOffset(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {

		// 计算基类的图片偏移量
		drawEditingIcon(tree.getCellRenderer().getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, expanded));
		if (editingIcon != null) {
			offset = renderer.getIconTextGap() + editingIcon.getIconWidth();
		} else {
			offset = renderer.getIconTextGap();
		}

	}

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {

		lastEditingPath = tree.getSelectionPath();
		// 给相关父类成员赋值
		setTree(tree);
		lastRow = row;

		// 计算offset
		determineOffset(tree, value, isSelected, expanded, leaf, row);

		// 清除editing
		if (null != editingComponent) {
			editingContainer.remove(editingComponent);
		}

		// 有editor返回编译使用的Component
		editingComponent = realEditor.getTreeCellEditorComponent(tree, stringTextField, isSelected, expanded, leaf, row);

		// 计算路径
		TreePath newPath = tree.getPathForRow(row);

		canEdit = lastPath != null && newPath != null && lastPath.equals(newPath);

		// 赋值
		Font font = getEditFont(tree);

		editingContainer.setFont(font);
		prepareForEditing();

		return editingContainer;
	}

	private Font getEditFont(JTree tree) {
		Font font = getFont();

		if (null == font) {

			if (null != renderer) {
				font = renderer.getFont();
			}

			if (null == font) {
				font = tree.getFont();
			}
		}
		return font;
	}

	@Override
	public boolean stopCellEditing() {
		TreePath treeSelectionPath = lastEditingPath;
		Object lastComponent = treeSelectionPath.getLastPathComponent();
		DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) lastComponent;
		TreeNodeData tempNodeData = (TreeNodeData) tempNode.getUserObject();
		try {
			Object data = tempNodeData.getData();
			if (data instanceof Datasource) {
				// 数据源重命名
				Datasource datasource = (Datasource) data;
				String message = null;
				// 数据源已存在
				if (datasource.getAlias().equals(stringTextField)) {
					// 我知道你点错了！
				} else if (null != Application.getActiveApplication().getWorkspace().getDatasources().get(stringTextField)) {
					message = MessageFormat.format(ControlsProperties.getString("String_RenameDatasourceFailed"), stringTextField);
					Application.getActiveApplication().getOutput().output(message);
				} else {
					int dialogResult = UICommonToolkit.showConfirmDialog(ControlsProperties.getString("String_RenameDatasourceInfo"));
					if (JOptionPane.OK_OPTION == dialogResult) {
						message = MessageFormat.format(ControlsProperties.getString("String_RenameDatasourceSuccess"), datasource.getAlias(), stringTextField);
						currentWorkspace.getDatasources().modifyAlias(datasource.getAlias(), stringTextField);
						Application.getActiveApplication().getOutput().output(message);
					}
				}

			} else if (data instanceof Dataset) {
				// 数据集重命名
				Dataset dataset = (Dataset) data;
				if (dataset.getDatasource().isReadOnly() || dataset.getName().equals(stringTextField)) {
					// do nothings
				} else if (!dataset.getDatasource().getDatasets().isAvailableDatasetName(stringTextField)) {
					Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_IllegalDatasetName"));
				} else {
					boolean isClosed = true;
					if (DatasetUtilities.isDatasetOpened(dataset)) {
						// show dialog
						int dialogResult = UICommonToolkit.showConfirmDialog(MessageFormat.format(
								ControlsProperties.getString("String_DatasetOpenWhileRename"), dataset.getName()));
						if (dialogResult == JOptionPane.OK_OPTION) {
							DatasetUtilities.closeDataset(dataset);
						} else {
							isClosed = false;
						}
					}
					if (isClosed) {
						dataset.getDatasource().getDatasets().rename(dataset.getName(), stringTextField);
						Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_RenameDatasetSuccess"));
					}
				}
			} else {
				NodeDataType type = tempNodeData.getType();
				if (type.equals(NodeDataType.LAYOUT_NAME)) {
					// 布局重命名
					String layoutName = (String) data;
					if (layoutName.equals(stringTextField)) {
						// 点错了！
					} else if (LayoutUtilities.checkAvailableLayoutName(stringTextField, layoutName)) {
						IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
						for (int i = 0; i < formManager.getCount(); i++) {
							if (formManager.get(i) instanceof IFormLayout && formManager.get(i).getText().equals(layoutName)) {
								formManager.get(i).setText(stringTextField);
							}
						}
						currentWorkspace.getLayouts().rename(layoutName, stringTextField);
						String message = MessageFormat.format(ControlsProperties.getString("String_RenameLayoutSuccess"), layoutName, stringTextField);
						Application.getActiveApplication().getOutput().output(message);
					} else {
						String message = MessageFormat.format(ControlsProperties.getString("String_RenameLayoutFailed"), stringTextField);
						Application.getActiveApplication().getOutput().output(message);
					}

				} else if (type.equals(NodeDataType.MAP_NAME)) {
					// 地图重命名
					String mapName = (String) data;
					if (mapName.equals(stringTextField)) {
						// 点错了！
					} else if (MapUtilities.checkAvailableMapName(stringTextField, mapName)) {
						IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
						for (int i = 0; i < formManager.getCount(); i++) {
							if (formManager.get(i) instanceof IFormMap && formManager.get(i).getText().equals(mapName)) {
								formManager.get(i).setText(stringTextField);
							}
						}
						currentWorkspace.getMaps().rename(mapName, stringTextField);
						String message = MessageFormat.format(ControlsProperties.getString("String_RenameMapSuccess"), mapName, stringTextField);
						Application.getActiveApplication().getOutput().output(message);
					} else {
						String message = MessageFormat.format(ControlsProperties.getString("String_RenameMapFailed"), stringTextField);
						Application.getActiveApplication().getOutput().output(message);
					}
				} else if (type.equals(NodeDataType.SCENE_NAME)) {
					// 场景重命名
					String sceneName = (String) data;
					if (sceneName.equals(stringTextField)) {
						// 点错了
					} else if (SceneUtilties.checkAvailableSceneName(stringTextField, sceneName)) {
						IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
						for (int i = 0; i < formManager.getCount(); i++) {
							if (formManager.get(i) instanceof IFormScene && formManager.get(i).getText().equals(sceneName)) {
								formManager.get(i).setText(stringTextField);
							}
						}
						currentWorkspace.getScenes().rename(sceneName, stringTextField);
						String message = MessageFormat.format(ControlsProperties.getString("String_RenameSceneSuccess"), sceneName, stringTextField);
						Application.getActiveApplication().getOutput().output(message);
					} else {
						String message = MessageFormat.format(ControlsProperties.getString("String_RenameSceneFailed"), stringTextField);
						Application.getActiveApplication().getOutput().output(message);
					}

				} else {
					cancelCellEditing();
				}
			}
		} catch (RuntimeException e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			cancelCellEditing();
		}

		if (realEditor.stopCellEditing()) {
			cleanupAfterEditing();
			return true;
		}
		return false;
	}

	/**
	 * Messages <code>cancelCellEditing</code> to the <code>realEditor</code> and removes it from this instance.
	 */
	@Override
	public void cancelCellEditing() {
		realEditor.cancelCellEditing();
		cleanupAfterEditing();
	}

	/**
	 * Returns the value currently being edited.
	 *
	 * @return the value currently being edited
	 */
	@Override
	public Object getCellEditorValue() {

		return realEditor.getCellEditorValue();
	}

	/**
	 * This is invoked if a <code>TreeCellEditor</code> is not supplied in the constructor. It returns a <code>TextField</code> editor.
	 *
	 * @return a new <code>TextField</code> editor
	 */
	@SuppressWarnings("serial")
	@Override
	protected TreeCellEditor createTreeCellEditor() {

		Border aBorder = UIManager.getBorder("Tree.editorBorder");
		DefaultTextField defaultTextField = new DefaultTextField(aBorder);
		defaultTextField.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				abstraDefaultTextFieldLisenter(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {/* Nonthing to do! */
			}

			@Override
			public void keyTyped(KeyEvent e) {/* Nonthing to do! */
			}
		});
		defaultTextField.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				abstractDefaultTextFieldLisenter();
			}

			@Override
			public void focusGained(FocusEvent e) {
				// do nothing
			}
		});
		DefaultCellEditor editor = new DefaultCellEditor(defaultTextField) {
			@Override
			public boolean shouldSelectCell(EventObject event) {
				boolean retValue = super.shouldSelectCell(event);
				return retValue;
			}
		};
		// One click to Edit.
		editor.setClickCountToStart(1);
		return editor;
	}

	private void abstraDefaultTextFieldLisenter(KeyEvent e) {
		int keyCode = e.getKeyChar();
		if (keyCode == KeyEvent.VK_ENTER) {
			stopEditing();
		}
	}

	private void abstractDefaultTextFieldLisenter() {
		stopEditing();
	}

	/**
	 * 结束编辑，并且刷新显示
	 */
	private synchronized void stopEditing() {
		try {
			if (!this.isCommitting) {
				this.isCommitting = true;
				Object obj = realEditor.getCellEditorValue();
				stringTextField = obj.toString();

				fireStopCellEditing();
				tree.updateUI();
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			this.isCommitting = false;
		}
	}

	private void cleanupAfterEditing() {
		if (editingComponent != null) {
			editingContainer.remove(editingComponent);
		}
		editingComponent = null;
	}

	private void fireStopCellEditing() {
		stopCellEditing();
	}

	private void drawEditingIcon(Object value) {
		if (value instanceof JLabel) {
			editingIcon = ((JLabel) value).getIcon();
			stringTextField = ((JLabel) value).getText();
		} else if (value instanceof JPanel) {
			for (int i = 0; i < ((JPanel) value).getComponentCount(); i++) {
				if (((JPanel) value).getComponent(i) instanceof JLabel) {
					stringTextField = ((JLabel) ((JPanel) value).getComponent(i)).getText();
					if (((JLabel) ((JPanel) value).getComponent(i)).getIcon() != null) {
						editingIcon = ((JLabel) ((JPanel) value).getComponent(i)).getIcon();
						return;
					}
				}
			}
		}
		/*
		 * DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) value; TreeNodeData tempNodeData = (TreeNodeData)
		 * defaultMutableTreeNode.getUserObject(); Object data = tempNodeData.getData(); if (data instanceof Workspace) { // editingIcon =
		 * InternalImageIconFactory.WORKSPACE; if ("UntitledWorkspace".equals(currentWorkspace.getCaption())) { stringTextField =
		 * ControlsProperties.getString(ControlsProperties.WorkspaceNodeDefaultName); } else { stringTextField = currentWorkspace.getCaption(); } } else if
		 * (data instanceof Datasources) { // editingIcon = InternalImageIconFactory.DATASOURCES; stringTextField =
		 * ControlsProperties.getString(ControlsProperties.DatasourcesNodeName); } else if (data instanceof Datasource) { Datasource tempDatasource =
		 * (Datasource) tempNodeData.getData(); EngineType engineType = tempDatasource.getEngineType(); if (engineType.equals(EngineType.SQLPLUS)) { //
		 * editingIcon = InternalImageIconFactory.DATASOURCE_SQL; } else if (engineType.equals(EngineType.IMAGEPLUGINS)) { // editingIcon =
		 * InternalImageIconFactory.DATASOURCE_IMAGEPLUGINS; } else if (engineType.equals(EngineType.OGC)) { // editingIcon =
		 * InternalImageIconFactory.DATASOURCE_OGC; } else if (engineType.equals(EngineType.ORACLEPLUS)) { // editingIcon =
		 * InternalImageIconFactory.DATASOURCE_ORACLE; } else if (engineType.equals(EngineType.UDB)) { // editingIcon = InternalImageIconFactory.DATASOURCE_UDB;
		 * } stringTextField = tempDatasource.getAlias(); } else if (data instanceof Dataset) { Dataset tempDataset = (Dataset) tempNodeData.getData();
		 * DatasetType instanceDatasetType = tempDataset.getType(); if (instanceDatasetType.equals(DatasetType.POINT)) { // editingIcon =
		 * InternalImageIconFactory.DT_POINT; stringTextField = tempDataset.getName(); } else if (instanceDatasetType.equals(DatasetType.LINE)) { // editingIcon
		 * = InternalImageIconFactory.DT_LINE; stringTextField = tempDataset.getName(); } else if (instanceDatasetType.equals(DatasetType.LINEM)) { //
		 * editingIcon = InternalImageIconFactory.DT_LINEM; stringTextField = tempDataset.getName(); } else if (instanceDatasetType.equals(DatasetType.REGION))
		 * { // editingIcon = InternalImageIconFactory.DT_REGION; stringTextField = tempDataset.getName(); } else if
		 * (instanceDatasetType.equals(DatasetType.CAD)) { // editingIcon = InternalImageIconFactory.DT_CAD; stringTextField = tempDataset.getName(); } else if
		 * (instanceDatasetType.equals(DatasetType.GRID)) { // editingIcon = InternalImageIconFactory.DT_GRID; stringTextField = tempDataset.getName(); } else
		 * if (instanceDatasetType.equals(DatasetType.IMAGE)) { // editingIcon = InternalImageIconFactory.DT_IMAGE; stringTextField = tempDataset.getName(); }
		 * else if (instanceDatasetType.equals(DatasetType.LINKTABLE)) { // editingIcon = InternalImageIconFactory.DT_LINKTABLE; stringTextField =
		 * tempDataset.getName(); } else if (instanceDatasetType.equals(DatasetType.NETWORK)) { // editingIcon = InternalImageIconFactory.DT_NETWORK;
		 * stringTextField = tempDataset.getName(); } else if (instanceDatasetType.equals(DatasetType.TABULAR)) { // editingIcon =
		 * InternalImageIconFactory.DT_TABULAR; stringTextField = tempDataset.getName(); } else if (instanceDatasetType.equals(DatasetType.TEXT)) { //
		 * editingIcon = InternalImageIconFactory.DT_TEXT; stringTextField = tempDataset.getName(); } else if (instanceDatasetType.equals(DatasetType.TOPOLOGY))
		 * { // editingIcon = InternalImageIconFactory.DT_TOPOLOGY; stringTextField = tempDataset.getName(); } else if
		 * (instanceDatasetType.equals(DatasetType.WCS)) { // editingIcon = InternalImageIconFactory.DT_WCS; stringTextField = tempDataset.getName(); } else if
		 * (instanceDatasetType.equals(DatasetType.WMS)) { // editingIcon = InternalImageIconFactory.DT_WMS; stringTextField = tempDataset.getName(); } } else
		 * if (data instanceof Maps) { // editingIcon = InternalImageIconFactory.MAPS; stringTextField =
		 * ControlsProperties.getString(ControlsProperties.MapsNodeName); } else if (tempNodeData.getType().equals(NodeDataType.MAP_NAME)) { // editingIcon =
		 * InternalImageIconFactory.MAP; stringTextField = tempNodeData.getData().toString().trim(); } else if (data instanceof Scenes) { // editingIcon =
		 * InternalImageIconFactory.SCENES; stringTextField = ControlsProperties.getString(ControlsProperties.ScenesNodeName); } else if
		 * (tempNodeData.getType().equals(NodeDataType.SCENE_NAME)) { // editingIcon = InternalImageIconFactory.SCENE; stringTextField =
		 * tempNodeData.getData().toString().trim(); } else if (data instanceof Layouts) { // editingIcon = InternalImageIconFactory.LAYOUTS; stringTextField =
		 * ControlsProperties.getString(ControlsProperties.LayoutsNodeName); } else if (tempNodeData.getType().equals(NodeDataType.LAYOUT_NAME)) { //
		 * editingIcon = InternalImageIconFactory.LAYOUT; stringTextField = tempNodeData.getData().toString().trim(); } else if (data instanceof Resources) { //
		 * editingIcon = InternalImageIconFactory.RESOURCES; stringTextField = ControlsProperties.getString(ControlsProperties.ResourcesNodeName); } else if
		 * (data instanceof SymbolMarkerLibrary) { // editingIcon = InternalImageIconFactory.SYMBOLMARKERLIB; stringTextField =
		 * ControlsProperties.getString(ControlsProperties.SymbolMarkerLibNodeName); } else if (data instanceof SymbolLineLibrary) { // editingIcon =
		 * InternalImageIconFactory.SYMBOLLINELIB; stringTextField = ControlsProperties.getString(ControlsProperties.SymbolLineLibNodeName); } else if (data
		 * instanceof SymbolFillLibrary) { // editingIcon = InternalImageIconFactory.SYMBOLFillLIB; stringTextField =
		 * ControlsProperties.getString(ControlsProperties.SymbolFillLibNodeName); }
		 */
	}
}
