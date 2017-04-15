package com.rl.ecps.dao.impl;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.rl.ecps.dao.EbBrandDao;
import com.rl.ecps.model.EbBrand;
@Repository
public class EbBrandDaoImpl extends SqlSessionDaoSupport implements EbBrandDao{

	String ns = "com.rl.ecps.mapper.EbBrandMapper.";
	
	public void saveBrand(EbBrand brand) {
		this.getSqlSession().insert(ns + "insert",brand);
	}

	public List<EbBrand> selectBrandAll() {
		return this.getSqlSession().selectList(ns + "selectBrandAll");
	}

	public List<EbBrand> validBrandName(String brandName) {
		return this.getSqlSession().selectList(ns + "validBrandName",brandName);
	}

}
