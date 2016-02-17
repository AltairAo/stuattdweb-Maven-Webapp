package com.stuattd.service.sys;

import java.util.List;

import com.stuattd.model.sys.MonitorLog;

import core.service.Service;

/**
 * @开发商:jit.gwm
 */
public interface MonitorLogService extends Service<MonitorLog> {

	List<MonitorLog> querySensorMonitorLog();

}
