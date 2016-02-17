package com.stuattd.controller.sys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jit.gwm.stuattd.model.Aqjxx;
import jit.gwm.stuattd.model.Sbjjbxx;
import jit.gwm.stuattd.model.Sxsjbxx;
import jit.gwm.stuattd.model.Tjzgjbxx;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.stuattd.core.Constant;
import com.stuattd.core.StuattdBaseController;
import com.stuattd.model.sys.SysUser;
import com.stuattd.service.sys.SbjjbxxService;
import com.stuattd.service.sys.SxsjbxxService;
import com.stuattd.service.sys.SysUserService;
import com.stuattd.service.sys.TjzgjbxxService;
import com.stuattd.controller.sys.ImportExecl;

import core.util.MD5;

@Controller
@RequestMapping("/sys/fileUpload")
public class FileUploadController extends StuattdBaseController<Aqjxx>
		implements Constant {
	@Resource
	private SysUserService sysUserService;
	@Resource(name = "sxsjbxxService")
	private SxsjbxxService sxsjbxxService;
	@Resource(name = "sbjjbxxService")
	private SbjjbxxService sbjjbxxService;
	@Resource(name = "tjzgjbxxService1")
	private TjzgjbxxService tjzgjbxxService1;

	@RequestMapping(value = "/getFile", method = { RequestMethod.POST,
			RequestMethod.GET })
	public void getFile(@RequestParam("file") MultipartFile file,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// String txtname = request.getParameter("rb");
		ImportExecl poi = new ImportExecl();
		String name = file.getOriginalFilename();
		// name = name.substring(0, name.indexOf("."));
		// System.out.println(name);
		// Class clazz =
		// Class.forName("jit.gwm.stuattd.model."+name.replaceFirst(name.substring(0,
		// 1),name.substring(0, 1).toUpperCase()));
		List<List<String>> list = poi.read(file.getInputStream(), false);
		if (list != null) {
			// try {
			if (name.startsWith("sxsjbxx")) {
				// Sxsjbxx sxsjbxx = new Sxsjbxx();
				for (int i = 1; i < list.size(); i++) {
					// System.out.print("第" + (i) + "行");??
					Map<String, String> map = new HashMap<String, String>();
					List<String> cellList = list.get(i);
					for (int j = 0; j < cellList.size(); j++) {
						map.put(list.get(0).get(j), cellList.get(j));
					}
					JSONObject jsonObject = JSONObject.fromObject(map);
					// System.out.println(jsonObject);
					Sxsjbxx sxsjbxx = (Sxsjbxx) JSONObject.toBean(jsonObject,
							Sxsjbxx.class);
					// System.out.println(sxsjbxx.getId());
					SysUser student = new SysUser();
					student.setEmail("A510@jit.edu.cn");
					student.setUserName(sxsjbxx.getXh());
					student.setPassword(MD5.crypt(sxsjbxx.getXh()));
					student.setRealName(sxsjbxx.getXm());
					student.setTel("123456");
					student.setRole((short) 3);
					sysUserService.persist(student);
					sxsjbxxService.save(sxsjbxx);
				}
			} else if (name.startsWith("sbjjbxx")) {
				for (int i = 1; i < list.size(); i++) {
					// System.out.print("第" + (i) + "行");
					Map<String, String> map = new HashMap<String, String>();
					List<String> cellList = list.get(i);
					for (int j = 0; j < cellList.size(); j++) {
						map.put(list.get(0).get(j), cellList.get(j));
					}
					JSONObject jsonObject = JSONObject.fromObject(map);
					// System.out.println(jsonObject);
					Sbjjbxx sbjjbxx = (Sbjjbxx) JSONObject.toBean(jsonObject,
							Sbjjbxx.class);
					// System.out.println(sxsjbxx.getId());
					sbjjbxxService.save(sbjjbxx);
				}
			} else if (name.startsWith("tjzgjbxx")) {
				for (int i = 1; i < list.size(); i++) {
					// System.out.print("第" + (i) + "行");
					Map<String, String> map = new HashMap<String, String>();
					List<String> cellList = list.get(i);
					for (int j = 0; j < cellList.size(); j++) {
						map.put(list.get(0).get(j), cellList.get(j));
					}
					JSONObject jsonObject = JSONObject.fromObject(map);
					// System.out.println(jsonObject);
					Tjzgjbxx tjzgjbxx = (Tjzgjbxx) JSONObject.toBean(
							jsonObject, Tjzgjbxx.class);
					// System.out.println(sxsjbxx.getId());
					SysUser teacher = new SysUser();
					teacher.setEmail("A510@jit.edu.cn");
					teacher.setUserName(tjzgjbxx.getGh());
					teacher.setPassword(MD5.crypt(tjzgjbxx.getGh()));
					teacher.setRealName(tjzgjbxx.getXm());
					teacher.setTel("123456");
					teacher.setRole((short) 2);
					sysUserService.persist(teacher);
					tjzgjbxxService1.save(tjzgjbxx);
				}
			}
			writeJSON(response, "{success:true}");
			// } catch (Exception e) {
			// e.printStackTrace();
			// writeJSON(response, "{success:false}");
			// }
		}
	}

}
