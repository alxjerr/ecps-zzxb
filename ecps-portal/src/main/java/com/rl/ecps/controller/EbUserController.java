package com.rl.ecps.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rl.ecps.model.EbArea;
import com.rl.ecps.model.EbShipAddr;
import com.rl.ecps.model.EbShipAddrBean;
import com.rl.ecps.model.TsPtlUser;
import com.rl.ecps.service.EbAreaService;
import com.rl.ecps.service.EbShipAddrService;
import com.rl.ecps.service.TsPtlUserService;
import com.rl.ecps.utils.ECPSUtils;
import com.rl.ecps.utils.MD5;

@Controller
@RequestMapping("/user")
public class EbUserController {
	
	@Autowired
	private TsPtlUserService userService;
	
	@Autowired
	private EbShipAddrService addrService;
	
	@Autowired
	private EbAreaService areaService;
	
	@RequestMapping("/toLogin.do")
	public String toLogin(){
		return "passport/login";
	}
	
	/**
	 * 生成验证码图片
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getImage.do")
	public void getImage(HttpServletRequest request,HttpServletResponse response)throws Exception{
		 System.out.println("#######################生成数字和字母的验证码#######################");  
		 BufferedImage img = new BufferedImage(68,22,BufferedImage.TYPE_INT_RGB);
		 //得到该图片的绘图对象
		 Graphics g = img.getGraphics();
		 Random r = new Random();
		 Color c = new Color(200,150,255);
		 g.setColor(c);
		 //填充整个图片的颜色
		 g.fillRect(0,0, 68,22);
		 //向图片中输出数字和字母
		 StringBuffer sb = new StringBuffer();
		 char[] ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
		 int index,len = ch.length;
		 for(int i = 0;i < 4;i++){
			 index = r.nextInt(len);
			 g.setColor(new Color(r.nextInt(88),r.nextInt(188),r.nextInt(255)));
			 g.setFont(new Font("Arial",Font.BOLD|Font.ITALIC,22));
			 //输出的字体和大小
			 g.drawString(""+ch[index],(i * 15) + 3,18);
			 //写什么数字，在图片的什么位置画
			 sb.append(ch[index]);  
		 }
		 //把图片上的值存储到session中，以便于验证
		 request.getSession().setAttribute("piccode", sb.toString());
		 ImageIO.write(img,"JPG",response.getOutputStream());
	}
	
	@RequestMapping("/login.do")
	public String login(HttpSession session,String username,String password,String captcha,
			Model model){
		//获得正确的验证码
		String rightCap = (String) session.getAttribute("piccode");
		if(!StringUtils.equalsIgnoreCase(captcha,rightCap)){
			model.addAttribute("tip","cap_error");
			return "passport/login";
		}
		password = new MD5().GetMD5Code(password);
		Map<String,String> map = new HashMap<String,String>();
		map.put("username",username);
		map.put("password",password);
		TsPtlUser user = userService.selectUserByUserPass(map);
		if(user == null){
			model.addAttribute("tip","userpass_error");
			return "passport/login";
		}
		session.setAttribute("user",user);
		return "redirect:/item/toIndex.do";
	}
	
	@RequestMapping("/loginAjax.do")
	public void loginAjax(HttpSession session, String username, String password, String captcha, PrintWriter out){
		//获得正确的验证码
		String rightCap = (String) session.getAttribute("piccode");
		if(!StringUtils.equalsIgnoreCase(captcha, rightCap)){
			out.write("cap_error");
			return;
		}
		password = new MD5().GetMD5Code(password);
		Map<String,String> map = new HashMap<String, String>();
		map.put("username",username);
		map.put("password",password);
		TsPtlUser user = userService.selectUserByUserPass(map);
		if(user == null){
			out.write("userpass_error");
			return;
		}
		session.setAttribute("user", user);
		out.write("success");
	}
	
	/**
	 * 获得session中的user
	 * @param session
	 * @param out
	 */
	@RequestMapping("/getUser.do")
	public void getUser(HttpSession session,PrintWriter out){
		TsPtlUser user = (TsPtlUser) session.getAttribute("user");
		JSONObject jo = new JSONObject();
		jo.accumulate("user", user);
		String result = jo.toString();
		out.write(result);
	}
	
	/**
	 * 跳转到个人中心
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("/login/toPersonIndex.do")
	public String toPersonIndex(){
		return "person/index";
	}
	
	/**
	 * 查询用户下的收货地址
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("/login/toAddr.do")
	public String toAddr(HttpSession session,Model model){
		TsPtlUser user = (TsPtlUser) session.getAttribute("user");
		List<EbShipAddrBean> addrList = addrService.selectAddrByUserId(user.getPtlUserId());
		model.addAttribute("addrList",addrList);
		//查询省级别的地址
		List<EbArea> aList = areaService.selectAreaByParentId(0l);
		model.addAttribute("aList",aList);
		return "person/deliverAddress";
	}
	
	/**
	 * 根据收货地址ID查询收货地址
	 * @param shipAddrId
	 * @param response
	 */
	@RequestMapping("/login/getAddrById.do")
	public void getAddrById(Long shipAddrId,HttpServletResponse response){
		EbShipAddr addr = addrService.selectAddrById(shipAddrId);
		JSONObject jo = new JSONObject();
		jo.accumulate("addr", addr);
		String result = jo.toString();
		ECPSUtils.printJSON(result, response);
	}
	
	/**
	 * 添加或者修改收货地址
	 * @return
	 */
	@RequestMapping("/login/saveOrUpdateAddr.do")
	public String saveOrUpdateAddr(HttpSession session,EbShipAddr addr){
		if(addr.getDefaultAddr() == null){
			addr.setDefaultAddr((short)0);
		}
		TsPtlUser user = (TsPtlUser) session.getAttribute("user");
		addr.setPtlUserId(user.getPtlUserId());
		addrService.saveOrUpdateAddr(addr);
		//如果重定向时同一个Controller中有共同的一段路径，那么久不需要来指定出这段路径
		return "redirect:toAddr.do";
	}
	
	@RequestMapping("/loadOption.do")
	public void loadOption(Long parentId,HttpServletResponse response){
		List<EbArea> aList = areaService.selectAreaByParentId(parentId);
		JSONObject jo = new JSONObject();
		jo.accumulate("aList", aList);
		String result = jo.toString();
		ECPSUtils.printJSON(result, response);
	}
	
	
	@RequestMapping("/login/modifyDefaultAddr.do")
	public String modifyDefaultAddr(HttpSession session,Long shipAddrId){
		TsPtlUser user = (TsPtlUser) session.getAttribute("user");
		System.out.println(user.getPtlUserId());
		EbShipAddr addr = addrService.selectAddrById(shipAddrId);
		addr.setPtlUserId(user.getPtlUserId());
		addrService.modifyDefaultAddr(addr);
		return "redirect:toAddr.do";
	}
}
