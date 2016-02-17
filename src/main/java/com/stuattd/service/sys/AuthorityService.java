package com.stuattd.service.sys;

import java.util.List;

import com.stuattd.model.sys.Authority;
import com.stuattd.model.sys.RoleAuthority;

import core.service.Service;

/**
 * @开发商:jit.gwm
 */
public interface AuthorityService extends Service<Authority> {

	List<Authority> queryByParentIdAndRole(Short role);

	List<Authority> queryChildrenByParentIdAndRole(Long parentId, Short role);

	String querySurfaceAuthorityList(List<RoleAuthority> queryByProerties, Long id, String buttons);

}
