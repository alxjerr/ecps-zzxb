package com.rl.ecps.dao.impl;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.rl.ecps.dao.EbAreaDao;
import com.rl.ecps.model.EbArea;

@Repository
public class EbAreaDaoImpl extends SqlSessionDaoSupport implements EbAreaDao{

	String ns = "com.rl.ecps.mapper.EbAreaMapper.";
	
	public List<EbArea> selectAreaByParentId(Long areaId) {
		return this.getSqlSession().selectList(ns+"selectAreaByParentId",areaId);
	}

}
