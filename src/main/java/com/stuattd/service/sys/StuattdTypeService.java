package com.stuattd.service.sys;

import java.util.List;

import com.stuattd.model.sys.StuattdType;

import core.service.Service;

/**
 * @开发商:jit.gwm
 */
public interface StuattdTypeService extends Service<StuattdType> {

	List<StuattdType> getStuattdTypeList(List<StuattdType> resultList);

}
