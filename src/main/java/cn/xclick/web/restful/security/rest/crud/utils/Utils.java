package cn.xclick.web.restful.security.rest.crud.utils;

import java.util.Map;

public class Utils {
	public static String getMapString(Map<String,Object> map, String key){
		return getMapString(map, key, "");
	}
	
	public static String getMapString(Map<String,Object> map, String key, String defaultValue){
		if(map!=null&&map.containsKey(key)&&map.get(key)!=null) {
			return map.get(key).toString();
		}
		return defaultValue ;
	}
}
