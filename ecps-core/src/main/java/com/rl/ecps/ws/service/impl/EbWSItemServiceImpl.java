package com.rl.ecps.ws.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rl.ecps.dao.EbItemDao;
import com.rl.ecps.model.EbItem;
import com.rl.ecps.utils.ECPSUtils;
import com.rl.ecps.utils.FMutil;
import com.rl.ecps.ws.service.EbWSItemService;
@Service
public class EbWSItemServiceImpl implements EbWSItemService {

	@Autowired
	private EbItemDao itemDao;
	
	public String publishItem(Long itemId, String password) {
		String isOk = "success";
		String wsPass = ECPSUtils.readProp("ws_pass");
		if(StringUtils.equals(password, wsPass)){
			Map<String,Object> map = new HashMap<String,Object>();
			EbItem item = itemDao.selectItemDetailById(itemId);
			map.put("item", item);
			map.put("path", ECPSUtils.readProp("portal_path"));
			map.put("file_path", ECPSUtils.readProp("file_path"));
			FMutil fm = new FMutil();
			try {
				fm.ouputFile("productDetail.ftl", item.getItemId()+".html", map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			isOk = "fail";
		}
		return isOk;
	}

}
