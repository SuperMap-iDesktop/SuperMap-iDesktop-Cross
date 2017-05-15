package com.supermap.desktop.dialog.cacheClip.cache;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


class SubprocessThread extends Thread {
	private  ArrayList<String> m_arguments;
	private long m_start;
	private Process m_ps;
	private InputStream is1;
	public static long TimeOutMS = 15*60*1000;
	public volatile boolean isExit = false;
	
	public SubprocessThread( ArrayList<String> arguments ){
		m_arguments = arguments;
		m_start = 0;
	}
	
	/**
	 * 判断是否超时，如果超时kill掉，默认超时时间为10分钟，超过10分钟Kill之
	 */
	public void timeout(){
		// 超时的时间设置
		synchronized(this){
			long cost = System.currentTimeMillis() - m_start;
			if(m_start > 0 && cost > TimeOutMS){
				int psHash = m_ps.hashCode();
				LogWriter log = LogWriter.getInstance();
				log.writelog("time out and kill it, PIDHASH:" + psHash);
				m_ps.destroy();				
			}			
		}		
	}		
	
	@Override
	 public void run() {
		LogWriter log = LogWriter.getInstance();
		try {			
			ProcessBuilder builder = new ProcessBuilder(m_arguments);
			builder.redirectErrorStream(true);
			m_ps = builder.start();
			
			int psHash = m_ps.hashCode();
			is1 = m_ps.getInputStream();
			String osName = System.getProperty("os.name").toLowerCase();
			BufferedReader br1;
			if (osName.startsWith("linux")) {
				br1 = new BufferedReader(new InputStreamReader(is1, "UTF-8"));
			} else {
				br1 = new BufferedReader(new InputStreamReader(is1, "GBK"));
			}
			String line1 = null;
			
			// 这里会自己等待读取到内容，不需要调用waitFor方法。
			while ((line1 = br1.readLine()) != null) {
				m_start = System.currentTimeMillis();
				//log.writelog("PIDHASH:"+psHash +"," + line1);
				log.writelog(line1);	
			}
			log.flush();
			is1.close();
			isExit = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

}
