package com.stuattd.controller.sys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.stuattd.core.StuattdBaseController;
import com.stuattd.model.sys.Attachment;
import com.stuattd.service.sys.AttachmentService;

/**
 * @开发商:jit.gwm
 */
@Controller
@RequestMapping("/sys/attachment")
public class AttachmentController extends StuattdBaseController<Attachment> {

	@Resource
	private AttachmentService attachmentService;

	@RequestMapping("/getFlower")
	public String getFlower(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String epcId = request.getParameter("epcId");
		List<Object[]> flowerList = attachmentService.queryFlowerList(epcId);
		Attachment attachment = null;
		List<Attachment> attachmentList = new ArrayList<Attachment>();
		for (int i = 0; i < flowerList.size(); i++) {
			attachment = new Attachment();
			attachment.setFileName(String.valueOf(flowerList.get(i)[0]));
			attachment.setFilePath(String.valueOf(flowerList.get(i)[1]));
			attachment.setDescription(String.valueOf(flowerList.get(i)[2]));
			attachment.setEpcId(String.valueOf(flowerList.get(i)[3]));
			attachmentList.add(attachment);
		}
		writeJSON(response, attachmentList);
		return null;
	}

}
