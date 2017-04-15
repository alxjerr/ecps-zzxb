package com.rl.ecps.service;

import java.util.List;

import com.rl.ecps.model.EbShipAddr;
import com.rl.ecps.model.EbShipAddrBean;

public interface EbShipAddrService {

	public List<EbShipAddrBean> selectAddrByUserId(Long userId);

	public EbShipAddr selectAddrById(Long shipAddrId);
	
	public void saveOrUpdateAddr(EbShipAddr addr);

	public void modifyDefaultAddr(EbShipAddr addr);
	
}
