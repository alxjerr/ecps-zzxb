package com.rl.ecps.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rl.ecps.dao.EbShipAddrDao;
import com.rl.ecps.model.EbShipAddr;
import com.rl.ecps.model.EbShipAddrBean;
import com.rl.ecps.service.EbShipAddrService;

@Service
public class EbShipAddrServiceImpl implements EbShipAddrService{

	@Autowired
	private EbShipAddrDao addrDao;
	
	public List<EbShipAddrBean> selectAddrByUserId(Long userId) {
		return addrDao.selectAddrByUserId(userId);
	}
	
	public EbShipAddr selectAddrById(Long shipAddrId) {
		return addrDao.selectAddrById(shipAddrId);
	}

	public void saveOrUpdateAddr(EbShipAddr addr) {
		//把旧的默认收货地址变成非默认
		if(addr.getDefaultAddr() == 1){
			addrDao.updateDefaultAddr(addr.getPtlUserId());
		}
		if(addr.getShipAddrId() == null){
			addrDao.saveAddr(addr);
		}else{
			addrDao.updateAddr(addr);
		}
	}

	public void modifyDefaultAddr(EbShipAddr addr) {
		addrDao.updateDefaultAddr1(addr);
	}

}
