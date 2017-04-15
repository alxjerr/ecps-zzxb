package com.rl.ecps.dao;

import java.util.List;

import com.rl.ecps.model.EbArea;

public interface EbAreaDao {

	public List<EbArea> selectAreaByParentId(Long areaId);
	
}
