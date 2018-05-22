package com.fantasi.common.db;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;


public class DBPool implements IDBPool{
	
	private String connectURI;
	private String username;
	private String password;
	private String driver;
	
	private int maxActive = 20;
	private int maxIdle = 2;
	private int maxWait = 10000;
	
	private DataSource dataSource = null;

	public DataSource getDataSource() {
		return dataSource;
	}


	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}


	public void init(String driver, String connectURI, String username, String password) {
		this.driver = driver;
		this.connectURI = connectURI;
		this.username = username;
		this.password = password;
		dataSource = setupDataSource();
		//dataSource.setLogWriter(new java.io.PrintWriter(System.out));
	}
	
	
	public void init(String driver, String connectURI, String username, String password, int maxActive, int maxIdle) {
		
		this.maxActive = maxActive;
		this.maxIdle = maxIdle;
		this.init(driver, connectURI, username, password);
	}
	
	public void init(String driver, String connectURI, String username, String password, int maxActive, int maxIdle, int maxWait) {
		this.maxWait = maxWait;
		this.init(driver, connectURI, username, password, maxActive, maxIdle);
	}
	
	private DataSource setupDataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driver);
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setUrl(connectURI);
		ds.setValidationQuery("select 1 from dual");
		ds.setRemoveAbandoned(true);
		ds.setTestWhileIdle(true);
		ds.setTestOnBorrow(true);
		ds.setTestOnReturn(false);
		ds.setMaxActive(maxActive);
		ds.setMaxIdle(maxIdle);
		ds.setMaxWait(maxWait);
		return ds;
	}
	
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
	
	public void close() throws SQLException {
		DriverManager.deregisterDriver(DriverManager.getDriver(connectURI));
	}
	
	public String getConnectionInfo() {
		BasicDataSource ds = (BasicDataSource)dataSource;
		return "[" + ds.getNumActive() + "," + ds.getNumIdle() + "]";
	}
}
