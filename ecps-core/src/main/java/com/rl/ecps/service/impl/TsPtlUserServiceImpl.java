package com.rl.ecps.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rl.ecps.dao.TsPtlUserDao;
import com.rl.ecps.model.TsPtlUser;
import com.rl.ecps.service.TsPtlUserService;

@Service
public class TsPtlUserServiceImpl implements TsPtlUserService{

	@Autowired
	private TsPtlUserDao userDao;
	
	public TsPtlUser selectUserByUserPass(Map<String, String> map) {
		return userDao.selectUserByUserPass(map);
	}

}
