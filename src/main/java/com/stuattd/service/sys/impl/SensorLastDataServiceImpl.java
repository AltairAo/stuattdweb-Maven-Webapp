package com.stuattd.service.sys.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.stuattd.dao.sys.SensorLastDataDao;
import com.stuattd.model.sys.SensorLastData;
import com.stuattd.service.sys.SensorLastDataService;

import core.service.BaseService;

/**
 * @开发商:jit.gwm
 */
@Service
public class SensorLastDataServiceImpl extends BaseService<SensorLastData> implements SensorLastDataService {

	private SensorLastDataDao sensorLastDataDao;

	@Resource
	public void setSensorLastDataDao(SensorLastDataDao sensorLastDataDao) {
		this.sensorLastDataDao = sensorLastDataDao;
		this.dao = sensorLastDataDao;
	}

}
