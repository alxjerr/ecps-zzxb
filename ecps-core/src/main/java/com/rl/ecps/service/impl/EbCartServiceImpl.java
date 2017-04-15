package com.rl.ecps.service.impl;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rl.ecps.dao.EbSkuDao;
import com.rl.ecps.model.EbCart;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.model.EbSpecValue;
import com.rl.ecps.service.EbCartService;
import com.rl.ecps.utils.ECPSUtils;

@Service
public class EbCartServiceImpl implements EbCartService{

	@Autowired
	private EbSkuDao skuDao;
	
	public void addCart(Long skuId, Integer quantity,
			HttpServletRequest request, HttpServletResponse response) {
		
		List<EbCart> cartList = new ArrayList<EbCart>();
		//把jsonArray转换成java对象
		JsonConfig jc = new JsonConfig();
		//设置要转换的类型
		jc.setRootClass(EbCart.class);
		jc.setExcludes(new String[]{"sku"});
		String cartKey = ECPSUtils.readProp("ecps_cart_data");
		//查询浏览器中是否存储购物车的cookie
		Cookie[] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0){
			for(Cookie cookie : cookies){
				//获得cookie的name
				String cookieKey = cookie.getName();
				//如果cookie中存储在购物车模块的cookie证明购物车中是有数据
				if(StringUtils.equals(cartKey, cookieKey)){
					//获得购物车cookie的value
					String cartVal = cookie.getValue();
					//base64解码
					cartVal = URLDecoder.decode(cartVal);
					//把json字符串转换json数组
					JSONArray ja = JSONArray.fromObject(cartVal);
					//把jsonArray转换成java对象的集合
					cartList = (List<EbCart>) JSONSerializer.toJava(ja, jc);
					boolean isExsit = false;
					for(EbCart cart : cartList){
						if(cart.getSkuId().longValue() == skuId.longValue()){
							cart.setQuantity(cart.getQuantity()+ quantity);
							isExsit = true;
							break;
						}
					}
					//当前浏览器中不存要添加的数据
					if(!isExsit){
						EbCart cart = new EbCart();
						cart.setSkuId(skuId);
						cart.setQuantity(quantity);
						cartList.add(cart);
					}
				}
			}
		}
		if(cartList.size() == 0){
			EbCart cart = new EbCart();
			cart.setSkuId(skuId);
			cart.setQuantity(quantity);
			cartList.add(cart);
		}
		//把java对象集合转换成JSONArray
		JSONArray ja = JSONArray.fromObject(cartList, jc);
		//把JSONArray转换成字符串
		String result=  ja.toString();
		result = URLEncoder.encode(result);
		//创建cookie
		Cookie cookie= new Cookie(cartKey, result);
		cookie.setMaxAge(Integer.MAX_VALUE);
		cookie.setPath("/");
		//把cookie写入浏览器
		response.addCookie(cookie);
		
	}

	public List<EbCart> selectCart(HttpServletRequest request,
			HttpServletResponse response) {
		List<EbCart> cartList = new ArrayList<EbCart>();
		//把jsonArray转换成java对象
		JsonConfig jc = new JsonConfig();
		//设置要转换的类型
		jc.setRootClass(EbCart.class);
		jc.setExcludes(new String[]{"sku"});
		String cartKey = ECPSUtils.readProp("ecps_cart_data");
		//查询浏览器中是否存储购物车的cookie
		Cookie[] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0){
			for(Cookie cookie : cookies){
				//获得cookie的name
				String cookieKey = cookie.getName();
				//如果cookie中存储在购物车模块的cookie证明购物车中是有数据
				if(StringUtils.equals(cartKey, cookieKey)){
					//获得购物车cookie的value
					String cartVal = cookie.getValue();
					//base64解码
					cartVal = URLDecoder.decode(cartVal);
					//把json字符串转换json数组
					JSONArray ja = JSONArray.fromObject(cartVal);
					//把jsonArray转换成java对象的集合
					cartList = (List<EbCart>) JSONSerializer.toJava(ja, jc);
					for(EbCart cart : cartList){
						EbSku sku = skuDao.getSkuDetail(cart.getSkuId());
						cart.setSku(sku);
					}
					
				}
			}
		}
		return cartList;
	}

	public void updateCart(Long skuId, Integer quantity,
			HttpServletRequest request, HttpServletResponse response) {
		List<EbCart> cartList = new ArrayList<EbCart>();
		//把jsonArray转换成java对象
		JsonConfig jc = new JsonConfig();
		//设置要转换的类型
		jc.setRootClass(EbCart.class);
		jc.setExcludes(new String[]{"sku"});
		String cartKey = ECPSUtils.readProp("ecps_cart_data");
		//查询浏览器中是否存储购物车的cookie
		Cookie[] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0){
			for(Cookie cookie : cookies){
				//获得cookie的name
				String cookieKey = cookie.getName();
				//如果cookie中存储在购物车模块的cookie证明购物车中是有数据
				if(StringUtils.equals(cartKey, cookieKey)){
					//获得购物车cookie的value
					String cartVal = cookie.getValue();
					//base64解码
					cartVal = URLDecoder.decode(cartVal);
					//把json字符串转换json数组
					JSONArray ja = JSONArray.fromObject(cartVal);
					//把jsonArray转换成java对象的集合
					cartList = (List<EbCart>) JSONSerializer.toJava(ja, jc);
					for(EbCart cart : cartList){
						if(cart.getSkuId().longValue() == skuId.longValue()){
							cart.setQuantity(quantity);
							break;
						}
					}
					
				}
			}
		}
		
		//把java对象集合转换成JSONArray
		JSONArray ja = JSONArray.fromObject(cartList, jc);
		//把JSONArray转换成字符串
		String result=  ja.toString();
		result = URLEncoder.encode(result);
		//创建cookie
		Cookie cookie= new Cookie(cartKey, result);
		cookie.setMaxAge(Integer.MAX_VALUE);
		cookie.setPath("/");
		//把cookie写入浏览器
		response.addCookie(cookie);
	}

	public void deleteCart(Long skuId, HttpServletRequest request,
			HttpServletResponse response) {
		List<EbCart> cartList = new ArrayList<EbCart>();
		//把jsonArray转换成java对象
		JsonConfig jc = new JsonConfig();
		//设置要转换的类型
		jc.setRootClass(EbCart.class);
		jc.setExcludes(new String[]{"sku"});
		String cartKey = ECPSUtils.readProp("ecps_cart_data");
		//查询浏览器中是否存储购物车的cookie
		Cookie[] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0){
			for(Cookie cookie : cookies){
				//获得cookie的name
				String cookieKey = cookie.getName();
				//如果cookie中存储在购物车模块的cookie证明购物车中是有数据
				if(StringUtils.equals(cartKey, cookieKey)){
					//获得购物车cookie的value
					String cartVal = cookie.getValue();
					//base64解码
					cartVal = URLDecoder.decode(cartVal);
					//把json字符串转换json数组
					JSONArray ja = JSONArray.fromObject(cartVal);
					//把jsonArray转换成java对象的集合
					cartList = (List<EbCart>) JSONSerializer.toJava(ja, jc);
					for(int i = 0; i < cartList.size(); i++){
						EbCart cart = cartList.get(i);
						if(cart.getSkuId().longValue() == skuId.longValue()){
							cartList.remove(cart);
						}
					}
					
				}
			}
		}
		
		//把java对象集合转换成JSONArray
		JSONArray ja = JSONArray.fromObject(cartList, jc);
		//把JSONArray转换成字符串
		String result=  ja.toString();
		result = URLEncoder.encode(result);
		//创建cookie
		Cookie cookie= new Cookie(cartKey, result);
		cookie.setMaxAge(Integer.MAX_VALUE);
		cookie.setPath("/");
		//把cookie写入浏览器
		response.addCookie(cookie);
		
	}

	public void clearCart(HttpServletRequest request,
			HttpServletResponse response) {

		List<EbCart> cartList = new ArrayList<EbCart>();
		//把jsonArray转换成java对象
		JsonConfig jc = new JsonConfig();
		//设置要转换的类型
		jc.setRootClass(EbCart.class);
		jc.setExcludes(new String[]{"sku"});
		String cartKey = ECPSUtils.readProp("ecps_cart_data");
		//查询浏览器中是否存储购物车的cookie
		Cookie[] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0){
			for(Cookie cookie : cookies){
				//获得cookie的name
				String cookieKey = cookie.getName();
				//如果cookie中存储在购物车模块的cookie证明购物车中是有数据
				if(StringUtils.equals(cartKey, cookieKey)){
					//获得购物车cookie的value
					String cartVal = cookie.getValue();
					//base64解码
					cartVal = URLDecoder.decode(cartVal);
					//把json字符串转换json数组
					JSONArray ja = JSONArray.fromObject(cartVal);
					//把jsonArray转换成java对象的集合
					cartList = (List<EbCart>) JSONSerializer.toJava(ja, jc);
					cartList.clear();
					
				}
			}
		}
		
		//把java对象集合转换成JSONArray
		JSONArray ja = JSONArray.fromObject(cartList, jc);
		//把JSONArray转换成字符串
		String result=  ja.toString();
		result = URLEncoder.encode(result);
		//创建cookie
		Cookie cookie= new Cookie(cartKey, result);
		cookie.setMaxAge(Integer.MAX_VALUE);
		cookie.setPath("/");
		//把cookie写入浏览器
		response.addCookie(cookie);
	}

	public String validCart(HttpServletRequest request,
			HttpServletResponse response) {
		String result = "success";
		
		List<EbCart> cartList = new ArrayList<EbCart>();
		//把jsonArray转换成java对象
		JsonConfig jc = new JsonConfig();
		//设置要转换的类型
		jc.setRootClass(EbCart.class);
		jc.setExcludes(new String[]{"sku"});
		String cartKey = ECPSUtils.readProp("ecps_cart_data");
		//查询浏览器中是否存储购物车的cookie
		Cookie[] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0){
			for(Cookie cookie : cookies){
				//获得cookie的name
				String cookieKey = cookie.getName();
				//如果cookie中存储在购物车模块的cookie证明购物车中是有数据
				if(StringUtils.equals(cartKey, cookieKey)){
					//获得购物车cookie的value
					String cartVal = cookie.getValue();
					//base64解码
					cartVal = URLDecoder.decode(cartVal);
					//把json字符串转换json数组
					JSONArray ja = JSONArray.fromObject(cartVal);
					//把jsonArray转换成java对象的集合
					cartList = (List<EbCart>) JSONSerializer.toJava(ja, jc);
					for(EbCart cart : cartList){
						EbSku sku = skuDao.getSkuDetail(cart.getSkuId());
						if(cart.getQuantity().intValue() > sku.getStockInventory().intValue()){
							//拼接商品名称
							result = result + sku.getItem().getItemName();
							//拼接商品规格信息
							List<EbSpecValue> specList = sku.getSpecList();
							for(EbSpecValue spec: specList){
								result = result + spec.getSpecValue();
							}
							result = result + "库存不足"+cart.getQuantity()+"个,实际只有"+sku.getStockInventory()+"个";
							break;
						}
					}
					
				}
			}
		}
		
		
		return result;
	}

	

}
