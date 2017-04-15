package com.rl.ecps.service;

import java.io.PrintWriter;

import com.rl.ecps.model.EbSku;

public interface EbSkuService {

	public EbSku getSkuById(Long skuId);

/*	public EbSku getSkuByIdWithRedis(Long skuId);*/
	
}
