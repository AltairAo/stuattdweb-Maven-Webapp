package com.stuattd.service.sys.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import jit.gwm.stuattd.model.Sxsjbxx;

import com.stuattd.dao.sys.SxsjbxxDao;
import com.stuattd.service.sys.SxsjbxxService;

import core.service.BaseService;
import core.support.BaseParameter;
import core.support.QueryResult;

@Service("sxsjbxxService")
public class SxsjbxxServiceImpl extends BaseService<Sxsjbxx> implements SxsjbxxService {

	@Resource(name="sxsjbxxDao")
	private SxsjbxxDao sxsjbxxDao;
	
	@Override
	public void save(Sxsjbxx sxsjbxx) {
		sxsjbxxDao.save(sxsjbxx);
	}

}
