package cn.xclick.web.restful.security.rest.crud.model;

public class BaseResult {
	private int code ;
	private String message ;
	
	public int getCode(){
		return this.code;
	}
	public void setCode(int code){
		this.code = code ;
	}
	public String getMessage(){
		return this.message;
	}
	public void setMessage(String message){
		this.message = message ;
	}
	
}
