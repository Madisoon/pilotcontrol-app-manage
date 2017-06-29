package com.fantasi.common.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fantasi.common.db.IDBPool;

public class BaseDao {
	private final static Logger logger = Logger.getLogger(BaseDao.class);
	protected IDBPool pool = null;
	
	public BaseDao() {}
	public BaseDao(IDBPool pool) {
		this.pool = pool;
	}
	
	public void setDBPool(IDBPool pool) {
		this.pool = pool;
	}
	
	public IDBPool getDBPool() {
		return this.pool;
	}
	
	
	protected void printCallStack(Throwable e) {
		 StackTraceElement[] stackElements = e.getStackTrace();
		 StringBuffer sb = new StringBuffer();
		 if (stackElements != null) {
			 for (int i = 0; i < stackElements.length; i++) {
				 String className = stackElements[i].getClassName();
				 if (className.contains("com.fantasi")) {
					 sb.append(stackElements[i].getClassName()+"\t");
//					 sb.append(stackElements[i].getFileName()+"\t");
					 sb.append(stackElements[i].getLineNumber()+"\t");
					 sb.append(stackElements[i].getMethodName() + "\n");	 
				 }
			 }
			 logger.error(sb.toString());
	     }
	}
	
	/**
	 * 执行一条sql语句
	 * @param sql
	 * @return 返回影响行数
	 */
	public int execute(String sql) {
		return this.execute(sql, null);
	}
	
	/**
	 * 执行一条sql语句
	 * @param sql
	 * @param params
	 * @return 返回影响行数
	 */
	public int execute(String sql, String[] params) {
		Connection conn = null;
		try {
			conn = this.pool.getConnection();
			return DBHelper.execute(conn, sql, params);
		} catch (SQLException e) {
			logger.error("execute错误:" + e.getLocalizedMessage());
			printCallStack(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				//关闭数据库连接的时候出现异常了
			}
		}
		return -1;
	}
	
	public int execute(Connection conn, String sql, String[] params) throws SQLException {
		return DBHelper.execute(conn, sql, params);
	}
	
	/**
	 * 查询
	 * @param sql
	 * @return 当没有结果时返回 null
	 */
	public Map<String, String> rawQueryForMap(String sql) {
		return rawQueryForMap(sql, null);
	}
	
	/**
	 * 查询
	 * @param sql
	 * @param params
	 * @return 当没有结果时返回 null
	 */
	public Map<String, String> rawQueryForMap(String sql, String[] params) {
		List<Map<String, String>> list = this.rawQuery(sql, params);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 查询
	 * @param sql
	 * @return 当没有结果时返回 null
	 */
	public int rawQueryForInt(String sql) {
		return rawQueryForInt(sql, null);
	}
	
	/**
	 * 查询
	 * @param sql
	 * @param params
	 * @return 当没有结果时返回 null
	 */
	public int rawQueryForInt(String sql, String[] params) {
		Connection conn = null;
		try {
			conn = this.pool.getConnection();
			return DBHelper.queryForInt(conn, sql, params);
		} catch (SQLException e) {
			logger.error("rawQueryForInt错误:" + e.getLocalizedMessage());
			printCallStack(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				//关闭数据库连接的时候出现异常了
			}
		}
		return -1;
	}
	
	/**
	 * 查询
	 * @param sql
	 * @return 当没有结果集时返回 null
	 */
	public List<Map<String, String>> rawQuery(String sql) {
		return this.rawQuery(sql, null);
	}
	
	/**
	 * 查询
	 * @param sql
	 * @param params
	 * @return 当没有结果集时返回 null
	 */
	public List<Map<String, String>> rawQuery(String sql, String[] params) {
		Connection conn = null;
		try {
			conn = this.pool.getConnection();
			return DBHelper.query(conn, sql, params);
		} catch (SQLException e) {
			logger.error("rawQuery错误:" + e.getLocalizedMessage());
			printCallStack(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				//关闭数据库连接的时候出现异常了
			}
		}
		return new ArrayList<Map<String,String>>();
	}
}