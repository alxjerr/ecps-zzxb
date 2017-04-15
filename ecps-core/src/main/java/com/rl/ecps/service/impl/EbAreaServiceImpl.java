package com.rl.ecps.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rl.ecps.dao.EbAreaDao;
import com.rl.ecps.model.EbArea;
import com.rl.ecps.service.EbAreaService;

@Service
public class EbAreaServiceImpl implements EbAreaService{

	@Autowired
	private EbAreaDao areaDao;
	
	public List<EbArea> selectAreaByParentId(Long areaId) {
		return areaDao.selectAreaByParentId(areaId);
	}

}
