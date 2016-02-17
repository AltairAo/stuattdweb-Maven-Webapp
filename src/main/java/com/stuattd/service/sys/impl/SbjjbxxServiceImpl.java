package com.stuattd.service.sys.impl;

import javax.annotation.Resource;

import jit.gwm.stuattd.model.Sbjjbxx;

import org.springframework.stereotype.Service;

import com.stuattd.dao.sys.SbjjbxxDao;
import com.stuattd.dao.sys.SxsjbxxDao;
import com.stuattd.service.sys.SbjjbxxService;

import core.service.BaseService;

@Service("sbjjbxxService")
public class SbjjbxxServiceImpl extends BaseService<Sbjjbxx> implements SbjjbxxService {

	@Resource(name="sbjjbxxDao")
	private SbjjbxxDao sbjjbxxDao;
	
	@Override
	public void save(Sbjjbxx sbjjbxx) {
		sbjjbxxDao.save(sbjjbxx);
	}

}
