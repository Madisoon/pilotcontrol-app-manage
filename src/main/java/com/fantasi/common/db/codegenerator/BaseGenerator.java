package com.fantasi.common.db.codegenerator;

import com.fantasi.common.db.tools.DBTable;

public class BaseGenerator {
	DBTable table = null;
	String t1 = "\t";
	String t2 = "\t\t";
	String t3 = "\t\t\t";
	String t4 = "\t\t\t\t";
	
	public BaseGenerator(DBTable table) {
		this.table = table;
	}
}
