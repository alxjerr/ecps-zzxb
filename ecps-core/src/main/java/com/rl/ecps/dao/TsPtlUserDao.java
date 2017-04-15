package com.rl.ecps.dao;

import java.util.Map;

import com.rl.ecps.model.TsPtlUser;

public interface TsPtlUserDao {
	
	public TsPtlUser selectUserByUserPass(Map<String,String> map);
	
}
