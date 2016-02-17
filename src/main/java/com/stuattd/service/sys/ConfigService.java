package com.stuattd.service.sys;

import java.util.List;

import com.stuattd.model.sys.Config;

import core.service.Service;

/**
 * @开发商:jit.gwm
 */
public interface ConfigService extends Service<Config> {

	List<Config> getConfigList(List<Config> resultList);

}
