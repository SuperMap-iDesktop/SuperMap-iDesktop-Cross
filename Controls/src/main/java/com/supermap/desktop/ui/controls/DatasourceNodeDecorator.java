package com.supermap.desktop.ui.controls;

import com.supermap.data.Datasource;
import com.supermap.data.EngineType;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 数据源节点装饰器
 * @author xuzw
 *
 */
class DatasourceNodeDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if(data.getType().equals(NodeDataType.DATASOURCE)){
			Datasource datasource = (Datasource) data.getData();
			label.setText(datasource.getAlias());
			ImageIcon icon = (ImageIcon) label.getIcon();
			EngineType engineType = datasource.getEngineType();
			BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH,
					IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			if(engineType.equals(EngineType.UDB)){
				graphics.drawImage(
						InternalImageIconFactory.DATASOURCE_UDB.getImage(), 0, 0, label);
			}else if(engineType.equals(EngineType.ORACLEPLUS)){
				graphics.drawImage(
						InternalImageIconFactory.DATASOURCE_ORACLE.getImage(), 0, 0, label);
			}else if(engineType.equals(EngineType.IMAGEPLUGINS)){
				graphics.drawImage(
						InternalImageIconFactory.DATASOURCE_IMAGEPLUGINS.getImage(), 0, 0, label);
			}else if(engineType.equals(EngineType.OGC)){
				graphics.drawImage(
						InternalImageIconFactory.DATASOURCE_OGC.getImage(), 0, 0, label);
			}else if(engineType.equals(EngineType.SQLPLUS)){
				graphics.drawImage(
						InternalImageIconFactory.DATASOURCE_SQL.getImage(), 0, 0, label);
			}
			else if(engineType.equals(EngineType.GOOGLEMAPS)){
				graphics.drawImage(
						InternalImageIconFactory.DATASOURCE_GOOGLEMAPS.getImage(), 0, 0, label);
			}
			else if(engineType.equals(EngineType.DB2)){
				graphics.drawImage(
						InternalImageIconFactory.DATASOURCE_DB2.getImage(), 0, 0, label);
			}
			else if(engineType.equals(EngineType.KINGBASE)){
				graphics.drawImage(
						InternalImageIconFactory.DATASOURCE_KINGBASE.getImage(), 0, 0, label);
			}
			else if(engineType.equals(EngineType.POSTGRESQL)){
				graphics.drawImage(
						InternalImageIconFactory.DATASOURCE_POSTGRESQL.getImage(), 0, 0, label);
			}
			else if(engineType.equals(EngineType.BAIDUMAPS)){
				graphics.drawImage(
						InternalImageIconFactory.DATASOURCE_BAIDUMAPS.getImage(), 0, 0, label);
			}
			else if(engineType.equals(EngineType.DM)){
				graphics.drawImage(
						InternalImageIconFactory.DATASOURCE_DMPLUS.getImage(), 0, 0, label);
			}
			else if(engineType.equals(EngineType.ISERVERREST)){
				graphics.drawImage(
						InternalImageIconFactory.DATASOURCE_ISERVERREST.getImage(), 0, 0, label);
			}
//			else if(engineType.equals(EngineType.MAPWORLD)){
//				graphics.drawImage(
//						InternalImageIconFactory.DATASOURCE_MAPWORLD.getImage(), 0, 0, label);
//			}
			else if(engineType.equals(EngineType.ORACLESPATIAL)){
				graphics.drawImage(
						InternalImageIconFactory.DATASOURCE_ORACLESPATIAL.getImage(), 0, 0, label);
			}
			else if(engineType.equals(EngineType.SUPERMAPCLOUD)){
				graphics.drawImage(
						InternalImageIconFactory.DATASOURCE_SUPERMAPCLOUD.getImage(), 0, 0, label);
			}
			else if(engineType.equals(EngineType.VECTORFILE)){
				graphics.drawImage(
						InternalImageIconFactory.DATASOURCE_VECTORFILE.getImage(), 0, 0, label);
			}
			else{
				graphics.drawImage(
						InternalImageIconFactory.DATASOURCE_DEFAULT.getImage(), 0, 0, label);
			}
			
			icon.setImage(bufferedImage);
		}
	}

}
