package com.stuattd.controller.sys;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.stuattd.core.StuattdBaseController;
import com.stuattd.model.sys.SensorLastData;
import com.stuattd.service.sys.SensorLastDataService;

/**
 * @开发商:jit.gwm
 */
@Controller
@RequestMapping("/sys/sensorlastdata")
public class SensorLastDataController extends StuattdBaseController<SensorLastData> {

	@Resource
	private SensorLastDataService sensorLastDataService;

}
