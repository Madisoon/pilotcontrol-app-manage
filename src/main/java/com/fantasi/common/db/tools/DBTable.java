package com.fantasi.common.db.tools;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fantasi.common.db.IDBPool;

public class DBTable extends DBBase {
	
	public DBTable(String tableName) {
		this.tableName = tableName;
	}
	
	public DBTable(IDBPool context, String tableName) {
		super(context);
		this.tableName = tableName;
	}
	
	private String tableName;
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public List<Column> getPrimaryKeys() {
		Connection conn = null;
		DatabaseMetaData metadata = null;
		ResultSet colRet = null;
		List<Column> columns = new ArrayList<Column>();
        try {
        	conn = context.getConnection();
			metadata = conn.getMetaData();
			colRet = metadata.getPrimaryKeys(null, null, tableName);
			while(colRet.next()) {
				Column c = new Column();
				c.setColumnName(colRet.getString("COLUMN_NAME")); 
//				c.setColumnType(colRet.getString("TYPE_NAME")); 
//				c.setColumnSize(colRet.getInt("COLUMN_SIZE")); 
//				c.setDecimalDigits(colRet.getInt("DECIMAL_DIGITS")); 
//				c.setNullable(colRet.getInt("NULLABLE"));
				columns.add(c);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (colRet != null) {
				try {
					colRet.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}	
		return columns;
	}

	public List<Column> getColumns() {
		Connection conn = null;
		DatabaseMetaData metadata = null;
		ResultSet colRet = null;
		List<Column> columns = new ArrayList<Column>();
		try {
			conn = context.getConnection();
			metadata = conn.getMetaData();
			colRet = metadata.getColumns(null,"%", tableName,"%"); 
			while(colRet.next()) {
				Column c = new Column();
				c.setColumnName(colRet.getString("COLUMN_NAME")); 
				c.setColumnType(colRet.getString("TYPE_NAME")); 
				c.setColumnSize(colRet.getInt("COLUMN_SIZE")); 
				c.setDecimalDigits(colRet.getInt("DECIMAL_DIGITS")); 
				c.setNullable(colRet.getInt("NULLABLE"));
				columns.add(c);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (colRet != null) {
				try {
					colRet.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}	
		return columns;
	}
	
	public static void main(String[] args) {
		
	}
}
