package com.rl.ecps.service.impl;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.rl.ecps.dao.EbSkuDao;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.service.EbSkuService;
import com.rl.ecps.utils.ECPSUtils;

@Service
public class EbSkuServiceImpl implements EbSkuService{

	@Autowired
	private EbSkuDao skuDao;
	
	public EbSku getSkuById(Long skuId) {
		return skuDao.getSkuById(skuId);
	}

	/*public EbSku getSkuByIdWithRedis(Long skuId) {
		String host = ECPSUtils.readProp("redis_path");
		String port = ECPSUtils.readProp("redis_port");
		Jedis je = new Jedis(host,new Integer(port));
		String skuPrice = je.hget("sku:"+skuId, "skuPrice");
		String marketPrice = je.hget("sku:"+skuId, "marketPrice");
		String stockInventory = je.hget("sku:"+skuId, "stockInventory");
		String itemId = je.hget("sku:"+skuId, "itemId");
		EbSku sku = new EbSku();
		if(StringUtils.isNotBlank(marketPrice)){
			sku.setMarketPrice(new BigDecimal(marketPrice));
		}
		sku.setSkuPrice(new BigDecimal(skuPrice));
		sku.setItemId(new Long(itemId));
		sku.setSkuId(skuId);
		sku.setStockInventory(new Integer(stockInventory));
		return sku;
	}*/

}
