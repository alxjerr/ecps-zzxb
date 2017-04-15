package com.rl.ecps.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;

import com.rl.ecps.dao.EbSkuDao;
import com.rl.ecps.model.EbBrand;
import com.rl.ecps.model.EbItem;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.model.EbSpecValue;
import com.rl.ecps.utils.ECPSUtils;
import com.rl.ecps.utils.FMutil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:beans.xml"})
public class EbBrandServiceTest {

	@Autowired
	private EbBrandService brandService;
	
	@Autowired
	private EbItemService itemService;
	
	@Autowired
	private EbSkuDao skuDao;
	
	@Autowired
	private FlowService flowService;
	
	@Test
	public void testSaveBrand(){
		EbBrand brand = new EbBrand();
		brand.setBrandName("香蕉");
		brand.setBrandDesc("好用");
		brand.setBrandSort(1);
		brand.setImgs("http://www");
		brand.setWebsite("http://www");
		brandService.saveBrand(brand);
		
	}
	
	@Test
	public void testSelectBrandAll(){
		
	}
	
	@Test
	public void testGenerateHtml(){
		Map<String,Object> map = new HashMap<String,Object>();
		EbItem item = itemService.selectItemDetailById(3060l);
		map.put("item", item);
		map.put("path",ECPSUtils.readProp("portal_path"));
		map.put("file_path",ECPSUtils.readProp("file_path"));
		FMutil fm = new FMutil();
		try {
			fm.ouputFile("productDetail.ftl", item.getItemId()+".html", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void importDatatoRedis(){
		String host = ECPSUtils.readProp("redis_path");
		String port = ECPSUtils.readProp("redis_port");
		Jedis jedis = new Jedis(host,new Integer(port));
		EbSku sku = skuDao.getSkuDetail(3061l);
		//把sku的ID放入skuList中
		jedis.lpush("skuList", sku.getSkuId()+"");	
		//存储sku这条数据
		jedis.hset("sku:"+sku.getSkuId(),"skuId",sku.getSkuId()+"");
		jedis.hset("sku:"+sku.getSkuId(),"itemId",sku.getItemId()+"");
		jedis.hset("sku:"+sku.getSkuId(),"skuPrice",sku.getSkuPrice()+"");
		jedis.hset("sku:"+sku.getSkuId(),"stockInventory",sku.getStockInventory()+"");
		jedis.hset("sku:"+sku.getSkuId(),"marketPrice",sku.getMarketPrice()+"");
		
		//存储商品信息
		jedis.hset("sku:item:"+sku.getItem().getItemId(),"itemId",sku.getItem().getItemId()+"");
		jedis.hset("sku:item:"+sku.getItem().getItemId(),"itemName",sku.getItem().getItemName()+"");
		jedis.hset("sku:item:"+sku.getItem().getItemId(),"itemName",sku.getItem().getItemName()+"");
		jedis.hset("sku:item:"+sku.getItem().getItemId(),"itemName",sku.getItem().getImgs()+"");

		//sku中的规格组合存储到redis中
		List<EbSpecValue> specList = sku.getSpecList();
		for(EbSpecValue spec:specList){
			jedis.hset("sku:spec:"+spec.getSpecId(), "specId",spec.getSpecId()+"");
			jedis.hset("sku:spec:"+spec.getSpecId(), "skuId",spec.getSkuId()+"");
			jedis.hset("sku:spec:"+spec.getSpecId(), "specValue",spec.getSpecValue()+"");
		}
		jedis.close();
	}
	
	@Test
	public void deployFlow(){
		flowService.deploy();
	}
}
