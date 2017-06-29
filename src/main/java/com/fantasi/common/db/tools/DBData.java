package com.fantasi.common.db.tools;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.fantasi.common.db.IDBPool;
public class DBData extends DBBase{
	
	public DBData(IDBPool context) {
		super(context);
	}
	
	public Object[] getDatas(Class newClass,String tableName, String whereClause, int limit) {
		DBTable table = new DBTable(this.context, tableName);
		Object[] result = null;
		String className = DBUtils.getFieldNameFromColumnName(table.getTableName(), false);
		
		
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		PreparedStatement stat1 = null;
		ResultSet rs1 = null;
		

		try {
			//Class newClass = Class.forName("cn.com.ips.dbtoclass." + className);
			Constructor cons = newClass.getConstructor();
			
			List<Column> columns = table.getColumns();
			
			conn = context.getConnection();
			String limitClause = limit > 0 ? " limit " + limit : "";
			String fields = "";
			for(Column column : columns) {
				fields += "a." + column.getColumnName() + ",";
			}
			String sql = "select " + fields.substring(0, fields.length() - 1) + " from " +
					table.getTableName() + " a " + whereClause + limitClause;
			String countSql = "select count(*) count from  " + table.getTableName() + " a "  + whereClause;
			stat1 = conn.prepareStatement(countSql);
			
			rs1 = stat1.executeQuery();
			rs1.next();
			int count = rs1.getInt("count");
			if (limit > 0 ) {
				count = count > limit ? limit : count;
			}
			result = new Object[count];
			stat = conn.prepareStatement(sql);
			rs = stat.executeQuery();
			int index = 0;
			while (rs.next()) {
				Object obj = cons.newInstance();
				for (Column column : columns) {
					Class[] args = null;
//					if (column.getColumnType().toLowerCase().contains("bigint")) {
//						args = new Class[] {long.class};
//					} else
					if (column.getColumnType().toLowerCase().contains("int")) {
						args = new Class[] {int.class};
					} else {
						args = new Class[]{String.class};	
					}
					
					Method method = newClass.getMethod("set" + DBUtils
							.getFieldNameFromColumnName(column.getColumnName(), false), args);
					method.invoke(obj, this.getDBColumnValue(rs, column.getColumnName(), column.getColumnType().toLowerCase()));
				}
				result[index++] = obj;
//				System.out.println(new java.util.Date().toString());
			}
		}  catch (Exception e) {
			e.printStackTrace();
			//logger.error("获取元数据错误" + e.getLocalizedMessage());
		} finally {
			try {
				if (stat != null) {
					stat.close();
					stat = null;
				}
			
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stat1 != null) {
					stat1.close();
					stat1 = null;
				}
			
				if (rs1 != null) {
					rs1.close();
					rs1 = null;
				}
				
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				//关闭数据库连接的时候出现异常了
			}
		}
		return result;
	}
	
	private Object getDBColumnValue(ResultSet rs ,String columnName, String columnType) throws SQLException {
		Object value = null;
//		if (columnType.contains("bigint")) {
//			value = rs.getLong(columnName);
//		} else
		if (columnType.contains("int")) {
			value = rs.getInt(columnName);
		} else if (columnType.contains("time")) {
			Timestamp time = rs.getTimestamp(columnName);
			if (time !=null) {
				//get rid of .0 of a timestamp field
				value = time.toString().substring(0, 19);
			} else {
				value = "0";
			}
		} else {
			String str = rs.getString(columnName);
			if (str != null) {
				value = DBUtils.stripNonValidXMLCharacters(str);
			} else {
				value = "";
			}
		}
		return value;
	}
}
