package com.rl.ecps.dao;

import java.util.List;

import com.rl.ecps.model.EbBrand;

public interface EbBrandDao {

	public void saveBrand(EbBrand brand);
	
	public List<EbBrand> selectBrandAll();
	
	public List<EbBrand> validBrandName(String brandName);
	
}
