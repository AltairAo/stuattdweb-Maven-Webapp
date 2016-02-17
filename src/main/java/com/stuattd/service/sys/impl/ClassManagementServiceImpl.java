package com.stuattd.service.sys.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.stuattd.dao.sys.ClassManagementDao;
import com.stuattd.service.sys.ClassManagementService;

import core.service.BaseService;

import jit.gwm.stuattd.model.Sbjjbxx;

@Service
public class ClassManagementServiceImpl extends BaseService<Sbjjbxx> implements
		ClassManagementService {

	private ClassManagementDao classManagementDao;

	@Resource
	public void setClassManagementDao(ClassManagementDao classManagementDao) {
		this.classManagementDao = classManagementDao;
		this.dao = classManagementDao;

	}

	@Override
	public List<Sbjjbxx> getClassType() {
		return (classManagementDao).getClassType();
	}

	@Override
	public boolean saveClass(Sbjjbxx entity) {
		return (classManagementDao).saveClass(entity);
	}

}
