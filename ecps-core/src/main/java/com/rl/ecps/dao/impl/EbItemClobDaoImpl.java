package com.rl.ecps.dao.impl;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.rl.ecps.dao.EbItemClobDao;
import com.rl.ecps.model.EbItem;
import com.rl.ecps.model.EbItemClob;
import com.rl.ecps.utils.QueryCondition;

@Repository
public class EbItemClobDaoImpl extends SqlSessionDaoSupport implements EbItemClobDao{
	
	String ns = "com.rl.ecps.mapper.EbItemClobMapper.";

	public void saveItemClob(EbItemClob itemClob) {
		this.getSqlSession().insert(ns+"insert",itemClob);
	}
	
}
