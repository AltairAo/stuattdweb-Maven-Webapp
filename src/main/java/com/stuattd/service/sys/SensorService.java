package com.stuattd.service.sys;

import java.util.List;

import com.stuattd.model.sys.Sensor;

import core.service.Service;
import core.support.QueryResult;

/**
 * @开发商:jit.gwm
 */
public interface SensorService extends Service<Sensor> {

	List<Sensor> querySensorBySensorType(Short sensorType);

	List<Sensor> querySensorLastData();

	QueryResult<Sensor> querySensorList(Sensor sensor);

	List<Sensor> querySensorLastDataWithEpcId();

	List<Sensor> queryStuattdSensorLastData();

}
