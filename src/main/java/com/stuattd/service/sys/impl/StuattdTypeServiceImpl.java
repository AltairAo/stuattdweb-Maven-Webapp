package com.stuattd.service.sys.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.stuattd.dao.sys.StuattdTypeDao;
import com.stuattd.model.sys.StuattdType;
import com.stuattd.service.sys.StuattdTypeService;

import core.service.BaseService;
import core.util.HtmlUtils;

/**
 * @开发商:jit.gwm
 */
@Service
public class StuattdTypeServiceImpl extends BaseService<StuattdType> implements StuattdTypeService {

	private StuattdTypeDao stuattdTypeDao;

	@Resource
	public void setStuattdTypeDao(StuattdTypeDao stuattdTypeDao) {
		this.stuattdTypeDao = stuattdTypeDao;
		this.dao = stuattdTypeDao;
	}

	@Override
	public List<StuattdType> getStuattdTypeList(List<StuattdType> resultList) {
		List<StuattdType> stuattdTypeList = new ArrayList<StuattdType>();
		for (StuattdType entity : resultList) {
			StuattdType stuattdType = new StuattdType();
			stuattdType.setId(entity.getId());
			stuattdType.setName(entity.getName());
			stuattdType.setDescription(entity.getDescription());
			stuattdType.setDescriptionNoHtml(HtmlUtils.htmltoText(entity.getDescription()));
			stuattdTypeList.add(stuattdType);
		}
		return stuattdTypeList;
	}

}
