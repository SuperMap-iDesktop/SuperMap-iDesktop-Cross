package com.supermap.desktop.iml;

import com.supermap.data.*;
import com.supermap.data.conversion.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.importUI.DataImportDialog;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.tableModel.ImportTableModel;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.DatasourceUtilities;
import com.supermap.desktop.utilities.JOptionPaneUtilities;
import com.supermap.desktop.utilities.MapUtilities;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.CancellationException;

/**
 * Created by xie on 2016/10/18.
 * 导入具体实现类，进度条实现
 */
public class ImportCallable extends UpdateProgressCallable {
    private ArrayList<ImportInfo> fileInfos;
    private JTable table;
    private ImportSetting importSetting;
    private DataImportDialog dataImportDialog;
    private SpatialIndexType[] spatialIndexTypes = {SpatialIndexType.MULTI_LEVEL_GRID, SpatialIndexType.QTREE, SpatialIndexType.RTREE, SpatialIndexType.TILE};

    public ImportCallable(List<ImportInfo> fileInfos, DataImportDialog dataImportDialog) {
        this.fileInfos = (ArrayList<ImportInfo>) fileInfos;
        this.dataImportDialog = dataImportDialog;
        this.table = dataImportDialog.getTable();
    }

    @Override
    public Boolean call() {
        final HashMap<String, Integer> map = new HashMap<String, Integer>();
        Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
        for (int i = 0; i < datasources.getCount(); i++) {
            map.put(datasources.get(i).getAlias(), 0);
        }
        // 不用了先置空回收对象
        datasources = null;
        try {
            for (int i = 0; i < fileInfos.size(); i++) {
                DataImport dataImport = new DataImport();
                ImportSettings importSettings = dataImport.getImportSettings();
                importSetting = fileInfos.get(i).getImportSetting();
                String datasetName = importSetting.getTargetDatasetName();
                Dataset dataset = DatasourceUtilities.getDataset(datasetName, importSetting.getTargetDatasource());
                if (importSetting.getImportMode().equals(ImportMode.OVERWRITE) && DatasetUtilities.isDatasetOpened(dataset)) {
                    if (JOptionPane.OK_OPTION == JOptionPaneUtilities.showConfirmDialog(DataConversionProperties.getString("String_FormImport_MessageBoxOverWrite"))) {
                        IForm form = MapUtilities.getFormMap(dataset);
                        if (form instanceof IFormMap) {
                            DatasetUtilities.removeByDataset(((IFormMap) form).getMapControl().getMap().getLayers(), dataset);
                            ((IFormMap) form).getMapControl().getMap().refresh();
                        }
                    }
                } else {
                    doImport(importSettings, i, dataImport, map);
                    if (!dataImportDialog.isVisible()) {
                        importSetting.dispose();
                    }
                    dataImport.dispose();
                }
            }
        } catch (Exception e2) {
            Application.getActiveApplication().getOutput().output(e2);
        } finally {
            // fixme UGDJ-244
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    for (Map.Entry<String, Integer> entry : map.entrySet()) {
                        if (entry.getValue() > 0) {
                            UICommonToolkit.refreshSelectedDatasourceNode(entry.getKey());
                        }
                    }
                }
            });
            if (importSetting instanceof ImportSettingWOR) {
                // 刷新地图节点
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
                        DefaultTreeModel treeModel = (DefaultTreeModel) workspaceTree.getModel();
                        MutableTreeNode treeNode = (MutableTreeNode) treeModel.getRoot();
                        UICommonToolkit.getWorkspaceManager().getWorkspaceTree().refreshNode((DefaultMutableTreeNode) treeNode.getChildAt(1));
                    }
                });
            }
        }
        return true;
    }

    private void doImport(ImportSettings importSettings, int i, DataImport dataImport, HashMap<String, Integer> map) {
        importSettings.add(importSetting);
        PercentProgress percentProgress = new PercentProgress(i);
        dataImport.addImportSteppedListener(percentProgress);
        long startTime = System.currentTimeMillis(); // 获取开始时间
        ImportResult result = dataImport.run();
        if (null != result.getSucceedSettings() && result.getSucceedSettings().length > 0) {
            map.put(result.getSucceedSettings()[0].getTargetDatasource().getAlias(),
                    map.get(result.getSucceedSettings()[0].getTargetDatasource().getAlias()) + 1);
        }
        long endTime = System.currentTimeMillis(); // 获取结束时间
        long time = endTime - startTime;
        printMessage(result, i, time);
        // 更新行
        ((ImportTableModel) table.getModel()).updateRows(fileInfos);
        if (null != percentProgress && percentProgress.isCancel()) {
            return;
        }
        dataImport.removeImportSteppedListener(percentProgress);
    }

    /**
     * 进度事件得到运行时间
     *
     * @author Administrator
     */
    class PercentProgress implements ImportSteppedListener {
        private int i;
        private boolean isCancel = false;

        public PercentProgress(int i) {
            this.i = i;
        }

        public boolean isCancel() {
            return this.isCancel;
        }

        @Override
        public void stepped(ImportSteppedEvent arg0) {
            try {
                double count = fileInfos.size();
                int totalPercent = (int) ((100 * this.i + arg0.getSubPercent()) / count);
                updateProgressTotal(arg0.getSubPercent(),
                        MessageFormat.format(CommonProperties.getString("String_TotalTaskNumber"), String.valueOf(fileInfos.size())), totalPercent,
                        MessageFormat.format(DataConversionProperties.getString("String_FileInport"), arg0.getCurrentTask().getSourceFilePath()));
            } catch (CancellationException e) {
                arg0.setCancel(true);
                this.isCancel = true;
            }
        }
    }

    /**
     * 打印导入信息
     *
     * @param result
     * @param i
     */

    private void printMessage(ImportResult result, int i, long time) {
        ImportSetting[] successImportSettings = result.getSucceedSettings();
        ImportSetting[] failImportSettings = result.getFailedSettings();
        String successImportInfo = DataConversionProperties.getString("String_FormImport_OutPutInfoOne");
        String failImportInfo = DataConversionProperties.getString("String_FormImport_OutPutInfoTwo");
        if (null != successImportSettings && 0 < successImportSettings.length) {
            String[] names = result.getSucceedDatasetNames(successImportSettings[0]);
            // 创建空间索引，字段索引
            fileInfos.get(i).setState(DataConversionProperties.getString("String_FormImport_Succeed"));
            // 导入成功提示信息
            ImportSetting sucessSetting = successImportSettings[0];
            if (null != names && names.length > 0) {
                for (int j = 0; j < names.length; j++) {
                    String targetDatasetName = names[j];
                    Dataset dataset = sucessSetting.getTargetDatasource().getDatasets().get(targetDatasetName);
                    boolean isBuildSpatialIndex = fileInfos.get(i).getSpatialIndex();
                    boolean isBuildFiledIndex = fileInfos.get(i).getFieldIndex();
                    if (dataset instanceof DatasetVector) {
                        if (isBuildFiledIndex) {
                            int count = ((DatasetVector) dataset).getFieldInfos().getCount();
                            for (int k = 0; k < count; k++) {
                                if (((DatasetVector) dataset).getFieldInfos().get(k) instanceof FieldInfo) {
                                    String fieldName = ((DatasetVector) dataset).getFieldInfos().get(k).getName();
                                    String uuidStr = UUID.randomUUID().toString();
                                    String fieldIndex = uuidStr.substring(0, 8) + uuidStr.substring(9, 13) + uuidStr.substring(14, 18) + uuidStr.substring(19, 23) + uuidStr.substring(24);
                                    String indexName = MessageFormat.format("{0}_{1}", fieldName, fieldIndex);
                                    if (indexName.length() > 30) {
                                        indexName = indexName.substring(0, 30);
                                    }
                                    ((DatasetVector) dataset).buildFieldIndex(new String[]{fieldName}, indexName);
                                }
                            }

                        }
                        if (isBuildSpatialIndex && sucessSetting.getSourceFileType() != FileType.DBF) {
                            SpatialIndexType spatialIndexType = getSupportSpatialIndexType((DatasetVector) dataset);
                            if (null != spatialIndexType) {
                                ((DatasetVector) dataset).buildSpatialIndex(spatialIndexType);
                            }
                        }
                    }
                    Application.getActiveApplication().getOutput().output(MessageFormat.format(successImportInfo, sucessSetting.getSourceFilePath(), "->", targetDatasetName, sucessSetting
                            .getTargetDatasource().getAlias(), String.valueOf((time / names.length) / 1000.0)));
                }
            }

        } else if (null != failImportSettings && 0 < failImportSettings.length) {
            fileInfos.get(i).setState(DataConversionProperties.getString("String_FormImport_NotSucceed"));
            Application.getActiveApplication().getOutput().output(MessageFormat.format(failImportInfo, failImportSettings[0].getSourceFilePath(), "->", ""));
        }
    }

    private SpatialIndexType getSupportSpatialIndexType(DatasetVector dataset) {
        SpatialIndexType result = null;
        for (SpatialIndexType tempType : spatialIndexTypes) {
            if (dataset.isSpatialIndexTypeSupported(tempType)) {
                result = tempType;
                break;
            }
        }
        return result;
    }

}

