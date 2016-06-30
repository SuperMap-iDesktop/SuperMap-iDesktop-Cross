package com.supermap.desktop.messagebus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.*;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;


import com.supermap.Interface.ITask;
import com.supermap.Interface.ITaskFactory;
import com.supermap.Interface.TaskEnum;
import com.supermap.data.*;
import com.supermap.data.conversion.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.CtrlAction.WebHDFS;
import com.supermap.desktop.CtrlAction.WebHDFS.HDFSDefine;
import com.supermap.desktop.Interface.*;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.http.FileManagerContainer;
import com.supermap.desktop.http.download.*;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.task.TaskFactory;
import com.supermap.desktop.utilities.CommonUtilities;
import com.supermap.mapping.*;

public class MessageBus {
	private static final String producerCommand = "gitstark-Server";
	private static final String consumerName = "gitstark-Result";
	private static final String tcp_url = "tcp://192.168.14.2:61616";

	private static ArrayList<String> tasks = null;
	private static Boolean stop = false;
	private static ITask task;

	public static void producer(String command) {
		if (tasks == null) {
			tasks = new ArrayList<String>();

			ExecutorService eService = Executors.newCachedThreadPool(new ThreadFactory() {

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

	public static void stop() {
		MessageBus.stop = true;
	}

	private static class MessageBusProducer implements Runnable, ExceptionListener {
		@Override
		public void run() {
			try {
				ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(tcp_url);
				Connection conn = factory.createConnection();
				conn.start();
				conn.setExceptionListener(this);
				Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
				Queue queue = session.createQueue(producerCommand);
				MessageProducer procucer = session.createProducer(queue);
				procucer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
				FileManagerContainer fileManagerContainer = CommonUtilities.getFileManagerContainer();
				ITaskFactory taskFactory = TaskFactory.getInstance();
				while (!MessageBus.stop) {
					for (String command : MessageBus.tasks) {
						TextMessage msg = session.createTextMessage(command);
							if (command.contains("HDFSGridIndexBuild")) {
								task = taskFactory.getTask(TaskEnum.CREATESPATIALINDEXTASK, null);
								addTask(fileManagerContainer, task);
							}
//							if (command.contains("HDFSGridIndexBuild")) {
//								task = taskFactory.getTask(TaskEnum.SPATIALQUERY, null);
//								addTask(fileManagerContainer, task);
//							}
//							if (type.equals(MessageBusType.AttributeQuery)) {
//								task = taskFactory.getTask(TaskEnum.ATTRIBUTEQUERY, null);
//								addTask(fileManagerContainer, task);
//							}
							if (command.contains("KernelDensity")&&command.endsWith("3")) {
								task = taskFactory.getTask(TaskEnum.KERNELDENSITYTASK, null);
								addTask(fileManagerContainer, task);
							}
							if (command.contains("KernelDensity")&&command.endsWith("4")) {
								task = taskFactory.getTask(TaskEnum.KERNELDENSITYREALTIMETASK, null);
								addTask(fileManagerContainer, task);
							}
						procucer.send(msg);
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

		private void addTask(FileManagerContainer fileManagerContainer, ITask task) throws InterruptedException {
			if (fileManagerContainer != null) {
				fileManagerContainer.addItem(task);
				Thread.sleep(1000);
				task.updateProgress(getRandomProgress(), "", "");
			}
		}

		@Override
		public void onException(JMSException exception) {
			Application.getActiveApplication().getOutput().output(exception);
		}
	}

	public static class MessageBusConsumer implements Runnable, ExceptionListener {
		@Override
		public void run() {
			try {
				ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(tcp_url);
				Connection conn = factory.createConnection();
				conn.start();
				conn.setExceptionListener(this);
				Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
				Queue queue = session.createQueue(consumerName);
				MessageConsumer consumer = session.createConsumer(queue);

				while (!MessageBus.stop) {
					FileManagerContainer fileManagerContainer = CommonUtilities.getFileManagerContainer();
					ITaskFactory taskFactory = TaskFactory.getInstance();
					Message message = consumer.receive();
					if (message instanceof TextMessage) {
						String command = ((TextMessage) message).getText();
						String[] params = command.split(",");
						if (params.length == 2 && !"error".equalsIgnoreCase(params[1])) {
							MessageBusType type = MessageBusType.getType(params[0]);
							switch (type) {
							case BuildSpatialIndex:
								updateProgress();
								finishBuildSpatialIndex(params[1], task);
								break;
							case SpatialQuery:
								updateProgress();
								finishSpatialQuery(params[1], task);
								break;
							case AttributeQuery:
								updateProgress();
								finishAttributeQuery(params[1], task);
								break;
							case KernelDensity:
								updateProgress();
								finishKernelDensity(params[1], task);
								break;
							case KernelDensityRealtime:
								updateProgress();
								finishKernelDensityRealtime(params[1], task);
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
				exception.printStackTrace();
				Application.getActiveApplication().getOutput().output(exception);
			}
		}

		private void updateProgress() throws InterruptedException {
			Thread.sleep(1000);
			task.updateProgress(getRandomProgress(), "", "");
		}

		@Override
		public void onException(JMSException exception) {
			Application.getActiveApplication().getOutput().output(exception);
		}
	}

	private static int getRandomProgress() {
		Random random = new Random(System.currentTimeMillis());
		return random.nextInt(100);
	}

	private static void finishBuildSpatialIndex(String path, ITask task) {
		try {
			task.updateProgress(100, "", "");
			Application.getActiveApplication().getOutput().output(LBSClientProperties.getString("String_KernelDensityFinished"));
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private static void finishSpatialQuery(String path, ITask task) {
		try {
			task.updateProgress(100, "", "");
			Application.getActiveApplication().getOutput().output(LBSClientProperties.getString("String_SpatialQueryFinished"));
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private static void finishAttributeQuery(String path, ITask task) {
		try {
			task.updateProgress(100, "", "");
			Application.getActiveApplication().getOutput().output(LBSClientProperties.getString("String_AttributeQueryFinished"));
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private static void finishKernelDensity(String path, ITask task) {
		try {
			String localPath = downloadKernelDesity(path);
			task.updateProgress(100, "", "");
			Application.getActiveApplication().getOutput().output(LBSClientProperties.getString("String_KernelDensityFinished"));
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private static void finishKernelDensityRealtime(String path, ITask task) {
		try {
			String localPath = downloadKernelDesity(path);
			task.updateProgress(100, "", "");
			Application.getActiveApplication().getOutput().output(LBSClientProperties.getString("String_KernelDensityRealtimeFinished"));
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

			FileInfo downloadInfo = new FileInfo(url, fileName, localPath, fileSize, 1, true);
			DownloadUtils.download(downloadInfo);

			if (!localPath.endsWith("/")) {
				localPath += "/";
			}
			localPath += fileName;

			CommonUtilities.addDownLoadTask(downloadInfo);
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

		private FileInfo downloadInfo = null;
		private String localPath = "";

		@Override
		public void run() {
			try {
				// 1. get download finished status
				BatchDownloadFile batchDownloadFile = new BatchDownloadFile(downloadInfo);
				try {
					while (!batchDownloadFile.isFinished()) {
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
						formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
					}

					if (formMap == null) {
						formMap = (IFormMap) CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.MAP);
					}

					int times = 0;
					while (formMap == null && times < 3) {
						IForm form = Application.getActiveApplication().getActiveForm();
						if (form != null && form instanceof IFormMap) {
							formMap = (IFormMap) form;
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
						// LayerSetting layerSetting = layer.getAdditionalSetting();
						LayerSettingGrid setting = (LayerSettingGrid) layer.getAdditionalSetting();
						setting.setOpaqueRate(70);
						formMap.getMapControl().getMap().refresh();
					}
				}
			} finally {
			}
		}

		public FileInfo getDownloadInfo() {
			return downloadInfo;
		}

		public void setDownloadInfo(FileInfo downloadInfo) {
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
				int index = Application.getActiveApplication().getWorkspace().getDatasources().indexOf(alias);
				if (index != -1) {
					datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(index);
				}

				if (datasource == null) {
					DatasourceConnectionInfo info = new DatasourceConnectionInfo();
					info.setEngineType(EngineType.UDB);
					info.setServer(":memory:");
					info.setAlias(alias);
					datasource = Application.getActiveApplication().getWorkspace().getDatasources().create(info);
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
		Unknow(-1), BuildSpatialIndex(0), SpatialQuery(1), AttributeQuery(2), KernelDensity(3), KernelDensityRealtime(4);

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
