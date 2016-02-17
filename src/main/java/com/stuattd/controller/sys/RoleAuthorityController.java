package com.stuattd.controller.sys;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.stuattd.core.StuattdBaseController;
import com.stuattd.model.sys.RoleAuthority;
import com.stuattd.service.sys.RoleAuthorityService;

/**
 * @开发商:jit.gwm
 */
@Controller
@RequestMapping("/sys/roleauthority")
public class RoleAuthorityController extends StuattdBaseController<RoleAuthority> {

	@Resource
	private RoleAuthorityService roleAuthorityService;

	@RequestMapping(value = "/saveRoleAuthority")
	public void saveRoleAuthority(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Short role = Short.valueOf(request.getParameter("role"));
		String ids = request.getParameter("ids");
		roleAuthorityService.deleteByProperties("role", role);
		String[] idsValue = ids.split(",");
		for (int i = 0; i < idsValue.length; i++) {
			RoleAuthority roleAuthority = new RoleAuthority();
			roleAuthority.setRole(role);
			roleAuthority.setAuthorityId(idsValue[i]);
			roleAuthorityService.persist(roleAuthority);
		}
		writeJSON(response, "{success:true}");
	}

}
