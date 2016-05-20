package com.supermap.desktop.CtrlAction;

import java.util.HashMap;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.supermap.desktop.Application;

/**
 * Hello world!
 *
 */
public class lbsCommandProducer {

	/**
	 * 用于往kafka服务中发送查询命令
	 * 
	 * @param args
	 */
	public static void commandProducer(String[] args) {
		if (args.length < 3) {
			System.err.println("Usage:  <metadataBrokerList> <topic> <comand> ");
			return;
		}

		String brokers = args[0];
		String topic = args[1];
		String command = args[2];
//		String secondes = args[3];
//		int second = Integer.parseInt(secondes);

		// Zookeeper connection properties
		HashMap<String, Object> props = new HashMap<String, Object>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);

		// Send some messages
		System.out.println("正在模拟往服务端发出查询任务，查询命令为：");
		System.out.println(command);
		int index = 0;
		while (index <= 0) {
			ProducerRecord<String, String> message = new ProducerRecord<String, String>(topic, null, String.valueOf(index),
					command);
			producer.send(message);
			index++;
			
//			// 5s发送次命令
//			try {
//				Thread.sleep(second*1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}

	}
}
