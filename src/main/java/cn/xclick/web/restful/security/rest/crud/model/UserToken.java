package cn.xclick.web.restful.security.rest.crud.model;

public class UserToken extends BaseResult{
	private String username ;
	private String token ;
	
	public String getUsername(){
		return this.username;
	}
	public void setUsername(String username){
		this.username = username ;
	}
	public String getToken(){
		return this.token;
	}
	public void setToken(String token){
		this.token = token ;
	}
}
