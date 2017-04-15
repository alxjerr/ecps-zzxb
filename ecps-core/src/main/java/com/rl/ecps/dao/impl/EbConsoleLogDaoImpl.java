package com.rl.ecps.dao.impl;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.rl.ecps.dao.EbConsoleLogDao;
import com.rl.ecps.model.EbConsoleLog;

@Repository
public class EbConsoleLogDaoImpl extends SqlSessionDaoSupport implements EbConsoleLogDao{

	String ns = "com.rl.ecps.mapper.EbConsoleLogMapper.";
	
	public void saveLog(EbConsoleLog log) {
		this.getSqlSession().insert(ns+"insert",log);
	}

}
