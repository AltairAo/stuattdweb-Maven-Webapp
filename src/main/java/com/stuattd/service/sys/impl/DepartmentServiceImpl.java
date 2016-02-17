package com.stuattd.service.sys.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.stuattd.dao.sys.DepartmentDao;
import com.stuattd.model.sys.Department;
import com.stuattd.service.sys.DepartmentService;

import core.service.BaseService;

/**
 * @开发商:jit.gwm
 */
@Service
public class DepartmentServiceImpl extends BaseService<Department> implements DepartmentService {

	private DepartmentDao departmentDao;

	@Resource
	public void setDepartmentDao(DepartmentDao departmentDao) {
		this.departmentDao = departmentDao;
		this.dao = departmentDao;
	}

}
