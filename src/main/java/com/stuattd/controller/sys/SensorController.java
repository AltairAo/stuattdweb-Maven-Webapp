package com.stuattd.controller.sys;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.stuattd.core.StuattdBaseController;
import com.stuattd.model.sys.Stuattd;
import com.stuattd.model.sys.Sensor;
import com.stuattd.service.sys.StuattdService;
import com.stuattd.service.sys.SensorService;

import core.extjs.ExtJSBaseParameter;
import core.extjs.ListView;
import core.support.QueryResult;

/**
 * @开发商:jit.gwm
 */
@Controller
@RequestMapping("/sys/sensor")
public class SensorController extends StuattdBaseController<Sensor> {

	@Resource
	private SensorService sensorService;
	@Resource
	private StuattdService stuattdService;

	@RequestMapping("/deleteSensor")
	public void deleteSensor(HttpServletRequest request, HttpServletResponse response, @RequestParam("ids") Long[] ids) throws IOException {
		boolean flag = sensorService.deleteByPK(ids);
		if (flag) {
			writeJSON(response, "{success:true}");
		} else {
			writeJSON(response, "{success:false}");
		}
	}

	@RequestMapping(value = "/getSensor")
	public void getSensor(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String identification = request.getParameter("identification");
		Integer firstResult = Integer.valueOf(request.getParameter("start"));
		Integer maxResults = Integer.valueOf(request.getParameter("limit"));
		String stuattdidentification = request.getParameter("stuattdidentification");
		String sortedObject = null;
		String sortedValue = null;
		List<LinkedHashMap<String, Object>> sortedList = mapper.readValue(request.getParameter("sort"), List.class);
		for (int i = 0; i < sortedList.size(); i++) {
			Map<String, Object> map = sortedList.get(i);
			sortedObject = (String) map.get("property");
			sortedValue = (String) map.get("direction");
		}
		Sensor sensor = new Sensor();
		if (identification == null) {
			sensor.set$eq_type((short) 1);
		}
		sensor.setFirstResult(firstResult);
		sensor.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		sensor.setSortedConditions(sortedCondition);
		QueryResult<Sensor> queryResult = sensorService.doPaginationQuery(sensor);
		if (null == stuattdidentification) {
			queryResult = sensorService.doPaginationQuery(sensor);
		} else {
			queryResult = sensorService.querySensorList(sensor);
		}
		ListView<Sensor> sensorListView = new ListView<Sensor>();
		sensorListView.setData(queryResult.getResultList());
		sensorListView.setTotalRecord(queryResult.getTotalCount());
		writeJSON(response, sensorListView);
	}

	@Override
	@RequestMapping(value = "/saveSensor", method = { RequestMethod.POST, RequestMethod.GET })
	public void doSave(Sensor entity, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ExtJSBaseParameter parameter = ((ExtJSBaseParameter) entity);
		Sensor checkSensorId = sensorService.getByProerties("sensorId", entity.getSensorId());
		if (null != checkSensorId && null == entity.getId()) {
			parameter.setSuccess(false);
		} else {
			if (CMD_EDIT.equals(parameter.getCmd())) {
				sensorService.update(entity);
			} else if (CMD_NEW.equals(parameter.getCmd())) {
				entity.setType((short) 1);
				sensorService.persist(entity);
			}
			parameter.setCmd(CMD_EDIT);
			parameter.setSuccess(true);
		}
		writeJSON(response, parameter);
	}

	@RequestMapping(value = "/saveIdentificationSensor", method = { RequestMethod.POST, RequestMethod.GET })
	public void saveIdentificationSensor(Sensor entity, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ExtJSBaseParameter parameter = ((ExtJSBaseParameter) entity);
		Sensor checkSensorId = sensorService.getByProerties("sensorId", entity.getSensorId());
		Sensor checkSensorEpcId = sensorService.getByProerties("epcId", entity.getEpcId());
		Stuattd checkStuattdEpcId = stuattdService.getByProerties("epcId", entity.getEpcId());
		if (null == checkStuattdEpcId) {
			parameter.setMessage("epc编码不存在，请重新输入！");
			parameter.setSuccess(false);
		} else {
			if (CMD_EDIT.equals(parameter.getCmd())) {
				if (checkSensorId != null && !checkSensorEpcId.getSensorId().equals(entity.getSensorId())) {
					parameter.setMessage("传感器ID已重复，请重新输入！");
					parameter.setSuccess(false);
					writeJSON(response, parameter);
					return;
				}
				sensorService.update(entity);
			} else if (CMD_NEW.equals(parameter.getCmd())) {
				if (checkSensorEpcId != null) {
					parameter.setMessage("epc编码已重复，请重新输入！");
					parameter.setSuccess(false);
					writeJSON(response, parameter);
					return;
				}
				if (entity.getType() == 2) {
					entity.setSensorId(new Random().nextInt(100000000));
					sensorService.persist(entity);
				} else {
					if (checkSensorId == null) {
						parameter.setMessage("传感器ID不存在，请重新输入！");
						parameter.setSuccess(false);
						writeJSON(response, parameter);
						return;
					}
					checkSensorId.setEpcId(entity.getEpcId());
					checkSensorId.setType((short) 1);
					sensorService.update(checkSensorId);
				}
			}
			parameter.setCmd(CMD_EDIT);
			parameter.setSuccess(true);
		}
		writeJSON(response, parameter);
	}

	@RequestMapping("/getSensorBySensorType")
	public void getSensorBySensorType(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Short sensorType = Short.valueOf(request.getParameter("sensorType"));
		List<Sensor> sensorList = sensorService.querySensorBySensorType(sensorType);
		writeJSON(response, sensorList);
	}

	@RequestMapping("/getSensorLastData")
	public void getSensorLastData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<Sensor> sensorList = sensorService.querySensorLastData();
		writeJSON(response, sensorList);
	}

	@RequestMapping("/getStuattdSensorLastData")
	public void getStuattdSensorLastData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<Sensor> sensorList = sensorService.queryStuattdSensorLastData();
		writeJSON(response, sensorList);
	}

	@RequestMapping("/getSensorLastDataWithEpcId")
	public void getSensorLastDataWithEpcId(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<Sensor> sensorList = sensorService.querySensorLastDataWithEpcId();
		writeJSON(response, sensorList);
	}

}
