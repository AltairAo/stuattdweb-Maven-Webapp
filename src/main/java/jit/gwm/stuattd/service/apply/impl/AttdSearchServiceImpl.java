package jit.gwm.stuattd.service.apply.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import core.service.BaseService;
import jit.gwm.stuattd.dao.apply.AqjxxDao;
import jit.gwm.stuattd.dao.apply.AttdDraftDao;
import jit.gwm.stuattd.dao.apply.AttdSearchDao;
import jit.gwm.stuattd.model.Aqjxx;
import jit.gwm.stuattd.service.apply.AttdSearchService;

@Service
public class AttdSearchServiceImpl extends BaseService<Aqjxx> implements AttdSearchService {

	
	private AqjxxDao aqjxxDao;
	private AttdSearchDao attdSearchDao;

	@Resource
	public void setAqjxxDao(AqjxxDao aqjxxDao) {
		this.aqjxxDao = aqjxxDao;
		this.dao=aqjxxDao;
	}

	@Resource
	public void setAttdSearchDao(AttdSearchDao attdSearchDao) {
		this.attdSearchDao = attdSearchDao;
	}

	@Override
	public List<Aqjxx> getTotalFromCgx(Long xsjbxxId) {
		return (attdSearchDao).getTotalFromCgx(xsjbxxId);
	}
	
	@Override
	public List<Aqjxx> getInfoByCondition(Long xsjbxxId, String kssj,
			String jssj) {
		return (attdSearchDao).getInfoByCondition(xsjbxxId, kssj, jssj);
	}

}
