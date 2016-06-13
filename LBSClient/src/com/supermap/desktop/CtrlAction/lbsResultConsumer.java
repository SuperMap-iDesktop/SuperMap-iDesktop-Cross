package com.supermap.desktop.CtrlAction;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import javax.swing.SwingUtilities;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.enums.WindowType;

public class lbsResultConsumer {


	private String filePath = "";
	private String topicNameRespond = "";
	/*
	 * 处理计算结果通知
	 */
	public void doWork(String topicName) {
		
		String server = "192.168.14.240:9092";
		String group = "Group";	
		topicNameRespond = topicName;
			
		Properties props = new Properties();
		props.put("bootstrap.servers", server);
		props.put("group.id", group);
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("session.timeout.ms", "30000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList(topicNameRespond));

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置日期格式
		Application.getActiveApplication().getOutput().output(df.format(new Date()) + "正在等待来自服务端的查询结果"); //new Date()为获取当前系统时间

		consumer.commitAsync();
		
		Boolean result = false;
		while (!result) {
			ConsumerRecords<String, String> records = consumer.poll(100);
			for (ConsumerRecord<String, String> record : records) {
				if (record.value() != null && !record.value().equals("") && !record.value().equals("开始执行操作")) {
					String temp = String.format("接收到的数据. offset=%s, key=%s, value=%s", record.offset(), record.key(),record.value());
					Application.getActiveApplication().getOutput().output(temp); //new Date()为获取当前系统时间
					
					File file = new File(record.value().trim());
					if (file.exists()) {
						filePath = file.getPath();
						result = true;
						break;
					}
				}	
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (result) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置日期格式
					Application.getActiveApplication().getOutput().output(df.format(new Date()) + "  任务完成，查询结果保存在：[" + filePath + "]。"); //new Date()为获取当前系统时间
					
					// 判断如果是UDB就直接打开
					if (filePath.endsWith(".udb")) {
						// open datasource
						DatasourceConnectionInfo info = new DatasourceConnectionInfo(filePath, topicNameRespond, "");
						Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().open(info);
						if (datasource != null) {
							Dataset datast = datasource.getDatasets().get(datasource.getDatasets().getCount() - 1);
							
							IFormMap formMap = null;
							IForm form = Application.getActiveApplication().getActiveForm();
							if (form != null && form instanceof IFormMap) {
								formMap = (IFormMap)form;
							}
							
							if (formMap == null) {
								formMap = (IFormMap)CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.MAP);
							}
							
							if (formMap != null) {
								formMap.getMapControl().getMap().getLayers().add(datast, true);	
//								formMap.getMapControl().getMap().setViewBounds(datast.getBounds());
								formMap.getMapControl().getMap().refresh();
							}
						}	
					}								
				}
			});
		}
		consumer.commitSync();		
		consumer.close();
	}

}
