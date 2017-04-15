package com.rl.ecps.service;

import java.util.List;

import com.rl.ecps.model.EbBrand;

public interface EbBrandService {

	public void saveBrand(EbBrand brand);
	
	public List<EbBrand> selectBrandAll();
	
	public List<EbBrand> validBrandName(String brandName);
	
}
