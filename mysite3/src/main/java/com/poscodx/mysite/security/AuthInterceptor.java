package com.poscodx.mysite.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.poscodx.mysite.vo.UserVo;

public class AuthInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// 1. handler 종류 확인 
		if (!(handler instanceof HandlerMethod)) {
			// DefaultServletHandler가 처리하는 경우(정적자원, /assets/**, mapping이 안되어있는 자원)  
			return true;
		}
		
		// 2. casting
		HandlerMethod handlerMethod = (HandlerMethod)handler;
		
		// 3. Handler Method의 @Auth 가져오기 
		Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
		
		// 4. Handler Method에 @Auth가 없는 경우
		if (auth == null) {
			return true;
		}
		
		// 5. @Auth가 붙어 있기 때문에 인증(Authentication) 확인 
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		
		// 5-1. 인증이 안되어 있는 경우 
		if (authUser == null) {
			response.sendRedirect(request.getContextPath() + "/user/login");
			// 뒤로 가면 핸들러가 있기때문에 큰일나서 return false 
			return false;
		}
		
		// 6. @Auth가 붙어 있고 인증도 된 경우 
		return true;
		
	}
	
}
