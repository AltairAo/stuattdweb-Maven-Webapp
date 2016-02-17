package com.stuattd.controller.sys;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.stuattd.core.StuattdBaseController;
import com.stuattd.model.sys.MonitorLog;
import com.stuattd.service.sys.MonitorLogService;

/**
 * @开发商:jit.gwm
 */
@Controller
@RequestMapping("/sys/monitorlog")
public class MonitorLogController extends StuattdBaseController<MonitorLog> {

	@Resource
	private MonitorLogService monitorLogService;

	@RequestMapping("/getSensorMonitorLog")
	public void getSensorMonitorLog(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<MonitorLog> sensorMonitorLogList = monitorLogService.querySensorMonitorLog();
		writeJSON(response, sensorMonitorLogList);
	}

}
