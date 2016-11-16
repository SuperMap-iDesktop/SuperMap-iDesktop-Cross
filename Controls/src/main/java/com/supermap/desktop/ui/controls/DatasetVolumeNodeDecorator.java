package com.supermap.desktop.ui.controls;

import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVolume;
import com.supermap.desktop.CommonToolkit;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * 体数据集节点装饰器
 * Created by xie on 2016/11/15.
 */
public class DatasetVolumeNodeDecorator implements TreeNodeDecorator {
    @Override
    public void decorate(JLabel label, TreeNodeData data) {
        if (data.getType().equals(NodeDataType.DATASET_VOLUME)) {
            DatasetVolume datasetGrid = (DatasetVolume) data.getData();
            label.setText(datasetGrid.getName());
            ImageIcon icon = (ImageIcon) label.getIcon();
            BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH, IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = bufferedImage.getGraphics();
            DatasetType type = datasetGrid.getType();
            String path = CommonToolkit.DatasetImageWrap.getImageIconPath(type);
            URL url = DatasetVectorNodeDecorator.class.getResource(path);
            try {
                graphics.drawImage(new ImageIcon(url).getImage(), 0, 0, label);
            } catch (Exception e) {
                e.printStackTrace();
            }

            icon.setImage(bufferedImage);
        }
    }
}
