package com.fantasi.common.db.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.fantasi.common.db.IDBPool;


public abstract class TransactionService {
	protected IDBPool pool = null;
	
	public TransactionService() {
		
	}
	
	public TransactionService(IDBPool pool) {
		this.pool = pool;
	}
	
	public void setDBPool(IDBPool pool) {
		this.pool = pool;
	}
	
	
	protected static interface Callback<T> {
		/**
		 * 回调方法，只处理实际的DAO操作，不必关系conn,pstmt和rs资源的资源释放问题
		 * @param conn
		 * @return
		 * @throws Exception
		 */
		T run(Connection conn) throws Exception;
	}
	
	/**
	 * 数据库操作的模板方法（模板模式）
	 * @param callback
	 * @return
	 * @throws TransactionException
	 */
	protected <T> T execute(Callback<T> callback) throws TransactionException{
		Connection conn = null;
		try {
			conn = pool.getConnection();
			conn.setAutoCommit(false);
			//执行真正的业务逻辑操作，将CDRU留给用户去实现
			T t = callback.run(conn);
			conn.commit();
			conn.setAutoCommit(true);
			
			return t;
		} catch (Exception e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					throw new TransactionException("rollback error", e1);
				}
			}
			throw new TransactionException("execute error", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new TransactionException("db close error", e);
				}
			}
		}
	}
}
