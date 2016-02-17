package jit.gwm.stuattd.service.apply.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import core.service.BaseService;
import jit.gwm.stuattd.dao.approve.AttdApproveDao;
import jit.gwm.stuattd.model.Aqjxx;
import jit.gwm.stuattd.model.Sxsjbxx;
import jit.gwm.stuattd.service.apply.AttdApproveService;

@Service
public class AttdApproveServiceImpl extends BaseService<Aqjxx> implements
		AttdApproveService {

	private AttdApproveDao attdApproveDao;

	@Resource
	public void setAttdApproveDao(AttdApproveDao attdApproveDao) {
		this.attdApproveDao = attdApproveDao;
		this.dao = attdApproveDao;
	}

	@Override
	public List<Aqjxx> getTotalFromCgx() {
		return (attdApproveDao).getTotalFromCgx();
	}

	@Override
	public List<Sxsjbxx> getStuName(Long xsjbxxId) {
		return (attdApproveDao).getStuName(xsjbxxId);
	}

	@Override
	public List<Aqjxx> getInfoByClass(int classId) {
		return (attdApproveDao).getInfoByClass(classId);
	}

	@Override
	public List<Aqjxx> getInfoByCondition(int classId, String stuName) {
		return (attdApproveDao).getInfoByCondition(classId, stuName);
	}

	@Override
	public boolean Withdraw(int id) {
		return (attdApproveDao).Withdraw(id);
	}

	@Override
	public boolean Negative(int id, String bpyy) {
		return (attdApproveDao).Negative(id, bpyy);
	}

	@Override
	public boolean Pass(int id) {
		return (attdApproveDao).Pass(id);
	}

}
