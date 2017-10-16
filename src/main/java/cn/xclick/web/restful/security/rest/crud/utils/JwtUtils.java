package cn.xclick.web.restful.security.rest.crud.utils;

import java.util.Date;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import cn.xclick.web.restful.security.rest.crud.auth.data.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtils {
	private static final Logger logger = Logger.getLogger(JwtUtils.class);
	
	public static void main(String[] args){
		logger.info("start");
		String jwt = JwtUtils.buildJwt(1);
		logger.info("jwt:"+jwt);
		boolean ok = JwtUtils.isJwtValid(jwt);
		logger.info("ok:"+ok);
	}
	
	public static String buildJwt(int expSeconds){
		DateTime expDate = DateTime.now().plusSeconds(expSeconds);
		String jwt = Jwts.builder()
				.signWith(SignatureAlgorithm.HS256, Constants.SECRET_KEY)
				.setExpiration(expDate.toDate())
				.claim("appid", "demo")
				.compact();
		return jwt ;
	}
	
	public static boolean isJwtValid(String jwt){
		try{
			Claims claims = Jwts.parser()
					.setSigningKey(Constants.SECRET_KEY)
					.parseClaimsJws(jwt)
					.getBody();
			String appid = claims.get("appid", String.class);
			if("demo".equals(appid)) return true ;
			return false ;
		}catch(Exception ex){
			logger.error(ex);
		}
		return false ;
	}
}
