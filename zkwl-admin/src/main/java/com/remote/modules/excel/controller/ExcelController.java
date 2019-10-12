package com.remote.modules.excel.controller;

import com.remote.common.enums.AllEnum;
import com.remote.common.enums.RunStatusEnum;
import com.remote.common.enums.TransportEnum;
import com.remote.common.es.utils.ESUtil;
import com.remote.common.utils.R;
import com.remote.common.utils.StringUtils;
import com.remote.modules.advancedsetting.entity.AdvancedSettingEntity;
import com.remote.modules.advancedsetting.service.AdvancedSettingService;
import com.remote.modules.cjdevice.entity.CjDevice;
import com.remote.modules.cjdevice.service.CjDeviceService;
import com.remote.modules.device.entity.DeviceEntity;
import com.remote.modules.device.service.DeviceService;
import com.remote.modules.devicetype.entity.DeviceTypeEntity;
import com.remote.modules.devicetype.service.DeviceTypeService;
import com.remote.modules.district.entity.DistrictEntity;
import com.remote.modules.district.service.DistrictService;
import com.remote.modules.group.entity.GroupEntity;
import com.remote.modules.group.service.GroupService;
import com.remote.modules.project.entity.ProjectEntity;
import com.remote.modules.project.service.ProjectService;
import com.remote.modules.sys.controller.AbstractController;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * @Author zhangwenping
 * @Date 2019/10/10 13:15
 * @Version 1.0
 **/
@RestController
@RequestMapping("/fun/excel")
public class ExcelController extends AbstractController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DeviceTypeService deviceTypeService;
    @Autowired
    private CjDeviceService cjDeviceService;
    @Autowired
    private DistrictService districtService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private AdvancedSettingService advancedSettingService;
    @Autowired
    private ESUtil esUtil;


    /**
     * @param filename
     * @return
     * @throws Exception
     * @description 导入表格   produces =  "text/html;charset=U
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public R importData(@RequestParam("filename")  MultipartFile filename,
                        HttpServletRequest request,
                        HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String name = filename.getOriginalFilename();
        String prefix=name.substring(0,name.lastIndexOf("."));
        ProjectEntity projectEntity = projectService.queryProjectByCode(prefix);
        if(projectEntity == null){
            return R.error(101,"模板错误");
        }
        InputStream inputStream = filename.getInputStream(); // 取得输入流
        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(inputStream); // 解析2007版本
        } catch (Exception ex) {
            try {
                POIFSFileSystem pfs = new POIFSFileSystem(filename.getInputStream()); // 解析2003版本
                workbook = new HSSFWorkbook(pfs);
            } catch (IOException e) {
            }
        }

        Sheet hssfSheet = workbook.getSheetAt(0);
        int rowCount = hssfSheet.getPhysicalNumberOfRows(); // 获取总行数
        if(rowCount >= 301){
            return R.error(101,"一次性最多导入300台设备");
        }
        List<DeviceEntity> list = new ArrayList<>();
        List<AdvancedSettingEntity> aseList = new ArrayList<>();
        List<Map<String, Object>> esList = new ArrayList<>();

        String msg = "";
        if (hssfSheet != null) {
            for (int i = 1; i < rowCount; i++) {
                Row excelData = hssfSheet.getRow(i);
                //处理行
                String str = rowHahdle(excelData,list,aseList,projectEntity,esList);
                if(!"".equals(str)){
                    msg+= str;
                }
                if(list.size() == 100){
                    boolean b = deviceService.insertList(list);
                    if(b){
                        advancedSettingService.saveAdvanceSetting(aseList);
                        esUtil.addListES(esList,"device_index");
                    }
                    aseList.clear();
                    list.clear();
                    esList.clear();
                }
            }
            if(list.size() > 0){
                boolean b = deviceService.insertList(list);
                if(b){
                    esUtil.addListES(esList,"device_index");
                    advancedSettingService.saveAdvanceSetting(aseList);
                }
            }

        }
        return R.ok(msg);
    }



    private String rowHahdle(Row row,List<DeviceEntity> list,List<AdvancedSettingEntity> aseList,ProjectEntity projectEntity,List<Map<String, Object>> esList) {
        DeviceEntity deviceEntity = new DeviceEntity();
        GroupEntity groupEntity = null;
        AdvancedSettingEntity advancedSettingEntity = new AdvancedSettingEntity();;

        String msg = "";
        int rowNum = row.getRowNum();
        Cell cell = null;
        //项目名称 处理项目start
        DistrictEntity districtEntity = districtService.queryDistrictById(projectEntity.getCityId());
        deviceEntity.setCityName(districtEntity.getDistrictName());
        deviceEntity.setCreateUser(projectEntity.getExclusiveUser());
        deviceEntity.setProjectId(projectEntity.getProjectId());

        //处理项目end

        //设备分组 处理分组start
        cell = row.getCell(0);
        String groupName = cell.toString();
        if(StringUtils.isEmpty(groupName)){
            msg+= "第"+rowNum+"行设备分组为空;";
        }else{
            if(projectEntity != null){
                groupEntity = groupService.selectGroupByName(projectEntity.getProjectId(), groupName);
            }
            if(groupEntity == null){
                msg+= "第"+rowNum+"行设备分组有误;";
            }else{
                deviceEntity.setGroupId(groupEntity.getGroupId());
                deviceEntity.setGroupName(groupEntity.getGroupName());
                advancedSettingEntity.setGroupId(groupEntity.getGroupId());
            }
        }

        //处理分组end
        //设备编号
        cell = row.getCell(1);
        String deviceCode = cell.toString();
        if(StringUtils.isEmpty(deviceCode)){
            msg+= "第"+rowNum+"行设备编号为空;";
        }else{

            String s = deviceService.queryByDevCode(deviceCode);
            if(StringUtils.isNotEmpty(s)){
                //代表数据库中已经有该条记录
                msg+= "第"+rowNum+"行设备已存在;";
            }else{
//                DeviceTypeEntity deviceTypeEntity = deviceTypeService.getDeviceTypeByCode("1", 1);
//                deviceEntity.setDeviceType(deviceTypeEntity.getDeviceTypeCode());
//                deviceEntity.setDeviceTypeName(deviceTypeEntity.getDeviceTypeName());
//                deviceEntity.setCommunicationType(1);
//                deviceEntity.setDeviceCode(deviceCode);
//                advancedSettingEntity.setDeviceCode(deviceCode);
//                if(deviceTypeEntity.getDeviceTypeCode().equals("1")){
//                    initAdvSet(advancedSettingEntity,1);
//                }else if(deviceTypeEntity.getDeviceTypeCode().equals("2")){
//                    initAdvSet(advancedSettingEntity,2);
//                }

                CjDevice cjDevice = cjDeviceService.queryCjDeviceByDeviceCode(deviceCode);
                if(cjDevice == null){
                    msg+= "第"+rowNum+"行设备编号不存在;";
                }else{
                    DeviceTypeEntity deviceTypeEntity = deviceTypeService.getDeviceTypeByCode(cjDevice.getDeviceTypeCode(), 1);
                    deviceEntity.setDeviceType(deviceTypeEntity.getDeviceTypeCode());
                    deviceEntity.setDeviceTypeName(deviceTypeEntity.getDeviceTypeName());
                    deviceEntity.setCommunicationType(cjDevice.getCommunicationType());
                    deviceEntity.setDeviceCode(deviceCode);
                    advancedSettingEntity.setDeviceCode(deviceCode);
                    if(deviceTypeEntity.getDeviceTypeCode().equals("1")){
                        initAdvSet(advancedSettingEntity,1);
                    }else if(deviceTypeEntity.getDeviceTypeCode().equals("2")){
                        initAdvSet(advancedSettingEntity,2);
                    }
                }

            }


        }
        //设备名称
        cell = row.getCell(2);
        String deviceName = cell.toString();
        if(StringUtils.isEmpty(deviceName)){
            msg+= "第"+rowNum+"行设备名称为空;";
        }else{
            deviceEntity.setDeviceName(deviceName);
        }
        if("".equals(msg)){
            //设备默认值
            deviceEntity.setUsrUser(getUser().getUserId()); // 创建人 和 使用人 存反
            deviceEntity.setIsDel(AllEnum.NO.getCode());
            deviceEntity.setCreateName(getUser().getRealName());
            deviceEntity.setCreateTime(new Date());
            deviceEntity.setDeviceId(UUID.randomUUID().toString());
            deviceEntity.setLight("100");
            deviceEntity.setOnOff(AllEnum.NO.getCode());
            deviceEntity.setRunState(RunStatusEnum.OFFLINE.getCode());
            deviceEntity.setSignalState(0);
            //厂家标识
            deviceEntity.setCjFlag(getUser().getOperation());
            deviceEntity.setTransport(TransportEnum.NO.getCode());
            advancedSettingEntity.setUid(getUser().getUserId());
            list.add(deviceEntity);
            aseList.add(advancedSettingEntity);
            Map<String, Object> map = esUtil.convertAddES(deviceEntity);
            esList.add(map);
        }
        return msg;
    }




    @RequestMapping(value = "/download", method= RequestMethod.GET)
    public void download(String projectId,HttpServletResponse response) throws Exception {

        HSSFWorkbook workbook = new HSSFWorkbook();

        ProjectEntity projectEntity = projectService.queryProjectMap(projectId);

        HSSFSheet sheet = workbook.createSheet(projectEntity.getProjectName());// 创建一个excel表单

        List<GroupEntity> groupList = groupService.queryGroupIdNoPage(projectId, 8);
        String[] textGroup = new String[groupList.size()];
        for(int i = 0 ; i < groupList.size() ;i++){
            textGroup[i] = groupList.get(i).getGroupName();
        }

        createTitle(workbook, sheet);
        //设置分组下拉列表框
        sheet = setHSSFValidation(sheet, textGroup, 1, 500, 0, 0);
        // 设置日期格式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));

        String fileName = projectEntity.getProjectCode()+".xls";
        // 生成excel文件
        //buildExcelFile(fileName, workbook);
        // 浏览器下载excel
        buildExcelDocument(fileName, workbook, response);

    }



    /**
     * 设置某些列的值只能输入预制的数据,显示下拉框.
     *
     * @param sheet
     *            要设置的sheet.
     * @param textlist
     *            下拉框显示的内容
     * @param firstRow
     *            开始行
     * @param endRow
     *            结束行
     * @param firstCol
     *            开始列
     * @param endCol
     *            结束列
     * @return 设置好的sheet.
     */
    public static HSSFSheet setHSSFValidation(HSSFSheet sheet,
                                              String[] textlist, int firstRow, int endRow, int firstCol,
                                              int endCol) {
        // 加载下拉列表内容
        DVConstraint constraint = DVConstraint
                .createExplicitListConstraint(textlist);
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow,
                endRow, firstCol, endCol);
        // 数据有效性对象
        HSSFDataValidation data_validation_list = new HSSFDataValidation(
                regions, constraint);
        sheet.addValidationData(data_validation_list);
        return sheet;
    }



    // 创建表头
    private void createTitle(HSSFWorkbook workbook, HSSFSheet sheet) {
        HSSFRow row = sheet.createRow(0);
        // 设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(1, 12 * 256);
        sheet.setColumnWidth(3, 17 * 256);

        // 设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("设备分组");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("设备编号");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("设备名称");
        cell.setCellStyle(style);

    }

    // 生成excel文件
    protected void buildExcelFile(String filename, HSSFWorkbook workbook) throws Exception {
        FileOutputStream fos = new FileOutputStream(filename);
        workbook.write(fos);
        fos.flush();
        fos.close();
    }

    // 浏览器下载excel
    protected void buildExcelDocument(String filename, HSSFWorkbook workbook, HttpServletResponse response)
            throws Exception {
        response.reset();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "utf-8"));
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }
    /**
     * 功能描述：初始化设备高级设置参数
     * @param advancedSettingEntity
     */
    public void initAdvSet(AdvancedSettingEntity advancedSettingEntity,int n){
        advancedSettingEntity.setCreateTime(new Date());
        advancedSettingEntity.setLoadWorkMode(5);
        advancedSettingEntity.setPowerLoad(500);
        advancedSettingEntity.setTimeTurnOn(1080);
        advancedSettingEntity.setTimeTurnOff(0);
        advancedSettingEntity.setTime1(10);
        advancedSettingEntity.setTime2(0);
        advancedSettingEntity.setTime3(0);
        advancedSettingEntity.setTime4(0);
        advancedSettingEntity.setTime5(0);
        advancedSettingEntity.setTimeDown(0);
        advancedSettingEntity.setPowerPeople1(100);
        advancedSettingEntity.setPowerPeople2(100);
        advancedSettingEntity.setPowerPeople3(100);
        advancedSettingEntity.setPowerPeople4(100);
        advancedSettingEntity.setPowerPeople5(100);
        advancedSettingEntity.setPowerDawnPeople(100);
        advancedSettingEntity.setPowerSensor1(0);
        advancedSettingEntity.setPowerSensor2(0);
        advancedSettingEntity.setPowerSensor3(0);
        advancedSettingEntity.setPowerSensor4(0);
        advancedSettingEntity.setPowerSensor5(0);
        advancedSettingEntity.setPowerSensorDown(0);
        advancedSettingEntity.setSavingSwitch(2);
        advancedSettingEntity.setAutoSleepTime(0);
        advancedSettingEntity.setLigntOnDuration(5);
        advancedSettingEntity.setPvSwitch(1);
        advancedSettingEntity.setTempCharge(55395);
        advancedSettingEntity.setTempDisCharge(55395);
        advancedSettingEntity.setInspectionTime(5);
        advancedSettingEntity.setInductionSwitch(0);
        advancedSettingEntity.setInductionLightOnDelay(30);
        advancedSettingEntity.setFirDownPower(1100);
        advancedSettingEntity.setTwoDownPower(1050);
        advancedSettingEntity.setThreeDownPower(1000);
        advancedSettingEntity.setFirReducAmplitude(60);
        advancedSettingEntity.setTwoReducAmplitude(40);
        advancedSettingEntity.setThreeReducAmplitude(20);
        advancedSettingEntity.setSwitchDelayTime(15);
        advancedSettingEntity.setCustomeSwitch(0);
        advancedSettingEntity.setTemControlSwitch(0);
        if(n == 1){
            advancedSettingEntity.setVpv(500);
            advancedSettingEntity.setBatType(4);
            advancedSettingEntity.setBatStringNum(3);
            advancedSettingEntity.setVolOverDisCharge(900);
            advancedSettingEntity.setVolCharge(1260);
            advancedSettingEntity.setICharge(2000);
        }else if(n == 2){
            advancedSettingEntity.setVpv(200);
            advancedSettingEntity.setBatType(3);
            advancedSettingEntity.setBatStringNum(1);
            advancedSettingEntity.setVolOverDisCharge(280);
            advancedSettingEntity.setVolCharge(360);
            advancedSettingEntity.setICharge(1500);
            advancedSettingEntity.setLowPowerConsumption(0);
        }
    }
}
