package cn.xclick.web.restful.security.rest.crud.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.xclick.web.restful.security.rest.crud.data.Constants;

public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = Logger.getLogger(AuthorizationInterceptor.class);
	
	@Override  
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
    	throws Exception {
		logger.debug("logger prehandle:"+request.getServletPath());
		String token = request.getHeader(Constants.TOKEN);
		logger.debug("token="+token);
		if(StringUtils.isEmpty(token)) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			return false ; 
		}else if("1".equals(token)){
			response.setStatus(HttpStatus.GONE.value());
			return false ;
		}
		return true ;
	}
	@Override  
	public void postHandle(HttpServletRequest request, HttpServletResponse response,Object handler, ModelAndView modelAndView)
            throws Exception{
		logger.debug("logger posthandle");
		
	}
}
