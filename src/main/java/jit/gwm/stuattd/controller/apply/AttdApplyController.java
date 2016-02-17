package jit.gwm.stuattd.controller.apply;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.stuattd.core.Constant;
import com.stuattd.core.StuattdBaseController;
import com.stuattd.model.sys.SysUser;
import com.stuattd.service.sys.SysUserService;

import core.extjs.ExtJSBaseParameter;
import core.support.Item;
import core.util.MD5;
import core.web.SystemCache;
import jit.gwm.stuattd.model.Aqjxx;
import jit.gwm.stuattd.model.Tjzgjbxx;
import jit.gwm.stuattd.service.apply.AqjxxService;
import jit.gwm.stuattd.service.apply.TjzgjbxxService;

@Controller
@RequestMapping("/attd/attdApply")
public class AttdApplyController extends StuattdBaseController<Aqjxx> implements Constant {
	
	@Resource
	private TjzgjbxxService tjzgjbxxService;
	
	@Resource
	private AqjxxService aqjxxService;
	
	//通过参数xsjbxxId取得审批该同学的请假单的老师列表
	@RequestMapping("/getApproveTeachers")
	public void getApproveTeachers(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Long xsjbxxId = Long.parseLong(request.getParameter("xsjbxxId"));
		List<Tjzgjbxx> listTjzgjbxx = tjzgjbxxService.getApproveTeachers(xsjbxxId);
		JSONArray jsonArray = new JSONArray();
		for (int ii=0; ii< listTjzgjbxx.size(); ii++) {
			Tjzgjbxx tjzgjbxx = listTjzgjbxx.get(ii) ;
			JSONObject jsonObject = new JSONObject();
			jsonObject.element("ItemText", tjzgjbxx.getXm());
			jsonObject.element("ItemValue",tjzgjbxx.getId());
			jsonArray.add(jsonObject);
		}
		JSONObject resultJSONObject = new JSONObject();
		resultJSONObject.element("list", jsonArray);
		writeJSON(response, resultJSONObject);
	}

	//保存请假单
	@Override
	@RequestMapping(value = "/saveApply", method = { RequestMethod.POST, RequestMethod.GET })
	public void doSave(Aqjxx entity, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ExtJSBaseParameter parameter = ((ExtJSBaseParameter) entity);
		/*SysUser checkuserName = aqjxxService.getByProerties("userName", entity.getUserName());
		if (null != checkuserName && null == entity.getId()) {
			parameter.setSuccess(false);
		} else {*/
		
		//entity.setKssj(Timestamp.valueOf(request.getParameter("kssj")+" 00:00:00"));
		//entity.setJssj(Timestamp.valueOf(request.getParameter("jssj")+" 00:00:00"));
		if (CMD_EDIT.equals(parameter.getCmd())) { //edit
			aqjxxService.update(entity);
			
		} else if (CMD_NEW.equals(parameter.getCmd())) {
			
			entity.setQjzt(new Integer(0));
			aqjxxService.persist(entity);
			parameter.setCmd(CMD_NEW);
		}
		
		parameter.setSuccess(true);
		
		writeJSON(response, parameter);
	}
	@RequestMapping(value = "/Action", method = { RequestMethod.POST, RequestMethod.GET })
	public void Action(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Aqjxx aqjxx=new Aqjxx();
		aqjxx.setId(Long.parseLong(request.getParameter("id")));
		aqjxx.setXsjbxxId(Long.parseLong(request.getParameter("xsjbxxId")));
		aqjxx.setQjly(request.getParameter("qjly"));
		aqjxx.setKssj(Timestamp.valueOf(request.getParameter("kssj")+" 00:00:00"));
		aqjxx.setJssj(Timestamp.valueOf(request.getParameter("jssj")+" 00:00:00"));
		aqjxx.setJzgjbxxId(Long.parseLong(request.getParameter("jzgjbxxId")));
		aqjxx.setQjzt(Integer.parseInt(request.getParameter("qjzt")));
		boolean flag=false;
		if(request.getParameter("action").equals("update")){
			flag=aqjxxService.doUpdate(aqjxx);
		}else if(request.getParameter("action").equals("save")){
			flag=aqjxxService.doSave(aqjxx);
		}
		//System.out.println(id);
		if (flag) {
			writeJSON(response, "{success:true}");
		} else {
			writeJSON(response, "{success:false}");
		}
	}
	
}
