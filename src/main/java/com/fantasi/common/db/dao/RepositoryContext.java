package com.fantasi.common.db.dao;

import java.util.HashMap;
import java.util.Map;

import com.fantasi.common.db.DBPool;

public class RepositoryContext {
	private static Map<String, BaseDictDao> container = null;
	private static DBPool pool = null;
	
	public static void init(DBPool dbPool) {
		pool = dbPool;
	}
	/**
	 * 根据表名进行存储层的注册，注册后可通过get方法获取BaseDictDao对象
	 * 多次注册一个table会忽略后面的注册
	 * @param table
	 */
	public static void register(String table) {
		if (container == null) {
			container = new HashMap<String, BaseDictDao>();
		}
		if (!container.containsKey(table)) {
			BaseDictDao dao = new BaseDictDao(pool, table);
			container.put(table, dao);	
		}
	}
	
	
	public static BaseDictDao get(String table) {
		if (container.containsKey(table)) {
			return container.get(table);
		}
		return null;
	}
}
