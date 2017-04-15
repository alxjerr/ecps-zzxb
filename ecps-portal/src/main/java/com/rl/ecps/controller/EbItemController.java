package com.rl.ecps.controller;

import java.io.PrintWriter;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rl.ecps.model.EbBrand;
import com.rl.ecps.model.EbFeature;
import com.rl.ecps.model.EbItem;
import com.rl.ecps.model.EbParaValue;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.service.EbBrandService;
import com.rl.ecps.service.EbFeatureService;
import com.rl.ecps.service.EbItemService;
import com.rl.ecps.service.EbSkuService;

@Controller
@RequestMapping("/item")
public class EbItemController {

	@Autowired
	private EbBrandService brandService;
	
	@Autowired
	private EbFeatureService featureService;
	
	@Autowired
	private EbItemService itemService;
	
	@Autowired
	private EbSkuService skuService;
	
	@RequestMapping("/toIndex.do")
	public String toIndex(Model model){
		//查询品牌
		List<EbBrand> bList = brandService.selectBrandAll();
		model.addAttribute("bList",bList);
		//查询筛选属性
		List<EbFeature> fList = featureService.selectIsSelectFeature();
		model.addAttribute("fList",fList);
		return "index";
	}
	
	/**
	 * 商品列表的查询
	 * @return
	 */
	@RequestMapping("/listItem.do")
	public String listItem (String price, Long brandId, String paraList, Model model){
		List<EbItem> itemList = itemService.selectItemFont(price, brandId, paraList);
		model.addAttribute("itemList", itemList);
		return "phoneClassification";
	}
	
	/**
	 * 查询商品详情页的数据
	 * @return
	 */
	@RequestMapping("/productDetail.do")
	public String productDetail(Long itemId,Model model){
		EbItem item = itemService.selectItemDetailById(itemId);
		model.addAttribute("item",item);
		return "productDetail";
	}
	
	@RequestMapping("/getSkuById.do")
	public void getSkuById(Long skuId,PrintWriter out){
		EbSku sku = skuService.getSkuById(skuId);
//		EbSku sku = skuService.getSkuByIdWithRedis(skuId);
		JSONObject jo = new JSONObject();
		jo.accumulate("sku", sku);
		String result = jo.toString();
		out.write(result);
	}
}	
