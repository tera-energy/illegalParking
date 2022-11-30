package com.teraenergy.illegalparking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.exception.TeraErrCode;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import com.teraenergy.illegalparking.model.entity.lawdong.domain.LawDong;
import com.teraenergy.illegalparking.model.entity.lawdong.service.LawDongService;
import com.teraenergy.illegalparking.model.entity.parking.domain.Parking;
import com.teraenergy.illegalparking.model.entity.parking.service.ParkingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/files")
@Controller
public class FileController {

    private final ObjectMapper objectMapper;

    private final ParkingService parkingService;

    private final LawDongService lawDongService;

    @Value("${file.resourcePath}")
    String resourcePath;

    @Value("${file.excelPath}")
    String excelPath;

    String KEY_REASON = "reason";
    String KEY_FILENAME = "filename";
    String KEY_FILEINPUTSTREAM = "fileInputStream";
    String KEY_RESULT = "success";

    public JsonNode fileDelete(HttpServletRequest request, String body) throws JsonProcessingException {

        JsonNode requestJson = objectMapper.readTree(body);
        HashMap<String, Object> requestMap = objectMapper.convertValue(requestJson, HashMap.class);

        /* DB 업데이트 */
        Integer fileInfoSeq = Integer.parseInt((String) requestMap.get("fileInfoSeq"));

        HashMap<String, Object> resultMap = Maps.newHashMap();
        String jsonStr = objectMapper.writeValueAsString(resultMap);
        return objectMapper.readTree(jsonStr);
    }

    public Object fileDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HashMap<String, Object> parameterMap = Maps.newHashMap();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            String value = request.getParameter(name);
            parameterMap.put(name, value);
        }
        try {
            if (parameterMap.get("fileType").equals("excel")) {
                downloadByExcel(request, response, parameterMap);
            } else {
                download(request, response, parameterMap);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    public Map<String, Object> fileUpload(HttpServletRequest request) throws TeraException {

        HashMap<String, Object> parameterMap = Maps.newHashMap();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            String value = request.getParameter(name);
            parameterMap.put(name, value);
        }

        HashMap<String, Object> resultMap = Maps.newHashMap();
        try {
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (!isMultipart) {
                String reason = "not exist file";
                resultMap.put(KEY_REASON, reason);
                resultMap.put(KEY_RESULT, "fail");
            } else {
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                Iterator<String> keys = multipartRequest.getFileNames();
                while (keys.hasNext()) {
                    MultipartFile multipartFile = multipartRequest.getFile(keys.next());
                    resultMap.put(KEY_FILEINPUTSTREAM, multipartFile.getInputStream());
                    resultMap.put(KEY_FILENAME, multipartFile.getOriginalFilename());
                }

                resultMap.put(KEY_RESULT, true);
            }
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.FILE_READ_FAILURE, e);
        }
    }

    @RequestMapping(value = "/image/set", method = RequestMethod.POST)
    @ResponseBody
    public JsonNode setImage(HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> resultMap = fileUpload(request);

            FileInputStream fis = (FileInputStream) resultMap.get(KEY_FILEINPUTSTREAM);
            String fileName = (String) resultMap.get(KEY_FILENAME);
            File file = new File(resourcePath + "/" + fileName);

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(fis.readAllBytes());

            // 파일 권한 설정 ( 리눅스 인 경우의 fileUpload 경로 )
            if (resourcePath.equals("/fileUpload/")) {
                Runtime.getRuntime().exec("chmod -R 666 " + file);
            }

            resultMap.remove(KEY_FILEINPUTSTREAM);
            resultMap.put(KEY_RESULT, "success");
            JsonNode jsonNode = objectMapper.readTree(objectMapper.writeValueAsString(resultMap));
            return jsonNode;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage(), e);
        }
    }

//    @RequestMapping(value = "/files/ai", method = RequestMethod.POST)
//    @ResponseBody
//    public JsonNode passToAI(HttpServletRequest request) throws Exception {
//        try {
//            Map<String, Object> resultMap = fileUpload(request);
//
//            if (resultMap.get(KEY_RESULT).equals("success")) {
//                FileInputStream fis = (FileInputStream) resultMap.get(KEY_FILEINPUTSTREAM);
//                String fileName = (String) resultMap.get(KEY_FILENAME);
//                // send ai server
//            }
//
//            resultMap.remove(KEY_FILEINPUTSTREAM);
//            JsonNode jsonNode = objectMapper.readTree(objectMapper.writeValueAsString(resultMap));
//            return jsonNode;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new Exception(e.getMessage(), e);
//        }
//    }

    @RequestMapping(value = "/lawDong", method = RequestMethod.POST)
    @ResponseBody
    public JsonNode parsingExcelLawDong(HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> resultMap = fileUpload(request);

            if (resultMap.get(KEY_RESULT).equals("success")) {
                FileInputStream fis = (FileInputStream) resultMap.get("fileInputStream");
                XSSFWorkbook book = new XSSFWorkbook(fis);
                XSSFSheet sheet = book.getSheetAt(0);
            }

            resultMap.remove(KEY_FILEINPUTSTREAM);
            JsonNode jsonNode = objectMapper.readTree(objectMapper.writeValueAsString(resultMap));
            return jsonNode;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/parking", method = RequestMethod.POST)
    @ResponseBody
    public JsonNode parsingExcelForParking(HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> resultMap = fileUpload(request);

            if (resultMap.get(KEY_RESULT).equals("success")) {
                FileInputStream fis = (FileInputStream) resultMap.get(KEY_FILEINPUTSTREAM);
                String fileName = (String) resultMap.get(KEY_FILENAME);
                XSSFWorkbook book = new XSSFWorkbook(fis);
                XSSFSheet sheet = book.getSheetAt(0);
                List<Parking> parkings = Lists.newArrayList();

                // 행수
                int rows = sheet.getPhysicalNumberOfRows();

                for (int rowindex = 0; rowindex < rows; rowindex++) {

                    // 0행은 1행부터 값들의 이름을 작성된 것으로 제외
                    if (rowindex == 0) {
                        continue;
                    }

                    // 행을읽는다
                    XSSFRow row = sheet.getRow(rowindex);
                    if (row != null) {
                        Parking parking = new Parking();
                        parking.setPrkplceNo(getCellData(row.getCell(0)));
                        parking.setPrkplceNm(getCellData(row.getCell(1)));
                        parking.setPrkplceSe(getCellData(row.getCell(2)));
                        parking.setPrkplceType(getCellData(row.getCell(3)));
                        parking.setRdnmadr(getCellData(row.getCell(4)));
                        parking.setLnmadr(getCellData(row.getCell(5)));
                        parking.setPrkcmprt((int)Float.parseFloat(getCellData(row.getCell(6))));
                        parking.setFeedingSe((int)Float.parseFloat(getCellData(row.getCell(7))));
                        parking.setEnforceSe(getCellData(row.getCell(8)));
                        parking.setOperDay(getCellData(row.getCell(9)));
                        parking.setWeekdayOperOpenHhmm(getCellData(row.getCell(10)).split(" ")[1]);
                        parking.setWeekdayOperColseHhmm(getCellData(row.getCell(11)).split(" ")[1]);
                        parking.setSatOperOpenHhmm(getCellData(row.getCell(12)).split(" ")[1]);
                        parking.setSatOperCloseHhmm(getCellData(row.getCell(13)).split(" ")[1]);
                        parking.setHolidayOperOpenHhmm(getCellData(row.getCell(14)).split(" ")[1]);
                        parking.setHolidayOperCloseHhmm(getCellData(row.getCell(15)).split(" ")[1]);
                        parking.setParkingchrgeInfo(getCellData(row.getCell(16)));
                        parking.setBasicTime(getCellData(row.getCell(17)));
                        parking.setBasicCharge((int)Float.parseFloat( getCellData(row.getCell(18)) == "" ? "0" : getCellData(row.getCell(18)) ));
                        parking.setAddUnitTime(getCellData(row.getCell(19)));
                        parking.setAddUnitCharge((int)Float.parseFloat( getCellData(row.getCell(20)) == "" ? "0" : getCellData(row.getCell(20)) ));
                        parking.setDayCmmtktAdjTime(getCellData(row.getCell(21)));
                        parking.setDayCmmtkt((int)Float.parseFloat( getCellData(row.getCell(22)) == "" ? "0" : getCellData(row.getCell(22)) ));
                        parking.setMonthCmmtkt((int)Float.parseFloat( getCellData(row.getCell(23)) == "" ? "0" : getCellData(row.getCell(23)) ));
                        parking.setMetpay(getCellData(row.getCell(24)));
                        parking.setSpcmnt(getCellData(row.getCell(25)));
                        parking.setInstitutionNm(getCellData(row.getCell(26)));
                        parking.setPhoneNumber(getCellData(row.getCell(27)));
                        parking.setLatitude(Double.parseDouble(getCellData(row.getCell(28))));
                        parking.setLongitude(Double.parseDouble(getCellData(row.getCell(29))));
                        parking.setReferenceDate(LocalDate.parse(getCellData(row.getCell(30)).split(" ")[0]));

                        String temp[] = parking.getLnmadr().trim().split(" ");
                        String addr = temp[0] + " " + temp[1] + " " +temp[2];
                        LawDong lawDong = lawDongService.getFromLnmadr(addr);

                        if ( lawDong != null) {
                            parking.setCode(lawDong.getCode());
                        } else {
                            parking.setCode("");
                        }
                        parkings.add(parking);
                    }
                }

                parkingService.sets(parkings);
            }
            resultMap.remove(KEY_FILEINPUTSTREAM);
            JsonNode jsonNode = objectMapper.readTree(objectMapper.writeValueAsString(resultMap));
            return jsonNode;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage(), e);
        }
    }

    private String getCellData(XSSFCell cell) {
        String value = "";
        switch (cell.getCellType()) {
            case FORMULA:
                value = cell.getCellFormula();
                break;
            case NUMERIC:
                if( DateUtil.isCellDateFormatted(cell)) {
                    Date date  = cell.getDateCellValue();
                    value = new SimpleDateFormat("yyyy-MM-dd hh:mm").format(date);
                } else {
                    value = cell.getNumericCellValue() + "";
                }
                break;
            case STRING:
                value = cell.getStringCellValue() + "";
                break;
            case BLANK:
                value = cell.getBooleanCellValue() + "";
                break;
            case ERROR:
                value = cell.getErrorCellValue() + "";
                break;
        }

        return value;
    }

    public void download(HttpServletRequest request, HttpServletResponse response, HashMap<String, Object> parameterMap) throws Exception {

        String fileName = (String) parameterMap.get("fileName");
        String filePath = Paths.get(resourcePath, fileName).toString();
        File file = new File(filePath);

        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        String mimeType = Files.probeContentType(Paths.get(file.getAbsolutePath()));
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        response.setContentType(mimeType);
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Length", "" + file.length());

        bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());

        try {
            byte[] buffer = new byte[8192];
            int read = 0;
            while ((read = bufferedInputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, read);
            }
            bufferedOutputStream.flush();
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            if (bufferedInputStream != null) bufferedInputStream.close();
            if (bufferedInputStream != null) bufferedOutputStream.close();
        }

    }

    public void downloadByExcel(HttpServletRequest request, HttpServletResponse response, HashMap<String, Object> parameterMap) throws Exception {

        String fileName = (String) parameterMap.get("fileName");
        String filePath = Paths.get(excelPath, fileName).toString();
        File file = new File(filePath);

        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        String mimeType = "application/x-msexcel";

        response.setContentType(mimeType);
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Length", "" + file.length());


        bufferedInputStream = new BufferedInputStream(new FileInputStream(file));  // << excel file read inputsream 으로 변경
        bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());

        try {
            byte[] buffer = new byte[8192];
            int read = 0;

            while ((read = bufferedInputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, read);
            }
            bufferedOutputStream.flush();
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            if (bufferedInputStream != null) bufferedInputStream.close();
            if (bufferedInputStream != null) bufferedOutputStream.close();
        }
    }

}