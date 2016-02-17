package jit.gwm.stuattd.service.apply.impl;

import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import jit.gwm.stuattd.dao.apply.TjzgjbxxDao;
import jit.gwm.stuattd.model.Tjzgjbxx;
import jit.gwm.stuattd.service.apply.TjzgjbxxService;

import org.springframework.stereotype.Service;

import core.service.BaseService;

/**
 * @开发商:jit.gwm
 */
@Service
public class TjzgjbxxServiceImpl extends BaseService<Tjzgjbxx> implements
		TjzgjbxxService {

	private TjzgjbxxDao tjzgjbxxDao;

	@Resource
	public void setTjzgjbxxDao(TjzgjbxxDao tjzgjbxxDao) {
		this.tjzgjbxxDao = tjzgjbxxDao;
		this.dao = tjzgjbxxDao;
	}

	@Override
	public List<Tjzgjbxx> getApproveTeachers(Long xsjbxxId) {
		return tjzgjbxxDao.getApproveTeachers(xsjbxxId);
	}

}
