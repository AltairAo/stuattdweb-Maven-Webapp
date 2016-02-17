package com.stuattd.service.sys.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.stuattd.dao.sys.AttachmentDao;
import com.stuattd.model.sys.Attachment;
import com.stuattd.service.sys.AttachmentService;

import core.service.BaseService;

/**
 * @开发商:jit.gwm
 */
@Service
public class AttachmentServiceImpl extends BaseService<Attachment> implements AttachmentService {

	private AttachmentDao attachmentDao;

	@Resource
	public void setAttachmentDao(AttachmentDao attachmentDao) {
		this.attachmentDao = attachmentDao;
		this.dao = attachmentDao;
	}

	@Override
	public List<Object[]> queryFlowerList(String epcId) {
		return attachmentDao.queryFlowerList(epcId);
	}

	@Override
	public void deleteAttachmentByStuattdTypeId(Long stuattdTypeId) {
		attachmentDao.deleteAttachmentByStuattdTypeId(stuattdTypeId);
	}

}
