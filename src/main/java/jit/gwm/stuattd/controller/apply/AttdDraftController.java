package jit.gwm.stuattd.controller.apply;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import com.stuattd.model.sys.Stuattd;
import com.stuattd.model.sys.SysUser;
import com.stuattd.service.sys.SysUserService;

import core.extjs.ExtJSBaseParameter;
import core.extjs.ListView;
import core.support.BaseParameter;
import core.support.Item;
import core.support.QueryResult;
import core.util.MD5;
import core.web.SystemCache;
import jit.gwm.stuattd.model.Aqjxx;
import jit.gwm.stuattd.model.Tjzgjbxx;
import jit.gwm.stuattd.service.apply.AttdDraftService;
import jit.gwm.stuattd.dao.*;

@Controller
@RequestMapping("/attd/attdDraft")
public class AttdDraftController extends StuattdBaseController<Aqjxx> implements Constant {
	
	
	
	@Resource
	private AttdDraftService AttdDraftService;

	@RequestMapping(value = "/getQjxx", method = { RequestMethod.POST, RequestMethod.GET })
	public void getQjxx(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long xsjbxxId = Long.parseLong(request.getParameter("xsjbxxId"));
		String param = request.getParameter("param");
		String kssj=request.getParameter("kssj");
		String jssj=request.getParameter("jssj");
		List<Aqjxx> listAqjxx=new ArrayList<Aqjxx>();
		if (StringUtils.isNotBlank(param)&&StringUtils.isNotBlank(kssj)&&StringUtils.isNotBlank(jssj)) {
			long lparam= Long.parseLong(request.getParameter("param"));
			listAqjxx=AttdDraftService.getInfoByCondition(lparam,kssj,jssj);
		}else if(StringUtils.isNotBlank(param)&&StringUtils.isBlank(kssj)&&StringUtils.isBlank(jssj)){
			long lparam= Long.parseLong(request.getParameter("param"));
			listAqjxx=AttdDraftService.getTotalFromCgx(lparam) ;
		}else{
		
			listAqjxx = AttdDraftService.getTotalFromCgx(xsjbxxId);
		}
	
		JSONArray jsonArray = new JSONArray();
		for (int ii=0; ii< listAqjxx.size(); ii++) {
			Aqjxx aqjxx = listAqjxx.get(ii) ;
			JSONObject jsonObject = new JSONObject();
			jsonObject.element("id", aqjxx.getId());
			jsonObject.element("xsjbxxId",aqjxx.getXsjbxxId());
			jsonObject.element("kssj",aqjxx.getKssj().toString().substring(0, 10));
			jsonObject.element("jssj",aqjxx.getJssj().toString().substring(0, 10));
			jsonObject.element("qjly",aqjxx.getQjly());
			//System.out.println(aqjxx.getKssj());
			jsonArray.add(jsonObject);
		}
		JSONObject resultJSONObject = new JSONObject();
		resultJSONObject.element("data", jsonArray);
		writeJSON(response, resultJSONObject);
	}
	@RequestMapping("/deleteFromCgx")
	public void deleteFromCgx(HttpServletRequest request, HttpServletResponse response, @RequestParam("ids") Long[] ids) throws IOException {
		boolean flag = AttdDraftService.deleteByPK(ids);
		
		if (flag) {
			writeJSON(response, "{success:true}");
		} else {
			writeJSON(response, "{success:false}");
		}
	}
	
	
}
