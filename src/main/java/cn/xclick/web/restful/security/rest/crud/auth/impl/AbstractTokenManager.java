package cn.xclick.web.restful.security.rest.crud.auth.impl;

import cn.xclick.web.restful.security.rest.crud.auth.TokenManager;
import cn.xclick.web.restful.security.rest.crud.exception.MethodNotSupportException;

public abstract class AbstractTokenManager implements TokenManager {
	protected int tokenExpireSeconds = 7 * 24 * 3600;

    protected boolean singleTokenWithUser = true;

    protected boolean flushExpireAfterOperation = true;
    
    public void setTokenExpireSeconds(int tokenExpireSeconds) {
        this.tokenExpireSeconds = tokenExpireSeconds;
    }

    public void setSingleTokenWithUser(boolean singleTokenWithUser) {
        this.singleTokenWithUser = singleTokenWithUser;
    }

    public void setFlushExpireAfterOperation(boolean flushExpireAfterOperation) {
        this.flushExpireAfterOperation = flushExpireAfterOperation;
    }
    
	@Override
	public void delRelationshipByKey(String key) {
		//如果是多个Token关联同一个Key，不允许直接通过Key删除所有Token，防止误操作
        if (!singleTokenWithUser) {
            throw new MethodNotSupportException("Not allowed if one with more than one token.");
        }
        delSingleRelationshipByKey(key);
	}

	protected abstract void delSingleRelationshipByKey(String key);
	
	@Override
    public void createRelationship(String key, String token) {
        //根据设置的每个用户是否只允许绑定一个Token，调用不同的方法
        if (singleTokenWithUser) {
            createSingleRelationship(key, token);
        } else {
            createMultipleRelationship(key, token);
        }
    }
	
	protected abstract void createMultipleRelationship(String key, String token);
	
	protected abstract void createSingleRelationship(String key, String token);
	
	@Override
	public String getKey(String token) {
		String key = getKeyByToken(token);
        //根据设置，在每次有效操作后刷新过期时间
        if (key != null && flushExpireAfterOperation) {
            flushExpireAfterOperation(key, token);
        }
        return key;
	}
	
	protected abstract String getKeyByToken(String token);
	
	protected abstract void flushExpireAfterOperation(String key, String token);
	

}
