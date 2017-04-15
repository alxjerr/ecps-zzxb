package com.rl.ecps.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.rl.ecps.model.TsPtlUser;

public class LoginInterceptor implements HandlerInterceptor{

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		TsPtlUser user = (TsPtlUser) request.getSession().getAttribute("user");
		if(user == null){
			//获得项目的根路径
			String contextPath = request.getContextPath();
			response.sendRedirect(contextPath + "/user/toLogin.do");
			return false;
		}
		return true;
	}

	
	
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

}
