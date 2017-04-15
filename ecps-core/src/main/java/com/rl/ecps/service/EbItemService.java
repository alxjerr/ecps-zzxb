package com.rl.ecps.service;

import java.util.List;

import com.rl.ecps.model.EbItem;
import com.rl.ecps.model.EbItemClob;
import com.rl.ecps.model.EbParaValue;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.utils.Page;
import com.rl.ecps.utils.QueryCondition;

public interface EbItemService {
	
	public Page selectItemByCondition(QueryCondition qc);
	
	public void saveItem(EbItem item, EbItemClob itemClob, 
			List<EbParaValue> paraList, List<EbSku> skuList);
	
	/**
	 * 商品审核
	 * @param itemId
	 * @param auditStatus
	 * @param notes
	 */
	public void auditItem(Long itemId, Short auditStatus, String notes);
	
	public void showItem(Long itemId, Short showStatus, String notes);
	
	public List<EbItem> selectItemFont(String price, Long brandId, String paraList);
	
	public EbItem selectItemDetailById(Long itemId);
	
	public String publishItem(Long itemId, String password);

}
