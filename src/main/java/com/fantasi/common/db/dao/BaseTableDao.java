package com.fantasi.common.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fantasi.common.db.IDBPool;
import com.fantasi.common.db.process.Filter;
import com.fantasi.common.db.process.SelectParam;

public class BaseTableDao extends BaseDao {
	private final static Logger logger = Logger.getLogger(BaseTableDao.class);
	protected String[] primaryKeys;

	public BaseTableDao(IDBPool pool) {
		super(pool);
		this.primaryKeys = new String[]{"id"};
	}

	public BaseTableDao(IDBPool pool, String[] keys) {
		super(pool);
		this.primaryKeys = keys;
	}
	public void setPrimaryKeys(String[] keys) {
		this.primaryKeys = keys;
	}

	public int insertTable(Connection conn, String table, Map<String, String> data) throws SQLException {
		if (data == null) {
			return 0;
		}

		String columnStr = "";
		String valueStr = "";
		String[] params = new String[data.keySet().size()];
		int index = 0;
		for (String key : data.keySet()) {
			columnStr += '`' + key + "`,";
			valueStr += "?,";
			params[index++] = data.get(key);
		}
		columnStr = columnStr.substring(0, columnStr.length() - 1);
		valueStr = valueStr.substring(0, valueStr.length() - 1);

		String sql = "insert into " + table + "(" + columnStr + ")"
				+ " values (" + valueStr + ");";

		return DBHelper.execute(conn, sql, params);
	}

	public int insertTable(String table, Map<String, String> data) {
		if (data == null) {
			return 0;
		}
		Connection conn = null;
		try {
			conn = pool.getConnection();
			return insertTable(conn, table, data);
		} catch (Exception e) {
			logger.error("insert错误:" + e.getLocalizedMessage());
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

	public int insertTable(String table, List<Map<String, String>> datas) {
		if (datas == null || datas.size() == 0) {
			return 0;
		}
		Connection conn = null;
		try {
			conn = pool.getConnection();
			conn.setAutoCommit(false);
			int result = 0;
			for (Map<String, String> map : datas) {
				result += this.insertTable(conn, table, map);
			}
			conn.commit();
			return result;
		} catch (Exception e) {
			logger.error("insert错误:" + e.getLocalizedMessage());
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

	public int insertTable(Connection conn, String table, List<Map<String, String>> datas) throws SQLException {
		if (datas == null || datas.size() == 0) {
			return 0;
		}
		int result = 0;
		for (Map<String, String> map : datas) {
			result += this.insertTable(conn, table, map);
		}
		return result;
	}

	public int insertTableAndReturn(String table, Map<String, String> data) {
		if (data == null) {
			return 0;
		}
		Connection conn = null;
		try {
			conn = pool.getConnection();
			conn.setAutoCommit(false);
			String columnStr = "";
			String valueStr = "";
			String[] params = new String[data.keySet().size()];
			int index = 0;
			for (String key : data.keySet()) {
				columnStr += '`' + key + "`,";
				valueStr += "?,";
				params[index++] = data.get(key);
			}
			columnStr = columnStr.substring(0, columnStr.length() - 1);
			valueStr = valueStr.substring(0, valueStr.length() - 1);

			String sql = "insert into " + table + "(" + columnStr + ")"
					+ " values (" + valueStr + ");";
			DBHelper.execute(conn, sql, params);
			Map<String, String> map = DBHelper.queryForMap(conn, "SELECT LAST_INSERT_ID() as id;");
			conn.commit();
			return Integer.parseInt(map.get("id"));

		} catch (Exception e) {
			logger.error("insertTableAndReturn错误:" + e.getLocalizedMessage());
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


	public int insertTableAndReturn(Connection conn, String table, Map<String, String> data) throws SQLException {
		if (data == null) {
			return 0;
		}

		String columnStr = "";
		String valueStr = "";
		String[] params = new String[data.keySet().size()];
		int index = 0;
		for (String key : data.keySet()) {
			columnStr += '`' + key + "`,";
			valueStr += "?,";
			params[index++] = data.get(key);
		}
		columnStr = columnStr.substring(0, columnStr.length() - 1);
		valueStr = valueStr.substring(0, valueStr.length() - 1);

		String sql = "insert into " + table + "(" + columnStr + ")"
				+ " values (" + valueStr + ");";
		DBHelper.execute(conn, sql, params);
		Map<String, String> map = DBHelper.queryForMap(conn, "SELECT LAST_INSERT_ID() as id;");
		return Integer.parseInt(map.get("id"));
	}

	public int updateTable(String table, Map<String, String> data) {
		if (data == null) {
			return 0;
		}
		String where = "";
		String[] params = new String[primaryKeys.length];
		for (int i = 0; i < primaryKeys.length; i++) {
			where += primaryKeys[i] + "=?";
			params[i] = data.get(primaryKeys[i]);
			if (i < primaryKeys.length - 1) {
				where += " and ";
			}
		}
		return this.updateTable(table, where, params, data);
	}

	public int updateTable(Connection conn, String table, Map<String, String> data) throws SQLException {
		if (data == null) {
			return 0;
		}
		String where = "";
		String[] params = new String[primaryKeys.length];
		for (int i = 0; i < primaryKeys.length; i++) {
			where += primaryKeys[i] + "=?";
			params[i] = data.remove(primaryKeys[i]);
			if (i < primaryKeys.length - 1) {
				where += " and ";
			}
		}
		return this.updateTable(conn, table, where, params, data);
	}

	public int updateTable(Connection conn, String table, List<Map<String, String>> datas) throws SQLException {
		int result = 0;
		for (Map<String, String> map : datas) {
			result += this.updateTable(conn, table, map);
		}
		return result;
	}

	public int updateTable(String table, List<Map<String, String>> datas) {
		if (datas == null || datas.size() == 0) {
			return 0;
		}
		Connection conn = null;
		try {
			conn = pool.getConnection();
			conn.setAutoCommit(false);
			int result = this.updateTable(conn, table, datas);
			conn.commit();
			return result;
		} catch (Exception e) {
			logger.error("update错误:" + e.getLocalizedMessage());
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

	public int updateTable(Connection conn, String table, String whereClause, String[] whereParams, Map<String, String> data) throws SQLException {
		if (data == null) {
			return 0;
		}

		if (!(whereClause == null || whereClause.equals(""))) {
			whereClause = " where " + whereClause;
		}

		String[] params = new String[whereParams.length + data.keySet().size()];

		String columnStr = " set ";
		int index = 0;
		for (String key : data.keySet()) {
			columnStr += '`' + key + "`=?,";
			params[index++] = data.get(key);
		}
		if (whereParams != null) {
			for (String param : whereParams) {
				params[index++] = param;
			}
		}
		columnStr = columnStr.substring(0, columnStr.length() - 1);
		String sql = "update " + table + columnStr + whereClause;

		return DBHelper.execute(conn, sql, params);

	}
	public int updateTable(Connection conn, String table, List<Filter> filters, String whereClause, String[] whereParams, Map<String, String> data) throws SQLException {
		if (data == null) {
			return 0;
		}

		SelectParam sp = getSelectParam(filters, whereClause, whereParams);

		String[] params = new String[sp.getParams().length + data.keySet().size()];

		String columnStr = " set ";
		int index = 0;
		for (String key : data.keySet()) {
			columnStr += '`' + key + "`=?,";
			params[index++] = data.get(key);
		}
		if (whereParams != null) {
			for (String param : whereParams) {
				params[index++] = param;
			}
		}
		for (String param : sp.getParams()) {
			params[index++] = param;
		}
		columnStr = columnStr.substring(0, columnStr.length() - 1);
		String sql = "update " + table + columnStr + sp.getWhereClause();

		return DBHelper.execute(conn, sql, params);

	}

	public int updateTable(String table, List<Filter> filters, String whereClause, String[] params, Map<String, String> data) {
		if (data == null) {
			return 0;
		}
		Connection conn = null;
		try {
			conn = pool.getConnection();
			return updateTable(conn, table, filters, whereClause, params, data);
		} catch (Exception e) {
			logger.error("update错误:" + e.getLocalizedMessage());
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


	public int updateTable(String table, String whereClause, String[] params, Map<String, String> data) {
		if (data == null) {
			return 0;
		}
		Connection conn = null;
		try {
			conn = pool.getConnection();
			return updateTable(conn, table, whereClause, params, data);
		} catch (Exception e) {
			logger.error("update错误:" + e.getLocalizedMessage());
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


	public int replaceTable(Connection conn, String table, Map<String, String> data) throws SQLException {
		String[] params = new String[this.primaryKeys.length];
		int index = 0;
		String whereClause = "";
		for (String key : this.primaryKeys) {
			if (!data.containsKey(key)) {
				return 0;
			} else {
				whereClause += key + "=? and ";
				params[index++] = data.get(key);
				//data.remove(key);
			}
		}
		whereClause = whereClause.substring(0, whereClause.length()-4);
		int result  = updateTable(conn, table, whereClause, params, data);
		if (result > 0) {
			return result;
		} else {
			return insertTable(conn, table, data);
		}
	}

	public int replaceTable(String table, Map<String, String> data) {
		if (data == null) {
			return 0;
		}
		Connection conn = null;
		try {
			conn = pool.getConnection();
			return replaceTable(conn, table, data);
		} catch (Exception e) {
			logger.error("replace错误:" + e.getLocalizedMessage());
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

	public int replaceTable(String table, List<Map<String, String>> datas) {
		if (datas == null || datas.size() == 0) {
			return 0;
		}
		Connection conn = null;
		try {
			conn = pool.getConnection();
			conn.setAutoCommit(false);
			int result = 0;
			for (Map<String, String> map : datas) {
				result += this.replaceTable(conn, table, map);
			}
			conn.commit();
			return result;
		} catch (Exception e) {
			logger.error("replace错误:" + e.getLocalizedMessage());
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

	public int deleteTable(Connection conn, String table, List<Filter> filters) throws SQLException {
		SelectParam sp = getSelectParam(filters, "", null);
		String sql = "delete from " + table + sp.getWhereClause();
		return DBHelper.execute(conn, sql, sp.getParams());
	}


	public int deleteTable(String table, List<Filter> filters) {
		Connection conn = null;
		try {
			conn = pool.getConnection();
			return this.deleteTable(conn, table, filters);
		} catch (Exception e) {
			logger.error("deleteTable wrong:" + e.getLocalizedMessage());
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

	public int deleteTable(String table, String whereClause, String[] params) {
		Connection conn = null;
		try {
			conn = pool.getConnection();
			return this.deleteTable(conn, table, whereClause, params);
		} catch (Exception e) {
			logger.error("delete错误:" + e.getLocalizedMessage());
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

	public int deleteTable(Connection conn, String table, String whereClause, String[] params) throws SQLException {
		if (!(whereClause == null || whereClause.equals(""))) {
			whereClause = " where " + whereClause;
		}
		String sql = "delete from " + table + whereClause;
		return DBHelper.execute(conn, sql, params);
	}

	public Map<String, String> queryTableForMap(String table, String whereClause, String[] params) {
		List<Map<String, String>> list = this.queryTable(table, whereClause, params);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public Map<String, String> queryTableForMap(Connection conn, String table, String whereClause, String[] params) throws SQLException {
		return queryTableForMap(conn, table, null, whereClause, params);
	}

	public Map<String, String> queryTableForMap(Connection conn, String table, Filter filter, String whereClause, String[] params) throws SQLException {

		SelectParam sp = getSelectParam(filter, whereClause, params);
		String sql = "select * from " +
				table + sp.getWhereClause();

		return DBHelper.queryForMap(conn, sql, sp.getParams());
	}

	public int queryTableForInt(String table, String field, String where, String[] params) {
		return this.queryTableForInt(table, field, null, where, params);
	}

	public int queryTableForInt(String table, String field, List<Filter> filters, String where, String[] params) {
		Connection conn = null;
		try {
			conn = pool.getConnection();
			return this.queryTableForInt(conn, table, field, filters, where, params);
		} catch (Exception e) {
			logger.error("queryTableForInt错误:" + e.getLocalizedMessage());
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

	public int queryTableForInt(String table, String field, String where, String[] params, Filter filter) {
		Connection conn = null;
		try {
			conn = pool.getConnection();
			return this.queryTableForInt(conn, table, field, where, params, filter);
		} catch (Exception e) {
			logger.error("queryTableForInt错误:" + e.getLocalizedMessage());
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

	public int queryTableForInt(Connection conn, String table, String field, List<Filter> filters, String where, String[] params) throws SQLException {
		SelectParam sp = getSelectParam(filters, where, params);
		String sql = "select " + field + " from " +
				table + sp.getWhereClause();
		return DBHelper.queryForInt(conn, sql, sp.getParams());
	}

	public int queryTableForInt(Connection conn, String table, String field, String where, String[] params, Filter filter) throws SQLException {
		SelectParam sp = getSelectParam(filter, where, params);
		String sql = "select " + field + " from " +
				table + sp.getWhereClause();
		return DBHelper.queryForInt(conn, sql, sp.getParams());
	}

	public long queryTableForLong(String table, String field) {
		return this.queryTableForLong(table, field, null, null, null);
	}

	public long queryTableForLong(String table, String field, String where, String[] params) {
		return this.queryTableForLong(table, field, null, where, params);
	}

	public long queryTableForLong(String table, String field, List<Filter> filters, String where, String[] params) {
		Connection conn = null;
		try {
			conn = pool.getConnection();
			return this.queryTableForLong(conn, table, field, filters, where, params);
		} catch (Exception e) {
			logger.error("queryTableForInt错误:" + e.getLocalizedMessage());
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

	public long queryTableForLong(Connection conn, String table, String field, List<Filter> filters, String where, String[] params) throws SQLException {
		SelectParam sp = getSelectParam(filters, where, params);
		String sql = "select " + field + " from " +
				table + sp.getWhereClause();
		return DBHelper.queryForLong(conn, sql, sp.getParams());
	}

	public int queryTableCount(String table, List<Filter> filters, String where, String[] params) {
		Connection conn = null;
		try {
			conn = pool.getConnection();
			return this.queryTableCount(conn, table, filters, where, params);
		} catch (Exception e) {
			logger.error("queryTableCount错误:" + e.getLocalizedMessage());
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

	public int queryTableCount(String table, String where, String[] params, Filter filter) {
		Connection conn = null;
		try {
			conn = pool.getConnection();
			return this.queryTableCount(conn, table, where, params, filter);
		} catch (Exception e) {
			logger.error("queryTableCount错误:" + e.getLocalizedMessage());
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


	public int queryTableCount(Connection conn, String table, List<Filter> filters, String where, String[] params) throws SQLException {
		SelectParam sp = getSelectParam(filters, where, params);
		String sql = "select count(*) from " +
				table + sp.getWhereClause();
		return DBHelper.queryForInt(conn, sql, sp.getParams());
	}

	public int queryTableCount(Connection conn, String table, String where, String[] params, Filter filter) throws SQLException {
		SelectParam sp = getSelectParam(filter, where, params);
		String sql = "select count(*) from " +
				table + sp.getWhereClause();
		return DBHelper.queryForInt(conn, sql, sp.getParams());
	}

	public List<Map<String, String>> queryTable(String table) {
		Connection conn = null;
		try {
			conn = pool.getConnection();
			String sql = "select * from " +
					table ;
			return DBHelper.query(conn, sql, null);
		}  catch (Exception e) {
			logger.error("query错误:" + e.getLocalizedMessage());
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

	public List<Map<String, String>> queryTable(String table, String where, String[] params) {
		return this.queryTable(table, null, null, where, params, null, null, null, null);
	}

	public List<Map<String, String>> queryTable(Connection conn, String table, String[] columns, List<Filter> filters, String where, String[] params,
			String groupBy, String having, String orderBy, String limit) throws SQLException {


		SelectParam sp = getSelectParam(filters, where, params);
		String columnStr = getColumnStr(columns);
		String groupByClause = getGroupByHaving(groupBy, having);
		String orderByCluase = getOrderByClause(orderBy);
		String limitByCluase = getLimitClause(limit);

		String sql = "select " + columnStr + " from " +
				table + sp.getWhereClause() + groupByClause + orderByCluase + limitByCluase;
		return DBHelper.query(conn, sql, sp.getParams());

	}

	public List<Map<String, String>> queryTable(String table, String[] columns, List<Filter> filters, String where, String[] params,
			String groupBy, String having, String orderBy, String limit) {


		SelectParam sp = getSelectParam(filters, where, params);
		String columnStr = getColumnStr(columns);
		String groupByClause = getGroupByHaving(groupBy, having);
		String orderByCluase = getOrderByClause(orderBy);
		String limitByCluase = getLimitClause(limit);

		Connection conn = null;
		try {
			conn = pool.getConnection();
			String sql = "select " + columnStr + " from " +
					table + sp.getWhereClause() + groupByClause + orderByCluase + limitByCluase;
			return DBHelper.query(conn, sql, sp.getParams());
		}  catch (Exception e) {
			logger.error("query错误:" + e.getLocalizedMessage());
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

	public List<Map<String, String>> queryTable(String table, String[] columns, String where, String[] params, Filter filter,
			String groupBy, String having, String orderBy, String limit) {


		SelectParam sp = getSelectParam(filter, where, params);
		String columnStr = getColumnStr(columns);
		String groupByClause = getGroupByHaving(groupBy, having);
		String orderByCluase = getOrderByClause(orderBy);
		String limitByCluase = getLimitClause(limit);

		Connection conn = null;
		try {
			conn = pool.getConnection();
			String sql = "select " + columnStr + " from " +
					table + sp.getWhereClause() + groupByClause + orderByCluase + limitByCluase;
			return DBHelper.query(conn, sql, sp.getParams());
		}  catch (Exception e) {
			logger.error("query错误:" + e.getLocalizedMessage());
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


	private SelectParam getSelectParam(Filter filter, String where, String[] params) {
		SelectParam sp = Filter.getFilterParams(filter);
		sp.setWhereClause(getWhereClause(sp.getWhereClause(), where));
		sp.setParams(getParams(sp.getParams(), params));
		return sp;
	}

	private SelectParam getSelectParam(List<Filter> filters, String where, String[] params) {
		SelectParam sp = Filter.getFilterParams(filters);
		sp.setWhereClause(getWhereClause(sp.getWhereClause(), where));
		sp.setParams(getParams(sp.getParams(), params));
		return sp;
	}

	private String getColumnStr (String[] columns) {

		if (columns != null && columns.length != 0) {
			String columnStr = "";
			for (String col : columns) {
				if (col.contains(" as ")) {
					columnStr += col + ",";
				} else {
					columnStr += "`" + col + "`,";
				}
			}
			return columnStr.substring(0, columnStr.length() - 1);
		} else {
			return "*";
		}
	}

	private String[] getParams(String[] filterParams, String[] params) {
		String[] newParams = null;
		if (params == null || params.length == 0) {
			newParams = filterParams;
		} else {
			newParams = new String[filterParams.length + params.length];
			int index = 0;
			for (String str : filterParams) {
				newParams[index++] = str;
			}
			for (String str : params) {
				newParams[index++] = str;
			}
		}
		return newParams;
	}

	private String getWhereClause(String filterWhere, String where) {
		String whereClause = "";
		if (where == null || where.equals("")) {
			if (!filterWhere.equals("")) {
				whereClause= filterWhere;
			}
		} else {
			if (!filterWhere.equals("")) {
				whereClause= filterWhere + " and " + where;
			} else {
				whereClause = " where " + where;
			}
		}
		return whereClause;
	}

	private String getGroupByHaving(String groupBy, String having) {
		if (groupBy == null || groupBy.equals("")) {
			return "";
		}
		if (having == null || having.equals("")) {
			return " group by " + groupBy;
		}
		return " group by " + groupBy + " having " + having;
	}

	private String getOrderByClause(String orderBy) {
		if (orderBy == null || orderBy.equals("")) {
			return "";
		}
		return " order by " + orderBy;
	}

	private String getLimitClause(String limit) {
		if (limit == null || limit.equals("")) {
			return "";
		}
		return " limit " + limit;
	}

}
