package com.fantasi.common.db.codegenerator;

import java.util.List;

import com.fantasi.common.db.tools.Column;
import com.fantasi.common.db.tools.DBTable;
import com.fantasi.common.db.tools.DBUtils;

public class BeanGenerator extends BaseGenerator{
	
	private String prefix = "Field";
	
	public BeanGenerator(DBTable table) {
		super(table);
	}

	public String generate() {
		List<Column> columns = table.getColumns();
		
		StringBuffer sb = new StringBuffer();
		sb.append("public class " + this.generateClassName() + " {\n");
		sb.append(t1 +"public final static String Table = \"" + table.getTableName() + "\";\n");
		for (Column column : columns) {
			String columnName = column.getColumnName();
			String property = DBUtils.getFieldNameFromColumnName(columnName, false);
			
			sb.append(t1 +"public final static String " + prefix + "_" +
					property + " = \"" + columnName + "\";\n");
		}
		
		sb.append("}");
		return sb.toString();
	}
	
	public String generateDao() {
		StringBuffer sb = new StringBuffer();
		sb.append("import com.ipscg.common.db.dao.BaseDictDao;\n");
		sb.append("public class " + this.generateClassName() + "Dao extends BaseDictDao {\n");
		sb.append(t1 + "public " + generateClassName() + "Dao() {\n");
		sb.append(t2 + "this.tableName = " + this.generateClassName() + ".Table;\n");
		sb.append(t2 + "this.primaryKeys = new String[]{" + this.getPrimaryKeys() + "};\n");
		sb.append(t1 + "}\n");
		sb.append("}");
		return sb.toString();
	}
	
	private String getPrimaryKeys() {
		String keys = "";
		List<Column> columns = table.getPrimaryKeys();
		for (Column column : columns) {
			keys += "\"" + column.getColumnName() + "\",";
		}
		if (!keys.equals("")) {
			return keys.substring(0, keys.length() - 1);
		}
		return keys;
	}
	
	private String generateClassName() {
		return DBUtils.getFieldNameFromColumnName(table.getTableName(), false);
	}
}
