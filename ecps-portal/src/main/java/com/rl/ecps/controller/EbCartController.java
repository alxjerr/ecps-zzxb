package com.rl.ecps.controller;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.util.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rl.ecps.model.EbCart;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.service.EbCartService;
import com.rl.ecps.service.EbSkuService;
import com.rl.ecps.utils.ECPSUtils;


@Controller
@RequestMapping("/cart")
public class EbCartController {

	@Autowired
	private EbSkuService skuService;
	
	@Autowired
	private EbCartService cartService;
	
	/**
	 * 查询购物车
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/listCart")
	public String listCart(HttpServletRequest request,HttpServletResponse response,Model model){
		List<EbCart> cartList = cartService.selectCart(request, response);
		model.addAttribute("cartList",cartList);
		Integer itemNum = 0;
		BigDecimal totalPrice = new BigDecimal(0);
		for(EbCart cart : cartList){
			//计算商品数量
			itemNum = cart.getQuantity() + itemNum;
			//计算商品总金额，不会损失精度
			totalPrice = totalPrice.add(cart.getSku().getSkuPrice()
								   .multiply(new BigDecimal(cart.getQuantity()))); 
		}
		model.addAttribute("itemNum",itemNum);
		model.addAttribute("totalPrice",totalPrice);
		return "shop/car";
	}
	
	/**
	 * 验证库存
	 * @param skuId
	 * @param quantity
	 * @param out
	 */
	@RequestMapping("/validStock.do")
	public void validStock(Long skuId,Integer quantity,PrintWriter out){
		String result = "yes";
		EbSku sku = skuService.getSkuById(skuId);
		if(sku.getStockInventory() < quantity){
			result = "no";
		}
		out.write(result);
	}

	@RequestMapping("/validStockDetail.do")
	public void validStockDetail(Long skuId, Integer quantity, PrintWriter out){
		String result = "yes";
		JSONObject jo = new JSONObject();
		EbSku sku = skuService.getSkuById(skuId);
		if(sku.getStockInventory() < quantity){
			result = "no";
			jo.accumulate("stock",sku.getStockInventory());
		}
		jo.accumulate("flag", result);
		out.write(jo.toString());
	}
	
	@RequestMapping("/addCart.do")
	public void addCart(Long skuId,Integer quantity,
			HttpServletRequest request,HttpServletResponse response,PrintWriter out){
		cartService.addCart(skuId, quantity, request, response);
		out.write("success");
	}
	
	@RequestMapping("/deleteCart.do")
	public String deleteCart(Long skuId, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
		cartService.deleteCart(skuId,request,response);
		return "redirect:listCart.do";
	}
	
	@RequestMapping("/updateCartNum.do")
	public String updateCartNum(Long skuId, Integer quantity, 
			HttpServletRequest request, HttpServletResponse response, PrintWriter out){
		cartService.updateCart(skuId, quantity, request, response);
		return "redirect:listCart.do";
	}
	
	@RequestMapping("/clearCart.do")
	public String clearCart(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
		cartService.clearCart(request, response);
		return "redirect:listCart.do";
	}
	
	@RequestMapping("/validCart.do")
	public void validCart(HttpServletRequest request, HttpServletResponse response){
		String result = cartService.validCart(request, response);
		ECPSUtils.printJSON(result, response);
	}
}
