package com.rl.ecps.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rl.ecps.dao.EbConsoleLogDao;
import com.rl.ecps.dao.EbItemClobDao;
import com.rl.ecps.dao.EbItemDao;
import com.rl.ecps.dao.EbParaValueDao;
import com.rl.ecps.dao.EbSkuDao;
import com.rl.ecps.model.EbConsoleLog;
import com.rl.ecps.model.EbItem;
import com.rl.ecps.model.EbItemClob;
import com.rl.ecps.model.EbParaValue;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.service.EbItemService;
import com.rl.ecps.stub.EbWSItemService;
import com.rl.ecps.stub.EbWSItemServiceService;
import com.rl.ecps.utils.ECPSUtils;
import com.rl.ecps.utils.Page;
import com.rl.ecps.utils.QueryCondition;
@Service
public class EbItemServiceImpl implements EbItemService {

	@Autowired
	private EbItemDao itemDao;
	
	@Autowired
	private EbItemClobDao itemClobDao;
	
	@Autowired
	private EbParaValueDao paraDao;
	
	@Autowired
	private EbSkuDao skuDao;
	
	@Autowired
	private EbConsoleLogDao logDao;

	public Page selectItemByCondition(QueryCondition qc) {
		//获得页码
		Integer pageNo = qc.getPageNo();
		//创建page对象
		Page page = new Page();
		page.setPageNo(pageNo);
		//查询当前条件下的记录数
		Integer totalCount = itemDao.selectItemByConditionCount(qc);
		page.setTotalCount(totalCount);
		//获得开始行号和结束行号
		Integer startNum = page.getStartNum();
		Integer endNum = page.getEndNum();
		//把开始行号和结束行号设置给qc对象
		qc.setStartNum(startNum);
		qc.setEndNum(endNum);
		//查询结果集
		List<EbItem> itemList = itemDao.selectItemByCondition(qc);
		//给page对象设置结果集
		page.setList(itemList);
		return page;
	}

	public void saveItem(EbItem item, EbItemClob itemClob,
			List<EbParaValue> paraList, List<EbSku> skuList) {
		//保存商品并且返回主键
		itemDao.saveItem(item);
		//给大字段表设置外键
		itemClob.setItemId(item.getItemId());
		itemClobDao.saveItemClob(itemClob);
		paraDao.saveParaValue(paraList, item.getItemId());
		skuDao.saveSku(skuList, item.getItemId());
	}

	public void auditItem(Long itemId, Short auditStatus, String notes) {
		//变更审核字段
		EbItem item = new EbItem();
		item.setAuditStatus(auditStatus);
		item.setItemId(itemId);
		itemDao.updateItem(item);
		
		//插入操作日志
		EbConsoleLog log = new EbConsoleLog();
		log.setEntityId(itemId);
		log.setEntityName("商品表");
		log.setNotes(notes);
		log.setOpTime(new Date());
		log.setOpType(ECPSUtils.readProp("audit_item_type"));
		log.setTableName("EB_ITEM");
		log.setUserId(1l);
		logDao.saveLog(log);
	}

	public void showItem(Long itemId, Short showStatus, String notes) {
		//变更审核字段
		EbItem item = new EbItem();
		item.setShowStatus(showStatus);
		item.setItemId(itemId);
		itemDao.updateItem(item);
		
		//插入操作日志
		EbConsoleLog log = new EbConsoleLog();
		log.setEntityId(itemId);
		log.setEntityName("商品表");
		log.setNotes(notes);
		log.setOpTime(new Date());
		log.setOpType(ECPSUtils.readProp("show_item_type"));
		log.setTableName("EB_ITEM");
		log.setUserId(1l);
		logDao.saveLog(log);
	}

	public List<EbItem> selectItemFont(String price, Long brandId, String paraList) {
		Map<String,Object> map = new HashMap<String,Object>();
		//拆分价钱字符串
		if(StringUtils.isNotBlank(price)){
			 String[] prices = price.split("-");
			 map.put("minPrice", prices[0]);
			 map.put("maxPrice", prices[1]);
		}
		map.put("brandId", brandId);
		if(org.apache.commons.lang3.StringUtils.isNotBlank(paraList)){
			String [] paraArr = paraList.split(",");
			map.put("paraList", paraArr);
		}
		return itemDao.listItem(map);
	}

	public EbItem selectItemDetailById(Long itemId) {
		return itemDao.selectItemDetailById(itemId);
	}

	public String publishItem(Long itemId, String password) {
		//创建服务访问点的集合
		EbWSItemServiceService itemServiceService = new EbWSItemServiceService();
		//获得服务端的接口，通过服务访问点的name在前面加上get这个方法就是获得webservice服务的接口类的方法，getEbWSItemServicePort
		EbWSItemService service = itemServiceService.getEbWSItemServicePort();
		//调用webservice的发布方法
		return service.publishItem(itemId, password);
	}
	
	

}
