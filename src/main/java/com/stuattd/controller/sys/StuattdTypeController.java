package com.stuattd.controller.sys;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContext;

import com.stuattd.core.StuattdBaseController;
import com.stuattd.model.sys.Attachment;
import com.stuattd.model.sys.StuattdType;
import com.stuattd.service.sys.AttachmentService;
import com.stuattd.service.sys.StuattdTypeService;

import core.extjs.ExtJSBaseParameter;
import core.extjs.ListView;
import core.support.QueryResult;
import core.util.StuattdUtils;

/**
 * @开发商:jit.gwm
 */
@Controller
@RequestMapping("/sys/stuattdtype")
public class StuattdTypeController extends StuattdBaseController<StuattdType> {

	@Resource
	private StuattdTypeService stuattdTypeService;
	@Resource
	private AttachmentService attachmentService;

	@RequestMapping("/getStuattdType")
	public void getStuattdType(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer firstResult = Integer.valueOf(request.getParameter("start"));
		Integer maxResults = Integer.valueOf(request.getParameter("limit"));
		String sortedObject = null;
		String sortedValue = null;
		List<LinkedHashMap<String, Object>> sortedList = mapper.readValue(request.getParameter("sort"), List.class);
		for (int i = 0; i < sortedList.size(); i++) {
			Map<String, Object> map = sortedList.get(i);
			sortedObject = (String) map.get("property");
			sortedValue = (String) map.get("direction");
		}
		StuattdType stuattdType = new StuattdType();
		stuattdType.setFirstResult(firstResult);
		stuattdType.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		stuattdType.setSortedConditions(sortedCondition);
		QueryResult<StuattdType> queryResult = stuattdTypeService.doPaginationQuery(stuattdType);
		List<StuattdType> stuattdTypeList = stuattdTypeService.getStuattdTypeList(queryResult.getResultList());
		ListView<StuattdType> stuattdTypeListView = new ListView<StuattdType>();
		stuattdTypeListView.setData(stuattdTypeList);
		stuattdTypeListView.setTotalRecord(queryResult.getTotalCount());
		writeJSON(response, stuattdTypeListView);
	}

	@RequestMapping("/deleteStuattdType")
	public void deleteStuattdType(HttpServletRequest request, HttpServletResponse response, @RequestParam("ids") Long[] ids) throws IOException {
		boolean flag = stuattdTypeService.deleteByPK(ids);
		if (flag) {
			writeJSON(response, "{success:true}");
		} else {
			writeJSON(response, "{success:false}");
		}
	}

	@Override
	@RequestMapping(value = "/saveStuattdtype", method = { RequestMethod.POST, RequestMethod.GET })
	public void doSave(StuattdType entity, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ExtJSBaseParameter parameter = ((ExtJSBaseParameter) entity);
		if (CMD_EDIT.equals(parameter.getCmd())) {
			stuattdTypeService.update(entity);
		} else if (CMD_NEW.equals(parameter.getCmd())) {
			stuattdTypeService.persist(entity);
		}
		attachmentService.deleteAttachmentByStuattdTypeId(entity.getId());
		String[] content = entity.getDescription().split(" ");
		for (int i = 0; i < content.length; i++) {
			if (content[i].indexOf("/static/img/upload/") != -1) {
				Attachment attachment = new Attachment();
				attachment.setFileName(entity.getName());
				attachment.setFilePath(content[i].substring(content[i].indexOf("2"), content[i].lastIndexOf("\"")));
				attachment.setStuattdType(entity);
				attachmentService.persist(attachment);
			}
		}
		parameter.setCmd(CMD_EDIT);
		parameter.setSuccess(true);
		writeJSON(response, parameter);
	}

	@RequestMapping("/getStuattdtypeById")
	public void getStuattdtypeById(HttpServletRequest request, HttpServletResponse response, @RequestParam("id") Long id) throws Exception {
		StuattdType stuattdType = stuattdTypeService.get(id);
		writeJSON(response, stuattdType);
	}

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	@RequestMapping(value = "/uploadAttachement", method = RequestMethod.POST)
	public void uploadAttachement(@RequestParam(value = "uploadAttachment", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
		RequestContext requestContext = new RequestContext(request);
		JSONObject json = new JSONObject();
		if (!file.isEmpty()) {
			if (file.getSize() > 2097152) {
				json.put("msg", requestContext.getMessage("g_fileTooLarge"));
			} else {
				try {
					String originalFilename = file.getOriginalFilename();
					String fileName = sdf.format(new Date()) + StuattdUtils.getRandomString(3) + originalFilename.substring(originalFilename.lastIndexOf("."));
					File filePath = new File(getClass().getClassLoader().getResource("/").getPath().replace("/WEB-INF/classes/", "/static/img/upload/" + DateFormatUtils.format(new Date(), "yyyyMM")));
					if (!filePath.exists()) {
						filePath.mkdirs();
					}
					file.transferTo(new File(filePath.getAbsolutePath() + "\\" + fileName));
					json.put("success", true);
					json.put("data", DateFormatUtils.format(new Date(), "yyyyMM") + "/" + fileName);
					json.put("msg", requestContext.getMessage("g_uploadSuccess"));
				} catch (Exception e) {
					e.printStackTrace();
					json.put("msg", requestContext.getMessage("g_uploadFailure"));
				}
			}
		} else {
			json.put("msg", requestContext.getMessage("g_uploadNotExists"));
		}
		writeJSON(response, json.toString());
	}

}
