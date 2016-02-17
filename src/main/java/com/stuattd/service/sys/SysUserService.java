package com.stuattd.service.sys;

import java.util.List;

import com.stuattd.model.sys.SysUser;

import core.service.Service;

/**
 * @开发商:jit.gwm
 */
public interface SysUserService extends Service<SysUser> {

	List<SysUser> getSysUserList(List<SysUser> resultList);

}
