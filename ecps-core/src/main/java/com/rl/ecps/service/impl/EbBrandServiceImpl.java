package com.rl.ecps.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rl.ecps.dao.EbBrandDao;
import com.rl.ecps.model.EbBrand;
import com.rl.ecps.service.EbBrandService;
@Service
public class EbBrandServiceImpl implements EbBrandService{

	@Autowired
	private EbBrandDao brandDao;
	
	public void saveBrand(EbBrand brand) {
		brandDao.saveBrand(brand);
	}

	public List<EbBrand> selectBrandAll() {
		return brandDao.selectBrandAll();
	}

	public List<EbBrand> validBrandName(String brandName) {
		return brandDao.validBrandName(brandName);
	}

}
