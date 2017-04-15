package com.rl.ecps.service;

import java.util.List;

import com.rl.ecps.model.EbFeature;
import com.rl.ecps.utils.Page;
import com.rl.ecps.utils.QueryCondition;

public interface EbFeatureService {
	
	public List<EbFeature> selectCommFeature();
	
	public List<EbFeature> selectSpecFeature();

	public List<EbFeature> selectIsSelectFeature(); 
}
