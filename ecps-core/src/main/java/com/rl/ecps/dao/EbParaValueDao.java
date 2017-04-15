package com.rl.ecps.dao;

import java.util.List;

import com.rl.ecps.model.EbParaValue;
import com.rl.ecps.utils.QueryCondition;

public interface EbParaValueDao {

	public void saveParaValue(List<EbParaValue> paraList,Long itemId);

}
