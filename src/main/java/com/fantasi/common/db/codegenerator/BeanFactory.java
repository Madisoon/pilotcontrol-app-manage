package com.fantasi.common.db.codegenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.fantasi.common.db.DBPool;
import com.fantasi.common.db.tools.DBTable;
import com.fantasi.common.db.tools.DBUtils;

public class BeanFactory {
	private DBPool pool;
	public BeanFactory() {
		this("jdbc.properties");
	}
	public BeanFactory(String jdbcPath) {
		Properties props = new Properties();
		try {
			props.load(ClassLoader.getSystemResourceAsStream(jdbcPath));
			String driver = props.getProperty("jdbc.driverClassName");
			String connectURI = props.getProperty("jdbc.url");
			String username = props.getProperty("jdbc.username");
			String password = props.getProperty("jdbc.password");
			pool = new DBPool();
			pool.init(driver, connectURI, username, password);
		} catch (IOException e) {
		}
	}
	
	public BeanFactory(DBPool pool) {
		this.pool = pool;
	}
	
	public Map<String, String> generate(String[] tables, String packageBase, String moduleName) {
		Map<String, String> result = new HashMap<String, String>();
		
		for (String tableName : tables) {
			String key = DBUtils.getFieldNameFromColumnName(tableName, false);
			String packageStr = "package " + packageBase + "." + moduleName + ".beans" + ";\n";
			DBTable table = new DBTable(pool, tableName);
			BeanGenerator bean = new BeanGenerator(table);
			result.put(key, packageStr + bean.generate());
		}
		return result;
	}
	
}
