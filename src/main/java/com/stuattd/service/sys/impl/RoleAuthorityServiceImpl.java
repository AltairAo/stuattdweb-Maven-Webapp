package com.stuattd.service.sys.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.stuattd.dao.sys.RoleAuthorityDao;
import com.stuattd.model.sys.RoleAuthority;
import com.stuattd.service.sys.RoleAuthorityService;

import core.service.BaseService;

/**
 * @开发商:jit.gwm
 */
@Service
public class RoleAuthorityServiceImpl extends BaseService<RoleAuthority> implements RoleAuthorityService {

	private RoleAuthorityDao roleAuthorityDao;

	@Resource
	public void setRoleAuthorityDao(RoleAuthorityDao roleAuthorityDao) {
		this.roleAuthorityDao = roleAuthorityDao;
		this.dao = roleAuthorityDao;
	}

}
