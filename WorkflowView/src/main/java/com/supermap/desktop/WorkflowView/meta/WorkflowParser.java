package com.supermap.desktop.WorkflowView.meta;

import com.supermap.analyst.spatialanalyst.InterpolationAlgorithmType;
import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.meta.dataconversion.MetaProcessImportFactory;
import com.supermap.desktop.WorkflowView.meta.metaProcessImplements.DataRun.*;
import com.supermap.desktop.WorkflowView.meta.metaProcessImplements.*;
import com.supermap.desktop.WorkflowView.meta.metaProcessImplements.spatialStatistics.*;
import com.supermap.desktop.WorkflowView.meta.metaProcessImplements.typeConversion.*;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.core.NodeMatrix;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;
import com.supermap.desktop.utilities.OverlayAnalystType;
import com.supermap.desktop.utilities.StringUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by xie on 2017/3/21.
 * Parse xml to NodeMatrix
 */
public class WorkflowParser {
	private static final int ITEM_PROCESSES = 3;
	private static final int ITEM_NODES = 1;
	private static final int ITME_WORKFLOW = 0;

	public WorkflowParser() {

	}

	public NodeMatrix parseXMLToMatrix(String path) {
		NodeMatrix nodeMatrix = new NodeMatrix();
		if (!StringUtilities.isNullOrEmpty(path)) {
			File xmlFile = new File(path);
			if (xmlFile.exists()) {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				try {
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document document = builder.parse(xmlFile);
					NodeList documentChildNodes = document.getChildNodes();
					NodeList processesList = documentChildNodes.item(ITME_WORKFLOW).getChildNodes().item(ITEM_PROCESSES).getChildNodes();
					for (int i = 0; i < processesList.getLength(); i++) {
						if ("process".equals(processesList.item(i).getNodeName())) {
							Node process = processesList.item(i);
							NamedNodeMap attrs = process.getAttributes();
							MetaProcess metaProcess = null;
							if (attrs.getLength() > 0 && attrs != null) {
								Node attr = attrs.getNamedItem("key");
								metaProcess = getMetaProcess(attr.getNodeValue());
								nodeMatrix.addNode(metaProcess);
							}

							IParameters processParameters = metaProcess.getParameters();
							Node parameters = process.getChildNodes().item(1);
							NodeList parameterList = parameters.getChildNodes();
							for (int j = 0; j < parameterList.getLength(); j++) {
								if ("Parameter".equals(parameterList.item(j).getNodeName())) {
									Node parameterNode = parameterList.item(j);
									NamedNodeMap parameterAttrs = parameterNode.getAttributes();
									if (parameterAttrs.getLength() > 0 && parameterAttrs != null) {
//                                        String type = parameterAttrs.getNamedItem("type").getNodeValue();
										String describe = parameterAttrs.getNamedItem("describe").getNodeValue();
										String value = parameterAttrs.getNamedItem("value").getNodeValue();
										setParameterValue(processParameters, describe, value);
									}
								}
							}
						}
					}
					NodeList nodesList = documentChildNodes.item(0).getChildNodes().item(ITEM_NODES).getChildNodes();
					for (int i = 0; i < nodesList.getLength(); i++) {
						if ("Node".equals(nodesList.item(i).getNodeName())) {
							NodeList nodeInfo = nodesList.item(i).getChildNodes();
							MetaProcess process = null;
							MetaProcess preProcess = null;
							MetaProcess nextProcess = null;
							for (int j = 0; j < nodeInfo.getLength(); j++) {
								if ("process".equals(nodeInfo.item(j).getNodeName())) {
									String key = nodeInfo.item(j).getTextContent();
									process = getMetaProcess(key, nodeMatrix);
								}
								if ("PreProcess".equals(nodeInfo.item(j).getNodeName())) {
									String key = nodeInfo.item(j).getTextContent();
									preProcess = getMetaProcess(key, nodeMatrix);
								}
								if ("NextProcess".equals(nodeInfo.item(j).getNodeName())) {
									String key = nodeInfo.item(j).getTextContent();
									nextProcess = getMetaProcess(key, nodeMatrix);
								}
							}
							if (null != preProcess && null != process) {
								//TODO
								//INodeConstriant now not exist in xml file,so add a new INodeConstriant
//                                process.getInputs().followProcess(preProcess);
//								nodeMatrix.addRelation(preProcess, process, IRelation.class);
							}
							if (null != process && null != nextProcess) {
//                                nextProcess.getInputs().followProcess(process);
//								nodeMatrix.addRelation(process, nextProcess, IRelation.class);
							}
						}
					}
				} catch (ParserConfigurationException e) {
					Application.getActiveApplication().getOutput().output(e);
				} catch (SAXException e) {
					Application.getActiveApplication().getOutput().output(e);
				} catch (IOException e) {
					Application.getActiveApplication().getOutput().output(e);
				}
			}
		}
		return nodeMatrix;
	}

	private MetaProcess getMetaProcess(String key, NodeMatrix nodeMatrix) {
		MetaProcess result = null;
		Vector metaProcesses = nodeMatrix.getNodes();
		for (int i = 0; i < metaProcesses.size(); i++) {
			if (metaProcesses.get(i) instanceof MetaProcess && key.equals(((MetaProcess) metaProcesses.get(i)).getKey())) {
				result = (MetaProcess) metaProcesses.get(i);
			}
		}
		return result;
	}


	public void setParameterValue(IParameters parameters, String describe, String value) {
		int size = parameters.size();
		for (int i = 0; i < size; i++) {
			if (describe.equals(parameters.getParameter(i).getDescribe()) && parameters.getParameter(i) instanceof ISelectionParameter) {
				((ISelectionParameter) parameters.getParameter(i)).setSelectedItem(value);
			}
		}
	}

	public static MetaProcess getMetaProcess(String key) {
		MetaProcess result = null;
		if (StringUtilities.isNullOrEmpty(key)) {
			return null;
		}
		if (MetaKeys.BUFFER.equals(key)) {
			result = new MetaProcessBuffer();
		} else if (MetaKeys.HEAT_MAP.equals(key)) {
			result = new MetaProcessHeatMap();
		} else if (MetaKeys.SIMPLE_DENSITY.equals(key)) {
			result = new MetaProcessSimpleDensity();
		} else if (MetaKeys.GRIDREGION_AGGREGATION.equals(key)) {
			result = new MetaProcessGridRegionAggregation();
		} else if (MetaKeys.POLYGON_AGGREGATION.equals(key)) {
			result = new MetaProcessPolygonAggregation();
		} else if (MetaKeys.INTERPOLATOR_IDW.equals(key)) {
			result = new MetaProcessInterpolator(InterpolationAlgorithmType.IDW);
		} else if (MetaKeys.INTERPOLATOR_RBF.equals(key)) {
			result = new MetaProcessInterpolator(InterpolationAlgorithmType.RBF);
		} else if (MetaKeys.INTERPOLATOR_UniversalKRIGING.equals(key)) {
			result = new MetaProcessInterpolator(InterpolationAlgorithmType.UniversalKRIGING);
		} else if (MetaKeys.INTERPOLATOR_SimpleKRIGING.equals(key)) {
			result = new MetaProcessInterpolator(InterpolationAlgorithmType.SimpleKRIGING);
		} else if (MetaKeys.INTERPOLATOR_KRIGING.equals(key)) {
			result = new MetaProcessInterpolator(InterpolationAlgorithmType.KRIGING);
		} else if (MetaKeys.ISOLINE.equals(key)) {
			result = new MetaProcessISOLine();
		} else if (MetaKeys.ISOPOINT.equals(key)) {
			result = new MetaProcessISOPoint();
		} else if (MetaKeys.ISOREGION.equals(key)) {
			result = new MetaProcessISORegion();
		} else if (MetaKeys.KERNEL_DENSITY.equals(key)) {
			result = new MetaProcessKernelDensity();
		} else if (MetaKeys.SET_PROJECTION.equals(key)) {
			result = new MetaProcessSetProjection();
		} else if (MetaKeys.PROJECTION.equals(key)) {
			result = new MetaProcessProjection();
		} else if (MetaKeys.SPATIAL_INDEX.equals(key)) {
			result = new MetaProcessSpatialIndex();
		} else if (MetaKeys.OVERLAY_ANALYST_CLIP.equals(key)) {
			result = new MetaProcessOverlayAnalyst(OverlayAnalystType.CLIP);
		} else if (MetaKeys.OVERLAY_ANALYST_UNION.equals(key)) {
			result = new MetaProcessOverlayAnalyst(OverlayAnalystType.UNION);
		} else if (MetaKeys.OVERLAY_ANALYST_ERASE.equals(key)) {
			result = new MetaProcessOverlayAnalyst(OverlayAnalystType.ERASE);
		} else if (MetaKeys.OVERLAY_ANALYST_IDENTITY.equals(key)) {
			result = new MetaProcessOverlayAnalyst(OverlayAnalystType.IDENTITY);
		} else if (MetaKeys.OVERLAY_ANALYST_INTERSECT.equals(key)) {
			result = new MetaProcessOverlayAnalyst(OverlayAnalystType.INTERSECT);
		} else if (MetaKeys.OVERLAY_ANALYST_UPDATE.equals(key)) {
			result = new MetaProcessOverlayAnalyst(OverlayAnalystType.UPDATE);
		} else if (MetaKeys.OVERLAY_ANALYST_XOR.equals(key)) {
			result = new MetaProcessOverlayAnalyst(OverlayAnalystType.XOR);
		} else if (MetaKeys.SQL_QUERY.equals(key)) {
			result = new MetaProcessSqlQuery();
		} else if (MetaKeys.CENTRAL_ELEMENT.equals(key)) {
			result = new MetaProcessCentralElement();
		} else if (MetaKeys.MEAN_CENTER.equals(key)) {
			result = new MetaProcessMeanCenter();
		} else if (MetaKeys.MEDIAN_CENTER.equals(key)) {
			result = new MetaProcessMedianCenter();
		} else if (MetaKeys.DIRECTIONAL.equals(key)) {
			result = new MetaProcessDirectional();
		} else if (MetaKeys.LINEAR_DIRECTIONAL_MEAN.equals(key)) {
			result = new MetaProcessLinearDirectionalMean();
		} else if (MetaKeys.STANDARD_DISTANCE.equals(key)) {
			result = new MetaProcessStandardDistance();
		} else if (MetaKeys.AUTO_CORRELATION.equals(key)) {
			return new MetaProcessAutoCorrelation();
		} else if (MetaKeys.HIGH_OR_LOW_CLUSTERING.equals(key)) {
			result = new MetaProcessHighOrLowClustering();
		} else if (MetaKeys.CLUSTER_OUTLIER_ANALYST.equals(key)) {
			result = new MetaProcessClusterOutlierAnalyst();
		} else if (MetaKeys.HOT_SPOT_ANALYST.equals(key)) {
			result = new MetaProcessHotSpotAnalyst();
		} else if (MetaKeys.GEOGRAPHIC_WEIGHTED_REGRESSION.equals(key)) {
			result = new MetaProcessGeographicWeightedRegression();
		} else if (MetaKeys.OPTIMIZED_HOT_SPOT_ANALYST.equals(key)) {
			result = new MetaProcessOptimizedHotSpotAnalyst();
		} else if (MetaKeys.INCREMENTAL_AUTO_CORRELATION.equals(key)) {
			result = new MetaProcessIncrementalAutoCorrelation();
		} else if (MetaKeys.AVERAGE_NEAREST_NEIGHBOR.equals(key)) {
			result = new MetaProcessAverageNearestNeighbor();
		} else if (MetaKeys.OVERLAYANALYSTGEO.equals(key)) {
			result = new MetaProcessOverlayanalystgeo();
		} else if (MetaKeys.SINGLE_QUERY.equals(key)) {
			result = new MetaProcessSingleQuery();
		} else if (key.contains(MetaKeys.IMPORT)) {
			String importType = key.replace(MetaKeys.IMPORT, "");
			result = MetaProcessImportFactory.createMetaProcessImport(importType);
		} else if (MetaKeys.DEMLAKE.equals(key)) {
			result = new MetaProcessDEMLake();
		} else if (MetaKeys.DEMBUILD.equals(key)) {
			result = new MetaProcessDEMBuild();
		} else if (MetaKeys.GRIDTOVECTOR.equals(key)) {
			result = new MetaProcessRasterToVector();
		} else if (MetaKeys.VECTORTOGRID.equals(key)) {
			result = new MetaProcessVectorToRaster();
		} else if (MetaKeys.THINRASTER.equals(key)) {
			result = new MetaProcessThinRaster();
		} else if (MetaKeys.THIESSENPOLYGON.equals(key)) {
			result = new MetaProcessThiessenPolygon();
		} else if (MetaKeys.COMPUTEDISTANCE.equals(key)) {
			result = new MetaProcessComputeDistance();
		} else if (MetaKeys.EXPORTGRID.equals(key)) {
			result = new MetaProcessExportGrid();
		} else if (MetaKeys.EXPORTVECTOR.equals(key)) {
			result = new MetaProcessExportVector();
		} else if (MetaKeys.AGGREGATE_POINTS.equals(key)) {
			result = new MetaProcessAggregatePoints();
		} else if (MetaKeys.RAREFY_POINTS.equals(key)) {
			result = new MetaProcessRarefyPoints();
		} else if (MetaKeys.FIELD_INDEX.equals(key)) {
			result = new MetaProcessFieldIndex();
		} else if (MetaKeys.VECTOR_RESAMPLE.equals(key)) {
			result = new MetaProcessVectorResample();
		} else if (MetaKeys.LINE_POLYGON_SMOOTH.equals(key)) {
			result = new MetaProcessLinePolygonSmooth();
		} else if (MetaKeys.CONVERSION_POINT_TO_LINE.equals(key)) {
			result = new MetaProcessPointToLine();
		} else if (MetaKeys.CONVERSION_LINE_TO_POINT.equals(key)) {
			result = new MetaProcessLineToPoint();
		} else if (MetaKeys.CONVERSION_REGION_TO_POINT.equals(key)) {
			result = new MetaProcessRegionToPoint();
		} else if (MetaKeys.CONVERSION_LINE_TO_REGION.equals(key)) {
			result = new MetaProcessLineToRegion();
		} else if (MetaKeys.CONVERSION_REGION_TO_LINE.equals(key)) {
			result = new MetaProcessRegionToLine();
		} else if (MetaKeys.CONVERSION_TABULAR_TO_POINT.equals(key)) {
			result = new MetaProcessTabularToPoint();
		} else if (MetaKeys.CONVERSION_TABULARPOINT_TO_REGION.equals(key)) {
			result = new MetaProcessTabularPointToRegion();
		} else if (MetaKeys.CONVERSION_CAD_TO_SIMPLE.equals(key)) {
			result = new MetaProcessCADToSimple();
		} else if (MetaKeys.CONVERSION_SIMPLE_TO_CAD.equals(key)) {
			result = new MetaProcessSimpleToCAD();
		} else if (MetaKeys.CONVERSION_FIELD_TO_TEXT.equals(key)) {
			result = new MetaProcessFieldToText();
		} else if (MetaKeys.CONVERSION_TEXT_TO_FILED.equals(key)) {
			result = new MetaProcessTextToField();
		} else if (MetaKeys.CONVERSION_TEXT_TO_POINT.equals(key)) {
			result = new MetaProcessTextToPoint();
		} else if (MetaKeys.CONVERSION_EPS_TO_SIMPLE.equals(key)) {
			result = new MetaProcessEPSToSimple();
		} else if (MetaKeys.CONVERSION_POINT3D_TO_2D.equals(key)) {
			result = new MetaProcessPoint3DTo2D();
		} else if (MetaKeys.CONVERSION_LINE3D_TO_2D.equals(key)) {
			result = new MetaProcessLine3DTo2D();
		} else if (MetaKeys.CONVERSION_REGION3D_TO_2D.equals(key)) {
			result = new MetaProcessRegion3DTo2D();
		} else if (MetaKeys.CONVERSION_POINT2D_TO_3D.equals(key)) {
			result = new MetaProcess2DTo3D(DatasetType.POINT);
		} else if (MetaKeys.CONVERSION_LINE2D_TO_3D.equals(key)) {
			result = new MetaProcess2DTo3D(DatasetType.LINE);
		} else if (MetaKeys.CONVERSION_REGION2D_TO_3D.equals(key)) {
			result = new MetaProcess2DTo3D(DatasetType.REGION);
		} else if (MetaKeys.EDGE_MATCH.equals(key)) {
			result = new MetaProcessEdgeMatch();
		} else if (MetaKeys.PICKUP_BORDER.equals(key)) {
			result = new MetaProcessPickupBorder();
		} else if (MetaKeys.DUALLINE_TO_CENTERLINE.equals(key)) {
			result = new MetaProcessDualLineToCenterLine();
		} else if (MetaKeys.REGION_TO_CENTERLINE.equals(key)) {
			result = new MetaProcessRegionToCenterLine();
		} else if (MetaKeys.REGION_TRUNK_TO_CENTERLINE.equals(key)) {
			result = new MetaProcessRegionTrunkToCenterLine();
		} else if(MetaKeys.APPENDFIELDS.equals(key)){
			result = new MetaProcessUpdateFields();
		}else{
		} else if (MetaKeys.SHORTEST_PATH.equals(key)) {
			result = new MetaProcessShortestPath();
		}else {
			result = new EmptyMetaProcess(ProcessProperties.getString("String_" + key));
		}


		return result;
	}
}
