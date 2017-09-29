package cn.xclick.web.restful.security.rest.crud.model;

public class User {
	private int id ;
	private String name ;
	
	public int getId(){
		return this.id;
	}
	public void setId(int id){
		this.id = id ;
	}
	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name ;
	}
	
	public String toString(){
		return "User:{" +
				"id:" + id + 
				", name:" + name +
				"}";
				
	}
}
