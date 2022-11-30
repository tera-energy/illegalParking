package com.teraenergy.illegalparking.scheduler;

import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import com.teraenergy.illegalparking.model.entity.comment.domain.Comment;
import com.teraenergy.illegalparking.model.entity.comment.service.CommentService;
import com.teraenergy.illegalparking.model.entity.illegalEvent.enums.IllegalType;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneService;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptStateType;
import com.teraenergy.illegalparking.model.entity.receipt.service.ReceiptService;
import com.teraenergy.illegalparking.model.entity.report.service.ReportService;
import com.teraenergy.illegalparking.model.entity.reportstatics.domain.ReportStatics;
import com.teraenergy.illegalparking.model.entity.reportstatics.service.ReportStaticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * Date : 2022-10-02
 * Author : zilet
 * Project : illegalParking
 * Description :
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class reportScheduler {

    /**
     * cron = "* * * * * *"
     *  초 / 분 / 시 / 일 / 월 / 요일 / 년도
     *
     * <!-- 5분 마다 실행 ex) 00:05, 00:10. 00:15.... -->
     * cron = "0 0/5 * * * *"
     *
     * <!-- 1시간 마다 실행 ex) 01:00, 02:00, 03:00.... -->
     * cron = "0 0 0/1 * * *"
     *
     * <!-- 매일 오후 18시마다 실행 ex) 18:00 -->
     * cron = "0 0 18 * * *"
     *
     * <!-- 2018년도만 매일 오후 18시마다 실행 ex) 18:00 -->
     * cron = "0 0 18 * * * 2018"
     *
     * <!-- 매일 오후 18시00분-18시55분 사이에 5분 간격으로 실행 ex) 18:00, 18:05.....18:55 -->
     * cron = "0 0/5 18 * * *"
     *
     * <!-- 매일 오후 9시00분-9시55분, 18시00분-18시55분 사이에 5분 간격으로 실행  -->
     * cron = "0 0/5 9,18 * * *"
     *
     * <!-- 매일 오후 9시00분-18시55분 사이에 5분 간격으로 실행  -->
     * cron = "0 0/5 9-18 * * *"
     *
     * <!-- 매달 1일 00시에 실행 -->
     * cron = "0 0 0 1 * *"
     *
     * <!-- 매년 3월내 월-금요일 10시 30분에만 실행 -->
     * cron = "0 30 10 ? 3 MON-FRI"
     *
     * <!-- 매월 마지막날 저녁 10시에 실행 -->
     * cron = "0 0 10 L * ?"
     * 출처: https://aljjabaegi.tistory.com/400 [알짜배기 프로그래머:티스토리]
     */

    private final ReportService reportService;

    private final ReceiptService receiptService;

    private final IllegalZoneService illegalZoneService;

    private final ReportStaticsService reportStaticsService;

    private final CommentService commentService;

    /**
     * 1분만다 한번씩 실행
     * OCCUR(신고발생) -> FORGET(신고누락)
     * 11분 전 부터 1시간 11분 전까지의 신고 발생 후 누락이 된 경우 모두 업데이트
     */
    @Scheduled(cron = "0 * * * * *")
    public void updateReceipt() {

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

        if ( receipts.size() > 0) {
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

        LocalDateTime endTaskTime = LocalDateTime.now();
        log.info("updateReceipt time .... {} : {}", startTaskTime, endTaskTime);
    }

    /**
     *  1시간 마다 한번식 실행
     *  1시간 단위 통계 자료 생성
     */
    @Scheduled(cron = "0 0 0/1 * * *")
    public void updateStatics() {

        LocalDateTime startTaskTime = LocalDateTime.now();        // 작업 시작 시간

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

        LocalDateTime endTaskTime = LocalDateTime.now();   // 작업 종료 시간

        log.info("updateStatics time .... {} : {}", startTaskTime, endTaskTime);
    }

}
