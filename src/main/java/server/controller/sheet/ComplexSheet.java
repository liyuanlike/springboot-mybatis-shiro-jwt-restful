package server.controller.sheet;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.sheet.ReportStationDataCez_Res;
import server.db.primary.model.sheet.ReportStationDataCez0_Res;
import server.service.SheetComplexService;
import server.tool.ExcelUtils;
import server.tool.Res;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("/sheet/complex")
@RestController()
public class ComplexSheet {
    @Autowired
    SheetComplexService sheetComplexService;

    private ArrayList<ExcelUtils.DataColumn> getColumnMap() {
        //前端网页数据所需的列，与后台导出excel所需的列统一在此设置
        //前端数据列，取map的id集合
        //excel数据列，取value非空
        return new ArrayList<ExcelUtils.DataColumn>() {{
            add(new ExcelUtils.DataColumn("id", "").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("report_time", "1", 1));
            add(new ExcelUtils.DataColumn("cyg_yw1", "1"));
            add(new ExcelUtils.DataColumn("cyg_jm1", "1"));
            add(new ExcelUtils.DataColumn("cyg_kr1", "1"));
            add(new ExcelUtils.DataColumn("cyg_yw2", "1"));
            add(new ExcelUtils.DataColumn("cyg_jm2", "1"));
            add(new ExcelUtils.DataColumn("cyg_kr2", "1"));
            add(new ExcelUtils.DataColumn("pi_1150", "1"));
            add(new ExcelUtils.DataColumn("ti_1270", "1"));
            add(new ExcelUtils.DataColumn("ft109", "1"));
            add(new ExcelUtils.DataColumn("ti_1250", "1"));
            add(new ExcelUtils.DataColumn("pi_1090", "1"));
            add(new ExcelUtils.DataColumn("ti_1260", "1"));
            add(new ExcelUtils.DataColumn("pi_1100", "1"));
            add(new ExcelUtils.DataColumn("ti_1230", "1"));
            add(new ExcelUtils.DataColumn("ti_1240", "1"));
            add(new ExcelUtils.DataColumn("pi_1140", "1"));
            add(new ExcelUtils.DataColumn("pi_1110", "1"));
            add(new ExcelUtils.DataColumn("pi_1120", "1"));
            add(new ExcelUtils.DataColumn("ti_1200", "1"));
            add(new ExcelUtils.DataColumn("pi_1070", "1"));
            add(new ExcelUtils.DataColumn("jiarelu1", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("jiarelu2", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("jiarelu3", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("rq_ckyl", "1"));
            add(new ExcelUtils.DataColumn("rq_ljds", "1"));
            add(new ExcelUtils.DataColumn("ranqiliang", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("pi_1060", "1"));
            add(new ExcelUtils.DataColumn("ti_1150", "1"));
            add(new ExcelUtils.DataColumn("ft104s", "1"));
            add(new ExcelUtils.DataColumn("ft_1040", "1"));
            add(new ExcelUtils.DataColumn("rsb_by1", "1"));
            add(new ExcelUtils.DataColumn("rsb_by2", "1"));
            add(new ExcelUtils.DataColumn("rsb_by3", "1"));
            add(new ExcelUtils.DataColumn("lt_1030", "1"));
            add(new ExcelUtils.DataColumn("hsg_wd", "1"));
            add(new ExcelUtils.DataColumn("pi_1080", "1"));
            add(new ExcelUtils.DataColumn("ti_1210", "1"));
            add(new ExcelUtils.DataColumn("pi_1010", "1"));
            add(new ExcelUtils.DataColumn("ti_1010", "1"));
            add(new ExcelUtils.DataColumn("ft_1010", "1"));
            add(new ExcelUtils.DataColumn("ft101s", "1"));
            add(new ExcelUtils.DataColumn("cshrq_hgwd", "1"));
            add(new ExcelUtils.DataColumn("ti_1020", "1"));
            add(new ExcelUtils.DataColumn("ti_1030", "1"));
            add(new ExcelUtils.DataColumn("ti_1220", "1"));
            add(new ExcelUtils.DataColumn("ws_yl", "1"));
            add(new ExcelUtils.DataColumn("ws_wd", "1"));
            add(new ExcelUtils.DataColumn("ws_llj", "1"));
            add(new ExcelUtils.DataColumn("yeliang", "1").setNoDataBaseColumn());
        }};
    }

    @PostMapping()
    public Res dataTable(@RequestBody JSONObject bodyJO) throws ParseException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<ReportStationDataCez_Res> reportDataWaterWell = getData(bodyJO);
        JSONObject jO = new JSONObject();
        jO.put("Production", reportDataWaterWell);
        return Res.successData(jO);
    }

    @PatchMapping()
    public Res dataEdit(@RequestBody JSONObject bodyJO) {
        ReportStationDataCez_Res reportData = bodyJO.getJSONObject("editData").toJavaObject(ReportStationDataCez_Res.class);
        if (reportData.getId() == null) {
            return Res.failureMsg("修改失败，无id");
        }
        if (sheetComplexService.updateReportData(reportData)) {
            return Res.successMsg("修改成功");
        } else {
            return Res.failureMsg("修改失败");
        }
    }

    @PostMapping("/excel")
    public void excel(@RequestBody() JSONObject bodyJO, HttpServletResponse response) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        int rowNum = 0;
        List<ReportStationDataCez_Res> reportData = getData(bodyJO);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date searchDate = bodyJO.getDate("searchDate");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);
        String fileName = "复杂报表（" + searchDateStr + "）";
        //以下表头格式使用ExcelReadTool类生成，代码打印在控制台，手动复制到本处
        List<ExcelUtils.HeaderCell> extraHeaderCell = new ArrayList<ExcelUtils.HeaderCell>() {{
//行分隔
            add(new ExcelUtils.HeaderCell("项目", 0, 0, 1, 2));
            add(new ExcelUtils.HeaderCell("储油罐", 1, 0, 6, 1));
            add(new ExcelUtils.HeaderCell("外输泵", 7, 0, 3, 1));
            add(new ExcelUtils.HeaderCell("进站（来液升温）换热器", 10, 0, 6, 1));
            add(new ExcelUtils.HeaderCell("分离缓冲罐", 16, 0, 3, 1));
            add(new ExcelUtils.HeaderCell("加热炉", 19, 0, 5, 1));
            add(new ExcelUtils.HeaderCell("燃气", 24, 0, 3, 1));
            add(new ExcelUtils.HeaderCell("热水泵", 27, 0, 7, 1));
            add(new ExcelUtils.HeaderCell("热回水罐", 34, 0, 2, 1));
            add(new ExcelUtils.HeaderCell("燃油泵", 36, 0, 2, 1));
            add(new ExcelUtils.HeaderCell("掺水泵", 38, 0, 4, 1));
            add(new ExcelUtils.HeaderCell("掺水换热器", 42, 0, 3, 1));
            add(new ExcelUtils.HeaderCell("燃油换热器", 45, 0, 1, 1));
            add(new ExcelUtils.HeaderCell("外输记录", 46, 0, 4, 1));
//行分隔
            add(new ExcelUtils.HeaderCell("1#", 1, 1, 3, 1));
            add(new ExcelUtils.HeaderCell("2#", 4, 1, 3, 1));
            add(new ExcelUtils.HeaderCell("出口汇管", 7, 1, 3, 1));
            add(new ExcelUtils.HeaderCell("出口汇管", 10, 1, 4, 1));
            add(new ExcelUtils.HeaderCell("1#", 14, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("2#", 15, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("汇管", 16, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("1#", 17, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("2#", 18, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("出口汇管", 19, 1, 2, 2));
            add(new ExcelUtils.HeaderCell("1#", 21, 1, 1, 2));
            add(new ExcelUtils.HeaderCell("2#", 22, 1, 1, 2));
            add(new ExcelUtils.HeaderCell("3#", 23, 1, 1, 2));
            add(new ExcelUtils.HeaderCell("#", 24, 1, 3, 2));
            add(new ExcelUtils.HeaderCell("出口汇管", 27, 1, 4, 2));
            add(new ExcelUtils.HeaderCell("1#", 31, 1, 1, 2));
            add(new ExcelUtils.HeaderCell("2#", 32, 1, 1, 2));
            add(new ExcelUtils.HeaderCell("3#", 33, 1, 1, 2));
            add(new ExcelUtils.HeaderCell("液位m", 34, 1, 1, 3));
            add(new ExcelUtils.HeaderCell("温度℃", 35, 1, 1, 3));
            add(new ExcelUtils.HeaderCell("#", 36, 1, 2, 1));
            add(new ExcelUtils.HeaderCell("出口汇管", 38, 1, 4, 2));
            add(new ExcelUtils.HeaderCell("出口汇管", 42, 1, 1, 2));
            add(new ExcelUtils.HeaderCell("1#", 43, 1, 1, 2));
            add(new ExcelUtils.HeaderCell("2#", 44, 1, 1, 2));
            add(new ExcelUtils.HeaderCell("油", 45, 1, 1, 2));
            add(new ExcelUtils.HeaderCell("压力MPa", 46, 1, 1, 3));
            add(new ExcelUtils.HeaderCell("温度℃", 47, 1, 1, 3));
            add(new ExcelUtils.HeaderCell("流量计读数m³", 48, 1, 1, 3));
            add(new ExcelUtils.HeaderCell("液量m³", 49, 1, 1, 3));
//行分隔
            add(new ExcelUtils.HeaderCell("时间", 0, 2, 1, 2));
            add(new ExcelUtils.HeaderCell("液位", 1, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("界面", 2, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("库容", 3, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("液位", 4, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("界面", 5, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("库容", 6, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("压力", 7, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("温度", 8, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("流量计读数m³", 9, 2, 1, 2));
            add(new ExcelUtils.HeaderCell("油", 10, 2, 2, 1));
            add(new ExcelUtils.HeaderCell("水", 12, 2, 2, 1));
            add(new ExcelUtils.HeaderCell("油", 14, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("油", 15, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("天然气", 16, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("天然气", 17, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("天然气", 18, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("出口汇管", 36, 2, 2, 1));
//行分隔
            add(new ExcelUtils.HeaderCell("m", 1, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m", 2, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("t", 3, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m", 4, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m", 5, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("t", 6, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("MPa", 7, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("℃", 8, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("温度℃", 10, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("压力MPa", 11, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("温度℃", 12, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("压力MPa", 13, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("出口温度℃", 14, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("出口温度℃", 15, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("压力MPa", 16, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("压力MPa", 17, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("压力MPa", 18, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("温度℃", 19, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("压力MPa", 20, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("出口温度℃", 21, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("出口温度℃", 22, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("出口温度℃", 23, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("出口压力MPa", 24, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("流量计读数m³", 25, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("燃气量m³", 26, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("压力MPa", 27, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("温度℃", 28, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("流量计读数m³(瞬时)", 29, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("流量计读数m³(累计)", 30, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("泵压MPa", 31, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("泵压MPa", 32, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("泵压MPa", 33, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("压力MPa", 36, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("温度℃", 37, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("压力MPa", 38, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("温度℃", 39, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("流量m³(累计)", 40, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("流量m³/h(瞬时)", 41, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("温度℃", 42, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("温度℃", 43, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("温度℃", 44, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("温度℃", 45, 3, 1, 1));
        }};
        ExcelUtils.LastRowColumnNum lastRowColumnNum = ExcelUtils.addRowsByData(wb, 0, 0, 0, fileName, getColumnMap(), false, reportData, false, extraHeaderCell);
        rowNum = lastRowColumnNum.getRowNum();

        List<ReportStationDataCez0_Res> formData = getFormData(bodyJO);
        if (formData != null) {
            StringBuilder allNote = new StringBuilder();
            for (ReportStationDataCez0_Res res : formData) {
                HSSFRow row = sheet.createRow(rowNum);
                int columnIndex = 0;
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellType(CellType.STRING);
                    if (res.getReport_hour() != null) {
                        cell.setCellValue(res.getReport_hour());
                    }
                    cell.setCellStyle(ExcelUtils.dataStyle(wb));
                    columnIndex++;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellValue("外输量(m³):");
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 2);
                    sheet.addMergedRegion(cellRangeAddress);
                    ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.headerStyle(wb));
                    columnIndex = columnIndex + 2 + 1;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellType(CellType.STRING);
                    if (res.getWsl() != null) {
                        cell.setCellValue(res.getWsl());
                    }
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 1);
                    sheet.addMergedRegion(cellRangeAddress);
                    ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.dataStyle(wb));
                    columnIndex = columnIndex + 1 + 1;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellValue("燃气量(吨):");
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 2);
                    sheet.addMergedRegion(cellRangeAddress);
                    ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.headerStyle(wb));
                    columnIndex = columnIndex + 2 + 1;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellType(CellType.STRING);
                    if (res.getRql() != null) {
                        cell.setCellValue(res.getRql());
                    }
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 1);
                    sheet.addMergedRegion(cellRangeAddress);
                    ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.dataStyle(wb));
                    columnIndex = columnIndex + 1 + 1;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellValue("卸油量(吨):");
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 2);
                    sheet.addMergedRegion(cellRangeAddress);
                    ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.headerStyle(wb));
                    columnIndex = columnIndex + 2 + 1;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellType(CellType.STRING);
                    if (res.getXyl() != null) {
                        cell.setCellValue(res.getXyl());
                    }
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 1);
                    sheet.addMergedRegion(cellRangeAddress);
                    ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.dataStyle(wb));
                    columnIndex = columnIndex + 1 + 1;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellValue("加药量(吨):");
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 2);
                    sheet.addMergedRegion(cellRangeAddress);
                    ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.headerStyle(wb));
                    columnIndex = columnIndex + 2 + 1;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellType(CellType.STRING);
                    if (res.getJyl() != null) {
                        cell.setCellValue(res.getJyl());
                    }
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 1);
                    sheet.addMergedRegion(cellRangeAddress);
                    ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.dataStyle(wb));
                    columnIndex = columnIndex + 1 + 1;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellValue("备注:");
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 2);
                    sheet.addMergedRegion(cellRangeAddress);
                    ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.headerStyle(wb));
                    columnIndex = columnIndex + 2 + 1;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellType(CellType.STRING);
                    if (res.getBz() != null) {
                        cell.setCellValue(res.getBz());
                        allNote.append(res.getBz()).append("  ");
                    }
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 25);
                    sheet.addMergedRegion(cellRangeAddress);
                    ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.dataStyle(wb));
                }
                rowNum++;
            }
            {
                HSSFRow row = sheet.createRow(rowNum);
                int columnIndex = 0;
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue("备注");
                    cell.setCellStyle(ExcelUtils.headerStyle(wb));
                    columnIndex++;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(allNote.toString());
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 48);
                    sheet.addMergedRegion(cellRangeAddress);
                    ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.headerStyle(wb));
                }
            }
        }
        ExcelUtils.responseOut(response, wb, fileName);
    }

    private List<ReportStationDataCez_Res> getData(JSONObject bodyJO) throws ParseException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Date searchDate = bodyJO.getDate("searchDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);

        searchDateStr = "2018-08-24";

        return sheetComplexService.selectReportData(searchDateStr, getColumnMap());
    }




    private Map<String, String> getFormColumnMap() {
        //前端网页数据所需的列，与后台导出excel所需的列统一在此设置
        //前端数据列，取map的id集合
        //excel数据列，取value非空值
        return new LinkedHashMap<String, String>() {{
            put("id", "");
            put("report_hour", "1");
            put("wsl", "1");
            put("rql", "1");
            put("xyl", "1");
            put("jyl", "1");
            put("bz", "1");
        }};
    }

    @PostMapping("/form")
    public Res formDataTable(@RequestBody JSONObject bodyJO) {
        List<ReportStationDataCez0_Res> res = getFormData(bodyJO);
        JSONObject jO = new JSONObject();
        jO.put("Production", res);
        return Res.successData(jO);
    }

    @PatchMapping("/form")
    public Res formDataEdit(@RequestBody JSONObject bodyJO) {
        ReportStationDataCez0_Res reportData = bodyJO.getJSONObject("editData").toJavaObject(ReportStationDataCez0_Res.class);
        if (reportData.getId() == null) {
            return Res.failureMsg("修改失败，无id");
        }
        if (sheetComplexService.updateReportForm(reportData)) {
            return Res.successMsg("修改成功");
        } else {
            return Res.failureMsg("修改失败");
        }
    }

    private List<ReportStationDataCez0_Res> getFormData(JSONObject bodyJO) {
        Date searchDate = bodyJO.getDate("searchDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);
        return sheetComplexService.selectReportForm(searchDateStr, getFormColumnMap());
    }
}
