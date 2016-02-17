package jit.gwm.stuattd.service.apply.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import core.service.BaseService;
import jit.gwm.stuattd.dao.approve.ApproveSearchDao;
import jit.gwm.stuattd.model.Aqjxx;
import jit.gwm.stuattd.model.Sxsjbxx;
import jit.gwm.stuattd.service.apply.ApproveSearchService;

@Service
public class ApproveSearchServiceImpl extends BaseService<Aqjxx> implements
		ApproveSearchService {

	private ApproveSearchDao approveSearchDao;

	@Resource
	public void setApproveSearchDao(ApproveSearchDao approveSearchDao) {
		this.approveSearchDao = approveSearchDao;
		this.dao = approveSearchDao;
	
	}

	@Override
	public List<Aqjxx> getTotalFromCgx() {
		return (approveSearchDao).getTotalFromCgx();
	}

	@Override
	public List<Sxsjbxx> getStuName(Long xsjbxxId) {
		return (approveSearchDao).getStuName(xsjbxxId);
	}

	@Override
	public List<Aqjxx> getInfoByClass(int classId) {
		return (approveSearchDao).getInfoByClass(classId);
	}

	@Override
	public List<Aqjxx> getInfoByCondition(int classId, String stuName) {
		return (approveSearchDao).getInfoByCondition(classId, stuName);
	}

	

}
