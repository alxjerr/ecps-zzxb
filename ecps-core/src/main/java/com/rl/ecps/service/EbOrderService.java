package com.rl.ecps.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rl.ecps.exception.EbStockException;
import com.rl.ecps.model.EbOrder;
import com.rl.ecps.model.EbOrderDetail;
import com.rl.ecps.model.TaskBean;

public interface EbOrderService {

	public String saveOrder(HttpServletResponse response,HttpServletRequest request,EbOrder order, List<EbOrderDetail> detailList)throws EbStockException ;

	public void updatePay(String processInstanceId, Long orderId);

	public List<TaskBean> selectOrderPay(String assignee, Short isCall);

}
