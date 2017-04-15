package com.rl.ecps.dao;

import java.util.List;

import com.rl.ecps.model.EbShipAddr;
import com.rl.ecps.model.EbShipAddrBean;

public interface EbShipAddrDao {

	public List<EbShipAddrBean> selectAddrByUserId(Long userId);

	public EbShipAddr selectAddrById(Long shipAddrId);

	public void updateDefaultAddr(Long userId);

	public void saveAddr(EbShipAddr addr);

	public void updateAddr(EbShipAddr addr);
	
	public void updateDefaultAddr1(EbShipAddr addr);
}
