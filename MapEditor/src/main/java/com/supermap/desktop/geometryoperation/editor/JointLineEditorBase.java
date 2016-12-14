package com.supermap.desktop.geometryoperation.editor;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.EditType;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoStyle;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.Recordset;
import com.supermap.data.Rectangle2D;
import com.supermap.desktop.Application;
import com.supermap.desktop.core.recordset.RecordsetDelete;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.control.JDialogFieldOperationSetting;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilities.CursorUtilities;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.TabularUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Selection;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public abstract class JointLineEditorBase extends AbstractEditor {


	@Override
	public void activate(EditEnvironment environment) {
		CursorUtilities.setWaitCursor(environment.getMapControl());

		try {
			JDialogFieldOperationSetting formCombination = new JDialogFieldOperationSetting(getTitle(), environment.getMap(), DatasetType.LINE);

			if (formCombination.showDialog() == DialogResult.OK) {
				jointLine(environment, formCombination.getEditLayer(), formCombination.getPropertyData());
				TabularUtilities.refreshTabularForm((DatasetVector) formCombination.getEditLayer().getDataset());
			}
		} finally {
			CursorUtilities.setDefaultCursor(environment.getMapControl());
			
			// 结束当前编辑。如果是交互性编辑，environment 会自动管理结束，就无需主动调用。
			environment.activateEditor(NullEditor.INSTANCE);
		}
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		boolean result = false;

		if (ListUtilities.isListContainAny(environment.getEditProperties().getEditableDatasetTypes(), DatasetType.CAD, DatasetType.LINE)
				&& environment.getEditProperties().getSelectedGeometryCount() > 1
				&& ListUtilities.isListOnlyContain(environment.getEditProperties().getSelectedGeometryTypeFeatures(), ILineFeature.class)) {
			result = true;
		}
		return result;
	}

	protected abstract GeoLine jointTwoLines(GeoLine baseLine, GeoLine targetLine, Boolean isOriginalBaseLine);

	/**
	 * 子类重写这个拿标题
	 * 
	 * @return
	 */
	protected String getTitle() {
		return MapEditorProperties.getString("String_GeometryOperation_JointLine");
	}

	protected double getDistance(Point2D point1, Point2D point2) {
		return Math.sqrt(Math.pow((point1.getX() - point2.getX()), 2) + Math.pow((point1.getY() - point2.getY()), 2));
	}

	protected void releaseGeometryList(ArrayList<GeoLine> lines) {
		if (lines != null) {
			for (int i = lines.size() - 1; i >= 0; i--) {
				GeoLine line = lines.get(i);
				if (line != null) {
					line.dispose();
					lines.remove(i);
				}
			}
		}
	}

	private double[][] arraySortProcess(double array[][], int arrayLength, int row, boolean sort)
	{
		if( array!=null && arrayLength > 0 ) // 假如传入的输入不为 NULL 值
		{
			int width = array[0].length;    // 得到数组的列数
			boolean exchange = true;            // 交换记录
			double temp[] = new double[width];    // 用于存放临时值
			if( width < row ) {
				return null;
			}
			for(int i = 0; i< arrayLength -1; i++)
			{
				exchange = false;    // 交换标记为假
				for(int j = arrayLength -2; j>=i; j--)
				{
					if(sort)    // 从高到底
					{
						if( Double.compare(array[j][row],(array[j+1][row])) < 0 )
						{
							for(int t=0; t<width; t++)
								temp[t] = array[j][t];    // 存放临时数据
							for(int t=0; t<width; t++)
								array[j][t] = array[j+1][t]; // 数据交换
							for(int t=0; t<width; t++)
								array[j+1][t] = temp[t];
							exchange = true;    // 置交还标记为真
						}
					} else { // 从底到高
						if( Double.compare(array[j][row],(array[j+1][row])) > 0 )
						{
							for(int t=0; t<width; t++)
								temp[t] = array[j][t];    // 存放临时数据
							for(int t=0; t<width; t++)
								array[j][t] = array[j+1][t]; // 数据交换
							for(int t=0; t<width; t++)
								array[j+1][t] = temp[t];
							exchange = true;    // 置交还标记为真
						}
					}
				}
				if( exchange==false )
					break;
			}
			return array;
		}
		return array;
	}

	private double[][] arraySort( double s[][], int row_1, int row_2 )
	{
		double array[][] = arraySortProcess( s,s.length, row_1, false ); // 对第一个条件进行排序
		if( array== null ) {
			return null;
		}
		int begin = 0;
		int Col = 0;
		for( int i=0; i<array.length-1; i=i+Col ) {
			Col = 1; //第一行
			for( int k=i+1; k<array.length; k++ ) {
				if( Double.compare(array[i][row_1],array[k][row_1])==0) {
					Col ++;
				} else {
					break;
				}
			}
			if( Col > 1 ) {
				double swpData[][] = new double[Col][s[0].length];
				for( int m=i; m<i+Col; m++ ) {
					swpData[m-i] = array[m];
				}
				arraySortProcess( swpData,swpData.length, row_2, true );// 对第二个条件进行排序
				for( int m=i; m<i+Col; m++ )
					array[m] = swpData[m-i];
			}
		}
		return array;
	}

	private void jointLine(EditEnvironment environment, Layer editLayer, Map<String, Object> propertyData) {
		GeoLine baseGeoLine = null;
		environment.getMapControl().getEditHistory().batchBegin();

		try {
			int partLineCounts = 0;
			boolean isOriginalBaseLine = true;
			ArrayList<Integer> deleteIDs = new ArrayList<Integer>();
			// 统一管理后面从记录集中取出来的GeoLine对象的释放
			ArrayList<GeoLine> releaseGeoLines = new ArrayList<GeoLine>();
			ArrayList<Layer> layers = MapUtilities.getLayers(environment.getMapControl().getMap());
			// new code
			Map<Point2D,GeoLine> map = new HashMap<>();
			Map<Point2D,GeoLine> mapRepeat = new HashMap<>();

			for (Layer layer : layers){
				Selection selection = null;

				if (layer.getDataset() instanceof DatasetVector) {
					selection = new Selection(layer.getSelection());
					layer.getSelection().clear();
				}

				if (selection != null && selection.getCount() > 0) {
					Recordset recordset = ((DatasetVector) layer.getDataset()).getRecordset(false, CursorType.DYNAMIC);

					for (int i = 0; i < selection.getCount(); i++) {
						recordset.seekID(selection.get(i));
						IGeometry geometry = DGeometryFactory.create(recordset.getGeometry());
						Point2D centerPoint2D=null;
						GeoLine currentLine = null;

						if (geometry.getGeometry().getType()== GeometryType.GEOLINE) {
							currentLine = ((ILineFeature) geometry).convertToLine(120);
							Rectangle2D tempRectangle=currentLine.getBounds();
							centerPoint2D=tempRectangle.getCenter();

//							if (map.containsKey(centerPoint2D)){
//								GeoLine  centerPointRepeat= map.get(centerPoint2D);
//								Point2D currentPoint=currentLine.getPart(0).getItem(0);
//								Point2D repeatPoint=centerPointRepeat.getPart(0).getItem(0);
//								if (Double.compare(repeatPoint.getX(),currentPoint.getX())==-1 || (Double.compare(repeatPoint.getX(),currentPoint.getX())==0 &&
//										Double.compare(repeatPoint.getY(),currentPoint.getY())==1)){
//									mapRepeat.put()
//								}
//								//Application.getActiveApplication().getOutput().output("true");
//							}
							map.put(centerPoint2D,currentLine);

							if (editLayer.getDataset().equals(layer.getDataset()) && currentLine != null) {
								deleteIDs.add(selection.get(i));
							}
						}
					}
					selection.dispose();
					if (recordset != null) {
						recordset.dispose();
					}
				}
			}

			//进行排序
			Object[] key =  map.keySet().toArray();
			double keySort[][]=new double[key.length][2];
			for (int i=0;i<key.length;i++){
				Point2D tempPoint=(Point2D) key[i];
				keySort[i][0]=tempPoint.getX();
				keySort[i][1]=tempPoint.getY();
			}

			arraySort(keySort,0,1);

			for (int i=0;i<key.length;i++){
				GeoLine currentLine = null;
				Point2D resultPoint2D=new Point2D(keySort[i][0],keySort[i][1]);
				currentLine=map.get(resultPoint2D);

				if (currentLine.getPartCount() > 1) {
					partLineCounts++;
					continue;
				}

				baseGeoLine = jointTwoLines(baseGeoLine, currentLine, isOriginalBaseLine);
				if (baseGeoLine != null && currentLine != null && !baseGeoLine.equals(currentLine)) {
					isOriginalBaseLine = false;
				}
				releaseGeoLines.add(currentLine);
			}

			// old code
			/*for (Layer layer : layers) {
				Selection selection = null;

				if (layer.getDataset() instanceof DatasetVector) {
					selection = new Selection(layer.getSelection());
					layer.getSelection().clear();
				}

				if (selection != null && selection.getCount() > 0) {
					Recordset recordset = ((DatasetVector) layer.getDataset()).getRecordset(false, CursorType.DYNAMIC);

					for (int i = 0; i < selection.getCount(); i++) {
						recordset.seekID(selection.get(i));
						IGeometry geometry = DGeometryFactory.create(recordset.getGeometry());
						GeoLine currentLine = null;

						if (geometry instanceof ILineFeature) {
							currentLine = ((ILineFeature) geometry).convertToLine(120);

							if (currentLine.getPartCount() > 1) {
								partLineCounts++;
								continue;
							}

							baseGeoLine = jointTwoLines(baseGeoLine, currentLine, isOriginalBaseLine);
							if (baseGeoLine != null && currentLine != null && !baseGeoLine.equals(currentLine)) {
								isOriginalBaseLine = false;
							}

							if (editLayer.getDataset().equals(layer.getDataset()) && currentLine != null) {
								deleteIDs.add(selection.get(i));
							}
							releaseGeoLines.add(currentLine);
						}
					}
					selection.dispose();
					if (recordset != null) {
						recordset.dispose();
					}
				}
			}*/

			if (!isOriginalBaseLine) {
				editLayer.getSelection().clear();
				Recordset resultRecordset = ((DatasetVector) editLayer.getDataset()).getRecordset(false, CursorType.DYNAMIC);
				GeoStyle style = null;
				RecordsetDelete delete = new RecordsetDelete((DatasetVector) editLayer.getDataset(), environment.getMapControl().getEditHistory());
				delete.update();

				if (deleteIDs.size() > 0) {
					for (int id : deleteIDs) {
						if (resultRecordset.seekID(id)) {
							// 记录结果图层中某一个对象的风格
							if (style == null) {
								Geometry geometry = resultRecordset.getGeometry();
								if (geometry.getStyle() != null) {
									style = geometry.getStyle().clone();
								}
								if (geometry != null) {
									geometry.dispose();
								}
							}

							delete.delete(id);
						}
					}
				}
				delete.update();

				baseGeoLine.setStyle(style);
				boolean isSucceed = false;

				if (resultRecordset.addNew(baseGeoLine, propertyData)) {
					if (resultRecordset.update()) {
						editLayer.getSelection().add(resultRecordset.getID());
						environment.getMapControl().getEditHistory().add(EditType.ADDNEW, resultRecordset, true);
						isSucceed = true;

						if (partLineCounts > 0) {
							Application.getActiveApplication().getOutput()
									.output(MessageFormat.format(MapEditorProperties.getString("String_GeometryEdit_ConnectLineErrorOutPut"), partLineCounts));
						}
						Application
								.getActiveApplication()
								.getOutput()
								.output(MessageFormat.format(MapEditorProperties.getString("String_GeometryEdit_ConnectLineTrueOutPut"),
										releaseGeoLines.size(), editLayer.getName()));
					}
				}

				if (!isSucceed) {
					Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_GeometryEdit_ConnectLineUpdateFaleOutPut"));
				}

				environment.getMapControl().getEditHistory().batchEnd();
				if (resultRecordset != null) {
					resultRecordset.close();
					resultRecordset.dispose();
				}
			} else {
				environment.getMapControl().getEditHistory().batchCancel();

				if (partLineCounts > 0) {
					Application.getActiveApplication().getOutput()
							.output(MessageFormat.format(MapEditorProperties.getString("String_GeometryEdit_ConnectLineErrorOutPut"), partLineCounts));
				}
				Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_GeometryEdit_ConnectLineFalseOutPut"));
			}

			if (baseGeoLine != null) {
				baseGeoLine.dispose();
			}

			releaseGeometryList(releaseGeoLines);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private class GeoLineSort{
		private ArrayList<GeoLine> arrayList=new ArrayList<GeoLine>();
		public void sort(){
			for (int i=0;i<arrayList.size();++i){
				GeoLine tempGeoline=(GeoLine) arrayList.get(i);
				Point2Ds temp2DS=tempGeoline.getPart(0);
				double startX=temp2DS.getItem(0).getX();
				double startY=temp2DS.getItem(0).getY();
				double endX=temp2DS.getItem((temp2DS.getCount()-1)).getX();
				double endY=temp2DS.getItem((temp2DS.getCount()-1)).getY();


			}
		}
	}
}
