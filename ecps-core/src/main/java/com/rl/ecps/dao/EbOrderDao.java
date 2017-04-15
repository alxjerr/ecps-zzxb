package com.rl.ecps.dao;

import com.rl.ecps.model.EbOrder;

public interface EbOrderDao {

	public void saveOrder(EbOrder order);

	public void updateOrder(EbOrder order);

	public EbOrder getOrderById(Long orderId);
	
}
