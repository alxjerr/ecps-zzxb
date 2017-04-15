package com.rl.ecps.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rl.ecps.model.TaskBean;
import com.rl.ecps.service.EbOrderService;

@Controller
@RequestMapping("/order")
public class EbOrderController {

	@Autowired
	private EbOrderService orderService;
	
	@RequestMapping("/toIndex.do")
	public String toIndex(){
		return "order/index";
	}
	
	@RequestMapping("/listOrderPay.do")
	public String listOrderPay(String assignee,Short isCall,Model model){
		List<TaskBean> tbList = orderService.selectOrderPay(assignee,isCall);
		model.addAttribute("tbList",tbList);
		model.addAttribute("isCall",isCall);
		return "order/orderPay/orderPay";
	}
	
	
}
