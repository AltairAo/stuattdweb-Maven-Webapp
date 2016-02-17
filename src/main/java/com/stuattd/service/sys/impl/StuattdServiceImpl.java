package com.stuattd.service.sys.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.stuattd.dao.sys.AttachmentDao;
import com.stuattd.dao.sys.StuattdDao;
import com.stuattd.dao.sys.StuattdTypeDao;
import com.stuattd.model.sys.Attachment;
import com.stuattd.model.sys.Stuattd;
import com.stuattd.service.sys.StuattdService;

import core.service.BaseService;

/**
 * @开发商:jit.gwm
 */
@Service
public class StuattdServiceImpl extends BaseService<Stuattd> implements StuattdService {

	private StuattdDao stuattdDao;
	@Resource
	private AttachmentDao attachmentDao;

	@Resource
	private StuattdTypeDao stuattdTypeDao;

	@Resource
	public void setStuattdDao(StuattdDao stuattdDao) {
		this.stuattdDao = stuattdDao;
		this.dao = stuattdDao;
	}

	@Override
	public List<Stuattd> getStuattdList(List<Stuattd> resultList) {
		List<Stuattd> stuattdList = new ArrayList<Stuattd>();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String imagePath = request.getContextPath();
		for (Stuattd entity : resultList) {
			Stuattd stuattd = new Stuattd();
			stuattd.setId(entity.getId());
			stuattd.setEpcId(entity.getEpcId());
			stuattd.setName(entity.getName());
			stuattd.setPlantTime(entity.getPlantTime());
			stuattd.setEntryTime(entity.getEntryTime());
			stuattd.setStuattdTypeName(stuattdTypeDao.get(entity.getStuattdType().getId()).getName());
			stuattd.setStuattdTypeId(entity.getStuattdType().getId());

			List<Attachment> attachmentList = attachmentDao.queryByProerties("stuattdtypeId", entity.getTypeId());
			StringBuilder sb = new StringBuilder("");
			for (int i = 0; i < attachmentList.size(); i++) {
				sb.append("<img src='" + imagePath + "/static/img/upload/" + attachmentList.get(i).getFilePath() + "' width = 300 height = 200 />");
			}

			stuattd.setStuattdTypeImagePath(sb.toString());
			stuattdList.add(stuattd);
		}
		return stuattdList;
	}

	@Override
	public List<Object[]> queryExportedStuattd(Long[] ids) {
		return stuattdDao.queryExportedStuattd(ids);
	}

}
