package com.rl.ecps.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.rl.ecps.dao.EbParaValueDao;
import com.rl.ecps.model.EbItem;
import com.rl.ecps.model.EbParaValue;
import com.rl.ecps.utils.QueryCondition;

@Repository
public class EbParaValueDaoImpl extends SqlSessionDaoSupport implements EbParaValueDao{
	
	String ns = "com.rl.ecps.mapper.EbParaValueMapper.";

	public void saveParaValue(List<EbParaValue> paraList, Long itemId) {
		//获得到链接
		SqlSession session = this.getSqlSession();
		for(EbParaValue ev : paraList){
			ev.setItemId(itemId);
			session.insert(ns + "insert",ev);
		}
	}

	
}
