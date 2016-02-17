package jit.gwm.stuattd.service.apply.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import core.service.BaseService;
import jit.gwm.stuattd.dao.approve.ClassInfoDao;
import jit.gwm.stuattd.dao.apply.AqjxxDao;
import jit.gwm.stuattd.model.Aqjxx;
import jit.gwm.stuattd.model.Sbjjbxx;
import jit.gwm.stuattd.service.apply.ClassInfoService;

@Service
public class ClassInfoServiceImpl extends BaseService<Sbjjbxx> implements ClassInfoService {
	
	
	private ClassInfoDao ClassInfoDao;
	
	@Resource
	public void setAttdApproveDao(ClassInfoDao ClassInfoDao) {
		this.ClassInfoDao = ClassInfoDao;
		this.dao = ClassInfoDao;
	}
	@Override
	public List<Sbjjbxx> getClassInfo() {
		return (ClassInfoDao).getClassInfo();
	}
}
