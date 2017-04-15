package com.rl.ecps.dao.impl;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.rl.ecps.dao.EbOrderDao;
import com.rl.ecps.model.EbOrder;

@Repository
public class EbOrderDaoImpl extends SqlSessionDaoSupport implements EbOrderDao{

	String ns = "com.rl.ecps.mapper.EbOrderMapper.";
	
	public void saveOrder(EbOrder order) {
		this.getSqlSession().insert(ns+"insert",order);
	}

	public void updateOrder(EbOrder order) {
		this.getSqlSession().update(ns+"updateByPrimaryKeySelective",order);
	}

	public EbOrder getOrderById(Long orderId) {
		return this.getSqlSession().selectOne(ns+"selectByPrimaryKey",orderId);
	}

}
