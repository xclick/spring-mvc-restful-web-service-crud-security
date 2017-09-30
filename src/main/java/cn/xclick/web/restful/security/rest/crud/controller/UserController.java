package cn.xclick.web.restful.security.rest.crud.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import cn.xclick.web.restful.security.rest.crud.model.User;
import cn.xclick.web.restful.security.rest.crud.service.UserService;

@Controller
@RequestMapping(value = "/users")
public class UserController {
	private static final Logger logger = Logger.getLogger(UserController.class);
	
	@Autowired
	UserService userService ;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseEntity<List<User>> getAll(
			@RequestParam(value = "offset", defaultValue = "0") int offset,
			@RequestParam(value = "count", defaultValue = "10") int count
			) {
        logger.info("getting all users:offset="+offset+",count="+count);
        List<User> users = userService.getAll(offset,count);
        if (users == null || users.isEmpty()){
            return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);
        }
        
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> get(@PathVariable("id")int id){
		logger.info("getting user with id: "+id);
        User user = userService.get(id);
        if (user == null){
        	logger.info("user with id not found: id="+id);
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Void> create(@RequestBody User user, UriComponentsBuilder ucBuilder){
		logger.info("creating new user: "+ user);
        if (userService.exists(user)){
        	logger.info("a user with name [" + user.getName() + "] already exists");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        userService.create(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public ResponseEntity<User> update(@PathVariable int id, @RequestBody User user){
    	logger.info("updating user:"+user);
        User currentUser = userService.get(id);
        if (currentUser == null){
        	logger.info("User not found: id="+id);
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }

        currentUser.setId(user.getId());
        currentUser.setName(user.getName());
        
        userService.update(user);
        return new ResponseEntity<User>(currentUser, HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("id") int id){
    	logger.info("deleting user with id: "+id);
        User user = userService.get(id);
        if (user == null){
        	logger.info("Unable to delete. User not found: id="+id);
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        userService.delete(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
