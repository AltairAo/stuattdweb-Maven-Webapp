package com.stuattd.service.sys;

import java.util.List;

import com.stuattd.model.sys.Stuattd;

import core.service.Service;

/**
 * @开发商:jit.gwm
 */
public interface StuattdService extends Service<Stuattd> {

	List<Stuattd> getStuattdList(List<Stuattd> resultList);

	List<Object[]> queryExportedStuattd(Long[] ids);

}
