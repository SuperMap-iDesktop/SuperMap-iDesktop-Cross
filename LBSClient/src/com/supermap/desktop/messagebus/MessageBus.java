package com.supermap.desktop.messagebus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.EngineType;
import com.supermap.data.conversion.DataImport;
import com.supermap.data.conversion.FileType;
import com.supermap.data.conversion.ImportResult;
import com.supermap.data.conversion.ImportSettingGRD;
import com.supermap.data.conversion.ImportSettings;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.CtrlAction.WebHDFS;
import com.supermap.desktop.CtrlAction.WebHDFS.HDFSDefine;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.dialog.JDialogFileSaveAs;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.http.BatchDownloadFile;
import com.supermap.desktop.http.DownloadInfo;
import com.supermap.desktop.http.DownloadProgressCallable;
import com.supermap.desktop.http.DownloadUtils;
import com.supermap.desktop.http.FileManager;
import com.supermap.desktop.http.FileManagerContainer;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilties.DatasourceUtilties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerSetting;
import com.supermap.mapping.LayerSettingGrid;
import com.supermap.mapping.LayerSettingImage;

public class MessageBus {
	private static final String producerCommand = "gitstark-Server";
	private static final String consumerName = "gitstark-Result";
	private static final String tcp_url = "tcp://192.168.14.2:61616";

	private static ArrayList<String> tasks = null;
	private static Boolean stop = false;

	public static void producer(String command) {
		if (tasks == null) {
			tasks = new ArrayList<String>();

			ExecutorService eService = Executors
					.newCachedThreadPool(new ThreadFactory() {

						@Override
						public Thread newThread(Runnable r) {
							Thread thread = new Thread(r);
							thread.setName("messagebus" + UUID.randomUUID());
							return thread;
						}
					});
			eService.submit(new MessageBusConsumer());
			Future<?> future = eService.submit(new MessageBusProducer());
			// future.get();
		}

		tasks.add(command);
	}

	// public static void main(String[] args) throws InterruptedException,
	// ExecutionException, IOException {
	// ExecutorService eService = Executors.newCachedThreadPool();
	// eService.submit(new MessageBusProducer());
	// Future<?> future = eService.submit(new MessageBusConsumer());
	// // future.get();
	// }

	public static void stop() {
		MessageBus.stop = true;
	}

	private static class MessageBusProducer implements Runnable,
			ExceptionListener {
		@Override
		public void run() {
			try {
				ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
						tcp_url);
				Connection conn = factory.createConnection();
				conn.start();
				conn.setExceptionListener(this);
				Session session = conn.createSession(false,
						Session.AUTO_ACKNOWLEDGE);
				Queue queue = session.createQueue(producerCommand);
				MessageProducer procucer = session.createProducer(queue);
				procucer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

				while (!MessageBus.stop) {
					for (String command : MessageBus.tasks) {
						TextMessage msg = session.createTextMessage(command);
						 procucer.send(msg);

//						String hdfsFile = "hdfs://192.168.14.1:9000/output/kerneldensity1464877189016.grd";
//						try {
//							finishKernelDensity(hdfsFile);
//						} catch (Throwable e) {
//							e.printStackTrace();
//						}
					}
					tasks.clear();
					Thread.sleep(100);
				}

				// Clean up
				session.close();
				conn.close();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onException(JMSException exception) {
			Application.getActiveApplication().getOutput().output(exception);
		}
	}

	public static class MessageBusConsumer implements Runnable,
			ExceptionListener {
		@Override
		public void run() {
			try {
				ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
						tcp_url);
				Connection conn = factory.createConnection();
				conn.start();
				conn.setExceptionListener(this);
				Session session = conn.createSession(false,
						Session.AUTO_ACKNOWLEDGE);
				Queue queue = session.createQueue(consumerName);
				MessageConsumer consumer = session.createConsumer(queue);

				while (!MessageBus.stop) {
					Message message = consumer.receive();
					if (message instanceof TextMessage) {
						String command = ((TextMessage) message).getText();
						String[] params = command.split(",");
						if (params.length == 2
								&& !"error".equalsIgnoreCase(params[1])) {
							MessageBusType type = MessageBusType.getType(params[0]);
							switch (type) {
							case BuildSpatialIndex:
								finishBuildSpatialIndex(params[1]);
								break;
							case SpatialQuery:
								finishSpatialQuery(params[1]);
								break;
							case AttributeQuery:
								finishAttributeQuery(params[1]);
								break;
							case KernelDensity:
								finishKernelDensity(params[1]);
								break;
							case KernelDensityRealtime:
								finishKernelDensityRealtime(params[1]);
								break;
							default:
								break;
							}
						}
					}
					Thread.sleep(100);
				}

				// Clean up
				session.close();
				conn.close();
			} catch (JMSException | InterruptedException exception) {
				Application.getActiveApplication().getOutput()
						.output(exception);
			}
		}

		@Override
		public void onException(JMSException exception) {
			Application.getActiveApplication().getOutput().output(exception);
		}
	}

	private static void finishBuildSpatialIndex(String path) {
		try {
			Application.getActiveApplication().getOutput().output("创建索引执行完成。");
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private static void finishSpatialQuery(String path) {
		try {
			Application.getActiveApplication().getOutput().output("空间查询执行完成。");
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private static void finishAttributeQuery(String path) {
		try {
			Application.getActiveApplication().getOutput().output("属性查询执行完成。");
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private static void finishKernelDensity(String path) {
		try {
			String localPath = downloadKernelDesity(path);
			Application.getActiveApplication().getOutput().output("热度图执行完成。");
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private static void finishKernelDensityRealtime(String path) {
		try {
			String localPath = downloadKernelDesity(path);
			Application.getActiveApplication().getOutput().output("实时热度图执行完成。");
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private static String downloadKernelDesity(String path) {
		String localPath = "F:/temp/";
		try {
			String[] items = path.split("/");
			String fileName = items[items.length - 1];
			String url = WebHDFS.hdfsToURL(path);
			HDFSDefine define = WebHDFS.getFileStatus(url);
			long fileSize = Long.parseLong(define.getSize());

			IDockbar dockbarPropertyContainer = null;
			try {
				dockbarPropertyContainer = Application
						.getActiveApplication()
						.getMainFrame()
						.getDockbarManager()
						.get(Class
								.forName(JDialogFileSaveAs.FILE_MANAGER_CONTROL_CLASS));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

//			FileManagerContainer fileManagerContainer = null;
//			if (dockbarPropertyContainer != null) {
//				fileManagerContainer = (FileManagerContainer) dockbarPropertyContainer
//						.getComponent();
//				dockbarPropertyContainer.setVisible(true);
//				dockbarPropertyContainer.active();
//			}
//
//			if (fileManagerContainer != null) {
//				DownloadInfo downloadInfo = new DownloadInfo(url, fileName,
//						localPath, fileSize, 1, true);
//				FileManager fileManager = new FileManager(downloadInfo);
//				DownloadProgressCallable downloadProgressCallable = new DownloadProgressCallable(downloadInfo);
//				fileManager.doWork(downloadProgressCallable);
//				fileManagerContainer.addItem(fileManager);
//			}			

			DownloadInfo downloadInfo = new DownloadInfo(url, fileName,
					localPath, fileSize, 1, true);
			DownloadUtils.download(downloadInfo);
			
			if (!localPath.endsWith("/")) {
				localPath += "/";
			}
			localPath += fileName;
			
			ShowResultThread showResultThread = new ShowResultThread();
			showResultThread.setDownloadInfo(downloadInfo);
			showResultThread.setLocalPath(localPath);
			showResultThread.start();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return localPath;
	}
	
	static class ShowResultThread extends Thread {
		
		private DownloadInfo downloadInfo = null;	
		private String localPath = "";

		@Override
		public void run() {
			try {
				// 1. get download finished status
				BatchDownloadFile batchDownloadFile = DownloadUtils.getBatchDownloadFileWorker(downloadInfo);
				try {
					while (batchDownloadFile != null && !batchDownloadFile.isFinished()) {
						sleep(500);
					}
					sleep(1000);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// 2. import grd file
				Dataset dataset = null;
				if (batchDownloadFile != null) {
					dataset = importTempGrd(localPath);
				}
				// 3. add to current map
				if (dataset != null) {
					IFormMap formMap = null;
					if (Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
						formMap = (IFormMap) Application.getActiveApplication()
								.getActiveForm();
					}

					if (formMap == null) {
						formMap = (IFormMap) CommonToolkit.FormWrap
								.fireNewWindowEvent(WindowType.MAP);
					}
					
					int times = 0;
					while (formMap == null && times < 3) {
						IForm form = Application.getActiveApplication().getActiveForm();
						if (form != null && form instanceof IFormMap ) {
							formMap = (IFormMap)form;
							break;
						}
						try {
							times++;
							sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					if (formMap != null) {
						Layer layer = formMap.getMapControl().getMap().getLayers().add(dataset, true);
//						LayerSetting layerSetting = layer.getAdditionalSetting();
						LayerSettingGrid setting = (LayerSettingGrid)layer.getAdditionalSetting();
						setting.setOpaqueRate(70);
						formMap.getMapControl().getMap().refresh();
					}
				}
			} finally {
			}
		}

		public DownloadInfo getDownloadInfo() {
			return downloadInfo;
		}


		public void setDownloadInfo(DownloadInfo downloadInfo) {
			this.downloadInfo = downloadInfo;
		}
		
		public String getLocalPath() {
			return this.localPath;
		}

		public void setLocalPath(String localPath) {
			this.localPath = localPath;
		}
	}

	private static Dataset importTempGrd(String path) {

		Dataset dataset = null;
		try {
			File file = new File(path);
			if (file.exists()) {
				Datasource datasource = null;

				String alias = "TempMemory";
				int index = Application.getActiveApplication().getWorkspace()
						.getDatasources().indexOf(alias);
				if (index != -1) {
					datasource = Application.getActiveApplication()
							.getWorkspace().getDatasources().get(index);
				}

				if (datasource == null) {
					DatasourceConnectionInfo info = new DatasourceConnectionInfo();
					info.setEngineType(EngineType.UDB);
					info.setServer(":memory:");
					info.setAlias(alias);
					datasource = Application.getActiveApplication()
							.getWorkspace().getDatasources().create(info);
				}

				DataImport dataImport = new DataImport();
				ImportSettingGRD setting = new ImportSettingGRD();
				setting.setSourceFilePath(path);
				setting.setTargetDatasource(datasource);
				String datasetName = datasource.getDatasets().getAvailableDatasetName("kerneldensity");
				setting.setTargetDatasetName(datasetName);
				ImportSettings importSettings = dataImport.getImportSettings();
				importSettings.add(setting);
				ImportResult importResult = dataImport.run();
				dataset = datasource.getDatasets().get(datasetName);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return dataset;
	}

	public enum MessageBusType {
		Unknow(-1), BuildSpatialIndex(0), SpatialQuery(1), AttributeQuery(2), KernelDensity(
				3), KernelDensityRealtime(4);

		private int value;

		MessageBusType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		@Override
		public String toString() {
			return String.format("%d", this.getValue());
		}

		public static MessageBusType getType(String name) {
			MessageBusType type = MessageBusType.Unknow;
			int value = -1;
			try {
				value = Integer.parseInt(name);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			switch (value) {
			case 0:
				type = MessageBusType.BuildSpatialIndex;
				break;
			case 1:
				type = MessageBusType.SpatialQuery;
				break;
			case 2:
				type = MessageBusType.AttributeQuery;
				break;
			case 3:
				type = MessageBusType.KernelDensity;
				break;
			case 4:
				type = MessageBusType.KernelDensityRealtime;
				break;
			}
			return type;
		}
	}
}
