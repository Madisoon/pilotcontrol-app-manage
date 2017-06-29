package com.fantasi.common.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDBPool {
	public Connection getConnection() throws SQLException;
	public void close() throws SQLException;
}
