package cn.xclick.web.restful.security.rest.crud.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.xclick.web.restful.security.rest.crud.mapper.UserMapper;
import cn.xclick.web.restful.security.rest.crud.model.User;
import cn.xclick.web.restful.security.rest.crud.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	@Resource
	UserMapper userMapper ;
	
	@Override
	public boolean create(User user) {
		try{
			if(this.userMapper.create(user)==0)
				throw new Exception("Not created.");
		}catch(Exception ex){
			logger.error(ex);
		}
		return false;
	}

	@Override
	public boolean delete(int id) {
		try{
			if(this.userMapper.delete(id)==0)
				throw new Exception("Not Deleted.");
		}catch(Exception ex){
			logger.error(ex);
		}
		return false;
	}
	
	@Override
	public List<User> getAll(int offset, int count){
		try{
			return this.userMapper.getAll(offset,count);
		}catch(Exception ex){
			logger.error(ex);
		}
		return null;
	}
	
	@Override
	public User get(int id){
		try{
			User condition = new User();
			condition.setId(id);
			List<User> users = this.userMapper.get(condition) ;
			logger.debug("users:"+users);
			if(users!=null && users.size()>0){
				return users.get(0);
			}
		}catch(Exception ex){
			logger.error(ex);
		}
		return null;
	}
	/*
	@Override
	public List<User> get(User user) {
		try{
			return this.userMapper.get(user) ;
		}catch(Exception ex){
			logger.error(ex);
		}
		return null;
	}*/

	@Override
	public boolean update(User user) {
		try{
			if(this.userMapper.update(user)==0)
				throw new Exception("Not Update.");
		}catch(Exception ex){
			logger.error(ex);
		}
		return false;
	}
	
	@Override
	public boolean exists(User user){
		try{
			User condition = new User();
			condition.setName(user.getName());
			List<User> users = this.userMapper.get(condition) ;
			if(users!=null&&users.size()>0){
				return true ;
			}
		}catch(Exception ex){
			logger.error(ex);
		}
		return false ;
	}

}
