package com.fantasi.common.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.fantasi.common.db.task.CheckDBConnectionTask;

public class DBPoolFactory {
	
	private static Logger logger = Logger.getLogger(DBPoolFactory.class);
	

	public static DBPool getWebDefautDBPool() {
		URL url = DBPoolFactory.class.getResource("/application.properties");
		if (url != null) {
			String path = url.getPath();
			return initFromProperties(path);
		}
		logger.error("getWebDefautDBPool:找不到jdbc.properties");
		return null;
	}
	
	private static DBPool initFromProperties(String path) {
		Properties props = new Properties();
		String connectURI = "";
		String username = "";
		String password = "";
		String driver = "";
		int refreshInterval = 10 * 1000;
		int printInterval = 10 * 60000;
		boolean enable = true;
		try {
			props.load(new FileInputStream(path));
			driver = props.getProperty("jdbc.driverClassName");
			connectURI = props.getProperty("jdbc.url");
			username = props.getProperty("jdbc.username");
			password = props.getProperty("jdbc.password");
			if (props.getProperty("jdbc.log.enable") != null) {
				enable = Boolean.parseBoolean(props.getProperty("jdbc.log.enable").trim());
			}
			if (props.getProperty("jdbc.log.refreshInterval") != null) {
				refreshInterval = Integer.parseInt(props.getProperty("jdbc.log.refreshInterval").trim());
			}
			if (props.getProperty("jdbc.log.printInterval") != null) {
				printInterval = Integer.parseInt(props.getProperty("jdbc.log.printInterval").trim());
			}
			logger.info("初始化数据库" + connectURI + "," + username + "," + password);
			DBPool pool = new DBPool();
			pool.init(driver, connectURI, username, password);
			
			if (enable) {
				logger.info("初始化数据库连接信息,refreshInterval=" + (refreshInterval/1000) + "秒,printInterval=" + (printInterval/60000) + "分");	
				CheckDBConnectionTask task = new CheckDBConnectionTask(pool, refreshInterval, printInterval);
				task.start();
			}
			return pool;
		} catch (IOException e) {
			logger.error("初始化数据库失败:" + e.getLocalizedMessage());
		}
		return null;
	}
}
