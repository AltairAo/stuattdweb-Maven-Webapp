package jit.gwm.stuattd.controller.apply;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.stuattd.core.Constant;
import com.stuattd.core.StuattdBaseController;
import com.stuattd.model.sys.SysUser;
import com.stuattd.service.sys.SysUserService;

import core.extjs.ExtJSBaseParameter;
import core.support.Item;
import core.util.MD5;
import core.web.SystemCache;
import jit.gwm.stuattd.model.Aqjxx;
import jit.gwm.stuattd.model.Sbjjbxx;
import jit.gwm.stuattd.model.Tjzgjbxx;
import jit.gwm.stuattd.service.apply.AqjxxService;
import jit.gwm.stuattd.service.apply.TjzgjbxxService;
import jit.gwm.stuattd.service.apply.ClassInfoService;
import jit.gwm.stuattd.service.apply.AttdApproveService;
@Controller
@RequestMapping("/attd/attdApprove")
public class AttdApproveController extends StuattdBaseController<Aqjxx> implements Constant {

	
	@Resource
	private ClassInfoService classInfoService;
	
	@Resource
	private AttdApproveService attdApproveService;
	
	@RequestMapping(value = "/getBjmc", method = { RequestMethod.POST, RequestMethod.GET })
	public void getBjmc(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Sbjjbxx> listSbjjbxx = classInfoService.getClassInfo();
		JSONArray jsonArray = new JSONArray();
		for (int ii=0; ii< listSbjjbxx.size(); ii++) {
			Sbjjbxx sbjjbxx = listSbjjbxx.get(ii) ;
			JSONObject jsonObject = new JSONObject();
			jsonObject.element("bh", sbjjbxx.getId());
			jsonObject.element("bjmc", sbjjbxx.getBjmc());
			jsonArray.add(jsonObject);
		}
		JSONObject resultJSONObject = new JSONObject();
		resultJSONObject.element("data", jsonArray);
		writeJSON(response, resultJSONObject);
	}
	@RequestMapping(value = "/getQjxx", method = { RequestMethod.POST, RequestMethod.GET })
	public void getQjxx(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Aqjxx> listAqjxx=new ArrayList<Aqjxx>();
		String bh = request.getParameter("bh");
		String stuName=request.getParameter("xm");
		if (StringUtils.isNotBlank(bh)&&StringUtils.isBlank(stuName)) {
			int classId= Integer.parseInt(bh);
			listAqjxx=attdApproveService.getInfoByClass(classId);
		}else if(StringUtils.isNotBlank(bh)&&StringUtils.isNotBlank(stuName)){
			int classId= Integer.parseInt(bh);
			listAqjxx=attdApproveService.getInfoByCondition(classId, stuName);
			//System.out.println(stuName);
		}else{
		
			listAqjxx =attdApproveService.getTotalFromCgx();
		}
		
		JSONArray jsonArray = new JSONArray();
		for (int ii=0; ii< listAqjxx.size(); ii++) {
			Aqjxx aqjxx = listAqjxx.get(ii) ;
			JSONObject jsonObject = new JSONObject();
			jsonObject.element("id", aqjxx.getId());
			jsonObject.element("xh",aqjxx.getXsjbxxId());
			//根据学号从数据库中调出学生姓名
			jsonObject.element("xm",attdApproveService.getStuName(aqjxx.getXsjbxxId()).get(0).getXm());
			//截取日期的前十个字符，去掉后面的 m:i:s，此处有嫌取巧
			jsonObject.element("kssj",aqjxx.getKssj().toString().substring(0, 10));
			jsonObject.element("jssj",aqjxx.getJssj().toString().substring(0, 10));
			jsonObject.element("qjly",aqjxx.getQjly());
			jsonArray.add(jsonObject);
		}
		JSONObject resultJSONObject = new JSONObject();
		resultJSONObject.element("data", jsonArray);
		writeJSON(response, resultJSONObject);
	}
	@RequestMapping(value = "/Withdraw", method = { RequestMethod.POST, RequestMethod.GET })
	public void Withdraw(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int id=Integer.parseInt(request.getParameter("id"));
		
		boolean flag=attdApproveService.Withdraw(id);
		//System.out.println(id);
		if (flag) {
			writeJSON(response, "{success:true}");
		} else {
			writeJSON(response, "{success:false}");
		}
	}
	@RequestMapping(value = "/Negative", method = { RequestMethod.POST, RequestMethod.GET })
	public void Negative(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int id=Integer.parseInt(request.getParameter("id"));
		String bpyy=request.getParameter("bpyy");
		System.out.println(bpyy);
		boolean flag=attdApproveService.Negative(id,bpyy);
		//System.out.println(id);
		if (flag) {
			writeJSON(response, "{success:true}");
		} else {
			writeJSON(response, "{success:false}");
		}
	}
	@RequestMapping(value = "/Pass", method = { RequestMethod.POST, RequestMethod.GET })
	public void Pass(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int id=Integer.parseInt(request.getParameter("id"));
		boolean flag=attdApproveService.Pass(id);
		//System.out.println(id);
		if (flag) {
			writeJSON(response, "{success:true}");
		} else {
			writeJSON(response, "{success:false}");
		}
	}
}
