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

import jit.gwm.stuattd.model.Aqjxx;
import jit.gwm.stuattd.service.apply.ApproveSearchService;
@Controller
@RequestMapping("/attd/ApproveSearch")
public class ApproveSearchController extends StuattdBaseController<Aqjxx> implements Constant {

	
	@Resource
	private ApproveSearchService approveSearchService;
	
	@RequestMapping(value = "/getQjxx", method = { RequestMethod.POST, RequestMethod.GET })
	public void getQjxx(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String yes="是";
		String no="否";
		List<Aqjxx> listAqjxx=new ArrayList<Aqjxx>();
		String bh = request.getParameter("bh");
		String stuName=request.getParameter("xm");
		if (StringUtils.isNotBlank(bh)&&StringUtils.isBlank(stuName)) {
			int classId= Integer.parseInt(bh);
			listAqjxx=approveSearchService.getInfoByClass(classId);
		}else if(StringUtils.isNotBlank(bh)&&StringUtils.isNotBlank(stuName)){
			int classId= Integer.parseInt(bh);
			listAqjxx=approveSearchService.getInfoByCondition(classId, stuName);
			//System.out.println(stuName);
		}else{
		
			listAqjxx =approveSearchService.getTotalFromCgx();
		}
		
		JSONArray jsonArray = new JSONArray();
		for (int ii=0; ii< listAqjxx.size(); ii++) {
			Aqjxx aqjxx = listAqjxx.get(ii) ;
			JSONObject jsonObject = new JSONObject();
			jsonObject.element("id", aqjxx.getId());
			jsonObject.element("xh",aqjxx.getXsjbxxId());
			//根据学号从数据库中调出学生姓名
			jsonObject.element("xm",approveSearchService.getStuName(aqjxx.getXsjbxxId()).get(0).getXm());
			//截取日期的前十个字符，去掉后面的 m:i:s，此处有嫌取巧
			jsonObject.element("kssj",aqjxx.getKssj().toString().substring(0, 10));
			jsonObject.element("jssj",aqjxx.getJssj().toString().substring(0, 10));
			jsonObject.element("qjly",aqjxx.getQjly());
			jsonObject.element("sfpj", aqjxx.getSfpj()==1 ? yes:no);
			jsonArray.add(jsonObject);
		}
		JSONObject resultJSONObject = new JSONObject();
		resultJSONObject.element("data", jsonArray);
		writeJSON(response, resultJSONObject);
	}
	
}
