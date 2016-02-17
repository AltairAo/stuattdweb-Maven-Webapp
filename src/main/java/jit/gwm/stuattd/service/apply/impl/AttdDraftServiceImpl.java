package jit.gwm.stuattd.service.apply.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.stuattd.model.sys.Attachment;
import com.stuattd.model.sys.Stuattd;

import core.service.BaseService;
import jit.gwm.stuattd.dao.apply.AqjxxDao;
import jit.gwm.stuattd.dao.apply.AttdDraftDao;
import jit.gwm.stuattd.model.Aqjxx;
import jit.gwm.stuattd.service.apply.AttdDraftService;

import com.stuattd.dao.sys.AttachmentDao;

@Service
public class AttdDraftServiceImpl extends BaseService<Aqjxx> implements
		AttdDraftService {

	private AqjxxDao aqjxxDao;
	private AttdDraftDao attdDraftDao;

	@Resource
	public void setAqjxxDao(AqjxxDao aqjxxDao) {
		this.aqjxxDao = aqjxxDao;
		this.dao = aqjxxDao;
	}

	@Resource
	public void setAttdDraftDao(AttdDraftDao AttdDraftDao) {
		this.attdDraftDao = AttdDraftDao;
		this.dao = AttdDraftDao;
	}

	@Override
	public List<Aqjxx> getTotalFromCgx(Long xsjbxxId) {
		return (attdDraftDao).getTotalFromCgx(xsjbxxId);
	}

	@Override
	public List<Aqjxx> getInfoByCondition(Long xsjbxxId, String kssj,
			String jssj) {
		return (attdDraftDao).getInfoByCondition(xsjbxxId, kssj, jssj);
	}

}
