package com.stuattd.service.sys;

import java.util.List;

import com.stuattd.model.sys.SensorData;

import core.service.Service;

/**
 * @开发商:jit.gwm
 */
public interface SensorDataService extends Service<SensorData> {

	List<Object[]> doGetSensorDataStatistics(Short sensorType);

	List<Object[]> doGetEnhanceSensorDataStatistics(List<Object[]> list);

}
