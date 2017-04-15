package com.rl.ecps.controller;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rl.ecps.model.EbBrand;
import com.rl.ecps.model.EbFeature;
import com.rl.ecps.model.EbItem;
import com.rl.ecps.model.EbItemClob;
import com.rl.ecps.model.EbParaValue;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.model.EbSpecValue;
import com.rl.ecps.service.EbBrandService;
import com.rl.ecps.service.EbFeatureService;
import com.rl.ecps.service.EbItemService;
import com.rl.ecps.utils.ECPSUtils;
import com.rl.ecps.utils.Page;
import com.rl.ecps.utils.QueryCondition;

@Controller
@RequestMapping("/item")
public class EbItemController {
	
	@Autowired
	private EbBrandService brandService;
	
	@Autowired
	private EbItemService itemService; 
	
	@Autowired
	private EbFeatureService featureService;
	
	@RequestMapping("/toIndex.do")
	public String  toIndex(){
		return "item/index";
	}
	
	/**
	 * 查询所有的品牌
	 * @param model
	 * @return
	 */
	@RequestMapping("/selectBrandAll.do")
	public String  selectBrandAll(Model model){
		List<EbBrand> bList = brandService.selectBrandAll();
		model.addAttribute("bList",bList);
		return "item/listbrand";
	}
	
	/**
	 * 跳转到添加品牌的页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/toAddBrand.do")
	public String  toAddBrand(Model model){
		return "item/addbrand";
	}

	/**
	 * 验证品牌名称的重复性
	 * @param brandName
	 * @param out
	 * @return
	 */
	@RequestMapping("/toAddBrand.do")
	public void validBrandName(String brandName,PrintWriter out){
		List<EbBrand> brandList = brandService.validBrandName(brandName);
		String flag = "yes";
		if(brandList.size() > 0){
			flag = "no";
		}
		out.write(flag);
	}
	
	/**
	 * 
	 * @param brand
	 * @return
	 */
	@RequestMapping("/addBrand.do")
	public String addBrand(EbBrand brand){
		brandService.saveBrand(brand);
		return "redirect:selectBrandAll.do";
	}
	
	@RequestMapping("/listItem.do")  
	public String listItem(QueryCondition qc,Model model){
		List<EbBrand> bList = brandService.selectBrandAll();
		model.addAttribute("bList",bList);
		if(qc.getPageNo() == null){
			qc.setPageNo(1);
		}
		Page page = itemService.selectItemByCondition(qc);
		model.addAttribute("page",page);
		//把查询条件写回去，目的是回显
		model.addAttribute("qc",qc);
		return "item/list";
	}
	
	@RequestMapping("/toAddItem.do")
	public String toAddItem(Model model){
		List<EbBrand> bList = brandService.selectBrandAll();
		model.addAttribute("bList",bList);
		//普通属性的查询
		List<EbFeature> commList = featureService.selectCommFeature();
		model.addAttribute("commList",commList);
		//特殊属性的查询
		List<EbFeature> specList = featureService.selectSpecFeature();
		model.addAttribute("specList",specList);
		return "item/addItem";
	}
	
	@RequestMapping("/addItem.do")
	public String addItem(EbItem item,EbItemClob itemClob,HttpServletRequest request,Integer divNum){
		item.setItemNo(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		//查询普通的属性
		List<EbFeature> commList = featureService.selectCommFeature();
		List<EbParaValue> paraList = new ArrayList<EbParaValue>();
		for(EbFeature feature : commList){
			//获得属性ID也就是前台页面上input的name
			Long featureId = feature.getFeatureId();
			if(feature.getInputType() == 3){
				String[] paraArr = request.getParameterValues(featureId+"");
				if(paraArr != null && paraArr.length > 0){
					String paraVals = "";
					for(String para : paraArr){
						paraVals = paraVals + para + ",";
					}
					paraVals= paraVals.substring(0,paraVals.length() - 1);
					//创建EbParaValue对象
					EbParaValue ev = new EbParaValue();
					ev.setFeatureId(featureId);
					ev.setParaValue(paraVals);
					paraList.add(ev);
				}
			}else{
				//获得单选和下拉框的值
				String para = request.getParameter(featureId+"");
				if(StringUtils.isNotBlank(para) && !StringUtils.equals(para,"")){
					//创建EbParaValue对象
					EbParaValue ev = new EbParaValue();
					ev.setFeatureId(featureId);
					ev.setParaValue(para);
					paraList.add(ev);
				}
			}
		}
		
		List<EbSku> skuList = new ArrayList<EbSku>();
		List<EbFeature> specList = featureService.selectSpecFeature();
		
		//在循环中获得每一个最小销售单元的值
		for(int i = 1;i <= divNum;i++){
			String skuPrice = request.getParameter("skuPrice"+i);
			String stockInventory = request.getParameter("stockInventory"+i);
			//判断divNum编号没有断档的情况，因为skuPrice 和 stockInventory是必填字段
			if(StringUtils.isNotBlank(skuPrice) && StringUtils.isNotBlank(stockInventory)){
				String sort = request.getParameter("sort"+i); 
				String skuType = request.getParameter("skuType" + i);
				String showStatus = request.getParameter("showStatus"+i);
				String marketPrice = request.getParameter("marketPrice" + i);
				String skuUpperLimit = request.getParameter("skuUpperLimit"+i);
				String sku = request.getParameter("sku" + i);
				String location = request.getParameter("location" + i);
				
				//创建sku对象
				EbSku skuObj = new EbSku();
				if(StringUtils.isNotBlank(sort)){
					skuObj.setSkuSort(new Integer(sort));
				}
				skuObj.setSkuPrice(new BigDecimal(skuPrice));
				skuObj.setStockInventory(new Integer(stockInventory));
				if(StringUtils.isNotBlank(skuType)){
					skuObj.setSkuType(new Short(skuType));
				}
				if(StringUtils.isNotBlank(showStatus)){
					skuObj.setSkuType(new Short(showStatus));
				}
				if(StringUtils.isNotBlank(marketPrice)){
					skuObj.setMarketPrice(new BigDecimal(marketPrice));
				}
				if(StringUtils.isNotBlank(skuUpperLimit)){
					skuObj.setSkuUpperLimit(new Integer(skuUpperLimit));
				}
				skuObj.setSku(sku);
				skuObj.setLocation(location);
				
				List<EbSpecValue> svList = new ArrayList<EbSpecValue>();
				for(EbFeature feature : specList){
					//获得特殊属性的ID
					Long featureId = feature.getFeatureId();
					//获得当前最小销售单元的每一个特殊属性的值
					String specVal = request.getParameter(featureId+"specradio"+i);
					EbSpecValue  sv = new EbSpecValue();
					sv.setFeatureId(featureId);
					sv.setSpecValue(specVal);
					svList.add(sv);
				}
				skuObj.setSpecList(svList);
				skuList.add(skuObj);
			}
		}
		itemService.saveItem(item, itemClob, paraList, skuList);
		return "redirect:listItem.do?showStatus=1";
	}
	
	/**
	 * 查询商品审核的列表
	 * @param qc
	 * @param model
	 * @return
	 */
	@RequestMapping("/listAuditItem.do")
	public String listAuditItem(QueryCondition qc,Model model){
		List<EbBrand> bList = brandService.selectBrandAll();
		model.addAttribute("bList",bList);
		if(qc.getPageNo() == null){
			qc.setPageNo(1);
		}
		Page page = itemService.selectItemByCondition(qc);
		model.addAttribute("page",page);
		//把查询条件写回去，目的是回显
		model.addAttribute("qc",qc);
		return "item/listAudit";
	}
	
	/**
	 * 商品审核
	 * @param notes
	 * @param itemId
	 * @param auditStatus
	 * @param model
	 * @return
	 */
	@RequestMapping("/auditItem.do")
	public String listAuditItem(String notes,Long itemId,Short auditStatus,Model model){
		itemService.auditItem(itemId, auditStatus, notes);
		return "redirect:listAuditItem.do?auditStatus=0&showStatus=1";
	}

	/**
	 * 商品上下架
	 * @param notes
	 * @param itemId
	 * @param auditStatus
	 * @param model
	 * @return
	 */
	@RequestMapping("/showItem.do")
	public String showItem(String notes,Long itemId,Short showStatus,Model model){
		itemService.showItem(itemId, showStatus, notes);
		String flag = "1";
		if(showStatus == 1){
			flag = "0";
		}
		return "redirect:listItem.do?auditStatus=1&showStatus="+flag;
	}
	
	@RequestMapping("/publish.do")
	public void publish(Long itemId,PrintWriter out){
		String password = ECPSUtils.readProp("ws_pass");
		String result = itemService.publishItem(itemId, password);
		out.write(result);
	}
	
	
}
