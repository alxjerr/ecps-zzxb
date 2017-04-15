package com.rl.ecps.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rl.ecps.dao.EbOrderDao;
import com.rl.ecps.dao.EbOrderDetailDao;
import com.rl.ecps.dao.EbSkuDao;
import com.rl.ecps.exception.EbStockException;
import com.rl.ecps.model.EbOrder;
import com.rl.ecps.model.EbOrderDetail;
import com.rl.ecps.model.TaskBean;
import com.rl.ecps.service.EbCartService;
import com.rl.ecps.service.EbOrderService;
import com.rl.ecps.service.FlowService;

@Service
public class EbOrderServiceImpl implements EbOrderService{

	@Autowired
	private EbOrderDao orderDao;
	
	@Autowired
	private EbOrderDetailDao detailDao;
	
	@Autowired
	private EbSkuDao skuDao;
	
	@Autowired
	private EbCartService cartService;
	
	@Autowired
	private FlowService flowService;
	
	public String saveOrder(HttpServletResponse response,HttpServletRequest request, EbOrder order,List<EbOrderDetail> detailList) throws EbStockException {
		Map<String,Object> map = new HashMap<String,Object>();
		orderDao.saveOrder(order);
		for(EbOrderDetail detail:detailList){
			detail.setOrderId(order.getOrderId());  
			detailDao.saveOrderDetail(detail);
			map.put("skuId",detail.getSkuId());
			map.put("quantity",detail.getQuantity());
			int flag = skuDao.updateStock(map);
			if(flag == 0){
				throw new EbStockException("stock_error");
			}
		}
		String processInstanceId = flowService.startFlow(order.getOrderId());
		cartService.clearCart(request, response);
		return processInstanceId;
	}

	public void updatePay(String processInstanceId, Long orderId) {
		EbOrder order = new EbOrder();
		order.setOrderId(orderId);
		order.setIsPaid((short)1);
		orderDao.updateOrder(order);
		flowService.completeTaskByPid(processInstanceId, "付款");
	}

	public List<TaskBean> selectOrderPay(String assignee, Short isCall) {
		List<TaskBean> tbList = flowService.selectTaskByAssignee(assignee);
		List<TaskBean> tbList1 = new ArrayList<TaskBean>();
		
		for(TaskBean tb : tbList){
			//获得业务主键，从而查询到订单
			String businessKey = tb.getBusinessKey();
			EbOrder order = orderDao.getOrderById(new Long(businessKey));
			if(isCall != null){
				if(order.getIsCall().shortValue() == isCall.shortValue()){
					tb.setOrder(order);
					tbList1.add(tb);
				}
			}else{
				tb.setOrder(order);
				tbList1.add(tb);
			}
		}
		return tbList1;
	}
	
	
	
}
