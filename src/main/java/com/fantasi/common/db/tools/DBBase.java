package com.fantasi.common.db.tools;

import com.fantasi.common.db.IDBPool;

public class DBBase {
	protected IDBPool context;
	protected String content = ""; 
	public void setContext(IDBPool context) {
		this.context = context;
	}
	public DBBase() {
		
	}
	
	public DBBase(IDBPool context) {
		this.context = context;
	}
	
	public void println(String str) {
		content += str + "\n";
		System.out.println(str);
	}
	
	public void print(String str) {
		content += str;
		System.out.print(str);
	}
}
