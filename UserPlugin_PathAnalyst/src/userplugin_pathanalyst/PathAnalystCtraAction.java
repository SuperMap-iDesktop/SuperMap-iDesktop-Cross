package userplugin_pathanalyst;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.text.MessageFormat;
import java.util.Properties;

import com.supermap.analyst.navigation.PathAnalyst;
import com.supermap.analyst.navigation.RouteType;
import com.supermap.analyst.navigation.SSCDataEnvironment;
import com.supermap.data.DatasetVector;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoPoint;
import com.supermap.data.GeoStyle;
import com.supermap.data.Point2D;
import com.supermap.data.Size2D;
import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.utilties.PathUtilties;
import com.supermap.mapping.TrackingLayer;
import com.supermap.ui.MapControl;

public class PathAnalystCtraAction extends CtrlAction {

	private MapControl m_mapControl;
	private TrackingLayer m_trackingLayer;
	private Boolean m_isCompute = false;
	private PathAnalyst m_pathAnalyst;
	private SSCDataEnvironment m_environment;
	private DatasetVector m_network;
	private double m_lineWidth;
	private Color m_lineColor;
	private int m_markerID;
	private double m_markerSize;
	private Color m_markerColor;

	public PathAnalystCtraAction(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		Application.getActiveApplication().getOutput().output(PathAnalystProperties.getString("String_PathAnalyst_Start"));
		String rootPath = Application.getActiveApplication().getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		rootPath = PathUtilties.getParentPath(rootPath);
		Application.getActiveApplication().getOutput().output(PathAnalystProperties.getString("String_PathAnalyst_CurrentPath") + rootPath);

		try {
			String iniPath = rootPath + "/PathAnalyst.ini";
			File file = new File(iniPath);
			if (!file.exists()) {
				Application.getActiveApplication().getOutput().output(iniPath + PathAnalystProperties.getString("String_PathAnalyst_CanNotFindINI"));
				return;
			}

			Properties iniReader = new Properties();
			iniReader.load(new FileInputStream(file));

			String datasource = iniReader.getProperty("Datasource");
			String dataset = iniReader.getProperty("Dataset");
			String sccPath = iniReader.getProperty("SCCPath");
			String lineWidth = iniReader.getProperty("LineWidth");
			String lineColor = iniReader.getProperty("LineColor");
			String markerID = iniReader.getProperty("MarkerID");
			String markerSize = iniReader.getProperty("MarkerSize");
			String markerColor = iniReader.getProperty("MarkerColor");

			m_lineWidth = Double.parseDouble(lineWidth);
			m_lineColor = new Color(Integer.valueOf(lineColor.substring(3, 5), 16), Integer.valueOf(lineColor.substring(5, 7), 16), Integer.valueOf(
					lineColor.substring(7, 9), 16), Integer.valueOf(lineColor.substring(1, 3), 16));
			m_markerID = new Integer(markerID);
			m_markerSize = Double.parseDouble(markerSize);
			m_markerColor = new Color(Integer.valueOf(markerColor.substring(3, 5), 16), Integer.valueOf(markerColor.substring(5, 7), 16), Integer.valueOf(
					markerColor.substring(7, 9), 16), Integer.valueOf(markerColor.substring(1, 3), 16));

			m_environment = new SSCDataEnvironment();

			Workspace workspace = Application.getActiveApplication().getWorkspace();
			m_network = (DatasetVector) workspace.getDatasources().get(datasource).getDatasets().get(dataset);

			Application.getActiveApplication().getOutput().output(PathAnalystProperties.getString("String_PathAnalyst_SetEnv"));
			Boolean bResult = m_environment.connectData(sccPath, m_network);

			if (bResult == false) {
				Application.getActiveApplication().getOutput().output(PathAnalystProperties.getString("String_PathAnalyst_SetEnvFaild"));
				return;
			}
			m_pathAnalyst = new PathAnalyst();
			m_pathAnalyst.setRouteMode(RouteType.MINLENGTH);
			bResult = m_pathAnalyst.setSSCEnvironment(m_environment);
			if (bResult == false) {
				Application.getActiveApplication().getOutput().output(PathAnalystProperties.getString("String_PathAnalyst_SetEnvFaild"));
				return;
			}
			Application.getActiveApplication().getOutput().output(PathAnalystProperties.getString("String_PathAnalyst_SetEnvSuccess"));

			// ָ����ʼλ�ã��찲�Ÿ���
			Point2D pntStart = new Point2D(116.38696, 39.90725); // ��γ�����
			// Point2D pntStart = new Point2D(12957317.108779509, 4852542.8999787383); //ͶӰ���

			m_pathAnalyst.setStartPoint(pntStart);
			m_isCompute = true;

			// ��ȡ��ǰ��������ͼ��
			IFormMap m_activeFormMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			m_mapControl = m_activeFormMap.getMapControl();
			m_trackingLayer = m_mapControl.getMap().getTrackingLayer();
			m_trackingLayer.setAntialias(true);

			m_mapControl.addMouseMotionListener(new MouseMotionListener() {
				@Override
				public void mouseMoved(MouseEvent e) {
					addMouseMoved(e);
				}

				@Override
				public void mouseDragged(MouseEvent e) {
					// 默认实现
				}
			});
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void addMouseMoved(MouseEvent e) {
		try {
			if (m_isCompute) {
				m_trackingLayer.clear();
				m_mapControl.getMap().refreshTrackingLayer();
				Point point = new Point(e.getX(), e.getY());
				Point2D mapPoint = m_mapControl.getMap().pixelToMap(point);

				if (m_mapControl.getMap().getBounds().contains(mapPoint)) {
					m_pathAnalyst.setEndPoint(mapPoint);

					Boolean bResult = m_pathAnalyst.plan();
					if (bResult) {
						GeoLine pathLine = new GeoLine(m_pathAnalyst.getPathPoints());
						GeoStyle pathLineStyle = new GeoStyle();
						pathLineStyle.setLineColor(m_lineColor);
						pathLineStyle.setLineWidth(m_lineWidth);
						pathLine.setStyle(pathLineStyle);
						m_trackingLayer.add(pathLine, "pathLine");

						GeoPoint targetPoint = new GeoPoint(mapPoint.getX(), mapPoint.getY());
						GeoStyle targetPointStyle = new GeoStyle();
						targetPointStyle.setMarkerSymbolID(m_markerID);
						targetPointStyle.setMarkerSize(new Size2D(m_markerSize, m_markerSize));
						targetPointStyle.setLineColor(m_markerColor);
						targetPoint.setStyle(targetPointStyle);
						m_trackingLayer.add(targetPoint, "target");

						m_mapControl.getMap().refreshTrackingLayer();

						Application.getActiveApplication().getOutput()
								.output(MessageFormat.format(PathAnalystProperties.getString("String_PathAnalyst_Result"), m_pathAnalyst.getPathLength()));
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}
}
