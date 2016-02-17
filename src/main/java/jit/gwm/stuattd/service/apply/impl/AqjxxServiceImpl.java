package jit.gwm.stuattd.service.apply.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import core.service.BaseService;
import jit.gwm.stuattd.dao.apply.AqjxxDao;
import jit.gwm.stuattd.model.Aqjxx;
import jit.gwm.stuattd.service.apply.AqjxxService;

@Service
public class AqjxxServiceImpl extends BaseService<Aqjxx> implements
		AqjxxService {

	private AqjxxDao aqjxxDao;

	@Resource
	public void setAqjxxDao(AqjxxDao aqjxxDao) {
		this.aqjxxDao = aqjxxDao;
		this.dao = aqjxxDao;
		
	}
	@Override
	public boolean doSave(Aqjxx aqjxx){
		return (aqjxxDao).doSave(aqjxx);
	}
	@Override
	public boolean doUpdate(Aqjxx aqjxx){
		return (aqjxxDao).doUpdate(aqjxx);
	}
}
