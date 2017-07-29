package com.supermap.desktop.ui.lbs.ui;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.supermap.data.Enum;
import com.supermap.data.GeometryType;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.lbs.HDFSDefine;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.GeometryTypeUtilities;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class JDialogPreviewCSV extends SmDialog {
	private String webURL;
	private HDFSDefine hdfsDefine;

	private JTable tableField = new JTable();
	private CSVFiledTableModel tableModelField;

	private JTable tablePreviewCSV = new JTable();
	private PreviewCSVTableModel tableModelPreviewCSV;

	private JLabel labelGeometryType = new JLabel();
	private JComboBox<GeometryType> comboBoxGeometryType = new JComboBox<>();

	private JLabel labelStorageType = new JLabel();
	private JComboBox<String> comboBoxStorageType = new JComboBox<>();

	private JCheckBox checkBoxHasHeader = new JCheckBox();

	private SmButton buttonOk = new SmButton(CommonProperties.getString(CommonProperties.OK));
	private SmButton buttonCancle = new SmButton(CommonProperties.getString(CommonProperties.Cancel));

	private static final int BUFF_LENGTH = 1024 * 8;

	private static final int DEFAULT_CSV_LENGTH = 10;

	private String csvFile = null;
	private String metaFile = null;

	public JDialogPreviewCSV(String webURL, HDFSDefine hdfsDefine) {
		this.webURL = webURL;
		if (!webURL.endsWith("/")) {
			this.webURL += "/";
		}
		this.hdfsDefine = hdfsDefine;
		init();
	}

	private void init() {
		initComponents();
		initLayout();
		initListeners();
		initComponentState();
	}

	private void initComponents() {
		comboBoxGeometryType.setRenderer(new ListCellRenderer<GeometryType>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends GeometryType> list, GeometryType value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel jLabel = new JLabel(GeometryTypeUtilities.toString(value));
				jLabel.setOpaque(false);
				if (isSelected) {
					jLabel.setBackground(list.getSelectionBackground());
					jLabel.setForeground(list.getSelectionForeground());
				} else {
					jLabel.setBackground(list.getBackground());
				}
				return jLabel;
			}
		});
		Enum[] enums = GeometryType.getEnums(GeometryType.class);
		for (Enum anEnum : enums) {
			comboBoxGeometryType.addItem((GeometryType) anEnum);
		}

		String[] storageTypes = {
				"WKT", "XY"
		};
		for (String storageType : storageTypes) {
			comboBoxStorageType.addItem(storageType);
		}
	}

	private void initLayout() {
		this.setLayout(new GridBagLayout());
		this.add(labelGeometryType, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 0, 0));
		this.add(comboBoxGeometryType, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(10, 5, 0, 10).setFill(GridBagConstraints.HORIZONTAL));

		this.add(labelStorageType, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 0, 0));
		this.add(comboBoxStorageType, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 0, 0).setFill(GridBagConstraints.HORIZONTAL));

		this.add(checkBoxHasHeader, new GridBagConstraintsHelper(0, 2, 2, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 0, 0));

		this.add(new JScrollPane(tableField), new GridBagConstraintsHelper(0, 3, 2, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 0, 0).setFill(GridBagConstraints.BOTH));

		this.add(new JScrollPane(tablePreviewCSV), new GridBagConstraintsHelper(0, 4, 2, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 0, 0).setFill(GridBagConstraints.BOTH));

		JPanel panelButton = new JPanel();
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(buttonOk, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(1, 0));
		panelButton.add(buttonCancle, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(0, 5, 0, 0));

		this.add(panelButton, new GridBagConstraintsHelper(0, 5, 2, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 10, 10));

	}

	private void initListeners() {
		buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogResult = DialogResult.OK;
// TODO: 2017/7/27
			}
		});

		buttonCancle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogResult = DialogResult.CANCEL;
				dispose();
			}
		});
	}

	private void initComponentState() {


		int startPos = 0;
		// TODO: 2017/7/28 下载csv和meta文件
		try {
			//region 获取Csv文件
			byte[] csvFileBytes = new byte[0];
			URL url = new URL(this.webURL + hdfsDefine.getName() + "?op=open&offset=" + 0 + "&length=" + hdfsDefine.getSize());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			setConnection(connection);
			//获取文件输入流，读取文件内容
			InputStream inputStream = connection.getInputStream();
			while (inputStream == null) {
				inputStream = connection.getInputStream();
			}
			byte[] buff = new byte[BUFF_LENGTH];
			int size = -1;


			while ((size = inputStream.read(buff)) > 0) {
				// 写入文件内容，返回最后写入的长度
				byte[] temp = new byte[csvFileBytes.length + buff.length];
				System.arraycopy(csvFileBytes, 0, temp, 0, csvFileBytes.length);
				System.arraycopy(buff, 0, temp, csvFileBytes.length, buff.length);
				csvFileBytes = temp;


				String csvFileString = new String(csvFileBytes, "UTF-8");
				String[] split = csvFileString.split(System.getProperty("line.separator"));
				if (split.length >= DEFAULT_CSV_LENGTH) {
					csvFile = csvFileString;
					break;
				}
				startPos += size;
			}
			inputStream.close();
			connection.disconnect();
			//endregion
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		try {
			byte[] buff = new byte[BUFF_LENGTH];
			String metaFileName = hdfsDefine.getName().substring(0, hdfsDefine.getName().length() - 4) + ".meta";
			URL urlMeta = new URL(this.webURL + metaFileName);
			HttpURLConnection connectionMeta = (HttpURLConnection) urlMeta.openConnection();
			setConnection(connectionMeta);

			//获取文件输入流，读取文件内容
			InputStream inputStreamMeta = connectionMeta.getInputStream();
			while (inputStreamMeta == null) {
				inputStreamMeta = connectionMeta.getInputStream();
			}
			int size = -1;

			byte[] csvMetaByte = new byte[0];

			while ((size = inputStreamMeta.read(buff)) > 0) {
				// 写入文件内容，返回最后写入的长度
				byte[] temp = new byte[csvMetaByte.length + buff.length];
				System.arraycopy(csvMetaByte, 0, temp, 0, csvMetaByte.length);
				System.arraycopy(buff, 0, temp, csvMetaByte.length, buff.length);
				csvMetaByte = temp;

				String csvFileString = new String(csvMetaByte, "UTF-8");
				String[] split = csvFileString.split(File.separator);
				if (split.length >= DEFAULT_CSV_LENGTH + 1) {
					metaFile = csvFileString;
					break;
				}
				startPos += size;
			}
			inputStreamMeta.close();
			connectionMeta.disconnect();
		} catch (Exception e) {
			// meta找不到就算了
		}

		ArrayList<RowData> rowDatas = new ArrayList<>();
		if (this.metaFile != null) {
			JSONObject jsonObject = JSONObject.parseObject(metaFile);
			if (jsonObject != null) {
				String geometryType = (String) jsonObject.get("GeometryType");
				String storageType = (String) jsonObject.get("StorageType");
				boolean hasHeader = Boolean.valueOf((String) jsonObject.get("HasHeader"));

				comboBoxGeometryType.setSelectedItem(GeometryType.parse(GeometryType.class, geometryType));
				comboBoxStorageType.setSelectedItem(storageType);
				checkBoxHasHeader.setSelected(hasHeader);

				JSONArray fieldInfos = (JSONArray) jsonObject.get("FieldInfos");
				for (Object fieldInfo : fieldInfos) {
					String name = (String) ((JSONObject) fieldInfo).get("name");
					String type = (String) ((JSONObject) fieldInfo).get("type");
					rowDatas.add(new RowData(name, type));
				}
			}
		}
		tableModelField = new CSVFiledTableModel(rowDatas);

		ArrayList<RowData> csvDatas = new ArrayList<>();
		String[] split = csvFile.split(File.separator);
		for (int i = 0; i < split.length - 1; i++) {
			String line = split[i];
			String[] columns = line.split("\",\"");
			csvDatas.add(new RowData(columns));
		}

		tableModelPreviewCSV = new PreviewCSVTableModel(csvDatas);
		tableField.setModel(tableModelField);
		tablePreviewCSV.setModel(tableModelPreviewCSV);

	}

	private void setConnection(HttpURLConnection connection) {
		// 设置连接超时时间为10000ms
		connection.setConnectTimeout(30000);
		// 设置读取数据超时时间为10000ms
		connection.setReadTimeout(30000);
		connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.3) Gecko/2008092510 Ubuntu/8.04 (hardy) Firefox/3.0.3");
		connection.setRequestProperty("Accept-Language", "en-us,en;q=0.7,zh-cn;q=0.3");
		connection.setRequestProperty("Accept-Encoding", "utf-8");
		connection.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		connection.setRequestProperty("Keep-Alive", "300");
		connection.setRequestProperty("connnection", "keep-alive");
		connection.setRequestProperty("If-Modified-Since", "Fri, 02 Jan 2009 17:00:05 GMT");
		connection.setRequestProperty("If-None-Match", "\"1261d8-4290-df64d224\"");
		connection.setRequestProperty("Cache-conntrol", "max-age=0");
		connection.setRequestProperty("Referer", "http://www.baidu.com");
	}

	class PreviewCSVTableModel extends DefaultTableModel {
		ArrayList<String> columnNames;

		private ArrayList<RowData> values = new ArrayList<>();

		PreviewCSVTableModel(ArrayList<RowData> values) {
			this.values = values;
		}

		@Override
		public int getRowCount() {
			return values.size();
		}

		@Override
		public int getColumnCount() {
			return columnNames.size();
		}

		@Override
		public String getColumnName(int column) {
			return columnNames.get(column);
		}

		@Override
		public Object getValueAt(int row, int column) {
			return values.get(row).getValueAt(column);
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

		public void setColumnName(int columnIndex, String columnName) {
			columnNames.set(columnIndex, columnName);
		}
	}

	class CSVFiledTableModel extends DefaultTableModel {

		private ArrayList<RowData> values = new ArrayList<>();
		private String[] columnNames = new String[]{
				ControlsProperties.getString("String_FieldInfoName"),
				ControlsProperties.getString("String_FieldInfoType")
		};

		CSVFiledTableModel(ArrayList<RowData> values) {
			this.values = values;
		}

		@Override
		public int getRowCount() {
			return values.size();
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}

		@Override
		public Object getValueAt(int row, int column) {
			return values.get(row).getValueAt(column);
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return true;
		}
	}

	class RowData {
		private String[] datas;

		public RowData(String... datas) {
			this.datas = datas;
		}

		public int getLength() {
			return datas.length;
		}

		public String getValueAt(int index) {
			String data = datas[index];
			if (data.startsWith("\"")) {
				data = data.substring(1);
			}
			if (data.endsWith("\"")) {
				data = data.substring(0, data.length() - 1);
			}
			return data;
		}

		public void setValueAt(int index, String value) {
			datas[index] = value;
		}
	}
}
