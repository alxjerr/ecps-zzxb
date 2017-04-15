package com.rl.ecps.controller;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rl.ecps.exception.EbStockException;
import com.rl.ecps.model.EbCart;
import com.rl.ecps.model.EbOrder;
import com.rl.ecps.model.EbOrderDetail;
import com.rl.ecps.model.EbShipAddr;
import com.rl.ecps.model.EbShipAddrBean;
import com.rl.ecps.model.EbSpecValue;
import com.rl.ecps.model.TsPtlUser;
import com.rl.ecps.service.EbCartService;
import com.rl.ecps.service.EbOrderService;
import com.rl.ecps.service.EbShipAddrService;

@Controller
@RequestMapping("/order")
public class EbOrderController {

	@Autowired
	private EbShipAddrService addrService;
	
	@Autowired
	private EbCartService cartService;
	
	@Autowired
	private EbOrderService orderService;
	
	/**
	 * 跳转到订单页面
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("/toSubmitOrder.do")
	public String toSubmitOrder(HttpServletRequest request,HttpServletResponse response,HttpSession session,Model model){
		TsPtlUser user = (TsPtlUser) session.getAttribute("user");
		//查询收货地址
		if(user != null){
			List<EbShipAddrBean> addrList = addrService.selectAddrByUserId(user.getPtlUserId());
			model.addAttribute("addrList",addrList);
		}else{
			return "redirect:/user/toLogin.do";
		}
		//查询购物车的数据
		List<EbCart> cartList = cartService.selectCart(request, response);
		model.addAttribute("cartList",cartList);
		Integer itemNum = 0;
		BigDecimal totalPrice = new BigDecimal(0);
		for(EbCart cart:cartList){
			//计算商品数量
			itemNum = cart.getQuantity() + itemNum;
			//计算商品余额，不会损失精度
			totalPrice = totalPrice.add(cart.getSku().getSkuPrice().multiply(new BigDecimal(cart.getQuantity())));
		}
		model.addAttribute("itemNum",itemNum);
		model.addAttribute("totalPrice",totalPrice);
		return "shop/confirmProductCase";
	}
	
	/**
	 * 订单提交
	 * @param response
	 * @param request
	 * @param session
	 * @param order
	 * @param address
	 * @param model
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@RequestMapping("/submitOrder.do")
	public String submitOrder(HttpServletResponse response,HttpServletRequest request,HttpSession session,
			EbOrder order,String address,Model model) throws IllegalAccessException, InvocationTargetException{
		TsPtlUser user = (TsPtlUser) session.getAttribute("user");
		request.getAttribute("totalPrice");
		if(user != null){
			order.setPtlUserId(user.getPtlUserId());
			order.setUsername(user.getUsername());
			order.setOrderNum(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
		}
		//如果address不等于add说明address传递过来的是shipAddr
		if(!StringUtils.equals(address, "add")){
			//根据地址id在查询收货地址对象
			EbShipAddr addr = addrService.selectAddrById(new Long(address));
			BeanUtils.copyProperties(order, addr);
		}
		
		//订单明细数据组装
		List<EbCart> cartList = cartService.selectCart(request, response);
		List<EbOrderDetail> detailList = new ArrayList<EbOrderDetail>();
		for(EbCart cart : cartList){
			EbOrderDetail detail = new EbOrderDetail();
			detail.setItemId(cart.getSku().getItem().getItemId());
			detail.setItemNo(cart.getSku().getItem().getItemNo());
			detail.setItemName(cart.getSku().getItem().getItemName());
			detail.setSkuId(cart.getSkuId());
			
			//组装商品规格信息
			String skuSpec = "";
			List<EbSpecValue> specList = cart.getSku().getSpecList();
			for(EbSpecValue spec : specList){
				skuSpec = skuSpec + spec.getSpecValue() + ",";
			}
			skuSpec = skuSpec.substring(0,skuSpec.length() - 1);
			detail.setSkuSpec(skuSpec);
			//市场价
			detail.setSkuPrice(cart.getSku().getSkuPrice());
			//商城价
			detail.setMarketPrice(cart.getSku().getMarketPrice());
			detail.setQuantity(cart.getQuantity());
			detailList.add(detail);
		}
		
		try {
			String processInstanceId = orderService.saveOrder(response, request, order, detailList);
			//把流程实例的id存到session中以便于支付的时候来完成任务
			session.setAttribute("processInstanceId", processInstanceId);
			model.addAttribute("order",order);
		} catch (Exception e) {
			if(e instanceof EbStockException){
				model.addAttribute("tip","stock_error");
			}
		}
		return "shop/confirmProductCase2";
	}
	
	/**
	 * 支付
	 * @param session
	 * @param orderId
	 * @param out
	 */
	@RequestMapping("/pay.do")
	public void pay(HttpSession session,Long orderId,PrintWriter out){
		String processInstanceId = (String) session.getAttribute("processInstanceId");
		orderService.updatePay(processInstanceId,orderId);
		out.write("success");
	}
}
