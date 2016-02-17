package com.stuattd.controller.sys;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.stuattd.core.StuattdBaseController;
import com.stuattd.model.sys.SensorData;
import com.stuattd.service.sys.SensorDataService;

/**
 * @开发商:jit.gwm
 */
@Controller
@RequestMapping("/sys/sensordata")
public class SensorDataController extends StuattdBaseController<SensorData> {

	@Resource
	private SensorDataService sensorDataService;

	@RequestMapping(value = "/getSensorDataStatistics")
	public void getSensorDataStatistics(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Object[]> list = sensorDataService.doGetSensorDataStatistics(Short.valueOf(request.getParameter("sensorType")));
		List<Object[]> enhanceList = sensorDataService.doGetEnhanceSensorDataStatistics(list);
		writeJSON(response, enhanceList);
	}

}
