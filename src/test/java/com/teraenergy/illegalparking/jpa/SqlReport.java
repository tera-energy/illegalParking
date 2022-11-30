package com.teraenergy.illegalparking.jpa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.ApplicationTests;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import com.teraenergy.illegalparking.model.entity.calculate.service.CalculateService;
import com.teraenergy.illegalparking.model.entity.illegalEvent.domain.IllegalEvent;
import com.teraenergy.illegalparking.model.entity.illegalEvent.enums.IllegalType;
import com.teraenergy.illegalparking.model.entity.point.service.PointService;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneMapperService;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneService;
import com.teraenergy.illegalparking.model.entity.comment.domain.Comment;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptStateType;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.comment.service.CommentService;
import com.teraenergy.illegalparking.model.entity.receipt.service.ReceiptService;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportStateType;
import com.teraenergy.illegalparking.model.entity.report.service.ReportService;
import com.teraenergy.illegalparking.model.entity.reportstatics.domain.ReportStatics;
import com.teraenergy.illegalparking.model.entity.reportstatics.service.ReportStaticsService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * Date : 2022-09-24
 * Author : young
 * Project : illegalParking
 * Description :
 */


@ActiveProfiles(value = "debug")
@SpringBootTest(classes = ApplicationTests.class)
@RunWith(SpringRunner.class)
@Transactional
public class SqlReport {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private IllegalZoneMapperService illegalZoneMapperService;

    @Autowired
    private IllegalZoneService illegalZoneService;

    @Autowired
    private UserService userService;

    @Autowired
    private PointService pointService;

    @Autowired
    private CalculateService calculateService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReportStaticsService reportStaticsService;


    @Test
    public void insert(){
        try {
            insertByReceipt();
            insertByReport();
            insertByComment();
        } catch (TeraException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void insertByReport() {
        List<Report> reports = Lists.newArrayList();

        Receipt receipt = receiptService.get(4);

        Report report1 = new Report();
        report1.setReceipt(receipt);
        report1.setRegDt(LocalDateTime.now());
        report1.setReportStateType(ReportStateType.COMPLETE);
        report1.setNote("");
        report1.setIsDel(false);
        reports.add(report1);

        reportService.sets(reports);
    }

    @Test
    public void updateReciptByILLEGAL(){
        LocalDateTime now = LocalDateTime.now();        // 현재 시간
        LocalDateTime startTime = now.minusDays(30);
        LocalDateTime endTime = now.minusMinutes(11);   // 11분 전 시간

        List<Receipt> receipts = receiptService.gets(startTime, endTime, ReceiptStateType.OCCUR, IllegalType.ILLEGAL);
        List<Comment> comments = Lists.newArrayList();

        for (Receipt receipt : receipts) {
            receipt.setReceiptStateType(ReceiptStateType.FORGET);
            Comment comment = new Comment();
            comment.setReceiptSeq(receipt.getReceiptSeq());
            comment.setContent(TeraExceptionCode.REPORT_NOT_ADD_FINISH.getMessage());
            comments.add(comment);
        }

        receiptService.sets(receipts);
        commentService.sets(comments);
    }

    @Test
    public void updateReciptByFIVE_MINUTE(){
        LocalDateTime now = LocalDateTime.now();        // 현재 시간
        LocalDateTime startTime = now.minusDays(30);
        LocalDateTime endTime = now.minusMinutes(16);   // 16분 전 시간

        List<Receipt> receipts = receiptService.gets(startTime, endTime, ReceiptStateType.OCCUR, IllegalType.FIVE_MINUTE);
        List<Comment> comments = Lists.newArrayList();

        for (Receipt receipt : receipts) {
            receipt.setReceiptStateType(ReceiptStateType.FORGET);
            Comment comment = new Comment();
            comment.setReceiptSeq(receipt.getReceiptSeq());
            comment.setContent(TeraExceptionCode.REPORT_NOT_ADD_FINISH.getMessage());
            comments.add(comment);
        }

        receiptService.sets(receipts);
        commentService.sets(comments);
    }

    @Test
    public void insertByReceipt() throws TeraException {
        List<Receipt> receipts = Lists.newArrayList();

        IllegalZone illegalZone = illegalZoneService.get(1);
        User user = userService.get(2);

        Receipt receipt1 = new Receipt();
        receipt1.setIllegalZone(illegalZone);
        receipt1.setUser(user);
        receipt1.setCarNum("123가1234");
        receipt1.setFileName("2F0D5DABDE074B3BA8BF9E82A89B3F81.jpg");
        receipt1.setCode("5013032000");
        receipt1.setReceiptStateType(ReceiptStateType.REPORT);
        receipt1.setIsDel(false);
        receipt1.setAddr("전라남도 나주시 빛가람동 상야1길 7");
        receipt1.setSecondFileName("2F0D5DABDE074B3BA8BF9E82A89B3F81.jpg");
        receipt1.setRegDt(LocalDateTime.now().minusMinutes(5));
        receipt1.setSecondRegDt(LocalDateTime.now());
        receipts.add(receipt1);

//        Receipt receipt2 = new Receipt();
//        receipt2.setIllegalZone(illegalZone);
//        receipt2.setUser(user);
//        receipt2.setRegDt(LocalDateTime.now().minusMinutes(6));
//        receipt2.setCarNum("123가1234");
//        receipt2.setFileName("2F0D5DABDE074B3BA8BF9E82A89B3F81.jpg");
//        receipt2.setCode("5013032000");
//        receipt2.setReceiptStateType(ReceiptStateType.PENALTY);
//        receipt2.setIsDel(false);
//        receipt2.setAddr("전라남도 나주시 산포면 신도리 378-4");
//        receipt2.setSecondFileName("2F0D5DABDE074B3BA8BF9E82A89B3F81.jpg");
//        receipt2.setSecondRegDt(LocalDateTime.now());
//        receipts.add(receipt2);
        receiptService.sets(receipts);
    }

    @Test
    public void insertByComment() throws TeraException {
        Comment comment = new Comment();
        comment.setContent("댓글 테스트 ....");
        comment.setReceiptSeq(1);
        commentService.set(comment);
    }

    @Test
    public void update(){
//        User user;
//        try {
//            user = userService.get(1);
//        } catch (TeraException e) {
//            throw new RuntimeException(e);
//        }
//
//        Report report = reportService.get(2);
//        Receipt secondReceipt = report.getSecondReceipt();
//        long pointValue = secondReceipt.getIllegalZone().getIllegalEvent().getZoneGroupType().getValue();
//        Point point = new Point();
//        point.setPointType(PointType.PLUS);
//        point.setValue(pointValue);
//        point.setNote(report.getNote());
//        point.setUserSeq(user.getUserSeq());
//        point = pointService.set(point);
//
//        // 결재 등록
//        long currentPointValue = 0;
//        long beforePointValue = 0;
//        Calculate oldCalculate = calculateService.getAtLast(user.getUserSeq());
//
//        if (oldCalculate != null) {
//            currentPointValue = oldCalculate.getCurrentPointValue();
//            beforePointValue = currentPointValue;
//        }
//
//        currentPointValue += currentPointValue + point.getValue();
//
//        Calculate calculate = new Calculate();
//        calculate.setCurrentPointValue(currentPointValue);
//        calculate.setBeforePointValue(beforePointValue);
//        calculate.setPoint(point);
//        calculate.setUser(user);
//        calculate.setRegDt(LocalDateTime.now());
//        calculate.setIsDel(false);
//        calculateService.set(calculate);
//
//        report.setNote("테스트 ... 과태료대상이 맞습니다.");
//        report.setStateType(StateType.PENALTY);
//        report.setReportUserSeq(2);
//        report.setRegDt(LocalDateTime.now());
//        reportService.set(report);

    }

    @Test
    public void select(){


    }

    @Test
    public void selectByReport() {
        List<Report> reports = reportService.gets();
        try {
            objectMapper.writeValueAsString(reports);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void selectByReceipt(){
        Receipt receipt = receiptService.get(6);

        IllegalZone illegalZone = receipt.getIllegalZone();
        IllegalEvent illegalEvent = illegalZone.getIllegalEvent();

        if (illegalEvent == null) {
            System.out.println(" ***************** event is not ");
        } else {
            System.out.println(" *****************  " + illegalEvent.getEventSeq());
        }
    }

    @Test
    public void insertByReportstatics(){

        // 1. 기존 통계 자료 가져오기
        List<ReportStatics> reportStaticsList = reportStaticsService.gets();

        // 2. 기존 통계 자료를 Map 으로 변환
        HashMap<Integer, ReportStatics> reportStaticsMap = Maps.newHashMap();

        for (ReportStatics reportStatics : reportStaticsList) {
            reportStaticsMap.put(reportStatics.getZoneSeq(), reportStatics);
        }

        // 3. 불법 주정차 구역
        List<IllegalZone> illegalZones = illegalZoneService.gets();

        // 새로운 통계 자료
        List<ReportStatics> newReportStaticsList = Lists.newArrayList();
        // 기존 통계 자료
        List<ReportStatics> oldReportStaticsList = Lists.newArrayList();

        for (IllegalZone illegalZone : illegalZones) {
            ReportStatics reportStatics = reportStaticsMap.get(illegalZone.getZoneSeq());
            if ( reportStatics == null) {
                reportStatics = new ReportStatics();
                reportStatics.setZoneSeq(illegalZone.getZoneSeq());
                reportStatics.setCode(illegalZone.getCode());
                reportStatics.setReceiptCount(reportService.getSizeForPenalty(illegalZone));
                newReportStaticsList.add(reportStatics);
            } else {
                reportStatics.setReceiptCount(reportService.getSizeForPenalty(illegalZone));
                oldReportStaticsList.add(reportStatics);
            }
        }

        reportStaticsService.sets(newReportStaticsList);
        reportStaticsService.sets(oldReportStaticsList);

    }

    @Test
    public void test(){
        LocalDateTime now = LocalDateTime.now();        // 현재 시간
        LocalDateTime startTaskTime = now;

        LocalDateTime startTimeByIllegal = now.minusMinutes(60);   // 11분 전의 60분전 시간
        LocalDateTime endTimeByIllegal = now.minusMinutes(11);   // 11분 전 시간
        List<Receipt> receiptsByIllegal = receiptService.gets(startTimeByIllegal, endTimeByIllegal, ReceiptStateType.OCCUR, IllegalType.ILLEGAL);

        LocalDateTime startTimeByFiveMinute = now.minusMinutes(60);   // 16분 전 시간
        LocalDateTime endTimeByFiveMinute = now.minusMinutes(16);   // 11분 전의 60분전 시간
        List<Receipt> receiptsByFiveMinute = receiptService.gets(startTimeByIllegal, endTimeByIllegal, ReceiptStateType.OCCUR, IllegalType.FIVE_MINUTE);

        List<Receipt> receipts = Lists.newArrayList();
        for(Receipt receipt : receiptsByIllegal) {
            receipts.add(receipt);
        }

        for (Receipt receipt : receiptsByFiveMinute) {
            receipts.add(receipt);
        }

        System.out.println("receipt size = " + receipts.size());
    }
}
