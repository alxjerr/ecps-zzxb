package com.rl.ecps.dao.impl;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.rl.ecps.dao.EbShipAddrDao;
import com.rl.ecps.model.EbShipAddr;
import com.rl.ecps.model.EbShipAddrBean;

@Repository
public class EbShipAddrDaoImpl extends SqlSessionDaoSupport implements EbShipAddrDao{
	
	String ns = "com.rl.ecps.mapper.EbShipAddrMapper.";
	
	public List<EbShipAddrBean> selectAddrByUserId(Long userId) {
		return this.getSqlSession().selectList(ns+"selectAddrByUserId",userId);
	}

	public EbShipAddr selectAddrById(Long shipAddrId) {
		return this.getSqlSession().selectOne(ns+"selectByPrimaryKey",shipAddrId);
	}
	
	public void updateDefaultAddr(Long userId) {
		this.getSqlSession().update(ns+"updateDefaultAddr",userId);
		//this.getSqlSession().update(ns+"updateDefaultAddr",userId);
	}

	public void updateDefaultAddr1(EbShipAddr addr) {
		System.out.println(addr.getPtlUserId());
		this.getSqlSession().update(ns+"updateDefaultAddr",addr.getPtlUserId());
		this.getSqlSession().update(ns+"updateDefaultAddr1",addr);
	}
	
	public void saveAddr(EbShipAddr addr) {
		this.getSqlSession().insert(ns+"insert",addr);
	}

	public void updateAddr(EbShipAddr addr) {
		this.getSqlSession().update(ns+"updateByPrimaryKeySelective",addr);
	}

}
