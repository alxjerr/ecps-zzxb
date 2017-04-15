package com.rl.ecps.dao.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.rl.ecps.dao.EbSkuDao;
import com.rl.ecps.model.EbItem;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.model.EbSpecValue;
import com.rl.ecps.utils.ECPSUtils;
import com.rl.ecps.utils.QueryCondition;

@Repository
public class EbSkuDaoImpl extends SqlSessionDaoSupport implements EbSkuDao{
	
	String ns = "com.rl.ecps.mapper.EbSkuMapper.";
	String ns1 = "com.rl.ecps.mapper.EbSpecValueMapper.";

	public void saveSku(List<EbSku> skuList,Long itemId) {
		SqlSession session = this.getSqlSession();
		for(EbSku sku : skuList){
			//设置itemId
			sku.setItemId(itemId);
			//保存sku，并且返回skuId
			session.insert(ns+"insert",sku);
			List<EbSpecValue> specList = sku.getSpecList();
			for(EbSpecValue sv:specList){
				//设置skuid外键
				sv.setSkuId(sku.getSkuId());
				session.insert(ns1+"insert",sv);
			}
		}
	}

	public EbSku getSkuById(Long skuId) {
		return this.getSqlSession().selectOne(ns+"selectByPrimaryKey",skuId);
	}

	public EbSku getSkuDetail(Long skuId) {
		return this.getSqlSession().selectOne(ns+"getSkuDetail",skuId);
	}

	public int updateStock(Map<String, Object> map) {
		return this.getSqlSession().update(ns+"updateStock",map);
	}

/*	public EbSku getSkuDetailWithRedis(Long skuId) {
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
		
		//huode
		String itemName = je.hget("sku:item:"+itemId, "itemName");
		String itemNo = je.hget("sku:item:"+itemId, "itemNo");
		String imgs = je.hget("sku:item:"+itemId, "imgs");
		EbItem item = new EbItem();
		item.setItemId(new Long(itemId));
		item.setItemName(itemName);
		item.setItemNo(itemNo);
		item.setImgs(imgs);
		sku.setItem(item);
		
		//
		je.hget("sku:"+skuId+"spec:"+specId,"specValue");
		
		return null;
	}*/
}
