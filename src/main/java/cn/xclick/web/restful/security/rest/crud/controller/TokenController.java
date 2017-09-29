package cn.xclick.web.restful.security.rest.crud.controller;

import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import cn.xclick.web.restful.security.rest.crud.model.UserToken;
import cn.xclick.web.restful.security.rest.crud.utils.Utils;

@RestController
@RequestMapping("/tokens")
public class TokenController {
	private static final Logger logger = Logger.getLogger(TokenController.class);

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<UserToken> create(@RequestBody Map<String,Object> map){
		logger.info("creating new token.");
		UserToken result = new UserToken();
		String username = Utils.getMapString(map, "username");
		String password = Utils.getMapString(map, "password");
		result.setUsername(username);
		result.setToken(UUID.randomUUID().toString());
		return new ResponseEntity<>(result,HttpStatus.OK) ;
	}		
	
}
