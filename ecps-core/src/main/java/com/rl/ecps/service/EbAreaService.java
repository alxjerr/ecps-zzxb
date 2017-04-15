package com.rl.ecps.service;

import java.util.List;

import com.rl.ecps.model.EbArea;

public interface EbAreaService {
	
	public List<EbArea> selectAreaByParentId(Long areaId);
	
}
