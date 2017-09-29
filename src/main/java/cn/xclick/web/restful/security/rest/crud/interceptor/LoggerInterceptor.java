package cn.xclick.web.restful.security.rest.crud.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.xclick.web.restful.security.rest.crud.data.Constants;

public class LoggerInterceptor extends HandlerInterceptorAdapter{
	private static final Logger logger = Logger.getLogger(LoggerInterceptor.class);
	
	@Override  
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
    	throws Exception {
		
		
		logger.debug("logger prehandle:"+request.getServletPath());
		return true ;
	}
	@Override  
	public void postHandle(HttpServletRequest request, HttpServletResponse response,Object handler, ModelAndView modelAndView)
            throws Exception{
		logger.debug("logger posthandle");
		
	}
}
