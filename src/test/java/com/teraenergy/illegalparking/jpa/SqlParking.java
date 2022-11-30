package com.teraenergy.illegalparking.jpa;

import com.teraenergy.illegalparking.ApplicationTests;
import com.teraenergy.illegalparking.model.entity.lawdong.domain.LawDong;
import com.teraenergy.illegalparking.model.entity.lawdong.service.LawDongService;
import com.teraenergy.illegalparking.model.entity.parking.domain.Parking;
import com.teraenergy.illegalparking.model.entity.parking.service.ParkingService;
import org.apache.commons.compress.utils.Lists;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@ActiveProfiles(value = "debug")
@SpringBootTest(classes = ApplicationTests.class)
@RunWith(SpringRunner.class)
@Transactional
public class SqlParking {

    @Value("${file.staticPath}")
    String staticPath;

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    ParkingService parkingService;

    @Autowired
    LawDongService lawDongService;

    private String getCellData(XSSFCell cell) {
        String value = "";
        if ( cell == null) return "";
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


    @Test
    public void insert() {
        List<String> fileNames = Lists.newArrayList();
        fileNames.add("전라남도_광양시_주차장정보_20220324_1648095649855_1146.xlsx");
        fileNames.add("전라남도_나주시_주차장정보_20190701.xlsx");
        fileNames.add("전라남도_목포시_주차장정보_20211020_1634703796251_12036.xlsx");
        fileNames.add("전라남도_여수시_주차장정보_20210625_1624602060324_10596.xlsx");

        try {
            for (String fileName : fileNames) {
                Resource resource = resourceLoader.getResource(staticPath + fileName);

                FileInputStream fis = new FileInputStream(resource.getFile());
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
                        parking.setPrkcmprt((int) Float.parseFloat(getCellData(row.getCell(6))));
                        parking.setFeedingSe((int) Float.parseFloat(getCellData(row.getCell(7))));
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
                        parking.setBasicCharge((int) Float.parseFloat(getCellData(row.getCell(18)).equals("") ? "0" : getCellData(row.getCell(18))));
                        parking.setAddUnitTime(getCellData(row.getCell(19)));
                        parking.setAddUnitCharge((int) Float.parseFloat(getCellData(row.getCell(20)).equals("") ? "0" : getCellData(row.getCell(20))));
                        parking.setDayCmmtktAdjTime(getCellData(row.getCell(21)));
                        parking.setDayCmmtkt((int) Float.parseFloat(getCellData(row.getCell(22)).equals("") ? "0" : getCellData(row.getCell(22))));
                        parking.setMonthCmmtkt((int) Float.parseFloat(getCellData(row.getCell(23)).equals("") ? "0" : getCellData(row.getCell(23))));
                        parking.setMetpay(getCellData(row.getCell(24)));
                        parking.setSpcmnt(getCellData(row.getCell(25)));
                        parking.setInstitutionNm(getCellData(row.getCell(26)));
                        parking.setPhoneNumber(getCellData(row.getCell(27)));
                        parking.setLatitude(Double.parseDouble(getCellData(row.getCell(28))));
                        parking.setLongitude(Double.parseDouble(getCellData(row.getCell(29))));
                        parking.setReferenceDate(LocalDate.parse(getCellData(row.getCell(30)).split(" ")[0]));

                        String temp[] = parking.getLnmadr().trim().split(" ");
                        if ( temp.length >= 3) {
                            String addr = temp[0] + " " + temp[1] + " " + temp[2];
                            LawDong lawDong = lawDongService.getFromLnmadr(addr);
                            if ( lawDong != null) {
                                parking.setCode(lawDong.getCode());
                            } else {
                                parking.setCode("");
                            }
                        } else {
                            parking.setCode("");
                        }

                        parking.setIsDel(false);
                        parkings.add(parking);
                    }
                }
                parkingService.sets(parkings);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void update(){
        List<Parking> parkings = parkingService.gets();
        List<Parking> newParkings = Lists.newArrayList();

        for (Parking parking : parkings) {
            if (!parking.getLnmadr().equals("")) {
                String temp[] = parking.getLnmadr().trim().split(" ");
                String addr = temp[0] + " " + temp[1] + " " +temp[2];
                LawDong lawDong = lawDongService.getFromLnmadr(addr);
                if ( lawDong != null) {
                    parking.setCode(lawDong.getCode());
                } else {
                    parking.setCode("");
                }
                newParkings.add(parking);
            } else {
                parking.setCode("");
                newParkings.add(parking);
            }
        }

        parkingService.sets(parkings);

    }

    @Test
    public void select() {
        Parking parking = parkingService.get(1000);
        if (parking == null) {
            System.out.println("parking is null ");
        } else  {
            System.out.println(parking.getCode());
        }
    }

}
