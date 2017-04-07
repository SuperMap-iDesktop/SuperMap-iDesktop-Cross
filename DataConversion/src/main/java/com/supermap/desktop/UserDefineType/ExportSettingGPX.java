package com.supermap.desktop.UserDefineType;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Recordset;
import com.supermap.data.conversion.ExportSettingCSV;
import com.supermap.data.conversion.ExportSteppedEvent;
import com.supermap.data.conversion.ExportSteppedListener;
import com.supermap.desktop.Application;
import com.supermap.desktop.utilities.XmlUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import java.io.File;
import java.util.Vector;

/**
 * Created by xie on 2017/3/29.
 */
public class ExportSettingGPX extends ExportSettingCSV {
    private Vector steppedListeners;

    public ExportSettingGPX() {

    }

    public UserDefineExportResult run() {
        UserDefineExportResult result = null;
        Document document = XmlUtilities.getEmptyDocument();
        Element gpxNode = XmlUtilities.createRoot(document, "gpx");
        Element trkNode = document.createElement("trk");
        gpxNode.appendChild(trkNode);
        Element trksegNode = document.createElement("trkseg");
        trkNode.appendChild(trksegNode);
        Recordset recordset = ((DatasetVector) this.getSourceData()).getRecordset(false, CursorType.DYNAMIC);
        recordset.moveFirst();
        int i = 0;
        int count = recordset.getRecordCount();
        while (!recordset.isEOF()) {
            Element trkptNode = document.createElement("trkpt");
            if (null != recordset.getFieldValue("SmX")) {
                trkptNode.setAttribute("lon", String.valueOf(recordset.getFieldValue("SmX")));
            }
            if (null != recordset.getFieldValue("SmY")) {
                trkptNode.setAttribute("lat", String.valueOf(recordset.getFieldValue("SmY")));
            }
            if (null != recordset.getFieldValue("ele")) {
                Element eleNode = document.createElement("ele");
                eleNode.setTextContent(String.valueOf(recordset.getFieldValue("ele")));
                trkptNode.appendChild(eleNode);
            }
            if (null != recordset.getFieldValue("time")) {
                Element timeNode = document.createElement("time");
                timeNode.setTextContent(String.valueOf(recordset.getFieldValue("time")));
                trkptNode.appendChild(timeNode);
            }
            trksegNode.appendChild(trkptNode);
            int percent = i * 100 / count;
            ExportSteppedEvent steppedEvent = new ExportSteppedEvent(this, 100, percent, this, 1, false);
            fireStepped(steppedEvent);
            recordset.moveNext();
            i++;
        }
        recordset.dispose();
        DOMSource source = new DOMSource(document);
        TransformerFactory tf = TransformerFactory.newInstance();
        try {
            Transformer transformer = tf.newTransformer();
            File file = new File(this.getTargetFilePath());
            if (!file.exists()) {
                file.createNewFile();
                XmlUtilities.parseFileToXML(transformer, source, file);
            } else {
                XmlUtilities.parseFileToXML(transformer, source, file);
            }
            if (file.length() > 0) {
                result = new UserDefineExportResult(this, null);
            } else {
                result = new UserDefineExportResult(null, this);
            }
        } catch (Exception e) {
            Application.getActiveApplication().getOutput().output(e);
        }
        return result;
    }

    public void addExportSteppedListener(ExportSteppedListener listener) {
        if (null == steppedListeners) {
            this.steppedListeners = new Vector();
        }
        if (!steppedListeners.contains(listener)) {
            steppedListeners.add(listener);
        }
    }

    public void removeExportSteppedListener(ExportSteppedListener listener) {
        if (null != steppedListeners && steppedListeners.contains(listener)) {
            steppedListeners.remove(listener);
        }
    }

    private void fireStepped(ExportSteppedEvent steppedEvent) {
        if (null != this.steppedListeners) {
            Vector listeners = this.steppedListeners;
            int size = listeners.size();
            for (int i = 0; i < size; i++) {
                ((ExportSteppedListener) listeners.get(i)).stepped(steppedEvent);
            }
        }
    }
}
