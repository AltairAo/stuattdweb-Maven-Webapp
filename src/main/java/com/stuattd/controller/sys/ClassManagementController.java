package com.stuattd.controller.sys;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContext;

import com.stuattd.core.StuattdBaseController;
import com.stuattd.model.sys.Stuattd;
import com.stuattd.model.sys.StuattdType;
import com.stuattd.service.sys.ClassManagementService;
import com.stuattd.service.sys.StuattdService;
import com.stuattd.service.sys.StuattdTypeService;

import jit.gwm.stuattd.model.Sbjjbxx;
import jit.gwm.stuattd.service.apply.ClassInfoService;
import core.extjs.ExtJSBaseParameter;
import core.extjs.ListView;
import core.support.QueryResult;
import core.util.StuattdUtils;

/**
 * @开发商:jit.gwm
 */
@Controller
@RequestMapping("/sys/ClassManagement")
public class ClassManagementController extends StuattdBaseController<Sbjjbxx> {

	private static final Logger log = Logger.getLogger(StuattdController.class);
	private static SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	@Resource
	private ClassManagementService classManagementService;
	@Resource
	private ClassInfoService classInfoService;
	@Resource
	private StuattdService stuattdService;
	@Resource
	private StuattdTypeService stuattdTypeService;

	@RequestMapping(value = "/getBjmc", method = { RequestMethod.POST,
			RequestMethod.GET })
	public void getBjmc(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<Sbjjbxx> listSbjjbxx = classInfoService.getClassInfo();
		JSONArray jsonArray = new JSONArray();
		for (int ii = 0; ii < listSbjjbxx.size(); ii++) {
			Sbjjbxx sbjjbxx = listSbjjbxx.get(ii);
			JSONObject jsonObject = new JSONObject();
			jsonObject.element("id", sbjjbxx.getId());
			jsonObject.element("bh", sbjjbxx.getBh());
			jsonObject.element("bjmc", sbjjbxx.getBjmc());
			jsonObject.element("jbny", sbjjbxx.getJbny().toString());
			jsonArray.add(jsonObject);
		}
		JSONObject resultJSONObject = new JSONObject();
		resultJSONObject.element("data", jsonArray);
		writeJSON(response, resultJSONObject);
	}

	@RequestMapping(value = "/saveClass", method = { RequestMethod.POST,
			RequestMethod.GET })
	public void doSave(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		System.out.println("start");
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String time = format.format(date);
		Date localtime = null;
		try {
			localtime = format.parse(time.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Sbjjbxx entity = new Sbjjbxx();
		String bh = request.getParameter("bh");
		String bjmc = request.getParameter("bjmc");
		entity.setZymlId((long) 12);
		entity.setDwjbxxId((long) 2);
		entity.setJzgjbxxIdBzr((long) 1);
		entity.setXsjbxxIdBz((long) 0);
		entity.setXsjbxxIdXw((long) 0);
		entity.setJzgjbxxIdFdy((long) 2);
		entity.setJzgjbxxIdBd((long) 2);
		entity.setBh(bh);
		entity.setBjmc(bjmc);
		entity.setJbny(localtime);
		entity.setSfddb(0);
		boolean flag = classManagementService.saveClass(entity);
		System.out.println("ok");
		if (flag) {
			writeJSON(response, "{success:true}");
		} else {
			writeJSON(response, "{success:false}");
		}
	}

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	@RequestMapping(value = "/importClassFile", method = RequestMethod.POST)
	public void importStuattdFile(
			@RequestParam(value = "importedFile", required = false) MultipartFile file,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RequestContext requestContext = new RequestContext(request);
		JSONObject json = new JSONObject();
		if (!file.isEmpty()) {
			if (file.getSize() > 2097152) {
				json.put("msg", requestContext.getMessage("g_fileTooLarge"));
			} else {
				try {
					String originalFilename = file.getOriginalFilename();
					String fileName = sdf.format(new Date())
							+ StuattdUtils.getRandomString(3)
							+ originalFilename.substring(originalFilename
									.lastIndexOf("."));
					File filePath = new File(getClass()
							.getClassLoader()
							.getResource("/")
							.getPath()
							.replace(
									"/WEB-INF/classes/",
									"/static/download/attachment/"
											+ DateFormatUtils.format(
													new Date(), "yyyyMM")));
					if (!filePath.exists()) {
						filePath.mkdirs();
					}
					String serverFile = filePath.getAbsolutePath() + "\\"
							+ fileName;
					file.transferTo(new File(serverFile));

					String fileType = fileName.substring(fileName
							.lastIndexOf(".") + 1);
					if (!fileType.equalsIgnoreCase("xls")
							&& !fileType.equalsIgnoreCase("xlsx")) {
						json.put("success", false);
						json.put("msg",
								requestContext.getMessage("g_notValidExcel"));
						writeJSON(response, json.toString());
						return;
					}
					int count = 0;
					StringBuilder stringBuilder = new StringBuilder();
					InputStream xls = new FileInputStream(serverFile);
					Workbook wb = null;
					Sheet sheet = null;
					Row currentRow = null;
					Row headRow = null;
					Cell currentCell = null;
					if (fileType.equals("xls")) {
						wb = new HSSFWorkbook(xls);
					} else if (fileType.equals("xlsx")) {
						wb = new XSSFWorkbook(xls);
					}
					sheet = wb.getSheetAt(0);// excel中至少会存在一个sheet页
					int rowNum = sheet.getPhysicalNumberOfRows();// 物理有效行数
					Object[] rowValues = null;// excel中一行树木信息
					List<Object[]> models = new ArrayList<Object[]>();// excel中全部树木信息
					if (rowNum > 1) {
						headRow = sheet.getRow(0);
						columns: for (int i = 1; i < rowNum; i++) {
							currentRow = sheet.getRow(i);
							if (currentRow != null) {
								rowValues = new Object[12];
								// int cellNum = currentRow.getLastCellNum();//
								// 总单元格数目
								for (short j = 0; j < 12; j++) {
									try {
										currentCell = currentRow.getCell(j);
										Object obj = null;
										if (currentCell == null) {
											obj = "";
										} else {
											switch (currentCell.getCellType()) {
											case Cell.CELL_TYPE_BLANK:
												obj = "";
												break;
											case Cell.CELL_TYPE_STRING:
												obj = currentCell
														.getRichStringCellValue();
												break;
											case Cell.CELL_TYPE_NUMERIC:
												if (HSSFDateUtil
														.isCellDateFormatted(currentCell)) {
													double d = currentCell
															.getNumericCellValue();
													Date date = HSSFDateUtil
															.getJavaDate(d);
													obj = sdfDate.format(date);
												} else {
													NumberFormat nf = NumberFormat
															.getInstance();
													nf.setGroupingUsed(false);// true时的格式：1,234,567,890
													obj = nf.format(currentCell
															.getNumericCellValue());
												}
												break;
											default:
												obj = "";
												break;
											}
										}
										String cellVal = obj.toString();
										rowValues[j] = cellVal;
									} catch (IllegalStateException e) {
										rowValues = null;
										stringBuilder
												.append("第"
														+ i
														+ "行,"
														+ headRow
																.getCell(j)
																.getRichStringCellValue()
														+ "列输入了非法值，未导入成功！");
										continue columns;
									} catch (NullPointerException e) {
										rowValues = null;
										stringBuilder
												.append("第"
														+ i
														+ "行,"
														+ headRow
																.getCell(j)
																.getRichStringCellValue()
														+ "列输入了空值，未导入成功!");
										continue columns;
									} catch (Exception e) {
										rowValues = null;
										stringBuilder.append(e.getMessage());
										continue columns;
									}
								}
								if (rowValues != null) {
									models.add(rowValues);
								}
							}
						}
					} else if (rowNum <= 1 && rowNum > 0) {// 表示模版中只存在头部信息
						json.put("success", false);
						json.put("msg", "Excel表格中没有需要导入 的内容！");
						writeJSON(response, json.toString());
						return;
					} else if (rowNum <= 0) {// 表示这是一个空sheet页
						json.put("success", false);
						json.put("msg", "所导入文件格式不正确，请下载模板！");
						writeJSON(response, json.toString());
						return;
					}
					List<Sbjjbxx> list = objectToSbjjbxx(models);// Object-->Sbjjbxx
					for (int i = 0; i < list.size(); i++) {
						classManagementService.saveClass((list.get(i)));
						count++;
					}

					json.put("success", true);
					json.put("msg",
							count + "条记录导入完成。" + stringBuilder.toString());
				} catch (Exception e) {
					e.printStackTrace();
					json.put("success", false);
					json.put("msg",
							requestContext.getMessage("g_operateFailure"));
					writeJSON(response, json.toString());
				}
			}
		} else {
			json.put("success", false);
			json.put("msg", requestContext.getMessage("g_uploadNotExists"));
		}
		writeJSON(response, json.toString());
	}

	private List<Sbjjbxx> objectToSbjjbxx(List<Object[]> models) {
		List<Sbjjbxx> sbjjbxxList = new ArrayList<Sbjjbxx>();
		// Stuattd stuattd = null;
		Sbjjbxx sbjjbxx = null;
		for (int i = 0; i < models.size(); i++) {
			try {
				sbjjbxx = new Sbjjbxx();
				sbjjbxx.setZymlId(Long.parseLong(models.get(i)[0].toString()));
				sbjjbxx.setDwjbxxId(Long.parseLong(models.get(i)[1].toString()));
				sbjjbxx.setJzgjbxxIdBzr(Long.parseLong(models.get(i)[2]
						.toString()));
				sbjjbxx.setXsjbxxIdBz(Long.parseLong(models.get(i)[3]
						.toString()));
				sbjjbxx.setXsjbxxIdXw(Long.parseLong(models.get(i)[4]
						.toString()));
				sbjjbxx.setJzgjbxxIdFdy(Long.parseLong(models.get(i)[5]
						.toString()));
				sbjjbxx.setJzgjbxxIdBd(Long.parseLong(models.get(i)[6]
						.toString()));
				sbjjbxx.setBh(models.get(i)[7].toString());
				sbjjbxx.setBjmc(models.get(i)[8].toString());
				sbjjbxx.setJbny(sdfDate.parse(models.get(i)[9].toString()));
				sbjjbxx.setSsnj(Integer.parseInt(models.get(i)[10].toString()));
				sbjjbxx.setSfddb(Integer.parseInt(models.get(i)[11].toString()));
				sbjjbxxList.add(sbjjbxx);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return sbjjbxxList;
	}

	@RequestMapping("/downloadImportedFile")
	public ResponseEntity<byte[]> downloadImportedFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		File filePath=null;
		if(request.getParameter("name").equals("class")){
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "sbjjbxx.xlsx");
		filePath = new File(getClass()
				.getClassLoader()
				.getResource("/")
				.getPath()
				.replace("/WEB-INF/classes/",
						"/static/download/attachment/" + "sbjjbxx.xlsx"));
		
		}else if(request.getParameter("name").equals("student")){
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", "sxsjbxx.xlsx");
			filePath = new File(getClass()
					.getClassLoader()
					.getResource("/")
					.getPath()
					.replace("/WEB-INF/classes/",
							"/static/download/attachment/" + "sxsjbxx.xlsx"));
		}else if(request.getParameter("name").equals("teacher")){
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", "tjzgjbxx.xlsx");
			filePath = new File(getClass()
					.getClassLoader()
					.getResource("/")
					.getPath()
					.replace("/WEB-INF/classes/",
							"/static/download/attachment/" + "tjzgjbxx.xlsx"));
		}
		
		return new ResponseEntity<byte[]>(
				FileUtils.readFileToByteArray(filePath), headers,
				HttpStatus.CREATED);
	}
}
