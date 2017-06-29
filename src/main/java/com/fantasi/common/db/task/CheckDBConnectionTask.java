package com.fantasi.common.db.task;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.fantasi.common.db.DBPool;

public class CheckDBConnectionTask {
	private static Logger logger = Logger.getLogger(CheckDBConnectionTask.class);
	
	private DBPool pool = null;
	private long refreshInterval = 10 * 1000;
	private long printInterval = 10 * 60000;
	
	public CheckDBConnectionTask(DBPool pool) {
		this.pool = pool;
	}
	
	public CheckDBConnectionTask(DBPool pool, long refreshInterval, long printInterval) {
		this(pool);
		this.refreshInterval = refreshInterval * 1000;
		this.printInterval = printInterval * 1000;
	}
	
	Timer timer = new Timer();
	public void start() {
		final StringBuffer sb = new StringBuffer();
		
		TimerTask task = new TimerTask() {   
		    public void run() {
		    	sb.append(pool.getConnectionInfo());
		    }   
		};
		
		TimerTask printTask = new TimerTask() {
			@Override
			public void run() {
				logger.info(sb.toString());
				sb.setLength(0);
			}
		};
		
		timer.schedule(task, 0, refreshInterval);
		
		timer.schedule(printTask, printInterval, printInterval);
	}
	
	public void stop() {
		timer.cancel();
	}
}
