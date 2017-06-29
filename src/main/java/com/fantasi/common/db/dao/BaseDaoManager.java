package com.fantasi.common.db.dao;

import com.fantasi.common.db.DBPool;

public class BaseDaoManager {
	protected DBPool pool = null;
	
	public void init(DBPool pool) {
		this.pool = pool;
	}
	
}
