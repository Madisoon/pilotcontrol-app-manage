package com.fantasi.common.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.fantasi.common.db.IDBPool;
import com.fantasi.common.db.process.Filter;


public class BaseDictDao extends BaseTableDao {
	public String tableName;
	
	public BaseDictDao(IDBPool pool,String tableName) {
		super(pool);
		this.tableName = tableName;
	}
	
	public BaseDictDao(IDBPool pool,String tableName, String[] primaryKeys) {
		super(pool, primaryKeys);
		this.tableName = tableName;
	}
	
	public int insert(Map<String, String> data) {
		return this.insertTable(this.tableName, data);
	}
	
	public int insert(Connection conn, Map<String, String> data) throws SQLException {
		return this.insertTable(conn, this.tableName, data);
	}
	
	public int insert(List<Map<String, String>> datas) {
		return this.insertTable(this.tableName, datas);
	}
	
	public int insert(Connection conn, List<Map<String, String>> datas) throws SQLException {
		return this.insertTable(conn, this.tableName, datas);
	}
	
	public int insertAndReturn(Map<String, String> data) {
		return this.insertTableAndReturn(this.tableName, data);
	}
	
	public int insertAndReturn(Connection conn, Map<String, String> data) throws SQLException {
		return this.insertTableAndReturn(conn, this.tableName, data);
	}
	
	public int update(Map<String, String> data) {

		return this.updateTable(this.tableName, data);
	}
	
	public int update(List<Map<String, String>> datas) {
		return this.updateTable(this.tableName, datas);
	}
	
	public int update(String whereClause, String[] params, Map<String, String> data) {
		return this.updateTable(this.tableName, whereClause, params, data);
	}
	
	public int update(List<Filter> filters, Map<String, String> data) {
		return this.updateTable(this.tableName, filters, null, null, data);
	}
	
	public int update(List<Filter> filters, String whereClause, String[] params, Map<String, String> data) {
		return this.updateTable(this.tableName, filters, whereClause, params, data);
	}
	
	public int update(Connection conn, Map<String, String> data) throws SQLException {
		String where = "";
		String[] params = new String[primaryKeys.length];
		for (int i = 0; i < primaryKeys.length; i++) {
			where += primaryKeys[i] + "=?";
			params[i] = data.get(primaryKeys[i]);
			if (i < primaryKeys.length - 1) {
				where += " and ";
			}
		}
		return this.updateTable(conn, this.tableName, where, params, data);
	}
	
	
	public int update(Connection conn, String whereClause, String[] params, Map<String, String> data) throws SQLException {
		return this.updateTable(conn, this.tableName, whereClause, params, data);
	}
	
	public int update(Connection conn, List<Filter> filters, String whereClause, String[] params, Map<String, String> data) throws SQLException {
		return this.updateTable(conn, this.tableName, filters, whereClause, params, data);
	}
	
	public int update(Connection conn, List<Filter> filters, Map<String, String> data) throws SQLException{
		return this.updateTable(conn, this.tableName, filters, null, null, data);		
	}
	
	public int replace(Connection conn, Map<String, String> data) throws SQLException {
		return this.replaceTable(conn, this.tableName, data);
	}
	
	public int replace(Map<String, String> data) {
		return this.replaceTable(this.tableName, data);
	}
	
	public int replace(List<Map<String, String>> datas) {
		return this.replaceTable(this.tableName, datas);
	}
	
	public int delete(String whereClause) {
		return this.delete(whereClause, null);
	}
	
	public int delete(List<Filter> filters) {
		return this.deleteTable(this.tableName, filters);
	}
	
	public int delete(Connection conn, List<Filter> filters) throws SQLException {
		return this.deleteTable(conn, this.tableName, filters);
	}
	
	public int delete(String whereClause, String[] params) {
		return this.deleteTable(this.tableName, whereClause, params);
	}
	
	public int delete(Connection conn, String whereClause, String[] params) throws SQLException {
		return this.deleteTable(conn, this.tableName, whereClause, params);
	}
	
	
	public Map<String, String> queryForMap(String whereClause, String[] params) {
		List<Map<String, String>> list = this.query(whereClause, params);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	public Map<String, String> queryForMap(String[] columns, String whereClause, String[] params) {
		List<Map<String, String>> list = this.query(columns, whereClause, params);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	public Map<String, String> queryForMap(Connection conn, String whereClause, String[] params) throws SQLException {
		return this.queryTableForMap(conn, this.tableName, whereClause, params);
	}
	
	public int queryForInt(String field, List<Filter> filters, String where, String[] params) {
		return queryTableForInt(this.tableName, field, filters, where, params);
	}
	
	public int queryForInt(Connection conn, String field, List<Filter> filters, String where, String[] params) throws SQLException {
		return queryTableForInt(conn, this.tableName, field, filters, where, params);
	}
	
	public long queryForLong(String field) {
		return queryTableForLong(this.tableName, field);
	}
	
	public long queryForLong(String field, String where, String[] params) {
		return queryTableForLong(this.tableName, field, null, where, params);
	}
	
	public long queryForLong(String field, List<Filter> filters, String where, String[] params) {
		return queryTableForLong(this.tableName, field, filters, where, params);
	}
	
	public long queryForLong(Connection conn, String field, List<Filter> filters, String where, String[] params) throws SQLException {
		return queryTableForLong(conn, this.tableName, field, filters, where, params);
	}
	
	public int queryCount(List<Filter> filters) {
		return this.queryTableCount(this.tableName, filters, null, null);
	}
	
	public int queryCount(Filter filter) {
		return this.queryTableCount(this.tableName, null, null, filter);
	}
	
	public int queryCount(String where) {
		return this.queryTableCount(this.tableName, null, where, null);
	}
	
	public int queryCount(String where, String[] params) {
		return this.queryTableCount(this.tableName, null, where, params);
	}
	
	public int queryCount(List<Filter> filters, String where, String[] params) {
		return this.queryTableCount(this.tableName, filters, where, params);
	}
	
	public int queryCount(Connection conn, List<Filter> filters) throws SQLException {
		return this.queryTableCount(conn, this.tableName, filters, null, null);
	}
	
	public int queryCount(Connection conn, String where) throws SQLException {
		return this.queryTableCount(conn, this.tableName, null, where, null);
	}
	
	public int queryCount(Connection conn, String where, String[] params) throws SQLException {
		return this.queryTableCount(conn, this.tableName, null, where, params);
	}
	
	public int queryCount(Connection conn, List<Filter> filters, String where, String[] params) throws SQLException {
		return this.queryTableCount(conn, this.tableName, filters, where, params);
	}
	
	public List<Map<String, String>> query() {
		return this.queryTable(this.tableName);
	}
	
	public List<Map<String, String>> query(List<Filter> filters) {
		return this.queryTable(this.tableName, null, filters, null, null, null, null, null, null);
	}
	
	public List<Map<String, String>> query(List<Filter> filters, String orderBy) {
		return this.queryTable(this.tableName, null, filters, null, null, null, null, orderBy, null);
	}
	
	public List<Map<String, String>> query(Filter filter, String orderBy, String limit) {
		return this.queryTable(this.tableName, null, null, null, filter, null, null, orderBy, limit);
	}
	
	public List<Map<String, String>> query(List<Filter> filters, String orderBy, String limit) {
		return this.queryTable(this.tableName, null, filters, null, null, null, null, orderBy, limit);
	}
	
	public List<Map<String, String>> query(String[] columns, List<Filter> filters, String groupBy, String having) {
		return this.queryTable(this.tableName, columns, filters, null, null, groupBy, having, null, null);
	}
	
	public List<Map<String, String>> query(Connection conn, String where, String[] params) throws SQLException {
		return this.queryTable(conn, this.tableName, null, null, where, params, null, null, null, null);
	}
	
	public List<Map<String, String>> query(String where, String[] params) {
		return this.queryTable(this.tableName, null, null, where, params, null, null, null, null);
	}
	
	public List<Map<String, String>> query(Connection conn, String where, String[] params, String orderBy) throws SQLException {
		return this.queryTable(conn, this.tableName, null, null, where, params, null, null, orderBy, null);
	}
	
	public List<Map<String, String>> query(String where, String[] params, String orderBy) {
		return this.queryTable(this.tableName, null, null, where, params, null, null, orderBy, null);
	}
	
	public List<Map<String, String>> query(String where, String[] params, String orderBy, String limit) {
		return this.queryTable(this.tableName, null, null, where, params, null, null, orderBy, limit);
	}
	
	public List<Map<String, String>> query(String where, String[] params, String groupBy, String having, String limit) {
		return this.queryTable(this.tableName, null, null, where, params, groupBy, having, null, limit);
	}
	
	public List<Map<String, String>> query(Connection conn, String[] columns, String where, String[] params) throws SQLException {
		return this.queryTable(conn, this.tableName, columns, null, where, params, null, null, null, null);
	}
	
	public List<Map<String, String>> query(String[] columns, String where, String[] params) {
		return this.queryTable(this.tableName, columns, null, where, params, null, null, null, null);
	}
	
	public List<Map<String, String>> query(Connection conn, String[] columns, String where, String[] params, String orderBy) throws SQLException {
		return this.queryTable(conn, this.tableName, columns, null, where, params, null, null, orderBy, null);
	}
	
	public List<Map<String, String>> query(String[] columns, String where, String[] params, String orderBy) {
		return this.queryTable(this.tableName, columns, null, where, params, null, null, orderBy, null);
	}
	
	public List<Map<String, String>> query(String[] columns, String where, String[] params, String orderBy, String limit) {
		return this.queryTable(this.tableName, columns, null, where, params, null, null, orderBy, limit);
	}
	
	public List<Map<String, String>> query(String[] columns, String where, String[] params, String groupBy, String having, String limit) {
		return this.queryTable(this.tableName, columns, null, where, params, groupBy, having, null, limit);
	}
	
	public List<Map<String, String>> query(String[] columns, List<Filter> filters, String where, String[] params, String groupBy, String having, String orderBy, String limit) {
		return this.queryTable(this.tableName, columns, filters, where, params, groupBy, having, orderBy, limit);
	}
	
}
