package com.rl.ecps.dao;

import java.util.List;

import com.rl.ecps.model.EbFeature;
import com.rl.ecps.model.EbItem;
import com.rl.ecps.utils.QueryCondition;

public interface EbFeatureDao {

	public List<EbFeature> selectCommFeature();
	
	public List<EbFeature> selectSpecFeature();

	public List<EbFeature> selectIsSelectFeature();
} 
