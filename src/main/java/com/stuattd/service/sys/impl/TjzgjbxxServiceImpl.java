package com.stuattd.service.sys.impl;

import javax.annotation.Resource;

import jit.gwm.stuattd.model.Tjzgjbxx;

import org.springframework.stereotype.Service;

import com.stuattd.dao.sys.TjzgjbxxDao;
import com.stuattd.service.sys.TjzgjbxxService;

import core.service.BaseService;

@Service("tjzgjbxxService1")
public class TjzgjbxxServiceImpl extends BaseService<Tjzgjbxx> implements TjzgjbxxService {

	@Resource(name="tjzgjbxxDao1")
	private TjzgjbxxDao tjzgjbxxDao1;
	
	@Override
	public void save(Tjzgjbxx tjzgjbxx) {
		tjzgjbxxDao1.save(tjzgjbxx);
	}

}
