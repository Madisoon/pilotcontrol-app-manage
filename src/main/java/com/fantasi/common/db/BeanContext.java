package com.fantasi.common.db;

import com.fantasi.common.db.DBPool;
import com.fantasi.common.db.dao.BaseDao;
import com.fantasi.common.db.dao.DBHelper;

public class BeanContext {

	protected DBHelper helper = null;
	protected DBPool pool = null;
	
	protected void initDao(BaseDao dao) {
		//dao.setDBPool(this.pool);
	}
}
