package com.stuattd.service.sys;

import java.util.List;

import com.stuattd.model.sys.Attachment;

import core.service.Service;

/**
 * @开发商:jit.gwm
 */
public interface AttachmentService extends Service<Attachment> {

	List<Object[]> queryFlowerList(String epcId);

	void deleteAttachmentByStuattdTypeId(Long stuattdTypeId);

}
